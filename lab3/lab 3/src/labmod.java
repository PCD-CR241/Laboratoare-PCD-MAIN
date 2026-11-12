
import javax.swing.*;

public class Lab3PCD {

    static int size = 100;
    static int counter = 0;
    static int[] b = new int[size];
    static int[] a;

    static Straight first;
    static Reverse second;
    static ReverseInterval third;
    static StraightInterval fourth;
    static class Straight extends Thread {
        Lucky mimir;
        String color;
        Straight(Lucky world, String pain) {
            mimir = world;
            color = pain;
        }

        @Override
        public void run() {
            mimir.write("Starting Thread 1 - Straight", color);
            for (int i = 0; i + 1 < counter; i += 2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int result = a[i] + a[i + 1];
                mimir.write(
                    "Thread 1: " + a[i] + " + " +
                    a[i + 1] + " = " + result,
                    color
                );
            }

            if (counter % 2 != 0) {
                mimir.write("Numar ramas: " + a[counter - 1], color);
            }

            while (fourth.isAlive()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mimir.write("1: Scutaru", color);
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
            mimir.write("Starting Thread 2 - Reverse", color);
            for (int i = counter - 1; i - 1 >= 0; i -= 2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int result = a[i] + a[i - 1];
                mimir.write(
                    "Thread 2: " + a[i] + " + " +
                    a[i - 1] + " = " + result,
                    color
                );
            }

            if (counter % 2 != 0) {
                mimir.write("Numar ramas: " + a[0], color);
            }
            mimir.write("2: Laurentiu", color);
        }
    }

    public static class ReverseInterval extends Thread {
        Lucky mimir;
        String color;
        ReverseInterval(Lucky world, String pain) {
            mimir = world;
            color = pain;
        }
        @Override
        public void run() {
            mimir.write("Starting Thread 3 - ReverseInterval", color);
            for (int i = 890; i >= 123; i--) {
                mimir.write(String.valueOf(i), color);
            }
            while (first.isAlive()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mimir.write("3: P C D", color);
        }
    }

    public static class StraightInterval extends Thread {

        Lucky mimir;
        String color;
        StraightInterval(Lucky world, String pain) {
            mimir = world;
            color = pain;
        }
        @Override
        public void run() {
            mimir.write("Starting Thread 4 - StraightInterval", color);
            for (int i = 234; i <= 987; i++) {
                mimir.write(String.valueOf(i), color);
            }
            while (second.isAlive()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mimir.write("4: CR-241", color);
        }
    }

    public static void main(String[] args) {
        final Lucky[] window = new Lucky[1];
        try {
            SwingUtilities.invokeAndWait(() -> {
                window[0] = new Lucky("Lab3 PCD");
                window[0].setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Lucky Window = window[0];

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
        third = new ReverseInterval(Window, "#008000");
        fourth = new StraightInterval(Window, "#990099");

        first.start();
        second.start();
        third.start();
        fourth.start();

        try {
            first.join();
            second.join();
            third.join();
            fourth.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Window.write("Program finished!", "#000000");
    }
}
