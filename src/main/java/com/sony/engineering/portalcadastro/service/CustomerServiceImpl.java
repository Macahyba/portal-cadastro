package com.sony.engineering.portalcadastro.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Contact;
import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.repository.CustomerDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;
//import com.sony.engineering.portalcadastro.repository.GenericDaoImpl;


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
	
	@Transactional(propagation = Propagation.NESTED)
	public Customer save(Customer customer) {	
		
		Set<Contact> contact = customer.getContacts();
		
		if (customer.getId() != null) {		
			
			try {
				customer = this.findOne(customer.getId());
			} catch (NoSuchElementException e) {
				logger.error("Invalid Id!");
				return null;	
			}
		}
		customer.setContacts(contact);

		return customerDao.save(customer);
		// TODO VERIFICAR SE CONTATO EXISTE ANTES DE PERSISTIR
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
	public List<Customer> findByCnpj(String cnpj) {
		return customerDao.findByCnpj(cnpj);
	}

	@Override
	public List<Customer> findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> findByFullName(String fullname) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
