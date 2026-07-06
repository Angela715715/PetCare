package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import util.DatePickerField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import entity.Pet;
import service.PetService;
import service.impl.PetServiceImpl;
import util.LoginSession;
import util.ModernUI;

public class AddPetUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JPanel sidebar;
    private JPanel formCard;
    private JPanel uploadBox;

    private JLabel logoLabel;
    private JLabel breadcrumbLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel photoPreview;
    private JLabel uploadHintLabel;
    private JLabel photoPathLabel;
    private JLabel nameLabel;
    private JLabel speciesLabel;
    private JLabel breedLabel;
    private JLabel genderLabel;
    private JLabel birthdayLabel;
    private JLabel noteLabel;

    private JButton sidebarBackButton;
    private JButton topBackButton;
    private JButton choosePhotoButton;
    private JButton cancelButton;
    private JButton saveButton;

    private JTextField nameField;
    private JComboBox<String> speciesBox;
    private JTextField breedField;
    private JComboBox<String> genderBox;
    private DatePickerField birthdayField;
    private JTextField noteField;

    private String selectedPhotoPath = "";

    public AddPetUI() {
        initialize();
    }

    private void initialize() {
        setTitle("PetCare 新增寵物");
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

        breadcrumbLabel = new JLabel("我的寵物 / 新增寵物");
        breadcrumbLabel.setFont(ModernUI.SMALL_FONT);
        breadcrumbLabel.setForeground(ModernUI.TEXT_GRAY);
        breadcrumbLabel.setBounds(330, 48, 230, 25);
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

        titleLabel = new JLabel("新增寵物");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setForeground(ModernUI.BLACK);
        titleLabel.setBounds(34, 28, 200, 38);
        formCard.add(titleLabel);

        subtitleLabel = new JLabel("填寫毛孩的基本資料");
        subtitleLabel.setFont(ModernUI.NORMAL_FONT);
        subtitleLabel.setForeground(ModernUI.TEXT_GRAY);
        subtitleLabel.setBounds(36, 66, 220, 25);
        formCard.add(subtitleLabel);

        uploadBox = new JPanel();
        uploadBox.setLayout(null);
        uploadBox.setBackground(ModernUI.SOFT_GRAY);
        uploadBox.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        uploadBox.setBounds(36, 135, 245, 210);
        formCard.add(uploadBox);

        photoPreview = new JLabel("點擊上傳照片");
        photoPreview.setFont(ModernUI.NORMAL_FONT);
        photoPreview.setForeground(ModernUI.BLACK);
        photoPreview.setHorizontalAlignment(JLabel.CENTER);
        photoPreview.setBounds(25, 48, 195, 80);
        uploadBox.add(photoPreview);

        uploadHintLabel = new JLabel("建議上傳清晰的正面照片");
        uploadHintLabel.setFont(ModernUI.SMALL_FONT);
        uploadHintLabel.setForeground(ModernUI.TEXT_GRAY);
        uploadHintLabel.setHorizontalAlignment(JLabel.CENTER);
        uploadHintLabel.setBounds(25, 132, 195, 24);
        uploadBox.add(uploadHintLabel);

        choosePhotoButton = new JButton("選擇照片");
        choosePhotoButton.setFont(ModernUI.BUTTON_FONT);
        choosePhotoButton.setBounds(62, 164, 120, 32);
        choosePhotoButton.setBackground(ModernUI.CARD);
        choosePhotoButton.setForeground(ModernUI.BLACK);
        choosePhotoButton.setFocusPainted(false);
        choosePhotoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        choosePhotoButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        uploadBox.add(choosePhotoButton);

        photoPathLabel = new JLabel("尚未選擇照片");
        photoPathLabel.setFont(ModernUI.SMALL_FONT);
        photoPathLabel.setForeground(ModernUI.TEXT_GRAY);
        photoPathLabel.setHorizontalAlignment(JLabel.CENTER);
        photoPathLabel.setBounds(36, 355, 245, 24);
        formCard.add(photoPathLabel);

        nameLabel = new JLabel("寵物名字 *");
        nameLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        nameLabel.setForeground(ModernUI.BLACK);
        nameLabel.setBounds(320, 124, 150, 24);
        formCard.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(ModernUI.NORMAL_FONT);
        nameField.setBounds(320, 154, 190, 36);
        nameField.setBackground(ModernUI.CARD);
        nameField.setForeground(ModernUI.BLACK);
        nameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        nameField.setToolTipText("例如：Momo");
        formCard.add(nameField);
        nameField.setColumns(10);

        speciesLabel = new JLabel("寵物種類 *");
        speciesLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        speciesLabel.setForeground(ModernUI.BLACK);
        speciesLabel.setBounds(545, 124, 150, 24);
        formCard.add(speciesLabel);

        speciesBox = new JComboBox<>(new String[] { "狗", "貓", "其他" });
        speciesBox.setFont(ModernUI.NORMAL_FONT);
        speciesBox.setBounds(545, 154, 190, 36);
        speciesBox.setBackground(ModernUI.CARD);
        speciesBox.setForeground(ModernUI.BLACK);
        speciesBox.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        formCard.add(speciesBox);

        breedLabel = new JLabel("品種");
        breedLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        breedLabel.setForeground(ModernUI.BLACK);
        breedLabel.setBounds(320, 205, 150, 24);
        formCard.add(breedLabel);

        breedField = new JTextField();
        breedField.setFont(ModernUI.NORMAL_FONT);
        breedField.setBounds(320, 235, 190, 36);
        breedField.setBackground(ModernUI.CARD);
        breedField.setForeground(ModernUI.BLACK);
        breedField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        formCard.add(breedField);
        breedField.setColumns(10);

        genderLabel = new JLabel("性別 *");
        genderLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        genderLabel.setForeground(ModernUI.BLACK);
        genderLabel.setBounds(545, 205, 150, 24);
        formCard.add(genderLabel);

        genderBox = new JComboBox<>(new String[] { "公", "母", "未知" });
        genderBox.setFont(ModernUI.NORMAL_FONT);
        genderBox.setBounds(545, 235, 190, 36);
        genderBox.setBackground(ModernUI.CARD);
        genderBox.setForeground(ModernUI.BLACK);
        genderBox.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        formCard.add(genderBox);

        birthdayLabel = new JLabel("生日");
        birthdayLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        birthdayLabel.setForeground(ModernUI.BLACK);
        birthdayLabel.setBounds(320, 286, 150, 24);
        formCard.add(birthdayLabel);

        birthdayField = new DatePickerField(320, 316, 250, 36);
        formCard.add(birthdayField);

        noteLabel = new JLabel("備註");
        noteLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
        noteLabel.setForeground(ModernUI.BLACK);
        noteLabel.setBounds(320, 367, 150, 24);
        formCard.add(noteLabel);

        noteField = new JTextField();
        noteField.setFont(ModernUI.NORMAL_FONT);
        noteField.setBounds(320, 397, 415, 36);
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
        cancelButton.setBounds(500, 462, 105, 38);
        cancelButton.setBackground(ModernUI.CARD);
        cancelButton.setForeground(ModernUI.BLACK);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        formCard.add(cancelButton);

        saveButton = new JButton("儲存");
        saveButton.setFont(ModernUI.BUTTON_FONT);
        saveButton.setBounds(625, 462, 110, 38);
        saveButton.setBackground(ModernUI.BLACK);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setBorder(new LineBorder(ModernUI.BLACK, 1, true));
        formCard.add(saveButton);

        topBackButton.addActionListener(e -> backToHome());
        sidebarBackButton.addActionListener(e -> backToHome());
        choosePhotoButton.addActionListener(e -> choosePhoto());
        cancelButton.addActionListener(e -> backToHome());
        saveButton.addActionListener(e -> savePet());
    }

    private void choosePhoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("圖片檔案", "jpg", "jpeg", "png", "gif"));

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            selectedPhotoPath = file.getAbsolutePath();
            photoPathLabel.setText(file.getName());
            photoPreview.setText("");
            photoPreview.setIcon(ModernUI.petImage(selectedPhotoPath, 195, 130));
        }
    }

    private void savePet() {
        if (LoginSession.loginMember == null) {
            JOptionPane.showMessageDialog(null, "請先從 LoginUI 登入後再新增寵物");
            return;
        }

        String name = nameField.getText().trim();
        String species = speciesBox.getSelectedItem().toString();
        String breed = breedField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();
        String birthdayText = birthdayField.getText().trim();
        String note = noteField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "請輸入寵物名稱");
            return;
        }

        Date birthday = null;
        if (!birthdayText.isEmpty()) {
            try {
                birthday = Date.valueOf(birthdayText);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "請從日曆選擇正確生日");
                return;
            }
        }

        String photoPathForDb = copyPhotoToUploads(selectedPhotoPath);

        Pet pet = new Pet(
                LoginSession.loginMember.getId(),
                name,
                species,
                breed,
                gender,
                birthday,
                note,
                photoPathForDb
        );

        PetService petService = new PetServiceImpl();
        petService.addPet(pet);

        JOptionPane.showMessageDialog(null, "新增寵物成功");
        backToHome();
    }

    private String copyPhotoToUploads(String sourcePath) {
        if (sourcePath == null || sourcePath.trim().isEmpty()) {
            return "";
        }

        try {
            File sourceFile = new File(sourcePath);
            Path uploadDir = Paths.get("uploads", "pet_photos");
            Files.createDirectories(uploadDir);

            String originalName = sourceFile.getName();
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
            String fileName = timestamp + "_" + originalName;

            Path targetPath = uploadDir.resolve(fileName);
            Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return targetPath.toString().replace("\\", "/");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "照片複製失敗，將只儲存寵物資料");
            return "";
        }
    }

    private void backToHome() {
        PetHomeUI home = new PetHomeUI();
        home.setVisible(true);
        dispose();
    }
}
