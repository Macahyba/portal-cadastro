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

import com.sony.engineering.portalcadastro.model.Service;
import com.sony.engineering.portalcadastro.service.ServiceService;

@RestController
public class ServiceController {

	@Autowired
	ServiceService serviceService;
	
	@GetMapping(value = "services")
	public List<Service> getAll(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "description") String description) {
		
		if (StringUtils.hasText(name)) {
			return serviceService.findDistinctByName(name);
		}
		
		if (StringUtils.hasText(description)) {
			return serviceService.findDistinctByDescription(description);
		}

		return serviceService.findAll();
		
	}
	
	@GetMapping(value = "services/{id}")
	public Service getServiceById(@PathVariable("id") Integer id){
		
		return serviceService.findById(id);
	}
	
	@PostMapping(value = "services")
	public Service setService(@RequestBody Service service) {
		return serviceService.save(service);
	}
	
	@PutMapping(value = "services/{id}")
	public Service updateService(@RequestBody Service service, @PathVariable("id") Integer id) {
		service.setId(id);
		return serviceService.edit(service);
	}
	
	@DeleteMapping(value = "services/{id}")
	public void deleteService(@PathVariable("id") Integer id) {
		serviceService.delete(id);
	}
}
