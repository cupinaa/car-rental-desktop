package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler.LegendPosition;

import izdavanje.Izdavanje;
import korisnici.Agent;
import korisnici.Klijent;
import korisnici.Korisnik;
import podaci.IzdavanjePodaci;
import podaci.KorisniciPodaci;
import podaci.PretplatePodaci;
import podaci.RezervacijePodaci;
import rezervacija.Rezervacija;

public class GrafikoniPanel extends JPanel {

	private IzdavanjePodaci ip;
	private RezervacijePodaci rp;
	private PretplatePodaci pp;
	private KorisniciPodaci kp;

	public GrafikoniPanel(IzdavanjePodaci ip, RezervacijePodaci rp, PretplatePodaci pp, KorisniciPodaci kp) {
		this.ip = ip;
		this.rp = rp;
		this.pp = pp;
		this.kp = kp;

		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new BorderLayout());

		// 1. Prihodi (Gore - Line chart)
		JPanel prihodiPanel = kreirajPrihodiChart();
		mainPanel.add(prihodiPanel, BorderLayout.NORTH);

		// 2. Pita grafikoni (Dole - GridLayout)
		JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
		bottomPanel.add(kreirajStatusiChart());
		bottomPanel.add(kreirajAgentiChart());
		
		mainPanel.add(bottomPanel, BorderLayout.CENTER);

		JScrollPane scroll = new JScrollPane(mainPanel);
		add(scroll, BorderLayout.CENTER);
	}

	private JPanel kreirajPrihodiChart() {
		LocalDate start = LocalDate.now().minusMonths(11).withDayOfMonth(1); 
		
		List<String> xData = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
		for (int i = 0; i < 12; i++) {
			xData.add(start.plusMonths(i).format(formatter));
		}

		double[] studentData = new double[12];
		double[] firmaData = new double[12];
		double[] penzionerData = new double[12];
		double[] bezKategorijeData = new double[12];
		double[] ukupnoData = new double[12];

		for (Izdavanje i : ip.getIzdavanja()) {
			LocalDate datum = i.getRezervacija().getDatumPocetka();
			if (!datum.isBefore(start)) {
				int index = (int) java.time.temporal.ChronoUnit.MONTHS.between(start.withDayOfMonth(1), datum.withDayOfMonth(1));
				if (index >= 0 && index < 12) {
					double cena = i.getRezervacija().getUkupnaCena();
					ukupnoData[index] += cena;
					
					Klijent k = i.getRezervacija().getKlijent();
					String kat = k.getKategorijaKlijenata() != null ? k.getKategorijaKlijenata().toString() : "STANDARDNI";
					if (kat.equals("STUDENT")) studentData[index] += cena;
					else if (kat.equals("FIRMA")) firmaData[index] += cena;
					else if (kat.equals("PENZIONER")) penzionerData[index] += cena;
					else bezKategorijeData[index] += cena;
				}
			}
		}

		CategoryChart chart = new CategoryChartBuilder().width(800).height(400).title("Prihodi po kategoriji klijenta").build();
		chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Line);
		chart.getStyler().setLegendPosition(LegendPosition.OutsideS);

		// Ako su sve vrednosti 0 za svih 12 meseci, XChart moe baciti izuzetak za renderovanje,
		// zato cemo dodati po mali broj cisto da se prikaze ravna linija na nuli.
		boolean hasAnyData = false;
		for(double d : ukupnoData) if(d > 0) hasAnyData = true;
		
		if (!hasAnyData) {
			for(int i=0; i<12; i++) ukupnoData[i] = 0.001;
		}

		chart.addSeries("Student", xData, toList(studentData));
		chart.addSeries("Firma", xData, toList(firmaData));
		chart.addSeries("Penzioner", xData, toList(penzionerData));
		chart.addSeries("Bez kategorije", xData, toList(bezKategorijeData));
		chart.addSeries("Ukupno", xData, toList(ukupnoData));

		return new XChartPanel<>(chart);
	}

	private JPanel kreirajAgentiChart() {
		LocalDate preMesecDana = LocalDate.now().minusDays(30);
		
		HashMap<String, Integer> izdataPoAgentu = new HashMap<>();
		for (Korisnik k : kp.getKorisnici()) {
			if (k instanceof Agent) {
				izdataPoAgentu.put(k.getIme() + " " + k.getPrezime(), 0);
			}
		}

		for (Izdavanje i : ip.getIzdavanja()) {
			if (!i.getRezervacija().getDatumPocetka().isBefore(preMesecDana)) {
				String agentIme = i.getAgent().getIme() + " " + i.getAgent().getPrezime();
				izdataPoAgentu.put(agentIme, izdataPoAgentu.getOrDefault(agentIme, 0) + 1);
			}
		}

		PieChart chart = new PieChartBuilder().width(400).height(350).title("Opterećenje agenata u prethodnih 30 dana").build();
		chart.getStyler().setLegendPosition(LegendPosition.OutsideS);

		boolean imaPodataka = false;
		for (String a : izdataPoAgentu.keySet()) {
			if (izdataPoAgentu.get(a) > 0) {
				chart.addSeries(a, izdataPoAgentu.get(a));
				imaPodataka = true;
			}
		}
		
		if (!imaPodataka) {
			chart.addSeries("Nema rezervacija", 1);
		}

		return new XChartPanel<>(chart);
	}

	private JPanel kreirajStatusiChart() {
		LocalDate preMesecDana = LocalDate.now().minusDays(30);
		
		int potvrdjene = 0, odbijene = 0, otkazane = 0, naCekanju = 0;
		for (Rezervacija r : rp.getRezervacije()) {
			if (!r.getDatumPocetka().isBefore(preMesecDana)) {
				String sName = r.getStatusRezervacije().name();
				if (sName.equals("ODOBRENA") || sName.equals("REALIZOVANA")) {
					potvrdjene++;
				} else if (sName.equals("ODBIJENA")) {
					odbijene++;
				} else if (sName.equals("OTKAZANA")) {
					otkazane++;
				} else if (sName.contains("EKANJU") || sName.contains("CEKANJU")) {
					naCekanju++;
				}
			}
		}

		PieChart chart = new PieChartBuilder().width(400).height(350).title("Status rezervacija u prethodnih 30 dana").build();
		chart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		
		if (potvrdjene > 0) chart.addSeries("POTVRĐENA", potvrdjene);
		if (odbijene > 0) chart.addSeries("ODBIJENA", odbijene);
		if (otkazane > 0) chart.addSeries("OTKAZANA", otkazane);
		if (naCekanju > 0) chart.addSeries("NA ČEKANJU", naCekanju);
		
		if (potvrdjene == 0 && odbijene == 0 && otkazane == 0 && naCekanju == 0) {
			chart.addSeries("Nema podataka", 1);
		}

		return new XChartPanel<>(chart);
	}

	private List<Double> toList(double[] arr) {
		List<Double> list = new ArrayList<>();
		for (double d : arr) list.add(d);
		return list;
	}
}
