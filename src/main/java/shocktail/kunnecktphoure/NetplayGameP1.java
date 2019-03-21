package shocktail.kunnecktphoure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NetplayGameP1 extends K4Panel {
	private Socket socket;
	private ServerSocket serversocket;
	private BufferedReader in;
	private PrintWriter out;

	public NetplayGameP1(int localport) throws IOException {
		this.serversocket = new ServerSocket(localport);
		this.socket = this.serversocket.accept();
		this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.out = new PrintWriter(this.socket.getOutputStream(), true);
	}

	@Override
	public void run() {
		String s;
		int c;
		while (true) {
			if (!this.getTurn()) {
				try {
					if ((s = this.in.readLine()) != null) {
						c = Integer.parseInt(s);
						if (c >= 0 && c <= 6)
							super.doTurn((byte) c);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
			System.out.print(""); // for some reason it didn't work without printing something
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
	protected void finalize() {
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
		try {
			this.serversocket.close();
		} catch (Exception ex) {
		}
	}
}