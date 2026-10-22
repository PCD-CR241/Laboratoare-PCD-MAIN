public class Pislari implements Runnable {

    private int[] mas;   
    private int start;   
    private int step;    

    public Pislari(int[] mas, int start, int step) {
        this.mas = mas;
        this.start = start;
        this.step = step;
    }

    @Override
    public void run() {
        String nume = Thread.currentThread().getName();
        int[] poz = new int[4];   
        int k = 0;                

        for (int i = start; i >= 0 && i < mas.length; i += step) {
            if (i % 2 == 0) {                 
                poz[k] = i;
                k++;

                if (k == 4) {                 
                    int p1 = mas[poz[0]] * mas[poz[1]];
                    int p2 = mas[poz[2]] * mas[poz[3]];
                    int dif = p1 - p2;

                    System.out.println(nume + ": (" + mas[poz[0]] + "*" + mas[poz[1]] + ") - ("
                            + mas[poz[2]] + "*" + mas[poz[3]] + ") = "
                            + p1 + " - " + p2 + " = " + dif
                            + "   poz: " + poz[0] + "," + poz[1] + "," + poz[2] + "," + poz[3]);

                    k = 0;                    
                    try {
                        Thread.sleep(20);     
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }
    }
}