package shocktail.kunnecktphoure;

import javax.swing.JFrame;

public class KunnecktPhoure {
	public static void main(String[] args) {
		JFrame window = new JFrame("KunnecktPhoure");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		window.setSize(464, 500);
		window.setResizable(false);
		window.add(new K4Panel());
		window.setVisible(true);
	}
}