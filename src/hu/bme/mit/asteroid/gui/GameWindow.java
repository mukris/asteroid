package hu.bme.mit.asteroid.gui;

import hu.bme.mit.asteroid.network.NetworkDiscover;
import hu.bme.mit.asteroid.network.NetworkDiscover.NetworkDiscoverListener;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * A program főablaka
 */
public class GameWindow extends JFrame implements NetworkDiscoverListener {

	private static final long serialVersionUID = -2774394870156151797L;

	private JButton mBtnServer;
	private JButton mBtnClient;
	private NetworkDiscover mNetworkDiscover;

	public GameWindow() {
		super("Asteroid Shooter");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		mNetworkDiscover = new NetworkDiscover();
		mNetworkDiscover.addNetworkDiscoverListener(this);
		initComponents();
	}

	private void initComponents() {
		mBtnClient = new JButton("Client");
		mBtnClient.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final Logger logger = Logger.getLogger("Client");
				logger.log(Level.INFO, "Staring to listen");
				mNetworkDiscover.startListening();
				logger.log(Level.INFO, "Listening...");
			}
		});

		mBtnServer = new JButton("Server");
		mBtnServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					final Logger logger = Logger.getLogger("Server");
					logger.log(Level.INFO, "Staring to broadcast");
					mNetworkDiscover.startBroadcasting();
					logger.log(Level.INFO, "Broadcasting...");
				} catch (SocketException e1) {
					e1.printStackTrace();
				}
			}
		});
		setLayout(new FlowLayout());
		add(mBtnClient);
		add(mBtnServer);

	}

	@Override
	public void onDiscover(InetAddress address, NetworkDiscover networkDiscover) {
		final Logger logger = Logger.getLogger("Client");
		logger.log(Level.INFO, address.getHostAddress());
	}

}
