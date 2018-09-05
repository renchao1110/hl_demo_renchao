package com.hl95.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RandomID {

	public static String getTemplateId() {
		String str = "1234567890";
		char[] arr = str.toCharArray();
		Random rand = new Random();
		StringBuilder sb = new StringBuilder("sms_");
		for (int i = 0; i < 6; i++) {
			char a = arr[rand.nextInt(arr.length)];
			sb.append(String.valueOf(a));
		}
		return sb.toString();
	}

	public static String getTemplateId2() {
		String str = "1234567890";
		char[] arr = str.toCharArray();
		Random rand = new Random();
		StringBuilder sb = new StringBuilder("tts_");
		for (int i = 0; i < 6; i++) {
			char a = arr[rand.nextInt(arr.length)];
			sb.append(String.valueOf(a));
		}
		return sb.toString();
	}

	public static String checkTemplateId(List<String> list) {
		String templateId = getTemplateId();
		int index = list.size();
		if (index == 0) {
			return templateId;
		}
		do {
			if (templateId.equals(list.get(index - 1))) {
				templateId = RandomID.getTemplateId();
				index = list.size();
			} else {
				index--;
			}
		} while (index > 0);

		return templateId;
	}

	public static String checkTemplateId2(List<String> list) {
		String templateId = getTemplateId2();
		int index = list.size();
		if (index == 0) {
			return templateId;
		}
		do {
			if (templateId.equals(list.get(index - 1))) {
				templateId = RandomID.getTemplateId2();
				index = list.size();
			} else {
				index--;
			}
		} while (index > 0);

		return templateId;
	}

	/**
	 * 随机生成用户名
	 * 
	 * @return
	 */
	public static String createUser() {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghigklmnopqrstuvwxyz";
		char[] arr = str.toCharArray();
		StringBuffer sbf = new StringBuffer();
		Random rand = new Random();
		for (int i = 0; i < 8; i++) {
			char a = arr[rand.nextInt(arr.length)];
			sbf.append(a);
		}
		return sbf.toString();
	}

	public static String checkAppKey(List<String> list, String appKey) {
		int index = list.size();
		if (index == 0) {
			return appKey;
		}
		do {
			if (appKey.equals(list.get(index - 1))) {
				appKey = RandomID.createUser();
				index = list.size();
			} else {
				index--;
			}
		} while (index > 0);

		return appKey;
	}

	public static String createExtCode() {
		String str = "0123456789";
		char[] arr = str.toCharArray();
		StringBuffer sbf = new StringBuffer();
		Random rand = new Random();
		for (int i = 0; i < 6; i++) {
			char a = arr[rand.nextInt(arr.length)];
			sbf.append(a);
		}
		return sbf.toString();
	}

	public static String checkExtCode(List<String> list, String extCode) {
		int index = list.size();
		if (index == 0) {
			return extCode;
		}
		do {
			if (extCode.equals(list.get(index - 1))) {
				extCode = RandomID.createExtCode();
				index = list.size();
			} else {
				index--;
			}
		} while (index > 0);

		return extCode;
	}

	/* 随机生成六位empid */
	public static String createEmpId() {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		char[] arr = str.toCharArray();
		StringBuffer sbf = new StringBuffer();
		Random rand = new Random();
		for (int i = 0; i < 6; i++) {
			char a = arr[rand.nextInt(arr.length)];
			sbf.append(a);
		}
		return sbf.toString();
	}

	public static String checkEmpId(List<String> list, String extCode) {
		int index = list.size();
		if (index == 0) {
			return extCode;
		}
		do {
			if (extCode.equals(list.get(index - 1))) {
				extCode = RandomID.createEmpId();
				index = list.size();
			} else {
				index--;
			}
		} while (index > 0);

		return extCode;
	}

	public static String createSecretKey(String appKey) {
		StringBuilder sb = new StringBuilder(appKey);
		String str = String.valueOf(new Date().getTime());
		String str2 = sb.append(str).toString();
		return MD5Util.MD5(str2);
	}

	public static double sum(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.add(bd2).doubleValue();
	}

	public static String getDepartmentId() {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		char[] arr = str.toCharArray();
		StringBuffer sbf = new StringBuffer();
		Random rand = new Random();
		for (int i = 0; i < 6; i++) {
			char a = arr[rand.nextInt(arr.length)];
			sbf.append(a);
		}
		return sbf.toString();
	}

	public static String checkDepartmentId(List<String> list) {
		String departmentId = getDepartmentId();
		int index = list.size();
		if (index == 0) {
			return departmentId;
		}
		do {
			if (departmentId.equals(list.get(index - 1))) {
				departmentId = RandomID.getDepartmentId();
				index = list.size();
			} else {
				index--;
			}
		} while (index > 0);

		return departmentId;

	}

	public static void main(String[] args) {
		String str = getTemplateId();
		System.out.println(str);
		System.out.println(createSecretKey("11111111"));
		String str1 = createUser();
		System.out.println(str1);
		List<String> list = new ArrayList<String>();
		list.add("6BMO1CZL");
		list.add("6BMO1CZL");
		System.out.println(createExtCode());
		System.out.println(System.currentTimeMillis());// 与上面的相同
		System.out.println(sum(55.15, 15.55));
		String name = "名字的测试-123456";
		String templateName = name.substring(0, name.indexOf("-"));
		String code = name.substring(name.indexOf("-") + 1);
		System.out.println(templateName);
		System.out.println(code);
	}
	
	public static String getTeamCode(List<String> list) {
		String templateId = getTeamID();
		int index = list.size();
		if (index == 0) {
			return templateId;
		}
		do {
			if (templateId.equals(list.get(index - 1))) {
				templateId = RandomID.getTeamID();
				index = list.size();
			} else {
				index--;
			}
		} while (index > 0);

		return templateId;
	}
	public static String getTeamID() {
		String str = "1234567890";
		char[] arr = str.toCharArray();
		Random rand = new Random();
		StringBuilder sb = new StringBuilder("sms_");
		for (int i = 0; i < 10; i++) {
			char a = arr[rand.nextInt(arr.length)];
			sb.append(String.valueOf(a));
		}
		return sb.toString();
	}
	public static String createImgVerify() {
		String str = "0123456789";
		char[] arr = str.toCharArray();
		StringBuffer sbf = new StringBuffer();
		Random rand = new Random();
		for (int i = 0; i < 4; i++) {
			char a = arr[rand.nextInt(arr.length)];
			sbf.append(a);
		}
		return sbf.toString();
	}
	
}
