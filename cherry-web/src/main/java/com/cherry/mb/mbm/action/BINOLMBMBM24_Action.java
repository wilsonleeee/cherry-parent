package com.cherry.mb.mbm.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM24_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM24_Form;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLMBMBM24_Action extends BaseAction implements
		ModelDriven<BINOLMBMBM24_Form> {

	/**
	 * 会员关键属性导入一览Action
	 * 
	 * @author LUOHONG
	 * @version 1.0 2013/09/03
	 */
	private static final long serialVersionUID = -6303988939745486690L;

	/** 会员关键属性导入一览 **/
	private BINOLMBMBM24_Form form = new BINOLMBMBM24_Form();

	/** 共通 */
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Resource
	private BINOLCM37_BL binOLCM37_BL;

	/** 会员关键属性导入一览BL */
	@Resource(name = "binOLMBMBM24_BL")
	private BINOLMBMBM24_BL binOLMBMBM24_BL;

	/** 会员关键属性导入一览List */

	private List<Map<String, Object>> memImportList;

	/** 导入一览明细list */
	private List<Map<String, Object>> detailList;
	
	/** 导入结果信息 */
    private Map<String, Object> sumInfo;

	/** 导入主表Id */
	private int keyAttrImportId;

	/** 流水号 */
	private String serialNo;
	/** Excel输入流 */
	private InputStream excelStream;

	/** 导出文件名 */
	private String exportName;

	public List<Map<String, Object>> getMemImportList() {
		return memImportList;
	}

	public void setMemImportList(List<Map<String, Object>> memImportList) {
		this.memImportList = memImportList;
	}
	public List<Map<String, Object>> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<Map<String, Object>> detailList) {
		this.detailList = detailList;
	}
	public Map<String, Object> getSumInfo() {
		return sumInfo;
	}
	
	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}
	
	public int getKeyAttrImportId() {
		return keyAttrImportId;
	}

	public void setKeyAttrImportId(int keyAttrImportId) {
		this.keyAttrImportId = keyAttrImportId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

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
	/**
	 * 初始化会员关键属性一览
	 * 
	 * @return
	 */
	public String init() {
		
		return SUCCESS;
	}

	/**
	 * 会员关键属性导入查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		Map<String, Object> paramMap = getCommonMap();
		ConvertUtil.setForm(form, paramMap);
		CherryUtil.trimMap(paramMap);
		// 关键属性导入一览总数
		int count = binOLMBMBM24_BL.getCount(paramMap);
		if (count > 0) {
			// 关键属性导入一览List
			memImportList = binOLMBMBM24_BL.getMemImportList(paramMap);
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
		String resultStr = binOLMBMBM24_BL.getImportName(map);
		ConvertUtil.setResponseByAjax(response, resultStr);

	}

	/**
	 * 导入明细一览画面
	 * 
	 * @return Excel导入明细一览画面
	 */
	public String detailInit() throws Exception {
		// 积分导入Id
		form.setMemImportId(keyAttrImportId);
		// 导入流水号
		form.setBillNo(serialNo);
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
		detailMap.put("keyAttrImportId", form.getMemImportId());
		// 会员卡号
		detailMap.put("memberCode", form.getMemberCode());
		// 结果区分
		detailMap.put("resultFlag", form.getResultFlag());
		ConvertUtil.setForm(form, detailMap);
		CherryUtil.trimMap(detailMap);
		// 导入明细总数
		int detailCount = binOLMBMBM24_BL.getDetailCount(detailMap);
		// 导入明细list
		if (detailCount > 0) {
			detailList = binOLMBMBM24_BL.getDetailList(detailMap);
			form.setITotalDisplayRecords(detailCount);
			form.setITotalRecords(detailCount);
		}
		//导入结果数量统计
		 sumInfo = binOLMBMBM24_BL.getSumInfo(detailMap);
		 return "BINOLMBMBM24_11";
		 
	}

	/**
	 * 导出明细一览数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getCommonMap();
		String language = ConvertUtil.getString(searchMap
				.get(CherryConstants.SESSION_LANGUAGE));
		// 积分主表Id
		searchMap.put("keyAttrImportId", form.getMemImportId());
		// 会员卡号
		searchMap.put("memberCode", form.getMemberCode().trim());
		// 结果区分
		searchMap.put("resultFlag", form.getResultFlag());
		// 取得查询条件
		Map<String, Object> paramMap = binOLMBMBM24_BL.getImportInfo(searchMap);
		if(null!=paramMap){
			// 导入流水号
			searchMap.put("billNo", paramMap.get("billNo"));
			// 导入原因
			searchMap.put("importReason", paramMap.get("importReason"));
			// 导入时间
			searchMap.put("importTime", paramMap.get("importTime"));
		}
		
		// 取得会员关键属性信息List
		try {
			// 下载文件名称
			String zipName = binOLMOCOM01_BL.getResourceValue("BINOLMBMBM24", language, "binolmbmbm24_exportName");
			 // 下载文件名称编码格式转换处理
	    	exportName = zipName+".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLMBMBM24_BL.exportExcel(searchMap), zipName+".xls"));
		} catch (Exception e) {
			this.addActionError(getText("EMO00022"));
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
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 导入名称
		map.put("importName", form.getImportName());
		// 开始日期
		map.put("startDate", form.getStartDate());
		// 结束日期
		map.put("endDate", form.getEndDate());
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}

	@Override
	public BINOLMBMBM24_Form getModel() {
		return form;
	}

}
