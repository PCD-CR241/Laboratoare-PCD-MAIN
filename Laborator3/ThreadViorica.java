import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ThreadViorica extends Thread {
    private final int[] date;
    private final JTextArea afisaj;
    private final boolean start;
    private final Thread firAsteptat;

    public ThreadViorica(int[] date, JTextArea afisaj, boolean start,
                         Thread firAsteptat) {
        this.date = date;
        this.afisaj = afisaj;
        this.start = start;
        this.firAsteptat = firAsteptat;
    }

    @Override
    public void run() {
        try {
            if (start) {
                asteaptaTh1();
            } else {
                asteaptaTh2();
            }

            int minim = start ? 100 : 300;
            int maxim = start ? 500 : 700;
            int numar = 0;
            int suma = 0;

            afiseaza((start ? "Th3 - Sarcina 3, parcurgere de la inceput, "
                    : "Th4 - Sarcina 4, parcurgere de la sfarsit, ")
                    + "intervalul [" + minim + ", " + maxim + "]\n");

            int pozitie = start ? 0 : date.length - 1;
            int pas = start ? 1 : -1;
            while (pozitie >= 0 && pozitie < date.length) {
                int valoare = date[pozitie];
                if (valoare >= minim && valoare <= maxim) {
                    afiseaza(valoare + " ");
                    numar++;
                    suma += valoare;
                }
                pozitie += pas;
                Thread.yield();
            }

            afiseaza("\n" + (start ? "Th3" : "Th4")
                    + " - valori: " + numar + ", suma: " + suma + "\n");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            afiseaza("\n" + (start ? "Th3" : "Th4")
                    + " a fost intrerupt.\n");
        }
    }

    private void afiseaza(String text) {
        SwingUtilities.invokeLater(() -> afisaj.append(text));
    }

    private void asteaptaTh1() throws InterruptedException {
        if (firAsteptat != null) {
            firAsteptat.join();
        }
    }

    private void asteaptaTh2() throws InterruptedException {
        if (firAsteptat != null) {
            firAsteptat.join();
        }
    }

}
