package gui;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import podaci.ModeliVozilaPodaci;
import podaci.VozilaPodaci;
import podaci.RezervacijePodaci;
import vozila.Vozilo;
import korisnici.Korisnik;
import korisnici.Agent;

public class VozilaPanel extends JPanel {

	private VozilaPodaci vp;
	private ModeliVozilaPodaci mp;
	private JTable tabela;
	private DefaultTableModel tableModel;

	private Korisnik ulogovaniKorisnik;
	private RezervacijePodaci rp;

	public VozilaPanel(VozilaPodaci vp, ModeliVozilaPodaci mp, Korisnik ulogovaniKorisnik, RezervacijePodaci rp) {
		this.vp = vp;
		this.mp = mp;
		this.ulogovaniKorisnik = ulogovaniKorisnik;
		this.rp = rp;
		setLayout(new BorderLayout()); 

		String[] kolone = {"ID", "Marka i Model", "Kategorija", "Registracija", "Status"};
		tableModel = new DefaultTableModel(kolone, 0); 
		tabela = new JTable(tableModel);

		JPanel panelPretraga = new JPanel();
		JTextField txtOd = new JTextField(10);
		JTextField txtDo = new JTextField(10);
		JButton btnPretrazi = new JButton("Pretrazi Slobodna");
		JButton btnReset = new JButton("Prikazi Sve");

		panelPretraga.add(new JLabel("Od (YYYY-MM-DD):"));
		panelPretraga.add(txtOd);
		panelPretraga.add(new JLabel("Do (YYYY-MM-DD):"));
		panelPretraga.add(txtDo);
		panelPretraga.add(btnPretrazi);
		panelPretraga.add(btnReset);
		
		add(panelPretraga, BorderLayout.NORTH);

		btnPretrazi.addActionListener(e -> {
			try {
				LocalDate datumOd = LocalDate.parse(txtOd.getText().trim());
				LocalDate datumDo = LocalDate.parse(txtDo.getText().trim());
				osveziTabelu(datumOd, datumDo);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Unesite validne datume.");
			}
		});

		btnReset.addActionListener(e -> {
			txtOd.setText("");
			txtDo.setText("");
			osveziTabelu(null, null);
		});

		osveziTabelu(null, null);

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER); 

		if (!(ulogovaniKorisnik instanceof Agent)) {
			JPanel panelDugmici = new JPanel();
			JButton btnDodaj = new JButton("Dodaj");
			JButton btnIzmeni = new JButton("Izmeni");
			JButton btnObrisi = new JButton("Obriši");

			panelDugmici.add(btnDodaj);
			panelDugmici.add(btnIzmeni);
			panelDugmici.add(btnObrisi);
			add(panelDugmici, BorderLayout.SOUTH);

			btnDodaj.addActionListener(e -> {
				VoziloForma vf = new VoziloForma(vp, mp, null, this::osveziTabelu);
				vf.setVisible(true); 
			});

			btnIzmeni.addActionListener(e -> {
				int selektovaniRed = tabela.getSelectedRow();
				if (selektovaniRed == -1) {
					JOptionPane.showMessageDialog(this, "Morate prvo selektovati vozilo iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int idVozila = (int) tableModel.getValueAt(selektovaniRed, 0);
				Vozilo zaIzmenu = vp.pronadjiVozilo(idVozila);

				if (zaIzmenu != null) {
					VoziloForma vf = new VoziloForma(vp, mp, zaIzmenu, this::osveziTabelu);
					vf.setVisible(true);
				}
			});

			btnObrisi.addActionListener(e -> {
				int selektovaniRed = tabela.getSelectedRow();
				if (selektovaniRed == -1) {
					JOptionPane.showMessageDialog(this, "Morate prvo selektovati vozilo iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int idVozila = (int) tableModel.getValueAt(selektovaniRed, 0);
				Vozilo zaBrisanje = vp.pronadjiVozilo(idVozila);

				if (zaBrisanje != null) {
					Object[] opcije = {"Da", "Ne"};
					int potvrda = JOptionPane.showOptionDialog(this,
							"Da li ste sigurni da želite da obrišete vozilo: " + zaBrisanje.getRegistarskeTablice() + "?",
							"Potvrda brisanja",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							opcije,
							opcije[1]);
					if (potvrda == JOptionPane.YES_OPTION) {
						vp.obrisiVozilo(zaBrisanje); 
						osveziTabelu(); 
					}
				}
			});
		}
	}

	private void osveziTabelu() {
		osveziTabelu(null, null);
	}

	private void osveziTabelu(LocalDate datumOd, LocalDate datumDo) {
		tableModel.setRowCount(0);
		for (Vozilo v : vp.getVozila()) {
			if (datumOd != null && datumDo != null) {
				if (rp != null && !rp.daLiJeVoziloSlobodno(v, datumOd, datumDo)) {
					continue;
				}
			}

			String markaModel = v.getModelVozila().getMarkaVozila() + " " + v.getModelVozila().getNazivModela();
			Object[] red = { 
				v.getId(), 
				markaModel, 
				v.getModelVozila().getKategorija().toString(), 
				v.getRegistarskeTablice(), 
				v.getStatusVozila().toString() 
			};
			tableModel.addRow(red);
		}
	}
}
