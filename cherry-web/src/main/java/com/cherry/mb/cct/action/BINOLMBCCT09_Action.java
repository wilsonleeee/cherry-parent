package com.cherry.mb.cct.action;

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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mb.cct.form.BINOLMBCCT09_Form;
import com.cherry.mb.cct.interfaces.BINOLMBCCT09_IF;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBCCT09_Action extends BaseAction implements ModelDriven<BINOLMBCCT09_Form>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLMBCCT09_Form form = new BINOLMBCCT09_Form();

	private static final Logger logger = LoggerFactory.getLogger(BINOLMBCCT09_Action.class);
	
	@Resource
	private BINOLMBCCT09_IF binolmbcct09_IF;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
	private List<Map<String, Object>> callLogList;
	
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

	public List<Map<String, Object>> getCallLogList() {
		return callLogList;
	}

	public void setCallLogList(List<Map<String, Object>> callLogList) {
		this.callLogList = callLogList;
	}

	public String init() throws Exception{
		try{
			String nowDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
			String beginDate = DateUtil.addDateByMonth(CherryConstants.DATE_PATTERN, nowDate, -1);
			String endDate = nowDate;
			form.setCallTimeStart(beginDate);
			form.setCallTimeEnd(endDate);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌不存在的场合
			if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
				// 不是总部的场合
				if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				}
			} else {
				map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			}
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 坐席工号
			map.put("cNo", form.getClassNo());
			// 来电时间查询起始时间
			map.put("callTimeStart", form.getCallTimeStart());
			// 来电时间查询截止时间
			if(!CherryChecker.isNullOrEmpty(form.getCallTimeEnd(), true)){
				map.put("callTimeEnd", DateUtil.addDateByDays("yyyy-MM-dd", form.getCallTimeEnd(), 1));
			}
			// 是否会员
			map.put("isMember", form.getIsMember());
			// 客户来电号码
			map.put("customerNumber", form.getCustomerNumber());
			//取得模板数量
			int count = binolmbcct09_IF.getCallLogCount(map);
			if(count > 0){
				List<Map<String, Object>> resultList = binolmbcct09_IF.getCallLogList(map);
				// 取得List
				this.setCallLogList(resultList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	/**
	 * 查询结果Excel导出
	 * @return
	 */
	public String export() {
        try {
        	Map<String, Object> map = new HashMap<String, Object>();
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌不存在的场合
			if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
				// 不是总部的场合
				if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				}
			} else {
				map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			}
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 坐席工号
			map.put("cNo", form.getClassNo());
			// 来电时间查询起始时间
			map.put("callTimeStart", form.getCallTimeStart());
			// 来电时间查询截止时间
			if(!CherryChecker.isNullOrEmpty(form.getCallTimeEnd(), true)){
				map.put("callTimeEnd", DateUtil.addDateByDays("yyyy-MM-dd", form.getCallTimeEnd(), 1));
			}
			// 是否会员
			map.put("isMember", form.getIsMember());
			// 客户来电号码
			map.put("customerNumber", form.getCustomerNumber());
            
            Map<String, Object> exportMap = binolmbcct09_IF.getExportMap(map);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && "0".equals(form.getExportFormat())) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binolmbcct09_IF);
            	 excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		String tempFilePath = binolmbcct09_IF.exportCsv(exportMap);
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
	
	@Override
	public BINOLMBCCT09_Form getModel() {
		return form;
	}
}
