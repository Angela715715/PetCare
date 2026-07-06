package dao;

import java.util.List;

import entity.VaccineRecord;

public interface VaccineRecordDao {
	void add(VaccineRecord record);
	List<VaccineRecord> selectByPetId(int petId);
	void delete(int id);
}
