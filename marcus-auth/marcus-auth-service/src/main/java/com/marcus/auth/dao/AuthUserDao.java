package com.marcus.auth.dao;

import com.marcus.auth.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthUserDao extends JpaRepository<AuthUser, Long>, JpaSpecificationExecutor<AuthUser> {

	AuthUser findByLoginName(String loginName);
}
