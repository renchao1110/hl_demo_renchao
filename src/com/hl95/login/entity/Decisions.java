package com.hl95.login.entity;

import org.pj.framework.business.core.entity.BusinessEntity;

public class Decisions extends BusinessEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	
	private String readYn;
	
	private String url;
	
	private int menuId;

	private String sort;

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getReadYn() {
		return readYn;
	}

	public void setReadYn(String readYn) {
		this.readYn = readYn;
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
}
