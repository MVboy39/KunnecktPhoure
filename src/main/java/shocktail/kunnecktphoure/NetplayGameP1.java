package shocktail.kunnecktphoure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;

public class NetplayGameP1 extends K4Panel {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public NetplayGameP1(int localport) throws IOException {
		ServerSocket serversocket = new ServerSocket(localport);
		serversocket.setSoTimeout(60000);
		try {
			this.socket = serversocket.accept();
		} catch (SocketTimeoutException ex) {
			JOptionPane.showMessageDialog(null, "No one joined.");
			serversocket.close();
			this.cleanup();
			System.exit(0);
		}
		serversocket.close();
		this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.out = new PrintWriter(this.socket.getOutputStream(), true);
	}

	@Override
	public void run() {
		String s;
		int c;
		try {
			while (true) {
				if (!this.getTurn()) {
					if ((s = this.in.readLine()) != null) {
						c = Integer.parseInt(s);
						if (c >= 0 && c <= 6)
							super.doTurn((byte) c);
					}

				}
				Thread.sleep(100);
			}
		} catch (SocketException ex) {
			JOptionPane.showMessageDialog(null, "The opponent closed the game.");
			this.cleanup();
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			this.cleanup();
			System.exit(1);
		}
	}

	@Override
	public void doTurn(byte col) {
		if (this.getTurn()) {
			super.doTurn(col);
			this.out.println(col);
		}
	}

	@Override
	public void cleanup() {
		try {
			this.in.close();
		} catch (Exception ex) {
		}
		try {
			this.out.close();
		} catch (Exception ex) {
		}
		try {
			this.socket.close();
		} catch (Exception ex) {
		}
	}
}