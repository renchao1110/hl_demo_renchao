package com.hl95.login.action;

import java.util.Comparator;

import com.hl95.login.entity.MyReqs;





public class ReqComparator implements Comparator<Object>{

	public int compare(Object arg0, Object arg1) {
		MyReqs a_object=(MyReqs)arg0;
		MyReqs b_object=(MyReqs)arg1;
		
		if(a_object.getSendDate()==null){
			return -1;
			
		}
		
		if(b_object.getSendDate()==null){
			return -1;
		}
		if(a_object.getSendDate().getTime()<b_object.getSendDate().getTime()){
		
			return 1;
		}else{
			return -1;
		}
	}

}
