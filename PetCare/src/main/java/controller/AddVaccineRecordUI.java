package controller;

import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import entity.Pet;
import entity.VaccineRecord;
import service.VaccineRecordService;
import service.impl.VaccineRecordServiceImpl;
import util.ModernUI;
import util.DatePickerField;

public class AddVaccineRecordUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Pet pet;
    private JPanel contentPane;
    private JTextField vaccineNameField;
    private DatePickerField vaccineDateField;
    private DatePickerField nextDateField;
    private JTextField hospitalField;
    private JTextField noteField;

    public AddVaccineRecordUI() {
        this(createDemoPet());
    }

    public AddVaccineRecordUI(Pet pet) {
        this.pet = pet;
        initialize();
    }

    private void initialize() {
        setTitle("PetCare 新增疫苗紀錄");
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

        JButton homeButton = ModernUI.whiteButton("返回首頁", 24, 550, 120, 36);
        sidebar.add(homeButton);

        JButton topBackButton = ModernUI.whiteButton("返回", 220, 42, 96, 36);
        contentPane.add(topBackButton);

        JLabel breadcrumbLabel = new JLabel("我的寵物 / 寵物詳細 / 新增疫苗紀錄");
        breadcrumbLabel.setFont(ModernUI.SMALL_FONT);
        breadcrumbLabel.setForeground(ModernUI.TEXT_GRAY);
        breadcrumbLabel.setBounds(330, 48, 360, 25);
        contentPane.add(breadcrumbLabel);

        JPanel card = ModernUI.createCard(220, 95, 775, 520);
        contentPane.add(card);

        JLabel titleLabel = new JLabel("新增疫苗紀錄");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setForeground(ModernUI.BLACK);
        titleLabel.setBounds(34, 28, 300, 38);
        card.add(titleLabel);

        JLabel subtitleLabel = new JLabel("為 " + ModernUI.safe(pet.getName()) + " 新增資料");
        subtitleLabel.setFont(ModernUI.NORMAL_FONT);
        subtitleLabel.setForeground(ModernUI.TEXT_GRAY);
        subtitleLabel.setBounds(36, 66, 360, 25);
        card.add(subtitleLabel);

        JLabel vaccineNameLabel = new JLabel("疫苗名稱 *");
        vaccineNameLabel.setFont(ModernUI.BUTTON_FONT);
        vaccineNameLabel.setForeground(ModernUI.BLACK);
        vaccineNameLabel.setBounds(70, 125, 180, 24);
        card.add(vaccineNameLabel);

        vaccineNameField = ModernUI.textField(70, 155, 250, 38);
        card.add(vaccineNameField);
        JLabel vaccineDateLabel = new JLabel("施打日期 *");
        vaccineDateLabel.setFont(ModernUI.BUTTON_FONT);
        vaccineDateLabel.setForeground(ModernUI.BLACK);
        vaccineDateLabel.setBounds(385, 125, 180, 24);
        card.add(vaccineDateLabel);

        vaccineDateField = new DatePickerField(385, 155, 250, 38);
        card.add(vaccineDateField);
        JLabel nextDateLabel = new JLabel("下次日期");
        nextDateLabel.setFont(ModernUI.BUTTON_FONT);
        nextDateLabel.setForeground(ModernUI.BLACK);
        nextDateLabel.setBounds(70, 220, 180, 24);
        card.add(nextDateLabel);

        nextDateField = new DatePickerField(70, 250, 250, 38);
        card.add(nextDateField);
        JLabel hospitalLabel = new JLabel("醫院");
        hospitalLabel.setFont(ModernUI.BUTTON_FONT);
        hospitalLabel.setForeground(ModernUI.BLACK);
        hospitalLabel.setBounds(385, 220, 180, 24);
        card.add(hospitalLabel);

        hospitalField = ModernUI.textField(385, 250, 250, 38);
        card.add(hospitalField);
        JLabel noteLabel = new JLabel("備註");
        noteLabel.setFont(ModernUI.BUTTON_FONT);
        noteLabel.setForeground(ModernUI.BLACK);
        noteLabel.setBounds(70, 315, 180, 24);
        card.add(noteLabel);

        noteField = ModernUI.textField(70, 345, 595, 38);
        card.add(noteField);


        JButton cancelButton = ModernUI.whiteButton("取消", 455, 445, 110, 38);
        card.add(cancelButton);

        JButton saveButton = ModernUI.blackButton("儲存", 585, 445, 110, 38);
        card.add(saveButton);

        homeButton.addActionListener(e -> backToHome());
        topBackButton.addActionListener(e -> backToList());
        cancelButton.addActionListener(e -> backToList());
        saveButton.addActionListener(e -> save());
    }

    private Date parseDate(String value, boolean required) {
        if (value == null || value.trim().isEmpty()) {
            if (required) throw new IllegalArgumentException("請選擇日期");
            return null;
        }
        return Date.valueOf(value.trim());
    }

    private void save() {
        try {
            VaccineRecord record = new VaccineRecord();
            record.setPetId(pet.getId());
            record.setVaccineName(vaccineNameField.getText().trim());
            record.setVaccineDate(parseDate(vaccineDateField.getText(), true));
            record.setNextDate(parseDate(nextDateField.getText(), false));
            record.setHospital(hospitalField.getText().trim());
            record.setNote(noteField.getText().trim());
            if (record.getVaccineName().isEmpty()) throw new IllegalArgumentException("請輸入疫苗名稱");
            VaccineRecordService service = new VaccineRecordServiceImpl();
            service.addRecord(record);
            JOptionPane.showMessageDialog(null, "新增成功");
            backToList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "資料格式錯誤：" + e.getMessage());
        }
    }

    private void backToList() {
        VaccineRecordListUI ui = new VaccineRecordListUI(pet);
        ui.setVisible(true);
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
        demo.setPhotopath("");
        return demo;
    }
}
