package com.sony.engineering.portalcadastro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
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

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ContactService contactService;

	public CustomerServiceImpl(GenericDao<Customer> dao) {
		super(dao);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Customer save(Customer customer) {
	
//		List<Customer> customers = customerDao.findDistinctByName(customer.getName());
//		
//		if (customers.isEmpty()) {
//			return customerDao.save(customer);
//		}
//		
//		
//		
//		Contact contact = contactService.findDistinctByEmail(customer.getContact().getEmail();
//			
//		if (contact == null) {
//			
//			contact = contactService.save(customer.getContact());
//			c.setContact(contact);
//			
//			return c;
//		}
//
//		return customerDao.save(c);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> findAll() {
		// TODO Auto-generated method stub
		return null;
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
