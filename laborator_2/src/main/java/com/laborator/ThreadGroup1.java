package com.laborator;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class ThreadGroup1 extends Application {

    public void start(Stage stage) {

        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(root, 1200, 800);

        stage.setTitle("ThreadGroup");
        stage.setScene(scene);
        stage.show();

    }

    static Thread newThread(ThreadGroup tg, String name, int priority) {

        Thread t = new Thread(tg, () -> {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
            }
        }, name);

        t.setDaemon(true);
        t.setPriority(priority);
        t.start();
        return t;

    }

    public static void main(String[] args) {

        ThreadGroup main = Thread.currentThread().getThreadGroup();

        ThreadGroup g3 = new ThreadGroup(main, "G3");
        g3.setMaxPriority(5);

        ThreadGroup g2 = new ThreadGroup(main, "G2");
        g2.setMaxPriority(8);

        ThreadGroup g4 = new ThreadGroup(g2, "G4");
        g4.setMaxPriority(8);

        ThreadGroup g1 = new ThreadGroup(g4, "G1");
        g1.setMaxPriority(8);

        newThread(main, "Th1", 3);
        newThread(main, "Th2", 6);

        newThread(g2, "ThA", 1);

        newThread(g3, "Th1", 4);
        newThread(g3, "Th2", 3);
        newThread(g3, "Th3", 5);

        newThread(g1, "Tha", 1);
        newThread(g1, "Thb", 3);
        newThread(g1, "Thc", 8);
        newThread(g1, "Thd", 3);

        System.out.println("=== Full tree ===");
        main.list();

        launch(args);
    }

}