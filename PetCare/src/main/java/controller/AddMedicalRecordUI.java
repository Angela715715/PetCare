package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.sql.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import entity.MedicalRecord;
import entity.Pet;
import service.MedicalRecordService;
import service.impl.MedicalRecordServiceImpl;
import util.ModernUI;
import util.DatePickerField;

public class AddMedicalRecordUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private Pet pet;
    private JPanel contentPane;
    private JPanel sidebar;
    private JPanel formCard;

    private JLabel logoLabel;
    private JLabel breadcrumbLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel typeLabel;
    private JLabel titleFieldLabel;
    private JLabel dateLabel;
    private JLabel hospitalLabel;
    private JLabel doctorLabel;
    private JLabel noteLabel;

    private JButton sidebarBackButton;
    private JButton topBackButton;
    private JButton cancelButton;
    private JButton saveButton;

    private JComboBox<String> typeBox;
    private JTextField titleField;
    private DatePickerField dateField;
    private JTextField hospitalField;
    private JTextField doctorField;
    private JTextField noteField;

    // 給 WindowBuilder 預覽用
    public AddMedicalRecordUI() {
        this.pet = createDemoPet();
        initialize();
    }

    // 實際使用：從 PetDetailUI 或 MedicalRecordListUI 傳入 pet
    public AddMedicalRecordUI(Pet pet) {
        this.pet = pet;
        initialize();
    }

    private void initialize() {
        setTitle("PetCare 新增健康紀錄");
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

        JLabel menuLabel = new JLabel("新增紀錄");
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

        breadcrumbLabel = new JLabel("我的寵物 / 寵物詳細 / 新增健康紀錄");
        breadcrumbLabel.setFont(ModernUI.SMALL_FONT);
        breadcrumbLabel.setForeground(ModernUI.TEXT_GRAY);
        breadcrumbLabel.setBounds(330, 48, 360, 25);
        contentPane.add(breadcrumbLabel);

        formCard = new JPanel();
        formCard.setBackground(ModernUI.CARD);
        formCard.setBounds(220, 95, 775, 520);
        formCard.setLayout(null);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        contentPane.add(formCard);

        titleLabel = new JLabel("新增健康紀錄");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setForeground(ModernUI.BLACK);
        titleLabel.setBounds(34, 28, 250, 38);
        formCard.add(titleLabel);

        subtitleLabel = new JLabel("為 " + ModernUI.safe(pet.getName()) + " 新增看診、疫苗或照護紀錄");
        subtitleLabel.setFont(ModernUI.NORMAL_FONT);
        subtitleLabel.setForeground(ModernUI.TEXT_GRAY);
        subtitleLabel.setBounds(36, 66, 420, 25);
        formCard.add(subtitleLabel);

        typeLabel = new JLabel("紀錄類型 *");
        typeLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        typeLabel.setForeground(ModernUI.BLACK);
        typeLabel.setBounds(70, 125, 150, 24);
        formCard.add(typeLabel);

        typeBox = new JComboBox<>(new String[] { "看診", "疫苗", "驅蟲", "檢查", "用藥", "其他" });
        typeBox.setFont(ModernUI.NORMAL_FONT);
        typeBox.setBounds(70, 155, 250, 38);
        typeBox.setBackground(ModernUI.CARD);
        typeBox.setForeground(ModernUI.BLACK);
        typeBox.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        formCard.add(typeBox);

        titleFieldLabel = new JLabel("紀錄標題 *");
        titleFieldLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        titleFieldLabel.setForeground(ModernUI.BLACK);
        titleFieldLabel.setBounds(385, 125, 150, 24);
        formCard.add(titleFieldLabel);

        titleField = new JTextField();
        titleField.setFont(ModernUI.NORMAL_FONT);
        titleField.setBounds(385, 155, 280, 38);
        titleField.setBackground(ModernUI.CARD);
        titleField.setForeground(ModernUI.BLACK);
        titleField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        titleField.setToolTipText("例如：狂犬病疫苗、腸胃不適");
        formCard.add(titleField);
        titleField.setColumns(10);

        dateLabel = new JLabel("日期 *");
        dateLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        dateLabel.setForeground(ModernUI.BLACK);
        dateLabel.setBounds(70, 220, 150, 24);
        formCard.add(dateLabel);

        dateField = new DatePickerField(70, 250, 250, 38);
        formCard.add(dateField);

        hospitalLabel = new JLabel("醫院名稱");
        hospitalLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        hospitalLabel.setForeground(ModernUI.BLACK);
        hospitalLabel.setBounds(385, 220, 150, 24);
        formCard.add(hospitalLabel);

        hospitalField = new JTextField();
        hospitalField.setFont(ModernUI.NORMAL_FONT);
        hospitalField.setBounds(385, 250, 280, 38);
        hospitalField.setBackground(ModernUI.CARD);
        hospitalField.setForeground(ModernUI.BLACK);
        hospitalField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        formCard.add(hospitalField);
        hospitalField.setColumns(10);

        doctorLabel = new JLabel("醫師名稱");
        doctorLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        doctorLabel.setForeground(ModernUI.BLACK);
        doctorLabel.setBounds(70, 315, 150, 24);
        formCard.add(doctorLabel);

        doctorField = new JTextField();
        doctorField.setFont(ModernUI.NORMAL_FONT);
        doctorField.setBounds(70, 345, 250, 38);
        doctorField.setBackground(ModernUI.CARD);
        doctorField.setForeground(ModernUI.BLACK);
        doctorField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        formCard.add(doctorField);
        doctorField.setColumns(10);

        noteLabel = new JLabel("備註");
        noteLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        noteLabel.setForeground(ModernUI.BLACK);
        noteLabel.setBounds(385, 315, 150, 24);
        formCard.add(noteLabel);

        noteField = new JTextField();
        noteField.setFont(ModernUI.NORMAL_FONT);
        noteField.setBounds(385, 345, 280, 38);
        noteField.setBackground(ModernUI.CARD);
        noteField.setForeground(ModernUI.BLACK);
        noteField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        formCard.add(noteField);
        noteField.setColumns(10);

        cancelButton = new JButton("取消");
        cancelButton.setFont(ModernUI.BUTTON_FONT);
        cancelButton.setBounds(440, 445, 105, 38);
        cancelButton.setBackground(ModernUI.CARD);
        cancelButton.setForeground(ModernUI.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        formCard.add(cancelButton);

        saveButton = new JButton("儲存紀錄");
        saveButton.setFont(ModernUI.BUTTON_FONT);
        saveButton.setBounds(565, 445, 120, 38);
        saveButton.setBackground(ModernUI.BLACK);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setBorder(new LineBorder(ModernUI.BLACK, 1, true));
        formCard.add(saveButton);

        topBackButton.addActionListener(e -> backToList());
        sidebarBackButton.addActionListener(e -> backToHome());
        cancelButton.addActionListener(e -> backToList());
        saveButton.addActionListener(e -> saveRecord());
    }

    private void saveRecord() {
        if (pet == null || pet.getId() == 0) {
            JOptionPane.showMessageDialog(null, "寵物資料不正確，請從寵物詳細頁進入");
            return;
        }

        String recordType = typeBox.getSelectedItem().toString();
        String title = titleField.getText().trim();
        String dateText = dateField.getText().trim();
        String hospital = hospitalField.getText().trim();
        String doctor = doctorField.getText().trim();
        String note = noteField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(null, "請輸入紀錄標題");
            return;
        }

        if (dateText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "請選擇日期");
            return;
        }

        Date recordDate;
        try {
            recordDate = Date.valueOf(dateText);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "請從日曆選擇正確日期");
            return;
        }

        MedicalRecord record = new MedicalRecord(
                pet.getId(),
                recordType,
                title,
                recordDate,
                hospital,
                doctor,
                note
        );

        MedicalRecordService service = new MedicalRecordServiceImpl();
        service.addRecord(record);

        JOptionPane.showMessageDialog(null, "新增健康紀錄成功");
        backToList();
    }

    private void backToList() {
        MedicalRecordListUI listUI = new MedicalRecordListUI(pet);
        listUI.setVisible(true);
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
