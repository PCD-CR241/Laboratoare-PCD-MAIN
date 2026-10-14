/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication10;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Lucrare de laborator 1 - Crearea thread-urilor
 *
 * Conditia 1:
 * Sumele numerelor impare doua cate doua,
 * incepand cautarea si sumarea de la primul element.
 *
 * Conditia 2:
 * Sumele numerelor impare doua cate doua,
 * incepand cautarea si sumarea de la ultimul element.
 *
 * Toate cele 4 fire folosesc acelasi tablou mas[].
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

        // Parcurgere de la primul sau de la ultimul element
        int step = fromStart ? 1 : -1;
        int i = fromStart ? 0 : mas.length - 1;

        int pairNo = 1;
        int firstIdx = -1;

        for (; i >= 0 && i < mas.length; i += step) {

            // Cautam doar numerele impare
            if (mas[i] % 2 != 0) {

                // Primul numar impar din pereche
                if (firstIdx == -1) {
                    firstIdx = i;

                } else {

                    // Al doilea numar impar din pereche
                    int a = mas[firstIdx];
                    int b = mas[i];

                    // Se face suma valorilor
                    int sum = a + b;

                    out.accept(String.format(
                            "%s | #%d: mas[%d]=%d, mas[%d]=%d -> suma = %d%n",
                            Thread.currentThread().getName(),
                            pairNo++,
                            firstIdx,
                            a,
                            i,
                            b,
                            sum
                    ));

                    // Perechea este terminata
                    firstIdx = -1;

                    // Pauza mica pentru a se observa executia firelor
                    sleepQuietly(20);
                }
            }
        }

        // Daca a ramas un numar impar fara pereche
        if (firstIdx != -1) {
            out.accept(String.format(
                    "%s | mas[%d]=%d ramas fara pereche%n",
                    Thread.currentThread().getName(),
                    firstIdx,
                    mas[firstIdx]
            ));
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

// ---------- Interfata grafica ----------
public class JavaApplication10 extends JFrame {

    private static final String STUDENT_INFO =
            "Studenti: Birca Maxim si Vozian Stefan, grupa CR-241, Laboratorul 1";

    // Tabloul comun pentru toate cele 4 fire
    private final int[] mas = new int[100];

    private final JTextArea arrayArea = new JTextArea(4, 60);

    private final JTextArea th1Area = new JTextArea();
    private final JTextArea th2Area = new JTextArea();
    private final JTextArea th3Area = new JTextArea();
    private final JTextArea th4Area = new JTextArea();

    private final JLabel infoLabel = new JLabel(" ");

    private final JButton startBtn =
            new JButton("Genereaza tablou si porneste");

    public JavaApplication10() {

        super("PCD - Lab 1 - 4 fire de executie");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // ---------- Tabloul ----------
        arrayArea.setEditable(false);
        arrayArea.setLineWrap(true);
        arrayArea.setWrapStyleWord(true);

        JScrollPane arrScroll = new JScrollPane(arrayArea);

        arrScroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Tabloul mas[] (100 elemente, valori 1..100)"
                )
        );

        add(arrScroll, BorderLayout.NORTH);

        // ---------- Cele 4 fire ----------
        JPanel center = new JPanel(
                new GridLayout(2, 2, 8, 8)
        );

        center.add(
                makePanel(
                        "Th1 - Conditia 1: de la primul element",
                        th1Area
                )
        );

        center.add(
                makePanel(
                        "Th2 - Conditia 2: de la ultimul element",
                        th2Area
                )
        );

        center.add(
                makePanel(
                        "Th3 - Conditia 1: de la primul element",
                        th3Area
                )
        );

        center.add(
                makePanel(
                        "Th4 - Conditia 2: de la ultimul element",
                        th4Area
                )
        );

        add(center, BorderLayout.CENTER);

        // ---------- Partea de jos ----------
        JPanel bottom = new JPanel(
                new BorderLayout(8, 8)
        );

        infoLabel.setFont(
                new Font(
                        Font.SANS_SERIF,
                        Font.BOLD,
                        14
                )
        );

        infoLabel.setBorder(
                BorderFactory.createTitledBorder(
                        "Thread principal"
                )
        );

        bottom.add(startBtn, BorderLayout.NORTH);
        bottom.add(infoLabel, BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);

        // Butonul de pornire
        startBtn.addActionListener(e -> startWork());

        setSize(1100, 750);
        setLocationRelativeTo(null);
    }

    // Creeaza un panou pentru fiecare thread
    private static JScrollPane makePanel(
            String title,
            JTextArea area
    ) {

        area.setEditable(false);

        area.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        12
                )
        );

        JScrollPane sp = new JScrollPane(area);

        sp.setBorder(
                BorderFactory.createTitledBorder(title)
        );

        return sp;
    }

    // ---------- Pornirea lucrarii ----------
    private void startWork() {

        startBtn.setEnabled(false);

        // Stergem rezultatele anterioare
        for (JTextArea a : new JTextArea[]{
                th1Area,
                th2Area,
                th3Area,
                th4Area
        }) {
            a.setText("");
        }

        infoLabel.setText(" ");

        // ---------- Generarea tabloului comun ----------
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < mas.length; i++) {

            // Valori intre 1 si 100
            mas[i] = (int) (Math.random() * 100) + 1;

            sb.append(mas[i]).append(' ');
        }

        arrayArea.setText(sb.toString());

        // ---------- Firul de coordonare ----------
        Thread controller = new Thread(() -> {

            // Th1 - Conditia 1:
            // parcurgere de la primul element
            Thread th1 = new Thread( new OddPairSumTask(mas, true, s -> append(th1Area, s)),"Th1");

            // Th2 - Conditia 2:
            // parcurgere de la ultimul element
            Thread th2 = new Thread(new OddPairSumTask(mas,false, s -> append(th2Area, s)),"Th2");

            // Th3 - Conditia 1:
            // parcurgere de la primul element
            Thread th3 = new Thread(new OddPairSumTask(mas, true, s -> append(th3Area, s) ), "Th3");
            // Th4 - Conditia 2:
            // parcurgere de la ultimul element
            Thread th4 = new Thread(new OddPairSumTask(mas, false, s -> append(th4Area, s) ), "Th4");

            // Pornim toate cele 4 fire
            th1.start();
            th2.start();
            th3.start();
            th4.start();

            try {
                // Asteptam terminarea tuturor celor 4 fire
                th1.join();
                th2.join();
                th3.join();
                th4.join();

                // ---------- Afisarea informatiilor despre studenti ----------
                // Fiecare litera apare la un interval de 100 ms
                for (int i = 1;
                     i <= STUDENT_INFO.length();
                     i++) {

                    final String part =
                            STUDENT_INFO.substring(0, i);

                    SwingUtilities.invokeLater(
                            () -> infoLabel.setText(part)
                    );

                    Thread.sleep(100);
                }

            } catch (InterruptedException ex) {

                Thread.currentThread().interrupt();
            }

            SwingUtilities.invokeLater(
                    () -> startBtn.setEnabled(true)
            );

        }, "main-controller");

        controller.start();
    }

    // Afisarea rezultatelor thread-urilor
    private static void append(
            JTextArea area,
            String text
    ) {

        SwingUtilities.invokeLater(
                () -> area.append(text)
        );
    }

    // ---------- Metoda main ----------
    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new JavaApplication10().setVisible(true)
        );
    }
}