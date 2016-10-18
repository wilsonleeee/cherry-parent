/*
 * @(#)BINOLPTRPS01_Action.java     1.0 2011/3/11
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */

package com.cherry.st.bil.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.st.bil.form.BINOLSTBIL09_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL09_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 盘点查询Action
 * 
 * 
 * 
 * @author niushunjie
 * @version 1.0 2011.3.11
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL09_Action extends BaseAction implements
		ModelDriven<BINOLSTBIL09_Form> {

    private static final long serialVersionUID = 5582161952782895825L;

    /**异常处理*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTBIL09_Action.class);
	
    @Resource(name="workflow")
    private Workflow workflow;
        
    @Resource(name="CodeTable")
    private CodeTable codeTable;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;

	@Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name="binOLSTBIL09_BL")
	private BINOLSTBIL09_IF binOLSTBIL09_BL;

	/** 参数FORM */
	private BINOLSTBIL09_Form form = new BINOLSTBIL09_Form();

	/** 盘点单List */
	private List takingList;

	/** 节日 */
	private String holidays;
	
    /** 汇总信息 */
    private Map<String, Object> sumInfo;

    /** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;

	public BINOLSTBIL09_Form getModel() {

		return form;
	}

	public List getTakingList() {
		return takingList;
	}

	public void setTakingList(List takingList) {
		this.takingList = takingList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型--库存数据
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE,
						CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo
				.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil
				.getSysDateTime(CherryConstants.DATE_PATTERN));
		
	    String organizationInfoCode = userInfo.getOrganizationInfoCode();
	    String brandCode = userInfo.getBrandCode();
		
        // 根据盘点工作流文件定义，从code值为1322的盘点审核状态取出需要显示的审核状态，返回到画面上的Select框
        // 如果没有定义则取默认的审核状态（1007）
        String workFlowName = ConvertUtil.getWfName(organizationInfoCode, brandCode, "productStockTaking");
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workFlowName);
        Map<String, Object> metaAttributes = wd.getInitialAction(1).getMetaAttributes();
        List<Map<String,Object>> vfList = new ArrayList<Map<String,Object>>();
        vfList = codeTable.getCodes("1007");
        if (null != metaAttributes && !metaAttributes.isEmpty()) {
            String brandVerifiedFlagCA = ConvertUtil.getString(metaAttributes.get("BrandVerifiedFlagCA"));
            if (!brandVerifiedFlagCA.equals("")) {
                String[] showVFArr = brandVerifiedFlagCA.split("\\|");
                vfList = codeTable.getCodes("1322");
                Arrays.sort(showVFArr);
                for (int i = 0; i < vfList.size(); i++) {
                    Map<String,Object> codeMap = (Map<String,Object>) vfList.get(i);
                    String codeKey = ConvertUtil.getString(codeMap.get("CodeKey"));
                    int result = Arrays.binarySearch(showVFArr, codeKey);
                    //只保留在工作流里配置的审核状态
                    if (result < 0) {
                        vfList.remove(i);
                        i--;
                        continue;
                    }
                }
            }
        }
        form.setVerifiedFlagsCAList(vfList);
		return SUCCESS;
	}

	/**
	 * <p>
	 * AJAX盘点单查询
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得盘点单总数
		int count = binOLSTBIL09_BL.searchTakingCount(searchMap);
		// ================= LuoHong修改：显示统计信息 ================== //
//        String prtVendorId = ConvertUtil.getString(searchMap.get("prtVendorId"));
//        String productName = ConvertUtil.getString(searchMap.get("productName"));
		if (count > 0) {
//			// 取得盘点单List
			takingList = binOLSTBIL09_BL.searchTakingList(searchMap);
//			
//            if (!CherryConstants.BLANK.equals(prtVendorId) || !CherryConstants.BLANK.equals(productName)) {
//                sumInfo = binOLSTBIL09_BL.getSumInfo(searchMap);
            }
//		}else{
//            if (!CherryConstants.BLANK.equals(prtVendorId) || !CherryConstants.BLANK.equals(productName)) {
//                sumInfo = new HashMap<String,Object>();
//                sumInfo.put("sumQuantity", 0);
//                sumInfo.put("sumAmount", 0);
//            }
//		}
		 sumInfo = binOLSTBIL09_BL.getSumInfo(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSTBIL09_1";
	}
	
	/**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = getSearchMap();
		
		int count = binOLSTBIL09_BL.getExportDetailCount(map);
		if(count <= 0) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00099"));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
	
	/**
	 * 盘点单一览明细excel导出
	 * @return
	 */
	public String export() {
		// 取得参数MAP 
        try {
        	Map<String, Object> searchMap = getSearchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTBIL09", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            excelStream=new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLSTBIL09_BL.exportExcel(searchMap), fileName+".xls"));
            return SUCCESS;
        } catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof CherryException){
				this.addActionError(((CherryException)e).getErrMessage());
			}else{
				this.addActionError(getText("EMO00022"));
			}
			return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
	}
	
	

	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 * @throws JSONException 
	 */
	private Map<String, Object> getSearchMap() throws JSONException {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 盘点单号
		map.put("stockTakingNo", form.getStockTakingNo());
		// 开始日期
		map.put("startDate", form.getStartDate());
		// 结束日期
		map.put("endDate", form.getEndDate());
		// 盈亏
		map.put("profitKbn", form.getProfitKbn());
		// 审核状态
		map.put("verifiedFlag", form.getVerifiedFlag());
	    //产品厂商ID
        map.put("prtVendorId", form.getPrtVendorId());
        //产品名称
        map.put("productName", form.getProductName());
        // 部门类型
        map.put("departType", form.getDepartType());
        //选中单据ID Arr
        map.put("checkedBillIdArr", form.getCheckedBillIdArr());
        
        Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);

		return map;
	}
	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean
	 * 			验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
		/*开始日期验证*/
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
				isCorrect = false;
			}
		}
		if (isCorrect && startDate != null && !"".equals(startDate)&& 
				endDate != null && !"".equals(endDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startDate, endDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect = false;
			}
		}
		return isCorrect;
	}

    public Map<String, Object> getSumInfo() {
        return sumInfo;
    }

    public void setSumInfo(Map<String, Object> sumInfo) {
        this.sumInfo = sumInfo;
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
}
