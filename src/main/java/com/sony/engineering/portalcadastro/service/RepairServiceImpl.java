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

	private RepairDao repairDao;
	private CustomerService customerService;
	private UserService userService;
	private StatusService statusService;
	private EquipmentService equipmentService;
	private RepairFupService repairFupService;

	@Autowired
	public RepairServiceImpl(GenericDao<Repair> dao,
							 RepairDao repairDao,
							 CustomerService customerService,
							 UserService userService,
							 StatusService statusService,
							 EquipmentService equipmentService,
							 RepairFupService repairFupService) {
		super(dao);
		this.repairDao = repairDao;
		this.customerService = customerService;
		this.userService = userService;
		this.statusService = statusService;
		this.equipmentService = equipmentService;
		this.repairFupService = repairFupService;
	}

	public RepairServiceImpl(GenericDao<Repair> dao) {
		super(dao);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Repair save(Repair repair) {

		customerInsertion(repair);
		contactInsertion(repair);
		userInsertion(repair);
		statusInsertion(repair);
		repairFupsInsertion(repair);
		equipmentInsertion(repair);
		
		// SAVE
		
		return repairDao.save(repair);
	}

	@Override
	public Repair patch(Repair repair) {

		
		try {
					
			Repair repairDb = findById(repair.getId());
			
			Equipment equipment = repairDb.getEquipment();
			
			repairFupService.repairFupsCheckId(repair);
			
			repairFupService.addEquipmentToRepairFups(repair.getRepairFups(), equipment);
						
			for (RepairFup rf : repair.getRepairFups()) {
				
				equipment.addSparePartsTop(rf.getSpareParts());
			}

			
			repair.setEquipment(equipment);
			
			if (!repairDb.getRepairFups().isEmpty()) {
				
				repair.addRepairFupsTop(repairDb.getRepairFups());
			}				
			
			merge(repair, repairDb);
			return this.save(repairDb);
			
		} catch (Exception e) {
			throw new RuntimeErrorException(null, e.getMessage());
		}
		
	}
	
	private void customerInsertion(Repair repair) {

		
		Customer customer = repair.getCustomer();
		
		if (customer == null) {
			throw new RuntimeErrorException(null, "Error on creating customer");
		}
		
		customer.addContact(repair.getContact());
		customer = customerService.save(customer);
		repair.setCustomer(customer);
	}
	
	private void contactInsertion(Repair repair) {

		if (repair.getCustomer().getContacts().size() > 1) {
			
			throw new RuntimeErrorException(null, "Error on creating contact - too many arguments");
		}
		
		repair.setContact(repair.getCustomer().getContacts().iterator().next());
	}
	
	private void userInsertion(Repair repair) {
		
		User user = repair.getUser();
		
		if(user == null) {
			throw new RuntimeErrorException(null, "Error on creating user");
		}
		
		user = userService.save(user);
		repair.setUser(user);		
	}
	
	private void statusInsertion(Repair repair) {
		
		Status status = repair.getStatus();
		
		try {

			status = statusService.save(status);
		} catch (Exception e) {
			throw new RuntimeErrorException(null, "Status not found!");
		}
	
		repair.setStatus(status);
		
	}	
	
	private void equipmentInsertion(Repair repair) {
		
		Equipment equipment = repair.getEquipment();
		
		if(equipment == null) {
			throw new RuntimeErrorException(null, "Error on creating equipment");
		}
		
		equipment = equipmentService.save(equipment);
		repair.setEquipment(equipment);
		
	}		
	
	private void repairFupsInsertion(Repair repair) {
		
		List<RepairFup> repairFups = repair.getRepairFups();
		
		if (repairFups != null && !repairFups.isEmpty()) {
			
			repairFups = repairFupService.saveAll(repairFups);
			repair.setRepairFups(repairFups);
		}
	}
	
}
