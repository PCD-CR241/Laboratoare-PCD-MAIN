package Stecalovici;

import javax.swing.*;

/**
 * Firul de executie: parcurge mas[] (de la primul sau de la ultimul element),
 * grupeaza elementele impare in perechi si aduna produsele lor.
 */
public class LThread implements Runnable {

        public static final int SIZE = 100;

        public static final int[] mas = new int[SIZE];

        private final int start;
        private final int step;
        private final JTextArea out;
        private final JLabel result;

        private long total = 0;
        private int pairs = 0;

        public LThread(int start, int step, JTextArea out, JLabel result) {
                this.start = start;
                this.step = step;
                this.out = out;
                this.result = result;
        }

        // Umple mas[] cu valori aleatorii 1..100 si intoarce textul pentru afisare
        public static String generateArray() {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < SIZE; i++) {
                        mas[i] = (int) (Math.random() * 100) + 1;
                        sb.append(mas[i]).append(' ');
                }
                return sb.toString();
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
                                Thread.sleep(20);
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
