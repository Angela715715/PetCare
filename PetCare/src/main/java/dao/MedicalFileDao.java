package dao;

import java.util.List;

import entity.MedicalFile;

public interface MedicalFileDao {
	void add(MedicalFile record);
	List<MedicalFile> selectByPetId(int petId);
	void delete(int id);
}
