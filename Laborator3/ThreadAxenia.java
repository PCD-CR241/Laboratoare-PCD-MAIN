import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ThreadAxenia extends Thread {
    private final int[] date;
    private final JTextArea afisaj;
    private final boolean deLaInceput;
    private final Thread firDeAsteptat;

    public ThreadAxenia(int[] date, JTextArea afisaj, boolean deLaInceput, Thread firDeAsteptat) {
        this.date = date;
        this.afisaj = afisaj;
        this.deLaInceput = deLaInceput;
        this.firDeAsteptat = firDeAsteptat;

        this.setPriority(deLaInceput ? Thread.MAX_PRIORITY : Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
        try {
            asteaptaFirAnterior();

            String numeFir = deLaInceput ? "Th1" : "Th2";
            String descriere = deLaInceput
                    ? "Sarcina 1 (sumele numerelor pare 2 cate 2, de la primul element)"
                    : "Sarcina 2 (sumele numerelor pare 2 cate 2, de la ultimul element)";

            scrieInConsola(numeFir + " - " + descriere + "\n");

            int numarParAnterior = -1;
            int perechiGasite = 0;

            int start = deLaInceput ? 0 : date.length - 1;
            int stop = deLaInceput ? date.length : -1;
            int pas = deLaInceput ? 1 : -1;

            for (int i = start; i != stop; i += pas) {
                if (date[i] % 2 == 0) {
                    if (numarParAnterior == -1) {
                        numarParAnterior = date[i];
                    } else {
                        int sumaPereche = numarParAnterior + date[i];
                        perechiGasite++;
                        scrieInConsola(numarParAnterior + "+" + date[i] + "=" + sumaPereche + "\n");
                        numarParAnterior = -1;
                    }
                }
            }

            if (numarParAnterior != -1) {
                scrieInConsola("(ramas fara pereche: " + numarParAnterior + ")");
            }

            scrieInConsola("\n" + numeFir + " - perechi pare sumate: " + perechiGasite + "\n\n");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            scrieInConsola("\n" + (deLaInceput ? "Th1" : "Th2") + " a fost intrerupt.\n");
        }
    }

    private void scrieInConsola(String text) {
        SwingUtilities.invokeLater(() -> afisaj.append(text));
    }

    private void asteaptaFirAnterior() throws InterruptedException {
        while (firDeAsteptat != null && firDeAsteptat.isAlive()) {
            Thread.sleep(50);
        }
    }
}