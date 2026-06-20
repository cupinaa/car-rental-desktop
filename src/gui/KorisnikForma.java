package gui;
import java.util.Locale;

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import korisnici.Administrator;
import korisnici.Agent;
import korisnici.KategorijaKlijenata;
import korisnici.Klijent;
import korisnici.Korisnik;
import korisnici.Pol;
import korisnici.StrucnaSprema;
import korisnici.Zaposleni;
import podaci.KorisniciPodaci;

public class KorisnikForma extends JDialog {

	private KorisniciPodaci kp;
	private Runnable naUspesnoDodavanje;
	private Korisnik kZaIzmenu; 

	private JComboBox<String> cbTip;
	private JComboBox<Pol> cbPol;
	private JTextField txtIme, txtPrezime, txtDatumRodjenja, txtTelefon, txtAdresa, txtKorisnickoIme, txtLozinka;

	private JLabel lblSprema, lblStaz, lblOsnova;
	private JComboBox<StrucnaSprema> cbSprema;
	private JTextField txtStaz, txtOsnova;

	private JLabel lblVozacka, lblKategorija;
	private JTextField txtDatumVozacke;
	private JComboBox<String> cbKategorija;


	public KorisnikForma(KorisniciPodaci kp, Korisnik kZaIzmenu, Runnable naUspesnoDodavanje, Korisnik ulogovaniKorisnik, boolean prikaziKlijente) {
		this.kp = kp;
		this.naUspesnoDodavanje = naUspesnoDodavanje;
		this.kZaIzmenu = kZaIzmenu;

		setTitle(kZaIzmenu == null ? "Novi korisnik" : "Izmena korisnika");
		setSize(400, 600);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(16, 2, 5, 5));

		add(new JLabel("Tip korisnika:"));
		if (ulogovaniKorisnik instanceof Agent || prikaziKlijente) {
			cbTip = new JComboBox<>(new String[] {"Klijent"});
		} else {
			cbTip = new JComboBox<>(new String[] {"Agent", "Administrator"});
		}
		add(cbTip);

		add(new JLabel("Ime:")); txtIme = new JTextField(); add(txtIme);
		add(new JLabel("Prezime:")); txtPrezime = new JTextField(); add(txtPrezime);
		add(new JLabel("Pol:")); cbPol = new JComboBox<>(Pol.values()); add(cbPol);
		add(new JLabel("Datum Rođenja (YYYY-MM-DD):")); txtDatumRodjenja = new JTextField(); add(txtDatumRodjenja);
		add(new JLabel("Telefon:")); txtTelefon = new JTextField(); add(txtTelefon);
		add(new JLabel("Adresa:")); txtAdresa = new JTextField(); add(txtAdresa);
		add(new JLabel("E-mail adresa (Korisničko ime):")); txtKorisnickoIme = new JTextField(); add(txtKorisnickoIme);
		add(new JLabel("Lozinka:")); txtLozinka = new JTextField(); add(txtLozinka);

		lblVozacka = new JLabel("Datum Vozačke (YYYY-MM-DD):");
		txtDatumVozacke = new JTextField();
		add(lblVozacka); add(txtDatumVozacke);

		lblKategorija = new JLabel("Kategorija (samo za klijente):");
		cbKategorija = new JComboBox<>(new String[] {"NEMA", "STUDENT", "PENZIONER", "FIRMA"});
		add(lblKategorija); add(cbKategorija);

		lblSprema = new JLabel("Stručna Sprema:");
		cbSprema = new JComboBox<>(StrucnaSprema.values());
		add(lblSprema); add(cbSprema);

		lblStaz = new JLabel("Godine Staža:");
		txtStaz = new JTextField("0");
		add(lblStaz); add(txtStaz);

		lblOsnova = new JLabel("Osnova za Platu:");
		txtOsnova = new JTextField("50000.0");
		add(lblOsnova); add(txtOsnova);

		JButton btnSacuvaj = new JButton("Sačuvaj");
		add(new JLabel("")); 
		add(btnSacuvaj);

		cbTip.addActionListener(e -> osveziVidljivostPolja());


		if (kZaIzmenu != null) {
			popuniPolja();
		}

		osveziVidljivostPolja(); 

		btnSacuvaj.addActionListener(e -> sacuvajKorisnika());
	}

	private void popuniPolja() {
		txtIme.setText(kZaIzmenu.getIme());
		txtPrezime.setText(kZaIzmenu.getPrezime());
		cbPol.setSelectedItem(kZaIzmenu.getPol());
		txtDatumRodjenja.setText(kZaIzmenu.getDatumRodjenja().toString());
		txtTelefon.setText(kZaIzmenu.getTelefon());
		txtAdresa.setText(kZaIzmenu.getAdresa());
		txtKorisnickoIme.setText(kZaIzmenu.getKorisnickoIme());
		txtLozinka.setText(kZaIzmenu.getLozinka());

		cbTip.setEnabled(false); 

		if (kZaIzmenu instanceof Klijent) {
			cbTip.setSelectedItem("Klijent");
			Klijent kl = (Klijent) kZaIzmenu;
			txtDatumVozacke.setText(kl.getDatumIzdavanjaVozacke().toString());
			if (kl.getKategorijaKlijenata() != null) {
				cbKategorija.setSelectedItem(kl.getKategorijaKlijenata().name());
			} else {
				cbKategorija.setSelectedItem("NEMA");
			}
		} else if (kZaIzmenu instanceof Zaposleni) {
			Zaposleni z = (Zaposleni) kZaIzmenu;
			cbTip.setSelectedItem(z instanceof Agent ? "Agent" : "Administrator");
			cbSprema.setSelectedItem(z.getStrucnaSprema());
			txtStaz.setText(String.valueOf(z.getGodineStaza()));


			double obrnutaOsnova = z.getPlata() / (z.getStrucnaSprema().getKoeficijent() + 0.004 * z.getGodineStaza());
			txtOsnova.setText(String.format(Locale.US, "%.2f", obrnutaOsnova)); 
		}
	}

	private void osveziVidljivostPolja() {
		String tip = cbTip.getSelectedItem().toString();
		boolean jeKlijent = tip.equals("Klijent");

		lblVozacka.setVisible(jeKlijent);
		txtDatumVozacke.setVisible(jeKlijent);
		lblKategorija.setVisible(jeKlijent);
		cbKategorija.setVisible(jeKlijent);

		lblSprema.setVisible(!jeKlijent);
		cbSprema.setVisible(!jeKlijent);
		lblStaz.setVisible(!jeKlijent);
		txtStaz.setVisible(!jeKlijent);
		lblOsnova.setVisible(!jeKlijent);
		txtOsnova.setVisible(!jeKlijent);
	}

	private void sacuvajKorisnika() {
		try {
			String tip = cbTip.getSelectedItem().toString();
			String ime = txtIme.getText();
			String prezime = txtPrezime.getText();
			Pol pol = (Pol) cbPol.getSelectedItem();
			LocalDate datumRodjenja = LocalDate.parse(txtDatumRodjenja.getText());
			String telefon = txtTelefon.getText();
			String adresa = txtAdresa.getText();
			String korIme = txtKorisnickoIme.getText();
			String loz = txtLozinka.getText();

			if (kZaIzmenu == null) {

				if(tip.equals("Klijent")) {
					LocalDate vozacka = LocalDate.parse(txtDatumVozacke.getText());
					String katStr = cbKategorija.getSelectedItem().toString();
					KategorijaKlijenata kategorija = katStr.equals("NEMA") ? null : KategorijaKlijenata.valueOf(katStr);
					Klijent k = new Klijent(0, ime, prezime, pol, datumRodjenja, telefon, adresa, korIme, loz, vozacka, kategorija);
					kp.dodajKorisnika(k);
				} 
				else {
					StrucnaSprema sprema = (StrucnaSprema) cbSprema.getSelectedItem();
					int staz = Integer.parseInt(txtStaz.getText());
					double osnova = Double.parseDouble(txtOsnova.getText());

					if(tip.equals("Agent")) {
						Agent a = new Agent(0, ime, prezime, pol, datumRodjenja, telefon, adresa, korIme, loz, sprema, staz, 0);
						a.racunanjePlate(osnova);
						kp.dodajKorisnika(a);
					} else {
						Administrator ad = new Administrator(0, ime, prezime, pol, datumRodjenja, telefon, adresa, korIme, loz, sprema, staz, 0);
						ad.racunanjePlate(osnova);
						kp.dodajKorisnika(ad);
					}
				}
				JOptionPane.showMessageDialog(this, "Uspešno dodato!");
			} else {

				kZaIzmenu.setIme(ime);
				kZaIzmenu.setPrezime(prezime);
				kZaIzmenu.setPol(pol);
				kZaIzmenu.setDatumRodjenja(datumRodjenja);
				kZaIzmenu.setTelefon(telefon);
				kZaIzmenu.setAdresa(adresa);
				kZaIzmenu.setKorisnickoIme(korIme);
				kZaIzmenu.setLozinka(loz);

				if (kZaIzmenu instanceof Klijent) {
					Klijent kl = (Klijent) kZaIzmenu;
					kl.setDatumIzdavanjaVozacke(LocalDate.parse(txtDatumVozacke.getText()));
					String katStr = cbKategorija.getSelectedItem().toString();
					kl.setKategorijaKlijenata(katStr.equals("NEMA") ? null : KategorijaKlijenata.valueOf(katStr));
				} else if (kZaIzmenu instanceof Zaposleni) {
					Zaposleni z = (Zaposleni) kZaIzmenu;
					z.setStrucnaSprema((StrucnaSprema) cbSprema.getSelectedItem());
					z.setGodineStaza(Integer.parseInt(txtStaz.getText()));
					z.racunanjePlate(Double.parseDouble(txtOsnova.getText()));
				}

				kp.sacuvajIzmene(); 
				JOptionPane.showMessageDialog(this, "Uspešno izmenjeno!");
			}

			naUspesnoDodavanje.run();
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Greška u formatu podataka! Proveri datume i brojeve.", "Greška", JOptionPane.ERROR_MESSAGE);
		}
	}
}
