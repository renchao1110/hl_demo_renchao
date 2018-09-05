package com.hl95.sys.action;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.Order;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.pj.criterion.core.support.excel.CreateExcelManager;
import org.pj.criterion.core.util.DateFormatUtil;
import org.pj.criterion.core.util.SearchModelUtil;
import org.pj.criterion.core.util.StringUtil;
import org.pj.criterion.core.util.StringUtils;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;

import com.hl95.sys.entity.SysDepartment;
import com.hl95.sys.entity.SysEmployee;
import com.hl95.sys.entity.SysMasterCode;
import com.hl95.sys.service.SysDepartmentService;
import com.hl95.sys.service.SysEmployeeService;
import com.hl95.sys.service.SysMasterCodeService;


public class MainLeftAction extends BusinessAction<SysEmployee, SysEmployeeService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysEmployee sysEmployee;
	private SysEmployeeService sysEmployeeService;
	private SysDepartmentService sysDepartmentService;
	
	
	@Override
	public String execute() throws Exception {
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		List<Long> idList = new ArrayList<Long>();
		//获取部门
		List<Long> cList = this.sysDepartmentService.getChrildDeptIdEx(userInfo.getDepartment().getId(),idList);
		if(cList != null && cList.size() > 0){
			cList.remove(userInfo.getDepartment().getId());
			if(cList.size() > 0){
				return super.execute();
			}
		}
		return "dwel";
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
	public SysEmployeeService getSysEmployeeService() {
		return sysEmployeeService;
	}
	public void setSysEmployeeService(SysEmployeeService sysEmployeeService) {
		this.sysEmployeeService = sysEmployeeService;
	}
	@Override
	protected SysEmployee getFromBean() {
		return getSysEmployee();
	}
	@Override
	public void searchModelCallBack() {
		UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
		List<Long> idList = new ArrayList<Long>();
		//获取部门
		List<Long> cList = this.sysDepartmentService.getChrildDeptIdEx(userInfo.getDepartment().getId(),idList);
		if(cList != null && cList.size() > 0){
			cList.remove(userInfo.getDepartment().getId());
			getPageCountCriteria().createAlias("departmentId", "departmentId");
			getPageResultCriteria().createAlias("departmentId", "departmentId");
			getPageCountCriteria().add(SearchModelUtil.in("departmentId.id",cList.toArray()));
			getPageResultCriteria().add(SearchModelUtil.in("departmentId.id",cList.toArray()));
		}else{
			getPageCountCriteria().createAlias("departmentId", "departmentId");
			getPageResultCriteria().createAlias("departmentId", "departmentId");
			getPageCountCriteria().add(SearchModelUtil.in("departmentId.id",new Object[]{userInfo.getDepartment().getId()}));
			getPageResultCriteria().add(SearchModelUtil.in("departmentId.id",new Object[]{userInfo.getDepartment().getId()}));
		}
		getPageResultCriteria().addOrder(Order.desc("created"));
	}
	
	public String doPageData() throws Exception {
		List<SysEmployee> list = this.doGetPageResultEntity();
		JSONObject root = new JSONObject();
		JSONArray arrayItems = new JSONArray();
		if (list != null) {
			int i = 1;
			for (SysEmployee obj : list) {
				try{
					JSONObject Item = new JSONObject();
					Item.put("ASIN", i);
					Item.put("empNameZh", StringUtil.defaultIfEmpty(obj.getEmpNameZh()));
					Item.put("created", StringUtil.defaultIfEmpty(DateFormatUtil.format(obj.getCreated(), DateFormatUtil.YMDHMS_PATTERN)));
					String useStatus = StringUtil.defaultIfEmpty(obj.getUseYn());
					if(StringUtils.isNotBlank(useStatus) && "Y".equals(useStatus)){
						Item.put("useYn","开通");
					}else{
						Item.put("useYn","禁止");
					}
					
					if(obj.getDepartmentId() != null){
						Item.put("dName", StringUtil.defaultIfEmpty(obj.getDepartmentId().getDepartmentNameZh()));
						if(obj.getDepartmentId().getParentId() != null){
							Item.put("dpName", StringUtil.defaultIfEmpty(obj.getDepartmentId().getParentId().getDepartmentNameZh()));
						}else{
							Item.put("dpName", "无");
						}
					}else{
						Item.put("dName", "无");
						Item.put("dpName", "无");
					}
					arrayItems.add(Item);
					i++;
				}catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			root.put("Items", arrayItems);
			root.put("totalCount", Integer
					.toString(getPage().getTotalRecords()));
		}
		writeTextByAction(root.toString());
		return "";
	}


	public SysDepartmentService getSysDepartmentService() {
		return sysDepartmentService;
	}


	public void setSysDepartmentService(SysDepartmentService sysDepartmentService) {
		this.sysDepartmentService = sysDepartmentService;
	}
	
	
	
	

	

}
