package com.cherry.wp.wr.krp.action;

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
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.wp.wr.common.service.BINOLWRCOM01_Service;
import com.cherry.wp.wr.krp.form.BINOLWRKRP01_Form;
import com.cherry.wp.wr.krp.interfaces.BINOLWRKRP01_IF;
import com.cherry.wp.wr.krp.service.BINOLWRKRP99_Service;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 库存报表Action
 * 
 * @author WangCT
 * @version 1.0 2014/09/16
 */
public class BINOLWRKRP01_Action extends BaseAction implements ModelDriven<BINOLWRKRP01_Form> {

	private static final long serialVersionUID = 3086324861907330631L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLWRKRP01_Action.class.getName());
	
	@Resource
	private BINOLWRCOM01_Service binOLWRCOM01_Service;
	
	@Resource
	private BINOLWRKRP01_IF binOLWRKRP01_BL;
	
	/** 导出共通BL **/
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource
	private BINOLWRKRP99_Service binOLWRKRP99_Service;
	
	/**
	 * 库存报表画面初始化
	 * 
	 * @return 库存报表画面
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
		
		logicInventoryList = binOLWRCOM01_Service.getLogicInventoryList(map);
		
		List<Map<String, Object>> categoryList = binOLWRCOM01_Service.getCategoryList(map);
		classList = new ArrayList<Map<String,Object>>();
		List<String[]> keyList = new ArrayList<String[]>();
		String[] key1 = {"bigClassId", "bigClassCode", "bigClassName"};
		keyList.add(key1);
		ConvertUtil.convertList2DeepList(categoryList,classList,keyList,0);
		
		classJson = JSONUtil.serialize(classList);
		
		Map<String, Object> couInfo = binOLWRKRP99_Service.getCouInfoByCouId(map);
		if(couInfo != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("businessType", "1");
			params.put("operationType", "1");
			params.put("departId", counterInfo.getOrganizationId());
			Integer counterKind = (Integer)couInfo.get("counterKind");
			if(counterKind != null && counterKind == 1) {
				params.put("testType", counterKind);
			}
			form.setParams(JSONUtil.serialize(params));
		}
		
		return SUCCESS;
	}
	
	/**
	 * 库存报表画面查询
	 * 
	 * @return 库存报表画面
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
		
		map.put(CherryConstants.SORT_ID, "bigClassId, smallClassId");
		
		int count = binOLWRKRP01_BL.getProStockCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			proStockCountInfo = binOLWRKRP01_BL.getProStockCountInfo(map);
			proStockList = binOLWRKRP01_BL.getProStockList(map);
		}
		String sysDate = binOLWRCOM01_Service.getDateYMD();
		form.setStartDate(DateUtil.getFirstOrLastDateYMD(sysDate, 0));
		form.setEndDate(sysDate);
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
		
		int count = binOLWRKRP01_BL.getProStockCount(map);
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
    		map.put(CherryConstants.SORT_ID, "bigClassId, smallClassId");
    		
    		// 语言
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, language);
    		
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLWRKRP01_BL.getExportParam(map);
    		
    		String zipName = CherryUtil.getResourceValue("wp", "BINOLWRKRP01", language, "downloadFileName");
    		downloadFileName = zipName+".zip";
            
    		// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLWRKRP01_BL);
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
    		map.put(CherryConstants.SORT_ID, "bigClassId, smallClassId");
    		
    		msgParam.put("TradeType", "exportMsg");
    		msgParam.put("SessionID", userInfo.getSessionID());
    		msgParam.put("LoginName", userInfo.getLoginName());
    		msgParam.put("OrgCode", userInfo.getOrgCode());
    		msgParam.put("BrandCode", userInfo.getBrandCode());
    		
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		
    		int count = binOLWRKRP01_BL.getProStockCount(map);
    		// CSV导出最大数据量
			int maxCount = CherryConstants.EXPORTCSV_MAXCOUNT;
			if(count > maxCount) {
				msgParam.put("exportStatus", "0");
				msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportCsv"), String.valueOf(maxCount)}));
			} else {
				String tempFilePath = binOLWRKRP01_BL.exportCSV(map);
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
	
	/** 产品分类List */
	private List<Map<String, Object>> classList;
	
	/** 产品分类Json */
	private String classJson;
	
	/** 逻辑仓库List */
	private List<Map<String, Object>> logicInventoryList;
	
	/** 产品库存List **/
	private List<Map<String, Object>> proStockList;
	
	/** 产品库存统计信息 **/
	private Map proStockCountInfo;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getClassList() {
		return classList;
	}

	public void setClassList(List<Map<String, Object>> classList) {
		this.classList = classList;
	}

	public String getClassJson() {
		return classJson;
	}

	public void setClassJson(String classJson) {
		this.classJson = classJson;
	}

	public List<Map<String, Object>> getLogicInventoryList() {
		return logicInventoryList;
	}

	public void setLogicInventoryList(List<Map<String, Object>> logicInventoryList) {
		this.logicInventoryList = logicInventoryList;
	}

	public List<Map<String, Object>> getProStockList() {
		return proStockList;
	}

	public void setProStockList(List<Map<String, Object>> proStockList) {
		this.proStockList = proStockList;
	}

	public Map getProStockCountInfo() {
		return proStockCountInfo;
	}

	public void setProStockCountInfo(Map proStockCountInfo) {
		this.proStockCountInfo = proStockCountInfo;
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

	/** 库存报表Form **/
	private BINOLWRKRP01_Form form = new BINOLWRKRP01_Form();

	@Override
	public BINOLWRKRP01_Form getModel() {
		return form;
	}

}
