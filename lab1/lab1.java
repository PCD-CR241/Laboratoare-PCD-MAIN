import java.util.Random;

class DiferentaProduse implements Runnable {
    private final int[] mas;
    private final int from, to, step;

    public DiferentaProduse(int[] mas, int from, int to, int step) {
        this.mas = mas;
        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public void run() {
        String nume = Thread.currentThread().getName();
        int[] poz = new int[4]; 
        int k = 0;

        for (int i = from; i != to + step; i += step) {
            if (i % 2 != 0) {              
                poz[k++] = i;
                if (k == 4) {
                    int p1 = mas[poz[0]] * mas[poz[1]];   
                    int p2 = mas[poz[2]] * mas[poz[3]];   
                    int dif = p1 - p2;
                    System.out.println(nume + ": (" + mas[poz[0]] + "*" + mas[poz[1]] + ") - ("
                            + mas[poz[2]] + "*" + mas[poz[3]] + ") = "
                            + p1 + " - " + p2 + " = " + dif
                            + "   [poz: " + poz[0] + "," + poz[1] + "," + poz[2] + "," + poz[3] + "]");
                    k = 0;
                }
            }
        }
    }
}

public class lab1 {
    public static void main(String[] args) throws InterruptedException {
        int[] mas = new int[100];
        Random rnd = new Random();

        for (int i = 0; i < mas.length; i++) {
            mas[i] = rnd.nextInt(100) + 1;
            System.out.print(mas[i] + " ");
        }
        System.out.println("\n");

        Thread th1 = new Thread(new DiferentaProduse(mas, 0, 99, 1), "Th1");
        Thread th2 = new Thread(new DiferentaProduse(mas, 99, 0, -1), "Th2");

        th1.start();
        th2.start();

        th1.join();
        th2.join();

        String info = "\nLucrarea a fost efectuata de: Rihter Marius-Damian, Taran Daniel , grupa CR-241\n";
        for (char c : info.toCharArray()) {
            System.out.print(c);
            Thread.sleep(100);
        }
    }
}