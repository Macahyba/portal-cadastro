package com.sony.engineering.portalcadastro.model;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"name", "serialNumber"})
    )
public class Equipment {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String serialNumber;
	

	@JsonManagedReference
	@OneToMany(
			mappedBy = "equipment", 
			cascade = CascadeType.ALL)
	private Set<SparePart> spareParts = new HashSet<SparePart>();
	
	public void addSparePart(SparePart sparePart) {
		spareParts.add(sparePart);
		sparePart.setEquipment(this);
	}
	
	public void removeSparePart(SparePart sparePart) {
		spareParts.remove(sparePart);
		sparePart.setEquipment(null);
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

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Set<SparePart> getSpareParts() {
		return spareParts;
	}
	
	public void setSpareParts(Set<SparePart> spareParts) {
		if (spareParts == null) {
			if(this.spareParts != null) {
				this.spareParts.forEach((c) -> {c.setEquipment(null);});
			}
		} else {
			spareParts.forEach((c) -> {c.setEquipment(this);});
		}
		this.spareParts = spareParts;
	}	

}
