package com.hl95.test.entity;

import org.pj.framework.business.core.entity.BusinessEntity;

public class User extends BusinessEntity{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String ip;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	private String state;
}
