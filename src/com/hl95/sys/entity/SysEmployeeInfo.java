package com.hl95.sys.entity;


import java.sql.Clob;
import java.util.Date;
import java.util.Set;

import org.pj.framework.business.core.entity.BusinessEntity;


public class SysEmployeeInfo extends BusinessEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private SysEmployee empId;//关联ID
	
	private String phone; //联系人电话
	
	private long mphone;//移动手机号
	
	private String contact;//联系人
	
	private String address;//联系地址
	
	private String remark;//备注

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SysEmployee getEmpId() {
		return empId;
	}

	public void setEmpId(SysEmployee empId) {
		this.empId = empId;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getMphone() {
		return mphone;
	}

	public void setMphone(long mphone) {
		this.mphone = mphone;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	
	
	
	
	
	

	
	


	

}
