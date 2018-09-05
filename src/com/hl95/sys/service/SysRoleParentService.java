package com.hl95.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;

import com.hl95.sys.dao.SysRoleParentDao;
import com.hl95.sys.entity.SysRoleParent;


public class SysRoleParentService extends CriterionService<SysRoleParent>{
	private SysRoleParentDao sysRoleParentDao;

	public SysRoleParentDao getSysRoleParentDao() {
		return sysRoleParentDao;
	}

	public void setSysRoleParentDao(SysRoleParentDao sysRoleParentDao) {
		this.sysRoleParentDao = sysRoleParentDao;
		this.setCriterionDao(this.sysRoleParentDao);
	}
	
	public List<SysRoleParent> getSysRoleParentByRoleId(Long roleId){
		return this.sysRoleParentDao.findByCriteria(RestrictionBuild.eq("roleId.id", roleId));
	}
	public void deleteSysRoleParentByRoleId(Long roleId){
		List<SysRoleParent> deleteRoleParent=this.getSysRoleParentByRoleId(roleId);
		this.sysRoleParentDao.deleteEntity(deleteRoleParent);
	}
	
	public void getSysRoleParent(Long roleId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysRoleParent.class);
		dc.add(RestrictionBuild.eq("roleId.id", roleId));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
	}


}
