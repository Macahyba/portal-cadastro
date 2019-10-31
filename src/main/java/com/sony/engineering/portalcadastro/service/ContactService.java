package com.sony.engineering.portalcadastro.service;

import java.util.List;

import com.sony.engineering.portalcadastro.model.Contact;

public interface ContactService extends GenericService<Contact>{
	
	List<Contact> findDistinctByEmail(String email);

	List<Contact> findDistinctByName(String name);

	List<Contact> findDistinctByNameOrEmail(String name, String email);
	
}
