package com.hl95.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.dao.hibernate.CriterionHibernateDao;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;

import com.hl95.sys.entity.SysMasterCode;


public class SysMasterCodeDao extends CriterionHibernateDao<SysMasterCode>{
	
	public SysMasterCode getSysMasterCodeByCodeId(String codeId){
		return (SysMasterCode) findObjectByCriteria(RestrictionBuild.eq("codeId", codeId));
	}
	
	
	public List<SysMasterCode> getChildMasterCodebyPids(String parentCodeId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.createAlias("parentId", "parentId");
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("parentId.codeId", parentCodeId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.findByCriteria(dc);
	}
	
	/**
	 * 获取sip短号码
	 * @return
	 */
	public synchronized Integer getSipShortNum(){
		Session session = null;
		try{
			String sql = "select code_value+1 from SYS_MASTER_CODE  where code_id = 'sip_set_seq'";
			session = getHibernateSession();
			Query query = session.createSQLQuery(sql);
			List<Integer> list = query.list();
			if(list != null && list.size() >0){
				int seq = ((Integer)list.get(0)).intValue();
				sql = "update SYS_MASTER_CODE set code_value=code_value+1 where code_id = 'sip_set_seq' ";
				query = session.createSQLQuery(sql);
				int upNum = query.executeUpdate();
				if(upNum >0){
					return seq;
				}
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
	
	public double getCusScale(){
		Session session = null;
		try{
			String sql = "select code_value from SYS_MASTER_CODE  where code_id = 'cus_scale'";
			session = getHibernateSession();
			Query query = session.createSQLQuery(sql);
			List<Double> list = query.list();
			if(list != null && list.size() >0){
				return list.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session != null){
				session.close();
			}
		}
		return 0.7;
	}
	
}
