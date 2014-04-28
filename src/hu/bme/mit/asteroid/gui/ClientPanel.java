package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.network.NetworkClient;
import hu.bme.mit.asteroid.network.NetworkDiscover;
import hu.bme.mit.asteroid.network.NetworkListener;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Hálózati játéknál a játékhoz való csatlakozást elősegítő panel
 */
public class ClientPanel extends GamePanel {
	private static final long serialVersionUID = 5038448698817588083L;

	private NetworkDiscover mNetworkDiscover;
	private NetworkClient mNetworkClient;

	private NetworkListener mNetworkListener = new NetworkListener() {

		@Override
		public void onConnect() {
			logger.info("onConnect");
			try {
				GameManager.getInstance().startNetworkClientMultiplayerGame();
				mGameWindow.showPanel(PanelId.GAME_FIELD);
			} catch (LevelNotExistsException e) {
				e.printStackTrace();
			}
		};

		@Override
		public void onDisconnect() {
			logger.info("onDisconnect");
		};

		@Override
		public void onDiscover(InetAddress address, NetworkDiscover networkDiscover) {
			logger.info("onDiscover");
			// FIXME: nem kapcsolódunk egyből...
			try {
				mNetworkClient.connect(address);
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	};

	public ClientPanel(GameWindow gameWindow) {
		super(gameWindow);

		add(getBackButton(PanelId.MULTIPLAYER_PANEL));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onShow() {
		super.onShow();
		logger.entering(this.getClass().getName(), "onShow()");

		GameManager gameManager = GameManager.getInstance();

		mNetworkDiscover = gameManager.getNetworkDiscover();
		mNetworkDiscover.addNetworkDiscoverListener(mNetworkListener);
		mNetworkDiscover.startListening();

		mNetworkClient = gameManager.getNetworkClient();
		mNetworkClient.addConnectionListener(mNetworkListener);
		logger.exiting(this.getClass().getName(), "onShow()");
	}

	@Override
	protected void onHide() {
		super.onHide();
		logger.entering(this.getClass().getName(), "onHide()");
		mNetworkDiscover.stopListening();
		mNetworkDiscover.removeNetworkDiscoverListener(mNetworkListener);
		mNetworkClient.removeConnectionListener(mNetworkListener);
		logger.exiting(this.getClass().getName(), "onHide()");
	}
}
