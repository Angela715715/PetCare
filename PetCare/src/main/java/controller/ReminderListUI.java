package controller;

import java.awt.Color;
import java.awt.Font;
import java.sql.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import entity.Pet;
import entity.Reminder;
import service.ReminderService;
import service.impl.ReminderServiceImpl;
import util.ModernUI;
import util.CrudHelper;

public class ReminderListUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Pet pet;
    private JPanel contentPane;
    private JTable table;

    public ReminderListUI() {
        this(createDemoPet());
    }

    public ReminderListUI(Pet pet) {
        this.pet = pet;
        initialize();
        loadData();
    }

    private void initialize() {
        setTitle("PetCare 提醒事項");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1040, 680);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel(null);
        contentPane.setBackground(ModernUI.CANVAS);
        setContentPane(contentPane);

        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(ModernUI.SIDEBAR);
        sidebar.setBounds(0, 0, 170, 680);
        contentPane.add(sidebar);

        JLabel logoLabel = new JLabel("PetCare");
        logoLabel.setFont(ModernUI.LOGO_FONT);
        logoLabel.setForeground(ModernUI.BLACK);
        logoLabel.setBounds(24, 28, 130, 30);
        sidebar.add(logoLabel);

        JLabel menuLabel = new JLabel("提醒事項");
        menuLabel.setFont(ModernUI.BUTTON_FONT);
        menuLabel.setForeground(ModernUI.BLACK);
        menuLabel.setBounds(34, 110, 110, 28);
        sidebar.add(menuLabel);

        JButton homeButton = ModernUI.whiteButton("返回首頁", 24, 550, 120, 36);
        sidebar.add(homeButton);

        JButton topBackButton = ModernUI.whiteButton("返回", 220, 42, 96, 36);
        contentPane.add(topBackButton);

        JLabel breadcrumbLabel = new JLabel("我的寵物 / 寵物詳細 / 提醒事項");
        breadcrumbLabel.setFont(ModernUI.SMALL_FONT);
        breadcrumbLabel.setForeground(ModernUI.TEXT_GRAY);
        breadcrumbLabel.setBounds(330, 48, 360, 25);
        contentPane.add(breadcrumbLabel);

        JPanel listCard = ModernUI.createCard(220, 95, 775, 520);
        contentPane.add(listCard);

        JLabel titleLabel = new JLabel("提醒事項");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setForeground(ModernUI.BLACK);
        titleLabel.setBounds(34, 28, 250, 38);
        listCard.add(titleLabel);

        JLabel subtitleLabel = new JLabel("寵物：" + ModernUI.safe(pet.getName()) + " 的提醒事項");
        subtitleLabel.setFont(ModernUI.NORMAL_FONT);
        subtitleLabel.setForeground(ModernUI.TEXT_GRAY);
        subtitleLabel.setBounds(36, 66, 430, 25);
        listCard.add(subtitleLabel);

        JButton addButton = ModernUI.blackButton("新增", 610, 40, 120, 38);
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

        JButton refreshButton = ModernUI.whiteButton("重新整理", 36, 458, 110, 38);
        listCard.add(refreshButton);

        JButton editButton = ModernUI.whiteButton("編輯選取", 160, 458, 110, 38);
        listCard.add(editButton);

        JButton deleteButton = ModernUI.whiteButton("刪除選取", 284, 458, 110, 38);
        listCard.add(deleteButton);

        JButton backButton = ModernUI.whiteButton("返回詳細頁", 610, 458, 120, 38);
        listCard.add(backButton);

        homeButton.addActionListener(e -> backToHome());
        topBackButton.addActionListener(e -> backToDetail());
        backButton.addActionListener(e -> backToDetail());
        addButton.addActionListener(e -> openAddUI());
        refreshButton.addActionListener(e -> loadData());
        editButton.addActionListener(e -> { int id = CrudHelper.selectedId(table); if (id > 0 && CrudHelper.edit("reminders", id)) loadData(); });
        deleteButton.addActionListener(e -> deleteSelected());
    }

    private DefaultTableModel createModel() {
        DefaultTableModel model = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        model.addColumn("ID");
        model.addColumn("標題");
        model.addColumn("日期");
        model.addColumn("時間");
        model.addColumn("狀態");
        model.addColumn("備註");
        return model;
    }

    private void loadData() {
        ReminderService service = new ReminderServiceImpl();
        List<Reminder> records = service.findByPetId(pet.getId());
        DefaultTableModel model = createModel();
        for (Reminder r : records) {
            model.addRow(new Object[] { r.getId(), r.getTitle(), r.getRemindDate(), r.getRemindTime(), r.getStatus(), r.getNote() });
        }
        table.setModel(model);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "請先選擇要刪除的資料");
            return;
        }
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(null, "確定要刪除這筆資料嗎？", "確認刪除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ReminderService service = new ReminderServiceImpl();
            service.deleteRecord(id);
            loadData();
        }
    }

    private void openAddUI() {
        AddReminderUI ui = new AddReminderUI(pet);
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
        demo.setNote("WindowBuilder 預覽資料");
        demo.setPhotopath("");
        return demo;
    }
}
