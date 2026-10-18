package com.laborator;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
        gd.setVgap(2.0);
        gd.setHgap(2.0);
        gd.getStyleClass().add("number-grid"); //stilul

        AnchorPane an1 = new AnchorPane();

        AnchorPane.setTopAnchor(gd,  10.0);
        AnchorPane.setRightAnchor(gd,  10.0);
        AnchorPane.setBottomAnchor(gd,  10.0);
        AnchorPane.setLeftAnchor(gd,  10.0);

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

        sum1.setName("Unu");
        sum1.start();
        sum2.setName("Doi");
        sum2.start();

        //|||||||||||||||||||||||

        ListView<String> view = new ListView<String>(SumEvenNum.getList());
        
        AnchorPane an2 = new AnchorPane();

        AnchorPane.setTopAnchor(view,  10.0);
        AnchorPane.setRightAnchor(view,  10.0);
        AnchorPane.setBottomAnchor(view,  10.0);
        AnchorPane.setLeftAnchor(view,  10.0);

        an2.getChildren().add(view);
  
        sp.getItems().addAll(an1,an2);
        sp.setDividerPosition(0, 0.7);



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