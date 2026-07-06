package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dao.MedicalFileDao;
import entity.MedicalFile;
import util.DbConnection;

public class MedicalFileDaoImpl implements MedicalFileDao {
    private Connection conn = DbConnection.getDb();

    @Override
    public void add(MedicalFile record) {
        String sql = "INSERT INTO medical_file(pet_id, file_name, file_path, file_type, upload_date, note) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, record.getPetId());
            ps.setString(2, record.getFileName());
            ps.setString(3, record.getFilePath());
            ps.setString(4, record.getFileType());
            if (record.getUploadDate() != null) ps.setDate(5, record.getUploadDate()); else ps.setNull(5, Types.DATE);
            ps.setString(6, record.getNote());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<MedicalFile> selectByPetId(int petId) {
        List<MedicalFile> list = new ArrayList<>();
        String sql = "SELECT * FROM medical_file WHERE pet_id=? ORDER BY upload_date DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, petId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MedicalFile r = new MedicalFile();
                r.setId(rs.getInt("id"));
                r.setPetId(rs.getInt("pet_id"));
                r.setFileName(rs.getString("file_name"));
                r.setFilePath(rs.getString("file_path"));
                r.setFileType(rs.getString("file_type"));
                r.setUploadDate(rs.getDate("upload_date"));
                r.setNote(rs.getString("note"));
                list.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM medical_file WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
