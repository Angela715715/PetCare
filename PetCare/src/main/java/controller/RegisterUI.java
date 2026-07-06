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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import entity.Member;
import service.MemberService;
import service.impl.MemberServiceImpl;

public class RegisterUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField nameField;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;

	private final Color WHITE = Color.WHITE;
	private final Color BLACK = Color.BLACK;
	private final Color TEXT_GRAY = new Color(90, 90, 90);
	private final Color BORDER = new Color(225, 225, 225);
	private final Font TITLE_FONT = new Font("Microsoft JhengHei", Font.BOLD, 28);
	private final Font NORMAL_FONT = new Font("Microsoft JhengHei", Font.PLAIN, 14);
	private final Font BUTTON_FONT = new Font("Microsoft JhengHei", Font.BOLD, 14);

	public RegisterUI() {
		setTitle("PetCare 註冊帳號");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 520, 560);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBackground(WHITE);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		setContentPane(contentPane);

		JLabel title = new JLabel("Create account");
		title.setFont(TITLE_FONT);
		title.setForeground(BLACK);
		title.setBounds(70, 55, 320, 40);
		contentPane.add(title);

		JLabel subtitle = new JLabel("建立 PetCare 一般使用者帳號");
		subtitle.setFont(NORMAL_FONT);
		subtitle.setForeground(TEXT_GRAY);
		subtitle.setBounds(72, 95, 320, 25);
		contentPane.add(subtitle);

		JPanel card = new JPanel();
		card.setLayout(null);
		card.setBackground(WHITE);
		card.setBounds(70, 145, 360, 333);
		card.setBorder(new LineBorder(BORDER, 1, true));
		contentPane.add(card);

		JLabel nameLabel = createLabel("姓名", 35, 30);
		card.add(nameLabel);
		nameField = createTextField(115, 25);
		card.add(nameField);

		JLabel usernameLabel = createLabel("帳號", 35, 85);
		card.add(usernameLabel);
		usernameField = createTextField(115, 80);
		card.add(usernameField);

		JLabel passwordLabel = createLabel("密碼", 35, 140);
		card.add(passwordLabel);
		passwordField = createPasswordField(115, 135);
		card.add(passwordField);

		JLabel confirmLabel = createLabel("確認密碼", 35, 195);
		card.add(confirmLabel);
		confirmPasswordField = createPasswordField(115, 190);
		card.add(confirmPasswordField);

		JButton registerButton = createBlackButton("註冊", 115, 245, 105, 34);
		card.add(registerButton);

		JButton backButton = createWhiteButton("返回登入", 230, 245, 95, 34);
		card.add(backButton);

		registerButton.addActionListener(e -> register());

		backButton.addActionListener(e -> {
			LoginUI login = new LoginUI();
			login.setVisible(true);
			dispose();
		});
	}

	private JLabel createLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setFont(NORMAL_FONT);
		label.setForeground(BLACK);
		label.setBounds(x, y, 80, 25);
		return label;
	}

	private JTextField createTextField(int x, int y) {
		JTextField field = new JTextField();
		field.setFont(NORMAL_FONT);
		field.setBounds(x, y, 210, 34);
		field.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(BORDER, 1, true),
				new EmptyBorder(0, 10, 0, 10)
		));
		field.setBackground(WHITE);
		field.setForeground(BLACK);
		return field;
	}

	private JPasswordField createPasswordField(int x, int y) {
		JPasswordField field = new JPasswordField();
		field.setFont(NORMAL_FONT);
		field.setBounds(x, y, 210, 34);
		field.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(BORDER, 1, true),
				new EmptyBorder(0, 10, 0, 10)
		));
		field.setBackground(WHITE);
		field.setForeground(BLACK);
		return field;
	}

	private JButton createBlackButton(String text, int x, int y, int w, int h) {
		JButton button = new JButton(text);
		button.setFont(BUTTON_FONT);
		button.setBounds(x, y, w, h);
		button.setBackground(BLACK);
		button.setForeground(WHITE);
		button.setFocusPainted(false);
		button.setBorder(new LineBorder(BLACK, 1, true));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}

	private JButton createWhiteButton(String text, int x, int y, int w, int h) {
		JButton button = new JButton(text);
		button.setFont(BUTTON_FONT);
		button.setBounds(115, 289, 105, 34);
		button.setBackground(WHITE);
		button.setForeground(BLACK);
		button.setFocusPainted(false);
		button.setBorder(new LineBorder(BORDER, 1, true));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}

	private void register() {
		String name = nameField.getText().trim();
		String username = usernameField.getText().trim();
		String password = new String(passwordField.getPassword()).trim();
		String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

		if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
			JOptionPane.showMessageDialog(null, "請完整輸入註冊資料");
			return;
		}

		if (!password.equals(confirmPassword)) {
			JOptionPane.showMessageDialog(null, "兩次密碼輸入不一致");
			return;
		}

		MemberService memberService = new MemberServiceImpl();

		if (memberService.usernameExists(username)) {
			JOptionPane.showMessageDialog(null, "此帳號已存在，請換一個帳號");
			return;
		}

		Member member = new Member(username, password, name, "USER");
		boolean success = memberService.register(member);

		if (success) {
			JOptionPane.showMessageDialog(null, "註冊成功，請重新登入");
			LoginUI login = new LoginUI();
			login.setVisible(true);
			dispose();
		} else {
			JOptionPane.showMessageDialog(null, "註冊失敗，請確認資料庫連線或帳號是否重複");
		}
	}
}
