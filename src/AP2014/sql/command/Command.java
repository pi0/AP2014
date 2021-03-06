package AP2014.sql.command;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import AP2014.sql.SQL;
import AP2014.sql.Resource;
import AP2014.sql.storage.*;
import AP2014.sql.storage.cell.*;
import AP2014.sql.command.condition.*;
import AP2014.sql.command.token.TokenList;

public class Command {

    private static final String querySplitter = "[\r\n;]+";
    private Resource resource;
    private Database db;
    private Table currentTable;
    private TokenList list;

    public static void query(String commands,Database db, Resource resource) {
        for (String command : commands.split(querySplitter)) {
            Command c=new Command(command,db,resource);
            if(c.isValid())
                c.exec();
            else
                resource.logError("Query error : "+command);
        }
    }

    public Command(String command,Database db, Resource resource) {
        this.db = db;
        this.resource = resource;
        this.list = new TokenList(command, resource);
    }

    public Command(TokenList list,Command c) {
        this.db = c.db;
        this.resource = c.resource;
        this.list=list;
    }

    public boolean isValid() {
        return list.isValid();
    }

    public void exec() {
        if(!isValid()) {
            resource.logError("Invalid query");
            return;
        }

        if (list.checkSequence("kk", "create", "table")) {
            list.next(2);
            createTable();
        } else if (list.checkSequence("kk", "insert", "into")) {
            list.next(2);
            insertInto();
        } else if (list.checkSequence("kk", "update", "from")) {
            list.next(2);
            updateFrom();
        } else if (list.checkSequence("kk", "delete", "from")) {
            list.next(2);
            deleteFrom();
        } else if (list.checkSequence("k", "select")) {
            list.next();
            select();
        } else if (list.checkSequence("k", "drop")) {
                list.next();
                drop();
        } else if (list.checkSequence("#") || list.isEndOfList())
            ;//quite!!
        else
            resource.logError("Unknown query : "+list.toString());
    }

    private void createTable() {
        //Get table name
        Table table = getTable(false);
        if (table != null)
            return;//Table already exists
        String tableName=list.getCurrentToken().getText();
        if(!SQL.isValidName(tableName)){
            resource.logError("Invalid table name "+ __(tableName));
            return;
        }
        list.next();

        //Get table params
        Vector<MetaCell> params=new Vector<MetaCell>();
        if(!list.checkSequenceAndLog("(")) {
            resource.logError("Table needs parameters");
            return;
        }
        list.next();

        while(!list.isEndOfList()) {

            if(!(list.checkSequence("vk","int")||
                    list.checkSequence("vk","float")||
                            list.checkSequence("vk","string"))) {
                if(list.checkSequence(")")) {
                    list.next();
                    break;
                } else {
                    resource.logError("Syntax error on table parameters");
                    return;
                }
            }

            //Get param name
            String paramName=list.getCurrentToken().getText();
            if(!SQL.isValidName(paramName)) {
                resource.logError("Invalid parameter name "+ __(paramName));
                return;
            }
            list.next();

            //Get param type
            String typeS=list.getCurrentToken().getText();
            CellType type= AbstractCell.getCellType(typeS);
            if(type==null){
                resource.logError("Invalid type "+ __(typeS));
                return;
            }
            list.next();

            //Optionally get param max value
            int maxVal=-1;
            if(list.checkSequence("v")) {
                try{
                    maxVal=Integer.parseInt(list.getCurrentToken().getText());
                } catch (NumberFormatException e) {
                    resource.logError("Invalid maximum value for parameter");
                    return;
                }
                list.next();
            }

            if(list.checkSequence(",") || list.checkSequence(")")) {
                list.next();
            }
            else {
                resource.logError("Table parameters syntax error");
                return;
            }

            //Add to table params
            params.add(new MetaCell(paramName,null,maxVal,type));

        }

        //Finally create table!
        table=new Table(tableName,params);
        db.addTable(table);

        resource.logInfo("Table '"+tableName+"'"+" successfully created!");
    }

    private void insertInto() {
        Table table = getTable(true);
        if (table == null)
            return;
        list.next();

        Vector<String> params=null;
        Vector<String> values=null;

        if(list.checkSequence("k","parameter")) {
            list.next();
            params=getParams();
            if(params==null) {
                resource.logError("Invalid parameters");
                return;
            }
        }
        if (list.checkSequence("k","values")) {
            list.next();
            values=getParams();
            if(values==null) {
                resource.logError("Invalid values");
                System.out.println(list);
                return;
            }
        }

        //validate and get params
        Vector<MetaCell> mParams;
        if(params!=null) {
            mParams=new Vector<MetaCell>(params.size());
            for(String param:params) {
                MetaCell m=table.getParam(param);
                if(m==null){
                    resource.logError("Invalid parameter : "+ __(param));
                    return;
                }
                mParams.add(m);
            }
        } else {
            //Set all params
            mParams=table.getParams();
        }

        //validate values - but be nice :D
        if(values==null)
            values=new Vector<String>();
        if(mParams.size()!=values.size()) {
            resource.logWarning("Invalid number of values , filling by null");
        }

        //Create a record
        Record r=table.makeRecord();
        for(int i=0;i<mParams.size() && i<values.size();i++) {
            DataCell c=r.getCell(mParams.get(i));
            String val=values.get(i);
            if(AbstractCell.detectValueType(val)!=c.getType()) {
                resource.logError("Incompatible type for parameter "+(mParams.get(i).toString()));
                return;
            }
            c.setValue(val);
        }

        //Add it to table
        table.addRecord(r);

        resource.logInfo("New record added to table "+ __(table.getName())+"\n"+r.toString());
    }

    private void updateFrom() {
        Table table = getTableExtended();
        if (table == null) return;

        if(!list.checkSequenceAndLog("k", "set")) return;
        list.next();

        Vector<Pair<MetaCell,DataCell>> updates=
                new Vector<Pair<MetaCell,DataCell>>();

        while(!list.isEndOfList()) {
            if(!list.checkSequenceAndLog("v=v"))
                return;

            //Read param
            String paramS=list.getCurrentToken().getText();
            MetaCell param=table.getParam(paramS);
            if(param==null) {
                resource.logError("Invalid parameter : "+ __(paramS));
                return;
            }
            list.next(2);

            //Read value
            DataCell value=param.createCell();
            String valueS=list.getCurrentToken().getText();
            if(AbstractCell.detectValueType(valueS)!=value.getType()) {
                resource.logError("Incompatible value for "+param.toString());
                return;
            }
            value.setValue(valueS);
            list.next();

            updates.add(new Pair<MetaCell, DataCell>(param, value));

            if(list.checkSequence("k","where"))
                break;
        }

        Vector<Record> records=getWhere(table);
        if(records==null)return;
        for(Record r:records) {
            StringBuilder l=new StringBuilder();
            l.append("Record updated :\n");
            l.append(r.toString()+"\nto\n");
            r.update(updates);
            l.append(r.toString());

            resource.logInfo(l.toString());
        }

        resource.logInfo("Records updated successfully!");
    }

    private void select() {

        //Get Params
        Vector<String> paramsS=null;
        boolean selectAllParams=false;

        if(list.checkSequence("*")) {
            selectAllParams=true;
            list.next();
        } else {
            paramsS=new Vector<String>();
            while (!list.isEndOfList()) {
                if(!list.checkSequence("v")) {
                    resource.logError("Invalid select parameters");
                    return;
                } else {
                    paramsS.add(list.getCurrentToken().getText());
                    list.next();
                }
                if(list.checkSequence(",")) {
                    list.next();
                    //continue;
                } else
                    break;
            }
        }

        //Get source
        if(!list.checkSequenceAndLog("k", "from"))
            return;
        list.next();
        Table table = getTableExtended();
        if (table == null) return;

        //Get and check wanted params
        Vector<MetaCell> params;
        if(selectAllParams)
            params=table.getParams();
        else {
            params=new Vector<MetaCell>();
            for(String paramS:paramsS) {
                MetaCell m=table.getParam(paramS);
                if(m==null) {//Be nice !
                    resource.logWarning("Invalid parameter : "+ __(paramS)+" and won't be selected");
                    continue;
                }
                params.add(m);
            }
        }

        //get condition accepted records
        Vector<Record> records=getWhere(table);
        if(records==null)
            return;

        //Create a new table
        Table t=new Table(table.getName()+"_subTable",params);
        t.addRecords(records);

        resource.logInfo(t.toString());

        //Add this table to resource
        resource.getTables().add(t);
    }

    private void deleteFrom() {
        Table table = getTable(true);
        if (table == null) return;
        list.next();

        Vector<Record> records=getWhere(table);
        if(records==null)return;
        table.deleteRecords(records);

        resource.logInfo("Record(s) successfully deleted!");
    }
    
    private void drop() {
        Table table = getTable(true);
        if (table == null) return;
        list.next();

        db.drop(table);
        resource.logInfo("Table "+__(table.getName())+" Successfully droped!");
    }
      
    private Vector<Record> getWhere(Table table) {
        if(!list.checkSequenceAndLog("k", "where")) {
            //Accept everything !
            return table.getRecords();
        }
        list.next();
        ConditionNode c=getCondition(table);
        if(c!=null)
            return c.getMatchedRecords(table);
        else return null;
    }

    private ConditionNode getCondition(Table table) {
        currentTable=table;
        ConditionNode c=getCondition(list);
        if(c==null)
            resource.logError("Invalid condition");
        resource.logInfo("Condition parsed : "+c.toString());
        return c;
    }

    private ConditionNode getCondition(TokenList l) {

        OperatorNode root=new OperatorNode
                (OperatorType.OPERATOR_TYPE_OR);

        ConditionNode n = readCondition(l);

        boolean andSeen=false;
        OperatorType ot = null;
        if (l.checkSequence("k", "or"))
            ot = OperatorType.OPERATOR_TYPE_OR;
        else if (l.checkSequence("k", "and"))
            ot = OperatorType.OPERATOR_TYPE_AND;
        if (ot == null)
            return n;

        l.next();

        ArrayList<ConditionNode> andList=new ArrayList<ConditionNode>();

        if(ot==OperatorType.OPERATOR_TYPE_AND) {
            andSeen = true;
            andList.add(n);
        } else {
            andSeen = false;
            root.addChild(n);
        }

        while (!l.isEndOfList()) {

            n= readCondition(l);

            andSeen=l.checkSequence("k", "and");
            l.next();

            if(!andSeen || l.isEndOfList()) {
                if(andList.size()>0) {
                    andList.add(n);
                    OperatorNode and = new OperatorNode
                            (OperatorType.OPERATOR_TYPE_AND);
                    for (ConditionNode aa : andList)
                        and.addChild(aa);
                    root.addChild(and);
                    andList.clear();
                } else
                    root.addChild(n);
            } else
                andList.add(n);

        }



        return root;
    }

    private ConditionNode readCondition(TokenList l) {
        if(l.checkSequence("(")) {
            TokenList A=l.getSubsequence();
            if(A!=null){
                ConditionNode inner=getCondition(A);
                return inner;
            }
            else return null;
        } else if(l.checkSequence("v")) {

            //Read expression syntax [value][=<>][value]
            String a=l.getCurrentToken().getText();
            l.next();
            String opr=l.getCurrentToken().getText();
            if(!opr.matches("[=<>]")) {
                resource.logError("Operator expected");
                return null;
            }
            l.next();
            if(!l.checkSequence("v")) {
                resource.logError("Expression syntax is : [value][operator][value]");
                return null;
            }
            String b=l.getCurrentToken().getText();
            l.next();

            //Get from table
            MetaCell param=currentTable.getParam(a);
            if(param==null) {
                resource.logError("Parameter "+ __(a)+
                        " doesn't exists in table "+ __(currentTable.getName()));
                return null;
            }
            DataCell paramVal=param.createCell();

            CellType type=AbstractCell.detectValueType(b);
            if(type!=param.getType())
                resource.logWarning("Incompatible data types for comparing "+
                        AbstractCell.getCellType(type)+" and "+
                        AbstractCell.getCellType(param.getType())
                        +" for param "+ param.getName() +" using java Object.compare");

            paramVal.setValue(b);

            ExpressionNode node=new ExpressionNode(param,paramVal,opr);
            return node;
        } else
            return null;
    }

    private Table getTable(boolean shouldExist) {
        if (list.checkSequence("v")) {
            Table table=db.getTable
                    (list.getCurrentToken().getText());
            if(shouldExist == (table!=null))
                return table;
            else
                resource.logError("Table "+ __(list.getCurrentToken().getText())+
                        (shouldExist?"not found":"already exists"));
        }
        return null;
    }

    private Table getTableExtended() {
        Table table=getTable(true);

        if(table!=null) {
            list.next();
            return table;
        }

        if(!list.checkSequence("(k","select"))
            return null;

        TokenList subCommand=list.getSubsequence();
        if(subCommand==null || !subCommand.isValid()){
            resource.logError("Invalid subQuery");
            return null;
        }
        Command c=new Command(subCommand,this);
        c.exec();

        if(c.resource.getTables().size()<=0) {
            resource.logError("SubQuery returned no tables");
            return null;
        }

        return c.resource.getTables().elementAt(0);
    }

    private Vector<String> getParams() {
        if(!list.checkSequenceAndLog("("))
            return null;
        list.next();
        Vector<String> params=new Vector<String>();
        while(!list.isEndOfList()) {
            boolean terminate;
            if(list.checkSequence("v,"))
                terminate=false;
            else if(list.checkSequence("v)"))
                terminate=true;
            else if(list.checkSequence(")")){
                list.next();
                return params;
            }
            else
                return null;
            params.add(list.getCurrentToken().getText());
            list.next(2);
            if(terminate)
                return params;
        }
        return params;
    }

    private static String __(String txt){
        return "'"+txt+"'";
    }

}

