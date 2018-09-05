package com.hl95.sys.action;


import java.util.HashSet;
import java.util.List;

import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.util.SearchModelUtil;
import org.pj.criterion.core.util.StringUtil;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.support.SearchMode;

import com.hl95.sys.entity.SysMasterCode;
import com.hl95.sys.entity.SysRole;
import com.hl95.sys.entity.SysRoleParent;
import com.hl95.sys.service.SysRoleParentService;
import com.hl95.sys.service.SysRoleService;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SysRoleAction extends BusinessAction<SysRole, SysRoleService>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysRoleService sysRoleService;
	
	private SysRoleParentService sysRoleParentService;
	
	private SysRole sysRole;
	
	private List<SysRole> sysRoleList;
	
	private String _roleId;
	
	private String _roleName;
	
	private String[] sysRoleParents;
	
	
	public String show() throws Exception{
		this.view();
		return "show";
	}
	@Override
	public void editCallBack(SysRole object) throws Exception{
		if(object!=null){
			if(object.getId()!=null){
				this.sysRoleParentService.deleteSysRoleParentByRoleId(object.getId());
			}
			if(sysRoleParents!=null){
				Set<SysRoleParent> roleParents=null;
				if(sysRoleParents.length>0){
					SysRoleParent sysRoleParent;
					SysRole parent;
					roleParents=new HashSet<SysRoleParent>();
					for(String id:sysRoleParents){
						sysRoleParent=new SysRoleParent();
						sysRoleParent.setRoleId(object);
						
						parent=new SysRole();
						parent.setId((Long) ConvertUtils.convert(id, Long.class));
						sysRoleParent.setParentId(parent);
						sysRoleParent.setUseYn("Y");
						this.bindEntity(sysRoleParent);
						roleParents.add(sysRoleParent);
					}
				}
				object.setSysRoleParents(roleParents);
				this.doSaveEntity(object);
			}
		}
	}
	
	public String onlyRoleId() throws Exception {
		if (getSysRole() != null) {
			SysRole onlyRole = this.sysRoleService.getSysRoleByRoleId(getSysRole().getRoleId());
			boolean onlyFlag = false;
			if (onlyRole == null) {
				onlyFlag = true;
			}
			if (onlyFlag) {
				writeTextByAction("true");
			} else {
				writeTextByAction("false");
			}
		}
		return "";
	}
	
	public String onlyRoleNameZh() throws Exception {
		if (getSysRole() != null) {
			String isEdit = getParameter("isEdit");

			SysRole onlyRole = this.sysRoleService.getSysRoleByRoleNameZh(getSysRole().getRoleNameZh());
			boolean onlyFlag = false;
			if (onlyRole == null) {
				onlyFlag = true;
			}else{
				if(isEdit.equals("1")){
					if(onlyRole.getId().equals(getSysRole().getId())){
						onlyFlag = true;
					}
				}
			}
			if (onlyFlag) {
				writeTextByAction("true");
			} else {
				writeTextByAction("false");
			}

		}
		return "";
	}

	
	
	@Override
	public void viewCallBack(SysRole object)  throws Exception{
		sysRoleList=this.sysRoleService.getSysRoleByUse();
	}
	
	public String doPageData() throws Exception {
		List<SysRole> roleList = this.doGetPageResultEntity();
		JSONObject root = new JSONObject();
		JSONArray arrayItems = new JSONArray();
		if (roleList != null) {
			for (SysRole role : roleList) {
				JSONObject Item = new JSONObject();
				Item.put("ASIN", role.getId().toString());
				Item.put("roleId", StringUtil.defaultIfEmpty(role.getRoleId()));
				Item.put("roleNameZh", StringUtil.defaultIfEmpty(role.getRoleNameZh()));
				Item.put("roleNameEn", StringUtil.defaultIfEmpty(role.getRoleNameEn()));
				Item.put("roleNameKo", StringUtil.defaultIfEmpty(role.getRoleNameKo()));
				Item.put("roleDescription", role.getRoleDescription());
				Item.put("useYn", StringUtil.defaultIfEmpty(role.getUseYn()));
				arrayItems.add(Item);
			}
			root.put("Items", arrayItems);
			root.put("totalCount", Integer
					.toString(getPage().getTotalRecords()));
		}
		writeTextByAction(root.toString());
		return "";
	}
	
	@Override
	protected SysRole getFromBean() {
		return getSysRole();
	}

	@Override
	public void searchModelCallBack() {
		if(StringUtils.isNotBlank(get_roleId())){
			getPageCountCriteria().add(SearchModelUtil.getSearchCriterion("roleId", get_roleId(), SearchMode.LIKE_ANYWHERE_MODE,true));
			getPageResultCriteria().add(SearchModelUtil.getSearchCriterion("roleId", get_roleId(), SearchMode.LIKE_ANYWHERE_MODE,true));
		}
		if(StringUtils.isNotBlank(get_roleName())){
			getPageCountCriteria().add(SearchModelUtil.or(SearchModelUtil.getSearchCriterion("roleNameZh",
							get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.or(SearchModelUtil.getSearchCriterion("roleNameEn",
							get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.getSearchCriterion("roleNameKo",
							get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true))));
			getPageResultCriteria().add(SearchModelUtil.or(SearchModelUtil.getSearchCriterion("roleNameZh",
					get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.or(SearchModelUtil.getSearchCriterion("roleNameEn",
					get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.getSearchCriterion("roleNameKo",
					get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true))));


		}
		
		getPageResultCriteria().addOrder(Order.asc("id"));
		
	}
	
	

	public SysRole getSysRole() {
		if(getProxyEntity()!=null){
			return getProxyEntity();
		}
		return sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}

	public List<SysRole> getSysRoleList() {
		if(getProxyEntityList()!=null){
			return getProxyEntityList();
		}
		return sysRoleList;
	}

	public void setSysRoleList(List<SysRole> sysRoleList) {
		this.sysRoleList = sysRoleList;
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	public String get_roleId() {
		return _roleId;
	}

	public void set_roleId(String id) {
		_roleId = id;
	}

	

	public String[] getSysRoleParents() {
		return sysRoleParents;
	}

	public void setSysRoleParents(String[] sysRoleParents) {
		this.sysRoleParents = sysRoleParents;
	}

	public SysRoleParentService getSysRoleParentService() {
		return sysRoleParentService;
	}

	public void setSysRoleParentService(SysRoleParentService sysRoleParentService) {
		this.sysRoleParentService = sysRoleParentService;
	}

	public String get_roleName() {
		return _roleName;
	}

	public void set_roleName(String name) {
		_roleName = name;
	}

}
