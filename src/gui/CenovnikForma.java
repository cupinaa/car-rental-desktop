package gui;
import java.util.HashMap;
import java.util.Map;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import cenovnik.Cenovnik;
import cenovnik.StavkaCenovnika;
import podaci.CenovniciPodaci;
import podaci.DodatneUslugePodaci;
import rezervacija.DodatnaUsluga;
import vozila.KategorijaVozila;

public class CenovnikForma extends JDialog {

	private CenovniciPodaci cp;
	private DodatneUslugePodaci dup;
	private Runnable naUspesnoDodavanje;
	private Cenovnik cZaIzmenu;

	private JTextField txtPocetak, txtKraj, txtPretplata, txtPopustStudent, txtPopustFirma, txtPopustPenzioner, txtKazna;
	private JTextField txtCenaEconomy, txtCenaStandard, txtCenaFamily, txtCenaLuxury;
	private Map<Integer, JTextField> txtCeneUsluga = new HashMap<>();

	public CenovnikForma(CenovniciPodaci cp, DodatneUslugePodaci dup, Cenovnik cZaIzmenu, Runnable naUspesnoDodavanje) {
		this.cp = cp;
		this.dup = dup;
		this.naUspesnoDodavanje = naUspesnoDodavanje;
		this.cZaIzmenu = cZaIzmenu;

		setTitle(cZaIzmenu == null ? "Novi Cenovnik" : "Izmena Cenovnika");

		int brojUsluga = dup.getUsluge().size();
		setSize(800, 450 + (brojUsluga * 20)); 
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(0, 4, 10, 10)); 

		add(new JLabel("Početak važenja (YYYY-MM-DD):")); txtPocetak = new JTextField(); add(txtPocetak);
		add(new JLabel("Kraj važenja (YYYY-MM-DD):")); txtKraj = new JTextField(); add(txtKraj);

		add(new JLabel("Cena pretplate (RSD):")); txtPretplata = new JTextField(); add(txtPretplata);
		add(new JLabel("Popust Student (0.0 - 1.0):")); txtPopustStudent = new JTextField(); add(txtPopustStudent);

		add(new JLabel("Popust Firma (0.0 - 1.0):")); txtPopustFirma = new JTextField(); add(txtPopustFirma);
		add(new JLabel("Popust Penzioner (0.0 - 1.0):")); txtPopustPenzioner = new JTextField(); add(txtPopustPenzioner);

		add(new JLabel("Iznos kazne za kašnjenje (dan):")); txtKazna = new JTextField(); add(txtKazna);
		add(new JLabel("")); add(new JLabel("")); 

		add(new JLabel("--- CENE ZA KATEGORIJE VOZILA ---")); add(new JLabel("")); add(new JLabel("")); add(new JLabel(""));

		add(new JLabel("Cena/dan ECONOMY:")); txtCenaEconomy = new JTextField(); add(txtCenaEconomy);
		add(new JLabel("Cena/dan STANDARD:")); txtCenaStandard = new JTextField(); add(txtCenaStandard);

		add(new JLabel("Cena/dan FAMILY:")); txtCenaFamily = new JTextField(); add(txtCenaFamily);
		add(new JLabel("Cena/dan LUXURY:")); txtCenaLuxury = new JTextField(); add(txtCenaLuxury);

		add(new JLabel("--- CENE ZA DODATNE USLUGE ---")); add(new JLabel("")); add(new JLabel("")); add(new JLabel(""));

		int uslugaCount = 0;
		for (DodatnaUsluga du : dup.getUsluge()) {
			add(new JLabel("Cena/dan " + du.getDodatnaUsluga() + ":"));
			JTextField txt = new JTextField();
			txtCeneUsluga.put(du.getId(), txt);
			add(txt);
			uslugaCount++;
		}
		if (uslugaCount % 2 != 0) {
			add(new JLabel("")); add(new JLabel(""));
		}

		add(new JLabel("")); add(new JLabel(""));
		JButton btnSacuvaj = new JButton("Sačuvaj");
		add(btnSacuvaj); add(new JLabel(""));

		if (cZaIzmenu != null) {
			popuniPolja();
		}

		btnSacuvaj.addActionListener(e -> sacuvajCenovnik());
	}

	private void popuniPolja() {
		txtPocetak.setText(cZaIzmenu.getPocetakVazenja().toString());
		txtKraj.setText(cZaIzmenu.getKrajVazenja().toString());
		txtPretplata.setText(String.valueOf(cZaIzmenu.getCenaPretplate()));
		txtPopustStudent.setText(String.valueOf(cZaIzmenu.getPopustStudent()));
		txtPopustFirma.setText(String.valueOf(cZaIzmenu.getPopustFirma()));
		txtPopustPenzioner.setText(String.valueOf(cZaIzmenu.getPopustPenzioner()));
		txtKazna.setText(String.valueOf(cZaIzmenu.getIznosKazne()));

		for (StavkaCenovnika s : cZaIzmenu.getStavkeCenovnika()) {
			switch(s.getKategorijaVozila()) {
				case ECONOMY: txtCenaEconomy.setText(String.valueOf(s.getCenaPoDanu())); break;
				case STANDARD: txtCenaStandard.setText(String.valueOf(s.getCenaPoDanu())); break;
				case FAMILY: txtCenaFamily.setText(String.valueOf(s.getCenaPoDanu())); break;
				case LUXURY: txtCenaLuxury.setText(String.valueOf(s.getCenaPoDanu())); break;
			}
		}

		if (cZaIzmenu.getCeneDodatnihUsluga() != null) {
			for (Map.Entry<Integer, Double> entry : cZaIzmenu.getCeneDodatnihUsluga().entrySet()) {
				if (txtCeneUsluga.containsKey(entry.getKey())) {
					txtCeneUsluga.get(entry.getKey()).setText(String.valueOf(entry.getValue()));
				}
			}
		}
	}

	private void sacuvajCenovnik() {
		try {
			LocalDate pocetak = LocalDate.parse(txtPocetak.getText());
			LocalDate kraj = LocalDate.parse(txtKraj.getText());
			double pretplata = Double.parseDouble(txtPretplata.getText());
			double pStudent = Double.parseDouble(txtPopustStudent.getText());
			double pFirma = Double.parseDouble(txtPopustFirma.getText());
			double pPenzioner = Double.parseDouble(txtPopustPenzioner.getText());
			double kazna = Double.parseDouble(txtKazna.getText());

			double cEco = Double.parseDouble(txtCenaEconomy.getText());
			double cStd = Double.parseDouble(txtCenaStandard.getText());
			double cFam = Double.parseDouble(txtCenaFamily.getText());
			double cLux = Double.parseDouble(txtCenaLuxury.getText());

			Map<Integer, Double> mapeCenaUsluga = new HashMap<>();
			for (Map.Entry<Integer, JTextField> entry : txtCeneUsluga.entrySet()) {
				mapeCenaUsluga.put(entry.getKey(), Double.parseDouble(entry.getValue().getText()));
			}

			ArrayList<StavkaCenovnika> stavke = new ArrayList<>();
			stavke.add(new StavkaCenovnika(KategorijaVozila.ECONOMY, cEco));
			stavke.add(new StavkaCenovnika(KategorijaVozila.STANDARD, cStd));
			stavke.add(new StavkaCenovnika(KategorijaVozila.FAMILY, cFam));
			stavke.add(new StavkaCenovnika(KategorijaVozila.LUXURY, cLux));

			if (cZaIzmenu == null) {
				Cenovnik c = new Cenovnik(pocetak, kraj, stavke, pretplata, pStudent, pFirma, pPenzioner, kazna, mapeCenaUsluga);
				cp.dodajCenovnik(c);
				JOptionPane.showMessageDialog(this, "Uspešno dodato!");
			} else {
				cZaIzmenu.setPocetakVazenja(pocetak);
				cZaIzmenu.setKrajVazenja(kraj);
				cZaIzmenu.setCenaPretplate(pretplata);
				cZaIzmenu.setPopustStudent(pStudent);
				cZaIzmenu.setPopustFirma(pFirma);
				cZaIzmenu.setPopustPenzioner(pPenzioner);
				cZaIzmenu.setIznosKazne(kazna);

				for (StavkaCenovnika sNova : stavke) {
					for (StavkaCenovnika sStara : cZaIzmenu.getStavkeCenovnika()) {
						if (sNova.getKategorijaVozila() == sStara.getKategorijaVozila()) {
							sNova.setId(sStara.getId());
						}
					}
				}
				cZaIzmenu.setStavkeCenovnika(stavke);
				cZaIzmenu.setCeneDodatnihUsluga(mapeCenaUsluga);

				cp.sacuvajIzmene();
				JOptionPane.showMessageDialog(this, "Uspešno izmenjeno!");
			}

			naUspesnoDodavanje.run();
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Proverite unos! Datumi moraju biti YYYY-MM-DD, a brojevi ispravni.", "Greška", JOptionPane.ERROR_MESSAGE);
		}
	}
}
