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
import com.cherry.mb.cct.form.BINOLMBCCT10_Form;
import com.cherry.mb.cct.interfaces.BINOLMBCCT10_IF;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBCCT10_Action extends BaseAction implements ModelDriven<BINOLMBCCT10_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BINOLMBCCT10_Form form = new BINOLMBCCT10_Form();

	private static final Logger logger = LoggerFactory.getLogger(BINOLMBCCT10_Action.class);
	
	@Resource
	private BINOLMBCCT10_IF binolmbcct10_IF;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	private List<Map<String, Object>> issueList;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<Map<String, Object>> issueList) {
		this.issueList = issueList;
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

	public String init() throws Exception{
		try{
			String nowDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
			String beginDate = DateUtil.addDateByMonth(CherryConstants.DATE_PATTERN, nowDate, -1);
			String endDate = nowDate;
			form.setCreateTimeStart(beginDate);
			form.setCreateTimeEnd(endDate);
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
			// 是否会员
			map.put("isMember", form.getIsMember());
			// 客户编号/会员卡号
			map.put("customerCode", form.getCustomerCode());
			// 客户来电号码
			map.put("customerNumber", form.getCustomerNumber());
			// 问题记录时间查询起始时间
			map.put("createTimeStart", form.getCreateTimeStart());
			// 问题记录时间查询截止时间
			if(!CherryChecker.isNullOrEmpty(form.getCreateTimeEnd(), true)){
				map.put("createTimeEnd", DateUtil.addDateByDays("yyyy-MM-dd", form.getCreateTimeEnd(), 1));
			}
			// 客户姓名
			map.put("customerName", form.getCustomerName());
			// 问题类型
			map.put("issueType", form.getIssueType());
			// 处理结果
			map.put("resolution", form.getResolution());
			// 问题来源
			String issueSource = ConvertUtil.getString(form.getIssueSource());
			if("1".equals(issueSource)){
				map.put("fromCallCenter", issueSource);
			}else if("2".equals(issueSource)){
				map.put("fromMemberManage", issueSource);
			}
			//取得模板数量
			int count = binolmbcct10_IF.getIssueCount(map);
			if(count > 0){
				List<Map<String, Object>> resultList = binolmbcct10_IF.getIssueList(map);
				// 取得List
				this.setIssueList(resultList);
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
			// 是否会员
			map.put("isMember", form.getIsMember());
			// 客户编号/会员卡号
			map.put("customerCode", form.getCustomerCode());
			// 客户来电号码
			map.put("customerNumber", form.getCustomerNumber());
			// 问题记录时间查询起始时间
			map.put("createTimeStart", form.getCreateTimeStart());
			// 问题记录时间查询截止时间
			if(!CherryChecker.isNullOrEmpty(form.getCreateTimeEnd(), true)){
				map.put("createTimeEnd", DateUtil.addDateByDays("yyyy-MM-dd", form.getCreateTimeEnd(), 1));
			}
			// 客户姓名
			map.put("customerName", form.getCustomerName());
			// 问题类型
			map.put("issueType", form.getIssueType());
			// 处理结果
			map.put("resolution", form.getResolution());
            
            Map<String, Object> exportMap = binolmbcct10_IF.getExportMap(map);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && "0".equals(form.getExportFormat())) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binolmbcct10_IF);
            	 excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		String tempFilePath = binolmbcct10_IF.exportCsv(exportMap);
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
	public BINOLMBCCT10_Form getModel() {
		return form;
	}
}
