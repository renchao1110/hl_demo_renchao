package com.hl95.sys.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.pj.criterion.core.dao.hibernate.CriterionHibernateDao;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;

import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.utils.RandomID;



public class SysEmployeeDao extends CriterionHibernateDao<SysEmployee>{
	
	public SysEmployee getSysEmployeeByEmpIdUse(String empId) {
		DetachedCriteria dc=DetachedCriteria.forClass(SysEmployee.class);
		dc.add(RestrictionBuild.eq("empId", empId));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		return (SysEmployee) this.findObjectByCriteria();
	}
	/**
	 * 验证用户是否存在
	 * @param empId
	 * @return
	 */
	public Integer checkSysEmployeeByEmpIdUse(String empId) {
		String sql = "select count(id) from SYS_EMPLOYEE where emp_id = ?";
		Session session = null;
		try{
			session = this.getHibernateSession();
			Query query = session.createSQLQuery(sql);
			query.setString(0, empId);
			return ((Integer)query.list().get(0)).intValue();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session != null){
				session.close();
			}
		}
		return null;
	}
	
	public SysEmployee getSysEmployeeByEmpId(Long id) {
		DetachedCriteria dc=DetachedCriteria.forClass(SysEmployee.class);
		dc.add(RestrictionBuild.eq("id", id));
		return (SysEmployee) this.findObjectByCriteria(dc);
	}
	/**
	 * 获取员工姓名
	 * @param empId
	 * @return
	 */
	public Object[] getEmpNameById(Long empId){
		String sql = "select a.emp_id,a.emp_name_zh,a.emp_name_en,b.department_name_zh," +
				" a.use_yn,a.att_card from SYS_EMPLOYEE a  join SYS_DEPARTMENT b " +
				" on a.department_id = b.id where a.id = ? ";
		Query query = this.getHibernateCurrentSession().createSQLQuery(sql);
		query.setLong(0, empId);
		List<Object[]> list = query.list();
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 根据名称获取人员数据
	 * @param empName
	 * @return
	 */
	public Long getEmpIdByName(String empName){
		String sql = "select id from SYS_EMPLOYEE where emp_name_zh = ? ";
		Query query = this.getHibernateCurrentSession().createSQLQuery(sql);
		query.setString(0, empName);
		List<BigDecimal> list = query.list();
		if(list != null && list.size() > 0){
			return ((BigDecimal)list.get(0)).longValue();
		}
		return null;
	}
	
	public boolean excUpAttCardByEid(Long eid,String attCard){
		String sql = "update SYS_EMPLOYEE set ATT_CARD = ? where id = ? ";
		Query query = this.getHibernateCurrentSession().createSQLQuery(sql);
		query.setString(0, attCard);
		query.setLong(1, eid);
		int rnum = query.executeUpdate();
		if(rnum > 0){
			return true;
		}
		return false;
	}
	
	public boolean excSave(SysEmployee emp,SysAdmin sysAdmin){
		Session session = null;
		Transaction bt = null;
		try{
			session = this.getHibernateSession();
			bt = session.beginTransaction();
			session.save(emp);
			session.save(sysAdmin);
			bt.commit();
		}catch (Exception e) {
			bt.rollback();
			e.printStackTrace();
		}finally{
			if(session != null){
				session.close();
			}
		}
		return false;
	}
}
