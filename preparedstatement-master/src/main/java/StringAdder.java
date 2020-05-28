/**
 * Created by pwilkin on 27-Apr-20.
 */
public class StringAdder {

    protected int parsed;

    public StringAdder(String start) {
        parsed = Integer.parseInt(start);
    }

    public void add(String val) {
        parsed += Integer.parseInt(val);
    }

    public int current() {
        return parsed;
    }

}
