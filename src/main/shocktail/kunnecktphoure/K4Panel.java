package shocktail.kunnecktphoure;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class K4Panel extends JPanel {
	private byte[][] coins; // 0 is empty, 1 is black, 2 is red
	private boolean turn; // true is black, false is red
	private JButton[] buttons;

	public K4Panel() {
		this.setLayout(null);
		this.coins = new byte[7][6];
		this.buttons = new JButton[7];
		this.turn = true;
		for (byte i = 0; i < 7; i++) {
			this.buttons[i] = new JButton();
			this.buttons[i].addActionListener(new K4Panel.TheListener(i));
			this.buttons[i].setSize(60, 60);
			this.buttons[i].setLocation(i * 60 + 15, 400);
			this.add(this.buttons[i]);
		}
		this.setSize(440, 500);
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
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * checks to see if a piece is in a four-in-a-row
	 *
	 * @param col the column of the piece
	 * @param row the row of the piece
	 * @return 0 if no winner, 1 if black, 2 if red
	 */
	private byte checkForWin() {
		// check vertical
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
				if (this.coins[i][j] != 0 && this.coins[i][j] == this.coins[i][j + 1]
						&& this.coins[i][j] == this.coins[i][j + 2] && this.coins[i][j] == this.coins[i][j + 3]) {
					return this.coins[i][j];
				}
			}
		}

		// check horizontal
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				if (this.coins[i][j] != 0 && this.coins[i][j] == this.coins[i + 1][j]
						&& this.coins[i][j] == this.coins[i + 2][j] && this.coins[i][j] == this.coins[i + 3][j]) {
					return this.coins[i][j];
				}
			}
		}

		// check diagonal down-right
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (this.coins[i][j] != 0 && this.coins[i][j] == this.coins[i + 1][j + 1]
						&& this.coins[i][j] == this.coins[i + 2][j + 2]
						&& this.coins[i][j] == this.coins[i + 3][j + 3]) {
					return this.coins[i][j];
				}
			}
		}

		// check diagonal down-left
		for (int i = 3; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
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
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++) {
				// draw box
				g.setColor(Color.GRAY);
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
			}
		}
	}

	private class TheListener implements ActionListener {
		private byte col;

		public TheListener(byte col) {
			this.col = col;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (K4Panel.this.placeCoin(this.col)) {
				K4Panel.this.repaint();
				byte win = K4Panel.this.checkForWin();
				switch (win) {
				case 1:
					JOptionPane.showMessageDialog(null, "Black wins");
					break;
				case 2:
					JOptionPane.showMessageDialog(null, "Red wins");
				}
			}
		}
	}
}