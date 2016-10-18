package com.cherry.bs.sam.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.sam.form.BINOLBSSAM03_Form;
import com.cherry.bs.sam.interfaces.BINOLBSSAM03_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLBSSAM03_Action extends BaseAction implements
ModelDriven<BINOLBSSAM03_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5944327304976986231L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLBSSAM03_Action.class);
	
	@Resource
	private BINOLBSSAM03_IF binOLBSSAM03_IF;
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	/** 参数FORM */
	private BINOLBSSAM03_Form form = new BINOLBSSAM03_Form();
	@Override
	public BINOLBSSAM03_Form getModel() {
		return form;
	}

	public String init(){
		return SUCCESS;
	}
	
	/**
	 * 业务小结画面查询
	 * 
	 * @return 业务小结画面
	 */
	@SuppressWarnings("unchecked")
	public String search() throws Exception {
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String organizationId = ConvertUtil.getString(userInfo.getBIN_OrganizationID());
		Map<String, Object> map = (Map<String,Object>) Bean2Map.toHashMap(form);
		// 登陆用户信息
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		map.put(CherryConstants.ORGANIZATIONID, organizationId);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 查询数据的条数
		int count = binOLBSSAM03_IF.getPayrollCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (count != 0) {
			List<Map<String,Object>> payrollList = binOLBSSAM03_IF.getPayrollList(map);
			form.setPayrollList(payrollList);
		}
		return SUCCESS;
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
						String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
						String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		
		
		int count = binOLBSSAM03_IF.getPayrollCount(map);
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
        	// 登陆用户信息
        	UserInfo userInfo = (UserInfo) session
        			.get(CherryConstants.SESSION_USERINFO);
        	Map<String, Object> msgParam = new HashMap<String, Object>();
    		msgParam.put("exportStatus", "1");
    		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
    		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
    		@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
    		// 所属品牌
    		map.put(CherryConstants.BRANDINFOID, brandInfoId);
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "departName desc");
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLBSSAM03_IF.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("bs", "BINOLBSSAM03", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLBSSAM03_IF);
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
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
    		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
    		msgParam.put("exportStatus", "1");
    		@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String,Object>)Bean2Map.toHashMap(form);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
    		// 所属品牌
    		map.put(CherryConstants.BRANDINFOID, brandInfoId);
    		
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "departName desc");
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		
    		int count = binOLBSSAM03_IF.getPayrollCount(map);
    		// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
			} else {
				String tempFilePath = binOLBSSAM03_IF.exportCSV(map);
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
    
    
    /** 下载文件名 */
    private String downloadFileName;
    /** Excel输入流 */
    private InputStream excelStream;
    
	public String getDownloadFileName() throws Exception {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
}
