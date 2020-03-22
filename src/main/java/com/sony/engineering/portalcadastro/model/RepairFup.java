package com.sony.engineering.portalcadastro.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "repair_fup")
public class RepairFup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp	
	private Date updateDate;
	
	private String description;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "repair_id")
	private Repair repair;

	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(name = "repair_fup_spare_part",
	joinColumns = {@JoinColumn(name = "repair_fup_id")},
	inverseJoinColumns = {@JoinColumn(name = "spare_part_id")})
	private Set<SparePart> spareParts = new HashSet<>();
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "equipment_id")
	private Equipment equipment;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Repair getRepair() {
		return repair;
	}

	public void setRepair(Repair repair) {
		this.repair = repair;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact )) return false;
        return id != null && id.equals(((Contact) o).getId());
    }
    @Override
    public int hashCode() {
        return 31;
    }

	public Set<SparePart> getSpareParts() {
		return spareParts;
	}

	public void setSpareParts(Set<SparePart> spareParts) {
		this.spareParts = spareParts;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}	
	
}
