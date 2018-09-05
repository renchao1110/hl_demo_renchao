package com.hl95.sys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.dao.hibernate.support.ProjectionBuild;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;
import org.pj.criterion.core.util.StringUtils;
import org.pj.framework.business.support.Permission;

import com.hl95.sys.dao.SysMenuDao;
import com.hl95.sys.entity.SysMenu;



public class SysMenuService extends CriterionService<SysMenu>{

	private SysMenuDao sysMenuDao;

	public SysMenuDao getSysMenuDao() {
		return sysMenuDao;
	}

	public void setSysMenuDao(SysMenuDao sysMenuDao) {
		this.sysMenuDao = sysMenuDao;
		setCriterionDao(this.sysMenuDao);
	}
	
	public String getMaxMenuIdByParentId(Long id){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.add(RestrictionBuild.eq("parentId.id",id));
//		dc.setProjection(ProjectionBuild.max("menuId"));
		//tibero menuid 存储异常 改为使用menunameen存储menuid  特此修改
		dc.setProjection(ProjectionBuild.max("menuNameEn"));
		return (String) this.sysMenuDao.findObjectByCriteria(dc);
	}
	
	/**
	 * 获得ROOT 节点
	 * @return
	 */
	public List<SysMenu> getRootMenus(){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.add(RestrictionBuild.eq("menuLevel", Integer.valueOf(0)));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMenuDao.findByCriteria(dc);
	}
	
	public List<SysMenu> getModuleMenus(String modeId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
//		dc.add(RestrictionBuild.eq("menuId", modeId));
		//tibero menuid 存储异常 改为使用menunameen存储menuid  特此修改
		dc.add(RestrictionBuild.eq("menuNameEn", modeId));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMenuDao.findByCriteria(dc);
	}
	
	public List<SysMenu> getRootMenusManager(){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.add(RestrictionBuild.eq("menuLevel", Integer.valueOf(0)));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMenuDao.findByCriteria(dc);
	}
	
	/**
	 * 传入父级ID 参看是否有无Childs
	 * @param parentId
	 * @return
	 */
	public boolean hasChilds(Long parentId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.setProjection(ProjectionBuild.rowCount());
		Integer rowCount=(Integer) this.sysMenuDao.findObjectByCriteria(dc);
		if(rowCount.intValue()>0){
			return false;
		}else{
			return true;
		}
	}
	public boolean hasChildsByUse(Long parentId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.setProjection(ProjectionBuild.rowCount());
		Integer rowCount=(Integer) this.sysMenuDao.findObjectByCriteria(dc);
		if(rowCount.intValue()>0){
			return false;
		}else{
			return true;
		}
	}
	public List<SysMenu> getChildMenus(Long parentId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMenuDao.findByCriteria(dc);
	}
	
	public List<SysMenu> getRuleMenus(Long parentId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMenuDao.findByCriteria(dc);
	}
	
	public List<SysMenu> getChildMenusManager(Long parentId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMenuDao.findByCriteria(dc);
	}
	public SysMenu getSysMenuByMenuIdUse(String menuId){
		return (SysMenu) this.sysMenuDao.findObjectByCriteria(RestrictionBuild.eq("menuId", menuId),RestrictionBuild.eq("useYn", "Y"));
	}
	
	public SysMenu getSysMenuByMenuId(String menuId){
		return (SysMenu) this.sysMenuDao.findObjectByCriteria(RestrictionBuild.eq("menuId", menuId));
	}
	

	public SysMenu getSysMenuById(Long id){
		return (SysMenu) this.sysMenuDao.findObjectByCriteria(RestrictionBuild.eq("id", id));
	}
	
	public SysMenu getSysMenuByMenuNameZh(String menuNameZh){
		return (SysMenu) this.sysMenuDao.findObjectByCriteria(RestrictionBuild.eq("menuNameZh", menuNameZh));
	}
	public List<SysMenu> getSysMenuByIn(Long[] ids){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.add(RestrictionBuild.in("id", ids));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMenuDao.findByCriteria(dc);
	}
	public boolean validatePermission(Long[] roleIds,String menuId,Permission permission){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.createAlias("permissions", "permissions");
		dc.add(RestrictionBuild.eqProperty("id", "permissions.resource.id"));
		dc.add(RestrictionBuild.in("permissions.roleId.id",roleIds));
		dc.add(RestrictionBuild.eq("permissions.action", permission.toString()));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("menuId", menuId));
		dc.add(RestrictionBuild.eq("permissions.useYn", "Y"));
		List<SysMenu> menuList=this.sysMenuDao.findByCriteria(dc);
		if(menuList==null){
			return false;
		}
		if(menuList.size()<=0){
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<SysMenu> getRootSysMenuByPermission(Long[] roleIds,Permission permission){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.createAlias("permissions", "permissions");
		dc.add(RestrictionBuild.eqProperty("id", "permissions.resource.id"));
		dc.add(RestrictionBuild.in("permissions.roleId.id",roleIds));
		dc.add(RestrictionBuild.eq("permissions.action", permission.toString()));
		dc.add(RestrictionBuild.eq("menuLevel", Integer.valueOf(1)));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("permissions.useYn", "Y"));
		dc.setProjection(ProjectionBuild.distinct("id"));
		List<Long> menuIds=this.sysMenuDao.findGeneralByCriteria(dc);
		if(menuIds==null){
			return null;
		}
		if(menuIds.size()<=0){
			return null;
		}
		Long[] ids=new Long[menuIds.size()];
		int index=0;
		for(Long id:menuIds){
			ids[index]=id;
			index++;
		}
		return getSysMenuByIn(ids);
	}
	
	@SuppressWarnings("unchecked")
	public List<SysMenu> getChildSysMenuByPermission(Long[] roleIds,Long parentId,Permission permission){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.createAlias("permissions", "permissions");
		dc.add(RestrictionBuild.eqProperty("id", "permissions.resource.id"));
		dc.add(RestrictionBuild.in("permissions.roleId.id",roleIds));
		dc.add(RestrictionBuild.eq("permissions.action", permission.toString()));
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		//dc.add(RestrictionBuild.or(RestrictionBuild.eq("menuLevel", Integer.valueOf(1)), RestrictionBuild.eq("menuLevel", Integer.valueOf(2))));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("permissions.useYn", "Y"));
		dc.setProjection(ProjectionBuild.distinct("id"));
		
		List<Long> menuIds=this.sysMenuDao.findGeneralByCriteria(dc);
		if(menuIds==null){
			return null;
		}
		if(menuIds.size()<=0){
			return null;
		}
		Long[] ids=new Long[menuIds.size()];
		int index=0;
		for(Long id:menuIds){
			ids[index]=id;
			index++;
		}
		return getSysMenuByIn(ids);
	}
	
	public boolean validateRulePermission(Long[] roleIds,String menuId,Permission permission){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMenu.class);
		dc.createAlias("permissions", "permissions");
		dc.add(RestrictionBuild.eqProperty("id", "permissions.resource.id"));
		dc.add(RestrictionBuild.in("permissions.roleId.id",roleIds));
		dc.add(RestrictionBuild.eq("permissions.action", permission.toString()));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("menuId", menuId));
		dc.add(RestrictionBuild.eq("permissions.useYn", "Y"));
		List<SysMenu> menuList=this.sysMenuDao.findByCriteria(dc);
		if(menuList==null){
			return false;
		}
		if(menuList.size()<=0){
			return false;
		}
		return true;
	}
	
	public List<String> getMenuUrlListByRoleIds(Long[] roleIds){
		List<String> urllist = new ArrayList<String>();
		Session session = this.sysMenuDao.getHibernateCurrentSession();
		String sql = "select menu_url from sys_menu where id in (" +
					 "	select distinct resource_id from SYS_PERMISSION where role_id" +
					 " in(";
		 for(int i=0;i<roleIds.length;i++){
			 if(i == (roleIds.length-1)){
				 sql+="?";
			 }else{
				 sql+="?,";
			 }
		 }
			    sql +="))";
		Query query = session.createSQLQuery(sql);
		for(int i=0;i<roleIds.length;i++){
			query.setLong(i, roleIds[i]);
		}
		urllist.addAll(query.list());
		//获取特殊过滤
		sql = "select SER_NAME from SYS_SPECIAL_PERMISSIONS where USE_YN = 'Y' AND (ROLE_ID in ( 0, " ;
		for(int i=0;i<roleIds.length;i++){
			 if(i == (roleIds.length-1)){
				 sql+="?";
			 }else{
				 sql+="?,";
			 }
		 }
			    sql +=") OR ROLE_ID IS NULL )";
		query = session.createSQLQuery(sql);
		for(int i=0;i<roleIds.length;i++){
			query.setLong(i, roleIds[i]);
		}
		urllist.addAll(query.list());
		return urllist;
	}
}
