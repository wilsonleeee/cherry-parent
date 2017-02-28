/*		
 * @(#)BINOLSSPRM68_Action.java     1.0 2013/10/10		
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

package com.cherry.ss.prm.action;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM39_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.cm.util.FileUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM03_IF;
import com.cherry.mo.common.bl.BINOLMOCOM01_BL;
import com.cherry.ss.common.PromotionConstants;
import com.cherry.ss.prm.bl.BINOLSSPRM13_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM68_BL;
import com.cherry.ss.prm.form.BINOLSSPRM68_Form;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 智能促销
 * @author lipc
 * @version 1.0 2013.10.10
 */
public class BINOLSSPRM68_Action extends BaseAction implements ModelDriven<BINOLSSPRM68_Form> {

	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM68_Action.class);

	private static final long serialVersionUID = -4155552754712330635L;

	private static final String SESSION_KEY = "PRM_RULE_PAGE";

	private BINOLSSPRM68_Form form = new BINOLSSPRM68_Form();

	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	@Resource
	private BINOLSSPRM13_BL binOLSSPRM13_BL;

	@Resource(name="binOLSSPRM68_BL")
	private BINOLSSPRM68_BL prm68_BL;

	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	@Resource
	private BINOLMOCOM01_BL binOLMOCOM01_BL;

	@Resource
	private BINOLCM03_BL binOLCM03_BL;

	/** 共通BL */
	@Resource
	private BINOLCM05_BL binolcm05_BL;

	@Resource
	protected BINOLCPCOM03_IF binolcpcom03IF;

	/** 会员检索条件转换共通BL **/
	@Resource
	private BINOLCM39_BL binOLCM39_BL;

	private String sendFlag;

	private String csrftoken;

	private int pageNo;

	private int step;

	private String opt;

	private String activeID;

	private int templateFlag;

	private Map<String,Object> pageTemp;

	private Map<String,String> pageA;

	private Map<String,String> pageB;

	private Map<String,String> pageC;

	private Map<String,String> pageD;

	/** Excel输入流 */
	private InputStream excelStream;

	/** 下载文件名 */
	private String downloadFileName;

	/**
	 * 页面初始化
	 *
	 * @return
	 * @throws Exception
	 */
	public String init(){
		// 默认第一页
		pageNo = pageNo == 0 ? 1: pageNo;
		Map<String,Object> comMap = getComMap();
		pageTemp = new HashMap<String, Object>();
		// 促销活动是否开启排除指定产品范围
		String exRangesFlag = binOLCM14_BL.getConfigValue("1368",
				ConvertUtil.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID)),
				ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID)));
		String couponFlag = binOLCM14_BL.getConfigValue("1403",
				ConvertUtil.getString(comMap.get(CherryConstants.ORGANIZATIONINFOID)),
				ConvertUtil.getString(comMap.get(CherryConstants.BRANDINFOID)));
		pageTemp.put("exRangesFlag", exRangesFlag);
		pageTemp.put("couponFlag", couponFlag);
		try {
			// 首次进入页面，初始化session
			if(step == 0){
				initSession(comMap,opt);
			}
			if(null == pageA){
				pageA = (Map<String,String>)session.get(SESSION_KEY + "A");
			}
			comMap.put(CherryConstants.BRANDINFOID, pageA.get(CherryConstants.BRANDINFOID));
			// 前进
			if(step == 1){
				// 保存当前页内容
				try {
					savePage(comMap,pageNo);
					if(pageNo == 5){
						this.addActionMessage(getText("ICM00001"));
						return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
					}else{
						if(!validate(comMap)){//验证不通过
							step = 0;
						}
					}
				} catch (CherryException e) {
					logger.error(e.getErrMessage(), e);
					this.addActionError(e.getErrMessage());
					return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
				}
			}
			pageNo += step;
			comMap.put("step",step);
			// 获取下一页展示信息
			initPage(comMap,pageNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//系统发生异常，请联系管理人员。
			this.addActionError(getText("ECM00036"));
			return CherryConstants.GLOBAL_ERROR;
		}
		return "BINOLSSPRM68";
	}

	/**
	 * session初始化
	 */
	private void initSession(Map<String,Object> map,String opt){
		// 清除session值
		session.remove(SESSION_KEY + "A");
		session.remove(SESSION_KEY + "B");
		session.remove(SESSION_KEY + "C");
		session.remove(SESSION_KEY + "D");
		pageA = new HashMap<String, String>();
		pageB = new HashMap<String, String>();
		pageC = new HashMap<String, String>();
		pageD = new HashMap<String, String>();
		if(!CampConstants.OPT_KBN1.equals(opt)){// 非新建
			Map<String,Object> info = prm68_BL.getActRuleInfo(map);
			if(null != info){
				for (Map.Entry<String, Object> entry : info.entrySet()) {
					String value = ConvertUtil.getString(entry.getValue());
					if("maxExecCount".equals(entry.getKey()) && "0".equals(value)){
						pageA.put(entry.getKey(), "");
					}else{
						pageA.put(entry.getKey(), ConvertUtil.getString(entry.getValue()));
					}
				}
			}
			if(null != sendFlag){
				pageA.put("sendFlag", sendFlag);
			}
			pageB.put("startTime",pageA.get("startTime"));
			pageB.put("endTime",pageA.get("endTime"));
			pageB.put("locationType",pageA.get("locationType"));
			pageB.put("ruleID",pageA.get("ruleID"));
			pageB.put(CampConstants.PLACE_JSON,pageA.get(CampConstants.PLACE_JSON));
			pageC.put("searchCode",pageA.get("searchCode"));
			pageC.put("memberType",pageA.get("memberType"));
			pageC.put("memberJson",pageA.get("memberJson"));
			pageC.put("memberCount",pageA.get("memberCount"));

			pageD.put("execFlag",pageA.get("execFlag"));
			pageD.put("ruleCondJson",pageA.get("ruleCondJson"));
			pageD.put("ruleResultJson",pageA.get("ruleResultJson"));
			pageD.put("exRanges",pageA.get("exRanges"));
		}
		pageA.put(CampConstants.OPT_KBN, opt);
		// 恢复session值
		session.put(SESSION_KEY + "A", pageA);
		session.put(SESSION_KEY + "B", pageB);
		session.put(SESSION_KEY + "C", pageC);
		session.put(SESSION_KEY + "D", pageD);
	}

	/**
	 * 加载下一页面信息
	 * @param PageNo
	 */
	@SuppressWarnings("unchecked")
	private void initPage(Map<String,Object> map,int PageNo){
		pageA = (Map<String,String>)session.get(SESSION_KEY + "A");
		pageB = (Map<String,String>)session.get(SESSION_KEY + "B");
		pageC = (Map<String,String>)session.get(SESSION_KEY + "C");
		pageD = (Map<String,String>)session.get(SESSION_KEY + "D");
		if(PageNo == 1){
			initPageA(map);
		}else if(PageNo == 2){
			initPageB(map,2);
		}else if(PageNo == 3){
			initPageC(map);
		}else if(PageNo == 4){
			initPageD(map);
		}else{
			initPageA(map);
			initPageB(map,5);
			initPageC(map);
			initPageD(map);
		}
	}
	
	/**
	 * 基础信息页初始化
	 *
	 * @param map
	 */
	private void initPageA(Map<String,Object> map){
		// 取得品牌list
		pageTemp.put("brandList", binolcm05_BL.getBrandList(session));

		pageTemp.put("prmActGrpList", prm68_BL.getActiveGrpList(map));
	}

	/**
	 * 时间地点页初始化
	 *
	 * @param map
	 * @param pageNo
	 */
	@SuppressWarnings("unchecked")
	private void initPageB(Map<String,Object> map,int pageNo){
			try {
				String[] startTime = ConvertUtil.getString(pageB.get("startTime")).split(" ");
				String[] endTime = ConvertUtil.getString(pageB.get("endTime")).split(" ");
				pageTemp.put("startDate", startTime[0]);
				pageTemp.put("endDate", endTime[0]);
				if(startTime.length > 1){
					pageTemp.put("startTime", startTime[1]);
				}
				if(endTime.length > 1){
					pageTemp.put("endTime", endTime[1]);
				}
				if(CampConstants.OPT_KBN2.equals(opt) && startTime != null && startTime.length > 0 && startTime[0] != null){// 编辑
					String now = DateUtil.date2String(new Date());
					int i = DateUtil.compareDate(now, startTime[0] );
					if(i > 0){// 活动开始
						pageTemp.put("disabled", true);
					}
				}
				if(pageNo == 5){
					//opt为空表示为促销活动一览
					String authorityFlag = binOLCM14_BL.getConfigValue("1352",
							ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),
							ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
					Integer createUser = ConvertUtil.getInt(pageA.get("createUser"));
					Integer currentUser = ConvertUtil.getInt(map.get("userID"));
					//开启活动权限以及当前用户不是创建用户时,一览显示当前用户权限柜台
					if(ConvertUtil.isBlank(opt)
							&&"1".equals(authorityFlag)
							&&(currentUser!=createUser)){
						String locationType = pageA.get("locationType");

						try {
							List<Map<String,Object>> resultPlaceList= prm68_BL.getReturnPlaceJson(map,locationType);
							pageTemp.put(CampConstants.PLACE_JSON, resultPlaceList);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							this.addActionError(e.getMessage());
						}

					}else{
						String palceJson = pageB.get(CampConstants.PLACE_JSON);
						List<Map<String, Object>> palceList = (List<Map<String, Object>>) JSONUtil
								.deserialize(palceJson);
						List<Map<String, Object>> newList = new LinkedList<Map<String, Object>>();
						for(Map<String, Object> palce : palceList){
							Object p = palce.get("half");
							if(null == p || !(Boolean)p){
								newList.add(palce);
							}
						}
						pageTemp.put(CampConstants.PLACE_JSON, newList);
					}

				}else{
					String[] placeJson = prm68_BL.getPlaceJson(pageB, map);
					pageTemp.put(CampConstants.PLACE_JSON, placeJson[0]);
					pageTemp.put("checkedJson", placeJson[1]);
				}
			} catch (JSONException e) {
				logger.error(e.getMessage(),e);
			}
	}

	/**
	 * 基础信息页初始化
	 * @param map
	 */
	private void initPageC(Map<String,Object> map){
		String searchCode = ConvertUtil.getString(pageC.get("searchCode"));
		String memberType = ConvertUtil.getString(pageC.get("memberType"));
		String memberJson = ConvertUtil.getString(pageC.get("memberJson"));
		String conInfo = "";
		int memberCount = ConvertUtil.getInt(pageC.get("memberCount"));
		if(!"".equals(searchCode)){
			Map<String, Object> p = new HashMap<String, Object>();
			// 品牌Id
			p.put(CherryConstants.BRANDINFOID,
					map.get(CherryConstants.BRANDINFOID));
			// 组织Id
			p.put(CherryConstants.ORGANIZATIONINFOID,
					map.get(CherryConstants.ORGANIZATIONINFOID));
			// 会员信息搜索条件
			p.put(CampConstants.SEARCH_CODE, searchCode);
			// 活动对象类型
			p.put(CampConstants.CAMP_MEB_TYPE,memberType);
			// 查询对象总数量
			try {
				memberCount = binolcpcom03IF.searchMemCount(p);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!"".equals(memberJson)&& ("1".equals(memberType) || "2".equals(memberType))){
			Map<String,Object> p = ConvertUtil.json2Map(memberJson);
			p.put(CherryConstants.SESSION_CHERRY_LANGUAGE,
					map.get(CherryConstants.SESSION_LANGUAGE));
 			conInfo = binOLCM39_BL.conditionDisplay(p);
		}
		pageTemp.put("prmActiveName",pageA.get("prmActiveName"));
		pageTemp.put("subCampValid",pageA.get("subCampValid"));
		pageTemp.put("searchCode",searchCode);
		pageTemp.put("memberType",memberType);
		pageTemp.put("memberJson",memberJson);
		pageTemp.put("memberCount",memberCount);
		pageTemp.put("conInfo",conInfo);
	}
	
	/**
	 * 规则内容页初始化
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void initPageD(Map<String,Object> map){
		pageTemp.put(CherryConstants.BRANDINFOID,map.get(CherryConstants.BRANDINFOID));
		String ruleCondJson = pageD.get("ruleCondJson");
		String ruleResultJson = pageD.get("ruleResultJson");
		String exRanges = pageD.get("exRanges");
		if(null != ruleCondJson && !"".equals(ruleCondJson)){
			try {
				Map<String, Object> conMap = (Map<String, Object>)JSONUtil.deserialize(ruleCondJson);
				pageTemp.put("basePrice", conMap.get("BasePrice"));
				pageTemp.put("conMap", conMap.get("Content"));
			} catch (JSONException e) {
				logger.error(e.getMessage(),e);
			}
		}
		if(null != ruleResultJson && !"".equals(ruleResultJson)){
			try {
				Map<String, Object> resMap = (Map<String, Object>)JSONUtil.deserialize(ruleResultJson);
				pageTemp.put("resMap", resMap.get("Content"));
			} catch (JSONException e) {
				logger.error(e.getMessage(),e);
			}
		}
		if(null != exRanges && !"".equals(exRanges)){
			List<Map<String, Object>> exRangeList = ConvertUtil.json2List(exRanges);
			if(null != exRangeList && !exRangeList.isEmpty()){
				pageTemp.put("exRangeList", exRangeList);
			}
		}
	}

	/**
	 * 保存页面信息
	 *
	 * @param PageNo
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void savePage(Map<String,Object> map,int PageNo) throws Exception{
		if(PageNo == 1){
			session.put(SESSION_KEY + "A", pageA);
		}else if(PageNo == 2){
			session.put(SESSION_KEY + "B", pageB);
		}else if(PageNo == 3){
			session.put(SESSION_KEY + "C", pageC);
		}else if(PageNo == 4){
			session.put(SESSION_KEY + "D", pageD);
		}else if(PageNo == 5){// 保存到数据库
			pageA = (Map<String,String>)session.get(SESSION_KEY + "A");
			pageB = (Map<String,String>)session.get(SESSION_KEY + "B");
			pageC = (Map<String,String>)session.get(SESSION_KEY + "C");
			pageD = (Map<String,String>)session.get(SESSION_KEY + "D");
			map.putAll(pageA);
			map.putAll(pageB);
			map.putAll(pageC);
			map.putAll(pageD);
			map.put("templateFlag", templateFlag);
			map.put("bussiDate", prm68_BL.getBusDate(map));
			prm68_BL.tran_saveRule(map);
		}
	}

	/**
	 * 数据验证
	 * @param map
	 * @return
	 */
	public boolean validate(Map<String,Object> map){
		boolean result = true;
		if(pageNo == 1){
			if(null == pageA.get("prmActiveName") || "".equals(pageA.get("prmActiveName"))){//规则名称为空
				this.addFieldError("pageA.prmActiveName", getText("ECM00009",new String[]{getText("ESS00065")}));
				result = false;
			}else if(pageA.get("prmActiveName").length() > 20){ //规则名称不能超过20个字符
				this.addFieldError("pageA.prmActiveName", getText("ECM00020", new String[]{getText("ESS00065"),"20"}));
				result = false;
			}else{
				//验证规则名中是否包含特殊字符串
				String prmActiveName = ConvertUtil.getString(pageA.get("prmActiveName"));
				if(prmActiveName.contains("\t")||prmActiveName.contains("\n")||prmActiveName.contains("\r")){
					this.addFieldError("pageA.prmActiveName", getText("ECM00109",new String[]{getText("ESS00065")}));
					result= false;
				}
			}
			String validFlag = binOLCM14_BL.getConfigValue("1284",
					ConvertUtil.getString(map.get(CherryConstants.ORGANIZATIONINFOID)),
					ConvertUtil.getString(map.get(CherryConstants.BRANDINFOID)));
			if("1".equals(validFlag)){
				// 规则名称重复校验
				List<Integer> idList = binOLSSPRM13_BL.getActIdByName(pageA.get("prmActiveName"), pageA.get(CherryConstants.BRANDINFOID));
				if(null != idList && idList.size() > 0){
					int oldActId = ConvertUtil.getInt(pageA.get("activeID"));
					if(idList.size() > 1 || !CampConstants.OPT_KBN2.equals(pageA.get(CampConstants.OPT_KBN)) || idList.get(0) != oldActId){
						// 活动名已存在
						this.addFieldError("pageA.prmActiveName", getText("ECM00032",new String[]{getText("ESS00065")}));
						result = false;
					}
				}
			}
			if(null == pageA.get("prmActGrp") || "".equals(pageA.get("prmActGrp"))){//所属主题活动为空
				this.addFieldError("pageA.prmActGrp", getText("ECM00009",new String[]{getText("ESS00066")}));
				result = false;
			}
			if(null == pageA.get("shortCode") || "".equals(pageA.get("shortCode"))){//促销键为空
				//this.addFieldError("pageA.shortCode", getText("ECM00009",new String[]{getText("ESS00067")}));
				//result = false;
			}else{
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.putAll(map);
				paramsMap.put("ruleCodeShort", pageA.get("shortCode"));
				paramsMap.put("ruleCode", pageA.get("activityCode"));
				if(prm68_BL.isExistShortCode(paramsMap)){//促销键重复
					this.addFieldError("pageA.shortCode", getText("ESS00068"));
					result = false;
				}
			}
			if(!CherryChecker.isNullOrEmpty(pageA.get("maxExecCount")) && !CherryChecker.isNumeric(pageA.get("maxExecCount"))){//最大匹配次数格式错误
				this.addFieldError("pageA.maxExecCount", getText("ECM00045",new String[]{getText("ESS00069"),"0"}));
				result = false;
			}
			//
			int reslut = binOLSSPRM13_BL.validLinkMainCode(pageA);
			if(reslut == 1){
				this.addFieldError("pageA.linkMainCode", "关联活动码不能为空");
				result = false;
			}else if(reslut == 2){
				this.addFieldError("pageA.systemCode", "第三方券平台不能为空");
				result = false;
			}else if(reslut >= 3){
				this.addFieldError("pageA.linkMainCode", "关联活动码已经被关联");
				result = false;
			}
		}else if(pageNo == 2){
			if(!CherryChecker.checkDate(pageB.get("startTime"), DateUtil.DATETIME_PATTERN) ){//开始时间格式错误
				this.addFieldError("pageB.startTime", getText("ESS00071", new String[]{getText("ESS00072"),DateUtil.DATETIME_PATTERN}));
				result = false;
			}
			if(!CherryChecker.checkDate(pageB.get("endTime"), DateUtil.DATETIME_PATTERN) ){//结束时间格式错误
				this.addFieldError("pageB.endTime", getText("ESS00071", new String[]{getText("ESS00073"),DateUtil.DATETIME_PATTERN}));
				result = false;
			}
			if(result && pageB.get("startTime").compareTo(pageB.get("endTime")) >= 0 ){//开始时间必须小于结束时间
				this.addFieldError("pageB.endTime", getText("ECM00051", new String[]{getText("ESS00072"),getText("ESS00073")}));
				result = false;
			}
			if(CherryChecker.isNullOrEmpty(pageB.get("placeJson"), true) || "[]".equals(pageB.get("placeJson"))){//活动地点为空
				this.addFieldError("pageB.placeJson", getText("ECM00009",new String[]{getText("ESS00074")}));
				result = false;
			}
		}else if(pageNo == 3){
				
		}else if(pageNo == 4){
			String ruleCondJson = pageD.get("ruleCondJson");
			String ruleResultJson = pageD.get("ruleResultJson");
			if(null != ruleCondJson && !"".equals(ruleCondJson)){
				ruleCondJson = prm68_BL.packJson("RV.01.0001","1","1",pageD.get("basePrice"),ruleCondJson);
				pageD.put("ruleCondJson",ruleCondJson);
			}
			if(null != ruleResultJson && !"".equals(ruleResultJson)){
				ruleResultJson = prm68_BL.packJson("RV.01.0001","1","2",pageD.get("basePrice"),ruleResultJson);
				pageD.put("ruleResultJson",ruleResultJson);
			}
			//促销规则购买条件验证
			result = this.checkRuleCond(ruleCondJson, result);
			//促销规则奖励内容验证
			result = this.checkRuleResult(ruleResultJson, result);
		}
		return result;
	}


	/**
	 * 赠送礼品excel导入
	 * @throws Exception
     */
	public void importProductExeclUpload() throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();

			String execLoadType = form.getExecLoadType();//产品导入区分
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 取得所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			int orgId = ConvertUtil.getInt(userInfo.getBIN_OrganizationInfoID());
			int brandId = ConvertUtil.getInt(userInfo.getBIN_BrandInfoID());
			String searchCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "EC");
			map.put("upMode",form.getUpMode()); // 导入模式 1-新增 2-覆盖
			map.put("upExcel",form.getUpExcel());
			map.put("execLoadType",execLoadType);
			map.put("searchCode",searchCode);
			map.put("productPageSize",form.getProductPageSize());//页面上已存在数据
			if(execLoadType.equals("shoppingCart")){//非整单导入
				map.put("excelProductShopping",form.getExcelProductShopping());
				resultMap = prm68_BL.tran_importShopProductExecl(map);
			} else if(execLoadType.equals("GIFT")){//奖励结果导入
				map.put("excelProductAward",form.getExcelProductAward());
				resultMap = prm68_BL.tran_importPresentExecl(map);
			} else if(execLoadType.equals("DPZK")){//折扣结果导入
				map.put("excelProductAward",form.getExcelProductAward());
				resultMap = prm68_BL.tran_importDisCountExecl(map);
			} else if(execLoadType.equals("DPTJ")){//特价结果导入
				map.put("excelProductAward",form.getExcelProductAward());
				resultMap = prm68_BL.tran_importProductSepcialExecl(map);
			}

		}catch (Exception e){
			logger.error(e.getMessage(),e);
			resultMap.put(PromotionConstants.RESULT_CODE,PromotionConstants.RESULT_CODE_2);
			resultMap.put(PromotionConstants.RESULT_MESSAGE,e.getMessage());
		}finally {
			ConvertUtil.setResponseByAjax(response,resultMap);
		}
	}

	/**
	 * 校验购买条件格式
	 * @param ruleCondJson
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean checkRuleCond(String ruleCondJson, boolean result){
		if(null != ruleCondJson && !"".equals(ruleCondJson)){//条件验证
			try {
				Map<String, Object> conMap = (Map<String, Object>)JSONUtil.deserialize(ruleCondJson);
				Map<String, Object> contentMap = (Map<String, Object>) conMap.get("Content");
				String condType = ConvertUtil.getString(contentMap.get("condType"));
				//条件验证
				if("1".equals(condType)){//整单类
					String propName = ConvertUtil.getString(contentMap.get("propName"));
					String propValue = ConvertUtil.getString(contentMap.get("propValue"));
					if("SUMAMOUNT".equals(propName)){//总金额
						if(!CherryChecker.isFloatValid(propValue, 10, 2)
								|| ConvertUtil.getFloat(propValue) < 0){
							this.addFieldError("pageD.propValue", getText("ESS00093",new String[]{getText("ESS00075"),getText("ESS00077")}));
							return false;
						}
					}else if("SUMQUANTITY".equals(propName)){//总数量
						if(!CherryChecker.isNumeric(propValue)
								|| ConvertUtil.getInt(propValue) == 0){
							this.addFieldError("pageD.propValue", getText("ESS00070", new String[]{getText("ESS00076"),getText("ESS00078")}));
							return false;
						}
					}
				}else if("2".equals(condType)){//非整单类
					List<Map<String, Object>> logicObjArr1 = (List<Map<String, Object>>) contentMap.get("logicObjArr");
					if(null == logicObjArr1 || logicObjArr1.size() == 0 ){//组合为空
						this.addFieldError("pageD.logicObjArr", getText("ESS00080", new String[]{getText("ESS00075")}));
						return false;
					}else{
						for(Map<String, Object> logicObjMap1 : logicObjArr1){
							List<Map<String, Object>> logicObjArr2 =  (List<Map<String, Object>>) logicObjMap1.get("logicObjArr");
							if(null == logicObjArr2 || logicObjArr2.size() == 0 ){//组合明细为空
								this.addFieldError("pageD.logicObjArr", getText("ESS00081", new String[]{getText("ESS00075")}));
								return false;
							}else{
								for(Map<String, Object> logicObjMap2 : logicObjArr2){
									String propValue = ConvertUtil.getString(logicObjMap2.get("propValue"));//属性值
									String rangeVal = ConvertUtil.getString(logicObjMap2.get("rangeVal"));//产品范围值
									String rangeType = ConvertUtil.getString(logicObjMap2.get("rangeType"));//范围类型
									String propName = ConvertUtil.getString(logicObjMap2.get("propName"));//属性名
									String rangeOpt = ConvertUtil.getString(logicObjMap2.get("rangeOpt"));//产品范围操作
									if((!"RANGE".equals(rangeType) && !"ALL".equals(rangeType) && !"ZD".equals(rangeType) && CherryChecker.isNullOrEmpty(rangeVal, true))){//产品范围对象不能为空
										this.addFieldError("pageD.rangeVal", getText("ESS00082",new String[]{getText("ESS00075"), getText("ESS00083")}));
										return false;
									}
									if("UNITCODE_BARCODE".indexOf(rangeType) != -1 && "LIKE".equals(rangeOpt)){//编码条码相似匹配
										if(null != rangeVal){
											if(rangeVal.indexOf("+") == 0){//匹配内容为空
												this.addFieldError("pageD.rangeVal", getText("ESS00082",new String[]{getText("ESS00075"), getText("ESS00084")}));
												return false;
											}else if(rangeVal.indexOf("+") == rangeVal.length()-1){//匹配开始位置为空
												this.addFieldError("pageD.rangeVal", getText("ESS00082",new String[]{getText("ESS00075"), getText("ESS00085")}));
												return false;
											}else{
												String[] rangeValArr = rangeVal.split("\\+");
												if(!CherryChecker.isNumeric(rangeValArr[1])){//开始位置格式错误
													this.addFieldError("pageD.rangeVal", getText("ESS00086", new String[]{getText("ESS00075"),getText("ESS00085")}));
													return false;
												}
											}
										}
									}else {
										//编码或条码有效性校验
										if(!this.checkCode(rangeType, rangeVal)){
											return false;
										}
									}
									if("QUANTITY".equals(propName) &&
											(!CherryChecker.isNumeric(propValue)
													|| ConvertUtil.getInt(propValue) == 0)){//数量值
										this.addFieldError("pageD.propValue", getText("ESS00070", new String[]{getText("ESS00075"), getText("ESS00078")}));
										return false;
									}else if("AMOUNT".equals(propName) &&
											!CherryChecker.isFloatValid(propValue, 10, 2)){//金额值
										this.addFieldError("pageD.propValue", getText("ESS00070", new String[]{getText("ESS00075"), getText("ESS00077")}));
										return false;
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				this.addFieldError("pageD",getText("ESS00087", new String[]{getText("ESS00075")}));
				return false;
			}
		}
		return result;
	}

	/**
	 * 校验奖励内容格式
	 * @param ruleResultJson
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean checkRuleResult(String ruleResultJson, boolean result){
		if(null != ruleResultJson && !"".equals(ruleResultJson)){//结果验证
			try {
				Map<String, Object> resMap = (Map<String, Object>)JSONUtil.deserialize(ruleResultJson);
				Map<String, Object> contentMap = (Map<String, Object>) resMap.get("Content");
				List<Map<String, Object>> logicObjArr1  = (List<Map<String, Object>>) contentMap.get("logicObjArr");
				if(null != logicObjArr1){
					for(Map<String, Object> logicObjMap1 : logicObjArr1){
						String rewardType = ConvertUtil.getString(logicObjMap1.get("rewardType"));//奖励类型
						String rewardVal = ConvertUtil.getString(logicObjMap1.get("rewardVal"));//奖励值

						String rewardType1 = "JJHG_ZDYH_YHTZ_ZDMS";//金额奖励类型
						String rewardType2 = "GIFT_DPZK_DPTJ_DNZK_JJHG_DPYH";//存在组合的类型
						String rewardType3 = "ZDZK_TZZK";// 折扣奖励类型
						if(rewardType1.indexOf(rewardType) >=0){
							if(!CherryChecker.isFloatValid(rewardVal, 10, 2)){
								this.addFieldError("pageD.rewardVal", getText("ESS00079", new String[]{getText("ESS00076"), getText("ESS00077")}));
								return false;
							}
						}
						if(rewardType3.indexOf(rewardType) >= 0){//整单,套装折扣
							if(!CherryChecker.isFloatValid(rewardVal, 10, 2)){//折扣率格式
								this.addFieldError("pageD.rewardVal", getText("ESS00079", new String[]{getText("ESS00076"), getText("ESS00088")}));
								return false;
							}else{
								float val = ConvertUtil.getFloat(rewardVal);
								if(val < 0 || val > 10){//整单折扣，折扣率范围为0~10
									this.addFieldError("pageD.rewardVal", getText("ESS00089", new String[]{getText("ESS00076"), getText("ESS00088")}));
									return false;
								}
							}
						}
						if(rewardType2.indexOf(rewardType) >= 0){//结果存在组合
							List<Map<String, Object>> logicObjArr2  = (List<Map<String, Object>>) logicObjMap1.get("logicObjArr");
							if(null == logicObjArr2 || logicObjArr2.size() == 0){//结果为空
								this.addFieldError("pageD.logicObjArr", getText("ESS00080", new String[]{getText("ESS00076")}));
								return false;
							}else{
								for(Map<String, Object> logicObjMap2 : logicObjArr2){
									List<Map<String, Object>> logicObjArr3  = (List<Map<String, Object>>) logicObjMap2.get("logicObjArr");
									if(null == logicObjArr3 || logicObjArr3.size() == 0){//组合为空
										this.addFieldError("pageD.logicObjArr", getText("ESS00081", new String[]{getText("ESS00076")}));
										return false;
									}else{
										for(Map<String, Object> logicObjMap3 : logicObjArr3){
											String quantity = ConvertUtil.getString(logicObjMap3.get("quantity"));//产品数值
											String rangeVal = ConvertUtil.getString(logicObjMap3.get("rangeVal"));//产品范围值
											String rangeType = ConvertUtil.getString(logicObjMap3.get("rangeType"));//产品范围类型
											String rewardVal3 = ConvertUtil.getString(logicObjMap3.get("rewardVal"));//奖励值
											String rangeOpt = ConvertUtil.getString(logicObjMap3.get("rangeOpt"));//产品范围操作
											String ranges = ConvertUtil.getString(logicObjMap3.get("ranges"));//产品范围
											if((!"RANGE".equals(rangeType) && !"ALL".equals(rangeType) && !"ZD".equals(rangeType) && CherryChecker.isNullOrEmpty(rangeVal, true))){//产品范围不能为空
												this.addFieldError("pageD.rangeVal", getText("ESS00082", new String[]{getText("ESS00076"), getText("ESS00083")}));
												return false;
											}
											if("UNITCODE_BARCODE".indexOf(rangeType) != -1 && "LIKE".equals(rangeOpt)){//编码条码相似匹配
												if(null != rangeVal){
													if(rangeVal.indexOf("+") == 0){//匹配内容为空
														this.addFieldError("pageD.rangeVal", getText("ESS00082", new String[]{getText("ESS00076"), getText("ESS00084")}));
														return false;
													}
													if(rangeVal.indexOf("+") == rangeVal.length()-1){//开始匹配位置为空
														this.addFieldError("pageD.rangeVal", getText("ESS00082", new String[]{getText("ESS00076"), getText("ESS00085")}));
														return false;
													}
													String[] rangeValArr = rangeVal.split("\\+");
													if(!CherryChecker.isNumeric(rangeValArr[1])){//开始匹配位置格式错误
														this.addFieldError("pageD.rangeVal", getText("ESS00086", new String[]{getText("ESS00076"), getText("ESS00085")}));
														return false;
													}
												}
											}else{
												//编码或条码有效性校验
												if(!this.checkCode(rangeType, rangeVal)){
													return false;
												}
											}
											if(!"DPZK".equals(rewardType) && !"DPTJ".equals(rewardType)&& !"DPYH".equals(rewardType)){
												if(!("N".equalsIgnoreCase(quantity) && "DNZK".equals(rewardType)) && (!CherryChecker.isNumeric(quantity) || ConvertUtil.getInt(quantity) == 0)){//产品数值格式错误
													this.addFieldError("pageC.quantity", getText("ESS00086", new String[]{getText("ESS00076"), getText("ESS00078")}));
													return false;
												}
											}
											if("DPYH".equals(rewardType) && !CherryChecker.isFloatValid(rewardVal3, 10, 2)){
												this.addFieldError("pageD.rewardVal3", getText("ESS00079", new String[]{getText("ESS00076"), getText("ESS00077")}));
												return false;
											}
											if("DPTJ".equals(rewardType)&& !CherryChecker.isFloatValid(rewardVal3, 10, 2)){
												this.addFieldError("pageD.rewardVal3", getText("ESS00079", new String[]{getText("ESS00076"), getText("ESS00077")}));
												return false;
											}
											if("DPZK_DNZK".indexOf(rewardType) != -1){
												if(!CherryChecker.isFloatValid(rewardVal3, 10, 2)){//折扣率格式
													this.addFieldError("pageD.rewardVal", getText("ESS00079", new String[]{getText("ESS00076"), getText("ESS00088")}));
													return false;
												} else {
													float val = ConvertUtil.getFloat(rewardVal3);
													if(val < 0 || val > 10){//折扣率范围
														this.addFieldError("pageD.rewardVal", getText("ESS00089", new String[]{getText("ESS00076"), getText("ESS00088")}));
														return false;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}

			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				this.addFieldError("pageD",getText("ESS00087", new String[]{getText("ESS00076")}));
				return false;
			}
		}
		return result;
	}

	/**
	 * 产品厂商编码或条码有效性校验
	 * @param type
	 * @param code
	 * @return
	 */
	private boolean checkCode(String type, String code){
		Map<String, Object> map = this.getComMap();
		if("UNITCODE".equals(type)){//编码条码存在校验
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
			paraMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
			paraMap.put("unitCode", code);
			paraMap.put("validFlag", "1");
			if (binOLCM05_BL.checkUnitCode(paraMap)) {
				this.addFieldError("pageD.rangeVal", getText("ESS00090", new String[]{code}));
				return false;
			}
		}else if("BARCODE".equals(type)){//编码条码存在校验
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
			paraMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
			paraMap.put("barCode", code);
			paraMap.put("validFlag", "1");
			//根据barCode取产品Id
			List<Integer> prtIds = binOLCM05_BL.getProductIdByBarCode(paraMap);
			//根据barCode取促销品Id
			List<Integer> prmIds = binOLCM05_BL.getPromotionIdByBarCode(paraMap);
			if ((null == prtIds || prtIds.size() == 0) && (null == prmIds || prmIds.size() == 0)) {
				//产品与促销品都不存在
				this.addFieldError("pageD.rangeVal", getText("ESS00091", new String[]{code}));
				return false;
			}
		}
		return true;
	}

	/**
	 * 取得共通MAP
	 * @return
	 */
	private Map<String,Object> getComMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 取得所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.SESSION_LANGUAGE, userInfo.getLanguage());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("userID", userInfo.getBIN_UserID());
		map.put(CampConstants.OPT_KBN, opt);
		map.put("activeID", activeID);
		// 业务日期
		String bussiDate = prm68_BL.getBusDate(map);
		map.put("bussiDate", bussiDate);
		return map;
	}

	/**
	 * 查找导入失败的产品
	 * @throws Exception
	 */
	public String searchFailUpload() throws Exception{
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 活动编码
		map.put("searchCode", form.getSearchCode());

		String execLoadType = prm68_BL.execLoadTypeChange(ConvertUtil.getString(form.getExecLoadType()));

		// Excel表格加载模板类型
		map.put("execLoadType", execLoadType);

		// 操作区分
		map.put("operateType", "2");

		// dataTable上传的参数设置到map
		map.put("START", form.getIDisplayStart() + 1);
		map.put("END", form.getIDisplayStart() + form.getIDisplayLength());
		int count = prm68_BL.getFailUploadCount(map);
		if (count != 0) {
			form.setFailList(prm68_BL.getFailUploadList(map));
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if (form.getExecLoadType().equals(PromotionConstants.EXECLOADTYPE_1)) {
			return "popLoadProductPromotion_2";
		} else if (form.getExecLoadType().equals(PromotionConstants.EXECLOADTYPE_2)) {
			return "popLoadProductPromotion_1";
		} else if (form.getExecLoadType().equals(PromotionConstants.EXECLOADTYPE_3)) {
			return "popLoadProductPromotion_3";
		} else  {
			return "popLoadProductPromotion_4";
		}
	}





	/**
	 *产品导入失败数据导出
	 * @return
	 * @throws JSONException
	 */
	public String export() throws JSONException{
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		/*UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);*/
		// 活动编码
		map.put("searchCode", form.getSearchCode());
		String execLoadType = prm68_BL.execLoadTypeChange(ConvertUtil.getString(form.getExecLoadType()));
		// Excel表格加载模板类型
		map.put("execLoadType",execLoadType);

		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		try{
			String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
			if(PromotionConstants.EXECLOADTYPE_1.equals(form.getExecLoadType())){
				downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM68", language, "downloadFileNameForShop");
			}else if(PromotionConstants.EXECLOADTYPE_2.equals(form.getExecLoadType())){
				downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM68", language, "downloadFileNameForGift");
			}else if (PromotionConstants.EXECLOADTYPE_3.equals(form.getExecLoadType())){
				downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM68", language, "downloadFileNameForDPZK");
			}else if (PromotionConstants.EXECLOADTYPE_4.equals(form.getExecLoadType())){
				downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM68", language, "downloadFileNameForDPTJ");
			}
			setExcelStream(new ByteArrayInputStream(prm68_BL.exportExcel(map)));
		}catch (Exception e){
			this.addActionError(getText("EMO00022"));
			e.printStackTrace();
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLSSPRM68_excel";
	}


	/**
	 *
	 * 优惠券导入加载
	 * @return
	 */
	public String execlLoadInit() throws Exception {
		//execl导入类型
		String execLoadType = ConvertUtil.getString(form.getExecLoadType());
		//页面产品数据
		String ruleCondProduct = ConvertUtil.getString(form.getRuleCondProduct());
		List<Map<String,Object>> productImportList = new ArrayList<Map<String,Object>>();
		Map<String,Object> ruleCondProductMap = (Map<String,Object>) JSONUtil.deserialize(ruleCondProduct);
		productImportList = (List<Map<String,Object>>) ruleCondProductMap.get("ruleCondProduct");
		int productImportListSize = 0;
		for (Map<String,Object> productImportMap:productImportList) {
			String unitCode = "";
			String rangeText = ConvertUtil.getString(productImportMap.get("rangeText"));
			if (!rangeText.equals("")) {
				//当第一次导入时对页面上的数据进行处理，转换为导入后的格式
				int startIndex = rangeText.indexOf(":") + 1;
				int ednIndex = rangeText.indexOf(",");
				if (startIndex!=0&&ednIndex!=-1){
					unitCode = rangeText.substring(startIndex, ednIndex);
					productImportMap.put("unitCode", unitCode);
					int barStartIndex = rangeText.indexOf("BC:") + 3;
					int barEndIndex = rangeText.indexOf(")");
					String barCode = rangeText.substring(barStartIndex, barEndIndex);
					productImportMap.put("barCode", barCode);
					if (execLoadType.equals("shoppingCart")) {
						productImportMap.put("rangeOpt", "EQUAL");
					}
				}
			}
			productImportListSize=productImportListSize+1;
		}
//		Map<String,Object> productMap = new HashMap<String, Object>();
//		productMap.put("ruleCondProduct",productImportList);
		String productString = CherryUtil.list2Json(productImportList);
		if (execLoadType.equals("shoppingCart")){//非整单条件
			form.setExcelProductShopping(productString);
		}else{//奖励产品
			form.setExcelProductAward(productString);
		}
		Map<String,Object> prodcutPageData=(Map<String,Object>)session.get(SESSION_KEY + "D");
		String ruleList = "";
		if (execLoadType.equals("shoppingCart")) {//购物车中的产品明细时
			ruleList = ConvertUtil.getString(prodcutPageData.get("ruleCondJson"));
		}else{//奖励中的产品明细时
			ruleList = ConvertUtil.getString(prodcutPageData.get("ruleResultJson"));
		}
		int productPageSize = 0;
		if(!StringUtils.isEmpty(ruleList)) {
			Map<String, Object> ruleCondJson = (Map<String, Object>) JSONUtil.deserialize(ruleList);
			Map<String, Object> contentMap = (Map<String, Object>) ruleCondJson.get("Content");
			// 产品列表
			List<Map<String, Object>> logicObjArrList = (List<Map<String, Object>>) contentMap.get("logicObjArr");
			if (!CollectionUtils.isEmpty(logicObjArrList)) {
				for (Map<String, Object> logicObjArrMap : logicObjArrList) {
					List<Map<String, Object>> logicObjList = (List<Map<String, Object>>) logicObjArrMap.get("logicObjArr");
					if (!CollectionUtils.isEmpty(logicObjList)) {
						productPageSize = productPageSize + logicObjList.size();
					}
				}
			}
		}
		form.setProductPageSize(productPageSize-productImportListSize);
		return "productDialog";
	}
	
	public String getCsrftoken() {
		return csrftoken;
	}

	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getActiveID() {
		return activeID;
	}

	public void setActiveID(String activeID) {
		this.activeID = activeID;
	}

	public Map<String, String> getPageA() {
		return pageA;
	}

	public void setPageA(Map<String, String> pageA) {
		this.pageA = pageA;
	}

	public Map<String, String> getPageB() {
		return pageB;
	}

	public void setPageB(Map<String, String> pageB) {
		this.pageB = pageB;
	}

	public Map<String, String> getPageC() {
		return pageC;
	}

	public void setPageC(Map<String, String> pageC) {
		this.pageC = pageC;
	}
	public Map<String, String> getPageD() {
		return pageD;
	}

	public void setPageD(Map<String, String> pageD) {
		this.pageD = pageD;
	}
	public Map<String, Object> getPageTemp() {
		return pageTemp;
	}

	public void setPageTemp(Map<String, Object> pageTemp) {
		this.pageTemp = pageTemp;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public int getTemplateFlag() {
		return templateFlag;
	}

	public void setTemplateFlag(int templateFlag) {
		this.templateFlag = templateFlag;
	}

	public BINOLSSPRM68_Form getModel() {
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
