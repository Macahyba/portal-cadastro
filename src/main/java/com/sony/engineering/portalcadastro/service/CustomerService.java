package com.sony.engineering.portalcadastro.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Contact;
import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.repository.CustomerDao;
import com.sony.engineering.portalcadastro.repository.GenericDaoImpl;

@Service
public class CustomerService extends GenericServiceImpl<Customer>{

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ContactService contactService;
	
	public CustomerService(GenericDaoImpl<Customer> customerDao) {
		this.customerDao = (CustomerDao)customerDao;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Customer save(Customer customer) {
	
		Contact contact = contactService.getOneByAttr("email", customer.getContact().getEmail());
			
		if (contact == null) {
			
			contact = contactService.save(customer.getContact());
			customer.setContact(contact);
		}
		customerDao.save(customer);	
		return customer;
	}
	
}
