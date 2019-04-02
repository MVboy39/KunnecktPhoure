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
	private byte last; // -1 if no last turn, otherwise represents where the most recent coin was dropped

	/**
	 * constructs the panel the game is in
	 */
	public K4Panel() {
		this.setLayout(null);
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
		this.coins = new byte[7][6];
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
	 * attempts to drop a coin on the board, then switches turn if it works
	 *
	 * @param col the column to drop in
	 * @return true if placed, false otherwise
	 */
	private boolean placeCoin(byte col) {
		if (col >= 0 && col <= 6) {
			for (int i = 5; i >= 0; i--) {
				if (this.coins[col][i] == 0) {
					this.coins[col][i] = (byte) (this.turn ? 1 : 2);
					this.turn = !this.turn;
					this.last = (byte) (col * 6 + i);
					return true;
				}
			}
		}
		return false;
	}

	public void doTurn(byte col) {
		if (this.placeCoin(col)) {
			this.repaint();
			byte win = this.checkForWin();
			switch (win) {
			case 1:
				JOptionPane.showMessageDialog(null, "Black wins");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "Red wins");
			}
		}
	}

	/**
	 * checks to see if a piece is in a four-in-a-row
	 *
	 * @param col the column of the piece
	 * @param row the row of the piece
	 * @return 0 if no winner, 1 if black, 2 if red
	 */
	private byte checkForWin() {
		byte i, j;

		// check vertical
		for (i = 0; i < 7; i++) {
			for (j = 0; j < 3; j++) {
				if (this.coins[i][j] != 0 && this.coins[i][j] == this.coins[i][j + 1]
						&& this.coins[i][j] == this.coins[i][j + 2] && this.coins[i][j] == this.coins[i][j + 3]) {
					return this.coins[i][j];
				}
			}
		}

		// check horizontal
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 6; j++) {
				if (this.coins[i][j] != 0 && this.coins[i][j] == this.coins[i + 1][j]
						&& this.coins[i][j] == this.coins[i + 2][j] && this.coins[i][j] == this.coins[i + 3][j]) {
					return this.coins[i][j];
				}
			}
		}

		// check diagonal down-right
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 3; j++) {
				if (this.coins[i][j] != 0 && this.coins[i][j] == this.coins[i + 1][j + 1]
						&& this.coins[i][j] == this.coins[i + 2][j + 2]
						&& this.coins[i][j] == this.coins[i + 3][j + 3]) {
					return this.coins[i][j];
				}
			}
		}

		// check diagonal down-left
		for (i = 3; i < 7; i++) {
			for (j = 0; j < 3; j++) {
				if (this.coins[i][j] != 0 && this.coins[i][j] == this.coins[i - 1][j + 1]
						&& this.coins[i][j] == this.coins[i - 2][j + 2]
						&& this.coins[i][j] == this.coins[i - 3][j + 3]) {
					return this.coins[i][j];
				}
			}
		}

		// nothing found
		return 0;
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