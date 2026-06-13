package gui;

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

	public RezervacijePanel(RezervacijePodaci rp) {
		this.rp = rp;
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
	}
	
	private void promeniStatus(StatusRezervacije noviStatus) {
		int selektovaniRed = tabela.getSelectedRow();
		if (selektovaniRed == -1) {
			javax.swing.JOptionPane.showMessageDialog(this, "Morate selektovati rezervaciju!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int idRezervacije = (int) tableModel.getValueAt(selektovaniRed, 0);
		Rezervacija r = rp.pronadjiRezervaciju(idRezervacije);
		
		if (r != null) {
			if (r.getStatusRezervacije() != StatusRezervacije.NA_ČEKANJU) {
				javax.swing.JOptionPane.showMessageDialog(this, "Možete menjati status samo rezervacijama koje su na čekanju!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Ako odobravamo, moramo prvo proveriti da li se vozilo u medjuvremenu zauzelo
			if (noviStatus == StatusRezervacije.ODOBRENA) {
				if (!rp.daLiJeVoziloSlobodno(r.getVozilo(), r.getDatumPocetka(), r.getDatumKraja())) {
					javax.swing.JOptionPane.showMessageDialog(this, "Vozilo je već zauzeto u ovom terminu! Rezervacija se mora odbiti.", "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
					rp.promeniStatusRezervacije(r, StatusRezervacije.ODBIJENA);
					osveziTabelu();
					return;
				}
			}
			
			rp.promeniStatusRezervacije(r, noviStatus);
			osveziTabelu();
			javax.swing.JOptionPane.showMessageDialog(this, "Status rezervacije uspešno izmenjen u " + noviStatus + ".");
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
