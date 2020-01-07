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
		
	public QuotationServiceImpl(GenericDao<Quotation> dao) {
		super(dao);
	}	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Quotation save(Quotation quotation) {
		
		// CUSTOMER INSERTION
		
		Customer customer = quotation.getCustomer();

		customer = customerService.save(customer);
		
		if (customer == null) {
			throw new RuntimeErrorException(null, "Error on creating customer");
		}
		
		quotation.setCustomer(customer);
				
		
		// USER INSERTION
		
		User user = quotation.getUser();
		
		user = userService.save(user);
		
		if(user == null) {
			throw new RuntimeErrorException(null, "Error on creating user");
		}
		
		quotation.setUser(user);
		
		// EQUIPMENT INSERTION
		
		Set<Equipment> equipments = quotation.getEquipments();
		
		equipments = equipmentService.saveAll(equipments);
		
		if(equipments.isEmpty()) {
			throw new RuntimeErrorException(null, "Error on creating equipments");
		}
		
		quotation.setEquipments(equipments);
		
		// SERVICE INSERTION
		
		Set<Service> services = quotation.getServices();
		
		services = serviceService.saveAll(services);
		
		if(services.isEmpty()) {
			throw new RuntimeErrorException(null, "Error on creating services");
		}	
		
		quotation.setServices(services);
		
		services.forEach(e ->{ serviceService.save(e);});

		quotation = quotationDao.save(quotation);
				
		quotation.setLabel(
				String.format("%s%s%04d", quotation.returnPrettyCreationDate(), "OS_", quotation.getId()));
		
		quotation = quotationDao.save(quotation);
		
		return quotation;

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

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
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
	
}
