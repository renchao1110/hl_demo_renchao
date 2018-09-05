package com.hl95.utils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.pj.criterion.core.support.encryptPassword.EncryptPassword;
import org.pj.criterion.core.support.properties.PropertiesManager;
import org.pj.criterion.core.util.MsgUtil;
import org.pj.criterion.core.util.StringUtils;



public class Constants {
	
	
    public static final Long IS_SEARCH_ID = 39l;
    public static final Long IS_SDEPT_ID = 43l;    
    public static final Long IS_SE_ID = 44l;
    public static final int IS_CONTACT_ID = 49;
    private static final Logger log = Logger.getLogger(MsgUtil.class);
	

	public static boolean isMobileNO(String mobiles){
		//Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		Pattern p = Pattern.compile("^(1[3-9])\\d{9}$");  
		Matcher m = p.matcher(mobiles);  
		return m.matches();  
	}
	/* 验证邮箱格式 */
	public static boolean isEmailNO(String email){
		//Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
		Matcher m = p.matcher(email);
		return m.matches();  
	}
	/* 判断是否包含特殊符号   */
	public static boolean ifExceptionMark(String mark){
		Pattern p = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");  
		Matcher m ;
		String ss ;
		for(int i=0;i<mark.length();i++){
			ss = mark.substring(i, i+1);
			m = p.matcher(ss);
			if(m.matches() == true){
				return true;
			}
		}
		return false;
	}
	public static boolean isSmsSig(String val){
		String str = val.trim();
		if(StringUtils.isBlank(str)){ 
			return false; 
		}else if(str.length() > 6){
			return false;
		}
		boolean oflag = false;
		boolean numf = false;
		boolean abcf = false;
		boolean cnf = false;
		for(int i=0;i<str.length();i++){
			String s = str.substring(i,i+1);
			Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\0-9a-zA-Z]{2,6}$");  
			if(!(p.matcher(s).matches())){
				oflag = true;;
				break;
			}
			
			p = Pattern.compile("^[0-9]$");  
			if(p.matcher(s).matches()){
				numf = true;
			}
			p = Pattern.compile("^[a-zA-Z]$");  
			if(p.matcher(s).matches()){
				abcf = true;
			}
			p = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]$");  
			if(p.matcher(s).matches()){
				cnf = true;
			}
		}
		/*System.out.println("中文："+cnf);
		System.out.println("abc："+abcf);
		System.out.println("num："+numf);
		System.out.println("杂项："+oflag);*/
		if(!oflag){
			if(cnf){
				return true;
			}else if(cnf&&abcf){
				return true;
			}else if(cnf&&numf){
				return true;
			}
		}
		return false;
	}
	
	
	public static String getTokenId(String empId){
		return EncryptPassword.encodeByMD5(System.currentTimeMillis()+empId);
	}
	
	public static void msgLogOut(String msg,Class<T> clazz){
		Logger log = Logger.getLogger(clazz);
		log.info(msg);
	}
	
	
	public static String getExtNum(int max){
		Random random = new Random();
		String extNum = "";
        for(int i = 0; i < max;i++) {
        	extNum += Math.abs(random.nextInt())%10;
            
        }
        return extNum;
	}
	
	public static String getOutTradeNo(String preName){
		return preName+System.currentTimeMillis()+getRandomNumr(7);
	}
	
	
	public static String getRandomCharAndNumr(Integer length) {  
	    String str = "";  
	    Random random = new Random();  
	    for (int i = 0; i < length; i++) {  
	        boolean b = random.nextBoolean();  
	        if (b) { // 字符串  
	            str += (char) (97 + random.nextInt(26));// 取得大写字母  取得65大写字母还是97小写字母 
	        } else { // 数字  
	            str += String.valueOf(random.nextInt(10));  
	        }  
	    }  
	    return str;  
	}  
	
	public static String getRandomNumr(Integer length) {  
	    String str = "";  
	    Random random = new Random();  
	    for (int i = 0; i < length; i++) {  
	    	 str += String.valueOf(random.nextInt(10));
	    }  
	    return str;  
	}
	
	public static int nextInt(final int min, final int max){
		Random rand= new Random();
		int tmp = Math.abs(rand.nextInt());
		return tmp % (max - min + 1) + min;
	}
	
	
	public static String msgDes(int msgNo) {
		try{
			String _msg = "";
			switch (msgNo) {
			case 0:
				_msg = "操作成功";
				break;
			case 1:
				_msg = "参数错误。";
				break;
			case 2:
				_msg = "系统处理错误，请联系管理员。";
				break;
			case 3:
				_msg = "用户不存在";
				break;
			case 4:
				_msg = "权限不足，请不要瞎操作。";
				break;
			case 5:
				_msg = "操作失败，请联系管理员。";
				break;
			case 100:
				_msg = "联系管理员添加扩展码";
				break;
			case 101:
				_msg = "短信提交成功";
				break;
			case 102:
				_msg = "请填写密码";
				break;
			case 103:
				_msg = "上传的文件类型必需为txt或xls格式";
				break;
			case 104:
				_msg = "文件大小不能超过5M";
				break;
			case 105:
				_msg = "请上传有内容的文件";
				break;
			default:
				_msg = "未知错误";
				break;
			}
			return URLEncoder.encode(_msg,"utf-8");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
    
    
}
