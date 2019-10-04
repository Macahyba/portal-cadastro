package com.sony.engineering.portalcadastro.repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.sony.engineering.portalcadastro.model.User;

@Repository
public class UserDao extends GenericDaoImpl<User> {

	public User getUserByLogin(String login) {
		
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> query = cb.createQuery(User.class);
		Root<User> quser = query.from(User.class);
		query.select(quser).where(cb.equal(quser.get("login"), login));
		
		try {
			return getEm().createQuery(query).getSingleResult();
		} catch(NoResultException noRes) {
			return null;
		}
	}
	
}
