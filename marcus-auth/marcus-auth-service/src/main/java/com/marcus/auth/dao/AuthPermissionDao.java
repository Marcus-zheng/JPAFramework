package com.marcus.auth.dao;

import com.marcus.auth.model.AuthPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;


public interface AuthPermissionDao extends JpaRepository<AuthPermission, Long> {

	AuthPermission findByCode(String code);

	List<AuthPermission> findByCodeIn(List<String> codeList);

	@Query(value = "SELECT e FROM auth_permission e LEFT JOIN auth_role_permission arp ON e.id = arp.permissionId " +
			"LEFT JOIN auth_user_role aur ON arp.roleId = aur.roleId " +
			"WHERE aur.userId = ?1 AND e.resourceType = ?2", nativeQuery = true)
	List<AuthPermission> getSystemMenus(Long userId, String resourceType);

	@Query(value = "SELECT e FROM auth_permission e LEFT JOIN auth_role_permission arp ON e.id = arp.permissionId " +
			"LEFT JOIN auth_user_role aur ON arp.roleId = aur.roleId " +
			"WHERE aur.userId = ?1 AND e.resourceType = ?2 AND e.parentId = ?3", nativeQuery = true)
	List<AuthPermission> getChildMenus(Long userId, String resourceType, Long parentId);

	@Query(value = "SELECT e.code FROM auth_permission e LEFT JOIN auth_role_permission arp ON e.id = arp.permissionId " +
			"WHERE arp.roleId = ?1", nativeQuery = true)
	List<String> getRoleMenuCodes(Long roleId);
}
