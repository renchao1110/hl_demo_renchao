package com.hl95.sys.action;

import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.support.Permission;
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;


import com.hl95.sys.entity.SysMenu;
import com.hl95.sys.service.SysMenuService;
import com.hl95.sys.service.SysPermissionService;
import com.opensymphony.xwork.Action;

public class SysMenuAction extends BusinessAction<SysMenu, SysMenuService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysMenuService sysMenuService;

	private SysMenu sysMenu;

	private List<SysMenu> sysMenuList;

	private SysPermissionService sysPermissionService;

	private String _link;

	private boolean linkHref;

	private SysMenu linkMenu;
	
	private SysMenu clickMenu;

	public String display() throws Exception {
		this.view();
		return "display";
	}

	public String onlyMenuId() throws Exception {
		if (getSysMenu() != null) {

			SysMenu onlySysMenu = this.sysMenuService
					.getSysMenuByMenuId(getSysMenu().getMenuNameEn());
			boolean onlyFlag = false;
			if (onlySysMenu == null) {
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

	public String onlyMenuNameZh() throws Exception {
		if (getSysMenu() != null) {
			String isEdit = getParameter("isEdit");
			SysMenu onlySysMenu = this.sysMenuService
					.getSysMenuByMenuNameZh(getSysMenu().getMenuNameZh());
			boolean onlyFlag = false;
			if (onlySysMenu == null) {
				onlyFlag = true;
			} else {
				if (isEdit.equals("1")) {
					if (onlySysMenu.getId().equals(getSysMenu().getId())) {
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
		return "only";
	}

	public String getTopMenuData() throws Exception {
		UserInfo userInfo = UserSession.getUserInfo(this
				.getHttpServletRequest());
		List<SysMenu> topMenus = this.sysMenuService
				.getRootSysMenuByPermission(userInfo.getRoleIds(),
						Permission.viewPermission);
		if(topMenus !=  null && topMenus.size() > 0){
			for(SysMenu menu : topMenus){
				List<SysMenu> childMenus = this.sysMenuService.getChildSysMenuByPermission(userInfo
						.getRoleIds(), menu.getId(), Permission.viewPermission);			
				menu.setIndexChildSysMenus(childMenus);
				
			}
		}
		
//		Long parentId = (Long) ConvertUtils.convert(
//				getParameter("parentId"), Long.class);
//		clickMenu = sysMenuService.getEntityById(parentId);
		setAttribute("fMenu", topMenus.get(0));
		setProxyEntityList(topMenus);
		return "topMenuData";
	}
	
	
	public String getLeftMenu() throws Exception {
		UserInfo userInfo = UserSession.getUserInfo(this
				.getHttpServletRequest());
		List<SysMenu> topMenus = this.sysMenuService
				.getRootSysMenuByPermission(userInfo.getRoleIds(),
						Permission.viewPermission);
		if(topMenus !=  null && topMenus.size() > 0){
			int i=0;
			for(SysMenu menu : topMenus){
				i++;
				List<SysMenu> childMenus = this.sysMenuService.getChildSysMenuByPermission(userInfo
						.getRoleIds(), menu.getId(), Permission.viewPermission);			
				menu.setIndexChildSysMenus(childMenus);
				if(i==1 && childMenus != null && childMenus.size() > 0){
					setAttribute("ffMenu", childMenus.get(0).getMenuId());
				}
			}
		}
		setAttribute("fMenu", topMenus);
		return "leftMenuData";
	}
	
	public String getLeftMenuDataEx() throws Exception {
		UserInfo userInfo = UserSession.getUserInfo(this
				.getHttpServletRequest());
		List<SysMenu> topMenus = this.sysMenuService
				.getRootSysMenuByPermission(userInfo.getRoleIds(),
						Permission.viewPermission);
		if(topMenus !=  null && topMenus.size() > 0){
			for(SysMenu menu : topMenus){
				List<SysMenu> childMenus = this.sysMenuService.getChildSysMenuByPermission(userInfo
						.getRoleIds(), menu.getId(), Permission.viewPermission);			
				menu.setIndexChildSysMenus(childMenus);
				
			}
		}
		
//		Long parentId = (Long) ConvertUtils.convert(
//				getParameter("parentId"), Long.class);
//		clickMenu = sysMenuService.getEntityById(parentId);
		setAttribute("fMenu", topMenus.get(0));
		setProxyEntityList(topMenus);
		return "leftMenuData";
	}

	public String getLeftMenuData() throws Exception {
		if (StringUtils.isNotBlank(getParameter("parentId"))) {
			UserInfo userInfo = UserSession.getUserInfo(this
					.getHttpServletRequest());

			Long parentId = (Long) ConvertUtils.convert(
					getParameter("parentId"), Long.class);
			
			List<SysMenu> leftMenus = this.sysMenuService
					.getChildSysMenuByPermission(userInfo.getRoleIds(),
							parentId, Permission.viewPermission);
			SysMenu menu = sysMenuService.getEntityById(parentId);
			setAttribute("menu", menu);
			String _sef = StringUtils.defaultIfEmpty(getParameter("_sef"),"");
			setAttribute("_sef",_sef );
			if(StringUtils.isNotBlank(_sef) && leftMenus.size() > 0 ){
				setAttribute("cMenu", leftMenus.get(0));
			}
			clickMenu = sysMenuService.getEntityById(parentId);
			List<SysMenu> clickLeftMenus = this.sysMenuService
			.getChildSysMenuByPermission(userInfo.getRoleIds(),
					parentId, Permission.viewPermission);
			if (menu != null) {
				if(menu.getMenuLevel().equals(2)){
					menu = menu.getParentId();
					leftMenus = this.sysMenuService
					.getChildSysMenuByPermission(userInfo.getRoleIds(),
							menu.getId(), Permission.viewPermission);
					if(clickMenu.getMenuUrl().equals("#")){
						if (clickLeftMenus != null) {
							linkMenu = clickLeftMenus.get(0);
							while (linkMenu.getChildSysMenus().size() > 0) {
								Set<SysMenu> childSysMenus = linkMenu.getChildSysMenus();
								for (SysMenu leMenu : childSysMenus) {
									linkMenu = leMenu;
									break;
								}
							}
						}
					}else{
						linkMenu = clickMenu;
					}
				}else{
					if(menu.getMenuUrl().equals("#")){
						if (leftMenus != null) {
							linkMenu = leftMenus.get(0);
							while (linkMenu.getChildSysMenus().size() > 0) {
								Set<SysMenu> childSysMenus = linkMenu.getChildSysMenus();
								for (SysMenu leMenu : childSysMenus) {
									linkMenu = leMenu;
									break;
								}
							}
						}
					}else{
						linkMenu = menu;
					}
				}
				
			}
			
			if (StringUtils.isNotBlank(get_link())) {
				setLinkHref(true);
			}
			setProxyEntityList(leftMenus);
		}

		return "leftMenuData";
	}

	public String loadMenuTreeManager() throws Exception {
		List<SysMenu> menuTreeList = null;
		if (StringUtils.isBlank(getParameter("parentId"))) {
			return Action.INPUT;
		} else {
			if (getParameter("parentId").equals("menuRoot")) {
				menuTreeList = this.sysMenuService.getRootMenusManager();
			} else {
				Long parentId = (Long) ConvertUtils.convert(
						getParameter("parentId"), Long.class);
				menuTreeList = this.sysMenuService
						.getChildMenusManager(parentId);
			}
		}
		JSONArray arrayItems = new JSONArray();
		boolean hasChindls = false;
		if (menuTreeList != null) {
			int index = 0;
			for (SysMenu sysMenu : menuTreeList) {
				JSONObject Item = new JSONObject();
				Item.put("text", geti18nByBean(sysMenu, "menuName"));
				Item.put("id", sysMenu.getId());
				Item.put("cls", "file");
				hasChindls = this.sysMenuService.hasChilds(sysMenu.getId());
				Item.put("leaf", hasChindls);
				Item.put("allowDrag", false);
				Item.put("allowDrop", false);
				arrayItems.add(Item);
				index++;
			}

		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}

	public String loadMenuTree() throws Exception {
		List<SysMenu> menuTreeList = null;
		if (StringUtils.isBlank(getParameter("parentId"))) {
			return Action.INPUT;
		} else {
			if (getParameter("parentId").equals("menuRoot")) {
				menuTreeList = this.sysMenuService.getRootMenus();
			} else {
				Long parentId = (Long) ConvertUtils.convert(
						getParameter("parentId"), Long.class);
				menuTreeList = this.sysMenuService.getChildMenus(parentId);
			}
		}
		String selfMenuId = getParameter("_selfMenuId");
		Long selfId = null;
		if (StringUtils.isNotBlank(selfMenuId)) {
			selfId = (Long) ConvertUtils.convert(selfMenuId, Long.class);

		}
		JSONArray arrayItems = new JSONArray();
		boolean hasChindls = false;
		if (menuTreeList != null) {
			int index = 0;
			for (SysMenu sysMenu : menuTreeList) {
				if (selfId != null) {
					if (sysMenu.getId().equals(selfId)) {
						continue;
					}
				}
				JSONObject Item = new JSONObject();
				Item.put("text", geti18nByBean(sysMenu, "menuName"));
				Item.put("id", sysMenu.getId());
				Item.put("cls", "file");
				hasChindls = this.sysMenuService
						.hasChildsByUse(sysMenu.getId());
				Item.put("leaf", hasChindls);
				Item.put("allowDrag", false);
				Item.put("allowDrop", false);
				arrayItems.add(Item);
				index++;
			}

		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}

	public String loadLeftMenuTreeByPermission() throws Exception {
		List<SysMenu> menuTreeList = null;
		UserInfo userInfo = UserSession.getUserInfo(this
				.getHttpServletRequest());
		if (StringUtils.isBlank(getParameter("parentId"))) {
			return Action.INPUT;
		} else {
			Long parentId = (Long) ConvertUtils.convert(
					getParameter("parentId"), Long.class);
			menuTreeList = this.sysMenuService.getChildSysMenuByPermission(
					userInfo.getRoleIds(), parentId, Permission.viewPermission);
		}

		JSONArray arrayItems = new JSONArray();
		boolean hasChindls = false;
		if (menuTreeList != null) {
			int index = 0;
			for (SysMenu sysMenu : menuTreeList) {
				JSONObject Item = new JSONObject();
				Item.put("text", geti18nByBean(sysMenu, "menuName"));
				Item.put("url", sysMenu.getMenuUrl());
				Item.put("id", sysMenu.getId());
				Item.put("cls", "file");
				hasChindls = this.sysMenuService
						.hasChildsByUse(sysMenu.getId());
				if (hasChindls) {
					Item.put("icon", "/images/icon/childMenu.gif");
				} else {
					Item.put("icon", "/images/icon/parentMenu.gif");
				}
				Item.put("leaf", hasChindls);
				Item.put("allowDrag", false);
				Item.put("allowDrop", false);
				arrayItems.add(Item);
				index++;
			}
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}



	public String loadMenuTreeByPermission() throws Exception {
		List<SysMenu> menuTreeList = null;
		if (StringUtils.isBlank(getParameter("parentId"))) {
			return Action.INPUT;
		} else {
			if (getParameter("parentId").equals("menuRoot")) {
				menuTreeList = this.sysMenuService.getRootMenus();
			} else {
				Long parentId = (Long) ConvertUtils.convert(
						getParameter("parentId"), Long.class);
				menuTreeList = this.sysMenuService.getChildMenus(parentId);
			}
		}
		String roleId = getParameter("role.id");
		List resources = this.sysPermissionService
				.getResourceByRoleId((Long) ConvertUtils.convert(roleId,
						Long.class));
		JSONArray arrayItems = new JSONArray();
		boolean hasChindls = false;
		if (menuTreeList != null) {
			int index = 0;
			for (SysMenu sysMenu : menuTreeList) {
				JSONObject Item = new JSONObject();

				Item.put("id", sysMenu.getId());
				Item.put("cls", "file");
				hasChindls = this.sysMenuService
						.hasChildsByUse(sysMenu.getId());
				Item.put("leaf", hasChindls);
				Item.put("allowDrag", false);
				Item.put("allowDrop", false);
				if (resources != null) {
					Item.put("text", geti18nByBean(sysMenu, "menuName"));
					Item.put("exist", false);
					for (Object o : resources) {
						if (sysMenu.getId().equals(o)) {
							Item.put("text", "<font color=red>"
									+ geti18nByBean(sysMenu, "menuName")
									+ "</font>");
							Item.put("exist", true);
							break;
						}
					}
				} else {
					Item.put("text", sysMenu.getMenuNameZh());
					Item.put("exist", false);
				}
				arrayItems.add(Item);
				index++;
			}

		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}

	public String loadMenuTreeCheckByPermission() throws Exception {
		List<SysMenu> menuTreeList = null;
		if (StringUtils.isBlank(getParameter("parentId"))) {
			return Action.INPUT;
		} else {
			if (getParameter("parentId").equals("menuRoot")) {
				menuTreeList = this.sysMenuService.getRootMenus();
			} else {
				Long parentId = (Long) ConvertUtils.convert(
						getParameter("parentId"), Long.class);
				menuTreeList = this.sysMenuService.getChildMenus(parentId);
			}
		}
		String roleId = getParameter("role.id");
		List resources = this.sysPermissionService
				.getResourceByRoleId((Long) ConvertUtils.convert(roleId,
						Long.class));
		JSONArray arrayItems = new JSONArray();
		boolean hasChindls = false;
		if (menuTreeList != null) {
			int index = 0;
			for (SysMenu sysMenu : menuTreeList) {
				JSONObject Item = new JSONObject();
				Item.put("text", geti18nByBean(sysMenu, "menuName"));
				Item.put("id", sysMenu.getId());
				Item.put("cls", "file");
				hasChindls = this.sysMenuService
						.hasChildsByUse(sysMenu.getId());
				Item.put("leaf", hasChindls);
				Item.put("allowDrag", false);
				Item.put("allowDrop", false);
				if (resources != null) {
					Item.put("checked", false);
					for (Object o : resources) {
						if (sysMenu.getId().equals(o)) {
							Item.put("checked", true);
							break;
						}
					}
				} else {
					Item.put("checked", false);
				}
				arrayItems.add(Item);
				index++;
			}

		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}

	/**
	 * EDIT回调函数 判断父级ID是否正确/存在 自动生成MenuLevel
	 */
	@Override
	public void extendsEdit(SysMenu object) throws Exception {
		if (object != null) {
			if (object.getParentId() != null) {
				if (object.getParentId().getId() != null) {
					SysMenu parentId = this.sysMenuService.getEntityById(object
							.getParentId().getId());
					if (parentId != null) {
						object.setMenuLevel(Integer.valueOf(parentId
								.getMenuLevel().intValue() + 1));
					}
				} else {
					object.setParentId(null);
					object.setMenuLevel(Integer.valueOf(0));
				}
			} else {
				object.setParentId(null);
				object.setMenuLevel(Integer.valueOf(0));
			}
		}
	}

	@Override
	protected SysMenu getFromBean() {
		return getSysMenu();
	}

	@Override
	public void searchModelCallBack() {
		// TODO Auto-generated method stub

	}

	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	public SysMenu getSysMenu() {
		if (getProxyEntity() != null) {
			return getProxyEntity();
		}
		return sysMenu;
	}

	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

	public List<SysMenu> getSysMenuList() {
		if (getProxyEntityList() != null) {
			return getProxyEntityList();
		}
		return sysMenuList;
	}

	public void setSysMenuList(List<SysMenu> sysMenuList) {
		this.sysMenuList = sysMenuList;
	}

	public SysPermissionService getSysPermissionService() {
		return sysPermissionService;
	}

	public void setSysPermissionService(
			SysPermissionService sysPermissionService) {
		this.sysPermissionService = sysPermissionService;
	}


	public SysMenu getLinkMenu() {
		return linkMenu;
	}

	public void setLinkMenu(SysMenu linkMenu) {
		this.linkMenu = linkMenu;
	}

	public boolean isLinkHref() {
		return linkHref;
	}

	public void setLinkHref(boolean linkHref) {
		this.linkHref = linkHref;
	}

	public String get_link() {
		return _link;
	}

	public void set_link(String _link) {
		this._link = _link;
	}

	public SysMenu getClickMenu() {
		return clickMenu;
	}

	public void setClickMenu(SysMenu clickMenu) {
		this.clickMenu = clickMenu;
	}

}
