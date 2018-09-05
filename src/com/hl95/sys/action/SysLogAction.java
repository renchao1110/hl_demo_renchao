package com.hl95.sys.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.util.DateFormatUtil;
import org.pj.criterion.core.util.SearchModelUtil;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.support.SearchMode;

import com.hl95.sys.entity.SysLog;
import com.hl95.sys.service.SysLogService;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SysLogAction extends BusinessAction<SysLog, SysLogService>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysLog sysLog;
	
	private SysLogService sysLogService;
	
	private String _startDate;
	
	private String _endDate;
	
	private String _empName;
	
	private String _logAction;
	
	
	public String get_empName() {
		return _empName;
	}

	public void set_empName(String name) {
		_empName = name;
	}

	public String get_logAction() {
		return _logAction;
	}

	public void set_logAction(String action) {
		_logAction = action;
	}

	public String doPageData() throws Exception {
		List<SysLog> logList = this.doGetPageResultEntity();
		JSONObject root = new JSONObject();
		JSONArray arrayItems = new JSONArray();
		if (logList != null) {
			for (SysLog log : logList) {
				JSONObject Item = new JSONObject();
				Item.put("ASIN", log.getId());
				Item.put("empId", log.getEmployee()==null?"":log.getEmployee().getEmpId());
				Item.put("empName", geti18nByBean(log.getEmployee(), "empName"));
				Item.put("logDate", DateFormatUtil.format(log.getLogDate(), DateFormatUtil.YEAR_MONTH_DAY_PATTERN));
				Item.put("logTime", DateFormatUtil.format(log.getLogDate(), DateFormatUtil.HOUR_MINUTE_SECOND_PATTERN));
				Item.put("logAction", log.getLogAction());
				Item.put("logIp", log.getLogIp());
				Item.put("useYn", log.getUseYn());
				arrayItems.add(Item);
			}
			root.put("Items", arrayItems);
			root.put("totalCount", Integer
					.toString(getPage().getTotalRecords()));
		}
		writeTextByAction(root.toString());
		return "";
	}
	
	@Override
	protected SysLog getFromBean() {
		// TODO Auto-generated method stub
		return getSysLog();
	}

	@Override
	public void searchModelCallBack() {
		if(StringUtils.isNotBlank(get_startDate())){
			Date start=DateFormatUtil.parse(get_startDate()+" 00:00:01",DateFormatUtil.YMDHM_PATTERN);
			getPageCountCriteria().add(SearchModelUtil.getSearchCriterion("logDate", start, SearchMode.GE_MODE,false));
			getPageResultCriteria().add(SearchModelUtil.getSearchCriterion("logDate", start, SearchMode.GE_MODE,false));
		}
		if(StringUtils.isNotBlank(get_endDate())){
			Date end=DateFormatUtil.parse(get_endDate()+" 23:59:59",DateFormatUtil.YMDHM_PATTERN);
			getPageCountCriteria().add(SearchModelUtil.getSearchCriterion("logDate", end, SearchMode.LE_MODE,false));
			getPageResultCriteria().add(SearchModelUtil.getSearchCriterion("logDate", end, SearchMode.LE_MODE,false));
		}
		
		if(StringUtils.isNotBlank(_empName)){
			getPageCountCriteria().createAlias("employee", "employee");
			getPageResultCriteria().createAlias("employee", "employee");
			
			getPageCountCriteria().add(
				SearchModelUtil.or(
					SearchModelUtil.getSearchCriterion("employee.empId", get_empName(),SearchMode.EQUAL_MODE,true),
					SearchModelUtil.or(
						SearchModelUtil.getSearchCriterion(
							"employee.empNameZh", get_empName(),SearchMode.LIKE_START_MODE, true),
						SearchModelUtil.or(
							SearchModelUtil.getSearchCriterion(
									"employee.empNameEn",get_empName(),SearchMode.LIKE_START_MODE,true), 
							SearchModelUtil.getSearchCriterion(
									"employee.empNameKo",get_empName(),SearchMode.LIKE_START_MODE,true)
						)
					)
				)
			);
			
			getPageResultCriteria().add(
				SearchModelUtil.or(
					SearchModelUtil.getSearchCriterion("employee.empId", get_empName(),SearchMode.EQUAL_MODE,true),
					SearchModelUtil.or(
						SearchModelUtil.getSearchCriterion(
							"employee.empNameZh", get_empName(),SearchMode.LIKE_START_MODE, true),
						SearchModelUtil.or(
							SearchModelUtil.getSearchCriterion(
									"employee.empNameEn",get_empName(),SearchMode.LIKE_START_MODE,true), 
							SearchModelUtil.getSearchCriterion(
									"employee.empNameKo",get_empName(),SearchMode.LIKE_START_MODE,true)
						)
					)
				)
			);
		}
		
		if(StringUtils.isNotBlank(_logAction)){
			getPageCountCriteria().add(SearchModelUtil.getSearchCriterion("logAction", get_logAction(), SearchMode.LIKE_ANYWHERE_MODE,false));
			getPageResultCriteria().add(SearchModelUtil.getSearchCriterion("logAction", get_logAction(), SearchMode.LIKE_ANYWHERE_MODE,false));			
		}
		
		getPageResultCriteria().addOrder(Order.desc("logDate"));
	}
	
	public SysLog getSysLog() {
		if(getProxyEntity()!=null){
			return getProxyEntity();
		}
		return sysLog;
	}

	public void setSysLog(SysLog sysLog) {
		this.sysLog = sysLog;
	}

	public SysLogService getSysLogService() {
		return sysLogService;
	}

	public void setSysLogService(SysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public String get_endDate() {
		return _endDate;
	}

	public void set_endDate(String date) {
		_endDate = date;
	}

	public String get_startDate() {
		return _startDate;
	}

	public void set_startDate(String date) {
		_startDate = date;
	}

}
