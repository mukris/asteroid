package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.network.NetworkDiscover;
import hu.bme.mit.asteroid.network.NetworkListener;
import hu.bme.mit.asteroid.network.NetworkServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * A hálózati játék indításáért felelős panel
 */
public class ServerPanel extends GamePanel {
	private static final long serialVersionUID = 5452872136300248715L;
	
	private DefaultListModel<String> mAddresses;
	private JList<String> mAddressList;

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
		
		mAddresses = new DefaultListModel<>();
		mAddressList = new JList<>(mAddresses);
		mAddressList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mAddressList.setVisibleRowCount(10);
		mAddressList.setEnabled(false);
		
		JScrollPane listScrollPane = new JScrollPane(mAddressList);
		
		add(getHeaderLabel("Szerver"));
		add(listScrollPane);
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
		
		mAddresses.clear();
		List<InetAddress> ownIPs = mNetworkServer.getOwnIPs();
		for (InetAddress inetAddress : ownIPs) {
			mAddresses.addElement(inetAddress.toString().substring(1));
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
