package com.cherry.mb.svc.action;

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
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.svc.form.BINOLMBSVC03_Form;
import com.cherry.mb.svc.interfaces.BINOLMBSVC03_IF;
import com.cherry.mb.svc.service.BINOLMBSVC02_Service;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBSVC03_Action extends BaseAction implements
ModelDriven<BINOLMBSVC03_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3595817617832491688L;
	/** 参数FORM */
	private BINOLMBSVC03_Form form = new BINOLMBSVC03_Form();
	
	@Resource(name="binOLMBSVC03_IF")
	private BINOLMBSVC03_IF binOLMBSVC03_IF;
	
	@Resource
	private BINOLMBSVC02_Service binOLMBSVC02_Service;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	@Resource
	private CodeTable code;
	
	
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLMBSVC03_Action.class);
	/**
	 * 初始化充值输入卡号界面
	 */
	public String cardRechargeInit(){
		return SUCCESS;
	}
	
	/**
	 * 初始化充值详细页面
	 */
	public String cardRechargeDetailInit(){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			String giftamountType = binOLCM14_BL.getWebposConfigValue("9020", organizationInfoId, brandInfoId);
			if(null == giftamountType || "".equals(giftamountType)){
				giftamountType = "GD";
			}
			form.setGiftamountType(giftamountType);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put("brandCode", userInfo.getBrandCode());
			if(null!=form.getCardCode() && !"".equals(form.getCardCode())){
				map.put("cardCode", form.getCardCode());
				map.put("counterCode", form.getCounterCode());
				// 储值卡/服务卡 信息查询
				map.put("codeType", "1338");
				//后期调用储值卡查询接口
				Map<String,Object> cardInfo_map=binOLMBSVC03_IF.getCard(map);
				if("0".equals(ConvertUtil.getString(cardInfo_map.get("ERRORCODE")))){
					if(null == cardInfo_map.get("ResultContent")) {
						form.setCard(new HashMap<String, Object>());
					} else {
						form.setCard((Map<String,Object>)cardInfo_map.get("ResultContent"));
					}
				} else {
					logger.error(ConvertUtil.getString(getText("SVC00011")+cardInfo_map.get("ERRORMSG")));
					this.addActionError(getText("ECM00036"));
					return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
				}
				if(null!=form.getCard() && !form.getCard().isEmpty()){
					form.setServerList(code.getCodesByGrade("1338"));
					map.put("cardType", form.getCard().get("CardType"));
					// 储值卡/服务卡 活动查询后期调用查询活动接口
					Map<String,Object> discount_map=binOLMBSVC03_IF.getRechargeDiscountList(map);
					if("0".equals(ConvertUtil.getString(discount_map.get("ERRORCODE")))){
						form.setRechargeDiscountList((List)discount_map.get("ResultContent"));
					}else{
						logger.error(ConvertUtil.getString(getText("SVC00012")+discount_map.get("ERRORMSG")));
						this.addActionError(getText("ECM00036"));
						return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
					}
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			} else {
				// 系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			}
		}
	}

	/**
	 * 检验卡号信息
	 * @throws Exception 
	 */
	public void checkCard() throws Exception{
		try {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
			map.put("brandCode", userInfo.getBrandCode());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
			//后期调用储值卡信息查询接口（废弃），现在改用直接查询数据库中是否有相同卡号的操作
//			Map result_map=binOLMBSVC03_IF.getCard(map);
//			if("0".equals(ConvertUtil.getString(result_map.get("ERRORCODE")))){
//				if(null == result_map.get("ResultContent")){
//					ConvertUtil.setResponseByAjax(response, 1);
//					return;
//				}
//				ConvertUtil.setResponseByAjax(response, 0);
//			}else{
//				ConvertUtil.setResponseByAjax(response, 1);
//				logger.error(ConvertUtil.getString(getText("SVC00011")+result_map.get("ERRORMSG")));
//			}
			int result=binOLMBSVC02_Service.checkCard(map);
			if(result > 0){
				ConvertUtil.setResponseByAjax(response, "0");
			}else{
				ConvertUtil.setResponseByAjax(response, "1");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "ERROR");
			 }
		}
	}
	/**
	 * 充值
	 * @return
	 * @throws Exception 
	 */
	public void savingsCardRecharge() throws Exception{
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 语言
			UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
    		map.put("employeeCode", userInfo.getEmployeeCode());
    		map.put("brandCode", userInfo.getBrandCode());
    		Map<String,Object> result_map=new HashMap<String, Object>();
    		if("2".equals(form.getCardType()) || "3".equals(form.getCardType())){
    			List<Map<String,Object>> service_list=ConvertUtil.json2List(form.getServiceArr());
    			if(service_list == null){
    				result_map.put("resultCode", 0);
    				result_map.put("resultMessage", getText("SVC00006"));
    				ConvertUtil.setResponseByAjax(response, result_map);
    				return;
    			}
    			map.put("service_list", service_list);
    		}
			//后台验证，检验参数的有效性
			result_map=this.validParam(map);
			if("0".equals(ConvertUtil.getString(result_map.get("resultCode")))){
				ConvertUtil.setResponseByAjax(response, result_map);
				logger.error(getText("SVC00014")+ConvertUtil.getString(result_map.get("resultMessage")));
				return;
			}else{
				//调用充值接口
				Map<String,Object> save_map=binOLMBSVC03_IF.savingsCardRecharge(map);
				if("0".equals(ConvertUtil.getString(save_map.get("ERRORCODE")))){
					result_map.put("resultCode", 1);
    				result_map.put("resultMessage", getText("SVC00008"));
				}else{
					result_map.put("resultCode", 0);
    				result_map.put("resultMessage", getText("SVC00009"));
    				logger.error(getText("SVC00013")+ConvertUtil.getString(save_map.get("ERRORMSG")));
				}
				ConvertUtil.setResponseByAjax(response, result_map);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Map<String,Object> result_map=new HashMap<String,Object>();
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				result_map.put("resultCode", 0);
				result_map.put("resultMessage", getText("SVC00009"));
				ConvertUtil.setResponseByAjax(response, result_map);
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				result_map.put("resultCode", 0);
				result_map.put("resultMessage", getText("SVC00009"));
				ConvertUtil.setResponseByAjax(response, result_map);
			 }
		}
	}
	
	private Map<String,Object> validParam(Map<String, Object> params) {
		Map<String,Object> resultMap=new HashMap<String, Object>();
		String cardCode=ConvertUtil.getString(params.get("cardCode"));
		String cardType=ConvertUtil.getString(params.get("cardType"));
		String counterCode=ConvertUtil.getString(params.get("counterCode"));
		String camount=ConvertUtil.getString(params.get("camount"));
		List<Map<String,Object>> service_list=(List<Map<String,Object>>)params.get("service_list");
		if("".equals(counterCode) || null ==counterCode){
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", getText("SVC00003"));
			return resultMap;
		}
		
		if("".equals(cardCode) || null ==cardCode){
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", getText("SVC00005"));
			return resultMap;
		}
		if("".equals(cardType) || null ==cardType){
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", getText("SVC00004"));
			return resultMap;
		}
		if("".equals(camount) || null ==camount || !isNum(camount)){
			resultMap.put("resultCode", 0);
			resultMap.put("resultMessage", getText("SVC00010"));
			return resultMap;
		}
		if("2".equals(cardType) || "3".equals(cardType)){
			String quantity;
			String serviceType;
			if(service_list.size() == 0){
				resultMap.put("resultCode", 0);
				resultMap.put("resultMessage", getText("SVC00007"));
				return resultMap;
			}
			for(Map<String,Object> map:service_list){
				serviceType=ConvertUtil.getString(map.get("ServiceType"));
				quantity=ConvertUtil.getString(map.get("Quantity"));
				if("".equals(serviceType) || !isNum(serviceType)){
					resultMap.put("resultCode", 0);
					resultMap.put("resultMessage", getText("SVC00006"));
					return resultMap;
				}
				if(!isNum(quantity) && !"".equals(quantity)){
					resultMap.put("resultCode", 0);
					resultMap.put("resultMessage", getText("SVC00007"));
					return resultMap;
				}
			}
		}
		resultMap.put("resultCode", 1);
		resultMap.put("resultMessage", getText("SVC00008"));
		return resultMap;
	}
	
	private  boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	@Override
	public BINOLMBSVC03_Form getModel() {
		return form;
	}

}
