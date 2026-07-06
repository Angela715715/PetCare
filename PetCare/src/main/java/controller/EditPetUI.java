package controller;

import java.sql.Date;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entity.Pet;
import service.PetService;
import service.impl.PetServiceImpl;
import util.ModernUI;
import util.DatePickerField;

public class EditPetUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Pet pet;
    private JTextField nameField;
    private JComboBox<String> speciesBox;
    private JTextField breedField;
    private JComboBox<String> genderBox;
    private DatePickerField birthdayField;
    private JTextField noteField;
    private JTextField photoPathField;

    public EditPetUI() { this(createDemoPet()); }

    public EditPetUI(Pet pet) {
        this.pet = pet;
        initialize();
        renderData();
    }

    private void initialize() {
        setTitle("PetCare 編輯寵物");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1040, 680);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel contentPane = new JPanel(null);
        contentPane.setBackground(ModernUI.CANVAS);
        setContentPane(contentPane);

        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(ModernUI.SIDEBAR);
        sidebar.setBounds(0, 0, ModernUI.SIDEBAR_W, ModernUI.FRAME_H);
        contentPane.add(sidebar);
        JLabel logoLabel = new JLabel("PetCare");
        logoLabel.setFont(ModernUI.LOGO_FONT);
        logoLabel.setBounds(24, 28, 130, 30);
        sidebar.add(logoLabel);
        javax.swing.JButton homeButton = ModernUI.whiteButton("返回首頁", 30, 555, 130, 42);
        sidebar.add(homeButton);
        homeButton.addActionListener(e -> backToHome());

        javax.swing.JButton topBackButton = ModernUI.whiteButton("返回", 220, 42, 96, 36);
        contentPane.add(topBackButton);
        contentPane.add(ModernUI.smallGray("我的寵物 / 寵物詳細 / 編輯寵物", 330, 48, 360, 25));

        JPanel card = ModernUI.createCard(220, 90, 775, 520);
        contentPane.add(card);
        JLabel titleLabel = new JLabel("編輯寵物資料");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setBounds(34, 28, 300, 38);
        card.add(titleLabel);
        card.add(ModernUI.smallGray("更新毛孩的基本資料與照片路徑", 36, 66, 360, 25));

        card.add(ModernUI.label("寵物名字 *", 70, 118, 160, 24));
        nameField = ModernUI.textField(70, 148, 250, 38);
        card.add(nameField);
        card.add(ModernUI.label("種類", 385, 118, 160, 24));
        speciesBox = new JComboBox<>(new String[] { "狗", "貓", "其他" });
        ModernUI.styleComboBox(speciesBox, 385, 148, 250, 38);
        card.add(speciesBox);
        card.add(ModernUI.label("品種", 70, 210, 160, 24));
        breedField = ModernUI.textField(70, 240, 250, 38);
        card.add(breedField);
        card.add(ModernUI.label("性別", 385, 210, 160, 24));
        genderBox = new JComboBox<>(new String[] { "公", "母", "未知" });
        ModernUI.styleComboBox(genderBox, 385, 240, 250, 38);
        card.add(genderBox);
        card.add(ModernUI.label("生日", 70, 302, 220, 24));
        birthdayField = new DatePickerField(70, 332, 250, 38);
        card.add(birthdayField);
        card.add(ModernUI.label("照片路徑", 385, 302, 160, 24));
        photoPathField = ModernUI.textField(385, 332, 250, 38);
        card.add(photoPathField);
        card.add(ModernUI.label("備註", 70, 395, 160, 24));
        noteField = ModernUI.textField(70, 425, 565, 38);
        card.add(noteField);

        javax.swing.JButton cancelButton = ModernUI.whiteButton("取消", 495, 475, 110, 38);
        card.add(cancelButton);
        javax.swing.JButton saveButton = ModernUI.blackButton("儲存", 625, 475, 110, 38);
        card.add(saveButton);

        topBackButton.addActionListener(e -> backToDetail());
        cancelButton.addActionListener(e -> backToDetail());
        saveButton.addActionListener(e -> save());
    }

    private void renderData() {
        nameField.setText(ModernUI.safe(pet.getName()).equals("未填寫") ? "" : pet.getName());
        speciesBox.setSelectedItem(pet.getSpecies() == null ? "狗" : pet.getSpecies());
        breedField.setText(pet.getBreed() == null ? "" : pet.getBreed());
        genderBox.setSelectedItem(pet.getGender() == null ? "未知" : pet.getGender());
        birthdayField.setText(pet.getBirthday() == null ? "" : pet.getBirthday().toString());
        noteField.setText(pet.getNote() == null ? "" : pet.getNote());
        photoPathField.setText(pet.getPhotopath() == null ? "" : pet.getPhotopath());
    }

    private void save() {
        try {
            if (nameField.getText().trim().isEmpty()) throw new IllegalArgumentException("請輸入寵物名字");
            pet.setName(nameField.getText().trim());
            pet.setSpecies(speciesBox.getSelectedItem().toString());
            pet.setBreed(breedField.getText().trim());
            pet.setGender(genderBox.getSelectedItem().toString());
            if (birthdayField.getText().trim().isEmpty()) pet.setBirthday(null); else pet.setBirthday(Date.valueOf(birthdayField.getText().trim()));
            pet.setNote(noteField.getText().trim());
            pet.setPhotopath(photoPathField.getText().trim());
            PetService service = new PetServiceImpl();
            service.updatePet(pet);
            JOptionPane.showMessageDialog(null, "更新成功");
            backToDetail();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "更新失敗：" + e.getMessage());
        }
    }

    private void backToDetail() { new PetDetailUI(pet).setVisible(true); dispose(); }
    private void backToHome() { new PetHomeUI().setVisible(true); dispose(); }
    private static Pet createDemoPet() { Pet p = new Pet(); p.setId(1); p.setName("Momo"); p.setSpecies("狗"); p.setBreed("馬爾濟斯"); p.setGender("母"); p.setBirthday(Date.valueOf("2020-03-15")); return p; }
}
