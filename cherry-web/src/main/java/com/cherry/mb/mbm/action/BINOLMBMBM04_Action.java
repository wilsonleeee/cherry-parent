/*
 * @(#)BINOLMBMBM04_Action.java     1.0 2012.10.10
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
package com.cherry.mb.mbm.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryMenu;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM04_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM04_Form;
import com.cherry.mb.ptm.bl.BINOLMBPTM02_BL;
import com.cherry.mb.ptm.bl.BINOLMBPTM03_BL;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员积分详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2012.10.10
 */
public class BINOLMBMBM04_Action extends BaseAction implements ModelDriven<BINOLMBMBM04_Form> {

	private static final long serialVersionUID = 3375611392577279937L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBMBM04_Action.class.getName());
	
	/** 会员积分详细画面BL */
	@Resource
	private BINOLMBMBM04_BL binOLMBMBM04_BL;
	
	/** 查询积分信息BL */
	@Resource
	private BINOLMBPTM02_BL binOLMBPTM02_BL;
	
	/** 查询积分明细信息BL */
	@Resource
	private BINOLMBPTM03_BL binOLMBPTM03_BL;
	
	/** 导出excel共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 会员俱乐部List */
	private List<Map<String, Object>> clubList;
	
	private String clubMod;
	
	public String getClubMod() {
		return clubMod;
	}

	public void setClubMod(String clubMod) {
		this.clubMod = clubMod;
	}

	public List<Map<String, Object>> getClubList() {
		return clubList;
	}

	public void setClubList(List<Map<String, Object>> clubList) {
		this.clubList = clubList;
	}
	
	/**
	 * 会员积分详细画面初期处理
	 * 
	 * @return 会员积分详细画面
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 俱乐部模式
		clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
				.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!"3".equals(clubMod)) {
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 取得会员俱乐部列表
			List<Map<String, Object>> clubInfoList = binOLCM05_BL.getClubList(map);
			if (null != clubInfoList && !clubInfoList.isEmpty()) {
				// 取得会员已经拥有的俱乐部列表
				clubList = binOLCM05_BL.getMemClubList(map);
				if (null == clubList || clubList.isEmpty()) {
					clubList = clubInfoList;
				} else {
					for (Map<String, Object> clubMap : clubList) {
						// 会员俱乐部ID
						String memberClubId = String.valueOf(clubMap.get("memberClubId"));
						for (int i = 0; i < clubInfoList.size(); i++) {
							Map<String, Object> clubInfo = clubInfoList.get(i);
							if (memberClubId.equals(String.valueOf(clubInfo.get("memberClubId")))) {
								clubMap.putAll(clubInfo);
								clubInfoList.remove(i);
								break;
							}
						}
					}
					clubList.addAll(clubInfoList);
				}
				// 用户ID
				map.put("userId", userInfo.getBIN_UserID());
				// 业务类型
				map.put("businessType", "2");
				// 是否带权限查询
				map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
				map.put("memberClubId", clubList.get(0).get("memberClubId"));
			}
		}
		// 查询会员积分信息
		memPointInfo = binOLMBMBM04_BL.getMemberPointInfo(map);
		// 取得积分规则信息List
		campaignNameList = binOLMBPTM02_BL.getCampaignNameList(map);
		
		String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
		form.setDisplayFlag(binOLCM14_BL.getConfigValue("1087", orgId, brandId));
		return SUCCESS;
	}
	/**
	 * 查询积分主表信息
	 * 
	 * @return 会员积分详细画面
	 */
	public String searchMemPoint() throws Exception {
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		if (!CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户ID
			map.put("userId", userInfo.getBIN_UserID());
			// 业务类型
			map.put("businessType", "2");
			// 是否带权限查询
			map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		}
		// 查询会员积分信息
		memPointInfo = binOLMBMBM04_BL.getMemberPointInfo(map);
		return SUCCESS;
	}
	/**
	 * 查询积分明细信息
	 * 
	 * @return 会员积分详细画面
	 */
	public String searchPointInfo() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		
		if(form.getChangeDateStart() != null && !"".equals(form.getChangeDateStart())) {
			map.put("changeDateStart", DateUtil.suffixDate(form.getChangeDateStart(), 0));
		}
		if(form.getChangeDateEnd() != null && !"".equals(form.getChangeDateEnd())) {
			map.put("changeDateEnd", DateUtil.suffixDate(form.getChangeDateEnd(), 1));
		}
		
		if(form.getSubCampaignId() != null && !"".equals(form.getSubCampaignId())) {
			String[] subCampaignIds = form.getSubCampaignId().split("_");
			if(subCampaignIds.length == 2) {
				map.put("subCampaignId", subCampaignIds[0]);
				map.put("pointRuleType", subCampaignIds[1]);
			} else {
				map.remove("subCampaignId");
			}
		}
		// 规则ID
		String subCampaignId = (String)map.get("subCampaignId");
		// 产品厂商ID
		String prtVendorId = (String)map.get("prtVendorId");
		// 关联退货单号
		String relevantSRCode = (String)map.get("relevantSRCode");
		if((subCampaignId != null && !"".equals(subCampaignId)) 
				|| (prtVendorId != null && !"".equals(prtVendorId))
				|| (relevantSRCode != null && !"".equals(relevantSRCode))) {
			map.put("detailCondition", "1");
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		if (!CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 用户ID
			map.put("userId", userInfo.getBIN_UserID());
			// 业务类型
			map.put("businessType", "2");
			// 是否带权限查询
			map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		}
		if(form.getDisplayFlag() != null && "0".equals(form.getDisplayFlag())) {
			// 查询积分明细信息总数
			int count = binOLMBMBM04_BL.getPointDetailCount(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				// 查询积分明细信息List
				pointDetailList = binOLMBMBM04_BL.getPointDetailList(map);
			}
			return "pointRecord";
		} else {
			// 查询积分明细信息总数
			int count = binOLMBMBM04_BL.getPointDetail2Count(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				// 查询积分明细信息List
				pointDetailList = binOLMBMBM04_BL.getPointDetail2List(map);
			}
			return "pointRecordDetail";
		}
	}
	
	/**
	 * 查询积分明细信息
	 * 
	 * @return 会员积分详细画面
	 */
	public String searchPointDetail() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 取得积分明细信息
		pointInfoMap = binOLMBPTM03_BL.getPointInfoDetail(map);
		return SUCCESS;
	}
	
	/**
     * 导出Excel
     */
    public String export() throws Exception {
        
        try {
        	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
        	
        	// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		}
    		
    		if(form.getChangeDateStart() != null && !"".equals(form.getChangeDateStart())) {
    			map.put("changeDateStart", DateUtil.suffixDate(form.getChangeDateStart(), 0));
    		}
    		if(form.getChangeDateEnd() != null && !"".equals(form.getChangeDateEnd())) {
    			map.put("changeDateEnd", DateUtil.suffixDate(form.getChangeDateEnd(), 1));
    		}
    		
    		if(form.getSubCampaignId() != null && !"".equals(form.getSubCampaignId())) {
    			String[] subCampaignIds = form.getSubCampaignId().split("_");
    			if(subCampaignIds.length == 2) {
    				map.put("subCampaignId", subCampaignIds[0]);
    				map.put("pointRuleType", subCampaignIds[1]);
    			} else {
    				map.remove("subCampaignId");
    			}
    		}
    		// 规则ID
    		String subCampaignId = (String)map.get("subCampaignId");
    		// 产品厂商ID
    		String prtVendorId = (String)map.get("prtVendorId");
    		// 关联退货单号
    		String relevantSRCode = (String)map.get("relevantSRCode");
    		if((subCampaignId != null && !"".equals(subCampaignId)) 
    				|| (prtVendorId != null && !"".equals(prtVendorId))
    				|| (relevantSRCode != null && !"".equals(relevantSRCode))) {
    			map.put("detailCondition", "1");
    		}
    		// dataTable上传的参数设置到map
    		ConvertUtil.setForm(form, map);
    		if (!CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
    			// 用户ID
    			map.put("userId", userInfo.getBIN_UserID());
    			// 业务类型
    			map.put("businessType", "2");
    			// 是否带权限查询
    			map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
    		}
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		String zipName = binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "downloadFileName");
            downloadFileName = zipName+".zip";
            map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "sheetName"));
            
            // 取得所有权限
			Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
			// 取得对应菜单下的权限
			CherryMenu doc = (CherryMenu)xmldocumentmap.get("MB");
			String saleCouFlag = null;
			if(doc != null && doc.getChildMenuByID("BINOLMBMBM10_29") != null) {
				saleCouFlag = "1";
			}
			List<String[]> titleRowList = new ArrayList<String[]>();
			titleRowList.add(new String[]{"memCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_memberCode"), "15", "", ""});
			titleRowList.add(new String[]{"proName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_proName"), "15", "", ""});
			titleRowList.add(new String[]{"unitCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_unitCode"), "15", "", ""});
			titleRowList.add(new String[]{"barCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_barCode"), "15", "", ""});
			titleRowList.add(new String[]{"saleType", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_saleType"), "15", "", "1106"});
			titleRowList.add(new String[]{"price", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_proPrice"), "15", "float", ""});
			titleRowList.add(new String[]{"quantity", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_proQuantity"), "15", "int", ""});
			titleRowList.add(new String[]{"point", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_proPoint"), "15", "", ""});
			titleRowList.add(new String[]{"pointType", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_pointType"), "15", "", "1214"});
			if(saleCouFlag != null && "1".equals(saleCouFlag)) {
				titleRowList.add(new String[]{"departCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_counterCode"), "15", "", ""});
				titleRowList.add(new String[]{"departName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_counterName"), "15", "", ""});
			}
			titleRowList.add(new String[]{"changeDate", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_changeDate"), "20", "", ""});
			titleRowList.add(new String[]{"combCampaignName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_combCampaignName"), "15", "", ""});
			titleRowList.add(new String[]{"mainCampaignName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_mainCampaignName"), "15", "", ""});
			titleRowList.add(new String[]{"subCampaignName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_subCampaignName"), "15", "", ""});
			titleRowList.add(new String[]{"reason", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_reason"), "15", "", ""});
			titleRowList.add(new String[]{"billCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_billCode"), "30", "", ""});
			titleRowList.add(new String[]{"srCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_relevantSRCode"), "30", "", ""});
			titleRowList.add(new String[]{"srTime", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_relevantSRTime"), "20", "", ""});
            map.put("titleRows", titleRowList.toArray(new String[][]{}));
     		map.put(CherryConstants.SORT_ID, "changeDate desc");
            byte[] byteArray = binOLCM37_BL.exportExcel(map, binOLMBMBM04_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("EMO00022"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	/** 会员积分信息 */
	private Map memPointInfo;
	
	/** 积分规则信息List */
	private List<Map<String, Object>> campaignNameList;
	
	/** 积分明细信息List */
	private List<Map<String, Object>> pointDetailList;
	
	/** 会员积分明细信息 */
	private Map pointInfoMap;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public Map getMemPointInfo() {
		return memPointInfo;
	}

	public void setMemPointInfo(Map memPointInfo) {
		this.memPointInfo = memPointInfo;
	}

	public List<Map<String, Object>> getCampaignNameList() {
		return campaignNameList;
	}

	public void setCampaignNameList(List<Map<String, Object>> campaignNameList) {
		this.campaignNameList = campaignNameList;
	}

	public List<Map<String, Object>> getPointDetailList() {
		return pointDetailList;
	}

	public void setPointDetailList(List<Map<String, Object>> pointDetailList) {
		this.pointDetailList = pointDetailList;
	}

	public Map getPointInfoMap() {
		return pointInfoMap;
	}

	public void setPointInfoMap(Map pointInfoMap) {
		this.pointInfoMap = pointInfoMap;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/** 会员积分详细画面Form */
	private BINOLMBMBM04_Form form = new BINOLMBMBM04_Form();

	@Override
	public BINOLMBMBM04_Form getModel() {
		return form;
	}

}
