package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.sql.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import entity.MedicalRecord;
import entity.Pet;
import service.MedicalRecordService;
import service.impl.MedicalRecordServiceImpl;
import util.ModernUI;
import util.CrudHelper;

public class MedicalRecordListUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private Pet pet;
    private JPanel contentPane;
    private JPanel sidebar;
    private JPanel listCard;
    private JLabel logoLabel;
    private JLabel breadcrumbLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JTable table;
    private JButton sidebarBackButton;
    private JButton topBackButton;
    private JButton addButton;
    private JButton refreshButton;
    private JButton backButton;
    private JButton editButton;
    private JButton deleteButton;

    // 給 WindowBuilder 預覽用，不會查資料庫
    public MedicalRecordListUI() {
        this.pet = createDemoPet();
        initialize();
        loadDemoData();
    }

    // 實際使用：從 PetDetailUI 傳入目前點選的寵物
    public MedicalRecordListUI(Pet pet) {
        this.pet = pet;
        initialize();
        loadData();
    }

    private void initialize() {
        setTitle("PetCare 健康紀錄");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1040, 680);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(ModernUI.CANVAS);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        sidebar = new JPanel();
        sidebar.setBackground(ModernUI.SIDEBAR);
        sidebar.setBounds(0, 0, 170, 680);
        sidebar.setLayout(null);
        contentPane.add(sidebar);

        logoLabel = new JLabel("PetCare");
        logoLabel.setFont(ModernUI.LOGO_FONT);
        logoLabel.setForeground(ModernUI.BLACK);
        logoLabel.setBounds(24, 28, 130, 30);
        sidebar.add(logoLabel);

        JLabel menuLabel = new JLabel("健康紀錄");
        menuLabel.setFont(ModernUI.BUTTON_FONT);
        menuLabel.setForeground(ModernUI.BLACK);
        menuLabel.setBounds(34, 110, 100, 28);
        sidebar.add(menuLabel);

        sidebarBackButton = new JButton("返回首頁");
        sidebarBackButton.setFont(ModernUI.BUTTON_FONT);
        sidebarBackButton.setBounds(24, 550, 120, 36);
        sidebarBackButton.setBackground(ModernUI.CARD);
        sidebarBackButton.setForeground(ModernUI.BLACK);
        sidebarBackButton.setFocusPainted(false);
        sidebarBackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sidebarBackButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        sidebar.add(sidebarBackButton);

        topBackButton = new JButton("返回");
        topBackButton.setFont(ModernUI.BUTTON_FONT);
        topBackButton.setBounds(220, 42, 96, 36);
        topBackButton.setBackground(ModernUI.CARD);
        topBackButton.setForeground(ModernUI.BLACK);
        topBackButton.setFocusPainted(false);
        topBackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topBackButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        contentPane.add(topBackButton);

        breadcrumbLabel = new JLabel("我的寵物 / 寵物詳細 / 健康紀錄");
        breadcrumbLabel.setFont(ModernUI.SMALL_FONT);
        breadcrumbLabel.setForeground(ModernUI.TEXT_GRAY);
        breadcrumbLabel.setBounds(330, 48, 320, 25);
        contentPane.add(breadcrumbLabel);

        listCard = new JPanel();
        listCard.setBackground(ModernUI.CARD);
        listCard.setBounds(220, 95, 775, 520);
        listCard.setLayout(null);
        listCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        contentPane.add(listCard);

        titleLabel = new JLabel("健康紀錄");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setForeground(ModernUI.BLACK);
        titleLabel.setBounds(34, 28, 200, 38);
        listCard.add(titleLabel);

        subtitleLabel = new JLabel("寵物：" + ModernUI.safe(pet.getName()) + " 的看診、疫苗與照護紀錄");
        subtitleLabel.setFont(ModernUI.NORMAL_FONT);
        subtitleLabel.setForeground(ModernUI.TEXT_GRAY);
        subtitleLabel.setBounds(36, 66, 430, 25);
        listCard.add(subtitleLabel);

        addButton = new JButton("新增紀錄");
        addButton.setFont(ModernUI.BUTTON_FONT);
        addButton.setBounds(610, 40, 120, 38);
        addButton.setBackground(ModernUI.BLACK);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setBorder(new LineBorder(ModernUI.BLACK, 1, true));
        listCard.add(addButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(36, 118, 695, 310);
        scrollPane.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        listCard.add(scrollPane);

        table = new JTable();
        table.setFont(ModernUI.NORMAL_FONT);
        table.setRowHeight(34);
        table.setForeground(ModernUI.BLACK);
        table.setGridColor(ModernUI.BORDER);
        table.setSelectionBackground(ModernUI.CHIP);
        table.setSelectionForeground(ModernUI.BLACK);
        table.getTableHeader().setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(250, 248, 245));
        table.getTableHeader().setForeground(ModernUI.BLACK);
        scrollPane.setViewportView(table);

        refreshButton = new JButton("重新整理");
        refreshButton.setFont(ModernUI.BUTTON_FONT);
        refreshButton.setBounds(36, 458, 100, 38);
        refreshButton.setBackground(ModernUI.CARD);
        refreshButton.setForeground(ModernUI.BLACK);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        listCard.add(refreshButton);

        editButton = new JButton("編輯選取");
        editButton.setFont(ModernUI.BUTTON_FONT);
        editButton.setBounds(150, 458, 100, 38);
        editButton.setBackground(ModernUI.CARD);
        editButton.setForeground(ModernUI.BLACK);
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        listCard.add(editButton);

        deleteButton = new JButton("刪除選取");
        deleteButton.setFont(ModernUI.BUTTON_FONT);
        deleteButton.setBounds(264, 458, 100, 38);
        deleteButton.setBackground(ModernUI.CARD);
        deleteButton.setForeground(ModernUI.BLACK);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        listCard.add(deleteButton);

        backButton = new JButton("返回詳細頁");
        backButton.setFont(ModernUI.BUTTON_FONT);
        backButton.setBounds(610, 458, 120, 38);
        backButton.setBackground(ModernUI.CARD);
        backButton.setForeground(ModernUI.BLACK);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        listCard.add(backButton);

        topBackButton.addActionListener(e -> backToDetail());
        sidebarBackButton.addActionListener(e -> backToHome());
        backButton.addActionListener(e -> backToDetail());
        addButton.addActionListener(e -> openAddRecordUI());
        refreshButton.addActionListener(e -> loadData());
        editButton.addActionListener(e -> { int id = CrudHelper.selectedId(table); if (id > 0 && CrudHelper.edit("health", id)) loadData(); });
        deleteButton.addActionListener(e -> { int id = CrudHelper.selectedId(table); if (id > 0 && CrudHelper.delete("health", id)) loadData(); });
    }

    private void loadData() {
        if (pet == null) {
            pet = createDemoPet();
        }

        MedicalRecordService service = new MedicalRecordServiceImpl();
        List<MedicalRecord> records = service.findByPetId(pet.getId());

        DefaultTableModel model = createTableModel();

        for (MedicalRecord record : records) {
            model.addRow(new Object[] {
                    record.getId(),
                    record.getRecordType(),
                    record.getTitle(),
                    record.getRecordDate(),
                    record.getHospital(),
                    record.getDoctor(),
                    record.getNote()
            });
        }

        table.setModel(model);
        setColumnWidth();
    }

    private void loadDemoData() {
        DefaultTableModel model = createTableModel();
        model.addRow(new Object[] { 1, "疫苗", "狂犬病疫苗", Date.valueOf("2024-04-10"), "幸福動物醫院", "王醫師", "一年一次" });
        model.addRow(new Object[] { 2, "看診", "腸胃不適", Date.valueOf("2024-05-02"), "幸福動物醫院", "陳醫師", "食慾不佳" });
        table.setModel(model);
        setColumnWidth();
    }

    private DefaultTableModel createTableModel() {
        DefaultTableModel model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("ID");
        model.addColumn("類型");
        model.addColumn("標題");
        model.addColumn("日期");
        model.addColumn("醫院");
        model.addColumn("醫師");
        model.addColumn("備註");
        return model;
    }

    private void setColumnWidth() {
        if (table.getColumnModel().getColumnCount() == 0) {
            return;
        }
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(95);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(180);
    }

    private void openAddRecordUI() {
        AddMedicalRecordUI ui = new AddMedicalRecordUI(pet);
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

    private static Pet createDemoPet() {
        Pet demo = new Pet();
        demo.setId(1);
        demo.setName("Momo");
        demo.setSpecies("狗");
        demo.setBreed("馬爾濟斯");
        demo.setGender("母");
        demo.setBirthday(Date.valueOf("2020-03-15"));
        demo.setNote("個性活潑，喜歡散步和玩球");
        demo.setPhotopath("");
        return demo;
    }
}
