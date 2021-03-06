package com.sony.engineering.portalcadastro.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Repair {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	private String SapNotification;
	
	private Boolean warranty;
	
	private String notaFiscal;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date partRequestDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date partArrivalDate;
	
	private Float tat;

	@Column(name = "active", nullable = false, columnDefinition = "bit default 1")
	private Boolean active;
	
	@ManyToOne
	@JoinColumn(name = "status_id")	
	private Status status;
	
	private String notaDeEntrada;
	
	private String trackingNumber;
	
	@OneToMany(
			mappedBy = "repair", 
			cascade = CascadeType.DETACH)
	private List<RepairFup> repairFups = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "user_id")	
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "equipment_id")
	private Equipment equipment;
	
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "contact_id")
	private Contact contact;

	public void addRepairFup(RepairFup repairFup) {
		repairFups.add(repairFup);
		repairFup.setRepair(this);
	}
	
	public void removeRepairFup(RepairFup repairFup) {
		repairFups.remove(repairFup);
		repairFup.setRepair(null);
	}	

	public void addRepairFupsTop(List<RepairFup> repairFupsList) {
		repairFups.addAll(0,repairFupsList);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSapNotification() {
		return SapNotification;
	}

	public void setSapNotification(String sapNotification) {
		SapNotification = sapNotification;
	}

	public Boolean getWarranty() {
		return warranty;
	}

	public void setWarranty(Boolean warranty) {
		this.warranty = warranty;
	}

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public Date getPartRequestDate() {
		return partRequestDate;
	}

	public void setPartRequestDate(Date partRequestDate) {
		this.partRequestDate = partRequestDate;
	}

	public Date getPartArrivalDate() {
		return partArrivalDate;
	}

	public void setPartArrivalDate(Date partArrivalDate) {
		this.partArrivalDate = partArrivalDate;
	}

	public Float getTat() {
		return tat;
	}

	public void setTat(Float tat) {
		this.tat = tat;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNotaDeEntrada() {
		return notaDeEntrada;
	}

	public void setNotaDeEntrada(String notaDeEntrada) {
		this.notaDeEntrada = notaDeEntrada;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public List<RepairFup> getRepairFups() {
		return repairFups;
	}

	public void setRepairFups(List<RepairFup> repairFups) {
		if (repairFups == null) {
			if(this.repairFups != null) {
				this.repairFups.forEach((c) -> c.setRepair(null));
			}
		} else {
			repairFups.forEach((c) -> c.setRepair(this));
		}
		this.repairFups = repairFups;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Equipment getEquipment() {
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

}
