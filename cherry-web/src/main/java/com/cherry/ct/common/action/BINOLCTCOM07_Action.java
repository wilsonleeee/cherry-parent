package com.cherry.ct.common.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.form.BINOLCTCOM07_Form;
import com.cherry.ct.common.interfaces.BINOLCTCOM07_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLCTCOM07_Action extends BaseAction implements ModelDriven<BINOLCTCOM07_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLCTCOM07_Form form = new BINOLCTCOM07_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTCOM07_Action.class);
	
	@Resource
	private BINOLCTCOM07_IF binOLCTCOM07_IF;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM32_BL binOLCM32_BL;
	
	/** 品牌List */
	private List<Map<String, Object>> brandList;
	
	/** 沟通模板变量List */
	private List paramList;
	
	/** 沟通模版内容非法字符List*/
	private List illegalCharList;
	
	public List<Map<String, Object>> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Map<String, Object>> brandList) {
		this.brandList = brandList;
	}

	public List getParamList() {
		return paramList;
	}

	public void setParamList(List paramList) {
		this.paramList = paramList;
	}

	public String init() throws Exception{
		try{
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织ID
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
			// 获取手机号验证规则
			String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
			
			String mobilePhone = form.getMobilePhone();
			String sourse = form.getSourse();
			
			if("".equals(ConvertUtil.getString(mobilePhone))){
				// 参数中不存在手机号码的情况
				this.addActionError(getText("CTM00019"));
				return "BINOLCTCOM07_1";
			}else if(!CherryChecker.isPhoneValid(mobilePhone, mobileRule)){
				this.addActionError(getText("ECM00008",new String[]{getText("PBS00070")}));
				return "BINOLCTCOM07_1";
			}else {
				if("".equals(ConvertUtil.getString(sourse))){
					// 参数中不存在数据来源的情况
					this.addActionError(getText("CTM00020"));
					return "BINOLCTCOM07_1";
				}else{
					Map<String, Object> map = new HashMap<String, Object>();
					// 所属组织
					map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
					
					map.put(CherryConstants.BRANDINFOID, brandInfoId);
					
					// 模板用途
					map.put("templateUse", "SDXX");
					
					// 默认签名
					form.setSignature(userInfo.getBrandName());
					
					//有效区分
					map.put("validFlag", "1");
					
					paramList = binOLCM32_BL.getVariableList(map);
					//沟通方式，默认为短信
					map.put("commType", 1);
					//非法字符
					setIllegalCharList(binOLCM32_BL.getIllegalCharList(map));
				}
			}
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return "BINOLCTCOM07_1";
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return "BINOLCTCOM07_1";
			 }
		}
		return SUCCESS;
	}
	
	public void send() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌代码
			String brandCode = userInfo.getBrandCode();
			// 品牌ID
			int brandInfoId = userInfo.getBIN_BrandInfoID();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 组织代码
    		map.put("orgCode", userInfo.getOrganizationInfoCode());
			// 品牌ID
			map.put("brandInfoId", brandInfoId);
			// 品牌代码
			map.put("brandCode", brandCode);
			// 沟通客户编号
			map.put("customerSysId", form.getCustomerSysId());
			// 会员卡号
			map.put("memberCode", form.getMemberCode());
			// 沟通信息接收号码
			map.put("mobilePhone", form.getMobilePhone());
			// 沟通信息内容
			map.put("contents", form.getMsgContents());
			// 数据来源
			map.put("sourse", form.getSourse());
			
			binOLCTCOM07_IF.tran_sendMsg(map);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
			 }
		}
	}
	
	@Override
	public BINOLCTCOM07_Form getModel() {
		return form;
	}

	public List getIllegalCharList() {
		return illegalCharList;
	}

	public void setIllegalCharList(List illegalCharList) {
		this.illegalCharList = illegalCharList;
	}
}
