package com.hl95.sys.action;

import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.util.SearchModelUtil;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.support.SearchMode;

import com.hl95.sys.entity.SysMenu;
import com.hl95.sys.entity.SysPermission;
import com.hl95.sys.entity.SysRole;
import com.hl95.sys.service.SysPermissionService;
import com.hl95.sys.service.SysRoleService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SysPermissionAction extends
		BusinessAction<SysPermission, SysPermissionService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysPermission sysPermission;

	private SysPermissionService sysPermissionService;

	private List<SysPermission> sysPermissionList;

	private List<SysRole> sysRoleList;

	private SysRoleService sysRoleService;

	private String resources;

	private String[] actions;

	private String _roleName;

	private String _resourceName;

	private String _action;

	public String show() throws Exception {
		this.view();
		return "show";
	}

	public String saveOne() throws Exception {
		if (getSysPermission() != null) {
			if (getSysPermission().getRoleId() != null) {
				if (getSysPermission().getRoleId().getId() != null) {
					if (StringUtils.isNotBlank(getResources())) {
						Long resourceId = (Long) ConvertUtils.convert(getResources(), Long.class);
						this.sysPermissionService.deletePermissionByRoleResourceId(getSysPermission().getRoleId().getId(), resourceId);
						if (getActions() != null && getActions().length > 0) {
							SysPermission permission;
							SysMenu menu;
							for (String action : actions) {
								permission = new SysPermission();
								permission.setRoleId(getSysPermission().getRoleId());
								permission.setUseYn(getSysPermission().getUseYn());
								menu = new SysMenu();
								menu.setId(resourceId);
								permission.setResource(menu);
								permission.setAction(action);
								this.bindEntity(permission);
								this.doSaveEntity(permission);
							}
						}
					}
				}
			}
		}
		return "saveOne";
	}

	public String save() throws Exception {
		if (getSysPermission() != null) {
			if (getSysPermission().getRoleId() != null) {
				if (getSysPermission().getRoleId().getId() != null) {
					this.sysPermissionService
							.deletePermissionByRoleId(getSysPermission()
									.getRoleId().getId());
					if (StringUtils.isNotBlank(getResources())) {
						if (getActions() != null && getActions().length > 0) {
							String[] ids = getResources().split(",");
							SysPermission permission;
							SysMenu menu;
							for (String id : ids) {
								for (String action : actions) {
									permission = new SysPermission();
									permission.setRoleId(getSysPermission()
											.getRoleId());
									permission.setUseYn(getSysPermission()
											.getUseYn());
									menu = new SysMenu();
									menu.setId((Long) ConvertUtils.convert(id,
											Long.class));
									permission.setResource(menu);
									permission.setAction(action);
									this.bindEntity(permission);
									this.doSaveEntity(permission);
								}
							}
						}
					}
				}
			}
		}
		return "save";
	}

	public String creates() throws Exception {
		this.view();
		return "creates";
	}

	public String create() throws Exception {
		this.view();
		return "create";
	}

	@Override
	public void viewCallBack(SysPermission object)  throws Exception{
		this.sysRoleList = this.sysRoleService.getSysRoleByUse();
	}

	public String doPageData() throws Exception {
		List<SysPermission> permissionList = this.doGetPageResultEntity();
		JSONObject root = new JSONObject();
		JSONArray arrayItems = new JSONArray();
		if (permissionList != null) {
			for (SysPermission permission : permissionList) {
				JSONObject Item = new JSONObject();
				Item.put("ASIN", permission.getId());
				Item.put("roleName", permission.getRoleId().getRoleNameZh());
				Item.put("resource", permission.getResource().getMenuNameZh());
				Item.put("action", permission.getAction());
				Item.put("useYn", permission.getUseYn());
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
	protected SysPermission getFromBean() {
		return getSysPermission();
	}

	@Override
	public void searchModelCallBack() {

		if (StringUtils.isNotBlank(get_roleName())) {
			getPageCountCriteria().createAlias("roleId", "roleId");
			getPageResultCriteria().createAlias("roleId", "roleId");

			getPageCountCriteria().add(SearchModelUtil.or(SearchModelUtil.getSearchCriterion("roleId.roleNameZh",
							get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.or(SearchModelUtil.getSearchCriterion("roleId.roleNameEn",
							get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.getSearchCriterion("roleId.roleNameKo",
							get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true))));
			
			getPageResultCriteria().add(SearchModelUtil.or(SearchModelUtil.getSearchCriterion("roleId.roleNameZh",
					get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.or(SearchModelUtil.getSearchCriterion("roleId.roleNameEn",
					get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.getSearchCriterion("roleId.roleNameKo",
					get_roleName(), SearchMode.LIKE_ANYWHERE_MODE,true))));		
			
		}
		if (StringUtils.isNotBlank(get_resourceName())) {
			getPageCountCriteria().createAlias("resource", "resource");
			getPageResultCriteria().createAlias("resource", "resource");

			getPageCountCriteria().add(SearchModelUtil.or(SearchModelUtil.getSearchCriterion("resource.menuNameZh",
							get_resourceName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.or(SearchModelUtil.getSearchCriterion("resource.menuNameEn",
							get_resourceName(), SearchMode.LIKE_ANYWHERE_MODE,true), SearchModelUtil.getSearchCriterion("resource.menuNameKo",
							get_resourceName(), SearchMode.LIKE_ANYWHERE_MODE,true))));
			getPageResultCriteria().add(SearchModelUtil.or(SearchModelUtil.getSearchCriterion("resource.menuNameZh",
					get_resourceName(),SearchMode.LIKE_ANYWHERE_MODE, true), SearchModelUtil.or(SearchModelUtil.getSearchCriterion("resource.menuNameEn",
					get_resourceName(),SearchMode.LIKE_ANYWHERE_MODE, true), SearchModelUtil.getSearchCriterion("resource.menuNameKo",
					get_resourceName(), SearchMode.LIKE_ANYWHERE_MODE,true))));
			
		}
		if (StringUtils.isNotBlank(get_action())) {
			if (!get_action().equals("all")) {
				getPageCountCriteria().add(
						SearchModelUtil.getSearchCriterion("action",
								get_action(), true));
				getPageResultCriteria().add(
						SearchModelUtil.getSearchCriterion("action",
								get_action(), true));
			}

		}

		getPageResultCriteria().addOrder(Order.asc("id"));

	}

	public SysPermission getSysPermission() {
		if (getProxyEntity() != null) {
			return getProxyEntity();
		}
		return sysPermission;
	}

	public void setSysPermission(SysPermission sysPermission) {
		this.sysPermission = sysPermission;
	}

	public List<SysPermission> getSysPermissionList() {
		if (getProxyEntityList() != null) {
			return getProxyEntityList();
		}
		return sysPermissionList;
	}

	public void setSysPermissionList(List<SysPermission> sysPermissionList) {
		this.sysPermissionList = sysPermissionList;
	}

	public SysPermissionService getSysPermissionService() {
		return sysPermissionService;
	}

	public void setSysPermissionService(
			SysPermissionService sysPermissionService) {
		this.sysPermissionService = sysPermissionService;
	}

	public List<SysRole> getSysRoleList() {
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

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public String get_action() {
		return _action;
	}

	public void set_action(String _action) {
		this._action = _action;
	}

	public String get_resourceName() {
		return _resourceName;
	}

	public void set_resourceName(String name) {
		_resourceName = name;
	}

	public String get_roleName() {
		return _roleName;
	}

	public void set_roleName(String name) {
		_roleName = name;
	}

}
