package shocktail.kunnecktphoure;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * contains the main method that runs everything
 *
 * @author Steve Hocktail
 */
public class KunnecktPhoure {
	private static K4Panel panel = null;

	public static void main(String[] args) throws IOException {
		Object o = JOptionPane.showInputDialog(null, "local or online?", "kunneckt phoure", JOptionPane.DEFAULT_OPTION,
				null, new String[] { "local", "host", "join", "exit" }, "0");
		if ("local".equals(o)) {
			KunnecktPhoure.panel = new K4Panel();
		} else if ("host".equals(o)) {
			try {
				int port = Integer.parseInt(JOptionPane.showInputDialog("port to listen on"));
				if (port < 0 || port > 65535) {
					JOptionPane.showMessageDialog(null, "bad input, exiting");
					return;
				}
				KunnecktPhoure.panel = new NetplayGameP1(port);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "bad input, exiting");
				return;
			}
		} else if ("join".equals(o)) {
			String s = JOptionPane.showInputDialog("ip:port");
			if (!s.matches("(([0-9]{1,3}\\.){3})[0-9]{1,3}:[0-9]{1,5}")) {
				JOptionPane.showMessageDialog(null, "bad input, exiting");
				return;
			}
			try {
				int port = Integer.parseInt(s.substring(s.indexOf(":") + 1));
				if (port < 0 || port > 65535) {
					JOptionPane.showMessageDialog(null, "bad input, exiting");
					return;
				}
				KunnecktPhoure.panel = new NetplayGameP2(InetAddress.getByName(s.substring(0, s.indexOf(":"))), port);
			} catch (UnknownHostException ex) {
				JOptionPane.showMessageDialog(null, "bad input, exiting");
				return;
			}
		} else {
			return;
		}

		JFrame window = new JFrame("KunnecktPhoure");
		window.setLayout(null);
		window.setSize(464, 550);
		window.setResizable(false);
		window.add(KunnecktPhoure.panel);
		window.setVisible(true);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				KunnecktPhoure.panel.cleanup();
				e.getWindow().dispose();
				System.exit(0);
			}
		});
		new Thread(KunnecktPhoure.panel).start();
	}
}