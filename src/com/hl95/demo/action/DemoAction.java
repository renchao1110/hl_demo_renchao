package com.hl95.demo.action;

import org.pj.framework.business.core.action.BusinessAction;

import com.hl95.demo.entity.DemoEntity;
import com.hl95.demo.service.DemoService;

public class DemoAction extends BusinessAction<DemoEntity, DemoService> {

	private static final long serialVersionUID = 1L;

	private DemoService demoService;

	public DemoService getDemoService() {
		return demoService;
	}

	public void setDemoService(DemoService demoService) {
		this.demoService = demoService;
	}

	@Override
	protected DemoEntity getFromBean() {
		return null;
	}

	@Override
	public void searchModelCallBack() {
	}

	@Override
	public String execute() throws Exception {
		return "success";
	}
	
}
