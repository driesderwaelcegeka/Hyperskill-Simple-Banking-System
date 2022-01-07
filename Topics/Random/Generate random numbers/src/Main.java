import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();


        int a = scanner.nextInt();
        int b = scanner.nextInt();
        Random random = new Random();

        int sum=0;

        int interval = b-a+1;

        for (int i = 0; i < n; i++) {
            sum += (random.nextInt(interval) + a);
        }
        System.out.println(sum);
    }
}