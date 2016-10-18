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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.sfh.form.BINOLSTSFH13_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH13_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTSFH13_Action extends BaseAction implements
		ModelDriven<BINOLSTSFH13_Form> {

	private static final long serialVersionUID = 5201597004892969698L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLSTSFH13_Action.class);
	private BINOLSTSFH13_Form form = new BINOLSTSFH13_Form();

	@Resource(name = "binOLSTSFH13_BL")
	private BINOLSTSFH13_IF binOLSTSFH13_BL;
	@Resource(name = "binOLMOCOM01_BL")
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource(name = "binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	/** 发货单（Excel导入）一览 */
	private List<Map<String, Object>> prtDeliverExcelList;

	/** 发货单（Excel导入）详细信息 */
	@SuppressWarnings("rawtypes")
	private Map prtDeliverExcelInfo;
	/** 发货单（Excel导入）产品明细 */
	private List<Map<String, Object>> prtDeliverExcelDetailList;

	/** Excel输入流 */
	private InputStream excelStream;

	/** 下载文件名 */
	private String downloadFileName;

	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
    /** 是否显示金蝶码 **/
    private String configVal;
	
	/**
	 * 发货导入详细查询画面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		return SUCCESS;
	}

	/**
	 * 查询
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String search() throws Exception {
		try {
			Map<String, Object> map = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			map.putAll(formMap);
			map = CherryUtil.removeEmptyVal(map);
			int count = binOLSTSFH13_BL.getPrtDeliverExcelCount(map);
			if (count != 0) {
				prtDeliverExcelList = binOLSTSFH13_BL
						.getPrtDeliverExcelList(map);
			}
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			return "BINOLSTSFH13_01";
		} catch (Exception e) {
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
	 * 取得一条发货单（Excel导入）明细信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDeliverExcelDetail() throws Exception {
		try {
			Map<String, Object> map = getSessionMap();
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			//配置项【是否开启金蝶码】0：关闭，1：开启
			configVal = binOLCM14_BL.getConfigValue("1360",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			map.putAll(formMap);
			map.put("prtDeliverExcelId", form.getPrtDeliverExcelId());
			map = CherryUtil.removeEmptyVal(map);
			prtDeliverExcelInfo = binOLSTSFH13_BL.getPrtDeliverExcelInfo(map);
			prtDeliverExcelDetailList = binOLSTSFH13_BL
					.getPrtDeliverExcelDetailList(map);
			return "BINOLSTSFH13_02";
		} catch (Exception e) {
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
	 * 导出发货单（Excel导入）明细
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String export() {
		try {
			Map<String, Object> searchMap = getSessionMap();
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			//配置项【是否开启金蝶码】0：关闭，1：开启
			configVal = binOLCM14_BL.getConfigValue("1360",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			searchMap.put("configVal", configVal);
			Map<String, Object> formMap = (Map<String, Object>) Bean2Map
					.toHashMap(form);
			searchMap.putAll(formMap);
			String language = ConvertUtil.getString(searchMap
					.get(CherryConstants.SESSION_LANGUAGE));
			String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTSFH13",
					language, "downloadFileName");
			downloadFileName = fileName + ".zip";
			excelStream = new ByteArrayInputStream(
					binOLCM37_BL.fileCompression(
							binOLSTSFH13_BL.exportExcel(searchMap), fileName
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
		map.put("language", userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		return map;
	}

	@Override
	public BINOLSTSFH13_Form getModel() {

		return form;
	}

	public List<Map<String, Object>> getPrtDeliverExcelList() {
		return prtDeliverExcelList;
	}

	public void setPrtDeliverExcelList(
			List<Map<String, Object>> prtDeliverExcelList) {
		this.prtDeliverExcelList = prtDeliverExcelList;
	}

	public List<Map<String, Object>> getPrtDeliverExcelDetailList() {
		return prtDeliverExcelDetailList;
	}

	public void setPrtDeliverExcelDetailList(
			List<Map<String, Object>> prtDeliverExcelDetailList) {
		this.prtDeliverExcelDetailList = prtDeliverExcelDetailList;
	}

	public Map getPrtDeliverExcelInfo() {
		return prtDeliverExcelInfo;
	}

	public void setPrtDeliverExcelInfo(Map prtDeliverExcelInfo) {
		this.prtDeliverExcelInfo = prtDeliverExcelInfo;
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

	public String getConfigVal() {
		return configVal;
	}

	public void setConfigVal(String configVal) {
		this.configVal = configVal;
	}

}
