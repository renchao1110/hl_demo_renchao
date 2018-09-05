package com.hl95.sys.service;

import java.util.List;

import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;

import com.hl95.sys.dao.SysCompanyDao;
import com.hl95.sys.entity.SysCompany;


public class SysCompanyService extends CriterionService<SysCompany> {

	private SysCompanyDao sysCompanyDao;

	public SysCompanyDao getSysCompanyDao() {
		return sysCompanyDao;
	}

	public void setSysCompanyDao(SysCompanyDao sysCompanyDao) {
		this.sysCompanyDao = sysCompanyDao;
		setCriterionDao(this.sysCompanyDao);
	}

	public SysCompany getSysCompanyById(String cpnyId) {
		return (SysCompany) this.sysCompanyDao
				.findObjectByCriteria(RestrictionBuild.eq("cpnyId", cpnyId));
	}
	
	public SysCompany getSysCompanyByNameZh(String cpnyNameZh) {
		return (SysCompany) this.sysCompanyDao
				.findObjectByCriteria(RestrictionBuild.eq("cpnyNameZh", cpnyNameZh));
	}
	
	public List<SysCompany> getSysCompanyByUse(){
		return this.sysCompanyDao.findByCriteria(RestrictionBuild.eq("useYn", "Y"));
	}
	
	public List<SysCompany> getSysCompanyByNotIn(Long[] ids){
		return 
		this.sysCompanyDao.findByCriteria(RestrictionBuild.not(RestrictionBuild.in("id", ids)));
	}
}
