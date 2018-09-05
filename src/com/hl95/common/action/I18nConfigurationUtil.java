package com.hl95.common.action;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.hl95.sys.entity.SysLanguage;
import com.hl95.sys.service.SysLanguageService;


public class I18nConfigurationUtil{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static I18nConfigurationUtil i18nConfigurationUtil=null;
	private Map<String, Object> i18nConfigs=new HashMap<String, Object>();
	private static final Logger log=Logger.getLogger(I18nConfigurationUtil.class);
	public static final String ROOT_WEB_APPLICATION_LANGUGAE="ROOT_WEB_APPLICATION_LANGUAGE"+I18nConfigurationUtil.class.getName();

	private ServletContext  servletContext =null;
	
	private I18nConfigurationUtil(ServletContext  servletContext) {
		this.servletContext=servletContext;
	}
	
	public static I18nConfigurationUtil getInstance(ServletContext servletContext) {
		if (i18nConfigurationUtil != null)
			return i18nConfigurationUtil;
		else
			return new I18nConfigurationUtil(servletContext);
	}
	
	public void initialization(List<SysLanguage> languageList){
		Map<String,String> languageMapZh=new HashMap<String, String>();
		Map<String,String> languageMapEn=new HashMap<String, String>();
		Map<String,String> languageMapKo=new HashMap<String, String>();
		for(SysLanguage language:languageList){
			languageMapZh.put(language.getLanguageId(), language.getLanguageZh());
			languageMapEn.put(language.getLanguageId(), language.getLanguageEn());
			languageMapKo.put(language.getLanguageId(), language.getLanguageKo());
		}
		
		i18nConfigs.put("languageZH", languageMapZh);
		System.out.println("系统:加载系统中文语言完成!");
		i18nConfigs.put("languageEN", languageMapEn);
		System.out.println("系统:加载系统英文语言完成!");
		i18nConfigs.put("languageKO", languageMapKo);
		System.out.println("系统:加载系统韩文语言完成!");
		getServletContext().setAttribute(ROOT_WEB_APPLICATION_LANGUGAE, this);


	}
	
	public void loadLangugae(){

		ApplicationContext web=(ApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		SysLanguageService languageService=(SysLanguageService) web.getBean("sysLanguageService");
		List<SysLanguage> languageList=languageService.getSysLanguageByUse();
		if(languageList==null || languageList.size()==0){
			log.warn("警告:从数据库加载系统语言失败.系统有可能出现语言显示问题!");
		}else{
			System.out.println("系统:正在加载系统语言..请等待..");
			initialization(languageList);
			System.out.println("系统:加载系统语言完成!");
			System.out.println("系统:总共加载条"+languageList.size()+"多国语言信息");
		}
	}
	@SuppressWarnings("unchecked")
	public  Map<String, String> getI18nConfigByLocale(Locale locale){
		String language=locale.getLanguage();
		if(language==null)
			language="zh";
		if(language.intern()=="zh".intern()){
			return (Map<String, String>) i18nConfigs.get("languageZH");
		}else if(language.intern()=="en".intern()){
			return (Map<String, String>) i18nConfigs.get("languageEN");
		}else if(language.intern()=="ko".intern()){
			return (Map<String, String>) i18nConfigs.get("languageKO");
		}
		throw new I18nConfigurationException("加载系统语言出错,本地Locale未被识别");
	}
	
	public  Map<String, Object> getI18nConfigs() {
		return i18nConfigs;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}


}
