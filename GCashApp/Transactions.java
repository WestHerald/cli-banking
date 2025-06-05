package GCashApp;

import java.util.ArrayList;

public class Transactions{
    private ArrayList<TransactionRecord> allTransactions;

    public Transactions(ArrayList<CashIn.Transaction> transactionsFromCashIn,
                        ArrayList<CashTransfer.TransferTransaction> transactionsFromCashTransfer) {
        allTransactions = new ArrayList<>();
        allTransactions.addAll(transactionsFromCashIn);
        allTransactions.addAll(transactionsFromCashTransfer);
    }

    public void viewAll() {
        if (allTransactions.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }

        for (TransactionRecord t : allTransactions) {
            System.out.println(t.toString());
        }
    }

    public void viewUserAll(int userId) {
        boolean found = false;
        for (TransactionRecord t : allTransactions) {
            if (t instanceof CashIn.Transaction) {
                CashIn.Transaction tx = (CashIn.Transaction) t;
                if (tx.getAccountId() == userId) {
                    System.out.println(tx.toString());
                    found = true;
                }
            } else if (t instanceof CashTransfer.TransferTransaction) {
                CashTransfer.TransferTransaction tx = (CashTransfer.TransferTransaction) t;
                if (tx.getSenderId() == userId || tx.getReceiverId() == userId) {
                    System.out.println(tx.toString());
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No transactions found for User ID: " + userId);
        }
    }

    public void viewTransaction(int transactionId) {
        for (TransactionRecord t : allTransactions) {
            if (t.getId() == transactionId) {
                System.out.println(t.toString());
                return;
            }
        }
        System.out.println("Transaction ID " + transactionId + " not found.");
    }

    // Shared transaction ID generator
    public static class TransactionIdGenerator {
        private static int idCounter = 1;

        public static int getNextId() {
            return idCounter++;
        }
    }
}
