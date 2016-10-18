/*	
 * @(#)BINOLPTRPS25_Action.java     1.0.0 2011/10/17		
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.common.ProductConstants;
import com.cherry.pt.rps.form.BINOLPTRPS25_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS25_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 产品在途库存查询Action
 * 
 * @author lipc
 * @version 1.0.0 2011.10.17
 * 
 */
public class BINOLPTRPS25_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS25_Form> {

	private static final long serialVersionUID = 5779285107926696629L;
	
    private static final Logger logger = LoggerFactory.getLogger(BINOLPTRPS25_Action.class);

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource(name="binOLPTRPS25_BL")
	private BINOLPTRPS25_IF binolptrps25BL;
	
    @Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;

	/** 参数FORM */
	private BINOLPTRPS25_Form form = new BINOLPTRPS25_Form();

	/** 在途库存记录List */
	private List<Map<String, Object>> recordList;

	/** 汇总信息 */
	private Map<String, Object> sumInfo;

	/** 假日信息 */
	private String holidays;
	
    /** Excel输入流 */
    private InputStream excelStream;
    
    /** 下载文件名 */
    private String downloadFileName;

	public List<Map<String, Object>> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<Map<String, Object>> recordList) {
		this.recordList = recordList;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	@Override
	public BINOLPTRPS25_Form getModel() {
		return form;
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
		// 所属组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "1");
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, "1");
		// 开始日期
		form.setStartDate(binolcm00BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil
						.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 查询假日
		holidays = binolcm00BL.getHolidays(map);
		return SUCCESS;
	}

	/**
	 * <p>
	 * 记录查询
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
			// 取得记录List
		int count = binolptrps25BL.getPrtDeliverCount(searchMap);
//			int count = 0;
			// ================= LuoHong修改：显示统计信息 ================== //
			// 产品厂商ID
//			String prtVendorId = ConvertUtil.getString(searchMap
//					.get(ProductConstants.PRT_VENDORID));
//			if (!CherryConstants.BLANK.equals(prtVendorId)
//					|| !CherryChecker.isNullOrEmpty(form.getNameTotal(), true)) {
//				sumInfo = binolptrps25BL.getSumInfo(searchMap);
//				count = CherryUtil.obj2int(sumInfo.get("count"));
//			}else{
//				count = binolptrps25BL.getPrtDeliverCount(searchMap);
//			}
			// 取得记录List
			recordList = binolptrps25BL.getPrtDeliverList(searchMap);
			sumInfo = binolptrps25BL.getSumInfo(searchMap);
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);

		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}

    /**
     * 查询结果Excel导出
     * @return
     */
    public String export() {
        // 取得参数MAP 
        try {
            Map<String, Object> searchMap = getSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLPTRPS25", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            excelStream=new ByteArrayInputStream(binOLCM37_BL.fileCompression(binolptrps25BL.exportExcel(searchMap), fileName+".xls"));
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            if(e instanceof CherryException){
                this.addActionError(((CherryException)e).getErrMessage());
            }else{
                this.addActionError(getText("EMO00022"));
            }
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
    }
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws JSONException 
	 */
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 单号
		map.put("deliverNoIF", form.getDeliverNoIF());
		// 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
		 //部门
        map.put("inOrganizationId", form.getInOrganizationId());
		
		Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
		map.putAll(paramsMap);
		map = CherryUtil.removeEmptyVal(map);
		//部门联动条标记
        map.put("linkageDepartFlag", linkageDepartFlag(map));
        
		return map;
	}
	
	/**
	 * 判断部门联动条查询参数是否为空
	 * 
	 * @return "0":查询参数为空  "1":查询参数不为空
	 */
	private String linkageDepartFlag(Map<String, Object> map) {
		String flag = "0";
		if (map.containsKey("departId") || map.containsKey("channelId")
				|| map.containsKey("departType") || map.containsKey("regionId")
				|| map.containsKey("provinceId") || map.containsKey("cityId")
				|| map.containsKey("countyId")) {
			// 查询参数不为空
			flag = "1";
		}
		return flag;
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
		/* 开始日期验证 */
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00001") }));
				isCorrect = false;
			}
		}
		/* 结束日期验证 */
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if (!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008",
						new String[] { getText("PCM00002") }));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)
				&& endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if (CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getDownloadFileName() throws UnsupportedEncodingException {
        return FileUtil.encodeFileName(request,downloadFileName);
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }
}
