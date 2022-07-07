package com.banking.bankaccount;

public class ViewTransactions {
    public int transId;
    public int fromAcc;
    public double amount;
    public int toAcc;
    public String transType;
    public String status;
    public String date;

    @Override
    public String toString() {
        return "Transfer ID:" + transId +
                " | From Account: " + fromAcc +
                " | Transfer Amount: $" + amount +
                " | To Account: " + toAcc +
                " | Transfer Type: " + transType +
                " | Status: " + status +
                " | Date: " + date +
                " |";
    }
    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public int getFromAcc() {
        return fromAcc;
    }

    public void setFromAcc(int fromAcc) {
        this.fromAcc = fromAcc;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getToAcc() {
        return toAcc;
    }

    public void setToAcc(int toAcc) {
        this.toAcc = toAcc;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
