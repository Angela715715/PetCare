package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dao.MedicalRecordDao;
import entity.MedicalRecord;
import util.DbConnection;

public class MedicalRecordDaoImpl implements MedicalRecordDao {

    private Connection conn = DbConnection.getDb();

    @Override
    public void add(MedicalRecord record) {

        String sql = "INSERT INTO medical_record"
                + "(pet_id, record_type, title, record_date, hospital, doctor, note) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, record.getPetId());
            ps.setString(2, record.getRecordType());
            ps.setString(3, record.getTitle());

            if (record.getRecordDate() != null) {
                ps.setDate(4, record.getRecordDate());
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setString(5, record.getHospital());
            ps.setString(6, record.getDoctor());
            ps.setString(7, record.getNote());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MedicalRecord> selectByPetId(int petId) {

        List<MedicalRecord> list = new ArrayList<>();

        String sql = "SELECT * FROM medical_record WHERE pet_id = ? ORDER BY record_date DESC, id DESC";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, petId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MedicalRecord record = new MedicalRecord();
                record.setId(rs.getInt("id"));
                record.setPetId(rs.getInt("pet_id"));
                record.setRecordType(rs.getString("record_type"));
                record.setTitle(rs.getString("title"));
                record.setRecordDate(rs.getDate("record_date"));
                record.setHospital(rs.getString("hospital"));
                record.setDoctor(rs.getString("doctor"));
                record.setNote(rs.getString("note"));

                list.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM medical_record WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
