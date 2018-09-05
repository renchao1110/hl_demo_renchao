package com.hl95.sys.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pj.criterion.core.dao.hibernate.CriterionHibernateDao;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.support.encryptPassword.EncryptPassword;

import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.utils.Constants;


public class SysAdminDao extends CriterionHibernateDao<SysAdmin>{
	
	public List<SysAdmin> getSysAdminAllByUse() {
		return this.findByCriteria(RestrictionBuild.eq("useYn", "Y"));
	}
	
	public List<SysAdmin> getSysAdminAll() {
		return this.getEntityAll();
	}
	
	public SysAdmin getSysAdminByEmpId(Long empId) {
		return (SysAdmin) this.findObjectByCriteria(
				RestrictionBuild.eq(
						"employeeId.id", empId));
	}
	
	public SysAdmin getSysAdminByAccount(String account){
		return (SysAdmin) this.findObjectByCriteria(RestrictionBuild.eq("account", account));
	}
	

}
