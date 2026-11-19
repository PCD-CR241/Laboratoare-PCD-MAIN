package com.laborator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * Laboratorul 3, varianta 2 - partea Bargan Ilie (firele Th1 si Th2).
 *
 * Th1: suma pozitiilor numerelor pare doua cate doua, de la primul element.
 * Th2: suma pozitiilor numerelor pare doua cate doua, de la ultimul element.
 * Dupa calcule: Th2 afiseaza Numele, apoi Th1 afiseaza Prenumele
 * (cate o litera la 100 ms).
 *
 * Metode de sincronizare (numai metode ale clasei Thread):
 *  1) interrupt() + sleep(): Th1, cand termina calculul, il intrerupe pe Th2,
 *     care asteapta acest semnal adormit in sleep();
 *  2) join(): Th1 asteapta cu th2.join() ca Th2 sa termine afisarea Numelui
 *     si abia apoi afiseaza Prenumele.
 */
public class Syncron extends Application {

    static final int SIZE = 100;
    static final String NUME = "Bargan";
    static final String PRENUME = "Ilie";

    static int[] a = new int[SIZE];

    static Th1 th1;
    static Th2 th2;

    // elemente GUI folosite de fire (actualizate prin Platform.runLater)
    static final ObservableList<String> log = FXCollections.observableArrayList();
    static final Button[] cells = new Button[SIZE];
    static Label lbText;

    // ---------------- Firul 1 ----------------
    static class Th1 extends Thread {

        Th1() {
            super("Th1");
        }

        @Override
        public void run() {
            log("Th1: a pornit (de la primul element)");

            sumEven(SIZE - 1, 0, 1, "th1");
            log("Th1: a terminat calculul -> th2.interrupt()");

            // sincronizarea 1: semnalul pentru Th2
            th2.interrupt();

            // sincronizarea 2: asteptam ca Th2 sa afiseze Numele
            try {
                th2.join();
            } catch (InterruptedException e) {
                return;
            }

            log("Th1: Th2 s-a terminat (join), afisez Prenumele");
            printLetters(" " + PRENUME);
            log("Th1: s-a terminat");
            Platform.runLater(Syncron::finished);
        }
    }

    // ---------------- Firul 2 ----------------
    static class Th2 extends Thread {

        Th2() {
            super("Th2");
        }

        @Override
        public void run() {
            log("Th2: a pornit (de la ultimul element)");

            // semnal = true daca interrupt() de la Th1 a venit deja in timpul calculului
            boolean semnal = sumEven(0, SIZE - 1, -1, "th2");

            // sincronizarea 1: Th2 doarme pana il intrerupe Th1
            if (!semnal) {
                log("Th2: a terminat calculul, asteapta semnalul lui Th1");
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    semnal = true;
                }
            }

            log("Th2: a primit interrupt() de la Th1, afisez Numele");
            printLetters(NUME);
            log("Th2: s-a terminat");
        }
    }

    static boolean sumEven(int max_min, int index, int step, String cls) {

        String name = Thread.currentThread().getName();
        boolean interrupted = false;
        int num1 = 0, num2 = 0;

        try {

            while (index != max_min + step) {

                if (a[index] % 2 == 0) {

                    num1 = index;
                    index += step;

                    while (a[index] % 2 != 0) {

                        index += step;
                    }

                    num2 = index;
                    int res = num1 + num2;

                    log(name + ": " + num1 + " + " + num2 + " = " + res);
                    mark(num1, num2, cls);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                }

                index += step;
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            log(name + ": pozitia " + num1 + " ramane fara pereche (tabloul nu are mai multe elemente)");
        }

        return interrupted;
    }

    static void log(String s) {
        System.out.println(s);
        Platform.runLater(() -> log.add(s));
    }

    static void mark(int p1, int p2, String cls) {
        Platform.runLater(() -> {
            cells[p1].getStyleClass().add(cls);
            cells[p2].getStyleClass().add(cls);
        });
    }

    // afiseaza textul litera cu litera, cu interval de 100 ms
    static void printLetters(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
            System.out.print(c);
            Platform.runLater(() -> lbText.setText(lbText.getText() + c));
        }
        System.out.println();
    }

    // ---------------- GUI ----------------
    static Button btnStart;
    static Label lbState1, lbState2;

    static void generate() {
        for (int i = 0; i < SIZE; i++) {
            a[i] = (int) (Math.random() * 100);
            cells[i].setText(i + ": " + a[i]);
            cells[i].getStyleClass().removeAll("even", "odd", "th1", "th2");
            cells[i].getStyleClass().add(a[i] % 2 == 0 ? "even" : "odd");
        }
    }

    static void startThreads() {
        btnStart.setDisable(true);
        log.clear();
        lbText.setText("");
        generate();

        th1 = new Th1();
        th2 = new Th2();
        th1.setDaemon(true);
        th2.setDaemon(true);
        th1.start();
        th2.start();
    }

    static void finished() {
        btnStart.setDisable(false);
    }

    static String state(Thread t) {
        if (t == null) {
            return "nu a fost creat";
        }
        return t.getState() + (t.isAlive() ? " (isAlive = true)" : " (isAlive = false)");
    }

    @Override
    public void start(Stage stage) {

        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 1150, 700);

        var css = getClass().getResource("style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        SplitPane sp = new SplitPane();
        AnchorPane.setTopAnchor(sp, 20.0);
        AnchorPane.setLeftAnchor(sp, 20.0);
        AnchorPane.setRightAnchor(sp, 20.0);
        AnchorPane.setBottomAnchor(sp, 20.0);
        root.getChildren().add(sp);

        // partea stanga: tabloul (pozitie: valoare)
        GridPane gd = new GridPane();
        gd.setHgap(2.0);
        gd.setVgap(2.0);
        gd.getStyleClass().add("number-grid");
        for (int k = 0; k < 10; k++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            gd.getColumnConstraints().add(cc);
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            gd.getRowConstraints().add(rc);
        }
        for (int i = 0; i < SIZE; i++) {
            cells[i] = new Button();
            cells[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gd.add(cells[i], i % 10, i / 10);
        }

        Label lb1 = new Label("Tabloul (pozitie: valoare). Verde - par, albastru - perechi Th1, portocaliu - perechi Th2");
        AnchorPane an1 = new AnchorPane();
        AnchorPane.setTopAnchor(lb1, 2.0);
        AnchorPane.setLeftAnchor(lb1, 10.0);
        AnchorPane.setTopAnchor(gd, 25.0);
        AnchorPane.setRightAnchor(gd, 10.0);
        AnchorPane.setBottomAnchor(gd, 10.0);
        AnchorPane.setLeftAnchor(gd, 10.0);
        an1.getChildren().addAll(lb1, gd);

        // partea dreapta: Th1 si Th2 (Bargan Ilie)
        Label lb2 = new Label("Firele Th1 - Th2. Bargan Ilie");

        btnStart = new Button("Porneste firele");
        btnStart.getStyleClass().add("start-btn");
        btnStart.setOnAction(e -> startThreads());

        lbState1 = new Label();
        lbState2 = new Label();
        lbState1.getStyleClass().add("state1");
        lbState2.getStyleClass().add("state2");

        ListView<String> view = new ListView<>(log);
        view.getStyleClass().add("log-list");
        view.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("unu", "doi");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    getStyleClass().add(item.startsWith("Th1") ? "unu" : "doi");
                }
            }
        });
        VBox.setVgrow(view, Priority.ALWAYS);
        // derulare automata la ultimul mesaj
        log.addListener((javafx.collections.ListChangeListener<String>) c -> {
            Platform.runLater(() -> {
                if (!log.isEmpty()) {
                    view.scrollTo(log.size() - 1);
                }
            });
        });

        Label lbTextTitle = new Label("Th2 - Numele, Th1 - Prenumele (o litera la 100 ms):");
        lbText = new Label("");
        lbText.getStyleClass().add("name-text");
        lbText.setMaxWidth(Double.MAX_VALUE);

        HBox top = new HBox(15, btnStart, new VBox(4, lbState1, lbState2));

        VBox right = new VBox(8, lb2, top, view, lbTextTitle, lbText);
        AnchorPane an2 = new AnchorPane();
        AnchorPane.setTopAnchor(right, 2.0);
        AnchorPane.setRightAnchor(right, 10.0);
        AnchorPane.setBottomAnchor(right, 10.0);
        AnchorPane.setLeftAnchor(right, 10.0);
        an2.getChildren().add(right);

        sp.getItems().addAll(an1, an2);
        sp.setDividerPosition(0, 0.55);

        generate();

        // starea firelor (getState / isAlive), actualizata la 100 ms
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            lbState1.setText("Th1: " + state(th1));
            lbState2.setText("Th2: " + state(th2));
        }));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();

        stage.setTitle("Laborator 3 - Sincronizarea firelor (Th1, Th2)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
