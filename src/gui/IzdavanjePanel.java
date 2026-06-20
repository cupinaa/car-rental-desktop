package gui;
import cenovnik.Cenovnik;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import podaci.Podesavanja;
import rezervacija.DodatnaUsluga;
import vozila.StatusVozila;
import vozila.Vozilo;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import korisnici.Agent;
import podaci.CenovniciPodaci;
import podaci.DodatneUslugePodaci;
import podaci.IzdavanjePodaci;
import podaci.RezervacijePodaci;
import podaci.VozilaPodaci;
import rezervacija.Rezervacija;
import rezervacija.StatusRezervacije;

public class IzdavanjePanel extends JPanel {

	private RezervacijePodaci rp;
	private IzdavanjePodaci ip;
	private VozilaPodaci vp;
	private DodatneUslugePodaci dup;
	private CenovniciPodaci cp;
	private Agent ulogovaniAgent;
	private JTable tabela;
	private DefaultTableModel tableModel;

	public IzdavanjePanel(RezervacijePodaci rp, IzdavanjePodaci ip, VozilaPodaci vp, DodatneUslugePodaci dup, CenovniciPodaci cp, Agent ulogovaniAgent) {
		this.rp = rp;
		this.ip = ip;
		this.vp = vp;
		this.dup = dup;
		this.cp = cp;
		this.ulogovaniAgent = ulogovaniAgent;

		setLayout(new BorderLayout()); 

		String[] kolone = {"ID Rez.", "Klijent", "Vozilo", "Datum Od - Do", "Ukupna Cena"};
		tableModel = new DefaultTableModel(kolone, 0); 
		tabela = new JTable(tableModel);

		osveziTabelu();

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER); 

		JPanel panelDugmici = new JPanel();
		JButton btnIzdaj = new JButton("Izdaj Vozilo");

		panelDugmici.add(btnIzdaj);
		add(panelDugmici, BorderLayout.SOUTH);

		btnIzdaj.addActionListener(e -> izdajVozilo());
	}

	private void izdajVozilo() {
		int selektovaniRed = tabela.getSelectedRow();
		if (selektovaniRed == -1) {
			JOptionPane.showMessageDialog(this, "Morate selektovati odobrenu rezervaciju!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int idRezervacije = (int) tableModel.getValueAt(selektovaniRed, 0);
		Rezervacija r = rp.pronadjiRezervaciju(idRezervacije);

		if (r != null) {


			ArrayList<Vozilo> slobodniPrimerci = new ArrayList<>();
			for (Vozilo v : vp.getVozila()) {
				if (v.getModelVozila().getId() == r.getVozilo().getModelVozila().getId()) {
					if (rp.daLiJeVoziloSlobodno(v, r.getDatumPocetka(), r.getDatumKraja()) || v.getId() == r.getVozilo().getId()) {
						slobodniPrimerci.add(v);
					}
				}
			}

			if (slobodniPrimerci.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Nema slobodnih primeraka za izdavanje!", "Greška", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String[] opcijePrimeraka = new String[slobodniPrimerci.size()];
			for (int i=0; i<slobodniPrimerci.size(); i++) {
				Vozilo v = slobodniPrimerci.get(i);
				opcijePrimeraka[i] = v.getRegistarskeTablice();
			}


			String izabraniPrimerakStr = (String) JOptionPane.showInputDialog(
					this, 
					"Izaberite konkretan primerak za izdavanje:", 
					"Odabir primerka", 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					opcijePrimeraka, 
					opcijePrimeraka[0]
			);

			if (izabraniPrimerakStr == null) return;

			int indeksPrimerka = -1;
			for (int i=0; i<opcijePrimeraka.length; i++) {
				if (opcijePrimeraka[i].equals(izabraniPrimerakStr)) {
					indeksPrimerka = i; break;
				}
			}

			Vozilo konacnoIzabranoVozilo = slobodniPrimerci.get(indeksPrimerka);

			String unos = JOptionPane.showInputDialog(this, "Unesite početnu kilometražu vozila (" + konacnoIzabranoVozilo.getRegistarskeTablice() + "):");
			if (unos != null && !unos.trim().isEmpty()) {
				try {
					double pocetnaK = Double.parseDouble(unos);
					if (pocetnaK < 0) {
						JOptionPane.showMessageDialog(this, "Kilometraža ne može biti negativna!", "Greška", JOptionPane.ERROR_MESSAGE);
						return;
					}

					Object[] opcijeDaNe = {"Da", "Ne"};
					int odzivUsluge = JOptionPane.showOptionDialog(this, "Da li klijent želi da doda neku dodatnu uslugu?", "Dodatne Usluge", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcijeDaNe, opcijeDaNe[0]);
					if (odzivUsluge == 0) {
						ArrayList<String> opcije = new ArrayList<>();
						ArrayList<DodatnaUsluga> dostupne = new ArrayList<>();
						Cenovnik danasnjiCenovnik = cp.pronadjiVazeciCenovnik(LocalDate.now());

						for (DodatnaUsluga du : dup.getUsluge()) {
							boolean vecIma = false;
							for (DodatnaUsluga postojeca : r.getDodatneUsluge()) {
								if (postojeca.getId() == du.getId()) vecIma = true;
							}
							if (!vecIma) {
								double cenaDanas = 0.0;
								if (danasnjiCenovnik != null && danasnjiCenovnik.getCeneDodatnihUsluga() != null && danasnjiCenovnik.getCeneDodatnihUsluga().containsKey(du.getId())) {
									cenaDanas = danasnjiCenovnik.getCeneDodatnihUsluga().get(du.getId());
								}
								opcije.add(du.getDodatnaUsluga() + " (" + cenaDanas + " RSD)");
								dostupne.add(du);
							}
						}

						if (!opcije.isEmpty()) {
							JList<String> lista = new JList<>(opcije.toArray(new String[0]));
							lista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
							JOptionPane.showMessageDialog(this, new JScrollPane(lista), "Izaberite usluge", JOptionPane.PLAIN_MESSAGE);

							int[] sel = lista.getSelectedIndices();
							if (sel.length > 0) {
								for (int idx : sel) {
									DodatnaUsluga novaUsluga = dostupne.get(idx);
									r.getDodatneUsluge().add(novaUsluga);

									double cenaDanas = 0.0;
									if (danasnjiCenovnik != null && danasnjiCenovnik.getCeneDodatnihUsluga() != null && danasnjiCenovnik.getCeneDodatnihUsluga().containsKey(novaUsluga.getId())) {
										cenaDanas = danasnjiCenovnik.getCeneDodatnihUsluga().get(novaUsluga.getId());
									}

									String imeUsluge = novaUsluga.getDodatnaUsluga().toLowerCase();
									if (imeUsluge.contains("produženo") || imeUsluge.contains("produzeno")) {
										Podesavanja p = new Podesavanja();
										p.ucitaj();
										long podrazumevano = p.getPodrazumevanoTrajanjeNajma();
										long brojDana = ChronoUnit.DAYS.between(r.getDatumPocetka(), r.getDatumKraja());
										long dodatniDani = brojDana - podrazumevano;
										if (dodatniDani > 0) {
											r.setUkupnaCena(r.getUkupnaCena() + (cenaDanas * dodatniDani));
										}
									} else {
										r.setUkupnaCena(r.getUkupnaCena() + cenaDanas);
									}
								}
							}
						} else {
							JOptionPane.showMessageDialog(this, "Sve dostupne usluge su već izabrane.");
						}
					}


					if (r.getVozilo().getId() != konacnoIzabranoVozilo.getId()) {

						r.getVozilo().setStatusVozila(StatusVozila.RASPOLOZIVO);

						r.setVozilo(konacnoIzabranoVozilo);
					}


					r.setStatusRezervacije(StatusRezervacije.REALIZOVANA);
					rp.sacuvajIzmene();


					ip.izdajVozilo(r, ulogovaniAgent, pocetnaK);

					JOptionPane.showMessageDialog(this, "Vozilo uspešno izdato klijentu!");
					osveziTabelu();

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(this, "Morate uneti validan broj za kilometražu!", "Greška", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (Rezervacija r : rp.getRezervacije()) {

			if (r.getStatusRezervacije() == StatusRezervacije.ODOBRENA) {
				String klijentInfo = r.getKlijent().getIme() + " " + r.getKlijent().getPrezime();
				String voziloInfo = r.getVozilo().getModelVozila().getMarkaVozila() + " " + r.getVozilo().getModelVozila().getNazivModela();
				String period = r.getDatumPocetka() + " do " + r.getDatumKraja();

				Object[] red = { 
					r.getId(), 
					klijentInfo, 
					voziloInfo, 
					period, 
					r.getUkupnaCena()
				};
				tableModel.addRow(red);
			}
		}
	}
}
