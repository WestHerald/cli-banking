package GCashApp;

import java.util.ArrayList;

public class CheckBalance {
    // Inner class to represent a balance record
    static class Balance {
        private double amount;
        private int userId;

        public Balance(double amount, int userId) {
            this.amount = amount;
            this.userId = userId;
        }

        public int getUserId() {
            return userId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    private ArrayList<Balance> balances = new ArrayList<>();

    // Sample existing user balances
    public CheckBalance() {
        balances.add(new Balance(500.0, 1)); // userID 1
        balances.add(new Balance(1200.0, 2)); // userID 2
        balances.add(new Balance(100.0, 3)); // userID 3
    }

    // Method to check balance by userId
    public double checkBalance(int userId) {
        for (Balance b : balances) {
            if (b.getUserId() == userId) {
                return b.getAmount();
            }
        }
        System.out.println("Balance not found for user ID: " + userId);
        return 0.0;
    }

}

