import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class ClasaViorica extends Thread {
    private final int[] tab;
    private final JTextArea afisaj;
    private final int start;
    private final int sens;
    private final String eticheta;
    private final String mesaj;
    
    public ClasaViorica(int[] tab, JTextArea afisaj, int start, int sens,
                        String eticheta, String mesaj) {
        this.tab = tab;
        this.afisaj = afisaj;
        this.start = start;
        this.sens = sens;
        this.eticheta = eticheta;
        this.mesaj = mesaj;
    }

    @Override
    public void run() {
        try {
            executaCautarea();
        } catch (IllegalArgumentException e) {
            if (afisaj != null) {
                afiseaza("Eroare: " + e.getMessage() + "\n");
            } else {
                System.err.println("Eroare: " + e.getMessage());
            }
        }
    }

    private void executaCautarea() {
        if (tab == null || afisaj == null) {
            throw new IllegalArgumentException("datele firului nu sunt valide");
        }
        if (sens != 1 && sens != -1) {
            throw new IllegalArgumentException("sensul trebuie sa fie 1 sau -1");
        }

        SwingUtilities.invokeLater(() ->
                afisaj.append(eticheta + " " + mesaj + "\n"));

        List<Integer> pare = new ArrayList<>();
        for (int pozitie = start;
             pozitie >= 0 && pozitie < tab.length;
             pozitie += sens) {
            if (tab[pozitie] % 2 == 0) {
                pare.add(tab[pozitie]);
            }
        }

        for (int pozitie = 0; pozitie + 1 < pare.size(); pozitie += 2) {
            int valoare1 = pare.get(pozitie);
            int valoare2 = pare.get(pozitie + 1);
            int suma = valoare1 + valoare2;
            String mesajPereche = eticheta + " Pereche gasita: "
                    + valoare1 + " + " + valoare2 + " = "
                    + suma + "\n";

            afiseaza(mesajPereche);
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                afiseaza(eticheta + " Fir intrerupt.\n");
                return;
            }
        }
    }

    private void afiseaza(String text) {
        SwingUtilities.invokeLater(() -> afisaj.append(text));
    }
}
