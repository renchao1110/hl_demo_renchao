package com.hl95.demo.service;

import org.pj.criterion.core.service.CriterionService;

import com.hl95.demo.dao.DemoDao;
import com.hl95.demo.entity.DemoEntity;

public class DemoService extends CriterionService<DemoEntity> {

	private DemoDao demoDao;

	public DemoDao getDemoDao() {
		return demoDao;
	}

	public void setDemoDao(DemoDao demoDao) {
		this.demoDao = demoDao;
		this.setCriterionDao(this.demoDao);
	}

}
