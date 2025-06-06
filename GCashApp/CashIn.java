/* 
 * Lewis Gerald Cerezo De Leon
 * 2025 CIICC TWSP PRJ NC III B2
*/
package GCashApp;

import java.util.ArrayList;
import java.util.Date;

public class CashIn {

    public static class Transaction implements TransactionRecord {
        private int id;
        private double amount;
        private String name;
        private int accountId;
        private Date date;
        private Integer transferToID;
        private Integer transferFromID;

        public Transaction(double amount, String name, int accountId, Integer transferToID, Integer transferFromID) {
            this.id = Transactions.TransactionIdGenerator.getNextId();
            this.amount = amount;
            this.name = name;
            this.accountId = accountId;
            this.transferToID = transferToID;
            this.transferToID = accountId;
            this.date = new Date();
        }

        @Override
        public int getId() {
            return id;
        }

        public int getAccountId() {
            return accountId;
        }

        @Override
        public String toString() {
            return String.format(
                "Transaction ID: %d | Name: %s | Amount: %.2f | From: %s | To: %s | Date: %s",
                id,
                name,
                amount,
                transferFromID == null ? "External" : transferFromID,
                transferToID == null ? accountId : transferToID,
                date.toString()
            );
        }
    }

    // Transaction ArrayList
    private ArrayList<Transaction> transactions = new ArrayList<>();

    private CheckBalance checkBalance;

    public CashIn(CheckBalance checkBalance) {
        this.checkBalance = checkBalance;
    }

    // Method to cash in
    public void cashIn(double amount, int userId, String name) {
        // Find and update balance
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

