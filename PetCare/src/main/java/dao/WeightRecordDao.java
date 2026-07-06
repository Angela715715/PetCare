package dao;

import java.util.List;

import entity.WeightRecord;

public interface WeightRecordDao {
	void add(WeightRecord record);
	List<WeightRecord> selectByPetId(int petId);
	void delete(int id);
}
