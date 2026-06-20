package gui;
import cenovnik.Cenovnik;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import rezervacija.StatusRezervacije;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import izdavanje.Izdavanje;
import korisnici.Agent;
import korisnici.Korisnik;
import korisnici.Pretplata;
import korisnici.StatusPretplate;
import korisnici.Zaposleni;
import podaci.CenovniciPodaci;
import podaci.IzdavanjePodaci;
import podaci.KorisniciPodaci;
import podaci.ModeliVozilaPodaci;
import podaci.PretplatePodaci;
import podaci.RezervacijePodaci;
import rezervacija.Rezervacija;
import vozila.ModelVozila;

public class IzvestajiPanel extends JPanel {

	private IzdavanjePodaci ip;
	private RezervacijePodaci rp;
	private ModeliVozilaPodaci mp;
	private KorisniciPodaci kp;
	private PretplatePodaci pp;
	private CenovniciPodaci cp;

	private JTextField txtDatumOd;
	private JTextField txtDatumDo;

	private JTextArea txtIzdavanja;
	private JTextArea txtRezervacije;
	private JTextArea txtModeli;
	private JTextArea txtPrihodi;

	public IzvestajiPanel(IzdavanjePodaci ip, RezervacijePodaci rp, ModeliVozilaPodaci mp, KorisniciPodaci kp, PretplatePodaci pp, CenovniciPodaci cp) {
		this.ip = ip;
		this.rp = rp;
		this.mp = mp;
		this.kp = kp;
		this.pp = pp;
		this.cp = cp;

		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("Od datuma (YYYY-MM-DD):"));
		txtDatumOd = new JTextField(LocalDate.now().minusMonths(1).toString(), 10);
		topPanel.add(txtDatumOd);
		topPanel.add(new JLabel("Do datuma:"));
		txtDatumDo = new JTextField(LocalDate.now().toString(), 10);
		topPanel.add(txtDatumDo);
		JButton btnPrikazi = new JButton("Generiši Izveštaje");
		topPanel.add(btnPrikazi);
		add(topPanel, BorderLayout.NORTH);

		JTabbedPane tabbedPane = new JTabbedPane();

		txtIzdavanja = createTextArea();
		tabbedPane.addTab("Izdavanja po Agentu", new JScrollPane(txtIzdavanja));

		txtRezervacije = createTextArea();
		tabbedPane.addTab("Rezervacije", new JScrollPane(txtRezervacije));

		txtModeli = createTextArea();
		tabbedPane.addTab("Modeli Vozila", new JScrollPane(txtModeli));

		txtPrihodi = createTextArea();
		tabbedPane.addTab("Prihodi i Rashodi", new JScrollPane(txtPrihodi));

		add(tabbedPane, BorderLayout.CENTER);

		btnPrikazi.addActionListener(e -> generisiSve());
	}

	private JTextArea createTextArea() {
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		ta.setFont(new Font("Monospaced", Font.PLAIN, 14));
		return ta;
	}

	private void generisiSve() {
		try {
			LocalDate odDatuma = LocalDate.parse(txtDatumOd.getText());
			LocalDate doDatuma = LocalDate.parse(txtDatumDo.getText());

			generisiIzdavanja(odDatuma, doDatuma);
			generisiRezervacije(odDatuma, doDatuma);
			generisiModele(odDatuma, doDatuma);
			generisiPrihodeRashode(odDatuma, doDatuma);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Neispravan format datuma. Koristite YYYY-MM-DD.", "GreĹˇka", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void generisiIzdavanja(LocalDate odDatuma, LocalDate doDatuma) {
		HashMap<Integer, Integer> izdataPoAgentu = new HashMap<>();
		for (Korisnik k : kp.getKorisnici()) {
			if (k instanceof Agent) izdataPoAgentu.put(k.getId(), 0);
		}

		for (Izdavanje i : ip.getIzdavanja()) {
			LocalDate d = i.getRezervacija().getDatumPocetka();
			if (!d.isBefore(odDatuma) && !d.isAfter(doDatuma)) {
				int agentId = i.getAgent().getId();
				izdataPoAgentu.put(agentId, izdataPoAgentu.getOrDefault(agentId, 0) + 1);
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Broj izdatih vozila po agentu u periodu ").append(odDatuma).append(" do ").append(doDatuma).append(":\n\n");
		for (Integer agentId : izdataPoAgentu.keySet()) {
			Agent a = kp.pronadjiAgenta(agentId);
			if (a != null) {
				sb.append(a.getIme()).append(" ").append(a.getPrezime()).append(" (ID: ").append(agentId).append(") -> ")
				  .append(izdataPoAgentu.get(agentId)).append(" izdata vozila\n");
			}
		}
		txtIzdavanja.setText(sb.toString());
	}

	private void generisiRezervacije(LocalDate odDatuma, LocalDate doDatuma) {
		int potvrdjene = 0, odbijene = 0, otkazane = 0, naCekanju = 0;
		for (Rezervacija r : rp.getRezervacije()) {
			if (!r.getDatumPocetka().isBefore(odDatuma) && !r.getDatumPocetka().isAfter(doDatuma)) {
				switch (r.getStatusRezervacije()) {
				case ODOBRENA:
				case REALIZOVANA:
					potvrdjene++; break;
				case ODBIJENA:
					odbijene++; break;
				case OTKAZANA:
					otkazane++; break;
				case NA_CEKANJU:
					naCekanju++; break;
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Statusi rezervacija u periodu ").append(odDatuma).append(" do ").append(doDatuma).append(":\n\n");
		sb.append("Potvrđene/Realizovane: ").append(potvrdjene).append("\n");
		sb.append("Odbijene: ").append(odbijene).append("\n");
		sb.append("Otkazane: ").append(otkazane).append("\n");
		sb.append("Na čekanju: ").append(naCekanju).append("\n");
		txtRezervacije.setText(sb.toString());
	}

	private void generisiModele(LocalDate odDatuma, LocalDate doDatuma) {
		StringBuilder sb = new StringBuilder();
		sb.append("Statistika po modelima u periodu ").append(odDatuma).append(" do ").append(doDatuma).append(":\n\n");

		for (ModelVozila m : mp.getModeli()) {
			int brojRezervacija = 0;
			int brojIzdavanja = 0;

			for (Rezervacija r : rp.getRezervacije()) {
				if (r.getVozilo().getModelVozila().getId() == m.getId()) {
					if (!r.getDatumPocetka().isBefore(odDatuma) && !r.getDatumPocetka().isAfter(doDatuma)) {
						brojRezervacija++;
						if (r.getStatusRezervacije() == StatusRezervacije.REALIZOVANA) {
							brojIzdavanja++;
						}
					}
				}
			}

			sb.append(m.getMarkaVozila()).append(" ").append(m.getNazivModela())
			  .append(" [").append(m.getKategorija()).append("]\n")
			  .append("   Broj rezervacija: ").append(brojRezervacija).append("\n")
			  .append("   Broj izdavanja: ").append(brojIzdavanja).append("\n\n");
		}
		txtModeli.setText(sb.toString());
	}

	private void generisiPrihodeRashode(LocalDate odDatuma, LocalDate doDatuma) {
		double prihodiNajmovi = 0;
		for (Izdavanje i : ip.getIzdavanja()) {
			LocalDate d = i.getRezervacija().getDatumPocetka();
			if (!d.isBefore(odDatuma) && !d.isAfter(doDatuma)) {
				prihodiNajmovi += i.getRezervacija().getUkupnaCena(); 
			}
		}

		double prihodiPretplate = 0;
		for (Pretplata p : pp.getPretplate()) {
			if (p.getStatus() == StatusPretplate.AKTIVNA) {
				LocalDate datumUplate = p.getDatumIsteka().minusYears(1);
				if (!datumUplate.isBefore(odDatuma) && !datumUplate.isAfter(doDatuma)) {
					Cenovnik vazeci = cp.pronadjiVazeciCenovnik(datumUplate);
					if (vazeci != null) {
						prihodiPretplate += vazeci.getCenaPretplate();
					}
				}
			}
		}


		double rashodiPlate = 0;
		long brojDana = ChronoUnit.DAYS.between(odDatuma, doDatuma);
		if (brojDana <= 0) brojDana = 1;

		for (Korisnik k : kp.getKorisnici()) {
			if (k instanceof Zaposleni) {
				double dnevnica = ((Zaposleni)k).getPlata() / 30.0;
				rashodiPlate += (dnevnica * brojDana);
			}
		}

		double ukupniPrihodi = prihodiNajmovi + prihodiPretplate;

		StringBuilder sb = new StringBuilder();
		sb.append("Finansijski bilans u periodu ").append(odDatuma).append(" do ").append(doDatuma).append(":\n\n");
		sb.append("PRIHODI OD NAJMOVA (Najmovi, usluge, kazne): ").append(String.format("%.2f RSD", prihodiNajmovi)).append("\n");
		sb.append("PRIHODI OD PRETPLATA: ").append(String.format("%.2f RSD", prihodiPretplate)).append("\n");
		sb.append("UKUPNI PRIHODI: ").append(String.format("%.2f RSD", ukupniPrihodi)).append("\n");
		sb.append("UKUPNI RASHODI (Plate zaposlenih - za ").append(brojDana).append(" dana): ").append(String.format("%.2f RSD", rashodiPlate)).append("\n");
		sb.append("----------------------------------------------------\n");
		sb.append("PROFIT: ").append(String.format("%.2f RSD", ukupniPrihodi - rashodiPlate)).append("\n");

		txtPrihodi.setText(sb.toString());
	}
}
