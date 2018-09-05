package com.hl95.sys.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.pj.criterion.core.service.CriterionService;
import com.hl95.login.entity.SmsYunmaClient;
import com.hl95.sys.dao.SysLogDao;
import com.hl95.sys.entity.SysLog;
import com.yunma.my.YunmaClient;


public class SysLogService extends CriterionService<SysLog>{

	private SysLogDao sysLogDao;

	public SysLogDao getSysLogDao() {
		return sysLogDao;
	}

	public void setSysLogDao(SysLogDao sysLogDao) {
		this.sysLogDao = sysLogDao;
		this.setCriterionDao(this.sysLogDao);
	}
	
	@SuppressWarnings("unchecked")
	public List<SysLog> getIpRecently(String sql){
		SQLQuery query = null;
		query = this.sysLogDao.getSessionFactory().getCurrentSession()
				.createSQLQuery(sql);
		query.addEntity("SysLog", SysLog.class);
		Iterator iterator = query.list().iterator();
		List<SysLog> logList = new ArrayList<SysLog>();
		SysLog log = new SysLog();
		while (iterator.hasNext()) {
			log = (SysLog) iterator.next();
			logList.add(log);
		}
		return logList;
	}
	
	public String iregCode(String mphone){
		return this.sysLogDao.iregCode(mphone);
	}
	
	public int getCountWithRegCodeWith1Mi(String mphone){
		return this.sysLogDao.getCountWithRegCodeWith1Mi(mphone);
	}
	public int getCountWithRegCode(String mphone){
		return this.sysLogDao.getCountWithRegCode(mphone);
	}
	
	/* 判断手机号是否已经注册过  */
	public int checkphone(String phone){
		return this.sysLogDao.checkphone(phone);
	}
	/* 获取跳转路径 */
	public String linkUrl(){
		return this.sysLogDao.linkUrl();
	}
	/* 判断账户是否正常登陆  */
	public int codeCount(String account,String codecount,String npwd){
		return this.sysLogDao.codeCount(account,codecount,npwd);
	}
	
	public SmsYunmaClient getyunmaClient(){
		return this.sysLogDao.getyunmaClient();
	}

	public String checkVerify(String policeno, String smsVerify) {
		return this.sysLogDao.checkVerify(policeno,smsVerify);
	}

	public String getVerifySms() {
		return this.sysLogDao.getVerifySms();
	}

	public int saveSmsLog(String conPhone, String regCode) {
		return this.sysLogDao.saveSmsLog(conPhone,regCode);
	}
	
}
