package com.hl95.sys.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.support.encryptPassword.EncryptPassword;
import org.pj.criterion.core.support.upload.UploadManager;
import org.pj.criterion.core.util.SearchModelUtil;
import org.pj.criterion.core.util.StringUtil;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.support.SearchMode;
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;

import sun.misc.BASE64Decoder;

import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysDeptRole;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysRole;
import com.hl95.sys.service.SysAdminService;
import com.hl95.sys.service.SysDepartmentService;
import com.hl95.sys.service.SysDeptRoleService;
import com.hl95.sys.service.SysEmployeeService;
import com.hl95.sys.service.SysRoleService;
import com.opensymphony.xwork.Action;

public class SysDeptRoleAction extends BusinessAction<SysDeptRole, SysDeptRoleService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysDeptRole sysDeptRole;

	private SysDeptRoleService sysDeptRoleService;
	
	private SysRoleService sysRoleService;
	
	

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	public SysDeptRole getSysDeptRole() {
		if (getProxyEntity() != null) {
			return getProxyEntity();
		}
		return sysDeptRole;
	}

	public void setSysDeptRole(SysDeptRole sysDeptRole) {
		this.sysDeptRole = sysDeptRole;
	}

	public SysDeptRoleService getSysDeptRoleService() {
		return sysDeptRoleService;
	}

	public void setSysDeptRoleService(SysDeptRoleService sysDeptRoleService) {
		this.sysDeptRoleService = sysDeptRoleService;
	}

	@Override
	protected SysDeptRole getFromBean() {
		// TODO Auto-generated method stub
		return getSysDeptRole();
	}

	@Override
	public void searchModelCallBack() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	public void ajaxRoleData(){
		JSONArray arrayItems=new JSONArray();
		try{
			List<SysRole> list = this.sysRoleService.getSysRoleByUse();
			//获取当前权限
			List<SysDeptRole> dList = null;
			try{
				Long _deptId = Long.valueOf(StringUtils.defaultString(getParameter("_deptId")));
				dList = this.sysDeptRoleService.getListByDeptId(_deptId);
			}catch (Exception e) {
			}
			List<Long> lList = new ArrayList<Long>();
			if(dList != null && dList.size() > 0){
				for(SysDeptRole sdr:dList){
					lList.add(sdr.getRoleId().getId());
				}
			}
			if(list!=null){
				int index=0;
				for(SysRole obj:list){	
					JSONObject Item=new JSONObject();
					Item.put("text", obj.getRoleNameZh());
					Item.put("id", obj.getId());
					Item.put("cls", "file");
					Item.put("leaf", true);
					Item.put("allowDrag", false);
					Item.put("allowDrop", false);
					Item.put("checked", lList.contains((long)obj.getId())?true:false);
					arrayItems.add(Item);
					index++;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		writeJsonByAction(arrayItems.toString());
	}


}
