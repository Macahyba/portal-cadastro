package com.sony.engineering.portalcadastro.repository;

import com.sony.engineering.portalcadastro.model.JwtUserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JwtUserDetailsDao extends GenericDao<JwtUserDetails> {

    JwtUserDetails findDistinctByUsername(String username);

    List<JwtUserDetails> findDistinctByProfileNot(String profile);
}
