package com.hl95.sys.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pj.criterion.core.dao.hibernate.CriterionHibernateDao;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;

import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysSetDept;


public class SysSetDeptDao extends CriterionHibernateDao<SysSetDept>{
	
	/**
	 * 批量保存
	 * @param list
	 */
	public void excSaveBat(Long eid,List<SysSetDept> list){
		Session session = null;
		Transaction tt = null;
		try{
			session = getHibernateSession();
			tt = session.beginTransaction();
			String sql = "delete SYS_SET_DEPT where emp_id = ?";
			Query query = session.createSQLQuery(sql);
			query.setLong(0, eid);
			query.executeUpdate();
			for(SysSetDept ssd:list){
				session.save(ssd);
			}
			tt.commit();
		}catch (Exception e) {
			e.printStackTrace();
			tt.rollback();
		}finally{
			if(session != null){
				session.close();
			};
			
		}
	}
	/**
	 * 验证是否存在
	 * @param eid
	 * @param deptId
	 * @return
	 */
	public Integer getCountByEidAndDept(Long eid,Long deptId){
		Session session = null;
		try{
			session = getHibernateCurrentSession();
			String sql = "select count(id) from SYS_SET_DEPT where emp_id = ? and dept_id = ? ";
			Query query = session.createSQLQuery(sql);
			query.setLong(0, eid);
			query.setLong(1, deptId);
			List list = query.list();
			if(list != null && list.size() > 0){
				return ((Integer)list.get(0)).intValue();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public List<Long> getDeptIdsByEid(Long eid){
		Session session = null;
		try{
			session = getHibernateCurrentSession();
			String sql = "select dept_id from SYS_SET_DEPT where emp_id = ? ";
			Query query = session.createSQLQuery(sql);
			query.setLong(0, eid);
			List<BigInteger> list = query.list();
			if(list != null && list.size() > 0){
				List<Long> rlist = new ArrayList<Long>();
				for(BigInteger did:list){
					rlist.add(did.longValue());
				}
				return rlist;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据人员id获取部门集合
	 * @param eid
	 * @return
	 */
	public List<SysDepartment> getDeptByEid(Long eid){
		Session session = null;
		try{
			session = getHibernateCurrentSession();
			String sql = "select a.* from SYS_DEPARTMENT a left join SYS_SET_DEPT b on a.id = b.dept_id where emp_id = ? ";
			Query query = session.createSQLQuery(sql).addEntity(SysDepartment.class);
			query.setLong(0, eid);
			return query.list();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
