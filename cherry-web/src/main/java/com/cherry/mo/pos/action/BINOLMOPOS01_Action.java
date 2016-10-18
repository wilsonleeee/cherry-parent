package com.cherry.mo.pos.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.pmc.action.BINOLMOPMC01_Action;
import com.cherry.mo.pos.bl.BINOLMOPOS01_BL;
import com.cherry.mo.pos.form.BINOLMOPOS01_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMOPOS01_Action extends BaseAction implements
ModelDriven<BINOLMOPOS01_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1211702703449716201L;

	/** 打印异常日志 */
	private static final Logger logger = LoggerFactory.getLogger(BINOLMOPMC01_Action.class);
	/** 参数FORM */
	private BINOLMOPOS01_Form form = new BINOLMOPOS01_Form();
	@Resource(name="binOLMOPOS01_BL")
	private BINOLMOPOS01_BL binOLMOPOS01_BL;
	@Resource
	private CodeTable code;
	private List<Map<String, Object>> storePayConfigList;
	private List<Map<String, Object>> codeListByCodeGrade;
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	public String init(){
		return SUCCESS;
	}
	public String search(){
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put("storePayCode", ConvertUtil.getString(form.getStorePayCode()));
		map.put("storePayValue", ConvertUtil.getString(form.getStorePayValue()));
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		int count = binOLMOPOS01_BL.getListCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count>0){
			storePayConfigList = binOLMOPOS01_BL.getStorePayConfigList(map);
		}
		return SUCCESS;
	}
	@SuppressWarnings("unchecked")
	public String addInit() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		codeListByCodeGrade = code.getCodesByGrade("1175");
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 云POS销售是否允去掉现金支付
		String isCA = binOLCM14_BL.getWebposConfigValue("9030", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		if(null == isCA || "".equals(isCA)){
			isCA = "N";
		}
		List<Map<String, Object>> storePayCodeList = binOLMOPOS01_BL.getStorePayCodeList(map);
		if(null!=codeListByCodeGrade && null!=storePayCodeList){
			for(Map<String, Object> cmap : storePayCodeList){
				for(int i=0;i<codeListByCodeGrade.size();i++){
					String codeKey = ConvertUtil.getString(codeListByCodeGrade.get(i).get("CodeKey"));
					if(ConvertUtil.getString(cmap.get("storePayCode")).equals(codeKey)){
						codeListByCodeGrade.remove(i);
					}
					if("CA".equals(codeKey) && "Y".equals(isCA)){
						continue;
					}else if("CA".equals(codeKey) && "N".equals(isCA)){
						codeListByCodeGrade.remove(i);
					}
				}
			}
		}
		if(codeListByCodeGrade.size()<1){
			ConvertUtil.setResponseByAjax(response, "NC");
			return null;
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void addStorePayConfig() throws Exception{
		String json = ConvertUtil.getString(form.getCodeKeyList());
		if(!"".equals(json)){
			List<Map<String, Object>> codeKeyList = ConvertUtil.json2List(json);
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			
			if(null!=codeKeyList && !codeKeyList.isEmpty()){
				List<Map<String, Object>> plist = new ArrayList<Map<String,Object>>();
				for(Map<String, Object> m:codeKeyList){
					Map<String, Object> pmap = code.getCode("1175", ConvertUtil.getString(m.get("codeKey")));
					pmap.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
					pmap.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
					pmap.put("createdBy", "BINOLMOPOS01");
					pmap.put("createPGM", "BINOLMOPOS01");
					if("PT".equals(ConvertUtil.getString(m.get("codeKey")))
							|| "WEPAY".equals(ConvertUtil.getString(m.get("codeKey")))
								|| "CZK".equals(ConvertUtil.getString(m.get("codeKey")))){
						pmap.put("payType", "1");
					}else {
						pmap.put("payType", "0");
					}
					plist.add(pmap);
				}
				try {
					binOLMOPOS01_BL.addStorePayConfig(plist);
					ConvertUtil.setResponseByAjax(response, "SUCCESS");
				} catch (Exception e) {
					logger.info(e.getMessage(),e);
				}
			}
		}
	}
	public void delStorePayConfig(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
		map.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		map.put("storePayCode", ConvertUtil.getString(form.getStorePayCode()));
		map.put("updatedBy", "BINOLMOPOS01");
		map.put("updatePGM", "BINOLMOPOS01");
		try {
			binOLMOPOS01_BL.delStorePayConfig(map);
			ConvertUtil.setResponseByAjax(response, "SUCCESS");
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
	}
	public void editStorePayConfig(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		/*map.put("storePayCode", ConvertUtil.getString(form.getStorePayCode()));
		map.put("isEnable", ConvertUtil.getString(form.getIsEnable()));
		map.put("payType", ConvertUtil.getString(form.getPayType()));
		map.put("defaultPay", ConvertUtil.getString(form.getDefaultPay()));*/
		
		String editJson = ConvertUtil.getString(form.getEditJson());
		if(!"".equals(editJson)){
			List<Map<String, Object>> editList = ConvertUtil.json2List(editJson);
			if(null!=editList && !editList.isEmpty()){
				for(Map<String, Object> m : editList){
					m.put("organizationInfoId", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()));
					m.put("brandInfoId", ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
					m.put("updatedBy", "BINOLMOPOS01");
					m.put("updatePGM", "BINOLMOPOS01");
				}
			}
			try {
				binOLMOPOS01_BL.editStorePayConfig(editList);
				ConvertUtil.setResponseByAjax(response, "SUCCESS");
			} catch (Exception e) {
				logger.info(e.getMessage(),e);
			}
		}
	}

	@Override
	public BINOLMOPOS01_Form getModel() {
		return form;
	}
	public List<Map<String, Object>> getStorePayConfigList() {
		return storePayConfigList;
	}
	public void setStorePayConfigList(List<Map<String, Object>> storePayConfigList) {
		this.storePayConfigList = storePayConfigList;
	}
	public List<Map<String, Object>> getCodeListByCodeGrade() {
		return codeListByCodeGrade;
	}
	public void setCodeListByCodeGrade(List<Map<String, Object>> codeListByCodeGrade) {
		this.codeListByCodeGrade = codeListByCodeGrade;
	}

}
