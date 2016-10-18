package com.cherry.pt.rps.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.pt.rps.form.BINOLPTRPS31_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS31_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 代理商提成报表Action
 * 
 * @author menghao
 * 
 */
public class BINOLPTRPS31_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS31_Form> {

	private static final long serialVersionUID = -2064706316120725967L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLPTRPS31_Action.class);

	private BINOLPTRPS31_Form form = new BINOLPTRPS31_Form();
	
	@Resource(name="binOLPTRPS31_BL")
	private BINOLPTRPS31_IF binOLPTRPS31_BL;
	
	/** 导出excel共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	/** 区域List */
	private List<Map<String, Object>> reginList;
	/** 假日信息 */
	private String holidays;
	
	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	/** 代理商提成统计信息*/
	private List<Map<String, Object>> baCommissionList;

	/**指定代理商固定的查询条件信息*/
	private Map baInfoMap;
	
	/**代理商推荐会员购买情况一览*/
	private List<Map<String, Object>> memberBuyInfoList;
	
	/**代理商推荐购买情况详细一览*/
	private List<Map<String, Object>> baSaleInfoList;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	/**
	 * 页面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		//所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 取得区域List
		reginList = binOLCM00_BL.getReginList(map);
		return SUCCESS;
	}

	/**
	 * 查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 取得汇总信息
		sumInfo = binOLPTRPS31_BL.getSumInfo(map);
		// 取得库存记录总数
		int count = ConvertUtil.getInt(sumInfo.get("count"));
		if(count > 0) {
			baCommissionList = binOLPTRPS31_BL.getBaCommissionList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 指定代理商的推荐会员销售情况页面初始化
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String memberBuyInit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
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
		// 当前代理商
		map.put("baInfoId", form.getBaInfoId());
		baInfoMap = binOLPTRPS31_BL.getBaNameFromId(map);
		baInfoMap.put("startDate", ConvertUtil.getString(form.getStartDate()));
		baInfoMap.put("endDate", ConvertUtil.getString(form.getEndDate()));
		return SUCCESS;
	}
	
	/**
	 * 代理商推荐会员销售明细查询
	 * @return
	 * @throws Exception
	 */
	public String memberBuySearch() throws Exception {
		Map<String, Object> map = this.getDetailSearchMap();
		
		int count = binOLPTRPS31_BL.getMemberBuyInfoCount(map);
		if(count > 0) {
			memberBuyInfoList = binOLPTRPS31_BL.getMemberBuyInfoList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 代理商推荐会员销售明细导出excel验证处理
	 * @throws Exception
	 */
	public void memberBuyExportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = this.getDetailSearchMap();
		
		int count = binOLPTRPS31_BL.getMemberBuyInfoCount(map);
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
	 * 代理商推荐会员销售明细导出
	 * @return
	 * @throws Exception
	 */
	public String memberBuyExport() throws Exception {
		try {
        	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
        	Map<String, Object> searchMap = this.getDetailSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            Map<String, Object> exportMap = binOLPTRPS31_BL.getMemberBuyExportMap(searchMap);
    		// 此参数用于区分是代理商推荐会员购买详细导出或者代理商推荐购买详细导出
            exportMap.put("exportModel", "0");
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && ("0".equals(form.getExportFormat()))) {
	        	// EXCEL导出
	        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLPTRPS31_BL);
	        	excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLPTRPS31_BL.exportCsvCommon(exportMap);
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
	 * 代理商推荐会员销售明细导出excel验证处理
	 * @throws Exception
	 */
	public void baSaleExportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = this.getDetailSearchMap();
		
		int count = binOLPTRPS31_BL.getBaSaleInfoCount(map);
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
	 * 代理商推荐购买详细导出
	 * @return
	 * @throws Exception
	 */
	public String baSaleExport() throws Exception {
		try {
			UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
        	Map<String, Object> searchMap = this.getDetailSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            Map<String, Object> exportMap = binOLPTRPS31_BL.getBaSaleExportMap(searchMap);
    		// 此参数用于区分是代理商推荐会员购买详细导出或者代理商推荐购买详细导出
            exportMap.put("exportModel", "1");
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && ("0".equals(form.getExportFormat()))) {
	        	// EXCEL导出
	        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLPTRPS31_BL);
	        	excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLPTRPS31_BL.exportCsvCommon(exportMap);
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
	 * 代理商推荐购买查询页面初始化
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String baSaleInit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
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
		// 当前代理商
		map.put("baInfoId", form.getBaInfoId());
		baInfoMap = binOLPTRPS31_BL.getBaNameFromId(map);
		baInfoMap.put("startDate", ConvertUtil.getString(form.getStartDate()));
		baInfoMap.put("endDate", ConvertUtil.getString(form.getEndDate()));
		return SUCCESS;
	}
	
	/**
	 * 代理商推荐购买查询
	 * @return
	 * @throws Exception
	 */
	public String baSaleSearch() throws Exception {
		Map<String, Object> map = this.getDetailSearchMap();
		int count = binOLPTRPS31_BL.getBaSaleInfoCount(map);
		if(count > 0) {
			baSaleInfoList = binOLPTRPS31_BL.getBaSaleInfoList(map);
		}
		
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		
		return SUCCESS;
	}
	
	/**
	 * 代理商推荐购买详细查询的共通参数
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getDetailSearchMap() throws Exception {
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
		// 去除MAP中的空值
		map = CherryUtil.removeEmptyVal(map);
		
		return map;
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
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		return map;
	}

	@Override
	public BINOLPTRPS31_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getBaCommissionList() {
		return baCommissionList;
	}

	public void setBaCommissionList(List<Map<String, Object>> baCommissionList) {
		this.baCommissionList = baCommissionList;
	}

	public Map getBaInfoMap() {
		return baInfoMap;
	}

	public void setBaInfoMap(Map baInfoMap) {
		this.baInfoMap = baInfoMap;
	}

	public List<Map<String, Object>> getMemberBuyInfoList() {
		return memberBuyInfoList;
	}

	public void setMemberBuyInfoList(List<Map<String, Object>> memberBuyInfoList) {
		this.memberBuyInfoList = memberBuyInfoList;
	}

	public List<Map<String, Object>> getBaSaleInfoList() {
		return baSaleInfoList;
	}

	public void setBaSaleInfoList(List<Map<String, Object>> baSaleInfoList) {
		this.baSaleInfoList = baSaleInfoList;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws UnsupportedEncodingException {
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

}
