// executaTh1() — parcurge tabloul de la început spre sfârșit și formează perechi din numerele pare.
// executaTh2() — parcurge tabloul de la sfârșit spre început și formează perechi din numerele pare.

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class ClasaAxenia implements Runnable {

    private final int[] tab;
    private final boolean first;
    private final String numeFir;
    private final JTextArea afisaj;

    public ClasaAxenia(int[] tab, boolean first, String numeFir, JTextArea afisaj) {
        this.tab = tab;
        this.first = first;
        this.numeFir = numeFir;
        this.afisaj = afisaj;
    }

    @Override
    public void run() {
        List<Integer> numerePare = new ArrayList<>();

        if (first) {
            for (int i = 0; i < tab.length; i++) {
                if (tab[i] % 2 == 0) {
                    numerePare.add(tab[i]);
                }
            }
        } else {
            for (int i = tab.length - 1; i >= 0; i--) {
                if (tab[i] % 2 == 0) {
                    numerePare.add(tab[i]);
                }
            }
        }

        for (int i = 0; i + 1 < numerePare.size(); i += 2) {
            int primul = numerePare.get(i);
            int alDoilea = numerePare.get(i + 1);
            int suma = primul + alDoilea;
            String text = numeFir + ": " + primul + " + " + alDoilea + " = " + suma + "\n";

            SwingUtilities.invokeLater(() -> afisaj.append(text));

            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(numeFir + " a fost intrerupt.");
                return;
            }
        }

        if (numerePare.size() % 2 == 1) {
            String textSingur = numeFir + ": " + numerePare.get(numerePare.size() - 1) + " ramane fara pereche\n";
            SwingUtilities.invokeLater(() -> afisaj.append(textSingur));
        }
    }
}
