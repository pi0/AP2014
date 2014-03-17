package AP2014.sql;

public class Param {

    private String name;
    private ParamType type;

    public Param(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParamType getType() {
        return type;
    }





    public enum ParamType {
        PARAM_TYPE_STRING,PARAM_TYPE_INT,PARAM_TYPE_FLOAT
    }
}


