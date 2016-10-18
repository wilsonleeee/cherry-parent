package com.cherry.wp.wr.srp.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import com.cherry.wp.wr.srp.form.BINOLWRSRP01_Form;
import com.cherry.wp.wr.srp.interfaces.BINOLWRSRP01_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 业务小结Action
 * 
 * @author WangCT
 * @version 1.0 2014/09/10
 */
public class BINOLWRSRP01_Action extends BaseAction implements ModelDriven<BINOLWRSRP01_Form> {
	
	private static final long serialVersionUID = 2980643222884191278L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLWRSRP01_Action.class.getName());
	
	@Resource
	private BINOLWRSRP01_IF binOLWRSRP01_BL;
	
	@Resource
	private BINOLWPCM01_IF binOLWPCM01_BL;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;
	
	/**
	 * 业务小结画面初始化
	 * 
	 * @return 业务小结画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		// 查询柜台营业员信息
		employeeList = binOLWPCM01_BL.getBAInfoList(map);
		
		String sysDate = binOLWRSRP99_Service.getDateYMD();
		// 开始日期
		form.setSaleDateStart(sysDate);
		// 截止日期
		form.setSaleDateEnd(sysDate);
		
		return SUCCESS;
	}
	
	/**
	 * 业务小结画面查询
	 * 
	 * @return 业务小结画面
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
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		int count = binOLWRSRP01_BL.getBASaleCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			baSaleCountInfo = binOLWRSRP01_BL.getBASaleCountInfo(map);
			baSaleList = binOLWRSRP01_BL.getBASaleList(map);
		}
		return SUCCESS;
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
		
		int count = binOLWRSRP01_BL.getBASaleCount(map);
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
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "quantity desc");
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLWRSRP01_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("wp", "BINOLWRSRP01", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLWRSRP01_BL);
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
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "quantity desc");
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		
    		int count = binOLWRSRP01_BL.getBASaleCount(map);
    		// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
			} else {
				String tempFilePath = binOLWRSRP01_BL.exportCSV(map);
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
	
	/** 营业员List */
	private List<Map<String, Object>> employeeList;
	
	/** 销售信息List */
	private List<Map<String, Object>> baSaleList;
	
	/** 销售信息统计信息 */
	private Map baSaleCountInfo;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Map<String, Object>> employeeList) {
		this.employeeList = employeeList;
	}

	public List<Map<String, Object>> getBaSaleList() {
		return baSaleList;
	}

	public void setBaSaleList(List<Map<String, Object>> baSaleList) {
		this.baSaleList = baSaleList;
	}

	public Map getBaSaleCountInfo() {
		return baSaleCountInfo;
	}

	public void setBaSaleCountInfo(Map baSaleCountInfo) {
		this.baSaleCountInfo = baSaleCountInfo;
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

	/** 业务小结Form **/
	private BINOLWRSRP01_Form form = new BINOLWRSRP01_Form();

	@Override
	public BINOLWRSRP01_Form getModel() {
		return form;
	}

}
