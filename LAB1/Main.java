import java.util.Random;

// Varianta 7: Sumele produselor numerelor pare doua cate doua
// Versiunea pentru CONSOLA. Interfata grafica se porneste din Fereastra.java

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // 1. Generam tabloul comun mas[] cu 100 de numere intre 1 si 100
        int[] mas = new int[100];
        Random rnd = new Random();
        for (int i = 0; i < mas.length; i++) {
            mas[i] = rnd.nextInt(100) + 1;
        }

        System.out.println("Tabloul generat:");
        for (int i = 0; i < mas.length; i++) {
            System.out.print(mas[i] + " ");
        }
        System.out.println("\n");

        // "Ecranul" consolei: tot ce primeste, scrie cu System.out
        Afisaj consola = new Afisaj() {
            public void afiseaza(String numeFir, String text) {
                System.out.println(numeFir + ": " + text);
            }
            public void sumaFinala(String numeFir, long suma) {
                System.out.println(numeFir + " a terminat. Suma finala a produselor: " + suma);
            }
        };

        // 2. Fiecare student isi creeaza cele doua fire
        Student1 s1 = new Student1(mas, consola);   // Manoli Bogdan  - extends Thread
        Student2 s2 = new Student2(mas, consola);   // Lavreca Artiom - implements Runnable

        // 3. Pornim toate cele 4 fire
        s1.pornesteFirele();
        s2.pornesteFirele();

        // 4. Asteptam sa se termine toate firele
        s1.asteaptaFirele();
        s2.asteaptaFirele();

        // 5. Abia acum firul principal afiseaza autorii, litera cu litera
        System.out.println();
        afiseazaText("Lucrarea a fost realizata de: Manoli Bogdan si Lavreca Artiom");
    }

    static void afiseazaText(String text) throws InterruptedException {
        for (int i = 0; i < text.length(); i++) {
            System.out.print(text.charAt(i));
            Thread.sleep(100);   // pauza de 100 ms intre litere
        }
        System.out.println();
    }
}
