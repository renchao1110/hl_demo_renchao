package com.hl95.sys.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pj.criterion.core.util.StringUtils;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;

import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysSetDept;
import com.hl95.sys.service.SysDepartmentService;
import com.hl95.sys.service.SysEmployeeService;
import com.hl95.sys.service.SysRoleService;
import com.hl95.sys.service.SysSetDeptService;
import com.hl95.utils.Constants;
import com.opensymphony.xwork.Action;

public class SysSetDeptAction extends BusinessAction<SysSetDept, SysSetDeptService>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysSetDept sysSetDept;
	private SysSetDeptService sysSetDeptService;
	private SysDepartmentService sysDepartmentService;
	private SysEmployeeService sysEmployeeService;
	private SysRoleService sysRoleService;
	private String _msg;
	private Long _eId;
	
	
	
	public Long get_eId() {
		return _eId;
	}

	public void set_eId(Long id) {
		_eId = id;
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	public SysSetDeptService getSysSetDeptService() {
		return sysSetDeptService;
	}

	public void setSysSetDeptService(SysSetDeptService sysSetDeptService) {
		this.sysSetDeptService = sysSetDeptService;
	}

	public SysSetDept getSysSetDept() {
		if(getProxyEntity()!=null){
			return getProxyEntity();
		}
		return sysSetDept;
	}

	public void setSysSetDept(SysSetDept sysSetDept) {
		this.sysSetDept = sysSetDept;
	}



	public String get_msg() {
		return _msg;
	}

	public void set_msg(String _msg) {
		this._msg = _msg;
	}

	public SysEmployeeService getSysEmployeeService() {
		return sysEmployeeService;
	}

	public void setSysEmployeeService(SysEmployeeService sysEmployeeService) {
		this.sysEmployeeService = sysEmployeeService;
	}

	public SysDepartmentService getSysDepartmentService() {
		return sysDepartmentService;
	}

	public void setSysDepartmentService(SysDepartmentService sysDepartmentService) {
		this.sysDepartmentService = sysDepartmentService;
	}



	@Override
	protected SysSetDept getFromBean() {
		// TODO Auto-generated method stub
		return getSysSetDept();
	}

	@Override
	public void searchModelCallBack() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String execute() throws Exception {
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		Long empId = userInfo.getEmployee().getId();
		try{
			empId = Long.valueOf(getParameter("_empId"));	
		}catch (Exception e) {
		}
		Object[] empInfo = this.sysEmployeeService.getEmpNameById(empId);
		setAttribute("_isSearch",true );
		setAttribute("_empInfo", empInfo);
		setAttribute("_empId", empId);
		return super.execute();
	}
	
	public String saveDept(){
		Long _eId = null;
		try{
			_eId = Long.valueOf(getParameter("_eid"));
		}catch (Exception e) {
		}
		String _deptIds = StringUtils.defaultIfEmpty(getParameter("_deptIds"));
		_msg = "err";
		set_eId(_eId);
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		boolean isSearchFlag = this.sysRoleService.getCheckRole(userInfo.getRoleIds(), Constants.IS_SDEPT_ID);
		if(_eId != null 
				&& isSearchFlag){
			try{
				List<Long> clist = new ArrayList<Long>();
				if(StringUtils.isNotBlank(_deptIds)){
					String[] deptIds = _deptIds.split(",");
					for(String cid:deptIds){
						clist.add(Long.valueOf(cid));
					}
				}
				this.sysSetDeptService.excSaveBat(_eId, clist,userInfo.getEmployee().getEmpId());
				_msg = "suc";
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return "edit";
	}
	
	
	public String loadDeptTree() throws Exception{
		Long parentId = -1l;
		List<SysDepartment> departmentTreeList=null;
		Long _eid = null;
		try{
			_eid = Long.valueOf(getParameter("_eid"));
		}catch (Exception e) {
		}
		if(StringUtils.isBlank(getParameter("parentId"))
				|| _eid == null){
			return Action.INPUT;
		}else{
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			try{
				if("departmentRoot".equals(getParameter("parentId"))){
					boolean isSearchFlag = this.sysRoleService.getCheckRole(userInfo.getRoleIds(), Constants.IS_SDEPT_ID);
					if(isSearchFlag){
						parentId = 1l;
					}else{
						return Action.INPUT; 
					}
				}else{
					parentId=Long.valueOf(getParameter("parentId"));
				}
			}catch (Exception e) {
				if(userInfo.getDepartment().getParentId() != null){
					parentId = userInfo.getDepartment().getParentId().getId();
				}else{
					parentId = 0l;
				}
				
			}
			departmentTreeList=this.sysDepartmentService.getChildDepartmentsManager(parentId);
		}
		JSONArray arrayItems=new JSONArray();
		boolean hasChindls=false;
		if(departmentTreeList!=null){
			int index=0;
			for(SysDepartment sysDepartment:departmentTreeList){	
				JSONObject Item=new JSONObject();
				Item.put("text", geti18nByBean(sysDepartment, "departmentName"));
				Item.put("id", sysDepartment.getId());
				Item.put("brncoBrncd", sysDepartment.getBandCode());
				Item.put("cls", "file");
				hasChindls=this.sysDepartmentService.hasChilds(sysDepartment.getId());
				Item.put("leaf", hasChindls);
				Item.put("allowDrag", false);
				Item.put("allowDrop", false);
				Integer ecount = this.sysSetDeptService.getCountByEidAndDept(_eid, sysDepartment.getId());
				if(ecount > 0){
					Item.put("checked", true);
				}else{
					Item.put("checked", false);
				}
				arrayItems.add(Item);
				index++;
			}
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}

}
