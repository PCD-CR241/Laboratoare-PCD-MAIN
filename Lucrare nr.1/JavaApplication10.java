package javaapplication10;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Lucrare de laborator 1 - Crearea thread-urilor
 * Conditia 1: suma numerelor impare doua cate doua, de la primul element.
 * Conditia 2: suma numerelor impare doua cate doua, de la ultimul element
 * Toate cele 4 fire folosesc acelasi tablou mas[] si scriu in ACEEASI fereastra
 */

// ---------- Firul de executie ----------
class OddPairSumTask implements Runnable {

    private final int[] mas;
    private final boolean fromStart;
    private final Consumer<String> out;

    public OddPairSumTask(int[] mas, boolean fromStart, Consumer<String> out) {
        this.mas = mas;
        this.fromStart = fromStart;
        this.out = out;
    }

    @Override
    public void run() {
        int step = fromStart ? 1 : -1;               // directia de parcurgere
        int i = fromStart ? 0 : mas.length - 1;      // pozitia de start
        int pairNo = 1;                              // numarul perechii
        int firstIdx = -1;                           // indexul primului impar din pereche

        for (; i >= 0 && i < mas.length; i += step) {
            if (mas[i] % 2 != 0) {                   // doar numere impare
                if (firstIdx == -1) {
                    firstIdx = i;                    // primul impar al perechii
                } else {
                    int a = mas[firstIdx];
                    int b = mas[i];
                    int sum = a + b;                 // suma perechii

                    out.accept(String.format(
                            "%s | #%d: mas[%d]=%d + mas[%d]=%d -> suma = %d%n",
                            Thread.currentThread().getName(),
                            pairNo++, firstIdx, a, i, b, sum));

                    firstIdx = -1;                   // perechea e terminata
                    sleepQuietly(20);                // pauza ca sa se vada alternarea firelor
                }
            }
        }

        // impar ramas fara pereche
        if (firstIdx != -1) {
            out.accept(String.format("%s | mas[%d]=%d ramas fara pereche%n",
                    Thread.currentThread().getName(), firstIdx, mas[firstIdx]));
        }
    }

    private static void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// ---------- Interfata grafica (o singura fereastra) ----------
public class JavaApplication10 extends JFrame {

    private static final String STUDENT_INFO =
            "Studenti: Birca Maxim si Vozian Stefan, grupa CR-241, Laboratorul 1";

    private final int[] mas = new int[100];

    private final JTextArea arrayArea = new JTextArea(4, 60);
    private final JTextArea outArea = new JTextArea();   // o singura zona pentru toate firele
    private final JLabel infoLabel = new JLabel(" ");
    private final JButton startBtn = new JButton("Genereaza tablou si porneste");

    public JavaApplication10() {
        super("PCD - Lab 1 - 4 fire de executie");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Sus: tabloul
        arrayArea.setEditable(false);
        arrayArea.setLineWrap(true);
        arrayArea.setWrapStyleWord(true);
        JScrollPane arrScroll = new JScrollPane(arrayArea);
        arrScroll.setBorder(BorderFactory.createTitledBorder(
                "Tabloul mas[] (100 elemente, valori 1..100)"));
        add(arrScroll, BorderLayout.NORTH);

        // Centru: rezultatele celor 4 fire, intr-o singura zona
        outArea.setEditable(false);
        outArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane outScroll = new JScrollPane(outArea);
        outScroll.setBorder(BorderFactory.createTitledBorder(
                "Rezultate Th1..Th4 (Th1, Th3: de la primul; Th2, Th4: de la ultimul)"));
        add(outScroll, BorderLayout.CENTER);

        // Jos: buton + informatii studenti
        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        infoLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        infoLabel.setBorder(BorderFactory.createTitledBorder("Thread principal"));
        bottom.add(startBtn, BorderLayout.NORTH);
        bottom.add(infoLabel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        startBtn.addActionListener(e -> startWork());

        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    private void startWork() {
        startBtn.setEnabled(false);
        outArea.setText("");
        infoLabel.setText(" ");

        // Generarea tabloului comun
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mas.length; i++) {
            mas[i] = (int) (Math.random() * 100) + 1;
            sb.append(mas[i]).append(' ');
        }
        arrayArea.setText(sb.toString());

        // Thread de coordonare (ca sa nu blocam interfata cu join())
        Thread controller = new Thread(() -> {

            // Toate firele scriu in acelasi outArea
            Thread th1 = new Thread(new OddPairSumTask(mas, true,  s -> append(outArea, s)), "Th1");
            Thread th2 = new Thread(new OddPairSumTask(mas, false, s -> append(outArea, s)), "Th2");
            Thread th3 = new Thread(new OddPairSumTask(mas, true,  s -> append(outArea, s)), "Th3");
            Thread th4 = new Thread(new OddPairSumTask(mas, false, s -> append(outArea, s)), "Th4");

            th1.start();
            th2.start();
            th3.start();
            th4.start();

            try {
                // Asteptam terminarea tuturor firelor
                th1.join();
                th2.join();
                th3.join();
                th4.join();

                // Informatiile despre studenti, litera cu litera (100 ms)
                for (int i = 1; i <= STUDENT_INFO.length(); i++) {
                    final String part = STUDENT_INFO.substring(0, i);
                    SwingUtilities.invokeLater(() -> infoLabel.setText(part));
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            SwingUtilities.invokeLater(() -> startBtn.setEnabled(true));
        }, "main-controller");

        controller.start();
    }

    // Scriere in JTextArea doar din Event Dispatch Thread
    private static void append(JTextArea area, String text) {
        SwingUtilities.invokeLater(() -> area.append(text));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JavaApplication10().setVisible(true));
    }
}