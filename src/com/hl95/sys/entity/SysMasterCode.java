package com.hl95.sys.entity;

import java.util.Set;

import org.pj.framework.business.core.entity.BusinessEntity;


public class SysMasterCode extends BusinessEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String codeId;
	
	private String codeNameZh;
	
	private String codeNameEn;
	
	private String codeNameKo;
	
	private SysMasterCode parentId;
	
	private Integer codeLevel;
	
	private Integer displayOrder;
	
	private String codeValue;
	
	private Integer childSize;

	private Set<SysMasterCode> childSysMasterCodes;
	
	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public Integer getCodeLevel() {
		return codeLevel;
	}

	public void setCodeLevel(Integer codeLevel) {
		this.codeLevel = codeLevel;
	}

	public String getCodeNameEn() {
		return codeNameEn;
	}

	public void setCodeNameEn(String codeNameEn) {
		this.codeNameEn = codeNameEn;
	}

	public String getCodeNameKo() {
		return codeNameKo;
	}

	public void setCodeNameKo(String codeNameKo) {
		this.codeNameKo = codeNameKo;
	}

	public String getCodeNameZh() {
		return codeNameZh;
	}

	public void setCodeNameZh(String codeNameZh) {
		this.codeNameZh = codeNameZh;
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

	public SysMasterCode getParentId() {
		return parentId;
	}

	public void setParentId(SysMasterCode parentId) {
		this.parentId = parentId;
	}

	

	public Set<SysMasterCode> getChildSysMasterCodes() {
		return childSysMasterCodes;
	}

	public void setChildSysMasterCodes(Set<SysMasterCode> childSysMasterCodes) {
		this.childSysMasterCodes = childSysMasterCodes;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public Integer getChildSize() {
		return childSize;
	}

	public void setChildSize(Integer childSize) {
		this.childSize = childSize;
	}
	
}
