package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dao.PetDao;
import entity.Pet;
import util.DbConnection;

public class PetDaoImpl implements PetDao {
	
	Connection conn=DbConnection.getDb();
	@Override
	public void add(Pet pet) {
		String sql="insert into pet(member_id, name, species, breed, gender, birthday, note, photo_path) "
				+"values(?,?,?,?,?,?,?,?)";
		
		try 
		{
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, pet.getMemberid());
			ps.setString(2, pet.getName());
			ps.setString(3, pet.getSpecies());
			ps.setString(4, pet.getBreed());
			ps.setString(5, pet.getGender());
			if (pet.getBirthday() != null) {
				ps.setDate(6, pet.getBirthday());
			} else {
				ps.setNull(6, Types.DATE);
			}
			ps.setString(7, pet.getNote());
			ps.setString(8, pet.getPhotopath());
			
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public List<Pet> selectByMemberId(int memberid) {
		List<Pet> l=new ArrayList<>();
		
		String sql="SELECT * FROM pet WHERE member_id = ?";
		try 
		{
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,memberid);
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				Pet pet=new Pet();
				pet.setId(rs.getInt("id"));
				pet.setMemberid(rs.getInt("member_id"));
				pet.setName(rs.getString("name"));
				pet.setSpecies(rs.getString("species"));
				pet.setBreed(rs.getString("breed"));
				pet.setGender(rs.getString("gender"));
				pet.setBirthday(rs.getDate("birthday"));
				pet.setNote(rs.getString("note"));
				pet.setPhotopath(rs.getString("photo_path"));
				
				l.add(pet);
			}
			
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return l;
	}

	@Override
	public void update(Pet pet) {
		String sql = "UPDATE pet SET name=?, species=?, breed=?, gender=?, birthday=?, note=?, photo_path=? WHERE id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, pet.getName());
			ps.setString(2, pet.getSpecies());
			ps.setString(3, pet.getBreed());
			ps.setString(4, pet.getGender());
			if (pet.getBirthday() != null) ps.setDate(5, pet.getBirthday()); else ps.setNull(5, Types.DATE);
			ps.setString(6, pet.getNote());
			ps.setString(7, pet.getPhotopath());
			ps.setInt(8, pet.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM pet WHERE id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
