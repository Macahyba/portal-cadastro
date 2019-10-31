package com.sony.engineering.portalcadastro.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping(value = "customers")
	public ResponseEntity<List<Customer>> getAll(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "cnpj") String cnpj){
		
		if(StringUtils.hasText(name)) {
			
			return new ResponseEntity<List<Customer>>(customerService.findDistinctByName(name), HttpStatus.OK);
		}
		
		if(StringUtils.hasText(cnpj)) {
			
			return new ResponseEntity<List<Customer>>(customerService.findDistinctByCnpj(cnpj), HttpStatus.OK);
		}		
		
		return new ResponseEntity<List<Customer>>(customerService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "customers/{id}")
	public Customer getCustomerById(@PathVariable("id") Integer id) {
		
		return customerService.findById(id);
	}
	
	@PostMapping(value = "customers")
	public ResponseEntity<Customer> setCustomer(@RequestBody Customer customer) {

		try {
			return new ResponseEntity<Customer>(customerService.save(customer), HttpStatus.OK);
		} catch (RuntimeException e) {
			logger.error("Error on creating customer!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
