package com.hl95.sys.service;

import java.util.List;

import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;

import com.hl95.sys.dao.SysLanguageDao;
import com.hl95.sys.entity.SysLanguage;


public class SysLanguageService extends CriterionService<SysLanguage>{

	private SysLanguageDao sysLanguageDao;

	public SysLanguageDao getSysLanguageDao() {
		return sysLanguageDao;
	}

	public void setSysLanguageDao(SysLanguageDao sysLanguageDao) {
		this.sysLanguageDao = sysLanguageDao;
		this.setCriterionDao(this.sysLanguageDao);
	}
	
	public SysLanguage getSysLanguageById(String languageId){
		return (SysLanguage) this.sysLanguageDao.findObjectByCriteria(RestrictionBuild.eq("languageId", languageId));
	}
	
	public List<SysLanguage> getSysLanguageByUse(){
		return this.sysLanguageDao.findByCriteria(RestrictionBuild.eq("useYn", "Y"));
	}
}
