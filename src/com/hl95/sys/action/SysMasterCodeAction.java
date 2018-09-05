package com.hl95.sys.action;

import java.io.File;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.pj.criterion.core.support.excel.ReadExcelManager;
import org.pj.framework.business.core.action.BusinessAction;
import org.pj.framework.business.web.session.UserInfo;
import org.pj.framework.business.web.session.UserSession;


import com.hl95.sys.entity.SysLanguage;
import com.hl95.sys.entity.SysMasterCode;
import com.hl95.sys.service.SysMasterCodeService;
import com.opensymphony.xwork.Action;

public class SysMasterCodeAction extends BusinessAction<SysMasterCode, SysMasterCodeService>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysMasterCodeService sysMasterCodeService;
	
	private SysMasterCode sysMasterCode;

	private List<SysMasterCode> sysMasterCodeList;
	
	private File excelFile;
	
	public String display() throws Exception{
		this.view();
		return "display";
	}
	public String onlyCodeId() throws Exception{
		if(getSysMasterCode()!=null){		
			SysMasterCode onlySysMasterCode=this.sysMasterCodeService.getSysMasterCodeByCodeId(getSysMasterCode().getCodeId());
			boolean onlyFlag = false;
			if(onlySysMasterCode==null){
				onlyFlag=true;
			}
			if (onlyFlag) {
				writeTextByAction("true");
			} else {
				writeTextByAction("false");
			}
		}
		return "";
	}
	
	public String onlyCodeNameZh() throws Exception{
		if(getSysMasterCode()!=null){
			String isEdit = getParameter("isEdit");
			SysMasterCode onlySysMasterCode=this.sysMasterCodeService.getSysMasterCodeByCodeNameZh(getSysMasterCode().getCodeNameZh());
			boolean onlyFlag = false;
			if(onlySysMasterCode==null){
				onlyFlag=true;
			}else{
				if(isEdit.equals("1")){
					if(onlySysMasterCode.getId().equals(getSysMasterCode().getId())){
						onlyFlag=true;
					}
				}
			}
			if (onlyFlag) {
				writeTextByAction("true");
			} else {
				writeTextByAction("false");
			}
		}
		return "";
	}

	public String loadMasterCodeTreeManager() throws Exception{
		List<SysMasterCode> masterCodeTreeList=null;
		if(StringUtils.isBlank(getParameter("parentId"))){
			return Action.INPUT;
		}else{
			if(getParameter("parentId").equals("masterRoot")){
				masterCodeTreeList=this.sysMasterCodeService.getRootMasterCodesManager();
			}else{
				Long parentId=(Long) ConvertUtils.convert(getParameter("parentId"), Long.class);
				masterCodeTreeList=this.sysMasterCodeService.getChildMasterCodesManager(parentId);
			}
		}
		JSONArray arrayItems=new JSONArray();
		boolean hasChindls=false;
		if(masterCodeTreeList!=null){
			int index=0;
			for(SysMasterCode sysMasterCode:masterCodeTreeList){	
				JSONObject Item=new JSONObject();
				Item.put("text", geti18nByBean(sysMasterCode, "codeName"));
				Item.put("id", sysMasterCode.getId());
				Item.put("cls", "file");
				hasChindls=this.sysMasterCodeService.hasChilds(sysMasterCode.getId());
				Item.put("leaf", hasChindls);
				arrayItems.add(Item);
				index++;
			}
			
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	public String importExcel(){
		return "importExcel";
	}
	
	public String importDate(){
		ReadExcelManager readExcel = new ReadExcelManager();
		readExcel.initializationRead(excelFile);
		readExcel.setSheetIndex(0);
		int rows = readExcel.getSheetRows();
		SysMasterCode codeObj = null;
		String codeZh = "";
		String codeKo = "";
		String codeId = "";
		for (int index = 1; index < rows; index++) {
			
			readExcel.setCellCol(0);
			readExcel.setCellRow(index);
			codeId = readExcel.getValue(readExcel.getCell()).toString().trim();
			codeObj = this.sysMasterCodeService.getSysMasterCodeByCodeId(codeId);
			if(codeObj==null){
				System.out.println(index+"行没有个code");
				continue;
			}
			bindEntity(codeObj);
			readExcel.setCellCol(1);
			readExcel.setCellRow(index);
			codeZh = readExcel.getValue(readExcel.getCell()).toString().trim();
			readExcel.setCellCol(2);
			readExcel.setCellRow(index);
			codeKo = readExcel.getValue(readExcel.getCell()).toString().trim();
			codeObj.setCodeNameZh(codeZh);
			codeObj.setCodeNameKo(codeKo);
			this.sysMasterCodeService.saveOrUpdate(codeObj);
		}
		return "importComplet";
	}
	
	public String loadMasterCodeTree() throws Exception{
		List<SysMasterCode> masterCodeTreeList=null;
		if(StringUtils.isBlank(getParameter("parentId"))){
			return Action.INPUT;
		}else{
			if(getParameter("parentId").equals("masterRoot")){
				masterCodeTreeList=this.sysMasterCodeService.getRootMasterCodes();
			}else{
				Long parentId=(Long) ConvertUtils.convert(getParameter("parentId"), Long.class);
				masterCodeTreeList=this.sysMasterCodeService.getChildMasterCodes(parentId);
			}
		}
		String selfMasterId=getParameter("_selfMasterId");
		Long selfId=null;
		if(StringUtils.isNotBlank(selfMasterId)){
			selfId=(Long) ConvertUtils.convert(selfMasterId,Long.class);
		}
		JSONArray arrayItems=new JSONArray();
		boolean hasChindls=false;
		if(masterCodeTreeList!=null){
			int index=0;
			for(SysMasterCode sysMasterCode:masterCodeTreeList){
				if(selfId!=null){
					if(sysMasterCode.getId().equals(selfId)){
						continue;
					}
				}
				JSONObject Item=new JSONObject();
				Item.put("text", geti18nByBean(sysMasterCode, "codeName"));
				Item.put("id", sysMasterCode.getId());
				Item.put("cls", "file");
				hasChindls=this.sysMasterCodeService.hasChildsByUse(sysMasterCode.getId());
				Item.put("leaf", hasChindls);
				arrayItems.add(Item);
				index++;
			}
			
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	public String loadMasterCodeTreeByCorner() throws Exception{
		List<SysMasterCode> masterCodeTreeList=null;
		if(StringUtils.isBlank(getParameter("parentId"))){
			return Action.INPUT;
		}else{
			if(getParameter("parentId").equals("masterRoot")){
				masterCodeTreeList=this.sysMasterCodeService.getSysMasterListByCodeIdUse("CORNER_CLASS");
			}else{
				Long parentId=(Long) ConvertUtils.convert(getParameter("parentId"), Long.class);
				masterCodeTreeList=this.sysMasterCodeService.getChildMasterCodes(parentId);
			}
		}
		String selfMasterId=getParameter("_selfMasterId");
		Long selfId=null;
		if(StringUtils.isNotBlank(selfMasterId)){
			selfId=(Long) ConvertUtils.convert(selfMasterId,Long.class);
		}
		JSONArray arrayItems=new JSONArray();
		boolean hasChindls=false;
		if(masterCodeTreeList!=null){
			int index=0;
			for(SysMasterCode sysMasterCode:masterCodeTreeList){
				if(selfId!=null){
					if(sysMasterCode.getId().equals(selfId)){
						continue;
					}
				}
				JSONObject Item=new JSONObject();
				Item.put("text", geti18nByBean(sysMasterCode, "codeName"));
				Item.put("id", sysMasterCode.getId());
				Item.put("cls", "file");
				hasChindls=this.sysMasterCodeService.hasChildsByUse(sysMasterCode.getId());
				Item.put("leaf", hasChindls);
				arrayItems.add(Item);
				index++;
			}
			
		}
		writeJsonByAction(arrayItems.toString());
		return "";
	}
	
	/**
	 * EDIT回调函数 判断父级ID是否正确/存在 自动生成MenuLevel
	 */
	@Override
	public void extendsEdit(SysMasterCode object) throws Exception{
		if(object!=null){
			if(object.getParentId()!=null){
				if(object.getParentId().getId()!=null){
					SysMasterCode parentId=this.sysMasterCodeService.getEntityById(object.getParentId().getId());
					if(parentId!=null){
						object.setCodeLevel(Integer.valueOf(parentId.getCodeLevel().intValue()+1));
					}
				}else{
					object.setParentId(null);
					object.setCodeLevel(Integer.valueOf(0));
				}	
			}else{
				object.setParentId(null);
				object.setCodeLevel(Integer.valueOf(0));
			}
		}
	}
	@Override
	protected SysMasterCode getFromBean() {
		return getSysMasterCode();
	}

	@Override
	public void searchModelCallBack() {
		// TODO Auto-generated method stub
		
	}


	public SysMasterCode getSysMasterCode() {
		if(getProxyEntity()!=null){
			return getProxyEntity();
		}
		return sysMasterCode;
	}
	public void setSysMasterCode(SysMasterCode sysMasterCode) {
		this.sysMasterCode = sysMasterCode;
	}
	public List<SysMasterCode> getSysMasterCodeList() {
		if(getProxyEntityList()!=null){
			return getProxyEntityList();
		}
		return sysMasterCodeList;
	}
	public void setSysMasterCodeList(List<SysMasterCode> sysMasterCodeList) {
		this.sysMasterCodeList = sysMasterCodeList;
	}
	public SysMasterCodeService getSysMasterCodeService() {
		return sysMasterCodeService;
	}
	public void setSysMasterCodeService(SysMasterCodeService sysMasterCodeService) {
		this.sysMasterCodeService = sysMasterCodeService;
	}
	public File getExcelFile() {
		return excelFile;
	}
	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
