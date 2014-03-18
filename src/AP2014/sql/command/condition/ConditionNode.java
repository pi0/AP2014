package AP2014.sql.command.condition;

import AP2014.sql.storage.Record;
import AP2014.sql.storage.Table;

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

    public Vector<Record> getMatchedRecords(Table table) {
        Vector<Record> m=new Vector<Record>();
        for(Record r:table.getRecords())
            if(accepts(r))
                m.add(r);
        return m;
    }
}

