package com.hl95.sys.entity;


import java.sql.Clob;
import java.util.Date;
import java.util.Set;

import org.pj.framework.business.core.entity.BusinessEntity;



public class SysEmployee extends BusinessEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String empId;
	
	private String empNameZh;
	
	private String empNameEn;
	
	private String empNameKo;
	
	private SysDepartment departmentId;
	
	private Set<SysDepartment> managerDepartments;
	
	private Set<SysAdmin> sysAdmins;
	
	private SysEmployeeInfo employeeInfo;
	
	private String ShiftId;
	
	private String attCard;
	private String extCode;
	private double balance;
	private String mphone;
	
	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getExtCode() {
		return extCode;
	}

	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}


	public String getAttCard() {
		return attCard;
	}

	public void setAttCard(String attCard) {
		this.attCard = attCard;
	}

	public String getShiftId() {
		return ShiftId;
	}

	public void setShiftId(String shiftId) {
		ShiftId = shiftId;
	}

	public SysEmployeeInfo getEmployeeInfo() {
		return employeeInfo;
	}

	public void setEmployeeInfo(SysEmployeeInfo employeeInfo) {
		this.employeeInfo = employeeInfo;
	}

	public SysDepartment getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(SysDepartment departmentId) {
		this.departmentId = departmentId;
	}

	

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpNameEn() {
		return empNameEn;
	}

	public void setEmpNameEn(String empNameEn) {
		this.empNameEn = empNameEn;
	}

	public String getEmpNameKo() {
		return empNameKo;
	}

	public void setEmpNameKo(String empNameKo) {
		this.empNameKo = empNameKo;
	}

	public String getEmpNameZh() {
		return empNameZh;
	}

	public void setEmpNameZh(String empNameZh) {
		this.empNameZh = empNameZh;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public Set<SysDepartment> getManagerDepartments() {
		return managerDepartments;
	}

	public void setManagerDepartments(Set<SysDepartment> managerDepartments) {
		this.managerDepartments = managerDepartments;
	}



	public Set<SysAdmin> getSysAdmins() {
		return sysAdmins;
	}

	public void setSysAdmins(Set<SysAdmin> sysAdmins) {
		this.sysAdmins = sysAdmins;
	}


	

}
