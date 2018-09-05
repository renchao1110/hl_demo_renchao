package com.hl95.sys.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.util.SearchModelUtil;
import org.pj.criterion.core.util.StringUtil;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.support.SearchMode;

import com.hl95.sys.entity.SysCompany;
import com.hl95.sys.service.SysCompanyService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SysCompanyAction extends
		BusinessAction<SysCompany, SysCompanyService> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysCompanyService sysCompanyService;

	private SysCompany sysCompany;

	private String _cpnyName;

	private String _cpnyId;

	private List<SysCompany> sysCompanyList;

	public String show() throws Exception {
		this.view();
		return "show";
	}

	public String onlyCpnyId() throws Exception {
		if (getSysCompany() != null) {
			SysCompany onlyCompany = this.sysCompanyService
					.getSysCompanyById(getSysCompany().getCpnyId());
			boolean onlyFlag = false;
			if (onlyCompany == null) {
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

	public String onlyCpnyNameZh() throws Exception {
		if (getSysCompany() != null) {
			String isEdit = getParameter("isEdit");

			SysCompany onlyCompany = this.sysCompanyService
					.getSysCompanyByNameZh(getSysCompany().getCpnyNameZh());
			boolean onlyFlag = false;
			if (onlyCompany == null) {
				onlyFlag = true;
			} else {
				if (isEdit.equals("1")) {
					if (onlyCompany.getId().equals(getSysCompany().getId())) {
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

	public String doPageData() throws Exception {
		List<SysCompany> companyList = this.doGetPageResultEntity();
		JSONObject root = new JSONObject();
		JSONArray arrayItems = new JSONArray();
		if (companyList != null) {
			for (SysCompany cpny : companyList) {
				JSONObject Item = new JSONObject();
				Item.put("ASIN", cpny.getId().toString());
				Item.put("cpnyId", StringUtil.defaultIfEmpty(cpny.getCpnyId()));
				Item.put("cpnyNameZh", StringUtil.defaultIfEmpty(cpny
						.getCpnyNameZh()));
				Item.put("cpnyNameEn", StringUtil.defaultIfEmpty(cpny
						.getCpnyNameEn()));
				Item.put("cpnyNameKo", StringUtil.defaultIfEmpty(cpny
						.getCpnyNameKo()));
				Item.put("cpnyDescription", StringUtil.defaultIfEmpty(cpny
						.getCpnyDescription()));
				Item.put("useYn", StringUtil.defaultIfEmpty(cpny.getUseYn()));
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
	public void searchModelCallBack() {

		getPageResultCriteria().addOrder(Order.asc("id"));

		if (StringUtils.isNotBlank(get_cpnyId())) {
			getPageCountCriteria().add(
					SearchModelUtil.getSearchCriterion("cpnyId", get_cpnyId(),SearchMode.LIKE_ANYWHERE_MODE,
							true));
			getPageResultCriteria().add(
					SearchModelUtil.getSearchCriterion("cpnyId", get_cpnyId(),SearchMode.LIKE_ANYWHERE_MODE,
							true));
		}
		if (StringUtils.isNotBlank(get_cpnyName())) {
			getPageCountCriteria().add(
					SearchModelUtil.or(SearchModelUtil.getSearchCriterion(
							"cpnyNameZh", get_cpnyName(),SearchMode.LIKE_ANYWHERE_MODE, true),
							SearchModelUtil
									.or(
											SearchModelUtil.getSearchCriterion(
													"cpnyNameEn",
													get_cpnyName(), SearchMode.LIKE_ANYWHERE_MODE,true),
											SearchModelUtil.getSearchCriterion(
													"cpnyNameKo",
													get_cpnyName(),SearchMode.LIKE_ANYWHERE_MODE, true))));
			getPageResultCriteria().add(
					SearchModelUtil.or(SearchModelUtil.getSearchCriterion(
							"cpnyNameZh", get_cpnyName(), SearchMode.LIKE_ANYWHERE_MODE,true),
							SearchModelUtil
									.or(
											SearchModelUtil.getSearchCriterion(
													"cpnyNameEn",
													get_cpnyName(),SearchMode.LIKE_ANYWHERE_MODE, true),
											SearchModelUtil.getSearchCriterion(
													"cpnyNameKo",
													get_cpnyName(),SearchMode.LIKE_ANYWHERE_MODE, true))));
			
		}
	}

	@Override
	protected SysCompany getFromBean() {
		return getSysCompany();
	}

	public SysCompany getSysCompany() {
		if (getProxyEntity() != null) {
			return getProxyEntity();
		}
		return sysCompany;
	}

	public void setSysCompany(SysCompany sysCompany) {
		this.sysCompany = sysCompany;
	}

	public SysCompanyService getSysCompanyService() {
		return sysCompanyService;
	}

	public void setSysCompanyService(SysCompanyService sysCompanyService) {
		this.sysCompanyService = sysCompanyService;
	}

	public String get_cpnyId() {
		return _cpnyId;
	}

	public void set_cpnyId(String id) {
		_cpnyId = id;
	}

	public List<SysCompany> getSysCompanyList() {
		if (getProxyEntityList() != null) {
			return getProxyEntityList();
		}
		return sysCompanyList;
	}

	public void setSysCompanyList(List<SysCompany> sysCompanyList) {
		this.sysCompanyList = sysCompanyList;
	}

	public String get_cpnyName() {
		return _cpnyName;
	}

	public void set_cpnyName(String name) {
		_cpnyName = name;
	}

}
