package GCashApp;
import java.util.ArrayList;
import java.util.Date;

public class CashIn {

    static class Transaction {
        private static int idCounter = 1;
        private int id;
        private double amount;
        private String name;
        private int accountId;
        private Date date;
        private Integer transferToID;
        private Integer transferFromID;

        public Transaction(double amount, String name, int accountId, Integer transferToID, Integer transferFromID) {
            this.id = idCounter++;
            this.amount = amount;
            this.name = name;
            this.accountId = accountId;
            this.transferToID = transferToID;
            this.transferFromID = transferFromID;
            this.date = new Date(); // current date
        }

        public String toString() {
            return String.format("Transaction ID: %d | Amount: %.2f | To User ID: %d | Date: %s",
                                  id, amount, accountId, date.toString());
        }
    }

    // Transaction ArrayList
    private ArrayList<Transaction> transactions = new ArrayList<>();

    // Reference to CheckBalance class
    private CheckBalance checkBalance;

    public CashIn(CheckBalance checkBalance) {
        this.checkBalance = checkBalance;
    }

    // Method to cash in
    public void cashIn(double amount, int userId, String name) {
        // Find and update the balance
        boolean found = false;
        for (CheckBalance.Balance b : checkBalance.getBalanceList()) {
            if (b.getUserId() == userId) {
                b.setAmount(b.getAmount() + amount);
                found = true;
                break;
            }
        }

        // Create new if there is no existing balance record
        if (!found) {
            checkBalance.addBalance(amount, userId);
        }

        // Log the transaction
        Transaction transaction = new Transaction(amount, name, userId, null, null);
        transactions.add(transaction);
        System.out.println("Cash-in successful: " + transaction.toString());
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}

