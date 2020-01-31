package com.sony.engineering.portalcadastro.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Contact;
import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.repository.CustomerDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;


@Service
public class CustomerServiceImpl extends GenericServiceImpl<Customer> implements CustomerService{

	private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	public CustomerServiceImpl(GenericDao<Customer> dao,
							   CustomerDao customerDao,
							   ContactService contactService) {
		super(dao);
		this.customerDao = customerDao;
		this.contactService = contactService;
	}

	private CustomerDao customerDao;
	private ContactService contactService;

	public CustomerServiceImpl(GenericDao<Customer> dao) {
		super(dao);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = UnexpectedRollbackException.class)
	public Customer save(Customer customer) {	

		Set<Contact> contacts = customer.getContacts();
		Set<Contact> newContacts = new HashSet<>();
		
		if (customer.getId() != null) {		
			
			try {
				customer = this.findById(customer.getId());
			} catch (NoSuchElementException e) {
				logger.error("Invalid Customer Id!");
				throw new NoSuchElementException();
			}
		} else {

			List<Customer> findName = this.findDistinctByName(customer.getName()); 
			
			if (!findName.isEmpty()) {
			
				customer = findName.get(0);
			}

		}

		if (contacts != null) {
			contacts.forEach(c -> {
				if (c.getId() != null) {
					
					try {					
						
						newContacts.add(contactService.findById(c.getId()));
						
					} catch (NoSuchElementException e){
						logger.error("Invalid Contact Id!");
						throw new NoSuchElementException();		
					}				
				} else {
					
					List<Contact> findContact = contactService.findDistinctByNameOrEmail(c.getName(), c.getEmail());

					newContacts.add(!findContact.isEmpty() ?
										findContact.get(0) :
										c);
				}
			});
		}
		
		customer.setContacts(newContacts);
		
		return customerDao.save(customer);

	}

	public Customer patch(Customer customer){

		Customer customerDb = customerDao.findById(customer.getId())
				.orElseThrow(() -> new NoSuchElementException("Invalid Quotation Id!"));

		merge(customer, customerDb);
		return customerDao.save(customerDb);
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
