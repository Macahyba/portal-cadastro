package com.sony.engineering.portalcadastro.service;

import java.util.List;

import com.sony.engineering.portalcadastro.model.Equipment;

public interface EquipmentService extends GenericService<Equipment>{

	List<Equipment> findByName(String name);

	List<Equipment> findBySerialNumber(String serialNumber);

}
