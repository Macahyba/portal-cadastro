package com.sony.engineering.portalcadastro.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sony.engineering.portalcadastro.model.Contact;

@Repository
public interface ContactDao extends GenericDao<Contact>{

	List<Contact> findDistinctByEmail(String email);

}
