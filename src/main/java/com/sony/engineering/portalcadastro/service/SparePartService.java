package com.sony.engineering.portalcadastro.service;

import java.util.List;
import java.util.Set;

import com.sony.engineering.portalcadastro.model.Equipment;
import com.sony.engineering.portalcadastro.model.SparePart;

public interface SparePartService extends GenericService<SparePart>{

	List<SparePart> findDistinctByPartNumber(String partNumber);
	
	Set<SparePart> saveAll(Set<SparePart> spareParts);

	SparePart addEquipmentToSparePart(SparePart sparePart, Equipment equipment);
	
}
