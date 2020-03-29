package com.sony.engineering.portalcadastro.controller;

import java.util.List;
import java.util.NoSuchElementException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sony.engineering.portalcadastro.model.Repair;
import com.sony.engineering.portalcadastro.service.RepairService;

@RestController
public class RepairController {

	private Logger logger = LoggerFactory.getLogger(RepairController.class);

	@Autowired
	public RepairController(RepairService repairService) {
		this.repairService = repairService;
	}

	private RepairService repairService;
	
	@GetMapping(value = "repairs")
	public ResponseEntity<List<Repair>> getRepairAll(){		
		
		return new ResponseEntity<>(repairService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "repairs/{id}")
	public ResponseEntity<Repair> getRepairOne(@PathVariable("id") Integer id){
		
		try {

			return new ResponseEntity<>(repairService.findById(id), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(value = "repairs")
	public ResponseEntity<Repair> postRepair(@RequestBody Repair repair) {
		
		try {
			
			repairService.save(repair);
			return new ResponseEntity<>(repair, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			logger.error("Error on creating repair: " + e); 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
	}	
	
	@PutMapping(value = "repairs/{id}")
	public ResponseEntity<Repair> updateRepair(
			@RequestBody Repair repair, @PathVariable("id") Integer id) {
		
		repair.setId(id);
		
		try {
			
			repair = repairService.edit(repair);			
			return new ResponseEntity<>(repair, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
			
		} catch (RuntimeException e) {
			logger.error("Error on updating repair: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping(value = "repairs/{id}")
	public ResponseEntity<Repair> patchRepair(
			@RequestBody Repair repair, @PathVariable("id") Integer id){

		repair.setId(id);
		
		try {
			
			repair = repairService.patch(repair);
			return new ResponseEntity<>(repair, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
						
		} catch (RuntimeException e) {
			logger.error("Error on patching repair: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}
	
	@DeleteMapping(value = "repairs/{id}")
	public ResponseEntity<Repair> deleteRepair(@PathVariable("id") Integer id){
		
		try {
			repairService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
		} catch (RuntimeException e) {
			logger.error("Error on deleting repair");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	
}
