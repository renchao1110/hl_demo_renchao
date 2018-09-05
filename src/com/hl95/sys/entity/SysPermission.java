package com.hl95.sys.entity;

import org.pj.framework.business.core.entity.BusinessEntity;

public class SysPermission extends BusinessEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private SysRole roleId;
	
	private SysMenu resource;
	
	private String action;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public SysMenu getResource() {
		return resource;
	}

	public void setResource(SysMenu resource) {
		this.resource = resource;
	}

	public SysRole getRoleId() {
		return roleId;
	}

	public void setRoleId(SysRole roleId) {
		this.roleId = roleId;
	}
	
	
}
