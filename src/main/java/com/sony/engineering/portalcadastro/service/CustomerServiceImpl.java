package com.sony.engineering.portalcadastro.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

		contacts = this.saveAndFetchAll(contacts, customer);

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

		customer.setContacts(contacts);
		
		return customerDao.save(customer);
	}

	@Override
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

	private Set<Contact> saveAndFetchAll(Set<Contact> contacts, Customer customer) {

		contacts = contactService.saveAll(contacts);
		Set<Contact> oldContacts = new HashSet<>();

		List<Customer> customers = findDistinctByName(customer.getName());

		customers.forEach(c -> oldContacts.addAll(c.getContacts()) );

		contacts.addAll(oldContacts);

		return contacts;
	}

}
