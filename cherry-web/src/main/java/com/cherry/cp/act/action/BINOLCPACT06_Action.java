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
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.cp.act.bl.BINOLCPACT02_BL;
import com.cherry.cp.act.bl.BINOLCPACT06_BL;
import com.cherry.cp.act.form.BINOLCPACT06_Form;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 活动结果Action
 * @author LuoHong
 * @version 1.0 2013/05/16
 */
public class BINOLCPACT06_Action extends BaseAction implements ModelDriven<BINOLCPACT06_Form>{
	
	
	private static final long serialVersionUID = 3378593424858917235L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLCPACT06_Action.class);
	
	private BINOLCPACT06_Form form = new BINOLCPACT06_Form();
	
	@Resource(name="binOLCPACT02_BL")
    private BINOLCPACT02_BL act02bl;
	@Resource(name="binOLCPACT06_BL")
	private BINOLCPACT06_BL bl;
	
	/** 系统配置项 共通BL */
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL cm14bl;
	
	/** 共通 */
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;
	@Resource
	private BINOLCM37_BL binOLCM37_BL;
	
	private String state;
	
	//活动Id
	private String campaignId;
	
	//活动信息
	private CampaignDTO campaignInfo;
	
	//活动结果一览List 
	private List<Map<String,Object>> campOrderList;
	
	//礼品信息List
	private List<Map<String,Object>> prtList;
	
	//礼品信息Map
	private Map campDetailMap;
	
	//编辑信息Map
	private Map<String,Object> editInfoMap;
	
	public CampaignDTO getCampaignInfo() {
		return campaignInfo;
	}

	public void setCampaignInfo(CampaignDTO campaignInfo) {
		this.campaignInfo = campaignInfo;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	
	public List<Map<String, Object>> getCampOrderList() {
		return campOrderList;
	}

	public void setCampOrderList(List<Map<String, Object>> campOrderList) {
		this.campOrderList = campOrderList;
	}

	public List<Map<String, Object>> getPrtList() {
		return prtList;
	}

	public void setPrtList(List<Map<String, Object>> prtList) {
		this.prtList = prtList;
	}
	
	public Map getCampDetailMap() {
		return campDetailMap;
	}

	public void setCampDetailMap(Map campDetailMap) {
		this.campDetailMap = campDetailMap;
	}

	public Map<String, Object> getEditInfoMap() {
		return editInfoMap;
	}

	public void setEditInfoMap(Map<String, Object> editInfoMap) {
		this.editInfoMap = editInfoMap;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
	 * 活动结果一览初始化
	 * @return 成功画面
	 * @throws Exception
	 */
	public String init()throws Exception{
		Map<String,Object> map = getComMap();
		// 会员活动ID
		map.put("campaignId", campaignId);
		// 查询会员活动首页内容
		campaignInfo = act02bl.getCampaignInfo(map);
		return "BINOLCPACT06";
	}
	
	/**
	 * 活动单据一览初始化
	 * @return 成功画面
	 * @throws Exception
	 */
	public String initRun()throws Exception{
		init();
		return "BINOLCPACT06_3";
	}

	/**
	 * 活动单据一览初始化
	 * @return 成功画面
	 * @throws Exception
	 */
	public String initOrderDispatch()throws Exception{
		init();
		return "BINOLCPACT06_6";
	}

	/**
	 * 查询给定状态的单据
	 * @return
	 * @throws Exception
	 */
	public String searchRun() throws Exception{
		return search();
	}

	/**
	 * 查询预约单据
	 * @return
	 * @throws Exception
	 */
	public String searchDispatchList() throws Exception{
		return dispatchSearch();
	}
	
	/**
	 * 操作单据状态
	 * @return
	 * @throws Exception
	 */
	public String optionRun() throws Exception{
		Map<String,Object> comMap = getComMap();
		Map<String,Object> map = new HashMap<String, Object>(comMap);
		//活动编码 
		map.put("campaignCode", form.getCampaignCode());
		//单据号
		map.put("tradeNoIF",form.getTradeNoIF());
		// 单据ID
		map.put("campOrderId", form.getCampOrderId());
		//会员卡号
		map.put("memCode",form.getMemCode());
		//领用柜台
		map.put("counterGot",form.getCounterGot());
		//预约柜台
		map.put("counterOrder", form.getCounterOrder());
		//coupon码
		map.put("couponCode",form.getCouponCode());
		//会员手机
		map.put("mobile",form.getMobile());
		//单据状态
		map.put("state",form.getState());
		//测试区分
		map.put("testType",form.getTestType());
		try {
			int result = bl.updOrder(map, form.getNewState(), comMap);
			int sucessCount = ConvertUtil.getInt(map.get("sucessCount"));
			int failureCount = ConvertUtil.getInt(map.get("failureCount"));
			String [] args = new String[]{(sucessCount+failureCount)+"",sucessCount+"",failureCount+""};
			if(result > CherryConstants.SUCCESS){
				this.addActionError(getText("ACT00040") + " " +getText("EBS00055", args));
			}else{
				this.addActionMessage(getText("ICM00002") + " " + getText("EBS00055", args));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError(getText("ECM00089"));
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 操作单据状态-红地球
	 * @throws Exception
	 */
	public void optionRun2() throws Exception{
		int errorCode = 0;
		Map<String,Object> comMap = getComMap();
		Map<String,Object> map = new HashMap<String,Object>();
		//单据号
		map.put("billNo",form.getTradeNoIF());
		map.put("expressCode",form.getExpressCode());
		map.put("expressNo",form.getExpressNo());
		map.put("state",form.getState());
		errorCode = bl.tran_updOrder2(map,comMap);
		//ajax返回查询条件
		ConvertUtil.setResponseByAjax(response, errorCode);
	}

	/**
	 * 取得条件参数
	 * @throws Exception
	 */
	public void getConditon() throws Exception{
		Map<String,Object> map = getComMap();
		map.put("SORT_ID", "campOrderId");
		//活动编码 
		map.put("campaignCode", form.getCampaignCode());
		//单据号
		map.put("tradeNoIF",form.getTradeNoIF());
		//会员卡号
		map.put("memCode",form.getMemCode());
		//领用柜台
		map.put("counterGot",form.getCounterGot());
		//预约柜台
		map.put("counterOrder", form.getCounterOrder());
		//会员手机
		map.put("mobile",form.getMobile());
		//活动预约状态
		map.put("state",form.getState());
		//测试区分
		map.put("testType",form.getTestType());
		//coupon码
		map.put("couponCode",form.getCouponCode());
		CherryUtil.trimMap(map);
		int count = bl.getOrderCount(map);
		//ajax返回查询条件
		ConvertUtil.setResponseByAjax(response, count);
	}
	/**
	 * 活动结果一览查询
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception{
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String,Object> map = getComMap();
		//活动编码 
		map.put("campaignCode", form.getCampaignCode());
		//单据号
		map.put("tradeNoIF",form.getTradeNoIF());
		//会员卡号
		map.put("memCode",form.getMemCode());
		//领用柜台
		map.put("counterGot",form.getCounterGot());
		//预约柜台
		map.put("counterOrder", form.getCounterOrder());
		//会员手机
		map.put("mobile",form.getMobile());
		//活动预约状态
		map.put("state",form.getState());
		//测试区分
		map.put("testType",form.getTestType());
		//coupon码
		map.put("couponCode",form.getCouponCode());
		//下发区分
		map.put("sendFlag",form.getSendFlag());
		//会员所属柜台
		map.put("counterBelong",form.getCounterBelong());
		ConvertUtil.setForm(form, map);
		CherryUtil.trimMap(map);
		int count = bl.getOrderCount(map);
		if(count>0){
			campOrderList = bl.getCampOrderList(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		}
		return SUCCESS;
	}


	/**
	 * 预约单据结果一览查询
	 * @return
	 * @throws Exception
	 */
	public String dispatchSearch() throws Exception{
		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String,Object> map = getComMap();
		//活动编码
		map.put("campaignCode", form.getCampaignCode());
		//单据号
		map.put("tradeNoIF",form.getTradeNoIF());
		//会员卡号
		map.put("memCode",form.getMemCode());
		//领用柜台
//		map.put("counterGot",form.getCounterGot());
		//预约柜台
		map.put("counterOrder", form.getCounterOrder());
		//会员手机
		map.put("mobile",form.getMobile());
		//活动预约状态
		map.put("state",form.getState());
		//测试区分
		map.put("testType",form.getTestType());
		//下发区分
		map.put("sendFlag",form.getSendFlag());
		//会员所属柜台
//		map.put("counterBelong",form.getCounterBelong());
		ConvertUtil.setForm(form, map);
		CherryUtil.trimMap(map);
		int count = bl.getOrderCount(map);
		if(count>0){
			List<Map<String,Object>> campDispatchOrderList = bl.getCampOrderList(map);
			form.setCampDispatchOrderList(campDispatchOrderList);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		}
		return SUCCESS;
	}
	/***
	 * 获得活动结果详细信息
	 * @return
	 * @throws Exception
	 */
	public String getOrderDetail()throws Exception{
		Map<String,Object> prtMap = new HashMap<String, Object>();
		prtMap.put("campOrderId", form.getCampOrderId());
		//活动明细详细信息
		campDetailMap = bl.getCampDetailMap(prtMap);
		//活动礼品详细信息
		prtList = bl.getPrtInfoList(prtMap);
		return "BINOLCPACT06_2";
	}

	/***
	 * 获得活动结果详细信息
	 * @return
	 * @throws Exception
	 */
	public String getDispatchDetail()throws Exception{
		Map<String,Object> prtMap = new HashMap<String, Object>();
		prtMap.put("campOrderId", form.getCampOrderId());
		//活动明细详细信息
		campDetailMap = bl.getCampDetailMap(prtMap);
		//活动礼品详细信息
		prtList = bl.getPrtInfoList(prtMap);
		return "BINOLCPACT06_8";
	}

	/***
	 * 批量更新活动单据初始化
	 * @return
	 * @throws Exception
	 */
	public String batchUpdateInit()throws Exception{
		Map<String,Object> map = getComMap();
		map.put("SORT_ID", "campOrderId");
		//活动编码 
		map.put("campaignCode", form.getCampaignCode());
		//单据号
		map.put("tradeNoIF",form.getTradeNoIF());
		//会员卡号
		map.put("memCode",form.getMemCode());
		//领用柜台
		map.put("counterGot",form.getCounterGot());
		//预约柜台
		map.put("counterOrder", form.getCounterOrder());
		//会员手机
		map.put("mobile",form.getMobile());
		//活动预约状态
		map.put("state",form.getState());
		//测试区分
		map.put("testType",form.getTestType());
		//coupon码
		map.put("couponCode",form.getCouponCode());
		//下发区分
		map.put("sendFlag",form.getSendFlag());
		//会员所属柜台
		map.put("counterBelong",form.getCounterBelong());
		CherryUtil.trimMap(map);
		//活动明细详细信息
		int count = bl.getOrderCount(map);
		editInfoMap = new HashMap<String, Object>();
		editInfoMap.put("billNum", count);
		return "BINOLCPACT06_5";
	}
	
	
	/**
	 * 活动单据批量更新
	 * @return 
	 * @throws Exception
	 */
	public String batchUpdate()throws Exception{
		// 验证提交的参数
		if (!validateBatchUpdate()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		//更新参数
		Map<String, Object>  updateMap= getComMap();
		 //领用柜台类型
		updateMap.put("counterType", form.getCounterType());
		 //领用柜台
		updateMap.put("counterGot", form.getBatchCounter());
		 //领用开始日期
		updateMap.put("getFromTime", form.getBatchFromTime());
		 //领用结束日期
		updateMap.put("getToTime", form.getBatchToTime());
		//领用开始类型
		updateMap.put("referFromType", form.getReferFromType());
		updateMap.put("referFromDate", form.getReferFromDate());
		updateMap.put("referFromParam", form.getReferFromParam());
		updateMap.put("referFromValue", form.getReferFromValue());
		updateMap.put("referToType", form.getReferToType());
		updateMap.put("referToDate", form.getReferToDate());
		updateMap.put("referToParam", form.getReferToParam());
		updateMap.put("referToValue", form.getReferToValue());
		//查询参数
		Map<String, Object>  searchMap= getComMap();
		//活动编码 
		searchMap.put("campaignCode", form.getCampaignCode());
		//单据号
		searchMap.put("tradeNoIF",form.getTradeNoIF());
		//会员卡号
		searchMap.put("memCode",form.getMemCode());
		//领用柜台
		searchMap.put("counterGot",form.getCounterGot());
		//预约柜台
		searchMap.put("counterOrder", form.getCounterOrder());
		//会员手机
		searchMap.put("mobile",form.getMobile());
		//活动预约状态
		searchMap.put("state",form.getState());
		//测试区分
		searchMap.put("testType",form.getTestType());
		//coupon码
		searchMap.put("couponCode",form.getCouponCode());
		//下发区分
		searchMap.put("sendFlag",form.getSendFlag());
		//会员所属柜台
		searchMap.put("counterBelong",form.getCounterBelong());
		//更新活动明细
	   	int update= 0;
	   	Map<String, Object>  msgMap= new HashMap<String, Object>();
	   	 try {
			 update = bl.tran_batchUpdate(updateMap,searchMap);
			 if(update==1){//修改成功
	    		 msgMap.put("error", update);
	    	 }else if(update==2){//领用柜台错误
	    		 msgMap.put("error", update);
	    	 }else if(update==3){//领用开始时间错误
	    		 msgMap.put("error", update);
	    	 }else if(update==4){//领用结束时间错误
	    		 msgMap.put("error", update);
	    	 }else if(update==6){//领用结束时间不能小于当前日期
	    		 msgMap.put("error", update);
	    	 }else if(update==7){//领用结束时间不能小于开始时间
	    		 msgMap.put("error", update);
	    	 }
	   	 } catch (Exception e) {//操作失败
			logger.error(e.getMessage(),e);
			 msgMap.put("error", 5);
		}    	
	  //ajax返回结果
	  ConvertUtil.setResponseByAjax(response, msgMap);
	  return null;
	}
	/**
	 * 活动 Excel导出
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export() throws Exception {
		// 取得参数MAP
		Map<String, Object>  map= getComMap();
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		//活动编码 
		map.put("campaignCode", form.getCampaignCode());
		//单据号
		map.put("tradeNoIF",form.getTradeNoIF());
		//会员卡号
		map.put("memCode",form.getMemCode());
		//领用柜台
		map.put("counterGot",form.getCounterGot());
		//预约柜台
		map.put("counterOrder", form.getCounterOrder());
		//会员手机
		map.put("mobile",form.getMobile());
		//活动预约状态
		map.put("state",form.getState());
		//测试区分
		map.put("testType",form.getTestType());
		//coupon码
		map.put("couponCode",form.getCouponCode());
		//下发区分
		map.put("sendFlag",form.getSendFlag());
		//会员所属柜台
		map.put("counterBelong",form.getCounterBelong());
		// 取得会员档案信息List
		try {
			// 下载文件名称
			String zipName = binOLMOCOM01_BL.getResourceValue("BINOLCPACT06", language, "ACT06_exportName");
			 // 下载文件名称编码格式转换处理
	    	exportName = zipName+".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(bl.exportExcel(map), zipName+".xls"));
		} catch (Exception e) {
			//错误日志
			logger.error(e.getMessage(),e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	/**
	 * 活动 Excel导出
	 * 
	 * @return
	 * @throws Exception
	 */
	public String couPonExport() throws Exception {
		// 取得参数MAP
		Map<String, Object>  map= getComMap();
		String language = ConvertUtil.getString(map
				.get(CherryConstants.SESSION_LANGUAGE));
		//活动编码 
		map.put("campaignCode", form.getCampaignCode());
		// 取得会员档案信息List
		try {
			// 下载文件名称
			String zipName = binOLMOCOM01_BL.getResourceValue("BINOLCPACT06", language, "ACT06_couponInfo");
			 // 下载文件名称编码格式转换处理
	    	exportName = zipName+".zip";
			excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(bl.couponExportExcel(map), zipName+".xls"));
		} catch (Exception e) {
			//错误日志
			logger.error(e.getMessage(),e);
			this.addActionError(getText("EMO00022"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	/**
	 * 活动明细信息编辑
	 * @throws Exception 
	 * 
	 */
     public void updDetail() throws Exception{
    	 Map<String, Object>  updMap= getComMap();
    	 //领用柜台
    	 updMap.put("counterGot", form.getCounterCode());
    	//领用结束时间
    	 updMap.put("getFromTime", form.getFromTime());
    	 //领用结束时间
    	 updMap.put("getToTime", form.getEndTime());
    	 //更新次数
    	 updMap.put("modifyCount", form.getModifyCount());
    	 //更新时间
    	 updMap.put("modifyTime", form.getModifyTime());
    	 //活动预约id
    	 updMap.put("campOrderId",form.getCampOrderId());
    	 //更新前的领用柜台
    	 updMap.put("counterGotOld", form.getCounterGotOld());
    	 //更新前领用开始时间
    	 updMap.put("getFromTimeOld", form.getGetFromTimeOld());
    	//更新前领用结束时间
    	 updMap.put("getToTimeOld", form.getGetToTimeOld());
    	//下发区分
    	 updMap.put("sendFlag", form.getSendFlag());
    	 //单据号
    	 updMap.put("tradeNoIF", form.getTradeNoIF());
    	//单据状态
    	 updMap.put("campState", form.getCampState());
    	 //更新活动明细
    	 int update= 0;
    	 Map<String, Object>  msgMap= new HashMap<String, Object>();
    	 try {
			 update = bl.tran_updateDetailInfo(updMap);
			 if(update==0){//修改的信息没有变更
	    		 msgMap.put("error", update);
	    	 }else if(update==1){//修改成功
	    		 Map<String,Object> resultMap = bl.getEditInfo(updMap);
	    		 msgMap.putAll(resultMap);
	    		 msgMap.put("error", update);
	    	 }else if(update==2){//领用柜台错误
	    		 msgMap.put("error", update);
	    	 }else if(update==3){//领用结束时间错误
	    		 msgMap.put("error", update);
	    	 }else if(update==4){//领用结束时间不能小于开始时间
	    		 msgMap.put("error", update);
	    	 }else if(update==6){//领用结束时间不能小于当前日期
	    		 msgMap.put("error", update);
	    	 }else if(update==7){//信息已被其他人修改
	    		 msgMap.put("error", update);
	    	 }else if(update==8){//领用开始时间
	    		 msgMap.put("error", update);
	    	 }else if(update==9){//领用开始时间不能大于领用结束时间
	    		 msgMap.put("error", update);
	    	 }
		} catch (Exception e) {//操作失败
			logger.error(e.getMessage(),e);
			 msgMap.put("error", 5);
		}    	
 		//ajax返回结果
 		ConvertUtil.setResponseByAjax(response, msgMap);
     }
	/**
	 * 共通参数Map
	 * @return Map
	 */
	private Map<String,Object> getComMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 系统时间
		String sysTime = bl.getSYSDateTime();
		// 业务日期
		String busDate = bl.getBusDate(map);
		// 操作时间
		String optTime = busDate + " " + sysTime.substring(11,19);
		map.put(CampConstants.OPT_TIME, optTime);
		map.put(CherryConstants.UPDATE_TIME, sysTime);
		map.put(CherryConstants.CREATE_TIME, sysTime);
		map.put(CherryConstants.BUSINESS_DATE, busDate);
		map.put(CherryConstants.CREATEPGM, "BINOLCPACT06");
		map.put(CherryConstants.UPDATEPGM, "BINOLCPACT06");
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		map.put("couponFlag", cm14bl.getConfigValue("1138", 
				userInfo.getBIN_OrganizationInfoID()+"", userInfo.getBIN_BrandInfoID()+""));
		//预约开始时间
		map.put("startDate",form.getStartDate());
		//预约结束时间
		map.put("endDate", form.getEndDate());
		return map;
	}
	/**
	 * 验证提交的参数
	 * 
	 * @return boolean
	 * 			验证结果
	 * 
	 */
	private boolean validateForm() {
		boolean isCorrect = true;
		boolean isCorrect_send = true;
		// 开始日期
		String startDate = form.getStartDate();
		// 结束日期
		String endDate = form.getEndDate();
		//发货开始日期
		String startSendDate = form.getSendStartDate();
		//发货截止日期
		String endSendDate = form.getEndDate();
		//开始日期验证
		if (startDate != null && !"".equals(startDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		if (startSendDate != null && !"".equals(startSendDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(startSendDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00001")}));
				isCorrect = false;
			}
		}
		//结束日期验证
		if (endDate != null && !"".equals(endDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endDate)) {
				this.addActionError(getText("ECM00008", new String[]{getText("PCM00002")}));
				isCorrect = false;
			}
		}
		if (endSendDate != null && !"".equals(endSendDate)) {
			// 日期格式验证
			if(!CherryChecker.checkDate(endSendDate)) {
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
		if (isCorrect_send && startSendDate != null && !"".equals(startSendDate)&&
				endSendDate != null && !"".equals(endSendDate)) {
			// 开始日期在结束日期之后
			if(CherryChecker.compareDate(startSendDate, endSendDate) > 0) {
				this.addActionError(getText("ECM00019"));
				isCorrect_send = false;
			}
		}
	    return isCorrect&&isCorrect_send;
	}
	
	/**
	 * 单据批量修改验证
	 * @return
	 */
	private boolean validateBatchUpdate(){
		// 验证结果
		boolean result = true;
		// 领用柜台验证
		if("0".equals(form.getCounterType())){
			if(null!=form.getBatchCounter()){
				if (CherryChecker.isEmptyString(form.getBatchCounter(), true)){
					// 领用柜台不能为空
					this.addFieldError("batchCounter", getText("ACT000100"));
					result = false;
				}	
			}
		}
		if("0".equals(form.getReferFromType())&&"0".equals(form.getReferToType())){
			//开始时间结束时间比较
			if(!CherryChecker.isNullOrEmpty(form.getBatchFromTime())&&!CherryChecker.isNullOrEmpty(form.getBatchToTime())){
				//  领用结束必须大于领用结束
				if (CherryChecker.compareDate(form.getBatchFromTime(), form.getBatchToTime()) > 0) {
					// 领用结束必须大于领用结束
					this.addFieldError("batchToTime", getText("ECM00027",new String[]{getText("ACT000104"),getText("ACT000103")}));
					result = false;
				}
			}
		}
		if("1".equals(form.getReferFromType())){
			//领用开始日期调整验证
			if(null!=form.getReferFromValue()){
				if (CherryChecker.isEmptyString(form.getReferFromValue(), true)){
					// 不能为空
					this.addFieldError("referFromValue", getText("ACT000100"));
					result = false;
				}else if(!CherryChecker.isNumeric(form.getReferFromValue())){
					// 格式错误
					this.addFieldError("referFromValue", getText("ACT000101"));
					result = false;
				}
			}
		}else{
			// 领用开始时间
			if(null!=form.getBatchFromTime()){
				if (CherryChecker.isEmptyString(form.getBatchFromTime(), true)){
					// 领用开始时间不能为空
					this.addFieldError("batchFromTime", getText("ACT000100"));
					result = false;
				}else if(!CherryChecker.checkDate(form.getBatchFromTime(),CherryConstants.DATE_PATTERN)){
					// 领用开始时间必须为日期格式
					this.addFieldError("batchFromTime", getText("ACT000102",new String[]{getText("ACT000103")}));
					result = false;
				}
			}
		}
		if("1".equals(form.getReferToType())){
			//领用结束日期调整验证
			if(null!=form.getReferToValue()){
				if (CherryChecker.isEmptyString(form.getReferToValue(), true)){
					// 不能为空
					this.addFieldError("referToValue", getText("ACT000100"));
					result = false;
				}else if(!CherryChecker.isNumeric(form.getReferToValue())){
					// 格式错误
					this.addFieldError("referToValue", getText("ACT000101"));
					result = false;
				}
			}
		}else{
			// 领用结束时间
			if(null!=form.getBatchToTime()){
				if (CherryChecker.isEmptyString(form.getBatchToTime(), true)){
					// 领用结束时间不能为空
					this.addFieldError("batchToTime", getText("ACT000100"));
					result = false;
				}else if(!CherryChecker.checkDate(form.getBatchToTime(),CherryConstants.DATE_PATTERN)){
					// 领用结束时间必须为日期格式
					this.addFieldError("batchToTime", getText("ACT000102",new String[]{getText("ACT000104")}));
					result = false;
				}
			}
		}
		return result;
	}
	@Override
	public BINOLCPACT06_Form getModel() {
		return form;
	}

}
