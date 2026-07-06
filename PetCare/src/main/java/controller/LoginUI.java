package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import entity.Member;
import service.MemberService;
import service.impl.MemberServiceImpl;
import util.BackgroundPanel;
import util.LoginSession;
import javax.swing.SwingConstants;

public class LoginUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;

	// 顏色設定
	
	private final Color CARD_COLOR = new Color(247, 241, 232);    // 米色卡片
	private final Color MAIN_COLOR = new Color(93, 181, 158);     // 薄荷綠
	private final Color DARK_COLOR = new Color(60, 72, 82);       // 深灰藍
	private final Color LIGHT_GREEN = new Color(224, 245, 239);   // 淡薄荷綠

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI frame = new LoginUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginUI() {
		setTitle("PetCare 登入系統");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 454, 534);
		setLocationRelativeTo(null);

		contentPane = new BackgroundPanel("/images/login_bg.png");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		ImageIcon logoicon=new ImageIcon(getClass().getResource("/images/logo.png"));
		Image logoImage=logoicon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
		
				JButton exitButton = new JButton("離開");
				exitButton.setBounds(10, 10, 82, 31);
				contentPane.add(exitButton);
				exitButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
				exitButton.setBackground(new Color(0, 0, 0));
				exitButton.setForeground(new Color(255, 255, 255));
				exitButton.setFocusPainted(false);
				exitButton.setBorder(new LineBorder(new Color(190, 225, 215), 1, true));
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
				JLabel logo = new JLabel(new ImageIcon(logoImage));
				logo.setBounds(189, 175, 60, 45);
				contentPane.add(logo);
				
						JLabel title = new JLabel("PetCare");
						title.setHorizontalAlignment(SwingConstants.CENTER);
						title.setBounds(143, 220, 150, 35);
						contentPane.add(title);
						title.setFont(new Font("Microsoft JhengHei", Font.BOLD, 26));
						title.setForeground(DARK_COLOR);
						
								JLabel subTitle = new JLabel("寵物健康紀錄系統");
								subTitle.setHorizontalAlignment(SwingConstants.CENTER);
								subTitle.setBounds(135, 255, 160, 25);
								contentPane.add(subTitle);
								subTitle.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
								subTitle.setForeground(new Color(120, 130, 135));
								
										JLabel usernameLabel = new JLabel("帳號");
										usernameLabel.setBounds(93, 290, 80, 25);
										contentPane.add(usernameLabel);
										usernameLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
										usernameLabel.setForeground(DARK_COLOR);
										
												username = new JTextField();
												username.setBounds(163, 290, 178, 32);
												contentPane.add(username);
												username.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
												username.setBorder(BorderFactory.createCompoundBorder(
														new LineBorder(new Color(210, 220, 218), 1, true),
														new EmptyBorder(0, 10, 0, 10)
												));
												username.setBackground(new Color(252, 252, 252));
												username.setColumns(10);
												
														JLabel passwordLabel = new JLabel("密碼");
														passwordLabel.setBounds(93, 335, 80, 25);
														contentPane.add(passwordLabel);
														passwordLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
														passwordLabel.setForeground(DARK_COLOR);
														
																password = new JPasswordField();
																password.setBounds(163, 335, 178, 32);
																contentPane.add(password);
																password.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
																password.setBorder(BorderFactory.createCompoundBorder(
																		new LineBorder(new Color(210, 220, 218), 1, true),
																		new EmptyBorder(0, 10, 0, 10)
																));
																password.setBackground(new Color(252, 252, 252));
																
																		JButton loginButton = new JButton("登入");
																		loginButton.setBounds(112, 396, 95, 35);
																		contentPane.add(loginButton);
																		loginButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
																		loginButton.setBackground(new Color(0, 0, 0));
																		loginButton.setForeground(Color.WHITE);
																		loginButton.setFocusPainted(false);
																		loginButton.setBorder(new LineBorder(MAIN_COLOR, 1, true));
																		loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
																		
																				JButton registerButton = new JButton("註冊");
																				registerButton.setBounds(235, 396, 95, 35);
																				contentPane.add(registerButton);
																				registerButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
																				registerButton.setBackground(new Color(255, 255, 255));
																				registerButton.setForeground(new Color(0, 0, 0));
																				registerButton.setFocusPainted(false);
																				registerButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
																				registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
																				
																						registerButton.addActionListener(e -> {
																							RegisterUI register = new RegisterUI();
																							register.setVisible(true);
																							dispose();
																						});
																		
																				loginButton.addActionListener(e -> {
																					login();
																				});
				
						exitButton.addActionListener(e -> {
							System.exit(0);
						});
	}

	private void login() {
		String inputUsername = username.getText();
		String inputPassword = new String(password.getPassword());

		if (inputUsername.trim().isEmpty() || inputPassword.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "請輸入帳號和密碼");
			return;
		}

		MemberService memberService = new MemberServiceImpl();
		Member member = memberService.login(inputUsername, inputPassword);

		if (member != null) {
			LoginSession.loginMember = member;

			JOptionPane.showMessageDialog(
					null,
					"登入成功，歡迎 " + member.getName()
			);
			
			String role=member.getRole();
			if(role.equals("ADMIN"))
			{
				MainUI main = new MainUI();
				main.setVisible(true);
				dispose();
			}
			else if(role.equals("USER"))
			{
				PetHomeUI pethome =new PetHomeUI();
				pethome.setVisible(true);
				dispose();
			}

		} else {
			JOptionPane.showMessageDialog(
					null,
					"帳號或密碼錯誤"
			);
		}
	}
}