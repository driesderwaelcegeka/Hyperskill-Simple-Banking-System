import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();

        int[][] field = new int[n][n];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                for (int k = 0; k <= n; k++){
                    if (i == j + k || i == j - k) { System.out.print(k+" ");}
                }
            }
            System.out.println();
        }
    }
}