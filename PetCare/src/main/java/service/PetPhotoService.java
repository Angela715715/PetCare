package service;

import java.util.List;

import entity.PetPhoto;

public interface PetPhotoService {
	void addRecord(PetPhoto record);
	List<PetPhoto> findByPetId(int petId);
	void deleteRecord(int id);
}
