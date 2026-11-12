
public class Main {
    public static void main(String[] args) {
        Depozit depozit = new Depozit(2);

        for (int i = 1; i <= 3; i++) {
            Producator producator = new Producator(depozit, "Producator " + i);
            producator.setDaemon(true);
            producator.start();
        }

        for (int i = 1; i <= 3; i++) {
            Consumator consumator = new Consumator(depozit, "Consumator " + i);
            consumator.start();
        }
    }
}
