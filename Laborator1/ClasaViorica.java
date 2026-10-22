import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ClasaViorica {
    private int[] mas;
    private JTextArea zonaText;
    
    public ClasaViorica(int[] mas, JTextArea zonaText) {
        this.mas = mas;
        this.zonaText = zonaText;
    }
    
    public void executaTh1() {
        SwingUtilities.invokeLater(() -> zonaText.append("[Viorica-Th1] Incepe cautarea de la inceput...\n"));
        Integer primulPar = null;

        for (int i = 0; i < mas.length; i++) {
            if (mas[i] % 2 == 0) {
                if (primulPar == null) {
                    primulPar = mas[i]; 
                } else {
                    int suma = primulPar + mas[i]; 
                    String text = "[Viorica-Th1] Pereche gasita: " + primulPar + " + " + mas[i] + " = " + suma + "\n";
                    
                    SwingUtilities.invokeLater(() -> zonaText.append(text));
                    primulPar = null; 

                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public void executaTh2() {
        SwingUtilities.invokeLater(() -> zonaText.append("[Viorica-Th2] Incepe cautarea de la sfarsit...\n"));
        Integer primulPar = null;

        for (int i = mas.length - 1; i >= 0; i--) {
            if (mas[i] % 2 == 0) {
                if (primulPar == null) {
                    primulPar = mas[i]; 
                } else {
                    int suma = primulPar + mas[i];
                    String text = "[Viorica-Th2] Pereche gasita: " + primulPar + " + " + mas[i] + " = " + suma + "\n";
                    
                    SwingUtilities.invokeLater(() -> zonaText.append(text));
                    primulPar = null; 

                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
