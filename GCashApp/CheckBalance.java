/* 
 * Lewis Gerald Cerezo De Leon
 * 2025 CIICC TWSP PRJ NC III B2
*/
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

    // Method to check balance by userId
    public double checkBalance(int userId) {
        for (Balance b : balances) {
            if (b.getUserId() == userId) {
                return b.getAmount();
            }
        }
        return 0.0;
    }

    public ArrayList<Balance> getBalanceList() {
        return balances;
    }

    public void addBalance(double amount, int userId) {
        balances.add(new Balance(amount, userId));
    }

}

