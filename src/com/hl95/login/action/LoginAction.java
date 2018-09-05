
package com.hl95.login.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.pj.criterion.core.action.CriterionAction;
import org.pj.criterion.core.support.encryptPassword.EncryptPassword;
import org.pj.criterion.core.util.DateFormatUtil;
import org.pj.framework.business.support.Permission;
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;
import org.pj.framework.business.web.session.UserSessionException;

import com.hl95.login.entity.SmsYunmaClient;
import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysCompany;
import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysLog;
import com.hl95.sys.entity.SysMenu;
import com.hl95.sys.entity.SysRole;
import com.hl95.sys.entity.SysRoleParent;
import com.hl95.sys.service.SysAdminService;
import com.hl95.sys.service.SysDepartmentService;
import com.hl95.sys.service.SysEmployeeService;
import com.hl95.sys.service.SysLogService;
import com.hl95.sys.service.SysMasterCodeService;
import com.hl95.sys.service.SysMenuService;
import com.hl95.sys.service.SysPermissionService;
import com.hl95.sys.service.SysRoleParentService;
import com.hl95.sys.service.SysRoleService;
import com.hl95.utils.Constants;
import com.hl95.utils.ImgVerifyCode;
import com.hl95.utils.MD5Util;
import com.hl95.utils.RandomID;
import com.yunma.my.YunmaClient;
import com.yunma.sms.entity.SmsSendRequest;


public class LoginAction extends CriterionAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysAdmin sysAdmin;

	private SysAdminService sysAdminService;

	private SysLogService sysLogService;

	private SysDepartmentService sysDepartmentService;

	private SysMasterCodeService sysMasterCodeService;

	private SysRoleParentService sysRoleParentService;

	private SysRoleService sysRoleService;

	private SysPermissionService sysPermissionService;

	private SysMenuService sysMenuService;
	
	private SysEmployeeService sysEmployeeService;

	private String errorMessage;

	private String saveId;

	private String sysLanguage;

	private String _pwd;
	
	private static int sessionMaxInactiveInterval = 1200;
	

	@Override
	public String execute() throws Exception {
		System.out.println(getSysAdmin().getAccount()+"-------------------------------");
		if (getSysAdmin() == null) {
			errorMessage = "001";
			return "input";
		}
		if (StringUtils.isBlank(getSysAdmin().getAccount())) {
			errorMessage = "002";
			return "input";
		}
		if (StringUtils.isBlank(getSysAdmin().getPassword())) {
			errorMessage = "003";
			return "input";
		}
		SysAdmin degree = this.sysAdminService.validateDegree(getSysAdmin().getAccount(), getSysAdmin().getPassword());
		if (degree == null) {
			degree = this.sysAdminService.validateDegree(getSysAdmin().getAccount());
			if (degree == null) {
				errorMessage = "004";
				registerLoginErrorLog();
				return "input";
			} else {
				if (!degree.getUseYn().equals("Y")) {
					errorMessage = "006";
					return "input";
				}
				errorMessage = "005";
				registerLoginErrorLog();
				return "input";
			}
		} else {
			if (!degree.getUseYn().equals("Y")) {
				errorMessage = "006";
				return "input";
			}
		}
		UserInfo userInfo = null;
		HttpSession session = this.getHttpServletRequest().getSession(false);
		if(session != null){
			session.invalidate();
			session = null;
		}
		if (session == null) {
			userInfo = new UserInfo();
			userInfo.setAdmin(degree);
			userInfo.setLanguagePreference(UserInfo.DEFAULT_LANGUAGE_PREFERENCE);
			userInfo.setCountryPreference(UserInfo.DEFAULT_COUNTRY_PREFERENCE);
			session = getHttpServletRequest().getSession(true);
			if (session.isNew()) {
				session.setMaxInactiveInterval(sessionMaxInactiveInterval);
			}
			session.setAttribute(UserSession.SESSION_NAME, userInfo);
		} else {
			try{
				userInfo = UserSession.getUserInfo(getHttpServletRequest());
			}catch(UserSessionException e){
				userInfo = null;
				session = null;
			}
			if (userInfo == null) {
				userInfo = new UserInfo();
				userInfo.setAdmin(degree);
				userInfo.setLanguagePreference(UserInfo.DEFAULT_LANGUAGE_PREFERENCE);
				userInfo.setCountryPreference(UserInfo.DEFAULT_COUNTRY_PREFERENCE);
				session = getHttpServletRequest().getSession(true);
				if (session.isNew()) {
					session.setMaxInactiveInterval(sessionMaxInactiveInterval);
				}
				session.setAttribute(UserSession.SESSION_NAME, userInfo);
			}
		}
		userInfo = UserSession.getUserInfo(getHttpServletRequest());
		userInfo.setLoggedIn(true);
		initialization(userInfo);
		initializationMenu(userInfo);
		registerLoginLog(userInfo);
		String page = "success";
		if (StringUtils.isNotBlank(getSaveId())) {
			if (getSaveId().equals("Y")) {
				page = "successSave";
			}
		}
		return page;
	}

	private void initializationMenu(UserInfo userInfo) {

		List<SysMenu> topMenus = sysMenuService.getRootSysMenuByPermission(userInfo.getRoleIds(),
				Permission.viewPermission);
		
	}

	

	private void registerLoginErrorLog() {
		SysLog sysLog = new SysLog();
		Date logDate = DateFormatUtil.getCurrentDate(true);
		sysLog.setLogDate(logDate);
		String ip = "";
		if (getHttpServletRequest().getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		} else {
			ip = getHttpServletRequest().getRemoteAddr();
		}
		sysLog.setLogIp(ip);
		sysLog.setLogAction("用户名密码错误,登陆失败");
		sysLog.setCreated(DateFormatUtil.getCurrentDate(true));
		sysLog.setCreatedBy("Administrator");
		sysLog.setUpdated(DateFormatUtil.getCurrentDate(true));
		sysLog.setUpdatedBy("Administrator");
		sysLog.setUseYn("Y");
		this.sysLogService.saveOrUpdate(sysLog);
	}

	private void registerLoginLog(UserInfo userInfo) {
		if (userInfo != null) {
			SysLog sysLog = new SysLog();
			sysLog.setEmployee(userInfo.getEmployee());
			Date logDate = DateFormatUtil.getCurrentDate(true);
			sysLog.setLogDate(logDate);
			String ip = "";
			if (getHttpServletRequest().getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
				ip = "127.0.0.1";
			} else {
				ip = getHttpServletRequest().getRemoteAddr();
			}
			sysLog.setLogIp(ip);
			sysLog.setLogAction("登陆系统");
			sysLog.setCreated(DateFormatUtil.getCurrentDate(true));
			sysLog.setCreatedBy("Administrator");
			sysLog.setUpdated(DateFormatUtil.getCurrentDate(true));
			sysLog.setUpdatedBy("Administrator");
			sysLog.setUseYn("Y");
			this.sysLogService.saveOrUpdate(sysLog);
		}
	}

	private void registerLogOutLog(UserInfo userInfo) {
		if (userInfo != null) {
			SysLog sysLog = new SysLog();
			sysLog.setEmployee(userInfo.getEmployee());
			Date logDate = DateFormatUtil.getCurrentDate(true);
			sysLog.setLogDate(logDate);
			String ip = "";
			if (getHttpServletRequest().getRemoteAddr().equals("0:0:0:0:0:0:0:1")) {
				ip = "127.0.0.1";
			} else {
				ip = getHttpServletRequest().getRemoteAddr();
			}
			sysLog.setLogIp(ip);
			sysLog.setLogAction("退出系统");
			sysLog.setCreated(DateFormatUtil.getCurrentDate(true));
			sysLog.setCreatedBy("LawrenceQi");
			sysLog.setUpdated(DateFormatUtil.getCurrentDate(true));
			sysLog.setUpdatedBy("LawrenceQi");
			sysLog.setUseYn("Y");
			this.sysLogService.saveOrUpdate(sysLog);
		}

	}

	private void initialization(UserInfo userInfo) {
		SysAdmin admin = userInfo.getAdmin();

		SysEmployee employee = new SysEmployee();
		SysDepartment department = new SysDepartment();
		SysCompany company = new SysCompany();
	

		SysRole role = new SysRole();
		if (admin.getEmployeeId() != null) {

			employee.setId(admin.getEmployeeId().getId());
			employee.setEmpId(admin.getEmployeeId().getEmpId());

			employee.setEmpNameZh(admin.getEmployeeId().getEmpNameZh());
			employee.setEmpNameEn(admin.getEmployeeId().getEmpNameEn());
			employee.setEmpNameKo(admin.getEmployeeId().getEmpNameKo());

			if (admin.getEmployeeId().getDepartmentId() != null) {
				department = admin.getEmployeeId().getDepartmentId();
				department = this.sysDepartmentService.getEntityById(department.getId());
				employee.setDepartmentId(department);
				if (admin.getEmployeeId().getDepartmentId().getSysCompany() != null) {
					company.setId(admin.getEmployeeId().getDepartmentId().getSysCompany().getId());
					company.setCpnyId(admin.getEmployeeId().getDepartmentId().getSysCompany().getCpnyId());
					company.setCpnyNameZh(admin.getEmployeeId().getDepartmentId().getSysCompany().getCpnyNameZh());
					company.setCpnyNameEn(admin.getEmployeeId().getDepartmentId().getSysCompany().getCpnyNameEn());
					company.setCpnyNameKo(admin.getEmployeeId().getDepartmentId().getSysCompany().getCpnyNameKo());
					department.setSysCompany(company);
				}
			}
		}

		if (admin.getRoleId() != null) {
			role.setId(admin.getRoleId().getId());
			role.setRoleId(admin.getRoleId().getRoleId());
			role.setRoleNameZh(admin.getRoleId().getRoleNameZh());
			role.setRoleNameEn(admin.getRoleId().getRoleNameEn());
			role.setRoleNameKo(admin.getRoleId().getRoleNameKo());
		}

		userInfo.setAdmin(admin);

		userInfo.setEmployee(employee);

		userInfo.setDepartment(department);

		userInfo.setSysCompany(company);

		userInfo.setRole(role);

		initializationPermission(userInfo);
	}

	private void initializationPermission(UserInfo userInfo) {
		Set<SysRole> roleParents = new HashSet<SysRole>();
		SysRole role = this.sysRoleService.getSysRoleByUse(userInfo.getRole().getId());
//		如果是超级管理员权限 加载所有角色
		if(role.getRoleId().equals("ROLE_ADMIN")){
			List<SysRole> allRole = this.sysRoleService.getEntityAll();
			Long[] ids = new Long[allRole.size()];
			int index = 0;
			for (SysRole r : allRole) {
				ids[index] = r.getId();
				index++;
			}
			userInfo.setRoleIds(ids);
		}else{
			loadPermission(roleParents, role);
			roleParents.add(role);
			Long[] ids = new Long[roleParents.size()];
			int index = 0;
			for (SysRole r : roleParents) {
				ids[index] = r.getId();
				index++;
			}
			userInfo.setRoleIds(ids);
		}
		
	}

	private void loadPermission(Set<SysRole> roleParents, SysRole role) {
		if (role.getSysRoleParents() != null) {
			if (role.getSysRoleParents().size() > 0) {
				Set<SysRoleParent> srp = role.getSysRoleParents();
				for (SysRoleParent rp : srp) {
					roleParents.add(rp.getParentId());
					loadPermission(roleParents, rp.getParentId());
				}
			}
		}
	}

	public String logout() {
		registerLogOutLog(UserSession.getUserInfo(this.getHttpServletRequest()));
		UserSession.invalidate(this.getHttpServletRequest());
		return "logout";
	}
	
	

	public SysAdmin getSysAdmin() {
		return sysAdmin;
	}

	public void setSysAdmin(SysAdmin sysAdmin) {
		this.sysAdmin = sysAdmin;
	}

	public SysAdminService getSysAdminService() {
		return sysAdminService;
	}

	public void setSysAdminService(SysAdminService sysAdminService) {
		this.sysAdminService = sysAdminService;
	}

	public SysRoleParentService getSysRoleParentService() {
		return sysRoleParentService;
	}

	public void setSysRoleParentService(SysRoleParentService sysRoleParentService) {
		this.sysRoleParentService = sysRoleParentService;
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	public SysPermissionService getSysPermissionService() {
		return sysPermissionService;
	}

	public void setSysPermissionService(SysPermissionService sysPermissionService) {
		this.sysPermissionService = sysPermissionService;
	}

	public SysLogService getSysLogService() {
		return sysLogService;
	}

	public void setSysLogService(SysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSaveId() {
		return saveId;
	}

	public void setSaveId(String saveId) {
		this.saveId = saveId;
	}

	public String getSysLanguage() {
		return sysLanguage;
	}

	public void setSysLanguage(String sysLanguage) {
		this.sysLanguage = sysLanguage;
	}

	public SysDepartmentService getSysDepartmentService() {
		return sysDepartmentService;
	}

	public void setSysDepartmentService(SysDepartmentService sysDepartmentService) {
		this.sysDepartmentService = sysDepartmentService;
	}

	public SysMasterCodeService getSysMasterCodeService() {
		return sysMasterCodeService;
	}

	public void setSysMasterCodeService(SysMasterCodeService sysMasterCodeService) {
		this.sysMasterCodeService = sysMasterCodeService;
	}

	public String get_pwd() {
		return _pwd;
	}

	public void set_pwd(String _pwd) {
		this._pwd = _pwd;
	}

	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}


	public SysEmployeeService getSysEmployeeService() {
		return sysEmployeeService;
	}

	public void setSysEmployeeService(SysEmployeeService sysEmployeeService) {
		this.sysEmployeeService = sysEmployeeService;
	}
	
	@SuppressWarnings({ "unused", "finally" })
	public String getVerifyImg() throws IOException{
		String imgOldPath = getParameter("_path");
		JSONObject root = new JSONObject();
		String verifyNumber="";
		String filePath ="";
		try {
			if(StringUtils.isNotBlank(imgOldPath)){
				clearImg(imgOldPath);
			}
			Map map = ImgVerifyCode.getImgVerify();
			verifyNumber = map.get("num").toString();
			String path = map.get("path").toString();
			File ifile = new File(path);
			String seqNo = ifile.getName();
			filePath = File.separator+"upload"+File.separator+"verify_img"+File.separator+seqNo;
			String filePath1 =getWebServletContext().getRealPath(File.separator)+File.separator+"upload"+File.separator+"verify_img"+File.separator+seqNo;
			
			try {
				ifile.renameTo(new File(filePath1));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			root.put("filePath", filePath);
			root.put("verifyNumber", verifyNumber);
			writeTextByAction(root.toString());
			return "";
		}
	}
	public String clearImg(String path){
        try{
        	if(StringUtils.isBlank(path)){
        		return "";
        	}
        	String path1 = getWebServletContext().getRealPath(File.separator)+path;
        	File file = new File(path1);
            if(!file.isDirectory()){
            	file.delete();
            }
        }catch(Exception e){
            return "";
        }
        return "";
	}
	
	
	public String loginSys() throws Exception {
		String policeno = getParameter("policeno");
		String smsVerify = getParameter("smsVerify");
		String imgPaths = getParameter("imgPaths");
		if (StringUtils.isBlank(policeno) || !Constants.isMobileNO(policeno)) {
			errorMessage = "002";
			return "input";
		}
		if (StringUtils.isBlank(smsVerify)) {
			errorMessage = "003";
			return "input";
		}
		//验证短信验证码是否正确并获取对应的账号
		String account = this.sysLogService.checkVerify(policeno,smsVerify);
		if(StringUtils.isBlank(account)){
			errorMessage = "007";
			return "input";
		}
		/*SysAdmin degree = this.sysAdminService.validateDegree(account, password);*/
		SysAdmin degree = this.sysAdminService.validateDegree(account);
		if (degree == null) {
			degree = this.sysAdminService.validateDegree(account);
			if (degree == null) {
				errorMessage = "004";
				registerLoginErrorLog();
				return "input";
			} else {
				if (!degree.getUseYn().equals("Y")) {
					errorMessage = "006";
					return "input";
				}
				errorMessage = "005";
				registerLoginErrorLog();
				return "input";
			}
		} else {
			if (!degree.getUseYn().equals("Y")) {
				errorMessage = "006";
				return "input";
			}
		}
		UserInfo userInfo = null;
		HttpSession session = this.getHttpServletRequest().getSession(false);
		if(session != null){
			session.invalidate();
			session = null;
		}
		if (session == null) {
			userInfo = new UserInfo();
			userInfo.setAdmin(degree);
			userInfo.setLanguagePreference(UserInfo.DEFAULT_LANGUAGE_PREFERENCE);
			userInfo.setCountryPreference(UserInfo.DEFAULT_COUNTRY_PREFERENCE);
			session = getHttpServletRequest().getSession(true);
			if (session.isNew()) {
				session.setMaxInactiveInterval(sessionMaxInactiveInterval);
			}
			session.setAttribute(UserSession.SESSION_NAME, userInfo);
		} else {
			try{
				userInfo = UserSession.getUserInfo(getHttpServletRequest());
			}catch(UserSessionException e){
				userInfo = null;
				session = null;
			}
			if (userInfo == null) {
				userInfo = new UserInfo();
				userInfo.setAdmin(degree);
				userInfo.setLanguagePreference(UserInfo.DEFAULT_LANGUAGE_PREFERENCE);
				userInfo.setCountryPreference(UserInfo.DEFAULT_COUNTRY_PREFERENCE);
				session = getHttpServletRequest().getSession(true);
				if (session.isNew()) {
					session.setMaxInactiveInterval(sessionMaxInactiveInterval);
				}
				session.setAttribute(UserSession.SESSION_NAME, userInfo);
			}
		}
		userInfo = UserSession.getUserInfo(getHttpServletRequest());
		userInfo.setLoggedIn(true);
		initialization(userInfo);
		initializationMenu(userInfo);
		registerLoginLog(userInfo);
		clearImg(imgPaths);
		String page = "success";
		/*String page = "successSave";*/
		if (StringUtils.isNotBlank(getSaveId())) {
			if (getSaveId().equals("Y")) {
				page = "successSave";
			}
		}
		return page;
	}
}
