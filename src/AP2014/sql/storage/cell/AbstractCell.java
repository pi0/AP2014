package AP2014.sql.storage.cell;



public abstract class AbstractCell{

    protected final CellType type;
    protected final String paramName;
    protected Object value;

    public AbstractCell(String paramName, Object value,CellType type) {
        this.paramName = paramName;
        this.value = (Comparable<Object>)value;
        this.type=type;
    }

    public String getName() {
        return paramName;
    }
}
