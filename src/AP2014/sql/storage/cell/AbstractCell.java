package AP2014.sql.storage.cell;



public abstract class AbstractCell{

    protected final CellType type;
    protected final String paramName;
    protected int maxValue;
    protected Object value;

    public AbstractCell(String paramName, Object value,int maxValue,CellType type) {
        this.paramName = paramName;
        this.value = (Comparable<Object>)value;
        this.maxValue=maxValue;
        this.type=type;
    }

    public String getName() {
        return paramName;
    }

    public CellType getType() {
        return type;
    }

    public static CellType getCellType(String t) {
        if(t.equals("string"))
            return CellType.CELL_TYPE_STRING;
        else if(t.equals("int"))
            return CellType.CELL_TYPE_INT;
        else if(t.equals("float"))
            return CellType.CELL_TYPE_FLOAT;
        else return null;
    }

    public static String getCellType(CellType type) {
        if(type==CellType.CELL_TYPE_FLOAT)
            return "float";
        else if(type==CellType.CELL_TYPE_INT)
            return "int";
        if(type==CellType.CELL_TYPE_STRING)
            return "string";
        else return "?";
    }

    public static CellType detectValueType(String value) {
        if(value.matches("\\d*\\.\\d*"))
            return CellType.CELL_TYPE_FLOAT;
        else if (value.matches("\\d+"))
            return CellType.CELL_TYPE_INT;
        else return CellType.CELL_TYPE_STRING;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
