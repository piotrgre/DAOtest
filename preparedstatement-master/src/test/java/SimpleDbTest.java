import java.sql.*;

import com.sun.xml.internal.ws.developer.UsesJAXBContext;

import org.junit.jupiter.api.*;

import static java.sql.DriverManager.getConnection;

/**
 * Created by pwilkin on 27-Apr-20.
 */
public class SimpleDbTest {

    private static final String DBDESC = "jdbc:hsqldb:mem:test";

    @BeforeAll
    public static void prepareDatabase() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            c.createStatement().execute("CREATE TABLE TESTING (ID INT PRIMARY KEY IDENTITY, TCOL VARCHAR(255), NUM DECIMAL(8, 2))");
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO TESTING (TCOL, NUM) VALUES (?, ?)")) {
                ps.setString(1, "val1");
                ps.setDouble(2, 4.2);
                ps.execute();
                ps.setString(1, "val2");
                ps.setDouble(2, 5.0);
                ps.execute();
                ps.setString(1, "val3");
                ps.setDouble(2, -4.3);
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void destroyDatabase() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            c.createStatement().execute("DROP TABLE TESTING");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testConnection() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            c.createStatement().executeQuery("SELECT * FROM TESTING");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testThreeEntries() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            int cnt = 0;
            try (ResultSet rs = c.createStatement().executeQuery("SELECT * FROM TESTING")) {
                while (rs.next()) {
                    cnt++;
                }
            }
            Assertions.assertEquals(cnt, 3);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // sprawdzic, czy uda sie dodac jeden nowy wpis do tabeli
    // sprawdzic, czy uda sie usunac jeden wpis z tabeli
    // sprawdzic, czy po dodaniu jednego wpisu i usunieciu jednego wpisu nadal w tabeli sa trzy wpisy
    // sprawdzic, czy po dodaniu wpisu z wartoscia 10.0 maksymalna wyciagnieta wartosc (podpowiedz: SELECT MAX(NUM) ...) wynosi 10.0)

    @Test
    public void testTransactionRollback() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            c.setAutoCommit(false);
            c.createStatement().execute("DELETE FROM TESTING WHERE ID=1");
            c.createStatement().execute("DELETE FROM TESTING WHERE ID=2");
            try (ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) AS CNT FROM TESTING")) {
                rs.next();
                Assertions.assertEquals(rs.getInt("CNT"), 1);
            }
            c.rollback();
            try (ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) AS CNT FROM TESTING")) {
                rs.next();
                Assertions.assertEquals(rs.getInt("CNT"), 3);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTransactionCommit() {
        try (Connection c = getConnection(DBDESC, "SA", "")) {
            c.setAutoCommit(false);
            c.createStatement().execute("DELETE FROM TESTING WHERE ID=1");
            c.createStatement().execute("DELETE FROM TESTING WHERE ID=2");
            try (ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) AS CNT FROM TESTING")) {
                rs.next();
                Assertions.assertEquals(rs.getInt("CNT"), 1);
            }
            c.commit();
            try (ResultSet rs = c.createStatement().executeQuery("SELECT COUNT(*) AS CNT FROM TESTING")) {
                rs.next();
                Assertions.assertEquals(rs.getInt("CNT"), 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ACID

}
