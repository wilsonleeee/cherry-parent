package com.cherry.mb.cct.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.cct.form.BINOLMBCCT01_Form;
import com.cherry.mb.cct.interfaces.BINOLMBCCT01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBCCT01_Action extends BaseAction implements ModelDriven<BINOLMBCCT01_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLMBCCT01_Form form = new BINOLMBCCT01_Form();
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBCCT01_Action.class);
	
	@Resource
	private BINOLMBCCT01_IF binolmbcct01_IF;
    
	private String showPage;
	
	private String customerSysId;
	
	private String memberInfoId;
	
	private String customerType;
	
	private String ccTelephone;
	
	private String ccMobilePhone;
	
	private int isMember;
	
	public String getShowPage() {
		return showPage;
	}

	public void setShowPage(String showPage) {
		this.showPage = showPage;
	}

	public String getCustomerSysId() {
		return customerSysId;
	}

	public void setCustomerSysId(String customerSysId) {
		this.customerSysId = customerSysId;
	}

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCcTelephone() {
		return ccTelephone;
	}

	public void setCcTelephone(String ccTelephone) {
		this.ccTelephone = ccTelephone;
	}

	public String getCcMobilePhone() {
		return ccMobilePhone;
	}

	public void setCcMobilePhone(String ccMobilePhone) {
		this.ccMobilePhone = ccMobilePhone;
	}

	public int getIsMember() {
		return isMember;
	}

	public void setIsMember(int isMember) {
		this.isMember = isMember;
	}

	public String init() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("START", "1");
	    	map.put("SORT_ID", "joinDate DESC");
	    	map.put("END", "20");
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			String brandId = "";
			String brandCode = userInfo.getBrandCode();
			// 所属品牌不存在的场合
			if(form.getCcBrandInfoId() == null || "".equals(form.getCcBrandInfoId())) {
				// 不是总部的场合
				if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					form.setCcBrandInfoId(ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
					brandId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
				}
			} else {
				map.put(CherryConstants.BRANDINFOID, form.getCcBrandInfoId());
				brandId = form.getCcBrandInfoId();
			}
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			
			String encryPhone = "";
			String phone = form.getCustomerNumber();
			String phoneType = form.getCustomerNumberType();
			int memberCount = 0;
			int customerCount = 0;
			
			if("1".equals(phoneType)){
				map.put("telephone", phone);
				ccTelephone = phone;
			}else{
				if (!CherryChecker.isNullOrEmpty(phone, true)) {
					try{
						encryPhone = CherrySecret.encryptData(brandCode, phone);
					}catch(Exception ex){}
				}
				map.put("mobilePhone", encryPhone);
				ccMobilePhone = phone;
			}
			map.put("validFlag", "1");
			map.put("cardValidFlag", "1");
			memberCount = binolmbcct01_IF.getMemberCountByPhone(map);
			if (memberCount > 1)
	        {
				isMember = 1;
				customerType = "1";
				showPage = "1";
	        }else if(memberCount == 1){
	        	isMember = 1;
	        	customerType = "1";
	        	showPage = "2";
	        	memberInfoId = binolmbcct01_IF.getMemberIdByPhone(map);
	        	customerSysId = memberInfoId;
	        }else{
	        	isMember = 2;
	        	customerType = "0";
	        	map.put("mobilePhone", phone);
	        	customerCount = binolmbcct01_IF.getCustomerCountByPhone(map);
	        	if (customerCount > 0)
	            {
	    			showPage = "3";
	    			customerSysId = binolmbcct01_IF.getCustomerIdByPhone(map);
	            	memberInfoId = "";
	            }else{
	            	showPage = "4";
	            	if(CherryChecker.isNumeric(phone)){
	            		customerSysId = brandId + phone;
	            	}else{
	            		customerSysId = "999999999";
	            	}
	            	memberInfoId = "";
	            }
	        }
			// 记录来电日志
			map.put("callId", form.getCallId());
			map.put("customerNumber", phone);
			map.put("customerNumberType", phoneType);
			map.put("customerAreaCode", form.getCustomerAreaCode());
			map.put("calltype", form.getCalltype());
			map.put("cno", form.getCno());
			map.put("isMember", isMember);
			map.put("customerSysId", customerSysId);
			binolmbcct01_IF.saveCallLog(map, "INSERT");
		} catch(Exception e){
			logger.error("CallCenterInitError:"+e.getMessage(), e);
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
		return SUCCESS;
	}
	
	@Override
	public BINOLMBCCT01_Form getModel() {
		return form;
	}

}
