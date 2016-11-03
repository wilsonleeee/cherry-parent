/*		
 * @(#)BINOLCM60_BL.java     1.0 2015/09/24	
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
package com.cherry.cm.cmbussiness.bl;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.form.SaleDetailEntity;
import com.cherry.cm.cmbussiness.form.SaleMainEntity;
import com.cherry.cm.cmbussiness.form.SaleRuleResultEntity;
import com.cherry.cm.cmbussiness.service.BINOLCM60_Service;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.client.WebserviceClient;

/**
 * 电商接口相关业务共通
 * @author jijw
 *
 */
public class BINOLCM60_BL {
	
	static{
		// 智能促销是否加载  0：加载dll 1：不加载dll 
		String loadDll = PropertiesUtil.pps.getProperty("smartPromotion.loadDll");
		if("1".equals(loadDll)){
			String path =BINOLCM60_BL.class.getClassLoader().getResource("").getPath();
			System.load(path+"JniServer_TB.dll");
		}
	}
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCM60_BL.class);
	
	@Resource(name="binOLCM60_Service")
	private BINOLCM60_Service binOLCM60_Service; 
	
    /** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
    @Resource(name="binOLCM15_BL")
    private BINOLCM15_BL binOLCM15_BL;
    
	/**
	 * 查询：1、查出满足要求的规则 2 、推荐出符合条件的规则 3 、最优解需要计算；终端每次传入客户输入的产品
	 * 备注：接口函数提供
	 * @param ip
	 * @param port
	 * @param dataLogFlag
	 * @param abbr
	 * @param BIN_OrganizationInfoID
	 * @param inputsalemain
	 * @param inputdetail
	 * @param outsalemain
	 * @param outdetail
	 * @param outresult
	 * @return
	 */
	public native int Cloud_MatchRule(String ip,String port,String dataLogFlag,String abbr ,String  BIN_OrganizationInfoID ,  
			List<SaleMainEntity> inputsalemain,List<SaleDetailEntity> inputdetail,
			List<SaleMainEntity> outsalemain,List<SaleDetailEntity> outdetail,List<SaleRuleResultEntity> outresult);
	
	/**
	 * 匹配活动规则，取得最优解活动的产品信息集合
	 * @param paramMap
	 * paramMap.brandCode 品牌CODE
	 * paramMap.organizationInfoId 柜台组织结构ID
	 * paramMap.mainInfo 主单数据 Map<String,Object>
	 * -- map.mainInfo.memberCode 会员号
	 * -- map.mainInfo.saleDate 交易日期 yyMMdd
	 * -- map.mainInfo.saleTime 交易时间 HHmmss
	 * -- map.mainInfo.baCode  美容顾问CODE
	 * -- map.mainInfo.counterCode 柜台号
	 * -- map.mainInfo.totalDiscountRate 整单折扣率
	 * --  
	 * paramMap.detailList 明细数据  List<Map<String,Object>>
	 * 		-- barCode 商品条码			
	 * 		-- unitCode 厂商编码			
	 * 		-- quantity 数量			
	 * 		-- price 价格			
	 * 		-- oriPrice 原价			
	 * 		-- proType 产品类型,产品是N，促销品是P			
	 * 		-- mainCode 主活动码			
	 * 		-- mainName 主活动名称			
	 * 		-- discount 单品折扣			
	 * 		-- productid 产品id(对应新后台的产品厂商ID)			
	 * 		-- proName 产品名称			
	 * 		-- mainitem_tag 一个主活动内部的分组号			
	 * 		-- activityCode 活动code			
	 * 		-- newFlag 是否是促销引擎新增的记录，如果0不是新增的记录，如果是1是新增的记录	
	 * 
	 * @return
	 * resultMap
	 * 		--resultMap.resultNum  int 返回结果标识  2：成功 
	 * 		--resultMap.outSalemainList  List<SaleMainEntity>
	 * 		--resultMap.outDetailList  List<SaleDetailEntity> 
	 * 		--resultMap.newFlagOutDetailList  List<SaleDetailEntity> 促销引擎新增的记录
	 * 		--resultMap.outResultList List<SaleRuleResultEntity> 引擎返回的促销活动
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getActProMap(Map<String,Object> paramMap){
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		List<SaleMainEntity> outSalemain  = new ArrayList<SaleMainEntity>(); 
		List<SaleDetailEntity> outDetail  = new ArrayList<SaleDetailEntity>();
		List<SaleRuleResultEntity> outResult= new ArrayList<SaleRuleResultEntity>();
		
		String brandCode = ConvertUtil.getString(paramMap.get("brandCode")); // 品牌CODE
//		String brandCode = "AVENE"; // 测试用
		String organizationId = ConvertUtil.getString(paramMap.get("organizationId")); // 组织结构ID
//		organizationId = "335"; // 测试用
		
		// 主单数据 
		Map<String,Object> mainInfo = (Map<String,Object>)paramMap.get("mainInfo");
		ArrayList<SaleMainEntity> inputSalemain=new ArrayList<SaleMainEntity>();
		SaleMainEntity sale_main = new SaleMainEntity();
		sale_main.setMemcode(ConvertUtil.getString(mainInfo.get("memberCode"))); //会员号
		sale_main.setTxddate(ConvertUtil.getString(mainInfo.get("saleDate"))); // 交易日期
		sale_main.setTxdtime(ConvertUtil.getString(mainInfo.get("saleTime"))); // 交易时间
		
		sale_main.setBacode(ConvertUtil.getString(mainInfo.get("baCode"))); // ba号
		sale_main.setCountercode(ConvertUtil.getString(mainInfo.get("counterCode")));// 柜台号
		
		sale_main.setDiscount_zd(Double.parseDouble("".equals(ConvertUtil.getString(mainInfo.get("totalDiscountRate"))) 
				? "100.0"
				: ConvertUtil.getString(mainInfo.get("totalDiscountRate")))); // 整单折扣率
		
		inputSalemain.add(sale_main);
		
		// 明细数据
		List<Map<String,Object>> detailList = (List<Map<String,Object>>)paramMap.get("detailList");
		List<SaleDetailEntity> inputSaledetail = saleDetail2Entity(detailList);
		
		// 调用接口
		String IP=PropertiesUtil.pps.getProperty("smartPromotion.IP");
		String PORT=PropertiesUtil.pps.getProperty("smartPromotion.PORT");
		String logFlag=PropertiesUtil.pps.getProperty("smartPromotion.LogFlag");
		int num_back = this.Cloud_MatchRule(IP,PORT, logFlag, brandCode, organizationId, inputSalemain, inputSaledetail,
				outSalemain, outDetail, outResult);
		
		this.SmartLog_MatchRule(num_back);
		
		if(num_back == 1){
			// 返回成功后，写入resultMap
			resultMap.put("outSalemain", outSalemain);
			resultMap.put("outDetail", outDetail);
			resultMap.put("outResult", outResult);
			// 定义促销引擎新增的记录
			List<SaleDetailEntity> newFlagOutDetail = new ArrayList<SaleDetailEntity>(); 
			for(SaleDetailEntity sde : outDetail){
				if(sde.getNew_flag() == 1){
					newFlagOutDetail.add(sde);
				}
			}
			resultMap.put("newFlagOutDetail", newFlagOutDetail);
			
			resultMap.put("resultNum", num_back);
		}
		
//		ConvertUtil.convertDTO(dto1, dto2, flg);
		// 转换Entity为Map
		if(resultMap.size() != 0){
			convertSaleEntity2Map(resultMap);
		}
		
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	private void convertSaleEntity2Map(Map<String,Object> resultMap){
		
		List<Map<String,Object>> outSalemainList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> outDetailList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> newFlagOutDetailList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> outResultList = new ArrayList<Map<String,Object>>();
		
		// 转换Entity为Map
		List<SaleMainEntity> outSalemain = (List<SaleMainEntity>)resultMap.get("outSalemain");
		for(SaleMainEntity smeEntity : outSalemain){
			Map<String,Object> smeMap = ConvertUtil.bean2Map(smeEntity);
			outSalemainList.add(smeMap);
		}
		resultMap.put("outSalemainList", outSalemainList);
		
		// 转换Entity为Map
		List<SaleDetailEntity> outDetail = (List<SaleDetailEntity>)resultMap.get("outDetail");
		for(SaleDetailEntity sdeEntity : outDetail){
			Map<String,Object> smdMap = ConvertUtil.bean2Map(sdeEntity);
			outDetailList.add(smdMap);
		}

		resultMap.put("outDetailList", outDetailList);
		
		// 转换Entity为Map
		List<SaleDetailEntity> newFlagOutDetail = (List<SaleDetailEntity>)resultMap.get("newFlagOutDetail");
		for(SaleDetailEntity newFlagSdeEntity : newFlagOutDetail){
			Map<String,Object> newSmdMap = ConvertUtil.bean2Map(newFlagSdeEntity);
			newFlagOutDetailList.add(newSmdMap);
		}
		resultMap.put("newFlagOutDetailList", newFlagOutDetailList);
		
		// 转换Entity为Map
		List<SaleRuleResultEntity> outResult= (List<SaleRuleResultEntity>)resultMap.get("outResult");
		for(SaleRuleResultEntity srrEntity : outResult){
			Map<String,Object> srrMap = ConvertUtil.bean2Map(srrEntity);
			outResultList.add(srrMap);
		}
		resultMap.put("outResultList", outResultList);
	}
	
	/**
	 * 单据明细转SaleDetailEntity
	 * @param saleDetailMap_list
	 * 		-- barCode 商品条码			
	 * 		-- unitCode 厂商编码			
	 * 		-- quantity 数量			
	 * 		-- price 价格			
	 * 		-- oriPrice 原价			
	 * 		-- proType 产品类型,产品是N，促销品是P			
	 * 		-- mainCode 主活动码			
	 * 		-- mainName 主活动名称			
	 * 		-- discount 单品折扣			
	 * 		-- productid 产品id			
	 * 		-- proName 产品名称			
	 * 		-- mainitem_tag 一个主活动内部的分组号			
	 * 		-- activityCode 活动code			
	 * 		-- newFlag 是否是促销引擎新增的记录，如果0不是新增的记录，如果是1是新增的记录			

	 * @return
	 */
	public List<SaleDetailEntity> saleDetail2Entity(List<Map<String, Object>> saleDetailMapList) {
		
		int lineNum = 1;
		
		List<SaleDetailEntity> inputSaledetail = new ArrayList<SaleDetailEntity>();
		
		for(Map<String,Object> saleDetailMap : saleDetailMapList){
			
			SaleDetailEntity sale_detail = new SaleDetailEntity();
			
			sale_detail.setBarcode(ConvertUtil.getString(saleDetailMap.get("barCode")));//商品条码
			sale_detail.setUnitcode(ConvertUtil.getString(saleDetailMap.get("unitCode")));//厂商编码
			sale_detail.setQuantity(ConvertUtil.getInt(saleDetailMap.get("quantity")));//数量
			
			double price=Double.parseDouble("".equals(ConvertUtil.getString(saleDetailMap.get("price")))?"0.00":ConvertUtil.getString(saleDetailMap.get("price")));
			sale_detail.setPrice(price);//价格pricePay
			
			double ori_price = Double.parseDouble("".equals(ConvertUtil.getString(saleDetailMap.get("oriPrice"))) ? "0.00" : ConvertUtil.getString(saleDetailMap.get("oriPrice")));
			sale_detail.setOri_price(ori_price);//原价
			
			sale_detail.setType(ConvertUtil.getString(saleDetailMap.get("proType")));// 产品类型  ,产品是N，促销品是P
			
			String mainCode = ConvertUtil.getString(saleDetailMap.get("mainCode"));  // 主活动码
			sale_detail.setMaincode(CherryBatchUtil.isBlankString(mainCode) ? "" : mainCode);// 主活动码
			
			String mainname = ConvertUtil.getString(saleDetailMap.get("mainName"));  // 主活动名称
			sale_detail.setMainname(CherryBatchUtil.isBlankString(mainname) ? "" : mainname);// 主活动名称
			
			String newFlag = ConvertUtil.getString(saleDetailMap.get("newFlag"));
			sale_detail.setNew_flag(CherryBatchUtil.isBlankString(newFlag) ? 0 : Integer.parseInt(newFlag));//是否是促销引擎新增的记录，如果0不是新增的记录，如果是1是新增的记录
			 
			String proName = ConvertUtil.getString(saleDetailMap.get("proName")); // 产品名称
			sale_detail.setProname(CherryBatchUtil.isBlankString(proName) ? "" : proName); // 产品名称
			
			String activityCode = ConvertUtil.getString(saleDetailMap.get("activityCode")); // 活动code
			sale_detail.setActivitycode(CherryBatchUtil.isBlankString(activityCode) ? "" : activityCode);  // 活动code
			
			sale_detail.setItemTag(lineNum );//业务的每个商品流水号discountRateArr
			
			sale_detail.setDiscount(Double.parseDouble("".equals(ConvertUtil.getString(saleDetailMap.get("discount"))) 
					? "100.0"
					: ConvertUtil.getString(saleDetailMap.get("discount"))));// 单品折扣，没有折扣的话就是100
			String productid = ConvertUtil.getString(saleDetailMap.get("productid")); // 产品厂商ID
			
//			sale_detail.setProductid(Integer.parseInt(productid));
			inputSaledetail.add(sale_detail);
			
//			ConvertUtil.convertDTO(dto1, dto2, flg);
			
			lineNum ++;
		}
		
		return inputSaledetail;
	}
	
	/**
	 * 用来打印智能促销查询规则方法返回所对应的日志
	 */
	private void SmartLog_MatchRule(int num){
		if(num == 0){
			logger.error("智能促销查询方法调用异常");
		}else if(num == 3){
			logger.error("智能促销查询方法数据库连接异常");
		}else if(num == 4){
			logger.error("智能促销查询方法更新最新的规则,稍后重试");
		}else if(num == 5){
			logger.error("智能促销查询方法socket通讯失败");
		}
	}
	
	/**
	 * 取得品牌对应的appID
	 * @param brandCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getAppID(String brandCode){
		
		Object key = null;
		String wsAppIdKeyObj = PropertiesUtil.pps.getProperty("WS_AppIdKey");
		
    	if(!CherryBatchUtil.isBlankString(wsAppIdKeyObj)){
    		try {
				Map<String,Object> wsAppIdKeyObjMap = CherryUtil.json2Map(wsAppIdKeyObj);
				
				key = wsAppIdKeyObjMap.get(brandCode);
						
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
		
		return key.toString();
	}
	
	/**
	 * 根据收货人联系方式（mobile or phone）及【电商订单线上线下会员合并规则】1321规则处理会员信息
     * @param
     * BIN_OrganizationInfoID : 组织ID
     * BIN_BrandInfoID ：品牌ID
     * CounterCode : 柜台Code
     * MobilePhone : 会员手机号
     * MemberName ：会员名字
     * BIN_EmployeeID : 员工ID
     * EmployeeCode : 员工代码
     * orderDate : 下单时间
     * telephone : 会员电话
     * MemberAddress : 会员地址
     * brandCode : 品牌代码
     * configValue : 配置项
	 * @return
	 * 如查询会员数据为空则进行以下根据传输的配置参数【conifgValue】返回不同的数据
	 * 		   configValue=0 不合并，新增会员数据，返回以下参数
	 * 			  resultMap.BIN_MemberInfoID
	 *            resultMap.MemberCode
	 *            resultMap.MemberName
	 *         configValue=1 按手机号合并 通过WebService新增数据，返回以下参数
	 *            resultMap.BIN_MemberInfoID
     *            resultMap.MemberCode
     *            resultMap.MemberName
     *         configValue=2 不合并且不存在也不新增,无返回参数
     * 如查询会员数据不为空，则统一返回以下参数
     * 		   resultMap.BIN_MemberInfoID
	 *	       resultMap.MemberCode
	 *	       resultMap.MemberName
	 *         resultMap.MemberLevel
	 * @throws Exception 
	 */
	public Map<String, Object> getMemInfo(Map<String, Object> paramMap) throws Exception {
		int organizationInfoID = CherryUtil.obj2int(paramMap.get("BIN_OrganizationInfoID"));
		int brandInfoID = CherryUtil.obj2int(paramMap.get("BIN_BrandInfoID"));
		String orderWay=ConvertUtil.getString(paramMap.get("orderWay"));
		String shopName=ConvertUtil.getString(paramMap.get("shopName"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BIN_OrganizationInfoID", organizationInfoID);
		param.put("BIN_BrandInfoID", brandInfoID);
		// 系统配置项1321:【电商订单线上线下会员合并规则】0：不合并（默认）1：按手机号合并 2:不合并且不存在也不新增
		 String configValue = binOLCM14_BL.getConfigValue("1321", String.valueOf(organizationInfoID),String.valueOf(brandInfoID));
		// 系统配置项1336:【电商订单会员查询条件】1：手机号（默认）2：会员昵称
		String memberConfigVaule = binOLCM14_BL.getConfigValue("1336", String.valueOf(organizationInfoID),String.valueOf(brandInfoID));
		if ("0".equals(configValue) || "".equals(configValue)) {
			//当电商线上线下会员合并规则配置项为：不合并时，判断电商会员查询条件配置项是否为：会员昵称
			if("2".equals(memberConfigVaule)){
				param.put("nickName", paramMap.get("nickName"));
			}else{
				// 匹配会员规则
				// 收货人手机号mobilePhone+收货人姓名ReceiverName+入会途径
				param.put("MemberName", paramMap.get("MemberName"));
				param.put("DataSource", getDataSourceByName(orderWay, shopName));
				param.put("MobilePhone", paramMap.get("MobilePhone"));
			}
		} else if ("1".equals(configValue)) {
			//当电商线上线下会员合并规则配置项为：按手机号合并时，判断电商会员查询条件配置项是否为：会员昵称
			if("2".equals(memberConfigVaule)){
				param.put("nickName", paramMap.get("nickName"));
			}else{
				// 按手机号合并会员信息【匹配会员规则：收货人手机号mobilePhone】
				param.put("MobilePhone", paramMap.get("MobilePhone"));
			}
		}else if ("2".equals(configValue)) {
			//当电商线上线下会员合并规则配置项为：不合并且不存在也不新增时，判断电商会员查询条件配置项是否为：会员昵称
			if("2".equals(memberConfigVaule)){
				param.put("nickName", paramMap.get("nickName"));
			}else{
				// 收货人手机号mobilePhone
				param.put("MobilePhone", paramMap.get("MobilePhone"));
			}
		}   
		//传参如果全部为空，则报错
		if(CherryBatchUtil.isBlankString(ConvertUtil.getString(param.get("MobilePhone"))) &&
			CherryBatchUtil.isBlankString(ConvertUtil.getString(param.get("MemberName"))) &&
			CherryBatchUtil.isBlankString(ConvertUtil.getString(param.get("DataSource"))) &&
			CherryBatchUtil.isBlankString(ConvertUtil.getString(param.get("nickName")))){
			logger.error("查询会员信息所需参数全为空，传参为："+param);
			return resultMap;
		}
		// 目前不支持已上线品牌对此规则的切换，故不会有历史遗留数据（手机号一样的两个会员，分别来自线上、线下）
		List<Map<String, Object>> memberList = binOLCM60_Service.getMemberInfo(param);
		if (!CherryBatchUtil.isBlankList(memberList)) {
			resultMap.put("BIN_MemberInfoID",memberList.get(0).get("BIN_MemberInfoID"));
			resultMap.put("MemberCode", memberList.get(0).get("MemberCode"));
			resultMap.put("MemberName", memberList.get(0).get("MemberName"));
			resultMap.put("MemberLevel", memberList.get(0).get("MemberLevel"));
		} else{
    		Map<String,Object> paramSeq = new HashMap<String,Object>();
            paramSeq.put("type", "I");
            paramSeq.put("organizationInfoId", organizationInfoID);
            paramSeq.put("brandInfoId", brandInfoID);
            paramSeq.put("length", "8");
            //会员卡号规则（1开头 + 8位递增数字）
            String memberPREFIX = "1";
            String memberCode = memberPREFIX + binOLCM15_BL.getSequenceId(paramSeq);
            
        	if("0".equals(configValue) || "".equals(configValue)) {
	            // 找不到需要新增会员，直接写入数据库
	            // 按手机号合并会员时以下两个字段未加入，在新增会员时就加入
	            param.put("MemberName", paramMap.get("MemberName"));
	        	param.put("DataSource", getDataSourceByName(orderWay,shopName));
	            param.put("MemberCode", memberCode);
	            param.put("BIN_EmployeeID", paramMap.get("BIN_EmployeeID"));
	            param.put("BAcode", paramMap.get("EmployeeCode"));
	            param.put("BIN_OrganizationID", paramMap.get("BIN_OrganizationID"));
	            param.put("CounterCode", paramMap.get("CounterCode"));
	            String orderDate = ConvertUtil.getString(paramMap.get("orderDate"));
	            SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
	            SimpleDateFormat sdf_YMD = new SimpleDateFormat("yyyyMMdd");
	            SimpleDateFormat sdf_HMS = new SimpleDateFormat("HH:mm:ss");
	            Date tradeDateTime = sdf.parse(orderDate);
	            param.put("JoinDate", sdf_YMD.format(tradeDateTime));
	            param.put("JoinTime", sdf_HMS.format(tradeDateTime));
	            param.put("Telephone", paramMap.get("telephone"));
	            param.put("MemberNickName", paramMap.get("nickName"));
	            String memberLevel="1";//新增的会员等级默认为“1”
	            param.put("memberLevel", memberLevel);
	            setInsertInfoMapKey(param);
	            int memberInfoID = binOLCM60_Service.addMemberInfo(param);
	            param.put("BIN_MemberInfoID", memberInfoID);
	            param.put("GrantDate", sdf_YMD.format(tradeDateTime));
	            param.put("GrantTime", sdf_HMS.format(tradeDateTime));
	            setInsertInfoMapKey(param);
	            binOLCM60_Service.addMemCardInfo(param);
	            // 添加地址信息
	            String address = ConvertUtil.getString(paramMap.get("MemberAddress"));
	            Map<String,Object> addressParam = new HashMap<String,Object>();
	            addressParam.put("address", address);
	            addressParam.put("cityId", null);
	            addressParam.put("provinceId", null);
	            addressParam.put("postcode", null);
	            setInsertInfoMapKey(addressParam);
	            int addressInfoId = binOLCM60_Service.addAddressInfo(addressParam);
	            addressParam.put("memberInfoId", memberInfoID);
	            addressParam.put("addressInfoId", addressInfoId);
	            // 添加会员地址
	            binOLCM60_Service.addMemberAddress(addressParam);
	            
	            // 最终要获取到会员ID
	            resultMap.put("BIN_MemberInfoID", memberInfoID);
	            resultMap.put("MemberCode", memberCode);
	            resultMap.put("MemberName", paramMap.get("MemberName"));
	            resultMap.put("MemberLevel", memberLevel);
        	} else if("1".equals(configValue)) {
        		// 调用新增会员的webService
    			Map<String, Object> memberMap = new HashMap<String, Object>();
    			memberMap.put("BrandCode", paramMap.get(CherryBatchConstants.BRAND_CODE));
    			memberMap.put("brandCode", paramMap.get(CherryBatchConstants.BRAND_CODE));
    			memberMap.put("TradeType", "MemberInfoMaintenance");
    			memberMap.put("SubType", "0");
        		// 无会员卡号时新增的会员卡号取手机号,手机号获取未加密的手机号
    			memberMap.put("MemberCode", paramMap.get("sourceMobilePhone"));
    			//当电商会员查询条件配置项为2时，代表手机号后8位已被*代替不能作为会员卡号新增
    			if("2".equals(memberConfigVaule)){
    				//使用新后台自动生成的卡号作为会员卡号
    				memberMap.put("MemberCode", memberCode);
    			}
    			memberMap.put("Name", paramMap.get("MemberName"));
//    			memberMap.put("Telephone", erpOrder.get("telephone"));
    			memberMap.put("MobilePhone", paramMap.get("sourceMobilePhone"));
    			String orderDate = ConvertUtil.getString(paramMap.get("orderDate"));
	            SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
	            SimpleDateFormat sdf_YMD = new SimpleDateFormat("yyyy-MM-dd");
	            SimpleDateFormat sdf_HMS = new SimpleDateFormat("HH:mm:ss");
	            Date tradeDateTime = sdf.parse(orderDate);
	            // 入会时间
	            memberMap.put("JoinDate", sdf_YMD.format(tradeDateTime));
	            memberMap.put("JoinTime", sdf_HMS.format(tradeDateTime));
	            memberMap.put("Address", ConvertUtil.getString(paramMap.get("MemberAddress")));
	            memberMap.put("EmployeeCode", ConvertUtil.getString(paramMap.get("EmployeeCode")));
	            memberMap.put("NickName", ConvertUtil.getString(paramMap.get("nickName")));
	            if(!"".equals(ConvertUtil.getString(paramMap.get("CounterCode")))) {
	            	// 开卡柜台号
	            	memberMap.put("CounterCode", paramMap.get("CounterCode"));
	            }
	            // 天猫订单
	            memberMap.put("DataSource1", getDataSourceByName(orderWay, shopName));
	            //入会途径
	            memberMap.put("JoinChannel", "Tmall");
	            String errorCode = "";
        		try {
        			Map<String, Object> resultWebServiceMap = WebserviceClient.accessCherryWebService(memberMap);
        			errorCode = ConvertUtil.getString(resultWebServiceMap.get("ERRORCODE"));
        			String errorMsg= ConvertUtil.getString(resultWebServiceMap.get("ERRORMSG"));
        			if(!"0".equalsIgnoreCase(errorCode)) {
        				// 只是记录一下，对于订单接收没有影响
        				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
                        batchLoggerDTO.setCode("EOT00097");//日志需要共通化
                        batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
                        batchLoggerDTO.addParam(errorMsg);
                        batchLoggerDTO.addParam(ConvertUtil.getString(memberMap.get("MemberCode")));
                        batchLoggerDTO.addParam(ConvertUtil.getString(paramMap.get("MemberName")));
                        batchLoggerDTO.addParam(memberMap.toString());//相关参数
                        CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
                        cherryBatchLogger.BatchLogger(batchLoggerDTO);
                        
        				resultMap.put("BIN_MemberInfoID", null);
                        resultMap.put("MemberCode", null);
                        resultMap.put("MemberName", paramMap.get("MemberName"));
                        resultMap.put("MemberLevel", null);
                        return resultMap;
        			}
        		}catch(ConnectException ce){
        			//通常为请求地址连接时异常
                    CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
        			cherryBatchLogger.outExceptionLog(ce);
                    
    				resultMap.put("BIN_MemberInfoID", null);
                    resultMap.put("MemberCode", null);
                    resultMap.put("MemberLevel", null);
                    resultMap.put("MemberName", paramMap.get("MemberName"));
                    return resultMap;
        		}catch(Exception e) {
        			// 只是记录一下，对于订单接收没有影响
                    CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
        			cherryBatchLogger.outExceptionLog(e);
                    
    				resultMap.put("BIN_MemberInfoID", null);
                    resultMap.put("MemberCode", null);
                    resultMap.put("MemberLevel", null);
                    resultMap.put("MemberName", paramMap.get("MemberName"));
                    return resultMap;
        		}
        		
    			// 重新用手机号（会员昵称）去查询会员信息（此时为当前新增的会员）
    			List<Map<String,Object>> memberResultList = binOLCM60_Service.getMemberInfo(param);
    			if(null != memberResultList && memberResultList.size()>0){
    				// 调用webService成功后再次查询得到会员ID
    				resultMap.put("BIN_MemberInfoID", memberResultList.get(0).get("BIN_MemberInfoID"));
    				resultMap.put("MemberCode", memberResultList.get(0).get("MemberCode"));
    				resultMap.put("MemberName", memberResultList.get(0).get("MemberName"));
    				resultMap.put("MemberLevel", memberResultList.get(0).get("MemberLevel"));
    			} else{
    				if("0".equalsIgnoreCase(errorCode)) {
    					// webservice已经新增成功，但是
    					logger.error("调用WebService返回【ErrorMsg】信息为0（成功）但是在新后台没有查询到WebService新增的会员数据(NickName:"+ memberMap.get("NickName")+")。");
    				} 
					logger.error("调用WebService后新增的会员，在新后台查询不到(NickName:"+ memberMap.get("NickName")+")。");
    			}
        	} else if ("2".equals(configValue)){
        		
				resultMap.put("BIN_MemberInfoID", null);
                resultMap.put("MemberCode", null);
                resultMap.put("MemberLevel", null);
                resultMap.put("MemberName", paramMap.get("MemberName"));
        	}
        }
		return resultMap;
	}
	
	/**
	 * 共通
	 * @param map
	 */
    private void setInsertInfoMapKey(Map<String,Object> map){
        map.put("CreatedBy","BINOLCM60_BL");
        map.put("CreatePGM","BINOLCM60_BL");
        map.put("UpdatedBy","BINOLCM60_BL");
        map.put("UpdatePGM","BINOLCM60_BL");
        map.put("createdBy","BINOLCM60_BL");
        map.put("createPGM","BINOLCM60_BL");
        map.put("updatedBy","BINOLCM60_BL");
        map.put("updatePGM","BINOLCM60_BL");
    }

    
	/**
	 * 把中文的来源转成定义的英文名
	 * 对于天猫订单，根据店铺名取不同来源
	 * @param name
	 * @param shopName
	 * @return
	 */
    public String getDataSourceByName(String name,String shopName){
        String dataSource = name;
        if(name.equals("官网订单")){
            dataSource = "OfficialWebsite";
        }else if(name.equals("淘宝订单") || name.equals("天猫订单")){
            //薇诺娜贝泰妮专卖店
            dataSource = "Tmall";
            if(shopName.equals("薇诺娜化妆品旗舰店")){
                dataSource = "TmallW";
              }
        }else if(name.equals("京东订单")){
            dataSource = "JD";
        }else if(name.equals("一号店订单")){
            dataSource = "YHD";
        }else if(name.equals("亚马逊订单")){
            dataSource = "Amazon";
        }else if(name.equals("苏宁订单")){
            dataSource = "SN";
        }else if(name.equals("拍拍订单")){
            dataSource = "PP";
        }
        return dataSource;
    }
    
    /**
     * 取得产品BOM的信息
     * 包括促销品
     * @param map
     * @return
     */
    public List<Map<String, Object>> getBomPrtList(Map<String, Object> map) {
    	return binOLCM60_Service.getBomPrtList(map);
    }
    
    /**
     * 取得产品+促销品信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getProPrmList(Map<String, Object> map) {
    	return binOLCM60_Service.getProPrmList(map);
    }
}
