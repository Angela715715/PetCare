package util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ModernUI {

    public static final int FRAME_W = 1040;
    public static final int FRAME_H = 680;
    public static final int SIDEBAR_W = 190;

    public static final Color CANVAS = new Color(248, 243, 236);
    public static final Color SIDEBAR = new Color(255, 252, 247);
    public static final Color CARD = new Color(255, 255, 255);
    public static final Color BLACK = new Color(18, 18, 18);
    public static final Color TEXT_GRAY = new Color(100, 96, 91);
    public static final Color LIGHT_TEXT = new Color(150, 145, 138);
    public static final Color SOFT_GRAY = new Color(250, 248, 245);
    public static final Color BORDER = new Color(230, 222, 212);
    public static final Color CHIP = new Color(246, 239, 230);
    public static final Color SELECTED = new Color(241, 229, 216);
    public static final Color ACCENT = new Color(205, 152, 97);
    public static final Color ACCENT_LIGHT = new Color(250, 240, 229);
    public static final Color CHART_LINE = new Color(196, 140, 87);

    public static final Font LOGO_FONT = new Font("Serif", Font.BOLD, 30);
    public static final Font TITLE_FONT = new Font("Microsoft JhengHei", Font.BOLD, 30);
    public static final Font PAGE_TITLE_FONT = new Font("Microsoft JhengHei", Font.BOLD, 30);
    public static final Font SECTION_FONT = new Font("Microsoft JhengHei", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Microsoft JhengHei", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Microsoft JhengHei", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Microsoft JhengHei", Font.BOLD, 14);
    public static final Font VALUE_FONT = new Font("Microsoft JhengHei", Font.BOLD, 24);

    private ModernUI() {}

    public static JPanel createRoot() {
        JPanel root = new JPanel(null);
        root.setBackground(CANVAS);
        return root;
    }

    public static JPanel createCard(int x, int y, int w, int h) {
        JPanel panel = new JPanel(null);
        panel.setBackground(CARD);
        panel.setBounds(x, y, w, h);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }

    public static JPanel createSoftCard(int x, int y, int w, int h) {
        JPanel panel = createCard(x, y, w, h);
        panel.setBackground(new Color(255, 254, 252));
        return panel;
    }

    public static JLabel logoMark(int x, int y) {
        JLabel logo = new JLabel();
        URL url = ModernUI.class.getResource("/images/logo.png");
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage().getScaledInstance(60, 45, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(image));
        } else {
            logo.setText("PetCare");
            logo.setFont(new Font("Microsoft JhengHei", Font.BOLD, 34));
            logo.setHorizontalAlignment(SwingConstants.CENTER);
        }
        logo.setBounds(x, y, 70, 52);
        return logo;
    }

    public static JLabel logoText(int x, int y) {
        JLabel logo = new JLabel("PetCare");
        logo.setFont(LOGO_FONT);
        logo.setForeground(BLACK);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setBounds(x, y, 130, 38);
        return logo;
    }

    public static JButton navButton(String icon, String text, int y, boolean active) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setHorizontalAlignment(JButton.LEFT);
        button.setBounds(20, y, 150, 42);
        button.setBackground(active ? SELECTED : SIDEBAR);
        button.setForeground(BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(active ? new LineBorder(SELECTED, 1, true) : new LineBorder(SIDEBAR, 1, true));
        return button;
    }

    public static JButton blackButton(String text, int x, int y, int w, int h) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBounds(x, y, w, h);
        button.setBackground(BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(BLACK, 1, true));
        return button;
    }

    public static JButton whiteButton(String text, int x, int y, int w, int h) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBounds(x, y, w, h);
        button.setBackground(CARD);
        button.setForeground(BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(BORDER, 1, true));
        return button;
    }

    public static JButton iconButton(String text, int x, int y, int w, int h, boolean black) {
        return black ? blackButton(text, x, y, w, h) : whiteButton(text, x, y, w, h);
    }

    public static JTextField textField(int x, int y, int w, int h) {
        JTextField field = new JTextField();
        field.setFont(NORMAL_FONT);
        field.setBounds(x, y, w, h);
        field.setBackground(CARD);
        field.setForeground(BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        return field;
    }

    public static void styleComboBox(JComboBox<String> box, int x, int y, int w, int h) {
        box.setFont(NORMAL_FONT);
        box.setBounds(x, y, w, h);
        box.setBackground(CARD);
        box.setForeground(BLACK);
        box.setBorder(new LineBorder(BORDER, 1, true));
    }

    public static JLabel label(String text, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        label.setForeground(BLACK);
        label.setBounds(x, y, w, h);
        return label;
    }

    public static JLabel smallGray(String text, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        label.setFont(SMALL_FONT);
        label.setForeground(TEXT_GRAY);
        label.setBounds(x, y, w, h);
        return label;
    }

    public static JLabel chip(String text, int x, int y, int w, int h) {
        JLabel chip = new JLabel(text);
        chip.setOpaque(true);
        chip.setBackground(CHIP);
        chip.setForeground(BLACK);
        chip.setFont(SMALL_FONT);
        chip.setHorizontalAlignment(JLabel.CENTER);
        chip.setBorder(new LineBorder(BORDER, 1, true));
        chip.setBounds(x, y, w, h);
        return chip;
    }

    public static String safe(String value) {
        if (value == null || value.trim().isEmpty()) return "未填寫";
        return value;
    }

    public static ImageIcon petImage(String photoPath, int width, int height) {
        ImageIcon icon = null;
        if (photoPath != null && !photoPath.trim().isEmpty()) {
            File file = new File(photoPath);
            if (file.exists()) icon = new ImageIcon(photoPath);
        }
        if (icon == null) {
            URL url = ModernUI.class.getResource("/images/default_pet.png");
            if (url != null) icon = new ImageIcon(url);
            else return defaultIcon(width, height);
        }
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    public static ImageIcon defaultIcon(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(SOFT_GRAY);
        g.fillRoundRect(0, 0, width - 1, height - 1, 28, 28);
        g.setColor(BORDER);
        g.drawRoundRect(0, 0, width - 1, height - 1, 28, 28);
        g.setColor(ACCENT);
        g.setFont(new Font("Microsoft JhengHei", Font.BOLD, 22));
        g.drawString("PetCare", Math.max(8, width / 2 - 42), height / 2 + 8);
        g.dispose();
        return new ImageIcon(image);
    }
}
