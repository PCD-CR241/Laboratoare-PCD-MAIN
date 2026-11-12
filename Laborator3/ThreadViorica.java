import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ThreadViorica extends Thread {
    private final int[] date;
    private final JTextArea afisaj;
    private final boolean sarcinaTrei;
    private final Thread firAnterior;

    public ThreadViorica(int[] date, JTextArea afisaj, boolean sarcinaTrei,
                         Thread firAnterior) {
        this.date = date;
        this.afisaj = afisaj;
        this.sarcinaTrei = sarcinaTrei;
        this.firAnterior = firAnterior;
    }

    @Override
    public void run() {
        try {
            if (firAnterior != null) {
                firAnterior.join();
            }

            int minim = sarcinaTrei ? 100 : 300;
            int maxim = sarcinaTrei ? 500 : 700;
            int numar = 0;
            int suma = 0;

            afiseaza((sarcinaTrei ? "Th3 - Sarcina 3, parcurgere de la inceput, "
                    : "Th4 - Sarcina 4, parcurgere de la sfarsit, ")
                    + "intervalul [" + minim + ", " + maxim + "]\n");

            int pozitie = sarcinaTrei ? 0 : date.length - 1;
            int pas = sarcinaTrei ? 1 : -1;
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

            afiseaza("\n" + (sarcinaTrei ? "Th3" : "Th4")
                    + " - valori: " + numar + ", suma: " + suma + "\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            afiseaza("\n" + (sarcinaTrei ? "Th3" : "Th4")
                    + " a fost intrerupt.\n");
        }
    }

    private void afiseaza(String text) {
        SwingUtilities.invokeLater(() -> afisaj.append(text));
    }
}
