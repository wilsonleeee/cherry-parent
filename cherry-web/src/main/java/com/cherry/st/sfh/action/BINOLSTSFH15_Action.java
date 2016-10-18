package com.cherry.st.sfh.action;

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
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.st.sfh.form.BINOLSTSFH15_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH15_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTSFH15_Action extends BaseAction implements ModelDriven<BINOLSTSFH15_Form>{

	private static final long serialVersionUID = 1L;
	
	private BINOLSTSFH15_Form form = new BINOLSTSFH15_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTSFH15_Action.class);
	
	@Resource(name="binOLSTSFH15_BL")
	private BINOLSTSFH15_IF binOLSTSFH15_IF;
	
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
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

	/** 页面初始化 */
	public String init() throws Exception{
		return SUCCESS;
	}
	
	public String search() throws Exception{
		try{
			Map<String, Object> map = getSearchMap();
			//取得模板数量
			int count = binOLSTSFH15_IF.getSaleOrdersCount(map);
			if(count > 0){
				List<Map<String, Object>> saleOrdersList = binOLSTSFH15_IF.getSaleOrdersList(map);
				// 取得List
				form.setSaleOrdersList(saleOrdersList);
			}
			Map<String,Object> sumInfo = binOLSTSFH15_IF.getSumInfo(map);
			form.setSumInfo(sumInfo);
			
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
        	Map<String, Object> searchMap = getSearchMap();
            Map<String, Object> exportMap = binOLSTSFH15_IF.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLSTSFH15_IF);
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
	
    /**
     * 查询明细结果Excel导出
     * @return
     */
    public String exportDetail() {
        try {
            Map<String, Object> searchMap = getSearchMap();
            Map<String, Object> exportMap = binOLSTSFH15_IF.getExportDetailMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLSTSFH15_IF);
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
	
	@SuppressWarnings("unchecked")
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
		
		// 销售单据号
		map.put("saleOrderNo", form.getSaleOrderNo());
		// 客户部门ID
		map.put("customerOrganizationId", form.getCustomerOrganizationId());
		// 销售部门ID
		map.put("organizationId", form.getOrganizationId());
		// 单据状态
		map.put("billState", form.getBillState());
		// 客户类型
		map.put("customerType", form.getCustomerType());
		// 导入批次
		map.put("importBatch", form.getImportBatch());
		// 销售日期（起始）
		if(!CherryChecker.isNullOrEmpty(form.getStartDate(), true)){
			map.put("startDate", form.getStartDate());
		}
		// 销售日期（截止）
		if(!CherryChecker.isNullOrEmpty(form.getEndDate(), true)){
			map.put("endDate", DateUtil.addDateByDays("yyyy-MM-dd", form.getEndDate(), 1));
		}
		// 共通条参数
		Map<String, Object> paramsMap = (Map<String, Object>)JSONUtil.deserialize(form.getParams());
        map.putAll(paramsMap);
        map = CherryUtil.removeEmptyVal(map);
		return map;
	}
	
	@Override
	public BINOLSTSFH15_Form getModel() {
		return form;
	}

}
