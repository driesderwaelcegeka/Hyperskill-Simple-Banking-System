package banking;

import java.util.Random;

public class Card {
    String cardNumber;
    String pin;
    int balance;

    Random random = new Random();

    public Card() {

        String generatedNumbers = "";
        for (int i=0; i < 9; i++) {
            generatedNumbers += random.nextInt(9);
        }
        int checkDigit = getCheckDigit("400000" + generatedNumbers);
        this.cardNumber = "400000" + generatedNumbers + checkDigit;

        String generatedPin = "";

        for (int i=0; i < 4; i++) {
            generatedPin += random.nextInt(9);
        }

        this.pin = generatedPin;

        this.balance = 0;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    private int getCheckDigit(String number) {

        int sum = 0;
        for (int i = 0; i < number.length(); i++) {

            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }
        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }
}
