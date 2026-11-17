package lab5;

import java.util.concurrent.*;

public class Taran implements Runnable {
    private final int id;

    public Taran(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= Main.OBIECTE; i++) {
                Integer numar;

                while ((numar = Main.depozit.poll(100, TimeUnit.MILLISECONDS)) == null)
                    Main.mesaj("Consumator " + id + ": depozitul este GOL.");

                int total = Main.consumate.incrementAndGet();

                Main.mesaj("Consumator " + id + " a consumat " + numar +
                        " | Proprii: " + i + "/5 | Total: " + total);

                Thread.sleep(150);
            }

            Main.mesaj("Consumator " + id + " a primit 5 obiecte.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}