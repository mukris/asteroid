package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

/**
 * A program főablaka
 */
public class GameWindow extends JFrame {
	private static final long serialVersionUID = -2774394870156151797L;

	/**
	 * A felhasználói felület paneljainak azonosítására szolgáló enum
	 */
	public enum PanelId {
		GAME_MODE_SELECTOR, LEVEL_SELECTOR, MULTIPLAYER_PANEL, SERVER_PANEL, CLIENT_PANEL, GAME_FIELD, GET_NAME_PANEL, TOPLIST, ERROR_PANEL
	}

	private Map<PanelId, GamePanel> mPanels = new HashMap<>();
	private GamePanel mCurrentPanel = null;
	@SuppressWarnings("unused")
	private GameManager mGameManager;

	public GameWindow() {
		super("Asteroid Shooter");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		initComponents();
		mGameManager = new GameManager((GameField) mPanels.get(PanelId.GAME_FIELD));
	}

	private void initComponents() {
		mPanels.put(PanelId.GAME_MODE_SELECTOR, new GameModeSelector(this));
		mPanels.put(PanelId.LEVEL_SELECTOR, new LevelSelectorPanel(this));
		mPanels.put(PanelId.MULTIPLAYER_PANEL, new MultiplayerPanel(this));
		mPanels.put(PanelId.SERVER_PANEL, new ServerPanel(this));
		mPanels.put(PanelId.CLIENT_PANEL, new ClientPanel(this));
		mPanels.put(PanelId.GAME_FIELD, new GameField(this));
		mPanels.put(PanelId.TOPLIST, new Toplist(this));

		showPanel(PanelId.GAME_MODE_SELECTOR);
	}

	/**
	 * A {@link PanelId}-val azonosított felhasználói felület elem megjelenítése
	 * 
	 * @param panelId
	 */
	public void showPanel(final PanelId panelId) {
		if (mCurrentPanel != null) {
			mCurrentPanel.onHide();
		}

		GamePanel newPanel = mPanels.get(panelId);
		setContentPane(newPanel);
		validate();
		newPanel.requestFocusInWindow();
		newPanel.onShow();
		mCurrentPanel = newPanel;
	}
}
