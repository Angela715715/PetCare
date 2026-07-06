package entity;

import java.sql.Date;

public class Pet {
	private int id;
	private int memberid;
	private String name;
	private String species;
	private String breed;
	private String gender;
	private Date birthday;
	private String note;
	private String photopath;
	
	public Pet() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Pet(int memberid, String name, String species, String breed, String gender, Date birthday, String note,
			String photopath) {
		super();
		this.memberid = memberid;
		this.name = name;
		this.species = species;
		this.breed = breed;
		this.gender = gender;
		this.birthday = birthday;
		this.note = note;
		this.photopath = photopath;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPhotopath() {
		return photopath;
	}

	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}
	
	
	

}
