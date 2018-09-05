package com.hl95.sys.entity;

import java.util.Set;

import org.pj.framework.business.core.entity.BusinessEntity;


public class SysRole extends BusinessEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String roleId;
	
	private String roleNameZh;
	
	private String roleNameEn;
	
	private String roleNameKo;
	
	private String roleDescription;
	
	
	
	private Set<SysRoleParent> sysRoleParents;

	private Set<SysPermission> sysPermissions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleNameEn() {
		return roleNameEn;
	}

	public void setRoleNameEn(String roleNameEn) {
		this.roleNameEn = roleNameEn;
	}

	public String getRoleNameKo() {
		return roleNameKo;
	}

	public void setRoleNameKo(String roleNameKo) {
		this.roleNameKo = roleNameKo;
	}

	public String getRoleNameZh() {
		return roleNameZh;
	}

	public void setRoleNameZh(String roleNameZh) {
		this.roleNameZh = roleNameZh;
	}

	public Set<SysRoleParent> getSysRoleParents() {
		return sysRoleParents;
	}

	public void setSysRoleParents(Set<SysRoleParent> sysRoleParents) {
		this.sysRoleParents = sysRoleParents;
	}

	

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Set<SysPermission> getSysPermissions() {
		return sysPermissions;
	}

	public void setSysPermissions(Set<SysPermission> sysPermissions) {
		this.sysPermissions = sysPermissions;
	}
	
}
