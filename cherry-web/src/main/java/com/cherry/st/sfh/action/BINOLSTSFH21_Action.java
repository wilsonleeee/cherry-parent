package com.cherry.st.sfh.action;

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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.mq.mes.atmosphere.JQueryPubSubPush;
import com.cherry.st.sfh.form.BINOLSTSFH21_Form;
import com.cherry.st.sfh.interfaces.BINOLSTSFH21_IF;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTSFH21_Action extends BaseAction implements ModelDriven<BINOLSTSFH21_Form> {

	private static final long serialVersionUID = -8494927832905645514L;
	
	private static final Logger logger = LoggerFactory
			.getLogger(BINOLSTSFH21_Action.class);
	
	@Resource(name="binOLSTSFH21_BL")
	private BINOLSTSFH21_IF binOLSTSFH21_BL;
	
	/** 导出excel共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;
	
	private BINOLSTSFH21_Form form = new BINOLSTSFH21_Form();
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
    
	// 假日
	private String holidays;
    
    /** 后台销售统计LIST*/
    private List<Map<String, Object>> backstageSaleReportList;
    
    /**
     * 初始画面
     * @return
     */
    public String init() {
    	Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
		//所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 开始日期
		form.setStartDate(binOLCM00_BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		return SUCCESS;
    }
    
    /**
     * 查询
     * @return
     * @throws Exception
     */
    public String search() throws Exception {
    	Map<String, Object> map = this.getCommonMap();
    	map.put("prtVendorId", form.getPrtVendorId());
    	map.put("startDate", form.getStartDate());
    	map.put("endDate", form.getEndDate());
    	int count = binOLSTSFH21_BL.getBackSaleReportCount(map);
    	if(count > 0) {
    		backstageSaleReportList = binOLSTSFH21_BL.getBackSaleReportList(map);
    	}
    	form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
    	
    	return "BINOLSTSFH21_01";
    }
    
    /**
     * 导出Excel验证处理
     */
	public void exportCheck() throws Exception {
		Map<String, Object> msgParam = new HashMap<String, Object>();
		msgParam.put("exportStatus", "1");
		Map<String, Object> map = this.getCommonMap();
		
		int count = binOLSTSFH21_BL.getBackSaleReportCount(map);
		// Excel导出最大数据量
		int maxCount = CherryConstants.EXPORTEXCEL_MAXCOUNT;
		if(count > maxCount) {
			msgParam.put("exportStatus", "0");
			msgParam.put("message", getText("ECM00098", new String[]{getText("global.page.exportExcel"), String.valueOf(maxCount)}));
		}
		ConvertUtil.setResponseByAjax(response, msgParam);
	}
    
    /**
     * 
     * @return
     * @throws Exception
     */
    public String export() throws Exception {
    	try {
        	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
        	Map<String, Object> searchMap = this.getCommonMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            Map<String, Object> exportMap = binOLSTSFH21_BL.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            if(form.getExportFormat() != null && ("0".equals(form.getExportFormat()) 
            		|| "2".equals(form.getExportFormat()))) {
            	 byte[] byteArray = binOLCM37_BL.exportExcel(exportMap,binOLSTSFH21_BL);
            	 excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls"));
            }else{
            	//CSV导出
        		exportMap.put("sessionId", request.getSession().getId());
        		exportMap.put(CherryConstants.SESSION_LANGUAGE, language);
        		String tempFilePath = binOLSTSFH21_BL.exportCsv(exportMap);
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
     * 取得查询共通参数
     * @return
     * @throws Exception
     */
	private Map<String, Object> getCommonMap() throws Exception{
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//品牌ID
		if(!CherryChecker.isNullOrEmpty(form.getBrandInfoId())){
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("bussnissDate", binOLSTSFH21_BL.getBussnissDate(map));
		// 此处固定为"China/Eternal"部门的财务报表
		map.put("departName", "China/Eternal");
		return map;
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
	@Override
	public BINOLSTSFH21_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

	public List<Map<String, Object>> getBackstageSaleReportList() {
		return backstageSaleReportList;
	}

	public void setBackstageSaleReportList(
			List<Map<String, Object>> backstageSaleReportList) {
		this.backstageSaleReportList = backstageSaleReportList;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

}
