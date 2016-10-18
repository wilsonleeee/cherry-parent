/*  
 * @(#)BINOLSTBIL15_Action.java     1.0 2012/8/23      
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
package com.cherry.st.bil.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
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
import com.cherry.st.bil.form.BINOLSTBIL15_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL15_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 产品盘点申请单一览Action
 * @author niushunjie
 * @version 1.0 2012.8.23
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL15_Action extends BaseAction implements
ModelDriven<BINOLSTBIL15_Form>{

    private static final long serialVersionUID = -1493368472276370527L;
    
    /** 异常处理 */
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLSTBIL15_Action.class);

    /** 参数FORM */
    private BINOLSTBIL15_Form form = new BINOLSTBIL15_Form();

    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource
    private BINOLCM37_BL binOLCM37_BL;

    @Resource(name="binOLSTBIL15_BL")
    private BINOLSTBIL15_IF binOLSTBIL15_BL;

    @Override
    public BINOLSTBIL15_Form getModel() {
        return form;
    }
    
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;

    public String init() throws JSONException {
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 开始日期
        form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
        // 截止日期
        form.setEndDate(CherryUtil
                .getSysDateTime(CherryConstants.DATE_PATTERN));
        return SUCCESS;
    }

    /**
     * <p>
     * 盘点申请单查询
     * </p>
     * 
     * @return
     */
    public String search() throws Exception {
        // 验证提交的参数
        if (!validateForm()) {
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();
        // 取得盘点申请单总数
        int count = binOLSTBIL15_BL.searchProStocktakeRequestCount(searchMap);
        if (count > 0) {
            // 取得盘点申请单List
            form.setProStocktakeRequestList(binOLSTBIL15_BL.searchProStocktakeRequestList(searchMap));
        }
        form.setSumInfo(binOLSTBIL15_BL.getSumInfo(searchMap));
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLSTBIL15_1";
    }
    
    /**
	 * 盘点申请单一览明细excel导出
	 * @return
	 */
	public String export() {
		// 取得参数MAP 
        try {
        	Map<String, Object> searchMap = getSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTBIL15", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            excelStream=new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLSTBIL15_BL.exportExcel(searchMap), fileName+".xls"));
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
     * @param tableParamsDTO
     * @throws JSONException 
     */
    private Map<String, Object> getSearchMap() throws JSONException {
        // 参数MAP
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // form参数设置到paramMap中
        ConvertUtil.setForm(form, map);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 所属品牌
        map.put("brandInfoId",userInfo.getBIN_BrandInfoID());
        // 退库单号
        map.put("stockTakingNo", form.getStockTakingNo().trim());
        // 开始日期
        map.put("startDate", form.getStartDate());
        // 结束日期
        map.put("endDate", form.getEndDate());
        // 审核状态
        map.put("verifiedFlag", form.getVerifiedFlag());
        // 产品厂商ID
        map.put("prtVendorId", form.getPrtVendorId());
        //业务类型
        map.put("tradeType", form.getTradeType());
        // 实体仓库
        map.put("depotId", form.getDepotId());
        // 产品名称
        map.put("nameTotal", null == form.getNameTotal() ? "" : form.getNameTotal().trim());
        // 员工ID
        map.put("employeeId", form.getEmployeeId());

        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
       
        return map;
    }

    /**
     * 验证提交的参数
     * 
     * @param 无
     * @return boolean
     *          验证结果
     * 
     */
    private boolean validateForm() {
        boolean isCorrect = true;
        // 开始日期
        String startDate = form.getStartDate();
        // 结束日期
        String endDate = form.getEndDate();
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