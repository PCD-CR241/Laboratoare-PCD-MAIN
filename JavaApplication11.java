/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication11;

/*
 * Lucrare de laborator nr. 2
*  1.Problema penrtu nivelul mediu:
*  Creați o structură, conform formulei date în tabelul 1, conform variantei.
*  Enumerați toate firele de execuţie din grupa principală şi subgrupele ce le conţine.
*  Afişați informaţia despre numele firului de execuţie, numele grupei din care face parte, și prioritatea sa.
*  Prioritatea fiecărui Thread este indicată în paranteze () pentru fieacare fir de execuție (vezi tabelul 1).
*  Nu suprascriți meroda run() din clasa Thread.
*  Varianta 3:Formula structurii, compusă din fire şi grupuri de fire:
*  Main{GN{GH{Tha(4), Thb(3), Thc(6), Thd(3)}, ThA(3)},GM{Th1(2), Th2(3), Th3(3)},Th1(8), Th2(3)}
 */


import java.util.concurrent.CountDownLatch;

public class JavaApplication11 {

    public static void main(String[] args) {

        // Crearea grupei principale Main
        ThreadGroup Main = new ThreadGroup("Main");

        // Crearea grupurilor si subgrupurilor
        ThreadGroup GN = new ThreadGroup(Main, "GN");
        ThreadGroup GH = new ThreadGroup(GN, "GH");
        ThreadGroup GM = new ThreadGroup(Main, "GM");

        // Firele asteapta pana cand programul afiseaza structura
        CountDownLatch asteptare = new CountDownLatch(1);

        // Grupul GH
        Thread Tha = creareFir(GH, "Tha", 4, asteptare);
        Thread Thb = creareFir(GH, "Thb", 3, asteptare);
        Thread Thc = creareFir(GH, "Thc", 6, asteptare);
        Thread Thd = creareFir(GH, "Thd", 3, asteptare);

        // Grupul GN
        Thread ThA = creareFir(GN, "ThA", 3, asteptare);

        // Grupul GM
        Thread Th1GM = creareFir(GM, "Th1", 2, asteptare);
        Thread Th2GM = creareFir(GM, "Th2", 3, asteptare);
        Thread Th3GM = creareFir(GM, "Th3", 3, asteptare);

        // Firele direct in grupa principala Main
        Thread Th1Main = creareFir(Main, "Th1", 8, asteptare);
        Thread Th2Main = creareFir(Main, "Th2", 3, asteptare);
        
        // Firului demon
        Thread Demon = creareFir(Main, "Demon", 3, asteptare);
        Demon.setDaemon(true);

        // Pornirea tuturor celor 10 fire
        Tha.start();
        Thb.start();
        Thc.start();
        Thd.start();
        ThA.start();

        Th1GM.start();
        Th2GM.start();
        Th3GM.start();

        Th1Main.start();
        Th2Main.start();
        
        Demon.start();

        // Afisarea structurii complete
        System.out.println("STRUCTURA THREAD-URILOR - VARIANTA 3 Sarcina 1");
        System.out.println("====================================");

        try {
            afisareGrup(Main);

            System.out.println("\n====================================");
            System.out.println("STRUCTURA  CU LISTA:");

            Main.list();

        } finally {
            // Permitem terminarea firelor
            asteptare.countDown();
        }
        

        // Asteptam terminarea tuturor firelor
        try {
            Tha.join();
            Thb.join();
            Thc.join();
            Thd.join();
            ThA.join();

            Th1GM.join();
            Th2GM.join();
            Th3GM.join();

            Th1Main.join();
            Th2Main.join();

            System.out.println("\nToate cele 10 fire au fost executate cu succes.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Executia a fost intrerupta.");
        }
    }

    // Crearea unui fir cu nume, grupa si prioritate
    // Metoda run() nu este suprascrisa.
    public static Thread creareFir(
            ThreadGroup grup,
            String nume,
            int prioritate,
            CountDownLatch asteptare) {

        Thread fir = new Thread(grup, () -> {
            try {
                asteptare.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, nume);

        fir.setPriority(prioritate);

        return fir;
    }

    // Afisarea firelor si a subgrupurilor, recursiv
    public static void afisareGrup(ThreadGroup grup) {

        System.out.println("\nGrupa: " + grup.getName());

        Thread[] fire = new Thread[grup.activeCount() + 10];

        int nrFire = grup.enumerate(fire, false);

        for (int i = 0; i < nrFire; i++) {
            if (fire[i] != null) {
                System.out.println(
                    "  Nume fir: " + fire[i].getName()
                    + " | Grupa: "
                    + fire[i].getThreadGroup().getName()
                    + " | Prioritate: "
                    + fire[i].getPriority()
                    + " | Demon: "
                    + fire[i].isDaemon()
                );
            }
        }

        ThreadGroup[] subgrupe =
                new ThreadGroup[grup.activeGroupCount() + 10];

        int nrGrupe = grup.enumerate(subgrupe, false);

        for (int i = 0; i < nrGrupe; i++) {
            if (subgrupe[i] != null) {
                afisareGrup(subgrupe[i]);
            }
        }
    }
}
