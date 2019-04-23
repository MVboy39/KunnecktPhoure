package shocktail.kunnecktphoure;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * the panel the game is held in
 *
 * @author Steve Hocktail
 */
public class K4Panel extends JPanel implements Runnable {
	private byte[][] coins; // 0 is empty, 1 is black, 2 is red
	private boolean turn; // true is black, false is red
	private byte last; // -1 if no last turn, otherwise represents where the most recent coin was
						// dropped

	/**
	 * constructs the panel the game is in
	 */
	public K4Panel() {
		this.setLayout(null);
		this.coins = new byte[7][6];
		this.startNewGame();
		JButton button;
		for (byte i = 0; i < 7; i++) {
			button = new JButton();
			button.addActionListener(new K4Panel.TheListener(i));
			button.setSize(60, 60);
			button.setLocation(i * 60 + 15, 400);
			this.add(button);
		}
		button = new JButton("new game");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				K4Panel.this.startNewGame();
			}
		});
		button.setSize(120, 30);
		button.setLocation(160, 490);
		this.add(button);
		this.setSize(440, 550);
	}

	/**
	 * resets the board and starts a new game
	 */
	public void startNewGame() {
		for (byte i = 0; i < 7; i++) {
			for (byte j = 0; j < 6; j++) {
				this.coins[i][j] = 0;
			}
		}
		this.turn = true;
		this.last = -1;
		this.repaint();
	}

	/**
	 * returns a boolean based on whose turn it is
	 *
	 * @return true if black's turn, false if red
	 */
	public boolean getTurn() {
		return this.turn;
	}

	/**
	 * goes through a whole turn of the game. places the coin, then checks if the
	 * game is over, then asks to start a new game if the game is over
	 *
	 * @param col the column to place the coin in
	 */
	public void doTurn(byte col) {
		byte a = this.placeCoin(col);
		if (a >= 0) { // if it placed successfully
			this.repaint();
			a = this.checkForWin(col, a);
			switch (a) {
			case 0:
				if (this.boardIsFull()) {
					JOptionPane.showMessageDialog(null, "It is a draw");
					a = -1;
				}
				break;
			case 1:
				JOptionPane.showMessageDialog(null, "Black wins");
				a = -1;
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "Red wins");
				a = -1;
				break;
			}
			if (a == -1) { // if the game is over
				if (JOptionPane.showConfirmDialog(null, "new game?", "KunnecktPhoure",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					this.startNewGame();
				} else {
					this.cleanup();
					System.exit(0);
				}
			}
		}
	}

	/**
	 * attempts to drop a coin on the board, then switches turn if it works
	 *
	 * @param col the column to drop in
	 * @return the row the coin is placed in, or -1 if it couldn't be placed
	 */
	private byte placeCoin(byte col) {
		if (col >= 0 && col <= 6) {
			for (byte i = 5; i >= 0; i--) {
				if (this.coins[col][i] == 0) {
					this.coins[col][i] = (byte) (this.turn ? 1 : 2);
					this.turn = !this.turn;
					this.last = (byte) (col * 6 + i);
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * checks to see if a piece is in a four-in-a-row
	 *
	 * @param x the column of the piece
	 * @param y the row of the piece
	 * @return 0 if no winner, 1 if black, 2 if red
	 */
	private byte checkForWin(byte x, byte y) {
		if (this.coins[x][y] != 0) {
			for (int i = -3; i <= 0; i++) {
				// check horizontal
				if (x + i >= 0 && x + i <= 3 && this.coins[x + i][y] == this.coins[x + i + 1][y]
						&& this.coins[x + i][y] == this.coins[x + i + 2][y]
						&& this.coins[x + i][y] == this.coins[x + i + 3][y]) {
					return this.coins[x][y];
				}

				// check vertical
				if (y + i >= 0 && y + i <= 2 && this.coins[x][y + i] == this.coins[x][y + i + 1]
						&& this.coins[x][y + i] == this.coins[x][y + i + 2]
						&& this.coins[x][y + i] == this.coins[x][y + i + 3]) {
					return this.coins[x][y];
				}

				// check diagonal down-right
				if (x + i >= 0 && x + i <= 3 && y + i >= 0 && y + i <= 2
						&& this.coins[x + i][y + i] == this.coins[x + i + 1][y + i + 1]
						&& this.coins[x + i][y + i] == this.coins[x + i + 2][y + i + 2]
						&& this.coins[x + i][y + i] == this.coins[x + i + 3][y + i + 3]) {
					return this.coins[x][y];
				}

				// check diagonal down-left
				if (x - i >= 3 && x - i <= 6 && y + i >= 0 && y + i <= 2
						&& this.coins[x - i][y + i] == this.coins[x - i - 1][y + i + 1]
						&& this.coins[x - i][y + i] == this.coins[x - i - 2][y + i + 2]
						&& this.coins[x - i][y + i] == this.coins[x - i - 3][y + i + 3]) {
					return this.coins[x][y];
				}
			}
		}
		return 0;
	}

	/**
	 * checks to see if the board is full
	 *
	 * @return true if the entire top row is filled, false otherwise
	 */
	private boolean boardIsFull() {
		for (int i = 0; i <= 6; i++) {
			if (this.coins[i][0] == 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		byte spot = 0;
		for (byte i = 0; i < 7; i++) {
			for (byte j = 0; j < 6; j++) {
				// draw box
				g.setColor(this.last == spot ? Color.YELLOW : Color.GRAY); // highlight the last turn
				g.fillRect(i * 60 + 15, j * 60 + 10, 60, 60);
				g.setColor(Color.BLACK);
				g.drawRect(i * 60 + 15, j * 60 + 10, 60, 60);

				// draw coin
				switch (this.coins[i][j]) {
				case 2:
					g.setColor(Color.RED);
				case 1:
					g.fillOval(i * 60 + 20, j * 60 + 15, 50, 50);
				}
				spot++;
			}
		}
	}

	/**
	 * unused here, for the netplay variants
	 */
	@Override
	public void run() {
	}

	/**
	 * so apparently finalize was deprecated this whole time, so instead this will
	 * be used to close the streams in the netplay variants. unused here, though.
	 */
	public void cleanup() {
	}

	/**
	 * listener for the buttons that drop coins
	 *
	 * @author Steve Hocktail
	 */
	private class TheListener implements ActionListener {
		private byte col;

		/**
		 * constructs a new listener for dropping coins
		 *
		 * @param col the column this listener is for
		 */
		public TheListener(byte col) {
			this.col = col;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			K4Panel.this.doTurn(this.col);
		}
	}
}