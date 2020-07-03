package com.sony.engineering.portalcadastro.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.SparePart;
import com.sony.engineering.portalcadastro.repository.EquipmentDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public class EquipmentServiceImpl extends GenericServiceImpl<Equipment> implements EquipmentService{

	private Logger logger = LoggerFactory.getLogger(EquipmentServiceImpl.class);

	@Autowired
	public EquipmentServiceImpl(GenericDao<Equipment> dao,
								EquipmentDao equipmentDao,
								SparePartService sparePartService) {
		super(dao);
		this.equipmentDao = equipmentDao;
		this.sparePartService = sparePartService;
	}

	private EquipmentDao equipmentDao;
	private SparePartService sparePartService;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Equipment save(Equipment equipment) {
		
		Set<SparePart> spareParts = equipment.getSpareParts();	
		
		if(equipment.getId() != null) {

			equipment = equipmentDao.findById(equipment.getId())
							.<NoSuchElementException>orElseThrow(() -> {
									logger.error("Invalid Equipment Id!");
									throw new NoSuchElementException();
							});

		} else {
			try {
				
				List<Equipment> findEquipment = equipmentDao.
						findDistinctByNameAndSerialNumber(equipment.getName(),
						equipment.getSerialNumber());

				if(!findEquipment.isEmpty()) {
					equipment = findEquipment.get(0);
				}
				
			} catch (IncorrectResultSizeDataAccessException e){
				logger.error("Duplicate equipment detected, please chech the DB");
				throw new IncorrectResultSizeDataAccessException(1);
			}
		}	
		
		if (spareParts != null && !spareParts.isEmpty()) {
			spareParts = sparePartService.saveAll(spareParts);
			equipment.setSpareParts(spareParts);
		}
		return equipmentDao.save(equipment);
	}

	@Override
	public Equipment patch(Equipment equipment){

		Equipment equipmentDb = equipmentDao.findById(equipment.getId())
				.<NoSuchElementException>orElseThrow(() -> new NoSuchElementException("Invalid Equipment Id!"));

		merge(equipment, equipmentDb);
		return equipmentDao.save(equipmentDb);
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
