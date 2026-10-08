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
    }
}

class SumEvenNum extends Thread {

    public SumEvenNum(String name){

        super(name);

    }

    public void run(){


    }
}