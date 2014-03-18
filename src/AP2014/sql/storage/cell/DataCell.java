package AP2014.sql.storage.cell;

public class DataCell extends  AbstractCell{

    public DataCell(String paramName,Object value,int maxValue,CellType type) {
        super(paramName, value ,maxValue,type);

    }

    public boolean equals(MetaCell metaCell){
        return (this.type==metaCell.type);
    }

    public boolean equals(DataCell cell){
        return (this.type==cell.type &&
                this.value.equals(cell));
    }

    public int compareTo(DataCell cell) {
        if(type!=cell.type)
            return 0;
        switch (type){
            case CELL_TYPE_FLOAT:
                return ((Float)value).compareTo((Float)cell.value);
            case CELL_TYPE_INT:
                return ((Integer)value).compareTo((Integer)cell.value);
            default:
                return value.toString().compareTo(cell.value.toString());
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }



}
