import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[] date = genereazaDate();

        JFrame fereastra = new JFrame("Proiect Comun - Agachi Viorica & Timbal Axenia");
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.setSize(650, 450);
        fereastra.setLayout(new BorderLayout());

        JTextArea zonaTextComuna = new JTextArea();
        zonaTextComuna.setEditable(false);
        zonaTextComuna.setFont(new Font("Monospaced", Font.PLAIN, 12));
        zonaTextComuna.setBackground(Color.BLACK);
        zonaTextComuna.setForeground(Color.GREEN);

        JScrollPane scroll = new JScrollPane(zonaTextComuna);
        fereastra.add(scroll, BorderLayout.CENTER);
        fereastra.setVisible(true);

        zonaTextComuna.append("START EXECUTIE FIRE\n\n");

        ThreadAxenia th1 = new ThreadAxenia(date, zonaTextComuna, true, null);
        ThreadAxenia th2 = new ThreadAxenia(date, zonaTextComuna, false, th1);

        ThreadViorica th3 = new ThreadViorica(date, zonaTextComuna, true, th2);
        ThreadViorica th4 = new ThreadViorica(date, zonaTextComuna, false, th3);

        th1.start();
        th2.start();

        th3.start();
        th4.start();

        try {
            th1.join();
            th2.join();

            th3.join();
            th4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            zonaTextComuna.append("\nExecutia a fost intrerupta.\n");
        }

        String textFinal = "\n\nTh1 - Prenumele studentelor: Axenia si Viorica\n"
                + "Th2 - Numele studentelor: Timbal si Agachi\n"
                + "Th3 - Denumirea disciplinei: Programarea concurenta si distribuita\n"
                + "Th4 - Grupa: CR-241\n"
                + "TOATE FIRELE AU FINISAT\n"
                + "Lucrarea a fost efectuata de studentele: Agachi si Timbal.";

        for (char litera : textFinal.toCharArray()) {
            System.out.print(litera);
            System.out.flush();
            SwingUtilities.invokeLater(() ->
                    zonaTextComuna.append(String.valueOf(litera)));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private static int[] genereazaDate() {
        Random random = new Random();
        int[] date = new int[100];
        for (int i = 0; i < date.length; i++) {
            date[i] = random.nextInt(1000) + 1;
        }
        return date;
    }
}
