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

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.service.EquipmentService;

@RestController
public class EquipmentController {

	private Logger logger = LoggerFactory.getLogger(EquipmentController.class);

	@Autowired
	public EquipmentController(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	private EquipmentService equipmentService;
	
	@GetMapping(value = "equipments")
	public ResponseEntity<List<Equipment>> getAll(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "serialNumber") String serialNumber) {
		
		if (StringUtils.hasText(name)) {
			return new ResponseEntity<>(equipmentService.findDistinctByName(name), HttpStatus.OK);
		}
		
		if (StringUtils.hasText(serialNumber)) {
			new ResponseEntity<>(equipmentService.findBySerialNumber(serialNumber), HttpStatus.OK);
		}

		return new ResponseEntity<>(equipmentService.findAll(), HttpStatus.OK);
		
	}
	
	@GetMapping(value = "equipments/{id}")
	public ResponseEntity<Equipment> getEquipmentById(@PathVariable("id") Integer id){
		
		try {
		
			return new ResponseEntity<>(equipmentService.findById(id), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "equipments")
	public ResponseEntity<Equipment> setEquipment(@RequestBody Equipment equipment) {
		
		try {
			return new ResponseEntity<>(equipmentService.save(equipment), HttpStatus.OK);
		} catch (RuntimeException e) {
			logger.error("Error on creating equipment!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping(value = "equipments/{id}")
	public ResponseEntity<Equipment> updateEquipment(@RequestBody Equipment equipment, @PathVariable("id") Integer id) {
		
		try {
			equipment.setId(id);
			return new ResponseEntity<>(equipmentService.edit(equipment), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@PatchMapping(value = "equipments/{id}")
	public ResponseEntity<Equipment> patchEquipment(@RequestBody Equipment equipment, @PathVariable("id") Integer id) {

		try {
			equipment.setId(id);
			return new ResponseEntity<>(equipmentService.patch(equipment), HttpStatus.OK);
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
			logger.error("Error on deleting equipment!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
}
