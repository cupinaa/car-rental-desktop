package gui;

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
		javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();

		if (ulogovaniKorisnik instanceof Administrator) {
			javax.swing.JMenu adminMenu = new javax.swing.JMenu("Administracija");

			javax.swing.JMenuItem korisniciItem = new javax.swing.JMenuItem("Zaposleni");
			korisniciItem.addActionListener(e -> {
				KorisniciPanel kpPanel = new KorisniciPanel(kp, ulogovaniKorisnik);
				prikaziPanel(kpPanel);
			});
			adminMenu.add(korisniciItem);

			javax.swing.JMenuItem izvestajiItem = new javax.swing.JMenuItem("Izveštaji");
			izvestajiItem.addActionListener(e -> {
				IzvestajiPanel izp = new IzvestajiPanel(ip, rp, mp, kp, pp, cp);
				prikaziPanel(izp);
			});
			adminMenu.add(izvestajiItem);

			javax.swing.JMenuItem grafikoniItem = new javax.swing.JMenuItem("Grafikoni (XChart)");
			grafikoniItem.addActionListener(e -> {
				GrafikoniPanel gp = new GrafikoniPanel(ip, rp, pp, kp);
				prikaziPanel(gp);
			});
			adminMenu.add(grafikoniItem);

			javax.swing.JMenuItem vozilaItem = new javax.swing.JMenuItem("Vozila");
			vozilaItem.addActionListener(e -> {
				VozilaPanel vpPanel = new VozilaPanel(vp, mp, ulogovaniKorisnik);
				prikaziPanel(vpPanel);
			});
			adminMenu.add(vozilaItem);

			javax.swing.JMenuItem uslugeItem = new javax.swing.JMenuItem("Dodatne Usluge");
			uslugeItem.addActionListener(e -> {
				UslugePanel upPanel = new UslugePanel(dup);
				prikaziPanel(upPanel);
			});
			adminMenu.add(uslugeItem);

			javax.swing.JMenuItem cenovniciItem = new javax.swing.JMenuItem("Cenovnici");
			cenovniciItem.addActionListener(e -> {
				CenovnikPanel cpPanel = new CenovnikPanel(cp, dup);
				prikaziPanel(cpPanel);
			});
			adminMenu.add(cenovniciItem);

			javax.swing.JMenuItem podesavanjaItem = new javax.swing.JMenuItem("Podešavanja");
			podesavanjaItem.addActionListener(e -> {
				podaci.Podesavanja pod = new podaci.Podesavanja();
				pod.ucitaj();
				PodesavanjaPanel podPanel = new PodesavanjaPanel(pod);
				prikaziPanel(podPanel);
			});
			adminMenu.add(podesavanjaItem);

			menuBar.add(adminMenu);

		} else if (ulogovaniKorisnik instanceof Agent) {
			javax.swing.JMenu agentMenu = new javax.swing.JMenu("Radna tabla (Agent)");

			javax.swing.JMenuItem klijentiItem = new javax.swing.JMenuItem("Klijenti");
			klijentiItem.addActionListener(e -> {
				KorisniciPanel kpPanel = new KorisniciPanel(kp, ulogovaniKorisnik);
				prikaziPanel(kpPanel);
			});
			agentMenu.add(klijentiItem);

			javax.swing.JMenuItem pretplateItem = new javax.swing.JMenuItem("Pretplate");
			pretplateItem.addActionListener(e -> {
				PretplatePanel ppPanel = new PretplatePanel(pp);
				prikaziPanel(ppPanel);
			});
			agentMenu.add(pretplateItem);

			javax.swing.JMenuItem rezervacijeItem = new javax.swing.JMenuItem("Rezervacije");
			rezervacijeItem.addActionListener(e -> {
				RezervacijePanel rpPanel = new RezervacijePanel(rp);
				prikaziPanel(rpPanel);
			});
			agentMenu.add(rezervacijeItem);

			javax.swing.JMenuItem izdavanjeItem = new javax.swing.JMenuItem("Izdavanje Vozila");
			izdavanjeItem.addActionListener(e -> {
				IzdavanjePanel ipan = new IzdavanjePanel(rp, ip, vp, dup, cp, (Agent) ulogovaniKorisnik);
				prikaziPanel(ipan);
			});
			agentMenu.add(izdavanjeItem);

			javax.swing.JMenuItem vracanjeItem = new javax.swing.JMenuItem("Vraćanje Vozila");
			vracanjeItem.addActionListener(e -> {
				VracanjePanel vpPanel = new VracanjePanel(ip, rp, kp, cp);
				prikaziPanel(vpPanel);
			});
			agentMenu.add(vracanjeItem);

			javax.swing.JMenuItem vozilaItem = new javax.swing.JMenuItem("Vozila");
			vozilaItem.addActionListener(e -> {
				VozilaPanel vpPanel = new VozilaPanel(vp, mp, ulogovaniKorisnik);
				prikaziPanel(vpPanel);
			});
			agentMenu.add(vozilaItem);

			menuBar.add(agentMenu);

		} else if (ulogovaniKorisnik instanceof Klijent) {
			javax.swing.JMenu klijentMenu = new javax.swing.JMenu("Klijentski Portal");

			javax.swing.JMenuItem novaRezItem = new javax.swing.JMenuItem("Nova Rezervacija");
			novaRezItem.addActionListener(e -> {
				KlijentRezervacijaPanel krp = new KlijentRezervacijaPanel(rp, mp, vp, dup, cp, pp, (Klijent) ulogovaniKorisnik);
				prikaziPanel(krp);
			});
			klijentMenu.add(novaRezItem);

			javax.swing.JMenuItem mojeRezItem = new javax.swing.JMenuItem("Moje Rezervacije");
			mojeRezItem.addActionListener(e -> {
				KlijentMojeRezervacijePanel kmrp = new KlijentMojeRezervacijePanel(rp, kp, cp, (Klijent) ulogovaniKorisnik);
				prikaziPanel(kmrp);
			});
			klijentMenu.add(mojeRezItem);

			javax.swing.JMenuItem mojaPretplataItem = new javax.swing.JMenuItem("Moja Pretplata");
			mojaPretplataItem.addActionListener(e -> {
				KlijentPretplataPanel kpp = new KlijentPretplataPanel(pp, (Klijent) ulogovaniKorisnik);
				prikaziPanel(kpp);
			});
			klijentMenu.add(mojaPretplataItem);

			menuBar.add(klijentMenu);
		}

		javax.swing.JMenu opcijeMenu = new javax.swing.JMenu("Opcije");
		javax.swing.JMenuItem odjavaItem = new javax.swing.JMenuItem("Odjavi se");
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
