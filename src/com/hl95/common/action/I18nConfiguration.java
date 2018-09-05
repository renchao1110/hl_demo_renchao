/**
 * 
 */
package com.hl95.common.action;


import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 */
public class I18nConfiguration{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static I18nConfiguration i18nConfig=null;
	private static Locale defaultLocale = new Locale("zh","Zh");
	private static Map<String, String> i18nLanguage=null;
	private static Map<String, String> i18nLanguageDefault=null;
	
	private ServletContext servletContext=null;
	@SuppressWarnings("unused")
	private static final Logger log= Logger.getLogger(I18nConfiguration.class);
	
	/**
	 * 
	 */
	private I18nConfiguration(Locale locale,ServletContext servletContext) {
	    if (locale == null) {
		      locale = defaultLocale;
		}
		this.servletContext=servletContext;
		I18nConfigurationUtil i18nUtil=(I18nConfigurationUtil) getServletContext().getAttribute(I18nConfigurationUtil.ROOT_WEB_APPLICATION_LANGUGAE);
		i18nLanguage=i18nUtil.getI18nConfigByLocale(locale);
		i18nLanguageDefault=i18nUtil.getI18nConfigByLocale(defaultLocale);
	}
	
	public static I18nConfiguration getInstance(Locale locale,ServletContext servletContext) {
		if (i18nConfig != null)
			return i18nConfig;
		else
			return new I18nConfiguration(locale,servletContext);
	}

	
	private  Object getMessage(String key) {
	    Object msg = null;
	        try{
	        	msg=i18nLanguage.get(key);
	        	if(msg==null){
	        		msg=i18nLanguageDefault.get(key);
	        	}else if(StringUtils.isBlank(msg.toString())){
	        		msg=i18nLanguageDefault.get(key);
	        	}
	        	if(msg==null){
	        		msg=key;
	        	}
	        }catch(Exception ex){
	        	msg=key;
	        }
	    return msg;
	  }
	
	public String getString(String key) {
		Object o = getMessage(key);
		if (o == null)
			throw new I18nConfigurationException("properties file key [[" + key
					+ "]] not exist");
		return o.toString();
	}

	public int getInt(String key) {
		Object o = getMessage(key);
		if (o == null)
			throw new I18nConfigurationException("properties file key [[" + key
					+ "]] not exist");
		int i = Integer.parseInt(o.toString());
		return i;
	}

	public long getLong( String key) {
		Object o = getMessage(key);
		if (o == null)
			throw new I18nConfigurationException("properties file key [[" + key
					+ "]] not exist");

		long l = Long.parseLong(o.toString());
		return l;
	}

	public float getFloat( String key) {
		Object o = getMessage(key);
		if (o == null)
			throw new I18nConfigurationException("properties file key [[" + key
					+ "]] not exist");
		float f = Float.parseFloat(o.toString());
		return f;
	}

	public double getDouble(String key) {
		Object o = getMessage(key);
		if (o == null)
			throw new I18nConfigurationException("properties file key [[" + key
					+ "]] not exist");
		double d = Double.parseDouble(o.toString());
		return d;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

}
