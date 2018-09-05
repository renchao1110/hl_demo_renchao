package com.hl95.test.dao;

import java.util.List;

import org.pj.criterion.core.dao.hibernate.CriterionHibernateDao;

import com.hl95.test.entity.User;

public class UserDao extends CriterionHibernateDao<User>{
	
	public List<User> getAllUsers(){
		return this.getEntityAll();
	}
}
