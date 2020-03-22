package com.sony.engineering.portalcadastro.model;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"name", "serial_number"})
    )
public class Equipment {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	@Column(name = "serial_number")
	private String serialNumber;

	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(name = "equipment_spare_part",
	joinColumns = {@JoinColumn(name = "equipment_id")}, 
	inverseJoinColumns = {@JoinColumn(name = "spare_part_id")})
	private Set<SparePart> spareParts = new HashSet<>();

	public void addSparePartsTop(Set<SparePart> sparePartsList) {
		spareParts.addAll(sparePartsList);
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
		this.spareParts = spareParts;
	}	

}
