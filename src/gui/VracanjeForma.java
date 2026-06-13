package gui;

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import cenovnik.Cenovnik;
import izdavanje.Izdavanje;
import podaci.CenovniciPodaci;
import podaci.IzdavanjePodaci;
import podaci.KorisniciPodaci;
import podaci.RezervacijePodaci;

public class VracanjeForma extends JDialog {

	private IzdavanjePodaci ip;
	private RezervacijePodaci rp;
	private KorisniciPodaci kp;
	private CenovniciPodaci cp;
	private Izdavanje izdavanje;
	private Runnable naUspesnoVracanje;
	
	private JTextField txtKilometraza;
	private JTextField txtDatumVracanja;

	public VracanjeForma(IzdavanjePodaci ip, RezervacijePodaci rp, KorisniciPodaci kp, CenovniciPodaci cp, Izdavanje izdavanje, Runnable naUspesnoVracanje) {
		this.ip = ip;
		this.rp = rp;
		this.kp = kp;
		this.cp = cp;
		this.izdavanje = izdavanje;
		this.naUspesnoVracanje = naUspesnoVracanje;
		
		setTitle("Vraćanje Vozila");
		setSize(350, 150);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(3, 2, 5, 5));
		
		add(new JLabel("Krajnja kilometraža:"));
		txtKilometraza = new JTextField();
		add(txtKilometraza);
		
		add(new JLabel("Datum vraćanja (YYYY-MM-DD):"));
		txtDatumVracanja = new JTextField(LocalDate.now().toString());
		add(txtDatumVracanja);
		
		JButton btnZavrsi = new JButton("Evidentiraj Vraćanje");
		add(new JLabel("")); 
		add(btnZavrsi);
		
		btnZavrsi.addActionListener(e -> evidentirajVracanje());
	}
	
	private void evidentirajVracanje() {
		try {
			double krajnja = Double.parseDouble(txtKilometraza.getText());
			if (krajnja < izdavanje.getPocetnaKilometraza()) {
				JOptionPane.showMessageDialog(this, "Krajnja kilometraža ne može biti manja od početne (" + izdavanje.getPocetnaKilometraza() + ")!", "Greška", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			LocalDate datumVracanja = LocalDate.parse(txtDatumVracanja.getText());
			
			// Nalazimo aktuelni cenovnik za potrebe kazni
			Cenovnik aktuelniCenovnik = cp.pronadjiVazeciCenovnik(datumVracanja);
			
			// Pozivamo metodu iz podataka koja obrađuje status vozila, cenu i kašnjenja
			ip.vratiVozilo(izdavanje, krajnja, datumVracanja, rp, aktuelniCenovnik, kp);
			
			// Prikaz izveštaja (koliko je koštalo, popust se automatski izracunao ranije, a kazne sada)
			double konacnaCena = izdavanje.getRezervacija().getUkupnaCena();
			JOptionPane.showMessageDialog(this, "Vozilo uspešno vraćeno!\nKonačna cena za naplatu iznosi: " + konacnaCena + " RSD");
			
			naUspesnoVracanje.run();
			dispose();
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Unesite validan broj za kilometražu i ispravan datum (YYYY-MM-DD)!", "Greška", JOptionPane.ERROR_MESSAGE);
		}
	}
}
