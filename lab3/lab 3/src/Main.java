import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class Main {
    static int size = 100;
    static int counter = 0;
    static int[] b = new int[size];
    static int[] a;

    static Straight first;
    static Reverse second;
    static Collar third;
    static Collar fourth;
    static class Straight extends Thread {
        Lucky mimir;
        String color;
        Straight(Lucky world, String pain) {
            mimir = world;
            color = pain;
        }

        @Override
        public void run() {
            mimir.writeln("Starting Thread 1 - Straight", color);
            for (int i = 0; i + 1 < counter; i += 2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int result = a[i] + a[i + 1];
                mimir.writeln(
                        "Thread 1: " + a[i] + " + " +
                                a[i + 1] + " = " + result,
                        color
                );
            }

            if (counter % 2 != 0) {
                mimir.writeln("Numar ramas: " + a[counter - 1], color);
            }

            while (fourth.isAlive()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mimir.writeln("1: Scutaru/Burdic", color);
        }
    }

    static class Reverse extends Thread {
        Lucky mimir;
        String color;
        Reverse(Lucky world, String pain) {
            mimir = world;
            color = pain;
        }
        @Override
        public void run() {
            mimir.writeln("Starting Thread 2 - Reverse", color);
            for (int i = counter - 1; i - 1 >= 0; i -= 2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int result = a[i] + a[i - 1];
                mimir.writeln(
                        "Thread 2: " + a[i] + " + " +
                                a[i - 1] + " = " + result,
                        color
                );
            }

            if (counter % 2 != 0) {
                mimir.writeln("Numar ramas: " + a[0], color);
            }
            mimir.writeln("2: Laurentiu/Stas", color);
        }
    }




    public static void main(String[] args) {
         Lucky Window = new Lucky("Lab3 PCD");
         Window.setVisible(true);


        Window.write("Printing Array:", "#000000");
        for (int i = 0; i < size; i++) {
            b[i] = (int) Math.round(
                    (Math.random() * 100) + 15
            );
            Window.write(String.valueOf(b[i]), "#000000");
            if (b[i] % 2 != 0) {
                counter++;
            }
        }

        a = new int[counter];
        int k = 0;
        for (int i = 0; i < size; i++) {
            if (b[i] % 2 != 0) {
                a[k] = b[i];
                k++;
            }
        }
        Window.write("Vectorul numerelor impare:", "#000000");
        for (int i = 0; i < counter; i++) {
            Window.write(String.valueOf(a[i]), "#000000");
        }
        Window.write("Numarul numerelor impare: " + counter, "#000000");
        first = new Straight(Window, "#d40b0b");
        second = new Reverse(Window, "#0000cc");
        third = new Collar("fir3",234,987,1, Window, "#0000ff");
        fourth = new Collar("fir4",123,890,-1, Window, "#d9b2ff");

        second.setPriority(10);
        first.start();
        second.start();
        third.start();
        fourth.start();


        Window.write("Program finished!", "#000000");
    }
    static class Collar extends Thread{
        int a;
        int b;
        int p;
        String color;
        Lucky mimir;
        Collar(String name,int a,int b,int p,Lucky world,String pain){
            super(name);

            this.a= a<b ? a:b;
            this.b=a>b ? a:b;
            this.p=p;
            mimir=world;
            color = pain;
        }
        @Override
        public void run(){

            for (int i = p<0?b:a;(i<=b)&&(i>=a);i+=p){
                mimir.write(this.getName()+" "+i+" ",color);
            }
            if(p>0){
                while(first.isAlive()){
                    try{
                    first.join();}catch (InterruptedException e){e.printStackTrace();}
                } mimir.writeln("3: P C D",color);
            }else{
                boolean ht=true;
                while(second.isAlive()){
                    this.yield();
                }
                mimir.writeln("4: CR-241",color);

            }


        }

    }
}

