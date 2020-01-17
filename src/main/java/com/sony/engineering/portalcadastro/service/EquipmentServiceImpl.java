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
import com.sony.engineering.portalcadastro.model.SparePart;
import com.sony.engineering.portalcadastro.repository.EquipmentDao;
import com.sony.engineering.portalcadastro.repository.GenericDao;

@Service
public class EquipmentServiceImpl extends GenericServiceImpl<Equipment> implements EquipmentService{

	Logger logger = LoggerFactory.getLogger(EquipmentServiceImpl.class);
	
	@Autowired
	EquipmentDao equipmentDao;
	
	@Autowired
	SparePartService sparePartService;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Equipment save(Equipment equipment) {
		
		Set<SparePart> spareParts = equipment.getSpareParts();
		Set<SparePart> newSpareParts = new HashSet<SparePart>();
		
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
		
		if (spareParts != null) {
			spareParts.forEach(sp ->{
				
				if (sp.getId() != null) {
					try {
						
						newSpareParts.add(sparePartService.findById(sp.getId()));
					} catch (NoSuchElementException e){
						logger.error("Invalid SparePart Id!");
						throw new NoSuchElementException();		
					}
				} else {
					
					List<SparePart> findSparePart = sparePartService.findDistinctByPartNumber(sp.getPartNumber());
					
					newSpareParts.add(!findSparePart.isEmpty() ?
										  findSparePart.get(0) :
										  sp);
				}
			});
		}
		
		equipment.setSpareParts(newSpareParts);
		
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
