package com.hl95.sys.entity;

import java.util.Date;

import org.pj.framework.business.core.entity.BusinessEntity;


public class SysLog extends BusinessEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private SysEmployee employee;
	
	private Date logDate;
	
	private String logAction;
	
	private String logIp;

	public SysEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(SysEmployee employee) {
		this.employee = employee;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogAction() {
		return logAction;
	}

	public void setLogAction(String logAction) {
		this.logAction = logAction;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getLogIp() {
		return logIp;
	}

	public void setLogIp(String logIp) {
		this.logIp = logIp;
	}
	
	
}
