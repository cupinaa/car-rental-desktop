package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import cenovnik.Cenovnik;
import korisnici.Klijent;
import podaci.CenovniciPodaci;
import podaci.DodatneUslugePodaci;
import podaci.ModeliVozilaPodaci;
import podaci.PretplatePodaci;
import podaci.RezervacijePodaci;
import podaci.VozilaPodaci;
import rezervacija.DodatnaUsluga;
import rezervacija.Rezervacija;
import vozila.KategorijaVozila;
import vozila.ModelVozila;
import vozila.Vozilo;

public class KlijentRezervacijaPanel extends JPanel {

	private RezervacijePodaci rp;
	private ModeliVozilaPodaci mp;
	private VozilaPodaci vp;
	private DodatneUslugePodaci dup;
	private CenovniciPodaci cp;
	private PretplatePodaci pp;
	private Klijent klijent;

	private JTextField txtMarka;
	private JTextField txtNazivModela;
	private JComboBox<String> cbKategorija;
	private JTextField txtDatumOd;
	private JTextField txtDatumDo;

	private JTable tabelaModela;
	private DefaultTableModel tableModel;

	private JList<String> listUsluge;
	private JButton btnRezervisi;

	public KlijentRezervacijaPanel(RezervacijePodaci rp, ModeliVozilaPodaci mp, VozilaPodaci vp, DodatneUslugePodaci dup, CenovniciPodaci cp, PretplatePodaci pp, Klijent klijent) {
		this.rp = rp;
		this.mp = mp;
		this.vp = vp;
		this.dup = dup;
		this.cp = cp;
		this.pp = pp;
		this.klijent = klijent;

		setLayout(new BorderLayout(10, 10));

		if (klijent.podZabranom()) {
			prikaziZabranu();
			return;
		}

		kreirajTopFilterPanel();
		kreirajCenterTabeluPanel();
		kreirajEastUslugePanel();
	}

	private void prikaziZabranu() {
		setLayout(new BorderLayout());
		JLabel lblZabrana = new JLabel("Pristup Odbijen. Imate aktivnu zabranu rezervisanja na 24h zbog nedavnog otkazivanja rezervacije. Zabrana važi do: " + klijent.getZabranaRezervisanjaDo().toString(), SwingConstants.CENTER);
		add(lblZabrana, BorderLayout.CENTER);
	}

	private void kreirajTopFilterPanel() {
		JPanel filterPanel = new JPanel(new java.awt.GridLayout(3, 4, 5, 5));

		filterPanel.add(new JLabel("Proizvođač (Marka):"));
		txtMarka = new JTextField();
		filterPanel.add(txtMarka);

		filterPanel.add(new JLabel("Model:"));
		txtNazivModela = new JTextField();
		filterPanel.add(txtNazivModela);

		filterPanel.add(new JLabel("Kategorija:"));
		cbKategorija = new JComboBox<>();
		cbKategorija.addItem("SVE KATEGORIJE");
		for (KategorijaVozila kat : KategorijaVozila.values()) {
			cbKategorija.addItem(kat.toString());
		}
		filterPanel.add(cbKategorija);

		filterPanel.add(new JLabel("Datum od (YYYY-MM-DD):"));
		txtDatumOd = new JTextField(LocalDate.now().plusDays(1).toString());
		filterPanel.add(txtDatumOd);

		filterPanel.add(new JLabel("Datum do (YYYY-MM-DD):"));
		txtDatumDo = new JTextField(LocalDate.now().plusDays(4).toString());
		filterPanel.add(txtDatumDo);

		JButton btnPretrazi = new JButton("Pretraži");
		filterPanel.add(new JLabel(""));
		filterPanel.add(btnPretrazi);

		btnPretrazi.addActionListener(e -> izvrsiPretragu());

		add(filterPanel, BorderLayout.NORTH);
	}

	private void kreirajCenterTabeluPanel() {
		String[] kolone = {"ID Modela", "Proizvođač", "Model", "Kategorija"};
		tableModel = new DefaultTableModel(kolone, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabelaModela = new JTable(tableModel);

		tabelaModela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(tabelaModela);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Dostupni modeli za izabrani period"));

		add(scrollPane, BorderLayout.CENTER);
	}

	private void kreirajEastUslugePanel() {
		JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
		rightPanel.setPreferredSize(new Dimension(250, 0));

		String[] naziviUsluga = new String[dup.getUsluge().size()];
		cenovnik.Cenovnik cDanas = cp.pronadjiVazeciCenovnik(java.time.LocalDate.now());
		for (int i = 0; i < dup.getUsluge().size(); i++) {
			DodatnaUsluga du = dup.getUsluge().get(i);
			double cenaUsluge = 0.0;
			if (cDanas != null && cDanas.getCeneDodatnihUsluga() != null && cDanas.getCeneDodatnihUsluga().containsKey(du.getId())) {
				cenaUsluge = cDanas.getCeneDodatnihUsluga().get(du.getId());
			}
			naziviUsluga[i] = du.getId() + " - " + du.getDodatnaUsluga() + " (" + cenaUsluge + " RSD)";
		}
		listUsluge = new JList<>(naziviUsluga);
		listUsluge.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JScrollPane scrollList = new JScrollPane(listUsluge);
		scrollList.setBorder(BorderFactory.createTitledBorder("Dodatne Usluge (Držite CTRL)"));
		rightPanel.add(scrollList, BorderLayout.CENTER);

		btnRezervisi = new JButton("Rezerviši odabrano");
		btnRezervisi.addActionListener(e -> potvrdiRezervaciju());

		rightPanel.add(btnRezervisi, BorderLayout.SOUTH);

		add(rightPanel, BorderLayout.EAST);
	}

	private void izvrsiPretragu() {
		tableModel.setRowCount(0);

		try {
			LocalDate pocetak = LocalDate.parse(txtDatumOd.getText());
			LocalDate kraj = LocalDate.parse(txtDatumDo.getText());

			if (pocetak.isBefore(LocalDate.now())) {
				JOptionPane.showMessageDialog(this, "Datum početka ne može biti u prošlosti!", "Greška", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (kraj.isBefore(pocetak)) {
				JOptionPane.showMessageDialog(this, "Datum kraja mora biti nakon datuma početka!", "Greška", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String filterMarka = txtMarka.getText().trim().toLowerCase();
			String filterModel = txtNazivModela.getText().trim().toLowerCase();
			String filterKat = (String) cbKategorija.getSelectedItem();

			for (ModelVozila m : mp.getModeli()) {

				if (!filterMarka.isEmpty() && !m.getMarkaVozila().toLowerCase().contains(filterMarka)) continue;
				if (!filterModel.isEmpty() && !m.getNazivModela().toLowerCase().contains(filterModel)) continue;
				if (!filterKat.equals("SVE KATEGORIJE") && !m.getKategorija().toString().equals(filterKat)) continue;


				Vozilo slobodnoVozilo = rp.pronadjiSlobodnoVoziloZaModel(m, vp, pocetak, kraj);

				if (slobodnoVozilo != null) {

					Object[] red = {m.getId(), m.getMarkaVozila(), m.getNazivModela(), m.getKategorija()};
					tableModel.addRow(red);
				}
			}

			if (tableModel.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "Nema dostupnih vozila za zadate kriterijume pretrage i period.", "Informacija", JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Pogrešan format datuma! Koristite YYYY-MM-DD.", "Greška", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void potvrdiRezervaciju() {
		int selektovaniRed = tabelaModela.getSelectedRow();
		if (selektovaniRed == -1) {
			JOptionPane.showMessageDialog(this, "Molimo vas izaberite model iz tabele sa leve strane!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			LocalDate pocetak = LocalDate.parse(txtDatumOd.getText());
			LocalDate kraj = LocalDate.parse(txtDatumDo.getText());

			long brojDana = java.time.temporal.ChronoUnit.DAYS.between(pocetak, kraj);
			podaci.Podesavanja pod = new podaci.Podesavanja();
			pod.ucitaj();
			long maxDana = pod.getPodrazumevanoTrajanjeNajma();

			ArrayList<DodatnaUsluga> izabraneUsluge = new ArrayList<>();
			int[] selektovaniIndeksi = listUsluge.getSelectedIndices();
			boolean imaProduzeno = false;
			for (int i : selektovaniIndeksi) {
				DodatnaUsluga du = dup.getUsluge().get(i);
				izabraneUsluge.add(du);
				if (du.getDodatnaUsluga().toLowerCase().contains("produz") || du.getDodatnaUsluga().toLowerCase().contains("produž")) {
					imaProduzeno = true;
				}
			}

			if (!imaProduzeno && brojDana > maxDana) {
				javax.swing.JOptionPane.showMessageDialog(this, "Trajanje najma je standardno ograničeno na " + maxDana + " dana.\nIzaberite uslugu produženog korišćenja ako želite da zadržite vozilo duže.", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idModela = (int) tableModel.getValueAt(selektovaniRed, 0);
			ModelVozila model = mp.pronadjiModel(idModela);


			Vozilo slobodnoVozilo = rp.pronadjiSlobodnoVoziloZaModel(model, vp, pocetak, kraj);
			if (slobodnoVozilo == null) {
				javax.swing.JOptionPane.showMessageDialog(this, "U međuvremenu je vozilo zauzeto, osvežite pretragu.", "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
				return;
			}

			Rezervacija privremena = new Rezervacija(klijent, slobodnoVozilo, pocetak, kraj, izabraneUsluge);
			Cenovnik aktuelniCenovnik = cp.pronadjiVazeciCenovnik(pocetak);
			double ukupnaCena = rp.izracunajUkupnuCenu(privremena, aktuelniCenovnik);

			if (ukupnaCena == 0.0) {
				JOptionPane.showMessageDialog(this, "Cenovnik za odabrani datum nije definisan!", "Greška", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String poruka = "Ukupna cena najma (sa popustima) iznosi: " + ukupnaCena + " RSD\nDa li želite da potvrdite rezervaciju?";
			Object[] opcije = {"Da", "Ne"};
			int odziv = JOptionPane.showOptionDialog(this, poruka, "Potvrda rezervacije", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcije, opcije[0]);

			if (odziv == JOptionPane.YES_OPTION) {
				Rezervacija novaRez = rp.napraviRezervaciju(klijent, slobodnoVozilo, pocetak, kraj, izabraneUsluge, aktuelniCenovnik, pp);
				if (novaRez != null) {
					JOptionPane.showMessageDialog(this, "Rezervacija uspešno kreirana! Vaš zahtev je u statusu: NA ČEKANJU.");
					tableModel.setRowCount(0); 
				} else {
					JOptionPane.showMessageDialog(this, "Vaš zahtev je odbijen od strane sistema. Proverite da li imate aktivnu pretplatu i minimum 2 godine vozačkog staža.", "Rezervacija Odbijena", JOptionPane.ERROR_MESSAGE);
				}
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Greška prilikom kreiranja rezervacije.", "Greška", JOptionPane.ERROR_MESSAGE);
		}
	}
}
