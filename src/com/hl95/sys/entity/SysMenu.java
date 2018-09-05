package com.hl95.sys.entity;


import java.util.List;
import java.util.Set;

import org.pj.framework.business.core.entity.BusinessEntity;


public class SysMenu extends BusinessEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String menuId;
	
	private String menuNameZh;
	
	private String menuNameEn;
	
	private String menuNameKo;
	
	private String menuUrl;
	
	private SysMenu parentId;
	
	private Integer menuLevel;
	
	private Integer displayOrder;
	
	private List<SysMenu> indexChildSysMenus;
	private Set<SysPermission> permissions;
	
	private Set<SysMenu> childSysMenus;

	
	public Set<SysMenu> getChildSysMenus() {
		return childSysMenus;
	}

	public void setChildSysMenus(Set<SysMenu> childSysMenus) {
		this.childSysMenus = childSysMenus;
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

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getMenuNameEn() {
		return menuNameEn;
	}

	public void setMenuNameEn(String menuNameEn) {
		this.menuNameEn = menuNameEn;
	}

	public String getMenuNameKo() {
		return menuNameKo;
	}

	public void setMenuNameKo(String menuNameKo) {
		this.menuNameKo = menuNameKo;
	}

	public String getMenuNameZh() {
		return menuNameZh;
	}

	public void setMenuNameZh(String menuNameZh) {
		this.menuNameZh = menuNameZh;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public SysMenu getParentId() {
		return parentId;
	}

	public void setParentId(SysMenu parentId) {
		this.parentId = parentId;
	}

	public Set<SysPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<SysPermission> permissions) {
		this.permissions = permissions;
	}

	

	public List<SysMenu> getIndexChildSysMenus() {
		return indexChildSysMenus;
	}

	public void setIndexChildSysMenus(List<SysMenu> indexChildSysMenus) {
		this.indexChildSysMenus = indexChildSysMenus;
	}

	
	

}
