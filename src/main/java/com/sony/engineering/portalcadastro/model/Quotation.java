package com.sony.engineering.portalcadastro.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Quotation {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	
	@NotEmpty
	@Column(columnDefinition = "float default 0")
	public Float totalPrice;
	
	@Column(columnDefinition = "float default 0")
	public Float discount;
	
	@NotEmpty
	public String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	public Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	public Date aprovalDate;
	
	@NotEmpty
	@OneToOne
	public User user;
	
	@NotEmpty
	@OneToMany
	public List<Equipment> equipments;
	
	@NotEmpty
	@OneToOne
	public Customer customer;

	@NotEmpty
	@OneToOne
	public User aprovalUser;
	
	@NotEmpty
	@OneToMany
	public List<Service> services;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getAprovalDate() {
		return aprovalDate;
	}

	public void setAprovalDate(Date aprovalDate) {
		this.aprovalDate = aprovalDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public User getAprovalUser() {
		return aprovalUser;
	}

	public void setAprovalUser(User aprovalUser) {
		this.aprovalUser = aprovalUser;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
	
}
