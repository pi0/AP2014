package AP2014.sql.storage;

import AP2014.sql.storage.cell.MetaCell;

import java.util.Vector;

public class Table {

    private String name;
    private Vector<MetaCell> params;
    private Vector<Record> records;


    public MetaCell getParam(String paramName) {
        for(MetaCell param:params)
            if(param.getName().equals(paramName))
                return param;
        return null;
    }

    public String getName() {
        return name;
    }
}
