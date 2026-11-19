public class Taran {
    static int[] numere = new int[100];

    static void afiseaza(String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            Thread.sleep(100);
        }
        System.out.println();
    }

    static void asteaptaSiAfiseaza(Thread fir, String text) {
        try {
            while (fir.getPriority() != Thread.MAX_PRIORITY) {
                Thread.sleep(10);
            }
            System.out.print(fir.getName() + ": ");
            afiseaza(text);
        } catch (InterruptedException e) {
            fir.interrupt();
        }
    }

    static class Th1 extends Thread {
        Th1() {
            super("Th1");
        }

        @Override
        public void run() {
            for (int i = 1; i + 6 < numere.length; i += 8) {
                int p1 = numere[i] * numere[i + 2];
                int p2 = numere[i + 4] * numere[i + 6];

                System.out.println(getName() + ": (" +
                        numere[i] + " * " + numere[i + 2] +
                        ") - (" + numere[i + 4] + " * " +
                        numere[i + 6] + ") = " + (p1 - p2));
            }

            asteaptaSiAfiseaza(this, "Daniel");
        }
    }

    static class Th2 extends Thread {
        Th2() {
            super("Th2");
        }

        @Override
        public void run() {
            for (int i = numere.length - 1; i - 6 >= 0; i -= 8) {
                int p1 = numere[i] * numere[i - 2];
                int p2 = numere[i - 4] * numere[i - 6];

                System.out.println(getName() + ": (" +
                        numere[i] + " * " + numere[i - 2] +
                        ") - (" + numere[i - 4] + " * " +
                        numere[i - 6] + ") = " + (p1 - p2));
            }

            asteaptaSiAfiseaza(this, "Taran");
        }
    }
}
