package com.banking.customers;

import com.banking.bankaccount.BankAccount;

import java.sql.SQLException;

public interface CustomerDao {

    void createCustomer(Customer newCustomer) throws SQLException; //create new customers

    boolean doesEmailExist(String email) throws SQLException;
    void createNewBankAccount(Customer customer, String accountName, double deposit); // create new saving or checking account.

    Customer loginCustomer(String email, String password) throws SQLException;
    void viewBankAccounts(Customer c);
    void viewBankAccount(Customer c, int i);

    void viewTransactionsForAccount(Customer c, String transType, String status, int accNum);

    void transferFromAccToAcc(Customer c, double amount, int fromAccountNum, int toAccountNum );
    void decideTransfer(Customer c, int transId, String status);
    void depositIntoAccount(Customer c, double amount, int accNum);
    void withdrawFromAccount(Customer c, double amount, int accNum);

}
