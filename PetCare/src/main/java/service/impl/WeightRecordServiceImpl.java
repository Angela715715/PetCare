package service.impl;

import java.util.List;

import dao.WeightRecordDao;
import dao.impl.WeightRecordDaoImpl;
import entity.WeightRecord;
import service.WeightRecordService;

public class WeightRecordServiceImpl implements WeightRecordService {
	private WeightRecordDao dao = new WeightRecordDaoImpl();

	@Override
	public void addRecord(WeightRecord record) {
		dao.add(record);
	}

	@Override
	public List<WeightRecord> findByPetId(int petId) {
		return dao.selectByPetId(petId);
	}

	@Override
	public void deleteRecord(int id) {
		dao.delete(id);
	}
}
