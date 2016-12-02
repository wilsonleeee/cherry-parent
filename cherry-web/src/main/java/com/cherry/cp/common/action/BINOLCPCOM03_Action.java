/*	
 * @(#)BINOLCPCOM03_Action.java     1.0 2011/7/18		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.cp.common.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM33_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.form.BINOLCPCOM03_Form;
import com.cherry.cp.common.interfaces.BINOLCPCOM03_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员活动共通调用方法 Action
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM03_Action extends BaseAction implements ModelDriven<BINOLCPCOM03_Form>{
	
	private static final long serialVersionUID = -1817026593827680484L;
	
	/**打印错误日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCPCOM03_Action.class);

	@Resource
    private BINOLCPCOM03_IF binolcpcom03IF;
	
	@Resource
    private BINOLCM33_BL binOLCM33_BL;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 会员等级ID */
	private String memberLevelId;
	
	/** 活动类型 */
	private String campaignType;
	
	/** 会员查询条件 */
	private String searchCode;
	
	private String campMebInfo;
	
	private String campMebType;
	
	private String recordName;
	
	private String saveFlag;
	
	/** 活动对象类型 */
	private String recordType;

	/** 导出文件名 */
	private String exportName;
	
	/** 会员查询条件 */
	private String batchCode;
	
	/** 活动编码 */
	private String campaignCode;
	
	/**导入成功会员list*/
    private List<Map<String,Object>> memList;
    
    /**导入couponlist*/
    private List<Map<String,Object>> couponList;

	private String groupType;
	
	public List<Map<String, Object>> getMemList() {
		return memList;
	}

	public void setMemList(List<Map<String, Object>> memList) {
		this.memList = memList;
	}
	
	public String getCampMebInfo() {
		return campMebInfo;
	}

	public void setCampMebInfo(String campMebInfo) {
		this.campMebInfo = campMebInfo;
	}
	
	public String getSaveFlag() {
		return saveFlag;
	}

	public void setSaveFlag(String saveFlag) {
		this.saveFlag = saveFlag;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}
	
	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	public String getMemberLevelId() {
		return memberLevelId;
	}

	public void setMemberLevelId(String memberLevelId) {
		this.memberLevelId = memberLevelId;
	}
	
	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getCampMebType() {
		return campMebType;
	}

	public void setCampMebType(String campMebType) {
		this.campMebType = campMebType;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	
	
	public List<Map<String, Object>> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<Map<String, Object>> couponList) {
		this.couponList = couponList;
	}

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/**
	 * AJAX 取得会员等级有效期
	 * 
	 * @throws Exception
	 */
	public void queryLevelDate() throws Exception {
		Map<String, Object> map = getMap();
		// 会员等级ID
		map.put("memberLevelId", memberLevelId);
		// 会员等级List
		List<Map<String, Object>> memberLevelList = binolcpcom03IF.getMemberLevelList(map);
		if (null != memberLevelList && !memberLevelList.isEmpty()) {
			// 响应JSON对象
			ConvertUtil.setResponseByAjax(response, memberLevelList.get(0));
		}
	}
	
	/**
	 * AJAX 取得会员活动组信息List
	 * 
	 * @throws Exception
	 */
	public void queryCampaignGrp() throws Exception {
		Map<String, Object> map = getMap();
		// 活动类型
		map.put("campaignType", campaignType);
		// 会员活动组信息List
		List<Map<String, Object>> campaignGrpList = binolcpcom03IF.getCampaignGrpList(map);
		if (null != campaignGrpList && !campaignGrpList.isEmpty()) {
			// 响应JSON对象
			ConvertUtil.setResponseByAjax(response, campaignGrpList);
		}
	}
	
	/**
	 * AJAX 取得会员搜索条件
	 * 
	 * @throws Exception
	 */
	public String queryMemSearchCode() throws Exception {
		try {
			Map<String, Object> map = getMap();
			//会员搜索参数
			Map<String,Object> searchMap = ConvertUtil.json2Map(campMebInfo);
			map.putAll(searchMap);
			//搜索结果Map
			Map<String, Object> resMap = null;
			if(CampConstants.CAMP_MEB_TYPE_2.equals(recordType)){
				// 批处理一页最大数
				int length = CherryConstants.BATCH_PAGE_MAX_NUM;
				map.put("SORT_ID", "memId");
				map.put("END", length);
				//搜索结果Map
				resMap = binOLCM33_BL.searchMemList(map);
				if(null != resMap){
					//活动对象总数量
					int total = ConvertUtil.getInt(resMap.get("total"));
					if(total>length){
						throw new CherryException("PCP00046");
					}
					if(total > 0){
						if("1".equals(saveFlag)){
							map.put("recordCount",total);
						}
					}
				}
			}	
			//对象类型（会员）
			map.put(CampConstants.CUSTOMER_TYPE, CampConstants.CUSTOMER_TYPE_1);
			// 搜索条件JSON
			map.put("conditionInfo",campMebInfo);
			// 搜索名称
			map.put("recordName",recordName);
			//数据来源（搜索）
			map.put(CampConstants.FROM_TYPE, CampConstants.FROM_TYPE_1);
			// 搜索条件
			String memSearchCode = binolcpcom03IF.addMemSearchLog(map, recordType);
			if("1".equals(saveFlag) && CampConstants.CAMP_MEB_TYPE_2.equals(recordType)){
				List<Map<String, Object>> memberList = (List<Map<String, Object>>)resMap.get("list");
				Map<String, Object> comMap = new HashMap<String, Object>();
				comMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
				comMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
				comMap.put(CampConstants.SEARCH_CODE, memSearchCode);
				comMap.put(CampConstants.CUSTOMER_TYPE, CampConstants.CUSTOMER_TYPE_1);
				
				for(Map<String, Object> mem : memberList){
					String birthYear = ConvertUtil.getString(mem.get(CampConstants.BIRTHYEAR));
					String birthMonth = ConvertUtil.getString(mem.get(CampConstants.BIRTHMONTH));
					String birthDay = ConvertUtil.getString(mem.get(CampConstants.BIRTHDAY));
					mem.put(CampConstants.BIRTHDAY, birthYear + birthMonth + birthDay);
					mem.putAll(comMap);
				}
				binolcpcom03IF.addCustomerInfo(memberList);
			}
			if(null!=memSearchCode && !"".equals(memSearchCode)){
				// 响应JSON对象
				ConvertUtil.setResponseByAjax(response, memSearchCode);
			}
		}catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}
	
	/**
	 * 取得会员数量
	 */
	public void searchMemCount()throws Exception{
		Map<String, Object> map = getMap();
		// 会员信息搜索条件
        map.put(CampConstants.SEARCH_CODE, searchCode);
        map.put(CampConstants.CAMP_MEB_TYPE, campMebType);
        map.put("conInfo", campMebInfo);
		int count = binolcpcom03IF.searchMemCount(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, count);
	}
	
	/**
	 * 取得共通信息Map
	 * @return
	 */
	private Map<String, Object> getMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		if(CherryChecker.isNullOrEmpty(brandInfoId)){
			// 品牌信息ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}else{
			// 品牌信息ID
			map.put(CherryConstants.BRANDINFOID, brandInfoId);
		}
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONID, userInfo.getBIN_OrganizationID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		return map;
	}
	    
	    /**
	     * 查询导入成功的会员
	     * @return
	     * @throws Exception
	     */
	    public String memInfosearch() throws Exception{
	    	//共通参数Map
	    	Map<String, Object> map = getMap();
	    	// form参数设置到map中
	    	ConvertUtil.setForm(form, map);
	    	int total = 0;
			int totalCount = 0;
	    	if(!CherryChecker.isNullOrEmpty(searchCode)){
	    		// 会员信息搜索条件
		        map.put(CampConstants.SEARCH_CODE, searchCode);
				map.put("groupType", groupType);
		        // 取得searchLog信息
				Map<String, Object> searchLogMap = binolcpcom03IF.getMemberInfo(map);
				if(null!=searchLogMap){
					total = ConvertUtil.getInt(searchLogMap.get("recordCount"));
					if(total == 0){
						campMebInfo = ConvertUtil.getString(searchLogMap.get("conditionInfo"));
					}else{
						totalCount = binolcpcom03IF.getMemInfoCount(map);
						//会员信息list
						memList=binolcpcom03IF.getMemInfoList(map);
					}
				}
	    	}
	    	if(total == 0){
	    		// 会员共通查询
				totalCount = searchMemList(map,campMebInfo);
	    	}
	    	if(null != memList){
	    		for(Map<String,Object> mem : memList){
	    			String birthDay = ConvertUtil.getString(mem.get(CampConstants.BIRTHDAY)).trim();
	    			String memCode = ConvertUtil.getString(mem.get("memCode")).trim();
	    			if(!CherryChecker.isNullOrEmpty(birthDay)){
	    				try {
	    					mem.put(CampConstants.BIRTHDAY, DateUtil.formatDate(birthDay));
						} catch (Exception e) {
							logger.error("会员卡号：【"+memCode+"】"+"================"+e.getMessage()+"================",e);
						}
	    			}
	    		}
	    	}
			// form表单设置
			form.setITotalDisplayRecords(totalCount);
			form.setITotalRecords(totalCount);
			
			return "BINOLCPACT03";
	    }
	    
	    /**
	     * 查询导入成功的coupon
	     * @return
	     * @throws Exception
	     */
	    public String couponsearch() throws Exception{
	    	//共通参数Map
	    	Map<String, Object> map = getMap();
	    	// form参数设置到map中
	    	ConvertUtil.setForm(form, map);
	    	int total = 0;
	    	if(!CherryChecker.isNullOrEmpty(batchCode)){
	    		// 会员信息搜索条件
		        map.put("batchCode", batchCode);
		        //活动编码
		        map.put("campaignCode", campaignCode);
		        //Coupon数量
		        total= binolcpcom03IF.getCouponCount(map);
				//会员信息list
		        couponList=binolcpcom03IF.getCouponList(map);
				
	    	}
			// form表单设置
			form.setITotalDisplayRecords(total);
			form.setITotalRecords(total);
			
			return SUCCESS;
	    }
	    
	    private  BINOLCPCOM03_Form form = new BINOLCPCOM03_Form();
	    @Override
	    public BINOLCPCOM03_Form getModel() {
			return form;
		}
	    
	    /**
	     * 会员共通查询
	     * @param map
	     * @param conInfo
	     * @return
	     */
	    @SuppressWarnings("unchecked")
		private int searchMemList(Map<String,Object> map,String campMebInfo){
	    	int total = 0;
	    	Map<String, Object> conMap = ConvertUtil.json2Map(campMebInfo);
			conMap.putAll(map);
			Map<String, Object> resMap = binOLCM33_BL.searchMemList(conMap);
			if(null != resMap){
				total = ConvertUtil.getInt(resMap.get("total"));
				//会员信息list
				memList= (List<Map<String, Object>>)resMap.get("list");
				if(null!=memList){
					for(Map<String, Object> memMap : memList){
						String birthYear = ConvertUtil.getString(memMap.get(CampConstants.BIRTHYEAR));
						String birthMonth = ConvertUtil.getString(memMap.get(CampConstants.BIRTHMONTH));
						String birthDay = ConvertUtil.getString(memMap.get(CampConstants.BIRTHDAY));
						memMap.put(CampConstants.BIRTHDAY, birthYear + birthMonth + birthDay);
						// 对象类型【会员】
						memMap.put(CampConstants.CUSTOMER_TYPE, CampConstants.CUSTOMER_TYPE_1);
					}
				}
			}
			return total;
	    }
}
