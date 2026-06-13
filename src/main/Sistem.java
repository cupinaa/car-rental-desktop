package main;

import java.io.IOException;

import podaci.CenovniciPodaci;
import podaci.DodatneUslugePodaci;
import podaci.IzdavanjePodaci;
import podaci.KorisniciPodaci;
import podaci.ModeliVozilaPodaci;
import podaci.PretplatePodaci;
import podaci.RezervacijePodaci;
import podaci.VozilaPodaci;
import gui.LoginProzor;

public class Sistem {
    
    private KorisniciPodaci kp;
    private ModeliVozilaPodaci mp;
    private VozilaPodaci vp;
    private DodatneUslugePodaci dup;
    private CenovniciPodaci cp;
    private RezervacijePodaci rp;
    private IzdavanjePodaci ip;
    private PretplatePodaci pp;

    public Sistem() {
        kp = new KorisniciPodaci();
        mp = new ModeliVozilaPodaci();
        vp = new VozilaPodaci();
        dup = new DodatneUslugePodaci();
        cp = new CenovniciPodaci();
        rp = new RezervacijePodaci();
        ip = new IzdavanjePodaci(); 
        pp = new PretplatePodaci();
    }

    public void pokreni() {
        try {
            kp.ucitaj("korisnici.csv");
            mp.ucitaj("modeli.csv");
            dup.ucitaj("dodatne_usluge.csv");
            
            vp.ucitaj("vozila.csv", mp);
            cp.ucitaj("cenovnici.csv", "stavke_cenovnika.csv", "stavke_usluga_cenovnika.csv");
            rp.ucitaj("rezervacije.csv", kp, vp, dup);
            ip.ucitaj("izdavanja.csv", rp, kp);
            pp.ucitaj("pretplate.csv", kp);
            
            LoginProzor login = new LoginProzor(kp, mp, vp, dup, cp, rp, ip, pp);
            login.setVisible(true);

        } catch (IOException e) {
            System.err.println("Greška pri učitavanju: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
