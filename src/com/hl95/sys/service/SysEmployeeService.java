package com.hl95.sys.service;

import java.util.List;
import java.util.Map;


import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.dao.hibernate.support.ProjectionBuild;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;

import com.hl95.sys.dao.SysEmployeeDao;
import com.hl95.sys.entity.SysEmployee;


public class SysEmployeeService extends CriterionService<SysEmployee> {

	private SysEmployeeDao sysEmployeeDao;

	public SysEmployeeDao getSysEmployeeDao() {
		return sysEmployeeDao;
	}

	public void setSysEmployeeDao(SysEmployeeDao sysEmployeeDao) {
		this.sysEmployeeDao = sysEmployeeDao;
		this.setCriterionDao(this.sysEmployeeDao);
	}

	public SysEmployee getSysEmployeeByEmpId(String empId) {
		return (SysEmployee) this.sysEmployeeDao
				.findObjectByCriteria(RestrictionBuild.eq("empId", empId));
	}

	public SysEmployee getSysEmployeeByEmpNameZh(String empNameZh) {
		return (SysEmployee) this.sysEmployeeDao
				.findObjectByCriteria(RestrictionBuild.eq("empNameZh",
						empNameZh));
	}

	public SysEmployee getSysEmployeeById(Long id) {
		return (SysEmployee) this.sysEmployeeDao
				.findObjectByCriteria(RestrictionBuild.eq("id", id));
	}

	



	public List<SysEmployee> getSysEmployeeListByDeptId(Long deptId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("departmentId.id", deptId));
		dc.addOrder(Order.asc("empNameZh"));
		return this.sysEmployeeDao.findByCriteria(dc);
	}

	public List<SysEmployee> getSysEmployeeListByDeptIdNotInId(Long deptId,
			Long[] notInId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("departmentId.id", deptId));
		dc.add(RestrictionBuild.not(RestrictionBuild.in("id", notInId)));
		dc.addOrder(Order.asc("empNameZh"));
		return this.sysEmployeeDao.findByCriteria(dc);
	}

	public List<SysEmployee> getSysEmployeeListByEmpName(String empName) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.add(RestrictionBuild.likeAnyWhere("empNameZh", empName));
		dc.addOrder(Order.asc("empNameZh"));
		return this.sysEmployeeDao.findByCriteria(dc);
	}

	public List<SysEmployee> getSysEmployeeListByDc(DetachedCriteria dc) {
		return this.sysEmployeeDao.findByCriteria(dc);
	}

	public List<SysEmployee> getEmployeesByUse() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		return this.sysEmployeeDao.findByCriteria(dc);
	}

	public List<SysEmployee> getEntityAllExcept() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.add(RestrictionBuild.not(RestrictionBuild.eq("id", Long
				.valueOf("927"))));
		dc.add(RestrictionBuild.not(RestrictionBuild.eq("id", Long
				.valueOf("13"))));
		dc.add(RestrictionBuild.not(RestrictionBuild.eq("id", Long
				.valueOf("15"))));
		return this.sysEmployeeDao.findByCriteria(dc);
	}

	public Integer getEmpCountByDeptId(Long deptId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.add(RestrictionBuild.eq("departmentId.id", deptId));
		dc.setProjection(ProjectionBuild.rowCount());
		return (Integer) this.sysEmployeeDao.findObjectByCriteria(dc);
	}


	@SuppressWarnings("unchecked")
	public List<SysEmployee> getSysEmployeeList(String qy, String[] p,
			Object[] v) {
		// return this.reqInBoxDao.findByNamedQuery(qy,p,v);
		return this.sysEmployeeDao.getHibernateTemplate()
				.findByNamedQueryAndNamedParam(qy, p, v);
		// return this.reqInBoxDao.getHibernateTemplate().findByNamedQuery(qy,
		// p, v);

	}

	

	public List<SysEmployee> getEmployeesByDc(DetachedCriteria dc) {
		return this.sysEmployeeDao.findByCriteria(dc);
	}
	
	public List<SysEmployee> getCheckList(Long id,List<Long> deptIds){
		try{
			DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
			dc.add(RestrictionBuild.eq("id", id));
			dc.add(RestrictionBuild.in("departmentId.id", deptIds.toArray()));
			return this.sysEmployeeDao.findByCriteria(dc);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据名称返回人员集合
	 * @param empName
	 * @return
	 */
	public List<SysEmployee> getListByName(String empName,List<Long> deptIdList){
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployee.class);
		dc.add(RestrictionBuild.or(RestrictionBuild.likeAnyWhere("empId", empName,true),
				RestrictionBuild.or(RestrictionBuild.likeAnyWhere("empNameZh", empName), RestrictionBuild.likeAnyWhere("empNameEn", empName,true))
				));
		dc.add(RestrictionBuild.in("departmentId.id", deptIdList.toArray()));
		dc.addOrder(Order.asc("empId"));
		return this.sysEmployeeDao.findByCriteria(dc);
	}
	
	public Object[] getEmpNameById(Long empId){
		return this.sysEmployeeDao.getEmpNameById(empId);
	}
	
	public boolean excUpAttCardByEid(Long eid,String attCard){
		return this.sysEmployeeDao.excUpAttCardByEid(eid, attCard);
		
	}
}
