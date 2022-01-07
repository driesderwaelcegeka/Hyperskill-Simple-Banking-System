package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {

    private Scanner scanner = new Scanner(System.in);

    List<Card> cardList = new ArrayList<>();

    String url = "";
    SQLiteDataSource dataSource = new SQLiteDataSource();

    String loggedInCard;

    public void  start(String[] args) {
        this.url = "jdbc:sqlite:./" + args[1];

        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");
                displayMenu();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void displayMenu() {
        while (true) {
            System.out.println(
                    "1. Create an account\n" +
                            "2. Log into account\n" +
                            "0. Exit"
            );

            int request = Integer.parseInt(scanner.nextLine());
            switch (request) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    logIn();
                    break;
                case 0:
                    System.exit(8);
                    break;
                default:
                    System.out.println("Such option doesn't exist.");
            }
        }

    }

    private void loggedInMenu() {
        while (true) {
            System.out.println(
                    "1. Balance\n" +
                            "2. Add income\n" +
                            "3. Do transfer\n" +
                            "4. Close account\n" +
                            "5. Log out\n" +
                            "0. Exit"
            );

            int request = Integer.parseInt(scanner.nextLine());
            switch (request) {
                case 1:
                    balance();
                    break;
                case 2:
                    addIncome();
                    break;
                case 3:
                    doTransfer();
                    break;
                case 4:
                    closeAccount();
                    break;
                case 5:
                    System.out.println("You have successfully logged out!");
                    displayMenu();
                    break;
                case 0:
                    System.exit(8);
                    break;
                default:
                    System.out.println("Such option doesn't exist.");
            }
        }    }

    private void closeAccount() {

        String sql = "DELETE FROM card where number = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggedInCard);
            pstmt.executeUpdate();
            System.out.println("The account has been closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void doTransfer() {
        System.out.println("Enter card number for transfer:");
        String receiver = scanner.nextLine();

        if (checkLuhn(receiver)){
            if (!receiver.equals(loggedInCard)) {


                    if (checkReceiver(receiver)) {
                        System.out.println("Enter how much money you want to transfer:");
                        int amount = Integer.parseInt(scanner.nextLine());

                        int balance = getBalance();

                        if (amount < balance) {

                            String sql1 = "update card set balance = balance - ? where number = ?";
                            String sql2 = "update card set balance = balance + ? where number = ?";

                            try (Connection conn = dataSource.getConnection()) {

                                // Disable auto-commit mode
                                conn.setAutoCommit(false);

                                try (PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                                     PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

                                    pstmt1.setInt(1, amount);
                                    pstmt1.setString(2, loggedInCard);
                                    pstmt1.executeUpdate();


                                    pstmt2.setInt(1, amount);
                                    pstmt2.setString(2, receiver);
                                    pstmt2.executeUpdate();



                                    System.out.println("Success!");

                                    conn.commit();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }


                        } else {
                            System.out.println("Not enough money!");
                        }


                    } else {
                        System.out.println("Such a card does not exist.");
                    }


            } else {
                System.out.println("You can't transfer money to the same account!");
            }
        } else {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        }


    }

    private void addIncome() {

        System.out.println("Enter income:");
        int income = Integer.parseInt(scanner.nextLine());

        String sql = "update card set balance = balance + ? where number = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, income);
            pstmt.setString(2, loggedInCard);
            pstmt.executeUpdate();
            System.out.println("Income was added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void balance() {

        int balance = 0;

        String sql = "SELECT balance FROM card WHERE number = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggedInCard);
            ResultSet rs = pstmt.executeQuery();

            balance = rs.getInt(1);

            rs.close();
            System.out.println(balance);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private int getBalance() {

        int balance = 0;

        String sql = "SELECT balance FROM card WHERE number = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggedInCard);
            ResultSet rs = pstmt.executeQuery();

            balance = rs.getInt(1);

            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;

    }


    private void logIn() {
        System.out.println("Enter you card number:");
        String givenCard = scanner.nextLine();

        System.out.println("Enter you PIN:");
        String givenPin = scanner.nextLine();

        Boolean found = false;


        try (final Connection con = dataSource.getConnection();
             final Statement statement = con.createStatement();
             final ResultSet resultSet = statement.executeQuery("SELECT * FROM card")) {
            while (resultSet.next()) {
                if (resultSet.getString("number").equals(givenCard) && resultSet.getString("pin").equals(givenPin)) {
                    found = true;
                    loggedInCard = givenCard;
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (found) {
            System.out.println("You have successfully logged in!\n");

            loggedInMenu();
        } else {
            System.out.println("Wrong card number or PIN!");
        }
    }


    private  void createAccount() {
        Card newCard = new Card();

        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newCard.getCardNumber());
            pstmt.setString(2, newCard.getPin());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        cardList.add(newCard);
        System.out.println("Your card has been created\n" +
                "Your card number:");
        System.out.println(newCard.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(newCard.getPin());
    }

    public static boolean checkLuhn(String ccNumber)
    {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public boolean checkReceiver(String ccNumber)
    {
        boolean found = false;
        try (final Connection con = dataSource.getConnection();
             final Statement statement = con.createStatement();
             final ResultSet resultSet = statement.executeQuery("SELECT * FROM card ")) {
            while (resultSet.next()) {
                if (resultSet.getString("number").equals(ccNumber)) {
                    found = true;
                    break;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;

    }



}
