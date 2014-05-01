package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.network.NetworkDiscover;
import hu.bme.mit.asteroid.network.NetworkListener;
import hu.bme.mit.asteroid.network.NetworkServer;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.JOptionPane;

/**
 * A hálózati játék indításáért felelős panel
 */
public class ServerPanel extends GamePanel {
	private static final long serialVersionUID = -1604212139878160821L;

	private NetworkDiscover mNetworkDiscover;
	private NetworkServer mNetworkServer;

	private NetworkListener mNetworkListener = new NetworkListener() {

		@Override
		public void onConnect() {
			try {
				mGameWindow.showPanel(PanelId.GAME_FIELD);
				GameManager.getInstance().startNetworkServerMultiplayerGame();
			} catch (LevelNotExistsException e) {
				e.printStackTrace();
			}
		}
	};

	public ServerPanel(GameWindow gameWindow) {
		super(gameWindow);
		add(getBackButton(PanelId.MULTIPLAYER_PANEL));
	}

	@Override
	protected void onShow() {
		super.onShow();

		GameManager gameManager = GameManager.getInstance();

		try {
			mNetworkDiscover = gameManager.getNetworkDiscover();
			mNetworkDiscover.startBroadcasting();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		try {
			mNetworkServer = gameManager.getNetworkServer();
			mNetworkServer.addConnectionListener(mNetworkListener);
			mNetworkServer.startListening();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Nem sikerült elindítani a szervert", "Hálózati hiba",
					JOptionPane.ERROR_MESSAGE);
			mGameWindow.showPanel(PanelId.MULTIPLAYER_PANEL);
			e.printStackTrace();
		}
	}

	@Override
	protected void onHide() {
		super.onHide();

		mNetworkDiscover.stopBroadcasting();
		mNetworkServer.stopListening();
		mNetworkServer.removeConnectionListener(mNetworkListener);
	}
}
