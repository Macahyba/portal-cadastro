package com.sony.engineering.portalcadastro.service;

import java.util.List;
import java.util.Set;

import com.sony.engineering.portalcadastro.model.Equipment;

public interface EquipmentService extends GenericService<Equipment>{

	List<Equipment> findDistinctByName(String name);

	List<Equipment> findBySerialNumber(String serialNumber);

	Set<Equipment> saveAll(Set<Equipment> equipments);

}
