package com.sony.engineering.portalcadastro.service;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.repository.CustomerDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;

public class CustomerService extends GenericServiceImpl<Customer>{

	private CustomerDao customerDao;
	
	public CustomerService(GenericDao<Customer> dao) {
		super(dao);
		this.customerDao = (CustomerDao) dao;
	}

	public CustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}
	

}
