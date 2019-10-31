package com.sony.engineering.portalcadastro.model;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Customer {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true)
	private String name;
	
	@Column(unique = true)
	private String fullName;
	
	@Column(unique = true)
	private String cnpj;
	
	@OneToMany(
			mappedBy = "customer", 
			cascade = CascadeType.ALL)
	private Set<Contact> contacts;
	
	public void addContact(Contact contact) {
		contacts.add(contact);
		contact.setCustomer(this);
	}
	
	public void removeContact(Contact contact) {
		contacts.remove(contact);
		contact.setCustomer(null);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		if (contacts == null) {
			if(this.contacts != null) {
				this.contacts.forEach((c) -> {c.setCustomer(null);});
			}
		} else {
			contacts.forEach((c) -> {c.setCustomer(this);});
		}
		this.contacts = contacts;
	}

}
