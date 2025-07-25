package AtmSoftware;

public class Main {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("SUP", "1234", 50000.0);
        ATM atm = new ATM(account);
        atm.start();
    }
}
