package com.hl95.login.action;


import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.apache.commons.lang.StringUtils;
import org.pj.criterion.core.action.CriterionAction;
import org.pj.framework.business.support.Permission;
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;

import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysMenu;
import com.hl95.sys.service.SysAdminService;
import com.hl95.sys.service.SysDepartmentService;
import com.hl95.sys.service.SysEmployeeService;
import com.hl95.sys.service.SysMasterCodeService;
import com.hl95.sys.service.SysMenuService;
import com.hl95.sys.service.SysLogService;




public class IndexAction extends CriterionAction {



	private static final long serialVersionUID = 1L;

	private SysMenuService sysMenuService;

	private List<SysMenu> sysMenus;
	
	private SysAdmin sysAdmin;

	private SysEmployeeService sysEmployeeService;

	private SysMasterCodeService sysMasterCodeService;
	
	private SysDepartmentService sysDepartmentService;
	private SysLogService sysLogService;
	
	private String noCheck;
	
	private String redirectToPassword;
	
	private SysAdminService sysAdminService;
	
	private String encryptedPassword;

	private String toLanguage;
	
	private String language;
	
	
	public String swLanguage() throws Exception {
		UserInfo userInfo = UserSession.getUserInfo(this
				.getHttpServletRequest());
		if (StringUtils.isNotBlank(getToLanguage())) {
			if (getToLanguage().equals("zh")) {
				userInfo.setLanguagePreference("zh");
				userInfo.setCountryPreference("CN");
				language = "ait_inet";
			} else if (getToLanguage().equals("en")) {
				userInfo.setLanguagePreference("en");
				userInfo.setCountryPreference("US");
				language = "ait_inet_en";
			} else if (getToLanguage().equals("ko")) {
				userInfo.setLanguagePreference("ko");
				userInfo.setCountryPreference("KR");
				language = "ait_inet_kor";
			} else {
				language = "ait_inet";
				userInfo
						.setLanguagePreference(UserInfo.DEFAULT_LANGUAGE_PREFERENCE);
				userInfo
						.setCountryPreference(UserInfo.DEFAULT_COUNTRY_PREFERENCE);
			}

		}
		return "swLanguage";
	}

	
	public String getIPAddress() { 
		HttpServletRequest request = this.getHttpServletRequest();
		String ip = request.getHeader("x-forwarded-for"); 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getRemoteAddr(); 
		} 
		return ip; 
	}
	


	@Override
	public String execute() throws Exception {
		UserInfo userInfo = UserSession.getUserInfo(this
				.getHttpServletRequest());

		if (!userInfo.isLoggedIn()) {
			setAttribute("_link",this.sysLogService.linkUrl());
			return "loginPage";
		}
		this.sysMenus = this.sysMenuService.getRootSysMenuByPermission(userInfo
				.getRoleIds(), Permission.viewPermission);
		redirectToPassword = "N";
		return "reSuc";
	}
	
	public String loginPage() {
		setAttribute("_link",this.sysLogService.linkUrl());
		return "loginPage";
	}
	
	

	
	public List<SysMenu> getSysMenus() {
		return sysMenus;
	}

	public void setSysMenus(List<SysMenu> sysMenus) {
		this.sysMenus = sysMenus;
	}

	public SysMasterCodeService getSysMasterCodeService() {
		return sysMasterCodeService;
	}

	public void setSysMasterCodeService(
			SysMasterCodeService sysMasterCodeService) {
		this.sysMasterCodeService = sysMasterCodeService;
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
	
	

	public SysLogService getSysLogService() {
		return sysLogService;
	}


	public void setSysLogService(SysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}


	public String getToLanguage() {
		return toLanguage;
	}

	public void setToLanguage(String toLanguage) {
		this.toLanguage = toLanguage;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}





	public String getNoCheck() {
		return noCheck;
	}

	public void setNoCheck(String noCheck) {
		this.noCheck = noCheck;
	}

	public String getRedirectToPassword() {
		return redirectToPassword;
	}

	public void setRedirectToPassword(String redirectToPassword) {
		this.redirectToPassword = redirectToPassword;
	}

	public SysAdminService getSysAdminService() {
		return sysAdminService;
	}

	public void setSysAdminService(SysAdminService sysAdminService) {
		this.sysAdminService = sysAdminService;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public SysDepartmentService getSysDepartmentService() {
		return sysDepartmentService;
	}
	public void setSysDepartmentService(SysDepartmentService sysDepartmentService) {
		this.sysDepartmentService = sysDepartmentService;
	}


	public SysAdmin getSysAdmin() {
		return sysAdmin;
	}


	public void setSysAdmin(SysAdmin sysAdmin) {
		this.sysAdmin = sysAdmin;
	}
	
	
}
