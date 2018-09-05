package com.hl95.sys.action;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;
import org.springframework.mail.MailException;

import com.hl95.sys.entity.SysAdmin;
import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysDeptRole;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysEmployeeInfo;
import com.hl95.sys.entity.SysMasterCode;
import com.hl95.sys.entity.SysRole;
import com.hl95.sys.service.SysAdminService;
import com.hl95.sys.service.SysDepartmentService;
import com.hl95.sys.service.SysDeptRoleService;
import com.hl95.sys.service.SysEmployeeInfoService;
import com.hl95.sys.service.SysEmployeeService;
import com.hl95.sys.service.SysMasterCodeService;
import com.hl95.sys.service.SysMenuService;
import com.hl95.sys.service.SysRoleService;


public class SysEmployeeAction extends
		BusinessAction<SysEmployee, SysEmployeeService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private SysEmployeeService sysEmployeeService;
	
	private SysEmployeeInfoService sysEmployeeInfoService;
	
	private SysDeptRoleService sysDeptRoleService;

	private SysEmployee sysEmployee;

	private List<SysEmployee> sysEmployeeList;

	private SysMasterCodeService sysMasterCodeService;

	
	private SysDepartmentService sysDepartmentService;


	private List<SysMasterCode> workStateList;

	private List<SysMasterCode> sexList;

	private List<SysMasterCode> degreeList;
	private SysRoleService sysRoleService;

	private List<SysRole> sysRoleList;

	private SysAdminService sysAdminService;

	private String _empId;

	private String _empName;

	private String _departmentName;

	private String _dutyName;

	private String _workState;

	private String openEmail;

	private String _sysAdminPassword;

	private String _sysAdminAccount;

	private String _sysAdminRoleId;

	private SysMenuService sysMenuService;

	private boolean isCreate;
	
	private File empXLS;
	
	private String incumbency;
	
	private String dimission;

	private String[] shortCuts = { "MENU060200", "MENU060600", "MENU040201",
			"MENU030300", "MENU020100", "MENU020200" };
//	存放错误原因
	List<String> listError;
	/**
	 * 导入员工通讯录
	 * 更新InsideContact 详细信息
	 * writed by lyq 2009-06-14 
	 * @return
	 * @throws MailException
	 */
	public String importEmpContact() {
		ReadExcelManager readExcel = new ReadExcelManager();
		readExcel.initializationRead(empXLS);
		readExcel.setSheetIndex(0);
		int rows = readExcel.getSheetRows();
		listError = new ArrayList<String>();
		SysEmployee emp = null;
		
		String selfOperation;      //担当业务
		String address;            //地址
		String jobTelephone;       //办公电话
		String fax;                //传真
		String mobileTelephone;    //移动电话
		String familyTelephone;    //家庭电话
		String birthday;
		String email;
		String tempWorkExperience;
		
		String empId;              //工号
		String empName;
		String empNameEn;
		int updateNum = 0;
		listError.add("按照模板,数据共"+(rows)+"行");
		//System.out.println(rows);
		for (int index = 2; index < rows; index++) {
			
			readExcel.setCellRow(index);
			readExcel.setCellCol(0);
			empName = readExcel.getValue(readExcel.getCell()).toString().trim();
			
			if(empName==null||empName.equals("")){
				listError.add("第"+(index)+"行导入失败，员工编号为空");
				continue;
			}
			emp = this.sysEmployeeService.getSysEmployeeByEmpNameZh(empName);
			if(emp==null){
				listError.add("第"+(index)+"行导入失败，没有"+empName+"这个员工");
				continue;
			}
//			英文姓名
			readExcel.setCellCol(1);
			readExcel.setCellRow(index);
			empNameEn = readExcel.getValue(readExcel.getCell()).toString().trim();
			if(empNameEn!=null && StringUtils.isNotEmpty(empNameEn)){
				emp.setEmpNameEn(empNameEn);
				this.sysEmployeeService.saveOrUpdate(emp);
			}
			
//			生日
			readExcel.setCellCol(3);
			readExcel.setCellRow(index);
			Date date = null;
			DateCell d = (DateCell) readExcel.getCell();
			date = d.getDate();
			birthday = DateFormatUtil.format(date, "yyyy-MM-dd");

//			对外电话
			readExcel.setCellCol(4);
			readExcel.setCellRow(index);
			jobTelephone = readExcel.getValue(readExcel.getCell()).toString().trim();
			
//			内线电话
			readExcel.setCellCol(5);
			readExcel.setCellRow(index);
			familyTelephone = readExcel.getValue(readExcel.getCell()).toString().trim();
			
//			移动电话
			readExcel.setCellCol(6);
			readExcel.setCellRow(index);
			mobileTelephone = readExcel.getValue(readExcel.getCell()).toString().trim();
			
//			地址
			readExcel.setCellCol(7);
			readExcel.setCellRow(index);
			address = readExcel.getValue(readExcel.getCell()).toString().trim();
			
//			传真
			readExcel.setCellCol(8);
			readExcel.setCellRow(index);
			fax = readExcel.getValue(readExcel.getCell()).toString().trim();
			
//			邮件
			readExcel.setCellCol(9);
			readExcel.setCellRow(index);
			email = readExcel.getValue(readExcel.getCell()).toString().trim();
			

			
			
//			担当业务
			readExcel.setCellCol(12);
			readExcel.setCellRow(index);
			selfOperation = readExcel.getValue(readExcel.getCell()).toString().trim();


			
//			地址
			readExcel.setCellCol(13);
			readExcel.setCellRow(index);
			tempWorkExperience = readExcel.getValue(readExcel.getCell()).toString().trim();
			
			String[] clobProperty = new String[1];
			clobProperty[0] = "workExperience";
			String[] clobTempProperty = new String[1];
			clobTempProperty[0] = "tempWorkExperience";
			StringBuilder[] clobObject = new StringBuilder[clobTempProperty.length];
			StringBuilder sb = null;
			int indexs = 0;
			for (String expression : clobTempProperty) {
				sb = new StringBuilder();
				sb.append(findValueStack(expression));
				clobObject[indexs] = sb;
				indexs++;
			}
			
			popValueStack();			
			updateNum++;
		}


		return "importEmployeeDetail";
	}

	public String importEmp() throws ParseException {
		ReadExcelManager readExcel = new ReadExcelManager();
		readExcel.initializationRead(empXLS);
		readExcel.setSheetIndex(0);
		int rows = readExcel.getSheetRows();
		SysEmployee emp = null;
		String deptId;
		String dutyId;
		String empId;
		String empNameZh;
		String empNameKo;
		String sex;
		String national;
		String incumbency;
//		String mail;
		System.out.println(rows);

		for (int index = 0; index < rows; index++) {
			if (index == rows - 1)
				System.out.println(index);
			// 工号
			readExcel.setCellCol(0);
			readExcel.setCellRow(index);
			System.out.println(readExcel.getValue(readExcel.getCell())
					.toString().trim());
			empId = readExcel.getValue(readExcel.getCell()).toString().trim();
			if(empId.indexOf(".")!=-1){
				empId = "0"+empId.substring(0, empId.indexOf("."));
			}
			if (empId == null || empId.equals("")) {
				System.out.println(index+"员工编号为空");
				continue;
			}
			System.out.println(empId);
			
			
			emp = new SysEmployee();
			emp.setEmpId(getString(empId));
//			 韩文名
			readExcel.setCellCol(1);
			readExcel.setCellRow(index);
			empNameKo = readExcel.getValue(readExcel.getCell()).toString()
					.trim();
			emp.setEmpNameKo(empNameKo);
			// 中文名
			readExcel.setCellCol(2);
			readExcel.setCellRow(index);
			empNameZh = readExcel.getValue(readExcel.getCell()).toString()
					.trim();
			emp.setEmpNameZh(empNameZh);

			if (this.sysEmployeeService.getSysEmployeeByEmpId(getString(empId)) != null) {
				System.out.println(getString(empId) + "姓名：" + empNameZh
						+ "已存在。");
				continue;
			}
//			 民族
			readExcel.setCellCol(3);
			readExcel.setCellRow(index);
			national = readExcel.getValue(readExcel.getCell()).toString().trim();
			SysMasterCode nationalCode = this.sysMasterCodeService.getSysMasterCodeByCodeNameZh(national);

			


			// 部门
			readExcel.setCellCol(5);
			readExcel.setCellRow(index);
			deptId = readExcel.getValue(readExcel.getCell()).toString().trim();
			emp.setDepartmentId(this.sysDepartmentService
					.getSysDepartmentByDepartmentNameZh(getString(deptId)));

			
			// 职位
			readExcel.setCellCol(7);
			readExcel.setCellRow(index);

			emp.setUseYn("Y");
			this.bindEntity(emp);
			this.sysEmployeeService.saveOrUpdate(emp);



			// 创建系统登陆账号
			SysAdmin sysAdmin = new SysAdmin();
			SysRole role = this.sysRoleService.getSysRoleByRoleId("ROLE02");
			sysAdmin.setEmployeeId(emp);
			sysAdmin.setRoleId(role);
			sysAdmin.setAccount(empId);
			sysAdmin.setPassword(EncryptPassword.createPassword("000000"));
			sysAdmin.setUseYn("Y");
			this.bindEntity(sysAdmin);
			this.sysAdminService.saveOrUpdate(sysAdmin);
		}

		return super.SUCCESS;
	}

	/**
	 * 更新InsideContact 详细信息 writed by lyq 2009-06-14
	 * 
	 * @return
	 * @throws MailException
	 */
//	public String importEmpDetail() throws MailException {
//		ReadExcelManager readExcel = new ReadExcelManager();
//		readExcel.initializationRead(empXLS);
//		readExcel.setSheetIndex(0);
//		int rows = readExcel.getSheetRows();
//
//		SysEmployee emp = null;
//		InsideContact insideContact = null;
//
//		String selfOperation; // 担当业务
//		String address; // 地址
//		String jobTelephone; // 办公电话
//		String fox; // 传真
//		String mobileTelephone; // 移动电话
//		String familyTelephone; // 家庭电话
//
//		String empId; // 工号
//		int updateNum = 0;
//
//		// System.out.println(rows);
//		for (int index = 1; index < rows; index++) {
//			if (index == rows - 1)
//				System.out.println(index);
//			// 工号
//			readExcel.setCellCol(2);
//			readExcel.setCellRow(index);
//			empId = readExcel.getValue(readExcel.getCell()).toString().trim();
//
//			if (empId == null || empId.equals("")) {
//				System.out.println("员工编号为空");
//				continue;
//			}
//			emp = this.sysEmployeeService.getSysEmployeeByEmpId(empId);
//			if (emp == null) {
//				System.out.println("没有工号为：" + empId + "的员工");
//				continue;
//			}
//			insideContact = this.insideContactService.getContactByEmpId(emp
//					.getId());
//
//			// 担当业务
//			readExcel.setCellCol(4);
//			readExcel.setCellRow(index);
//			selfOperation = readExcel.getValue(readExcel.getCell()).toString()
//					.trim();
//			insideContact.setSelfOperation(selfOperation);
//
//			// 地址
//			readExcel.setCellCol(5);
//			readExcel.setCellRow(index);
//			address = readExcel.getValue(readExcel.getCell()).toString().trim();
//			insideContact.setAddress(address);
//
//			// 办公电话
//			readExcel.setCellCol(6);
//			readExcel.setCellRow(index);
//			jobTelephone = readExcel.getValue(readExcel.getCell()).toString()
//					.trim();
//			insideContact.setJobTelephone(jobTelephone);
//
//			// 传真
//			readExcel.setCellCol(7);
//			readExcel.setCellRow(index);
//			fox = readExcel.getValue(readExcel.getCell()).toString().trim();
//			insideContact.setFax(fox);
//
//			// 移动电话
//			readExcel.setCellCol(8);
//			readExcel.setCellRow(index);
//			mobileTelephone = readExcel.getValue(readExcel.getCell())
//					.toString().trim();
//			insideContact.setMobileTelephone(mobileTelephone);
//
//			// 家庭电话
//			readExcel.setCellCol(9);
//			readExcel.setCellRow(index);
//			familyTelephone = readExcel.getValue(readExcel.getCell())
//					.toString().trim();
//			insideContact.setFamilyTelephone(familyTelephone);
//
//			this.bindEntity(insideContact);
//			this.insideContactService.saveOrUpdate(insideContact);
//
//			updateNum++;
//		}
//		System.out.println("一共更新了： " + updateNum + " 条信息！");
//
//		return super.SUCCESS;
//	}

	public String getString(String str) {
		if (str.indexOf(".") != -1) {
			return str.substring(0, str.indexOf("."));
		} else {
			return str;
		}
	}

	public String importEmployee() {
		return "importEmployee";
	}

//	跳转到导入通讯录页面
	public String importEmployeeDetail() {
		return "importEmployeeDetail";
	}

	public String show() throws Exception {
		this.view();
		return "show";
	}

	public String showBySearch() throws Exception {
		this.view();
		return "showBySearch";
	}

	@Override
	public void editCallBack(SysEmployee object) throws Exception {
		if (object != null) {
			if (StringUtils.isNotBlank(get_sysAdminRoleId())) {
				Long roleId = (Long) ConvertUtils.convert(get_sysAdminRoleId(),
						Long.class);

				SysRole role  = new SysRole();
				role.setId(roleId);
				SysAdmin sysAdmin = null;
				if(object.getId() != null){
					sysAdmin = this.sysAdminService.getSysAdminByEmpId(object.getId());
				}
				if(sysAdmin == null){
					sysAdmin = new SysAdmin();
				}
				sysAdmin.setEmployeeId(object);
				sysAdmin.setRoleId(role);
				sysAdmin.setAccount(object.getEmpId());
				sysAdmin.setPassword(EncryptPassword.createPassword(object.getEmpId()));
				sysAdmin.setUseYn("Y");
				this.bindEntity(sysAdmin);
				if(isEditExecutePermission()){
					/*String _phone = StringUtils.defaultIfEmpty(getParameter("_phone"), "");
					String _mphone = StringUtils.defaultIfEmpty(getParameter("_mphone"), "");
					String _address = StringUtils.defaultIfEmpty(getParameter("_address"), "");
					String _remark = StringUtils.defaultIfEmpty(getParameter("_remark"), "");
					String _contact = StringUtils.defaultIfEmpty(getParameter("_contact"), "");
					UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
					SysEmployeeInfo info =this.sysEmployeeInfoService.getObjInfoByEmpId(object.getId());
					if(info == null){
						info = new SysEmployeeInfo();
					}
					info.setPhone(_phone);
					info.setMphone(Long.valueOf(_mphone));
					info.setAddress(_address);
					info.setRemark(_remark);
					info.setEmpId(object);
					info.setContact(_contact);
					this.sysEmployeeInfoService.saveOrUpdate(info);*/
					this.sysAdminService.saveOrUpdate(sysAdmin);
				}
			}
		}

	}

	@Override
	public String delete() throws Exception {
		Serializable[] ids = null;
		ids = getDeleteEntity();
		extendsBeforDelete(ids);
		if (ids != null && isEditExecutePermission()) {
			for (Serializable id : ids) {
				this.sysAdminService.deleteSysAdminByEmpId(Long.valueOf(id
						.toString()));
			}
		}
		deleteCallBack();
		return getDELETE();
	}
	
	@Override
	public void extendsBeforDelete(Serializable[] ids) throws Exception {
		boolean delFlag = false;
		//加入查看权限
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		List<Long> idList = new ArrayList<Long>();
		//获取部门
		this.sysDepartmentService.getChrildDeptIdEx(userInfo.getDepartment().getId(),idList);
		if(idList != null && idList.size() > 0){
			idList.remove(userInfo.getDepartment().getId());
			for(Serializable id:ids){
				long did = (Long) id;
				List<SysEmployee> delList = this.sysEmployeeService.getCheckList(did, idList);
				if(delList != null && delList.size() > 0){
					delFlag = true;
				}else{
					delFlag = false;
					break;
				}
			}
		}
		setEditExecutePermission(delFlag);
	}
	
	@Override
	public void extendsBeforEdit(SysEmployee object) throws Exception {
		if(object != null){
			String _phone = StringUtils.defaultIfEmpty(getParameter("_phone"), "");
			String _mphone = StringUtils.defaultIfEmpty(getParameter("_mphone"), "");
			String _address = StringUtils.defaultIfEmpty(getParameter("_address"), "");
			String _remark = StringUtils.defaultIfEmpty(getParameter("_remark"), "");
			String _contact = StringUtils.defaultIfEmpty(getParameter("_contact"), "");
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");       
			Matcher m = p.matcher(_mphone);  
			//手机号码验证
			/*if(!m.matches() || StringUtils.isBlank(_phone) 
					|| StringUtils.isBlank(_address) 
					|| StringUtils.isBlank(_remark)
					|| StringUtils.isBlank(_contact)){
				setAttribute("_msg", "参数不能为空！");
				setEditExecutePermission(false);
				return;
			}*/
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			Long[] roleIds = userInfo.getRoleIds();
			boolean roleFlag = false;
			for(long roleId : roleIds){
				if(roleId == 1l){
					roleFlag = true;
					break;
				}
			}
			if(!roleFlag){
				boolean editFlag = false;
				SysEmployee emp = this.getSysEmployeeService().getSysEmployeeById(object.getId());
				if(emp != null){
					long deptId = userInfo.getDepartment().getId();
					List<SysDepartment> deptList = this.sysDepartmentService.getOneNextChrildDeptIdEx(deptId);
					if(deptList != null && deptList.size() > 0){
						for(SysDepartment dept:deptList){
							if(emp.getDepartmentId().getId() == dept.getId()){
								editFlag = true;
								break;
							}
						}
					}
				}
				setEditExecutePermission(editFlag);
			}else{
				setEditExecutePermission(roleFlag);
			}
		}
	}
	

	@Override
	public void extendsEdit(SysEmployee object) throws Exception {
		if(!isEditExecutePermission()){
			return;
		}
		if (object != null && object.getId() == null) {
			isCreate = true;
		}
		boolean editFlag = false;
		if (object.getDepartmentId() != null) {
			if (object.getDepartmentId().getId() == null) {
				object.setDepartmentId(null);
			}else{
				UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
				Long[] roleIds = userInfo.getRoleIds();
				for(long roleId : roleIds){
					if(roleId == 1l){
						editFlag = true;
						break;
					}
				}
				if(!editFlag){
					//判断分配的deptId是否在合法范围内
					List<SysDepartment> list = this.sysDepartmentService.getOneNextChrildDeptIdEx(userInfo.getDepartment().getId());
					if(list != null && list.size() > 0){
						long nId = object.getDepartmentId().getId();
						for(SysDepartment dept:list){
							if(dept.getId() == nId){
								editFlag = true;
								break;
							}
						}
					}
				}
			}
		}	
		setEditExecutePermission(editFlag);
		if (StringUtils.isNotBlank(getOpenEmail())) {
			if (getOpenEmail().equals("Y")) {
				setEDIT("openEmail");
			}
		}

	}

	public String onlyEmployeeId() throws Exception {
		if (getSysEmployee() != null) {
			SysEmployee onlySysEmployee = this.sysEmployeeService
					.getSysEmployeeByEmpId(getSysEmployee().getEmpId());
			boolean onlyFlag = false;
			if (onlySysEmployee == null) {
				onlyFlag = true;
			}
			if (onlyFlag) {
				writeTextByAction("true");
			} else {
				writeTextByAction("false");
			}
		}
		return "";
	}

	@Override
	public String execute() throws Exception {
		workStateList = this.sysMasterCodeService
				.getSysMasterListByCodeIdUse("EmpStatus");

		return super.execute();

	}
	@Override
	public void extendsBeforView(Serializable id){
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		Long[] roleIds = userInfo.getRoleIds();
		boolean editFlag = false;
		for(long roleId : roleIds){
			if(roleId == 1l){
				editFlag = true;
			}
		}
		if(!editFlag){
			long nDeptId = userInfo.getDepartment().getId();
			SysEmployee emp = this.getSysEmployeeService().getSysEmployeeById((Long)id);
			if(emp != null){
				List<SysDepartment> deptList = this.sysDepartmentService.getOneNextChrildDeptIdEx(nDeptId);
				if(deptList != null && deptList.size() > 0){
					for(SysDepartment dept:deptList){
						if(emp.getDepartmentId().getId() == dept.getId()){
							editFlag = true;
							break;
						}
					}
				}
			}
			setViewExecutePermission(editFlag);
		}
	}

	@Override
	public void viewCallBack(SysEmployee object) throws Exception {
		if(getViewExecutePermission()){
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			List<SysDeptRole> roleList = this.sysDeptRoleService.getListByDeptId(userInfo.getDepartment().getId());
			setAttribute("roleList", roleList);
			Long[] roleIds = userInfo.getRoleIds();
			if(roleIds != null){
				for(long roleid:roleIds){
					if(1==roleid){
						setAttribute("roleflag", "scu");
						break;
					}
				}
			}
			if(object != null){
				if(object.getId() != null){
					SysAdmin sysAdmin = this.sysAdminService.getSysAdminByEmpId(object.getId());
					setAttribute("_sysAdminRoleId", sysAdmin.getRoleId().getId());
				}
			}
			this.sysRoleList = this.sysRoleService.getSysRoleByUse();
		}
	}

	public String searchEmp() throws Exception {
		return "empHomeList";
	}

	public String doPageData() throws Exception {
		List<SysEmployee> employeeList = this.doGetPageResultEntity();
		JSONObject root = new JSONObject();
		JSONArray arrayItems = new JSONArray();
		if (employeeList != null) {
			for (SysEmployee emp : employeeList) {
				try{
					SysEmployeeInfo info = this.sysEmployeeInfoService.getObjInfoByEmpId(emp.getId());
					JSONObject Item = new JSONObject();
					Item.put("ASIN", emp.getId());
					Item.put("empId", StringUtil.defaultIfEmpty(emp.getEmpId()));
					Item.put("empNameZh", StringUtil.defaultIfEmpty(emp
							.getEmpNameZh()));
					String phone = "";
					String mphone = "";
					String contact = "";
					if(info != null){
						phone = StringUtil.defaultIfEmpty(info.getPhone());
						mphone = StringUtil.defaultIfEmpty(info.getMphone());
						contact = StringUtil.defaultIfEmpty(info.getContact());
					}
					Item.put("phone",phone );
					Item.put("mphone", mphone);
					Item.put("contact", contact);
					Item.put("departmentName", geti18nByBean(emp.getDepartmentId(),
							"departmentName"));
					String uesYn = "<font color=red>禁用</font>";
					if("Y".equals(StringUtil.defaultIfEmpty(emp.getUseYn()))){
						uesYn = "正常";
					}
					Item.put("useVal", StringUtil.defaultIfEmpty(emp.getUseYn()));
					Item.put("useYn", uesYn);
					arrayItems.add(Item);
				}catch (Exception e) {
				}
			}
			root.put("Items", arrayItems);
			root.put("totalCount", Integer
					.toString(getPage().getTotalRecords()));
		}
		writeTextByAction(root.toString());
		return "";
	}

	@Override
	protected SysEmployee getFromBean() {
		return getSysEmployee();
	}

	@Override
	public void searchModelCallBack() {
		//加入查看权限
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		List<Long> idList = new ArrayList<Long>();
		getPageCountCriteria().createAlias("departmentId", "departmentId");
		getPageResultCriteria().createAlias("departmentId", "departmentId");
		//获取部门
		List<Long> cList = this.sysDepartmentService.getChrildDeptIdEx(userInfo.getDepartment().getId(),idList);
		if(cList != null && cList.size() > 0){
			boolean roflag = this.sysRoleService.getCheckRole(userInfo.getRoleIds(), 1);
			if(!roflag){
				cList.remove(userInfo.getDepartment().getId());
			}
			getPageCountCriteria().add(SearchModelUtil.in("departmentId.id",cList.toArray()));
			getPageResultCriteria().add(SearchModelUtil.in("departmentId.id",cList.toArray()));
		}else{
			getPageCountCriteria().add(SearchModelUtil.in("departmentId.id",new Object[]{0l}));
			getPageResultCriteria().add(SearchModelUtil.in("departmentId.id",new Object[]{0l}));
		}
		//获取syadmin表的人员数据
		List<SysAdmin> list = this.sysAdminService.getSysAdminListByDeptIdList(cList);
		if(list != null && list.size() > 0){
			List<Long> idsList = new ArrayList<Long>();
			for(SysAdmin sysAdmin:list){
				idsList.add(sysAdmin.getEmployeeId().getId());
			}
			if(idsList != null && idsList.size() > 0){
				getPageCountCriteria().add(
						SearchModelUtil.in("id", idsList.toArray()));
				getPageResultCriteria().add(
						SearchModelUtil.in("id", idsList.toArray()));
			}
		}
		if (StringUtils.isNotBlank(get_empId())) {
			getPageCountCriteria().add(
					SearchModelUtil.getSearchCriterion("empId", get_empId(),
							SearchMode.LIKE_ANYWHERE_MODE, true));
			getPageResultCriteria().add(
					SearchModelUtil.getSearchCriterion("empId", get_empId(),
							SearchMode.LIKE_ANYWHERE_MODE, true));
		}
		if (StringUtils.isNotBlank(get_empName())) {
			getPageCountCriteria()
					.add(
							SearchModelUtil
									.or(
											SearchModelUtil
													.getSearchCriterion(
															"empNameZh",
															get_empName(),
															SearchMode.LIKE_ANYWHERE_MODE,
															true),
											SearchModelUtil
													.or(
															SearchModelUtil
																	.getSearchCriterion(
																			"empNameEn",
																			get_empName(),
																			SearchMode.LIKE_ANYWHERE_MODE,
																			true),
															SearchModelUtil
																	.getSearchCriterion(
																			"empNameKo",
																			get_empName(),
																			SearchMode.LIKE_ANYWHERE_MODE,
																			true))));
			getPageResultCriteria()
					.add(
							SearchModelUtil
									.or(
											SearchModelUtil
													.getSearchCriterion(
															"empNameZh",
															get_empName(),
															SearchMode.LIKE_ANYWHERE_MODE,
															true),
											SearchModelUtil
													.or(
															SearchModelUtil
																	.getSearchCriterion(
																			"empNameEn",
																			get_empName(),
																			SearchMode.LIKE_ANYWHERE_MODE,
																			true),
															SearchModelUtil
																	.getSearchCriterion(
																			"empNameKo",
																			get_empName(),
																			SearchMode.LIKE_ANYWHERE_MODE,
																			true))));

		}
		if (StringUtils.isNotBlank(get_departmentName())) {

			getPageCountCriteria().add(
					SearchModelUtil.or(
							SearchModelUtil.getSearchCriterion("departmentId.departmentNameZh",
															get_departmentName(),
															SearchMode.LIKE_ANYWHERE_MODE,
															true),
											SearchModelUtil.or(SearchModelUtil.getSearchCriterion(
																			"departmentId.departmentNameEn",
																			get_departmentName(),
																			SearchMode.LIKE_ANYWHERE_MODE,
																			true),
															SearchModelUtil.getSearchCriterion(
																			"departmentId.departmentNameKo",
																			get_departmentName(),
																			SearchMode.LIKE_ANYWHERE_MODE,
																			true))));
			getPageResultCriteria()
					.add(SearchModelUtil.or(SearchModelUtil.getSearchCriterion(
															"departmentId.departmentNameZh",
															get_departmentName(),
															SearchMode.LIKE_ANYWHERE_MODE,
															true),
											SearchModelUtil.or(SearchModelUtil.getSearchCriterion(
																			"departmentId.departmentNameEn",
																			get_departmentName(),
																			SearchMode.LIKE_ANYWHERE_MODE,
																			true),
															SearchModelUtil.getSearchCriterion(
																			"departmentId.departmentNameKo",
																			get_departmentName(),
																			SearchMode.LIKE_ANYWHERE_MODE,
																			true))));

		}
		
		//
		getPageResultCriteria().addOrder(Order.desc("created"));
	}
	/**
	 * 禁止账号
	 */
	public void ajaxByDisable(){
		JSONObject root = new JSONObject();
		String[] idsStr = getParameters("_id");
		String useYn = StringUtil.defaultIfEmpty(getParameter("_useYn"));
		boolean delFlag = false;
		if(idsStr != null && ("Y".equals(useYn) || "N".equals(useYn))){
			List<Long> uList = new ArrayList<Long>();
			//加入查看权限
			UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
			List<Long> idList = new ArrayList<Long>();
			//获取部门
			this.sysDepartmentService.getChrildDeptIdEx(userInfo.getDepartment().getId(),idList);
			if(idList != null && idList.size() > 0){
				idList.remove(userInfo.getDepartment().getId());
				for(String id:idsStr){
					long did = Long.valueOf(id) ;
					uList.add(did);
					List<SysEmployee> delList = this.sysEmployeeService.getCheckList(did, idList);
					if(delList != null && delList.size() > 0){
						delFlag = true;
					}else{
						delFlag = false;
						break;
					}
				}
			}
			if(delFlag){
				delFlag = this.sysAdminService.updateSyaAdminUseByUseYn(uList, useYn);
			}
		}
		if(delFlag){
			root.put("result", "suc");
		}else{
			root.put("result", "err");
		}
		writeTextByAction(root.toString());
	}

	public SysEmployee getSysEmployee() {
		if (getProxyEntity() != null) {
			return getProxyEntity();
		}
		return sysEmployee;
	}

	public void setSysEmployee(SysEmployee sysEmployee) {
		this.sysEmployee = sysEmployee;
	}

	public List<SysEmployee> getSysEmployeeList() {
		if (getProxyEntityList() != null) {
			return getProxyEntityList();
		}
		return sysEmployeeList;
	}

	public void setSysEmployeeList(List<SysEmployee> sysEmployeeList) {
		this.sysEmployeeList = sysEmployeeList;
	}

	public SysEmployeeService getSysEmployeeService() {
		return sysEmployeeService;
	}

	public void setSysEmployeeService(SysEmployeeService sysEmployeeService) {
		this.sysEmployeeService = sysEmployeeService;
	}

	public SysMasterCodeService getSysMasterCodeService() {
		return sysMasterCodeService;
	}

	public void setSysMasterCodeService(
			SysMasterCodeService sysMasterCodeService) {
		this.sysMasterCodeService = sysMasterCodeService;
	}

	public List<SysMasterCode> getWorkStateList() {
		return workStateList;
	}

	public void setWorkStateList(List<SysMasterCode> workStateList) {
		this.workStateList = workStateList;
	}

	public String get_departmentName() {
		return _departmentName;
	}

	public void set_departmentName(String name) {
		_departmentName = name;
	}

	public String get_dutyName() {
		return _dutyName;
	}

	public void set_dutyName(String name) {
		_dutyName = name;
	}

	public String get_empId() {
		return _empId;
	}

	public void set_empId(String id) {
		_empId = id;
	}

	public String get_workState() {
		return _workState;
	}

	public void set_workState(String state) {
		_workState = state;
	}

	public List<SysMasterCode> getSexList() {
		return sexList;
	}

	public void setSexList(List<SysMasterCode> sexList) {
		this.sexList = sexList;
	}

	public List<SysMasterCode> getDegreeList() {
		return degreeList;
	}

	public void setDegreeList(List<SysMasterCode> degreeList) {
		this.degreeList = degreeList;
	}

	
	public String getOpenEmail() {
		return openEmail;
	}

	public void setOpenEmail(String openEmail) {
		this.openEmail = openEmail;
	}

	

	public List<SysRole> getSysRoleList() {
		return sysRoleList;
	}

	public void setSysRoleList(List<SysRole> sysRoleList) {
		this.sysRoleList = sysRoleList;
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	public String get_sysAdminAccount() {
		return _sysAdminAccount;
	}

	public void set_sysAdminAccount(String adminAccount) {
		_sysAdminAccount = adminAccount;
	}

	public String get_sysAdminPassword() {
		return _sysAdminPassword;
	}

	public void set_sysAdminPassword(String adminPassword) {
		_sysAdminPassword = adminPassword;
	}

	public String get_sysAdminRoleId() {
		return _sysAdminRoleId;
	}

	public void set_sysAdminRoleId(String adminRoleId) {
		_sysAdminRoleId = adminRoleId;
	}

	public SysAdminService getSysAdminService() {
		return sysAdminService;
	}

	public void setSysAdminService(SysAdminService sysAdminService) {
		this.sysAdminService = sysAdminService;
	}

	public String get_empName() {
		return _empName;
	}

	public void set_empName(String name) {
		_empName = name;
	}



	

	public SysMenuService getSysMenuService() {
		return sysMenuService;
	}

	public void setSysMenuService(SysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	public File getEmpXLS() {
		return empXLS;
	}

	public void setEmpXLS(File empXLS) {
		this.empXLS = empXLS;
	}

	public SysDepartmentService getSysDepartmentService() {
		return sysDepartmentService;
	}

	public void setSysDepartmentService(SysDepartmentService sysDepartmentService) {
		this.sysDepartmentService = sysDepartmentService;
	}

	public String getDimission() {
		return dimission;
	}

	public void setDimission(String dimission) {
		this.dimission = dimission;
	}

	public String getIncumbency() {
		return incumbency;
	}

	public void setIncumbency(String incumbency) {
		this.incumbency = incumbency;
	}

	public List<String> getListError() {
		return listError;
	}

	public void setListError(List<String> listError) {
		this.listError = listError;
	}

	public SysDeptRoleService getSysDeptRoleService() {
		return sysDeptRoleService;
	}

	public void setSysDeptRoleService(SysDeptRoleService sysDeptRoleService) {
		this.sysDeptRoleService = sysDeptRoleService;
	}

	public SysEmployeeInfoService getSysEmployeeInfoService() {
		return sysEmployeeInfoService;
	}

	public void setSysEmployeeInfoService(
			SysEmployeeInfoService sysEmployeeInfoService) {
		this.sysEmployeeInfoService = sysEmployeeInfoService;
	}
	
	
	

}
