package AP2014.sql.storage.cell;

public class MetaCell extends AbstractCell {

    public MetaCell(String paramName, Object defaultValue,int maxValue,CellType type) {
        super(paramName, defaultValue,maxValue,type);
    }

    public DataCell createCell() {
        return new DataCell(paramName,value,maxValue,type);
    }

    @Override
    public String toString() {
        return paramName.toString()+":"+getCellType(type);
    }
}
