package com.sony.engineering.portalcadastro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.repository.EquipmentDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public class EquipmentService extends GenericServiceImpl<Equipment>{

	@Autowired
	private EquipmentDao equipmentDao;
	
	public EquipmentService(GenericDao<Equipment> dao) {
		super(dao);
		this.equipmentDao = (EquipmentDao) dao;
	}

	public EquipmentDao getEquipmentDao() {
		return equipmentDao;
	}

	public void setEquipmentDao(EquipmentDao equipmentDao) {
		this.equipmentDao = equipmentDao;
	}
	
}
