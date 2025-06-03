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

        public int getId() { return id; }
        public String getEmail() { return email; }
        public String getNumber() { return number; }
        public String getPin() { return pin; }
        public void setPin(String newPin) { this.pin = newPin; }
    }

    // ArrayList to store users
    private ArrayList<User> users = new ArrayList<>();

    // Register method
    public boolean register(String name, String email, String number, String pin) {
        if (name.isEmpty() || email.isEmpty() || number.isEmpty() || pin.length() != 4) {
            System.out.println("All fields are required and PIN must be 4 digits.");
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

    // Login method
    public int login(String email, String pin) {
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPin().equals(pin)) {
                System.out.println("Login successful. Welcome " + u.name + "!");
                return u.getId();
            }
        }
        System.out.println("Login failed: Incorrect email or PIN.");
        return -1;
    }

    // Change PIN method
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

    // Logout method
    public void logout(int userId) {
        System.out.println("User with ID " + userId + " has logged out.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserAuthentication auth = new UserAuthentication();
        int currentUserId = -1;

        while (true) {
            System.out.println("\n=== GkashApp Terminal ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Change PIN");
            System.out.println("4. Logout");
            System.out.println("5. Exit");
            System.out.println("6. Check Balance");
            System.out.print("Enter option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Mobile Number: ");
                    String number = scanner.nextLine();
                    System.out.print("4-digit PIN: ");
                    String pin = scanner.nextLine();
                    auth.register(name, email, number, pin);
                    break;

                case "2":
                    System.out.print("Email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("PIN: ");
                    String loginPin = scanner.nextLine();
                    currentUserId = auth.login(loginEmail, loginPin);

                    if (currentUserId != -1) {
                        // After successful login
                        CheckBalance check = new CheckBalance();
                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\nWelcome! What would you like to do?");
                            System.out.println("1. Check Balance");
                            System.out.println("2. Logout");
                            System.out.print("Enter option: ");
                            String subOption = scanner.nextLine();

                            switch (subOption) {
                                case "1":
                                    double balance = check.checkBalance(currentUserId);
                                    System.out.println("Your balance is: PHP " + balance);
                                    break;

                                case "2":
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
                    if (currentUserId == -1) {
                        System.out.println("Please login first.");
                        break;
                    }
                    System.out.print("Old PIN: ");
                    String oldPin = scanner.nextLine();
                    System.out.print("New PIN: ");
                    String newPin = scanner.nextLine();
                    auth.changePin(currentUserId, oldPin, newPin);
                    break;

                case "4":
                    if (currentUserId != -1) {
                        auth.logout(currentUserId);
                        currentUserId = -1;
                    } else {
                        System.out.println("No user is currently logged in.");
                    }
                    break;

                case "5":
                    scanner.close();
                    System.out.println("Thank you for using GkashApp!");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
