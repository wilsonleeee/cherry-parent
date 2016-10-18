package com.cherry.mb.mbm.action;

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
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM18_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM18_Form;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBMBM18_Action extends BaseAction implements
		ModelDriven<BINOLMBMBM18_Form> {

	/**
	 * 会员档案导入一览Action
	 * 
	 * @author LUOHONG
	 * @version 1.0 2013/06/26
	 */
	private static final long serialVersionUID = -6303988939745486690L;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLMBMBM18_Action.class.getName());

	/** 会员档案导入一览form **/
	private BINOLMBMBM18_Form form = new BINOLMBMBM18_Form();

	/** 共通BL */
	@Resource
	private BINOLCM00_BL binOLCM00BL;

	@Resource
	private BINOLCM37_BL binOLCM37_BL;

	/** 会员档案导入一览BL */
	@Resource(name = "binOLMBMBM18_BL")
	private BINOLMBMBM18_BL binOLMBMBM18_BL;

	/** 会员档案导入一览List */

	private List<Map<String, Object>> memImportList;

	public List<Map<String, Object>> getMemImportList() {
		return memImportList;
	}

	public void setMemImportList(List<Map<String, Object>> memImportList) {
		this.memImportList = memImportList;
	}

	/** 导入一览明细list */
	private List<Map<String, Object>> detailList;

	public List<Map<String, Object>> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Map<String, Object>> detailList) {
		this.detailList = detailList;
	}

	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;

	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExportName() throws UnsupportedEncodingException{
		return FileUtil.encodeFileName(request,exportName);
	}
	public void setExportName(String exportName) {
		this.exportName = exportName;
	}
	  /** 导入结果信息 */
    private Map<String, Object> sumInfo;

	/** 导入主表Id */
	private int profileImportId;

	/** 导入主表流水号 */
	private String profileBillNo;
	/** 导入类型*/
	private String importType;
	
	public int getProfileImportId() {
		return profileImportId;
	}

	public void setProfileImportId(int profileImportId) {
		this.profileImportId = profileImportId;
	}

	public String getProfileBillNo() {
		return profileBillNo;
	}

	public void setProfileBillNo(String profileBillNo) {
		this.profileBillNo = profileBillNo;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}
	
	
	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	/**
	 * 初始化会员档案一览
	 * 
	 * @return
	 */
	public String init() {
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 开始日期
		form.setStartDate(binOLCM00BL.getFiscalDate(
				userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
	}

	/**
	 * 会员档案导入查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		Map<String, Object> paramMap = getCommonMap();
		// 导入方式
		paramMap.put("importType", form.getImportAway());
		ConvertUtil.setForm(form, paramMap);
		CherryUtil.trimMap(paramMap);
		// 档案导入一览总数
		int count = binOLMBMBM18_BL.getCount(paramMap);
		if (count > 0) {
			// 档案导入一览List
			memImportList = binOLMBMBM18_BL.getMemImportList(paramMap);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}

	/**
	 * 根据输入的字符串模糊查询导入名称
	 * 
	 * 
	 * */
	public void getImportName() throws Exception {
		Map<String, Object> map = getCommonMap();
		// 活动名称模糊查询
		map.put("importNameStr", form.getImportNameStr().trim());
		String resultStr = binOLMBMBM18_BL.getImportName(map);
		ConvertUtil.setResponseByAjax(response, resultStr);

	}

	/**
	 * 导入明细一览画面
	 * 
	 * @return Excel导入明细一览画面
	 */
	public String detailInit() throws Exception {
		// 积分导入Id
		form.setMemImportId(profileImportId);
		// 导入流水号
		form.setBillNo(profileBillNo);
		// 导入类型
		form.setImpType(importType);
	
		return SUCCESS;
	}

	/**
	 * 导入明细查询
	 * 
	 * @return
	 */
	public String detailSearch() throws Exception {
		Map<String, Object> detailMap = getCommonMap();
		// 积分主表Id
		detailMap.put("profileImportID", form.getMemImportId());
		// 会员卡号
		detailMap.put("memberCode", form.getMemberCode());
		// 结果区分
		detailMap.put("resultFlag", form.getResultFlag());
		ConvertUtil.setForm(form, detailMap);
		CherryUtil.trimMap(detailMap);
		// 导入明细总数
		int detailCount = binOLMBMBM18_BL.getDetailCount(detailMap);
		// 导入明细list
		if (detailCount > 0) {
			detailList = binOLMBMBM18_BL.getDetailList(detailMap);
			form.setITotalDisplayRecords(detailCount);
			form.setITotalRecords(detailCount);
		}
		//导入结果数量统计
		 sumInfo = binOLMBMBM18_BL.getSumInfo(detailMap);
		 if("1".equals(form.getImpType())){
			 return "BINOLMBMBM18_11";
		 }else{
			 return "BINOLMBMBM18_12";
		 }
	}

	/**
     * 导出Excel
     */
    public String export() throws Exception {
        
        try {
    		// 取得参数MAP
    		Map<String, Object> map = getCommonMap();
    		map.put("profileImportID", form.getMemImportId());
    		// 会员卡号
    		map.put("memberCode", form.getMemberCode().trim());
    		// 结果区分
    		map.put("resultFlag", form.getResultFlag());
    		// 导入方式
    		map.put("impType", form.getImpType());
    		// 取得查询条件
    		Map<String, Object> paramMap = binOLMBMBM18_BL.getImportInfo(map);
    		if(null!=paramMap){
    			// 导入流水号
    			map.put("billNo", paramMap.get("billNo"));
    			// 导入原因
    			map.put("importReason", paramMap.get("importReason"));
    			// 导入时间
    			map.put("importTime", paramMap.get("importTime"));
    		}
    		// 设置排序字段
    		map.put(CherryConstants.SORT_ID, "BIN_MemProfileImportDetailID desc");
    		// 语言
    		map.put(CherryConstants.SESSION_CHERRY_LANGUAGE, (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE));
    		// sessionId
    		map.put("sessionId", request.getSession().getId());
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		// 获取导出参数
    		Map<String, Object> exportMap = binOLMBMBM18_BL.getExportParam(map);
    		//压缩文件
    		String zipName = binOLCM37_BL.getResourceValue("BINOLMBMBM18", language, "binolmbmbm18_exportName");
    		exportName = zipName+".zip";
        	// 导出excel处理
        	byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLMBMBM18_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("ECM00094"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	/**
	 * 共通参数Map
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getCommonMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 所属组织Code
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 开始日期
		map.put("startDate", form.getStartDate());
		// 结束日期
		map.put("endDate", form.getEndDate());
		// 导入名称
		map.put("importName", form.getImportName());
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}

	@Override
	public BINOLMBMBM18_Form getModel() {
		return form;
	}

}
