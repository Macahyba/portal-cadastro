package com.sony.engineering.portalcadastro.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Quotation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true)
	private String label;
	
	@Column(columnDefinition = "float default 0")
	private Float totalPrice;
	
	@Column(columnDefinition = "float default 0")
	private Float totalDiscount;
	
	@ManyToOne
	@JoinColumn(name = "status_id")	
	private Status status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvalDate;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

//	@ManyToMany(cascade = CascadeType.DETACH)
//	@JoinTable(name = "quotation_equipment",
//	joinColumns = {@JoinColumn(name = "quotation_id")},
//	inverseJoinColumns = {@JoinColumn(name = "equipment_id")})
//	private Set<Equipment> equipments = new HashSet<>();
	@ManyToOne
	@JoinColumn(name = "equipment_id")
	private Equipment equipment;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "contact_id")
	private Contact contact;

	@ManyToOne
	@JoinColumn(name = "approval_user_id")
	private User approvalUser;
	
	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(name = "quotation_service", 
	joinColumns = {@JoinColumn(name = "quotation_id")},
	inverseJoinColumns = {@JoinColumn(name = "service_id")})
	private Set<Service> services = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Float getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Float totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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

	public Equipment getEquipment(){
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public User getApprovalUser() {
		return approvalUser;
	}

	public void setApprovalUser(User approvalUser) {
		this.approvalUser = approvalUser;
	}

	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}

	public String returnPrettyCreationDate() {

		String pattern = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		if (Objects.isNull(this.creationDate)){
			return sdf.format(new Date());
		}
		return sdf.format(this.creationDate);
	}
	
}
