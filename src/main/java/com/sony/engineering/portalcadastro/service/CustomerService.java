package com.sony.engineering.portalcadastro.service;


import java.util.List;


import com.sony.engineering.portalcadastro.model.Customer;

public interface CustomerService extends GenericService<Customer>{
	
	List<Customer> findByCnpj(String cnpj);
	
	List<Customer> findByName(String name);

	List<Customer> findByFullName(String fullname);
	
}
