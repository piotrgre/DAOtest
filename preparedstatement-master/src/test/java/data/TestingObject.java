package data;

/**
 * Created by pwilkin on 03-May-20.
 */
public class TestingObject {

    protected Integer id;
    protected String tcol;
    protected Double num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTcol() {
        return tcol;
    }

    public void setTcol(String tcol) {
        this.tcol = tcol;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "TestingObject{" +
                "id=" + id +
                ", tcol='" + tcol + '\'' +
                ", num=" + num +
                '}';
    }
}
