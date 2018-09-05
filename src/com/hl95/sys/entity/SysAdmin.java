package com.hl95.sys.entity;

import org.pj.framework.business.core.entity.BusinessEntity;

public class SysAdmin extends BusinessEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private SysEmployee employeeId;
	
	private SysRole roleId;
	
	private String account;
	
	private String password;
	
	private String changePassword;
	
	private String question;
	
	private String loginCode;
	
	
	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public SysEmployee getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(SysEmployee employeeId) {
		this.employeeId = employeeId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public SysRole getRoleId() {
		return roleId;
	}

	public void setRoleId(SysRole roleId) {
		this.roleId = roleId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChangePassword() {
		return changePassword;
	}

	public void setChangePassword(String changePassword) {
		this.changePassword = changePassword;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
}
