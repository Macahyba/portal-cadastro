package com.sony.engineering.portalcadastro.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.repository.EquipmentDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public class EquipmentServiceImpl extends GenericServiceImpl<Equipment> implements EquipmentService{

	Logger logger = LoggerFactory.getLogger(EquipmentServiceImpl.class);
	
	@Autowired
	EquipmentDao equipmentDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Equipment save(Equipment equipment) {
		
		if(equipment.getId() != null) {
			
			try {
				equipment = equipmentDao.findById(equipment.getId()).get();
			} catch (NoSuchElementException e) {
				logger.error("Invalid Equipment Id!");
				throw new NoSuchElementException();
			}
		} else {
			try {
				
				List<Equipment> findEquipment = equipmentDao.findDistinctByNameAndSerialNumber(equipment.getName(), equipment.getSerialNumber());
				
				if(!findEquipment.isEmpty()) {
					equipment = findEquipment.get(0);
				}
				
			} catch (IncorrectResultSizeDataAccessException e){
				logger.error("Duplicate equipment detected, please chech the DB");
				throw new IncorrectResultSizeDataAccessException(1);
			}
		}
		
		return equipmentDao.save(equipment);
	}
	
	@Override
	public Set<Equipment> saveAll(Set<Equipment> equipments){
		
		Set<Equipment> newEquipments = new HashSet<>();
		
		equipments.forEach(eq ->{ 
					
			try {
			
				newEquipments.add(this.save(eq));
			} catch (RuntimeException e) {
				logger.error("Error persisting equipment");	
				throw new RuntimeException();
			}
		
		});
		
		return newEquipments;
	}

	@Override
	public Equipment patch(Equipment t) {
		// TODO Auto-generated method stub
		return null;
	}	
	
	public EquipmentServiceImpl(GenericDao<Equipment> dao) {
		super(dao);
	}

	@Override
	public List<Equipment> findDistinctByName(String name) {
		return equipmentDao.findDistinctByName(name);
	}

	@Override
	public List<Equipment> findBySerialNumber(String serialNumber) {
		return equipmentDao.findBySerialNumber(serialNumber);
	}
	
}
