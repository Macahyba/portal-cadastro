package com.sony.engineering.portalcadastro.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.sony.engineering.portalcadastro.model.Equipment;

@Repository
public class EquipmentDao extends GenericDaoImpl<Equipment> {

	public List<Equipment> getEquipNameList() {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Equipment> query = cb.createQuery(Equipment.class);
		Root<Equipment> equipRoot = query.from(Equipment.class);
		query.select(equipRoot.get("name")).distinct(true);
		return getEm().createQuery(query).getResultList();
		
	}

}
