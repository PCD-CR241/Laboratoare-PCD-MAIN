package lab5;

import java.util.concurrent.*;

public class Rihter implements Runnable {
    private final int id;

    public Rihter(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (Main.loturi.getAndIncrement() < Main.TOTAL_LOTURI) {
                for (int i = 0; i < 2; i++) {
                    int numar = ThreadLocalRandom.current().nextInt(1, 51) * 2 - 1;

                    while (!Main.depozit.offer(numar, 100, TimeUnit.MILLISECONDS))
                        Main.mesaj("Producator " + id + ": depozitul este PLIN.");

                    int total = Main.produse.incrementAndGet();

                    Main.mesaj("Producator " + id + " a produs " + numar +
                            " | Total: " + total);
                }

                Thread.sleep(100);
            }

            Main.mesaj("Producator " + id + " a terminat.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}