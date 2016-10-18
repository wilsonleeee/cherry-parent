package com.cherry.wp.wr.mrp.action;

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
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.wr.mrp.form.BINOLWRMRP05_Form;
import com.cherry.wp.wr.mrp.interfaces.BINOLWRMRP05_IF;
import com.cherry.wp.wr.srp.service.BINOLWRSRP99_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 有销售无会员资料Action
 * 
 * @author WangCT
 * @version 1.0 2014/10/30
 */
public class BINOLWRMRP05_Action extends BaseAction implements ModelDriven<BINOLWRMRP05_Form> {

	private static final long serialVersionUID = 4725577200691953048L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLWRMRP05_Action.class.getName());
	
	@Resource
	private BINOLWPCM01_IF binOLWPCM01_BL;
	
	@Resource
	private BINOLWRMRP05_IF binOLWRMRP05_BL;
	
	@Resource
	private BINOLWRSRP99_Service binOLWRSRP99_Service;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/**
	 * 有销售无会员资料画面初始化
	 * 
	 * @return 有销售无会员资料画面
	 */
	public String init() {
		
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
		
		String sysDate = binOLWRSRP99_Service.getDateYMD();
		// 开始日期
		form.setStartDate(DateUtil.addDateByMonth("yyyy-MM-dd", sysDate, -1));
		// 截止日期
		form.setEndDate(sysDate);
		
		return SUCCESS;
	}
	
	/**
	 * 有销售无会员资料查询
	 * 
	 * @return 有销售无会员资料画面
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
		
		memSaleCountInfo = binOLWRMRP05_BL.getMemSaleCountInfo(map);
		int count = 0;
		if(memSaleCountInfo != null) {
			count = (Integer)memSaleCountInfo.get("count");
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			memSaleList = binOLWRMRP05_BL.getMemSaleList(map);
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
		
		int count = binOLWRMRP05_BL.getMemSaleCount(map);
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
    		map.put(CherryConstants.SORT_ID, "saleTime desc");
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLWRMRP05_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("wp", "BINOLWRMRP05", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLWRMRP05_BL);
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
    		map.put(CherryConstants.SORT_ID, "saleTime desc");
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		
    		int count = binOLWRMRP05_BL.getMemSaleCount(map);
    		// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
			} else {
				String tempFilePath = binOLWRMRP05_BL.exportCSV(map);
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
	
	/** 无资料会员的销售统计信息 */
	private Map memSaleCountInfo;
	
	/** 无资料会员的销售信息List */
	private List<Map<String, Object>> memSaleList;
	
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

	public Map getMemSaleCountInfo() {
		return memSaleCountInfo;
	}

	public void setMemSaleCountInfo(Map memSaleCountInfo) {
		this.memSaleCountInfo = memSaleCountInfo;
	}

	public List<Map<String, Object>> getMemSaleList() {
		return memSaleList;
	}

	public void setMemSaleList(List<Map<String, Object>> memSaleList) {
		this.memSaleList = memSaleList;
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

	/** 有销售无会员资料Form **/
	private BINOLWRMRP05_Form form = new BINOLWRMRP05_Form();

	@Override
	public BINOLWRMRP05_Form getModel() {
		return form;
	}

}
