package util;

/**
 * A set of utilities functions
 */
public class Util {
    /**
     * Print a two dimensional array (double)
     *
     * @param array The array to print
     */
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
