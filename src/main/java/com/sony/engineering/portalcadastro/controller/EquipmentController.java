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

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.service.EquipmentService;

@RestController
public class EquipmentController {

	@Autowired
	EquipmentService equipmentService;
	
	@GetMapping(value = "equipments")
	public List<Equipment> getAll(
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "serialNumber") String serialNumber) {
		
		if (StringUtils.hasText(name)) {
			return equipmentService.findByName(name);
		}
		
		if (StringUtils.hasText(serialNumber)) {
			return equipmentService.findBySerialNumber(serialNumber);
		}

		return equipmentService.findAll();
		
	}
	
	@GetMapping(value = "equipments/{id}")
	public Equipment getEquipmentById(@PathVariable("id") Integer id){
		
		return equipmentService.findOne(id);
	}
	
	@PostMapping(value = "equipments")
	public Equipment setEquipment(@RequestBody Equipment equipment) {
		return equipmentService.save(equipment);
	}
	
	@PutMapping(value = "equipments/{id}")
	public Equipment updateEquipment(@RequestBody Equipment equipment, @PathVariable("id") Integer id) {
		equipment.setId(id);
		return equipmentService.edit(equipment);
	}
	
	@DeleteMapping(value = "equipments/{id}")
	public void deleteEquipment(@PathVariable("id") Integer id) {
		equipmentService.delete(id);
	}
}
