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

	public static void main(String[] args) throws UnknownHostException, IOException {
		Object o = JOptionPane.showInputDialog(null, "local or online?", "kunneckt phoure", JOptionPane.DEFAULT_OPTION,
				null, new String[] { "local", "host", "join" }, "0");
		if ("local".equals(o)) {
			KunnecktPhoure.panel = new K4Panel();
		} else if ("host".equals(o)) {
			KunnecktPhoure.panel = new NetplayGameP1(Integer.parseInt(JOptionPane.showInputDialog("port to listen on")));
		} else if ("join".equals(o)) {
			String s = JOptionPane.showInputDialog("ip:port");
			KunnecktPhoure.panel = new NetplayGameP2(InetAddress.getByName(s.substring(0, s.indexOf(":"))),
					Integer.parseInt(s.substring(s.indexOf(":") + 1)));
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
			public void windowClosing(WindowEvent arg0) {
				KunnecktPhoure.panel.cleanup();
				System.exit(0);
			}
		});
		new Thread(KunnecktPhoure.panel).start();
	}
}