package com.sony.engineering.portalcadastro.repository;

import com.sony.engineering.portalcadastro.model.Repair;

import java.util.List;
import java.util.Optional;

public interface RepairDao extends GenericDao<Repair>{

    List<Repair> findByActiveEquals(Boolean value);

    Optional<Repair> findByIdAndActiveEquals(Integer id, Boolean value);

}
