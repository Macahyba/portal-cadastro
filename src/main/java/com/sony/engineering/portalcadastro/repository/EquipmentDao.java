package com.sony.engineering.portalcadastro.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sony.engineering.portalcadastro.model.Equipment;

@Repository
public interface EquipmentDao extends GenericDao<Equipment>{

	List<Equipment> findByName(String name);

	List<Equipment> findBySerialNumber(String serialNumber);

}
