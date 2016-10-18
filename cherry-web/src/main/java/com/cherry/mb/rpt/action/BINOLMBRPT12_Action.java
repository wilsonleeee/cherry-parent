package com.cherry.mb.rpt.action;

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
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.mb.rpt.form.BINOLMBRPT12_Form;
import com.cherry.mb.rpt.interfaces.BINOLMBRPT12_IF;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员连带销售报表Action
 * 
 * @author hub
 * @version 1.0 2016-04-12
 */
public class BINOLMBRPT12_Action extends BaseAction implements ModelDriven<BINOLMBRPT12_Form>{

	private static final long serialVersionUID = -4795462033951823798L;
	private static Logger logger = LoggerFactory.getLogger(BINOLMBRPT12_Action.class);
	
	private static final String MAIN_TYPE_JOINT_PRT = "2";
	private static final int SEARCH_PRODUCT = 1;
	private static final int SEARCH_MEMBER = 2;
	
	private BINOLMBRPT12_Form form = new BINOLMBRPT12_Form();
	
	@Resource(name="binOLMBRPT12_IF")
	private BINOLMBRPT12_IF binOLMBRPT12_IF;
	
	@Resource(name="binOLMBRPT12_exportIF")
	private BINOLCM37_IF binOLMBRPT12_exportIF;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	private InputStream excelStream;

    private String downloadFileName;
    
	@Override
	public BINOLMBRPT12_Form getModel() {
		return form;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/**
     * 页面初始化
     * @return
     * @throws JSONException
     */
	public String init() throws Exception{
		Map<String, Object> map = getCommMap();
		String busDate = binOLMBRPT12_IF.getBussinessDate(map);
		String startDate = DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, busDate, -6);
		form.setStartDate(startDate);
		form.setEndDate(busDate);
		return SUCCESS;
	}
	
	/**
	 * 初始化产品信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initPrtDialog() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 取得产品信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popPrtDialog() throws Exception {
		if (!validateForm(SEARCH_PRODUCT)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String, Object> map = getCommMap();
		int count = 0;
		List<Map<String, Object>> prtList = null;
		if (MAIN_TYPE_JOINT_PRT.equals(form.getMainType())) {
			// 连带产品
			count = binOLMBRPT12_IF.getJointPrtInfoCount(map);
			if (count > 0) {
				prtList = binOLMBRPT12_IF.getJointPrtList(map);
			}
		} else {
			// 主产品
			count = binOLMBRPT12_IF.getProductInfoCount(map);
			if (count > 0) {
				prtList = binOLMBRPT12_IF.getProductInfoList(map);
			}
		}
		if (null != prtList) {
			for (Map<String, Object> prtInfo : prtList) {
				Map<String, Object> productInfo = new HashMap<String, Object>();
				productInfo.put("productVendorId", prtInfo.get("prtVendorId"));
				productInfo.put("prtName", prtInfo.get("nameTotal"));
				prtInfo.put("productInfo", JSONUtil.serialize(productInfo));
			}
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		form.setPrtList(prtList);
		return SUCCESS;
	}
	
	/**
	 * 初始化产品信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String initCateDialog() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 取得产品信息弹出框
	 * @return
	 * @throws Exception
	 */
	public String popCateDialog() throws Exception {
		if (!validateForm(SEARCH_PRODUCT)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String, Object> map = getCommMap();
		int count = 0;
		List<Map<String, Object>> cateList = null;
		if (MAIN_TYPE_JOINT_PRT.equals(form.getMainType())) {
			// 连带产品
			count = binOLMBRPT12_IF.getJointCateCount(map);
			if (count > 0) {
				cateList = binOLMBRPT12_IF.getJointCateList(map);
			}
		} else {
			// 主产品
			count = binOLMBRPT12_IF.getCateCount(map);
			if (count > 0) {
				cateList = binOLMBRPT12_IF.getCateList(map);
			}
		}
		if (null != cateList) {
			for (Map<String, Object> cateInfo : cateList) {
				Map<String, Object> productInfo = new HashMap<String, Object>();
				productInfo.put("cateValId", cateInfo.get("cateId"));
				productInfo.put("cateName", cateInfo.get("primaryCategoryBig"));
				cateInfo.put("bigCateInfo", JSONUtil.serialize(productInfo));
			}
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		form.setCateList(cateList);
		return SUCCESS;
	}
	
	private Map<String, Object> getCommMap() throws Exception {
		Map<String, Object> map = (Map<String, Object>) Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		return map;
	}
	
	/**
	 * 查询
	 * @return
	 * @throws Exception 
	 */
	public String search() throws Exception {
		if (!validateForm(SEARCH_MEMBER)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String, Object> map = getCommMap();
		int count = binOLMBRPT12_IF.getMemberCount(map);
		if(count > 0) {
			form.setMemList(binOLMBRPT12_IF.getMemberList(map));
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 导出
	 * @return
	 */
	public String export() {
        try {
        	if (!validateForm(SEARCH_MEMBER)) {
    			return CherryConstants.GLOBAL_ACCTION_RESULT;
    		}
        	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
        	Map<String, Object> searchMap = getCommMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            Map<String, Object> exportMap = binOLMBRPT12_IF.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + "Excel.zip";
            //Excel
            if(form.getExportType() != null && "Excel".equals(form.getExportType())) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBRPT12_exportIF);
            	 excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLMBRPT12_IF.export(exportMap);
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
            return SUCCESS;
        } catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("ECM00094"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
	}
	
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	private boolean validateForm(int flag) throws Exception {
		boolean isCorrect = true;
		String startDate = form.getStartDate();
		String endDate = form.getEndDate();
		if (!CherryChecker.isNullOrEmpty(startDate) && !CherryChecker.isNullOrEmpty(endDate) && 
				CherryChecker.compareDate(DateUtil.addDateByMonth(DateUtil.DATE_PATTERN, startDate, 12), endDate) < 0) {
			isCorrect = false;
			this.addActionError("搜索的时间范围不能超过12个月！");
		}
		if (SEARCH_MEMBER == flag || 
				SEARCH_PRODUCT == flag && MAIN_TYPE_JOINT_PRT.equals(form.getMainType())) {
			if (CherryChecker.isNullOrEmpty(form.getMainPrtId()) 
					&& CherryChecker.isNullOrEmpty(form.getMainCateId())) {
				isCorrect = false;
				this.addActionError("主产品或者分类必须选择！");
			}
		}
		return isCorrect;
	}
}
