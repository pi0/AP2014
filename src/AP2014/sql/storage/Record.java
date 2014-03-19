package AP2014.sql.storage;

import AP2014.sql.storage.cell.DataCell;
import AP2014.sql.storage.cell.MetaCell;
import javafx.util.Pair;

import java.util.Vector;

public class Record{

    private Vector<DataCell> cells;

    public Record(Vector<DataCell> cells) {
        this.cells=cells;
    }


    public DataCell getCell(MetaCell param) {
        for(DataCell cell:cells)
            if(cell.equals(param))
                return cell;
        return null;
    }

    public void update(Pair<MetaCell,DataCell> p) {
        DataCell c=getCell(p.getKey());
        if(c!=null)
            c.setValue(p.getValue().getValue().toString());
    }

    public void update(Vector<Pair<MetaCell,DataCell>> l){
        for (Pair<MetaCell,DataCell> p:l)
            update(p);
    }

    public String[] toStringArr() {
        String[] c=new String[cells.size()];
        for(int i=0;i<c.length;i++)
            c[i]=cells.get(i).toString();
        return c;
    }

}