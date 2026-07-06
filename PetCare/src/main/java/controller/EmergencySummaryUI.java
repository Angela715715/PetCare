package controller;

import java.sql.Date;
import java.awt.print.PrinterException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import entity.MedicalRecord;
import entity.Pet;
import entity.Reminder;
import entity.VaccineRecord;
import entity.WeightRecord;
import entity.WeightRecord;
import service.MedicalRecordService;
import service.ReminderService;
import service.VaccineRecordService;
import service.WeightRecordService;
import service.impl.MedicalRecordServiceImpl;
import service.impl.ReminderServiceImpl;
import service.impl.VaccineRecordServiceImpl;
import service.impl.WeightRecordServiceImpl;
import util.ModernUI;

public class EmergencySummaryUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Pet pet;
    private JTextArea summaryArea;

    public EmergencySummaryUI() { this(createDemoPet()); }
    public EmergencySummaryUI(Pet pet) { this.pet = pet; initialize(); loadSummary(); }

    private void initialize() {
        setTitle("PetCare 緊急醫療卡");
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
        contentPane.add(ModernUI.smallGray("我的寵物 / 寵物詳細 / 緊急醫療卡", 330, 48, 360, 25));

        JPanel card = ModernUI.createCard(220, 95, 775, 520);
        contentPane.add(card);
        JLabel titleLabel = new JLabel("緊急醫療卡");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setBounds(34, 28, 300, 38);
        card.add(titleLabel);
        card.add(ModernUI.smallGray("緊急送醫時可快速提供給醫師參考，並可直接列印", 36, 66, 500, 25));

        summaryArea = new JTextArea();
        summaryArea.setFont(ModernUI.NORMAL_FONT);
        summaryArea.setForeground(ModernUI.BLACK);
        summaryArea.setBackground(ModernUI.SOFT_GRAY);
        summaryArea.setEditable(false);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setBounds(36, 115, 700, 340);
        card.add(summaryArea);

        JButton refreshButton = ModernUI.whiteButton("重新整理", 36, 468, 110, 38);
        card.add(refreshButton);
        JButton printButton = ModernUI.whiteButton("列印", 160, 468, 90, 38);
        card.add(printButton);
        JButton backButton = ModernUI.blackButton("返回詳細頁", 585, 468, 120, 38);
        card.add(backButton);

        refreshButton.addActionListener(e -> loadSummary());
        printButton.addActionListener(e -> printSummary());
        backButton.addActionListener(e -> backToDetail());
        topBackButton.addActionListener(e -> backToDetail());
        homeButton.addActionListener(e -> backToHome());
    }

    private void loadSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("寵物基本資料\n");
        sb.append("名字：").append(ModernUI.safe(pet.getName())).append("\n");
        sb.append("種類 / 品種：").append(ModernUI.safe(pet.getSpecies())).append(" / ").append(ModernUI.safe(pet.getBreed())).append("\n");
        sb.append("性別：").append(ModernUI.safe(pet.getGender())).append("\n");
        sb.append("生日：").append(pet.getBirthday() == null ? "未填寫" : pet.getBirthday().toString()).append("\n");
        sb.append("備註：").append(ModernUI.safe(pet.getNote())).append("\n\n");

        try {
            WeightRecordService weightService = new WeightRecordServiceImpl();
            List<WeightRecord> weights = weightService.findByPetId(pet.getId());
            sb.append("最新體重\n");
            if (weights.isEmpty()) sb.append("尚無體重紀錄\n");
            else sb.append(weights.get(0).getWeightDate()).append("：").append(weights.get(0).getWeightKg()).append(" kg\n");

            MedicalRecordService medicalService = new MedicalRecordServiceImpl();
            List<MedicalRecord> records = medicalService.findByPetId(pet.getId());
            sb.append("\n最近看診 / 健康紀錄\n");
            for (int i = 0; i < records.size() && i < 3; i++) {
                MedicalRecord r = records.get(i);
                sb.append(r.getRecordDate()).append("/").append(r.getRecordType()).append("/").append(r.getTitle()).append("\n");
            }
            if (records.isEmpty()) sb.append("尚無健康紀錄\n");

            VaccineRecordService vaccineService = new VaccineRecordServiceImpl();
            List<VaccineRecord> vaccines = vaccineService.findByPetId(pet.getId());
            sb.append("\n疫苗紀錄\n");
            if (vaccines.isEmpty()) sb.append("尚無疫苗紀錄\n");
            for (int i = 0; i < vaccines.size() && i < 3; i++) {
                VaccineRecord v = vaccines.get(i);
                sb.append(v.getVaccineDate()).append("/").append(v.getVaccineName()).append("/下次：").append(v.getNextDate()).append("\n");
            }

            ReminderService reminderService = new ReminderServiceImpl();
            List<Reminder> reminders = reminderService.findByPetId(pet.getId());
            sb.append("\n待辦提醒\n");
            int count = 0;
            for (Reminder r : reminders) {
                if (!"已完成".equals(r.getStatus())) {
                    sb.append(r.getRemindDate()).append(" ").append(ModernUI.safe(r.getRemindTime())).append("/").append(r.getTitle()).append("\n");
                    count++;
                }
                if (count >= 3) break;
            }
            if (count == 0) sb.append("目前沒有未完成提醒\n");
        } catch (Exception e) {
            sb.append("\n提醒：部分資料表尚未建立，請先執行 sql/petcare_final_schema.sql\n");
        }

        summaryArea.setText(sb.toString());
    }

    private void printSummary() {
        try {
            boolean printed = summaryArea.print();
            if (!printed) {
                javax.swing.JOptionPane.showMessageDialog(null, "已取消列印");
            }
        } catch (PrinterException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "列印失敗：" + e.getMessage());
        }
    }

    private void backToDetail() { new PetDetailUI(pet).setVisible(true); dispose(); }
    private void backToHome() { new PetHomeUI().setVisible(true); dispose(); }
    private static Pet createDemoPet() { Pet p = new Pet(); p.setId(1); p.setName("Momo"); p.setSpecies("狗"); p.setBreed("馬爾濟斯"); p.setGender("母"); p.setBirthday(Date.valueOf("2020-03-15")); return p; }
}
