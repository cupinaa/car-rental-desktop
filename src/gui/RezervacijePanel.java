package gui;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import podaci.RezervacijePodaci;
import rezervacija.Rezervacija;
import rezervacija.StatusRezervacije;

public class RezervacijePanel extends JPanel {

	private RezervacijePodaci rp;
	private JTable tabela;
	private DefaultTableModel tableModel;
	private korisnici.Korisnik ulogovaniKorisnik;

	public RezervacijePanel(RezervacijePodaci rp, korisnici.Korisnik ulogovaniKorisnik) {
		this.rp = rp;
		this.ulogovaniKorisnik = ulogovaniKorisnik;
		setLayout(new BorderLayout()); 

		String[] kolone = {"ID", "Klijent", "Vozilo", "Od - Do", "Cena", "Status"};
		tableModel = new DefaultTableModel(kolone, 0); 
		tabela = new JTable(tableModel);

		osveziTabelu();

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER); 

		JPanel panelDugmici = new JPanel();
		JButton btnOdobri = new JButton("Odobri");
		JButton btnOdbij = new JButton("Odbij");

		panelDugmici.add(btnOdobri);
		panelDugmici.add(btnOdbij);
		add(panelDugmici, BorderLayout.SOUTH);

		btnOdobri.addActionListener(e -> promeniStatus(StatusRezervacije.ODOBRENA));
		btnOdbij.addActionListener(e -> promeniStatus(StatusRezervacije.ODBIJENA));

		if (ulogovaniKorisnik instanceof korisnici.Administrator) {
			JButton btnIzmeni = new JButton("Izmeni");
			JButton btnObrisi = new JButton("Obrisi");
			panelDugmici.add(btnIzmeni);
			panelDugmici.add(btnObrisi);

			btnIzmeni.addActionListener(e -> {
				int red = tabela.getSelectedRow();
				if (red == -1) { JOptionPane.showMessageDialog(this, "Odaberite rezervaciju."); return; }
				int id = (int) tableModel.getValueAt(red, 0);
				Rezervacija r = rp.pronadjiRezervaciju(id);
				if (r != null) { new RezervacijaForma(rp, r, this::osveziTabelu).setVisible(true); }
			});

			btnObrisi.addActionListener(e -> {
				int red = tabela.getSelectedRow();
				if (red == -1) { JOptionPane.showMessageDialog(this, "Odaberite rezervaciju."); return; }
				int id = (int) tableModel.getValueAt(red, 0);
				Rezervacija r = rp.pronadjiRezervaciju(id);
				if (r != null && JOptionPane.showConfirmDialog(this, "Sigurno?", "Potvrda", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					rp.getRezervacije().remove(r);
					try { rp.upisi("rezervacije.csv"); osveziTabelu(); } catch (Exception ex) {}
				}
			});
		}
	}

	private void promeniStatus(StatusRezervacije noviStatus) {
		int selektovaniRed = tabela.getSelectedRow();
		if (selektovaniRed == -1) {
			JOptionPane.showMessageDialog(this, "Morate selektovati rezervaciju!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int idRezervacije = (int) tableModel.getValueAt(selektovaniRed, 0);
		Rezervacija r = rp.pronadjiRezervaciju(idRezervacije);

		if (r != null) {
			if (r.getStatusRezervacije() != StatusRezervacije.NA_CEKANJU) {
				JOptionPane.showMessageDialog(this, "MoĹľete menjati status samo rezervacijama koje su na ÄŤekanju!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
				return;
			}


			if (noviStatus == StatusRezervacije.ODOBRENA) {
				if (!rp.daLiJeVoziloSlobodno(r.getVozilo(), r.getDatumPocetka(), r.getDatumKraja(), r.getId())) {
					JOptionPane.showMessageDialog(this, "Vozilo je vec zauzeto u ovom terminu! Rezervacija se mora odbiti.", "Greska", JOptionPane.ERROR_MESSAGE);
					rp.promeniStatusRezervacije(r, StatusRezervacije.ODBIJENA);
					osveziTabelu();
					return;
				}
			}

			rp.promeniStatusRezervacije(r, noviStatus);
			osveziTabelu();
			JOptionPane.showMessageDialog(this, "Status rezervacije uspeĹˇno izmenjen u " + noviStatus + ".");
		}
	}

	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (Rezervacija r : rp.getRezervacije()) {
			String klijentInfo = r.getKlijent().getIme() + " " + r.getKlijent().getPrezime();
			String voziloInfo = r.getVozilo().getModelVozila().getMarkaVozila() + " " + r.getVozilo().getModelVozila().getNazivModela();
			String period = r.getDatumPocetka() + " do " + r.getDatumKraja();

			Object[] red = { 
				r.getId(), 
				klijentInfo, 
				voziloInfo, 
				period, 
				r.getUkupnaCena(), 
				r.getStatusRezervacije().toString() 
			};
			tableModel.addRow(red);
		}
	}
}
