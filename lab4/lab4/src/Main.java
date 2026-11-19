import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Lucky star=new Lucky("lab4");
        star.setVisible(true);
        Store store = new Store(star);
        ThreadGroup producer=new ThreadGroup("Producer");
        Producer p1 = new Producer(producer,store,"#960748");
        p1.setDaemon(true);
        p1.setName("Producator №1");
        Producer p2 = new Producer(producer,store,"#810f74");
        p2.setDaemon(true);
        p2.setName("Producator №2");
        Producer p3 = new Producer(producer,store,"#b8196a");
        p3.setDaemon(true);
        Thread.currentThread().setPriority(10);
        p3.setName("Producator №3");
        Consumer c1 = new Consumer(store,"#fa0219");
        c1.setName("Consumator №1");
        Consumer c2 = new Consumer(store,"#ed233d");
        c2.setName("Consumator №2");

       // System.out.println(c1.getPriority());
        // System.out.println(c2.getPriority());
        p1.start();
        p2.start();
        p3.start();
        c1.start();
        c2.start();
        c1.join();
        c2.join();



        while(c1.isAlive() || c2.isAlive()){   }
        //try{producer.stop();}catch (UnsupportedOperationException e){e.printStackTrace();}
        System.out.println("Toate thread-urile au finalizat");
    }
}

class Store {
    ArrayList<Character> stockList = new ArrayList<Character>();
    Lucky sun;
Store(Lucky smile){
    sun=smile;
}
    public synchronized void get(String str,String clr) {
        while (stockList.size() < 1) {
            try {
                wait();
            } catch (InterruptedException e) {e.printStackTrace(); }
        }

        sun.writeln(str + " a luat din depozit: " +
                stockList.get(stockList.size() - 1),clr);
        stockList.remove(stockList.size() - 1);

        if (stockList.size() != 0) {
            sun.write("Depozitul are " + stockList.size() + " litere -> ",clr);
            for (char litera : stockList) {
                sun.write(litera + " ",clr);
            }
            sun.writeln(" ","#000000");
        } else {
            sun.writeln("Depozitul este gol",clr);
        }
        notifyAll();

    }

    public synchronized void put(String str, char a, char b,String clr) {
        while (stockList.size() > 10) { //11.... da uit
            try {
                wait();
            } catch (InterruptedException e) { }
        }

            if (stockList.size() <= 9) {
                sun.write(str + " a pus in depozit doua consoane: ", clr);
                stockList.add(a);
                sun.write(stockList.get(stockList.size() - 1) + ", ", clr);
                stockList.add(b); //teoretic spoate de taiat aici + add mai jos, va lucra dar avem probabilitate de amestacatura de text si asta nu e frumos
                sun.writeln(stockList.get(stockList.size() - 1) + " ", clr);
                sun.write("Depozitul are " + stockList.size() + " litere -> ", clr);
                for (char litera : stockList) {
                    sun.write(litera + " ", clr);
                }
                sun.writeln(" ", "#ffffff");
                notifyAll();
            } else {
                sun.write(str + " a pus in depozit o consoana: ", clr);
                stockList.add(a);
                sun.writeln(stockList.get(stockList.size() - 1) + "", clr);
                sun.write("Depozitul are " + stockList.size() + " litere -> ", clr);
                for (char litera : stockList) {
                    sun.write(litera + " ", clr);
                }
                sun.writeln(" ", "#ffffff");
                notifyAll();
            }
    }
}

class Producer extends Thread {
    Store s;
    String clr;

    char[] consoane = {
            'B', 'C', 'D', 'F', 'G', 'H',
            'J', 'K', 'L', 'M', 'N', 'P',
            'R', 'S', 'T', 'V', 'Z'
    };

    public Producer(Store s,String sector) {
        this.s = s;
        clr=sector;
    }
    public Producer(ThreadGroup g,Store s,String sector) {
        super(g," ");
        this.s = s;
        clr=sector;
    }

    @Override
    public void run() {

        while(true){
            try{sleep(1);}catch (InterruptedException e){e.printStackTrace();}
            char a = consoane[(int)(Math.random() * consoane.length)];
            char b = consoane[(int)(Math.random() * consoane.length)];
            s.put(getName(), a, b,clr);
        }

    }
}

class Consumer extends Thread {
    Store s;

    String clr;
    public Consumer(Store s,String sector) {
        this.s = s;
        clr=sector;
    }
    @Override
    public void run() {
        char t;
        for (int i = 0; i < 12; i++) {
            try{sleep(1);}catch (InterruptedException e){e.printStackTrace();}
            s.get(getName(),clr);
            //sun.writeln(getName()+" consoana :"+t+" "+i,clr);
        }

        System.out.println(getName() + " a consumat 12 consoane. Thread-ul a finalizat");
    }
}
