package com.marcus.auth.dao;

import com.marcus.auth.model.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface AuthRoleDao extends JpaRepository<AuthRole, Long>, JpaSpecificationExecutor<AuthRole> {

	AuthRole findByName(String name);

    AuthRole findByCode(String code);
}
