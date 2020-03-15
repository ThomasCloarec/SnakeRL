package util;

public class Util {
    public static void printTwoDimArray(double[][] array) {
        System.out.println("**********");
        for (double[] row : array) {
            for (double element : row) {
                System.out.print((double) ((int) (element * 100)) / 100 + "\t");
            }
            System.out.println();
        }
        System.out.println("**********");
    }
}
