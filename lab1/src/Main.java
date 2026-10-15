import javax.swing.*;
import java.awt.*;

/**
 * Lucrare de laborator 1 - Varianta 8
 *
 * Th1: sumele produselor numerelor impare doua cate doua, de la PRIMUL element.
 * Th2: sumele produselor numerelor impare doua cate doua, de la ULTIMUL element.
 *
 * Clasa contine tabloul comun, sarcina firelor (Runnable) si interfata grafica.
 * Metoda main se afla in clasa Main.
 */
class LThread {

        public static final int SIZE = 100;

        // Tablou comun: firele doar il CITESC, deci nu exista sectiune critica.
        public static final int[] mas = new int[SIZE];

        private JTextArea areaMas, areaTh1, areaTh2, areaInfo;
        private JLabel lblTh1, lblTh2;
        private volatile Thread th1, th2;
        private volatile Thread runner; // firul care executa secventa principala
        private JButton b1;

        private static final String INFO = "Lucrare de laborator 1 - Varianta 8\n"
                + "Student 1: Stecalovici Lucian, grupa CR-241\n"
                + "Student 2: Macovetchi Bogdan, grupa CR-241\n";

        // ------------------------------------------------------------------
        // Sarcina unui fir (interfata Runnable)
        // step = +1 -> de la primul element; step = -1 -> de la ultimul element
        // ------------------------------------------------------------------
        static class ProductTask implements Runnable {
                private final int start;
                private final int step;
                private final JTextArea out;
                private final JLabel result;

                private long total = 0;
                private int pairs = 0;

                ProductTask(int start, int step, JTextArea out, JLabel result) {
                        this.start = start;
                        this.step = step;
                        this.out = out;
                        this.result = result;
                }

                @Override
                public void run() {
                        String name = Thread.currentThread().getName();
                        int pendingIdx = -1; // primul impar al perechii curente

                        for (int i = start; i >= 0 && i < SIZE; i += step) {
                                if (mas[i] % 2 != 0) {
                                        if (pendingIdx == -1) {
                                                pendingIdx = i;
                                        } else {
                                                long prod = (long) mas[pendingIdx] * mas[i];
                                                total += prod;
                                                pairs++;
                                                String line = String.format("%s: %d * %d = %d | suma = %d%n",
                                                        name, mas[pendingIdx], mas[i], prod, total);
                                                SwingUtilities.invokeLater(() -> out.append(line));
                                                pendingIdx = -1;
                                        }
                                }
                                try {
                                        Thread.sleep(20); // pauza mica, pentru a vedea executia concurenta
                                } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        return;
                                }
                        }

                        String fin = String.format("%s: perechi = %d, suma produselor = %d",
                                name, pairs, total);
                        SwingUtilities.invokeLater(() -> result.setText(fin));
                }
        }

        // ------------------------------------------------------------------
        // Interfata grafica
        // ------------------------------------------------------------------
        public void buildGui() throws Exception {
                SwingUtilities.invokeAndWait(() -> {
                        JFrame frame = new JFrame("Laborator 1 - Thread-uri (Varianta 8)");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLayout(new BorderLayout(8, 8));

                        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 13);

                        areaMas = new JTextArea(4, 60);
                        areaMas.setEditable(false);
                        areaMas.setLineWrap(true);
                        areaMas.setFont(mono);
                        JScrollPane spMas = new JScrollPane(areaMas);
                        spMas.setBorder(BorderFactory.createTitledBorder("Tabloul mas[] (100 elemente)"));

                        areaTh1 = new JTextArea();
                        areaTh2 = new JTextArea();
                        areaTh1.setEditable(false);
                        areaTh2.setEditable(false);
                        areaTh1.setFont(mono);
                        areaTh2.setFont(mono);

                        lblTh1 = new JLabel("Th1: se executa...");
                        lblTh2 = new JLabel("Th2: se executa...");

                        JPanel p1 = new JPanel(new BorderLayout());
                        p1.setBorder(BorderFactory.createTitledBorder("Th1 - de la primul element"));
                        p1.add(new JScrollPane(areaTh1), BorderLayout.CENTER);
                        p1.add(lblTh1, BorderLayout.SOUTH);

                        JPanel p2 = new JPanel(new BorderLayout());
                        p2.setBorder(BorderFactory.createTitledBorder("Th2 - de la ultimul element"));
                        p2.add(new JScrollPane(areaTh2), BorderLayout.CENTER);
                        p2.add(lblTh2, BorderLayout.SOUTH);

                        JPanel center = new JPanel(new GridLayout(1, 2, 8, 8));
                        center.add(p1);
                        center.add(p2);

                        areaInfo = new JTextArea(5, 60);
                        areaInfo.setEditable(false);
                        areaInfo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
                        JScrollPane spInfo = new JScrollPane(areaInfo);
                        spInfo.setBorder(BorderFactory.createTitledBorder("Thread-ul principal"));

                        frame.add(spMas, BorderLayout.NORTH);
                        frame.add(center, BorderLayout.CENTER);
                        b1 = new JButton("Reset");
                        b1.addActionListener(e -> reset());

                        JPanel south = new JPanel(new BorderLayout(8, 8));
                        south.add(spInfo, BorderLayout.CENTER);
                        south.add(b1, BorderLayout.EAST);

                        frame.add(south, BorderLayout.SOUTH);

                        frame.setSize(900, 650);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                });
        }

        // Generare tablou: valori intre 1 si 100
        public void generateArray() {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < SIZE; i++) {
                        mas[i] = (int) (Math.random() * 100) + 1;
                        sb.append(mas[i]).append(' ');
                }
                String text = sb.toString();
                SwingUtilities.invokeLater(() -> areaMas.setText(text));
        }

        // Creare si pornire fire prin Runnable
        public void startThreads() {
                th1 = new Thread(new ProductTask(0, +1, areaTh1, lblTh1), "Th1");
                th2 = new Thread(new ProductTask(SIZE - 1, -1, areaTh2, lblTh2), "Th2");
                th1.start();
                th2.start();
        }

        // Asteptarea terminarii ambelor fire
        public void awaitThreads() throws InterruptedException {
                th1.join();
                th2.join();
        }

        // Adauga un caracter in zona thread-ului principal
        public void appendInfoChar(char c) {
                SwingUtilities.invokeLater(() -> areaInfo.append(String.valueOf(c)));
        }

        // Secventa completa: tablou nou, Th1 si Th2, apoi informatia litera cu litera
        public void runAll() {
                runner = Thread.currentThread();
                try {
                        generateArray();   // mas[] aleatoriu, valori 1..100
                        startThreads();    // porneste Th1 si Th2 (Runnable)
                        awaitThreads();    // join() pe ambele fire

                        // Dupa terminarea ambelor fire: litera cu litera, la 100 ms
                        for (char c : INFO.toCharArray()) {
                                appendInfoChar(c);
                                Thread.sleep(100);
                        }
                } catch (InterruptedException e) {
                        // executia a fost oprita de butonul Reset
                }
        }

        // Butonul Reset: opreste firele curente, curata interfata si reia secventa
        private void reset() {
                b1.setEnabled(false);
                new Thread(() -> {
                        stopAndJoin(runner);
                        stopAndJoin(th1);
                        stopAndJoin(th2);
                        runner = Thread.currentThread();

                        SwingUtilities.invokeLater(() -> {
                                areaTh1.setText("");
                                areaTh2.setText("");
                                areaInfo.setText("");
                                lblTh1.setText("Th1: se executa...");
                                lblTh2.setText("Th2: se executa...");
                                b1.setEnabled(true);
                        });
                        runAll();
                }, "Main-reset").start();
        }

        private static void stopAndJoin(Thread t) {
                if (t == null) return;
                t.interrupt();
                try {
                        t.join();
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }
        }
}

/**
 * Clasa principala: ruleaza thread-ul principal.
 */
public class Main {

        public static void main(String[] args) throws Exception {
                LThread lab = new LThread();

                lab.buildGui();   // interfata grafica
                lab.runAll();     // tablou, Th1/Th2, join(), apoi afisarea informatiei
        }
}