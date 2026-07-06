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

import entity.Pet;
import entity.PetPhoto;
import service.PetPhotoService;
import service.impl.PetPhotoServiceImpl;
import util.ModernUI;
import util.DatePickerField;

public class AddPetPhotoUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Pet pet;
    private JTextField titleField;
    private DatePickerField photoDateField;
    private JTextField noteField;
    private JLabel fileLabel;
    private String selectedPhotoPath = "";

    public AddPetPhotoUI() { this(createDemoPet()); }
    public AddPetPhotoUI(Pet pet) { this.pet = pet; initialize(); }

    private void initialize() {
        setTitle("PetCare 新增成長照片");
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
        JLabel breadcrumbLabel = ModernUI.smallGray("我的寵物 / 寵物詳細 / 新增照片", 330, 48, 360, 25);
        contentPane.add(breadcrumbLabel);

        JPanel card = ModernUI.createCard(220, 95, 775, 520);
        contentPane.add(card);
        JLabel titleLabel = new JLabel("新增成長照片");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setBounds(34, 28, 300, 38);
        card.add(titleLabel);
        card.add(ModernUI.smallGray("為 " + ModernUI.safe(pet.getName()) + " 上傳生活照片", 36, 66, 360, 25));

        card.add(ModernUI.label("照片標題 *", 70, 125, 160, 24));
        titleField = ModernUI.textField(70, 155, 250, 38);
        card.add(titleField);
        card.add(ModernUI.label("照片日期", 385, 125, 220, 24));
        photoDateField = new DatePickerField(385, 155, 250, 38);
        photoDateField.setText(LocalDate.now().toString());
        card.add(photoDateField);
        card.add(ModernUI.label("備註", 70, 220, 160, 24));
        noteField = ModernUI.textField(70, 250, 565, 38);
        card.add(noteField);
        JButton chooseButton = ModernUI.whiteButton("選擇照片", 70, 320, 120, 38);
        card.add(chooseButton);
        fileLabel = ModernUI.smallGray("尚未選擇照片", 205, 327, 460, 25);
        card.add(fileLabel);
        JButton cancelButton = ModernUI.whiteButton("取消", 455, 445, 110, 38);
        card.add(cancelButton);
        JButton saveButton = ModernUI.blackButton("儲存", 585, 445, 110, 38);
        card.add(saveButton);

        chooseButton.addActionListener(e -> choosePhoto());
        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> backToList());
        topBackButton.addActionListener(e -> backToList());
        homeButton.addActionListener(e -> backToHome());
    }

    private void choosePhoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("圖片檔", "jpg", "jpeg", "png", "gif"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            selectedPhotoPath = file.getAbsolutePath();
            fileLabel.setText(file.getName());
        }
    }

    private String copyPhoto(String sourcePath) throws IOException {
        if (sourcePath == null || sourcePath.trim().isEmpty()) return "";
        Path uploadDir = Paths.get("uploads", "pet_album");
        Files.createDirectories(uploadDir);
        Path source = Paths.get(sourcePath);
        String fileName = System.currentTimeMillis() + "_" + source.getFileName().toString();
        Path target = uploadDir.resolve(fileName);
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    private void save() {
        try {
            if (titleField.getText().trim().isEmpty()) throw new IllegalArgumentException("請輸入照片標題");
            if (selectedPhotoPath.trim().isEmpty()) throw new IllegalArgumentException("請選擇照片");
            PetPhoto photo = new PetPhoto();
            photo.setPetId(pet.getId());
            photo.setTitle(titleField.getText().trim());
            photo.setPhotoPath(copyPhoto(selectedPhotoPath));
            photo.setPhotoDate(Date.valueOf(photoDateField.getText().trim()));
            photo.setNote(noteField.getText().trim());
            PetPhotoService service = new PetPhotoServiceImpl();
            service.addRecord(photo);
            JOptionPane.showMessageDialog(null, "新增成功");
            backToList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "新增失敗：" + e.getMessage());
        }
    }

    private void backToList() { new PetPhotoListUI(pet).setVisible(true); dispose(); }
    private void backToHome() { new PetHomeUI().setVisible(true); dispose(); }
    private static Pet createDemoPet() { Pet p = new Pet(); p.setId(1); p.setName("Momo"); return p; }
}
