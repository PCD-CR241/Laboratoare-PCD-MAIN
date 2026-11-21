package com.laborator;

/*
Bargan Ilie: Th1 si Th2. Sincronizare cu interrupt si interrupted
*/
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{

    static int numbers[] = new int[100];
    static SumStart fromStart = new SumStart("Th1");
    static SumFinish fromFinish = new SumFinish("Th2");

    public void start(Stage stage){

        BorderPane bdRoot = new BorderPane();
        Scene scene = new Scene(bdRoot, 1200, 800);

        stage.setTitle("Sincronizarea folosind metodele clasei Thread");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {

        for(int i = 0; i < 100; i++){

            numbers[i] = (int)(Math.random() * 100);
        }

        System.out.println("Display Array: ");

        for(int i = 0; i < 100; i++){

            System.out.print(i + ": " + numbers[i] + ", ");
        }

        System.out.println();

        fromStart.start();
        fromFinish.start();


        launch(args);
    }

    static class SumStart extends Thread {

        int i = 0;
        int idx1, idx2, res;
        boolean wasInterrupted = false;

        public SumStart(String name) {

            super(name);
        }

        @Override
        public void run() {

            System.out.println(fromStart.getName() + " a startat");

            while (i + 1 <= numbers.length) {

                if (numbers[i] % 2 == 0) {

                    idx1 = i;
                    i++;

                    while (i + 1 < numbers.length && numbers[i] % 2 != 0) {

                        i++;

                    }

                    if(i > 99) break;

                    idx2 = i;

                    res = idx1 + idx2;
                    System.out.println(fromStart.getName() +
                            ": " + idx1 + " + " + idx2 + " = " + res);

                    try {

                        Thread.sleep(1);

                    } catch (InterruptedException e) {
                        
                        wasInterrupted = true;
                    }

                }
                i++;
                
            }

            fromFinish.interrupt();

            try{

                boolean otherInterrupted = Thread.interrupted() || wasInterrupted;

                while(!otherInterrupted){

                    SumFinish.sleep(2000);
                }


            }catch(InterruptedException e){

            }



            System.out.println("1. Surname: ");
        }
    }

    static class SumFinish extends Thread {

        int i = numbers.length - 1;
        int idx1, idx2, res, step;
        boolean wasInterrupted = false;

        public SumFinish(String name) {

            super(name);
        }

        @Override
        public void run() {

            System.out.println(fromFinish.getName() + " a startat");

            while (i >= 0) {

                if (numbers[i] % 2 == 0) {

                    idx1 = i;
                    i--;
    

                    while (i >= 0 && numbers[i] % 2 != 0) {

                        i--;

                    }

                    if(i < 0) break;

                    idx2 = i;

                    res = idx1 + idx2;
                    System.out.println(fromFinish.getName() +
                            ": " + idx1 + " + " + idx2 + " = " + res);

                    try {

                        Thread.sleep(100);


                    } catch (InterruptedException e) {
                        
                        wasInterrupted = true;
                    }

                }
                i--;

            }

            fromStart.interrupt();

            try {

                boolean otherInterrupted = Thread.interrupted() || wasInterrupted;

                while (!otherInterrupted) {

                    SumStart.sleep(2000);
                }

            } catch (InterruptedException e) {
            }

            System.out.println("2. Name: ");
        }
    }
}

