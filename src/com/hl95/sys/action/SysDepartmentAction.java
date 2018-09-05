package com.hl95.sys.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.util.DateFormatUtil;
import org.pj.criterion.core.util.SearchModelUtil;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.support.SearchMode;
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;


import com.hl95.sys.entity.SysCompany;
import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysDeptRole;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysMasterCode;
import com.hl95.sys.service.SysCompanyService;
import com.hl95.sys.service.SysDepartmentService;
import com.hl95.sys.service.SysDeptRoleService;
import com.hl95.sys.service.SysEmployeeService;
import com.hl95.sys.service.SysMasterCodeService;
import com.opensymphony.xwork.Action;

public class SysDepartmentAction extends BusinessAction<SysDepartment, SysDepartmentService>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysDepartmentService sysDepartmentService;
	
	private SysDepartment sysDepartment;

	private List<SysDepartment> sysDepartmentList;
	
	private SysCompanyService sysCompanyService;
	
	private List<SysCompany> sysCompanyList;

	private SysEmployeeService sysEmployeeService;
	
	private SysDeptRoleService sysDeptRoleService;
	
	private SysMasterCodeService sysMasterCodeService;
	
	private String _deptName;
	
	private String _msg;
	
	public SysMasterCodeService getSysMasterCodeService() {
		return sysMasterCodeService;
	}


	public void setSysMasterCodeService(SysMasterCodeService sysMasterCodeService) {
		this.sysMasterCodeService = sysMasterCodeService;
	}


	public String get_msg() {
			return _msg;
		}


	public void set_msg(String _msg) {
		this._msg = _msg;
	}
	
	@Override
	public String execute() throws Exception {
		String _type = StringUtils.defaultString(getParameter("_type"));
		if("info".equals(_type)){
			return "rsuc";
		}
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		setAttribute("_pDeptName", userInfo.getDepartment().getDepartmentNameZh());
		return super.execute();
	}


	//	收信部门设置页面搜索部门 ajax select
	public String doSearchPop() throws Exception {
			StringBuilder sb = new StringBuilder();
			if(StringUtils.isNotBlank(_deptName)){
				initPageCriteria();
				getPageResultCriteria().add(
						SearchModelUtil.or(SearchModelUtil.getSearchCriterion(
								"departmentId", _deptName,
								SearchMode.LIKE_START_MODE, true), SearchModelUtil
								.or(SearchModelUtil.getSearchCriterion(
										"departmentNameZh", get_deptName(),
										SearchMode.LIKE_START_MODE, true),
										SearchModelUtil.or(SearchModelUtil
												.getSearchCriterion("departmentNameEn",
														get_deptName(),
														SearchMode.LIKE_START_MODE,
														true), SearchModelUtil
												.getSearchCriterion("departmentNameKo",
														get_deptName(),
														SearchMode.LIKE_START_MODE,
														true)))));
				getPageResultCriteria().addOrder(Order.asc("id"));
				List<SysDepartment> deptlist = this.sysDepartmentService.getSysDepartmentListByDc(getPageResultCriteria());
				sb = new StringBuilder();
				if (deptlist != null) {					
					for (SysDepartment dept : deptlist) {
						sb.append(dept.getId()+":");
						sb.append(dept.getDepartmentId()+",");
						sb.append(geti18nByBean(dept, "departmentName")+";");
					}
				}else{
					sb.append("");
				}
			}else{
				sb.append("");
			}
			writeTextByAction(sb.toString());
			return "";
		}
	
	
	@Override
	public void extendsBeforDelete(Serializable[] ids) throws Exception {
		boolean delFlag = false;
		int len = 0;
		if(ids != null){
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			if(userInfo.getDepartment().getDepartmentLevel() != 0){
				List<Long> deptIdList = new ArrayList<Long>();
				this.sysDepartmentService.getChrildDeptIdEx(userInfo.getDepartment().getId(), deptIdList);
				for(Serializable sid:ids){
					long nsid = (Long)sid;
					int bCount = this.sysDepartmentService.getCountUserByDeptId(nsid);
					if(bCount <= 0){
						if(deptIdList != null){
							for(Long deptId:deptIdList){
								if(nsid == deptId){
									len++;
								}
							}
						}
					}else if(bCount > 0){
						set_msg("hasDept");
						break;
					}else{
						set_msg("has");
						break;
					}
				}
				if(len == ids.length && ids.length > 0){
					set_msg("dreload");
					delFlag = true;
				}
			}else{
				set_msg("dreload");
				delFlag = true;
			}
		}else{
			set_msg("dreload");
		}
		setEditExecutePermission(delFlag);
	}
	
	@Override
	public void viewCallBack(SysDepartment object)  throws Exception{
		//sysCompanyList=this.sysCompanyService.getSysCompanyByUse();
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		Long[] roleIds = userInfo.getRoleIds();
		boolean rflag = false;
		if(roleIds != null && roleIds.length > 0){
			for(Long rid:roleIds){
				if(40==rid){
					rflag = true;
					break;
				}
			}
		}
		
		if(rflag){
			List<SysMasterCode> typeList = this.sysMasterCodeService.getChildMasterCodebyPids("area_dept_type");
			setAttribute("typeList", typeList);
			setAttribute("adrl", "suc");
		}else if(object != null && StringUtils.isNotBlank(object.getAgenciesCode())){
			SysMasterCode typeCode = this.sysMasterCodeService.getSysMasterCodeByCodeId("area_dept_type_"+object.getAgenciesCode());
			setAttribute("typeCode", typeCode);
		}else{
			SysMasterCode typeCode = this.sysMasterCodeService.getSysMasterCodeByCodeId("area_dept_type_4");
			setAttribute("typeCode", typeCode);
		}
		
		setAttribute("_pDeptName", userInfo.getDepartment().getDepartmentNameZh());
	}
	public String display() throws Exception{
		this.view();
		setAttribute("_msg", StringUtils.defaultIfEmpty(getParameter("_msg"), ""));
		return "display";
	}
	
	
	
	@Override
	public void extendsBeforView(Serializable id){
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		if(userInfo.getDepartment().getDepartmentLevel() != 0){
			List<Long> deptIdList = new ArrayList<Long>();
			this.sysDepartmentService.getChrildDeptIdEx(userInfo.getDepartment().getId(), deptIdList);
			boolean editPerFlag = false;
			long vId = (Long) id;
			deptIdList.remove(userInfo.getDepartment().getId());
			for(Long deptId:deptIdList){
				if(vId == deptId){
					editPerFlag = true;
					break;
				}
			}
			if(!editPerFlag){
				setAttribute("_per", "noper");
			}
			setViewExecutePermission(editPerFlag);
		}
	}
	
	public String onlyDepartmentId() throws Exception{
		if(getSysDepartment()!=null){		
			SysDepartment onlySysDepartment=this.sysDepartmentService.getSysDepartmentByDepartmentId(getSysDepartment().getDepartmentId());
			boolean onlyFlag = false;
			if(onlySysDepartment==null){
				onlyFlag=true;
			}
			if (onlyFlag) {
				writeTextByAction("true");
			} else {
				writeTextByAction("false");
			}
		}
		return "";
	}
	
	public String onlyDepartmentNameZh() throws Exception{
		if(getSysDepartment()!=null){
			String isEdit = getParameter("isEdit");
			SysDepartment onlySysDepartment=this.sysDepartmentService.getSysDepartmentByDepartmentNameZh(getSysDepartment().getDepartmentNameZh());
			boolean onlyFlag = false;
			if(onlySysDepartment==null){
				onlyFlag=true;
			}else{
				if(isEdit.equals("1")){
					if(onlySysDepartment.getId().equals(getSysDepartment().getId())){
						onlyFlag=true;
					}
				}
			}
			if (onlyFlag) {
				writeTextByAction("true");
			} else {
				writeTextByAction("false");
			}
		}
		return "";
	}

	
	public String loadDepartmentTreeManager() throws Exception{
		List<SysDepartment> departmentTreeList=null;
		if(StringUtils.isBlank(getParameter("parentId"))){
			return Action.INPUT;
		}else{
			Long parentId = -1l;
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			try{
				parentId=Long.valueOf(getParameter("parentId"));
				if(!(this.getCInfoFlag(userInfo.getDepartment().getId(), parentId))){
					return Action.INPUT; 
				};
			}catch (Exception e) {
				parentId = userInfo.getDepartment().getId();
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
				
				arrayItems.add(Item);
				index++;
			}
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	public String loadDepartmentTreeForReq() throws Exception{
		List<SysDepartment> departmentTreeList=null;
		if(StringUtils.isBlank(getParameter("parentId"))){
			return Action.INPUT;
		}else{
			if(getParameter("parentId").equals("departmentRoot")){
				departmentTreeList=this.sysDepartmentService.getRootDepartments();
			}else{
				Long parentId=(Long) ConvertUtils.convert(getParameter("parentId"), Long.class);
				departmentTreeList=this.sysDepartmentService.getChildDepartments(parentId);
			}
		}
		String selfDeptId=getParameter("_selfDeptId");
		Long selfId=null;
		if(StringUtils.isNotBlank(selfDeptId)){
			selfId=(Long) ConvertUtils.convert(selfDeptId,Long.class);
		}
		JSONArray arrayItems=new JSONArray();
		boolean hasChindls=false;
		if(departmentTreeList!=null){
			int index=0;
			for(SysDepartment sysDepartment:departmentTreeList){	
				if(selfId!=null){
					if(sysDepartment.getId().equals(selfId)){
						continue;
					}
				}
				JSONObject Item=new JSONObject();
				Item.put("text", geti18nByBean(sysDepartment, "departmentName"));
				Item.put("id", sysDepartment.getId());
				Item.put("deptId", sysDepartment.getDepartmentId());
				Item.put("cls", "file");
				hasChindls=this.sysDepartmentService.hasChildsByUse(sysDepartment.getId());
				Item.put("leaf", hasChindls);
				Item.put("allowDrag", false);
				Item.put("allowDrop", false);
				
				arrayItems.add(Item);
				index++;
			}
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	public String loadDepartmentTree() throws Exception{
		List<SysDepartment> departmentTreeList=null;
		String pId= StringUtils.defaultIfEmpty(getParameter("parentId"), "");
		if(StringUtils.isBlank(pId)){
			return Action.INPUT;
		}else{
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			long parentId = userInfo.getDepartment().getId();
			if("departmentRoot".equals(pId)){
				departmentTreeList = new ArrayList<SysDepartment>();
				departmentTreeList.add(this.sysDepartmentService.getEntityById(parentId));
			}else if("deptRoot".equals(pId)){
				departmentTreeList=this.sysDepartmentService.getOneNextChrildDeptIdEx(parentId);
			}else{
				try{
					parentId=Long.valueOf(getParameter("parentId"));
				}catch (Exception e) {
				}
				departmentTreeList=this.sysDepartmentService.getChildDepartments(parentId);
			}
		}
		String selfDeptId=getParameter("_selfDeptId");
		Long selfId=null;
		if(StringUtils.isNotBlank(selfDeptId)){
			selfId=(Long) ConvertUtils.convert(selfDeptId,Long.class);
		}
		JSONArray arrayItems=new JSONArray();
		boolean hasChindls=false;
		if(departmentTreeList!=null){
			int index=0;
			for(SysDepartment sysDepartment:departmentTreeList){	
				if(selfId!=null){
					if(sysDepartment.getId().equals(selfId)){
						continue;
					}
				}
				JSONObject Item=new JSONObject();
				Item.put("text", geti18nByBean(sysDepartment, "departmentName"));
				Item.put("id", sysDepartment.getId());
				Item.put("cls", "file");
				if("deptRoot".equals(pId)){
					hasChindls = true;
				}else{
					hasChindls=this.sysDepartmentService.hasChildsByUse(sysDepartment.getId());
				}
				Item.put("leaf", hasChindls);
				Item.put("allowDrag", false);
				Item.put("allowDrop", false);
				
				arrayItems.add(Item);
				index++;
			}
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	
	public String loadDepartmentTreeCheckByEmployee() throws Exception{
		List<SysDepartment> departmentTreeList=null;
		SysDepartment sysDepartmentParent=null;
		if(StringUtils.isBlank(getParameter("parentId"))){
			return Action.INPUT;
		}else{
			if(getParameter("parentId").equals("managerRoot")){
				departmentTreeList=this.sysDepartmentService.getRootDepartments();
			}else{
				Long parentId=(Long) ConvertUtils.convert(getParameter("parentId"), Long.class);
				sysDepartmentParent=this.sysDepartmentService.getEntityById(parentId);
				departmentTreeList=this.sysDepartmentService.getChildDepartments(parentId);
			}
		}
		JSONArray arrayItems=new JSONArray();
		JSONObject Item=null;
		List<SysEmployee> employees=null;
		if(sysDepartmentParent!=null){
			employees=this.sysEmployeeService.getSysEmployeeListByDeptId(sysDepartmentParent.getId());
			if(employees!=null){
				if(employees.size()>0){
					for(SysEmployee employee:employees){
						Item=new JSONObject();
						Item.put("text", geti18nByBean(employee, "empName"));
						Item.put("id", employee.getId());
						Item.put("cls", "file");
						Item.put("leaf", true);
						Item.put("icon", "/images/icon/member.gif");
						
						Item.put("allowDrag", false);
						Item.put("allowDrop", false);
						Item.put("checked", false);
						Item.put("typeClass", "employee");
						arrayItems.add(Item);
					}
				}
			}
		}
		boolean hasChindls=false;
		boolean hasEmployees=true;
		boolean finalB;
		if(departmentTreeList!=null){
			int index=0;
			
			for(SysDepartment sysDepartment:departmentTreeList){
				employees=this.sysEmployeeService.getSysEmployeeListByDeptId(sysDepartment.getId());
				Item=new JSONObject();
				Item.put("text", geti18nByBean(sysDepartment, "departmentName"));
				Item.put("id", sysDepartment.getId());
				Item.put("cls", "file");
				hasChindls=this.sysDepartmentService.hasChilds(sysDepartment.getId());
				if(employees!=null){
					if(employees.size()>0){
						hasEmployees=false;
					}else{
						hasEmployees=true;
					}
				}
				if(!hasChindls){
					finalB=false;
				}else{
					if(hasChindls&&!hasEmployees){
						finalB=false;
					}else{
						finalB=true;
					}
				}
				Item.put("leaf", finalB);
				if(!finalB){
					Item.put("checked", false);
				}
				Item.put("typeClass", "department");
				arrayItems.add(Item);
				index++;
			}
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	public String loadDepartmentTreeByEmployee() throws Exception{
		List<SysDepartment> departmentTreeList=null;
		SysDepartment sysDepartmentParent=null;
		if(StringUtils.isBlank(getParameter("parentId"))){
			return Action.INPUT;
		}else{
			if(getParameter("parentId").equals("managerRoot")){
				departmentTreeList=this.sysDepartmentService.getRootDepartments();
			}else{
				Long parentId=(Long) ConvertUtils.convert(getParameter("parentId"), Long.class);
				sysDepartmentParent=this.sysDepartmentService.getEntityById(parentId);
				departmentTreeList=this.sysDepartmentService.getChildDepartments(parentId);
			}
		}
		JSONArray arrayItems=new JSONArray();
		JSONObject Item=null;
		List<SysEmployee> employees=null;
		if(sysDepartmentParent!=null){
			employees=this.sysEmployeeService.getSysEmployeeListByDeptId(sysDepartmentParent.getId());
			if(employees!=null){
				if(employees.size()>0){
					for(SysEmployee employee:employees){
						Item=new JSONObject();
						Item.put("text", geti18nByBean(employee, "empName"));
						Item.put("id", employee.getId());
						Item.put("cls", "file");
						Item.put("leaf", true);
						Item.put("icon", "/images/icon/member.gif");
						if(employee.getDepartmentId().getDepartmentLevel()==4){
							Item.put("deptName", geti18nByBean(employee.getDepartmentId().getParentId(), "departmentName"));
						}else{
							Item.put("deptName", geti18nByBean(employee.getDepartmentId(), "departmentName"));
						}					
						
						Item.put("allowDrag", false);
						Item.put("allowDrop", false);
						Item.put("typeClass", "employee");
						arrayItems.add(Item);
					}
				}
			}
		}
		boolean hasChindls=false;
		boolean hasEmployees=true;
		boolean finalB;
		if(departmentTreeList!=null){
			int index=0;
			
			for(SysDepartment sysDepartment:departmentTreeList){
				employees=this.sysEmployeeService.getSysEmployeeListByDeptId(sysDepartment.getId());
				Item=new JSONObject();
				Item.put("text", geti18nByBean(sysDepartment, "departmentName"));
				Item.put("id", sysDepartment.getId());
				Item.put("cls", "file");
				hasChindls=this.sysDepartmentService.hasChilds(sysDepartment.getId());
				if(employees!=null){
					if(employees.size()>0){
						hasEmployees=false;
					}else{
						hasEmployees=true;
					}
				}
				if(!hasChindls){
					finalB=false;
				}else{
					if(hasChindls&&!hasEmployees){
						finalB=false;
					}else{
						finalB=true;
					}
				}
				Item.put("leaf", finalB);
				Item.put("typeClass", "department");
				arrayItems.add(Item);
				index++;
			}
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	
	
	/**
	 * EDIT回调函数 判断父级ID是否正确/存在 自动生成MenuLevel
	 */
	@Override
	public void extendsEdit(SysDepartment object) throws Exception{
		if(object!=null){
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			boolean editPerFlag = false;
			if(userInfo.getDepartment().getDepartmentLevel() != 0){
				List<Long> deptIdList = new ArrayList<Long>();
				this.sysDepartmentService.getChrildDeptIdEx(userInfo.getDepartment().getId(), deptIdList);
				if(object.getId() != null){
					deptIdList.remove(userInfo.getDepartment().getId());
					for(Long deptId:deptIdList){
						if(object.getId() == deptId){
							editPerFlag = true;
							break;
						}
					}
				}else{
					editPerFlag = true;
				}
			}else{
				editPerFlag = true;
			}
			/*boolean eflag = false;
			try{
				if(Integer.parseInt(object.getAgenciesCode()) < 4  ){
					Long[] roleIds = userInfo.getRoleIds();
					for(Long rid:roleIds){
						if(40==rid){
							eflag = true;
							break;
						}
					}
				}else{
					eflag = true;
				}
			}catch (Exception e) {
			}
			if(!eflag){
				editPerFlag = eflag;
			}*/
			setEditExecutePermission(editPerFlag);
			if(editPerFlag){
				if(object.getParentId()!=null){
					if(object.getParentId().getId()!=null){
						SysDepartment parentId=this.sysDepartmentService.getEntityById(object.getParentId().getId());
						if(parentId!=null){
							object.setDepartmentLevel(Integer.valueOf(parentId.getDepartmentLevel().intValue()+1));
						}
					}else{
						object.setParentId(null);
						object.setDepartmentLevel(Integer.valueOf(0));
					}	
				}else{
					object.setParentId(null);
					object.setDepartmentLevel(Integer.valueOf(0));
				}
				if(object.getEmployeeId()!=null){
					if(object.getEmployeeId().getId()==null){
						object.setEmployeeId(null);
					}
				}
			}
		}
	}
	/**
	 * 判断cId 是否是deptId的子类 
	 * @param deptId
	 * @param cId
	 * @return
	 */
	private boolean getCInfoFlag(long deptId,long cId){
		List<Long> deptIdList = new ArrayList<Long>();
		this.sysDepartmentService.getChrildDeptIdEx(deptId,deptIdList);
		if(deptIdList != null && deptIdList.size() > 0){
			deptIdList.remove(deptId);
			for(long dId:deptIdList){
				if(cId == dId){
					return true;
				}
			}
		}
		return false;
	}
	
	public String loadDepartmentTreeInfo() throws Exception{
		List<SysDepartment> departmentTreeList=null;
		if(StringUtils.isBlank(getParameter("_pId"))){
			return Action.INPUT;
		}else{
			long parentId = -1l;
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			try{
				parentId=Long.valueOf(getParameter("_pId"));
				if(!(this.getCInfoFlag(userInfo.getDepartment().getId(), parentId))){
					return Action.INPUT; 
				};
			}catch (Exception e){
				parentId = userInfo.getDepartment().getId();
			}
			departmentTreeList=this.sysDepartmentService.getChildDepartmentsManager(parentId);
		}
		JSONArray arrayItems=new JSONArray();
		boolean hasChindls=false;
		if(departmentTreeList!=null){
			int index=0;
			for(SysDepartment dept:departmentTreeList){
				JSONObject Item=new JSONObject();
				Item.put("id", dept.getId());
				Item.put("dname", dept.getDepartmentNameZh());
				String created = "无";
				if(dept.getUpdated() != null){
					created = DateFormatUtil.format(dept.getUpdated(), DateFormatUtil.YMDHM_PATTERN);
				}else if(dept.getCreated() != null){
					created = DateFormatUtil.format(dept.getCreated(), DateFormatUtil.YMDHM_PATTERN);
				}
				Item.put("created", created);
				hasChindls=this.sysDepartmentService.hasChilds(dept.getId());
				Item.put("leaf", hasChindls);
				if(!hasChindls){
					Item.put("iconCls", "task-folder");
					String dInfo = "旗下有";
					if(dept.getDepartmentLevel() >= 1){
						List<SysDepartment> cList = new ArrayList<SysDepartment>();
						this.sysDepartmentService.getChrildByDeptIdEx(dept, cList);
						int dNum = 0;
						int yNum = 0;
						if(cList != null && cList.size() > 0){
							cList.remove(dept);
							for(SysDepartment dObj:cList){
								if(dObj.getDepartmentLevel() == 2){
									dNum++;
								}else if(dObj.getDepartmentLevel() == 3){
									yNum++;
								}
							}
						}
						if(dNum > 0){
							dInfo +=  "  "+dNum+" 家代理商";
						}
						if(yNum > 0){
							dInfo += "   "+yNum+" 家药店";
						}
					}
					Item.put("dInfo", dInfo.equals("旗下有")?"":"<span style=\"color:red\">"+dInfo+"</span>");
				}else{
					if(dept.getDepartmentLevel() == 2){
						Item.put("dInfo","<span style=\"color:red\">旗下有 0 家药店</span>");
					}
					Item.put("iconCls", "task");
				}
				Item.put("uiProvider", "col");
				arrayItems.add(Item);
				index++;
			}
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	
	public String loadDeptTree() throws Exception{
		List<SysDepartment> departmentTreeList=null;
		if(StringUtils.isBlank(getParameter("parentId"))){
			return Action.INPUT;
		}else{
			Long parentId = -1l;
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			try{
				parentId=Long.valueOf(getParameter("parentId"));
				if(!(this.getCInfoFlag(userInfo.getDepartment().getId(), parentId))){
					return Action.INPUT; 
				};
			}catch (Exception e) {
				parentId = userInfo.getDepartment().getId();
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
				Item.put("checked", false);
				arrayItems.add(Item);
				index++;
			}
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	
	
	
	
	@Override
	protected SysDepartment getFromBean() {
		return getSysDepartment();
	}

	@Override
	public void searchModelCallBack() {
		// TODO Auto-generated method stub
		
	}
	public SysDepartment getSysDepartment() {
		if(getProxyEntity()!=null){
			return getProxyEntity();
		}
		return sysDepartment;
	}
	public void setSysDepartment(SysDepartment sysDepartment) {
		this.sysDepartment = sysDepartment;
	}
	public List<SysDepartment> getSysDepartmentList() {
		if(getProxyEntityList()!=null){
			return getProxyEntityList();
		}
		return sysDepartmentList;
	}
	public void setSysDepartmentList(List<SysDepartment> sysDepartmentList) {
		this.sysDepartmentList = sysDepartmentList;
	}
	public SysDepartmentService getSysDepartmentService() {
		return sysDepartmentService;
	}
	public void setSysDepartmentService(SysDepartmentService sysDepartmentService) {
		this.sysDepartmentService = sysDepartmentService;
	}
	public SysCompanyService getSysCompanyService() {
		return sysCompanyService;
	}
	public void setSysCompanyService(SysCompanyService sysCompanyService) {
		this.sysCompanyService = sysCompanyService;
	}
	public List<SysCompany> getSysCompanyList() {
		return sysCompanyList;
	}
	public void setSysCompanyList(List<SysCompany> sysCompanyList) {
		this.sysCompanyList = sysCompanyList;
	}
	public SysEmployeeService getSysEmployeeService() {
		return sysEmployeeService;
	}
	public void setSysEmployeeService(SysEmployeeService sysEmployeeService) {
		this.sysEmployeeService = sysEmployeeService;
	}


	public String get_deptName() {
		return _deptName;
	}
	public void set_deptName(String name) {
		_deptName = name;
	}


	public SysDeptRoleService getSysDeptRoleService() {
		return sysDeptRoleService;
	}


	public void setSysDeptRoleService(SysDeptRoleService sysDeptRoleService) {
		this.sysDeptRoleService = sysDeptRoleService;
	}

	

	
	
}
