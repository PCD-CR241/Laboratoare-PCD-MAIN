package com.laborator;
public class Varianta21 {

    static class MyThread extends Thread {

        public MyThread(ThreadGroup group, String name, int priority) {
            super(group, name);
            setPriority(priority);
        }

        @Override
        public void run() {

            System.out.println(">>> Ruleaza: " + getName()
                    + " | Grup: " + getThreadGroup().getName()
                    + " | Prioritate: " + getPriority());

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();

        // G2
        ThreadGroup g2 = new ThreadGroup(mainGroup, "G2");

        // G4
        ThreadGroup g4 = new ThreadGroup(g2, "G4");

        // G1
        ThreadGroup g1 = new ThreadGroup(g4, "G1");

        // Fire din G1
        Thread tha = new MyThread(g1, "Tha", 1);
        Thread thb = new MyThread(g1, "Thb", 3);
        Thread thc = new MyThread(g1, "Thc", 8);
        Thread thd = new MyThread(g1, "Thd", 3);

        // ThA direct in G2
        Thread thA = new MyThread(g2, "ThA", 1);

        // G3 direct in Main
        ThreadGroup g3 = new ThreadGroup(mainGroup, "G3");

        // Fire din G3
        Thread th1_g3 = new MyThread(g3, "Th1", 4);
        Thread th2_g3 = new MyThread(g3, "Th2", 3);
        Thread th3_g3 = new MyThread(g3, "Th3", 5);

        // Fire directe in Main
        Thread th1_main = new MyThread(mainGroup, "Th1", 3);
        Thread th2_main = new MyThread(mainGroup, "Th2", 6);

        System.out.println("=== Pornesc toate firele ===");

        tha.start();
        thb.start();
        thc.start();
        thd.start();

        thA.start();

        th1_g3.start();
        th2_g3.start();
        th3_g3.start();

        th1_main.start();
        th2_main.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== Structura completa (list()) ===");
        mainGroup.list();

        System.out.println("\n=== Enumerare detaliata a tuturor firelor ===");
        enumerateAll(mainGroup);

        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== Toate firele s-au terminat ===");
    }

    // Enumerare recursiva
    public static void enumerateAll(ThreadGroup group) {

        System.out.println("\nGrup: " + group.getName()
                + " | maxPriority = "
                + group.getMaxPriority());

        Thread[] threads =
                new Thread[group.activeCount() + 10];

        int count = group.enumerate(threads, false);

        for (int i = 0; i < count; i++) {

            if (threads[i] != null) {

                System.out.printf(
                        "   Thread: %-6s | Grup: %-4s | Prioritate: %d%n",
                        threads[i].getName(),
                        threads[i].getThreadGroup().getName(),
                        threads[i].getPriority());
            }
        }

        ThreadGroup[] groups =
                new ThreadGroup[group.activeGroupCount() + 10];

        int groupCount = group.enumerate(groups, false);

        for (int i = 0; i < groupCount; i++) {

            if (groups[i] != null) {
                enumerateAll(groups[i]);
            }
        }
    }
}