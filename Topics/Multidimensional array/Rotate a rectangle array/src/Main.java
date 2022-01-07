import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();

        int[][] field = new int[n][n];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (i == j || i==n-j-1 || j ==n/2 || i == n/2) { System.out.print("* ");} else
                { System.out.print(". ");}
            }
            System.out.println();
        }
    }
}