package com.hl95.sys.service;

import java.text.SimpleDateFormat;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.dao.hibernate.support.ProjectionBuild;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;

import com.hl95.sys.dao.SysEmployeeDao;
import com.hl95.sys.dao.SysEmployeeInfoDao;
import com.hl95.sys.dao.SysMasterCodeDao;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysEmployeeInfo;


public class SysEmployeeInfoService extends CriterionService<SysEmployeeInfo> {

	private SysEmployeeInfoDao sysEmployeeInfoDao;
	private SysMasterCodeDao sysMasterCodeDao;
	
	public SysMasterCodeDao getSysMasterCodeDao() {
		return sysMasterCodeDao;
	}

	public void setSysMasterCodeDao(SysMasterCodeDao sysMasterCodeDao) {
		this.sysMasterCodeDao = sysMasterCodeDao;
	}

	public SysEmployeeInfoDao getSysEmployeeInfoDao() {
		return sysEmployeeInfoDao;
	}

	public void setSysEmployeeInfoDao(SysEmployeeInfoDao sysEmployeeInfoDao) {
		this.sysEmployeeInfoDao = sysEmployeeInfoDao;
		this.setCriterionDao(this.sysEmployeeInfoDao);
	}
	/**
	 * 返回用户详细信息
	 * @param empId 用户编号
	 * @return
	 */
	public SysEmployeeInfo getObjInfoByEmpId(long empId){
		DetachedCriteria dc = DetachedCriteria.forClass(SysEmployeeInfo.class);
		dc.add(RestrictionBuild.in("empId.id",new Long[]{empId}));
		List<SysEmployeeInfo> list =this.sysEmployeeInfoDao.findByCriteria(dc);
		if(list != null && list.size() > 0){
			int i = 1;
			for(SysEmployeeInfo info :list){
				if(i > 1){
					this.sysEmployeeInfoDao.deleteEntity(info);
				}
				i++;
			}
			return list.get(0);
		}
		return null;
	}
	
	
	
	
	

}
