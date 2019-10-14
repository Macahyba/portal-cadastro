package com.sony.engineering.portalcadastro.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.repository.CustomerDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public class CustomerService extends GenericServiceImpl<Customer>{

	@Autowired
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
