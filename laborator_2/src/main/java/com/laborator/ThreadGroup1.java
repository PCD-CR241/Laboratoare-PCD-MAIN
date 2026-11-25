package com.laborator;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ThreadGroup1 extends Application {

    static ThreadGroup mainGroup, g1, g2, g3, g4;

    static {

        mainGroup = Thread.currentThread().getThreadGroup();

        g3 = new ThreadGroup(mainGroup, "G3");

        g2 = new ThreadGroup(mainGroup, "G2");

        g4 = new ThreadGroup(g2, "G4");

        g1 = new ThreadGroup(g4, "G1");

        newThread(mainGroup, "Th1", 3);
        newThread(mainGroup, "Th2", 6);

        newThread(g2, "ThA", 1);

        newThread(g3, "Th1", 4);
        newThread(g3, "Th2", 3);
        newThread(g3, "Th3", 5);

        newThread(g1, "Tha", 1);
        newThread(g1, "Thb", 3);
        newThread(g1, "Thc", 8);
        newThread(g1, "Thd", 3);

    }

    @Override
    public void start(Stage stage) {

        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 1200, 800);

        var css = getClass().getResource("style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        ObservableList<String> items = FXCollections.observableArrayList();
        addGroup(mainGroup, 0, items);

        ListView<String> view = new ListView<>(items);
        view.getStyleClass().add("log-list");

        Label lb = new Label("Arborele ThreadGroup");

        AnchorPane.setTopAnchor(view, 20.0);
        AnchorPane.setRightAnchor(view, 20.0);
        AnchorPane.setBottomAnchor(view, 20.0);
        AnchorPane.setLeftAnchor(view, 20.0);

        AnchorPane.setTopAnchor(lb, 2.0);
        AnchorPane.setLeftAnchor(lb, 40.0);

        root.getChildren().addAll(lb, view);

        stage.setTitle("ThreadGroup");
        stage.setScene(scene);
        stage.show();

    }

   
    static void addGroup(ThreadGroup g, int level, ObservableList<String> items) {

        String indent = "    ".repeat(level);
        items.add(indent + g.getName() + " (maxPriority=" + g.getMaxPriority() + ")");

        ThreadGroup[] groups = new ThreadGroup[g.activeGroupCount() + 4];
        int gc = g.enumerate(groups, false);

        for (int i = 0; i < gc; i++) {
            addGroup(groups[i], level + 1, items);
        }

        Thread[] threads = new Thread[g.activeCount() + 4];
        int tc = g.enumerate(threads, false);

        for (int i = 0; i < tc; i++) {
            Thread t = threads[i];

            items.add(indent + "    " +
                    t.getName() +
                    " (Priority: " + t.getPriority() +
                    ", Daemon: " + t.isDaemon() + ")");
        }
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
        launch(args);
    }

}