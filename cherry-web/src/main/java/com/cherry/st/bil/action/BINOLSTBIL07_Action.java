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
import com.cherry.pt.common.ProductConstants;
import com.cherry.st.bil.form.BINOLSTBIL07_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL07_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTBIL07_Action extends BaseAction implements
ModelDriven<BINOLSTBIL07_Form>{
	
	private static final long serialVersionUID = 4197998404367163583L;
	
	/**异常处理*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTBIL07_Action.class);
	
    @Resource(name="workflow")
    private Workflow workflow;
	    
    @Resource(name="CodeTable")
    private CodeTable codeTable;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00BL;
	
	@Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
	
	@Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name="binOLSTBIL07_BL")
	private BINOLSTBIL07_IF binOLSTBIL07_IF;
	
	/** 参数FORM */
	private BINOLSTBIL07_Form form = new BINOLSTBIL07_Form();
	
	/** 汇总信息 */
	private Map<String, Object> sumInfo;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
    
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
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		String language = (String) session.get(CherryConstants.SESSION_LANGUAGE);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 操作类型--查询
		map.put(CherryConstants.OPERATION_TYPE, CherryConstants.OPERATION_TYPE1);
		map.put(CherryConstants.SESSION_LANGUAGE, language);
		// 开始日期
		form.setStartDate(binOLCM00BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		
	    String organizationInfoCode = userInfo.getOrganizationInfoCode();
	    String brandCode = userInfo.getBrandCode();
		
        // 根据移库工作流文件定义，从code值为1324的移库审核状态取出需要显示的审核状态，返回到画面上的Select框
        // 如果没有定义则取默认的审核状态（1007）
        String workFlowName = ConvertUtil.getWfName(organizationInfoCode, brandCode, "productShift");
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workFlowName);
        Map<String, Object> metaAttributes = wd.getInitialAction(1).getMetaAttributes();
        List<Map<String,Object>> vfList = new ArrayList<Map<String,Object>>();
        vfList = codeTable.getCodes("1007");
        if (null != metaAttributes && !metaAttributes.isEmpty()) {
            String brandVerifiedFlagMV = ConvertUtil.getString(metaAttributes.get("BrandVerifiedFlagMV"));
            if (!brandVerifiedFlagMV.equals("")) {
                String[] showVFArr = brandVerifiedFlagMV.split("\\|");
                vfList = codeTable.getCodes("1324");
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
        form.setVerifiedFlagsMVList(vfList);
		return SUCCESS;
	}
	
	/**
	 * <p>
	 *移库单一览
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
		Map<String, Object> searchMap= getSearchMap();
		// 取得总数
		int count = binOLSTBIL07_IF.searchShiftCount(searchMap);
		if (count > 0) {
			// 取得渠道List
			form.setShiftList(binOLSTBIL07_IF.searchShiftList(searchMap));
		}
		// ================= LuoHong修改：显示统计信息 ================== //
//		// 产品厂商ID
//		String prtVendorId = ConvertUtil.getString(searchMap
//				.get(ProductConstants.PRT_VENDORID));
//		if (!CherryConstants.BLANK.equals(prtVendorId)
//				|| !CherryChecker.isNullOrEmpty(form.getNameTotal(), true)) {
			setSumInfo(binOLSTBIL07_IF.getSumInfo(searchMap));
//		}
			
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSTBIL07_1";
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
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTBIL07", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            excelStream=new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLSTBIL07_IF.exportExcel(searchMap), fileName+".xls"));
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
		// 所属组织
		map.put("organizationInfoId",userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put("brandInfoId",userInfo.getBIN_BrandInfoID());
		// 单据
		map.put("billNo",form.getBillNo());
		// 审核区分
		map.put("verifiedFlag",form.getVerifiedFlag());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
	    // 开始日
		map.put("startDate", form.getStartDate());
		// 结束日
		map.put("endDate", form.getEndDate());
		// 产品厂商ID
		map.put(ProductConstants.PRT_VENDORID, form.getPrtVendorId());
		// 产品名称
		map.put(CherryConstants.NAMETOTAL, form.getNameTotal());

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
				this.addActionError(getText("ECM00008", new String[]{"开始日期"}));
				isCorrect = false;
			}
		}
		/*结束日期验证*/
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{"结束日期"}));
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
	
	@Override
	public BINOLSTBIL07_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public void setSumInfo(Map<String, Object> sumInfo) {
		this.sumInfo = sumInfo;
	}

	public Map<String, Object> getSumInfo() {
		return sumInfo;
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
