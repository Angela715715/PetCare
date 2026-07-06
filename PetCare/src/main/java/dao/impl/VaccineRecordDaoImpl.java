package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dao.VaccineRecordDao;
import entity.VaccineRecord;
import util.DbConnection;

public class VaccineRecordDaoImpl implements VaccineRecordDao {
    private Connection conn = DbConnection.getDb();

    @Override
    public void add(VaccineRecord record) {
        String sql = "INSERT INTO vaccine_record(pet_id, vaccine_name, vaccine_date, next_date, hospital, note) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, record.getPetId());
            ps.setString(2, record.getVaccineName());
            if (record.getVaccineDate() != null) ps.setDate(3, record.getVaccineDate()); else ps.setNull(3, Types.DATE);
            if (record.getNextDate() != null) ps.setDate(4, record.getNextDate()); else ps.setNull(4, Types.DATE);
            ps.setString(5, record.getHospital());
            ps.setString(6, record.getNote());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<VaccineRecord> selectByPetId(int petId) {
        List<VaccineRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM vaccine_record WHERE pet_id=? ORDER BY vaccine_date DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, petId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                VaccineRecord r = new VaccineRecord();
                r.setId(rs.getInt("id"));
                r.setPetId(rs.getInt("pet_id"));
                r.setVaccineName(rs.getString("vaccine_name"));
                r.setVaccineDate(rs.getDate("vaccine_date"));
                r.setNextDate(rs.getDate("next_date"));
                r.setHospital(rs.getString("hospital"));
                r.setNote(rs.getString("note"));
                list.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM vaccine_record WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
