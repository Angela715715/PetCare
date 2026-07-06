package dao;

import java.util.List;

import entity.Pet;

public interface PetDao {
	
	//create
	void add(Pet pet);
	
	//用會員id查寵物
	List<Pet> selectByMemberId(int memberid);

	void update(Pet pet);

	void delete(int id);

}
