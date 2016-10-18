package com.cherry.wp.wy.wyl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.activity.interfaces.Activity_IF;
import com.cherry.webserviceout.couponcode.ISynchroCodeStatus;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.wy.wyl.form.BINOLWYWYL02_Form;
import com.cherry.wp.wy.wyl.interfaces.BINOLWYWYL02_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLWYWYL02_Action extends BaseAction implements ModelDriven<BINOLWYWYL02_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1532013961483947269L;
	
	private BINOLWYWYL02_Form form = new BINOLWYWYL02_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLWYWYL02_Action.class);
	
	@Resource
	private Activity_IF activity_IF;
	
	@Resource
	private ISynchroCodeStatus isynchroCodeStatus;
	
	@Resource(name="binOLWPCM01_BL")
	private BINOLWPCM01_IF binOLWPCM01_IF;
	
	@Resource(name="binOLWYWYL02_BL")
	private BINOLWYWYL02_IF binOLWYWYL02_IF;
	
	private String userName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String init(){
		// 用户信息
		try{
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			userName = userInfo.getLoginName();
		}catch(Exception e){
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
	
	public void search() throws Exception{
		try{
			String applyCoupon = ConvertUtil.getString(form.getApplyCoupon());
			if(!"".equals(applyCoupon)){
				// 用户信息
				UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
				// 柜台信息
				CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
				
				Map<String, Object> map = new HashMap<String, Object>();
				// 组织ID
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 品牌ID
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 状态
				map.put("stateArr", new String[]{"AR", "OK"});
				// 申领代号
				map.put("couponCode", applyCoupon);
				// 柜台号
				map.put("counterCode", counterInfo.getCounterCode());
				
				// 获取单据详细信息
				List<Map<String, Object>> resultList = binOLWYWYL02_IF.getActivityOrderInfo(map);
				if(resultList != null && !resultList.isEmpty()){
					if(resultList.size() > 1){
						Map<String, Object> orderMap = new HashMap<String, Object>();
						// 查询结果若存在多个单据的情况下先取一个单据，单据需要一个一个处理
						for(Map<String,Object> orderInfoMap : resultList){
							String orderState = ConvertUtil.getString(orderInfoMap.get("state"));
							if("AR".equals(orderState)){
								orderMap = new HashMap<String, Object>();
								orderMap.putAll(orderInfoMap);
								break;
							}else{
								orderMap = new HashMap<String, Object>();
								orderMap.putAll(orderInfoMap);
							}
						}
						ConvertUtil.setResponseByAjax(response, orderMap);
					}else{
						// 查询结果为单个单据的情况
						for(Map<String,Object> orderInfoMap : resultList){
							ConvertUtil.setResponseByAjax(response, orderInfoMap);
						}
					}
				}else{
					// 返回结果为空的情况
					ConvertUtil.setResponseByAjax(response, "");
				}
			}else{
				// 返回空的情况
				ConvertUtil.setResponseByAjax(response, "");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "");
			 }
		}
	}
	
	public void getGift() throws Exception{
		try{
			String billCode = ConvertUtil.getString(form.getBillCode());
			String subType = ConvertUtil.getString(form.getSubType());
			String sysTime = binOLWPCM01_IF.getSYSDate();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String brandCode = userInfo.getBrandCode();
			// 柜台信息
			CounterInfo counterInfo = (CounterInfo) session.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			
			Map<String, Object> map = new HashMap<String, Object>();
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌ID1
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 用户ID
			map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			// 品牌ID2
			map.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			// 组织代码
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 品牌代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 员工号
			map.put("employeeCode", userInfo.getEmployeeCode());
			// 单据号
			map.put("BillNo", billCode);
			// 单据号
			map.put("SubType", subType);
			// 状态
			map.put("State", "OK");
			// 操作时间
			map.put("OptTime", sysTime);
			// 会员ID
			map.put("memberInfoId", form.getMemberInfoId());
			// 微信号
			map.put("messageId", form.getMessageId());
			// 会员姓名
			map.put("memberName", form.getMemberName());
			// 会员手机号
			map.put("mobilePhone", form.getMobile());
			// 会员生日月
			map.put("month", form.getBirthDayMonthQ());
			// 会员生日日期
			map.put("day", form.getBirthDayDateQ());
			// 性别
			map.put("gender", form.getGender());
			// 原会员姓名
			map.put("orgMemberName", form.getOrgMemberName());
			// 原会员生日
			map.put("orgBirthDay", form.getOrgBirthDay());
			// 原会员性别
			map.put("orgGender", form.getOrgGender());
			// 部门ID
			map.put("organizationId", counterInfo.getOrganizationId());
			// 柜台号
			map.put("counterCode", counterInfo.getCounterCode());
			
			// 同步到第三方接口需要的参数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 组织ID
			paramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			paramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			// 状态
			paramMap.put("Status", "0");
			// 申领代码
			paramMap.put("CouponCode", form.getApplyCoupon());
			
			// 获取单据详细信息
			Map<String, Object> resultMap = activity_IF.tran_changeOrderState(map);
			if(resultMap != null && !resultMap.isEmpty()){
				String errorCode = ConvertUtil.getString(map.get("ERRORCODE"));
				String errorMsg = ConvertUtil.getString(map.get("ERRORMSG"));
				if("0".equals(errorCode) || "".equals(errorCode)){
					try{
						// 同步到第三方接口
						isynchroCodeStatus.synchroCodeStatus(brandCode, paramMap);
					}catch(Exception e){
						logger.error(e.getMessage(), e);
					}
					try{
						// 更新会员信息
						Map<String, Object> returnMap = binOLWYWYL02_IF.updateMemberInfo(map);
						// 更新单据中的会员ID和会员卡号
						if(returnMap != null && !returnMap.isEmpty()){
							String updateFlag = ConvertUtil.getString(returnMap.get("updateFlag"));
							if("Y".equals(updateFlag)){
								String memberInfoId = ConvertUtil.getString(returnMap.get("memberInfoId"));
								String memCode = ConvertUtil.getString(returnMap.get("memCode"));
								Map<String, Object> praMap = new HashMap<String, Object>();
								praMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
								praMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
								praMap.put("billCode", billCode);
								praMap.put("memberInfoId", memberInfoId);
								praMap.put("memCode", memCode);
								binOLWYWYL02_IF.updateOrderMemberId(praMap);
							}
						}
					}catch(Exception e){
						logger.error(e.getMessage(), e);
					}
					ConvertUtil.setResponseByAjax(response, "SUCCESS");
				}else{
					ConvertUtil.setResponseByAjax(response, errorMsg);
				}
			}else{
				try{
					// 同步到第三方接口
					isynchroCodeStatus.synchroCodeStatus(brandCode, paramMap);
				}catch(Exception e){
					logger.error(e.getMessage(), e);
				}
				try{
					// 更新会员信息
					Map<String, Object> returnMap = binOLWYWYL02_IF.updateMemberInfo(map);
					// 更新单据中的会员ID和会员卡号
					if(returnMap != null && !returnMap.isEmpty()){
						String updateFlag = ConvertUtil.getString(returnMap.get("updateFlag"));
						if("Y".equals(updateFlag)){
							String memberInfoId = ConvertUtil.getString(returnMap.get("memberInfoId"));
							String memCode = ConvertUtil.getString(returnMap.get("memCode"));
							Map<String, Object> praMap = new HashMap<String, Object>();
							praMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
							praMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
							praMap.put("billCode", billCode);
							praMap.put("memberInfoId", memberInfoId);
							praMap.put("memCode", memCode);
							binOLWYWYL02_IF.updateOrderMemberId(praMap);
						}
					}
				}catch(Exception e){
					logger.error(e.getMessage(), e);
				}
				// 返回结果为空的情况
				ConvertUtil.setResponseByAjax(response, "SUCCESS");
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "");
			 }
		}
	}
	
	@Override
	public BINOLWYWYL02_Form getModel() {
		return form;
	}

}
