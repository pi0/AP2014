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

    public boolean update(Pair<MetaCell,DataCell> p) {
        DataCell c=getCell(p.getKey());
        if(c!=null) {
            c.setValue(p.getValue().getValue().toString());
            return true;
        }
        return false;
    }

    public boolean update(Vector<Pair<MetaCell,DataCell>> l){
    	boolean state=true;
        for (Pair<MetaCell,DataCell> p:l)
            if(!update(p))
            	state=false;
        return state;
    }

    public String[] toStringArr() {
        String[] c=new String[cells.size()];
        for(int i=0;i<c.length;i++)
            c[i]=cells.get(i).toString();
        return c;
    }
    
    public String dump(Table parent) {
    	StringBuilder sb=new StringBuilder();
    	sb.append("insert into '"+parent.getName()+"'");
    	
    	if(cells.size()>0) {
	    	sb.append(" values(");
	    	for(DataCell cell:cells) {
	    		sb.append(cell.getValue().toString());
	    		sb.append(",");
	    	}
	    	sb.replace(sb.length()-1, sb.length(), ");");
    	}
    	
    	return sb.toString();
    }

}
