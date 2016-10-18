package com.cherry.bs.sam.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.sam.form.BINOLBSSAM06_Form;
import com.cherry.bs.sam.interfaces.BINOLBSSAM06_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLBSSAM06_Action extends BaseAction implements
ModelDriven<BINOLBSSAM06_Form> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3199218832463691803L;
	/**异常处理*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSSAM06_Action.class);
	
	@Resource(name="binOLBSSAM06_BL")
	private BINOLBSSAM06_IF binOLBSSAM06_IF;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
    /** 下载文件名 */
    private String downloadFileName;
    
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
    
	private BINOLBSSAM06_Form form = new BINOLBSSAM06_Form();
	public String init(){
		return SUCCESS;
	}
	
	public String search(){
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		map.put("employeeName", ConvertUtil.getString(form.getEmployeeName()));
		map.put("startDateTime", ConvertUtil.getString(form.getStartDateTime()));
		map.put("endDateTime", ConvertUtil.getString(form.getEndDateTime()));
		map.put("attendanceType", ConvertUtil.getString(form.getAttendanceType()));
		map.put("departName", ConvertUtil.getString(form.getDepartName()));
		try {
			int count = binOLBSSAM06_IF.getBAAttendanceCount(map);
			if(count>0){
				ConvertUtil.setForm(form, map);
				form.setBAAttendanceList(binOLBSSAM06_IF.getBAAttendanceList(map));
				form.setITotalDisplayRecords(count);
				form.setITotalRecords(count);
			}
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
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
            Map<String, Object> exportMap = binOLBSSAM06_IF.getExportMap(searchMap);
            String zipName = ConvertUtil.getString(exportMap.get("downloadFileName"));
            downloadFileName = zipName + ".zip";
            byte[] byteArray = binOLCM37_BL.exportExcel(exportMap, binOLBSSAM06_IF);
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
        // 员工姓名
  		map.put("employeeName", form.getEmployeeName());
  		// 员工部门
  		map.put("departName", form.getDepartName());
  		// 考勤时间
  		map.put("startDateTime", form.getStartDateTime());
  		// 考勤时间
  		map.put("endDateTime", form.getEndDateTime());
  		// 部门
  		map.put("departName", form.getDepartName());
        map = CherryUtil.removeEmptyVal(map);
        // map参数trim处理
     	CherryUtil.trimMap(map);
		return map;
	}
	
	@Override
	public BINOLBSSAM06_Form getModel() {
		return form;
	}

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

}
