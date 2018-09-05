package com.hl95.sys.action;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.DateCell;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.support.encryptPassword.EncryptPassword;
import org.pj.criterion.core.support.excel.ReadExcelManager;
import org.pj.criterion.core.util.BeanUtil;
import org.pj.criterion.core.util.DateFormatUtil;
import org.pj.criterion.core.util.SearchModelUtil;
import org.pj.criterion.core.util.StringUtil;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.support.SearchMode;
import org.springframework.mail.MailException;

import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysEmployeeInfo;
import com.hl95.sys.entity.SysMasterCode;
import com.hl95.sys.entity.SysRole;
import com.hl95.sys.service.SysAdminService;
import com.hl95.sys.service.SysDepartmentService;
import com.hl95.sys.service.SysEmployeeInfoService;
import com.hl95.sys.service.SysEmployeeService;
import com.hl95.sys.service.SysMasterCodeService;
import com.hl95.sys.service.SysMenuService;
import com.hl95.sys.service.SysRoleService;


public class SysEmployeeInfoAction extends
		BusinessAction<SysEmployeeInfo, SysEmployeeInfoService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysEmployeeInfoService sysEmployeeInfoService;

	private SysEmployeeInfo sysEmployeeInfo;

	public SysEmployeeInfoService getSysEmployeeInfoService() {
		return sysEmployeeInfoService;
	}

	public void setSysEmployeeInfoService(
			SysEmployeeInfoService sysEmployeeInfoService) {
		this.sysEmployeeInfoService = sysEmployeeInfoService;
	}

	public SysEmployeeInfo getSysEmployeeInfo() {
		if (getProxyEntity() != null) {
			return getProxyEntity();
		}
		return sysEmployeeInfo;
	}

	public void setSysEmployeeInfo(SysEmployeeInfo sysEmployeeInfo) {
		this.sysEmployeeInfo = sysEmployeeInfo;
	}

	@Override
	protected SysEmployeeInfo getFromBean() {
		// TODO Auto-generated method stub
		return getSysEmployeeInfo();
	}

	@Override
	public void searchModelCallBack() {
		// TODO Auto-generated method stub
		
	}

	

}
