import static java.sql.DriverManager.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.VoterRecordHsqlDAO;
import model.VoterRecord;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DAOTest {
	
private static final String DBDESC = "jdbc:hsqldb:mem:test";

@BeforeAll
public static void prepareDatabase() {
    try (Connection c = getConnection(DBDESC, "SA", "")) {
        c.createStatement().execute("CREATE TABLE VOTER_RECORDS(ID INT PRIMARY KEY IDENTITY, PESEL VARCHAR(255), FIRST_NAME VARCHAR(255), LAST_NAME VARCHAR(255), PROVINCE VARCHAR(255), MUNICIPALITY VARCHAR(255), CITY VARCHAR(8000), ADDRESS VARCHAR(255), HAS_VOTED INT)");
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO VOTER_RECORDS (PESEL, FIRST_NAME, LAST_NAME, PROVINCE, MUNICIPALITY, CITY, ADDRESS, HAS_VOTED) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, "78022899698");
            ps.setString(2, "Jan");
            ps.setString(3, "Kolec");
            ps.setString(4, "Mazowieckie");
            ps.setString(5, "Warszawa");
            ps.setString(6, "Warszawa");
            ps.setString(7, "ul. Siema 1");
            ps.setString(8, "1");
            ps.execute();
            ps.setString(1, "62032183966");
            ps.setString(2, "Janek");
            ps.setString(3, "Kolczyk");
            ps.setString(4, "Ma³opolskie");
            ps.setString(5, "Kraków");
            ps.setString(6, "Kraków");
            ps.setString(7, "ul. Nara 1");
            ps.setString(8, "0");
            ps.execute();
          
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

@AfterAll
public static void destroyDatabase() {
    try (Connection c = getConnection(DBDESC, "SA", "")) {
        c.createStatement().execute("DROP TABLE VOTER_RECORDS");
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}

@Test
public void testConnection() {
    try (Connection c = getConnection(DBDESC, "SA", "")) {
        c.createStatement().executeQuery("SELECT * FROM VOTER_RECORDS");
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

@Test
public void testTwoEntries() {
    try (Connection c = getConnection(DBDESC, "SA", "")) {
        int cnt = 0;
        try (ResultSet rs = c.createStatement().executeQuery("SELECT * FROM VOTER_RECORDS")) {
            while (rs.next()) {
                cnt++;
            }
        }
        Assertions.assertEquals(cnt, 2);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

@Test
public void testFetchAllVoterRecords() {
	int maks = 0;
	VoterRecordHsqlDAO siema = new VoterRecordHsqlDAO();
    int jakis = siema.fetchAllVoterRecords().size()-1;
    try (Connection c = getConnection(DBDESC, "SA", "")) {
        try (ResultSet rs = c.createStatement().executeQuery("SELECT MAX(ID) FROM VOTER_RECORDS")) {
            rs.next();
            maks = rs.getInt(1);
        }  
        Assertions.assertEquals(maks, jakis);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

@Test
public void testFetchAllVoterRecordsFromCity() {
	int cnt = 0;
	VoterRecordHsqlDAO siema = new VoterRecordHsqlDAO();
    int jakis = siema.fetchAllVoterRecordsFromCity("Warszawa").size();
    try (Connection c = getConnection(DBDESC, "SA", "")) {
        try (ResultSet rs = c.createStatement().executeQuery("SELECT * FROM VOTER_RECORDS WHERE CITY = 'Warszawa'")) {
        	while (rs.next()) {
                cnt++;
            }
        }  
        Assertions.assertEquals(cnt, jakis);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

@Test
public void testFetchRecordByPesel() {
	VoterRecord record;
	VoterRecordHsqlDAO siema = new VoterRecordHsqlDAO();
    VoterRecord jakis = siema.fetchRecordByPesel("78022899698");
    try (Connection c = getConnection(DBDESC, "SA", "")) {
        try (ResultSet rs = c.createStatement().executeQuery("SELECT * FROM VOTER_RECORDS WHERE PESEL = '78022899698'")) {
        	rs.next();
        	record = new VoterRecord();
            record.setId(rs.getInt("ID"));
            record.setAddress(rs.getString("ADDRESS"));
            record.setCity(rs.getString("CITY"));
            record.setFirstName(rs.getString("FIRST_NAME"));
            record.setLastName(rs.getString("LAST_NAME"));
            record.setHasVoted(rs.getInt("HAS_VOTED") == 1);
            record.setMunicipality(rs.getString("MUNICIPALITY"));
            record.setPesel(rs.getString("PESEL"));
            record.setProvince(rs.getString("PROVINCE"));
        	}
        
        	Assertions.assertEquals(record.getId(), jakis.getId());
    
        	
    	} catch (SQLException e) {
            throw new RuntimeException(e);
        }
    
        
        
        }
@Test
public void testDeleteVoterRecord() {
	int przed = 0;
	int po = 0;
	VoterRecord record;
	VoterRecordHsqlDAO siema = new VoterRecordHsqlDAO();
    try (Connection c = getConnection(DBDESC, "SA", "")) {
    	
        try (ResultSet rs = c.createStatement().executeQuery("SELECT MAX(ID) FROM VOTER_RECORDS")) {
            rs.next();
            przed = rs.getInt(1)+1;
        }
        try (ResultSet rs = c.createStatement().executeQuery("SELECT * FROM VOTER_RECORDS WHERE CITY = 'Kraków'")) {
        	rs.next();
        	record = new VoterRecord();
            record.setId(rs.getInt("ID"));
            record.setAddress(rs.getString("ADDRESS"));
            record.setCity(rs.getString("CITY"));
            record.setFirstName(rs.getString("FIRST_NAME"));
            record.setLastName(rs.getString("LAST_NAME"));
            record.setHasVoted(rs.getInt("HAS_VOTED") == 1);
            record.setMunicipality(rs.getString("MUNICIPALITY"));
            record.setPesel(rs.getString("PESEL"));
            record.setProvince(rs.getString("PROVINCE"));
        	}
        siema.deleteVoterRecord(record);
        try (ResultSet rs = c.createStatement().executeQuery("SELECT MAX(ID) FROM VOTER_RECORDS")) {
            rs.next();
            po = rs.getInt(1)+1;
        }
        Assertions.assertEquals(przed, po + 1);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}




}
