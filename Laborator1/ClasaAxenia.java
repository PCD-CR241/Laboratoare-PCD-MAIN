import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ClasaAxenia {
    private int[] mas;
    private JTextArea zonaText;

    public ClasaAxenia(int[] mas, JTextArea zonaText) {
        this.mas = mas;
        this.zonaText = zonaText;
    }

    public void executaTh1() {
        SwingUtilities.invokeLater(() -> zonaText.append("[Axenia-Th1] Cautare de la inceput...\n"));
        Integer primulPar = null;
        for (int i = 0; i < mas.length; i++) {
            if (mas[i] % 2 == 0) {
                if (primulPar == null) {
                    primulPar = mas[i];
                } else {
                    int suma = primulPar + mas[i];
                    String text = "[Axenia-Th1] Pereche: " + primulPar + " + " + mas[i] + " = " + suma + "\n";
                    SwingUtilities.invokeLater(() -> zonaText.append(text));
                    primulPar = null;
                    try { Thread.sleep(40); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }
    }

    public void executaTh2() {
        SwingUtilities.invokeLater(() -> zonaText.append("[Axenia-Th2] Cautare de la sfarsit...\n"));
        Integer primulPar = null;
        for (int i = mas.length - 1; i >= 0; i--) {
            if (mas[i] % 2 == 0) {
                if (primulPar == null) {
                    primulPar = mas[i];
                } else {
                    int suma = primulPar + mas[i];
                    String text = "[Axenia-Th2] Pereche: " + primulPar + " + " + mas[i] + " = " + suma + "\n";
                    SwingUtilities.invokeLater(() -> zonaText.append(text));
                    primulPar = null;
                    try { Thread.sleep(40); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }
    }
}
