package Macovetchi;

import Stecalovici.GuiFrame;
import Stecalovici.LThread;


public class MThread implements Runnable {

        private final GuiFrame gui;
        private final int start;
        private final int pas;
        private final int nums[];

        private long total = 0;
        private int pairs = 0;

        public MThread(GuiFrame gui, int start, int step,int[] tablou) {
                this.gui = gui;
                this.start = start;
                this.pas=step;
                this.nums = tablou;
        }

        @Override
        public void run() {
                String name = Thread.currentThread().getName();
                int first = -1; // primul impar al perechii curente

                for (int i = start; i>=0 && i <Shared.SIZE; i+=pas) {
                        if (nums[i] % 2 != 0) {
                                if (first == -1) {
                                        first = nums[i];
                                } else {
                                        long prod = (long) first * nums[i];
                                        total += prod;
                                        pairs++;
                                        String line = String.format("%s: %d * %d = %d | suma = %d%n",
                                                name, first, nums[i], prod, total);
                                        gui.append(line);
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

                String fin = String.format("%s: perechi = %d, suma produselor = %d%n",
                        name, pairs, total);
                gui.append(fin);
        }
}
