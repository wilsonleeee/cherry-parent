/*  
 * @(#)BINOLPTUNQ01_Action    1.0 2016-05-26     
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

package com.cherry.pt.unq.action;

import java.util.HashMap;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.unq.form.BINOLPTUNQ01_Form;
import com.cherry.pt.unq.interfaces.BINOLPTUNQ01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 唯一码生成Action
 * 
 * @author zw
 * @version 1.0 2016.05.26
 */
public class BINOLPTUNQ01_Action extends BaseAction implements ModelDriven<BINOLPTUNQ01_Form> {

	private static final long serialVersionUID = -7948012427709370613L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLPTUNQ01_Action.class);

	/** 参数FORM */
	private BINOLPTUNQ01_Form form = new BINOLPTUNQ01_Form();
	
	/** 接口 */
	@Resource
	private BINOLPTUNQ01_IF binOLPTUNQ01_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private transient BINOLCM14_BL binOLCM14_BL;
	
	/** 导出excel共通BL **/
    @Resource
    private BINOLCM37_BL binOLCM37_BL;

	
    /** Excel输入流 */
    private transient InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
    @Resource
    private transient  BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Override
	public BINOLPTUNQ01_Form getModel() {
		return form;
	}

	
	/** 唯一码生成一览List */
	private List<Map<String, Object>> unqViewList;
	
	/** 新批次号 */
	private String newBatchNo;

	/**
	 * 唯一码生成页面初始化
	 * 
	 * @return 
	 * @throws Exception 
	 */
	public String init() throws Exception{
		
		getNewPrtUnqBatchNo();
		
		return SUCCESS;
	}
	
	public void getNewPrtUnqBatchNo()throws Exception{
		Map<String, Object> map = getSearchMap();
		newBatchNo = binOLPTUNQ01_BL.getNewPrtUniqueCodeBatchNo(map);
		ConvertUtil.setResponseByAjax(response, newBatchNo);
	}
	
	/**
	 * 查询唯一码生成信息
	 * 
	 * @param
	 * @return String
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String search() throws Exception {	
		
		Map<String, Object> map = getSearchMap();

		// 获取唯一码生成总次数
		int count = binOLPTUNQ01_BL.getUnqGenerateCount(map);
		// 获取唯一码生成列表
		unqViewList = binOLPTUNQ01_BL.getUnqViewList(map);
		if (count > 0) {
			form.setUnqGenerateList(unqViewList);
		}
		
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
       return SUCCESS;
	}
	
	
	/**
	 * 导出唯一码生成明细Excel
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String exportExcel() throws Exception {	

		// 取得参数MAP
        Map<String, Object> unqDetailsMap = getSearchMap();
        unqDetailsMap.put("prtUniqueCodeID", form.getPrtUniqueCodeID());
        // 生成批次号
        String prtUniqueCodeBatchNo=form.getPrtUniqueCodeBatchNo();
        
        // 导出唯一码明细List
        try {
        	  	
            String language = ConvertUtil.getString(unqDetailsMap.get(CherryConstants.SESSION_LANGUAGE));
            // 导出文件名称
            String zipName = binOLMOCOM01_BL.getResourceValue("BINOLPTUNQ01", language, "downloadFileName");
            downloadFileName = zipName+"("+prtUniqueCodeBatchNo+")"+".zip";
            // 设置编码，防止乱码
            downloadFileName = new String(downloadFileName.getBytes("gb2312"), "iso8859-1");
            byte[] byteArray = binOLPTUNQ01_BL.exportExcel(unqDetailsMap);
//            setExcelStream(new ByteArrayInputStream(binOLPTUNQ01_BL.exportExcel(unqDetailsMap)));
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName +"("+prtUniqueCodeBatchNo+")"+ ".xls"));

        } catch (Exception e) {
            this.addActionError(getText("EMO00022"));
            e.printStackTrace();
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return "BINOLPTUNQ01_excel";
	}
	
	
	/**
	 * 修改导出Excel此时和导出Excel时间
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String updExpExcCountAndExpExcTime() throws Exception {	

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 取得参数MAP
        Map<String, Object> unqDetailsMap = getSearchMap();
        unqDetailsMap.put("prtUniqueCodeID", form.getPrtUniqueCodeID());
        
        try {
            // 修改导出次数和最终导出时间
            binOLPTUNQ01_BL.updateExportExcelCountAndExportExcelTime(unqDetailsMap);
			resultMap.put("errorCode", "0");
			resultMap.put("successMsg", getText("ECM000120"));
        } catch (Exception e) {
        	resultMap.put("errorCode", "1");
			resultMap.put("errorMsg",  e.getMessage());
        }
        
        ConvertUtil.setResponseByAjax(response, resultMap);
        return null;
	}
	
	
	
	 /**
     * 导出CSV
     */
    public String exportCsv() throws Exception {
    	
    	Map<String, Object> msgParam = new HashMap<String, Object>();
    	try {
    		msgParam.put("exportStatus", "1");
    		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 所属品牌
    		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "createTime desc");
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		// 用户ID
    		map.put("userId", userInfo.getBIN_UserID());
    		// 业务类型
    		map.put("businessType", "2");
    		// 生成唯一码最大限制是100000
    		int count = 100000;
    		// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
			} else {
				String tempFilePath =binOLPTUNQ01_BL.exportCSV(map);
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
	
	
	
	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_EmployeeID());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_EmployeeID());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_EmployeeID());
		// 作成模块
		map.put(CherryConstants.CREATEPGM, "BINOLPTUNQ01");
		// 更新模块
		map.put(CherryConstants.UPDATEPGM, "BINOLPTUNQ01");
	
		
		return map;
	}

	public List<Map<String, Object>> getUnqViewList() {
		return unqViewList;
	}

	public void setUnqViewList(List<Map<String, Object>> unqViewList) {
		this.unqViewList = unqViewList;
	}
	
	public String getNewBatchNo() {
		return newBatchNo;
	}

	public void setNewBatchNo(String newBatchNo) {
		this.newBatchNo = newBatchNo;
	}
	

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/* ************************************************************************************************************* */
	
	/**
	 * 生成唯一码
	 * 生成箱码、积分唯一码、关联唯一码
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String generateUnqCode() throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, Object> map = getSearchMap();
		
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		
		//validFlag=0&productId=40675&updateTime=&modifyCount=&csrftoken=581dad2be3aa42e390d61675fc27be57
		try{
			map.put("productVendorID", map.get("prtVendorId"));
//			binOlCNBAS12_IF.tran_delCounterMallAct(map);
			resultMap = binOLPTUNQ01_BL.tran_GenerateUnqCode(map);
			resultMap.put("errorCode", "0");
			resultMap.put("successMsg", getText("ECM000120"));
		}catch(Exception e){

			if(e instanceof CherryException) {
				CherryException temp = (CherryException)e;
				resultMap.put("errorCode", "2");
				resultMap.put("errorMsg", getText(temp.getErrCode()));
			} else {
				resultMap.put("errorCode", "1");
				resultMap.put("errorMsg",  e.getMessage());
			}

		}
		
		ConvertUtil.setResponseByAjax(response, resultMap);
		
		return null;
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	private boolean validateForm() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 验证结果
		boolean isCorrect = true;
		// 规格
		if (CherryChecker.isNullOrEmpty(form.getSpec())) {
			this.addFieldError("spec", getText("ECM00009",
					new String[] { getText("规格") }));
			isCorrect = false;
		}
		// 箱数
		if (CherryChecker.isNullOrEmpty(form.getBoxCount())) {
			this.addFieldError("boxCount", getText("ECM00009",
					new String[] { getText("箱数") }));
			isCorrect = false;
		}
		// 生成总数
		if (CherryChecker.isNullOrEmpty(form.getGenerateCountVal())) {
			this.addFieldError("generateCountVal", getText("ECM00009",
					new String[] { getText("唯一码数量") }));
			isCorrect = false;
		} else if (ConvertUtil.getFloat(form.getGenerateCountVal()) > 100000){
			this.addFieldError("generateCountVal", getText("ECM00052",
					new String[] { getText("唯一码数量"),"100000" }));
			isCorrect = false;
		}
		
		// 生成方式
		if (CherryChecker.isNullOrEmpty(form.getGenerateType())) {
			this.addFieldError("generateType", getText("ECM00009",
					new String[] { getText("生成方式") }));
			isCorrect = false;
		}
		// 是否需要箱码
		if (CherryChecker.isNullOrEmpty(form.getNeedBoxCode())) {
			this.addFieldError("needBoxCode", getText("ECM00009",
					new String[] { getText("是否需要箱码") }));
			isCorrect = false;
		}
		// defaultActivationStatus
		if (CherryChecker.isNullOrEmpty(form.getDefaultActivationStatus())) {
			this.addFieldError("defaultActivationStatus", getText("ECM00009",
					new String[] { getText("默认激活方式") }));
			isCorrect = false;
		}
		
		// 是否强制依据产品生成唯一码
		boolean isRelyOn = binOLCM14_BL.isConfigOpen("1356", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		
		// 产品
		if (isRelyOn && CherryChecker.isNullOrEmpty(form.getPrtVendorId())) {
			this.addFieldError("prtVendorId", getText("ECM00009",
					new String[] { getText("产品") }));
			isCorrect = false;
		}
		
		// 激活状态为激活必须对应产品，产品不能为空。
		if (CherryChecker.isNullOrEmpty(form.getPrtVendorId()) && "1".equals(form.getDefaultActivationStatus())) {
			this.addFieldError("prtVendorId", getText("UNQ00012"));
			isCorrect = false;
		}
		
		return isCorrect;
	}
}
