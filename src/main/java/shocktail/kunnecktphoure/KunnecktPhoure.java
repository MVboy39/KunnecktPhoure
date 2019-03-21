package shocktail.kunnecktphoure;

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
	public static void main(String[] args) throws UnknownHostException, IOException {
		K4Panel panel = null;
		Object o = JOptionPane.showInputDialog(null, "local or online?", "kunneckt phoure", JOptionPane.DEFAULT_OPTION,
				null, new String[] { "local", "host", "join" }, "0");
		if ("local".equals(o)) {
			panel = new K4Panel();
		} else if ("host".equals(o)) {
			panel = new NetplayGameP1(Integer.parseInt(JOptionPane.showInputDialog("port to listen on")));
		} else if ("join".equals(o)) {
			String s = JOptionPane.showInputDialog("ip:port");
			panel = new NetplayGameP2(InetAddress.getByName(s.substring(0, s.indexOf(":"))),
					Integer.parseInt(s.substring(s.indexOf(":") + 1)));
		} else {
			System.exit(0);
		}

		JFrame window = new JFrame("KunnecktPhoure");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		window.setSize(464, 550);
		window.setResizable(false);
		window.add(panel);
		window.setVisible(true);
		new Thread(panel).start();
	}
}