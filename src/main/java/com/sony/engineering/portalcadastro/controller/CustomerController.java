package com.sony.engineering.portalcadastro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.service.CustomerService;

@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	@GetMapping(value = "customers")
	public List<Customer> getAll(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "cnpj") String cnpj){
		
		if(StringUtils.hasText(name)) {
			
			return customerService.findDistinctByName(name);
		}
		
		if(StringUtils.hasText(cnpj)) {
			
			return customerService.findDistinctByCnpj(cnpj);
		}		
		
		return customerService.findAll();
	}
	
	@GetMapping(value = "customers/{id}")
	public Customer getCustomerById(@PathVariable("id") Integer id) {
		
		return customerService.findById(id);
	}
	
	@PostMapping(value = "customers")
	public Customer setCustomer(@RequestBody Customer customer) {

		try {
			return customerService.save(customer);
		} catch (RuntimeException e) {
			return null;
		}
	}
	
	@PutMapping(value = "customers/{id}")
	public Customer updateCustomer(@RequestBody Customer customer, @PathVariable("id") Integer id) {
		customer.setId(id);
		return customerService.edit(customer);
	}
	
	@DeleteMapping(value = "customers/{id}")
	public void deleteCustomer(@PathVariable("id") Integer id){
		customerService.delete(id);
	}

}
