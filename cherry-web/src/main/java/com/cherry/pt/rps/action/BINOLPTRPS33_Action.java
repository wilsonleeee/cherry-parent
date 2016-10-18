/*
 * @(#)BINOLPTRPS33_Action.java     1.0 2014/9/24
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
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.rps.form.BINOLPTRPS33_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS33_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 电商订单一览Action
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS33_Action extends BaseAction implements ModelDriven<BINOLPTRPS33_Form> {

    private static final long serialVersionUID = 5582161952782895825L;
    
    private static Logger logger = LoggerFactory.getLogger(BINOLPTRPS33_Action.class.getName());

    @Resource(name="binOLPTRPS33_BL")
    private BINOLPTRPS33_IF binOLPTRPS33_BL;
    
    @Resource(name="binOLCM00_BL")
    private BINOLCM00_BL binOLCM00_BL;
    
    @Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;
    
    /** Excel输入流 */
    private InputStream excelStream;

    /** 导出文件名 */
    private String exportName;
    
	/** 参数FORM */
	private BINOLPTRPS33_Form form = new BINOLPTRPS33_Form();

    @Override
    public BINOLPTRPS33_Form getModel() {
        return form;
    }

    public String init() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
        // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 所属组织
        map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
        // 所属品牌
        map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
        // 语言类型
        map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
        // 业务类型
        map.put(CherryConstants.BUSINESS_TYPE, "3");
        // 操作类型
        map.put(CherryConstants.OPERATION_TYPE, "1");
        String date = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
        // 开始日期
        form.setStartDate(date);
        // 截止日期
        form.setEndDate(date);
        // 查询假日
        form.setHolidays(binOLCM00_BL.getHolidays(map));
        
        return SUCCESS;
    }
    
    public String search() throws Exception{
        // 验证提交的参数
        if (!validateForm()) {
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        // 取得参数MAP
        Map<String, Object> searchMap = getSearchMap();

        Map<String,Object> sumInfo = binOLPTRPS33_BL.getSumInfo(searchMap);
        form.setSumInfo(sumInfo);
        
        // 获取销售记录总数
        int count = CherryUtil.obj2int(sumInfo.get("sum"));
        
        // 获取销售记录LIST
        form.setEsOrderMainList(binOLPTRPS33_BL.getESOrderMainList(searchMap));
        // form表单设置
        form.setITotalDisplayRecords(count);
        form.setITotalRecords(count);
        // AJAX返回至dataTable结果页面
        return "BINOLPTRPS33_1";
    }
    
    /**
     * 查询参数MAP取得
     * 
     * @return
     * @throws Exception 
     */
    private Map<String, Object> getSearchMap() throws Exception {
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
        // 单号
        map.put("billCode", form.getBillCode().trim());
        // 开始日
        map.put("startDate", form.getStartDate());
        // 结束日
        map.put("endDate", form.getEndDate());
        // 单据付款开始日
        map.put("payStartDate", form.getPayStartDate());
        // 单据付款结束日
        map.put("payEndDate", form.getPayEndDate());
        // 业务类型
        map.put("saleType", form.getSaleType());
        //消费者类型
        map.put("consumerType", form.getConsumerType());
        //会员卡号
        map.put("memCode", form.getMemCode());
        // 销售人员
        map.put("employeeCode", form.getEmployeeCode());
        // 活动类型（0：会员活动，1：促销活动）
        map.put("campaignMode", form.getCampaignMode());
        // 活动代码
        map.put("campaignCode", form.getCampaignCode());
        // 活动名称（用于显示导出报表的查询条件）
        map.put("campaignName", form.getCampaignName());
        // 支付方式
        map.put("payTypeCode", form.getPayTypeCode());
        // 流水号
        map.put("saleRecordCode", form.getSaleRecordCode());
        // 单据类型
        map.put("ticketType", form.getTicketType());
        // 单据状态
        map.put("billState", form.getBillState());
        // 是否预售
        map.put("preSale", form.getPreSale());
        // 销售商品厂商ID(id,saleType)
        if(null!=form.getPrtVendorIdList() && !"".equals(form.getPrtVendorIdList().toString())){
        	List<Map<String, Object>> list = CherryUtil.json2ArryList(form.getPrtVendorIdList().toString());
        	if(null!=list && !list.isEmpty()){
        		//存储所有的产品厂商ID及saleType
        		List<Map<String, Object>> prtVendorIdList = new ArrayList<Map<String,Object>>();
        		for(Map<String, Object> m:list){
        			Map<String, Object> saleMap=new HashMap<String, Object>();
        			saleMap.put("prtVendorId", m.get("salePrtVendorId").toString().split("_")[0]);
        			saleMap.put("saleType", m.get("salePrtVendorId").toString().split("_")[1]);
        			prtVendorIdList.add(saleMap);        			
        		}
        		map.put("prtVendorIdList", prtVendorIdList);
        	}
        }

        //销售商品对应的关联关系
        map.put("saleProPrmConcatStr", form.getSaleProPrmConcatStr());
        // 宝贝编码
        map.put("outCode", form.getOutCode());         
        // 是否是异常单
        map.put("exceptionList", form.getExceptionList());
        // 取得所属组织
        map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
        if(CherryConstants.BRAND_INFO_ID_VALUE != userInfo.getBIN_BrandInfoID()){
            map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
        }
        
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
        // map参数trim处理
        CherryUtil.trimMap(map);
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
        // 单据付款开始日期
        String payStartDate = form.getPayStartDate();
        // 单据付款结束日期
        String payEndDate = form.getPayEndDate();
        
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
        
        
        
        /*单据付款开始日期验证*/
        if (payStartDate != null && !"".equals(payStartDate)) {
            // 日期格式验证
            if(!CherryChecker.checkDate(payStartDate)) {
                this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
                isCorrect = false;
            }
        }
        /*单据付款结束日期验证*/
        if (payEndDate != null && !"".equals(payEndDate)) {
            // 日期格式验证
            if(!CherryChecker.checkDate(payEndDate)) {
                this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
                isCorrect = false;
            }
        }
        if (isCorrect && payStartDate != null && !"".equals(payStartDate)&& 
        		payEndDate != null && !"".equals(payEndDate)) {
            // 单据付款开始日期在结束日期之后
            if(CherryChecker.compareDate(payStartDate, payEndDate) > 0) {
                this.addActionError(getText("ECM00019"));
                isCorrect = false;
            }
        }
        return isCorrect;
    }
    
    /**
     * 导出Excel验证处理
     */
    public void exportCheck() throws Exception {
        Map<String, Object> msgParam = new HashMap<String, Object>();
        msgParam.put("exportStatus", "1");
        Map<String, Object> map = getSearchMap();
        
        int count = binOLPTRPS33_BL.getExportDetailCount(map);
        // Excel导出最大数据量
        int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
        if(count > maxCount) {
            msgParam.put("exportStatus", "0");
            msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
        }else if(count == 0){
            msgParam.put("exportStatus", "0");
            msgParam.put("message", getText("ECM00099"));
        }
        ConvertUtil.setResponseByAjax(response, msgParam);
    }
    
    /**
     * 导出数据
     * 
     * @return
     * @throws Exception
     */
    public String export() throws Exception {
        // 取得参数MAP
        Map<String, Object> map = getSearchMap();
        // 设置排序ID（必须）
        map.put("SORT_ID", "billCreateTime desc");
        // 取得销售记录信息List
        try {
            String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
            String extName = binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_exportName");
            exportName = extName+ ".zip";
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLPTRPS33_BL.exportExcel(map), extName+".xls"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.addActionError(getText("EMO00022"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return "BINOLPTRPS33_excel";
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
            map.put("SORT_ID", "billCreateTime desc");
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
            
            int count = binOLPTRPS33_BL.getExportDetailCount(map);
            // 导出CSV最大数据量
            int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
            
            if(count > maxCount) {
                // 明细数据量大于CSV导出最大数据量时给出提示
                msgParam.put("exportStatus", "0");
                msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
            } else {
                String tempFilePath = binOLPTRPS33_BL.exportCSV(map);
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
            msgParam.put("message", getText("ECM00094"));
        }
        JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
        return null;
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
}