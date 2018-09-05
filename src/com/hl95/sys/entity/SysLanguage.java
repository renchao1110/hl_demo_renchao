package com.hl95.sys.entity;


import org.pj.framework.business.core.entity.BusinessEntity;

public class SysLanguage extends BusinessEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	

	
	private String languageId;
	
	private String languageZh;
	
	private String languageEn;
	
	private String languageKo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLanguageEn() {
		return languageEn;
	}

	public void setLanguageEn(String languageEn) {
		this.languageEn = languageEn;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public String getLanguageKo() {
		return languageKo;
	}

	public void setLanguageKo(String languageKo) {
		this.languageKo = languageKo;
	}

	public String getLanguageZh() {
		return languageZh;
	}

	public void setLanguageZh(String languageZh) {
		this.languageZh = languageZh;
	}


	
	

}
