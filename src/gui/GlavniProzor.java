package gui;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import podaci.Podesavanja;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import korisnici.Administrator;
import korisnici.Agent;
import korisnici.Klijent;
import korisnici.Korisnik;
import podaci.*;

public class GlavniProzor extends JFrame {

	private Korisnik ulogovaniKorisnik;

	private KorisniciPodaci kp;
	private ModeliVozilaPodaci mp;
	private VozilaPodaci vp;
	private DodatneUslugePodaci dup;
	private CenovniciPodaci cp;
	private RezervacijePodaci rp;
	private IzdavanjePodaci ip;
	private PretplatePodaci pp;

	private JPanel glavniSadrzaj; 

	public GlavniProzor(Korisnik ulogovaniKorisnik, KorisniciPodaci kp, ModeliVozilaPodaci mp, VozilaPodaci vp, DodatneUslugePodaci dup,
			CenovniciPodaci cp, RezervacijePodaci rp, IzdavanjePodaci ip, PretplatePodaci pp) {

		this.ulogovaniKorisnik = ulogovaniKorisnik;
		this.kp = kp;
		this.mp = mp;
		this.vp = vp;
		this.dup = dup;
		this.cp = cp;
		this.rp = rp;
		this.ip = ip;
		this.pp = pp;

		setTitle("Rent-a-Car Sistem - Prijavljeni: " + ulogovaniKorisnik.getIme() + " " + ulogovaniKorisnik.getPrezime());
		setSize(900, 600); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);


		glavniSadrzaj = new JPanel(new BorderLayout());
		add(glavniSadrzaj, BorderLayout.CENTER);

		JLabel lblDobrodosli = new JLabel("Dobrodošli u Rent-a-Car, " + ulogovaniKorisnik.getIme(), SwingConstants.CENTER);
		lblDobrodosli.setFont(new Font("Arial", Font.BOLD, 24));
		glavniSadrzaj.add(lblDobrodosli, BorderLayout.CENTER); 

		kreirajMeni();
	}


	private void prikaziPanel(JPanel noviPanel) {
		glavniSadrzaj.removeAll();
		glavniSadrzaj.add(noviPanel, BorderLayout.CENTER);
		glavniSadrzaj.revalidate(); 
		glavniSadrzaj.repaint();
	}

	private void kreirajMeni() {
		JMenuBar menuBar = new JMenuBar();

		if (ulogovaniKorisnik instanceof Administrator) {
			JMenu adminMenu = new JMenu("Administracija");

			JMenuItem korisniciItem = new JMenuItem("Zaposleni");
			korisniciItem.addActionListener(e -> {
				KorisniciPanel kpPanel = new KorisniciPanel(kp, ulogovaniKorisnik);
				prikaziPanel(kpPanel);
			});
			adminMenu.add(korisniciItem);

			JMenuItem izvestajiItem = new JMenuItem("Izveštaji");
			izvestajiItem.addActionListener(e -> {
				IzvestajiPanel izp = new IzvestajiPanel(ip, rp, mp, kp, pp, cp);
				prikaziPanel(izp);
			});
			adminMenu.add(izvestajiItem);

			JMenuItem grafikoniItem = new JMenuItem("Grafikoni (XChart)");
			grafikoniItem.addActionListener(e -> {
				GrafikoniPanel gp = new GrafikoniPanel(ip, rp, pp, kp);
				prikaziPanel(gp);
			});
			adminMenu.add(grafikoniItem);

			JMenuItem vozilaItem = new JMenuItem("Vozila");
			vozilaItem.addActionListener(e -> {
				VozilaPanel vpPanel = new VozilaPanel(vp, mp, ulogovaniKorisnik);
				prikaziPanel(vpPanel);
			});
			adminMenu.add(vozilaItem);

			JMenuItem uslugeItem = new JMenuItem("Dodatne Usluge");
			uslugeItem.addActionListener(e -> {
				UslugePanel upPanel = new UslugePanel(dup);
				prikaziPanel(upPanel);
			});
			adminMenu.add(uslugeItem);

			JMenuItem cenovniciItem = new JMenuItem("Cenovnici");
			cenovniciItem.addActionListener(e -> {
				CenovnikPanel cpPanel = new CenovnikPanel(cp, dup);
				prikaziPanel(cpPanel);
			});
			adminMenu.add(cenovniciItem);

			JMenuItem podesavanjaItem = new JMenuItem("Podešavanja");
			podesavanjaItem.addActionListener(e -> {
				Podesavanja pod = new Podesavanja();
				pod.ucitaj();
				PodesavanjaPanel podPanel = new PodesavanjaPanel(pod);
				prikaziPanel(podPanel);
			});
			adminMenu.add(podesavanjaItem);

			menuBar.add(adminMenu);

		} else if (ulogovaniKorisnik instanceof Agent) {
			JMenu agentMenu = new JMenu("Radna tabla (Agent)");

			JMenuItem klijentiItem = new JMenuItem("Klijenti");
			klijentiItem.addActionListener(e -> {
				KorisniciPanel kpPanel = new KorisniciPanel(kp, ulogovaniKorisnik);
				prikaziPanel(kpPanel);
			});
			agentMenu.add(klijentiItem);

			JMenuItem pretplateItem = new JMenuItem("Pretplate");
			pretplateItem.addActionListener(e -> {
				PretplatePanel ppPanel = new PretplatePanel(pp);
				prikaziPanel(ppPanel);
			});
			agentMenu.add(pretplateItem);

			JMenuItem rezervacijeItem = new JMenuItem("Rezervacije");
			rezervacijeItem.addActionListener(e -> {
				RezervacijePanel rpPanel = new RezervacijePanel(rp);
				prikaziPanel(rpPanel);
			});
			agentMenu.add(rezervacijeItem);

			JMenuItem izdavanjeItem = new JMenuItem("Izdavanje Vozila");
			izdavanjeItem.addActionListener(e -> {
				IzdavanjePanel ipan = new IzdavanjePanel(rp, ip, vp, dup, cp, (Agent) ulogovaniKorisnik);
				prikaziPanel(ipan);
			});
			agentMenu.add(izdavanjeItem);

			JMenuItem vracanjeItem = new JMenuItem("Vraćanje Vozila");
			vracanjeItem.addActionListener(e -> {
				VracanjePanel vpPanel = new VracanjePanel(ip, rp, kp, cp);
				prikaziPanel(vpPanel);
			});
			agentMenu.add(vracanjeItem);

			JMenuItem vozilaItem = new JMenuItem("Vozila");
			vozilaItem.addActionListener(e -> {
				VozilaPanel vpPanel = new VozilaPanel(vp, mp, ulogovaniKorisnik);
				prikaziPanel(vpPanel);
			});
			agentMenu.add(vozilaItem);

			menuBar.add(agentMenu);

		} else if (ulogovaniKorisnik instanceof Klijent) {
			JMenu klijentMenu = new JMenu("Klijentski Portal");

			JMenuItem novaRezItem = new JMenuItem("Nova Rezervacija");
			novaRezItem.addActionListener(e -> {
				KlijentRezervacijaPanel krp = new KlijentRezervacijaPanel(rp, mp, vp, dup, cp, pp, (Klijent) ulogovaniKorisnik);
				prikaziPanel(krp);
			});
			klijentMenu.add(novaRezItem);

			JMenuItem mojeRezItem = new JMenuItem("Moje Rezervacije");
			mojeRezItem.addActionListener(e -> {
				KlijentMojeRezervacijePanel kmrp = new KlijentMojeRezervacijePanel(rp, kp, cp, (Klijent) ulogovaniKorisnik);
				prikaziPanel(kmrp);
			});
			klijentMenu.add(mojeRezItem);

			JMenuItem mojaPretplataItem = new JMenuItem("Moja Pretplata");
			mojaPretplataItem.addActionListener(e -> {
				KlijentPretplataPanel kpp = new KlijentPretplataPanel(pp, (Klijent) ulogovaniKorisnik);
				prikaziPanel(kpp);
			});
			klijentMenu.add(mojaPretplataItem);

			menuBar.add(klijentMenu);
		}

		JMenu opcijeMenu = new JMenu("Opcije");
		JMenuItem odjavaItem = new JMenuItem("Odjavi se");
		odjavaItem.addActionListener(e -> {
			this.dispose();
			LoginProzor lp = new LoginProzor(kp, mp, vp, dup, cp, rp, ip, pp);
			lp.setVisible(true);
		});
		opcijeMenu.add(odjavaItem);
		menuBar.add(opcijeMenu);

		setJMenuBar(menuBar);
	}
}
