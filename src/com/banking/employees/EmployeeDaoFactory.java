package com.banking.employees;

import com.banking.customers.CustomerDao;
import com.banking.customers.CustomerDaoImpl;

public class EmployeeDaoFactory {
    public static EmployeeDao empDao;

    private EmployeeDaoFactory(){}

    public static EmployeeDao getEmployeeDao() {
        if (empDao == null)
            empDao = new EmployeeDaoImpl();
        return empDao;
    }
}
