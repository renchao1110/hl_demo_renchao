package com.hl95.sys.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.dao.hibernate.CriterionHibernateDao;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;

import com.hl95.sys.entity.SysDepartment;


public class SysDepartmentDao extends CriterionHibernateDao<SysDepartment>{
	
	
	
	/**
	 * 获取2级子类
	 * @param list
	 * @param dc
	 * @param rlist
	 */
	public void excSSEx(List<SysDepartment> list,DetachedCriteria dc
			,List<SysDepartment> rlist){
		for(SysDepartment dept:list){
			dc = DetachedCriteria.forClass(SysDepartment.class);
			dc.add(RestrictionBuild.eq("useYn", "Y"));
			dc.add(RestrictionBuild.eq("parentId.id", dept.getId()));
			dc.add(RestrictionBuild.in("agenciesCode",new String[]{"1","2","3"}));
			dc.addOrder(Order.asc("displayOrder"));
			List<SysDepartment> clist1 = this.findByCriteria(dc);
			boolean ff = true;
			if(clist1 != null && clist1.size() > 0){
				for(SysDepartment cdept:clist1){
					SysDepartment dept0 = new SysDepartment();
					dept0.setDepartmentNameZh(dept.getDepartmentNameZh());
					dept0.setTemp1(cdept);
					dc = DetachedCriteria.forClass(SysDepartment.class);
					dc.add(RestrictionBuild.eq("useYn", "Y"));
					dc.add(RestrictionBuild.eq("parentId.id", cdept.getId()));
					dc.add(RestrictionBuild.in("agenciesCode",new String[]{"1","2","3"}));
					dc.addOrder(Order.asc("displayOrder"));
					List<SysDepartment> clist2 = this.findByCriteria(dc);
					boolean fa = true;
					if(clist2 != null && clist2.size() > 0){
						for(SysDepartment ccdept:clist2){
							SysDepartment dept1 = new SysDepartment();
							dept1.setDepartmentNameZh(dept.getDepartmentNameZh());
							dept1.setTemp1(cdept);
							dept1.setTemp2(ccdept);
							dept1.setTnum(2);
							if(fa){
								dept1.setCnum(clist2.size());
								fa = false;
							}
							dept1.setId(ccdept.getId());
							rlist.add(dept1);
						}
					}else{
						dept0.setTnum(2);
						cdept.setTnum(1);
						if(ff){
							dept0.setCnum(clist1.size());
							ff = false;
						}
						dept0.setId(cdept.getId());
						rlist.add(dept0);
					}
				}
				
			}else{
				dept.setTnum(0);
				rlist.add(dept);
			}
		}
	}
	
	/**
	 * 判断部门是否存在
	 * @param id
	 * @return
	 */
	public Long checkObj(String rid){
		Session session = null;
		try{
			session = this.getHibernateSession();
			String sql = "select id from department where department_id = ? ";
			Query query = session.createSQLQuery(sql);
			query.setString(0, rid);
			if(query.list() != null && query.list().size() > 0){
				return ((BigInteger)query.list().get(0)).longValue();
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
