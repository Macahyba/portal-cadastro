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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.service.EquipmentService;

@RestController
public class EquipmentController {

	Logger logger = LoggerFactory.getLogger(EquipmentController.class);
	
	@Autowired
	EquipmentService equipmentService;
	
	@GetMapping(value = "equipments")
	public ResponseEntity<List<Equipment>> getAll(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "serialNumber") String serialNumber) {
		
		if (StringUtils.hasText(name)) {
			return new ResponseEntity<List<Equipment>>(equipmentService.findDistinctByName(name), HttpStatus.OK);
		}
		
		if (StringUtils.hasText(serialNumber)) {
			new ResponseEntity<List<Equipment>>(equipmentService.findBySerialNumber(serialNumber), HttpStatus.OK);
		}

		return new ResponseEntity<List<Equipment>>(equipmentService.findAll(), HttpStatus.OK);
		
	}
	
	@GetMapping(value = "equipments/{id}")
	public ResponseEntity<Equipment> getEquipmentById(@PathVariable("id") Integer id){
		
		try {
		
			return new ResponseEntity<Equipment>(equipmentService.findById(id), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "equipments")
	public ResponseEntity<Equipment> setEquipment(@RequestBody Equipment equipment) {
		
		try {
			return new ResponseEntity<Equipment>(equipmentService.save(equipment), HttpStatus.OK);	
		} catch (RuntimeException e) {
			logger.error("Error on creating equipment!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping(value = "equipments/{id}")
	public ResponseEntity<Equipment> updateEquipment(@RequestBody Equipment equipment, @PathVariable("id") Integer id) {
		
		try {
			equipment.setId(id);
			return new ResponseEntity<Equipment>(equipmentService.edit(equipment), HttpStatus.OK);			
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
	
	@DeleteMapping(value = "equipments/{id}")
	public ResponseEntity<Equipment> deleteEquipment(@PathVariable("id") Integer id) {
		try {
			equipmentService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
		} catch (RuntimeException e) {
			logger.error("Error on deleting customer");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
}
