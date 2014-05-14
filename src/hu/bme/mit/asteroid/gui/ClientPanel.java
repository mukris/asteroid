package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.network.NetworkClient;
import hu.bme.mit.asteroid.network.NetworkDiscover;
import hu.bme.mit.asteroid.network.NetworkListener;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Hálózati játéknál a játékhoz való csatlakozást elősegítő panel
 */
public class ClientPanel extends GamePanel {
	private static final long serialVersionUID = -4235684544731063205L;

	private static final String PLACEHOLDER = "A szerver címe";

	private JTextField mAddressEdit;
	private JButton mBtnConnect;

	private DefaultListModel<String> mAddresses;
	private JList<String> mAddressList;

	private NetworkDiscover mNetworkDiscover;
	private NetworkClient mNetworkClient;

	private NetworkListener mNetworkListener = new NetworkListener() {

		@Override
		public void onConnect() {
			logger.info("onConnect");
			try {
				mGameWindow.showPanel(PanelId.GAME_FIELD);
				GameManager.getInstance().startNetworkClientMultiplayerGame();
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
			logger.info("onDiscover(" + address + ")");

			mAddresses.addElement(address.toString().substring(1));
			if (mAddressList.getSelectedValue() == null) {
				mAddressList.setSelectedIndex(0);
			}
		};

		public void onDiscoveredTimeout(InetAddress address) {
			mAddresses.removeElement(address.toString().substring(1));
		};
	};

	public ClientPanel(GameWindow gameWindow) {
		super(gameWindow);

		mAddressEdit = new JTextField(PLACEHOLDER, 16);
		mAddressEdit.setFont(mStandardFont);
		mAddressEdit.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent event) {
				modified();
			}

			@Override
			public void insertUpdate(DocumentEvent event) {
				modified();
			}

			@Override
			public void changedUpdate(DocumentEvent event) {
			}

			private void modified() {
				final String text = mAddressEdit.getText();
				if (text.isEmpty() || text.equals(PLACEHOLDER)) {
					mBtnConnect.setEnabled(false);
				} else {
					mBtnConnect.setEnabled(true);
				}
			}
		});

		mBtnConnect = new JButton("Csatlakozás");
		mBtnConnect.setFont(mButtonFont);
		mBtnConnect.setEnabled(false);
		mBtnConnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					InetAddress address = InetAddress.getByName(mAddressEdit.getText());
					mNetworkClient.connect(address);
				} catch (IOException e) {
					logger.severe("Could not connect to host: " + e.getMessage());
				}
			}
		});

		mAddresses = new DefaultListModel<>();
		mAddressList = new JList<>(mAddresses);
		mAddressList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mAddressList.setVisibleRowCount(5);
		mAddressList.setFont(mStandardFont);
		mAddressList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				final String address = mAddressList.getSelectedValue();
				mAddressEdit.setText((address != null) ? address : PLACEHOLDER);
			}
		});

		JScrollPane listScrollPane = new JScrollPane(mAddressList);

		// TODO: normális layout
		Container clientButtons = new Container();

		clientButtons.setLayout(new GridLayout(3, 0));
		clientButtons.setSize(10, 10);
		clientButtons.add(mAddressEdit);
		clientButtons.add(mBtnConnect);
		clientButtons.add(listScrollPane);

		add(getHeaderLabel("Csatlakozás játékhoz"));
		add(clientButtons);
		add(getBackButton(PanelId.MULTIPLAYER_PANEL));
	}

	@Override
	protected void onShow() {
		super.onShow();
		logger.entering(this.getClass().getName(), "onShow()");

		GameManager gameManager = GameManager.getInstance();

		mAddresses.clear();

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
