package com.hl95.login.action;

import java.util.Comparator;

import com.hl95.sys.entity.SysMenu;





public class MenuComparator implements Comparator<Object> {

	public int compare(Object arg0, Object arg1) {

		int a=((SysMenu)arg0).getDisplayOrder()==null?0:((SysMenu)arg0).getDisplayOrder().intValue();
		int b=((SysMenu)arg1).getDisplayOrder()==null?0:((SysMenu)arg1).getDisplayOrder().intValue();
		if(a>b){
			return 1;
		}else{
			return -1;
		}
	}
}
