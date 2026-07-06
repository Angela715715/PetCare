package service;

import java.util.List;

import entity.Pet;

public interface PetService {
	//create
	void addPet(Pet pet);
	
	List<Pet> findPetsByMemberId(int memberid);

	void updatePet(Pet pet);

	void deletePet(int id);
	
}
