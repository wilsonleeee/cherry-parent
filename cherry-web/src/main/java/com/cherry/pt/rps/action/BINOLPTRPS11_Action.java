/*	
 * @(#)BINOLPTRPS11_Action.java     1.0 2011/03/15		
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
package com.cherry.pt.rps.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.jcs.interfaces.BINOLPTJCS03_IF;
import com.cherry.pt.rps.form.BINOLPTRPS11_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS11_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 库存查询Action
 * 
 * @author lipc
 * @version 1.0 2011.03.15
 */
public class BINOLPTRPS11_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS11_Form> {

	private static final long serialVersionUID = -5113325845359666482L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS11_Action.class.getName());

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;

	@Resource(name="BINOLPTRPS11_IF")
	private BINOLPTRPS11_IF binolptrps11BL;
	
	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14bl;
	
	/** 导出excel共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 取得产品分类List */
	@Resource(name = "binOLPTJCS03_IF")
	private BINOLPTJCS03_IF binOLPTJCS03_IF;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;

	/** 参数FORM */
	private BINOLPTRPS11_Form form = new BINOLPTRPS11_Form();

	/** 库存记录List */
	private List<Map<String, Object>> proStockList;
	
	/** 产品分类List */
	private List<Map<String, Object>> cateList;
	
	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;
	
	/** 渠道List */
	private List<Map<String, Object>> channelList;
	
	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	/** 假日信息 */
	private String holidays;

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	@Override
	public BINOLPTRPS11_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getProStockList() {
		return proStockList;
	}

	public void setProStockList(List<Map<String, Object>> proStockList) {
		this.proStockList = proStockList;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExportName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,exportName);
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 开始日期
		form.setStartDate(binolcm00BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		//品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 截止日期
		form.setEndDate(CherryUtil
						.getSysDateTime(CherryConstants.DATE_PATTERN));
		//产品分类List
		cateList = binOLPTJCS03_IF.getCategoryList(map);
		// 查询假日
		holidays = binolcm00BL.getHolidays(map);
		
		// 取得渠道List
		channelList=binOLCM00_BL.getChannelList(map);
		// 库存统计方式，1：厂商编码；2：产品条码
		String type = cm14bl.getConfigValue("1095",
				userInfo.getBIN_OrganizationInfoID()+"",
				userInfo.getBIN_BrandInfoID()+"");
		form.setType(type);
		return SUCCESS;
	}

	/**
	 * <p>
	 * 库存记录查询
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return
	 * 
	 */
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得库存记录总数
//		int count = binolptrps11BL.getProStockCount(searchMap);
		// 取得汇总信息
		sumInfo = binolptrps11BL.getSumInfo(searchMap);
		// 取得库存记录总数
		int count = ConvertUtil.getInt(sumInfo.get("count"));
		// 取得库存记录List
		proStockList = binolptrps11BL.getProStockList(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLPTRPS11_1";
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		boolean isCorrect = true;
		Map<String, Object> map = getSearchMap();
		int count = 0;
		try {
			count = binolptrps11BL.getDeptStockCount(map);
		} catch(Exception e) {
			isCorrect = false;
			msgParam.put("exportStatus", "0");
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				// 系统繁忙，暂时无法导出大数据量，请尝试缩小导出的数据量或者稍后再试！
				msgParam.put("message", getText(temp.getErrCode()));
			} else {
				logger.error(e.getMessage(), e);
				// 导出失败！
				msgParam.put("message", getText("ECM00094"));
			}
		}
		if(isCorrect) {
			// Excel导出最大数据量
			int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
			}
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
	 * 导出一览明细数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		// 设置排序ID（必须）
		map.put("SORT_ID", "departCode,departName,classifyBig,classifyMid,classifySmall asc");
		// 取得库存信息List
		try {
			String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
			String extName = binOLCM37_BL.getResourceValue("BINOLPTRPS12", language, "RPS12_exportName");
			exportName = extName+ ".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binolptrps11BL.exportExcel(map), extName+".xls"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(getText(temp.getErrCode()));
			} else {
				this.addActionError(getText("EMO00022"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
	 * 导出一览概要数据
	 * @return
	 * @throws Exception
	 */
	public String exportSummary() throws Exception {
		// 取得参数MAP
		Map<String, Object> map = getSearchMap();
		String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
		try {
			exportName = binOLCM37_BL.getResourceValue("BINOLPTRPS12",
					language, "RPS12_exportSummaryName")
					+ CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
			excelStream = new ByteArrayInputStream(binolptrps11BL.exportSummaryExcel(map));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
     * 导出CSV
     */
    public String exportCsv() throws Exception {
    	
    	Map<String, Object> msgParam = new HashMap<String, Object>();
        // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    	try {
    		// 取得参数MAP
    		Map<String, Object> map = getSearchMap();
    		// 设置排序ID（必须）
    		map.put("SORT_ID", "departCode,departName,classifyBig,classifyMid,classifySmall asc");
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		map.put("charset", form.getCharset());
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		int count = binolptrps11BL.getDeptStockCount(map);
    		// CSV导出最大数据量
    		int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
    		if(count > maxCount){
    			// 明细数据量大于Excel导出最大数据量时给出提示
    			msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
    		} else {
    			String tempFilePath = binolptrps11BL.exportCSV(map);
        		if(tempFilePath != null) {
        			msgParam.put("exportStatus", "1");
        			msgParam.put("message", getText("ECM00096"));
        			msgParam.put("tempFilePath", tempFilePath);
        		} else {
        			msgParam.put("exportStatus", "0");
        			msgParam.put("message", getText("ECM00094"));
        		}
    		}
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		msgParam.put("exportStatus", "0");
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				// 系统繁忙，暂时无法导出大数据量，请尝试缩小导出的数据量或者稍后再试！
				msgParam.put("message", getText(temp.getErrCode()));
			} else {
				msgParam.put("message", getText("ECM00094"));
			}
    	}
    	JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
    	return null;
    }

	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		int orgInfoId = userInfo.getBIN_OrganizationInfoID();
		// 库存相关的日期参数设置到paramMap中
		binolcm00BL.setParamsMap(map, orgInfoId, form.getStartDate(), 
				form.getEndDate(),"Pro");
		// form中dataTable相关参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		  // 用户组织
        map.put(CherryConstants.ORGANIZATIONINFOID, orgInfoId);
        // 所属品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
		// 产品ID
		map.put(ProductConstants.PRODUCTID, form.getProductId());
		// 子品牌
		map.put("originalBrand", form.getOriginalBrand());
		// 库存统计方式
		map.put("type",form.getType());
		// 产品有效状态
		map.put(CherryConstants.VALID_FLAG,form.getValidFlag());
		
		// 大分类
		map.put("catePropValId", form.getCatePropValId());
		map.put("bigClassName", form.getBigClassName());
		// 逻辑仓库
		map.put("lgcInventoryId", form.getLgcInventoryId());
		
		map.put("excludeFlag", form.getExcludeFlag());
		map.put("channelIdClude", form.getChannelIdClude());
		
		// 共通条参数
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		
		//取得大中小分类查询条件
		String cateInfo = form.getCateInfo();
		if(!"null".equalsIgnoreCase(cateInfo) && null != cateInfo && !"".equals(cateInfo)) {
			List<Map<String, Object>> cateInfoList = (List<Map<String, Object>>) JSONUtil.deserialize(cateInfo);
			if (cateInfoList.size() > 0 && !cateInfoList.isEmpty()) {
				//大分类
				StringBuffer bigCateInfoBuffer=new StringBuffer();
				//中分类
				StringBuffer meduimCateInfoBuffer=new StringBuffer();
				//小分类
				StringBuffer samllCateInfoBuffer=new StringBuffer();
				//分类信息list
				List<String> tempList = new ArrayList<String>();
				for (Map<String, Object> cateInfoMap : cateInfoList) {
					String teminalFlag = cateInfoMap.get("teminalFlag").toString();
					List<String> cateList = (List<String>) cateInfoMap.get("propValArr");
					tempList.addAll(cateList);
					if (cateList.size() != 0) {
						if ("1".equals(teminalFlag)) {
							for (int i = 0; i < cateList.size(); i++) {
								bigCateInfoBuffer.append(cateList.get(i) + ",");
							}
							map.put("bigCateInfo", bigCateInfoBuffer.substring(0, bigCateInfoBuffer.length() - 1));
						} else if ("2".equals(teminalFlag)) {
							for (int i = 0; i < cateList.size(); i++) {
								samllCateInfoBuffer.append(cateList.get(i) + ",");
							}
							map.put("samllCateInfo",samllCateInfoBuffer.substring(0,samllCateInfoBuffer.length() - 1));
						} else if ("3".equals(teminalFlag)) {
							for (int i = 0; i < cateList.size(); i++) {
								meduimCateInfoBuffer.append(cateList.get(i) + ",");
							}
							map.put("mediumCateInfo",meduimCateInfoBuffer.substring(0,meduimCateInfoBuffer.length() - 1));
						} 
					}
				}
			}
		}
		
		map = CherryUtil.removeEmptyVal(map);
		
        // 业务日期
        String businessDate = binolcm00BL.getBussinessDate(map);
        map.put("businessDate",businessDate);
        
		return map;
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate().trim();
		// 结束日期
		String endDate = form.getEndDate().trim();
		if(CherryChecker.isNullOrEmpty(startDate)){
			// 开始日期不能为空
			this.addActionError(getText("ECM00009",new String[]{getText("PCM00001")}));
			isCorrect = false;
		}
		if(CherryChecker.isNullOrEmpty(endDate)){
			// 结束日期不能为空
			this.addActionError(getText("ECM00009",new String[]{getText("PCM00002")}));
			isCorrect = false;
		}
		
		/*开始日期验证*/
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)&& 
				endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}

	public List<Map<String, Object>> getCateList() {
		return cateList;
	}

	public void setCateList(List<Map<String, Object>> cateList) {
		this.cateList = cateList;
	}

	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
	}
	
}
