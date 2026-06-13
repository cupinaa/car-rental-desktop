package podaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import vozila.KategorijaVozila;
import vozila.ModelVozila;

public class ModeliVozilaPodaci {

    protected ArrayList<ModelVozila> modeli = new ArrayList<>();

    public void ucitaj(String putanja) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(putanja));
        String linija;
        while ((linija = br.readLine()) != null) {
            String[] delovi = linija.split("\\|");
            int id = Integer.parseInt(delovi[0]);
            String marka = delovi[1];
            String nazivModela = delovi[2];
            KategorijaVozila kategorija = KategorijaVozila.valueOf(delovi[3]);

            ModelVozila model = new ModelVozila(id, marka, nazivModela, kategorija);
            modeli.add(model);
        }
        br.close();
    }

    public void upisi(String putanja) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(putanja));
        for (ModelVozila m : modeli) {
            pw.println(m.getId() + "|" + m.getMarkaVozila() + "|" + m.getNazivModela() + "|" + m.getKategorija());
        }
        pw.close();
    }

    private int generisiNoviId() {
        int maxId = 0;
        for (ModelVozila m : modeli) {
            if (m.getId() > maxId) {
                maxId = m.getId();
            }
        }
        return maxId + 1;
    }

    public void dodajModel(ModelVozila m) {
        m.setId(generisiNoviId());
        modeli.add(m);
        try {
            upisi("modeli.csv");
        } catch (IOException e) {
            System.out.println("Greška pri upisu modela: " + e.getMessage());
        }
    }
    
    
    public ModelVozila pronadjiModel(int id) {
        for (ModelVozila m : modeli) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public ArrayList<ModelVozila> getModeli() {
        return modeli;
    }

	public void setModeli(ArrayList<ModelVozila> modeli) {
		this.modeli = modeli;
	}
}