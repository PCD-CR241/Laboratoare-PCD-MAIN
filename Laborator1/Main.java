import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[] mas = new int[100];
        Random rand = new Random();
        for (int i = 0; i < mas.length; i++) {
            mas[i] = rand.nextInt(100) + 1;
        }

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

        Thread tAxenia1 = new Thread(new ClasaAxenia(mas, true, "Axenia-Th1", zonaTextComuna));
        Thread tAxenia2 = new Thread(new ClasaAxenia(mas, false, "Axenia-Th2", zonaTextComuna));

        tAxenia1.start();
        tAxenia2.start();

        Thread tViorica1 = null;
        Thread tViorica2 = null;
        try {
            Class<?> clasaVio = Class.forName("ClasaViorica");
            java.lang.reflect.Constructor<?> constructorVio =
                    clasaVio.getConstructor(
                    int[].class, JTextArea.class, int.class, int.class, String.class, String.class
            );

            tViorica1 = (Thread) constructorVio.newInstance(
                    mas, zonaTextComuna, 0, 1,
                    "Viorica-Th1", "Incepe cautarea de la inceput...");
            tViorica2 = (Thread) constructorVio.newInstance(
                    mas, zonaTextComuna, mas.length - 1, -1,
                    "Viorica-Th2", "Incepe cautarea de la sfarsit...");

            tViorica1.start();
            tViorica2.start();
        } catch (ClassNotFoundException e) {
            System.out.println("[Sistem] ClasaViorica nu a fost gasita inca. Ruleaza doar Axenia.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tAxenia1.join();
            tAxenia2.join();
            if (tViorica1 != null) tViorica1.join();
            if (tViorica2 != null) tViorica2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String textFinal = "\n\nTOATE FIRELE AU FINISAT \nLucrarea a fost efectuata de studentele: Agachi si Timbal.";
        for (char litera : textFinal.toCharArray()) {
            System.out.print(litera);
            System.out.flush();
            SwingUtilities.invokeLater(() -> zonaTextComuna.append(String.valueOf(litera)));
            try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
