package service.impl;

import java.util.List;

import dao.PetDao;
import dao.impl.PetDaoImpl;
import entity.Pet;
import service.PetService;

public class PetServiceImpl implements PetService{
	
	PetDao pdi=new PetDaoImpl();
	
	@Override
	public void addPet(Pet pet) {
		pdi.add(pet);
			
	}

	@Override
	public List<Pet> findPetsByMemberId(int memberid) {
		
		return pdi.selectByMemberId(memberid);
	}

	@Override
	public void updatePet(Pet pet) {
		pdi.update(pet);
	}

	@Override
	public void deletePet(int id) {
		pdi.delete(id);
	}

}
