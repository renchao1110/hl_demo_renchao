package com.hl95.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.pj.criterion.core.dao.hibernate.support.ProjectionBuild;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;
import org.pj.framework.business.support.Permission;

import com.hl95.sys.dao.SysPermissionDao;
import com.hl95.sys.entity.SysPermission;


public class SysPermissionService extends CriterionService<SysPermission> {

	private SysPermissionDao sysPermissionDao;

	public SysPermissionDao getSysPermissionDao() {
		return sysPermissionDao;
	}

	public void setSysPermissionDao(SysPermissionDao sysPermissionDao) {
		this.sysPermissionDao = sysPermissionDao;
		this.setCriterionDao(this.sysPermissionDao);
	}

	public List getResourceByRoleId(Long roleId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysPermission.class);
		dc.add(RestrictionBuild.eq("roleId.id", roleId));
		dc.setProjection(ProjectionBuild.distinct("resource.id"));
		return this.sysPermissionDao.findGeneralByCriteria(dc);
	}

	public List<SysPermission> getResource(Long roleId) {
		return this.sysPermissionDao.findByCriteria(RestrictionBuild.eq(
				"roleId.id", roleId));
	}
	
	public void deletePermissionByRoleId(Long roleId) {
		List<SysPermission> permissions = this.getResource(roleId);
		this.sysPermissionDao.deleteEntity(permissions);
	}
	
	
	public List<SysPermission> getPermissionByResourceId(Long resourceId){
		return  this.sysPermissionDao.findByCriteria(RestrictionBuild.eq(
						"resource.id", resourceId));
	}
	public void deletePermissionByResourceId(Long resourceId){
		List<SysPermission> permissions = this.getPermissionByResourceId(resourceId);
		if(permissions==null){
			return;
		}
		if(permissions.size()<=0){
			return;
		}
		this.sysPermissionDao.deleteEntity(permissions);
	}
	public List<SysPermission> getPermissionByRoleResource(Long roleId,Long resourceId) {
		return  this.sysPermissionDao.findByCriteria(RestrictionBuild.eq(
				"roleId.id", roleId),RestrictionBuild.eq(
						"resource.id", resourceId));
	}
	
	public void deletePermissionByRoleResourceId(Long roleId,Long resourceId) {
		List<SysPermission> permissions = this.getPermissionByRoleResource(roleId,resourceId);
		if(permissions==null){
			return;
		}
		if(permissions.size()<=0){
			return;
		}
		this.sysPermissionDao.deleteEntity(permissions);
	}
	
	public List<SysPermission> getSysPermissionInRoleIdsView(Long[] ids) {
		return this.sysPermissionDao.findByCriteria(RestrictionBuild.eq(
				"useYn", "Y"), RestrictionBuild.in("roleId.id", ids),
				RestrictionBuild.eq("action", Permission.viewPermission.toString()));
	}
	
	public List<SysPermission> getSysPermissionInRoleIdsAdd(Long[] ids) {
		return this.sysPermissionDao.findByCriteria(RestrictionBuild.eq(
				"useYn", "Y"), RestrictionBuild.in("roleId.id", ids),
				RestrictionBuild.eq("action", Permission.addPermission.toString()));
	}
	
	public List<SysPermission> getSysPermissionInRoleIdsEdit(Long[] ids) {
		return this.sysPermissionDao.findByCriteria(RestrictionBuild.eq(
				"useYn", "Y"), RestrictionBuild.in("roleId.id", ids),
				RestrictionBuild.eq("action", Permission.editPermission.toString()));
	}
	public List<SysPermission> getSysPermissionInRoleIdsDelete(Long[] ids) {
		return this.sysPermissionDao.findByCriteria(RestrictionBuild.eq(
				"useYn", "Y"), RestrictionBuild.in("roleId.id", ids),
				RestrictionBuild.eq("action", Permission.deletePermission.toString()));
	}
}
