package com.cherry.bs.wem.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.wem.form.BINOLBSWEM05_Form;
import com.cherry.bs.wem.interfaces.BINOLBSWEM05_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;


public class BINOLBSWEM05_Action extends BaseAction implements ModelDriven<BINOLBSWEM05_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -492276812107201124L;
	
	private BINOLBSWEM05_Form form = new BINOLBSWEM05_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSWEM05_Action.class);
	
	@Resource(name="binOLBSWEM05_BL")
	private BINOLBSWEM05_IF binOLBSWEM05_IF;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name="CodeTable")
	private CodeTable CodeTable;
	
	private List commissionEmployeeLevelList;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
    /** 下载文件名 */
    private String downloadFileName;
	
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

	public String init() throws Exception{
		try{
			String nowDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
			String beginDate = DateUtil.addDateByMonth(CherryConstants.DATE_PATTERN, nowDate, -1);
			String endDate = nowDate;
			form.setStartDate(beginDate);
			form.setEndDate(endDate);
			// 获取级别（总部、省代、市代、商城）
			List codeList = CodeTable.getAllByCodeType("1000");
			setCommissionEmployeeLevelList(binOLBSWEM05_IF.getAgentLevelList(codeList));
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
	
	public String search() throws Exception{
		try{
			Map<String, Object> map = getSearchMap();
			//取得模板数量
			int count = binOLBSWEM05_IF.getBonusCount(map);
			if(count > 0){
				List<Map<String, Object>> bonusList = binOLBSWEM05_IF.getBonusList(map);
				// 取得List
				form.setBonusList(bonusList);
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
	
	private Map<String, Object> getSearchMap() throws Exception {
		//参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
	    // 用户ID
        map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
        // 单据号
     	map.put("billCode", form.getBillCode());
        //会员卡号
  		map.put("memCode", form.getMemCode());
  		// 销售人员
  		map.put("employeeCode", form.getEmployeeCode());
  		// 收益人
  		map.put("commissionEmployeeCode", form.getCommissionEmployeeCode());
  		// 收益人级别
  		map.put("commissionEmployeeLevel", form.getCommissionEmployeeLevel());
		// 销售日期（起始）
		if(!CherryChecker.isNullOrEmpty(form.getStartDate(), true)){
			map.put("startDate", form.getStartDate());
		}
		// 销售日期（截止）
		if(!CherryChecker.isNullOrEmpty(form.getEndDate(), true)){
			map.put("endDate", DateUtil.addDateByDays("yyyy-MM-dd", form.getEndDate(), 1));
		}
		map.put("privilegeFlag", "1");
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, "3");
        map = CherryUtil.removeEmptyVal(map);
        // map参数trim处理
     	CherryUtil.trimMap(map);
		return map;
	}
	
	/**
	 * 查询结果Excel导出
	 * @return
	 */
	public String export() {
        try {
        	Map<String, Object> searchMap = getSearchMap();
            Map<String, Object> exportMap = binOLBSWEM05_IF.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLBSWEM05_IF);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
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
	public BINOLBSWEM05_Form getModel() {
		return form;
	}

	public List getCommissionEmployeeLevelList() {
		return commissionEmployeeLevelList;
	}

	public void setCommissionEmployeeLevelList(
			List commissionEmployeeLevelList) {
		this.commissionEmployeeLevelList = commissionEmployeeLevelList;
	}
}
