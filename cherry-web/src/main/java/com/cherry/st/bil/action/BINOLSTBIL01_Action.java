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
import com.cherry.st.bil.form.BINOLSTBIL01_Form;
import com.cherry.st.bil.interfaces.BINOLSTBIL01_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.xwork2.ModelDriven;

public class BINOLSTBIL01_Action extends BaseAction implements ModelDriven<BINOLSTBIL01_Form>{
	
	private static final long serialVersionUID = 4653288705092517045L;
	
	/**异常处理*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTBIL01_Action.class);
	
	private BINOLSTBIL01_Form form = new BINOLSTBIL01_Form();
	
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
	
	@Resource(name="binOLSTBIL01_BL")
	private BINOLSTBIL01_IF binOLSTBIL01_BL;
	
	/** Excel输入流 */
    private InputStream excelStream;
    
	/** 下载文件名 */
    private String downloadFileName;
	
	public String init() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		String organizationInfoCode = userInfo.getOrganizationInfoCode();
		String brandCode = userInfo.getBrandCode();
		
		// 开始日期
		form.setStartDate(binOLCM00BL.getFiscalDate(userInfo.getBIN_OrganizationInfoID(), new Date()));
		// 截止日期
		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		
        // 根据入库工作流文件定义，从code值为1305的入库审核状态取出需要显示的审核状态，返回到画面上的Select框
        // 如果没有定义则取默认的审核状态（1007）
        String workFlowName = ConvertUtil.getWfName(organizationInfoCode, brandCode, "productInOut");
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workFlowName);
        Map<String, Object> metaAttributes = wd.getInitialAction(1).getMetaAttributes();
        List<Map<String,Object>> vfList = new ArrayList<Map<String,Object>>();
        vfList = codeTable.getCodes("1007");
        if (null != metaAttributes && !metaAttributes.isEmpty()) {
            String brandVerifiedFlagGR = ConvertUtil.getString(metaAttributes.get("BrandVerifiedFlagGR"));
            if (!brandVerifiedFlagGR.equals("")) {
                String[] showVFArr = brandVerifiedFlagGR.split("\\|");
                vfList = codeTable.getCodes("1305");
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
        form.setVerifiedFlagsGRList(vfList);
		
		return SUCCESS;
	}
	
	public String search() throws JSONException{
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		
		// 取得参数MAP
		Map<String, Object> map = searchMap();

		// 取得总数
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		int count = binOLSTBIL01_BL.getPrtInDepotCount(map);
		if (count > 0) {
			// 取得渠道List
			form.setPrtInDepotList(binOLSTBIL01_BL.getPrtInDepotList(map));
		}
//      ================= LuoHong修改：显示统计信息 ================== //
		// 产品厂商ID
//		String prtVendorId = ConvertUtil.getString(map.get("prtVendorId"));
//		if (!CherryConstants.BLANK.equals(prtVendorId)|| 
//				!CherryChecker.isNullOrEmpty(form.getNameTotal(), true)) {
			form.setSumInfo(binOLSTBIL01_BL.getSumInfo(map));
//		}
		
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLSTBIL01_1";
	}
	
	/**
	 * 入库单一览明细导出
	 * @return
	 */
	public String export() {
		// 取得参数MAP 
        try {
        	Map<String, Object> searchMap = searchMap();
            String language = ConvertUtil.getString(searchMap.get(CherryConstants.SESSION_LANGUAGE));
            String fileName = binOLMOCOM01_BL.getResourceValue("BINOLSTBIL01", language, "downloadFileName");
            downloadFileName = fileName + ".zip";
            excelStream=new ByteArrayInputStream(binOLCM37_BL.fileCompression(binOLSTBIL01_BL.exportExcel(searchMap), fileName+".xls"));
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
	 * 获取查询参数
	 * @return
	 * @throws JSONException
	 */
	private Map<String,Object> searchMap() throws JSONException{
		Map<String,Object> map = new HashMap<String,Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		//品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		//单据号
		map.put("billNoIF", form.getBillNoIF());
		//关联单号
		map.put("relevanceNo", form.getRelevanceNo());
		//导入批次
		map.put("importBatch", form.getImportBatch());
		//开始日期
		map.put("startDate", form.getStartDate());
		//结束日期
		map.put("endDate", form.getEndDate());
		//审核区分
		map.put("verifiedFlag", form.getVerifiedFlag());
		//入库状态
		map.put("tradeStatus", form.getTradeStatus());
		// 产品厂商ID
		map.put("prtVendorId", form.getPrtVendorId());
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
		//开始日期验证
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{"开始日期"}));
				isCorrect = false;
			}
		}
		//结束日期验证
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
	public BINOLSTBIL01_Form getModel() {
		return form;
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
