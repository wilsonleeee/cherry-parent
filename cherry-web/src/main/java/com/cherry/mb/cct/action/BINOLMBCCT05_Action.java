package com.cherry.mb.cct.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.cct.form.BINOLMBCCT05_Form;
import com.cherry.mb.cct.interfaces.BINOLMBCCT05_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBCCT05_Action extends BaseAction implements ModelDriven<BINOLMBCCT05_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BINOLMBCCT05_Form form = new BINOLMBCCT05_Form();
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBCCT05_Action.class);
	
	@Resource
	private BINOLMBCCT05_IF binolmbcct05_IF;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	private String mobileRule;
	
	public String getMobileRule() {
		return mobileRule;
	}

	public void setMobileRule(String mobileRule) {
		this.mobileRule = mobileRule;
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
			// 手机号校验规则
			mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
			
			if(form.getCustomerSysId() != null && !"".equals(form.getCustomerSysId())) {
				//参数Map
				Map<String, Object> map = new HashMap<String, Object>();
				
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
						.getBIN_OrganizationInfoID());
				// 所属品牌不存在的场合
				if(form.getCcBrandInfoId() == null || "".equals(form.getCcBrandInfoId())) {
					// 不是总部的场合
					if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
					// 所属品牌
						map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					}
				} else {
					map.put(CherryConstants.BRANDINFOID, form.getCcBrandInfoId());
				}
				map.put("customerCode", form.getCustomerSysId());
				Map<String, Object> customerMap = binolmbcct05_IF.getCustomerInfo(map);
				if(null != customerMap && !customerMap.isEmpty()){
					form.setCustomerName(ConvertUtil.getString(customerMap.get("customerName")));
					form.setCustomerNumber(ConvertUtil.getString(customerMap.get("customerNumber")));
					form.setCcGender(ConvertUtil.getString(customerMap.get("gender")));
					form.setCcMobilePhone(ConvertUtil.getString(customerMap.get("mobilePhone")));
					form.setCcTelephone(ConvertUtil.getString(customerMap.get("telephone")));
					form.setCcBirthDay(ConvertUtil.getString(customerMap.get("birthDay")));
					form.setCustomerType(ConvertUtil.getString(customerMap.get("customerType")));
					form.setCompany(ConvertUtil.getString(customerMap.get("company")));
					form.setPost(ConvertUtil.getString(customerMap.get("post")));
					form.setIndustry(ConvertUtil.getString(customerMap.get("industry")));
					form.setCcZip(ConvertUtil.getString(customerMap.get("zip")));
					form.setCcMessageId(ConvertUtil.getString(customerMap.get("messageId")));
					form.setCcEmail(ConvertUtil.getString(customerMap.get("email")));
					form.setCcProvince(ConvertUtil.getString(customerMap.get("province")));
					form.setCcCity(ConvertUtil.getString(customerMap.get("city")));
					form.setCcAddress(ConvertUtil.getString(customerMap.get("address")));
					form.setCcMemo(ConvertUtil.getString(customerMap.get("memo")));
					form.setCcIsReceiveMsg(ConvertUtil.getString(customerMap.get("isReceiveMsg")));
				}else{
					form.setCcGender("2");
					form.setCcIsReceiveMsg("1");
				}
			}
			return SUCCESS;
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
	}
	
	public String save() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			String type = "";
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌不存在的场合
			if(form.getCcBrandInfoId() == null || "".equals(form.getCcBrandInfoId())) {
				// 不是总部的场合
				if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				}
			} else {
				map.put(CherryConstants.BRANDINFOID, form.getCcBrandInfoId());
			}
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 客户编号
			map.put("customerCode", form.getCustomerSysId());
			// 客户姓名
			map.put("customerName", form.getCustomerName());
			// 客户性别
			map.put("gender", form.getCcGender());
			// 手机号码
			map.put("mobilePhone", form.getCcMobilePhone());
			// 固定电话号码
			map.put("telephone", form.getCcTelephone());
			// 客户生日
			map.put("birthDay", form.getCcBirthDay());
			// 客户类型
			map.put("customerType", form.getCustomerType());
			// 公司/单位
			map.put("company", form.getCompany());
			// 职务
			map.put("post", form.getPost());
			// 所属行业
			map.put("industry", form.getIndustry());
			// 邮编
			map.put("zip", form.getCcZip());
			// 微信号
			map.put("messageId", form.getCcMessageId());
			// 电子邮箱
			map.put("email", form.getCcEmail());
			// 省份
			map.put("province", form.getCcProvince());
			// 城市
			map.put("city", form.getCcCity());
			// 详细地址
			map.put("address", form.getCcAddress());
			// 数据来源
			map.put("dataSource", "1");
			// 备注
			map.put("memo", form.getCcMemo());
			// 是否接收短信
			map.put("isReceiveMsg", form.getCcIsReceiveMsg());
			
			Map<String, Object> customerMap = binolmbcct05_IF.getCustomerInfo(map);
			if(null != customerMap && !customerMap.isEmpty()){
				type = "UPDATE";
			}else{
				type = "INSERT";
			}
			// 保存沟通设置
			binolmbcct05_IF.saveCustomer(map, type);
			//处理成功
			return SUCCESS;
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
	}
	
	@Override
	public BINOLMBCCT05_Form getModel() {
		return form;
	}

}
