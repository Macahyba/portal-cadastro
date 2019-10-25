package com.sony.engineering.portalcadastro.service;


import java.util.List;


import com.sony.engineering.portalcadastro.model.Customer;

public interface CustomerService extends GenericService<Customer>{
	
	List<Customer> findDistinctByCnpj(String cnpj);
	
	List<Customer> findDistinctByName(String name);

}
