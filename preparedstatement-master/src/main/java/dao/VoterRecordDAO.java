package dao;

import java.util.List;

import model.VoterRecord;

/**
 * Created by pwilkin on 10-May-20.
 */
public interface VoterRecordDAO {

    public List<VoterRecord> fetchAllVoterRecords();
    public VoterRecord fetchRecordByPesel(String pesel);
    public List<VoterRecord> fetchAllVoterRecordsFromCity(String city);
    public void persistVoterRecord(VoterRecord record);
    public void deleteVoterRecord(VoterRecord record);

}
