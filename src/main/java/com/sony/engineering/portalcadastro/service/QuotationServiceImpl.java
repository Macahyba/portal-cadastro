package com.sony.engineering.portalcadastro.service;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.Quotation;
import com.sony.engineering.portalcadastro.model.Service;
import com.sony.engineering.portalcadastro.model.Status;
import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.QuotationDao;

@org.springframework.stereotype.Service
public class QuotationServiceImpl extends GenericServiceImpl<Quotation> implements QuotationService{

	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private StatusService statusService;
		
	public QuotationServiceImpl(GenericDao<Quotation> dao) {
		super(dao);
	}	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Quotation save(Quotation quotation) {
		
		customerInsertion(quotation);
		
		contactInsertion(quotation);
		
		userInsertion(quotation);
		
		statusInsertion(quotation);
		
		equipmentInsertion(quotation);
		
		serviceInsertion(quotation);
		
		approvalUserInsertion(quotation);
		
		// PREPARE TO SAVE

		quotation = quotationDao.save(quotation);

		quotation.setLabel(
				String.format("%s%04d", quotation.returnPrettyCreationDate(), quotation.getId()));
		
		// SAVE
		
		return quotationDao.save(quotation);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Quotation patch(Quotation quotation) {
		
		try {
			Quotation quotationDb = quotationDao.findById(quotation.getId()).get();
			
			merge(quotation, quotationDb);
			return this.save(quotationDb);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("Invalid Quotation Id!");
		} catch (Exception e) {
			return null;
		}

	}

	public QuotationDao getQuotationDao() {
		return quotationDao;
	}

	public void setQuotationDao(QuotationDao quotationDao) {
		this.quotationDao = quotationDao;
	}
	
	public static String[] getNullPropertyNames (Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    PropertyDescriptor[] pds = src.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<String>();
	    for(PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
	
	public static void merge(Object src, Object target) {
	    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
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

		if (quotation.getCustomer().getContacts().size() > 1) {
			
			throw new RuntimeErrorException(null, "Error on creating contact - too many arguments");
		}
		
		quotation.setContact(quotation.getCustomer().getContacts().iterator().next());;
		
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
		
		Set<Equipment> equipments = quotation.getEquipments();
		
		if(equipments.isEmpty()) {
			throw new RuntimeErrorException(null, "Error on creating equipments");
		}
		
		equipments = equipmentService.saveAll(equipments);
		quotation.setEquipments(equipments);
		
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
	
}
