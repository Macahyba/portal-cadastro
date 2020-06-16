package com.sony.engineering.portalcadastro.service;

import java.util.*;

import com.sony.engineering.portalcadastro.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.sony.engineering.portalcadastro.model.Contact;
import com.sony.engineering.portalcadastro.repository.ContactDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public class ContactServiceImpl extends GenericServiceImpl<Contact> implements ContactService{

	@Autowired
	public ContactServiceImpl(GenericDao<Contact> dao, ContactDao contactDao) {
		super(dao);
		this.contactDao = contactDao;
	}

	private Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
	private ContactDao contactDao;

	public ContactServiceImpl(GenericDao<Contact> dao) {
		super(dao);
	}

	@Override
	public List<Contact> findDistinctByEmail(String email) {
		return contactDao.findDistinctByEmail(email);
	}

	@Override
	public List<Contact> findDistinctByName(String name) {
		return contactDao.findDistinctByName(name);
	}

	@Override
	public List<Contact> findDistinctByNameOrEmail(String name, String email) {
		return contactDao.findDistinctByNameOrEmail(name, email);
	}

	@Override
	public Contact save(Contact contact) {

		if (contact.getId() != null) {

			contactDao.findById(contact.getId()).orElseThrow(() -> {
				logger.error("Invalid Contact Id!");
				throw new NoSuchElementException();
			});

		} else {

			try {

				List<Contact> findContact = contactDao.findDistinctByNameOrEmail(contact.getName(), contact.getEmail());

				if(!findContact.isEmpty()) {
					contact = findContact.iterator().next();
				}

			} catch (IncorrectResultSizeDataAccessException e){
				logger.error("Duplicate Contact detected, please check the DB");
				throw new IncorrectResultSizeDataAccessException(1);
			}
		}

		return contactDao.save(contact);

	}

	@Override
	public Set<Contact> saveAll(Set<Contact> contacts) {

		Set<Contact> newContacts = new HashSet<>();

		contacts.forEach(c ->{

			try {

				newContacts.add(this.save(c));

			} catch (RuntimeException e) {
				logger.error("Error persisting contacts");
				throw new RuntimeException();
			}
		});

		return newContacts;
	}

}
