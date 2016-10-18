package com.cherry.wp.wr.srp.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.wr.common.service.BINOLWRCOM01_Service;
import com.cherry.wp.wr.srp.form.BINOLWRSRP06_Form;
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP02_IF;
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP06_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 销售月报表Action
 * 
 * @author WangCT
 * @version 1.0 2014/10/29
 */
public class BINOLWRSRP06_Action extends BaseAction implements ModelDriven<BINOLWRSRP06_Form> {

	private static final long serialVersionUID = 2364498275971103192L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLWRSRP06_Action.class.getName());
	
	@Resource
	private BINOLWPCM01_IF binOLWPCM01_BL;
	
	@Resource
	private BINOLWRCOM01_Service binOLWRCOM01_Service;
	
	@Resource
	private BINOLWRSRP06_IF binOLWRSRP06_BL;
	
	@Resource
	private BINOLWRSRP02_IF binOLWRSRP02_BL;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;
	
	/**
	 * 销售月报表画面初始化
	 * 
	 * @return 销售月报表画面
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		// 查询柜台营业员信息
		employeeList = binOLWPCM01_BL.getBAInfoList(map);
		
		String sysDate = binOLWRCOM01_Service.getDateYMD();
		// 年
		form.setYear(sysDate.substring(0, 4));
		// 月
		form.setMonth(String.valueOf(Integer.parseInt(sysDate.substring(5, 7))));
		
		yearList = new ArrayList<String>();
		int yearLength = 10;
		int lastYear = Integer.parseInt(sysDate.substring(0, 4));
		for(int i = lastYear; i > lastYear - yearLength; i--) {
			yearList.add(String.valueOf(i));
		}
		monthList = new ArrayList<String>();
		for(int i = 1; i <= 12; i++) {
			monthList.add(String.valueOf(i));
		}
		
		return SUCCESS;
	}
	
	/**
	 * 销售月报表画面查询
	 * 
	 * @return 销售月报表画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		saleByDateList = binOLWRSRP06_BL.getSaleByDayList(map);
		saleByDayCountInfo = binOLWRSRP06_BL.getSaleByDayCountInfo(map);
		return SUCCESS;
	}
	
	/**
	 * 销售明细画面初始化
	 * 
	 * @return 销售明细画面
	 */
	public String detailInit() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		// 查询柜台营业员信息
		employeeList = binOLWPCM01_BL.getBAInfoList(map);
		
		return SUCCESS;
	}
	
	/**
	 * 查询销售明细
	 * 
	 * @return 销售明细画面
	 */
	public String searchDetail() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		map.put("startDate", form.getSaleDate());
		map.put("endDate", form.getSaleDate());
		
		int count = binOLWRSRP02_BL.getSaleRecordCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			saleCountInfo = binOLWRSRP02_BL.getSaleCountInfo(map);
			saleList = binOLWRSRP02_BL.getSaleRecordList(map);
		}
		return SUCCESS;
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
    		
    		// 柜台信息
    		CounterInfo counterInfo = (CounterInfo) session
    				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
    		map.put("organizationId", counterInfo.getOrganizationId());
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLWRSRP06_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("wp", "BINOLWRSRP06", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLWRSRP06_BL);
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
    		
    		// 柜台信息
    		CounterInfo counterInfo = (CounterInfo) session
    				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
    		map.put("organizationId", counterInfo.getOrganizationId());
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		
    		String tempFilePath = binOLWRSRP06_BL.exportCSV(map);
    		if(tempFilePath != null) {
    			msgParam.put("exportStatus", "1");
    			msgParam.put("message", getText("ECM00096"));
    			msgParam.put("tempFilePath", tempFilePath);
    		} else {
    			msgParam.put("exportStatus", "0");
    			msgParam.put("message", getText("ECM00094"));
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
		
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		map.put("startDate", form.getSaleDate());
		map.put("endDate", form.getSaleDate());
		
		int count = binOLWRSRP99_Service.getSaleRecordDetailCount(map);
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
    public String exportDetailExcel() throws Exception {
        
        try {
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
    		
    		// 柜台信息
    		CounterInfo counterInfo = (CounterInfo) session
    				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
    		map.put("organizationId", counterInfo.getOrganizationId());
    		
    		map.put("startDate", form.getSaleDate());
    		map.put("endDate", form.getSaleDate());
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "saleTime desc");
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLWRSRP02_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("wp", "BINOLWRSRP02", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLWRSRP02_BL);
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
    public String exportDetailCsv() throws Exception {
    	
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
    		
    		// 柜台信息
    		CounterInfo counterInfo = (CounterInfo) session
    				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
    		map.put("organizationId", counterInfo.getOrganizationId());
    		
    		map.put("startDate", form.getSaleDate());
    		map.put("endDate", form.getSaleDate());
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "saleTime desc");
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		msgParam.put("pageName", "detail");
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		
    		int count = binOLWRSRP99_Service.getSaleRecordDetailCount(map);
    		// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
			} else {
				String tempFilePath = binOLWRSRP02_BL.exportCSV(map);
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
	
	/** 画面条件年List **/
	private List<String> yearList;
	
	/** 画面条件月List **/
	private List<String> monthList;
	
	/** 营业员List */
	private List<Map<String, Object>> employeeList;
	
	/** 柜台每天的销售统计信息List */
	private List<Map<String, Object>> saleByDateList;
	
	/** 柜台的销售统计信息 */
	private Map saleByDayCountInfo;
	
	/** 销售信息List */
	private List<Map<String, Object>> saleList;
	
	/** 销售统计信息 */
	private Map saleCountInfo;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<String> getYearList() {
		return yearList;
	}

	public void setYearList(List<String> yearList) {
		this.yearList = yearList;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public List<Map<String, Object>> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Map<String, Object>> employeeList) {
		this.employeeList = employeeList;
	}

	public List<Map<String, Object>> getSaleByDateList() {
		return saleByDateList;
	}

	public void setSaleByDateList(List<Map<String, Object>> saleByDateList) {
		this.saleByDateList = saleByDateList;
	}

	public Map getSaleByDayCountInfo() {
		return saleByDayCountInfo;
	}

	public void setSaleByDayCountInfo(Map saleByDayCountInfo) {
		this.saleByDayCountInfo = saleByDayCountInfo;
	}

	public List<Map<String, Object>> getSaleList() {
		return saleList;
	}

	public void setSaleList(List<Map<String, Object>> saleList) {
		this.saleList = saleList;
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
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/** 销售月报表Form **/
	private BINOLWRSRP06_Form form = new BINOLWRSRP06_Form();

	@Override
	public BINOLWRSRP06_Form getModel() {
		return form;
	}

}
