package lab5;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static final int PRODUCATORI = 3;
    static final int CONSUMATORI = 3;
    static final int OBIECTE = 5;
    static final int CAPACITATE = 2;
    static final int TOTAL_LOTURI = 8;

    static BlockingQueue<Integer> depozit = new ArrayBlockingQueue<>(CAPACITATE);
    static AtomicInteger loturi = new AtomicInteger();
    static AtomicInteger produse = new AtomicInteger();
    static AtomicInteger consumate = new AtomicInteger();

    static synchronized void mesaj(String text) {
        System.out.println(text);
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(6);

        for (int i = 1; i <= PRODUCATORI; i++)
            executor.execute(new Rihter(i));

        for (int i = 1; i <= CONSUMATORI; i++)
            executor.execute(new Taran(i));

        executor.shutdown();

        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            System.out.println("Timpul de executie a fost depasit.");
            return;
        }

        System.out.println("\n===== RAPORT FINAL =====");
        System.out.println("Produse: " + produse.get());
        System.out.println("Consumate: " + consumate.get());
        System.out.println("Ramase in depozit: " + depozit.size());
        System.out.println("=======================");
    }
}