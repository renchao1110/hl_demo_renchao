package com.hl95.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;
import org.pj.criterion.core.support.encryptPassword.EncryptPassword;

import com.hl95.sys.dao.SysAdminDao;
import com.hl95.sys.entity.SysAdmin;




public class SysAdminService extends CriterionService<SysAdmin> {

	private SysAdminDao sysAdminDao;

	public SysAdminDao getSysAdminDao() {
		return sysAdminDao;
	}

	public void setSysAdminDao(SysAdminDao sysAdminDao) {
		this.sysAdminDao = sysAdminDao;
		this.setCriterionDao(this.sysAdminDao);
	}

	public SysAdmin validateDegree(String account, String password) {
		DetachedCriteria dc=DetachedCriteria.forClass(SysAdmin.class);
		dc.createAlias("employeeId", "employeeId");
		dc.add(RestrictionBuild.eq("account", account, true));
		password = EncryptPassword.createPassword(password);
		dc.add(RestrictionBuild.eq(
						"password", password));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("employeeId.useYn", "Y"));
		return (SysAdmin) this.sysAdminDao.findObjectByCriteria(dc);
		
	}

	public SysAdmin validateDegree(String account) {
		return (SysAdmin) this.sysAdminDao
				.findObjectByCriteria(RestrictionBuild.eq("account", account));
	}
	
	public SysAdmin validateDegreeEx(String account) {
		return (SysAdmin) this.sysAdminDao
				.findObjectByCriteria(RestrictionBuild.eq("useYn", "Y"),RestrictionBuild.eq("account", account));
	}

	public SysAdmin getSysAdminByEmpIdUse(Long empId) {
		return (SysAdmin) this.sysAdminDao.findObjectByCriteria(
				RestrictionBuild.eq("useYn", "Y"), RestrictionBuild.eq(
						"employeeId.id", empId));
	}
	
	public SysAdmin getSysAdminByEmpId(Long empId) {
		return (SysAdmin) this.sysAdminDao.findObjectByCriteria(
				RestrictionBuild.eq(
						"employeeId.id", empId));
	}
	
	public SysAdmin getSysAdminByAccount(String account){
		return (SysAdmin) this.sysAdminDao.findObjectByCriteria(RestrictionBuild.eq("account", account));
	}
	
	public void deleteSysAdminByEmpId(Long empId){
		SysAdmin sysAdmin=this.getSysAdminByEmpId(empId);
		this.sysAdminDao.deleteEntity(sysAdmin);
	}
	
	public List<SysAdmin> getSysAdminListByDeptId(Long deptId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysAdmin.class);
		dc.createAlias("employeeId", "employeeId");
		//dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("employeeId.departmentId.id", deptId));
		return this.sysAdminDao.findByCriteria(dc);
	}
	
	public List<SysAdmin> getSysAdminListByDeptIdList(List<Long> deptIdList){
		DetachedCriteria dc=DetachedCriteria.forClass(SysAdmin.class);
		dc.createAlias("employeeId", "employeeId");
		//dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.in("employeeId.departmentId.id", deptIdList.toArray()));
		return this.sysAdminDao.findByCriteria(dc);
	}
	
	public List<SysAdmin> getSysAdminAllByUse() {
		return this.sysAdminDao.findByCriteria(RestrictionBuild.eq("useYn", "Y"));
	}
	/**
	 * 批量更新用户状态
	 * @param aId
	 * @param useYn
	 * @return
	 */
	public boolean updateSyaAdminUseByUseYn(List<Long> aId,String useYn){
		try{
			DetachedCriteria dc=DetachedCriteria.forClass(SysAdmin.class);
			dc.add(RestrictionBuild.in("employeeId.id", aId.toArray()));
			List<SysAdmin> list = this.sysAdminDao.findByCriteria(dc);
			if(list != null && list.size() > 0){
				for(SysAdmin admin:list){
					admin.setUseYn(useYn);
					admin.getEmployeeId().setUseYn(useYn);
					this.updateEntity(admin);
				}
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
