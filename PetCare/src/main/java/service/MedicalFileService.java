package service;

import java.util.List;

import entity.MedicalFile;

public interface MedicalFileService {
	void addRecord(MedicalFile record);
	List<MedicalFile> findByPetId(int petId);
	void deleteRecord(int id);
}
