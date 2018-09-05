package com.hl95.sys.entity;

import org.pj.framework.business.core.entity.BusinessEntity;

public class SysRoleParent extends BusinessEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private SysRole roleId;
	
	private SysRole parentId;
	

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SysRole getParentId() {
		return parentId;
	}

	public void setParentId(SysRole parentId) {
		this.parentId = parentId;
	}

	public SysRole getRoleId() {
		return roleId;
	}

	public void setRoleId(SysRole roleId) {
		this.roleId = roleId;
	}

}
