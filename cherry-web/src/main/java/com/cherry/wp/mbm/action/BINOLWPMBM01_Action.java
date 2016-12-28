package com.cherry.wp.mbm.action;

import com.cherry.cm.cmbeans.CounterInfo;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.core.*;
import com.cherry.cm.util.*;
import com.cherry.ct.common.interfaces.BINOLCTCOM10_IF;
import com.cherry.mb.mbm.bl.*;
import com.cherry.mb.mbm.service.BINOLMBMBM11_Service;
import com.cherry.mb.ptm.bl.BINOLMBPTM02_BL;
import com.cherry.mb.ptm.bl.BINOLMBPTM03_BL;
import com.cherry.wp.common.interfaces.BINOLWPCM01_IF;
import com.cherry.wp.mbm.form.BINOLWPMBM01_Form;
import com.cherry.wp.mbm.interfaces.BINOLWPMBM01_IF;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员管理Action
 * 
 * @author WangCT
 * @version 1.0 2014/08/15
 */
public class BINOLWPMBM01_Action extends BaseAction implements ModelDriven<BINOLWPMBM01_Form> {

	private static final long serialVersionUID = -4852325595046193280L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLWPMBM01_Action.class);
	
	/** 会员管理BL **/
	@Resource
	private BINOLWPMBM01_IF binOLWPMBM01_BL;
	
	/** 会员详细画面BL */
	@Resource
	private BINOLMBMBM02_BL binOLMBMBM02_BL;
	
	/** 标准区域共通BL */
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	
	/** 会员搜索画面BL */
	@Resource
	private BINOLMBMBM09_BL binOLMBMBM09_BL;
	
	/** 会员添加画面BL **/
	@Resource
	private BINOLMBMBM11_BL binOLMBMBM11_BL;
	
	/** 会员资料修改画面BL **/
	@Resource
	private BINOLMBMBM06_BL binOLMBMBM06_BL;
	
	/** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 会员销售详细画面BL */
	@Resource(name="binOLMBMBM05_BL")
	private BINOLMBMBM05_BL binOLMBMBM05_BL;
	
	/** 导出excel共通BL **/
	@Resource(name="binOLCM37_BL")
	private BINOLCM37_BL binOLCM37_BL;
	
	@Resource
	private BINOLWPCM01_IF binOLWPCM01_BL;
	
	/** 会员积分详细画面BL */
	@Resource
	private BINOLMBMBM04_BL binOLMBMBM04_BL;
	
	/** 查询积分信息BL */
	@Resource
	private BINOLMBPTM02_BL binOLMBPTM02_BL;
	
	/** 查询积分明细信息BL */
	@Resource
	private BINOLMBPTM03_BL binOLMBPTM03_BL;
	
	/** 会员添加画面Service **/
	@Resource
	private BINOLMBMBM11_Service binOLMBMBM11_Service;
	
	private String birthFormat;
	
	@Resource
	private BINOLCTCOM10_IF binOLCTCOM10_IF;
	
	/**
	 * 会员一览画面初始化
	 * 
	 * @return 会员一览画面
	 */
	public String init() {
		
		if(form.getMemCode() != null && !"".equals(form.getMemCode())) {
			form.setMemCodeQ(form.getMemCode());
		} else {
			if(form.getSearchStr() != null && !"".equals(form.getSearchStr())) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 不是总部的场合
				if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
					// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				}
				map.put("memberCode", form.getSearchStr());
				int count = binOLWPMBM01_BL.getMemberCount(map);
//				mobileRule = binOLCM14_BL.getConfigValue("2012", organizationInfoId, brandInfoId);
				if(count > 0) {
					form.setMemCodeQ(form.getSearchStr());
				} else {
					form.setMobilePhoneQ(form.getSearchStr());
				}
			}
		}
		return SUCCESS;
	}
	
	/**
	 * AJAX取得会员信息List
	 * 
	 * @return 会员一览画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 会员卡号
		if(form.getMemCodeQ() != null && !"".equals(form.getMemCodeQ())) {
			map.put("memberCode", form.getMemCodeQ());
		}
		// 会员姓名
		if(form.getMemNameQ() != null && !"".equals(form.getMemNameQ())) {
			map.put("memberName", form.getMemNameQ());
		}
		// 会员手机
		String mobilePhone = form.getMobilePhoneQ();
		// 会员手机加密
		if (mobilePhone != null && !"".equals(mobilePhone)) {
			map.put("mobilePhone", CherrySecret.encryptData(userInfo.getBrandCode(),mobilePhone));
		}
		// 会员生日
		String brithMonth = form.getBirthDayMonthQ();
		String brithDate = form.getBirthDayDateQ();
		if(brithMonth != null && !"".equals(brithMonth) 
				&& brithDate != null && !"".equals(brithDate)) {
			if(brithMonth.length() == 1) {
				brithMonth = "0" + brithMonth;
			}
			if(brithDate.length() == 1) {
				brithDate = "0" + brithDate;
			}
			map.put("birthDay", brithMonth+brithDate);
		}
		Map<String, Object> resultMap = binOLWPMBM01_BL.searchMemList(map);
		if(resultMap != null && !resultMap.isEmpty()) {
			int count = Integer.parseInt(resultMap.get("total").toString());
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				memberInfoList = (List<Map<String, Object>>)resultMap.get("list");
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 会员详细信息画面
	 * 
	 * @return 会员详细信息画面
	 */
	public String detail() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		map.put("memberInfoId", form.getMemberInfoId());
		map.put("memCode", form.getMemCode());
		// 取得会员信息
		memberInfoMap = binOLMBMBM02_BL.getMemberInfo(map);
		return SUCCESS;
	}
	
	/**
	 * 添加会员画面初始化
	 * 
	 * @return 添加会员画面
	 */
	public String addInit() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		
		// 取得会员扩展信息List
		extendPropertyList = binOLMBMBM09_BL.getExtendProperty(map);
		
		// 取得区域List
		reginList = binOLCM08_BL.getReginList(map);
		
		// 柜台信息
		CounterInfo counterInfo = (CounterInfo) session
				.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
		map.put("organizationId", counterInfo.getOrganizationId());
		
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 查询柜台营业员信息
		// 获取BA列表,根据配置项来取用是考勤的员工还是忽略考勤的员工
		String attendanceFlag=binOLCM14_BL.getWebposConfigValue("9044", organizationInfoId, brandInfoId);
		if(null == attendanceFlag || "".equals(attendanceFlag)){
			attendanceFlag = "N";
		}
		if("N".equals(attendanceFlag)){
			employeeList = binOLWPCM01_BL.getBAInfoList(map);
		}else{
			employeeList = binOLWPCM01_BL.getActiveBAList(map);
		}
		birthFormat = binOLCM14_BL.getWebposConfigValue("9012", organizationInfoId, brandInfoId);
		//是否需要手机验证
		isNeedCheck = "".equals(ConvertUtil.getString(binOLCM14_BL.getWebposConfigValue("9049", organizationInfoId, brandInfoId)))?"N":binOLCM14_BL.getWebposConfigValue("9049", organizationInfoId, brandInfoId);
		//是否开启手机卡号同步处理
		form.setCardMobileSyn(binOLCM14_BL.getWebposConfigValue("9040", organizationInfoId, brandInfoId));
		//生日是否为必填
		form.setBirthFlag(binOLCM14_BL.getWebposConfigValue("9041", organizationInfoId, brandInfoId));
//		if("2".equals(ConvertUtil.getString(birthFormat))){
//			String birthMonthValue = form.getBirthDayMonthQ();
//			String birthDayValue = form.getBirthDayDateQ();
//			if(birthMonthValue.length() == 1){
//				birthMonthValue = "0" + birthMonthValue;
//			}
//			if(birthDayValue.length() == 1){
//				birthDayValue = "0" + birthDayValue;
//			}
//			String birthDay = birthMonthValue + birthDayValue;
//			map.put("birthDay", birthDay);
//		}else{
//			map.put("birth", form.getDgBirthday());
//		}
		return SUCCESS;
	}
	
	/**
	 * 添加会员处理
	 * 
	 * @return 添加会员画面
	 */
	public String add() {
		
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			if(!CherryChecker.isNullOrEmpty(form.getBirthDayMonthQ())&&!CherryChecker.isNullOrEmpty(form.getBirthDayDateQ())){
				String month=null;
				String date=null;;
				if(form.getBirthDayMonthQ().length()==1){
					month="0"+form.getBirthDayMonthQ();
				}
				if(form.getBirthDayDateQ().length()==1){
					date="0"+form.getBirthDayDateQ();
				}
				String birthDate=month+date;
//				form.setBirth(birthDate);
				map.put("birthDay", birthDate);
			}
			
			// 剔除map中的空值
			map = CherryUtil.removeEmptyVal(map);
			//
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织代码
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 品牌代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLWPMBM01");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLWPMBM01");
			// 操作员工
			map.put("modifyEmployee", userInfo.getEmployeeCode());
			
			// 柜台信息
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			String counterCode = counterInfo.getCounterCode();
			map.put("counterCode", counterCode);
			// 操作柜台
			map.put("modifyCounter", counterCode);
			// 添加会员信息
			binOLWPMBM01_BL.addMem(map);
			this.addActionMessage(getText("ICM00001"));
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	this.addActionError(getText("ECM00005"));
            }
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}
	
	public String updateInit() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 所属品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 取得会员信息
		memberInfoMap = binOLMBMBM06_BL.getMemberInfo(map);

		//得到云POS配置项会员修改时是否允许修改生日
		String isAllowUpdate = binOLCM14_BL.getWebposConfigValue("9053",ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
		memberInfoMap.put("isAllowUpdate",isAllowUpdate);

		// 取得会员扩展信息List
		extendPropertyList = (List)memberInfoMap.get("memPagerList");
		
		// 取得区域List
		reginList = binOLCM08_BL.getReginList(map);
		

		//是否需要手机验证
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		isNeedCheck = binOLCM14_BL.getWebposConfigValue("9037", organizationInfoId, brandInfoId);
		//是否开启手机卡号同步处理
		form.setCardMobileSyn(binOLCM14_BL.getWebposConfigValue("9040", organizationInfoId, brandInfoId));
		// 会员地址信息
		Map<String, Object> memberAddressInfo = (Map)memberInfoMap.get("memberAddressInfo");
		if(memberAddressInfo != null) {
			// 省ID
			Object provinceId = memberAddressInfo.get("provinceId");
			// 省存在的场合，取得城市List
			if(provinceId != null && !"".equals(provinceId.toString())) {
				//城市Id
				Object cityId = memberAddressInfo.get("cityId");
				if(cityId != null && !"".equals(cityId.toString())){
					// 区域Id
					map.put("regionId", cityId);
					countyList=binOLCM08_BL.getAllChildStandRegionList(map);
				}else{
					// 区域Id
					map.put("regionId", provinceId);
					cityList = binOLCM08_BL.getAllChildStandRegionList(map);
				}
				
				
			}
		}
		return SUCCESS;
	}
	
	public String update() {
		
		try {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 剔除map中的空值
			map = CherryUtil.removeEmptyVal(map);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 组织代码
			map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
			// 品牌代码
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLMBMBM06");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLMBMBM06");
			// 操作员工
			map.put("modifyEmployee", userInfo.getEmployeeCode());
			CounterInfo counterInfo = (CounterInfo) session
					.get(CherryConstants.SESSION_CHERRY_COUNTERINFO);
			String counterCode = counterInfo.getCounterCode();
			// 操作柜台
			map.put("modifyCounter", counterCode);
			// 更新会员基本信息
			binOLMBMBM06_BL.tran_updMemberInfo(map);
			this.addActionMessage(getText("ICM00001"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());
            }else{
            	this.addActionError(getText("ECM00005"));
            }
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return null;
	}
	
	/**
	 * 会员购买记录画面初期处理
	 * 
	 * @return 会员购买记录画面
	 */
	public String saleInit() throws Exception {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);	
		String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
		form.setDisplayFlag(binOLCM14_BL.getConfigValue("1087", orgId, brandId));
		
	    //系统配置项是否显示唯一码
        form.setSysConfigShowUniqueCode(binOLCM14_BL.getConfigValue("1140", orgId, brandId));
		return SUCCESS;
	}
	
	/**
	 * 查询会员销售信息List
	 * 
	 * @return 会员详细画面
	 */
	public String searchSaleRecord() throws Exception {
	    // 登陆用户信息
        UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);   
        String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
        String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
        //系统配置项是否显示唯一码
        form.setSysConfigShowUniqueCode(binOLCM14_BL.getConfigValue("1140", orgId, brandId));

		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		if(form.getDisplayFlag() != null && "0".equals(form.getDisplayFlag())) {
			// 统计会员销售信息
			saleCountInfo = binOLMBMBM05_BL.getSaleCount(map);
			if(saleCountInfo != null && !saleCountInfo.isEmpty()) {
				int count = (Integer)saleCountInfo.get("count");
				form.setITotalDisplayRecords(count);
				form.setITotalRecords(count);
				if(count != 0) {
					// 查询会员销售信息List
					saleRecordList = binOLMBMBM05_BL.getSaleRecordList(map);
				}
			}
			return "SaleRecord";
		} else {
			// 统计会员销售信息
			saleCountInfo = binOLMBMBM05_BL.getSaleCount(map);
			// 查询会员销售明细总数
			int count = binOLMBMBM05_BL.getSaleDetailCount(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				// 查询会员销售明细List
				saleRecordList = binOLMBMBM05_BL.getSaleDetailList(map);
			}
			return "SaleRecordDetail";
		}
	}
	
	/**
	 * 查询会员销售明细信息
	 * 
	 * @return 会员详细画面
	 */
	public String searchSaleDetail() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 查询会员销售明细信息
		saleRecordDetail = binOLMBMBM05_BL.getSaleRecordDetail(map);
		
	    //系统配置项是否显示唯一码
		String orgId = ConvertUtil.getString(saleRecordDetail.get("organizationInfoId"));
        String brandId = ConvertUtil.getString(saleRecordDetail.get("brandInfoId"));
        form.setSysConfigShowUniqueCode(binOLCM14_BL.getConfigValue("1140", orgId, brandId));
		form.setOpenCounter(binOLCM14_BL.getWebposConfigValue("9039", orgId, brandId));
		return SUCCESS;
	}
	
	/**
     * 导出销售明细Excel
     */
    public String exportSale() throws Exception {
        
        try {
        	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
    		// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		}
    		// dataTable上传的参数设置到map
    		ConvertUtil.setForm(form, map);
        	
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		
    		// 统计会员销售信息
    		saleCountInfo = binOLMBMBM05_BL.getSaleCount(map);
    		if(saleCountInfo != null && !saleCountInfo.isEmpty()) {
    			String quantity = new DecimalFormat("0").format(saleCountInfo.get("quantity"));
        		String amount = new DecimalFormat("0.00").format(saleCountInfo.get("amount"));
        		StringBuffer strBuf = new StringBuffer();
        		strBuf.append(CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_sumQuantity")+"："+quantity);
        		strBuf.append("\0\0\0\0");
        		strBuf.append(CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_sumAmount")+"："+amount);
        		map.put("header", strBuf.toString());
    		}
    		
    		String zipName = CherryUtil.getResourceValue("BINOLMBMBM05", language, "downloadFileName");
            downloadFileName = zipName+".zip";
            map.put("sheetName", CherryUtil.getResourceValue("BINOLMBMBM05", language, "sheetName"));
            
            // 取得所有权限
			Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
			// 取得对应菜单下的权限
			CherryMenu doc = (CherryMenu)xmldocumentmap.get("MB");
			String saleCouFlag = null;
			if(doc != null && doc.getChildMenuByID("BINOLMBMBM10_29") != null) {
				saleCouFlag = "1";
			}
			List<String[]> titleRowList = new ArrayList<String[]>();
			titleRowList.add(new String[]{"memberCode", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_saleMemCode"), "15", "", ""});
			titleRowList.add(new String[]{"prtName", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_proName"), "15", "", ""});
			titleRowList.add(new String[]{"unitCode", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_unitCode"), "15", "", ""});
			titleRowList.add(new String[]{"barCode", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_barCode"), "15", "", ""});
			titleRowList.add(new String[]{"saleType", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_saleType"), "15", "", "1055"});
			titleRowList.add(new String[]{"detailSaleType", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_detailSaleType"), "15", "", "1106"});
			titleRowList.add(new String[]{"quantity", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_quantity"), "15", "int", ""});
			titleRowList.add(new String[]{"pricePay", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_pricePay"), "15", "float", ""});
			titleRowList.add(new String[]{"billCode", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_billCode"), "30", "", ""});
			if(saleCouFlag != null && "1".equals(saleCouFlag)) {
				titleRowList.add(new String[]{"departCode", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_counterCode"), "15", "", ""});
				titleRowList.add(new String[]{"departName", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_counterName"), "15", "", ""});
			}
			titleRowList.add(new String[]{"employeeCode", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_employeeCode"), "15", "", ""});
			titleRowList.add(new String[]{"employeeName", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_employeeName"), "15", "", ""});
			titleRowList.add(new String[]{"saleTime", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_saleTime"), "20", "", ""});
			titleRowList.add(new String[]{"activityName", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_campaignName"), "15", "", ""});
			titleRowList.add(new String[]{"saleExt", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_saleExt"), "15", "", ""});
            
            String organizationInfoID = ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID));
            String brandInfoID = ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID));
            String configValue = binOLCM14_BL.getConfigValue("1140", organizationInfoID, brandInfoID);
            if(configValue.equals(CherryConstants.SYSTEM_CONFIG_ENABLE)){
                //导出数据列数组（有唯一码）
                titleRowList.add(new String[]{ "uniqueCode", CherryUtil.getResourceValue("BINOLMBMBM05", language, "binolmbmbm05_UniqueCode"), "25", "", "" });
            }
            map.put("titleRows",titleRowList.toArray(new String[][]{}));
            
     		map.put(CherryConstants.SORT_ID, "saleTime desc");
            byte[] byteArray = binOLCM37_BL.exportExcel(map, binOLMBMBM05_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("EMO00022"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
	/**
	 * 查询会员报表信息
	 * 
	 */
	public void memReport() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberInfoId", form.getMemberInfoId());
		map.put("TOP", form.getTickLength());
		Map<String, Object> memReport = binOLWPMBM01_BL.getMemReport(map);
		ConvertUtil.setResponseByAjax(response, memReport);
	}
	
	/**
	 * 会员积分详细画面初期处理
	 * 
	 * @return 会员积分详细画面
	 */
	public String pointInit() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 查询会员积分信息
		memPointInfo = binOLMBMBM04_BL.getMemberPointInfo(map);
		// 取得积分规则信息List
		campaignNameList = binOLMBPTM02_BL.getCampaignNameList(map);
		
		String orgId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String brandId = String.valueOf(userInfo.getBIN_BrandInfoID());
		form.setDisplayFlag(binOLCM14_BL.getConfigValue("1087", orgId, brandId));
		return SUCCESS;
	}
	
	/**
	 * 查询积分明细信息
	 * 
	 * @return 会员积分详细画面
	 */
	public String searchPointInfo() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		
		if(form.getChangeDateStart() != null && !"".equals(form.getChangeDateStart())) {
			map.put("changeDateStart", DateUtil.suffixDate(form.getChangeDateStart(), 0));
		}
		if(form.getChangeDateEnd() != null && !"".equals(form.getChangeDateEnd())) {
			map.put("changeDateEnd", DateUtil.suffixDate(form.getChangeDateEnd(), 1));
		}
		
		if(form.getSubCampaignId() != null && !"".equals(form.getSubCampaignId())) {
			String[] subCampaignIds = form.getSubCampaignId().split("_");
			if(subCampaignIds.length == 2) {
				map.put("subCampaignId", subCampaignIds[0]);
				map.put("pointRuleType", subCampaignIds[1]);
			} else {
				map.remove("subCampaignId");
			}
		}
		// 规则ID
		String subCampaignId = (String)map.get("subCampaignId");
		// 产品厂商ID
		String prtVendorId = (String)map.get("prtVendorId");
		// 关联退货单号
		String relevantSRCode = (String)map.get("relevantSRCode");
		if((subCampaignId != null && !"".equals(subCampaignId)) 
				|| (prtVendorId != null && !"".equals(prtVendorId))
				|| (relevantSRCode != null && !"".equals(relevantSRCode))) {
			map.put("detailCondition", "1");
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		if(form.getDisplayFlag() != null && "0".equals(form.getDisplayFlag())) {
			// 查询积分明细信息总数
			int count = binOLMBMBM04_BL.getPointDetailCount(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				// 查询积分明细信息List
				pointDetailList = binOLMBMBM04_BL.getPointDetailList(map);
			}
			return "pointRecord";
		} else {
			// 查询积分明细信息总数
			int count = binOLMBMBM04_BL.getPointDetail2Count(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
			if(count != 0) {
				// 查询积分明细信息List
				pointDetailList = binOLMBMBM04_BL.getPointDetail2List(map);
			}
			return "pointRecordDetail";
		}
	}
	
	/**
	 * 查询积分明细信息
	 * 
	 * @return 会员积分详细画面
	 */
	public String searchPointDetail() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 取得积分明细信息
		pointInfoMap = binOLMBPTM03_BL.getPointInfoDetail(map);
		return SUCCESS;
	}
	
	/**
     * 导出Excel
     */
    public String exportPoint() throws Exception {
        
        try {
        	Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
        	
        	// 登陆用户信息
    		UserInfo userInfo = (UserInfo) session
    				.get(CherryConstants.SESSION_USERINFO);
    		// 所属组织
    		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
    		// 不是总部的场合
    		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
    			// 所属品牌
    			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
    		}
    		
    		if(form.getChangeDateStart() != null && !"".equals(form.getChangeDateStart())) {
    			map.put("changeDateStart", DateUtil.suffixDate(form.getChangeDateStart(), 0));
    		}
    		if(form.getChangeDateEnd() != null && !"".equals(form.getChangeDateEnd())) {
    			map.put("changeDateEnd", DateUtil.suffixDate(form.getChangeDateEnd(), 1));
    		}
    		
    		if(form.getSubCampaignId() != null && !"".equals(form.getSubCampaignId())) {
    			String[] subCampaignIds = form.getSubCampaignId().split("_");
    			if(subCampaignIds.length == 2) {
    				map.put("subCampaignId", subCampaignIds[0]);
    				map.put("pointRuleType", subCampaignIds[1]);
    			} else {
    				map.remove("subCampaignId");
    			}
    		}
    		// 规则ID
    		String subCampaignId = (String)map.get("subCampaignId");
    		// 产品厂商ID
    		String prtVendorId = (String)map.get("prtVendorId");
    		// 关联退货单号
    		String relevantSRCode = (String)map.get("relevantSRCode");
    		if((subCampaignId != null && !"".equals(subCampaignId)) 
    				|| (prtVendorId != null && !"".equals(prtVendorId))
    				|| (relevantSRCode != null && !"".equals(relevantSRCode))) {
    			map.put("detailCondition", "1");
    		}
    		// dataTable上传的参数设置到map
    		ConvertUtil.setForm(form, map);
        	
    		String language = (String)session.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
    		String zipName = binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "downloadFileName");
            downloadFileName = zipName+".zip";
            map.put("sheetName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "sheetName"));
            
            // 取得所有权限
			Map<String, Object> xmldocumentmap = (Map)session.get(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
			// 取得对应菜单下的权限
			CherryMenu doc = (CherryMenu)xmldocumentmap.get("MB");
			String saleCouFlag = null;
			if(doc != null && doc.getChildMenuByID("BINOLMBMBM10_29") != null) {
				saleCouFlag = "1";
			}
			List<String[]> titleRowList = new ArrayList<String[]>();
			titleRowList.add(new String[]{"memCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_memberCode"), "15", "", ""});
			titleRowList.add(new String[]{"proName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_proName"), "15", "", ""});
			titleRowList.add(new String[]{"unitCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_unitCode"), "15", "", ""});
			titleRowList.add(new String[]{"barCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_barCode"), "15", "", ""});
			titleRowList.add(new String[]{"saleType", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_saleType"), "15", "", "1106"});
			titleRowList.add(new String[]{"price", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_proPrice"), "15", "float", ""});
			titleRowList.add(new String[]{"quantity", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_proQuantity"), "15", "int", ""});
			titleRowList.add(new String[]{"point", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_proPoint"), "15", "", ""});
			titleRowList.add(new String[]{"pointType", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_pointType"), "15", "", "1214"});
			if(saleCouFlag != null && "1".equals(saleCouFlag)) {
				titleRowList.add(new String[]{"departCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_counterCode"), "15", "", ""});
				titleRowList.add(new String[]{"departName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_counterName"), "15", "", ""});
			}
			titleRowList.add(new String[]{"changeDate", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_changeDate"), "20", "", ""});
			titleRowList.add(new String[]{"combCampaignName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_combCampaignName"), "15", "", ""});
			titleRowList.add(new String[]{"mainCampaignName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_mainCampaignName"), "15", "", ""});
			titleRowList.add(new String[]{"subCampaignName", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_subCampaignName"), "15", "", ""});
			titleRowList.add(new String[]{"reason", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_reason"), "15", "", ""});
			titleRowList.add(new String[]{"billCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_billCode"), "30", "", ""});
			titleRowList.add(new String[]{"srCode", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_relevantSRCode"), "30", "", ""});
			titleRowList.add(new String[]{"srTime", binOLCM37_BL.getResourceValue("BINOLMBMBM04", language, "binolmbmbm04_relevantSRTime"), "20", "", ""});
            map.put("titleRows", titleRowList.toArray(new String[][]{}));
     		map.put(CherryConstants.SORT_ID, "changeDate desc");
            byte[] byteArray = binOLCM37_BL.exportExcel(map, binOLMBMBM04_BL);
            excelStream = new ByteArrayInputStream(binOLCM37_BL.fileCompression(byteArray, zipName+".xls")); 
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.addActionError(getText("EMO00022"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
        }
        return SUCCESS;
    }
	
    /**
     * 
     * 更换手机(卡号)前校验
     * 
     * */
    public void validateMobileCheckUpdateInit() throws Exception {

		// 会员卡号必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemCode())) {
			this.addFieldError("memCode", getText("ECM00009",new String[]{getText("PMB00055")}));
		} else {
			// 会员卡号不能超过20位验证
			if(form.getMemCode().length() > 20) {
				this.addFieldError("memCode", getText("ECM00020",new String[]{getText("PMB00055"),"20"}));
			} else {
				boolean checkResult = true;
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				String memCodeRule = binOLCM14_BL.getConfigValue("1070", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
				if(memCodeRule != null && !"".equals(memCodeRule)) {
					if (!form.getMemCode().matches(memCodeRule)){
			    		this.addFieldError("memCode", getText("EMB00017"));
			    		checkResult = false;
			    	}
				}
				if(checkResult) {
					String memCodeFunName = binOLCM14_BL.getConfigValue("1133", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
					if(memCodeFunName != null && !"".equals(memCodeFunName)) {
						if(!CherryChecker.checkMemCodeByFun(memCodeFunName, form.getMemCode())) {
							this.addFieldError("memCode", getText("EMB00017"));
							checkResult = false;
						}
					}
				}
			}
		}
		// 会员姓名必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemName())) {
			this.addFieldError("memName", getText("ECM00009",new String[]{getText("PMB00049")}));
		} else {
			// 会员姓名不能超过30位验证
			if(form.getMemName().length() > 30) {
				this.addFieldError("memName", getText("ECM00020",new String[]{getText("PMB00049"),"30"}));
			}
		}
		// 手机验证
		if(form.getMobilePhone() != null && !"".equals(form.getMobilePhone())) {
			boolean isCheck = true;
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String mobileRule = binOLCM14_BL.getConfigValue("1090", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
			if(mobileRule != null && !"".equals(mobileRule)) {
				if (!form.getMobilePhone().matches(mobileRule)){
		    		this.addFieldError("mobilePhone", getText("ECM00086"));
		    		isCheck = false;
		    	}
			}
			if(isCheck) {
				if(form.getMobilePhoneOld() == null || !form.getMobilePhone().equals(form.getMobilePhoneOld())) {
					if(binOLCM14_BL.isConfigOpen("1301", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()))) {
						Map<String, Object> map = new HashMap<String, Object>();
						// 所属组织
						map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
						// 所属品牌
						map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
						// 会员手机号
						map.put("mobilePhone", form.getMobilePhone());
						List<String> memMobileList = binOLMBMBM11_Service.getMemMobile(map);
						if(memMobileList != null && !memMobileList.isEmpty()) {
							this.addFieldError("mobilePhone", getText("MBM00052"));
						}
					}
				}
			}
		}
		// 电话验证
		if(form.getTelephone() != null && !"".equals(form.getTelephone())) {
			if(!CherryChecker.isTelValid(form.getTelephone())) {
				this.addFieldError("telephone", getText("ECM00085"));
			}
		}
		// 手机和电话必须填一项验证
		if((form.getMobilePhone() == null || "".equals(form.getMobilePhone())) 
				&& (form.getTelephone() == null || "".equals(form.getTelephone()))) {
			this.addFieldError("mobilePhone", getText("ECM00087"));
		}
		// 邮箱验证
		if(form.getEmail() != null && !"".equals(form.getEmail())) {
			if(!CherryChecker.isEmail(form.getEmail())) {
				this.addFieldError("email", getText("ECM00069"));
			}
		}
		// 生日必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBirth())) {
			this.addFieldError("birth", getText("ECM00009",new String[]{getText("PMB00051")}));
		} else {
			// 生日日期格式验证
			if(!CherryChecker.checkDate(form.getBirth())) {
				this.addFieldError("birth", getText("ECM00022",new String[]{getText("PMB00051")}));
			}
		}
		// 性别必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getGender())) {
			this.addFieldError("gender", getText("ECM00009",new String[]{getText("PMB00064")}));
		}
		// 推荐会员卡号不能超过20位验证
		if(form.getReferrer() != null && !"".equals(form.getReferrer())) {
			if(form.getReferrer().length() > 20) {
				this.addFieldError("referrer", getText("ECM00020",new String[]{getText("PMB00057"),"20"}));
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 推荐会员卡号
				map.put("memCode", form.getReferrer());
				// 推荐会员卡号是否存在验证
				String memberInfoId = binOLMBMBM11_BL.getMemberInfoId(map);
				if(memberInfoId == null || "".equals(memberInfoId)) {
					this.addFieldError("referrer", getText("EMB00016"));
				} else {
					if(memberInfoId.equals(form.getMemberInfoId())) {
						this.addFieldError("referrer", getText("EMB00023"));
					}
				}
			}
		}
		// 身份证不能超过18位验证
		if(form.getIdentityCard() != null && !"".equals(form.getIdentityCard())) {
			if(form.getIdentityCard().length() > 18) {
				this.addFieldError("identityCard", getText("ECM00020",new String[]{getText("PMB00054"),"18"}));
			}
		}
		// 微博号不能超过30位验证
		if(form.getBlogId() != null && !"".equals(form.getBlogId())) {
			if(form.getBlogId().length() > 30) {
				this.addFieldError("blogId", getText("ECM00020",new String[]{getText("PMB00058"),"30"}));
			}
		}
		// 微信号不能超过30位验证
		if(form.getMessageId() != null && !"".equals(form.getMessageId())) {
			if(form.getMessageId().length() > 30) {
				this.addFieldError("messageId", getText("ECM00020",new String[]{getText("PMB00059"),"30"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo1() != null && !"".equals(form.getMemo1())) {
			if(form.getMemo1().length() > 512) {
				this.addFieldError("memo1", getText("ECM00020",new String[]{getText("PMB00070"),"512"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo2() != null && !"".equals(form.getMemo2())) {
			if(form.getMemo2().length() > 512) {
				this.addFieldError("memo2", getText("ECM00020",new String[]{getText("PMB00072"),"512"}));
			}
		}
		// 会员地址不能超过100位验证
		if(form.getAddress() != null && !"".equals(form.getAddress())) {
			if(form.getAddress().length() > 100) {
				this.addFieldError("address", getText("ECM00020",new String[]{getText("PMB00060"),"100"}));
			}
		}
		// 邮编不能超过10位验证
		if(form.getPostcode() != null && !"".equals(form.getPostcode())) {
			if(form.getPostcode().length() > 10) {
				this.addFieldError("postcode", getText("ECM00020",new String[]{getText("PMB00061"),"10"}));
			}
		}
		if(!this.hasFieldErrors()) {
			// 换卡的场合，新卡唯一验证
			if(!form.getMemCodeOld().equals(form.getMemCode())) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 会员卡号
				map.put("memCode", form.getMemCode());
				// 通过会员卡号查询会员信息
				Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(map);
				if(memberInfoMap != null) {
					Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
					if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
						String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
						form.setNewMemberInfoId(memberInfoId);
						Object newAddressInfoId = memberInfoMap.get("addressInfoId");
						if(newAddressInfoId != null) {
							form.setNewAddressInfoId(newAddressInfoId.toString());
						}
					} else {
//						String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
//						if(form.getMemberInfoId().equals(memberInfoId)) {
//							form.setNewMemberInfoId(memberInfoId);
//						} else {
//							this.addFieldError("memCode", getText("EMB00015"));
//						}
						this.addFieldError("memCode", getText("EMB00015"));
					}
				}
			}
		}
	
    }
    
    /**
     * 
     * 手机验证前校验
     * */
    public void validateMobileCheckInit() throws Exception {
		
		// 会员卡号必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemCode())) {
			this.addFieldError("memCode", getText("ECM00009",new String[]{getText("PMB00055")}));
		} else {
			// 会员卡号不能超过20位验证
			if(form.getMemCode().length() > 20) {
				this.addFieldError("memCode", getText("ECM00020",new String[]{getText("PMB00055"),"20"}));
			} else {
				boolean checkResult = true;
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				String memCodeRule = binOLCM14_BL.getConfigValue("1070", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
				if(memCodeRule != null && !"".equals(memCodeRule)) {
					if (!form.getMemCode().matches(memCodeRule)){
						this.addFieldError("memCode", getText("EMB00017"));
						checkResult = false;
			    	}
				}
				if(checkResult) {
					String memCodeFunName = binOLCM14_BL.getConfigValue("1133", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
					if(memCodeFunName != null && !"".equals(memCodeFunName)) {
						if(!CherryChecker.checkMemCodeByFun(memCodeFunName, form.getMemCode())) {
							this.addFieldError("memCode", getText("EMB00017"));
							checkResult = false;
						}
					}
				}
			}
		}
		// 会员姓名必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemName())) {
			this.addFieldError("memName", getText("ECM00009",new String[]{getText("PMB00049")}));
		} else {
			// 会员姓名不能超过30位验证
			if(form.getMemName().length() > 30) {
				this.addFieldError("memName", getText("ECM00020",new String[]{getText("PMB00049"),"30"}));
			}
		}
		// 发卡BA必须入力验证
 		if(CherryChecker.isNullOrEmpty(form.getEmployeeId())) {
			this.addFieldError("employeeId", getText("ECM00009",new String[]{getText("PMB00063")}));
		}
		// 手机验证
		if(form.getMobilePhone() != null && !"".equals(form.getMobilePhone())) {
			boolean isCheck = true;
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String mobileRule = binOLCM14_BL.getConfigValue("1090", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
			if(mobileRule != null && !"".equals(mobileRule)) {
				if (!form.getMobilePhone().matches(mobileRule)){
		    		this.addFieldError("mobilePhone", getText("ECM00086"));
		    		isCheck = false;
		    	}
			}
			if(isCheck) {
				if(binOLCM14_BL.isConfigOpen("1301", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()))) {
					Map<String, Object> map = new HashMap<String, Object>();
					// 所属组织
					map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
					// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					// 会员手机号
					map.put("mobilePhone", form.getMobilePhone());
					List<String> memMobileList = binOLMBMBM11_Service.getMemMobile(map);
					if(memMobileList != null && !memMobileList.isEmpty()) {
						this.addFieldError("mobilePhone", getText("MBM00052"));
					}
				}
			}
		}
		// 电话验证
		if(form.getTelephone() != null && !"".equals(form.getTelephone())) {
			if(!CherryChecker.isTelValid(form.getTelephone())) {
				this.addFieldError("telephone", getText("ECM00085"));
			}
		}
		// 手机和电话必须填一项验证
		if((form.getMobilePhone() == null || "".equals(form.getMobilePhone())) 
				&& (form.getTelephone() == null || "".equals(form.getTelephone()))) {
			this.addFieldError("mobilePhone", getText("ECM00087"));
		}
		// 邮箱验证
		if(form.getEmail() != null && !"".equals(form.getEmail())) {
			if(!CherryChecker.isEmail(form.getEmail())) {
				this.addFieldError("email", getText("ECM00069"));
			}
		}
//		Boolean f1=CherryChecker.isNullOrEmpty(form.getBirth());
//		Boolean f2=CherryChecker.isNullOrEmpty(form.getBirthDayMonthQ());
//		Boolean f3=CherryChecker.isNullOrEmpty(form.getBirthDayDateQ());
//		Boolean f4=CherryChecker.isNullOrEmpty(form.getBirthDayMonthQ())&&CherryChecker.isNullOrEmpty(form.getBirthDayDateQ());
		//云POS会员入会生日必填/选填
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String birthFlag=binOLCM14_BL.getWebposConfigValue("9041", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		if("Y".equals(birthFlag)){
			// 生日必须入力验证
			if(CherryChecker.isNullOrEmpty(form.getBirth())&&(CherryChecker.isNullOrEmpty(form.getBirthDayMonthQ())&&CherryChecker.isNullOrEmpty(form.getBirthDayDateQ()))) {
				this.addFieldError("birth", getText("ECM00009",new String[]{getText("PMB00051")}));
			} else {
				if(form.getBirth()!=null){
					// 生日日期格式验证
					if(!CherryChecker.checkDate(form.getBirth())) {
						this.addFieldError("birth", getText("ECM00022",new String[]{getText("PMB00051")}));
					}
				}
			}
		}else{
			if(form.getBirth()!=null && form.getBirth().length()>0){
				// 生日日期格式验证
				if(!CherryChecker.checkDate(form.getBirth())) {
					this.addFieldError("birth", getText("ECM00022",new String[]{getText("PMB00051")}));
				}
			}
		}
		// 性别必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getGender())) {
			this.addFieldError("gender", getText("ECM00009",new String[]{getText("PMB00064")}));
		}
		// 推荐会员卡号不能超过20位验证
		if(form.getReferrer() != null && !"".equals(form.getReferrer())) {
			if(form.getReferrer().length() > 20) {
				this.addFieldError("referrer", getText("ECM00020",new String[]{getText("PMB00057"),"20"}));
			} else {
				if(form.getReferrer().equals(form.getMemCode())) {
					this.addFieldError("referrer", getText("EMB00023"));
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					// 所属组织
					map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
					// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					// 推荐会员卡号
					map.put("memCode", form.getReferrer());
					// 推荐会员卡号是否存在验证
					String memberInfoId = binOLMBMBM11_BL.getMemberInfoId(map);
					if(memberInfoId == null || "".equals(memberInfoId)) {
						this.addFieldError("referrer", getText("EMB00016"));
					}
				}
			}
		}
		// 身份证不能超过18位验证
		if(form.getIdentityCard() != null && !"".equals(form.getIdentityCard())) {
			if(form.getIdentityCard().length() > 18) {
				this.addFieldError("identityCard", getText("ECM00020",new String[]{getText("PMB00054"),"18"}));
			}
		}
		// 微博号不能超过30位验证
		if(form.getBlogId() != null && !"".equals(form.getBlogId())) {
			if(form.getBlogId().length() > 30) {
				this.addFieldError("blogId", getText("ECM00020",new String[]{getText("PMB00058"),"30"}));
			}
		}
		// 微信号不能超过30位验证
		if(form.getMessageId() != null && !"".equals(form.getMessageId())) {
			if(form.getMessageId().length() > 30) {
				this.addFieldError("messageId", getText("ECM00020",new String[]{getText("PMB00059"),"30"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo1() != null && !"".equals(form.getMemo1())) {
			if(form.getMemo1().length() > 512) {
				this.addFieldError("memo1", getText("ECM00020",new String[]{getText("PMB00070"),"512"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo2() != null && !"".equals(form.getMemo2())) {
			if(form.getMemo2().length() > 512) {
				this.addFieldError("memo2", getText("ECM00020",new String[]{getText("PMB00072"),"512"}));
			}
		}
		// 会员地址不能超过100位验证
		if(form.getAddress() != null && !"".equals(form.getAddress())) {
			if(form.getAddress().length() > 100) {
				this.addFieldError("address", getText("ECM00020",new String[]{getText("PMB00060"),"100"}));
			}
		}
		// 邮编不能超过10位验证
		if(form.getPostcode() != null && !"".equals(form.getPostcode())) {
			if(form.getPostcode().length() > 10) {
				this.addFieldError("postcode", getText("ECM00020",new String[]{getText("PMB00061"),"10"}));
			}
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 会员卡号
			map.put("memCode", form.getMemCode());
			// 通过会员卡号查询会员信息
			Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(map);
			if(memberInfoMap != null) {
				Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
				if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
					Object memberLevel = memberInfoMap.get("memberLevel");
					if(memberLevel != null) {
						String levelName = (String)memberInfoMap.get("levelName");
						form.setOldLevelName(levelName);
						form.setOldMemberLevel(memberLevel.toString());
					}
					String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
					form.setMemberInfoId(memberInfoId);
				} else {
					this.addFieldError("memCode", getText("EMB00015"));
				}
			}
		}
	}
	/**
	 * 
	 * 添加会员前字段验证处理
	 * 
	 */
	public void validateAdd() throws Exception {
		
		// 会员卡号必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemCode())) {
			this.addFieldError("memCode", getText("ECM00009",new String[]{getText("PMB00055")}));
		} else {
			// 会员卡号不能超过20位验证
			if(form.getMemCode().length() > 20) {
				this.addFieldError("memCode", getText("ECM00020",new String[]{getText("PMB00055"),"20"}));
			} else {
				boolean checkResult = true;
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				String memCodeRule = binOLCM14_BL.getConfigValue("1070", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
				if(memCodeRule != null && !"".equals(memCodeRule)) {
					if (!form.getMemCode().matches(memCodeRule)){
						this.addFieldError("memCode", getText("EMB00017"));
						checkResult = false;
			    	}
				}
				if(checkResult) {
					String memCodeFunName = binOLCM14_BL.getConfigValue("1133", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
					if(memCodeFunName != null && !"".equals(memCodeFunName)) {
						if(!CherryChecker.checkMemCodeByFun(memCodeFunName, form.getMemCode())) {
							this.addFieldError("memCode", getText("EMB00017"));
							checkResult = false;
						}
					}
				}
			}
		}
		// 会员姓名必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemName())) {
			this.addFieldError("memName", getText("ECM00009",new String[]{getText("PMB00049")}));
		} else {
			// 会员姓名不能超过30位验证
			if(form.getMemName().length() > 30) {
				this.addFieldError("memName", getText("ECM00020",new String[]{getText("PMB00049"),"30"}));
			}
		}
		// 发卡BA必须入力验证
 		if(CherryChecker.isNullOrEmpty(form.getEmployeeId())) {
			this.addFieldError("employeeId", getText("ECM00009",new String[]{getText("PMB00063")}));
		}
		// 手机验证
		if(form.getMobilePhone() != null && !"".equals(form.getMobilePhone())) {
			boolean isCheck = true;
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String mobileRule = binOLCM14_BL.getConfigValue("1090", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
			if(mobileRule != null && !"".equals(mobileRule)) {
				if (!form.getMobilePhone().matches(mobileRule)){
		    		this.addFieldError("mobilePhone", getText("ECM00086"));
		    		isCheck = false;
		    	}
			}
			if(isCheck) {
				if(binOLCM14_BL.isConfigOpen("1301", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()))) {
					Map<String, Object> map = new HashMap<String, Object>();
					// 所属组织
					map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
					// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					// 会员手机号
					map.put("mobilePhone", form.getMobilePhone());
					List<String> memMobileList = binOLMBMBM11_Service.getMemMobile(map);
					if(memMobileList != null && !memMobileList.isEmpty()) {
						this.addFieldError("mobilePhone", getText("MBM00052"));
					}
				}
			}
		}
		// 电话验证
		if(form.getTelephone() != null && !"".equals(form.getTelephone())) {
			if(!CherryChecker.isTelValid(form.getTelephone())) {
				this.addFieldError("telephone", getText("ECM00085"));
			}
		}
		// 手机和电话必须填一项验证
		if((form.getMobilePhone() == null || "".equals(form.getMobilePhone())) 
				&& (form.getTelephone() == null || "".equals(form.getTelephone()))) {
			this.addFieldError("mobilePhone", getText("ECM00087"));
		}
		// 邮箱验证
		if(form.getEmail() != null && !"".equals(form.getEmail())) {
			if(!CherryChecker.isEmail(form.getEmail())) {
				this.addFieldError("email", getText("ECM00069"));
			}
		}
//		Boolean f1=CherryChecker.isNullOrEmpty(form.getBirth());
//		Boolean f2=CherryChecker.isNullOrEmpty(form.getBirthDayMonthQ());
//		Boolean f3=CherryChecker.isNullOrEmpty(form.getBirthDayDateQ());
//		Boolean f4=CherryChecker.isNullOrEmpty(form.getBirthDayMonthQ())&&CherryChecker.isNullOrEmpty(form.getBirthDayDateQ());
		//云POS会员入会生日必填/选填
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		String birthFlag=binOLCM14_BL.getWebposConfigValue("9041", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
		if("Y".equals(birthFlag)){
			// 生日必须入力验证
			if(CherryChecker.isNullOrEmpty(form.getBirth())&&(CherryChecker.isNullOrEmpty(form.getBirthDayMonthQ())&&CherryChecker.isNullOrEmpty(form.getBirthDayDateQ()))) {
				this.addFieldError("birth", getText("ECM00009",new String[]{getText("PMB00051")}));
			} else {
				if(form.getBirth()!=null){
					// 生日日期格式验证
					if(!CherryChecker.checkDate(form.getBirth())) {
						this.addFieldError("birth", getText("ECM00022",new String[]{getText("PMB00051")}));
					}
				}
			}
		}else{
			if(form.getBirth()!=null && form.getBirth().length()>0){
				// 生日日期格式验证
				if(!CherryChecker.checkDate(form.getBirth())) {
					this.addFieldError("birth", getText("ECM00022",new String[]{getText("PMB00051")}));
				}
			}
		}
		// 性别必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getGender())) {
			this.addFieldError("gender", getText("ECM00009",new String[]{getText("PMB00064")}));
		}
		// 推荐会员卡号不能超过20位验证
		if(form.getReferrer() != null && !"".equals(form.getReferrer())) {
			if(form.getReferrer().length() > 20) {
				this.addFieldError("referrer", getText("ECM00020",new String[]{getText("PMB00057"),"20"}));
			} else {
				if(form.getReferrer().equals(form.getMemCode())) {
					this.addFieldError("referrer", getText("EMB00023"));
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					// 所属组织
					map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
					// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
					// 推荐会员卡号
					map.put("memCode", form.getReferrer());
					// 推荐会员卡号是否存在验证
					String memberInfoId = binOLMBMBM11_BL.getMemberInfoId(map);
					if(memberInfoId == null || "".equals(memberInfoId)) {
						this.addFieldError("referrer", getText("EMB00016"));
					}
				}
			}
		}
		// 身份证不能超过18位验证
		if(form.getIdentityCard() != null && !"".equals(form.getIdentityCard())) {
			if(form.getIdentityCard().length() > 18) {
				this.addFieldError("identityCard", getText("ECM00020",new String[]{getText("PMB00054"),"18"}));
			}
		}
		// 微博号不能超过30位验证
		if(form.getBlogId() != null && !"".equals(form.getBlogId())) {
			if(form.getBlogId().length() > 30) {
				this.addFieldError("blogId", getText("ECM00020",new String[]{getText("PMB00058"),"30"}));
			}
		}
		// 微信号不能超过30位验证
		if(form.getMessageId() != null && !"".equals(form.getMessageId())) {
			if(form.getMessageId().length() > 30) {
				this.addFieldError("messageId", getText("ECM00020",new String[]{getText("PMB00059"),"30"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo1() != null && !"".equals(form.getMemo1())) {
			if(form.getMemo1().length() > 512) {
				this.addFieldError("memo1", getText("ECM00020",new String[]{getText("PMB00070"),"512"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo2() != null && !"".equals(form.getMemo2())) {
			if(form.getMemo2().length() > 512) {
				this.addFieldError("memo2", getText("ECM00020",new String[]{getText("PMB00072"),"512"}));
			}
		}
		// 会员地址不能超过100位验证
		if(form.getAddress() != null && !"".equals(form.getAddress())) {
			if(form.getAddress().length() > 100) {
				this.addFieldError("address", getText("ECM00020",new String[]{getText("PMB00060"),"100"}));
			}
		}
		// 邮编不能超过10位验证
		if(form.getPostcode() != null && !"".equals(form.getPostcode())) {
			if(form.getPostcode().length() > 10) {
				this.addFieldError("postcode", getText("ECM00020",new String[]{getText("PMB00061"),"10"}));
			}
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			// 会员卡号
			map.put("memCode", form.getMemCode());
			// 通过会员卡号查询会员信息
			Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(map);
			if(memberInfoMap != null) {
				Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
				if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
					Object memberLevel = memberInfoMap.get("memberLevel");
					if(memberLevel != null) {
						String levelName = (String)memberInfoMap.get("levelName");
						form.setOldLevelName(levelName);
						form.setOldMemberLevel(memberLevel.toString());
					}
					String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
					form.setMemberInfoId(memberInfoId);
				} else {
					this.addFieldError("memCode", getText("EMB00015"));
				}
			}
		}
	}
	
	/**
	 * 
	 * 会员资料修改前字段验证处理
	 * 
	 */
	public void validateUpdate() throws Exception {
		
		// 会员卡号必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemCode())) {
			this.addFieldError("memCode", getText("ECM00009",new String[]{getText("PMB00055")}));
		} else {
			// 会员卡号不能超过20位验证
			if(form.getMemCode().length() > 20) {
				this.addFieldError("memCode", getText("ECM00020",new String[]{getText("PMB00055"),"20"}));
			} else {
				boolean checkResult = true;
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				String memCodeRule = binOLCM14_BL.getConfigValue("1070", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
				if(memCodeRule != null && !"".equals(memCodeRule)) {
					if (!form.getMemCode().matches(memCodeRule)){
			    		this.addFieldError("memCode", getText("EMB00017"));
			    		checkResult = false;
			    	}
				}
				if(checkResult) {
					String memCodeFunName = binOLCM14_BL.getConfigValue("1133", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
					if(memCodeFunName != null && !"".equals(memCodeFunName)) {
						if(!CherryChecker.checkMemCodeByFun(memCodeFunName, form.getMemCode())) {
							this.addFieldError("memCode", getText("EMB00017"));
							checkResult = false;
						}
					}
				}
			}
		}
		// 会员姓名必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getMemName())) {
			this.addFieldError("memName", getText("ECM00009",new String[]{getText("PMB00049")}));
		} else {
			// 会员姓名不能超过30位验证
			if(form.getMemName().length() > 30) {
				this.addFieldError("memName", getText("ECM00020",new String[]{getText("PMB00049"),"30"}));
			}
		}
		// 手机验证
		if(form.getMobilePhone() != null && !"".equals(form.getMobilePhone())) {
			boolean isCheck = true;
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			String mobileRule = binOLCM14_BL.getConfigValue("1090", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()));
			if(mobileRule != null && !"".equals(mobileRule)) {
				if (!form.getMobilePhone().matches(mobileRule)){
		    		this.addFieldError("mobilePhone", getText("ECM00086"));
		    		isCheck = false;
		    	}
			}
			if(isCheck) {
				if(form.getMobilePhoneOld() == null || !form.getMobilePhone().equals(form.getMobilePhoneOld())) {
					if(binOLCM14_BL.isConfigOpen("1301", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(userInfo.getBIN_BrandInfoID()))) {
						Map<String, Object> map = new HashMap<String, Object>();
						// 所属组织
						map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
						// 所属品牌
						map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
						// 会员手机号
						map.put("mobilePhone", form.getMobilePhone());
						List<String> memMobileList = binOLMBMBM11_Service.getMemMobile(map);
						if(memMobileList != null && !memMobileList.isEmpty()) {
							this.addFieldError("mobilePhone", getText("MBM00052"));
						}
					}
				}
			}
		}
		// 电话验证
		if(form.getTelephone() != null && !"".equals(form.getTelephone())) {
			if(!CherryChecker.isTelValid(form.getTelephone())) {
				this.addFieldError("telephone", getText("ECM00085"));
			}
		}
		// 手机和电话必须填一项验证
		if((form.getMobilePhone() == null || "".equals(form.getMobilePhone())) 
				&& (form.getTelephone() == null || "".equals(form.getTelephone()))) {
			this.addFieldError("mobilePhone", getText("ECM00087"));
		}
		// 邮箱验证
		if(form.getEmail() != null && !"".equals(form.getEmail())) {
			if(!CherryChecker.isEmail(form.getEmail())) {
				this.addFieldError("email", getText("ECM00069"));
			}
		}

		//只有会员修改时允许修改会员生日，才对会员生日进行校验
		if(form.getIsAllowUpdate().equals("1")){
			// 生日必须入力验证
			if(CherryChecker.isNullOrEmpty(form.getBirth())) {
				this.addFieldError("birth", getText("ECM00009",new String[]{getText("PMB00051")}));
			} else {
				// 生日日期格式验证
				if(!CherryChecker.checkDate(form.getBirth())) {
					this.addFieldError("birth", getText("ECM00022",new String[]{getText("PMB00051")}));
				}
			}
		}
		// 性别必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getGender())) {
			this.addFieldError("gender", getText("ECM00009",new String[]{getText("PMB00064")}));
		}
		// 推荐会员卡号不能超过20位验证
		if(form.getReferrer() != null && !"".equals(form.getReferrer())) {
			if(form.getReferrer().length() > 20) {
				this.addFieldError("referrer", getText("ECM00020",new String[]{getText("PMB00057"),"20"}));
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 推荐会员卡号
				map.put("memCode", form.getReferrer());
				// 推荐会员卡号是否存在验证
				String memberInfoId = binOLMBMBM11_BL.getMemberInfoId(map);
				if(memberInfoId == null || "".equals(memberInfoId)) {
					this.addFieldError("referrer", getText("EMB00016"));
				} else {
					if(memberInfoId.equals(form.getMemberInfoId())) {
						this.addFieldError("referrer", getText("EMB00023"));
					}
				}
			}
		}
		// 身份证不能超过18位验证
		if(form.getIdentityCard() != null && !"".equals(form.getIdentityCard())) {
			if(form.getIdentityCard().length() > 18) {
				this.addFieldError("identityCard", getText("ECM00020",new String[]{getText("PMB00054"),"18"}));
			}
		}
		// 微博号不能超过30位验证
		if(form.getBlogId() != null && !"".equals(form.getBlogId())) {
			if(form.getBlogId().length() > 30) {
				this.addFieldError("blogId", getText("ECM00020",new String[]{getText("PMB00058"),"30"}));
			}
		}
		// 微信号不能超过30位验证
		if(form.getMessageId() != null && !"".equals(form.getMessageId())) {
			if(form.getMessageId().length() > 30) {
				this.addFieldError("messageId", getText("ECM00020",new String[]{getText("PMB00059"),"30"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo1() != null && !"".equals(form.getMemo1())) {
			if(form.getMemo1().length() > 512) {
				this.addFieldError("memo1", getText("ECM00020",new String[]{getText("PMB00070"),"512"}));
			}
		}
		// 备注不能超过512位验证
		if(form.getMemo2() != null && !"".equals(form.getMemo2())) {
			if(form.getMemo2().length() > 512) {
				this.addFieldError("memo2", getText("ECM00020",new String[]{getText("PMB00072"),"512"}));
			}
		}
		// 会员地址不能超过100位验证
		if(form.getAddress() != null && !"".equals(form.getAddress())) {
			if(form.getAddress().length() > 100) {
				this.addFieldError("address", getText("ECM00020",new String[]{getText("PMB00060"),"100"}));
			}
		}
		// 邮编不能超过10位验证
		if(form.getPostcode() != null && !"".equals(form.getPostcode())) {
			if(form.getPostcode().length() > 10) {
				this.addFieldError("postcode", getText("ECM00020",new String[]{getText("PMB00061"),"10"}));
			}
		}
		if(!this.hasFieldErrors()) {
			// 换卡的场合，新卡唯一验证
			if(!form.getMemCodeOld().equals(form.getMemCode())) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				// 会员卡号
				map.put("memCode", form.getMemCode());
				// 通过会员卡号查询会员信息
				Map<String, Object> memberInfoMap = binOLMBMBM11_BL.getMemberInfoByMemCode(map);
				if(memberInfoMap != null) {
					Object memInfoRegFlg = memberInfoMap.get("memInfoRegFlg");
					if(memInfoRegFlg != null && "1".equals(memInfoRegFlg.toString())) {
						String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
						form.setNewMemberInfoId(memberInfoId);
						Object newAddressInfoId = memberInfoMap.get("addressInfoId");
						if(newAddressInfoId != null) {
							form.setNewAddressInfoId(newAddressInfoId.toString());
						}
					} else {
//						String memberInfoId = String.valueOf(memberInfoMap.get("memberInfoId"));
//						if(form.getMemberInfoId().equals(memberInfoId)) {
//							form.setNewMemberInfoId(memberInfoId);
//						} else {
//							this.addFieldError("memCode", getText("EMB00015"));
//						}
						this.addFieldError("memCode", getText("EMB00015"));
					}
				}
			}
		}
	}
	
	/**
	 * 发送短信
	 */
	public void sendSMS(){
		try {
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌代码
			String brandCode = userInfo.getBrandCode();
			// 品牌ID
			int brandInfoId = userInfo.getBIN_BrandInfoID();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 组织代码
    		map.put("orgCode", userInfo.getOrganizationInfoCode());
			// 品牌ID
			map.put("brandInfoId", brandInfoId);
			// 品牌代码
			map.put("brandCode", brandCode);
			// 沟通类型
			map.put("messageType",1);
			String sendFlag = "";
			int type = ConvertUtil.getInt(request.getParameter("checkType"));
			String phoneNumber = ConvertUtil.getString(request.getParameter("mobilePhone"));
			// 信息接收号码列表
			map.put("resCodeList", phoneNumber);
			//短信通道类型
			map.put("smsChannel", 2);
			binOLWPMBM01_BL.setContents(map, type);
			String contents = ConvertUtil.getString(map.get("contents"));
			if(!"".equals(contents) && contents !=null){
				map.put("contents", contents);
				binOLCTCOM10_IF.tran_sendWPMsg(map);
			}
			ConvertUtil.setResponseByAjax(response, sendFlag);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 初始化入会短信验证界面
	 * @return
	 */
	public String mobileCheckInit(){
		return SUCCESS;
	}
	
	/**
	 * 初始化修改短信验证界面
	 * @return
	 */
	public String mobileCheckUpdateInit(){
		//若手机一样则普通资料修改
		String phoneNumber = ConvertUtil.getString(request.getParameter("mobilePhone"));
		String phoneNumberOld = ConvertUtil.getString(request.getParameter("mobilePhoneOld"));
		if(phoneNumber.equals(phoneNumberOld)){
			return "update";
		}else{
			return "changeCard";
		}
	}
	
	/**
	 * 短信验证
	 * @throws Exception 
	 */
	public void couponCheck() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
    	
    	// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		//获取手机号码以及coupon码
		String phoneNumber = ConvertUtil.getString(request.getParameter("mobilePhone"));
		String couponCode = ConvertUtil.getString(request.getParameter("couponCode"));
		String phoneNumberOld = ConvertUtil.getString(request.getParameter("mobilePhoneOld"));
		String couponCode2 = ConvertUtil.getString(request.getParameter("couponCodeOld"));
		String checkFlag = "";
		if((couponCode2 == null || couponCode2.equals("")) &&
				(phoneNumberOld == null || phoneNumberOld.equals(""))){
			checkFlag = binOLWPMBM01_BL.couponCheck(phoneNumber, couponCode, map);
		}else{
			checkFlag = binOLWPMBM01_BL.cardChangeCheck(phoneNumber, couponCode, phoneNumberOld, couponCode2, map);
		}
		ConvertUtil.setResponseByAjax(response, checkFlag);
	}
	
	/** 是否开启手机验证 */
	private String isNeedCheck;
	
	public String getIsNeedCheck() {
		return isNeedCheck;
	}

	public void setIsNeedCheck(String isNeedCheck) {
		this.isNeedCheck = isNeedCheck;
	}

	/** 会员手机 */
	private String mobilePhone;
	
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {	
		this.mobilePhone = mobilePhone;
	}
	
	/** 会员旧手机 */
	private String mobilePhoneOld;
	
	public String getMobilePhoneOld() {
		return mobilePhoneOld;
	}

	public void setMobilePhoneOld(String mobilePhoneOld) {
		this.mobilePhoneOld = mobilePhoneOld;
	}

	/** 会员信息List */
	private List<Map<String, Object>> memberInfoList;
	
	/** 会员详细信息 */
	private Map memberInfoMap;
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 城市List */
	private List<Map<String, Object>> cityList;
	
	/** 区县List */
	private List<Map<String, Object>> countyList;
	
	public List<Map<String, Object>> getCountyList() {
		return countyList;
	}

	public void setCountyList(List<Map<String, Object>> countyList) {
		this.countyList = countyList;
	}

	/** 会员扩展信息List */
	private List<Map<String, Object>> extendPropertyList;
	
	/** 营业员List */
	private List<Map<String, Object>> employeeList;
	
	/** 会员销售信息List */
	private List<Map<String, Object>> saleRecordList;
	
	/** 会员销售明细信息 */
	private Map saleRecordDetail;
	
	/** 会员销售统计信息 */
	private Map saleCountInfo;
	
	/** 会员积分信息 */
	private Map memPointInfo;
	
	/** 积分规则信息List */
	private List<Map<String, Object>> campaignNameList;
	
	/** 积分明细信息List */
	private List<Map<String, Object>> pointDetailList;
	
	/** 会员积分明细信息 */
	private Map pointInfoMap;
	
	/** Excel输入流 */
    private InputStream excelStream;

    /** 下载文件名 */
    private String downloadFileName;
	
	public List<Map<String, Object>> getMemberInfoList() {
		return memberInfoList;
	}

	public void setMemberInfoList(List<Map<String, Object>> memberInfoList) {
		this.memberInfoList = memberInfoList;
	}

	public Map getMemberInfoMap() {
		return memberInfoMap;
	}

	public void setMemberInfoMap(Map memberInfoMap) {
		this.memberInfoMap = memberInfoMap;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, Object>> getExtendPropertyList() {
		return extendPropertyList;
	}

	public void setExtendPropertyList(List<Map<String, Object>> extendPropertyList) {
		this.extendPropertyList = extendPropertyList;
	}

	public List<Map<String, Object>> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Map<String, Object>> employeeList) {
		this.employeeList = employeeList;
	}

	public List<Map<String, Object>> getSaleRecordList() {
		return saleRecordList;
	}

	public void setSaleRecordList(List<Map<String, Object>> saleRecordList) {
		this.saleRecordList = saleRecordList;
	}

	public Map getSaleRecordDetail() {
		return saleRecordDetail;
	}

	public void setSaleRecordDetail(Map saleRecordDetail) {
		this.saleRecordDetail = saleRecordDetail;
	}

	public Map getSaleCountInfo() {
		return saleCountInfo;
	}

	public void setSaleCountInfo(Map saleCountInfo) {
		this.saleCountInfo = saleCountInfo;
	}

	public Map getMemPointInfo() {
		return memPointInfo;
	}

	public void setMemPointInfo(Map memPointInfo) {
		this.memPointInfo = memPointInfo;
	}

	public List<Map<String, Object>> getCampaignNameList() {
		return campaignNameList;
	}

	public void setCampaignNameList(List<Map<String, Object>> campaignNameList) {
		this.campaignNameList = campaignNameList;
	}

	public List<Map<String, Object>> getPointDetailList() {
		return pointDetailList;
	}

	public void setPointDetailList(List<Map<String, Object>> pointDetailList) {
		this.pointDetailList = pointDetailList;
	}

	public Map getPointInfoMap() {
		return pointInfoMap;
	}

	public void setPointInfoMap(Map pointInfoMap) {
		this.pointInfoMap = pointInfoMap;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getDownloadFileName() throws Exception {
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/** 会员管理Form **/
	private BINOLWPMBM01_Form form = new BINOLWPMBM01_Form();

	@Override
	public BINOLWPMBM01_Form getModel() {
		return form;
	}

	public String getBirthFormat() {
		return birthFormat;
	}

	public void setBirthFormat(String birthFormat) {
		this.birthFormat = birthFormat;
	}
	
}
