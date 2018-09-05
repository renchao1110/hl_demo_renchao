package com.hl95.sys.entity;

import org.pj.framework.business.core.entity.BusinessEntity;

public class SysSetDept extends BusinessEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private SysDepartment deptId;
	private SysEmployee empId;
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
	public SysEmployee getEmpId() {
		return empId;
	}
	public void setEmpId(SysEmployee empId) {
		this.empId = empId;
	}
	
	

}
