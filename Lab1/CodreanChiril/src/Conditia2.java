/**
 * Condiția 2, varianta 5:
 * "Sumele produselor numerelor de pe poziții pare două câte două,
 *  începând căutarea și sumarea cu ultimul element."
 *
 * Se parcurg indicii pari descrescător, de la cel mai mare indice par din tablou
 * (pentru 100 de elemente: 98, 96, 94, ...) și se formează perechi consecutive:
 * (98,96), (94,92), ... Pentru fiecare pereche se calculează produsul,
 * se adaugă la suma acumulată și se afișează rezultatul.
 */
public class Conditia2 implements Runnable {

    // Tabloul comun cu datele; este doar citit, niciodată modificat
    private final int[] mas;

    // Constructorul primește tabloul care va fi prelucrat de thread
    public Conditia2(int[] mas) {
        this.mas = mas;
    }

    // Metoda run() conține codul executat de thread după apelul start()
    @Override
    public void run() {
        // Numele thread-ului curent (ex. "Th2"), afișat la începutul fiecărei linii
        String nume = Thread.currentThread().getName();

        // Suma acumulată a produselor; long ca să evităm depășirea lui int
        long suma = 0;

        // Găsim cel mai mare indice par din tablou:
        // dacă ultimul indice (mas.length - 1) e par, pornim de la el,
        // altfel pornim de la penultimul (mas.length - 2)
        int start = mas.length - 1;
        if (start % 2 != 0) {
            start = mas.length - 2;
        }

        // i = primul indice par al perechii, j = i - 2 = al doilea indice par.
        // Pasul este 4 (descrescător), pentru că o pereche consumă două poziții pare.
        // Condiția i - 2 >= 0 garantează că nu ieșim din limitele tabloului;
        // dacă primul element par rămâne fără pereche, bucla se oprește și el se ignoră.
        for (int i = start; i - 2 >= 0; i -= 4) {
            int j = i - 2;

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
