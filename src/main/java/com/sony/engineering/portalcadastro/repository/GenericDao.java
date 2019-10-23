package com.sony.engineering.portalcadastro.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericDao<T> extends JpaRepository<T, Integer>{
		
}
