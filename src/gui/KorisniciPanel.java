package gui;
import javax.swing.JOptionPane;
import korisnici.Zaposleni;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import podaci.KorisniciPodaci;
import korisnici.Korisnik;
import korisnici.Administrator;
import korisnici.Agent;
import korisnici.Klijent;

public class KorisniciPanel extends JPanel {

	private KorisniciPodaci kp;
	private JTable tabela;
	private DefaultTableModel tableModel;

	private Korisnik ulogovaniKorisnik;
	private boolean prikaziKlijente;

	public KorisniciPanel(KorisniciPodaci kp, Korisnik ulogovaniKorisnik, boolean prikaziKlijente) {
		this.kp = kp;
		this.ulogovaniKorisnik = ulogovaniKorisnik;
		this.prikaziKlijente = prikaziKlijente;
		setLayout(new BorderLayout()); 

		String[] kolone;
		if (prikaziKlijente) {
			kolone = new String[]{"ID", "Ime", "Prezime", "E-mail", "Kategorija", "Zabrana do", "Datum Vozacke"};
		} else {
			kolone = new String[]{"ID", "Uloga", "Ime", "Prezime", "E-mail", "Plata"};
		}
		tableModel = new DefaultTableModel(kolone, 0); 
		tabela = new JTable(tableModel);

		osveziTabelu();

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER); 

		JPanel panelDugmici = new JPanel();
		JButton btnDodaj = new JButton("Dodaj");
		JButton btnIzmeni = new JButton("Izmeni");
		JButton btnObrisi = new JButton("Obriši");

		panelDugmici.add(btnDodaj);
		panelDugmici.add(btnIzmeni);
		panelDugmici.add(btnObrisi);
		add(panelDugmici, BorderLayout.SOUTH);

		btnDodaj.addActionListener(e -> {
			KorisnikForma kf = new KorisnikForma(kp, null, () -> osveziTabelu(), ulogovaniKorisnik, prikaziKlijente); 
			kf.setVisible(true); 
		});

		btnObrisi.addActionListener(e -> {
			int selektovaniRed = tabela.getSelectedRow();
			if (selektovaniRed == -1) {
				JOptionPane.showMessageDialog(this, "Morate prvo selektovati korisnika iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idKorisnika = (int) tableModel.getValueAt(selektovaniRed, 0);
			Korisnik zaBrisanje = null;
			for (Korisnik k : kp.getKorisnici()) {
				if (k.getId() == idKorisnika) { zaBrisanje = k; break; }
			}

			if (zaBrisanje != null) {
				Object[] opcije = {"Da", "Ne"};
				int potvrda = JOptionPane.showOptionDialog(this,
						"Da li ste sigurni da želite da obrišete korisnika: " + zaBrisanje.getKorisnickoIme() + "?",
						"Potvrda brisanja",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						opcije,
						opcije[1]);
				if (potvrda == JOptionPane.YES_OPTION) {
					kp.obrisiKorisnika(zaBrisanje); 
					osveziTabelu(); 
				}
			}
		});

		btnIzmeni.addActionListener(e -> {
			int selektovaniRed = tabela.getSelectedRow();
			if (selektovaniRed == -1) {
				JOptionPane.showMessageDialog(this, "Morate prvo selektovati korisnika iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idKorisnika = (int) tableModel.getValueAt(selektovaniRed, 0);
			Korisnik zaIzmenu = null;
			for (Korisnik k : kp.getKorisnici()) {
				if (k.getId() == idKorisnika) { zaIzmenu = k; break; }
			}

			if (zaIzmenu != null) {

				KorisnikForma kf = new KorisnikForma(kp, zaIzmenu, this::osveziTabelu, ulogovaniKorisnik, prikaziKlijente);
				kf.setVisible(true);
			}
		});
	}

	private void osveziTabelu() {
		tableModel.setRowCount(0); 
		for (Korisnik k : kp.getKorisnici()) {
			if (prikaziKlijente && !(k instanceof Klijent)) continue;
			if (!prikaziKlijente && (k instanceof Klijent)) continue;

			String uloga = k.getClass().getSimpleName(); 

			if (prikaziKlijente) {
				Klijent kl = (Klijent) k;
				String kategorija = kl.getKategorijaKlijenata() != null ? kl.getKategorijaKlijenata().name() : "NEMA";
				String zabrana = kl.getZabranaRezervisanjaDo() != null ? kl.getZabranaRezervisanjaDo().toString() : "-";
				Object[] red = { kl.getId(), kl.getIme(), kl.getPrezime(), kl.getKorisnickoIme(), kategorija, zabrana, kl.getDatumIzdavanjaVozacke().toString() };
				tableModel.addRow(red);
			} else {
				String plata = "-";
				if (k instanceof Zaposleni) {
					plata = String.valueOf(((Zaposleni) k).getPlata());
				}
				Object[] red = { k.getId(), uloga, k.getIme(), k.getPrezime(), k.getKorisnickoIme(), plata };
				tableModel.addRow(red);
			}
		}
	}
}
