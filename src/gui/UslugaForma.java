package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import podaci.DodatneUslugePodaci;
import rezervacija.DodatnaUsluga;

public class UslugaForma extends JDialog {

	private DodatneUslugePodaci dup;
	private Runnable naUspesnoDodavanje;
	private DodatnaUsluga duZaIzmenu;
	
	private JTextField txtNaziv;

	public UslugaForma(DodatneUslugePodaci dup, DodatnaUsluga duZaIzmenu, Runnable naUspesnoDodavanje) {
		this.dup = dup;
		this.naUspesnoDodavanje = naUspesnoDodavanje;
		this.duZaIzmenu = duZaIzmenu;
		
		setTitle(duZaIzmenu == null ? "Nova usluga" : "Izmena usluge");
		setSize(300, 120);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(2, 2, 5, 5));
		
		add(new JLabel("Naziv usluge:"));
		txtNaziv = new JTextField();
		add(txtNaziv);
		
		JButton btnSacuvaj = new JButton("Sačuvaj");
		add(new JLabel("")); 
		add(btnSacuvaj);
		
		if (duZaIzmenu != null) {
			popuniPolja();
		}
		
		btnSacuvaj.addActionListener(e -> sacuvajUslugu());
	}
	
	private void popuniPolja() {
		txtNaziv.setText(duZaIzmenu.getDodatnaUsluga());
	}

	private void sacuvajUslugu() {
		try {
			String naziv = txtNaziv.getText();
			
			if (naziv.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Morate uneti naziv usluge!", "Greška", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (duZaIzmenu == null) {
				DodatnaUsluga du = new DodatnaUsluga(0, naziv);
				dup.dodajUslugu(du);
				JOptionPane.showMessageDialog(this, "Uspešno dodato!");
			} else {
				duZaIzmenu.setDodatnaUsluga(naziv);
				dup.sacuvajIzmene();
				JOptionPane.showMessageDialog(this, "Uspešno izmenjeno!");
			}
			
			naUspesnoDodavanje.run();
			dispose();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Greška prilikom čuvanja!", "Greška", JOptionPane.ERROR_MESSAGE);
		}
	}
}
