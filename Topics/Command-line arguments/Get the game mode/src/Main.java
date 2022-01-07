class Problem {
    public static void main(String[] args) {
        if (!args[2].equals("mode") || !args[0].equals("mode")) {
            System.out.println("default");
        } else {
            System.out.println(args[3]);
        }

    }
}