package com.banking.customers;

public class CustomerDaoFactory {
    public static CustomerDao dao;

    private CustomerDaoFactory(){}

    public static CustomerDao getCustomerDao() {
        if (dao == null)
            dao = new CustomerDaoImpl();
        return dao;
    }
}
