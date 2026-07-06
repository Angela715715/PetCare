package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import util.LoginSession;
import util.ModernUI;

public class MainUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JPanel sidebar;
    private JPanel dashboardCard;
    private JPanel featurePetCard;
    private JPanel featureMemberCard;
    private JPanel featureHealthCard;
    private JPanel featureReminderCard;
    private JPanel featureFileCard;
    private JPanel featureSettingCard;

    private JLabel logoLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel userInfoLabel;
    private JLabel sectionTitleLabel;
    private JLabel sectionHintLabel;

    private JButton logoutButton;
    private JButton petButton;
    private JButton memberButton;
    private JButton healthButton;
    private JButton reminderButton;
    private JButton fileButton;
    private JButton settingButton;

    public MainUI() {
        initialize();
    }

    private void initialize() {
        setTitle("PetCare Admin");
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

        logoutButton = new JButton("登出");
        logoutButton.setFont(ModernUI.BUTTON_FONT);
        logoutButton.setBounds(24, 550, 120, 36);
        logoutButton.setBackground(ModernUI.CARD);
        logoutButton.setForeground(ModernUI.BLACK);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        sidebar.add(logoutButton);

        titleLabel = new JLabel("管理後台");
        titleLabel.setFont(ModernUI.PAGE_TITLE_FONT);
        titleLabel.setForeground(ModernUI.BLACK);
        titleLabel.setBounds(220, 48, 250, 38);
        contentPane.add(titleLabel);

        subtitleLabel = new JLabel("系統管理功能總覽");
        subtitleLabel.setFont(ModernUI.NORMAL_FONT);
        subtitleLabel.setForeground(ModernUI.TEXT_GRAY);
        subtitleLabel.setBounds(222, 86, 260, 25);
        contentPane.add(subtitleLabel);

        userInfoLabel = new JLabel("目前登入者：" + getUserText());
        userInfoLabel.setFont(ModernUI.SMALL_FONT);
        userInfoLabel.setForeground(ModernUI.TEXT_GRAY);
        userInfoLabel.setHorizontalAlignment(JLabel.RIGHT);
        userInfoLabel.setBounds(660, 48, 320, 25);
        contentPane.add(userInfoLabel);

        dashboardCard = new JPanel();
        dashboardCard.setBackground(ModernUI.CARD);
        dashboardCard.setBounds(220, 130, 775, 470);
        dashboardCard.setLayout(null);
        dashboardCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ModernUI.BORDER, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        contentPane.add(dashboardCard);

        sectionTitleLabel = new JLabel("Management");
        sectionTitleLabel.setFont(ModernUI.SECTION_FONT);
        sectionTitleLabel.setForeground(ModernUI.BLACK);
        sectionTitleLabel.setBounds(28, 26, 180, 30);
        dashboardCard.add(sectionTitleLabel);

        sectionHintLabel = new JLabel("請選擇要管理的項目");
        sectionHintLabel.setFont(ModernUI.SMALL_FONT);
        sectionHintLabel.setForeground(ModernUI.TEXT_GRAY);
        sectionHintLabel.setBounds(28, 58, 260, 24);
        dashboardCard.add(sectionHintLabel);

        featurePetCard = createFeaturePanel("所有寵物資料", "All pets", 28, 112);
        dashboardCard.add(featurePetCard);

        featureMemberCard = createFeaturePanel("使用者管理", "Members", 270, 112);
        dashboardCard.add(featureMemberCard);

        featureHealthCard = createFeaturePanel("健康紀錄總覽", "Health records", 512, 112);
        dashboardCard.add(featureHealthCard);

        featureReminderCard = createFeaturePanel("提醒事項總覽", "Reminders", 28, 272);
        dashboardCard.add(featureReminderCard);

        featureFileCard = createFeaturePanel("醫療文件", "Files", 270, 272);
        dashboardCard.add(featureFileCard);

        featureSettingCard = createFeaturePanel("系統設定", "Settings", 512, 272);
        dashboardCard.add(featureSettingCard);

        petButton = createCardButton(featurePetCard, "所有寵物資料");
        memberButton = createCardButton(featureMemberCard, "使用者管理");
        healthButton = createCardButton(featureHealthCard, "健康紀錄總覽");
        reminderButton = createCardButton(featureReminderCard, "提醒事項總覽");
        fileButton = createCardButton(featureFileCard, "醫療文件");
        settingButton = createCardButton(featureSettingCard, "系統設定");

        petButton.addActionListener(e -> openAdminTable("pets"));
        memberButton.addActionListener(e -> openAdminTable("members"));
        healthButton.addActionListener(e -> openAdminTable("health"));
        reminderButton.addActionListener(e -> openAdminTable("reminders"));
        fileButton.addActionListener(e -> openAdminTable("files"));
        settingButton.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "系統設定\n目前資料庫：petcare\n管理員可查看所有寵物、會員、健康紀錄、提醒事項與醫療文件。"));

        logoutButton.addActionListener(e -> {
            LoginSession.loginMember = null;
            LoginUI login = new LoginUI();
            login.setVisible(true);
            dispose();
        });
    }

    private String getUserText() {
        if (LoginSession.loginMember == null) {
            return "WindowBuilder 預覽 / ADMIN";
        }
        return LoginSession.loginMember.getName() + " / " + LoginSession.loginMember.getRole();
    }

    private JPanel createFeaturePanel(String title, String subtitle, int x, int y) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(ModernUI.CARD);
        card.setBounds(x, y, 210, 120);
        card.setBorder(new LineBorder(ModernUI.BORDER, 1, true));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
        titleLabel.setForeground(ModernUI.BLACK);
        titleLabel.setBounds(20, 24, 165, 30);
        card.add(titleLabel);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(ModernUI.SMALL_FONT);
        subLabel.setForeground(ModernUI.TEXT_GRAY);
        subLabel.setBounds(20, 55, 165, 24);
        card.add(subLabel);

        return card;
    }

    private JButton createCardButton(JPanel card, String featureName) {
        JButton button = new JButton("查看");
        button.setFont(ModernUI.BUTTON_FONT);
        button.setBounds(20, 82, 90, 28);
        button.setBackground(ModernUI.CARD);
        button.setForeground(ModernUI.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(ModernUI.BORDER, 1, true));
        card.add(button);
        return button;
    }

    private void openAdminTable(String type) {
        AdminTableUI ui = new AdminTableUI(
                AdminTableUI.getTitle(type),
                AdminTableUI.getSubtitle(type),
                AdminTableUI.getSql(type));
        ui.setVisible(true);
        dispose();
    }
}
