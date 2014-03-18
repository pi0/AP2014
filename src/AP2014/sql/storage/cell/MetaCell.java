package AP2014.sql.storage.cell;

public class MetaCell extends AbstractCell {

    public MetaCell(String paramName, Object defaultValue,CellType type) {
        super(paramName, defaultValue,type);
    }

    public DataCell createCell() {
        return new DataCell(paramName,value,type);
    }

    public MetaCell byValue(Object value){
        return new MetaCell(paramName,value,type);
    }

}
