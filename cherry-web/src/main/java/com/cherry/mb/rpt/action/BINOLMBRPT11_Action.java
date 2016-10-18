package com.cherry.mb.rpt.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.rpt.bl.BINOLMBRPT11_BL;
import com.cherry.mb.rpt.form.BINOLMBRPT11_Form;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 扫码关注报表Action
 * 
 * @author Hujh
 * @version 1.0 2015-11-11
 */
public class BINOLMBRPT11_Action extends BaseAction implements ModelDriven<BINOLMBRPT11_Form> {

	private static final long serialVersionUID = 2291187612307181103L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBRPT11_Action.class);
	
	private BINOLMBRPT11_Form form = new BINOLMBRPT11_Form();
	
	@Resource(name = "binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource(name="binOLMBRPT11_BL")
	private BINOLMBRPT11_BL binOLMBRPT11_BL;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;

	private InputStream excelStream;

    private String downloadFileName;
    
    /**
     * 页面初始化
     * @return
     * @throws JSONException
     */
	public String init() throws JSONException {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
	    String organizationInfoID = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		map.put(CherryConstants.ORGANIZATIONINFOID,organizationInfoID);
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		form.setHolidays(binOLCM00_BL.getHolidays(map));
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}
	
	/**
	 * 查询
	 * @return
	 */
	public String search() {
		Map<String, Object> map = getSearchMap();
		int count = binOLMBRPT11_BL.getSubscribeCount(map);
		if(count > 0) {
			form.setSubscribeList(binOLMBRPT11_BL.getSubscribeList(map));
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 获取查询参数
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		map.put("counter", form.getCounter());
		map.put("counterBAS", form.getCounterBAS());
		map.put("AgencyName", form.getAgencyName());
		map.put("OpenID", form.getOpenID());
		map.put("SubscribeEventKey", form.getSubscribeEventKey());
		map.put("FirstFlag", form.getFirstFlag());//是否排除非首次关注
		map.put("KeyFlag", form.getKeyFlag());//是否排除无参数
		if(null != form.getStartDate() && !"".equals(form.getStartDate())) {
			map.put("StartDate", DateUtil.suffixDate(form.getStartDate(), 0));
		} else {
			map.put("StartDate", "0000-00-00 00:00:00");
		}
		if(null != form.getEndDate() && !"".equals(form.getEndDate())) {
			map.put("EndDate", DateUtil.suffixDate(form.getEndDate(), 1));
		} else {
			map.put("EndDate", "9999-99-99 99:99:99");
		}
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		ConvertUtil.setForm(form, map);
		CherryUtil.removeEmptyVal(map);
		return map;
	}
	
	/**
	 * 导出
	 * @return
	 */
	public String export() {
        try {
        	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
        	Map<String, Object> searchMap = getSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            Map<String, Object> exportMap = binOLMBRPT11_BL.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + "Excel.zip";
            //Excel
            if(form.getExportType() != null && "Excel".equals(form.getExportType())) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBRPT11_BL);
            	 excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLMBRPT11_BL.export(exportMap);
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
	 * 查询BAS
	 * @throws Exception
	 */
	public void getCounterBAS() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		map.put("userId", userInfo.getBIN_UserID());
		map.put("businessType", "0");
		map.put("operationType", "1");
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		map.put("number", form.getNumber());
		map.put("counterBAS", form.getCounterBAS().trim());
		String resultStr = binOLMBRPT11_BL.getCounterBAS(map);
		ConvertUtil.setResponseByAjax(response, resultStr);
	}
	
	@Override
	public BINOLMBRPT11_Form getModel() {
		return form;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
    	return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	
}
