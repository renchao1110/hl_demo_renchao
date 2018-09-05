package com.hl95.sys.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysRole;
import com.hl95.sys.service.SysAdminService;
import com.hl95.sys.service.SysDepartmentService;
import com.hl95.sys.service.SysEmployeeService;
import com.hl95.sys.service.SysRoleService;
import com.hl95.utils.Constants;
import com.hl95.utils.MD5Util;
import com.hl95.utils.RandomID;
import com.opensymphony.xwork.Action;

public class SysAdminAction extends BusinessAction<SysAdmin, SysAdminService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysAdmin sysAdmin;

	private SysAdminService sysAdminService;

	private List<SysAdmin> sysAdminList;

	private SysRoleService sysRoleService;

	private List<SysRole> sysRoleList;

	private String _empName;

	private String _roleName;

	private SysEmployeeService sysEmployeeService;

	private SysDepartmentService sysDepartmentService;

	private String errorMsg;

	private File[] doc;

	private String[] docFileName;

	private String originalPwd;

	private String userName;

	private String question;

	private String lang;

	private static final String downloadKey = "intranet.incontact.incontactPic";

	public String show() throws Exception {
		this.view();
		return "show";
	}
	
	public String loadDepartmentTreeByEmployeeAdmin() throws Exception {
		List<SysDepartment> departmentTreeList = null;
		SysDepartment sysDepartmentParent = null;
		Long[] notInId = null;
		if (StringUtils.isBlank(getParameter("parentId"))) {
			return Action.INPUT;
		} else {
			if (getParameter("parentId").equals("managerRoot")) {
				departmentTreeList = this.sysDepartmentService
						.getRootDepartments();
			} else {

				Long parentId = (Long) ConvertUtils.convert(
						getParameter("parentId"), Long.class);
				List<SysAdmin> adminList = this.sysAdminService
						.getSysAdminListByDeptId(parentId);
				if (adminList != null) {
					if (adminList.size() > 0) {
						notInId = new Long[adminList.size()];
						int index = 0;
						for (SysAdmin ad : adminList) {
							notInId[index] = ad.getEmployeeId().getId();
							index++;
						}
					}
				}
				sysDepartmentParent = this.sysDepartmentService
						.getEntityById(parentId);
				departmentTreeList = this.sysDepartmentService
						.getChildDepartments(parentId);
			}
		}
		JSONArray arrayItems = new JSONArray();
		JSONObject Item = null;

		List<SysEmployee> employees = null;
		if (sysDepartmentParent != null) {
			if (notInId != null) {
				employees = this.sysEmployeeService
						.getSysEmployeeListByDeptIdNotInId(
								sysDepartmentParent.getId(), notInId);
			} else {
				employees = this.sysEmployeeService
						.getSysEmployeeListByDeptId(sysDepartmentParent.getId());
			}
			if (employees != null) {
				if (employees.size() > 0) {
					for (SysEmployee employee : employees) {
						Item = new JSONObject();
						Item.put("text", geti18nByBean(employee, "empName"));
						Item.put("id", employee.getId());
						Item.put("cls", "file");
						Item.put("leaf", true);
						Item.put("icon", "/images/icon/member.gif");
						Item.put("typeClass", "employee");
						Item.put("allowDrag", false);
						Item.put("allowDrop", false);
						arrayItems.add(Item);
					}
				}
			}
		}
		boolean hasChindls = false;
		boolean hasEmployees = true;
		boolean finalB;
		if (departmentTreeList != null) {
			int index = 0;

			for (SysDepartment sysDepartment : departmentTreeList) {
				employees = this.sysEmployeeService
						.getSysEmployeeListByDeptId(sysDepartment.getId());
				Item = new JSONObject();
				Item.put("text", geti18nByBean(sysDepartment, "departmentName"));
				Item.put("id", sysDepartment.getId());
				Item.put("cls", "file");
				hasChindls = this.sysDepartmentService.hasChilds(sysDepartment
						.getId());
				if (employees != null) {
					if (employees.size() > 0) {
						hasEmployees = false;
					} else {
						hasEmployees = true;
					}
				}
				if (!hasChindls) {
					finalB = false;
				} else {
					if (hasChindls && !hasEmployees) {
						finalB = false;
					} else {
						finalB = true;
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

	public String onlyAccount() throws Exception {
		if (getSysAdmin() != null) {
			String isEdit = getParameter("isEdit");

			SysAdmin onlyAdmin = this.sysAdminService
					.getSysAdminByAccount(getSysAdmin().getAccount());
			boolean onlyFlag = false;
			if (onlyAdmin == null) {
				onlyFlag = true;
			} else {
				if (isEdit.equals("1")) {
					if (onlyAdmin.getId().equals(getSysAdmin().getId())) {
						onlyFlag = true;
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

	// public String editPassword() throws Exception {
	// this.edit();
	// setErrorMsg(geti18nResource("sys.admin.ess.password"));
	// return "editPassword";
	// }

	public static String getBASE64(String s) {
		if (s == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void extendsEdit(SysAdmin object) throws Exception {
		if (object.getId() == null) {
			object.setPassword(EncryptPassword.createPassword(object
					.getAccount()));
		}

		// MailInfo mailInfo = mailInfoService.getMailInfoByEmpId(object
		// .getEmployeeId().getId());
		// if(mailInfo!=null){
		// mailInfo.setPassword(object.getPassword());
		// mailInfoService.saveOrUpdate(mailInfo);
		// }
		// String mailPassword = object.getPassword();

		// object
		// .setPassword(EncryptPassword.createPassword(object
		// .getPassword()));
		// String str = object.getPassword();
		// System.out.println("[原始串]"+str);

		// String enstr = URLEncoder.encode(str,"gb2312");
		// object.setUpdatedBy(enstr);
		// System.out.println("[编码后]"+enstr);
		// String destr = URLDecoder.decode(enstr, "gb2312");
		// System.out.println("[解码后]"+destr);

		// object.setQuestion(EncryptPassword.createPassword(object.getQuestion()));
		// object.setChangePassword("Y");
		// editMailPassword(object.getAccount(), mailPassword);
		// setErrorMsg(geti18nResource("mail.client.password.alert"));
		// InsideContact insideContact = insideContactService
		// .getContactByEmpId(UserSession.getUserInfo(
		// this.getHttpServletRequest()).getEmployee().getId());
		// if (insideContact != null) {
		// MailManager manager = new MailManager("GW系统密码修改确认", insideContact
		// .getEmailExternal(),
		// "尊敬的用户您好！<br/>您的密码已经被成功修改,请您妥善保管,该邮件是自动发送邮件,不需要回复,如有任何问题请联络管理员。");
		// manager.send();
		// }
		// sysAdminService.saveOrUpdate(object);
	}

	public String editPassword() throws Exception {

		UserInfo userInfo = UserSession.getUserInfo(this
				.getHttpServletRequest());

		/**
		 * 上传照片
		 */
		if (getDoc() != null && docFileName != null) {
			UploadManager uploadManager = new UploadManager(
					this.getWebRootPath(), downloadKey, getWebServletContext());
			for (File f : getDoc()) {
				String fileName = docFileName[0].toString();
				String postfix = "";
				int _index = fileName.lastIndexOf(".") + 1;
				if (_index > -1) {
					postfix = fileName.substring(_index);
					postfix = postfix.toLowerCase();

				}
				uploadManager.setUploadFileName(userInfo.getEmployee()
						.getEmpId() + ".jpg");
				uploadManager.doUploadFile(f);
			}

		}
		JSONObject root = new JSONObject();
		int result = 2;
		originalPwd = getParameter("old_pwd");
		if (originalPwd != null) {
			SysAdmin originalAdmin = this.sysAdminService
					.getSysAdminByEmpIdUse(UserSession
							.getUserInfo(this.getHttpServletRequest())
							.getEmployee().getId());
			if (originalAdmin != null
					&& originalAdmin.getPassword().equals(
							EncryptPassword.createPassword(originalPwd))) {
				originalAdmin.setPassword(EncryptPassword
						.createPassword(getParameter("new_pwd")));
				originalAdmin.setQuestion(getParameter("sysAdmin.question"));
				originalAdmin.setChangePassword("Y");
				this.sysAdminService.saveOrUpdate(originalAdmin);
				setErrorMsg(geti18nResource("sys.admin.ess.password"));
				result = 0;
			} else {
				setErrorMsg("Original Password is incorrect");
				result = 1;
			}
		}
		root.put("result", result);
		writeTextByAction(root.toString());
		return "";
	}

	public String password() throws Exception {
		UserInfo userInfo = UserSession.getUserInfo(this
				.getHttpServletRequest());
		sysAdmin = this.sysAdminService.getSysAdminByEmpIdUse(userInfo
				.getEmployee().getId());
		return "password";
	}

	public String resetPassword() throws Exception {
		if (sysAdmin != null) {
			if (sysAdmin.getId() != null
					&& StringUtils.isNotEmpty(sysAdmin.getId().toString())) {
				sysAdmin = this.sysAdminService.getEntityById(sysAdmin.getId());
				sysAdmin.setPassword(EncryptPassword.createPassword(sysAdmin
						.getAccount()));
				sysAdmin.setChangePassword(null);
				sysAdmin.setQuestion(null);
				this.sysAdminService.saveOrUpdate(sysAdmin);
			}
		}

		return "edit";
	}

	public String resetPasswordLogin() throws Exception {

		SysAdmin admin = sysAdminService.getSysAdminByAccount(userName);
		if (admin != null) {
			if (admin.getQuestion() != null) {
				if (admin.getQuestion().equals(question)) {
					admin.setPassword(EncryptPassword.createPassword("000000"));
					admin.setChangePassword(null);
					this.sysAdminService.saveOrUpdate(admin);
					setErrorMsg("ok");
				} else {
					setErrorMsg("errorQuestion");
				}
			} else {
				setErrorMsg("errorNoQuestion");
			}
		} else {
			setErrorMsg("errorEmployee");
		}

		return "resumePassword";
	}

	@Override
	public void viewCallBack(SysAdmin object) throws Exception {
		this.sysRoleList = this.sysRoleService.getSysRoleByUse();
	}

	public String doPageData() throws Exception {
		List<SysAdmin> adminList = this.doGetPageResultEntity();
		JSONObject root = new JSONObject();
		JSONArray arrayItems = new JSONArray();
		if (adminList != null) {
			for (SysAdmin admin : adminList) {
				JSONObject Item = new JSONObject();
				Item.put("ASIN", admin.getId());
				Item.put("empName",
						geti18nByBean(admin.getEmployeeId(), "empName"));
				Item.put("roleName",
						geti18nByBean(admin.getRoleId(), "roleName"));
				Item.put("account",
						StringUtil.defaultIfEmpty(admin.getAccount()));
				Item.put("password",
						StringUtil.defaultIfEmpty(admin.getPassword()));
				Item.put("useYn", StringUtil.defaultIfEmpty(admin.getUseYn()));
				arrayItems.add(Item);
			}
			root.put("Items", arrayItems);
			root.put("totalCount",
					Integer.toString(getPage().getTotalRecords()));
		}
		writeTextByAction(root.toString());
		return "";
	}

	@Override
	protected SysAdmin getFromBean() {
		// TODO Auto-generated method stub
		return getSysAdmin();
	}

	@Override
	public void searchModelCallBack() {

		if (StringUtils.isNotBlank(get_empName())) {
			getPageCountCriteria().createAlias("employeeId", "employeeId");
			getPageResultCriteria().createAlias("employeeId", "employeeId");

			getPageCountCriteria()
					.add(SearchModelUtil.or(
							SearchModelUtil.getSearchCriterion(
									"employeeId.empNameZh", get_empName(),
									SearchMode.LIKE_ANYWHERE_MODE, true),
							SearchModelUtil.or(
									SearchModelUtil
											.getSearchCriterion(
													"employeeId.empNameEn",
													get_empName(),
													SearchMode.LIKE_ANYWHERE_MODE,
													true),
									SearchModelUtil
											.getSearchCriterion(
													"employeeId.empNameKo",
													get_empName(),
													SearchMode.LIKE_ANYWHERE_MODE,
													true))));
			getPageResultCriteria()
					.add(SearchModelUtil.or(
							SearchModelUtil.getSearchCriterion(
									"employeeId.empNameZh", get_empName(),
									SearchMode.LIKE_ANYWHERE_MODE, true),
							SearchModelUtil.or(
									SearchModelUtil
											.getSearchCriterion(
													"employeeId.empNameEn",
													get_empName(),
													SearchMode.LIKE_ANYWHERE_MODE,
													true),
									SearchModelUtil
											.getSearchCriterion(
													"employeeId.empNameKo",
													get_empName(),
													SearchMode.LIKE_ANYWHERE_MODE,
													true))));

		}
		if (StringUtils.isNotBlank(get_roleName())) {
			getPageCountCriteria().createAlias("roleId", "roleId");
			getPageResultCriteria().createAlias("roleId", "roleId");

			getPageCountCriteria()
					.add(SearchModelUtil.or(
							SearchModelUtil.getSearchCriterion(
									"roleId.roleNameZh", get_roleName(),
									SearchMode.LIKE_ANYWHERE_MODE, true),
							SearchModelUtil.or(
									SearchModelUtil
											.getSearchCriterion(
													"roleId.roleNameEn",
													get_roleName(),
													SearchMode.LIKE_ANYWHERE_MODE,
													true),
									SearchModelUtil
											.getSearchCriterion(
													"roleId.roleNameKo",
													get_roleName(),
													SearchMode.LIKE_ANYWHERE_MODE,
													true))));

			getPageResultCriteria()
					.add(SearchModelUtil.or(
							SearchModelUtil.getSearchCriterion(
									"roleId.roleNameZh", get_roleName(),
									SearchMode.LIKE_ANYWHERE_MODE, true),
							SearchModelUtil.or(
									SearchModelUtil
											.getSearchCriterion(
													"roleId.roleNameEn",
													get_roleName(),
													SearchMode.LIKE_ANYWHERE_MODE,
													true),
									SearchModelUtil
											.getSearchCriterion(
													"roleId.roleNameKo",
													get_roleName(),
													SearchMode.LIKE_ANYWHERE_MODE,
													true))));

		}

		getPageResultCriteria().addOrder(Order.asc("id"));

	}

	public SysAdmin getSysAdmin() {
		if (getProxyEntity() != null) {
			return getProxyEntity();
		}
		return sysAdmin;
	}

	public void setSysAdmin(SysAdmin sysAdmin) {
		this.sysAdmin = sysAdmin;
	}

	public List<SysAdmin> getSysAdminList() {
		if (getProxyEntityList() != null) {
			return getProxyEntityList();
		}
		return sysAdminList;
	}

	public void setSysAdminList(List<SysAdmin> sysAdminList) {
		this.sysAdminList = sysAdminList;
	}

	public SysAdminService getSysAdminService() {
		return sysAdminService;
	}

	public void setSysAdminService(SysAdminService sysAdminService) {
		this.sysAdminService = sysAdminService;
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

	public String get_empName() {
		return _empName;
	}

	public void set_empName(String name) {
		_empName = name;
	}

	public String get_roleName() {
		return _roleName;
	}

	public void set_roleName(String name) {
		_roleName = name;
	}

	public SysDepartmentService getSysDepartmentService() {
		return sysDepartmentService;
	}

	public void setSysDepartmentService(
			SysDepartmentService sysDepartmentService) {
		this.sysDepartmentService = sysDepartmentService;
	}

	public SysEmployeeService getSysEmployeeService() {
		return sysEmployeeService;
	}

	public void setSysEmployeeService(SysEmployeeService sysEmployeeService) {
		this.sysEmployeeService = sysEmployeeService;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public File[] getDoc() {
		return doc;
	}

	public void setDoc(File[] doc) {
		this.doc = doc;
	}

	public String[] getDocFileName() {
		return docFileName;
	}

	public void setDocFileName(String[] docFileName) {
		this.docFileName = docFileName;
	}

	public String getOriginalPwd() {
		return originalPwd;
	}

	public void setOriginalPwd(String originalPwd) {
		this.originalPwd = originalPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
