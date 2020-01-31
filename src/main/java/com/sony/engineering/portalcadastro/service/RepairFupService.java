package com.sony.engineering.portalcadastro.service;

import java.util.List;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.Repair;
import com.sony.engineering.portalcadastro.model.RepairFup;

public interface RepairFupService extends GenericService<RepairFup>{

	List<RepairFup> saveAll(List<RepairFup> repairFup);

	void repairFupsCheckId(Repair repair);
	
	void addEquipmentToRepairFups(List<RepairFup> repairFups, Equipment equipment);
	
}
