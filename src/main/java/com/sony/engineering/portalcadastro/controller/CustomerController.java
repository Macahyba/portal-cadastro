package com.sony.engineering.portalcadastro.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.sony.engineering.portalcadastro.model.Customer;
import com.sony.engineering.portalcadastro.service.CustomerService;

@RestController
public class CustomerController {

	private Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	private CustomerService customerService;
	
	@GetMapping(value = "customers")
	public ResponseEntity<List<Customer>> getAll(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "cnpj") String cnpj){
		
		if(StringUtils.hasText(name)) {
			
			return new ResponseEntity<>(customerService.findDistinctByName(name), HttpStatus.OK);
		}
		
		if(StringUtils.hasText(cnpj)) {
			
			return new ResponseEntity<>(customerService.findDistinctByCnpj(cnpj), HttpStatus.OK);
		}		
		
		return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "customers/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Integer id) {
		
		try {
			return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(value = "customers")
	public ResponseEntity<Customer> setCustomer(@RequestBody Customer customer) {

		try {
			return new ResponseEntity<>(customerService.save(customer), HttpStatus.OK);
		} catch (RuntimeException e) {
			logger.error("Error on creating customer!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "customers/{id}")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable("id") Integer id) {

		customer.setId(id);
		try {
			return new ResponseEntity<>(customerService.edit(customer), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}

	@PatchMapping(value = "customers/{id}")
	public ResponseEntity<Customer> patchCustomer(@RequestBody Customer customer, @PathVariable("id") Integer id){

		customer.setId(id);
		try {

			return new ResponseEntity<>(customerService.patch(customer), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} catch (RuntimeException e) {
			logger.error("Error on patching repair: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
	
	@DeleteMapping(value = "customers/{id}")
	public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") Integer id){
		
		try {
			customerService.delete(id);	
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
		} catch (RuntimeException e) {
			logger.error("Error on deleting customer!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}	
		
	}

}
