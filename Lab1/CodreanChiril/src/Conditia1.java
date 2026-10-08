/**
 * Condiția 1, varianta 5:
 * "Sumele produselor numerelor de pe poziții pare două câte două,
 *  începând căutarea și sumarea cu primul element."
 *
 * Se parcurg indicii pari 0, 2, 4, ... și se formează perechi consecutive:
 * (0,2), (4,6), (8,10), ... Pentru fiecare pereche se calculează produsul,
 * se adaugă la suma acumulată și se afișează rezultatul.
 */
public class Conditia1 implements Runnable {

    // Tabloul comun cu datele; este doar citit, niciodată modificat
    private final int[] mas;

    // Constructorul primește tabloul care va fi prelucrat de thread
    public Conditia1(int[] mas) {
        this.mas = mas;
    }

    // Metoda run() conține codul executat de thread după apelul start()
    @Override
    public void run() {
        // Numele thread-ului curent (ex. "Th1"), afișat la începutul fiecărei linii
        String nume = Thread.currentThread().getName();

        // Suma acumulată a produselor; long ca să evităm depășirea lui int
        long suma = 0;

        // i = primul indice par al perechii, j = i + 2 = al doilea indice par.
        // Pasul este 4, pentru că o pereche consumă două poziții pare (i și i+2).
        // Condiția j < mas.length garantează că nu ieșim din limitele tabloului;
        // dacă ultimul element par rămâne fără pereche, bucla se oprește și el se ignoră.
        for (int i = 0; i + 2 < mas.length; i += 4) {
            int j = i + 2;

            // Produsul celor două elemente, calculat în long
            long produs = (long) mas[i] * mas[j];

            // Adăugăm produsul la suma acumulată
            suma += produs;

            // Afișăm linia în formatul cerut
            System.out.println(nume + ": poz " + i + " si " + j
                    + " | " + mas[i] + " * " + mas[j] + " = " + produs
                    + " | suma = " + suma);
        }
    }
}
