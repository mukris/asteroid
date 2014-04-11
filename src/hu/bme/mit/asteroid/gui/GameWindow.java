package hu.bme.mit.asteroid.gui;

import javax.swing.JFrame;

/**
 * A program főablaka
 */
public class GameWindow extends JFrame {

	private static final long serialVersionUID = -2774394870156151797L;
	private GameModeSelector mGameModeSelector;
	private LevelSelectorPanel mLevelSelectorPanel;
	private Toplist mToplist;

	public enum PanelId {
		GAME_MODE_SELECTOR, LEVEL_SELECTOR, NETWORK_PANEL, SERVER_PANEL, CLIENT_PANEL, GAME_FIELD, GET_NAME_PANEL, TOPLIST, ERROR_PANEL
	}

	public GameWindow() {
		super("Asteroid Shooter");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		initComponents();
	}

	private void initComponents() {
		mGameModeSelector = new GameModeSelector();
		mLevelSelectorPanel = new LevelSelectorPanel();
		mToplist = new Toplist();

		showPanel(PanelId.GAME_MODE_SELECTOR);
	}

	public void showPanel(PanelId panelId) {
		switch (panelId) {
		case LEVEL_SELECTOR:
			removeAll();
			add(mLevelSelectorPanel);
			break;

		case NETWORK_PANEL:
			break;

		case SERVER_PANEL:
			break;

		case CLIENT_PANEL:
			break;

		case GAME_FIELD:
			break;

		case GET_NAME_PANEL:
			break;

		case TOPLIST:
			add(mToplist);
			break;

		case ERROR_PANEL:
			break;

		case GAME_MODE_SELECTOR:
		default:
			removeAll();
			add(mGameModeSelector);
			break;
		}
	}

}
