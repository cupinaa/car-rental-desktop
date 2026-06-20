package podaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import vozila.ModelVozila;
import vozila.StatusVozila;
import vozila.Vozilo;

public class VozilaPodaci {

    protected ArrayList<Vozilo> vozila = new ArrayList<>();

    public void ucitaj(String putanja, ModeliVozilaPodaci mp) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(putanja));
        String linija;
        while ((linija = br.readLine()) != null) {
            String[] delovi = linija.split("\\|");

            int idVozila = Integer.parseInt(delovi[0]);
            int idModela = Integer.parseInt(delovi[1]);
            String registracija = delovi[2];
            StatusVozila status = StatusVozila.valueOf(delovi[3]);

            ModelVozila povezaniModel = mp.pronadjiModel(idModela);

            if (povezaniModel != null) {
                Vozilo v = new Vozilo(idVozila, povezaniModel, registracija, status);
                vozila.add(v);
            } else {
                System.out.println("Upozorenje: Model sa ID-jem " + idModela + " nije nađen za vozilo " + idVozila);
            }
        }
        br.close();
    }

    public void upisi(String putanja) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(putanja));
        for (Vozilo v : vozila) {
            pw.println(v.getId() + "|" + v.getModelVozila().getId() + "|" + v.getRegistarskeTablice() + "|" + v.getStatusVozila());
        }
        pw.close();
    }

    private int generisiNoviId() {
        int maxId = 0;
        for (Vozilo v : vozila) {
            if (v.getId() > maxId) {
                maxId = v.getId();
            }
        }
        return maxId + 1;
    }

    public void dodajVozilo(Vozilo v) {
        v.setId(generisiNoviId());
        vozila.add(v);
        sacuvajIzmene();
    }


    public Vozilo pronadjiVozilo(int id) {
        for (Vozilo v : vozila) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }


    public void obrisiVozilo(Vozilo v) {
        vozila.remove(v);
        sacuvajIzmene();
    }

    public void sacuvajIzmene() {
        try {
            upisi("vozila.csv");
        } catch (IOException e) {
            System.out.println("Greška pri čuvanju vozila: " + e.getMessage());
        }
    }

    public ArrayList<Vozilo> getVozila() {
        return vozila;
    }
}
