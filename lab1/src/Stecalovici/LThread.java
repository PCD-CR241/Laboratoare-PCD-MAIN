package Stecalovici;


public class LThread implements Runnable {

        public static final int SIZE = 100;

        private final int[] vector;

        private final int pozInceput;
        private final int directie;
        private final GuiFrame gui;

        private long sumaProduse = 0;
        private int nrPerechi = 0;

        public LThread(int start, int step, GuiFrame gui, int[] tablou) {
                this.pozInceput = start;
                this.directie = step;
                this.gui = gui;
                this.vector = tablou;
        }


        @Override
        public void run() {
                String numeFir = Thread.currentThread().getName();
                int contorImpare = 0;   // cate numere impare au fost intalnite
                int valoareAnterioara = 0; // impar ramas fara pereche

                int pozitie = pozInceput;
                int ramase = (directie > 0) ? SIZE - pozInceput : pozInceput + 1;

                while (ramase-- > 0) {
                        int curent = vector[pozitie];

                        // impar <=> ultimul bit este 1
                        if ((curent & 1) == 1) {
                                contorImpare++;
                                if (contorImpare % 2 == 0) {
                                        long produs = (long) valoareAnterioara * curent;
                                        sumaProduse += produs;
                                        nrPerechi++;
                                        gui.append(String.format("%s: %d * %d = %d | suma = %d%n",
                                                numeFir, valoareAnterioara, curent, produs, sumaProduse));
                                } else {
                                        valoareAnterioara = curent;
                                }
                        }

                        pozitie += directie;

                        try {
                                Thread.sleep(20);
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                        }
                }

                gui.append(String.format("%s: perechi = %d, suma produselor = %d%n",
                        numeFir, nrPerechi, sumaProduse));
        }
}
