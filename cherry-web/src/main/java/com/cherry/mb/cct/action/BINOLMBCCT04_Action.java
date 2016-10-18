package com.cherry.mb.cct.action;

import java.util.HashMap;
import java.util.List;
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
import com.cherry.mb.cct.form.BINOLMBCCT04_Form;
import com.cherry.mb.cct.interfaces.BINOLMBCCT01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBCCT04_Action extends BaseAction implements ModelDriven<BINOLMBCCT04_Form>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BINOLMBCCT04_Form form = new BINOLMBCCT04_Form();
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBCCT04_Action.class);
	
	@Resource
	private BINOLMBCCT01_IF binolmbcct01_IF;
	
	private List<Map<String, Object>> memberList;
	
	public List<Map<String, Object>> getMemberList() {
		return memberList;
	}
	
	public void setMemberList(List<Map<String, Object>> memberList) {
		this.memberList = memberList;
	}

	public String init() throws Exception{
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
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
			String birthYear = "";
			String birthDay = form.getCcBirth();
			if(null!=birthDay && !"".equals(birthDay)){
				birthDay = birthDay.replace("-", "");
				birthDay = birthDay.replace("/", "");
				if(birthDay.length()==8){
					birthYear = birthDay.substring(0,4);
					birthDay = birthDay.substring(4,8);
				}
			}
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			
			String encryPhone = "";
			String brandCode = userInfo.getBrandCode();
			String phone = form.getCcMobilePhone();
			if (!CherryChecker.isNullOrEmpty(phone, true)) {
				try{
					encryPhone = CherrySecret.encryptData(brandCode, phone);
				}catch(Exception ex){}
			}
			map.put("brandCode", brandCode);
			// 手机号码
			map.put("mobilePhone", encryPhone);
			// 固定电话
			map.put("telephone", form.getCcTelephone());
			// 会员卡号
			map.put("memCode", form.getCcMemCode());
			// 客户姓名
			map.put("memName", form.getCcMemName());
			// 客户出生年份
			map.put("birthYear", birthYear);
			// 客户生日
			map.put("birthDay", birthDay);
			// 会员有效标识
			map.put("validFlag", "1");
			// 会员卡有效标识
			map.put("cardValidFlag", "1");
			//取得模板数量
			int count = binolmbcct01_IF.getMemberCountByPhone(map);
			if(count > 0){
				List<Map<String, Object>> memList = binolmbcct01_IF.getMemberListByPhone(map);
				// 取得List
				this.setMemberList(memList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
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
		return SUCCESS;
	}
	
	public void updateCallLog() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
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
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			
			map.put("callId", form.getCallId());
			
			map.put("customerSysId", form.getCustomerSysId());
			
			map.put("isMember", form.getIsMember());
			
			binolmbcct01_IF.saveCallLog(map, "");
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
	public BINOLMBCCT04_Form getModel() {
		return form;
	}

}
