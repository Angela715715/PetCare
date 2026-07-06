package util;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComponent;

/**
 * 簡易 CRUD 工具。
 * AdminTableUI 和一般使用者的清單頁都可以共用。
 */
public class CrudHelper {

    private static class Config {
        String table;
        String idColumn;
        String[] columns;
        String[] labels;
        Config(String table, String idColumn, String[] columns, String[] labels) {
            this.table = table;
            this.idColumn = idColumn;
            this.columns = columns;
            this.labels = labels;
        }
    }

    private static Config config(String type) {
        switch (type) {
        case "members":
            return new Config("member", "id",
                    new String[] {"username", "password", "name", "role"},
                    new String[] {"帳號", "密碼", "姓名", "角色 ADMIN/USER"});
        case "pets":
            return new Config("pet", "id",
                    new String[] {"member_id", "name", "species", "breed", "gender", "birthday", "note", "photo_path"},
                    new String[] {"會員ID", "寵物名稱", "種類", "品種", "性別", "生日", "備註", "照片路徑"});
        case "health":
            return new Config("medical_record", "id",
                    new String[] {"pet_id", "record_type", "title", "record_date", "hospital", "doctor", "note"},
                    new String[] {"寵物ID", "類型", "標題", "日期", "醫院", "醫師", "備註"});
        case "vaccines":
            return new Config("vaccine_record", "id",
                    new String[] {"pet_id", "vaccine_name", "vaccine_date", "next_date", "hospital", "note"},
                    new String[] {"寵物ID", "疫苗名稱", "施打日期", "下次日期", "醫院", "備註"});
        case "weights":
            return new Config("weight_record", "id",
                    new String[] {"pet_id", "weight_date", "weight_kg", "note"},
                    new String[] {"寵物ID", "日期", "體重kg", "備註"});
        case "reminders":
            return new Config("reminder", "id",
                    new String[] {"pet_id", "title", "remind_date", "remind_time", "status", "note"},
                    new String[] {"寵物ID", "提醒事項", "日期", "時間 HH:mm", "狀態", "備註"});
        case "files":
            return new Config("medical_file", "id",
                    new String[] {"pet_id", "file_name", "file_path", "file_type", "upload_date", "note"},
                    new String[] {"寵物ID", "檔名", "檔案路徑", "類型", "上傳日期", "備註"});
        case "photos":
            return new Config("pet_photo", "id",
                    new String[] {"pet_id", "title", "photo_path", "photo_date", "note"},
                    new String[] {"寵物ID", "標題", "照片路徑", "照片日期", "備註"});
        default:
            return config("pets");
        }
    }

    public static boolean add(String type) {
        Config c = config(type);
        String[] values = showForm("新增資料", c.labels, null);
        if (values == null) return false;
        String placeholders = String.join(",", java.util.Collections.nCopies(c.columns.length, "?"));
        String sql = "INSERT INTO " + c.table + "(" + String.join(",", c.columns) + ") VALUES(" + placeholders + ")";
        return execute(sql, values);
    }

    public static boolean edit(String type, int id) {
        Config c = config(type);
        String[] current = readCurrent(c, id);
        String[] values = showForm("編輯資料", c.labels, current);
        if (values == null) return false;
        List<String> sets = new ArrayList<>();
        for (String col : c.columns) sets.add(col + "=?");
        String sql = "UPDATE " + c.table + " SET " + String.join(",", sets) + " WHERE " + c.idColumn + "=?";
        String[] all = Arrays.copyOf(values, values.length + 1);
        all[all.length - 1] = String.valueOf(id);
        return execute(sql, all);
    }

    public static boolean delete(String type, int id) {
        int confirm = JOptionPane.showConfirmDialog(null, "確定要刪除這筆資料嗎？", "確認刪除", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return false;
        Config c = config(type);
        String sql = "DELETE FROM " + c.table + " WHERE " + c.idColumn + "=?";
        return execute(sql, new String[] {String.valueOf(id)});
    }

    public static int selectedId(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "請先選擇一筆資料");
            return -1;
        }
        Object value = table.getValueAt(row, 0);
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "無法取得資料 ID");
            return -1;
        }
    }

    private static String[] readCurrent(Config c, int id) {
        String[] values = new String[c.columns.length];
        String sql = "SELECT " + String.join(",", c.columns) + " FROM " + c.table + " WHERE " + c.idColumn + "=?";
        try {
            Connection conn = DbConnection.getDb();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                for (int i = 0; i < c.columns.length; i++) {
                    Object obj = rs.getObject(i + 1);
                    values[i] = obj == null ? "" : obj.toString();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "讀取資料失敗：" + e.getMessage());
        }
        return values;
    }

    private static String[] showForm(String title, String[] labels, String[] current) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 8, 8));
        JComponent[] fields = new JComponent[labels.length];
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            if (isDateLabel(labels[i])) {
                DatePickerField dateField = new DatePickerField(0, 0, 240, 34);
                dateField.setText(current == null ? "" : safe(current[i]));
                fields[i] = dateField;
            } else {
                fields[i] = new JTextField(current == null ? "" : safe(current[i]));
            }
            panel.add(fields[i]);
        }
        int result = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return null;
        String[] values = new String[labels.length];
        for (int i = 0; i < labels.length; i++) {
            if (fields[i] instanceof DatePickerField) {
                values[i] = ((DatePickerField) fields[i]).getText().trim();
            } else {
                values[i] = ((JTextField) fields[i]).getText().trim();
            }
        }
        return values;
    }

    private static boolean isDateLabel(String label) {
        return label != null && (label.contains("生日") || label.contains("日期"));
    }

    private static boolean execute(String sql, String[] values) {
        try {
            Connection conn = DbConnection.getDb();
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                if (values[i] == null || values[i].trim().isEmpty()) ps.setObject(i + 1, null);
                else ps.setString(i + 1, values[i]);
            }
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "資料操作失敗：" + e.getMessage());
            return false;
        }
    }

    private static String safe(String s) { return s == null ? "" : s; }
}
