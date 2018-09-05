package com.hl95.test.service;

import java.util.List;

import org.pj.criterion.core.service.CriterionService;

import com.hl95.test.dao.UserDao;
import com.hl95.test.entity.User;

public class UserService extends CriterionService<User>{
	
	private UserDao userDao;

	
	public List<User> getAllUsers(){
		return userDao.getAllUsers();
	}
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
		this.setCriterionDao(this.userDao);
	}
	
}
