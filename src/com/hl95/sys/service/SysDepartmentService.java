package com.hl95.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.dao.hibernate.support.ProjectionBuild;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;
import org.pj.criterion.core.util.DateFormatUtil;
import org.pj.criterion.core.util.SearchModelUtil;
import org.pj.criterion.core.util.StringUtils;
import org.pj.framework.business.support.SearchMode;
import org.pj.framework.business.web.session.UserInfo;

import com.hl95.sys.dao.SysDepartmentDao;
import com.hl95.sys.entity.SysDepartment;


public class SysDepartmentService extends CriterionService<SysDepartment> {

	private SysDepartmentDao sysDepartmentDao;

	public SysDepartmentDao getSysDepartmentDao() {
		return sysDepartmentDao;
	}

	public void setSysDepartmentDao(SysDepartmentDao sysDepartmentDao) {
		this.sysDepartmentDao = sysDepartmentDao;
		this.setCriterionDao(this.sysDepartmentDao);
	}

	public SysDepartment getSysDepartmentByDepartmentId(String departmentId) {
		return (SysDepartment) this.sysDepartmentDao
				.findObjectByCriteria(RestrictionBuild.eq("departmentId",
						departmentId));
	}

	public SysDepartment getSysDepartmentByDepartmentNameZh(
			String departmentNameZh) {
		return (SysDepartment) this.sysDepartmentDao
				.findObjectByCriteria(RestrictionBuild.eq("departmentNameZh",
						departmentNameZh));
	}
	
	public SysDepartment getSysDepartmentByShortNameZh(String shortNameZh) {
		return (SysDepartment) this.sysDepartmentDao
				.findObjectByCriteria(RestrictionBuild.eq("shortNameZh",
						shortNameZh));
	}

	public List<SysDepartment> getSysDepartmentByDepartmentName(
			String departmentName) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(SearchModelUtil.or(SearchModelUtil.getSearchCriterion(
				"departmentNameZh", departmentName,
				SearchMode.LIKE_ANYWHERE_MODE, true), SearchModelUtil.or(
				SearchModelUtil.getSearchCriterion("departmentNameEn",
						departmentName, SearchMode.LIKE_ANYWHERE_MODE, true),
				SearchModelUtil.getSearchCriterion("departmentNameKo",
						departmentName, SearchMode.LIKE_ANYWHERE_MODE, true))));
		return this.sysDepartmentDao.findByCriteria(dc);
	}

	public List<SysDepartment> getDepartmentsByUse() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		return this.sysDepartmentDao.findByCriteria(dc);
	}

	public List<SysDepartment> getRootDepartments() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.eq("departmentLevel", Integer.valueOf(0)));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysDepartmentDao.findByCriteria(dc);
	}

	public List<SysDepartment> getRootDepartmentsManager() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.eq("departmentLevel", Integer.valueOf(0)));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysDepartmentDao.findByCriteria(dc);
	}

	/**
	 * 传入父级ID 参看是否有无Childs
	 * 
	 * @param parentId
	 * @return
	 */
	public boolean hasChilds(Long parentId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.setProjection(ProjectionBuild.rowCount());
		Integer rowCount = (Integer) this.sysDepartmentDao
				.findObjectByCriteria(dc);
		if (rowCount.intValue() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean hasChildsByUse(Long parentId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.setProjection(ProjectionBuild.rowCount());
		Integer rowCount = (Integer) this.sysDepartmentDao
				.findObjectByCriteria(dc);
		if (rowCount.intValue() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public List<SysDepartment> getChildDepartments(Long parentId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysDepartmentDao.findByCriteria(dc);
	}

	public List<SysDepartment> getChildDepartmentsManager(Long parentId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysDepartmentDao.findByCriteria(dc);
	}
	
	public List<SysDepartment> getOwnAndChildDept(Long parentId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.or(RestrictionBuild.eq("parentId.id", parentId),
				RestrictionBuild.eq("id", parentId)));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysDepartmentDao.findByCriteria(dc);
	}

	public List<SysDepartment> getSysDepartmentListByDc(DetachedCriteria dc) {
		// TODO Auto-generated method stub
		return this.sysDepartmentDao.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<SysDepartment> getBranchDepartmentsList(String departmentId) {
//		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
//		dc.add(RestrictionBuild.eq("useYn", "Y"));
//		
//		dc.add(RestrictionBuild.or(
//					RestrictionBuild.likeStartNew("departmentNameZh", "分行"), 
//					RestrictionBuild.likeStartNew("departmentNameZh", "支行")
//				)
//			);
//		
//		dc.addOrder(Order.asc("displayOrder"));
		String[] p = new String[1];
		p[0] = "DEPARTMENT_ID";
		Object[] v=new Object[1];
		v[0]= departmentId;
		
		return this.sysDepartmentDao.getHibernateTemplate()
					.findByNamedQueryAndNamedParam("sysDepartmentListByPermission", p, v);
	}
	
	public List<SysDepartment> getDepartments(List<Long> deptIds) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.in("id", deptIds.toArray()));
		dc.add(RestrictionBuild.isNotNull("shortNameZh"));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("departmentLevel"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysDepartmentDao.findByCriteria(dc);
	}
	
	public List<SysDepartment> getDepartmentsAll() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.not(RestrictionBuild.eq("shortNameZh","IBK")));
		dc.add(RestrictionBuild.isNotNull("shortNameZh"));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("departmentLevel"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysDepartmentDao.findByCriteria(dc);
	}
	
	public List<String> getChrildDeptId(Long pid){
		Session session = this.sysDepartmentDao.getHibernateCurrentSession();
		try{
			String sql =  "select  c.bandcode " +
			" from SYS_DEPARTMENT c where c.id in (" +
			"select g.id from SYS_DEPARTMENT g  START WITH g.id = ? "+
			" CONNECT BY PRIOR g.id = g.parent_id " +
			") ";
			Query query = session.createSQLQuery(sql);
			query.setLong(0, pid);
			return query.list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Long> getChrildDeptIdEx(Long pid,List<Long> deptIdList){
		if(deptIdList != null){
			deptIdList.add(pid);
			try{
			 List<SysDepartment> list = this.getChildDepartments(pid);
			 if(list != null && list.size() > 0){
				 for(SysDepartment dept:list){
					 this.getChrildDeptIdEx(dept.getId(),deptIdList);
				 }
			 }
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deptIdList;
	}
	
	public List<SysDepartment> getChrildByDeptIdEx(SysDepartment pdept,List<SysDepartment> deptIdList){
		if(deptIdList != null){
			deptIdList.add(pdept);
			try{
			 List<SysDepartment> list = this.getChildDepartments(pdept.getId());
			 if(list != null && list.size() > 0){
				 for(SysDepartment dept:list){
					 this.getChrildByDeptIdEx(dept,deptIdList);
				 }
			 }
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deptIdList;
	}
	
	public List<SysDepartment> getOneNextChrildDeptIdEx(Long pid){
		try{
			 List<SysDepartment> list = this.getChildDepartments(pid);
			 return list;
			}catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public String getDeptListByBandcode(String bandCode,String localStr){
		Session session = this.sysDepartmentDao.getHibernateCurrentSession();
		String sql = "";
		try{
			if("ko".equals(localStr)){
				sql = "select s.department_name_ko from SYS_DEPARTMENT s where s.bandcode = ? ";
			}else{
				sql = "select s.department_name_zh from SYS_DEPARTMENT s where s.bandcode = ? ";
			}
			Query query = session.createSQLQuery(sql);
			query.setString(0, bandCode);
			List<String> list = query.list();
			if(list != null && list.size() > 0){
				return list.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getDeptByDeptId(long dId){
		Session session = this.sysDepartmentDao.getHibernateCurrentSession();
		String sql = "";
		try{
			sql = "select s.department_name_zh from SYS_DEPARTMENT s where s.id = ? ";
			Query query = session.createSQLQuery(sql);
			query.setLong(0, dId);
			List<String> list = query.list();
			if(list != null && list.size() > 0){
				return list.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public List<SysDepartment> getObjNameByBandCode(String bandCode,long pId){
		List<Long> idsList = new ArrayList<Long>(); 
		this.getChrildDeptIdEx(pId, idsList);
		DetachedCriteria dc = DetachedCriteria.forClass(SysDepartment.class);
		dc.add(RestrictionBuild.not(RestrictionBuild.eq("bandCode",bandCode)));
		dc.add(RestrictionBuild.in("id", idsList.toArray()));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		return this.sysDepartmentDao.findByCriteria(dc);
	}
	
	
	public boolean getPerFlag(long pid,long deptId){
		boolean editPerFlag = false;
		try{
			List<Long> idList = new ArrayList<Long>();
			this.getChrildDeptIdEx(deptId,idList);
			if(idList != null && idList.size() > 0){
				for(long ids:idList){
				   if(ids == pid){
					   editPerFlag = true;
					   break;
				   }
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return editPerFlag;
	}
	
	public int getCountUserByDeptId(long deptId){
		Session session = this.sysDepartmentDao.getHibernateCurrentSession();
		String sql = "";
		try{
			sql = "select count(id) from SYS_EMPLOYEE s where s.department_id = ? ";
			Query query = session.createSQLQuery(sql);
			query.setLong(0, deptId);
			return ((Integer)(query.list().get(0))).intValue();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public void saveDeptByAuto(String pdpetName,String deptName){
		Session session = this.sysDepartmentDao.getHibernateCurrentSession();
		String sql = "";
		try{
			sql = "select count(id) from SYS_DEPARTMENT s where s.department_name_zh lik %?% ";
			Query query = session.createSQLQuery(sql);
			query.setString(0, deptName);
			int countNum = ((Integer)(query.list().get(0))).intValue();
			if(countNum == 0){
				SysDepartment dept = new SysDepartment();
				dept.setDepartmentNameZh("");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SysDepartment saveDeptByAutoEx(SysDepartment pdept,String deptName,String createdBy){
		Session session = this.sysDepartmentDao.getHibernateCurrentSession();
		String sql = "";
		try{
			sql = "select count(id) from SYS_DEPARTMENT s where s.department_name_zh = ? ";
			Query query = session.createSQLQuery(sql);
			query.setString(0, deptName);
			int countNum = ((Integer)(query.list().get(0))).intValue();
			if(countNum == 0){
				SysDepartment dept = new SysDepartment();
				dept.setDepartmentNameZh(deptName);
				dept.setParentId(pdept);
				dept.setDepartmentLevel(pdept.getDepartmentLevel()+1);
				dept.setDisplayOrder(1);
				Date ndate = new Date();
				dept.setCreated(ndate);
				dept.setCreatedBy(createdBy);
				String newDeptId = DateFormatUtil.format(ndate, DateFormatUtil.YYMMDDHHMMSS);
				dept.setDepartmentId(pdept.getDepartmentId()+"_"+newDeptId);
				this.sysDepartmentDao.saveEntity(dept);
				return  dept;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean getCInfoFlag(long deptId,long cId){
		List<Long> deptIdList = new ArrayList<Long>();
		this.getChrildDeptIdEx(deptId,deptIdList);
		if(deptIdList != null && deptIdList.size() > 0){
			deptIdList.remove(deptId);
			for(long dId:deptIdList){
				if(cId == dId){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean getCInfoFlagEx(long deptId,List<Long> clist){
		List<Long> deptIdList = new ArrayList<Long>();
		this.getChrildDeptIdEx(deptId,deptIdList);
		if(deptIdList != null && deptIdList.size() > 0){
			for(long dId:deptIdList){
				boolean cflag = false;
				for(long cId:clist){
					if(cId == dId){
						cflag = true;
					}
				}
				if(!cflag){
					return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}
	
	public List<SysDepartment> getDeptListByDeptName(String deptName){
		Session session = this.sysDepartmentDao.getHibernateCurrentSession();
		String sql = "";
		try{
			sql = "select s.* from SYS_DEPARTMENT s where s.department_name_zh like ?  ";
			Query query = session.createSQLQuery(sql).addEntity(SysDepartment.class);
			query.setString(0, "%"+deptName+"%");
			return query.list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
}
