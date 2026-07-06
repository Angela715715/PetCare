package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

import entity.Pet;
import entity.WeightRecord;
import service.WeightRecordService;
import service.impl.WeightRecordServiceImpl;
import util.CrudHelper;
import util.LoginSession;
import util.ModernUI;

public class WeightRecordListUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private Pet pet;
    private JPanel contentPane;
    private JTable table;
    private WeightChartPanel chartPanel;
    private boolean previewMode;

    private JLabel latestValueLabel;
    private JLabel previousValueLabel;
    private JLabel changeValueLabel;
    private JLabel idealValueLabel;
    private JLabel statusLabel;

    private final DecimalFormat df = new DecimalFormat("0.0");

    public WeightRecordListUI() {
        this.pet = createDemoPet();
        this.previewMode = true;
        initialize();
        loadData();
    }

    public WeightRecordListUI(Pet pet) {
        this.pet = pet;
        this.previewMode = false;
        initialize();
        loadData();
    }

    private void initialize() {
        setTitle("PetCare 體重分析");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, ModernUI.FRAME_W, ModernUI.FRAME_H);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = ModernUI.createRoot();
        setContentPane(contentPane);

        createSidebar("體重分析");
        createPageHeader();
        createProfileCard();
        createChartCard();
        createRecordListCard();
    }

    private void createSidebar(String active) {
        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(ModernUI.SIDEBAR);
        sidebar.setBounds(0, 0, ModernUI.SIDEBAR_W, ModernUI.FRAME_H);
        sidebar.setBorder(new LineBorder(ModernUI.BORDER, 1, false));
        contentPane.add(sidebar);

        sidebar.add(ModernUI.logoMark(58, 34));
        sidebar.add(ModernUI.logoText(30, 88));

        JButton navPet = ModernUI.navButton("", "我的寵物", 165, active.equals("我的寵物"));
        JButton navHealth = ModernUI.navButton("", "健康紀錄", 217, active.equals("健康紀錄"));
        JButton navWeight = ModernUI.navButton("", "體重分析", 269, active.equals("體重分析"));
        JButton navReserve = ModernUI.navButton("", "預約管理", 321, active.equals("預約管理"));
        JButton navNotice = ModernUI.navButton("", "通知中心", 373, active.equals("通知中心"));
        JButton navEmergency = ModernUI.navButton("", "緊急醫療卡", 425, active.equals("緊急醫療卡"));
        JButton navMember = ModernUI.navButton("", "會員資料", 477, active.equals("會員資料"));
        sidebar.add(navPet);
        sidebar.add(navHealth);
        sidebar.add(navWeight);
        sidebar.add(navReserve);
        sidebar.add(navNotice);
        sidebar.add(navEmergency);
        sidebar.add(navMember);

        JButton logoutButton = ModernUI.whiteButton("登出", 30, 570, 130, 42);
        sidebar.add(logoutButton);

        navPet.addActionListener(e -> backToHome());
        navHealth.addActionListener(e -> openMedicalRecords());
        navWeight.addActionListener(e -> loadData());
        navReserve.addActionListener(e -> openReminder());
        navNotice.addActionListener(e -> openReminder());
        navEmergency.addActionListener(e -> openEmergencyCard());
        navMember.addActionListener(e -> showMemberInfo());
        logoutButton.addActionListener(e -> {
            LoginSession.loginMember = null;
            LoginUI login = new LoginUI();
            login.setVisible(true);
            dispose();
        });
    }

    private void createPageHeader() {
        JLabel title = new JLabel("體重分析");
        title.setFont(new Font("Microsoft JhengHei", Font.BOLD, 34));
        title.setForeground(ModernUI.BLACK);
        title.setBounds(245, 35, 260, 45);
        contentPane.add(title);

        JLabel subtitle = new JLabel("追蹤毛孩的體重變化，掌握健康狀況");
        subtitle.setFont(ModernUI.NORMAL_FONT);
        subtitle.setForeground(ModernUI.TEXT_GRAY);
        subtitle.setBounds(248, 82, 360, 26);
        contentPane.add(subtitle);

        JLabel userInfo = new JLabel("目前登入者：" + getLoginNameForDisplay());
        userInfo.setFont(ModernUI.NORMAL_FONT);
        userInfo.setForeground(ModernUI.TEXT_GRAY);
        userInfo.setHorizontalAlignment(SwingConstants.RIGHT);
        userInfo.setBounds(800, 45, 180, 30);
        contentPane.add(userInfo);
    }

    private void createProfileCard() {
        JPanel card = ModernUI.createCard(245, 125, 755, 110);
        contentPane.add(card);

        JLabel imageLabel = new JLabel(ModernUI.petImage(pet.getPhotopath(), 115, 78));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        imageLabel.setBounds(24, 16, 115, 78);
        card.add(imageLabel);

        JLabel nameLabel = new JLabel(ModernUI.safe(pet.getName()));
        nameLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        nameLabel.setForeground(ModernUI.BLACK);
        nameLabel.setBounds(165, 18, 220, 32);
        card.add(nameLabel);

        JLabel speciesChip = ModernUI.chip(ModernUI.safe(pet.getSpecies()), 165, 57, 52, 26);
        JLabel genderChip = ModernUI.chip(ModernUI.safe(pet.getGender()), 228, 57, 52, 26);
        card.add(speciesChip);
        card.add(genderChip);

        String birthday = pet.getBirthday() == null ? "未填寫" : pet.getBirthday().toString();
        card.add(profileInfo("生日", birthday, 340, 34));
        card.add(profileInfo("品種", ModernUI.safe(pet.getBreed()), 500, 34));

        JButton backButton = ModernUI.whiteButton("返回", 580, 18, 50, 32);
        JButton addButton = ModernUI.blackButton("記錄體重", 640, 18, 90, 32);
        card.add(backButton);
        card.add(addButton);

        backButton.addActionListener(e -> backToDetail());
        addButton.addActionListener(e -> openAddUI());
    }

    private JLabel profileInfo(String title, String value, int x, int y) {
        JLabel label = new JLabel("<html><span style='color:#777777;'>" + title + "</span><br>" + value + "</html>");
        label.setFont(ModernUI.NORMAL_FONT);
        label.setForeground(ModernUI.BLACK);
        label.setBounds(x, y, 145, 50);
        return label;
    }

    private void createChartCard() {
        JPanel card = ModernUI.createCard(245, 255, 390, 365);
        contentPane.add(card);

        JLabel title = new JLabel("體重趨勢圖");
        title.setFont(ModernUI.SECTION_FONT);
        title.setForeground(ModernUI.BLACK);
        title.setBounds(20, 18, 180, 28);
        card.add(title);

        latestValueLabel = createMetricCard(card, "最新體重", "--", 20, 58);
        previousValueLabel = createMetricCard(card, "較上次", "--", 112, 58);
        changeValueLabel = createMetricCard(card, "平均體重", "--", 204, 58);
        idealValueLabel = createMetricCard(card, "狀態", "--", 296, 58);

        statusLabel = new JLabel("狀態：尚無資料");
        statusLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        statusLabel.setForeground(ModernUI.BLACK);
        statusLabel.setBounds(22, 142, 340, 24);
        card.add(statusLabel);

        chartPanel = new WeightChartPanel();
        chartPanel.setBounds(20, 175, 350, 165);
        chartPanel.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        card.add(chartPanel);
    }

    private void createRecordListCard() {
        JPanel card = ModernUI.createCard(650, 255, 350, 365);
        contentPane.add(card);

        JLabel title = new JLabel("體重紀錄列表");
        title.setFont(ModernUI.SECTION_FONT);
        title.setForeground(ModernUI.BLACK);
        title.setBounds(20, 18, 180, 28);
        card.add(title);

        JButton refreshButton = ModernUI.whiteButton("重新整理", 144, 18, 70, 32);
        JButton editButton = ModernUI.whiteButton("編輯", 222, 18, 50, 32);
        JButton deleteButton = ModernUI.whiteButton("刪除", 280, 18, 50, 32);
        card.add(refreshButton);
        card.add(editButton);
        card.add(deleteButton);

        JScrollPane tablePane = new JScrollPane();
        tablePane.setBounds(20, 64, 310, 275);
        tablePane.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        tablePane.getVerticalScrollBar().setUnitIncrement(16);
        card.add(tablePane);

        table = new JTable();
        table.setFont(ModernUI.SMALL_FONT);
        table.setRowHeight(28);
        table.setForeground(ModernUI.BLACK);
        table.setGridColor(ModernUI.BORDER);
        table.setSelectionBackground(ModernUI.CHIP);
        table.setSelectionForeground(ModernUI.BLACK);
        table.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(250, 248, 245));
        table.getTableHeader().setForeground(ModernUI.BLACK);
        tablePane.setViewportView(table);

        refreshButton.addActionListener(e -> loadData());
        editButton.addActionListener(e -> {
            int id = CrudHelper.selectedId(table);
            if (id > 0 && CrudHelper.edit("weights", id)) loadData();
        });
        deleteButton.addActionListener(e -> {
            int id = CrudHelper.selectedId(table);
            if (id > 0 && CrudHelper.delete("weights", id)) loadData();
        });
    }

    private JLabel createMetricCard(JPanel parent, String title, String value, int x, int y) {
        JPanel box = new JPanel(null);
        box.setBackground(new Color(255, 254, 252));
        box.setBounds(x, y, 82, 74);
        box.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        parent.add(box);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ModernUI.SMALL_FONT);
        titleLabel.setForeground(ModernUI.TEXT_GRAY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(4, 9, 74, 18);
        box.add(titleLabel);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 15));
        valueLabel.setForeground(ModernUI.BLACK);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setBounds(4, 34, 74, 24);
        box.add(valueLabel);
        return valueLabel;
    }

    private DefaultTableModel createModel() {
        DefaultTableModel model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        model.addColumn("ID");
        model.addColumn("日期");
        model.addColumn("kg");
        model.addColumn("備註");
        return model;
    }

    private void loadData() {
        List<WeightRecord> records;
        if (previewMode) {
            records = createDemoWeightRecords();
        } else {
            try {
                WeightRecordService service = new WeightRecordServiceImpl();
                records = service.findByPetId(pet.getId());
            } catch (Exception e) {
                e.printStackTrace();
                records = new ArrayList<>();
            }
        }
        updateAnalysis(records);
        updateChart(records);
        updateTable(records);
    }

    private void updateAnalysis(List<WeightRecord> records) {
        if (records == null || records.isEmpty()) {
            latestValueLabel.setText("--");
            previousValueLabel.setText("--");
            changeValueLabel.setText("--");
            idealValueLabel.setText("--");
            statusLabel.setText("狀態：目前沒有體重資料，請先新增紀錄");
            return;
        }

        WeightRecord latest = records.get(records.size() - 1);
        WeightRecord previous = records.size() >= 2 ? records.get(records.size() - 2) : null;

        double total = 0;
        for (WeightRecord r : records) {
            total += r.getWeightKg();
        }
        double avg = total / records.size();

        latestValueLabel.setText(df.format(latest.getWeightKg()) + " kg");
        changeValueLabel.setText(df.format(avg) + " kg");

        if (previous == null) {
            previousValueLabel.setText("--");
            idealValueLabel.setText("追蹤中");
            statusLabel.setText("狀態：已有第一筆體重紀錄，建議持續追蹤");
            return;
        }

        double change = latest.getWeightKg() - previous.getWeightKg();
        previousValueLabel.setText(formatSignedKg(change));

        if (change > 0.20) {
            idealValueLabel.setText("上升");
            statusLabel.setText("狀態：體重上升，建議持續觀察飲食與活動量");
        } else if (change < -0.20) {
            idealValueLabel.setText("下降");
            statusLabel.setText("狀態：體重下降，建議留意食慾與健康狀況");
        } else {
            idealValueLabel.setText("穩定");
            statusLabel.setText("狀態：體重穩定，維持目前照護狀態");
        }
    }

    private void updateChart(List<WeightRecord> records) {
        chartPanel.setRecords(records);
    }

    private void updateTable(List<WeightRecord> records) {
        DefaultTableModel model = createModel();
        if (records != null) {
            for (int i = records.size() - 1; i >= 0; i--) {
                WeightRecord r = records.get(i);
                model.addRow(new Object[] {
                        r.getId(),
                        r.getWeightDate(),
                        df.format(r.getWeightKg()),
                        ModernUI.safe(r.getNote())
                });
            }
        }
        table.setModel(model);
        if (table.getColumnModel().getColumnCount() >= 4) {
            table.getColumnModel().getColumn(0).setPreferredWidth(35);
            table.getColumnModel().getColumn(1).setPreferredWidth(85);
            table.getColumnModel().getColumn(2).setPreferredWidth(45);
            table.getColumnModel().getColumn(3).setPreferredWidth(145);
        }
    }

    private String formatSignedKg(double value) {
        return (value > 0 ? "+" : "") + df.format(value) + " kg";
    }

    private void openAddUI() {
        AddWeightRecordUI ui = new AddWeightRecordUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void backToDetail() {
        PetDetailUI detail = new PetDetailUI(pet);
        detail.setVisible(true);
        dispose();
    }

    private void backToHome() {
        PetHomeUI home = new PetHomeUI();
        home.setVisible(true);
        dispose();
    }

    private void openMedicalRecords() {
        MedicalRecordListUI ui = new MedicalRecordListUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void openReminder() {
        ReminderListUI ui = new ReminderListUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void openEmergencyCard() {
        EmergencySummaryUI ui = new EmergencySummaryUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void showMemberInfo() {
        if (LoginSession.loginMember == null) JOptionPane.showMessageDialog(null, "目前沒有登入資料");
        else JOptionPane.showMessageDialog(null,
                "目前登入者：" + LoginSession.loginMember.getName()
                + "\n帳號：" + LoginSession.loginMember.getUsername()
                + "\n角色：" + LoginSession.loginMember.getRole());
    }

    private String getLoginNameForDisplay() {
        if (LoginSession.loginMember == null) return "Guest";
        return LoginSession.loginMember.getName();
    }

    private static Pet createDemoPet() {
        Pet demo = new Pet();
        demo.setId(1);
        demo.setName("Momo");
        demo.setSpecies("狗");
        demo.setBreed("馬爾濟斯");
        demo.setGender("母");
        demo.setBirthday(Date.valueOf("2020-03-15"));
        demo.setNote("WindowBuilder 預覽資料");
        demo.setPhotopath("");
        return demo;
    }

    private static List<WeightRecord> createDemoWeightRecords() {
        List<WeightRecord> list = new ArrayList<>();
        list.add(new WeightRecord(1, Date.valueOf("2026-01-01"), 3.2, "第一次記錄體重"));
        list.add(new WeightRecord(1, Date.valueOf("2026-02-01"), 3.5, "食慾正常"));
        list.add(new WeightRecord(1, Date.valueOf("2026-03-01"), 3.8, "活動量增加"));
        list.add(new WeightRecord(1, Date.valueOf("2026-04-01"), 4.1, "穩定增加"));
        list.add(new WeightRecord(1, Date.valueOf("2026-05-01"), 4.4, "精神良好"));
        list.add(new WeightRecord(1, Date.valueOf("2026-06-01"), 4.7, "持續追蹤"));
        return list;
    }

    private class WeightChartPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private List<WeightRecord> records = new ArrayList<>();

        public WeightChartPanel() { setBackground(Color.WHITE); }

        public void setRecords(List<WeightRecord> records) {
            this.records = records == null ? new ArrayList<>() : new ArrayList<>(records);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, w, h);

            if (records == null || records.isEmpty()) {
                g2.setFont(ModernUI.NORMAL_FONT);
                g2.setColor(ModernUI.TEXT_GRAY);
                g2.drawString("目前沒有體重資料", Math.max(20, w / 2 - 60), h / 2);
                g2.dispose();
                return;
            }

            int left = 48, right = 28, top = 18, bottom = 30;
            int chartW = w - left - right;
            int chartH = h - top - bottom;
            double min = records.get(0).getWeightKg();
            double max = min;
            for (WeightRecord r : records) {
                min = Math.min(min, r.getWeightKg());
                max = Math.max(max, r.getWeightKg());
            }
            if (Math.abs(max - min) < 0.01) { max += 0.5; min -= 0.5; }
            else { double p = (max - min) * 0.18; max += p; min -= p; }

            FontMetrics fm = g2.getFontMetrics(ModernUI.SMALL_FONT);
            g2.setFont(ModernUI.SMALL_FONT);
            g2.setColor(new Color(242, 236, 229));
            for (int i = 0; i <= 4; i++) {
                int y = top + chartH * i / 4;
                g2.drawLine(left, y, left + chartW, y);
                double value = max - (max - min) * i / 4.0;
                String label = df.format(value);
                g2.setColor(ModernUI.TEXT_GRAY);
                g2.drawString(label, left - fm.stringWidth(label) - 8, y + 4);
                g2.setColor(new Color(242, 236, 229));
            }

            int count = records.size();
            int[] xs = new int[count];
            int[] ys = new int[count];
            for (int i = 0; i < count; i++) {
                double xRatio = count == 1 ? 0.5 : (double) i / (count - 1);
                double yRatio = (records.get(i).getWeightKg() - min) / (max - min);
                xs[i] = left + (int) Math.round(chartW * xRatio);
                ys[i] = top + chartH - (int) Math.round(chartH * yRatio);
            }

            g2.setColor(new Color(250, 241, 231));
            for (int i = 0; i < count - 1; i++) {
                int[] xPoly = { xs[i], xs[i + 1], xs[i + 1], xs[i] };
                int[] yPoly = { ys[i], ys[i + 1], top + chartH, top + chartH };
                g2.fillPolygon(xPoly, yPoly, 4);
            }

            g2.setColor(ModernUI.CHART_LINE);
            g2.setStroke(new BasicStroke(2.2f));
            for (int i = 0; i < count - 1; i++) g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);

            for (int i = 0; i < count; i++) {
                g2.setColor(Color.WHITE);
                g2.fillOval(xs[i] - 5, ys[i] - 5, 10, 10);
                g2.setColor(ModernUI.CHART_LINE);
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawOval(xs[i] - 5, ys[i] - 5, 10, 10);
                String value = df.format(records.get(i).getWeightKg());
                g2.setFont(ModernUI.SMALL_FONT);
                g2.setColor(ModernUI.BLACK);
                int tw = fm.stringWidth(value);
                g2.drawString(value, xs[i] - tw / 2, ys[i] - 10);
            }

            g2.setColor(ModernUI.TEXT_GRAY);
            int step = Math.max(1, (int) Math.ceil(count / 5.0));
            for (int i = 0; i < count; i++) {
                if (i % step == 0 || i == count - 1) {
                    String date = records.get(i).getWeightDate() == null ? "--" : records.get(i).getWeightDate().toString().substring(5);
                    int tw = fm.stringWidth(date);
                    g2.drawString(date, xs[i] - tw / 2, top + chartH + 20);
                }
            }
            g2.dispose();
        }
    }
}
