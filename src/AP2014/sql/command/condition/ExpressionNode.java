package AP2014.sql.command.condition;

import AP2014.sql.storage.Record;
import AP2014.sql.storage.cell.DataCell;
import AP2014.sql.storage.cell.MetaCell;


enum ExpressionType {
    EXPRESSION_TYPE_EQUALS,
    EXPRESSION_TYPE_LESS,
    EXPRESSION_TYPE_MORE,
}

public class ExpressionNode extends ConditionNode{

    private final ExpressionType expressionType;
    private final MetaCell compareBy;
    private final DataCell compareByVal;

    public ExpressionNode(MetaCell compareBy,ExpressionType expressionType) {
        super(ConditionNodeType.CONDITION_NODE_TYPE_EXPRESSION);
        this.expressionType=expressionType;
        this.compareBy=compareBy;
        compareByVal=compareBy.createCell();
    }

    public ExpressionNode(MetaCell compareBy,String opr) {
        this(compareBy,getTypeByString(opr));
    }

    private static ExpressionType getTypeByString(String opr) {
        ExpressionType type=null;
        if(opr.equals("="))
            type=ExpressionType.EXPRESSION_TYPE_EQUALS;
        else if (opr.equals(">"))
            type=ExpressionType.EXPRESSION_TYPE_MORE;
        else if (opr.equals("<"))
            type=ExpressionType.EXPRESSION_TYPE_LESS;
        return type;
    }

    public boolean accepts(Record record) {
        int c=record.getCell(compareBy).compareTo(compareByVal);
        switch (expressionType) {
            case EXPRESSION_TYPE_EQUALS:
                return c==0;
            case EXPRESSION_TYPE_LESS:
                return c<0;
            case EXPRESSION_TYPE_MORE:
                return c>0;
            default:
                return true;
        }
    }
}

