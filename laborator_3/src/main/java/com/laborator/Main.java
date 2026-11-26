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

    // Th3 și Th4 (Țurcan Ion): Sarcina 3 și Sarcina 4, varianta 2
    static ParcurgereInainte inainte = new ParcurgereInainte("Th3", 120, 690);
    static ParcurgereInapoi inapoi = new ParcurgereInapoi("Th4", 1567, 1000);
    static Label infoLabel = new Label();
    static String grupa = "", disciplina = "";

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
        nameLabel.setStyle("-fx-font-size: 18px;");   // incap pe un rand Numele si Prenumele
        BorderPane.setMargin(nameLabel, new Insets(8, 0, 0, 0));
        BorderPane mine = new BorderPane();
        mine.setCenter(createLogView(th12List));
        mine.setBottom(nameLabel);
        AnchorPane an2 = createPane("Th1 și Th2. Bargan Ilie", mine);

        infoLabel.getStyleClass().add("name-text");
        infoLabel.setMaxWidth(Double.MAX_VALUE);
        infoLabel.setWrapText(true);
        BorderPane.setMargin(infoLabel, new Insets(8, 0, 0, 0));
        BorderPane al_meu = new BorderPane();
        al_meu.setCenter(createLogView(th34List));
        al_meu.setBottom(infoLabel);
        AnchorPane an3 = createPane("Th3 și Th4. Țurcan Ion", al_meu);

        sp.getItems().addAll(an1, an2, an3);
        sp.setDividerPosition(0, 0.4);
        sp.setDividerPosition(1, 0.7);

        stage.setTitle("Sincronizarea folosind metodele clasei Thread");
        stage.setScene(scene);
        stage.show();

        fromStart.start();
        fromFinish.start();
        inainte.start();
        inapoi.start();
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
            // Numele (Bargan) in stanga, Prenumele (Ilie) in dreapta
            nameLabel.setText((name + "    " + surname).trim());
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
        String surname = "Țurcan, Bargan";

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

            // Th1 a terminat calculul: il anunta pe Th2 (il trezeste din sleep)
            fromFinish.interrupt();

            // Th1 doarme pana il trezeste Th4 prin interrupt(), dupa ce Th4 a afisat grupa
            // (ordinea ceruta: Th2 numele, Th4 grupa, Th1 prenumele, Th3 disciplina)
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
            }

            show(th12List, "1. Prenumele: ", surname, true);
        }
    }

    static class SumFinish extends Thread {

        int i = numbers.length - 1;
        int idx1, idx2, res;
        boolean wasInterrupted = false;
        String name = "Ion, Ilie";

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

            // A fost Th2 deja trezit de Th1 (Th1 a terminat calculul mai devreme)?
            boolean th1Terminat = Thread.interrupted() || wasInterrupted;

            // Daca nu, Th2 doarme pana il trezeste Th1 prin interrupt()
            if (!th1Terminat) {

                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                }
            }

            // Afisarea incepe doar dupa finalizarea sarcinilor tuturor firelor:
            // Th2 verifica periodic (sleep) daca si Th3, Th4 si-au terminat sarcinile
            while (!inainte.sarcinaTerminata || !inapoi.sarcinaTerminata) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
            log(th12List, "Toate sarcinile Th1-Th4 s-au terminat, începe afișarea");

            // Th2 afiseaza primul (numele), apoi se termina -> Th4 vede ca Th2 nu mai e viu
            show(th12List, "2. Numele: ", name, false);
        }
    }

    // ===================== Th3 și Th4 — Țurcan Ion =====================
    // Ordinea afișării cerută: Th2 numele -> Th4 grupa -> Th1 prenumele -> Th3 disciplina
    // Metode de sincronizare folosite (diferite de sleep()/interrupt() de la Th1-Th2):
    //   1) isAlive() - Th4 așteaptă cât timp Th2 mai este în viață, apoi afișează grupa
    //                  și îl trezește pe Th1 (care doarme) ca să-și afișeze prenumele
    //   2) join()    - Th3 așteaptă ca Th1 să se termine complet, apoi afișează disciplina

    // Clasa de bază: parcurge intervalul cu pasul dat, caută numerele pare două câte două
    // și afișează suma pozițiilor lor (poziția = indexul numărului în interval, de la 0)
    static abstract class Parcurgere extends Thread {

        protected final int de_la, pana_la;
        protected final int inceput;   // primul număr din interval (are poziția 0)

        // devine true când firul a terminat de parcurs intervalul (sarcina lui)
        volatile boolean sarcinaTerminata = false;

        Parcurgere(String nume, int de_la, int pana_la) {
            super(nume);
            this.de_la = de_la;
            this.pana_la = pana_la;
            this.inceput = Math.min(de_la, pana_la);
            setDaemon(true);
        }

        protected void parcurge(int pas) {

            int pozPrecedenta = -1;   // poziția primului număr par din pereche, -1 = încă nu avem
            int perechi = 0;

            for (int x = de_la; x != pana_la + pas; x += pas) {

                if (x % 2 != 0) continue;

                int poz = x - inceput;

                if (pozPrecedenta == -1) {
                    pozPrecedenta = poz;
                    continue;
                }

                int suma = pozPrecedenta + poz;
                log(th34List, getName() + ": " + pozPrecedenta + " + " + poz + " = " + suma
                        + "   (" + (pozPrecedenta + inceput) + ", " + x + ")");
                perechi++;
                pozPrecedenta = -1;

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            log(th34List, getName() + " a terminat: " + perechi + " perechi");
            sarcinaTerminata = true;
        }
    }

    // Th3: Sarcina 3 - parcurge de la început intervalul [120, 690]
    static class ParcurgereInainte extends Parcurgere {

        ParcurgereInainte(String nume, int de_la, int pana_la) {
            super(nume, de_la, pana_la);
        }

        @Override
        public void run() {

            log(th34List, getName() + " a startat: [" + de_la + ", " + pana_la + "] de la început");
            parcurge(+1);

            // Sincronizarea 2: join() - Th3 nu continuă până nu se termină Th1
            // (Th1 se termină doar după Th4, deci disciplina apare ultima)
            log(th34List, getName() + " așteaptă Th1 prin join()");
            try {
                fromStart.join();
            } catch (InterruptedException e) {
                return;
            }

            scrieLent("3. Disciplina: ", "Programarea concurentă și distribuită", false);
        }
    }

    // Th4: Sarcina 4 - parcurge de la sfârșit intervalul [1000, 1567]
    static class ParcurgereInapoi extends Parcurgere {

        ParcurgereInapoi(String nume, int de_la, int pana_la) {
            super(nume, de_la, pana_la);
        }

        @Override
        public void run() {

            log(th34List, getName() + " a startat: [" + pana_la + ", " + de_la + "] de la sfârșit");
            parcurge(-1);

            // Sincronizarea 1: isAlive() - Th4 verifică periodic dacă Th2 încă rulează
            log(th34List, getName() + " așteaptă cât timp Th2.isAlive()");
            while (fromFinish.isAlive()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    return;
                }
            }

            scrieLent("4. Grupa: ", "CR-241", true);

            // Grupa e afișată: Th1 (care doarme) poate afișa prenumele
            log(th34List, getName() + " îl trezește pe Th1");
            fromStart.interrupt();
        }
    }

    // Afișează textul literă cu literă, la 100 ms, în listă și în eticheta de jos
    static void scrieLent(String eticheta, String text, boolean eGrupa) {

        LogEntry rand = new LogEntry(Thread.currentThread().getName(), eticheta);
        Platform.runLater(() -> th34List.add(rand));
        System.out.print(eticheta);

        for (int j = 1; j <= text.length(); j++) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.print(text.charAt(j - 1));
            String bucata = text.substring(0, j);

            Platform.runLater(() -> {
                rand.text = eticheta + bucata;
                int idx = th34List.indexOf(rand);
                if (idx >= 0) th34List.set(idx, rand);

                if (eGrupa) grupa = bucata; else disciplina = bucata;
                infoLabel.setText((grupa + "\n" + disciplina).trim());
            });
        }
        System.out.println();
    }
}
