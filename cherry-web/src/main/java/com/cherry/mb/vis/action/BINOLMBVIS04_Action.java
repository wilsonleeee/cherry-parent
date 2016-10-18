package com.cherry.mb.vis.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.vis.bl.BINOLMBVIS02_BL;
import com.cherry.mb.vis.bl.BINOLMBVIS04_BL;
import com.cherry.mb.vis.form.BINOLMBVIS04_Form;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;


public class BINOLMBVIS04_Action extends BaseAction implements ModelDriven<BINOLMBVIS04_Form> {
	
	private static final long serialVersionUID = 7586285408977225687L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLMBVIS04_Action.class);
	
	@Resource
	private BINOLMBVIS04_BL binOLMBVIS04_BL;
	
	@Resource
	private BINOLMBVIS02_BL binOLMBVIS02_BL;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/**
	 * 会员回访任务画面初期处理
	 * 
	 * @return 会员回访任务画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 取得会员回访类型List
		visitCategoryList = binOLMBVIS02_BL.getVisitCategoryList(map);
		
		monthList = new ArrayList<String>();
		for(int i = 1; i <= 12; i++) {
			if(i >= 10){
				monthList.add(""+i);	
			}else{
				monthList.add("0"+i);
			}	
		}
		dateList = new ArrayList<String>();
		for(int i = 1; i <= 31; i++) {
			if(i >= 10){
				dateList.add(""+i);	
			}else{
				dateList.add("0"+i);
			}			
		}
		
		return SUCCESS;
	}

	/**
	 * AJAX查询回访任务
	 * 
	 * @return 会员回访任务画面
	 */
	public String search() throws Exception {

		Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		setCommParams(map);
		// 取得回访信息总数
		int count = binOLMBVIS04_BL.getVisitTaskCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			// 取得回访信息List
			visitTaskList = binOLMBVIS04_BL.getVisitTaskList(map);
		}

		return SUCCESS;
	}
	
	private void setCommParams(Map<String, Object> map) {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
	}
	
	/**
	 * 取消回访任务处理
	 * 
	 * @return 会员回访任务画面
	 */
	public void cancel() throws Exception {
		String code = "ok";
		String errorMes = "";
		try{
			Map<String, Object> map = (Map) Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			binOLMBVIS04_BL.updateVisitTaskState(map);
		} catch (Exception e) {
			code = "error";
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				errorMes = ((CherryException)e).getErrMessage();
			}else{
				//系统发生异常，请联系管理人员。
				errorMes = getText("ECM00036");
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", code);
		resultMap.put("errorMes", errorMes);
		ConvertUtil.setResponseByAjax(response, resultMap);
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		setCommParams(map);
		int count = binOLMBVIS04_BL.getVisitTaskCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
     * 导出Excel
     */
    public String exportExcel() throws Exception {
        
        try {
    		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 所属品牌
    		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "visitTaskId desc");
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		setCommParams(map);
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBVIS04_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("mb", "BINOLMBVIS04", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBVIS04_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
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
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 所属品牌
    		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "visitTaskId desc");
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		setCommParams(map);
    		int count = binOLMBVIS04_BL.getVisitTaskCount(map);
    		// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
			} else {
				String tempFilePath = binOLMBVIS04_BL.exportCSV(map);
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
	
	private List<Map<String, Object>> visitCategoryList;
	
	/** 月信息List */
	private List<String> monthList;
	
	/** 日信息List */
	private List<String> dateList;
	
	private List<Map<String, Object>> visitTaskList;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getVisitCategoryList() {
		return visitCategoryList;
	}

	public void setVisitCategoryList(List<Map<String, Object>> visitCategoryList) {
		this.visitCategoryList = visitCategoryList;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}

	public List<Map<String, Object>> getVisitTaskList() {
		return visitTaskList;
	}

	public void setVisitTaskList(List<Map<String, Object>> visitTaskList) {
		this.visitTaskList = visitTaskList;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	private BINOLMBVIS04_Form form = new BINOLMBVIS04_Form();

	@Override
	public BINOLMBVIS04_Form getModel() {
		return form;
	}
	
	

}
