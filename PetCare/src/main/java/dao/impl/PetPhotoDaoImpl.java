package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dao.PetPhotoDao;
import entity.PetPhoto;
import util.DbConnection;

public class PetPhotoDaoImpl implements PetPhotoDao {
    private Connection conn = DbConnection.getDb();

    @Override
    public void add(PetPhoto record) {
        String sql = "INSERT INTO pet_photo(pet_id, title, photo_path, photo_date, note) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, record.getPetId());
            ps.setString(2, record.getTitle());
            ps.setString(3, record.getPhotoPath());
            if (record.getPhotoDate() != null) ps.setDate(4, record.getPhotoDate()); else ps.setNull(4, Types.DATE);
            ps.setString(5, record.getNote());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<PetPhoto> selectByPetId(int petId) {
        List<PetPhoto> list = new ArrayList<>();
        String sql = "SELECT * FROM pet_photo WHERE pet_id=? ORDER BY photo_date DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, petId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PetPhoto r = new PetPhoto();
                r.setId(rs.getInt("id"));
                r.setPetId(rs.getInt("pet_id"));
                r.setTitle(rs.getString("title"));
                r.setPhotoPath(rs.getString("photo_path"));
                r.setPhotoDate(rs.getDate("photo_date"));
                r.setNote(rs.getString("note"));
                list.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM pet_photo WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
