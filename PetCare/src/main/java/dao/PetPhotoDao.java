package dao;

import java.util.List;

import entity.PetPhoto;

public interface PetPhotoDao {
	void add(PetPhoto record);
	List<PetPhoto> selectByPetId(int petId);
	void delete(int id);
}
