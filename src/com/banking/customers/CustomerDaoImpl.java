package com.banking.customers;


import com.banking.bankaccount.BankAccount;
import com.banking.bankaccount.ViewTransactions;
import com.banking.dbconnection.ConnectionFactory;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerDaoImpl implements CustomerDao {

    Connection connection;

    /**
     * Create bank account procedure needs (int account id, string account name, double deposit)
     */
    private final String CREATE_BANK_ACCOUNT = "CALL create_bank_account(?, ?, ?)";
    /**
     * View All customers accounts, needs (int customer id)
     */
    private final String VIEW_CUSTOMER_BANK_ACCOUNTS = "CALL view_customer_bank_accounts(?)";
    /**
     * View selected accounts, needs (int customer_id, int account_number)
     */
    private final String VIEW_SELECTED_BANK_ACCOUNTS = "CALL view_selected_bank_accounts(?,?)";

    /**
     * deposit_transactions needs (account_number, amount, customer_id)
     */
    private final String DEPOSIT_TRANSACTIONS = "CALL deposit_transactions(?,?,?)";
    /**
     * withdraw_transactions needs (account_number, amount, customer_id)
     */
    private final String WITHDRAW_TRANSACTIONS = "CALL withdraw_transactions(?,?,?)";
    /**
     * transfer_to_account needs (from_account_num, to_account_num, amount, customer_id)
     */
    private final String TRANSFER_TO_ACCOUNT = "CALL transfer_to_account(?,?,?,?)";
    /**
     * decide_transfer needs (customer_id, trans_id, status(Approved or Denied))
     */
    private final String DECIDE_TRANSFER = "CAll decide_transfer (?,?,?)";

    /**
     *  transactions_view (String trans_type, String status1, int accNum
     */
    private final String TRANSACTIONS_VIEW = "CALL transactions_view (?,?,?,?)";
    public CustomerDaoImpl() {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public void createCustomer(@NotNull Customer newCustomer) {
        try {
            if (doesEmailExist(newCustomer.getEmail()) == false) {
                String sql = "INSERT INTO customers (firstName, email, password) VALUES (?, ?, SHA2(?, 224))";

                PreparedStatement prepStatement = connection.prepareStatement(sql);
                prepStatement.setString(1, newCustomer.getFirstName());
                prepStatement.setString(2, newCustomer.getEmail());
                prepStatement.setString(3, newCustomer.getPassword());
                int count = prepStatement.executeUpdate();
                if (count > 0)
                    System.out.println("Customer Account Created. Please Proceed to login.");
            } else
                System.out.println("Could not create customer account. Please retry.");
        } catch (SQLException e) {
            System.out.println("Sorry, something went wrong.");
        }
    }

    @Override
    public boolean doesEmailExist(@NotNull String email) throws SQLException {
        String sql = "SELECT * FROM customers WHERE email=?";
        PreparedStatement prepStatement = connection.prepareStatement(sql);
        prepStatement.setString(1, email.toLowerCase());
        ResultSet result = prepStatement.executeQuery();
        if (result.next()) {
            System.out.println("That email can't register a new account.");
            return true;
        }
        return false;
    }

    @Override
    public void createNewBankAccount(@NotNull Customer customer, String accountName, double deposit) {
        System.out.println(accountName);
        System.out.println("$" + deposit);
        BankAccount acc = new BankAccount();
        acc.setCustomer_Id(customer.getId());
        acc.setAccount_Name(accountName);
        acc.setBalance(deposit);
        //DB
        try {
            PreparedStatement prep = connection.prepareStatement(CREATE_BANK_ACCOUNT);
            prep.setInt(1, acc.getCustomer_Id());
            prep.setString(2, acc.getAccount_Name());
            prep.setDouble(3, acc.getBalance());
            if (prep.executeUpdate() > 0) {
                System.out.println("Bank account created.");
            } else {
                System.out.println("Could not create a new bank account at this time.\nPlease try again.");
            }
        } catch (SQLException e) {
            System.out.println("Could not create new Bank Account.\nPlease try again later.");
        }
    }

    @Override
    public Customer loginCustomer(String email, String password) throws SQLException {
        String sql = "SELECT * FROM customers WHERE email=? AND password=Sha2(?,224)";
        try {
            PreparedStatement prepStatement = connection.prepareStatement(sql);
            prepStatement.setString(1, email);
            prepStatement.setString(2, password);
            ResultSet result = prepStatement.executeQuery();
            if (result.next())
                return new Customer(result.getInt(1), result.getString(2), result.getString(3));
        } catch (SQLException e) {
            System.out.println("Sorry, could not Login. Try again.");
        }
        return new Customer();
    }

    @Override
    public void viewBankAccounts(@NotNull Customer c) {
        try {
            PreparedStatement prep = connection.prepareStatement(VIEW_CUSTOMER_BANK_ACCOUNTS);
            prep.setInt(1, c.getId());
            ResultSet result = prep.executeQuery();
            int count = 0;
            List<BankAccount> bankAccountList = new ArrayList<>();

            while (result.next()) {
                BankAccount ba = new BankAccount();
                ba.setAccount_Id(result.getInt(1));
                ba.setAccount_Name(result.getString(2));
                ba.setBalance(result.getDouble(3));
                ba.setStatus(result.getString(4));
                ba.setCreated_Date(result.getString(5));
                bankAccountList.add(ba);
                count = result.getRow();
            }
            int linebreak = 0;

            for (BankAccount print : bankAccountList) {
                if (++linebreak > 1)
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------");
                System.out.println(print.customerToString(c));
            }
            System.out.println("-----------------------");
            System.out.println("Total of '" + count + "' Accounts");
        } catch (SQLException e) {
            System.out.println("Sorry, something went wrong.");
        }
    }

    @Override
    public void viewBankAccount(@NotNull Customer c, int i) {
        try {
            PreparedStatement prep = connection.prepareStatement(VIEW_SELECTED_BANK_ACCOUNTS);
            prep.setInt(1, c.getId());
            prep.setInt(2, i);
            ResultSet result = prep.executeQuery();
            BankAccount ba = new BankAccount();
            while (result.next()) {
                ba.setAccount_Id(result.getInt(1));
                ba.setAccount_Name(result.getString(2));
                ba.setBalance(result.getDouble(3));
                ba.setStatus(result.getString(4));
                ba.setCreated_Date(result.getString(5));
            }
            if (ba.getAccount_Id() != 0)
                System.out.println(ba.customerToString(c));
            else
                System.out.println("Looks like you entered a wrong Account Number.");
        } catch (SQLException e) {
            System.out.println("Sorry, Something went wrong.");
        }
    }


    @Override
    public void transferFromAccToAcc(Customer c, double amount, int fromAccountNum, int toAccountNum) {
        if (amount > (double) 0){
            try{
                CallableStatement prep = connection.prepareCall(TRANSFER_TO_ACCOUNT);
                prep.setInt(1, fromAccountNum);
                prep.setInt(2, toAccountNum);
                prep.setDouble(3, amount);
                prep.setInt(4, c.getId());
                if (prep.executeUpdate() > 0) {
                    System.out.println("Transfer Completed.");
                } else{
                    System.out.println("Could not complete transfer transactions.");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else
            System.out.println("Invalid Amount.");

    }

    @Override
    public void viewTransactionsForAccount(@NotNull Customer c, String transType, String status, int accNum) {
        try{
            CallableStatement prep = connection.prepareCall(TRANSACTIONS_VIEW);
            prep.setString(1, transType);
            prep.setString(2, status);
            prep.setInt(3, accNum);
            prep.setInt(4, c.getId());
            ResultSet result = prep.executeQuery();

            while (result.next()) {
                ViewTransactions trans = new ViewTransactions();
                trans.setTransId(result.getInt(1));
                trans.setFromAcc(result.getInt(2));
                trans.setAmount(result.getDouble(3));
                trans.setToAcc(result.getInt(4));
                trans.setTransType(result.getString(5));
                trans.setStatus(result.getString(6));
                trans.setDate(result.getString(7));
                System.out.println(trans.toString());
            }

        }catch (SQLException e) {
            System.out.println("Could not complete transfer transactions." + e);
        }
    }

    @Override
    public void decideTransfer(@NotNull Customer c, int transId, String status) {
        try{
            //account number is the transaction_id
            CallableStatement prep = connection.prepareCall(DECIDE_TRANSFER);
            prep.setInt(1, c.getId());
            prep.setInt(2, transId); // transaction
            prep.setString(3, status);
            if (prep.executeUpdate() > 0)
                System.out.println("That transaction was "+ status +".");
        } catch (SQLException e) {
            System.out.println("Could not complete transfer transactions.");
        }
    }

    @Override
    public void withdrawFromAccount(Customer c, double amount, int accNum) {
        if (amount > (double) 0) {
            try{
                CallableStatement prep = connection.prepareCall(WITHDRAW_TRANSACTIONS);
                prep.setInt(1, accNum);
                prep.setDouble(2, amount);
                prep.setInt(3, c.getId());
                if (prep.executeUpdate() > 0)
                    System.out.println("$"+ amount + " was Withdrawn from Bank Account "+ accNum +".");
                else
                    System.out.println("Could not withdraw...");
            }catch (SQLException e) {
                System.out.println("Could not complete transfer transactions.");// change
            }
        } else
            System.out.println("Invalid Amount.");
    }

    @Override
    public void depositIntoAccount(Customer c, double amount, int accNum) {
        if (amount > (double) 0) {
            try{
                CallableStatement prep = connection.prepareCall(DEPOSIT_TRANSACTIONS);
                prep.setInt(1, accNum);
                prep.setDouble(2, amount);
                prep.setInt(3, c.getId());
                if (prep.executeUpdate() > 0)
                    System.out.println("$"+ amount + " was Deposited into Bank Account "+ accNum +".");
                else
                    System.out.println("Could not be Deposited.");
            }catch (SQLException e) {
                System.out.println("Could not complete transfer transactions.");// change
            }
        } else
            System.out.println("Invalid Amount.");
    }

}
