package com.sony.engineering.portalcadastro.service;

import java.util.List;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.RepairFup;

public interface RepairFupService extends GenericService<RepairFup>{

	List<RepairFup> saveAll(List<RepairFup> repairFup);

	RepairFup addEquipmentToSparePart(RepairFup repairFup, Equipment equipment);
	
}
