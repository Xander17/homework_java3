package lesson1.arraytest;

import java.util.ArrayList;

public class ArraysTest {

    public static void main(String[] args) {
        Integer[] arrI = {1, 2, 3, 4, 5, 6};
        printArr(arrI);
        arraySwap(arrI, 1, 2);
        ArrayList<Integer> listI = toList(arrI);
        listI.forEach(x -> System.out.print(x + " "));
        System.out.println();

        String[] arrS = {"a", "b", "c", "d", "e"};
        printArr(arrS);
        arraySwap(arrS, 0, 4);
        ArrayList<String> listS = toList(arrS);
        listS.forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    private static <T> void arraySwap(T[] arr, int index1, int index2) {
        if (index1 < arr.length && index2 < arr.length) {
            T a = arr[index1];
            arr[index1] = arr[index2];
            arr[index2] = a;
        }
    }

    private static <T> ArrayList<T> toList(T[] arr) {
        return new ArrayList<T>(java.util.Arrays.asList(arr));
    }

    private static <T> void printArr(T[] arr) {
        for (T a : arr) System.out.print(a + " ");
        System.out.println();
    }
}
