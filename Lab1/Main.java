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

        Thread th1 = new Thread(new Pislari(mas, 0, 1), "Th1 Pislari");
        Thread th2 = new Thread(new Pislari(mas, mas.length - 1, -1), "Th2 Pislari");

        // 3. Firele colegei - le activezi cand primesti clasa ei
        // Thread th3 = new Thread(new NumeColega(mas, ...), "Th1 Colega");
        // Thread th4 = new Thread(new NumeColega(mas, ...), "Th2 Colega");

        
        th1.start();
        th2.start();
        // th3.start();
        // th4.start();

        th1.join();
        th2.join();
        // th3.join();
        // th4.join();

        System.out.println();
        String info = "Lucrarea nr. 1 a fost realizata de: Pislari Andreea si <Nume Prenume colega>, grupa <Grupa>";
        for (int i = 0; i < info.length(); i++) {
            System.out.print(info.charAt(i));
            Thread.sleep(100);
        }
        System.out.println();
    }
}
