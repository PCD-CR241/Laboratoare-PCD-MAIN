package com.laborator;

/*

Varianta 2

Sunt dați 3 producători care generează aleatoriu 2 obiecte care sunt consumate de 4
consumatori. De afişat informaţia despre producerea şi consumarea obiectelor,
mesajele despre cazurile când “depozitul e gol sau plin”.
Toate operaţiile se efectuează până când fiecare consumator
este îndestulat cu 2 obiecte.

Dimensiunea depozitului este 5 obiecte.

Tip obiecte: Numere impare

Producator: Țurcan Ion
Consumator: Bargan Ilie

*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{

    // Valorile din varianta 2
    static final int X = 3;   // producatori
    static final int Y = 4;   // consumatori
    static final int Z = 2;   // obiecte pentru fiecare consumator
    static final int D = 5;   // dimensiunea depozitului
    static final int F = 2;   // cate obiecte produce un producator o data

    static final int POOL = X + Y;   // dimensiunea pool-ului fix de fire

    static Deposit depozit = new Deposit(D, Y * Z);
    static ExecutorService executor;

    // Contor pentru consumatori (il incrementeaza consumatorii - Bargan Ilie)
    static final AtomicInteger totalConsumate = new AtomicInteger(0);

    // Elementele interfetei
    static ObservableList<LogEntry> logProducatori = FXCollections.observableArrayList();
    static ObservableList<LogEntry> logConsumatori = FXCollections.observableArrayList();
    static Label[] locuri = new Label[D];
    static Label stareDepozit = new Label();
    static Label baraStare = new Label();
    static Map<String, Label[]> carduri = new HashMap<>();   // nume -> {fir, stare, contor}
    static long timpStart;
    static long timpFinal = -1;   // durata totala, dupa terminarea pool-ului

    public void start(Stage stage){

        BorderPane bdRoot = new BorderPane();
        bdRoot.setPadding(new Insets(16));
        Scene scene = new Scene(bdRoot, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Sus: depozitul cu cele D locuri si butonul Start
        Button btnStart = new Button("Start");
        btnStart.getStyleClass().add("start-btn");
        btnStart.setOnAction(e -> {
            btnStart.setDisable(true);
            pornestePool();
        });
        bdRoot.setTop(createDepositPane(btnStart));

        // Centru: producatorii (stanga) si consumatorii (dreapta)
        HBox cardProd = new HBox(10);
        for (int i = 1; i <= X; i++){
            cardProd.getChildren().add(createCard("Producător " + i, "p" + i, "pus"));
        }
        HBox cardCons = new HBox(10);
        for (int i = 1; i <= Y; i++){
            cardCons.getChildren().add(createCard("Consumator " + i, "c" + i, "luat"));
        }

        SplitPane sp = new SplitPane(
                createPane("Producători (" + X + ") — Țurcan Ion", cardProd, createLogView(logProducatori)),
                createPane("Consumatori (" + Y + ") — Bargan Ilie", cardCons, createLogView(logConsumatori)));
        sp.setDividerPosition(0, 0.5);
        BorderPane.setMargin(sp, new Insets(16, 0, 16, 0));
        bdRoot.setCenter(sp);

        // Jos: starea pool-ului
        baraStare.getStyleClass().add("status-bar");
        baraStare.setMaxWidth(Double.MAX_VALUE);
        bdRoot.setBottom(baraStare);

        showDeposit(new ArrayList<>());
        updateStatus();

        stage.setTitle("Pool-uri de fire de execuție în Java");
        stage.setScene(scene);
        stage.show();
    }

    // Creeaza pool-ul fix de fire si trimite sarcinile (Runnable) spre executie
    private static void pornestePool(){

        timpStart = System.currentTimeMillis();
        executor = Executors.newFixedThreadPool(POOL);

        for (int i = 1; i <= X; i++){
            executor.execute(new Producator(depozit, "Producător " + i));
        }

        // Consumatorii (Bargan Ilie): sarcini trimise in acelasi pool fix
        for (int i = 1; i <= Y; i++){
            executor.execute(new Consumator(depozit, "Consumator " + i));
        }

        executor.shutdown();   // nu se mai primesc sarcini noi; cele trimise se termina

        // Asteapta terminarea tuturor sarcinilor si afiseaza raportul final
        Thread final_ = new Thread(() -> {
            boolean terminat = false;
            try {
                terminat = executor.awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                return;
            }
            if (!terminat){
                executor.shutdownNow();   // opreste fortat sarcinile blocate
                addLog(logProducatori, "Timpul de așteptare (60 s) a expirat, pool-ul a fost oprit forțat", "warn");
            }
            long timp = System.currentTimeMillis() - timpStart;
            timpFinal = timp;
            addLog(logProducatori, "===== RAPORT FINAL =====", "info");
            addLog(logProducatori, "Total produse: " + depozit.getTotalProduse() + "/" + depozit.getTotalNecesar(), "info");
            addLog(logProducatori, "Total consumate: " + totalConsumate.get() + "/" + depozit.getTotalNecesar(), "info");
            addLog(logProducatori, "Pool fix de " + POOL + " fire, depozit de " + D + " -> timp " + timp + " ms", "info");
            updateStatus();
        }, "Raport final");
        final_.setDaemon(true);
        final_.start();

        // Actualizeaza bara de stare (timpul) cat lucreaza pool-ul
        Thread ceas = new Thread(() -> {
            while (!executor.isTerminated()){
                updateStatus();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "Ceas");
        ceas.setDaemon(true);
        ceas.start();
    }

    private static VBox createDepositPane(Button btnStart){

        Label titlu = new Label("Depozit — BlockingQueue (capacitate " + D + " numere impare)");
        titlu.getStyleClass().add("pane-title");

        HBox rand = new HBox(10);
        rand.setAlignment(Pos.CENTER_LEFT);
        for (int i = 0; i < D; i++){
            locuri[i] = new Label();
            locuri[i].getStyleClass().add("slot");
            locuri[i].setAlignment(Pos.CENTER);
            rand.getChildren().add(locuri[i]);
        }

        stareDepozit.getStyleClass().add("deposit-state");
        Region spatiu = new Region();
        HBox.setHgrow(spatiu, Priority.ALWAYS);
        rand.getChildren().addAll(stareDepozit, spatiu, btnStart);

        VBox box = new VBox(8, titlu, rand);
        box.getStyleClass().add("panel");
        return box;
    }

    private static VBox createCard(String nume, String culoare, String verb){

        Label lbNume = new Label(nume);
        lbNume.getStyleClass().add("card-name");
        Label lbFir = new Label("fir: -");
        lbFir.getStyleClass().add("card-thread");
        Label lbStare = new Label("nu a pornit");
        lbStare.getStyleClass().add("card-state");
        Label lbContor = new Label("A " + verb + ": 0");
        lbContor.getStyleClass().add("card-count");

        carduri.put(nume, new Label[]{lbFir, lbStare, lbContor});

        VBox card = new VBox(3, lbNume, lbFir, lbStare, lbContor);
        card.getStyleClass().addAll("card", culoare);
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private static VBox createPane(String title, HBox cards, ListView<LogEntry> log){

        Label lb = new Label(title);
        lb.getStyleClass().add("pane-title");
        VBox.setVgrow(log, Priority.ALWAYS);
        VBox box = new VBox(10, lb, cards, log);
        box.setPadding(new Insets(0, 8, 0, 8));
        return box;
    }

    private static ListView<LogEntry> createLogView(ObservableList<LogEntry> list){

        ListView<LogEntry> view = new ListView<>(list);
        view.getStyleClass().add("log-list");

        view.setCellFactory(lv -> new ListCell<LogEntry>() {
            @Override
            protected void updateItem(LogEntry item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("p1", "p2", "p3", "c1", "c2", "c3", "c4", "warn", "info");
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.text);
                    getStyleClass().add(item.style);
                }
            }
        });

        list.addListener((javafx.collections.ListChangeListener<LogEntry>) c ->
                Platform.runLater(() -> view.scrollTo(list.size() - 1)));

        return view;
    }

    static class LogEntry {

        final String text;
        final String style;

        LogEntry(String text, String style){
            this.text = text;
            this.style = style;
        }

        @Override
        public String toString(){
            return text;
        }
    }

    // Clasa de stil (culoarea) dupa nume: "Producător 2" -> "p2", "Consumator 3" -> "c3"
    private static String styleOf(String cine){
        String nr = cine.substring(cine.lastIndexOf(' ') + 1);
        return (cine.startsWith("Producător") ? "p" : "c") + nr;
    }

    private static void addLog(ObservableList<LogEntry> list, String msg, String style){

        System.out.println(msg);
        LogEntry entry = new LogEntry(msg, style);
        Platform.runLater(() -> list.add(entry));
    }

    // Afiseaza un mesaj in consola si in lista producatorilor / consumatorilor.
    // cine = "Producător 1", "Consumator 2", ... ; warn = true pentru "depozitul e plin / gol".
    // (Firele din pool au nume ca "pool-1-thread-3", de aceea numele se da explicit.)
    static void log(String cine, String msg, boolean warn){

        ObservableList<LogEntry> list = cine.startsWith("Producător") ? logProducatori : logConsumatori;
        addLog(list, msg, warn ? "warn" : styleOf(cine));
    }

    // Actualizeaza cardul: firul din pool care executa sarcina, starea si contorul.
    // contor < 0 -> contorul nu se schimba.
    static void setState(String cine, String stare, int contor){

        String fir = Thread.currentThread().getName();
        Platform.runLater(() -> {
            Label[] lb = carduri.get(cine);
            if (lb == null) return;
            lb[0].setText("fir: " + fir);
            lb[1].setText(stare);
            if (contor >= 0){
                String text = lb[2].getText();
                lb[2].setText(text.substring(0, text.indexOf(':') + 1) + " " + contor);
            }
        });
        updateStatus();
    }

    // Afiseaza continutul depozitului (o copie a cozii)
    static void showDeposit(List<Integer> obiecte){

        Platform.runLater(() -> {
            for (int i = 0; i < D; i++){
                locuri[i].getStyleClass().remove("full");
                if (i < obiecte.size()){
                    locuri[i].setText(String.valueOf(obiecte.get(i)));
                    locuri[i].getStyleClass().add("full");
                } else {
                    locuri[i].setText("");
                }
            }
            stareDepozit.getStyleClass().removeAll("plin", "gol");
            if (obiecte.size() >= D){
                stareDepozit.setText("Depozitul e plin (" + D + "/" + D + ")");
                stareDepozit.getStyleClass().add("plin");
            } else if (obiecte.isEmpty()){
                stareDepozit.setText("Depozitul e gol (0/" + D + ")");
                stareDepozit.getStyleClass().add("gol");
            } else {
                stareDepozit.setText(obiecte.size() + "/" + D + " numere");
            }
        });
        updateStatus();
    }

    // Bara de jos: pool-ul, cate obiecte s-au produs / consumat si timpul
    static void updateStatus(){

        String pool;
        long timp = 0;
        if (executor == null){
            pool = "nepornit";
        } else {
            pool = executor.isTerminated() ? "terminat" : "lucrează";
            timp = timpFinal >= 0 ? timpFinal : System.currentTimeMillis() - timpStart;
        }
        String text = "Pool fix: " + POOL + " fire (" + pool + ")"
                + "   |   Produse: " + depozit.getTotalProduse() + "/" + depozit.getTotalNecesar()
                + "   |   Consumate: " + totalConsumate.get() + "/" + depozit.getTotalNecesar()
                + "   |   Timp: " + String.format("%.1f", timp / 1000.0) + " s";
        Platform.runLater(() -> baraStare.setText(text));
    }

    public static void main(String[] args) {
        launch(args);
    }
}


// Depozitul: o coada BlockingQueue cu capacitatea D.
// Coada este deja sincronizata: put() asteapta cand e plina, take() asteapta cand e goala.
class Deposit{

    final BlockingQueue<Integer> coada;
    private final int totalNecesar;                                // Y * Z = 8 obiecte
    private final AtomicInteger totalProduse = new AtomicInteger(0);

    // Lacatul producatorilor: cele 2 numere ale unui producator intra unul dupa altul,
    // fara sa se amestece cu numerele altui producator.
    // Consumatorii NU folosesc acest lacat (doar coada.take()), altfel s-ar bloca reciproc.
    private final Object lacatProducatori = new Object();

    public Deposit(int capacitate, int totalNecesar){
        this.coada = new ArrayBlockingQueue<>(capacitate);
        this.totalNecesar = totalNecesar;
    }

    // Producatorul pune 2 numere impare in depozit.
    // Intoarce false daca s-au produs deja toate obiectele necesare (nu mai pune nimic).
    public boolean put(String producator, int a, int b) throws InterruptedException {
        synchronized (lacatProducatori){
            if (totalProduse.get() >= totalNecesar){
                return false;
            }
            if (coada.remainingCapacity() < 2){
                Main.log(producator, "Depozitul e plin " + coada + " -> " + producator + " așteaptă", true);
                Main.setState(producator, "așteaptă (plin)", -1);
            }

            coada.put(a);   // asteapta daca nu e loc
            coada.put(b);
            int total = totalProduse.addAndGet(2);
            List<Integer> continut = new ArrayList<>(coada);   // o singura "fotografie" a cozii

            Main.log(producator, producator + " a pus " + a + ", " + b + " -> "
                    + continut.size() + "/" + Main.D + " " + continut
                    + " (total " + total + "/" + totalNecesar + ")", false);
            if (continut.size() == Main.D){
                Main.log(producator, "Depozitul e plin " + continut, true);
            }
            Main.showDeposit(continut);
            return true;
        }
    }

    public int getTotalProduse(){
        return totalProduse.get();
    }

    public int getTotalNecesar(){
        return totalNecesar;
    }

    // Consumatorul ia un numar impar din depozit (Bargan Ilie).
    // Sincronizarea o face BlockingQueue: take() asteapta singur cat timp coada e goala
    // si, dupa ce scoate numarul, ii anunta pe producatorii care asteapta loc.
    // Lacatul consumatorilor: un singur consumator ia si afiseaza la un moment dat,
    // ca mesajele lor sa apara in ordine. Este separat de lacatProducatori,
    // deci un consumator care asteapta in take() nu blocheaza producatorii.
    private final Object lacatConsumatori = new Object();

    public int get(String consumator) throws InterruptedException {
        synchronized (lacatConsumatori){
            if (coada.isEmpty()){
                Main.log(consumator, "Depozitul e gol -> " + consumator + " așteaptă", true);
                Main.setState(consumator, "așteaptă (gol)", -1);
            }

            int numar = coada.take();   // asteapta daca depozitul e gol
            int total = Main.totalConsumate.incrementAndGet();
            List<Integer> continut = new ArrayList<>(coada);   // o "fotografie" a cozii

            Main.log(consumator, consumator + " a luat " + numar + " -> "
                    + continut.size() + "/" + Main.D + " " + continut
                    + " (total " + total + "/" + totalNecesar + ")", false);
            if (continut.isEmpty()){
                Main.log(consumator, "Depozitul e gol", true);
            }
            Main.showDeposit(continut);
            return numar;
        }
    }
}

// Consumatorul (Bargan Ilie): sarcina (Runnable) executata de un fir din pool.
// Ia numere impare din depozit pana este indestulat cu Z = 2 obiecte.
// Apoi sarcina se termina si firul se intoarce in pool.
class Consumator implements Runnable{

    private final Deposit depozit;
    private final String nume;
    private int luate = 0;   // cate numere a luat acest consumator

    public Consumator(Deposit depozit, String nume){
        this.depozit = depozit;
        this.nume = nume;
    }

    @Override
    public void run(){
        Main.log(nume, nume + " a pornit pe firul " + Thread.currentThread().getName(), false);
        try {
            while (luate < Main.Z){
                Main.setState(nume, "vrea un număr...", luate);
                Thread.sleep(300 + (int)(Math.random() * 700));   // pauza intre cereri

                int numar = depozit.get(nume);
                luate++;
                Main.setState(nume, "a luat " + numar, luate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();   // pool-ul a fost oprit (shutdownNow)
            Main.setState(nume, "oprit", -1);
            return;
        }
        Main.log(nume, nume + " a fost îndestulat cu " + luate + " numere", false);
        Main.setState(nume, "îndestulat ✓", luate);
    }
}

// Producatorul: sarcina (Runnable) executata de un fir din pool.
// Genereaza aleatoriu cate 2 numere impare (F = 2) si le pune in depozit, pana cand
// s-au produs Y * Z obiecte (cate au nevoie consumatorii). Apoi sarcina se termina
// si firul se intoarce in pool.
class Producator implements Runnable{

    private final Deposit depozit;
    private final String nume;
    private int puse = 0;   // cate numere a pus acest producator

    public Producator(Deposit depozit, String nume){
        this.depozit = depozit;
        this.nume = nume;
    }

    // Numar impar aleatoriu intre 1 si 99
    private int numarImpar(){
        return 2 * (int)(Math.random() * 50) + 1;
    }

    @Override
    public void run(){
        Main.log(nume, nume + " a pornit pe firul " + Thread.currentThread().getName(), false);
        try {
            while (depozit.getTotalProduse() < depozit.getTotalNecesar()){
                Main.setState(nume, "produce...", puse);
                Thread.sleep(500 + (int)(Math.random() * 1000));   // timpul de "producere"

                int a = numarImpar();
                int b = numarImpar();
                if (!depozit.put(nume, a, b)){
                    break;   // alt producator a pus deja ultimele obiecte
                }
                puse += Main.F;
                Main.setState(nume, "a pus " + a + ", " + b, puse);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();   // pool-ul a fost oprit (shutdownNow)
            Main.setState(nume, "oprit", -1);
            return;
        }
        Main.log(nume, nume + " s-a finalizat (a pus " + puse + " numere)", false);
        Main.setState(nume, "finalizat", puse);
    }
}
