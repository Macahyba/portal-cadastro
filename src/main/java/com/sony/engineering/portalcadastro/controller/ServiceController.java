package com.sony.engineering.portalcadastro.controller;

import java.util.List;
import java.util.NoSuchElementException;

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

import com.sony.engineering.portalcadastro.model.Service;
import com.sony.engineering.portalcadastro.service.ServiceService;

@RestController
public class ServiceController {

	private Logger logger = LoggerFactory.getLogger(ServiceController.class);

	@Autowired
	public ServiceController(ServiceService serviceService) {
		this.serviceService = serviceService;
	}

	private ServiceService serviceService;
	
	@GetMapping(value = "services")
	public ResponseEntity<List<Service>> getAll(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "description") String description) {
		
		if (StringUtils.hasText(name)) {
			return new ResponseEntity<>(serviceService.findDistinctByName(name), HttpStatus.OK);
		}
		
		if (StringUtils.hasText(description)) {
			return new ResponseEntity<>(serviceService.findDistinctByDescription(description), HttpStatus.OK);
		}

		return new ResponseEntity<>(serviceService.findAll(), HttpStatus.OK);
		
	}
	
	@GetMapping(value = "services/{id}")
	public ResponseEntity<Service> getServiceById(@PathVariable("id") Integer id){
		
		try {
			return new ResponseEntity<>(serviceService.findById(id), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(value = "services")
	public ResponseEntity<Service> setService(@RequestBody Service service) {
		
		try {
			return new ResponseEntity<>(serviceService.save(service), HttpStatus.OK);
		} catch (RuntimeException e) {
			logger.error("Error on creating service: " + e); 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
		
	}
	
	@PutMapping(value = "services/{id}")
	public ResponseEntity<Service> updateService(@RequestBody Service service, @PathVariable("id") Integer id) {

		try {
		
			service.setId(id);
			return new ResponseEntity<>(serviceService.edit(service), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
			
		} catch (RuntimeException e) {
			logger.error("Error on updating service: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(value = "services/{id}")
	public ResponseEntity<Service> deleteService(@PathVariable("id") Integer id) {
		
		try {
			
			serviceService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
						
		} catch (RuntimeException e) {
			logger.error("Error on patching service: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
}
