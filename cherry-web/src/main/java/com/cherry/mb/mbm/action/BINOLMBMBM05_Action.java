/*
 * @(#)BINOLMBMBM05_Action.java     1.0 2012.10.10
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM05_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM05_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员销售详细画面Action
 * 
 * @author WangCT
 * @version 1.0 2012.10.10
 */
public class BINOLMBMBM05_Action extends BaseAction implements ModelDriven<BINOLMBMBM05_Form> {

	private static final long serialVersionUID = -8845394727486038056L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBMBM05_Action.class.getName());
	
	/** 会员销售详细画面BL */
	@Resource(name="binOLMBMBM05_BL")
	private BINOLMBMBM05_BL binOLMBMBM05_BL;
	
	/** 导出excel共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
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
	 * 会员购买记录画面初期处理
	 * 
	 * @return 会员购买记录画面
	 */
	public String init() throws Exception {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);	
		String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
		form.setDisplayFlag(binOLCM14_BL.getConfigValue("1087", orgId, brandId));
		
	    //系统配置项是否显示唯一码
        form.setSysConfigShowUniqueCode(binOLCM14_BL.getConfigValue("1140", orgId, brandId));
        // 俱乐部模式
 		clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
 				.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
 		if (!"3".equals(clubMod)) {
 			Map<String, Object> map = new HashMap<String, Object>();
 			// 语言类型
 			map.put(CherryConstants.SESSION_LANGUAGE, session
 					.get(CherryConstants.SESSION_LANGUAGE));
 			// 所属组织
 			map.put(CherryConstants.ORGANIZATIONINFOID, orgId);
 			// 所属品牌
 			map.put(CherryConstants.BRANDINFOID, brandId);
 			// 取得会员俱乐部列表
 			List<Map<String, Object>> clubInfoList = binOLCM05_BL.getClubList(map);
 			if (null != clubInfoList && !clubInfoList.isEmpty()) {
 				map.put("memberInfoId", form.getMemberInfoId());
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
 			}
 		}
		return SUCCESS;
	}
	
	/**
	 * 查询会员销售信息List
	 * 
	 * @return 会员详细画面
	 */
	public String searchSaleRecord() throws Exception {
	    // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);   
        String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
        //系统配置项是否显示唯一码
        form.setSysConfigShowUniqueCode(binOLCM14_BL.getConfigValue("1140", orgId, brandId));

		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
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
		if(form.getDisplayFlag() != null && "0".equals(form.getDisplayFlag())) {
			// 统计会员销售信息
			saleCountInfo = binOLMBMBM05_BL.getSaleCount(map);
			if(saleCountInfo != null && !saleCountInfo.isEmpty()) {
				int count = (Integer)saleCountInfo.get("count");
				form.setITotalDisplayRecords(count);
				form.setITotalRecords(count);
				if(count != 0) {
					// 查询会员销售信息List
					saleRecordList = binOLMBMBM05_BL.getSaleRecordList(map);
				}
			}
			return "SaleRecord";
		} else {
			// 统计会员销售信息
			saleCountInfo = binOLMBMBM05_BL.getSaleCount(map);
			// 查询会员销售明细总数
			int count = binOLMBMBM05_BL.getSaleDetailCount(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				// 查询会员销售明细List
				saleRecordList = binOLMBMBM05_BL.getSaleDetailList(map);
			}
			return "SaleRecordDetail";
		}
	}
	
	/**
	 * 查询会员销售明细信息
	 * 
	 * @return 会员详细画面
	 */
	public String searchSaleDetail() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 查询会员销售明细信息
		saleRecordDetail = binOLMBMBM05_BL.getSaleRecordDetail(map);
		
	    //系统配置项是否显示唯一码
		String orgId = ConvertUtil.getString(saleRecordDetail.get("organizationInfoId"));
        String brandId = ConvertUtil.getString(saleRecordDetail.get("brandInfoId"));
        form.setSysConfigShowUniqueCode(binOLCM14_BL.getConfigValue("1140", orgId, brandId));
		
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
    		// dataTable上传的参数设置到map
    		ConvertUtil.setForm(form, map);
        	
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		if (!CherryChecker.isNullOrEmpty(map.get("memberClubId"))) {
    			// 用户ID
    			map.put("userId", userInfo.getBIN_UserID());
    			// 业务类型
    			map.put("businessType", "2");
    			// 是否带权限查询
    			map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
    		}
    		// 统计会员销售信息
    		saleCountInfo = binOLMBMBM05_BL.getSaleCount(map);
    		if(saleCountInfo != null && !saleCountInfo.isEmpty()) {
    			String quantity = new DecimalFormat("0").format(saleCountInfo.get("quantity"));
        		String amount = new DecimalFormat("0.00").format(saleCountInfo.get("amount"));
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_sumQuantity")+"："+quantity);
        		strBuf.append("\0\0\0\0");
        		strBuf.append(binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_sumAmount")+"："+amount);
        		map.put("header", strBuf.toString());
    		}
    		
    		String zipName = binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "downloadFileName");
            downloadFileName = zipName+".zip";
            map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "sheetName"));
            
            // 取得所有权限
			Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
			// 取得对应菜单下的权限
			CherryMenu doc = (CherryMenu)xmldocumentmap.get("MB");
			String saleCouFlag = null;
			if(doc != null && doc.getChildMenuByID("BINOLMBMBM10_29") != null) {
				saleCouFlag = "1";
			}
			List<String[]> titleRowList = new ArrayList<String[]>();
			titleRowList.add(new String[]{"memberCode", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_saleMemCode"), "15", "", ""});
			titleRowList.add(new String[]{"prtName", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_proName"), "15", "", ""});
			titleRowList.add(new String[]{"unitCode", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_unitCode"), "15", "", ""});
			titleRowList.add(new String[]{"barCode", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_barCode"), "15", "", ""});
			titleRowList.add(new String[]{"saleType", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_saleType"), "15", "", "1055"});
			titleRowList.add(new String[]{"detailSaleType", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_detailSaleType"), "15", "", "1106"});
			titleRowList.add(new String[]{"quantity", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_quantity"), "15", "int", ""});
			titleRowList.add(new String[]{"pricePay", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_pricePay"), "15", "float", ""});
			titleRowList.add(new String[]{"billCode", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_billCode"), "30", "", ""});
			if(saleCouFlag != null && "1".equals(saleCouFlag)) {
				titleRowList.add(new String[]{"departCode", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_counterCode"), "15", "", ""});
				titleRowList.add(new String[]{"departName", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_counterName"), "15", "", ""});
			}
			titleRowList.add(new String[]{"employeeCode", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_employeeCode"), "15", "", ""});
			titleRowList.add(new String[]{"employeeName", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_employeeName"), "15", "", ""});
			titleRowList.add(new String[]{"saleTime", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_saleTime"), "20", "", ""});
			titleRowList.add(new String[]{"activityName", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_campaignName"), "15", "", ""});
			titleRowList.add(new String[]{"saleExt", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_saleExt"), "15", "", ""});
            
            String organizationInfoID = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
            String brandInfoID = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
            String configValue = binOLCM14_BL.getConfigValue("1140", organizationInfoID, brandInfoID);
            if(configValue.equals(CherryConstants.SYSTEM_CONFIG_ENABLE)){
                //导出数据列数组（有唯一码）
                titleRowList.add(new String[]{ "uniqueCode", binOLCM37_BL.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_UniqueCode"), "25", "", "" });
            }
            map.put("titleRows",titleRowList.toArray(new String[][]{}));
            
     		map.put(CherryConstants.SORT_ID, "saleTime desc");
            byte[] byteArray = binOLCM37_BL.exportExcel(map, binOLMBMBM05_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("EMO00022"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	/** 会员销售信息List */
	private List<Map<String, Object>> saleRecordList;
	
	/** 会员销售明细信息 */
	private Map saleRecordDetail;
	
	/** 会员销售统计信息 */
	private Map saleCountInfo;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getSaleRecordList() {
		return saleRecordList;
	}

	public void setSaleRecordList(List<Map<String, Object>> saleRecordList) {
		this.saleRecordList = saleRecordList;
	}

	public Map getSaleRecordDetail() {
		return saleRecordDetail;
	}

	public void setSaleRecordDetail(Map saleRecordDetail) {
		this.saleRecordDetail = saleRecordDetail;
	}

	public Map getSaleCountInfo() {
		return saleCountInfo;
	}

	public void setSaleCountInfo(Map saleCountInfo) {
		this.saleCountInfo = saleCountInfo;
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

	/** 会员销售详细画面Form */
	private BINOLMBMBM05_Form form = new BINOLMBMBM05_Form();

	@Override
	public BINOLMBMBM05_Form getModel() {
		return form;
	}

}
