package service.impl;

import java.util.List;

import dao.VaccineRecordDao;
import dao.impl.VaccineRecordDaoImpl;
import entity.VaccineRecord;
import service.VaccineRecordService;

public class VaccineRecordServiceImpl implements VaccineRecordService {
	private VaccineRecordDao dao = new VaccineRecordDaoImpl();

	@Override
	public void addRecord(VaccineRecord record) {
		dao.add(record);
	}

	@Override
	public List<VaccineRecord> findByPetId(int petId) {
		return dao.selectByPetId(petId);
	}

	@Override
	public void deleteRecord(int id) {
		dao.delete(id);
	}
}
