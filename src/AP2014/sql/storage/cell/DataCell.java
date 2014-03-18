package AP2014.sql.storage.cell;

public class DataCell extends  AbstractCell{

    public DataCell(String paramName,Object value,int maxValue,CellType type) {
        super(paramName, value ,maxValue,type);

    }

    public boolean equals(MetaCell metaCell){
        return (this.type==metaCell.type &&
                this.getName().equals(metaCell.getName()));
    }


    public void setValue(String value) {
        try{
        switch (type) {
                case CELL_TYPE_FLOAT:
                    this.value=Float.parseFloat(value);break;
                case CELL_TYPE_INT:
                    this.value=Integer.parseInt(value);break;
                case CELL_TYPE_STRING:
                    this.value=value;break;
            }
        } catch (Exception e) {
            System.err.print("fatal error ! unable to set value ...");
        }
    }

    public int compareTo(DataCell cell) {
        if(type!=cell.type)
            return 0;
        try{
        switch (type){
            case CELL_TYPE_FLOAT:
                return ((Float)value).compareTo((Float)cell.value);
            case CELL_TYPE_INT:
                return ((Integer)value).compareTo((Integer)cell.value);
            default:
                return value.toString().compareTo(cell.value.toString());
        }} catch (Exception e){
            return 0;
        }
    }

    @Override
    public String toString() {
        if(value!=null)
            return value.toString();
        else return "NULL";
    }



}
