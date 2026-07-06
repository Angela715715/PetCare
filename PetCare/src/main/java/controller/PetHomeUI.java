package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import entity.Pet;
import service.PetService;
import service.impl.PetServiceImpl;
import util.LoginSession;

public class PetHomeUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JPanel sidebar;
	private JPanel petPanel;

	private final Color BG_COLOR = new Color(246, 241, 234);
	private final Color SIDEBAR_COLOR = new Color(250, 247, 242);
	private final Color CARD_COLOR = Color.WHITE;
	private final Color BLACK = new Color(20, 20, 20);
	private final Color GRAY = new Color(100, 100, 100);
	private final Color LIGHT_BORDER = new Color(225, 218, 208);
	private final Color ACTIVE_MENU = new Color(238, 226, 210);

	public PetHomeUI() {
		setTitle("PetCare 我的寵物");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1040, 680);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBackground(BG_COLOR);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		createSidebar();
		createHeader();
		createPetListCard();

		loadPets();
	}

	private void createSidebar() {
		sidebar = new JPanel();
		sidebar.setLayout(null);
		sidebar.setBackground(SIDEBAR_COLOR);
		sidebar.setBounds(0, 0, 190, 680);
		contentPane.add(sidebar);

		JLabel logoImage = new JLabel();
		logoImage.setBounds(55, 35, 70, 45);
		logoImage.setHorizontalAlignment(JLabel.CENTER);
		logoImage.setIcon(loadImageIcon("/images/logo.png", 70, 45));
		sidebar.add(logoImage);

		JLabel logoText = new JLabel("PetCare");
		logoText.setFont(new Font("Serif", Font.BOLD, 30));
		logoText.setForeground(BLACK);
		logoText.setBounds(42, 85, 120, 45);
		sidebar.add(logoText);

		JButton petButton = createMenuButton("我的寵物", 165, true);
		sidebar.add(petButton);

		JButton healthButton = createMenuButton("健康紀錄", 218, false);
		sidebar.add(healthButton);

		JButton weightButton = createMenuButton("體重分析", 271, false);
		sidebar.add(weightButton);

		JButton reminderButton = createMenuButton("預約管理", 324, false);
		sidebar.add(reminderButton);

		JButton noticeButton = createMenuButton("通知中心", 377, false);
		sidebar.add(noticeButton);

		JButton emergencyButton = createMenuButton("緊急醫療卡", 430, false);
		sidebar.add(emergencyButton);

		JButton memberButton = createMenuButton("會員資料", 483, false);
		sidebar.add(memberButton);

		JButton logoutButton = new JButton("登出");
		logoutButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
		logoutButton.setBounds(28, 570, 130, 42);
		logoutButton.setBackground(Color.WHITE);
		logoutButton.setForeground(BLACK);
		logoutButton.setFocusPainted(false);
		logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		logoutButton.setBorder(new LineBorder(LIGHT_BORDER, 1, true));
		sidebar.add(logoutButton);

		petButton.addActionListener(e -> {
			PetHomeUI home = new PetHomeUI();
			home.setVisible(true);
			dispose();
		});

		healthButton.addActionListener(e -> {
			Pet firstPet = getFirstPet();

			if (firstPet != null) {
				MedicalRecordListUI ui = new MedicalRecordListUI(firstPet);
				ui.setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "目前沒有寵物資料");
			}
		});

		weightButton.addActionListener(e -> {
			Pet firstPet = getFirstPet();

			if (firstPet != null) {
				WeightRecordListUI ui = new WeightRecordListUI(firstPet);
				ui.setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "目前沒有寵物資料");
			}
		});

		reminderButton.addActionListener(e -> {
			Pet firstPet = getFirstPet();

			if (firstPet != null) {
				ReminderListUI ui = new ReminderListUI(firstPet);
				ui.setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "目前沒有寵物資料");
			}
		});

		noticeButton.addActionListener(e -> {
			JOptionPane.showMessageDialog(null, "通知中心功能可放提醒事項、逾期提醒、疫苗提醒");
		});

		emergencyButton.addActionListener(e -> {
			Pet firstPet = getFirstPet();

			if (firstPet != null) {
				EmergencySummaryUI ui = new EmergencySummaryUI(firstPet);
				ui.setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "目前沒有寵物資料，請先新增寵物");
			}
		});

		memberButton.addActionListener(e -> {
			if (LoginSession.loginMember != null) {
				JOptionPane.showMessageDialog(
						null,
						"目前登入者：" + LoginSession.loginMember.getName()
								+ "\n帳號：" + LoginSession.loginMember.getUsername()
								+ "\n角色：" + LoginSession.loginMember.getRole()
				);
			}
		});

		logoutButton.addActionListener(e -> {
			LoginSession.loginMember = null;

			LoginUI login = new LoginUI();
			login.setVisible(true);
			dispose();
		});
	}

	private JButton createMenuButton(String text, int y, boolean active) {
		JButton button = new JButton(text);
		button.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
		button.setHorizontalAlignment(JButton.LEFT);
		button.setBounds(20, y, 150, 42);
		button.setBackground(active ? ACTIVE_MENU : SIDEBAR_COLOR);
		button.setForeground(BLACK);
		button.setFocusPainted(false);
		button.setBorder(null);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}

	private void createHeader() {
		JLabel title = new JLabel("我的寵物");
		title.setFont(new Font("Microsoft JhengHei", Font.BOLD, 30));
		title.setForeground(BLACK);
		title.setBounds(250, 58, 250, 45);
		contentPane.add(title);

		JLabel subtitle = new JLabel("管理您可愛的毛孩們");
		subtitle.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 15));
		subtitle.setForeground(GRAY);
		subtitle.setBounds(252, 105, 250, 25);
		contentPane.add(subtitle);

		String loginName = "使用者";

		if (LoginSession.loginMember != null) {
			loginName = LoginSession.loginMember.getName();
		}

		JLabel loginInfo = new JLabel("目前登入者：" + loginName);
		loginInfo.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
		loginInfo.setForeground(GRAY);
		loginInfo.setBounds(715, 58, 160, 30);
		contentPane.add(loginInfo);

		JButton addButton = new JButton("新增寵物");
		addButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 14));
		addButton.setBounds(875, 52, 120, 42);
		addButton.setBackground(BLACK);
		addButton.setForeground(Color.WHITE);
		addButton.setFocusPainted(false);
		addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		addButton.setBorder(new LineBorder(BLACK, 1, true));
		contentPane.add(addButton);

		addButton.addActionListener(e -> {
			AddPetUI addPetUI = new AddPetUI();
			addPetUI.setVisible(true);
			dispose();
		});
	}

	private void createPetListCard() {
		JPanel listCard = new JPanel();
		listCard.setBackground(CARD_COLOR);
		listCard.setLayout(null);
		listCard.setBounds(250, 160, 745, 460);
		listCard.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(LIGHT_BORDER, 1, true),
				new EmptyBorder(10, 10, 10, 10)
		));
		contentPane.add(listCard);

		JLabel cardTitle = new JLabel("寵物列表");
		cardTitle.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18));
		cardTitle.setForeground(BLACK);
		cardTitle.setBounds(28, 22, 150, 28);
		listCard.add(cardTitle);

		JLabel cardSubTitle = new JLabel("點選寵物卡片可查看詳細資料與健康紀錄");
		cardSubTitle.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));
		cardSubTitle.setForeground(GRAY);
		cardSubTitle.setBounds(29, 50, 350, 24);
		listCard.add(cardSubTitle);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 90, 690, 340);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		listCard.add(scrollPane);

		petPanel = new JPanel();
		petPanel.setBackground(Color.WHITE);
		petPanel.setLayout(new GridLayout(0, 2, 22, 22));
		petPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		scrollPane.setViewportView(petPanel);
	}

	private void loadPets() {
		petPanel.removeAll();

		PetService petService = new PetServiceImpl();

		int memberId = 0;

		if (LoginSession.loginMember != null) {
			memberId = LoginSession.loginMember.getId();
		}

		List<Pet> pets = petService.findPetsByMemberId(memberId);

		if (pets == null || pets.isEmpty()) {
			JLabel emptyLabel = new JLabel("目前還沒有寵物資料，請先新增寵物");
			emptyLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 16));
			emptyLabel.setForeground(GRAY);
			petPanel.add(emptyLabel);
		} else {
			for (Pet pet : pets) {
				petPanel.add(createPetCard(pet));
			}
		}

		petPanel.revalidate();
		petPanel.repaint();
	}

	private JPanel createPetCard(Pet pet) {
		JPanel card = new JPanel();
		card.setLayout(null);
		card.setBackground(Color.WHITE);

		// 卡片加高，避免圖片拉長後文字被擠壓
		card.setPreferredSize(new Dimension(315, 360));

		card.setBorder(new LineBorder(LIGHT_BORDER, 1, true));
		card.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JLabel imageLabel = new JLabel();

		// 圖片高度從 150 改成 185，照片不會太扁
		imageLabel.setBounds(18, 18, 220, 185);
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		imageLabel.setBorder(new LineBorder(LIGHT_BORDER, 1, true));
		imageLabel.setIcon(getPetImageIcon(pet.getPhotopath(), 220, 185));
		card.add(imageLabel);

		JLabel nameLabel = new JLabel(safe(pet.getName()));
		nameLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
		nameLabel.setForeground(BLACK);

		// 下面文字整體往下移
		nameLabel.setBounds(18, 215, 260, 32);
		card.add(nameLabel);

		JLabel speciesLabel = createTagLabel(safe(pet.getSpecies()));
		speciesLabel.setBounds(18, 258, 65, 26);
		card.add(speciesLabel);

		JLabel genderLabel = createTagLabel(safe(pet.getGender()));
		genderLabel.setBounds(95, 258, 65, 26);
		card.add(genderLabel);

		JLabel birthdayLabel = new JLabel("生日  " + birthdayText(pet));
		birthdayLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));
		birthdayLabel.setForeground(GRAY);
		birthdayLabel.setBounds(18, 295, 250, 24);
		card.add(birthdayLabel);

		JLabel detailLabel = new JLabel("查看詳細資料");
		detailLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
		detailLabel.setForeground(BLACK);
		detailLabel.setBounds(18, 325, 120, 22);
		card.add(detailLabel);

		JLabel arrowLabel = new JLabel(">");
		arrowLabel.setFont(new Font("Arial", Font.BOLD, 16));
		arrowLabel.setForeground(BLACK);
		arrowLabel.setBounds(270, 325, 20, 22);
		card.add(arrowLabel);

		card.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				PetDetailUI detail = new PetDetailUI(pet);
				detail.setVisible(true);
				dispose();
			}
		});

		return card;
	}

	private JLabel createTagLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setForeground(BLACK);
		label.setBackground(new Color(246, 241, 234));
		label.setOpaque(true);
		label.setBorder(new LineBorder(LIGHT_BORDER, 1, true));
		return label;
	}

	private ImageIcon getPetImageIcon(String photoPath, int width, int height) {
		ImageIcon icon = null;

		if (photoPath != null && !photoPath.trim().isEmpty()) {
			File file = new File(photoPath);

			if (file.exists()) {
				icon = new ImageIcon(photoPath);
			}
		}

		if (icon == null) {
			java.net.URL url = getClass().getResource("/images/default_pet.png");

			if (url != null) {
				icon = new ImageIcon(url);
			} else {
				return createEmptyImage(width, height);
			}
		}

		Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(image);
	}

	private ImageIcon loadImageIcon(String path, int width, int height) {
		java.net.URL url = getClass().getResource(path);

		if (url == null) {
			return createEmptyImage(width, height);
		}

		ImageIcon icon = new ImageIcon(url);
		Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(image);
	}

	private ImageIcon createEmptyImage(int width, int height) {
		java.awt.image.BufferedImage img =
				new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);

		java.awt.Graphics2D g = img.createGraphics();
		g.setColor(new Color(249, 246, 244));
		g.fillRect(0, 0, width, height);
		g.setColor(new Color(210, 200, 190));
		g.drawRect(0, 0, width - 1, height - 1);
		g.dispose();

		return new ImageIcon(img);
	}

	private Pet getFirstPet() {
		PetService petService = new PetServiceImpl();

		int memberId = 0;

		if (LoginSession.loginMember != null) {
			memberId = LoginSession.loginMember.getId();
		}

		List<Pet> pets = petService.findPetsByMemberId(memberId);

		if (pets != null && !pets.isEmpty()) {
			return pets.get(0);
		}

		return null;
	}

	private String safe(String text) {
		if (text == null || text.trim().isEmpty()) {
			return "未填寫";
		}

		return text;
	}

	private String birthdayText(Pet pet) {
		if (pet == null || pet.getBirthday() == null) {
			return "未填寫";
		}

		return pet.getBirthday().toString();
	}
}