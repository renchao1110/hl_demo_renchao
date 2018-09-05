package com.hl95.sys.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.pj.criterion.core.dao.hibernate.CriterionHibernateDao;

import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysEmployeeInfo;



public class SysEmployeeInfoDao extends CriterionHibernateDao<SysEmployeeInfo>{
	
	public SysEmployee getInfoByEmpId(String empId){
		Session session = null;
		try{
			session = getHibernateSession();
			String sql = "select emp_name_zh,balance,mphone from sys_employee where emp_id = ?";
			Query query = session.createSQLQuery(sql);
			query.setString(0,empId );
			List<Object[]> list = query.list();
			if(list != null && list.size() > 0){
				Object[] obj = list.get(0);
				SysEmployee emp = new SysEmployee();
				emp.setEmpId(empId);
				emp.setEmpNameZh((String)obj[0]);
				emp.setBalance(((BigDecimal)obj[1]).doubleValue());
				emp.setMphone((String)obj[2]);
				return emp;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session != null){
				session.close();
			}
		}
		return null;
	}

	
}
