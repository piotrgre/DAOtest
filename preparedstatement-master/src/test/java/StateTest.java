import java.util.HashSet;
import java.util.Set;

import data.TestingObject;
import org.junit.jupiter.api.Test;

/**
 * Created by pwilkin on 03-May-20.
 */
public class StateTest {

    @Test
    public void testCreateObjects() {
        Set<TestingObject> objs = new HashSet<>();
        TestingObject theObj = new TestingObject();
        theObj.setId(1);
        theObj.setNum(124.2);
        theObj.setTcol("blabla");
        objs.add(theObj);

        theObj = new TestingObject();
        theObj.setId(2);
        theObj.setNum(-241.3);
        theObj.setTcol("hahaha");
        objs.add(theObj);
    }

}
