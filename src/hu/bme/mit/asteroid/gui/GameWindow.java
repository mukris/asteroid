package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A program főablaka
 */
public class GameWindow extends JFrame {

	private static final long serialVersionUID = -2774394870156151797L;

	public enum PanelId {
		GAME_MODE_SELECTOR, LEVEL_SELECTOR, NETWORK_PANEL, SERVER_PANEL, CLIENT_PANEL, GAME_FIELD, GET_NAME_PANEL, TOPLIST, ERROR_PANEL
	}

	private Map<PanelId, JPanel> mPanels = new HashMap<>();
	private GameManager mGameManager;

	public GameWindow() {
		super("Asteroid Shooter");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		initComponents();
		mGameManager = new GameManager((GameField) mPanels.get(PanelId.GAME_FIELD));
	}

	private void initComponents() {
		mPanels.put(PanelId.GAME_MODE_SELECTOR, new GameModeSelector(this));
		mPanels.put(PanelId.LEVEL_SELECTOR, new LevelSelectorPanel(this));
		mPanels.put(PanelId.GAME_FIELD, new GameField(this));
		mPanels.put(PanelId.TOPLIST, new Toplist(this));
		// TODO: többi panel osztályból példány létrehozása

		showPanel(PanelId.GAME_MODE_SELECTOR);
	}

	/**
	 * A {@link PanelId}-val azonosított felhasználói felület elem megjelenítése
	 * 
	 * @param panelId
	 */
	public void showPanel(final PanelId panelId) {
		JPanel panel = mPanels.get(panelId);
		setContentPane(panel);
		panel.requestFocusInWindow();
		validate();
	}
}
