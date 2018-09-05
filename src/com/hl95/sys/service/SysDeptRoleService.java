package com.hl95.sys.service;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;
import org.pj.criterion.core.support.encryptPassword.EncryptPassword;

import com.hl95.sys.dao.SysAdminDao;
import com.hl95.sys.dao.SysDeptRoleDao;
import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysDeptRole;
import com.hl95.sys.entity.SysRole;




public class SysDeptRoleService extends CriterionService<SysDeptRole> {

	private SysDeptRoleDao sysDeptRoleDao;

	public SysDeptRoleDao getSysDeptRoleDao() {
		return sysDeptRoleDao;
	}

	public void setSysDeptRoleDao(SysDeptRoleDao sysDeptRoleDao) {
		this.sysDeptRoleDao = sysDeptRoleDao;
		this.setCriterionDao(this.sysDeptRoleDao);
	}
	/**
	 * 根据部门id获取分配的角色权限
	 * @param deptId
	 * @return
	 */
	public List<SysDeptRole> getListByDeptId(long deptId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysDeptRole.class);
		dc.createAlias("deptId", "deptId");
		dc.add(RestrictionBuild.in("deptId.id", new Long[]{deptId}));
		return this.sysDeptRoleDao.findByCriteria(dc);
	}
	/**
	 * 部门与角色的关系
	 * @param roleList
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public boolean procSetRoleDept(List<Long> roleList,long deptId,String createdBy) throws Exception{
		Session session = null;
		Transaction tx = null;
		try{
			session = this.sysDeptRoleDao.getHibernateCurrentSession();
			tx = session.beginTransaction();
			String sql = "delete SYS_DEPT_ROLE  where dept_id = ? ";
			Query query = session.createSQLQuery(sql);
			query.setLong(0, deptId);
			query.executeUpdate();
			SysDepartment dept = new SysDepartment();
			dept.setId(deptId);
			for(Long roleId:roleList){
				SysDeptRole s = new SysDeptRole();
				SysRole role = new SysRole();
				role.setId(roleId);
				s.setRoleId(role);
				s.setDeptId(dept);
				s.setCreated(new Date());
				s.setCreatedBy(createdBy);
				session.save(s);
			}
			tx.commit();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}finally{
			if(session != null){
				session.close();
				session = null;
			}
		}
		return false;
	}
	
	
	

		

}
