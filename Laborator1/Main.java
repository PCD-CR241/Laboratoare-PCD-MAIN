import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[] mas = new int [100];
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

        zonaTextComuna.append("=== START EXECUTIE FIRE ===\n\n");

        ClasaAxenia axenia = new ClasaAxenia(mas, zonaTextComuna);
        Thread tAxenia1 = new Thread(axenia::executaTh1);
        Thread tAxenia2 = new Thread(axenia::executaTh2);
        tAxenia1.start(); tAxenia2.start();

        Thread tViorica1 = null;
        Thread tViorica2 = null;
        try {
            Class<?> clasaVio = Class.forName("ClasaViorica");
            Object vioricaInstance = clasaVio.getConstructor(int[].class, JTextArea.class).newInstance(mas, zonaTextComuna);

            tViorica1 = new Thread(() -> {
                try { clasaVio.getMethod("executaTh1").invoke(vioricaInstance); } catch (Exception e) {}
            });
            tViorica2 = new Thread(() -> {
                try { clasaVio.getMethod("executaTh2").invoke(vioricaInstance); } catch (Exception e) {}
            });

            tViorica1.start(); tViorica2.start();
        } catch (ClassNotFoundException e) {
            System.out.println("[Sistem] ClasaViorica nu a fost gasita inca. Ruleaza doar Axenia.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tAxenia1.join(); tAxenia2.join();
            if (tViorica1 != null) tViorica1.join();
            if (tViorica2 != null) tViorica2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String textFinal = "\n\n=== TOATE FIRELE AU FINISAT ===\nLucrarea a fost efectuata de studentele: Agachi si Timbal.";
        for (char litera : textFinal.toCharArray()) {
            SwingUtilities.invokeLater(() -> zonaTextComuna.append(String.valueOf(litera)));
            try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
