package GCashApp;

import java.util.Date;
import java.util.ArrayList;

public class CashTransfer {
    private CheckBalance checkBalance;
    private ArrayList<String> logs = new ArrayList<>();
    private UserAuthentication auth;
    private ArrayList<TransferTransaction> transferTransactions = new ArrayList<>();

    public ArrayList<TransferTransaction> getTransactions() {
        return transferTransactions;
    }
    
    public CashTransfer(CheckBalance checkBalance, UserAuthentication auth) {
        this.checkBalance = checkBalance;
        this.auth = auth;
    }

    public void transfer(int senderId, int receiverId, double amount) {
        if (senderId == receiverId) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Transfer amount must be greater than zero.");
            return;
        }

        CheckBalance.Balance senderBalance = null;
        CheckBalance.Balance receiverBalance = null;

        for (CheckBalance.Balance b : checkBalance.getBalanceList()) {
            if (b.getUserId() == senderId) senderBalance = b;
            if (b.getUserId() == receiverId) receiverBalance = b;
        }

        if (senderBalance == null || senderBalance.getAmount() < amount) {
            System.out.println("Insufficient balance! Please Cash In and try Transfer Money again when you have enough balance.");
            return;
        }

        if (!auth.userExists(receiverId)) {
            System.out.println("Transfer failed: Receiver ID does not exist.");
            return;
        }

        if (receiverBalance == null) {
            // Receiver has no balance record yet, create one
            checkBalance.addBalance(0.0, receiverId);
            // Retrieve the newly created balance
            for (CheckBalance.Balance b : checkBalance.getBalanceList()) {
                if (b.getUserId() == receiverId) {
                    receiverBalance = b;
                    break;
                }
            }
        }

        // Add the transferred amount
        receiverBalance.setAmount(receiverBalance.getAmount() + amount);
        senderBalance.setAmount(senderBalance.getAmount() - amount);

        String log = String.format("Transferred PHP %.2f from User %d to User %d on %s",
                amount, senderId, receiverId, new Date());
        logs.add(log);

        System.out.println("Transfer successful.");
        System.out.println(log);

        String senderName = auth.getUserNameById(senderId);
        String receiverName = auth.getUserNameById(receiverId);
        TransferTransaction tx = new TransferTransaction(amount, senderId, receiverId, senderName, receiverName);

        logs.add(tx.toString());
        transferTransactions.add(tx);

    }

    public ArrayList<String> getLogs() {
        return logs;
    }

    public static class TransferTransaction implements TransactionRecord {
        private int id;
        private double amount;
        private int senderId;
        private int receiverId;
        private String senderName;
        private String receiverName;
        private Date date;

        public TransferTransaction(double amount, int senderId, int receiverId, String senderName, String receiverName) {
            this.id = Transactions.TransactionIdGenerator.getNextId();
            this.amount = amount;
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.senderName = senderName;
            this.receiverName = receiverName;
            this.date = new Date();
        }

        public int getId() {
            return id;
        }

        public int getSenderId() {
            return senderId;
        }

        public int getReceiverId() {
            return receiverId;
        }

        @Override
        public String toString() {
            return String.format("Transaction ID: %d | Sender: %s | Receiver: %s | Amount: %.2f | Date: %s", 
                                              id,      senderName,  receiverName,  amount,        date.toString());
        }
    }
}