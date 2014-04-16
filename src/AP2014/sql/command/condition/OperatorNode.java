package AP2014.sql.command.condition;

import AP2014.sql.storage.Record;

import java.util.ArrayList;
import java.util.Vector;



public class OperatorNode extends ConditionNode{

    private ArrayList<ConditionNode> childs;
    private OperatorType operatorType;

    public OperatorNode(OperatorType operatorType) {
        super(ConditionNodeType.CONDITION_NODE_TYPE_OPERATOR);
        this.operatorType=operatorType;
        this.childs=new ArrayList<ConditionNode>();
    }

    public OperatorNode() {
        this(OperatorType.OPERATOR_TYPE_AND);
    }

    public void addChild(ConditionNode cn) {
        childs.add(cn);
    }

    public boolean accepts(Record record) {

        for(ConditionNode node:childs) {
            boolean acc=node.accepts(record);
            if(acc){
                if(operatorType==OperatorType.OPERATOR_TYPE_OR)
                    return true;
            }else//!acc
                    if(operatorType==OperatorType.OPERATOR_TYPE_AND)
                        return false;
        }

        if(operatorType==OperatorType.OPERATOR_TYPE_AND)
            return true;//AND , but no false
        else
            return false;//OR , but all false
    }

    public String toString() {
        StringBuilder b=new StringBuilder();
        String o=operatorType==OperatorType.OPERATOR_TYPE_AND?"AND":"OR";
        b.append("[ ");

        for(int i=0;i<childs.size()-1;i++) {
//            if (childs.get(i).nodeType == ConditionNodeType.CONDITION_NODE_TYPE_EXPRESSION)
//                b.append("(" + childs.get(i) + ")");
//            else
                b.append(childs.get(i));
            b.append(" "+o+" ");
        }
        if(childs.size()>0)
            b.append(childs.get(childs.size()-1));
        b.append("]");
        return b.toString();
    }

}

