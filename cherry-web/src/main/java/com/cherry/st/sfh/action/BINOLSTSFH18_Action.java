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
import com.cherry.st.sfh.form.BINOLSTSFH18_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH18_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTSFH18_Action extends BaseAction implements
		ModelDriven<BINOLSTSFH18_Form> {
	
	private static final long serialVersionUID = -76151562263135648L;

	private static final Logger logger = LoggerFactory
					.getLogger(BINOLSTSFH18_Action.class);
	
	@Resource(name = "binOLSTSFH18_BL")
	private BINOLSTSFH18_IF binOLSTSFH18_BL;
	@Resource(name = "binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource(name = "binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/** 订货单（Excel导入）一览 */
	private List<Map<String, Object>> prtOrderExcelList;

	/** 订货单（Excel导入）详细信息 */
	@SuppressWarnings("rawtypes")
	private Map prtOrderExcelInfo;
	/** 订货单（Excel导入）产品明细 */
	private List<Map<String, Object>> prtOrderExcelDetailList;
	
	/** Excel输入流 */
	private InputStream excelStream;

	/** 下载文件名 */
	private String downloadFileName;
			
	private BINOLSTSFH18_Form form = new BINOLSTSFH18_Form();
	
	public String init() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 查询订货（Excel导入）单据LIST
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
			int count = binOLSTSFH18_BL.getPrtOrderExcelCount(map);
			if(count > 0) {
				prtOrderExcelList = binOLSTSFH18_BL.getPrtOrderExcelList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return "BINOLSTSFH18_01";
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
	 * 取得一条单据的明细
	 * @return
	 * @throws Exception
	 */
	public String getOrderExcelDetail() throws Exception {
		try {
			Map<String, Object> map = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map.toHashMap(form);
			map.putAll(formMap);
			map.put("prtOrderExcelId", form.getPrtOrderExcelId());
			map = CherryUtil.removeEmptyVal(map);
			prtOrderExcelInfo = binOLSTSFH18_BL.getPrtOrderExcelInfo(map);
			prtOrderExcelDetailList = binOLSTSFH18_BL.getPrtOrderExcelDetailList(map);
			return "BINOLSTSFH18_02";
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
	 * 导出订货单（Excel导入）明细
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String export() {
		try {
			Map<String, Object> searchMap = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			searchMap.putAll(formMap);
			String language = ConvertUtil.getString(searchMap
					.get(CherryConstants.SESSION_LANGUAGE));
			String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH18",
					language, "downloadFileName");
			downloadFileName = fileName + ".zip";
			excelStream = new ByteArrayInputStream(
					binOLCM37_BL.fileCompression(
							binOLSTSFH18_BL.exportExcel(searchMap), fileName
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
	private Map<String, Object> getSessionMap() throws Exception {
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
	public BINOLSTSFH18_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getPrtOrderExcelList() {
		return prtOrderExcelList;
	}

	public void setPrtOrderExcelList(List<Map<String, Object>> prtOrderExcelList) {
		this.prtOrderExcelList = prtOrderExcelList;
	}

	public Map getPrtOrderExcelInfo() {
		return prtOrderExcelInfo;
	}

	public void setPrtOrderExcelInfo(Map prtOrderExcelInfo) {
		this.prtOrderExcelInfo = prtOrderExcelInfo;
	}

	public List<Map<String, Object>> getPrtOrderExcelDetailList() {
		return prtOrderExcelDetailList;
	}

	public void setPrtOrderExcelDetailList(List<Map<String, Object>> prtOrderExcelDetailList) {
		this.prtOrderExcelDetailList = prtOrderExcelDetailList;
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
