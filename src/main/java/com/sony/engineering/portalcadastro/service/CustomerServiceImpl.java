package com.sony.engineering.portalcadastro.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Contact;
import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.repository.CustomerDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;


@Service
public class CustomerServiceImpl extends GenericServiceImpl<Customer> implements CustomerService{

	Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class); 
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ContactService contactService;

	public CustomerServiceImpl(GenericDao<Customer> dao) {
		super(dao);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Customer save(Customer customer) {	
		
		Set<Contact> contacts = customer.getContacts();
		
		if (customer.getId() != null) {		
			
			try {
				customer = this.findOne(customer.getId());
			} catch (NoSuchElementException e) {
				logger.error("Invalid Customer Id!");
				return null;	// error 500
			}
		} else {
			

			List<Customer> findName = this.findDistinctByName(customer.getName()); 
			
			if (!findName.isEmpty()) {
			
				customer = findName.get(0);
			}

		}

		for (Contact c: contacts) {
			if (c.getId() != null) {
				
				try {
					contacts.clear();
					contacts.add(contactService.findOne(c.getId()));
					
				} catch (NoSuchElementException e){
					logger.error("Invalid Contact Id!");
					return null;
					
				}				
			} else {
				
				List<Contact> findContact = contactService.findDistinctByNameOrEmail(c.getName(), c.getEmail());
				
				if(!findContact.isEmpty()) {
					
					contacts.clear();
					contacts.add(findContact.get(0));
				}
			}
		}

		customer.setContacts(contacts);
		
		return customerDao.save(customer);

	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Customer edit(Customer t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer findOne(Integer id) {
		return customerDao.findById(id).get();
	}

	@Override
	public List<Customer> findAll() {
		return customerDao.findAll();
	}

	@Override
	public List<Customer> findDistinctByCnpj(String cnpj) {
		return customerDao.findDistinctByCnpj(cnpj);
	}

	@Override
	public List<Customer> findDistinctByName(String name) {
		return customerDao.findDistinctByName(name);
	}

}
