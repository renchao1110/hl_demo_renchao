package com.hl95.sys.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.pj.criterion.core.dao.hibernate.CriterionHibernateDao;

import com.hl95.login.entity.SmsYunmaClient;
import com.hl95.sys.entity.SysLog;
import com.hl95.utils.RandomID;
import com.yunma.my.YunmaClient;


public class SysLogDao extends CriterionHibernateDao<SysLog>{

	public String iregCode(String mphone){
		try{
			String sql = "select emp_id from sys_employee where mphone= ? ";
			Query query = getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, mphone);
			List<Object> list = query.list();
			if(list !=null && list.size()>0){
				String empid = (String) list.get(0);
				return empid;
			}
			/*if(empid==null){
				return -2;//没有手机号对应的账号
			}
			sql = "select DISTINCT app_key from users_info where (del_flag !='Y' || del_flag is null) and use_def='Y' and createdby = ?";
			query = getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, empid);
			String appKey = (String) query.list().get(0);
			if(appKey==null){
				return -1;//账户不可用
			}
			sql = "insert into sms_send (mphone,team_code,scontent,createdby,sms_template_id,sms_template_name,sms_from,send_time,app_key) values (?,?,?,?,?,?,?,?,?)";
			query = getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, mphone);
			query.setString(1, regCode);
			query.setString(2, "smsVerify");
			int count = query.executeUpdate();
			if(count >0){
				sql = "insert into send_regcode (mphone,reg_code,code_type) values (?,?,?)";
				query = getHibernateCurrentSession().createSQLQuery(sql);
				query.setString(0, mphone);
				query.setString(1, regCode);
				query.setString(2, "smsVerify");
				count = query.executeUpdate();
				return count;
			}*/
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ""; 
	}
	/**
	 * 判断验证码发送次数，一分钟不能超过三次
	 * @param mphone
	 * @return
	 */
	public int getCountWithRegCodeWith1Mi(String mphone){
		try{
			String sql = "select count(id) from send_regcode where mphone = ? and created > date_add(now(),interval -1 minute) ";
			Query query = getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, mphone);
			return ((BigInteger) query.list().get(0)).intValue();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	

	/**
	 * 判断今天验证码发送量
	 * @param mphone
	 * @return
	 */
	public int getCountWithRegCode(String mphone){
		try{
			String sql = "select count(id) from send_regcode where mphone = ? and created >  date_format(now(),'%Y-%m-%d') ";
			Query query = getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, mphone);
			return ((BigInteger) query.list().get(0)).intValue();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/* 判断手机号是否已经注册过  */
	public int checkphone(String phone) {
		try{
			String sql = "select count(id) num from sys_employee where mphone =? ";
			Query query = getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, phone);
			return ((BigInteger) query.list().get(0)).intValue();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	/* 获取登录跳转地址 */
	public String linkUrl(){
		String sql = "select code_value from sys_master_code where code_id='login_link' ";
		Query query = this.getHibernateCurrentSession().createSQLQuery(sql);
		return (String) query.list().get(0);
	}
	/* 判断账户是否正常登陆  */
	public int codeCount(String account, String codecount,String npwd) {
		try{
			String sql = "select count(id) from login_log where account = ? and login_code = ? and created >= date_sub(now(), interval 30 SECOND) " +
					" and mm_code = ? ";
			Query query = getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, account);
			query.setString(1, codecount);
			query.setString(2, npwd);
			return ((BigInteger) query.list().get(0)).intValue();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public SmsYunmaClient getyunmaClient(){
		try{
			String sql = "select code_id,code_value from sys_master_code where code_id in (" +
					" 'smsRegParam','smsSign','sms_appkey','sms_secret') and use_yn = 'Y'";
			Query query = getHibernateCurrentSession().createSQLQuery(sql);
			List<Object[]> list = query.list();
			if(list != null && list.size() > 0){
				SmsYunmaClient client = new SmsYunmaClient();
				for(Object[] str:list){
					String skey = (String)str[0];
					String svalue = (String)str[1];
					if("sms_appkey".equals(skey)){
						client.setAppkey(svalue);
						continue;
					}else if("sms_secret".equals(skey)){
						client.setSecretkey(svalue);
						continue;
					}else if("smsRegParam".equals(skey)){
						client.setSmsRegParam(svalue);
						continue;
					}else if("smsSign".equals(skey)){
						client.setSmsSign(svalue);
						continue;
					}
					
				}
				return client;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 验证短信验证码，并返回账号等信息
	 * @param policeno
	 * @param smsVerify
	 * @return
	 */
	public String checkVerify(String policeno, String smsVerify) {
		try {
			String sql = "select count(id) from send_regcode where mphone =? and reg_code=? and created >= date_sub(now(), interval 120 SECOND)  ";
			Query query = this.getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, policeno);
			query.setString(1, smsVerify);
			int num = ((BigInteger) query.list().get(0)).intValue();
			if(num <=0){
				return "";
			}
			sql = "select account from sys_admin a left join sys_employee b on a.emp_id=b.id" +
				  " where b.mphone=? and a.use_yn='Y' and b.use_yn='Y' ";
			query = this.getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, policeno);
			return (String) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getVerifySms() {
		try {
			String sql = "select code_value from sys_master_code where code_id ='sms_verify' and use_yn='Y' ";
			Query query = this.getHibernateCurrentSession().createSQLQuery(sql);
			return (String) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public int saveSmsLog(String conPhone, String regCode) {
		try {
			String sql = "insert into send_regcode (mphone,reg_code,code_type) values (?,?,?)";
			Query query = getHibernateCurrentSession().createSQLQuery(sql);
			query.setString(0, conPhone);
			query.setString(1, regCode);
			query.setString(2, "smsVerify");
			int count = query.executeUpdate();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
