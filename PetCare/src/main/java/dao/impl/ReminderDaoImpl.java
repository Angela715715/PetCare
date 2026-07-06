package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dao.ReminderDao;
import entity.Reminder;
import util.DbConnection;

public class ReminderDaoImpl implements ReminderDao {
    private Connection conn = DbConnection.getDb();

    @Override
    public void add(Reminder record) {
        String sql = "INSERT INTO reminder(pet_id, title, remind_date, remind_time, status, note) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, record.getPetId());
            ps.setString(2, record.getTitle());
            if (record.getRemindDate() != null) ps.setDate(3, record.getRemindDate()); else ps.setNull(3, Types.DATE);
            ps.setString(4, record.getRemindTime());
            ps.setString(5, record.getStatus());
            ps.setString(6, record.getNote());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Reminder> selectByPetId(int petId) {
        List<Reminder> list = new ArrayList<>();
        String sql = "SELECT * FROM reminder WHERE pet_id=? ORDER BY remind_date ASC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, petId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reminder r = new Reminder();
                r.setId(rs.getInt("id"));
                r.setPetId(rs.getInt("pet_id"));
                r.setTitle(rs.getString("title"));
                r.setRemindDate(rs.getDate("remind_date"));
                r.setRemindTime(rs.getString("remind_time"));
                r.setStatus(rs.getString("status"));
                r.setNote(rs.getString("note"));
                list.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void delete(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM reminder WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void updateStatus(int id, String status) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE reminder SET status=? WHERE id=?");
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
