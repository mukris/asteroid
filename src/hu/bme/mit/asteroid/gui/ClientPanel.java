package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.GameManager;
import hu.bme.mit.asteroid.exceptions.LevelNotExistsException;
import hu.bme.mit.asteroid.gui.GameWindow.PanelId;
import hu.bme.mit.asteroid.network.NetworkClient;
import hu.bme.mit.asteroid.network.NetworkDiscover;
import hu.bme.mit.asteroid.network.NetworkListener;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
		mAddressEdit.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (mAddressEdit.getText().isEmpty()) {
					mAddressEdit.setText(PLACEHOLDER);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (mAddressEdit.getText().equals(PLACEHOLDER)) {
					mAddressEdit.setText("");
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

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.ipadx = 200;
		constraints.weighty = 1;
		constraints.weightx = 1;
		constraints.anchor = GridBagConstraints.PAGE_START;
		add(getHeaderLabel("Csatlakozás játékhoz"), constraints);

		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 3;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 30, 15, 15);
		add(mAddressEdit, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.insets = new Insets(0, 15, 15, 30);
		add(mBtnConnect, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.weighty = 2;
		constraints.insets = new Insets(15, 30, 0, 30);
		add(listScrollPane, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.ipady = 30;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.PAGE_END;
		constraints.insets = new Insets(0, 30, 30, 30);
		add(getBackButton(PanelId.MULTIPLAYER_PANEL), constraints);
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
