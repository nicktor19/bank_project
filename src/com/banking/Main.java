package com.banking;

import com.banking.customers.Customer;
import com.banking.customers.CustomerDao;
import com.banking.customers.CustomerDaoFactory;
import com.banking.customers.CustomerDaoImpl;
import com.banking.employees.Employee;
import com.banking.employees.EmployeeDao;
import com.banking.employees.EmployeeDaoFactory;
import com.banking.employees.EmployeeDaoImpl;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.ls.LSOutput;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.Scanner;



public class Main {

    static boolean shutDown = false;

    public static void main(String[] args) {
        Customer loggedCustomer = new Customer();
        Employee loggedEmployee = new Employee();

        while (!shutDown) { // whole interface.
            boolean flag = true;
            if (loggedCustomer.isLoggedIn() == false || loggedEmployee.isLoggedIn() == false) {
                while (flag) {
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nSelect the numeric values below:\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("1. Login\n2. Register new Customer");
                    System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n-> ");
                    try {
                        switch (getUserInt()) {
                            case 1:
                                boolean loginFlag = true;
                                while (loginFlag) {
                                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nEnter a Numeric values below:\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                                    System.out.println("1. Customer Login\n2. Employee Login\n*. Go back (Enter anything)");
                                    System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n-> ");
                                    switch (getUserInt()) {
                                        case 1:
                                            System.out.println("=-=-=-=-=-=-=-=\nCUSTOMER LOGIN\n=-=-=-=-=-=-=-=");
                                            System.out.print("Enter Account Email:\n-> ");
                                            String email = getUserString().toLowerCase();
                                            System.out.print("\nEnter password:\n-> ");
                                            String password = getUserString();
                                            CustomerDao dao = new CustomerDaoImpl();
                                            try {
                                                loggedCustomer = dao.loginCustomer(email, password);
                                                if (loggedCustomer.isLoggedIn()) {
                                                    loginFlag = false;
                                                    flag = false;
                                                } else {
                                                    System.out.println("Incorrect Login, please try again.");
                                                }
                                                break;
                                            } catch (SQLException e) {
                                                System.out.println("Something went wrong, please try logging in again.");
                                            }
                                            break;
                                        case 2:
                                            //employee login!should be here
                                            System.out.println("#~#~#~#~#~#~#~#~#\nEmployee Login\n#~#~#~#~#~#~#~#~#");
                                            System.out.print("Enter Account Email:\n-> ");
                                            String eEmail = getUserString().toLowerCase();
                                            System.out.print("\nEnter password:\n-> ");
                                            String ePassword = getUserString();
                                            EmployeeDao empDao = new EmployeeDaoImpl();
                                            try {
                                                loggedEmployee = empDao.loginEmployee(eEmail, ePassword);
                                                if (loggedEmployee.isLoggedIn()) {
                                                    loginFlag = false;
                                                    flag = false;
                                                } else {
                                                    System.out.println("Incorrect Login, please try again.");
                                                }
                                            } catch (SQLException e) {
                                                System.out.println("Something went wrong, please try logging in again.");
                                            }
                                            break;
                                        default:
                                            System.out.println("Taking you back home....\n");
                                            loginFlag = false;
                                            break;
                                    }
                                }
                                break;
                            case 2:// register customer.
                                try {
                                    Customer newCustomer = userSignup();
                                    //create new customer:
                                    CustomerDao dao = CustomerDaoFactory.getCustomerDao();
                                    dao.createCustomer(newCustomer);
                                } catch (Exception e) {
                                    System.out.println("Please complete form again, something went wrong.\n");
                                }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a number:\n");
                    }
                }
            }
/**
 * CUSTOMER LOGGED IN!
 */
            while (loggedCustomer.isLoggedIn()) {
                CustomerDao tools = new CustomerDaoImpl();
                System.out.println("****************************************");
                System.out.println("* Welcome " + loggedCustomer.getFirstName() + ", this is your DashBoard: *");
                System.out.println("****************************************");
                System.out.println("Enter a Numeric value from below:");
                System.out.println("1. View your bank accounts & Transactions ->");
                System.out.println("2. Select bank account to view->");
                System.out.println("3. Create New Bank Account->");
                System.out.println("4. Make New Transactions (Withdrawal, Deposit, and transfers)-> ");
                System.out.println("5. LogOut->");
                System.out.print("****************************************\n-> ");
                //another interface using switches.
                try {
                    switch (getUserInt()) {
                        case 1:
                            //view bank accounts...
                            tools.viewBankAccounts(loggedCustomer);
                            //another interface to view all transactions
                            System.out.print("Select bank account # to check all transactions.\n->");
                            int accNum1 = getUserInt();
                            tools.viewTransactionsForAccount(loggedCustomer, "All", "All", accNum1);

                            break;
                        case 2:
                            //view selected account...
                            try {
                                System.out.print("\nEnter Account Number:\n-> ");
                                tools.viewBankAccount(loggedCustomer, getUserInt());
                            } catch (InputMismatchException e) {
                                System.out.println("Account number must be of integer value\n");
                            }
                            break;
                        case 3:
                            //create a new bank account
                            System.out.println("***************************");
                            System.out.println("Setup New Bank Account");
                            System.out.println("***************************");
                            System.out.print("Set Bank Account name:\n-> ");
                            String accName = getUserString();
                            System.out.println();
                            boolean dFlag = true;
                            while (dFlag) {
                                System.out.print("How much would you like to deposit?\n $ ");
                                try {
                                    double deposit = getUserDouble();
                                    if (deposit >= 0)
                                        tools.createNewBankAccount(loggedCustomer, accName, deposit);
                                    else
                                        System.out.println("Deposit can only be a positive number, or zero.\n");
                                    dFlag = false;
                                } catch (InputMismatchException e) {
                                    System.out.println("Deposit has to be numeric.\n");
                                }
                            }
                            break;
                        case 4:
                            //transactions
                            System.out.println("***************************");
                            System.out.println("Select # below:\nNot: Only Approved Accounts can use these options.");
                            System.out.println("***************************");
                            System.out.println("1. Withdraw");
                            System.out.println("2. Deposit");
                            System.out.println("3. Transfer");
                            System.out.println("4. Accept/Deny Transfers");
                            System.out.println("***************************");
                            try{
                                int accNum;
                                int toAccNum;
                                double amount;
                                System.out.print("Transaction Selection#: \n-> ");
                                switch (getUserInt()){
                                    case 1:
                                        tools.viewBankAccounts(loggedCustomer); //view all this customers bank_accounts
                                        System.out.println("Select your bank Account.");
                                        accNum = getUserInt();
                                        System.out.print("Withdraw Amount?\n$");
                                        amount = getUserDouble();
                                        tools.withdrawFromAccount(loggedCustomer, amount, accNum);
                                        break;
                                    case 2:
                                        tools.viewBankAccounts(loggedCustomer); //view all this customers bank_accounts
                                        System.out.println("Select your bank Account.");
                                        accNum = getUserInt();
                                        System.out.print("Deposit Amount?\n$");
                                        amount = getUserDouble();
                                        tools.depositIntoAccount(loggedCustomer, amount, accNum);
                                    case 3:
                                        tools.viewBankAccounts(loggedCustomer); //view all this customers bank_accounts
                                        System.out.print("Enter bank account to transfer from:\n-> ");
                                        accNum = getUserInt(); //acts as from account
                                        System.out.print("Enter bank account to transfer to: (Can be internal, external)\n-> ");
                                        toAccNum = getUserInt();
                                        System.out.print("Enter amount you would like to transfer:\n-> $");
                                        amount = getUserDouble();

                                        tools.transferFromAccToAcc(loggedCustomer, amount, accNum, toAccNum);
                                        break;
                                    case 4:
                                        int transId;
                                        String status = null;
                                        //view all transactions PENDING Not enough time to perfect it

                                        //get inputs
                                        System.out.print("What is the transfer ID #\n-> ");
                                        transId = getUserInt();
                                        System.out.print("Enter 1.Approve or 2.Deny?\n-># ");
                                        switch (getUserInt()) {
                                            case 1:
                                                status = "Approved";
                                                break;
                                            case 2:
                                                status = "Denied";
                                                break;
                                        }
                                        if (status.equals("Approved") || status.equals("Denied")) {
                                        tools.decideTransfer(loggedCustomer, transId, status);
                                        } else
                                            System.out.println("You entered invalid response to status.\nPlease try again.");
                                        break;
                                }
                            } catch (InputMismatchException e){
                                System.out.println("Submit only numbers.");
                            }
                            break;
                        default:
                            //logout.
                            loggedCustomer.setLoggedIn(false);
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Only Numeric values allowed, try again.\n");
                }
            }
/**
 * EMPLOYEE LOGGED IN!
 */
            while (loggedEmployee.isLoggedIn()) {
                EmployeeDao empDao = EmployeeDaoFactory.getEmployeeDao();
                System.out.println("-------------------------------------------");
                System.out.println("| Welcome " + loggedEmployee.getFirstName() + ", to your DashBoard.    |");
                System.out.println("-------------------------------------------");
                System.out.println("1. Pending Accounts");
                System.out.println("2. View Selected Bank Accounts:");
                System.out.println("3. View All transactions...");
                System.out.println("8. Create new Employee Account");
                System.out.println("9. Logout");
                System.out.println("Press any letter to shutdown the server...");
                System.out.println("-------------------------------------------");
                System.out.print("}> ");
                try {

                    switch (getUserInt()) {
                        case 1:
                            // Pending accounts->will automatically check all pending
                            boolean statusFlag = true;
                            while (statusFlag) {
                                String status = null;
                                int accNum;
                                if (empDao.viewAllPendingAcc(loggedEmployee) > 0) {
                                    // give options-> 1. Approve #account number  , go back
                                    System.out.println("Change Account STATUS\n-------------------------------------");
                                    System.out.println("SELECT Status #");
                                    System.out.print("1. Approved | 2. Denied | 3. Closed | 4. Blocked\n-> ");
                                    switch (getUserInt()) {
                                        case 1:
                                            status = "Approved";
                                            break;
                                        case 2:
                                            status = "Denied";
                                            break;
                                        case 3:
                                            status = "Closed";
                                            break;
                                        case 4:
                                            status = "Blocked";
                                            break;
                                        default:
                                            System.out.println("You entered an Incorrect number.");
                                            break;
                                    }
                                    System.out.print("SELECT account number:\n-> ");
                                    accNum = getUserInt();
                                    if (status != null && accNum > 0) {
                                        empDao.bankAccountApproval(loggedEmployee,  status, accNum);
                                        statusFlag = false;
                                    }
                                } else {
                                    statusFlag = false;
                                }
                            }
                            break;
                        case 2:
                            System.out.print("Enter an Account Number:\n-> ");
                            empDao.viewCustomersBankAccounts(loggedEmployee, getUserInt());
                            break;
                        case 3:
                            empDao.viewAllTransactions();
                            break;
                        case 8:
                            System.out.println("Register a new employee.");
                            try {
                                Employee newEmployee = employeeSignup();
                                //create new customer:
                                empDao.createEmployee(newEmployee);
                            } catch (Exception e) {
                                System.out.println("Please complete form again, something went wrong.\n");
                            }
                            break;
                        case 9://logout
                            loggedEmployee.setLoggedIn(false);
                            break;
                    }
                } catch (InputMismatchException e) {
                    //shut down make confirmations!!!
                    System.out.print("Are you sure you want to shutdown the Banking Server?\nConfirm by entering SHUTDOWN:\n}> ");
                    if (getUserString().toUpperCase().equals("SHUTDOWN")){
                        System.out.println();
                        loggedEmployee.setLoggedIn(false);
                        shutDown = empDao.shutDownServer();
                    }
                }
            }
/**
 * Logout done here
 */
            if (loggedCustomer.getId() > 0 && loggedCustomer.isLoggedIn() == false) {
                System.out.println("Thank you "+ loggedCustomer.getFirstName() +" for banking with us!\n");
                loggedCustomer = new Customer();

            }
            if (loggedEmployee.getId() > 0 && loggedEmployee.isLoggedIn() == false) {

                System.out.println("Thank you "+ loggedEmployee.getFirstName() +" for your time today!\n");
                loggedEmployee = new Employee();
            }
        }
    }

    public static double getUserDouble() {
        Scanner scan = new Scanner(System.in);
        return scan.nextDouble();
    }

    public static String getUserString() {
        Scanner scan = new Scanner(System.in);
        return scan.next();
    }

    public static int getUserInt() {
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
    }

    public static @Nullable Customer userSignup() {
        boolean passFlag = true;
        System.out.println("Please Enter your First Name: ");
        String firstName = getUserString();
        System.out.println("Please Enter your Email: ");
        String email = getUserString();
        while (passFlag) {
            System.out.println("Please Enter your Password: ");
            String password = getUserString();
            System.out.println("Re-enter your Password for confirmation: ");
            String password2 = getUserString();

            if (!password.equals(password2))
                System.out.println("Passwords don't match; please re-enter passwords.");
            else {
                return new Customer((firstName.substring(0, 1).toUpperCase() + firstName.substring(1)), email.toLowerCase(), password);
            }
        }
        return null;
    }
    public static @Nullable Employee employeeSignup() {
        boolean passFlag = true;
        System.out.println("Please Enter your First Name: ");
        String firstName = getUserString();
        System.out.println("Please Enter your Email: ");
        String email = getUserString();
        while (passFlag) {
            System.out.println("Please Enter your Password: ");
            String password = getUserString();
            System.out.println("Re-enter your Password for confirmation: ");
            String password2 = getUserString();

            if (!password.equals(password2))
                System.out.println("Passwords don't match; please re-enter passwords.");
            else {
                return new Employee((firstName.substring(0, 1).toUpperCase() + firstName.substring(1)), email.toLowerCase(), password);
            }
        }
        return null;
    }
}
