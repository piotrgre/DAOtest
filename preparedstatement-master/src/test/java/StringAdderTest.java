import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by pwilkin on 27-Apr-20.
 */
public class StringAdderTest {

    @Test
    public void testTwoPlusTwo() {
        int x = 2, y = 2;
        Assertions.assertEquals(x + y, 4);
    }

    @Test
    public void testTwoPlusTwoString() {
        String x = "2", y = "2";
        StringAdder sa = new StringAdder(x);
        sa.add(y);
        Assertions.assertEquals(sa.current(), 4);
    }

    @Test
    public void testAddingLettersFails() {
        String x = "2", y = "A";
        StringAdder sa = new StringAdder(x);
        Assertions.assertThrows(NumberFormatException.class, () -> {
            sa.add(y);
        });
    }


}
