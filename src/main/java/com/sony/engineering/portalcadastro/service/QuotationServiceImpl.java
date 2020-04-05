package com.sony.engineering.portalcadastro.service;

import java.util.NoSuchElementException;
import java.util.Set;

import javax.management.RuntimeErrorException;

import com.sony.engineering.portalcadastro.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.QuotationDao;

@org.springframework.stereotype.Service
public class QuotationServiceImpl extends GenericServiceImpl<Quotation> implements QuotationService{

	private QuotationDao quotationDao;
	private CustomerService customerService;
	private UserService userService;
	private EquipmentService equipmentService;
	private ServiceService serviceService;
	private StatusService statusService;

	@Autowired
	public QuotationServiceImpl(GenericDao<Quotation> dao,
								QuotationDao quotationDao,
								CustomerService customerService,
								UserService userService,
								EquipmentService equipmentService,
								ServiceService serviceService,
								StatusService statusService) {
		super(dao);
		this.quotationDao = quotationDao;
		this.customerService = customerService;
		this.userService = userService;
		this.equipmentService = equipmentService;
		this.serviceService = serviceService;
		this.statusService = statusService;
	}

	public QuotationServiceImpl(GenericDao<Quotation> dao) {
		super(dao);
	}	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Quotation save(Quotation quotation) {

		labelInsertion(quotation);
		customerInsertion(quotation);
		contactInsertion(quotation);
		userInsertion(quotation);
		statusInsertion(quotation);
		equipmentInsertion(quotation);
		serviceInsertion(quotation);
		priceInsertion(quotation);
		approvalUserInsertion(quotation);

		return quotationDao.save(quotation);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Quotation patch(Quotation quotation) {

		Quotation quotationDb = quotationDao.findById(quotation.getId()).orElseThrow(() ->
			new NoSuchElementException("Invalid Quotation Id!")
		);

		merge(quotation, quotationDb);
		return this.save(quotationDb);

	}
	
	private void customerInsertion(Quotation quotation) {

		
		Customer customer = quotation.getCustomer();
		
		if (customer == null) {
			throw new RuntimeErrorException(null, "Error on creating customer");
		}
				
		customer.addContact(quotation.getContact());
		customer = customerService.save(customer);
		quotation.setCustomer(customer);
	}
	
	private void contactInsertion(Quotation quotation) {

		quotation.setContact(quotation.getCustomer().getContacts().iterator().next());
		
	}
	
	private void userInsertion(Quotation quotation) {
		
		User user = quotation.getUser();
		
		if(user == null) {
			throw new RuntimeErrorException(null, "Error on creating user");
		}
		
		user = userService.save(user);
		quotation.setUser(user);		
	}
	
	private void statusInsertion(Quotation quotation) {
		
		Status status = quotation.getStatus();
		
		try {

			status = statusService.findById(status.getId());
		} catch (Exception e) {
			throw new RuntimeErrorException(null, "Status not found!");
		}
	
		quotation.setStatus(status);
		
	}	

	private void equipmentInsertion(Quotation quotation) {

		Equipment equipment = quotation.getEquipment();

		if(equipment == null) {
			throw new RuntimeErrorException(null, "Error on creating equipment");
		}

		equipment = equipmentService.save(equipment);
		quotation.setEquipment(equipment);

	}

	private void serviceInsertion(Quotation quotation) {

		Set<Service> services = quotation.getServices();
		
		if(services.isEmpty()) {
			throw new RuntimeErrorException(null, "Error on creating services");
		}	
		
		services = serviceService.saveAll(services);
		quotation.setServices(services);
		
	}	
	
	private void approvalUserInsertion(Quotation quotation) {
		
		if (quotation.getApprovalUser() != null) {
			
			try {
			
				User approvalUser = userService.findById(quotation.getApprovalUser().getId());
				quotation.setApprovalUser(approvalUser);
			} catch (Exception e) {
				throw new RuntimeErrorException(null, "ApprovalUser not found!");
			}
			
		}
		
	}

	private void labelInsertion(Quotation quotation){

		if (quotation.getLabel() == null){
			Quotation last = quotationDao.findFirstByOrderByIdDesc();
			if (last == null) {
				last = new Quotation();
				last.setId(0);
			}
			quotation.setLabel(
					String.format("%s%04d", quotation.returnPrettyCreationDate(), last.getId()+1));
		}
	}

	private void priceInsertion(Quotation quotation){

		if (quotation.getTotalPrice() == null){
			Float tPrice = quotation.getServices().stream()
					.map(s -> s.getPrice())
					.reduce(0f, (a, b) -> a + b);
			quotation.setTotalPrice(tPrice);
		}
	}
	
}
