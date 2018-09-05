package com.hl95.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;
import org.pj.criterion.core.support.encryptPassword.EncryptPassword;

import com.hl95.sys.dao.SysAdminDao;
import com.hl95.sys.dao.SysSetDeptDao;
import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysSetDept;




public class SysSetDeptService extends CriterionService<SysSetDept> {

	private SysSetDeptDao sysSetDeptDao;

	public SysSetDeptDao getSysSetDeptDao() {
		return sysSetDeptDao;
	}

	public void setSysSetDeptDao(SysSetDeptDao sysSetDeptDao) {
		this.sysSetDeptDao = sysSetDeptDao;
		this.setCriterionDao(this.sysSetDeptDao);

	}
	
	
	public void excSaveBat(Long eid,List<Long> deptIds,String createdBy){
		List<SysSetDept> list = new ArrayList<SysSetDept>();
		Date nowDate = new Date(); 
		for(Long deptId:deptIds){
			SysSetDept ssd = new SysSetDept();
			SysEmployee emp = new SysEmployee();
			emp.setId(eid);
			ssd.setEmpId(emp);
			SysDepartment dept = new SysDepartment();
			dept.setId(deptId);
			ssd.setDeptId(dept);
			ssd.setCreated(nowDate);
			ssd.setCreatedBy(createdBy);
			list.add(ssd);
		}
		this.sysSetDeptDao.excSaveBat(eid,list);
	}
	
	
	public Integer getCountByEidAndDept(Long eid,Long deptId){
		return this.sysSetDeptDao.getCountByEidAndDept(eid	, deptId);
	}
	/**
	 * 获取用户下的部门集合
	 * @param eid
	 * @return
	 */
	public List<Long> getDeptIdsByEid(Long eid){
		return this.sysSetDeptDao.getDeptIdsByEid(eid);
	}
	
	public List<SysDepartment> getDeptByEid(Long eid){
		return this.sysSetDeptDao.getDeptByEid(eid);
	}
	
	


}
