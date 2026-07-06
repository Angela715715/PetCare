package service;

import java.util.List;

import entity.VaccineRecord;

public interface VaccineRecordService {
	void addRecord(VaccineRecord record);
	List<VaccineRecord> findByPetId(int petId);
	void deleteRecord(int id);
}
