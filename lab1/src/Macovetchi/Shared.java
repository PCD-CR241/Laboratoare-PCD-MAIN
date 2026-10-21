package Macovetchi;


public class Shared {
    public static final int SIZE = 100;
    public static final int[] tablou = generateArray(SIZE);

    public static int[] generateArray(int SIZE) {
        int[] table = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            table[i] = (int) (Math.random() * 100) + 1;
        }
        return table;
    }
}