public class ThreadGroup1 {

    static class Thread1 extends Thread {

        public Thread1(ThreadGroup group, String name) {
            super(group, name);//lathread
            }

        @Override
        public void run() {
            System.out.println(
                "Started: " + getName()
                + " | Priority: " + getPriority()
                + " | Group: " + getThreadGroup().getName());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Finished: " + getName());}}

    public static void main(String[] args) {

        ThreadGroup main = Thread.currentThread().getThreadGroup();

        ThreadGroup O = new ThreadGroup("O");
        ThreadGroup Z = new ThreadGroup(O, "Z");
        Z.setMaxPriority(1);
        Thread1 a = new Thread1(Z, "a");
        Z.setMaxPriority(3);
        Thread1 b = new Thread1(Z, "b");
        Thread1 c = new Thread1(Z, "c");
        Z.setMaxPriority(7);
        Thread1 d = new Thread1(Z, "d");
        d.setPriority(7);

        ThreadGroup V = new ThreadGroup("V");
        Thread1 A = new Thread1(V, "A");
        V.setMaxPriority(3);

        ThreadGroup F = new ThreadGroup("F");
        Thread1 t1 = new Thread1(F, "1");
        F.setMaxPriority(3);
        Thread1 t2 = new Thread1(F, "2");
        F.setMaxPriority(9);
        Thread1 t3 = new Thread1(F, "3");
        t3.setPriority(9);

        main.setMaxPriority(3);
        Thread1 main1 = new Thread1(main, "1");
        main.setMaxPriority(5);
        Thread1 main2 = new Thread1(main, "2");

        //start
        a.start();
        b.start();
        c.start();
        d.start();
        A.start();
        t1.start();
        t2.start();
        t3.start();
        main1.start();
        main2.start();

        //timpinainte
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nThread group hierarchy:");
        main.list();
        System.out.println("\nAll threads finished.");
    }
}
