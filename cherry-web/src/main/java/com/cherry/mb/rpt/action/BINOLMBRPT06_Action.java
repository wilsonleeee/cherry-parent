package com.cherry.mb.rpt.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.rpt.bl.BINOLMBRPT06_BL;
import com.cherry.mb.rpt.form.BINOLMBRPT06_Form;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员推荐会员Action
 * 
 * @author menghao
 * @version 1.0 2014/12/02
 */
public class BINOLMBRPT06_Action extends BaseAction implements
		ModelDriven<BINOLMBRPT06_Form> {

	private static final long serialVersionUID = 3663423039823219237L;

	private static Logger logger = LoggerFactory
			.getLogger(BINOLMBRPT06_Action.class.getName());

	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;

	@Resource(name = "binOLMBRPT06_BL")
	private BINOLMBRPT06_BL binOLMBRPT06_BL;

	/** 导出共通BL **/
	@Resource(name = "binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	/**会员推荐会员一览*/
	private List<Map<String, Object>> memRecommendedRptList;
	
	/** 假日信息 */
	private String holidays;

	/**
	 * 会员推荐会员查询画面
	 */
	public String init() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 查询假日
		setHolidays(binOLCM00_BL.getHolidays(map));
		return SUCCESS;
	}

	/**
	 * 会员推荐会员查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		Map<String, Object> map = getSearchMap();
		sumInfo = binOLMBRPT06_BL.getSumInfo(map);
		int count =  ConvertUtil.getInt(sumInfo.get("count"));
		if(count > 0) {
			memRecommendedRptList = binOLMBRPT06_BL.getMemRecommendedRptList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 取得查询参数
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 品牌code[用于解密会员手机号]
		map.put("brandCode", binOLCM05_BL.getBrandCode(ConvertUtil.getInt(form.getBrandInfoId())));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		return map;
	}
	
	/**
	 * 导出数据量校验
	 * @throws Exception
	 */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = this.getSearchMap();
		
		int count = binOLMBRPT06_BL.getMemRecommendedRptCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		} else if(count == 0){
			// 需要导出的明细数据为空，不能导出！
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00099"));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}

	/**
	 * 导出Excel
	 */
	public String exportExcel() throws Exception {

		try {
			UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
			Map<String, Object> map = this.getSearchMap();
			String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));

			// 获取导出参数
			Map<String, Object> exportMap = binOLMBRPT06_BL.getExportParam(map);

			String zipName = CherryUtil.getResourceValue("mb", "BINOLMBRPT06",
					language, "downloadFileName");
			downloadFileName = zipName + ".zip";
			if(form.getExportFormat() != null && ("0".equals(form.getExportFormat()))){
				// 导出excel处理
				byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,
						binOLMBRPT06_BL);
				excelStream = new ByteArrayInputStream(
						binOLCM37_BL.fileCompression(byteArray, zipName + ".xls"));
			}else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLMBRPT06_BL.exportCsvCommon(exportMap);
        		Map<String, Object> msgParam = new HashMap<String, Object>();
            	msgParam.put("TradeType", "exportMsg");
        		msgParam.put("SessionID", userInfo.getSessionID());
        		msgParam.put("LoginName", userInfo.getLoginName());
        		msgParam.put("OrgCode", userInfo.getOrgCode());
        		msgParam.put("BrandCode", userInfo.getBrandCode());
        		if(tempFilePath != null) {
        			msgParam.put("exportStatus", "1");
        			msgParam.put("message", getText("ECM00096"));
        			msgParam.put("tempFilePath", tempFilePath);
        		} else {
        			msgParam.put("exportStatus", "0");
        			msgParam.put("message", getText("ECM00094"));
        		}
        		//导出完成推送导出信息
        		JQueryPubSubPush.pushMsg(msgParam, "pushMsg", 1);
            	return null;
            }
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.addActionError(getText("ECM00094"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}

	/** Excel输入流 */
	private InputStream excelStream;

	/** 下载文件名 */
	private String downloadFileName;

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		// 转码下载文件名 Content-Disposition
		return FileUtil.encodeFileName(request, downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/** 会员推荐会员Form */
	private BINOLMBRPT06_Form form = new BINOLMBRPT06_Form();

	@Override
	public BINOLMBRPT06_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getMemRecommendedRptList() {
		return memRecommendedRptList;
	}

	public void setMemRecommendedRptList(List<Map<String, Object>> memRecommendedRptList) {
		this.memRecommendedRptList = memRecommendedRptList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

}
