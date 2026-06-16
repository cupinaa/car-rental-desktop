package podaci;
import korisnici.StrucnaSprema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import cenovnik.Cenovnik;
import cenovnik.StavkaCenovnika;
import izdavanje.Izdavanje;
import korisnici.Administrator;
import korisnici.Agent;
import korisnici.Klijent;
import korisnici.KategorijaKlijenata;
import korisnici.Pol;
import korisnici.Pretplata;
import korisnici.StatusPretplate;
import rezervacija.DodatnaUsluga;
import rezervacija.Rezervacija;
import rezervacija.StatusRezervacije;
import vozila.KategorijaVozila;
import vozila.ModelVozila;
import vozila.StatusVozila;
import vozila.Vozilo;

public class DemoDataGenerator {

    public static void main(String[] args) {
        try {
            System.out.println("Pocinje generisanje demo podataka...");

            KorisniciPodaci kp = new KorisniciPodaci();
            kp.getKorisnici().clear();
            ModeliVozilaPodaci mp = new ModeliVozilaPodaci();
            mp.getModeli().clear();
            VozilaPodaci vp = new VozilaPodaci();
            vp.getVozila().clear();
            DodatneUslugePodaci dup = new DodatneUslugePodaci();
            dup.getUsluge().clear();
            CenovniciPodaci cp = new CenovniciPodaci();
            cp.getCenovnici().clear();
            PretplatePodaci pp = new PretplatePodaci();
            pp.getPretplate().clear();
            RezervacijePodaci rp = new RezervacijePodaci();
            rp.getRezervacije().clear();
            IzdavanjePodaci ip = new IzdavanjePodaci();
            ip.getIzdavanja().clear();


            Administrator admin = new Administrator(1, "Admin", "Adminovic", Pol.Muski, LocalDate.of(1980, 1, 1), "060111111", "Adresa 1", "admin", "admin", StrucnaSprema.VSS, 10, 100000);
            kp.getKorisnici().add(admin);

            Agent agentAna = new Agent(2, "Ana", "Anic", Pol.Zenski, LocalDate.of(1990, 5, 5), "060222222", "Adresa 2", "agentAna", "123", StrucnaSprema.SSS, 5, 50000);
            kp.getKorisnici().add(agentAna);

            Agent agentMarko = new Agent(3, "Marko", "Markovic", Pol.Muski, LocalDate.of(1985, 3, 3), "060333333", "Adresa 3", "agentMarko", "123", StrucnaSprema.MAGISTAR, 8, 60000);
            kp.getKorisnici().add(agentMarko);

            Klijent kStudent = new Klijent(4, "Student", "Studentovic", Pol.Muski, LocalDate.of(2000, 1, 1), "06111", "St 1", "student@gmail.com", "123", LocalDate.of(2018, 1, 1), KategorijaKlijenata.STUDENT);
            kp.getKorisnici().add(kStudent);

            Klijent kPenzioner = new Klijent(5, "Penzioner", "Penzionerovic", Pol.Zenski, LocalDate.of(1950, 1, 1), "06222", "St 2", "penzioner@gmail.com", "123", LocalDate.of(1970, 1, 1), KategorijaKlijenata.PENZIONER);
            kp.getKorisnici().add(kPenzioner);

            Klijent kFirma = new Klijent(6, "Firma", "Doo", Pol.Muski, LocalDate.of(1980, 1, 1), "06333", "St 3", "firma@gmail.com", "123", LocalDate.of(2000, 1, 1), KategorijaKlijenata.FIRMA);
            kp.getKorisnici().add(kFirma);

            Klijent kObican = new Klijent(7, "Pera", "Peric", Pol.Muski, LocalDate.of(1990, 1, 1), "06444", "St 4", "pera@gmail.com", "123", LocalDate.of(2010, 1, 1), null);
            kObican.setBrojKasnjenja(6);
            kp.getKorisnici().add(kObican);


            ModelVozila m1 = new ModelVozila(1, "Skoda", "Octavia", KategorijaVozila.STANDARD);
            mp.getModeli().add(m1);

            ModelVozila m2 = new ModelVozila(2, "Toyota", "Yaris", KategorijaVozila.ECONOMY);
            mp.getModeli().add(m2);

            ModelVozila m3 = new ModelVozila(3, "BMW", "X5", KategorijaVozila.LUXURY);
            mp.getModeli().add(m3);

            Vozilo v1 = new Vozilo(1, m1, "BG-123-AA", StatusVozila.RASPOLOZIVO);
            vp.getVozila().add(v1);

            Vozilo v2 = new Vozilo(2, m2, "NS-456-BB", StatusVozila.RASPOLOZIVO);
            vp.getVozila().add(v2);

            Vozilo v3 = new Vozilo(3, m3, "NI-789-CC", StatusVozila.RASPOLOZIVO);
            vp.getVozila().add(v3);

            Vozilo v4 = new Vozilo(4, m2, "BG-999-DD", StatusVozila.RASPOLOZIVO);
            vp.getVozila().add(v4);


            DodatnaUsluga du1 = new DodatnaUsluga(1, "GPS Navigacija");
            dup.getUsluge().add(du1);

            DodatnaUsluga du2 = new DodatnaUsluga(2, "Decije Sediste");
            dup.getUsluge().add(du2);

            DodatnaUsluga du3 = new DodatnaUsluga(3, "Produzeno Koriscenje");
            dup.getUsluge().add(du3);


            ArrayList<StavkaCenovnika> stavke = new ArrayList<>();
            stavke.add(new StavkaCenovnika(1, KategorijaVozila.ECONOMY, 2000));
            stavke.add(new StavkaCenovnika(2, KategorijaVozila.STANDARD, 4000));
            stavke.add(new StavkaCenovnika(3, KategorijaVozila.FAMILY, 5000));
            stavke.add(new StavkaCenovnika(4, KategorijaVozila.LUXURY, 8000));

            HashMap<Integer, Double> ceneUsluga = new HashMap<>();
            ceneUsluga.put(1, 1000.0);
            ceneUsluga.put(2, 500.0);
            ceneUsluga.put(3, 2000.0);

            Cenovnik c1 = new Cenovnik(1, LocalDate.now().minusYears(1).minusDays(1), LocalDate.now().plusYears(1), stavke, 15000.0, 0.1, 0.2, 0.3, 5000.0, ceneUsluga);
            cp.getCenovnici().add(c1);


            Pretplata p1 = new Pretplata(1, kStudent, LocalDate.now().plusMonths(6), StatusPretplate.AKTIVNA);
            pp.getPretplate().add(p1);

            Pretplata p2 = new Pretplata(2, kFirma, LocalDate.now().plusMonths(3), StatusPretplate.AKTIVNA);
            pp.getPretplate().add(p2);

            Pretplata p3 = new Pretplata(3, kPenzioner, LocalDate.now().plusMonths(2), StatusPretplate.AKTIVNA);
            pp.getPretplate().add(p3);


            int[] monthsAgo = {11, 10, 8, 6, 5, 3, 2, 1};
            Klijent[] klis = {kStudent, kFirma, kPenzioner, kObican};
            Vozilo[] vozs = {v1, v2, v3, v4};
            Agent[] agents = {agentAna, agentMarko};

            int rezId = 1;
            int izdId = 1;

            for (int i = 0; i < 20; i++) {
                Klijent k = klis[i % 4];
                Vozilo v = vozs[i % 4];
                Agent a = agents[i % 2];

                int mAgo = monthsAgo[i % monthsAgo.length];
                LocalDate start = LocalDate.now().minusMonths(mAgo).minusDays(i % 15);
                LocalDate end = start.plusDays(3);

                ArrayList<DodatnaUsluga> usluge = new ArrayList<>();
                if (i % 2 == 0) usluge.add(du1);

                double cena = 4000 * 3;
                if (k.getKategorijaKlijenata() == KategorijaKlijenata.STUDENT) cena *= 0.9;
                if (k.getKategorijaKlijenata() == KategorijaKlijenata.PENZIONER) cena *= 0.8;
                if (k.getKategorijaKlijenata() == KategorijaKlijenata.FIRMA) cena *= 0.7;
                if (i % 2 == 0) cena += 1000;

                Rezervacija r = new Rezervacija(rezId++, k, v, start, end, cena, StatusRezervacije.REALIZOVANA, usluge);
                rp.getRezervacije().add(r);

                Izdavanje izd = new Izdavanje(izdId++, r, a, 100000, 100500);
                ip.getIzdavanja().add(izd);
            }

            LocalDate recent = LocalDate.now().minusDays(5);
            rp.getRezervacije().add(new Rezervacija(rezId++, kStudent, v2, recent, recent.plusDays(2), 2000, StatusRezervacije.OTKAZANA, new ArrayList<>()));
            rp.getRezervacije().add(new Rezervacija(rezId++, kPenzioner, v1, recent, recent.plusDays(2), 3000, StatusRezervacije.OTKAZANA, new ArrayList<>()));
            rp.getRezervacije().add(new Rezervacija(rezId++, kObican, v3, recent, recent.plusDays(2), 5000, StatusRezervacije.ODBIJENA, new ArrayList<>()));
            rp.getRezervacije().add(new Rezervacija(rezId++, kStudent, v4, recent, recent.plusDays(2), 2000, StatusRezervacije.values()[0], new ArrayList<>()));
            rp.getRezervacije().add(new Rezervacija(rezId++, kFirma, v2, recent, recent.plusDays(2), 4000, StatusRezervacije.values()[0], new ArrayList<>()));
            rp.getRezervacije().add(new Rezervacija(rezId++, kStudent, v1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), 5000, StatusRezervacije.ODOBRENA, new ArrayList<>()));

            System.out.println("Zavrseno generisanje objekata, cuvam u fajlove...");
            kp.upisi("korisnici.csv");
            mp.upisi("modeli.csv");
            vp.upisi("vozila.csv");
            dup.upisi("dodatne_usluge.csv");
            cp.upisi("cenovnici.csv", "stavke_cenovnika.csv", "stavke_usluga_cenovnika.csv");
            pp.upisi("pretplate.csv");
            rp.upisi("rezervacije.csv");
            ip.upisi("izdavanja.csv");

            System.out.println("USPESNO ZAVRSENO! Svi CSV fajlovi su kreirani.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
