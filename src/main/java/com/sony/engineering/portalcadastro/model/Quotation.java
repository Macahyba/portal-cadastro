package com.sony.engineering.portalcadastro.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
	
	@NotEmpty
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	public Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	public Date approvalDate;
	
	@NotEmpty
	@OneToOne
	@JoinColumn(name = "user_id")
	public User user;

	@ManyToMany
	@JoinTable(name = "quotation_equipment", 
	joinColumns = {@JoinColumn(name = "quotation_id")}, 
	inverseJoinColumns = {@JoinColumn(name = "equipment_id")})
	public List<Equipment> equipments;
	
	@NotEmpty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	public Customer customer;

	@OneToOne
	@JoinColumn(name = "approval_user_id")
	public User approvalUser;
	
	@ManyToMany
	@JoinTable(name = "quotation_service", joinColumns = {@JoinColumn(name = "quotations_id")},
	inverseJoinColumns = {@JoinColumn(name = "service_id")})
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

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
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

	public User getApprovalUser() {
		return approvalUser;
	}

	public void setApprovalUser(User approvalUser) {
		this.approvalUser = approvalUser;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
	
}
