package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import korisnici.Korisnik;
import podaci.*;

public class LoginProzor extends JFrame {

	private KorisniciPodaci kp;
	private ModeliVozilaPodaci mp;
	private VozilaPodaci vp;
	private DodatneUslugePodaci dup;
	private CenovniciPodaci cp;
	private RezervacijePodaci rp;
	private IzdavanjePodaci ip;
	private PretplatePodaci pp;

	public LoginProzor(KorisniciPodaci kp, ModeliVozilaPodaci mp, VozilaPodaci vp, DodatneUslugePodaci dup,
			CenovniciPodaci cp, RezervacijePodaci rp, IzdavanjePodaci ip, PretplatePodaci pp) {

		this.kp = kp;
		this.mp = mp;
		this.vp = vp;
		this.dup = dup;
		this.cp = cp;
		this.rp = rp;
		this.ip = ip;
		this.pp = pp;

		setTitle("Prijava na sistem");
		setSize(350, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);

		JLabel lblKorisnickoIme = new JLabel("E-mail adresa:");
		lblKorisnickoIme.setBounds(30, 30, 100, 25);
		add(lblKorisnickoIme);

		JTextField txtKorisnickoIme = new JTextField();
		txtKorisnickoIme.setBounds(140, 30, 150, 25);
		add(txtKorisnickoIme);

		JLabel lblLozinka = new JLabel("Lozinka:");
		lblLozinka.setBounds(30, 70, 100, 25);
		add(lblLozinka);

		JPasswordField pfLozinka = new JPasswordField();
		pfLozinka.setBounds(140, 70, 150, 25);
		add(pfLozinka);

		JButton btnPrijava = new JButton("Prijavi se");
		btnPrijava.setBounds(140, 110, 150, 30);
		add(btnPrijava);

		btnPrijava.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String korisnickoIme = txtKorisnickoIme.getText().trim();
				String lozinka = new String(pfLozinka.getPassword());

				if (korisnickoIme.isEmpty() || lozinka.isEmpty()) {
					JOptionPane.showMessageDialog(LoginProzor.this, "Sva polja su obavezna!", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Korisnik ulogovani = kp.login(korisnickoIme, lozinka);
				if (ulogovani != null) {
					JOptionPane.showMessageDialog(LoginProzor.this, "Dobrodošli, " + ulogovani.getIme() + "!",
							"Uspešna prijava", JOptionPane.INFORMATION_MESSAGE);

					GlavniProzor glavni = new GlavniProzor(ulogovani, kp, mp, vp, dup, cp, rp, ip, pp);
					glavni.setVisible(true);
					LoginProzor.this.dispose();

				} else {
					JOptionPane.showMessageDialog(LoginProzor.this, "Pogrešan e-mail/korisničko ime ili lozinka!", "Greška",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
