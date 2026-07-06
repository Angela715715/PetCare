package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import entity.MedicalFile;
import entity.Pet;
import service.MedicalFileService;
import service.impl.MedicalFileServiceImpl;
import util.ModernUI;
import util.DatePickerField;

public class AddMedicalFileUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Pet pet;
    private JTextField fileTypeField;
    private DatePickerField uploadDateField;
    private JTextField noteField;
    private JLabel fileLabel;
    private String selectedFilePath = "";
    private String selectedFileName = "";

    public AddMedicalFileUI() { this(createDemoPet()); }
    public AddMedicalFileUI(Pet pet) { this.pet = pet; initialize(); }

    private void initialize() {
        setTitle("PetCare 新增醫療文件");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1040, 680);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel contentPane = new JPanel(null);
        contentPane.setBackground(ModernUI.CANVAS);
        setContentPane(contentPane);

        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(ModernUI.SIDEBAR);
        sidebar.setBounds(0, 0, 170, 680);
        contentPane.add(sidebar);
        JLabel logoLabel = new JLabel("PetCare");
        logoLabel.setFont(ModernUI.LOGO_FONT);
        logoLabel.setBounds(24, 28, 130, 30);
        sidebar.add(logoLabel);
        JButton homeButton = ModernUI.whiteButton("返回首頁", 24, 550, 120, 36);
        sidebar.add(homeButton);

        JButton topBackButton = ModernUI.whiteButton("返回", 220, 42, 96, 36);
        contentPane.add(topBackButton);
        contentPane.add(ModernUI.smallGray("我的寵物 / 寵物詳細 / 新增醫療文件", 330, 48, 390, 25));

        JPanel card = ModernUI.createCard(220, 95, 775, 520);
        contentPane.add(card);
        JLabel titleLabel = new JLabel("新增醫療文件");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setBounds(34, 28, 300, 38);
        card.add(titleLabel);
        card.add(ModernUI.smallGray("上傳檢驗報告、處方、收據或其他醫療檔案", 36, 66, 420, 25));

        JButton chooseButton = ModernUI.whiteButton("選擇檔案", 70, 130, 120, 38);
        card.add(chooseButton);
        fileLabel = ModernUI.smallGray("尚未選擇檔案", 205, 137, 460, 25);
        card.add(fileLabel);
        card.add(ModernUI.label("文件類型", 70, 210, 160, 24));
        fileTypeField = ModernUI.textField(70, 240, 250, 38);
        fileTypeField.setText("檢驗報告");
        card.add(fileTypeField);
        card.add(ModernUI.label("上傳日期", 385, 210, 220, 24));
        uploadDateField = new DatePickerField(385, 240, 250, 38);
        uploadDateField.setText(LocalDate.now().toString());
        card.add(uploadDateField);
        card.add(ModernUI.label("備註", 70, 315, 160, 24));
        noteField = ModernUI.textField(70, 345, 565, 38);
        card.add(noteField);
        JButton cancelButton = ModernUI.whiteButton("取消", 455, 445, 110, 38);
        card.add(cancelButton);
        JButton saveButton = ModernUI.blackButton("儲存", 585, 445, 110, 38);
        card.add(saveButton);

        chooseButton.addActionListener(e -> chooseFile());
        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> backToList());
        topBackButton.addActionListener(e -> backToList());
        homeButton.addActionListener(e -> backToHome());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            selectedFilePath = file.getAbsolutePath();
            selectedFileName = file.getName();
            fileLabel.setText(selectedFileName);
        }
    }

    private String copyFile(String sourcePath) throws IOException {
        if (sourcePath == null || sourcePath.trim().isEmpty()) return "";
        Path uploadDir = Paths.get("uploads", "medical_files");
        Files.createDirectories(uploadDir);
        Path source = Paths.get(sourcePath);
        String fileName = System.currentTimeMillis() + "_" + source.getFileName().toString();
        Path target = uploadDir.resolve(fileName);
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    private void save() {
        try {
            if (selectedFilePath.trim().isEmpty()) throw new IllegalArgumentException("請選擇檔案");
            MedicalFile file = new MedicalFile();
            file.setPetId(pet.getId());
            file.setFileName(selectedFileName);
            file.setFilePath(copyFile(selectedFilePath));
            file.setFileType(fileTypeField.getText().trim());
            file.setUploadDate(Date.valueOf(uploadDateField.getText().trim()));
            file.setNote(noteField.getText().trim());
            MedicalFileService service = new MedicalFileServiceImpl();
            service.addRecord(file);
            JOptionPane.showMessageDialog(null, "新增成功");
            backToList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "新增失敗：" + e.getMessage());
        }
    }

    private void backToList() { new MedicalFileListUI(pet).setVisible(true); dispose(); }
    private void backToHome() { new PetHomeUI().setVisible(true); dispose(); }
    private static Pet createDemoPet() { Pet p = new Pet(); p.setId(1); p.setName("Momo"); return p; }
}
