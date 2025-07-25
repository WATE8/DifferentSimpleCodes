package CorporateEmailAddressGenerator;

import java.util.Random;

public class EmailGenerator {
    private static final String COMPANY = "corp.com";
    private static final int PASSWORD_LENGTH = 8;

    public static String generateEmail(String firstName, String lastName, String department) {
        return lastName.toLowerCase() + "." + firstName.toLowerCase() + "@" + department.toLowerCase() + "." + COMPANY;
    }

    public static String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        Random rand = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return password.toString();
    }
}
