package com.banking.bankaccount;

import com.banking.customers.Customer;
import com.banking.employees.Employee;

import java.text.DecimalFormat;

public class BankAccount {
    private int account_Id;
    private int customer_Id;
    private String customer_name;
    private String account_Name;
    private double balance;
    private int approver_Id;
    private String approver_name;
    private String status;
    private String created_Date;



    public BankAccount() {}

    public BankAccount(int account_Id, int customer_Id, String account_Name, double balance, int approver_Id, String created_Date) {
        setAccount_Id(account_Id);
        setCustomer_Id(customer_Id);
        setAccount_Name(account_Name);
        setBalance(balance);
        setApprover_Id(approver_Id);
        setCreated_Date(created_Date);
    }

    public String customerToString(Customer c) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return  "" + account_Name + " ( " +
                "Account Number: " + account_Id +
                " | Balance: " +  df.format(balance) +
                " | Status: " + status +
                " | created_Date: " + created_Date +
                " )";
    }

    public String employeeToString(Employee e) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return "" + account_Name + " ( " +
                "Account Number: " + account_Id +
                " | Customer Name/ID: " + customer_name +
                " | Balance: " + df.format(balance) +
                " | Approver Id: " + approver_Id +
                " | Status: " + status +
                " | Created: " + created_Date +
                " )";
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "account_Id=" + account_Id +
                ", customer_Id=" + customer_Id +
                ", account_Name='" + account_Name + '\'' +
                ", balance=" + balance +
                ", approver_Id=" + approver_Id +
                ", status='" + status + '\'' +
                ", created_Date='" + created_Date + '\'' +
                '}';
    }

    public int getAccount_Id() {
        return account_Id;
    }

    public void setAccount_Id(int account_Id) {
        this.account_Id = account_Id;
    }

    public int getCustomer_Id() {
        return customer_Id;
    }

    public void setCustomer_Id(int customer_Id) {
        this.customer_Id = customer_Id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAccount_Name() {
        return account_Name;
    }

    public void setAccount_Name(String account_Name) {
        this.account_Name = account_Name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getApprover_Id() {
        return approver_Id;
    }

    public void setApprover_Id(int approver_Id) {
        this.approver_Id = approver_Id;
    }

    public String getApprover_name() {
        return approver_name;
    }

    public void setApprover_name(String approver_name) {
        this.approver_name = approver_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_Date() {
        return created_Date;
    }

    public void setCreated_Date(String created_Date) {
        this.created_Date = created_Date;
    }


}
