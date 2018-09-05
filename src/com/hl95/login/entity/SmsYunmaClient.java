package com.hl95.login.entity;

public class SmsYunmaClient {
 
	private String appkey;
	private String secretkey;
	private String smsSendUrl;
	private String smsQueryUrl;
	private String smsSign;
	private String smsRegTcode;
	private String smsPwdTcode;
	private String smsRegParam;
	private String smsPwdParam;
	private String mstr;
	
	public String getMstr() {
		return mstr;
	}
	public void setMstr(String mstr) {
		this.mstr = mstr;
	}
	public String getSmsQueryUrl() {
		return smsQueryUrl;
	}
	public void setSmsQueryUrl(String smsQueryUrl) {
		this.smsQueryUrl = smsQueryUrl;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getSecretkey() {
		return secretkey;
	}
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
	public String getSmsSendUrl() {
		return smsSendUrl;
	}
	public void setSmsSendUrl(String smsSendUrl) {
		this.smsSendUrl = smsSendUrl;
	}
	public String getSmsSign() {
		return smsSign;
	}
	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}
	public String getSmsRegTcode() {
		return smsRegTcode;
	}
	public void setSmsRegTcode(String smsRegTcode) {
		this.smsRegTcode = smsRegTcode;
	}
	public String getSmsPwdTcode() {
		return smsPwdTcode;
	}
	public void setSmsPwdTcode(String smsPwdTcode) {
		this.smsPwdTcode = smsPwdTcode;
	}
	public String getSmsRegParam() {
		return smsRegParam;
	}
	public void setSmsRegParam(String smsRegParam) {
		this.smsRegParam = smsRegParam;
	}
	public String getSmsPwdParam() {
		return smsPwdParam;
	}
	public void setSmsPwdParam(String smsPwdParam) {
		this.smsPwdParam = smsPwdParam;
	}
	
	
	
}
