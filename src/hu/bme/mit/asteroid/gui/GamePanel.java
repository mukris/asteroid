package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.gui.GameWindow.PanelId;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * A kezelőfelület paneljeinek közös őse
 */
public abstract class GamePanel extends JPanel {
	private static final long serialVersionUID = -7570121177800374977L;

	protected final GameWindow mGameWindow;
	protected final Font mHeaderFont = new Font("Serif", Font.BOLD, 30);
	protected final Font mStandardFont = new Font("Serif", Font.PLAIN, 20);
	protected final Font mButtonFont = new Font("Serif", Font.PLAIN, 20);

	protected Image mBackgroundImage;
	protected JButton mBtnBack;
	protected JLabel mHeaderLabel;

	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	public GamePanel(GameWindow gameWindow) {
		mGameWindow = gameWindow;
		try {
			mBackgroundImage = ImageIO
					.read(GamePanel.class.getResourceAsStream("/hu/bme/mit/asteroid/res/space_1.jpg"));
			UIManager.put("Label.foreground", Color.WHITE);
		} catch (IOException e) {
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(mBackgroundImage, 0, 0, null);
	}

	/**
	 * A panelokon megjelenő Vissza gomb példányosítása, működésének definiálása
	 * 
	 * @param panelId
	 *            Melyik panelre lépjünk vissza?
	 * @return A Vissza gomb
	 */
	protected JButton getBackButton(final PanelId panelId) {
		if (mBtnBack == null) {

			mBtnBack = new JButton("Vissza");
			mBtnBack.setFont(mButtonFont);
			mBtnBack.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					mGameWindow.showPanel(panelId);
				}
			});
		}
		return mBtnBack;
	}

	protected JLabel getHeaderLabel(String title) {
		if (mHeaderLabel == null) {
			mHeaderLabel = new JLabel(title);
			mHeaderLabel.setFont(mHeaderFont);
			mHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
		}
		return mHeaderLabel;
	}

	/**
	 * Akkor hívódik, amikor a panel megjelenik
	 */
	protected void onShow() {

	}

	/**
	 * Akkor hívódik, amikor a panel helyére másik kerül
	 */
	protected void onHide() {

	}
}
