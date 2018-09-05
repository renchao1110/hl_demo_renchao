package com.hl95.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.pj.criterion.core.dao.hibernate.support.ProjectionBuild;
import org.pj.criterion.core.dao.hibernate.support.RestrictionBuild;
import org.pj.criterion.core.service.CriterionService;

import com.hl95.sys.dao.SysMasterCodeDao;
import com.hl95.sys.entity.SysMasterCode;


public class SysMasterCodeService extends CriterionService<SysMasterCode>{
	private SysMasterCodeDao sysMasterCodeDao;

	public SysMasterCodeDao getSysMasterCodeDao() {
		return sysMasterCodeDao;
	}

	public void setSysMasterCodeDao(SysMasterCodeDao sysMasterCodeDao) {
		this.sysMasterCodeDao = sysMasterCodeDao;
		this.setCriterionDao(this.sysMasterCodeDao);
	}
	
	public SysMasterCode getSysMasterCodeByCodeId(String codeId){
		return (SysMasterCode) this.sysMasterCodeDao.findObjectByCriteria(RestrictionBuild.eq("codeId", codeId));
	}
	
	public SysMasterCode getSysMasterCodeByCodeNameZh(String codeNameZh){
		return (SysMasterCode) this.sysMasterCodeDao.findObjectByCriteria(RestrictionBuild.eq("codeNameZh", codeNameZh));
	}
	
	public List<SysMasterCode> getRootMasterCodesManager(){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.add(RestrictionBuild.eq("codeLevel", Integer.valueOf(0)));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMasterCodeDao.findByCriteria(dc);
	}
	
	public List<SysMasterCode> getRootMasterCodes(){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.add(RestrictionBuild.eq("codeLevel", Integer.valueOf(0)));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMasterCodeDao.findByCriteria(dc);
	}
	
	/**
	 * 传入父级ID 参看是否有无Childs
	 * @param parentId
	 * @return
	 */
	public boolean hasChilds(Long parentId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.setProjection(ProjectionBuild.rowCount());
		Integer rowCount=(Integer) this.sysMasterCodeDao.findObjectByCriteria(dc);
		if(rowCount.intValue()>0){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean hasChildsByUse(Long parentId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.setProjection(ProjectionBuild.rowCount());
		Integer rowCount=(Integer) this.sysMasterCodeDao.findObjectByCriteria(dc);
		if(rowCount.intValue()>0){
			return false;
		}else{
			return true;
		}
	}
	public List<SysMasterCode> getChildMasterCodesManager(Long parentId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMasterCodeDao.findByCriteria(dc);
	}
	
	public List<SysMasterCode> getChildMasterCodes(Long parentId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("parentId.id", parentId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMasterCodeDao.findByCriteria(dc);
	}	

	public List<SysMasterCode> getChildMasterCodebyPids(String parentCodeId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.createAlias("parentId", "parentId");
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("parentId.codeId", parentCodeId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMasterCodeDao.findByCriteria(dc);
	}
	/**
	 * 扩展（加入查询出下一级的数量）
	 * @return
	 */
	public List<SysMasterCode> getChildMasterCodebyPidsEx(Long parentId){
		List<SysMasterCode> list = this.getChildMasterCodes(parentId);
		if(list != null && list.size()>0){
			for(SysMasterCode code:list){
				List<SysMasterCode> clist = this.getChildMasterCodes(code.getId());
				if(clist != null){
					code.setChildSize(clist.size());
				}else{
					code.setChildSize(0);
				}
			}
		}
		return list;
	}
	
	public SysMasterCode getSysMasterCodeByCodeIdUse(String codeId){
		return (SysMasterCode) this.sysMasterCodeDao.findObjectByCriteria(RestrictionBuild.eq("codeId", codeId),RestrictionBuild.eq("useYn", "Y"));
	}
	
	public List<SysMasterCode> getSysMasterListByCodeIdUse(String codeId){
		SysMasterCode masterCode=this.getSysMasterCodeByCodeIdUse(codeId);
		List<SysMasterCode> masterCodes=null;
		if(masterCode!=null){
			DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
			dc.add(RestrictionBuild.eq("parentId.id", masterCode.getId()));
			dc.add(RestrictionBuild.eq("useYn", "Y"));
			dc.addOrder(Order.asc("displayOrder"));
			masterCodes=this.sysMasterCodeDao.findByCriteria(dc);
		}
		return masterCodes;
	}
	public List<SysMasterCode> getSysMasterListByCodeIdNotInUse(String codeId,Long[] ids){
		SysMasterCode masterCode=this.getSysMasterCodeByCodeIdUse(codeId);
		List<SysMasterCode> masterCodes=null;
		if(masterCode!=null){
			DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
			dc.add(RestrictionBuild.eq("parentId.id", masterCode.getId()));
			dc.add(RestrictionBuild.eq("useYn", "Y"));
			dc.add(RestrictionBuild.not(RestrictionBuild.in("id", ids)));
			dc.addOrder(Order.asc("displayOrder"));
			masterCodes=this.sysMasterCodeDao.findByCriteria(dc);
		}
		return masterCodes;
	}
	
	public SysMasterCode getSysMasterCodeByCodeValue(String codeValue){
		List<SysMasterCode> masterCodes=null;
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.add(RestrictionBuild.eq("codeValue", codeValue));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		masterCodes=this.sysMasterCodeDao.findByCriteria(dc);
		if(masterCodes!=null && masterCodes.size()>0){
			return masterCodes.get(0);
		}else{
			return null;
		}
	}
	
	public List<SysMasterCode> getSysMasterListByCodeValue(String deptId){
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("codeValue", deptId));
		dc.addOrder(Order.asc("displayOrder"));
		return this.sysMasterCodeDao.findByCriteria(dc);
	}

	public SysMasterCode getCodeByCodeValueAndParentCodeId(String jigou, String parentCodeId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc=DetachedCriteria.forClass(SysMasterCode.class);
		dc.createAlias("parentId", "parentId");
		dc.add(RestrictionBuild.eq("parentId.codeId", parentCodeId));
		dc.add(RestrictionBuild.eq("useYn", "Y"));
		dc.add(RestrictionBuild.eq("codeValue", jigou));
		List<SysMasterCode> codeList = this.sysMasterCodeDao.findByCriteria(dc);
		if(codeList!=null && codeList.size()>0){
			return codeList.get(0);
		}
		return null;
	}
	/**
	 * 返回充值卡
	 * @param pcodeId
	 * @return
	 */
	public Map<String, Object> getCardType(String pcodeId){
		List<SysMasterCode> list = getChildMasterCodebyPids(pcodeId);
		Map<String, Object> map = null;
		if(list != null && list.size() == 3){
			map = new HashMap<String, Object>();
			for(SysMasterCode sm:list){
				if((pcodeId+"_1").equals(sm.getCodeId())){
					map.put("type", sm.getCodeValue());
				}else if((pcodeId+"_2").equals(sm.getCodeId())){
					map.put("noLen", Integer.parseInt(sm.getCodeValue()));
				}else if((pcodeId+"_3").equals(sm.getCodeId())){
					map.put("pwdLen", Integer.parseInt(sm.getCodeValue()));
				}
			}
		}
		return map;
	}
	
}
