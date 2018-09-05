package com.hl95.sys.entity;

import java.util.Set;

import org.pj.framework.business.core.entity.BusinessEntity;


public class SysDeptRole extends BusinessEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private SysDepartment deptId;
	
	private SysRole roleId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SysDepartment getDeptId() {
		return deptId;
	}

	public void setDeptId(SysDepartment deptId) {
		this.deptId = deptId;
	}

	public SysRole getRoleId() {
		return roleId;
	}

	public void setRoleId(SysRole roleId) {
		this.roleId = roleId;
	}
	
	
	
	
}
