import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankingSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Account> accounts = new ArrayList<>();

    static int nextAccountNumber = 10001;
    static Account loggedInUser = null;

    static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    static class Account {

        int accountNumber;
        String name;
        String address;
        String contact;
        String password;
        double balance;

        ArrayList<String> transactions = new ArrayList<>();

        Account(int accountNumber, String name, String address,
                String contact, String password, double balance) {

            this.accountNumber = accountNumber;
            this.name = name;
            this.address = address;
            this.contact = contact;
            this.password = password;
            this.balance = balance;
        }

        void addTransaction(String type, double amount) {

            String date = LocalDateTime.now().format(formatter);

            transactions.add(
                date + " | " + type +
                " | Amount: Rs." + amount +
                " | Balance: Rs." + balance
            );
        }
    }

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n================================");
            System.out.println("     BANKING INFORMATION SYSTEM");
            System.out.println("================================");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Fund Transfer");
            System.out.println("6. Check Balance");
            System.out.println("7. Account Management");
            System.out.println("8. Account Statement");
            System.out.println("9. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                registerAccount();
            }
            else if (choice == 2) {
                login();
            }
            else if (choice == 3) {
                deposit();
            }
            else if (choice == 4) {
                withdraw();
            }
            else if (choice == 5) {
                fundTransfer();
            }
            else if (choice == 6) {
                checkBalance();
            }
            else if (choice == 7) {
                accountManagement();
            }
            else if (choice == 8) {
                accountStatement();
            }
            else if (choice == 9) {

                System.out.println(
                    "Thank you for using our banking system!"
                );

                break;
            }
            else {
                System.out.println("Invalid choice!");
            }
        }
    }

    // ================= REGISTER =================

    static void registerAccount() {

        System.out.println("\n===== ACCOUNT REGISTRATION =====");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your address: ");
        String address = sc.nextLine();

        System.out.print("Enter your contact number: ");
        String contact = sc.nextLine();

        System.out.print("Create a password: ");
        String password = sc.nextLine();

        System.out.print("Enter initial deposit amount: ");
        double balance = sc.nextDouble();
        sc.nextLine();

        if (balance < 0) {
            System.out.println("Invalid initial deposit!");
            return;
        }

        Account newAccount = new Account(
                nextAccountNumber,
                name,
                address,
                contact,
                password,
                balance
        );

        accounts.add(newAccount);

        if (balance > 0) {
            newAccount.addTransaction(
                    "Initial Deposit",
                    balance
            );
        }

        System.out.println("\n===== REGISTRATION SUCCESSFUL =====");
        System.out.println("Name           : " + name);
        System.out.println("Address        : " + address);
        System.out.println("Contact        : " + contact);
        System.out.println("Account Number : " + nextAccountNumber);
        System.out.println("Balance        : Rs." + balance);

        nextAccountNumber++;
    }

    // ================= LOGIN =================

    static void login() {

        if (accounts.isEmpty()) {

            System.out.println(
                "\nPlease register an account first."
            );

            return;
        }

        System.out.println("\n===== LOGIN =====");

        System.out.print("Enter account number: ");
        int accNo = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter password: ");
        String enteredPassword = sc.nextLine();

        Account account = findAccount(accNo);

        if (account != null &&
            account.password.equals(enteredPassword)) {

            loggedInUser = account;

            System.out.println("\nLogin successful!");
            System.out.println(
                "Welcome, " + loggedInUser.name
            );
            System.out.println(
                "Current Balance: Rs." +
                loggedInUser.balance
            );

        }
        else {

            System.out.println(
                "\nInvalid account number or password!"
            );
        }
    }

    // ================= FIND ACCOUNT =================

    static Account findAccount(int accountNumber) {

        for (Account account : accounts) {

            if (account.accountNumber == accountNumber) {
                return account;
            }
        }

        return null;
    }

    // ================= DEPOSIT =================

    static void deposit() {

        if (!checkLogin()) {
            return;
        }

        System.out.print("\nEnter deposit amount: ");
        double amount = sc.nextDouble();

        if (amount <= 0) {

            System.out.println(
                "Invalid deposit amount!"
            );

        }
        else {

            loggedInUser.balance += amount;

            loggedInUser.addTransaction(
                "Deposit",
                amount
            );

            System.out.println(
                "Deposit successful!"
            );

            System.out.println(
                "Deposited Amount : Rs." + amount
            );

            System.out.println(
                "New Balance      : Rs." +
                loggedInUser.balance
            );
        }
    }

    // ================= WITHDRAW =================

    static void withdraw() {

        if (!checkLogin()) {
            return;
        }

        System.out.print(
            "\nEnter withdrawal amount: "
        );

        double amount = sc.nextDouble();

        if (amount <= 0) {

            System.out.println(
                "Invalid withdrawal amount!"
            );

        }
        else if (amount > loggedInUser.balance) {

            System.out.println(
                "Insufficient balance!"
            );

        }
        else {

            loggedInUser.balance -= amount;

            loggedInUser.addTransaction(
                "Withdrawal",
                amount
            );

            System.out.println(
                "Withdrawal successful!"
            );

            System.out.println(
                "Withdrawn Amount : Rs." + amount
            );

            System.out.println(
                "Remaining Balance: Rs." +
                loggedInUser.balance
            );
        }
    }

    // ================= FUND TRANSFER =================

    static void fundTransfer() {

        if (!checkLogin()) {
            return;
        }

        System.out.println("\n===== FUND TRANSFER =====");

        System.out.print(
            "Enter recipient account number: "
        );

        int recipientNumber = sc.nextInt();

        System.out.print(
            "Enter transfer amount: "
        );

        double amount = sc.nextDouble();

        Account recipient = findAccount(recipientNumber);

        if (recipient == null) {

            System.out.println(
                "Recipient account not found!"
            );

        }
        else if (
            recipient.accountNumber ==
            loggedInUser.accountNumber
        ) {

            System.out.println(
                "Cannot transfer to the same account!"
            );

        }
        else if (amount <= 0) {

            System.out.println(
                "Invalid transfer amount!"
            );

        }
        else if (amount > loggedInUser.balance) {

            System.out.println(
                "Insufficient balance!"
            );

        }
        else {

            loggedInUser.balance -= amount;

            recipient.balance += amount;

            loggedInUser.addTransaction(
                "Fund Transfer to Account " +
                recipient.accountNumber,
                amount
            );

            recipient.addTransaction(
                "Fund Transfer from Account " +
                loggedInUser.accountNumber,
                amount
            );

            System.out.println(
                "\nFund transfer successful!"
            );

            System.out.println(
                "Recipient Account : " +
                recipient.accountNumber
            );

            System.out.println(
                "Transferred Amount: Rs." +
                amount
            );

            System.out.println(
                "Your Balance      : Rs." +
                loggedInUser.balance
            );

            System.out.println(
                "Recipient Balance : Rs." +
                recipient.balance
            );
        }
    }

    // ================= CHECK BALANCE =================

    static void checkBalance() {

        if (!checkLogin()) {
            return;
        }

        System.out.println(
            "\n===== CHECK BALANCE ====="
        );

        System.out.println(
            "Account Number : " +
            loggedInUser.accountNumber
        );

        System.out.println(
            "Account Holder : " +
            loggedInUser.name
        );

        System.out.println(
            "Current Balance: Rs." +
            loggedInUser.balance
        );
    }

    // ================= ACCOUNT MANAGEMENT =================

    static void accountManagement() {

        if (!checkLogin()) {
            return;
        }

        System.out.println(
            "\n===== ACCOUNT MANAGEMENT ====="
        );

        System.out.println("1. Update Name");
        System.out.println("2. Update Address");
        System.out.println("3. Update Contact");
        System.out.println("4. View Account Details");

        System.out.print("Enter your choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {

            System.out.print(
                "Enter new name: "
            );

            loggedInUser.name = sc.nextLine();

            System.out.println(
                "Name updated successfully!"
            );
        }
        else if (choice == 2) {

            System.out.print(
                "Enter new address: "
            );

            loggedInUser.address =
                sc.nextLine();

            System.out.println(
                "Address updated successfully!"
            );
        }
        else if (choice == 3) {

            System.out.print(
                "Enter new contact number: "
            );

            loggedInUser.contact =
                sc.nextLine();

            System.out.println(
                "Contact updated successfully!"
            );
        }
        else if (choice == 4) {

            System.out.println(
                "\n===== ACCOUNT DETAILS ====="
            );

            System.out.println(
                "Name           : " +
                loggedInUser.name
            );

            System.out.println(
                "Address        : " +
                loggedInUser.address
            );

            System.out.println(
                "Contact        : " +
                loggedInUser.contact
            );

            System.out.println(
                "Account Number : " +
                loggedInUser.accountNumber
            );

            System.out.println(
                "Balance        : Rs." +
                loggedInUser.balance
            );
        }
        else {

            System.out.println(
                "Invalid choice!"
            );
        }
    }

    // ================= ACCOUNT STATEMENT =================

    static void accountStatement() {

        if (!checkLogin()) {
            return;
        }

        System.out.println(
            "\n===== ACCOUNT STATEMENT ====="
        );

        System.out.println(
            "Account Number : " +
            loggedInUser.accountNumber
        );

        System.out.println(
            "Account Holder : " +
            loggedInUser.name
        );

        if (loggedInUser.transactions.isEmpty()) {

            System.out.println(
                "No transactions available."
            );

        }
        else {

            System.out.println(
                "\nTransaction History:"
            );

            for (String transaction :
                    loggedInUser.transactions) {

                System.out.println(transaction);
            }
        }

        System.out.println(
            "\nCurrent Balance: Rs." +
            loggedInUser.balance
        );
    }

    // ================= LOGIN CHECK =================

    static boolean checkLogin() {

        if (loggedInUser == null) {

            System.out.println(
                "\nPlease login first."
            );

            return false;
        }

        return true;
    }
}