package com.cherry.cp.point.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.point.bl.BINOLCPPOI01_BL;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员积分基本信息 Action
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPPOI01_Action extends BaseAction{
	
	private static final long serialVersionUID = -2623974491811233042L;
	
	private static Logger logger = LoggerFactory
	.getLogger(BINOLCPPOI01_Action.class.getName());
	
	@Resource
    private BINOLCPPOI01_BL binOLCPPOI01_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 区域选择类型 */
	private String locationType;
	
	/** 城市Id */
	private String cityID;
	
	/** 渠道Id */
	private String channelID;
	
	/** 会员等级有效期*/
	private String levelDateList;
	
	/** 上传的文件 */
	private File upExcel;
	
	/** 导入柜台信息 */
	private String counterStr;
	
	/** 是否加载柜台  */
	private int loadingCnt;
	
	/** 导入会员批次号 */
	private String searchCode;
	
	/** 批次名称 */
	private String recordName;
	
	/** 导入类型*/
	private String importType;
	
	/** 导入类型*/
	private String customerType;

	/** 导入批次号 */
	private String batchCode;
	
	/** Coupon失效时间*/
	private String obtainToDate;
	
	/** 活动编号 */
	private String campaignCode;
	
	/** 备注 */
	private String comments;
	
	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public int isLoadingCnt() {
		return loadingCnt;
	}

	public void setLoadingCnt(int loadingCnt) {
		this.loadingCnt = loadingCnt;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	
	public void setLevelDateList(String levelDateList) {
		this.levelDateList = levelDateList;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public String getCounterStr() {
		return counterStr;
	}

	public void setCounterStr(String counterStr) {
		this.counterStr = counterStr;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	
	public String getObtainToDate() {
		return obtainToDate;
	}

	public void setObtainToDate(String obtainToDate) {
		this.obtainToDate = obtainToDate;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * 通过Ajax取得活动地点信息
	 * @throws Exception 
	 */
	public void getLocationByAjax() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		List resultTreeList = binOLCPPOI01_BL.getActiveLocation(map);
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
	
	/**
	 * 通过AJAX取得柜台节点
	 * @throws Exception
	 */
	public void getCounterByAjax() throws Exception{
		// 取得session信息
		Map<String, Object> map  = this.getSessionInfo();
		if(null != channelID){
			map.put("channel", channelID);
		}
		if(null != cityID){
			map.put("city", cityID);
		}
		List resultTreeList = binOLCPPOI01_BL.getCounterInfoList(map);
		ConvertUtil.setResponseByAjax(response, resultTreeList);
	}
	
	/**
	 * 取得等级有效期List
	 * @throws Exception
	 */
	public void getLevelDateList() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			// 取得所管辖的品牌List
			List<Map<String, Object>> brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				map.put("brandInfoId", ((Map<String, Object>) 
						brandInfoList.get(0)).get("brandInfoId"));
			}
		} else {
			map.put("brandInfoId", brandInfoId);
		}
		// 取得会员活动等级List
		levelDateList = JSONUtil.serialize(binOLCPPOI01_BL.getLevelDateList(map));
	}
	
	/**
	 * <p>
	 * 导入柜台处理
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importCounter() throws Exception {
		try {
			// 参数MAP
			Map<String, Object> map = getSessionInfo();
			// 上传的文件
			map.put("upExcel", upExcel);
			// 导入柜台处理
			Map<String, Object> infoMap = binOLCPPOI01_BL.ResolveExcel(map);
			// 取得出错的柜台
			List<Map<String, Object>> errorList = (List<Map<String, Object>>) infoMap.get("errorList");
			// 循环出错的柜台list，整理出错信息
			String errorMsg = "";
			if(null != errorList && !errorList.isEmpty()){
				for(int i = 0;i < errorList.size();i++){
					Map<String, Object> errorMap = errorList.get(i);
					errorMsg = errorMsg + errorMap.get("counterCode")+ "(" + errorMap.get("counterNameErrInfo") + ")" ;
					if(i < errorList.size() - 1){
						errorMsg = errorMsg + ",";
					}
					if(i > 5){
						break;
					}
				}
			}
			if(!"".equals(errorMsg)){
				infoMap.put("errorMsg", getText("PMB00048", new String[] { errorMsg, String.valueOf(errorList.size()) }));
			}
			// 响应JSON对象
			ConvertUtil.setResponseByAjax(response, infoMap);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}
	
	/**
	 * <p>
	 * 导入特定商品处理
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importSpecPrt() throws Exception {
		try {
			// 参数MAP
			Map<String, Object> map = getSessionInfo();
			// 上传的文件
			map.put("upExcel", upExcel);
			// 导入特定商品处理
			Map<String, Object> infoMap = binOLCPPOI01_BL.ResolvePrtExcel(map);
			// 取得出错的商品
			List<Map<String, Object>> errorList = (List<Map<String, Object>>) infoMap.get("errorList");
			// 循环出错的商品list，整理出错信息
			String errorMsg = "";
			if(null != errorList && !errorList.isEmpty()){
				for(int i = 0;i < errorList.size();i++){
					Map<String, Object> errorMap = errorList.get(i);
					errorMsg = errorMsg + "(" + errorMap.get("barCode") + ")" + errorMap.get("nameTotal");
					if(i < errorList.size() - 1){
						errorMsg = errorMsg + ",";
					}
					if(i > 5){
						break;
					}
				}
			}
			if(!"".equals(errorMsg)){
				infoMap.put("errorMsg", getText("PMB00082", new String[] { errorMsg, String.valueOf(errorList.size()) }));
			}
			// 响应JSON对象
			ConvertUtil.setResponseByAjax(response, infoMap);
		} catch (CherryException e) {
			logger.error(e.getMessage(),e);
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}
	
	/**
	 * <p>
	 * 导入会员处理
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importMember() throws Exception {
		try {
			// 参数MAP
			Map<String, Object> map = getSessionInfo();
			// 上传的文件
			map.put("upExcel", upExcel);
			map.put(CampConstants.SEARCH_CODE, searchCode);
			//导入批次名称
			map.put("recordName", recordName);
			//导入类型
			map.put("importType", importType);
			//对象类型
			map.put("customerType", customerType);
			//备注
			map.put("comments", comments);
			// 导入会员处理
			Map<String, Object> infoMap = binOLCPPOI01_BL.ResolveMemExcel(map);
			infoMap.put("importType", importType);
			//消息msgInfo
			Map<String, Object> msgInfo = new HashMap<String, Object>();
			ArrayList<String> list = new ArrayList<String>();
			if("1".equals(importType)||"2".equals(importType)){
				list.add(getText("MMP00001", new String[] {ConvertUtil.getString(infoMap.get("newSize")),ConvertUtil.getString((infoMap.get("totalSize")))}));
			}else{
				list.add(getText("MMP00003", new String[] {ConvertUtil.getString(infoMap.get("newSize"))}));
			}
			msgInfo.put("level", 0);
			msgInfo.put("msgList", list);
			infoMap.put("msgInfo", msgInfo);
			// 响应JSON对象
			ConvertUtil.setResponseByAjax(response, infoMap);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}

	/**
	 * <p>
	 * 导入CouponCode处理
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String importCouponCode() throws Exception {
		try {
			// 参数MAP
			Map<String, Object> map = getSessionInfo();
			// 上传的文件
			map.put("upExcel", upExcel);
			//批次号
			map.put("batchCode", batchCode);
			//导入类型
			map.put("importType", importType);
			//失效时间 
			map.put("obtainToDate", obtainToDate);
			//活动档次Coupon
			map.put("campaignCode", campaignCode);
			// 导入会员处理
			Map<String, Object> infoMap = binOLCPPOI01_BL.ResolveCouponExcel(map);
			infoMap.put("importType", importType);
			//消息msgInfo
			Map<String, Object> msgInfo = new HashMap<String, Object>();
			ArrayList<String> list = new ArrayList<String>();
			if("1".equals(importType)){
				list.add(getText("MMP00006", new String[] {ConvertUtil.getString(infoMap.get("newSize")),ConvertUtil.getString((infoMap.get("totalSize")))}));
			}else{
				list.add(getText("MMP00002", new String[] {ConvertUtil.getString(infoMap.get("newSize"))}));
			}
			msgInfo.put("level", 0);
			msgInfo.put("msgList", list);
			infoMap.put("msgInfo", msgInfo);
			// 响应JSON对象
			ConvertUtil.setResponseByAjax(response, infoMap);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}
	
	/**
	 * 取得session的信息
	 * @param map
	 * @throws Exception 
	 */
	private Map getSessionInfo() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoID.equals("-9999")){
			// 取得所属品牌
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}else{
			map.put("brandInfoId",userInfo.getCurrentBrandInfoID());
		}
		map.put("organizationId", userInfo.getBIN_OrganizationID());
		map.put("language", userInfo.getLanguage());
		map.put("userID", userInfo.getBIN_UserID());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("userName", userInfo.getLoginName());
		map.put("brandCode", userInfo.getBrandCode());
		map.put("locationType", locationType);
		map.put("loadingCnt", loadingCnt);
		return map;
	}	
}
