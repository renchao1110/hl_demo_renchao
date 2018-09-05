package com.hl95.sys.entity;


import org.pj.framework.business.core.entity.BusinessEntity;

public class SysCompany extends BusinessEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String cpnyId;
	
	private String cpnyNameZh;
	
	private String cpnyNameEn;
	
	private String cpnyNameKo;
	
	private String cpnyDescription;
	
	

	

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getCpnyDescription() {
		return cpnyDescription;
	}

	public void setCpnyDescription(String cpnyDescription) {
		this.cpnyDescription = cpnyDescription;
	}

	public String getCpnyId() {
		return cpnyId;
	}

	public void setCpnyId(String cpnyId) {
		this.cpnyId = cpnyId;
	}

	public String getCpnyNameEn() {
		return cpnyNameEn;
	}

	public void setCpnyNameEn(String cpnyNameEn) {
		this.cpnyNameEn = cpnyNameEn;
	}

	public String getCpnyNameKo() {
		return cpnyNameKo;
	}

	public void setCpnyNameKo(String cpnyNameKo) {
		this.cpnyNameKo = cpnyNameKo;
	}

	public String getCpnyNameZh() {
		return cpnyNameZh;
	}

	public void setCpnyNameZh(String cpnyNameZh) {
		this.cpnyNameZh = cpnyNameZh;
	}
	
}
