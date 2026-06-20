package podaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import rezervacija.DodatnaUsluga;

public class DodatneUslugePodaci {

    protected ArrayList<DodatnaUsluga> usluge = new ArrayList<>();

    public void ucitaj(String putanja) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(putanja));
        String linija;
        while ((linija = br.readLine()) != null) {
            String[] delovi = linija.split("\\|");
            int id = Integer.parseInt(delovi[0]);
            String naziv = delovi[1];

            DodatnaUsluga du = new DodatnaUsluga(id, naziv);
            usluge.add(du);
        }
        br.close();
    }

    public void upisi(String putanja) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(putanja));
        for (DodatnaUsluga du : usluge) {
            pw.println(du.getId() + "|" + du.getDodatnaUsluga());
        }
        pw.close();
    }

    public DodatnaUsluga pronadjiUslugu(int id) {
        for (DodatnaUsluga du : usluge) {
            if (du.getId() == id) {
                return du;
            }
        }
        return null;
    }

    private int generisiNoviId() {
        int maxId = 0;
        for (DodatnaUsluga du : usluge) {
            if (du.getId() > maxId) {
                maxId = du.getId();
            }
        }
        return maxId + 1;
    }

    public void dodajUslugu(DodatnaUsluga du) {
        du.setId(generisiNoviId());
        usluge.add(du);
        sacuvajIzmene();
    }

    public void obrisiUslugu(DodatnaUsluga du) {
        usluge.remove(du);
        sacuvajIzmene();
    }

    public void sacuvajIzmene() {
        try {
            upisi("dodatne_usluge.csv");
        } catch (IOException e) {
            System.out.println("Greška pri čuvanju usluga: " + e.getMessage());
        }
    }

    public ArrayList<DodatnaUsluga> getUsluge() {
        return usluge;
    }
}