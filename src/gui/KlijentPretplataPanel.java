package gui;
import korisnici.StatusPretplate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import korisnici.Klijent;
import korisnici.Pretplata;
import podaci.PretplatePodaci;

public class KlijentPretplataPanel extends JPanel {

	private PretplatePodaci pp;
	private Klijent klijent;

	private JLabel lblNaslov;
	private JLabel lblStatus;
	private JLabel lblIstek;
	private JButton btnPodnesi;

	public KlijentPretplataPanel(PretplatePodaci pp, Klijent klijent) {
		this.pp = pp;
		this.klijent = klijent;

		setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;

		lblNaslov = new JLabel("Status Vaše Pretplate", SwingConstants.CENTER);
		centerPanel.add(lblNaslov, gbc);

		gbc.gridy++;
		lblStatus = new JLabel("", SwingConstants.CENTER);
		centerPanel.add(lblStatus, gbc);

		gbc.gridy++;
		lblIstek = new JLabel("", SwingConstants.CENTER);
		centerPanel.add(lblIstek, gbc);

		gbc.gridy++;
		btnPodnesi = new JButton("Podnesi zahtev za novu pretplatu");
		centerPanel.add(btnPodnesi, gbc);

		add(centerPanel, BorderLayout.CENTER);

		btnPodnesi.addActionListener(e -> podnesiZahtev());

		osveziPrikaz();
	}

	private void osveziPrikaz() {
		Pretplata p = pp.pronadjiPretplatuZaKlijenta(klijent.getId());

		if (p == null) {
			lblStatus.setText("Trenutno nemate aktivnu pretplatu.");
			lblIstek.setText(" ");
			btnPodnesi.setEnabled(true);
		} else {
			lblStatus.setText("Status: " + p.getStatus());
			lblIstek.setText("Važi do: " + p.getDatumIsteka());

			if (p.getStatus() == StatusPretplate.AKTIVNA || p.getStatus() == StatusPretplate.CEKA_ODOBRENJE) {
				btnPodnesi.setEnabled(false);
			} else {
				btnPodnesi.setEnabled(true);
			}
		}
	}

	private void podnesiZahtev() {
		Object[] opcije = {"Da", "Ne"};
		int odziv = JOptionPane.showOptionDialog(this, 
			"Da li ste sigurni da želite da podnesete zahtev za novu pretplatu?", 
			"Podnošenje zahteva", 
			JOptionPane.YES_NO_OPTION, 
			JOptionPane.QUESTION_MESSAGE, 
			null, 
			opcije, 
			opcije[0]
		);

		if (odziv == 0) {
			pp.podnesiZahtevZaPretplatu(klijent);

			Pretplata azurirana = pp.pronadjiPretplatuZaKlijenta(klijent.getId());
			if (azurirana != null && azurirana.getStatus() == StatusPretplate.ODBIJENA) {
				JOptionPane.showMessageDialog(this, "Vaš zahtev je AUTOMATSKI ODBIJEN zbog prevelikog broja kašnjenja (" + klijent.getBrojKasnjenja() + ")!", "Zahtev odbijen", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Zahtev je uspešno podnet! Očekujte odobrenje od strane agenta.");
			}
			osveziPrikaz();
		}
	}
}
