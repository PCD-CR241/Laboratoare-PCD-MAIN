package com.laborator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
 * Laboratorul 3, varianta 2.
 *
 * Partea Bargan Ilie (firele Th1 si Th2):
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
 *
 * Partea Turcan Ion (firele Th3 si Th4) - se adauga in startThreads34():
 * Th3: parcurge de la inceput intervalul [120, 690], la final afiseaza disciplina;
 * Th4: parcurge de la sfarsit intervalul [1000, 1567], la final afiseaza grupa.
 * In GUI se folosesc: log34(...) pentru mesaje si printLetters(text, lbText34)
 * pentru textul afisat litera cu litera.
 */
public class Syncron extends Application {

    static final int SIZE = 100;
    static final String NUME = "Bargan";
    static final String PRENUME = "Ilie";

    static int[] a = new int[SIZE];

    // firele Bargan Ilie
    static Th1 th1;
    static Th2 th2;

    // firele Turcan Ion (se creeaza in startThreads34)
    static Thread th3;
    static Thread th4;

    // elemente GUI folosite de fire (actualizate prin Platform.runLater)
    static final ObservableList<String> log = FXCollections.observableArrayList();
    static final ObservableList<String> log34 = FXCollections.observableArrayList();
    static final Button[] cells = new Button[SIZE];
    static Label lbText;
    static Label lbText34;

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
            printLetters(" " + PRENUME, lbText);
            log("Th1: s-a terminat");
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
            printLetters(NUME, lbText);
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

    // ---------------- Firele 3 si 4 (Turcan Ion) ----------------
    static void startThreads34() {
        // TODO Turcan Ion: aici se creeaza si se pornesc th3 si th4, de exemplu:
        // th3 = new Th3();
        // th4 = new Th4();
        // th3.setDaemon(true);
        // th4.setDaemon(true);
        // th3.start();
        // th4.start();
    }

    // ---------------- metode comune ----------------
    static void log(String s) {
        System.out.println(s);
        Platform.runLater(() -> log.add(s));
    }

    // mesajele firelor Th3 si Th4 (panoul Turcan Ion)
    static void log34(String s) {
        System.out.println(s);
        Platform.runLater(() -> log34.add(s));
    }

    static void mark(int p1, int p2, String cls) {
        Platform.runLater(() -> {
            cells[p1].getStyleClass().add(cls);
            cells[p2].getStyleClass().add(cls);
        });
    }

    // afiseaza textul litera cu litera, cu interval de 100 ms, in eticheta data
    static void printLetters(String text, Label target) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
            System.out.print(c);
            Platform.runLater(() -> target.setText(target.getText() + c));
        }
        System.out.println();
    }

    // ---------------- GUI ----------------
    static Button btnStart;
    static Label lbState1, lbState2, lbState3, lbState4;

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
        log34.clear();
        lbText.setText("");
        lbText34.setText("");
        generate();

        th1 = new Th1();
        th2 = new Th2();
        th1.setDaemon(true);
        th2.setDaemon(true);
        th1.start();
        th2.start();

        startThreads34();
    }

    static boolean alive(Thread t) {
        return t != null && t.isAlive();
    }

    static String state(Thread t) {
        if (t == null) {
            return "nu a fost creat";
        }
        return t.getState() + (t.isAlive() ? " (isAlive = true)" : " (isAlive = false)");
    }

    // lista cu mesajele firelor, colorate dupa numele firului
    static ListView<String> logView(ObservableList<String> items, String placeholder) {
        ListView<String> view = new ListView<>(items);
        view.getStyleClass().add("log-list");
        view.setPlaceholder(new Label(placeholder));
        view.setCellFactory(lv -> new ListCell<>() {
            {
                // randurile lungi trec pe randul urmator (fara bara orizontala)
                setWrapText(true);
                setPrefWidth(0);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("unu", "doi", "trei", "patru");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if (item.startsWith("Th1")) {
                        getStyleClass().add("unu");
                    } else if (item.startsWith("Th2")) {
                        getStyleClass().add("doi");
                    } else if (item.startsWith("Th3")) {
                        getStyleClass().add("trei");
                    } else if (item.startsWith("Th4")) {
                        getStyleClass().add("patru");
                    }
                }
            }
        });
        VBox.setVgrow(view, Priority.ALWAYS);
        // derulare automata la ultimul mesaj
        items.addListener((ListChangeListener<String>) c -> Platform.runLater(() -> {
            if (!items.isEmpty()) {
                view.scrollTo(items.size() - 1);
            }
        }));
        return view;
    }

    // panoul unui membru al echipei: titlu, mesajele firelor, textul afisat la final
    static AnchorPane memberPane(String title, ListView<String> view, String textTitle, Label text) {
        Label lbTitle = new Label(title);
        lbTitle.getStyleClass().add("pane-title");
        lbTitle.setWrapText(true);

        Label lbTextTitle = new Label(textTitle);
        lbTextTitle.setWrapText(true);

        text.getStyleClass().add("name-text");
        text.setMaxWidth(Double.MAX_VALUE);
        text.setWrapText(true);

        VBox box = new VBox(8, lbTitle, view, lbTextTitle, text);
        AnchorPane pane = new AnchorPane(box);
        AnchorPane.setTopAnchor(box, 2.0);
        AnchorPane.setRightAnchor(box, 10.0);
        AnchorPane.setBottomAnchor(box, 10.0);
        AnchorPane.setLeftAnchor(box, 10.0);
        return pane;
    }

    @Override
    public void start(Stage stage) {

        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 1200, 700);

        var css = getClass().getResource("style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        // bara de sus: butonul si starea celor 4 fire
        btnStart = new Button("Porneste firele");
        btnStart.getStyleClass().add("start-btn");
        btnStart.setOnAction(e -> startThreads());

        lbState1 = new Label();
        lbState2 = new Label();
        lbState3 = new Label();
        lbState4 = new Label();
        lbState1.getStyleClass().add("state1");
        lbState2.getStyleClass().add("state2");
        lbState3.getStyleClass().add("state3");
        lbState4.getStyleClass().add("state4");

        GridPane states = new GridPane();
        states.setHgap(30);
        states.setVgap(4);
        states.add(lbState1, 0, 0);
        states.add(lbState2, 0, 1);
        states.add(lbState3, 1, 0);
        states.add(lbState4, 1, 1);

        HBox top = new HBox(20, btnStart, states);
        AnchorPane.setTopAnchor(top, 15.0);
        AnchorPane.setLeftAnchor(top, 20.0);
        AnchorPane.setRightAnchor(top, 20.0);

        SplitPane sp = new SplitPane();
        AnchorPane.setTopAnchor(sp, 75.0);
        AnchorPane.setLeftAnchor(sp, 20.0);
        AnchorPane.setRightAnchor(sp, 20.0);
        AnchorPane.setBottomAnchor(sp, 20.0);
        root.getChildren().addAll(top, sp);

        // panoul 1: tabloul (pozitie: valoare) pentru Th1 si Th2
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

        Label lb1 = new Label("Tabloul (pozitie: valoare): verde - par, albastru - Th1, portocaliu - Th2");
        lb1.getStyleClass().add("pane-title");
        AnchorPane an1 = new AnchorPane();
        AnchorPane.setTopAnchor(lb1, 2.0);
        AnchorPane.setLeftAnchor(lb1, 10.0);
        AnchorPane.setTopAnchor(gd, 25.0);
        AnchorPane.setRightAnchor(gd, 10.0);
        AnchorPane.setBottomAnchor(gd, 10.0);
        AnchorPane.setLeftAnchor(gd, 10.0);
        an1.getChildren().addAll(lb1, gd);

        // panoul 2: Th1 si Th2 (Bargan Ilie)
        lbText = new Label("");
        AnchorPane an2 = memberPane(
                "Firele Th1 - Th2. Bargan Ilie\nTh1: perechi de la primul element\nTh2: perechi de la ultimul element",
                logView(log, "Apasati \"Porneste firele\""),
                "Th2 - Numele, Th1 - Prenumele:",
                lbText);

        // panoul 3: Th3 si Th4 (Turcan Ion)
        lbText34 = new Label("");
        AnchorPane an3 = memberPane(
                "Firele Th3 - Th4. Țurcan Ion\nTh3: [120, 690] de la început\nTh4: [1000, 1567] de la sfârșit",
                logView(log34, "Firele Th3 și Th4 vor fi adăugate"),
                "Th4 - Grupa, Th3 - Disciplina:",
                lbText34);

        sp.getItems().addAll(an1, an2, an3);
        sp.setDividerPositions(0.44, 0.72);

        generate();

        // starea firelor (getState / isAlive), actualizata la 100 ms;
        // butonul devine activ cand niciun fir nu mai lucreaza
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            lbState1.setText("Th1: " + state(th1));
            lbState2.setText("Th2: " + state(th2));
            lbState3.setText("Th3: " + state(th3));
            lbState4.setText("Th4: " + state(th4));
            btnStart.setDisable(alive(th1) || alive(th2) || alive(th3) || alive(th4));
        }));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();

        stage.setTitle("Laborator 3 - Sincronizarea firelor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
