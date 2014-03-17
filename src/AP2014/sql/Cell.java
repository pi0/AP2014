package AP2014.sql;

public class Cell {
    private Param param;
    private Value value;

    public Cell(Param param, Value value) {
        this.param = param;
        this.value = value;
    }

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
