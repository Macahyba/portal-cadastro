package com.sony.engineering.portalcadastro.service;

import java.util.List;
import java.util.Set;

import com.sony.engineering.portalcadastro.model.Contact;
import com.sony.engineering.portalcadastro.model.Customer;

public interface ContactService extends GenericService<Contact>{
	
	List<Contact> findDistinctByEmail(String email);

	List<Contact> findDistinctByName(String name);

	List<Contact> findDistinctByNameOrEmail(String name, String email);

	Set<Contact> saveAll(Set<Contact> contacts);
	
}
