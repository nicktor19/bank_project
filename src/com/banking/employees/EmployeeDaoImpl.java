package com.banking.employees;

import com.banking.bankaccount.BankAccount;
import com.banking.bankaccount.ViewTransactions;
import com.banking.dbconnection.ConnectionFactory;
import org.jetbrains.annotations.NotNull;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeDaoImpl implements EmployeeDao {

    Connection connection;
    //set final procedures below
    /**
     * Login Employee procedure needs (string email, string password)
     * This procedure already hashes the password. No need to do anything.
     */
    private final String LOGIN_EMPLOYEE = "CALL login_employee(?,?)";

    /**
     * Create employee account needs (String firstname, string email, String password)
     * This procedure already hashes the password. No need to do anything.
     */
    private final String CREATE_EMP_ACCOUNT = "CALL create_emp_account(?,?,?)";

    private final String  BANK_ACCOUNT_PENDING = "CALL bank_account_pending()";

    /**
     * CHANGE_ACCOUNT_STATUS needs (status, employee_id, account number)
     */
    private final String CHANGE_ACCOUNT_STATUS= "CALL change_account_status(?,?,?)";


    public EmployeeDaoImpl() {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public void createEmployee(@NotNull Employee newEmp) {
        try {
            if (doesEmailExist(newEmp.getEmail()) == false) {
                PreparedStatement prepStatement = connection.prepareStatement(CREATE_EMP_ACCOUNT);
                prepStatement.setString(1, newEmp.getFirstName());
                prepStatement.setString(2, newEmp.getEmail());
                prepStatement.setString(3, newEmp.getPassword());
                int count = prepStatement.executeUpdate();
                if (count > 0)
                    System.out.println("Customer Account Created.");
            } else
                System.out.println("Could not create customer account. Please retry.");
        } catch (SQLException e) {
            System.out.println("Sorry, something went wrong." + e.getMessage());
        }
    }

    @Override
    public boolean doesEmailExist(@NotNull String email) {
        try {
            String sql = "SELECT * FROM employees WHERE email=?";
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, email.toLowerCase());
            ResultSet result = prep.executeQuery();
            if (result.next()) {
                System.out.println("That email can't register a new account.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Sorry, something went wrong.");
        }
        return false;
    }

    @Override
    public Employee loginEmployee(@NotNull String eEmail, String ePassword) throws SQLException {
        PreparedStatement prep = connection.prepareStatement(LOGIN_EMPLOYEE);
        prep.setString(1, eEmail.toLowerCase());
        prep.setString(2, ePassword);
        ResultSet result = prep.executeQuery();
        if (result.next())
            return new Employee(result.getInt(1), result.getString(2), result.getString(3));
        return new Employee();
    }

    @Override
    public void bankAccountApproval(Employee emp, String status, int accountNum) {
        //enter bank account number and update the approval SELECT a number
        try {
            PreparedStatement prep = connection.prepareStatement(CHANGE_ACCOUNT_STATUS);
            prep.setString(1, status);
            prep.setInt(2, emp.getId());
            prep.setInt(3, accountNum);
            if (prep.executeUpdate() > 0)
                System.out.println("Account Updated.\n");
            else
                System.out.println("Could not update account.\n");

        } catch (SQLException e) {
            System.out.println("Sorry, Something went wrong.\n");
        }
    }

    @Override
    public int viewAllPendingAcc(Employee emp) {
        try {
            int count = 0; // get max of 5 accounts pending at a time.
            PreparedStatement prep = connection.prepareStatement(BANK_ACCOUNT_PENDING);
            ResultSet result = prep.executeQuery();
            BankAccount b = new BankAccount();
            System.out.println("Only 5 accounts listed at a time.");
            System.out.println("----------------------------------------------------------------------");
            while (result.next() && count < 5) {
                count++;
                b.setAccount_Id(result.getInt(1));
                b.setCustomer_name(result.getString(2));
                b.setAccount_Name(result.getString(3));
                b.setBalance(result.getDouble(4));
                b.setStatus(result.getString(5));
                b.setApprover_Id(result.getInt(6));
                b.setCreated_Date(result.getString(7));
                System.out.println(b.employeeToString(emp));
            }
            return 1;
        } catch (SQLException e) {
            System.out.println("Server Issues. Try again later.");
        }
        return 0;
    }

    @Override
    public void viewCustomersBankAccounts(Employee emp, int accountNum) {
        String sql = "SELECT * FROM bank_accounts WHERE account_id=?";
        try {
            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setInt(1, accountNum);
            ResultSet result = prep.executeQuery();

            if (result.next()) {
                BankAccount ba = new BankAccount();
                ba.setAccount_Id(result.getInt(1));
                ba.setCustomer_name(result.getString(2));
                ba.setAccount_Name(result.getString(3));
                ba.setBalance(result.getDouble(4));
                ba.setStatus(result.getString(5));
                ba.setApprover_Id(result.getInt(6));
                ba.setCreated_Date(result.getString(7));
                System.out.println(ba.employeeToString(emp));
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong.");
        }

    }

    @Override
    public void viewAllTransactions() {
        String sql = "SELECT * FROM transactions";
        try {
            PreparedStatement prep = connection.prepareStatement(sql);
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
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
        }
    }

    @Override
    public boolean shutDownServer() {
        return true;
    }
}
