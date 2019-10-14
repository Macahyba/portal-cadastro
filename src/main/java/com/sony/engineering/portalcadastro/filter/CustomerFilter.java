package com.sony.engineering.portalcadastro.filter;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.sony.engineering.portalcadastro.model.Customer;

public class CustomerFilter implements Specification<Customer>{

	public Integer id;
	public String name;
	public String fullName;
	public String cnpj;
	
	@Override
	public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

		ArrayList<Predicate> predicates = new ArrayList<Predicate>();
		
		if (StringUtils.hasText(String.valueOf(id))) {
			predicates.add(criteriaBuilder.equal(root.get("id"), id));
		}

		if (StringUtils.hasText(name)) {
			predicates.add(criteriaBuilder.equal(root.get("name"), name));
		}
		
		if (StringUtils.hasText(fullName)) {
			predicates.add(criteriaBuilder.equal(root.get("fullName"), fullName));
		}
		
		if (StringUtils.hasText(cnpj)) {
			predicates.add(criteriaBuilder.equal(root.get("cnpj"), cnpj));
		}
		
		return predicates.size() <= 0 ? null : criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

}
