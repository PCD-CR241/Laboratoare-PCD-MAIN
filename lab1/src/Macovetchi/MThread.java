package Macovetchi;

import Stecalovici.LThread;

import javax.swing.*;

/**
 * Firul de executie Th2 (partea lui Macovetchi Bogdan):
 * sumele produselor numerelor impare doua cate doua,
 * incepand cautarea si sumarea cu ultimul element din mas[].
 */
public class MThread implements Runnable {

        private final JTextArea out;
        private final JLabel result;

        private long total = 0;
        private int pairs = 0;

        public MThread(JTextArea out, JLabel result) {
                this.out = out;
                this.result = result;
        }

        @Override
        public void run() {
                String name = Thread.currentThread().getName();
                int[] mas = LThread.mas;
                int first = -1; // primul impar al perechii curente

                for (int i = LThread.SIZE - 1; i >= 0; i--) {
                        if (mas[i] % 2 != 0) {
                                if (first == -1) {
                                        first = mas[i];
                                } else {
                                        long prod = (long) first * mas[i];
                                        total += prod;
                                        pairs++;
                                        String line = String.format("%s: %d * %d = %d | suma = %d%n",
                                                name, first, mas[i], prod, total);
                                        SwingUtilities.invokeLater(() -> out.append(line));
                                        first = -1;
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
