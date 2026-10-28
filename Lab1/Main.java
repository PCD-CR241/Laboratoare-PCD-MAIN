public class Main {
public static void main(String[] args) throws InterruptedException {

        int[] mas = new int[100];
        for (int i = 0; i < mas.length; i++) {
            mas[i] = (int) (Math.random() * 100) + 1;
            System.out.print("[" + i + "]=" + mas[i] + "  ");
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();

        Pislari th1 = new Pislari("Th1 Pislari", mas, false);
        Pislari th2 = new Pislari("Th2 Pislari", mas, true);

        Thread th3 = new Thread(new Volcovschi(mas, true), "Th1 Volcovschi");
        Thread th4 = new Thread(new Volcovschi(mas, false), "Th2 Volcovschi");

        th1.start();
        th2.start();
        th3.start();
        th4.start();

        th1.join();
        th2.join();
        th3.join();
        th4.join();

        System.out.println();
        String info = "Lucrarea nr. 1 a fost realizata de: Pislari Andreea si Volcovschi Laura, grupa CR-241";
        for (int i = 0; i < info.length(); i++) {
            System.out.print(info.charAt(i));
            Thread.sleep(100);
        }
        System.out.println();
    }
}