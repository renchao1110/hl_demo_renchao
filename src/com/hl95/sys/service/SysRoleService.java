package com.hl95.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;

import com.hl95.sys.dao.SysRoleDao;
import com.hl95.sys.entity.SysRole;


public class SysRoleService extends CriterionService<SysRole> {

	private SysRoleDao sysRoleDao;

	public SysRoleDao getSysRoleDao() {
		return sysRoleDao;
	}

	public void setSysRoleDao(SysRoleDao sysRoleDao) {
		this.sysRoleDao = sysRoleDao;
		this.setCriterionDao(this.sysRoleDao);
	}

	public List<SysRole> getSysRoleByUse() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("id"));
		return this.sysRoleDao.findByCriteria(dc);
	}

	public SysRole getComplianceCommonRoleByUse() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
		dc.add(RestrictionBuild.eq("roleId", "ROLE_COMPLIANCE_COMMON"));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("id"));
		return (SysRole) sysRoleDao.findObjectByCriteria(dc);
	}

	public SysRole getSysRoleByRoleId(String roleId) {
		return (SysRole) this.sysRoleDao
				.findObjectByCriteria(RestrictionBuild.eq("roleId", roleId));
	}
	
	public SysRole getSysRoleUseByRoleId(String roleId) {
		return (SysRole) this.sysRoleDao
				.findObjectByCriteria(RestrictionBuild.eq("roleId", roleId),
						RestrictionBuild.eq("useYn", "Y"));
	}

	public SysRole getSysRoleByRoleNameZh(String roleNameZh) {
		return (SysRole) this.sysRoleDao.findObjectByCriteria(RestrictionBuild.eq("roleNameZh",
				roleNameZh));
	}

	public SysRole getSysRoleByUse(Long id) {
		return (SysRole) this.sysRoleDao.findObjectByCriteria(RestrictionBuild.eq("id", id),
				RestrictionBuild.eq("useYn", "Y"));
	}
	
	public SysRole getSysRoleById(long id) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
		dc.add(RestrictionBuild.in("id", new Long[]{id}));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		return (SysRole) this.sysRoleDao.findObjectByCriteria(dc);
	}
	/**
	 * 根据checkRoleId判断是否在roleIds集合里
	 * @param roleIds
	 * @param checkRoleId
	 * @return
	 */
	public boolean getCheckRole(Long[] roleIds,long checkRoleId){
		if(roleIds != null){
			for(long roid:roleIds){
				if(roid == checkRoleId){
					return true;
				}
			}
		}
		return false;
	}

}
