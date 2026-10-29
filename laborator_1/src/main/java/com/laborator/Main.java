package com.laborator;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class Main extends Application{

    @Override 
    public void start(Stage stage){

        //Layout root and the scene
        AnchorPane anchRoot = new AnchorPane();
        Scene scene = new Scene(anchRoot, 1200, 800);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); //stilul

        SplitPane sp = new SplitPane();

        AnchorPane.setTopAnchor(sp,20.0);
        AnchorPane.setLeftAnchor(sp, 20.0);
        AnchorPane.setRightAnchor(sp, 20.0);
        AnchorPane.setBottomAnchor(sp, 20.0);

        anchRoot.getChildren().add(sp);


        // Code
        int[] numbers = new int[100];

        GridPane gd = new GridPane();

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);

        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);

        gd.setVgap(2.0);
        gd.setHgap(2.0);
        gd.getColumnConstraints().addAll(cc,cc,cc,cc,cc,cc,cc,cc,cc,cc);
        gd.getRowConstraints().addAll(rc,rc,rc,rc,rc,rc,rc,rc,rc,rc);
        gd.getStyleClass().add("number-grid"); //stilul

        Label lb = new Label("Tabloul cu elemente generate aleatoriu");

        AnchorPane an1 = new AnchorPane();

        AnchorPane.setTopAnchor(gd,  20.0);
        AnchorPane.setRightAnchor(gd,  10.0);
        AnchorPane.setBottomAnchor(gd,  10.0);
        AnchorPane.setLeftAnchor(gd,  10.0);

        AnchorPane.setTopAnchor(lb, 2.0);
        AnchorPane.setRightAnchor(lb, 40.0);
        AnchorPane.setLeftAnchor(lb, 40.0);

        an1.getChildren().add(lb);
        an1.getChildren().add(gd);

        //Codul pentru laborator
        for(int i = 0; i < 100; i++){
            numbers[i] = (int)(Math.random() * 99);
            System.out.print(numbers[i] + ", ");
        }
        System.out.println();

        int j = 0;
        int step = 0;
        for(int i = 1; i <= 100; i++){

            Button btn = new Button(String.valueOf(numbers[i - 1]));
            btn.getStyleClass().add(numbers[i - 1] % 2 == 0 ? "even" : "odd");
            gd.add(btn, (i - 1) - step, j);

            if(i % 10 == 0){
                step += 10;
                j++;
            }
        }

        Thread sum1 = new Thread(new SumEvenNum(0, 99, numbers));
        Thread sum2 = new Thread(new SumEvenNum(99, 0, numbers));

        Thread hello = new Thread(new HelloMir(0, 99, numbers));
        Thread hello2 = new Thread(new HelloMir(99, 0, numbers));


        sum1.setName("Unu");
        sum1.start();
        sum2.setName("Doi");
        sum2.start();

hello.setName("Hello");
hello.start();
hello2.setName("Hello2");
hello2.start();



        //Sfarsitul codului

        //Partea Bargan Ilie
        ListView<String> view = new ListView<String>(SumEvenNum.getList());
        
        Label lb2 = new Label("Rezultatul programului. Bargan Ilie");
        AnchorPane an2 = new AnchorPane();

        AnchorPane.setTopAnchor(view,  20.0);
        AnchorPane.setRightAnchor(view,  10.0);
        AnchorPane.setBottomAnchor(view,  10.0);
        AnchorPane.setLeftAnchor(view,  10.0);
        
        AnchorPane.setTopAnchor(lb2, 2.0);
        AnchorPane.setRightAnchor(lb2, 40.0);
        AnchorPane.setLeftAnchor(lb2, 40.0);

        an2.getChildren().add(lb2);
        an2.getChildren().add(view);

        //Partea Turcan Ion

        ListView<String> view2 = new ListView<String>(HelloMir.getList());
        
        Label lb3 = new Label("Rezultatul programului. Țurcan Ion");
        AnchorPane an3 = new AnchorPane();

        AnchorPane.setTopAnchor(view2,  20.0);
        AnchorPane.setRightAnchor(view2,  10.0);
        AnchorPane.setBottomAnchor(view2,  10.0);
        AnchorPane.setLeftAnchor(view2,  10.0);
        
        AnchorPane.setTopAnchor(lb3, 2.0);
        AnchorPane.setRightAnchor(lb3, 40.0);
        AnchorPane.setLeftAnchor(lb3, 40.0);


        an3.getChildren().add(lb3);
        an3.getChildren().add(view2);
  
        sp.getItems().addAll(an1,an2,an3);
        sp.setDividerPosition(0, 0.4);
        sp.setDividerPosition(1, 0.7);



        stage.setTitle("Practicing layouts");
        stage.setScene(scene);
        stage.show();

    

    }
    public static void main(String[] args) {
        launch(args);
    }
}

class SumEvenNum implements Runnable {

    private int from, to, step, i, j;
    private int arr[];
    private static ObservableList<String> threadList = FXCollections.observableArrayList();

    public SumEvenNum(int from, int to, int[] arr){

        this.from = from;
        this.to = to;
        this.arr = arr;

    }

    private void sumEven(int max_min, int index, int step, int[] arr){

        int num1 = 0, num2 = 0;

        try{

            while(index != max_min + step){

                if(arr[index] % 2 == 0){

                    num1 = index;
                    index += step;

                    
                    while(arr[index] % 2 != 0){

                        index += step;
                    }

                    num2 = index;
                    int res = num1 + num2;

                    String thread = Thread.currentThread().getName() + ": " + num1 + 
                    " + " + num2 + " = " + res;
                    System.out.println(thread);
                    threadList.add(thread);

                }

                index += step;   
            }

        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println(Thread.currentThread().getName() + ": Tabelul nu are mai multe elemente");
        }
        
    }

    public static ObservableList<String> getList(){

        return threadList;
    }

    @Override
    public void run(){


        if(from > to){

            step = -1;
            i = from;

            sumEven(to, i, step, arr);

        }else{

            step = 1;
            j = from;

            sumEven(to, j, step, arr);


        }

        

    }
}

class HelloMir implements Runnable {

    private int from, to, step;
    private int[] arr;

    private static ObservableList<String> threadList =
            FXCollections.observableArrayList();

    public HelloMir(int from, int to, int[] arr) {
        this.from = from;
        this.to = to;
        this.arr = arr;
    }

    @Override
    public void run() {

        try {

            int firstPos = -1;

            if (from < to) {

                for (int i = from; i <= to; i++) {

                    if (arr[i] % 2 == 0) {

                        if (firstPos == -1) {
                            firstPos = i;
                        } else {

                            int suma = firstPos + i;

                            String rezultat =
                                    Thread.currentThread().getName()
                                    + ": " + firstPos
                                    + " + " + i
                                    + " = " + suma;

                            System.out.println(rezultat);
                            threadList.add(rezultat);

                            firstPos = -1;
                        }
                    }
                }

            } else {

                for (int i = from; i >= to; i--) {

                    if (arr[i] % 2 == 0) {

                        if (firstPos == -1) {
                            firstPos = i;
                        } else {

                            int suma = firstPos + i;

                            String rezultat =
                                    Thread.currentThread().getName()
                                    + ": " + firstPos
                                    + " + " + i
                                    + " = " + suma;

                            System.out.println(rezultat);
                            threadList.add(rezultat);

                            firstPos = -1;
                        }
                    }
                }
            }

        } catch (Exception e) {
            threadList.add(Thread.currentThread().getName()
                    + ": Eroare la procesare");
        }
    }

    public static ObservableList<String> getList() {
        return threadList;
    }
}