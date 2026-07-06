package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import entity.Pet;
import util.LoginSession;
import util.ModernUI;

public class PetDetailUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private Pet pet;
    private JPanel contentPane;

    public PetDetailUI() {
        this(createDemoPet());
    }

    public PetDetailUI(Pet pet) {
        this.pet = pet == null ? createDemoPet() : pet;
        initialize();
    }

    private void initialize() {
        setTitle("PetCare 寵物詳細資料");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, ModernUI.FRAME_W, ModernUI.FRAME_H);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = ModernUI.createRoot();
        setContentPane(contentPane);

        createSidebar("我的寵物");
        createTopBar();
        createProfileCard();
        createFeatureCard();
    }

    private void createSidebar(String active) {
        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(ModernUI.SIDEBAR);
        sidebar.setBounds(0, 0, ModernUI.SIDEBAR_W, ModernUI.FRAME_H);
        sidebar.setBorder(new LineBorder(ModernUI.BORDER, 1, false));
        contentPane.add(sidebar);

        sidebar.add(ModernUI.logoMark(58, 34));
        sidebar.add(ModernUI.logoText(30, 88));

        JButton navPet = ModernUI.navButton("", "我的寵物", 170, active.equals("我的寵物"));
        JButton navHealth = ModernUI.navButton("", "健康紀錄", 222, active.equals("健康紀錄"));
        JButton navWeight = ModernUI.navButton("", "體重分析", 274, active.equals("體重分析"));
        JButton navReserve = ModernUI.navButton("", "預約管理", 326, active.equals("預約管理"));
        JButton navNotice = ModernUI.navButton("", "通知中心", 378, active.equals("通知中心"));
        JButton navEmergency = ModernUI.navButton("", "緊急醫療卡", 430, active.equals("緊急醫療卡"));
        JButton navMember = ModernUI.navButton("", "會員資料", 482, active.equals("會員資料"));
        sidebar.add(navPet);
        sidebar.add(navHealth);
        sidebar.add(navWeight);
        sidebar.add(navReserve);
        sidebar.add(navNotice);
        sidebar.add(navEmergency);
        sidebar.add(navMember);

        JButton homeButton = ModernUI.whiteButton("返回首頁", 30, 535, 130, 42);
        JButton logoutButton = ModernUI.whiteButton("登出", 30, 585, 130, 42);
        sidebar.add(homeButton);
        sidebar.add(logoutButton);

        homeButton.addActionListener(e -> backToHome());
        navPet.addActionListener(e -> backToHome());
        navHealth.addActionListener(e -> openMedicalRecords());
        navWeight.addActionListener(e -> openWeightRecords());
        navReserve.addActionListener(e -> openReminder());
        navNotice.addActionListener(e -> openReminder());
        navEmergency.addActionListener(e -> openEmergencySummary());
        navMember.addActionListener(e -> showMemberInfo());
        logoutButton.addActionListener(e -> {
            LoginSession.loginMember = null;
            LoginUI login = new LoginUI();
            login.setVisible(true);
            dispose();
        });
    }

    private void createTopBar() {
        JLabel breadcrumb = new JLabel("我的寵物 / " + ModernUI.safe(pet.getName()));
        breadcrumb.setFont(ModernUI.SMALL_FONT);
        breadcrumb.setForeground(ModernUI.TEXT_GRAY);
        breadcrumb.setBounds(250, 54, 340, 26);
        contentPane.add(breadcrumb);

        JLabel userInfo = new JLabel("目前登入者：" + getLoginNameForDisplay());
        userInfo.setFont(ModernUI.SMALL_FONT);
        userInfo.setForeground(ModernUI.TEXT_GRAY);
        userInfo.setHorizontalAlignment(SwingConstants.RIGHT);
        userInfo.setBounds(800, 54, 180, 26);
        contentPane.add(userInfo);

        JButton homeButton = ModernUI.whiteButton("返回首頁", 250, 105, 110, 42);
        JButton editButton = ModernUI.whiteButton("編輯", 735, 105, 100, 42);
        JButton addRecordButton = ModernUI.blackButton("新增紀錄", 850, 105, 130, 42);
        contentPane.add(homeButton);
        contentPane.add(editButton);
        contentPane.add(addRecordButton);

        homeButton.addActionListener(e -> backToHome());

        editButton.addActionListener(e -> {
            EditPetUI editPetUI = new EditPetUI(pet);
            editPetUI.setVisible(true);
            dispose();
        });
        addRecordButton.addActionListener(e -> {
            AddMedicalRecordUI ui = new AddMedicalRecordUI(pet);
            ui.setVisible(true);
            dispose();
        });
    }

    private void createProfileCard() {
        JPanel card = ModernUI.createCard(250, 160, 735, 205);
        contentPane.add(card);

        JLabel imageLabel = new JLabel(ModernUI.petImage(pet.getPhotopath(), 170, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        imageLabel.setBounds(30, 25, 170, 155);
        card.add(imageLabel);

        JLabel nameLabel = new JLabel(ModernUI.safe(pet.getName()));
        nameLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 28));
        nameLabel.setForeground(ModernUI.BLACK);
        nameLabel.setBounds(235, 24, 260, 40);
        card.add(nameLabel);

        String breed = ModernUI.safe(pet.getBreed());
        String species = ModernUI.safe(pet.getSpecies());
        String gender = ModernUI.safe(pet.getGender());
        JLabel subLabel = new JLabel(breed + " / " + species + " / " + gender);
        subLabel.setFont(ModernUI.NORMAL_FONT);
        subLabel.setForeground(ModernUI.TEXT_GRAY);
        subLabel.setBounds(237, 64, 360, 28);
        card.add(subLabel);

        String birthday = pet.getBirthday() == null ? "未填寫" : pet.getBirthday().toString();
        card.add(infoRow("種類", species, 237, 104));
        card.add(infoRow("性別", gender, 237, 136));
        card.add(infoRow("生日", birthday, 440, 104));
        card.add(infoRow("備註", ModernUI.safe(pet.getNote()), 440, 136));

        JLabel hintLabel = new JLabel("下方可管理健康紀錄、疫苗、體重、提醒、相簿與醫療文件");
        hintLabel.setFont(ModernUI.SMALL_FONT);
        hintLabel.setForeground(ModernUI.TEXT_GRAY);
        hintLabel.setBounds(237, 168, 430, 24);
        card.add(hintLabel);
    }

    private JLabel infoRow(String title, String value, int x, int y) {
        JLabel label = new JLabel(title + "    " + value);
        label.setFont(ModernUI.NORMAL_FONT);
        label.setForeground(ModernUI.BLACK);
        label.setBounds(x, y, 260, 26);
        return label;
    }

    private void createFeatureCard() {
        JPanel card = ModernUI.createCard(250, 390, 735, 210);
        contentPane.add(card);

        JLabel title = new JLabel("健康管理");
        title.setFont(ModernUI.SECTION_FONT);
        title.setForeground(ModernUI.BLACK);
        title.setBounds(30, 20, 160, 30);
        card.add(title);

        JLabel subtitle = new JLabel("選擇要管理的項目");
        subtitle.setFont(ModernUI.SMALL_FONT);
        subtitle.setForeground(ModernUI.TEXT_GRAY);
        subtitle.setBounds(32, 48, 220, 24);
        card.add(subtitle);

        JButton medicalButton = featureButton("健康紀錄", 30, 85);
        JButton vaccineButton = featureButton("疫苗紀錄", 200, 85);
        JButton weightButton = featureButton("體重分析", 370, 85);
        JButton reminderButton = featureButton("提醒事項", 540, 85);
        JButton photoButton = featureButton("成長相簿", 30, 140);
        JButton fileButton = featureButton("醫療文件", 200, 140);
        JButton emergencyButton = featureButton("緊急醫療卡", 370, 140);

        card.add(medicalButton);
        card.add(vaccineButton);
        card.add(weightButton);
        card.add(reminderButton);
        card.add(photoButton);
        card.add(fileButton);
        card.add(emergencyButton);

        medicalButton.addActionListener(e -> openMedicalRecords());
        vaccineButton.addActionListener(e -> openVaccineRecords());
        weightButton.addActionListener(e -> openWeightRecords());
        reminderButton.addActionListener(e -> openReminder());
        photoButton.addActionListener(e -> openPhotoAlbum());
        fileButton.addActionListener(e -> openMedicalFiles());
        emergencyButton.addActionListener(e -> openEmergencySummary());
    }

    private JButton featureButton(String text, int x, int y) {
        JButton button = ModernUI.whiteButton(text, x, y, 145, 38);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }

    private void openMedicalRecords() {
        MedicalRecordListUI ui = new MedicalRecordListUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void openVaccineRecords() {
        VaccineRecordListUI ui = new VaccineRecordListUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void openWeightRecords() {
        WeightRecordListUI ui = new WeightRecordListUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void openReminder() {
        ReminderListUI ui = new ReminderListUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void openPhotoAlbum() {
        PetPhotoListUI ui = new PetPhotoListUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void openMedicalFiles() {
        MedicalFileListUI ui = new MedicalFileListUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void openEmergencySummary() {
        EmergencySummaryUI ui = new EmergencySummaryUI(pet);
        ui.setVisible(true);
        dispose();
    }

    private void backToHome() {
        PetHomeUI home = new PetHomeUI();
        home.setVisible(true);
        dispose();
    }

    private void showMemberInfo() {
        if (LoginSession.loginMember == null) {
            JOptionPane.showMessageDialog(null, "目前沒有登入資料");
        } else {
            JOptionPane.showMessageDialog(null,
                    "目前登入者：" + LoginSession.loginMember.getName()
                    + "\n帳號：" + LoginSession.loginMember.getUsername()
                    + "\n角色：" + LoginSession.loginMember.getRole());
        }
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
        demo.setNote("個性活潑，喜歡散步和玩球");
        demo.setPhotopath("");
        return demo;
    }
}
