package com.laborator;

public class App 
{
    public static void main( String[] args )
    {
        int[] arr = new int[100];

        for(int i = 0; i < arr.length; i++){

            arr[i] = (int)(Math.random() * 99);
            System.out.print(arr[i] + ", ");
        }
        System.out.println();

        SumEvenNum sum1 = new SumEvenNum(0, 99, arr);
        SumEvenNum sum2 = new SumEvenNum(99, 0, arr);

        sum1.setName("Unu");
        sum1.start();
        sum2.setName("Doi");
        sum2.start();
  
    }
}

class SumEvenNum extends Thread {

    private int from, to, step, i, j;
    private int arr[];

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
                    System.out.println(getName() + ": " + num1 + " + " + num2 + " = " + res);
                }

                index += step;   
            }

        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println(getName() + ": Tabelul nu are mai multe elemente");
        }
        
    }

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