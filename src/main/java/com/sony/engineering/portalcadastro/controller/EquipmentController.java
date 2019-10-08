package com.sony.engineering.portalcadastro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.service.EquipmentService;

@RestController
public class EquipmentController {

	@Autowired
	EquipmentService equipmentService;
	
	@GetMapping(value = "getequip")
	public List<Equipment> getList( ) {
		
		return equipmentService.getAll();
		
	}
	
	@GetMapping(value = "getname")
	public List<Equipment> getName(){
		
		return equipmentService.getEquipNameList();
	}
}
