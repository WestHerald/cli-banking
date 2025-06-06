/* 
 * Lewis Gerald Cerezo De Leon
 * 2025 CIICC TWSP PRJ NC III B2
*/
package GCashApp;

import java.util.ArrayList;
import java.util.Scanner;

public class UserAuthentication {

    static class User {
        private static int idCounter = 1;
        private int id;
        private String name;
        private String email;
        private String number;
        private String pin;

        public User(String name, String email, String number, String pin) {
            this.id = idCounter++;
            this.name = name;
            this.email = email;
            this.number = number;
            this.pin = pin;
        }

        public String getName() { return name; }
        public int getId() { return id; }
        public String getEmail() { return email; }
        public String getNumber() { return number; }
        public String getPin() { return pin; }
        public void setPin(String newPin) { this.pin = newPin; }
    }

    private ArrayList<User> users = new ArrayList<>();

    public boolean register(String name, String email, String number, String pin) {
        if (name.isEmpty() || email.isEmpty() || number.isEmpty() || pin.length() != 4) {
            System.out.println("All fields are required and PIN must be 4 digits.");
            return false;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("Invalid email format. Please enter a valid email address.");
            return false;
        }

        if (!number.matches("^09\\d{9}$")) {
            System.out.println("Invalid mobile number. It must start with '09' and contain exactly 11 digits.");
            return false;
        }

        for (User u : users) {
            if (u.getEmail().equals(email) || u.getNumber().equals(number)) {
                System.out.println("User with this email or number already exists.");
                return false;
            }
        }

        users.add(new User(name, email, number, pin));
        System.out.println("User registered successfully.");
        return true;
    }

    public User login(String email, String pin) {
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPin().equals(pin)) {
                return u;
            }
        }
        System.out.println("Login failed: Incorrect email or PIN.");
        return null;
    }

    public boolean changePin(int userId, String oldPin, String newPin) {
        for (User u : users) {
            if (u.getId() == userId) {
                if (!u.getPin().equals(oldPin)) {
                    System.out.println("Incorrect old PIN.");
                    return false;
                }
                if (newPin.length() != 4) {
                    System.out.println("New PIN must be 4 digits.");
                    return false;
                }
                u.setPin(newPin);
                System.out.println("PIN changed successfully.");
                return true;
            }
        }
        System.out.println("User not found.");
        return false;
    }

    public void logout(int userId) {
        System.out.println("User with ID " + userId + " has logged out.");
    }

    public boolean userExists(int userId) {
        for (User u : users) {
            if (u.getId() == userId) return true;
        }
        return false;
    }

    public String getUserNameById(int userId) {
    for (User u : users) {
        if (u.getId() == userId) {
            return u.getName();
        }
    }
    return "Unknown";
}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserAuthentication auth = new UserAuthentication();

        CheckBalance checkBalance = new CheckBalance();
        CashIn cashIn = new CashIn(checkBalance);
        CashTransfer transfer = new CashTransfer(checkBalance, auth);
        Transactions transactions = null;

        int currentUserId = -1;

        System.out.println("Lewis Gerald Cerezo De Leon");
        System.out.println("2025 CIICC TWSP PRJ NC III B2");

        while (true) {
            System.out.println("\n=== GkashApp Terminal ===");
            System.out.println("1. Register New Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("\n=== New Account Registration ===");
                    System.out.print("Enter your Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter your Email: ");
                    String email = scanner.nextLine().toLowerCase();
                    System.out.print("Enter your Mobile Number: ");
                    String number = scanner.nextLine();
                    System.out.print("Enter 4-digit PIN: ");
                    String pin = scanner.nextLine();
                    auth.register(name, email, number, pin);
                    break;

                case "2":
                    System.out.println("\n=== GkashApp Login ===");
                    System.out.print("Email: ");
                    String loginEmail = scanner.nextLine().toLowerCase();
                    System.out.print("PIN: ");
                    String loginPin = scanner.nextLine();
                    User loggedInUser = auth.login(loginEmail, loginPin);
                    if (loggedInUser != null) {
                        currentUserId = loggedInUser.getId();
                        transactions = new Transactions(cashIn.getTransactions(), transfer.getTransactions());
                        System.out.println("=== Login successful. ===");

                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\n=== Welcome " + loggedInUser.getName() + "! What would you like to do? ===");
                            System.out.println("1. Cash In");
                            System.out.println("2. Check Balance");
                            System.out.println("3. Transfer Money");
                            System.out.println("4. View Transaction History");
                            System.out.println("5. Change 4-digit PIN");
                            System.out.println("6. Logout");
                            System.out.print("Enter option: ");
                            String subOption = scanner.nextLine();

                            switch (subOption) {
                                case "1":
                                System.out.println("\n=== Cash In ===");
                                    System.out.print("Enter amount to cash in: ");
                                    double amount;
                                    try {
                                        amount = Double.parseDouble(scanner.nextLine());
                                        if (amount <= 0) {
                                            System.out.println("Amount must be greater than zero.");
                                            break;
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid amount.");
                                        break;
                                    }
                                    cashIn.cashIn(amount, currentUserId, loggedInUser.getName());
                                    break;

                                case "2":
                                    double balance = checkBalance.checkBalance(currentUserId);
                                    System.out.println("\n=== Check Balance ===");
                                    System.out.println("Your balance is: PHP " + balance);
                                    break;

                                case "3":
                                    System.out.println("\n=== Transfer Money ===");
                                    System.out.print("Enter recipient user ID: ");
                                    String receiverInput = scanner.nextLine().trim();
                                    if (receiverInput.isEmpty()) {
                                        System.out.println("Recipient user ID cannot be empty.");
                                        break;
                                    }
                                    int receiverId;
                                    try {
                                        receiverId = Integer.parseInt(receiverInput);
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input. Please enter a numeric User ID.");
                                        break;
                                    }
                                    System.out.print("Enter amount to transfer: ");
                                    String amountInput = scanner.nextLine().trim();
                                    if (amountInput.isEmpty()) {
                                        System.out.println("Amount cannot be empty.");
                                        break;
                                    }
                                    double transferAmount;
                                    try {
                                        transferAmount = Double.parseDouble(amountInput);
                                        if (transferAmount <= 0) {
                                            System.out.println("Amount must be greater than zero.");
                                            break;
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input. Please enter a numeric amount.");
                                        break;
                                    }
                                    transfer.transfer(currentUserId, receiverId, transferAmount);
                                    break;

                                case "4":
                                    System.out.println("\n=== Transaction History ===");
                                    System.out.println("Select type of transactions that you wanted to see:");
                                    System.out.println("1. View All Transactions");
                                    System.out.println("2. View My Transactions");
                                    System.out.println("3. Search by Transaction ID");
                                    System.out.print("Choose option: ");
                                    String histOption = scanner.nextLine();

                                    switch (histOption) {
                                        case "1":
                                            transactions = new Transactions(cashIn.getTransactions(), transfer.getTransactions());
                                            System.out.println("\n=== All Transaction History ===");
                                            transactions.viewAll();
                                            break;
                                        case "2":
                                            transactions = new Transactions(cashIn.getTransactions(), transfer.getTransactions());
                                            System.out.println("\n=== My Transaction History ===");
                                            transactions.viewUserAll(currentUserId);
                                            break;
                                        case "3":
                                            System.out.print("Enter Transaction ID: ");
                                            try {
                                                int txId = Integer.parseInt(scanner.nextLine());
                                                transactions = new Transactions(cashIn.getTransactions(), transfer.getTransactions());
                                                System.out.println("\n=== Via Search Transaction History ===");
                                                transactions.viewTransaction(txId);
                                            } catch (NumberFormatException e) {
                                                System.out.println("Invalid Transaction ID.");
                                            }
                                            break;
                                        default:
                                            System.out.println("Invalid history option.");
                                    }
                                    break;

                                case "5":
                                    System.out.println("\n=== Change 4-digit PIN ===");
                                    System.out.print("Old PIN: ");
                                    String oldPin = scanner.nextLine();
                                    System.out.print("New PIN: ");
                                    String newPin = scanner.nextLine();
                                    auth.changePin(currentUserId, oldPin, newPin);
                                    break;

                                case "6":
                                    auth.logout(currentUserId);
                                    currentUserId = -1;
                                    loggedIn = false;
                                    break;

                                default:
                                    System.out.println("Invalid option.");
                            }
                        }
                    }
                    break;

                case "3":
                    scanner.close();
                    System.out.println("\nThank you for using GkashApp!");
                    System.out.println("=== End of Program ===");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}