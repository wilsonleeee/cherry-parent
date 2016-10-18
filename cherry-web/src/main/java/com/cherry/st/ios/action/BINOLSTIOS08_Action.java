/*  
 * @(#)BINOLSTIOS08_Action.java     1.0 2013/07/15    
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
package com.cherry.st.ios.action;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.ios.form.BINOLSTIOS08_Form;
import com.cherry.st.ios.interfaces.BINOLSTIOS08_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 入库单（Excel导入）详细Action
 * 
 * @author zhangle
 * @version 1.0 2013.07.15
 */
public class BINOLSTIOS08_Action extends BaseAction implements
		ModelDriven<BINOLSTIOS08_Form> {
			
	private static final long serialVersionUID = -6533826836444476255L;
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTIOS08_Action.class);
	private BINOLSTIOS08_Form form = new BINOLSTIOS08_Form();
	@Resource(name="binOLSTIOS08_BL")
	private BINOLSTIOS08_IF binOLSTIOS08_BL;
	@Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    @Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;
	
	
	/**
	 * 入库（Excel导入）查询画面
	 * 
	 * @return 入库（Excel导入）查询画面
	 */
	public String init() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 入库（Excel导入）结果
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String search() {
		try {
			Map<String, Object> map = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>)Bean2Map.toHashMap(form);
			map.putAll(formMap);
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLSTIOS08_BL.getProductInDepotExcelCount(map);
			if(count != 0){
				productInDepotExcelList = binOLSTIOS08_BL.getProductInDepotExcelList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 入库（Excel导入）明细
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getIndepotExcelDetail() {
		try {
			Map<String, Object> map = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>)Bean2Map.toHashMap(form);
			map.putAll(formMap);
			map.put("productInDepotExcelId", form.getProductInDepotExcelId());
			map = CherryUtil.removeEmptyVal(map);
			productInDepotExcelDetailList = binOLSTIOS08_BL.getProductInDepotExcelDetailList(map);	
			setProductInDepotExcelInfo(binOLSTIOS08_BL.getProductInDepotExcelInfo(map));	
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	
	
	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSessionMap() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put("userInfo", userInfo);
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoId.equals("-9999")){
			map.put("brandInfoId", brandInfoId);
		}else{
			map.put("brandInfoId",userInfo.getCurrentBrandInfoID());
		}
		map.put("language", userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		return map;
	}	

	/**
	 * 查询结果Excel导出
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String export() {
        // 取得参数MAP 
        try {
        	Map<String, Object> searchMap = getSessionMap();
        	Map<String, Object> formMap = (Map<String, Object>)Bean2Map.toHashMap(form);
        	searchMap.putAll(formMap);
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTIOS08", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLSTIOS08_BL.exportExcel(searchMap), fileName+".xls"));
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
	
	/** 入库单（Excel导入）list*/
	private List<Map<String, Object>> productInDepotExcelList;
	
	/** 入库单（Excel导入）明细*/
	private List<Map<String, Object>> productInDepotExcelDetailList;
	
	/** 入库单（Excel导入）详细信息*/
	@SuppressWarnings("rawtypes")
	private Map productInDepotExcelInfo;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getProductInDepotExcelList() {
		return productInDepotExcelList;
	}

	public void setProductInDepotExcelList(
			List<Map<String, Object>> productInDepotExcelList) {
		this.productInDepotExcelList = productInDepotExcelList;
	}
	
	public List<Map<String, Object>> getProductInDepotExcelDetailList() {
		return productInDepotExcelDetailList;
	}

	public void setProductInDepotExcelDetailList(
			List<Map<String, Object>> productInDepotExcelDetailList) {
		this.productInDepotExcelDetailList = productInDepotExcelDetailList;
	}

	@SuppressWarnings("rawtypes")
	public Map getProductInDepotExcelInfo() {
		return productInDepotExcelInfo;
	}

	@SuppressWarnings("rawtypes")
	public void setProductInDepotExcelInfo(Map productInDepotExcelInfo) {
		this.productInDepotExcelInfo = productInDepotExcelInfo;
	}
	
	@Override
	public BINOLSTIOS08_Form getModel() {
		return form;
	}

	public String getDownloadFileName() throws Exception {
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