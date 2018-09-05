package com.hl95.sys.entity;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.pj.framework.business.core.entity.BusinessEntity;



public class SysDepartment extends BusinessEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String departmentId;
	
	private String departmentNameZh;
	
	private String departmentNameEn;
	
	private String departmentNameKo;
	
	private SysDepartment parentId;
	
	private Integer departmentLevel;
	
	private SysCompany sysCompany;
	
	private SysEmployee employeeId;
	
	private Integer displayOrder;
	
	private String shortNameZh;
	
	private String shortNameEn;
	
	private String shortNameKo;
	
	
	private Set<SysDepartment> childSysDepartments;
	
	private Set<SysEmployee> inEmployees;
	
	
	private String address;//地址
	private String insidePhone;//内线电话
	private String phone;//外线电话
	private String fax;//传真
	private String agenciesCode;
	private String bandCode;
	private String SWIFTCode;
	private SysDepartment temp1;
	private SysDepartment temp2;
	private int tnum = 0;
	private int cnum = 0;
	private Map<Integer, Integer> smap;
	private Integer maxMonths;
	private String mflag;
	
	public String getMflag() {
		return mflag;
	}

	public void setMflag(String mflag) {
		this.mflag = mflag;
	}

	public Integer getMaxMonths() {
		return maxMonths;
	}

	public void setMaxMonths(Integer maxMonths) {
		this.maxMonths = maxMonths;
	}

	public Map<Integer, Integer> getSmap() {
		return smap;
	}

	public void setSmap(Map<Integer, Integer> smap) {
		this.smap = smap;
	}


	public int getCnum() {
		return cnum;
	}

	public void setCnum(int cnum) {
		this.cnum = cnum;
	}

	public int getTnum() {
		return tnum;
	}

	public void setTnum(int tnum) {
		this.tnum = tnum;
	}

	public SysDepartment getTemp1() {
		return temp1;
	}

	public void setTemp1(SysDepartment temp1) {
		this.temp1 = temp1;
	}

	public SysDepartment getTemp2() {
		return temp2;
	}

	public void setTemp2(SysDepartment temp2) {
		this.temp2 = temp2;
	}

	public Set<SysDepartment> getChildSysDepartments() {
		return childSysDepartments;
	}

	public void setChildSysDepartments(Set<SysDepartment> childSysDepartments) {
		this.childSysDepartments = childSysDepartments;
	}

	
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDepartmentLevel() {
		return departmentLevel;
	}

	public void setDepartmentLevel(Integer departmentLevel) {
		this.departmentLevel = departmentLevel;
	}

	public String getDepartmentNameEn() {
		return departmentNameEn;
	}

	public void setDepartmentNameEn(String departmentNameEn) {
		this.departmentNameEn = departmentNameEn;
	}

	public String getDepartmentNameKo() {
		return departmentNameKo;
	}

	public void setDepartmentNameKo(String departmentNameKo) {
		this.departmentNameKo = departmentNameKo;
	}

	public String getDepartmentNameZh() {
		return departmentNameZh;
	}

	public void setDepartmentNameZh(String departmentNameZh) {
		this.departmentNameZh = departmentNameZh;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SysDepartment getParentId() {
		return parentId;
	}

	public void setParentId(SysDepartment parentId) {
		this.parentId = parentId;
	}

	public SysCompany getSysCompany() {
		return sysCompany;
	}

	public void setSysCompany(SysCompany sysCompany) {
		this.sysCompany = sysCompany;
	}

	

	public SysEmployee getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(SysEmployee employeeId) {
		this.employeeId = employeeId;
	}

	public Set<SysEmployee> getInEmployees() {
		return inEmployees;
	}

	public void setInEmployees(Set<SysEmployee> inEmployees) {
		this.inEmployees = inEmployees;
	}

	
	

	

	public String getShortNameEn() {
		return shortNameEn;
	}

	public void setShortNameEn(String shortNameEn) {
		this.shortNameEn = shortNameEn;
	}

	public String getShortNameKo() {
		return shortNameKo;
	}

	public void setShortNameKo(String shortNameKo) {
		this.shortNameKo = shortNameKo;
	}

	public String getShortNameZh() {
		return shortNameZh;
	}

	public void setShortNameZh(String shortNameZh) {
		this.shortNameZh = shortNameZh;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAgenciesCode() {
		return agenciesCode;
	}

	public void setAgenciesCode(String agenciesCode) {
		this.agenciesCode = agenciesCode;
	}

	public String getBandCode() {
		return bandCode;
	}

	public void setBandCode(String bandCode) {
		this.bandCode = bandCode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getInsidePhone() {
		return insidePhone;
	}

	public void setInsidePhone(String insidePhone) {
		this.insidePhone = insidePhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSWIFTCode() {
		return SWIFTCode;
	}

	public void setSWIFTCode(String code) {
		SWIFTCode = code;
	}
	
}
