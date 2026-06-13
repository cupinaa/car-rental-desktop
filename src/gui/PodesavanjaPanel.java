package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import podaci.Podesavanja;

public class PodesavanjaPanel extends JPanel {

	private Podesavanja p;

	public PodesavanjaPanel(Podesavanja p) {
		this.p = p;
		setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		
		centerPanel.add(new JLabel("Podrazumevano trajanje najma (u danima):"));
		JTextField txtTrajanje = new JTextField(10);
		txtTrajanje.setText(String.valueOf(p.getPodrazumevanoTrajanjeNajma()));
		centerPanel.add(txtTrajanje);

		JButton btnSacuvaj = new JButton("Sačuvaj");
		centerPanel.add(btnSacuvaj);

		add(centerPanel, BorderLayout.NORTH);

		btnSacuvaj.addActionListener(e -> {
			try {
				int novoTrajanje = Integer.parseInt(txtTrajanje.getText().trim());
				if (novoTrajanje <= 0) {
					JOptionPane.showMessageDialog(this, "Trajanje najma mora biti pozitivan broj!", "Greška", JOptionPane.ERROR_MESSAGE);
					return;
				}
				p.setPodrazumevanoTrajanjeNajma(novoTrajanje);
				JOptionPane.showMessageDialog(this, "Podešavanja su uspešno sačuvana!");
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Molimo unesite validan ceo broj!", "Greška", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
