import java.util.Scanner;

class ArrayOperations {
    public static void reverseElements(int[][] twoDimArray) {


        for (int i = 0; i < twoDimArray.length; i++) {
            for (int j = 0; j < twoDimArray[i].length; j++) {
                System.out.print(twoDimArray[i][twoDimArray[i].length-j-1]);
            }
            System.out.println();
        }

    }
}