package AP2014.sql.command.condition;

import AP2014.sql.storage.Record;

import java.util.Vector;



public class OperatorNode extends ConditionNode{

    private Vector<ConditionNode> childs;
    private OperatorType operatorType;

    public OperatorNode(OperatorType operatorType) {
        super(ConditionNodeType.CONDITION_NODE_TYPE_OPERATOR);
        this.operatorType=operatorType;
        this.childs=new Vector<ConditionNode>();
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
        return true;//no child?
    }

}

