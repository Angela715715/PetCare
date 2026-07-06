package util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image backgroundImage;

	public BackgroundPanel(String imagePath) {
		URL url = getClass().getResource(imagePath);

		if (url != null) {
			backgroundImage = new ImageIcon(url).getImage();
		} else {
			System.out.println("找不到背景圖片：" + imagePath);
			backgroundImage = null;
		}

		setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		} else {
			g.setColor(new Color(248, 246, 240));
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
}
