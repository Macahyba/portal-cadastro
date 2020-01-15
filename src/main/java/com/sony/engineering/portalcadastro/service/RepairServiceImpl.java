package com.sony.engineering.portalcadastro.service;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.Repair;
import com.sony.engineering.portalcadastro.model.RepairFup;
import com.sony.engineering.portalcadastro.model.Status;
import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.RepairDao;

@Service
public class RepairServiceImpl extends GenericServiceImpl<Repair> implements RepairService{

	@Autowired
	RepairDao repairDao;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	StatusService statusService;
	
	@Autowired
	EquipmentService equipmentService;
	
	public RepairServiceImpl(GenericDao<Repair> dao) {
		super(dao);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Repair save(Repair repair) {
		
		// CUSTOMER INSERTION
		
		Customer customer = repair.getCustomer();

		customer = customerService.save(customer);
		
		if (customer == null) {
			throw new RuntimeErrorException(null, "Error on creating customer");
		}
		
		repair.setCustomer(customer);
		
		// CONTACT INSERTION
		
		repair.setContact(repair.getCustomer().getContacts().iterator().next());
		
		// USER INSERTION
		
		User user = repair.getUser();
		
		user = userService.save(user);
		
		if(user == null) {
			throw new RuntimeErrorException(null, "Error on creating user");
		}
		
		repair.setUser(user);
		
		// STATUS INSERTION
		
		Status status = repair.getStatus();
		
		status = statusService.save(status);
		
		repair.setStatus(status);
		
		// EQUIPMENT INSERTION
		
		Equipment equipment = repair.getEquipment();
		
		equipment = equipmentService.save(equipment);
		
		if(equipment == null) {
			throw new RuntimeErrorException(null, "Error on creating equipment");
		}
		
		repair.setEquipment(equipment);
		
		// SAVE
		
		return repairDao.save(repair);
		
	}
	
	public Repair patch(Repair repair) {

		
		try {
		
			Repair repairDb = repairDao.findById(repair.getId()).get();
			
			List<RepairFup> repairFups = repair.getRepairFups();
			
			if (!repairFups.isEmpty()) {
				
				repairDb.addRepairFups(repairFups);
			}			
			
			return repairDao.save(repairDb);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

	public RepairDao getRepairDao() {
		return repairDao;
	}

	public void setRepairDao(RepairDao repairDao) {
		this.repairDao = repairDao;
	}

}
