package com.sony.engineering.portalcadastro.service;

import com.sony.engineering.portalcadastro.model.Repair;

import java.util.List;

public interface RepairService extends GenericService<Repair>{

    List<Repair> findAllActive();

    Repair findByIdActive(Integer id);

}
