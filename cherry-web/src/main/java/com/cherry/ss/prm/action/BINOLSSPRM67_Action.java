/*  
 * @(#)BINOLSSPRM67_Action.java     1.0 2013/09/17      
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
package com.cherry.ss.prm.action;

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
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;
import com.cherry.ss.prm.form.BINOLSSPRM67_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM67_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 入库导入明细Action
 * 
 * @author zhangle
 * @version 1.0 2013.09.17
 */
public class BINOLSSPRM67_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM67_Form> {

	private static final long serialVersionUID = 3626901483315250462L;

	private BINOLSSPRM67_Form form = new BINOLSSPRM67_Form();

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLSSPRM67_Action.class);

	@Resource
	private BINOLSSPRM67_IF binOLSSPRM67_BL;
	@Resource
	private BINOLMOCOM01_BL binOLMOCOM01_BL;
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	/** Excel输入流 */
	private InputStream excelStream;
	/** 下载文件名 */
	private String downloadFileName;
	/** 入库单（Excel导入）list */
	private List<Map<String, Object>> prmInDepotExcelList;
	/** 入库（Excel导入）主单据 */
	@SuppressWarnings("rawtypes")
	private Map prmInDepotExcelMain;
	/** 入库（Excel导入）明细 */
	@SuppressWarnings("rawtypes")
	private List prmInDepotExcelDetail;

	public String init() {
		return SUCCESS;
	}

	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSessionMap() throws Exception {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put("userInfo", userInfo);
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoId.equals("-9999")) {
			map.put("brandInfoId", brandInfoId);
		} else {
			map.put("brandInfoId", userInfo.getCurrentBrandInfoID());
		}
		map.put("language", userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("validFlag", "1");
		return map;
	}

	/**
	 * 入库（Excel导入）结果
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String search() {
		try {
			Map<String, Object> map = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			map.putAll(formMap);
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLSSPRM67_BL.getPrmInDepotExcelCount(map);
			if (count != 0) {
				prmInDepotExcelList = binOLSSPRM67_BL
						.getPrmInDepotExcelList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}

	/**
	 * 入库（Excel导入）单明细
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String detailInit() {
		try {
			Map<String, Object> map = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			map.putAll(formMap);
			map = CherryUtil.removeEmptyVal(map);
			prmInDepotExcelMain = binOLSSPRM67_BL
					.getPrmInDepotExcelInfobyId(map);
			prmInDepotExcelList = binOLSSPRM67_BL
					.getPrmInDepotExcelDetailList(map);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}

	/**
	 * 查询结果Excel导出
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String export() {
		// 取得参数MAP
		try {
			Map<String, Object> searchMap = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			searchMap.putAll(formMap);
			String language = ConvertUtil.getString(searchMap
					.get(CherryConstants.SESSION_LANGUAGE));
			String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM67",
					language, "downloadFileName");
			setDownloadFileName(fileName + ".zip");
			setExcelStream(new ByteArrayInputStream(
					binOLCM37_BL.fileCompression(
							binOLSSPRM67_BL.exportExcel(searchMap), fileName
									+ ".xls")));
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				this.addActionError(getText("EMO00022"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}

	@Override
	public BINOLSSPRM67_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getPrmInDepotExcelList() {
		return prmInDepotExcelList;
	}

	public void setPrmInDepotExcelList(
			List<Map<String, Object>> prmInDepotExcelList) {
		this.prmInDepotExcelList = prmInDepotExcelList;
	}

	@SuppressWarnings("rawtypes")
	public Map getPrmInDepotExcelMain() {
		return prmInDepotExcelMain;
	}

	@SuppressWarnings("rawtypes")
	public void setPrmInDepotExcelMain(Map prmInDepotExcelMain) {
		this.prmInDepotExcelMain = prmInDepotExcelMain;
	}

	@SuppressWarnings("rawtypes")
	public List getPrmInDepotExcelDetail() {
		return prmInDepotExcelDetail;
	}

	@SuppressWarnings("rawtypes")
	public void setPrmInDepotExcelDetail(List prmInDepotExcelDetail) {
		this.prmInDepotExcelDetail = prmInDepotExcelDetail;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		return FileUtil.encodeFileName(request, downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

}