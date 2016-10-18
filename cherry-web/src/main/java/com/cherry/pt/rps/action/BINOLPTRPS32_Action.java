package com.cherry.pt.rps.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.digester.SetRootRule;
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
import com.cherry.pt.rps.form.BINOLPTRPS32_Form;
import com.cherry.pt.rps.interfaces.BINOLPTRPS32_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 代理商优惠券使用情况Action
 * @author menghao
 *
 */
public class BINOLPTRPS32_Action extends BaseAction implements
		ModelDriven<BINOLPTRPS32_Form> {

	private static final long serialVersionUID = -1349288984409523987L;

	private static final Logger logger = LoggerFactory
			.getLogger(BINOLPTRPS32_Action.class);

	private BINOLPTRPS32_Form form = new BINOLPTRPS32_Form();
	
	@Resource(name="binOLPTRPS32_BL")
	private BINOLPTRPS32_IF binOLPTRPS32_BL;
	/** 导出excel共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	/** 区域List */
	private List<Map<String, Object>> reginList;
	/** 假日信息 */
	private String holidays;
	/**代理商优惠券使用情况一览数据*/
	private List<Map<String, Object>> baCouponUsedInfoList;
	
	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	/**指定代理商固定的查询条件信息*/
	private Map baInfoMap;
	
	/**代理商优惠券使用情况明细一览LIST*/
	private List<Map<String, Object>> couponUsedDetailList;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
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
		setHolidays(binOLCM00_BL.getHolidays(map));
		setReginList(binOLCM00_BL.getReginList(map));
		return SUCCESS;
	}
	
	/**
	 * 优惠券使用情况统计查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 取得汇总信息
		sumInfo = binOLPTRPS32_BL.getSumInfo(map);
		// 取得库存记录总数
		int count = ConvertUtil.getInt(sumInfo.get("count"));
		if(count > 0) {
			baCouponUsedInfoList = binOLPTRPS32_BL.getBaCouponUsedInfoList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 代理商优惠券使用情况详细页面初始化
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String baCouponUsedInit() throws Exception {
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
		map.put("resellerCode", form.getResellerCode());
		// 此处取得代理商名称已经由ba名称改为代理商名称
		baInfoMap = binOLPTRPS32_BL.getResellerNameFromCode(map);
		baInfoMap.put("startDate", ConvertUtil.getString(form.getStartDate()));
		baInfoMap.put("endDate", ConvertUtil.getString(form.getEndDate()));
		return SUCCESS;
	}
	
	/**
	 * 代理商优惠券使用情况详细一览查询
	 * @return
	 * @throws Exception
	 */
	public String baCouponUsedSearch() throws Exception {
		Map<String, Object> map = this.getSearchMap();
		int count = binOLPTRPS32_BL.getCouponUsedDetailCount(map);
		if(count > 0) {
			couponUsedDetailList = binOLPTRPS32_BL.getCouponUsedDetailList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	/**
	 * 代理商优惠券使用情况明细导出excel验证处理
	 * @throws Exception
	 */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = this.getSearchMap();
		
		int count = binOLPTRPS32_BL.getCouponUsedDetailCount(map);
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
	public String export() throws Exception {
		try {
        	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
        	Map<String, Object> searchMap = this.getSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            Map<String, Object> exportMap = binOLPTRPS32_BL.getExportMap(searchMap);
    		// 此参数用于区分是代理商推荐会员购买详细导出或者代理商推荐购买详细导出
            exportMap.put("exportModel", "0");
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && ("0".equals(form.getExportFormat()))) {
	        	// EXCEL导出
	        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLPTRPS32_BL);
	        	excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLPTRPS32_BL.exportCsv(exportMap);
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
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
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

	@Override
	public BINOLPTRPS32_Form getModel() {
		return form;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public List<Map<String, Object>> getBaCouponUsedInfoList() {
		return baCouponUsedInfoList;
	}

	public void setBaCouponUsedInfoList(List<Map<String, Object>> baCouponUsedInfoList) {
		this.baCouponUsedInfoList = baCouponUsedInfoList;
	}

	public Map getBaInfoMap() {
		return baInfoMap;
	}

	public void setBaInfoMap(Map baInfoMap) {
		this.baInfoMap = baInfoMap;
	}

	public List<Map<String, Object>> getCouponUsedDetailList() {
		return couponUsedDetailList;
	}

	public void setCouponUsedDetailList(List<Map<String, Object>> couponUsedDetailList) {
		this.couponUsedDetailList = couponUsedDetailList;
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
