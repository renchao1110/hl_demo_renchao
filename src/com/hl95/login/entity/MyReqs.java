package com.hl95.login.entity;

import java.util.Date;

import org.pj.framework.business.core.entity.BusinessEntity;


public class MyReqs extends BusinessEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	
	private String result;
	
	private String url;
	
	private int menuId;

	private String sort;

	private Date sendDate;
	
	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
}
