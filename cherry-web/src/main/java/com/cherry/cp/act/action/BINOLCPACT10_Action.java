package com.cherry.cp.act.action;

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
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.cp.act.bl.BINOLCPACT10_BL;
import com.cherry.cp.act.form.BINOLCPACT10_Form;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.bl.BINOLPTRPS14_BL;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLCPACT10_Action extends BaseAction implements ModelDriven<BINOLCPACT10_Form>{

	private static final long serialVersionUID = 1807654776792917324L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCPACT10_Action.class);
	/** 参数Form*/
	private BINOLCPACT10_Form  form = new BINOLCPACT10_Form();
	@Resource(name="binOLCPACT10_BL")
	private BINOLCPACT10_BL  binOLCPACT10_BL;
	/** 共通 */
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	/**兑换结果List*/
	private  List<Map<String, Object>> resultList;
	
	public List<Map<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}
	@Resource
	private BINOLPTRPS14_BL binolptrps14Bl;
	// 销售记录单据详细信息
	private Map getSaleRecordDetail;
	// 销售记录中商品详细信息
	private List getSaleRecordProductDetail;
	// 操作员
	private Map getEmployeeName;
	// 支付明细
	private List getPayTypeDetail;

	public List getGetPayTypeDetail() {
		return getPayTypeDetail;
	}

	public void setGetPayTypeDetail(List getPayTypeDetail) {
		this.getPayTypeDetail = getPayTypeDetail;
	}

	public Map getGetEmployeeName() {
		return getEmployeeName;
	}

	public void setGetEmployeeName(Map getEmployeeName) {
		this.getEmployeeName = getEmployeeName;
	}

	public Map getGetSaleRecordDetail() {
		return getSaleRecordDetail;
	}

	public void setGetSaleRecordDetail(Map getSaleRecordDetail) {
		this.getSaleRecordDetail = getSaleRecordDetail;
	}

	public List getGetSaleRecordProductDetail() {
		return getSaleRecordProductDetail;
	}

	public void setGetSaleRecordProductDetail(List getSaleRecordProductDetail) {
		this.getSaleRecordProductDetail = getSaleRecordProductDetail;
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
	/**
	 * 页面初始化
	 * @return
	 */
	public String init(){
		
		return "BINOLCPACT10";
	}
	
	/**
	 * 兑换结果查询
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String search() throws Exception {
		Map<String, Object> map = getCommonMap();
		ConvertUtil.setForm(form, map);
		CherryUtil.trimMap(map);
		// 品牌Code
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 会员【手机号码】字段加密
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
		}
		int count = binOLCPACT10_BL.getExchangeCount(map);
		if (count > 0) {
			resultList = binOLCPACT10_BL.getExchangeList(map);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLCPACT10_1";
	}
	/**
	 * 明细画面
	 * @return
	 * @throws Exception
	 */
	public String detailInit() throws Exception {
		// 取得map
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("saleRecordId", form.getSaleRecordId());
		// 获取销售记录住单据详细信息
		getSaleRecordDetail = binolptrps14Bl.getSaleRecordDetail(map);
		if (getSaleRecordDetail != null) {
			map.put("saleRecordId", getSaleRecordDetail.get("saleRecordId"));
			// 获取销售记录商品详细信息
			getSaleRecordProductDetail = binolptrps14Bl
					.getSaleRecordProductDetail(map);
			// 获取支付方式详细信息
			getPayTypeDetail = binolptrps14Bl.getPayTypeDetail(map);
			if (getSaleRecordProductDetail != null && getSaleRecordProductDetail.size() > 0) {
				//每条销售记录详细的操作员一致
				map.put("employeeCode",
						((Map<String, Object>) getSaleRecordProductDetail.get(0)).get("employeeCode"));
				// 获取操作员姓名
				getEmployeeName = binolptrps14Bl.getEmployeeName(map);
			}
		}
		return "BINOLCPACT10_2";
	}
	/**
	 * 活动 Excel导出
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object>  map= getCommonMap();
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得会员档案信息List
		try {
			// 下载文件名称
			String zipName = binOLMOCOM01_BL.getResourceValue("BINOLCPACT10", language, "ACT10_exportName");
			 // 下载文件名称编码格式转换处理
	    	exportName = zipName+".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLCPACT10_BL.exportExcel(map), zipName+".xls"));
		} catch (Exception e) {
			//错误日志
			logger.error(e.getMessage(),e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	/**
	 * 共通参数Map
	 * @return
	 */
	private Map<String, Object> getCommonMap(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		paramMap.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		paramMap.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 组织
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户Id
		paramMap.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 品牌
		paramMap.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 品牌Code
		paramMap.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 更新者
		paramMap.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		paramMap.put(CherryConstants.UPDATEPGM, "BINOLCPACT10");
		// 活动Id
		paramMap.put("campaignId",form.getCampaignId());
		// 单据号
		paramMap.put("billCode",form.getBillCode().trim());
		// 开始日期
		paramMap.put("startDate",form.getStartDate().trim());
		// 结束日期
		paramMap.put("endDate",form.getEndDate().trim());
		// 会员类型
		paramMap.put("testType",form.getTestType().trim());
		// 柜台Code
		paramMap.put("departCode",form.getDepartCode().trim());
		// 会员卡号
		paramMap.put("memberCode",form.getMemberCode().trim());
		// 会员手机
		paramMap.put("mobilePhone",form.getMobilePhone());
		return paramMap;
	}
	@Override
	public BINOLCPACT10_Form getModel() {
		return form;
	}

}
