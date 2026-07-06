package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dao.WeightRecordDao;
import entity.WeightRecord;
import util.DbConnection;

public class WeightRecordDaoImpl implements WeightRecordDao {
    private Connection conn = DbConnection.getDb();

    @Override
    public void add(WeightRecord record) {
        String sql = "INSERT INTO weight_record(pet_id, weight_date, weight_kg, note) VALUES(?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, record.getPetId());
            if (record.getWeightDate() != null) ps.setDate(2, record.getWeightDate()); else ps.setNull(2, Types.DATE);
            ps.setDouble(3, record.getWeightKg());
            ps.setString(4, record.getNote());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<WeightRecord> selectByPetId(int petId) {
        List<WeightRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM weight_record WHERE pet_id=? ORDER BY weight_date ASC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, petId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                WeightRecord r = new WeightRecord();
                r.setId(rs.getInt("id"));
                r.setPetId(rs.getInt("pet_id"));
                r.setWeightDate(rs.getDate("weight_date"));
                r.setWeightKg(rs.getDouble("weight_kg"));
                r.setNote(rs.getString("note"));
                list.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM weight_record WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
