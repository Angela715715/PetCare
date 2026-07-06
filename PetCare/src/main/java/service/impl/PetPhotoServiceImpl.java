package service.impl;

import java.util.List;

import dao.PetPhotoDao;
import dao.impl.PetPhotoDaoImpl;
import entity.PetPhoto;
import service.PetPhotoService;

public class PetPhotoServiceImpl implements PetPhotoService {
	private PetPhotoDao dao = new PetPhotoDaoImpl();

	@Override
	public void addRecord(PetPhoto record) {
		dao.add(record);
	}

	@Override
	public List<PetPhoto> findByPetId(int petId) {
		return dao.selectByPetId(petId);
	}

	@Override
	public void deleteRecord(int id) {
		dao.delete(id);
	}
}
