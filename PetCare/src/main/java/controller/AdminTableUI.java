package controller;

import java.awt.Cursor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import util.DbConnection;
import util.LoginSession;
import util.ModernUI;
import util.CrudHelper;

public class AdminTableUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable table;
    private String pageTitle;
    private String pageSubtitle;
    private String sql;
    private String currentType;

    public AdminTableUI() {
        this("所有寵物資料", "查看系統內所有寵物資料", getSql("pets"));
    }

    public AdminTableUI(String pageTitle, String pageSubtitle, String sql) {
        this.pageTitle = pageTitle;
        this.pageSubtitle = pageSubtitle;
        this.sql = sql;
        this.currentType = detectTypeByTitle(pageTitle);
        initialize();
        loadData();
    }

    private void initialize() {
        setTitle("PetCare Admin - " + pageTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, ModernUI.FRAME_W, ModernUI.FRAME_H);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = ModernUI.createRoot();
        setContentPane(contentPane);

        createSidebar();
        createHeader();
        createTableCard();
    }

    private void createSidebar() {
        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(ModernUI.SIDEBAR);
        sidebar.setBounds(0, 0, ModernUI.SIDEBAR_W, ModernUI.FRAME_H);
        sidebar.setBorder(new LineBorder(ModernUI.BORDER, 1, false));
        contentPane.add(sidebar);

        sidebar.add(ModernUI.logoMark(58, 34));
        sidebar.add(ModernUI.logoText(30, 88));

        JButton dashboardButton = ModernUI.navButton("", "管理首頁", 145, false);
        JButton petButton = ModernUI.navButton("", "所有寵物", 190, pageTitle.contains("寵物"));
        JButton memberButton = ModernUI.navButton("", "使用者管理", 235, pageTitle.contains("使用者"));
        JButton healthButton = ModernUI.navButton("", "健康紀錄", 280, pageTitle.contains("健康"));
        JButton reminderButton = ModernUI.navButton("", "提醒事項", 325, pageTitle.contains("提醒"));
        JButton fileButton = ModernUI.navButton("", "醫療文件", 370, pageTitle.contains("文件"));
        JButton vaccineButton = ModernUI.navButton("", "疫苗紀錄", 415, pageTitle.contains("疫苗"));
        JButton weightButton = ModernUI.navButton("", "體重紀錄", 460, pageTitle.contains("體重"));
        JButton photoButton = ModernUI.navButton("", "成長相簿", 505, pageTitle.contains("相簿"));
        sidebar.add(dashboardButton);
        sidebar.add(petButton);
        sidebar.add(memberButton);
        sidebar.add(healthButton);
        sidebar.add(reminderButton);
        sidebar.add(fileButton);
        sidebar.add(vaccineButton);
        sidebar.add(weightButton);
        sidebar.add(photoButton);

        JButton logoutButton = ModernUI.whiteButton("登出", 30, 595, 130, 34);
        sidebar.add(logoutButton);

        dashboardButton.addActionListener(e -> backToAdmin());
        petButton.addActionListener(e -> open("pets"));
        memberButton.addActionListener(e -> open("members"));
        healthButton.addActionListener(e -> open("health"));
        reminderButton.addActionListener(e -> open("reminders"));
        fileButton.addActionListener(e -> open("files"));
        vaccineButton.addActionListener(e -> open("vaccines"));
        weightButton.addActionListener(e -> open("weights"));
        photoButton.addActionListener(e -> open("photos"));
        logoutButton.addActionListener(e -> logout());
    }

    private void createHeader() {
        JLabel titleLabel = new JLabel(pageTitle);
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setForeground(ModernUI.BLACK);
        titleLabel.setBounds(220, 62, 400, 42);
        contentPane.add(titleLabel);

        JLabel subtitleLabel = new JLabel(pageSubtitle);
        subtitleLabel.setFont(ModernUI.NORMAL_FONT);
        subtitleLabel.setForeground(ModernUI.TEXT_GRAY);
        subtitleLabel.setBounds(222, 104, 500, 26);
        contentPane.add(subtitleLabel);

        JLabel userInfoLabel = new JLabel("目前登入者：" + getLoginNameForDisplay());
        userInfoLabel.setFont(ModernUI.NORMAL_FONT);
        userInfoLabel.setForeground(ModernUI.TEXT_GRAY);
        userInfoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        userInfoLabel.setBounds(720, 66, 275, 28);
        contentPane.add(userInfoLabel);
    }

    private void createTableCard() {
        JPanel card = ModernUI.createCard(220, 145, 775, 480);
        contentPane.add(card);

        JButton addButton = ModernUI.blackButton("新增", 420, 26, 86, 36);
        JButton editButton = ModernUI.whiteButton("編輯", 514, 26, 86, 36);
        JButton deleteButton = ModernUI.whiteButton("刪除", 608, 26, 86, 36);
        JButton refreshButton = ModernUI.whiteButton("整理", 702, 26, 54, 36);
        card.add(addButton);
        card.add(editButton);
        card.add(deleteButton);
        card.add(refreshButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(28, 82, 720, 360);
        scrollPane.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        card.add(scrollPane);

        table = new JTable();
        table.setFont(ModernUI.NORMAL_FONT);
        table.setRowHeight(30);
        table.getTableHeader().setFont(ModernUI.BUTTON_FONT);
        table.getTableHeader().setBackground(ModernUI.SOFT_GRAY);
        table.getTableHeader().setForeground(ModernUI.BLACK);
        scrollPane.setViewportView(table);

        addButton.addActionListener(e -> { if (CrudHelper.add(currentType)) loadData(); });
        editButton.addActionListener(e -> { int id = CrudHelper.selectedId(table); if (id > 0 && CrudHelper.edit(currentType, id)) loadData(); });
        deleteButton.addActionListener(e -> { int id = CrudHelper.selectedId(table); if (id > 0 && CrudHelper.delete(currentType, id)) loadData(); });
        refreshButton.addActionListener(e -> loadData());
    }

    private void loadData() {
        DefaultTableModel model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            Connection conn = DbConnection.getDb();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(meta.getColumnLabel(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "資料載入失敗，請確認 MySQL 與資料表。\n" + e.getMessage());
        }

        table.setModel(model);
    }

    private void open(String type) {
        AdminTableUI ui = new AdminTableUI(getTitle(type), getSubtitle(type), getSql(type));
        ui.setVisible(true);
        dispose();
    }

    private void backToAdmin() {
        MainUI main = new MainUI();
        main.setVisible(true);
        dispose();
    }

    private void logout() {
        LoginSession.loginMember = null;
        LoginUI login = new LoginUI();
        login.setVisible(true);
        dispose();
    }

    private String getLoginNameForDisplay() {
        if (LoginSession.loginMember == null) return "ADMIN";
        return LoginSession.loginMember.getName();
    }

    private String detectTypeByTitle(String title) {
        if (title == null) return "pets";
        if (title.contains("使用者")) return "members";
        if (title.contains("健康")) return "health";
        if (title.contains("提醒")) return "reminders";
        if (title.contains("文件")) return "files";
        if (title.contains("疫苗")) return "vaccines";
        if (title.contains("體重")) return "weights";
        if (title.contains("相簿")) return "photos";
        return "pets";
    }

    public static String getTitle(String type) {
        switch (type) {
        case "members": return "使用者管理";
        case "health": return "健康紀錄總覽";
        case "reminders": return "提醒事項總覽";
        case "files": return "醫療文件總覽";
        case "vaccines": return "疫苗紀錄總覽";
        case "weights": return "體重紀錄總覽";
        case "photos": return "成長相簿總覽";
        default: return "所有寵物資料";
        }
    }

    public static String getSubtitle(String type) {
        switch (type) {
        case "members": return "查看系統內所有會員與角色";
        case "health": return "查看所有寵物的健康與看診紀錄";
        case "reminders": return "查看所有提醒事項與完成狀態";
        case "files": return "查看所有醫療文件上傳紀錄";
        case "vaccines": return "查看所有疫苗紀錄";
        case "weights": return "查看所有體重紀錄";
        case "photos": return "查看所有寵物相簿照片";
        default: return "查看系統內所有寵物與飼主資料";
        }
    }

    public static String getSql(String type) {
        switch (type) {
        case "members":
            return "SELECT id AS 編號, username AS 帳號, name AS 姓名, role AS 角色 FROM member ORDER BY id";
        case "health":
            return "SELECT mr.id AS 編號, p.name AS 寵物, m.name AS 飼主, mr.record_type AS 類型, "
                    + "mr.title AS 標題, mr.record_date AS 日期, mr.hospital AS 醫院, mr.doctor AS 醫師, mr.note AS 備註 "
                    + "FROM medical_record mr JOIN pet p ON mr.pet_id = p.id JOIN member m ON p.member_id = m.id "
                    + "ORDER BY mr.record_date DESC, mr.id DESC";
        case "reminders":
            return "SELECT r.id AS 編號, p.name AS 寵物, m.name AS 飼主, r.title AS 提醒事項, r.remind_date AS 日期, "
                    + "r.remind_time AS 時間, r.status AS 狀態, r.note AS 備註 "
                    + "FROM reminder r JOIN pet p ON r.pet_id = p.id JOIN member m ON p.member_id = m.id "
                    + "ORDER BY r.remind_date DESC, r.id DESC";
        case "files":
            return "SELECT f.id AS 編號, p.name AS 寵物, m.name AS 飼主, f.file_name AS 檔名, f.file_type AS 類型, "
                    + "f.upload_date AS 上傳日期, f.file_path AS 路徑, f.note AS 備註 "
                    + "FROM medical_file f JOIN pet p ON f.pet_id = p.id JOIN member m ON p.member_id = m.id "
                    + "ORDER BY f.upload_date DESC, f.id DESC";
        case "vaccines":
            return "SELECT v.id AS 編號, p.name AS 寵物, m.name AS 飼主, v.vaccine_name AS 疫苗名稱, "
                    + "v.vaccine_date AS 接種日期, v.next_date AS 下次日期, v.hospital AS 醫院, v.note AS 備註 "
                    + "FROM vaccine_record v JOIN pet p ON v.pet_id = p.id JOIN member m ON p.member_id = m.id "
                    + "ORDER BY v.vaccine_date DESC, v.id DESC";
        case "weights":
            return "SELECT w.id AS 編號, p.name AS 寵物, m.name AS 飼主, w.weight_date AS 日期, "
                    + "w.weight_kg AS 體重kg, w.note AS 備註 "
                    + "FROM weight_record w JOIN pet p ON w.pet_id = p.id JOIN member m ON p.member_id = m.id "
                    + "ORDER BY w.weight_date DESC, w.id DESC";
        case "photos":
            return "SELECT ph.id AS 編號, p.name AS 寵物, m.name AS 飼主, ph.title AS 標題, ph.photo_date AS 日期, "
                    + "ph.photo_path AS 照片路徑, ph.note AS 備註 "
                    + "FROM pet_photo ph JOIN pet p ON ph.pet_id = p.id JOIN member m ON p.member_id = m.id "
                    + "ORDER BY ph.photo_date DESC, ph.id DESC";
        default:
            return "SELECT p.id AS 編號, p.name AS 寵物名稱, m.name AS 飼主, p.species AS 種類, p.breed AS 品種, "
                    + "p.gender AS 性別, p.birthday AS 生日, p.note AS 備註 "
                    + "FROM pet p JOIN member m ON p.member_id = m.id ORDER BY p.id";
        }
    }
}
