package service.impl;

import java.util.List;

import dao.MedicalFileDao;
import dao.impl.MedicalFileDaoImpl;
import entity.MedicalFile;
import service.MedicalFileService;

public class MedicalFileServiceImpl implements MedicalFileService {
	private MedicalFileDao dao = new MedicalFileDaoImpl();

	@Override
	public void addRecord(MedicalFile record) {
		dao.add(record);
	}

	@Override
	public List<MedicalFile> findByPetId(int petId) {
		return dao.selectByPetId(petId);
	}

	@Override
	public void deleteRecord(int id) {
		dao.delete(id);
	}
}
