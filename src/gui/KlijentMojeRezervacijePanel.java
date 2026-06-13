package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import korisnici.Klijent;
import podaci.CenovniciPodaci;
import podaci.KorisniciPodaci;
import podaci.RezervacijePodaci;
import rezervacija.DodatnaUsluga;
import rezervacija.Rezervacija;
import rezervacija.StatusRezervacije;

public class KlijentMojeRezervacijePanel extends JPanel {

	private RezervacijePodaci rp;
	private KorisniciPodaci kp;
	private CenovniciPodaci cp;
	private Klijent ulogovaniKlijent;
	
	private JTable tabela;
	private DefaultTableModel tableModel;

	public KlijentMojeRezervacijePanel(RezervacijePodaci rp, KorisniciPodaci kp, CenovniciPodaci cp, Klijent ulogovaniKlijent) {
		this.rp = rp;
		this.kp = kp;
		this.cp = cp;
		this.ulogovaniKlijent = ulogovaniKlijent;
		
		setLayout(new BorderLayout()); 
		
		String[] kolone = {"ID", "Vozilo", "Period", "Potrošeno na najam", "Potrošeno na usluge/kazne", "Ukupno za plaćanje", "Status"};
		tableModel = new DefaultTableModel(kolone, 0); 
		tabela = new JTable(tableModel);
		
		osveziTabelu();
		
		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER); 
		
		JPanel panelDugmici = new JPanel();
		JButton btnOtkazi = new JButton("Otkaži Rezervaciju");
		
		panelDugmici.add(btnOtkazi);
		add(panelDugmici, BorderLayout.SOUTH);
		
		btnOtkazi.addActionListener(e -> {
			int selektovaniRed = tabela.getSelectedRow();
			if (selektovaniRed == -1) {
				JOptionPane.showMessageDialog(this, "Morate prvo selektovati rezervaciju iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			int idRezervacije = (int) tableModel.getValueAt(selektovaniRed, 0);
			Rezervacija r = rp.pronadjiRezervaciju(idRezervacije);
			
			if (r.getStatusRezervacije() == StatusRezervacije.NA_ČEKANJU || r.getStatusRezervacije() == StatusRezervacije.ODOBRENA) {
				Object[] opcije = {"Da", "Ne"};
				int potvrda = JOptionPane.showOptionDialog(this,
						"Da li ste sigurni da želite da otkažete rezervaciju? (Bićete blokirani za nove rezervacije naredna 24h)",
						"Potvrda otkazivanja",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						opcije,
						opcije[1]);
				if (potvrda == JOptionPane.YES_OPTION) {
					r.setStatusRezervacije(StatusRezervacije.OTKAZANA);
					
					ulogovaniKlijent.setZabranaRezervisanjaDo(java.time.LocalDateTime.now().plusHours(24));
					kp.sacuvajIzmene();
					rp.sacuvajIzmene();
					
					JOptionPane.showMessageDialog(this, "Rezervacija uspešno otkazana. Nećete moći da rezervišete naredna 24 časa.");
					osveziTabelu(); 
				}
			} else {
				JOptionPane.showMessageDialog(this, "Možete otkazati samo rezervacije u statusu NA_ČEKANJU ili ODOBRENA.", "Greška", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
	
	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (Rezervacija r : rp.getRezervacije()) {
			if (r.getKlijent().getId() == ulogovaniKlijent.getId()) {
				
				String voziloInfo = r.getVozilo().getModelVozila().getMarkaVozila() + " " + r.getVozilo().getModelVozila().getNazivModela();
				String period = r.getDatumPocetka() + " do " + r.getDatumKraja();
				
				double cenaUsluga = 0;
				cenovnik.Cenovnik cZaR = cp.pronadjiVazeciCenovnik(r.getDatumPocetka());
				for (DodatnaUsluga du : r.getDodatneUsluge()) {
					double cUsl = 0;
					if (cZaR != null && cZaR.getCeneDodatnihUsluga() != null && cZaR.getCeneDodatnihUsluga().containsKey(du.getId())) {
						cUsl = cZaR.getCeneDodatnihUsluga().get(du.getId());
					}
					
					String naziv = du.getDodatnaUsluga().toLowerCase();
					if (naziv.contains("produženo") || naziv.contains("produzeno")) {
						long brojDana = java.time.temporal.ChronoUnit.DAYS.between(r.getDatumPocetka(), r.getDatumKraja());
						if (brojDana <= 0) brojDana = 1;
						cenaUsluga += cUsl * brojDana; 
					} else {
						cenaUsluga += cUsl;
					}
				}
				
				double cenaNajma = r.getUkupnaCena() - cenaUsluga; 
				
				Object[] red = { 
					r.getId(), 
					voziloInfo, 
					period, 
					String.format("%.2f RSD", cenaNajma), 
					String.format("%.2f RSD", cenaUsluga), 
					String.format("%.2f RSD", r.getUkupnaCena()), 
					r.getStatusRezervacije() 
				};
				tableModel.addRow(red);
			}
		}
	}
}
