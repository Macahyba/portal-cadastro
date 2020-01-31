package com.sony.engineering.portalcadastro.model;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true)
	@NotEmpty
	private String name;
	
	@Column(unique = true)
	@NotEmpty
	private String fullName;
	
	@Column(unique = true)
	@NotEmpty
	private String cnpj;
	
	@OneToMany(
			mappedBy = "customer", 
			cascade = CascadeType.ALL)
	private Set<Contact> contacts = new HashSet<>();
	
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
				this.contacts.forEach((c) -> c.setCustomer(null));
			}
		} else {
			contacts.forEach((c) -> c.setCustomer(this));
		}
		this.contacts = contacts;
	}

}
