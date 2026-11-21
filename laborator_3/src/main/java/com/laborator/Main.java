package com.laborator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class Main extends Application{

    static int numbers[] = new int[100];
    static SumStart fromStart = new SumStart("Th1");
    static SumFinish fromFinish = new SumFinish("Th2");

    static ObservableList<LogEntry> th12List = FXCollections.observableArrayList();
    static ObservableList<LogEntry> th34List = FXCollections.observableArrayList();
    static Button[] cells = new Button[100];
    static Label nameLabel = new Label();
    static String surname = "", name = "";

    public void start(Stage stage){

        BorderPane bdRoot = new BorderPane();
        bdRoot.setPadding(new Insets(20));
        Scene scene = new Scene(bdRoot, 1600, 1000);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        SplitPane sp = new SplitPane();
        bdRoot.setCenter(sp);

        GridPane gd = new GridPane();

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);

        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);

        gd.setVgap(2.0);
        gd.setHgap(2.0);
        for(int k = 0; k < 10; k++){
            gd.getColumnConstraints().add(cc);
            gd.getRowConstraints().add(rc);
        }
        gd.getStyleClass().add("number-grid");

        for(int i = 0; i < 100; i++){

            Button btn = new Button(String.valueOf(numbers[i]));
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btn.getStyleClass().add(numbers[i] % 2 == 0 ? "even" : "odd");
            cells[i] = btn;
            gd.add(btn, i % 10, i / 10);
        }

        AnchorPane an1 = createPane("Tabloul cu elemente generate aleatoriu", gd);

        nameLabel.getStyleClass().add("name-text");
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        BorderPane.setMargin(nameLabel, new Insets(8, 0, 0, 0));
        BorderPane mine = new BorderPane();
        mine.setCenter(createLogView(th12List));
        mine.setBottom(nameLabel);
        AnchorPane an2 = createPane("Th1 și Th2. Bargan Ilie", mine);

        AnchorPane an3 = createPane("Th3 și Th4", createLogView(th34List));

        sp.getItems().addAll(an1, an2, an3);
        sp.setDividerPosition(0, 0.4);
        sp.setDividerPosition(1, 0.7);

        stage.setTitle("Sincronizarea folosind metodele clasei Thread");
        stage.setScene(scene);
        stage.show();

        fromStart.start();
        fromFinish.start();
    }

    private static AnchorPane createPane(String title, javafx.scene.Node content){

        Label lb = new Label(title);
        lb.getStyleClass().add("pane-title");
        AnchorPane an = new AnchorPane();

        AnchorPane.setTopAnchor(content, 20.0);
        AnchorPane.setRightAnchor(content, 10.0);
        AnchorPane.setBottomAnchor(content, 10.0);
        AnchorPane.setLeftAnchor(content, 10.0);

        AnchorPane.setTopAnchor(lb, 2.0);
        AnchorPane.setRightAnchor(lb, 40.0);
        AnchorPane.setLeftAnchor(lb, 40.0);

        an.getChildren().addAll(lb, content);
        return an;
    }

    private static ListView<LogEntry> createLogView(ObservableList<LogEntry> list){

        ListView<LogEntry> view = new ListView<LogEntry>(list);
        view.getStyleClass().add("log-list");

        view.setCellFactory(lv -> new ListCell<LogEntry>() {
            @Override
            protected void updateItem(LogEntry item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("unu", "doi", "trei", "patru");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.text);
                    String cls = switch (item.thread) {
                        case "Th1" -> "unu";
                        case "Th2" -> "doi";
                        case "Th3" -> "trei";
                        case "Th4" -> "patru";
                        default -> null;
                    };
                    if (cls != null) getStyleClass().add(cls);
                }
            }
        });

        list.addListener((javafx.collections.ListChangeListener<LogEntry>) c ->
                Platform.runLater(() -> view.scrollTo(list.size() - 1)));

        return view;
    }

    static class LogEntry {

        final String thread;
        String text;

        LogEntry(String thread, String text){
            this.thread = thread;
            this.text = text;
        }

        @Override
        public String toString(){
            return text;
        }
    }

    static void log(ObservableList<LogEntry> list, String msg){

        System.out.println(msg);
        LogEntry entry = new LogEntry(Thread.currentThread().getName(), msg);
        Platform.runLater(() -> list.add(entry));
    }

    static void mark(String styleClass, int idx1, int idx2){

        Platform.runLater(() -> {
            cells[idx1].getStyleClass().add(styleClass);
            cells[idx2].getStyleClass().add(styleClass);
        });
    }

    static void showName(String s, String n){

        Platform.runLater(() -> {
            if (s != null) surname = s;
            if (n != null) name = n;
            nameLabel.setText((surname + "    " + name).trim());
        });
    }

    static void show(ObservableList<LogEntry> list, String label, String word, boolean isSurname) {

        System.out.print(label);

        LogEntry entry = new LogEntry(Thread.currentThread().getName(), label);
        Platform.runLater(() -> list.add(entry));

        String box = label.substring(label.indexOf(' ') + 1);
        boolean interrupted = false;

        for (int j = 0; j < word.length(); j++) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                interrupted = true;
            }

            System.out.print(word.charAt(j));

            String text = label + word.substring(0, j + 1);
            String part = box + word.substring(0, j + 1);
            Platform.runLater(() -> {
                entry.text = text;
                int idx = list.indexOf(entry);
                if (idx >= 0) list.set(idx, entry);
            });
            showName(isSurname ? part : null, isSurname ? null : part);
        }

        System.out.println();

        if (interrupted) {
            Thread.currentThread().interrupt();
        }
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

        launch(args);
    }

    static class SumStart extends Thread {

        int i = 0;
        int idx1, idx2, res;
        boolean wasInterrupted = false;
        String surname = "Ilie";

        public SumStart(String name) {

            super(name);
            setDaemon(true);
        }

        @Override
        public void run() {

            log(th12List, getName() + " a startat");

            while (i < numbers.length) {

                if (numbers[i] % 2 == 0) {

                    idx1 = i;
                    i++;

                    while (i < numbers.length && numbers[i] % 2 != 0) {

                        i++;

                    }

                    if (i >= numbers.length) break;

                    idx2 = i;

                    res = idx1 + idx2;
                    log(th12List, getName() +
                            ": " + idx1 + " + " + idx2 + " = " + res);
                    mark("th1", idx1, idx2);

                    try {

                        Thread.sleep(100);

                    } catch (InterruptedException e) {

                        wasInterrupted = true;
                    }

                }
                i++;

            }

            boolean otherInterrupted = Thread.interrupted() || wasInterrupted;

            fromFinish.interrupt();

            if (!otherInterrupted) {

                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                }
            }

            show(th12List, "1. Surname: ", surname, true);

            fromFinish.interrupt();
        }
    }

    static class SumFinish extends Thread {

        int i = numbers.length - 1;
        int idx1, idx2, res;
        boolean wasInterrupted = false;
        String name = "Bargan";

        public SumFinish(String name) {

            super(name);
            setDaemon(true);
        }

        @Override
        public void run() {

            log(th12List, getName() + " a startat");

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
                    log(th12List, getName() +
                            ": " + idx1 + " + " + idx2 + " = " + res);
                    mark("th2", idx1, idx2);

                    try {

                        Thread.sleep(100);


                    } catch (InterruptedException e) {

                        wasInterrupted = true;
                    }

                }
                i--;

            }

            boolean otherInterrupted = Thread.interrupted() || wasInterrupted;

            fromStart.interrupt();

            if (!otherInterrupted) {

                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                }
            }

            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
            }

            show(th12List, "2. Name: ", name, false);
        }
    }
}
