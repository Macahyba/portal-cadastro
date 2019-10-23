package com.sony.engineering.portalcadastro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sony.engineering.portalcadastro.model.Contact;
import com.sony.engineering.portalcadastro.repository.ContactDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public class ContactServiceImpl extends GenericServiceImpl<Contact> implements ContactService{

	@Autowired
	ContactDao contactDao;
	
	public ContactServiceImpl(GenericDao<Contact> dao) {
		super(dao);
	}

	@Override
	public List<Contact> findDistinctByEmail(String email) {
		return contactDao.findDistinctByEmail(email);
	}

	
	
}
