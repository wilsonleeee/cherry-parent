package com.cherry.st.sfh.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import com.cherry.st.sfh.form.BINOLSTSFH20_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH20_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTSFH20_Action extends BaseAction implements ModelDriven<BINOLSTSFH20_Form> {
	
	private static final long serialVersionUID = -6357559867505564218L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH20_Action.class);
	
	private BINOLSTSFH20_Form form = new BINOLSTSFH20_Form();
	
	@Resource(name = "binOLSTSFH20_BL")
	private BINOLSTSFH20_IF binOLSTSFH20_BL;
	@Resource(name = "binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource(name = "binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 后台销售单（Excel导入）一览 */
	private List<Map<String, Object>> backstageSaleExcelList;
	
	/** 后台销售单（Excel导入）主信息 */
	@SuppressWarnings("rawtypes")
	private Map backstageSaleExcelInfo;
	/** 后台销售单（Excel导入）产品明细 */
	private List<Map<String, Object>> backstageSaleExcelDetailList;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 下载文件名 */
	private String downloadFileName;
	
	/**
	 * 一笔导入批次查询画面初始化
	 * @return
	 */
	public String init() {
		return SUCCESS;
	}
	
	/**
	 * 对一笔导入批次进行查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		try {
			Map<String, Object> map = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			map.putAll(formMap);
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLSTSFH20_BL.getBackstageSaleExcelCount(map);
			if(count > 0) {
				setBackstageSaleExcelList(binOLSTSFH20_BL.getBackstageSaleExcelList(map));
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return "BINOLSTSFH20_01";
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				// 对不起，查询发生异常，请重试。
				this.addActionError(getText("ECM00018"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 一条导入单据明细
	 * @return
	 * @throws Exception
	 */
	public String getBackstageSaleExcelDetail() throws Exception {
		try {
			Map<String, Object> map = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map.toHashMap(form);
			map.putAll(formMap);
			map.put("backstageSaleExcelId", form.getBackstageSaleExcelId());
			backstageSaleExcelInfo = binOLSTSFH20_BL.getBackstageSaleExcelInfo(map);
			backstageSaleExcelDetailList = binOLSTSFH20_BL.getBackstageSaleExcelDetailList(map);
			return "BINOLSTSFH20_02";
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof CherryException) {
				this.addActionError(((CherryException) e).getErrMessage());
			} else {
				// 操作失败！
				this.addActionError(getText("ECM00089"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
	}
	
	/**
	 * 导出后台销售单（Excel导入）明细
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() {
			try {
				Map<String, Object> searchMap = getSessionMap();
				Map<String, Object> formMap = (Map<String, Object>) Bean2Map
						.toHashMap(form);
				searchMap.putAll(formMap);
				String language = ConvertUtil.getString(searchMap
						.get(CherryConstants.SESSION_LANGUAGE));
				String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH20",
						language, "downloadFileName");
				downloadFileName = fileName + ".zip";
				excelStream = new ByteArrayInputStream(
						binOLCM37_BL.fileCompression(
								binOLSTSFH20_BL.exportExcel(searchMap), fileName
										+ ".xls"));
				return SUCCESS;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				if (e instanceof CherryException) {
					this.addActionError(((CherryException) e).getErrMessage());
				} else {
					// 导出Excel失败！
					this.addActionError(getText("EMO00022"));
				}
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
	}
	

	/**
	 * 登陆用户信息参数MAP取得
	 * 
	 * @return
	 */
	private Map<String, Object> getSessionMap() {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		ConvertUtil.setForm(form, map);
		map.put("userInfo", userInfo);
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		return map;
	}

	@Override
	public BINOLSTSFH20_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getBackstageSaleExcelList() {
		return backstageSaleExcelList;
	}

	public void setBackstageSaleExcelList(List<Map<String, Object>> backstageSaleExcelList) {
		this.backstageSaleExcelList = backstageSaleExcelList;
	}

	public Map getBackstageSaleExcelInfo() {
		return backstageSaleExcelInfo;
	}

	public void setBackstageSaleExcelInfo(Map backstageSaleExcelInfo) {
		this.backstageSaleExcelInfo = backstageSaleExcelInfo;
	}

	public List<Map<String, Object>> getBackstageSaleExcelDetailList() {
		return backstageSaleExcelDetailList;
	}

	public void setBackstageSaleExcelDetailList(
			List<Map<String, Object>> backstageSaleExcelDetailList) {
		this.backstageSaleExcelDetailList = backstageSaleExcelDetailList;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws UnsupportedEncodingException {
		return FileUtil.encodeFileName(request, downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

}
