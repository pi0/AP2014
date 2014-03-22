package AP2014.sql.storage;

import AP2014.sql.storage.cell.AbstractCell;
import AP2014.sql.storage.cell.DataCell;
import AP2014.sql.storage.cell.MetaCell;
import com.bethecoder.ascii_table.ASCIITable;

import java.util.Vector;

public class Table {

    private String name;
    private final Vector<MetaCell> params;
    private Vector<Record> records;

    public Table(String name, Vector<MetaCell> params) {
        this.name = name;
        this.params = params;
        this.records=new Vector<Record>();
    }

    public MetaCell getParam(String paramName) {
        for(MetaCell param:params)
            if(param.getName().equals(paramName))
                return param;
        return null;
    }

    public Vector<MetaCell> getParams() {
        return params;
    }

    public Record makeRecord() {
        Vector<DataCell> cells;
        cells=new Vector<DataCell>(params.size());
        for(MetaCell mcell:params)
            cells.add(mcell.createCell());
        return new Record(cells);
    }

    public Vector<Record> getRecords() {
        return records;
    }

    public void deleteRecord(Record r) {
        records.remove(r);
    }

    public void deleteRecords(Vector<Record> records) {
        this.records.removeAll(records);
    }

    public String getName() {
        return name;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    public void addRecords(Vector<Record> records) {
        this.records.addAll(records);
    }

    @Override
    public String toString() {

        String[] header=new String[params.size()];
        for(int i=0;i<header.length;i++)
            header[i]=params.get(i).toString();

        String[][] rows;

        if(records.size()>0) {
        rows=new String[records.size()][];
        for(int i=0;i<rows.length;i++)
            rows[i]=records.get(i).toStringArr();
        } else {
            rows=new String[1][];
            rows[0]=new String[params.size()];
            for(int i=0;i<params.size();i++)
                rows[0][i]="";
        }

        ASCIITable table=ASCIITable.getInstance();
        
        StringBuffer t=new StringBuffer();
        t.append(getName()+":\n");
        t.append(table.getTable(header, rows));
        return t.toString();
    }

	public String dump() {
		StringBuilder sb=new StringBuilder();
		
		sb.append("# Table : "+getName());
		sb.append("\n##\n");
		
		sb.append("create table '"+getName()+"' (");
		
		for(MetaCell m:getParams())
			sb.append(String.format("'%s' %s %d,", m.getName(),
					AbstractCell.getCellType(m.getType()),m.getMaxValue()));
		sb.replace(sb.length()-1, sb.length(), ");\n");
		
		
		for(Record record:records) {
			sb.append(record.dump(this));
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
