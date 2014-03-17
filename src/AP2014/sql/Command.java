package AP2014.sql;

import javafx.util.Pair;

import java.util.Vector;

public class Command {

    private Resource resource;
    private Database db;
    TokenList list;
    private String command;

    public static void query(String commands,Database db, Resource resource) {
        for (String command : commands.split("[\n;]+")) {
            Command c=new Command(command,db,resource);
            if(c.isValid())
                c.exec();
            else
                resource.logError("Invalid command : "+command);
        }
    }

    public Command(String command,Database db, Resource resource) {
        this.db = db;
        this.resource = resource;
        this.list = new TokenList(command, resource);
        this.command=command;
    }

    public boolean isValid() {
        return list.isValid();
    }

    public void exec() {
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
            select();
        } else
            resource.logError("unknown command : "+command);
    }

    private void createTable() {
        Table table = getTable(false);
        if (table != null)
            return;
        String tableName=list.getCurrentToken().getText();
        list.next();
        if(!list.checkSequenceAndLog("("))
            return;
        while(!list.isEndOfList()) {
            if(list.checkSequence("vv")) {
                String name=list.getCurrentToken().getText();
                list.next();
                String type=list.getCurrentToken().getText();
                list.next();
                int maxVal=-1;
                if(list.checkSequence("v")) {
                    try{
                        maxVal=Integer.parseInt(list.getCurrentToken().getText());
                    } catch (NumberFormatException e) {
                        resource.logError("invalid maximum value.assuming unlimited");
                    }
                    list.next();
                }
                if(list.checkSequence(")")) {
                    list.next();
                    break;
                }
                else if(list.checkSequence(",")) {
                    list.next();
                    //continue;
                }
                else {
                    resource.logError("invalid table parameters");
                    return;
                }
                //TODO
            }
        }
        //TODO
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
                resource.logError("invalid parameters syntax");
                return;
            }
        }
        if (list.checkSequence("k","values")) {
            list.next();
            values=getParams();
            if(values==null) {
                resource.logError("invalid values syntax");
                return;
            }
        }
        //TODO
    }

    private void updateFrom() {
        Table table = getTable(true);
        if (table == null)
            return;
        list.next();
        if(!list.checkSequenceAndLog("k", "set"))
            return;
        list.next();

        Vector<Pair<String,String>> values=new Vector<Pair<String,String>>();

        while(!list.isEndOfList()) {
            if(!list.checkSequenceAndLog("v=v"))
                return;
            String a=list.getCurrentToken().getText();
            list.next();
            String b=list.getCurrentToken().getText();
            list.next();
            values.add(new Pair(a,b));
            if(list.checkSequence("where"))
                break;
        }

        //TODO : GET CONDITION
        //TODO

    }

    private void deleteFrom() {
        Table table = getTable(true);
        if (table == null)
            return;
        list.next();

        //TODO : GET CONDITION
        //TODO
    }

    private void select() {
        if(list.checkSequence("*")) {
          //TODO : use all params
            list.next();
        } else {
            while (!list.isEndOfList()) {
                if(!list.checkSequence("v")) {
                    resource.logError("invalid select params");
                    return;
                } else {
                    String param=list.getCurrentToken().getText();
                    //TODO
                    list.next();
                }
                if(list.checkSequence(",")) {
                    list.next();
                    //continue;
                } else
                    break;
            }
        }

        //BIG TODO : get select source !
        //TODO : condition
        //TODO

    }

    private Table getTable(boolean shouldExist) {
        if (list.checkSequenceAndLog("v")) {
            Table table=db.getTable(list.getCurrentToken().getText());
            if(shouldExist == (table!=null))
                return table;
            else
                resource.logError("table '"+list.getCurrentToken().getText()+"' "+
                        (shouldExist?"not found":"already exists"));
        }
        return null;
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
            else
                return null;
            params.add(list.getCurrentToken().getText());
            list.next(2);
            if(terminate)
                return params;
        }
        return null;
    }
}

