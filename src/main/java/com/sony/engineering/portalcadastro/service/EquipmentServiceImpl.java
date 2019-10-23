package com.sony.engineering.portalcadastro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.repository.EquipmentDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public class EquipmentServiceImpl extends GenericServiceImpl<Equipment> implements EquipmentService{

	@Autowired
	EquipmentDao equipmentDao;
	
	public EquipmentServiceImpl(GenericDao<Equipment> dao) {
		super(dao);
	}

	@Override
	public List<Equipment> findByName(String name) {
		return equipmentDao.findByName(name);
	}

	@Override
	public List<Equipment> findBySerialNumber(String serialNumber) {
		return equipmentDao.findBySerialNumber(serialNumber);
	}

	
}
