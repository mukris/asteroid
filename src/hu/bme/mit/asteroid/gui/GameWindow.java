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
		mGameModeSelector = new GameModeSelector(this);
		mLevelSelectorPanel = new LevelSelectorPanel(this);
		mToplist = new Toplist();
		// TODO: többi panel osztályból példány létrehozása

		showPanel(PanelId.GAME_MODE_SELECTOR);
	}

	/**
	 * A {@link PanelId}-val azonosított felhasználói felület elem megjelenítése
	 * 
	 * @param panelId
	 */
	public void showPanel(final PanelId panelId) {
		// TODO: Többi panel osztály létrehozása, itt a hiányzó részek kitöltése
		switch (panelId) {
		case LEVEL_SELECTOR:
			setContentPane(mLevelSelectorPanel);
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
			setContentPane(mToplist);
			break;

		case ERROR_PANEL:
			break;

		case GAME_MODE_SELECTOR:
		default:
			setContentPane(mGameModeSelector);
			break;
		}
		validate();
	}
}
