/*		
 * @(#)BINOLSSPRM73_Action.java     1.0 2016/03/28		
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
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.*;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.ss.prm.bl.BINOLSSPRM99_BL;
import com.cherry.ss.prm.core.CouponConstains;
import com.cherry.ss.prm.dto.BillInfo;
import com.cherry.ss.prm.dto.CouponRuleDTO;
import com.cherry.ss.prm.dto.FailUploadDataDTO;
import com.cherry.ss.prm.form.BINOLSSPRM73_Form;
import com.cherry.ss.prm.interfaces.BINOLSSPRM73_IF;
import com.cherry.ss.prm.rule.CouponEngine;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 优惠券规则Action
 * @author hub
 * @version 1.0 2016.03.28
 */
public class BINOLSSPRM73_Action extends BaseAction implements ModelDriven<BINOLSSPRM73_Form>{

	private static final long serialVersionUID = 5565301651534313429L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLSSPRM73_Action.class);

	/** 参数FORM */
	private BINOLSSPRM73_Form form = new BINOLSSPRM73_Form();
	
	/** 优惠券规则BL */
	@Resource
	private BINOLSSPRM73_IF binOLSSPRM73_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource
	private CouponEngine coupEngine;
	
	@Resource(name="coupon_IF")
	private BINOLSSPRM99_BL coupon_IF;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;

	/** 共通 */
	@Resource
	private BINOLMOCOM01_IF binOLMOCOM01_BL;

	@Override
	public BINOLSSPRM73_Form getModel() {
		return form;
	}
	
	private List<Map<String, Object>> ruleList;

	public List<Map<String, Object>> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<Map<String, Object>> ruleList) {
		this.ruleList = ruleList;
	}

	/** 产品: 发送门槛_白名单 产品明细集合 */
	private List<Map<String,Object>> prt_s_w_PrtCouponPrtDetail;

	/** 产品: 发送门槛_黑名单 产品明细集合 */
	private List<Map<String,Object>> prt_s_b_PrtCouponPrtDetail;

	/** 券集合及里面的使用门槛 */
	private Map bigContentMap;

	/** 使用门槛useCondJson */
	private String useCondJson;

	/** Excel输入流 */
	private InputStream excelStream;

	/** 下载文件名 */
	private String downloadFileName;

	public String getDownloadFileName() throws UnsupportedEncodingException {
		//转码下载文件名 Content-Disposition
		return FileUtil.encodeFileName(request,downloadFileName);
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	/**
	 * 页面初始化
	 * @return
	 * @throws Exception 
	 */
	public String init () throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			map.put("noHeadKbn", "1");
			// 取得所管辖的品牌List
			List<Map<String, Object>> brandList = binOLCM05_BL.getBrandInfoList(map);
			form.setBrandList(brandList);
		} else {
			form.setBrandInfoId(String.valueOf(brandInfoId));
		}
		return SUCCESS;
	}
	
	/**
	 * AJAX查询优惠券规则
	 * 
	 * @return 查询优惠券规则画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = getSearchMap();
		// 取得优惠券规则总数
		int count = binOLSSPRM73_BL.getRuleInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得优惠券规则List
			ruleList = binOLSSPRM73_BL.getRuleInfoList(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 编辑画面初始显示
	 * 
	 * @return 查询积分信息画面
	 */
	public String editInit() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleCode", form.getRuleCode());
		// 取得优惠券规则详细信息
		CouponRuleDTO couponRule = binOLSSPRM73_BL.getCouponRuleInfo(map);
		/*
		try {
			// 使用券时间JSON
			String useTimeJson = couponRule.getUseTimeJson();
			if (!CherryChecker.isNullOrEmpty(useTimeJson)) {
				Map<String, Object> useTimeInfo = (Map<String, Object>) JSONUtil.deserialize(useTimeJson);
				// 使用券时间模式
				String useTimeType = (String) useTimeInfo.get("useTimeType");
				form.setUseTimeType(useTimeType);
				form.setUseTimeInfo(useTimeInfo);
			}
		} catch (Exception e) {
			logger.error("使用券时间JSON转换失败：" + e.getMessage(),e);
			throw e;
		}
		*/

		// 清除临时数据
		binOLSSPRM73_BL.deleteTempDataByRuleCode(form.getRuleCode());

		int brandInfoId = couponRule.getBrandInfoId();
		List<Map<String, Object>> levelList = binOLSSPRM73_BL.getLevelList(brandInfoId);
		List<Map<String, Object>> useLevelList = ConvertUtil.copyList(levelList);
		try {
			// 发送门槛
			String sendCond = couponRule.getSendCond();
			if (!CherryChecker.isNullOrEmpty(sendCond)) {
				Map<String, Object> sendCondInfo = (Map<String, Object>) JSONUtil.deserialize(sendCond);
				sendCondInfo.put("levelList", levelList);
				//已经导入的柜台黑名单列表(发送门槛)
				Map<String, Object> cntParam_map = new HashMap<String, Object>();
				cntParam_map.put("ruleCode",form.getRuleCode());
				cntParam_map.put("filterType",CherryConstants.filterType_b);//黑名单
				cntParam_map.put("conditionType",CherryConstants.conditionType_s);//发券门槛
				cntParam_map.put("contentNo",CherryConstants.sendCond_defContentNo);//子券标识
				List<Map<String,Object>> cntBlackList=binOLSSPRM73_BL.getCounterList(cntParam_map);
				sendCondInfo.put("cntBlackList",cntBlackList);
				String counterKbn_w=ConvertUtil.getString(sendCondInfo.get("counterKbn_w"));
				//根据选择选择项来确定是否需要确定查询白名单
				if(CherryConstants.counterKbn_impType.equals(counterKbn_w)){//(1为柜台导入模式)
					cntParam_map.put("filterType",CherryConstants.filterType_w);//白名单
					List<Map<String,Object>> cntWhiteList=binOLSSPRM73_BL.getCounterList(cntParam_map);
					sendCondInfo.put("cntWhiteList",cntWhiteList);
				}else if(CherryConstants.counterKbn_channel2Counter.equals(counterKbn_w)){//(2为根据渠道选择柜台模式)
					cntParam_map.put("filterType",CherryConstants.filterType_w);//白名单
					List<Map<String,Object>> cntWhiteList=binOLSSPRM73_BL.getCounterList(cntParam_map);
					String cntwhiteChannel=this.getCntChannelString(cntWhiteList);
					sendCondInfo.put("cntwhiteChannel",cntwhiteChannel);
				}else if(CherryConstants.counterKbn_channel.equals(counterKbn_w)){//渠道选择模式
					List<Map<String,Object>> channel_list= binOLSSPRM73_BL.seachChannelWay(this.getSessionInfo());
					List<Map<String,Object>> cntChannelList_w=(List<Map<String,Object>>)sendCondInfo.get("cntChannelList_w");
					this.convertChannelList(channel_list,cntChannelList_w);
					sendCondInfo.put("channel_list",channel_list);
				}
				binOLSSPRM73_BL.condSetting(sendCondInfo, brandInfoId);

				// 处理发送门槛-产品(分类)
				handelPrt(CherryConstants.conditionType_s,CherryConstants.sendCond_defContentNo,sendCondInfo,map);

				// 处理发送门槛-会员
				binOLSSPRM73_BL.setMemberList(levelList,sendCondInfo,form.getRuleCode());

				form.setSendCondInfo(sendCondInfo);


			} else {
				form.getSendCondInfo().put("levelList", levelList);
			}
		} catch (Exception e) {
			logger.error("发送门槛JSON转换失败：" + e.getMessage(),e);
			throw e;
		}

		/* 使用门槛setUseCondJson方法里处理
		try {
			// 使用门槛
			String useCond = couponRule.getUseCond();
			if (!CherryChecker.isNullOrEmpty(useCond)) {
				Map<String, Object> useCondInfo = (Map<String, Object>) JSONUtil.deserialize(useCond);
				useCondInfo.put("levelList", useLevelList);
				form.setUseCondInfo(useCondInfo);
				binOLSSPRM73_BL.condSetting(useCondInfo, brandInfoId);
			} else {
				form.getUseCondInfo().put("levelList", useLevelList);
			}

		} catch (Exception e) {
			logger.error("使用门槛JSON转换失败：" + e.getMessage(),e);
			throw e;
		}*/
		try {
			// 规则内容

			String content = couponRule.getContent();
			if (!CherryChecker.isNullOrEmpty(content)) {
				// TODO 后期会放开以下注释
				// 组织BigCentent结构
				handelBigContent(couponRule);
				/* 券内容
				List<Map<String, Object>> contentList = (List<Map<String, Object>>) JSONUtil.deserialize(content);
				if (null != contentList) {
					int maxContentNo = 0;
					for (Map<String, Object> contentInfo : contentList) {
						binOLSSPRM73_BL.contentSetting(contentInfo, brandInfoId);
						contentInfo.put("isSameFlag","0");
						if (contentInfo.containsKey("contentNo")) {
							int contentNo = Integer.parseInt(String.valueOf(contentInfo.get("contentNo")));
							if (contentNo > maxContentNo) {
								maxContentNo = contentNo;
							}
						}
					}
					couponRule.setMaxContentNo(maxContentNo);
				}
				form.setContentInfoList(contentList);
				*/

			}
		} catch (Exception e) {
			logger.error("券内容JSON转换失败：" + e.getMessage(),e);
			throw e;
		}
		form.setCouponRule(couponRule);
		return SUCCESS;
	}

	/**
	 * 组织BigCentent结构
	 * @param couponRule
	 * @throws Exception
     */
	private void handelBigContent(CouponRuleDTO couponRule) throws Exception{

		bigContentMap = new HashMap();

		Map<String, Object> useCondContentNoMap = new HashMap<String, Object>(); // 定义每个contentNo对应的使用门槛
		List<Map<String, Object>> newContentList = new ArrayList<Map<String, Object>>(); // 定义券内容集合
		String emptyUseCondJson = CherryUtil.map2Json(new HashMap());
		String isSameFlag = "";

		// 规则内容
		String content = couponRule.getContent();
		String couponFlag = couponRule.getCouponFlag();
		if (!CherryChecker.isNullOrEmpty(content)) {
			List<Map<String, Object>> contentList = (List<Map<String, Object>>) JSONUtil.deserialize(content);
			if (null != contentList) {

				// 使用门槛
				String useCond = couponRule.getUseCond();
				if (!CherryChecker.isNullOrEmpty(useCond)) {
					Map<String, Object> useCondInfo = (Map<String, Object>) JSONUtil.deserialize(useCond);

					// 设置BigContent.isSameFlag
					String useCond_mode = ConvertUtil.getString(useCondInfo.get(CouponConstains.USECOND_MODE));
					if (couponFlag.equals("9")) {
						if (CouponConstains.USECOND_MODE_1.equals(useCond_mode)) {
							isSameFlag = CouponConstains.USECOND_ISSAME_FLAG_1;
						} else if (CouponConstains.USECOND_MODE_2.equals(useCond_mode)) {
							isSameFlag = CouponConstains.USECOND_ISSAME_FLAG_0;
						}
						bigContentMap.put(CouponConstains.USECOND_ISSAME_FLAG, isSameFlag);
					} else {
						bigContentMap.put(CouponConstains.USECOND_ISSAME_FLAG, isSameFlag);
					}


					if (CherryUtil.isEmpty(useCond_mode)) {
						// 未设置过门槛,给一个空的
						bigContentMap.put("useCondJson", emptyUseCondJson);
					}

					// 将useCond的list内的使用门槛按contentNo为key设置到useCondContentNoMap,以便后面整合到contentList里面
					List<Map<String, Object>> list = (List<Map<String, Object>>) useCondInfo.get("useInfo");
					if (null != list && list.size() > 0) {
						for (Map<String, Object> useCondMap : list) {
							useCondContentNoMap.put(ConvertUtil.getString(useCondMap.get("contentNo")), useCondMap); // contentNo : contentNo对应的useCond
						}

					}

					// 使用门槛使用相同时,设置BigContent.useCondJson
					if (CouponConstains.USECOND_ISSAME_FLAG_1.equals(isSameFlag)) {
						if (null != list && list.size() == 1) {
							String useCondJson = setUseCondJson(CherryConstants.useCond_9DefContentNo, list.get(0), couponRule);
							bigContentMap.put("useCondJson", useCondJson);
						} else {
							bigContentMap.put("useCondJson", emptyUseCondJson); // 使用门槛的配置相同,但没有设置门槛时,给一个空
						}
					}
				} else {
					// 没有使用门槛时,默认给一个空的
					bigContentMap.put("useCondJson", emptyUseCondJson);
				}

				// 设置contentList 券内容集合
				newContentList = setContentList(contentList, useCondContentNoMap, isSameFlag,couponRule);
				bigContentMap.put("contentList", newContentList);

			}
		} else {
			// 没有券内容时,设置为一个空
			bigContentMap.put("contentList", newContentList);
		}

	}

	/**
	 * 设置contentList 券内容集合
	 * @param contentList
	 * @param isSameFlag
     * @return
     */
	private List<Map<String,Object>> setContentList(List<Map<String,Object>> contentList,Map<String,Object> useCondContentNoMap,String isSameFlag,CouponRuleDTO couponRule) throws Exception {
		List<Map<String, Object>> newContentList = new ArrayList<Map<String, Object>>();
		for (Map<String,Object> content : contentList) {

			binOLSSPRM73_BL.contentSetting2(content, couponRule.getBrandInfoId());

			String contentNo = ConvertUtil.getString(content.get("contentNo"));

			// 使用不同门槛时,将useCondJson写到content内容里面
			if (isSameFlag.equals("")||CouponConstains.USECOND_ISSAME_FLAG_0.equals(isSameFlag)) {
				Map<String,Object> useCondMap = (Map<String, Object>) useCondContentNoMap.get(contentNo);
				if (null != useCondMap && !useCondMap.isEmpty()) {
					String useCondJson = setUseCondJson(contentNo,useCondMap,couponRule);
					content.put("useCondJson", useCondJson);
				} else {
					content.put("useCondJson", CherryUtil.map2Json(new HashMap())); // 将使用门槛不存在时,写一个空的
				}
			}

			newContentList.add(content);
		}

		return newContentList;
	}

	/**
	 * 设置UseCondJson
	 * @return
     */
	private String setUseCondJson(String contentNo, Map<String,Object> useCondMap,CouponRuleDTO couponRule) throws Exception {

		Map<String, Object> newUseCondMap = new HashMap<String, Object>();

		Map<String, Object> commonParam_map = MapBuilder.newInstance()
				.put("ruleCode", couponRule.getRuleCode())
				.put("conditionType", CherryConstants.conditionType_u)
				.put("contentNo", contentNo)
				.build();

		// 柜台
		// 柜台白名单-选项
		String counterKbn_w = ConvertUtil.getString(useCondMap.get("counterKbn_w"));
		if (!CherryChecker.isNullOrEmpty(counterKbn_w)) {
			if (CherryConstants.counterKbn_impType.equals(counterKbn_w)
					|| CherryConstants.counterKbn_channel2Counter.equals(counterKbn_w)) {
				Map<String, Object> cntParamW_map = MapBuilder.newInstance().put("filterType", CherryConstants.filterType_w).build();
				cntParamW_map.putAll(commonParam_map);
				List<Map<String, Object>> counterList_w = binOLSSPRM73_BL.getCounterList(cntParamW_map);
				useCondMap.put("counterList_w", counterList_w);
			}
		} else {
			useCondMap.put("counterKbn_w",CherryConstants.counterKbn_all);
		}

		// 柜台黑名单-选项
		Map<String, Object> cntParamB_map = MapBuilder.newInstance().put("filterType", CherryConstants.filterType_b).build();
		cntParamB_map.putAll(commonParam_map);
		List<Map<String,Object>> counterList_b = binOLSSPRM73_BL.getCounterList(cntParamB_map);
		useCondMap.put("counterList_b",counterList_b);

		// 产品
		// 产品白名单-选项
		String productKbn_w = ConvertUtil.getString(useCondMap.get("productKbn_w"));
		if (!CherryChecker.isNullOrEmpty(productKbn_w)) {
			Map<String, Object> prtParamW_map = MapBuilder.newInstance()
					.put("filterType", CherryConstants.filterType_w)
					.build();
			prtParamW_map.putAll(commonParam_map);

			// 产品选择类型为产品的产品明细集合
			if (CherryConstants.productKbn_selPrt.equals(productKbn_w)) {
				prtParamW_map.put("prtObjType", CherryConstants.prtObjType_prt);
				prtParamW_map.putAll(commonParam_map);
				List<Map<String, Object>> prtList_w = binOLSSPRM73_BL.getPrtForCouponProductDetail(prtParamW_map);
				for(Map<String,Object> prtInfo:prtList_w){
					prtInfo.put("prtVendorId",prtInfo.get("prtObjId"));
					prtInfo.put("proNum",prtInfo.get("prtObjNum"));
				}
				useCondMap.put("prtList_w", prtList_w);
			}

			if (CherryConstants.productKbn_impPrt.equals(productKbn_w)) {
				prtParamW_map.put("prtObjType", CherryConstants.prtObjType_prt);
				prtParamW_map.putAll(commonParam_map);
//				List<Map<String, Object>> prtList_w = binOLSSPRM73_BL.getImpPrtForCouponProductDetail(prtParamW_map);
//				useCondMap.put("prtList_w", prtList_w);
			}
			// 产品选择类型为分类的产品明细集合
			else if (CherryConstants.productKbn_selType.equals(productKbn_w)) {
				prtParamW_map.put("prtObjType", CherryConstants.prtObjType_type);
				prtParamW_map.putAll(commonParam_map);
				List<Map<String, Object>> proTypeList_w = binOLSSPRM73_BL.getCateForCouponProductDetail(prtParamW_map);
				for(Map<String,Object> prtInfo:proTypeList_w){
					prtInfo.put("cateValId",prtInfo.get("prtObjId"));
					prtInfo.put("proNum",prtInfo.get("prtObjNum"));
				}
				useCondMap.put("prtList_w", proTypeList_w);
			}

		} else {
			useCondMap.put("productKbn_w",CherryConstants.productKbn_all);
		}
		// 产品黑名单-选项
		String productKbn_b = ConvertUtil.getString(useCondMap.get("productKbn_b"));
		Map<String, Object> prtParamB_map = MapBuilder.newInstance()
				.put("filterType", CherryConstants.filterType_b)
				.build();
		prtParamB_map.put("prtObjType", CherryConstants.prtObjType_prt);
		prtParamB_map.putAll(commonParam_map);
//		List<Map<String, Object>> prtList_b = binOLSSPRM73_BL.getPrtForCouponProductDetail(prtParamB_map);
//		useCondMap.put("prtList_b", prtList_b);

		// TODO 对象
		/*
		// 对象白名单-选项
		String memberKbn_w = ConvertUtil.getString(useCondMap.get("memberKbn_w"));
		if (CherryConstants.memberKbn_impMem.equals(memberKbn_w)) {
			Map<String, Object> memParamW_map = MapBuilder.newInstance().put("filterType", CherryConstants.filterType_w).build();
			memParamW_map.putAll(commonParam_map);
			List<Map<String, Object>> memberList_w = binOLSSPRM73_BL.getExeclUploadMemberList(memParamW_map);
			useCondMap.put("memberList_w", memberList_w);
		}
		// 对象黑名单-选项
		String memberKbn_b = ConvertUtil.getString(useCondMap.get("memberKbn_b"));
		Map<String, Object> memParamB_map = MapBuilder.newInstance().put("filterType", CherryConstants.filterType_b).build();
		memParamB_map.putAll(commonParam_map);
		List<Map<String, Object>> memberList_w = binOLSSPRM73_BL.getPrtForCouponProductDetail(memParamB_map);
		useCondMap.put("memberList_w", memberList_w);
		*/

		// 活动
		// campList_w campList_b 只存在于useCond中

		// 使用时间
		// 已存在于useCond

		// 购买金额
		// 已存在于useCond

		newUseCondMap.putAll(useCondMap);
		String newUseCondJson = CherryUtil.map2Json(newUseCondMap);
		return newUseCondJson;
	}

	/**
	 * 处理发送门槛-产品/分类
	 * @param condInfo
	 * @param map
     */
	private void handelPrt(String conditionType,String contentNo,Map<String,Object> condInfo,Map<String,Object> map) {

		Map<String, Object> commonParamMap = MapBuilder.newInstance()
				.put("brandInfoId", ConvertUtil.getString(map.get("brandInfoId")))
				.put("ruleCode", ConvertUtil.getString(map.get("ruleCode")))
				.put("conditionType", conditionType)
				.put("contentNo", contentNo)
				.build();

		// 产品发送门槛-白名单处理
		String productKbn_w = ConvertUtil.getString(condInfo.get("productKbn_w"));
		if (!CherryChecker.isNullOrEmpty(productKbn_w)) {
			Map<String, Object> selWMap = MapBuilder.newInstance().put("filterType", CherryConstants.filterType_w).build();
			selWMap.putAll(commonParamMap);
			// 产品选择类型为产品的产品明细集合
			if (CherryConstants.productKbn_selPrt.equals(productKbn_w)) {
				selWMap.put("prtObjType", CherryConstants.prtObjType_prt);
				prt_s_w_PrtCouponPrtDetail = binOLSSPRM73_BL.getPrtForCouponProductDetail(selWMap);
			} else if (CherryConstants.productKbn_impPrt.equals(productKbn_w)) {
				selWMap.put("prtObjType", CherryConstants.prtObjType_prt);
				prt_s_w_PrtCouponPrtDetail = binOLSSPRM73_BL.getImpPrtForCouponProductDetail(selWMap);
			}
			// 产品选择类型为分类的产品明细集合
			else if (CherryConstants.productKbn_selType.equals(productKbn_w)) {
				selWMap.put("prtObjType", CherryConstants.prtObjType_type);
				prt_s_w_PrtCouponPrtDetail = binOLSSPRM73_BL.getCateForCouponProductDetail(selWMap);
			}
		} else {
			condInfo.put("productKbn_w",CherryConstants.productKbn_all);
		}

		// 产品发送门槛-黑名单处理
		//String productKbn_b = ConvertUtil.getString(condInfo.get("productKbn_b"));
		condInfo.put("productKbn_b",CherryConstants.productKbn_impPrt);
		Map<String, Object> selBMap = MapBuilder.newInstance().put("filterType", CherryConstants.filterType_b).build();
		selBMap.putAll(commonParamMap);

			// 产品选择类型为产品的产品明细集合
//			if (productKbn_impPrt.equals(productKbn_b)) {
				selBMap.put("prtObjType", CherryConstants.prtObjType_prt);
				prt_s_b_PrtCouponPrtDetail = binOLSSPRM73_BL.getPrtForCouponProductDetail(selBMap);
//			}

//		}
	}

	
	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 *
	 * @return String 跳转页面
     * @throws Exception 
	 * 
	 */
    public String save() throws Exception{
    	// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			commParams(map);
			CouponRuleDTO couponRule = form.getCouponRule(); 
			couponRule.setOrganizationInfoId(Integer.parseInt(String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID))));
			// 保存优惠券规则
			binOLSSPRM73_BL.tran_saveCouponRule(couponRule);
			coupEngine.refreshRule(couponRule.getRuleCode());
		} catch (Exception e) {
			this.addActionError(getText("ECM00005"));
			logger.error(e.getMessage(),e);
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
    }
    
    /**
   	 * 验证提交的参数
   	 *
   	 * @return boolean 验证结果
   	 * @throws Exception
   	 * 
   	 */
   	private boolean validateForm() throws Exception {
   		CouponRuleDTO couponRule = form.getCouponRule();
   		// 验证结果
   		boolean isCorrect = true;
   		// 规则名称
   		String ruleName = couponRule.getRuleName();
   		// 规则名称必须入力验证
   		if (CherryChecker.isNullOrEmpty(ruleName, true)) {
   			this.addFieldError("couponRule.ruleName",
   					getText("ECM00009", new String[] { getText("PSS01000") }));
   			isCorrect = false;
   		} else if (ruleName.length() > 40) {
   			// 规则名称不能超过40位验证
   			this.addFieldError(
   					"couponRule.ruleName",
   					getText("ECM00020", new String[] { getText("PSS01000"),
   							"40" }));
   			isCorrect = false;
   		}
   		// 发券开始日期
		String sendStartTime = couponRule.getSendStartTime();
		// 发券结束日期
		String sendEndTime = couponRule.getSendEndTime();
		boolean dateFlg = true;
		// 发券开始日期验证
		if (CherryChecker.isNullOrEmpty(sendStartTime, true)) {
			this.addFieldError("couponRule.sendStartTime",
   					getText("ECM00009", new String[] { getText("PSS01001") }));
   			isCorrect = false;
   			dateFlg = false;
		} else {
			// 日期格式验证
			if (!CherryChecker.checkDate(sendStartTime)) {
				this.addFieldError(
						"couponRule.sendStartTime",
						getText("ECM00008",
								new String[] { getText("PSS01001") }));
				isCorrect = false;
				dateFlg = false;
			}
		}
		// 发券结束日期验证
		if (CherryChecker.isNullOrEmpty(sendEndTime, true)) {
			this.addFieldError("couponRule.sendEndTime",
   					getText("ECM00009", new String[] { getText("PSS01002") }));
   			isCorrect = false;
   			dateFlg = false;
		} else {
			// 日期格式验证
			if (!CherryChecker.checkDate(sendEndTime)) {
				this.addFieldError(
						"couponRule.sendEndTime",
						getText("ECM00008",
								new String[] { getText("PSS01002") }));
				isCorrect = false;
				dateFlg = false;
			}
		}
		if (dateFlg) {
			// 发券开始日期在发券结束日期之后
			if (CherryChecker.compareDate(sendStartTime, sendEndTime) > 0) {
				this.addFieldError(
						"couponRule.sendEndTime",
						getText("ECM00033", new String[] { getText("PSS01002"),
								getText("PSS01001") }));
				isCorrect = false;
			}
		}
		String useCond = couponRule.getUseCond();
		if (!CherryChecker.isNullOrEmpty(useCond)) {
			List<Map<String, Object>> contentInfoList = (List<Map<String, Object>>)JSONUtil.deserialize(useCond);
			if (!StringUtils.isEmpty(contentInfoList)){
				for (Map<String, Object> contentInfo : contentInfoList) {
					String useMinAmount = ConvertUtil.getString(contentInfo.get("useMinAmount"));
					if(!StringUtils.isEmpty(useMinAmount)){
						if (!CherryChecker.isNumeric(useMinAmount)){
							this.addActionError(getText("PSS01006"));
							isCorrect = false;
							break;
						}
					}
					Map<String, Object> useTimeJson =(Map<String, Object>)contentInfo.get("useTimeJson");
					if (!StringUtils.isEmpty(useTimeJson)){
						String useTimeType = ConvertUtil.getString(useTimeJson.get("useTimeType"));
						if (useTimeType.equals(CherryConstants.useTimeType_0)){
							String useStartTime = ConvertUtil.getString(useTimeJson.get("useStartTime"));
							String useEndTime = ConvertUtil.getString(useTimeJson.get("useStartTime"));
							if (!StringUtils.isEmpty(useStartTime)){
								if (!CherryChecker.checkDate(useStartTime)) {
									this.addActionError(getText("ECM00008", new String[] { getText("PSS01004") }));
									isCorrect = false;
									break;
								}
							}
							if (!StringUtils.isEmpty(useEndTime)){
								if (!CherryChecker.checkDate(useEndTime)) {
									this.addActionError(getText("ECM00008", new String[] { getText("PSS01005") }));
									isCorrect = false;
									break;
								}
							}
							if (!CherryChecker.isNullOrEmpty(useStartTime)&&!CherryChecker.isNullOrEmpty(useEndTime)){
								if (CherryChecker.compareDate(useStartTime, useEndTime) > 0) {
									this.addActionError(
											getText("ECM00033", new String[] { getText("PSS01005"),
													getText("PSS01004") }));
									isCorrect = false;
									break;
								}
							}
						}else if (useTimeType.equals(CherryConstants.useTimeType_1)){
							String afterDays = ConvertUtil.getString(useTimeJson.get("afterDays"));
							if (!StringUtils.isEmpty(afterDays)){
								if (!CherryChecker.isNumeric(afterDays)){
									this.addActionError(getText("PSS01007"));
									isCorrect = false;
									break;
								}
							}

							String validity = ConvertUtil.getString(useTimeJson.get("validity"));
							if (!StringUtils.isEmpty(validity)){
								if (!CherryChecker.isNumeric(validity)){
									this.addActionError(getText("PSS01007"));
									isCorrect = false;
									break;
								}
							}
						}
					}
				}
			}
		}
		/*String useTime = couponRule.getUseTimeJson();
		Map<String, Object> useTimeInfo = null;
		if (!CherryChecker.isNullOrEmpty(useTime, true)) {
			try {
				useTimeInfo = (Map<String, Object>) JSONUtil.deserialize(useTime);
			} catch (Exception e) {
				logger.error("使用券时间JSON转换失败：" + e.getMessage(),e);
			}
		}
		if (null == useTimeInfo || useTimeInfo.isEmpty()) {
			this.addActionError(getText("ECM00009", new String[] { getText("PSS01003") }));
			isCorrect = false;
		} else {
			useTimeInfo.put("useTimeType", form.getUseTimeType());
			couponRule.setUseTimeJson(JSONUtil.serialize(useTimeInfo));
			// 指定日期
			if (CouponConstains.USETIMETYPE_0.equals(form.getUseTimeType())) {
				// 券使用开始日期
				String useStartTime = (String) useTimeInfo.get("useStartTime");
				// 券使用结束日期
				String useEndTime = (String) useTimeInfo.get("useEndTime");
				dateFlg = true;
				// 券使用开始日期验证
				if (CherryChecker.isNullOrEmpty(useStartTime, true)) {
					this.addFieldError("useStartTime",
		   					getText("ECM00009", new String[] { getText("PSS01004") }));
		   			isCorrect = false;
		   			dateFlg = false;
				} else {
					// 日期格式验证
					if (!CherryChecker.checkDate(useStartTime)) {
						this.addFieldError(
								"useStartTime",
								getText("ECM00008",
										new String[] { getText("PSS01004") }));
						isCorrect = false;
						dateFlg = false;
					}
				}
				// 券使用结束日期验证
				if (CherryChecker.isNullOrEmpty(useEndTime, true)) {
					this.addFieldError("useEndTime",
		   					getText("ECM00009", new String[] { getText("PSS01005") }));
		   			isCorrect = false;
		   			dateFlg = false;
				} else {
					// 日期格式验证
					if (!CherryChecker.checkDate(useEndTime)) {
						this.addFieldError(
								"useEndTime",
								getText("ECM00008",
										new String[] { getText("PSS01005") }));
						isCorrect = false;
						dateFlg = false;
					}
				}
				if (dateFlg) {
					// 券使用开始日期在券使用结束日期之后
					if (CherryChecker.compareDate(useStartTime, useEndTime) > 0) {
						this.addFieldError(
								"useEndTime",
								getText("ECM00033", new String[] { getText("PSS01005"),
										getText("PSS01004") }));
						isCorrect = false;
					}
				}
			}
		}*/
   		return isCorrect;
   	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		commParams(map);
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		return map;
	}
	
	private void commParams(Map<String, Object> map) {
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
	}
	
	/**
	 * 批量生成优惠券
	 * 
	 * @return 
	 */
	public String couponBatch() throws Exception {
		// 用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = getSearchMap();
		String ruleCode=ConvertUtil.getString(map.get("ruleCode"));
		// 取得优惠券规则详细信息
		CouponRuleDTO couponRule = binOLSSPRM73_BL.getCouponRuleInfo(map);
		// 批量生成优惠券(非会员)
		String batchMode=ConvertUtil.getString(map.get("batchMode"));
		try {
			if ("0".equals(batchMode) && !validateBatch(couponRule) ) {
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			if("0".equals(batchMode)){
				// 批量生成优惠券(非会员)
				binOLSSPRM73_BL.tran_couponBatch(couponRule, form.getBatchCount());
			}else if("1".equals(batchMode)){
				//获取该活动对应导入的会员活动信息
				map.put("conditionType", 3);
				List<Map<String, Object>> member_list=binOLSSPRM73_BL.getMemberInfoListWithoutPage(map);
				if(member_list != null && member_list.size()>0){
					List<Map<String,Object>> couponResultList=new ArrayList<Map<String,Object>>();
					binOLSSPRM73_BL.tran_couponBatch(couponRule, member_list.size(),member_list,couponResultList);
					//批量发短信操作
					if(couponResultList != null && couponResultList.size() > 0 && binOLCM14_BL.isConfigOpen("1370", userInfo.getBIN_OrganizationInfoID(), userInfo.getBIN_BrandInfoID())){
						for(Map<String,Object> coupon:couponResultList){
							BillInfo billInfo=new BillInfo();
							billInfo.setAllCoupon(ConvertUtil.getString(coupon.get("couponCode")));
							billInfo.setMobile(ConvertUtil.getString(coupon.get("mobile")));
							billInfo.setBrandInfoId(userInfo.getBIN_BrandInfoID());
							billInfo.setOrganizationInfoId(userInfo.getBIN_OrganizationInfoID());
							coupon_IF.sendSms(billInfo);
						}
					}
				}else{
					this.addActionError("没有查询到需要生成券的相关会员信息");
					return CherryConstants.GLOBAL_ACCTION_RESULT;
				}
			}else{
				logger.error("批量生成券：无效的batchMode，值为:"+batchMode);
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			this.addActionMessage(getText("ICM00002"));
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError(getText("ECM00089"));
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_DIALOG;
	}
	
	/**
   	 * 验证提交的参数
   	 * 
   	 * @param couponRule
   	 * @return boolean 验证结果
   	 * @throws Exception
   	 * 
   	 */
   	private boolean validateBatch(CouponRuleDTO couponRule) throws Exception {
   		// 验证结果
   		boolean isCorrect = true;
   		if (!CouponConstains.ISGIVE_1.equals(couponRule.getIsGive())) {
   			this.addActionError("不限会员的模式下，优惠券规则必须设置为允许转赠");
   			isCorrect = false;
   		}
   		return isCorrect;
   	}
	
	/**
	 * 批量生成优惠券对话框
	 * @return
	 * @throws Exception
	 */
	public String batchInit() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleCode", form.getRuleCode());
		// 取得优惠券规则详细信息
		CouponRuleDTO couponRule = binOLSSPRM73_BL.getCouponRuleInfo(map);
		form.setRuleName(couponRule.getRuleName());
		return SUCCESS;
	}
	
	/**
	 * 审核通过
	 * @throws Exception
	 */
	public void checkRule() throws Exception{
		Map<String, Object> msgMap = new HashMap<String, Object>();
		String errCode = "0";
		try {
			// 更新审核状态
			binOLSSPRM73_BL.tran_check(form.getRuleCode());
			coupEngine.refreshRule(form.getRuleCode());
		} catch (Exception e) {
			logger.error("**********"+e.getMessage()+"**********",e);
			errCode = "201";
		}
		msgMap.put("ERRORCODE", errCode);
		ConvertUtil.setResponseByAjax(response, msgMap);
	}
	
	/**
	 * <p>
	 * 导入门店处理
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importCounter() throws Exception {
		try {
			CouponRuleDTO couponRule = form.getCouponRule();
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 上传的文件
			map.put("upExcel", form.getUpExcel());
			// 活动编码
			map.put("ruleCode", couponRule.getRuleCode());
			// 品牌ID
			map.put("brandInfoId", couponRule.getBrandInfoId());
			// 条件类型
			map.put("conditionType", form.getConditionType());
			// 导入模式
			map.put("upMode", form.getUpMode());
			// 导入柜台处理
			Map<String, Object> resultMap = binOLSSPRM73_BL.tran_importCounter(map);
			// 结果代号
			int resultCode = Integer.parseInt(String.valueOf(resultMap.get("resultCode")));
			List<String> msgList = (List<String>) resultMap.get("msgList");
			if (0 == resultCode) {
				for (String msg : msgList) {
					this.addActionMessage(msg);
				}
			} else {
				for (String msg : msgList) {
					this.addActionError(msg);
				}
			}
		} catch (CherryException e) {
			logger.error(e.getErrMessage());
			this.addActionError(e.getErrMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError("导入发生异常");
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * <p>
	 * 批量生成券的时候导入会员手机号
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importCouponByMemberPhone() throws Exception {
		try {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 上传的文件
			map.put("upExcel", form.getUpExcel());
			map.put("ruleCode", form.getRuleCode());
			map.put("conditionType", form.getConditionType());
			map.put("upMode", form.getUpMode());
			// 导入会员处理
			Map<String, Object> resultMap = binOLSSPRM73_BL.tran_importCouponMember(map);
			// 结果代号
			int resultCode = Integer.parseInt(String.valueOf(resultMap.get("resultCode")));
			List<String> msgList = (List<String>) resultMap.get("msgList");
			if (0 == resultCode) {
				for (String msg : msgList) {
					this.addActionMessage(msg);
				}
			} else {
				for (String msg : msgList) {
					this.addActionError(msg);
				}
			}
		} catch (CherryException e) {
			logger.error(e.getErrMessage());
			this.addActionError(e.getErrMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError("导入发生异常");
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	
	/**
	 * 初始化柜台结果一览
	 * @return
	 * @throws Exception
	 */
	public String counterInit() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 柜台结果一览查询
	 * @return
	 * @throws Exception
	 */
	public String counterDialog() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("counterKey", form.getSSearch());
		}
		// 取得柜台总数
		int count = binOLSSPRM73_BL.getCounterDialogCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得柜台信息List
			form.setCounterList(binOLSSPRM73_BL.getCounterDialogList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 导入会员处理
	 * </p>
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String importMember() throws Exception {
		try {
			CouponRuleDTO couponRule = form.getCouponRule();
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			// 上传的文件
			map.put("upExcel", form.getUpExcel());
			// 活动编码
			map.put("ruleCode", couponRule.getRuleCode());
			// 品牌ID
			map.put("brandInfoId", couponRule.getBrandInfoId());
			// 条件类型
			map.put("conditionType", form.getConditionType());
			// 导入模式
			map.put("upMode", form.getUpMode());
			// 导入柜台处理
			Map<String, Object> resultMap = binOLSSPRM73_BL.tran_importMember(map);
			// 结果代号
			int resultCode = Integer.parseInt(String.valueOf(resultMap.get("resultCode")));
			List<String> msgList = (List<String>) resultMap.get("msgList");
			if (0 == resultCode) {
				for (String msg : msgList) {
					this.addActionMessage(msg);
				}
			} else {
				for (String msg : msgList) {
					this.addActionError(msg);
				}
			}
		} catch (CherryException e) {
			logger.error(e.getErrMessage());
			this.addActionError(e.getErrMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.addActionError("导入发生异常");
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * 初始化会员结果一览
	 * @return
	 * @throws Exception
	 */
	public String memberInit() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 会员结果一览查询
	 * @return
	 * @throws Exception
	 */
	public String memberDialog() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 画面查询条件
		if(form.getSSearch() != null && !"".equals(form.getSSearch())) {
			map.put("memberKey", form.getSSearch());
		}
		// 取得柜台总数
		int count = binOLSSPRM73_BL.getMemberDialogCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得会员信息List
			form.setMemberList(binOLSSPRM73_BL.getMemberDialogList(map));
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化渠道一览
	 * @return
	 * @throws Exception
	 */
	public String channelInit() throws Exception{
		return SUCCESS;
	}
	
	/**
	 * 渠道一览查询
	 * @return
	 * @throws Exception
	 */
	public void channelDialog() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		}
		List<Map<String, Object>> resultList = binOLSSPRM73_BL.getChannelList(map);
		
		if(null != resultList && resultList.size() > 0){
			List<Map<String, Object>> resultTreeList = new ArrayList<Map<String, Object>>();
			List<String[]> keysList = new ArrayList<String[]>();
			String[] keys1 = { "channelId", "name" };
			String[] keys2 = { "memCounterId", "counterName","counterCode" };
			keysList.add(keys1);
			keysList.add(keys2);
			ConvertUtil.jsTreeDataDeepList(resultList, resultTreeList,keysList, 0);
			ConvertUtil.setResponseByAjax(response, resultTreeList);
		}
		
	}

	/**
	 * 优惠券导入加载
	 * @return
     */
	public String execlLoadInit(){
		//execl导入类型 1为柜台导入 2为产品导入 3为会员导入
		int execLoadType = form.getExecLoadType();
		String resultStr = "";
		if (execLoadType==1){
			resultStr="counterDialog";
		} else if(execLoadType==2){
			resultStr="productDialog";
		} else if(execLoadType==3){
			resultStr="memberDialog";
		}
		return resultStr;
	}

	/**
	 * 券规则导入Execl通用map
	 * @param map
     */
	public void uploadComm(Map<String,Object> map){
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		CouponRuleDTO couponRule = form.getCouponRule();
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 上传的文件
		map.put("upExcel", form.getUpExcel());
		// 活动编码
		map.put("ruleCode", form.getRuleCode());
		// 品牌ID
		map.put("brandInfoId", couponRule.getBrandInfoId());
		// 条件类型
		map.put("conditionType", form.getConditionType());
		// 导入模式
		map.put("upMode", form.getUpMode());
		// 子券No
		map.put("contentNo",ConvertUtil.getInt(form.getContentNo()));
		// 黑白名单
		map.put("filterType",form.getFilterType());

	}

	/**
	 * 券规则柜台Execl导入通用
	 * @return
	 * @throws Exception
	 */
	public void counterExeclLoad() throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			//时间戳
			String operateFlag = DateUtil.date2String(new Date(),"yyyyMMddHHmmss");
			map.put("operateFlag",operateFlag);
			// 传递页面参数
			uploadComm(map);
			// 导入柜台处理
			resultMap = binOLSSPRM73_BL.tran_importCounterExecl(map);
			// 结果代号
			int resultCode = Integer.parseInt(String.valueOf(resultMap.get(CouponConstains.RESULT_CODE)));
			if (0 != resultCode) {
				resultMap.put("operateFlag",operateFlag);
			}
			ConvertUtil.setResponseByAjax(response,CherryUtil.map2Json(resultMap));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			resultMap.put(CouponConstains.RESULT_CODE,CouponConstains.Fail_Flag);
			resultMap.put(CouponConstains.RESULT_MESSAGE,e.getMessage());
			ConvertUtil.setResponseByAjax(response,resultMap);
		}
	}

	/**
	 * 券规则产品Execl导入通用
	 * @return
	 * @throws Exception
	 */
	public void productExeclLoad() throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			//时间戳
			String operateFlag = DateUtil.date2String(new Date(),"yyyyMMddHHmmss");
			map.put("operateFlag",operateFlag);
			// 传递页面参数
			uploadComm(map);
			// 导入柜台处理
			resultMap = binOLSSPRM73_BL.tran_importProductExecl(map);
			// 结果代号
			int resultCode = Integer.parseInt(String.valueOf(resultMap.get("resultCode")));
			if (0 != resultCode) {
				resultMap.put("operateFlag",operateFlag);
			}
			ConvertUtil.setResponseByAjax(response,resultMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			resultMap.put(CouponConstains.RESULT_CODE,CouponConstains.Fail_Flag);
			resultMap.put(CouponConstains.RESULT_MESSAGE,e.getMessage());
			ConvertUtil.setResponseByAjax(response,resultMap);
		}
	}

	/**
	 * 券规则产品Execl导入通用
	 * @return
	 * @throws Exception
	 */
	public void memberExeclLoad() throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 参数MAP
			Map<String, Object> map = new HashMap<String, Object>();
			//时间戳
			String operateFlag = DateUtil.date2String(new Date(),"yyyyMMddHHmmss");
			map.put("operateFlag",operateFlag);
			// 传递页面参数
			uploadComm(map);
			// 导入柜台处理
			resultMap = binOLSSPRM73_BL.tran_importMemberExecl(map);
			// 结果代号
			int resultCode = Integer.parseInt(String.valueOf(resultMap.get("resultCode")));
			if (0 != resultCode) {
				resultMap.put("operateFlag",operateFlag);
			}
			ConvertUtil.setResponseByAjax(response,resultMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			resultMap.put(CouponConstains.RESULT_CODE,CouponConstains.Fail_Flag);
			resultMap.put(CouponConstains.RESULT_MESSAGE,e.getMessage());
			ConvertUtil.setResponseByAjax(response,resultMap);
		}
	}

	/**
	 * 查找导入失败的柜台List
	 * @throws Exception
     */
	public String searchFailUploadCounter() throws Exception{
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		FailUploadDataDTO failUploadDataDTO = form.getFailUploadDataDTO();
		// 活动编码
		map.put("ruleCode", failUploadDataDTO.getRuleCode());
		// 黑白名单
		map.put("filterType",failUploadDataDTO.getFilterType());
		// 条件类型
		map.put("conditionType", failUploadDataDTO.getConditionType());
		// 操作区分
		map.put("operateType","1");
		// 时间戳
		map.put("operateFlag",failUploadDataDTO.getOperateFlag());
		// 子券No
		map.put("contentNo",ConvertUtil.getInt(failUploadDataDTO.getContentNo()));
		// dataTable上传的参数设置到map
		map.put("START", form.getIDisplayStart() + 1);
		map.put("END", form.getIDisplayStart()
				+ form.getIDisplayLength());
		int count = binOLSSPRM73_BL.getFailUploadCount(map);
		if(count!=0){
			List<Map<String,Object>>  failList = binOLSSPRM73_BL.getFailUploadList(map);
			form.setFailList(failList);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}

	/**
	 * 查找导入失败的会员List
	 * @throws Exception
	 */
	public String searchFailUploadMember() throws Exception{
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		FailUploadDataDTO failUploadDataDTO = form.getFailUploadDataDTO();
		// 活动编码
		map.put("ruleCode", failUploadDataDTO.getRuleCode());
		// 黑白名单
		map.put("filterType",failUploadDataDTO.getFilterType());
		// 条件类型
		map.put("conditionType", failUploadDataDTO.getConditionType());
		// 操作区分
		map.put("operateType","3");
		// 时间戳
		map.put("operateFlag",failUploadDataDTO.getOperateFlag());
		// 子券No
		map.put("contentNo",ConvertUtil.getInt(failUploadDataDTO.getContentNo()));
		// dataTable上传的参数设置到map
		map.put("START", form.getIDisplayStart() + 1);
		map.put("END", form.getIDisplayStart()
				+ form.getIDisplayLength());
		int count = binOLSSPRM73_BL.getFailUploadCount(map);
		if(count!=0){
			List<Map<String,Object>> failList = binOLSSPRM73_BL.getFailUploadList(map);
			form.setFailList(failList);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}

	/**
	 * 查找导入失败的产品List
	 * @throws Exception
     */
	public String searchFailUploadProduct() throws Exception{
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		FailUploadDataDTO failUploadDataDTO = form.getFailUploadDataDTO();
		// 活动编码
		map.put("ruleCode", failUploadDataDTO.getRuleCode());
		// 黑白名单
		map.put("filterType",failUploadDataDTO.getFilterType());
		// 条件类型
		map.put("conditionType", failUploadDataDTO.getConditionType());
		// 操作区分
		map.put("operateType","2");
		// 时间戳
		map.put("operateFlag",failUploadDataDTO.getOperateFlag());
		// 子券No
		map.put("contentNo",ConvertUtil.getInt(failUploadDataDTO.getContentNo()));
		// dataTable上传的参数设置到map
		map.put("START", form.getIDisplayStart() + 1);
		map.put("END", form.getIDisplayStart()
				+ form.getIDisplayLength());
		int count = binOLSSPRM73_BL.getFailUploadCount(map);
		if(count!=0){
			List<Map<String,Object>>  failList = binOLSSPRM73_BL.getFailUploadList(map);
			form.setFailList(failList);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if("1".equals(failUploadDataDTO.getFilterType())){//1为白名单
			return "popLoadProduct1";
		}else{//2为黑名单
			return "popLoadProduct2";
		}

	}

	/**
	 * 读取导入的产品
	 * @throws Exception
	 */
	public void readCouponPrtDetail() throws Exception{

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> map = MapBuilder.newInstance()
				.put("ruleCode",form.getRuleCode())
				.put("conditionType",form.getConditionType())
				.put("filterType",form.getFilterType())
				.put("contentNo",form.getContentNo())
				.put("prtObjType",form.getPrtObjType())
				.build();

		try {
			// 读取电子券规则
//			CouponRuleDTO couponRule = binOLSSPRM73_BL.getCouponRuleInfo(map);

			// 读取电子券产品明细表
			List<Map<String, Object>> couponProductDetail = binOLSSPRM73_BL.getImpPrtForCouponProductDetail(map);

			resultMap.put("couponProductDetail", couponProductDetail);
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
		catch(Exception e){
			ConvertUtil.setResponseByAjax(response, resultMap);
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 使用门槛-确定后处理导入数据的问题
	 * @throws Exception
	 */
	public void confirmUseCond() throws Exception{

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> map = MapBuilder.newInstance()
				.put("ruleCode",form.getRuleCode())
				.put("contentNo",form.getContentNo())
				.put("conditionType",CherryConstants.conditionType_u)
				.build();

		try {

			String result = binOLSSPRM73_BL.tran_confirmUseCond(map);

			resultMap.put("result", result);
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
		catch(Exception e){
			ConvertUtil.setResponseByAjax(response, resultMap);
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 使用门槛-取消后处理导入数据的问题
	 * @throws Exception
	 */
	public void cancelUseCond() throws Exception{

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> map = MapBuilder.newInstance()
				.put("ruleCode",form.getRuleCode())
				.put("contentNo",form.getContentNo())
				.put("conditionType",CherryConstants.conditionType_u)
				.put("isTemp",CouponConstains.ISTEMP_3)
				.build();

		try {

			String result = binOLSSPRM73_BL.tran_cancelUseCond(map);

			resultMap.put("result", result);
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
		catch(Exception e){
			ConvertUtil.setResponseByAjax(response, resultMap);
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 删除指定的门槛条件/黑白名单过滤类型/contentNo下的产品明细
	 * @throws Exception
	 */
	public void delCouponPrtDetailForConditionTypeFilterTypeContentNo() throws Exception{

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> delMap = MapBuilder.newInstance()
				.put("prtObjType",form.getPrtObjType())
				.put("prtObjId",form.getPrtObjId())
				.build();
		this.delCouponPrtDetail(delMap);
	}

	/**
	 * 删除指定的产品节点
	 * @throws Exception
	 */
	public void delCouponPrtDetailForNode() throws Exception{

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> delMap = MapBuilder.newInstance()
				.put("prtObjType",form.getPrtObjType())
				.put("prtObjId",form.getPrtObjId())
				.build();
		this.delCouponPrtDetail(delMap);
	}

	/**
	 * 删除指定的导入产品节点
	 * @throws Exception
	 */
	public void delImpCouponPrtDetailForNode() throws Exception{

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> delMap = MapBuilder.newInstance()
				.put("ruleCode",form.getRuleCode())
				.put("conditionType",form.getConditionType())
				.put("filterType",form.getFilterType())
				.put("contentNo",form.getContentNo())
				.put("prtObjType",form.getPrtObjType())
				.put("prtObjId",form.getPrtObjId())
				.build();

		try {

			// 删除电子券产品明细
			binOLSSPRM73_BL.tran_delImpCouponProductDetail(delMap);

			resultMap.put("result", "0");
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
		catch(Exception e){
			resultMap.put("result", "1");
			resultMap.put("errorMsg", "删除失败!");
			ConvertUtil.setResponseByAjax(response, resultMap);
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 删除指定电子券产品明细
	 * @throws Exception
	 */
	private void delCouponPrtDetail(Map<String, Object> map) throws Exception{

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 参数MAP
		Map<String, Object> delMap = MapBuilder.newInstance()
				.put("ruleCode",form.getRuleCode())
				.put("conditionType",form.getConditionType())
				.put("filterType",form.getFilterType())
				.put("contentNo",form.getContentNo())
				.build();

		try {
			delMap.putAll(map);

			// 删除电子券产品明细
			binOLSSPRM73_BL.delCouponProductDetail(delMap);

			resultMap.put("result", "0");
			ConvertUtil.setResponseByAjax(response, resultMap);
		}
		catch(Exception e){
			resultMap.put("result", "1");
			resultMap.put("errorMsg", "删除失败!");
			ConvertUtil.setResponseByAjax(response, resultMap);
			logger.error(e.getMessage(), e);


		}
	}

	public List<Map<String, Object>> getPrt_s_w_PrtCouponPrtDetail() {
		return prt_s_w_PrtCouponPrtDetail;
	}

	public void setPrt_s_w_PrtCouponPrtDetail(List<Map<String, Object>> prt_s_w_PrtCouponPrtDetail) {
		this.prt_s_w_PrtCouponPrtDetail = prt_s_w_PrtCouponPrtDetail;
	}

	public List<Map<String, Object>> getPrt_s_b_PrtCouponPrtDetail() {
		return prt_s_b_PrtCouponPrtDetail;
	}

	public void setPrt_s_b_PrtCouponPrtDetail(List<Map<String, Object>> prt_s_b_PrtCouponPrtDetail) {
		this.prt_s_b_PrtCouponPrtDetail = prt_s_b_PrtCouponPrtDetail;
	}

	public Map getBigContentMap() {
		return bigContentMap;
	}

	public void setBigContentMap(Map bigContentMap) {
		this.bigContentMap = bigContentMap;
	}

	public String getUseCondJson() {
		return useCondJson;
	}

	public void setUseCondJson(String useCondJson) {
		this.useCondJson = useCondJson;
	}

	/**
	 * 获取柜台白名单或者黑名单的AJAX方法
	 */
	public void getCNTList()  throws Exception{
		String filterType=ConvertUtil.getString(form.getFilterType());//黑白名单区别
		String ruleCode=ConvertUtil.getString(form.getRuleCode());//规则Code
		String conditionType=ConvertUtil.getString(form.getConditionType());//用券发券区分
		String contentNo=ConvertUtil.getString(form.getContentNo());//子券No区分
		if("".equals(filterType) || "".equals(ruleCode) ||"".equals(conditionType) || "".equals(contentNo)){
			ConvertUtil.setResponseByAjax(response, "ERROR");
		}else{
			Map<String, Object> cntParam_map = new HashMap<String, Object>();
			cntParam_map.put("ruleCode",ruleCode);
			cntParam_map.put("filterType",filterType);//黑名单
			cntParam_map.put("conditionType",conditionType);//发券门槛
			cntParam_map.put("contentNo",contentNo);//子券No区分
			List<Map<String,Object>> cntList=binOLSSPRM73_BL.getCounterList(cntParam_map);
			ConvertUtil.setResponseByAjax(response, cntList);
		}
	}

	public void delCNT()  throws Exception{
		String filterType=ConvertUtil.getString(form.getFilterType());//黑白名单区别
		String ruleCode=ConvertUtil.getString(form.getRuleCode());//规则Code
		String conditionType=ConvertUtil.getString(form.getConditionType());//用券发券区分
		String organizationID=form.getOrganizationID();//删除的柜台号
		String contentNo=ConvertUtil.getString(form.getContentNo());
		if("".equals(filterType) || "".equals(ruleCode) ||"".equals(conditionType) || "".equals(contentNo)){
			ConvertUtil.setResponseByAjax(response, "ERROR");
		}else{
			Map<String,Object> param=MapBuilder.newInstance()
				.put("ruleCode",ruleCode)
				.put("contentNo",contentNo)
				.put("conditionType",conditionType)
				.put("filterType",filterType)
				.put("organizationID",organizationID)
				.build();
			int result=binOLSSPRM73_BL.removeCntLi(param);
			ConvertUtil.setResponseByAjax(response, result);
		}
	}


	private String getCntChannelString(List<Map<String,Object>> counterList){
		StringBuffer result=new StringBuffer();
		for(int i=0;i<counterList.size();i++){
			String counterCode=ConvertUtil.getString(counterList.get(i).get("organizationID"));
			if(i != counterList.size()-1){
				result.append(counterCode+",");
			}else{
				result.append(counterCode);
			}
		}

		return result.toString();
	}

	public void getExeclUploadMemberList() throws Exception{
		String conditionType = form.getConditionType();
		String filterType = form.getFilterType();
		String ruleCode = form.getRuleCode();
		Integer contentNo = ConvertUtil.getInt(form.getContentNo());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("conditionType",conditionType);
		map.put("filterType",filterType);
		map.put("ruleCode",ruleCode);
		map.put("contentNo",contentNo);
		List<Map<String,Object>> memberList =  binOLSSPRM73_BL.getExeclUploadMemberList(map);
		ConvertUtil.setResponseByAjax(response,memberList);
	}

	public void getMemLevelList() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		List<Map<String,Object>> list = binOLSSPRM73_BL.getLevelList(ConvertUtil.getInt(userInfo.getBIN_BrandInfoID()));
		ConvertUtil.setResponseByAjax(response,list);
	}


	public String initUseContent(){

		String useCondJson = this.getUseCondJson();
		try {
			Map<String,Object> useCondJsonMap = CherryUtil.json2Map(useCondJson);

			Map<String, Object> commonParam_map = MapBuilder.newInstance()
					.put("ruleCode", form.getRuleCode())
					.put("conditionType", CherryConstants.conditionType_u)
					.put("contentNo", ConvertUtil.getString(form.getContentNo()))
					.build();

			// TODO 柜台
			//已经导入的柜台黑名单列表(发送门槛)
			Map<String, Object> cntParam_map = new HashMap<String, Object>();
			cntParam_map.putAll(commonParam_map);
			cntParam_map.put("filterType",CherryConstants.filterType_b);//黑名单
			List<Map<String,Object>> cntBlackList=binOLSSPRM73_BL.getCounterList(cntParam_map);
			useCondJsonMap.put("cntBlackList",cntBlackList);

			String counterKbn_w=ConvertUtil.getString(useCondJsonMap.get("counterKbn_w"));
			//根据选择选择项来确定是否需要确定查询白名单
			if(CherryConstants.counterKbn_impType.equals(counterKbn_w)){//(1为柜台导入模式)
				cntParam_map.put("filterType",CherryConstants.filterType_w);//白名单
				List<Map<String,Object>> cntWhiteList=binOLSSPRM73_BL.getCounterList(cntParam_map);
				useCondJsonMap.put("cntWhiteList",cntWhiteList);
			} else if (CherryConstants.counterKbn_channel2Counter.equals(counterKbn_w)) {
				List<Map<String,Object>> counterList_w = (List<Map<String, Object>>) useCondJsonMap.get("counterList_w");
				if (null != counterList_w && counterList_w.size() != 0) {
					useCondJsonMap.put("cntwhiteChannel_use",this.getCntChannelString(counterList_w));
				}
			}else if(CherryConstants.counterKbn_channel.equals(counterKbn_w)){
				List<Map<String,Object>> channel_list= binOLSSPRM73_BL.seachChannelWay(this.getSessionInfo());
				List<Map<String,Object>> cntChannelList_w=(List<Map<String,Object>>)useCondJsonMap.get("cntChannelList_w");
				this.convertChannelList(channel_list,cntChannelList_w);
				useCondJsonMap.put("channel_list",channel_list);
			}

			// TODO 产品
//			Map prtMap = MapBuilder.newInstance().put("ruleCode",form.getRuleCode()).build();
//			this.handelPrt(CherryConstants.conditionType_u,ConvertUtil.getString(form.getContentNo()),useCondJsonMap,prtMap);
			// 产品白名单-选项
			String productKbn_w = ConvertUtil.getString(useCondJsonMap.get("productKbn_w"));
			if (!CherryChecker.isNullOrEmpty(productKbn_w)) {
				Map<String, Object> prtParamW_map = MapBuilder.newInstance().put("filterType", CherryConstants.filterType_w).build();
				prtParamW_map.putAll(commonParam_map);

				// 产品选择类型为产品的产品明细集合
				if (CherryConstants.productKbn_impPrt.equals(productKbn_w)) {
					prtParamW_map.put("prtObjType", CherryConstants.prtObjType_prt);
					prtParamW_map.putAll(commonParam_map);
					List<Map<String, Object>> prtList_w = binOLSSPRM73_BL.getImpPrtForCouponProductDetail(prtParamW_map);
					useCondJsonMap.put("prtList_w", prtList_w);
				}

			} else {
				useCondJsonMap.put("productKbn_w",CherryConstants.productKbn_all);
			}

			// 产品黑名单-选项
			String productKbn_b = ConvertUtil.getString(useCondJsonMap.get("productKbn_b"));
			Map<String, Object> prtParamB_map = MapBuilder.newInstance().put("filterType", CherryConstants.filterType_b).build();
			prtParamB_map.put("prtObjType", CherryConstants.prtObjType_prt);
			prtParamB_map.putAll(commonParam_map);
			List<Map<String, Object>> prtList_b = binOLSSPRM73_BL.getImpPrtForCouponProductDetail(prtParamB_map);
			useCondJsonMap.put("prtList_b", prtList_b);

			// TODO 活动

			//使用对象
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌ID
			int brandInfoId = userInfo.getBIN_BrandInfoID();
			List<Map<String, Object>> levelList = binOLSSPRM73_BL.getLevelList(brandInfoId);
			String memberKbn_w = ConvertUtil.getString(useCondJsonMap.get("memberKbn_w"));

			if(memberKbn_w.equals(CouponConstains.MEMBERKBN_2)){//会员等级
				String levelStr = ConvertUtil.getString(useCondJsonMap.get("memLevel_w"));
				String[] level = levelStr.split(",");
				for(Map<String,Object> levelMap : levelList){
					String id = ConvertUtil.getString(levelMap.get("levelId"));
					for (String temp : level){
						if (id.equals(temp)){
							levelMap.put("flag","1");
							break;
						}
					}
				}
				useCondJsonMap.put("memLevel_w",levelList);
			}
			// TODO 使用时间
			if (!ConvertUtil.getString(useCondJsonMap.get("useTimeJson")).equals("")) {
				Map<String, Object> useTimeJson = (Map<String, Object>)useCondJsonMap.get("useTimeJson");
				for (Map.Entry<String, Object> entry : useTimeJson.entrySet()) {
					useCondJsonMap.put(entry.getKey(), ConvertUtil.getString(entry.getValue()));
				}
			}

			// TODO 购买金额

			form.setUseCondInfo(useCondJsonMap);

			form.setUseCondInfo(useCondJsonMap);
		} catch (Exception e) {
			logger.error("解析useCondJson失败:" + e.getMessage(),e);
		}
		return SUCCESS;
	}

	public String export() throws JSONException{
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		FailUploadDataDTO failUploadDataDTO = form.getFailUploadDataDTO();
		// 活动编码
		map.put("ruleCode", failUploadDataDTO.getRuleCode());
		// 黑白名单
		map.put("filterType",failUploadDataDTO.getFilterType());
		// 条件类型
		map.put("conditionType", failUploadDataDTO.getConditionType());
		// 操作区分
		map.put("operateType",failUploadDataDTO.getOperateType());
		// 时间戳
		map.put("operateFlag",failUploadDataDTO.getOperateFlag());
		// 子券No
		map.put("contentNo",ConvertUtil.getInt(failUploadDataDTO.getContentNo()));
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		try{
			String language = ConvertUtil.getString(session.get(CherryConstants.SESSION_LANGUAGE));
			if(CouponConstains.Fail_OperateType_1.equals(failUploadDataDTO.getOperateType())){
				downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM73", language, "downloadFileNameForCounter");
			}else if(CouponConstains.Fail_OperateType_2.equals(failUploadDataDTO.getOperateType())){
				downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM73", language, "downloadFileNameForProduct");
			}else{
				downloadFileName = binOLMOCOM01_BL.getResourceValue("BINOLSSPRM73", language, "downloadFileNameForMember");
			}
			setExcelStream(new ByteArrayInputStream(binOLSSPRM73_BL.exportExcel(map)));
		}catch (Exception e){
			this.addActionError(getText("EMO00022"));
			e.printStackTrace();
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return "BINOLSSPRM73_excel";
	}

	/**
	 * 删除导入会员明细节点
	 */
	public void delImpCouponMemberDetail() throws Exception{
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 活动编码
		map.put("ruleCode", form.getRuleCode());
		// 黑白名单
		map.put("filterType",form.getFilterType());
		// 条件类型
		map.put("conditionType", form.getConditionType());
		// 子券No
		map.put("contentNo",ConvertUtil.getInt(form.getContentNo()));
		// 会员手机
		map.put("mobile",form.getMobile());
		int resultCode = binOLSSPRM73_BL.tran_delImpCouponMemberDetail(map);
		ConvertUtil.setResponseByAjax(response,resultCode);
	}


	/**
	 * 查询出相关的渠道信息
     */
	public void seachChannelWay() throws Exception{
		Map<String,Object> param=this.getSessionInfo();
		List<Map<String,Object>> channelList= binOLSSPRM73_BL.seachChannelWay(param);
		Map<String,Object> result_map=MapBuilder.newInstance().put("channelList",channelList).build();
		ConvertUtil.setResponseByAjax(response,result_map);
	}

	/**
	 * 取得session的信息
	 * @throws Exception
	 */
	private Map getSessionInfo() throws Exception{
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		Map<String, Object> map = new HashMap<String, Object>();
		// 取得所属组织
		map.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
		String brandInfoID = (String.valueOf(userInfo.getBIN_BrandInfoID()));
		if (!brandInfoID.equals("-9999")){
			// 取得所属品牌
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}else{
			map.put("brandInfoId",userInfo.getCurrentBrandInfoID());
		}
		map.put("organizationId", userInfo.getBIN_OrganizationID());
		map.put("language", userInfo.getLanguage());
		map.put("userID", userInfo.getBIN_UserID());
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		map.put("userName", userInfo.getLoginName());
		map.put("brandCode", userInfo.getBrandCode());
//		map.put("locationType", locationType);
//		map.put("loadingCnt", loadingCnt);
		return map;
	}

	private void convertChannelList(List<Map<String,Object>>channel_list,List<Map<String,Object>> cntChannelList_w){
		if(channel_list != null && cntChannelList_w != null){
			for(Map<String,Object> channel_info:channel_list){
				String channel_info_id=ConvertUtil.getString(channel_info.get("id"));
				if("".equals(channel_info_id)){
					continue;
				}
				for(Map<String,Object> cntChannelInfo:cntChannelList_w){
					String cntChannelInfo_id=ConvertUtil.getString(cntChannelInfo.get("id"));
					if("".equals(cntChannelInfo_id)){
						continue;
					}
					if(channel_info_id.equals(cntChannelInfo_id)){
						channel_info.put("checkFlag","1");
					}
				}

			}
		}
	}

}
