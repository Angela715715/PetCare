package service;

import java.util.List;

import entity.WeightRecord;

public interface WeightRecordService {
	void addRecord(WeightRecord record);
	List<WeightRecord> findByPetId(int petId);
	void deleteRecord(int id);
}
