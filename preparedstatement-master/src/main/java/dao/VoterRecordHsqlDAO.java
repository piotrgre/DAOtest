package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseLogic;
import model.VoterRecord;

/**
 * Created by pwilkin on 11-May-20.
 */
public class VoterRecordHsqlDAO implements VoterRecordDAO {

    @Override
    public List<VoterRecord> fetchAllVoterRecords() {
        DatabaseLogic dl = new DatabaseLogic();
        List<VoterRecord> retVal = new ArrayList<>();
        dl.runWithConnection(c -> {
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM VOTER_RECORDS")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        VoterRecord record = getVoterRecordFromCursor(rs);
                        retVal.add(record);
                    }
                }
            }
        });
        return retVal;
    }

    @Override
    public List<VoterRecord> fetchAllVoterRecordsFromCity(String city) {
        DatabaseLogic dl = new DatabaseLogic();
        List<VoterRecord> retVal = new ArrayList<>();
        dl.runWithConnection(c -> {
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM VOTER_RECORDS WHERE CITY=?")) {
                ps.setString(1, city);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        VoterRecord record = getVoterRecordFromCursor(rs);
                        retVal.add(record);
                    }
                }
            }
        });
        return retVal;
    }

    @Override
    public VoterRecord fetchRecordByPesel(String pesel) {
        DatabaseLogic dl = new DatabaseLogic();
        VoterRecord[] retVal = new VoterRecord[1];
        dl.runWithConnection(c -> {
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM VOTER_RECORDS WHERE PESEL=?")) {
                ps.setString(1, pesel);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        retVal[0] = getVoterRecordFromCursor(rs);
                    }
                }
            }
        });
        return retVal[0];
    }

    private VoterRecord getVoterRecordFromCursor(ResultSet rs) throws SQLException {
        VoterRecord record = new VoterRecord();
        record.setId(rs.getInt("ID"));
        record.setAddress(rs.getString("ADDRESS"));
        record.setCity(rs.getString("CITY"));
        record.setFirstName(rs.getString("FIRST_NAME"));
        record.setLastName(rs.getString("LAST_NAME"));
        record.setHasVoted(rs.getInt("HAS_VOTED") == 1);
        record.setMunicipality(rs.getString("MUNICIPALITY"));
        record.setPesel(rs.getString("PESEL"));
        record.setProvince(rs.getString("PROVINCE"));
        return record;
    }

    @Override
    public void persistVoterRecord(VoterRecord record) {
        DatabaseLogic dl = new DatabaseLogic();
        if (record.getId() != null) {
            dl.runWithConnection(c -> {
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO VOTER_RECORDS (ADDRESS, CITY, FIRST_NAME, LAST_NAME, HAS_VOTED, MUNICIPALITY, PESEL, PROVINCE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, record.getAddress());
                    ps.setString(2, record.getCity());
                    ps.setString(3, record.getFirstName());
                    ps.setString(4, record.getLastName());
                    ps.setInt(5, record.isHasVoted() ? 1 : 0);
                    ps.setString(6, record.getMunicipality());
                    ps.setString(7, record.getPesel());
                    ps.setString(8, record.getProvince());
                    ps.execute();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        record.setId(rs.getInt(1));
                    }
                }
            });
        } else {
            dl.runWithConnection(c -> {
                try (PreparedStatement ps = c.prepareStatement("UPDATE VOTER_RECORDS SET ADDRESS=?, CITY=?, FIRST_NAME=?, LAST_NAME=?, HAS_VOTED=?, MUNICIPALITY=?, PESEL=?, PROVINCE=? WHERE ID=?")) {
                    ps.setString(1, record.getAddress());
                    ps.setString(2, record.getCity());
                    ps.setString(3, record.getFirstName());
                    ps.setString(4, record.getLastName());
                    ps.setInt(5, record.isHasVoted() ? 1 : 0);
                    ps.setString(6, record.getMunicipality());
                    ps.setString(7, record.getPesel());
                    ps.setString(8, record.getProvince());
                    ps.setInt(9, record.getId());
                    ps.execute();
                }
            });
        }
    }

    @Override
    public void deleteVoterRecord(VoterRecord record) {
        if (record.getId() != null) {
            DatabaseLogic dl = new DatabaseLogic();
            dl.runWithConnection(c -> {
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM VOTER_RECORDS WHERE ID=?")) {
                    ps.setInt(1, record.getId());
                    ps.execute();
                }
            });
            record.setId(null);
        }
    }
}
