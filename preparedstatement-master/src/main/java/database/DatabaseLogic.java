package database;

import java.sql.*;

/**
 * Created by pwilkin on 15-Apr-20.
 */
public class DatabaseLogic {

    public static final String DB_LOCATION = "jdbc:hsqldb:mem:test";

    public static interface RunnableWithConnection {
        public void run(Connection c) throws SQLException;
    }

    public String performTests() {
        try (Connection c = DriverManager.getConnection(DB_LOCATION, "SA", "")) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO PERSONS (FIRST_NAME, LAST_NAME, AGE) VALUES (?, ?, ?)")) {
                ps.setString(1, "Jan");
                ps.setString(2, "Kowalski");
                ps.setInt(3, 28);
                ps.execute();

                ps.setString(1, "Anna");
                ps.setString(2, "Nowak");
                ps.setInt(3, 44);
                ps.execute();

                ps.setString(1, "Janina");
                ps.setString(2, "Lelempol");
                ps.setInt(3, 35);
                ps.execute();
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT MIN(LAST_NAME) FROM PERSONS WHERE AGE > ?")) {
                ps.setInt(1, 30);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void runWithConnection(RunnableWithConnection rc) {
        try (Connection c = DriverManager.getConnection(DB_LOCATION, "SA", "")) {
            rc.run(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
