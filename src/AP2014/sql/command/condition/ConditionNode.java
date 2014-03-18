package AP2014.sql.command.condition;

import AP2014.sql.storage.Record;

import java.util.Vector;

enum ConditionNodeType {
    CONDITION_NODE_TYPE_OPERATOR,
    CONDITION_NODE_TYPE_EXPRESSION
}

public abstract class ConditionNode {

    public final ConditionNodeType nodeType;

    public ConditionNode(ConditionNodeType nodeType) {
        this.nodeType=nodeType;
    }

    public abstract boolean accepts(Record record);

}

