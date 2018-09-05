package com.hl95.test.action;

import java.util.ArrayList;
import java.util.List;

import org.pj.framework.business.core.action.BusinessAction;

import com.hl95.test.entity.User;
import com.hl95.test.service.UserService;

public class UserAction extends BusinessAction<User, UserService>{

	private static final long serialVersionUID = 1L;

	private UserService userService;
	private List<User> users = new ArrayList<User>();
	
	@Override
	public String execute() throws Exception {
		users = userService.getAllUsers();
		return SUCCESS;
	}
	@Override
	protected User getFromBean() {
		return null;
	}

	@Override
	public void searchModelCallBack() {
		
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

}
