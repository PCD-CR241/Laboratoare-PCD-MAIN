package Stecalovici;


public class LThread implements Runnable {

        public static final int SIZE = 100;

        public int[] tablou = new int[SIZE];

        private final int start;
        private final int step;
        private final GuiFrame gui;

        private long total = 0;
        private int pairs = 0;

        public LThread(int start, int step, GuiFrame gui, int[] tablou) {
                this.start = start;
                this.step = step;
                this.gui = gui;
                this.tablou = tablou;
        }


        @Override
        public void run() {
                String name = Thread.currentThread().getName();
                int pendingIdx = -1; // primul impar al perechii curente

                for (int i = start; i >= 0 && i < SIZE; i += step) {
                        if (tablou[i] % 2 != 0) {
                                if (pendingIdx == -1) {
                                        pendingIdx = i;
                                } else {
                                        long prod = (long) tablou[pendingIdx] * tablou[i];
                                        total += prod;
                                        pairs++;
                                        String line = String.format("%s: %d * %d = %d | suma = %d%n",
                                                name, tablou[pendingIdx], tablou[i], prod, total);
                                        gui.append(line);
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

                String fin = String.format("%s: perechi = %d, suma produselor = %d%n",
                        name, pairs, total);
                gui.append(fin);
        }
}
