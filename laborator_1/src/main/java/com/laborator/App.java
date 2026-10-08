package com.laborator;

public class App 
{
    public static void main( String[] args )
    {
        int[] arr = new int[100];

        for(int i = 0; i < 99; i++){

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

    private void sumEven(int index, int step, int[] arr){

        int num1 = 0, num2 = 0;

        try{

            if(arr[index] % 2 == 0){

                num1 = index;
                index += step;

                do{

                    if(arr[index] % 2 == 0){

                        num2 = index;
                        break;
                    }

                }while(arr[index] % 2 != 0);

            }else{

                index += step;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            System.err.println("Tabloul nu mai are alte elemente");
        }

                        
        int res = num1 + num2;
        System.out.println(getName() + ": " + res);
    }

    public void run(){


        if(from > to){

            step = -1;
            i = from;

            while(i != to){

                sumEven(i, step, arr);

            }

        }else{

            step = 1;
            j = to;

            while(j != from){

                sumEven(j, step, arr);

            }
        }

        

    }
}