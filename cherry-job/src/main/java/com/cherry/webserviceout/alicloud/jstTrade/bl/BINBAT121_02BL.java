/*	
 * @(#)BINBAT121_BL.java     1.0 @2015-9-16
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
package com.cherry.webserviceout.alicloud.jstTrade.bl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryBatchSecret;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.client.WebserviceClient;
import com.cherry.webserviceout.alicloud.jstTrade.service.BINBAT121_Service;


/**
 * 
 * 协作类，编写业务共通方法块
 
 */
public class BINBAT121_02BL {
	
	private static CherryBatchLogger logger = new CherryBatchLogger(BINBAT121_02BL.class);	
	
	@Resource
	private static  BINBAT121_Service binbat121_Service;
	
    /** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private static  BINOLCM14_BL binOLCM14_BL;
	
    @Resource(name="binOLCM15_BL")
    private static BINOLCM15_BL binOLCM15_BL;
	
	/**
	 * 取得品牌对应的appID
	 * @param brandCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getAppID(String brandCode){
		
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
     * BIN_OrganizationInfoID :组织ID
     * BIN_BrandInfoID： 品牌ID
     * CounterName:柜台名称
     * MemberMobile：会员手机号
     * DataSource: 来源方式
     * MemberName：会员名字
     * BIN_EmployeeID :员工ID
     * EmployeeCode ：员工代码
     * orderDate ： 下单时间
     * telephone ：会员电话
     * MemberAddress：会员地址
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, Object> getMemInfo(Map<String, Object> paramMap) throws Exception {
		int organizationInfoID = CherryUtil.obj2int(paramMap.get("BIN_OrganizationInfoID"));
		int brandInfoID = CherryUtil.obj2int(paramMap.get("BIN_BrandInfoID"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BIN_OrganizationInfoID", organizationInfoID);
		param.put("BIN_BrandInfoID", brandInfoID);
		// 根据店铺名称查询组织结构ID
		param.put("DepartName", paramMap.get("CounterName"));
		List<Map<String, Object>> departList = binbat121_Service.getDepartInfo(param);
		
		String organizationID = "";
		// 柜台Code
		String counterCode = "";
		if (!CherryBatchUtil.isBlankList(departList)) {
			organizationID = ConvertUtil.getString(departList.get(0).get("BIN_OrganizationID"));
			counterCode = ConvertUtil.getString(departList.get(0).get("DepartCode"));
			resultMap.put("BIN_OrganizationID", organizationID);
			resultMap.put("CounterCode", counterCode);
		}

		String mobilePhone = ConvertUtil.getString(paramMap.get("MemberMobile"));
		String brandCode = ConvertUtil.getString(paramMap.get("BrandCode"));
		mobilePhone = CherryBatchSecret.encryptData(brandCode,mobilePhone);
		param.put("MobilePhone", mobilePhone);
		 String shopName =ConvertUtil.getString(paramMap.get("CounterName"));
		 String orderWay = ConvertUtil.getString(paramMap.get("DataSource"));
		// 系统配置项1321:【电商订单线上线下会员合并规则】0：不合并（默认）1：按手机号合并
		String configValue = binOLCM14_BL.getConfigValue("1321", String.valueOf(organizationInfoID),String.valueOf(brandInfoID));
		if ("0".equals(configValue) || "".equals(configValue)) {
			// 匹配会员规则
			// 收货人手机号ReceiverMobile+收货人姓名ReceiverName+入会途径
			param.put("MemberName", paramMap.get("MemberName"));
			param.put("DataSource", getDataSourceByName(orderWay,shopName));
		} else if ("1".equals(configValue)) {
			// 按手机号合并会员信息【匹配会员规则：收货人手机号mobilePhone】
			
		} else if ("2".equals(configValue)) {
			// 会员名称 接收webservice返回数据字段ReceiverName 2：不合并且不存在也不新增  配置值是2或者空时则取得收货人名称
			param.put("MemberName", paramMap.get("MemberName"));
			param.put("DataSource", getDataSourceByName(orderWay,shopName));
		}
		// 目前不支持已上线品牌对此规则的切换，故不会有历史遗留数据（手机号一样的两个会员，分别来自线上、线下）
		List<Map<String, Object>> memberList = binbat121_Service.getMemberInfo(param);
		if (!CherryBatchUtil.isBlankList(memberList)) {
			resultMap.put("BIN_MemberInfoID",memberList.get(0).get("BIN_MemberInfoID"));
			resultMap.put("MemberCode", memberList.get(0).get("MemberCode"));
			resultMap.put("MemberName", memberList.get(0).get("MemberName"));
			resultMap.put("MemberLevel", memberList.get(0).get("MemberLevel"));
		} else{
        	if("0".equals(configValue) || "".equals(configValue)) {
        		Map<String,Object> paramSeq = new HashMap<String,Object>();
	            paramSeq.put("type", "I");
	            paramSeq.put("organizationInfoId", organizationInfoID);
	            paramSeq.put("brandInfoId", brandInfoID);
	            paramSeq.put("length", "8");
	            //会员卡号规则（1开头 + 8位递增数字）
	            String memberPREFIX = "1";
	            String memberCode = memberPREFIX + binOLCM15_BL.getSequenceId(paramSeq);
	            // 找不到需要新增会员，直接写入数据库
	            // 按手机号合并会员时以下两个字段未加入，在新增会员时就加入
	            param.put("MemberName", paramMap.get("MemberName"));
	        	param.put("DataSource", getDataSourceByName(orderWay,shopName));
	            param.put("MemberCode", memberCode);
	            param.put("BIN_EmployeeID", paramMap.get("BIN_EmployeeID"));
	            param.put("BAcode", paramMap.get("EmployeeCode"));
	            param.put("BIN_OrganizationID", organizationID);
	            param.put("CounterCode", counterCode);
	            String orderDate = ConvertUtil.getString(paramMap.get("orderDate"));
	            SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
	            SimpleDateFormat sdf_YMD = new SimpleDateFormat("yyyyMMdd");
	            SimpleDateFormat sdf_HMS = new SimpleDateFormat("HH:mm:ss");
	            Date tradeDateTime = sdf.parse(orderDate);
	            param.put("JoinDate", sdf_YMD.format(tradeDateTime));
	            param.put("JoinTime", sdf_HMS.format(tradeDateTime));
	            param.put("Telephone", paramMap.get("telephone"));
	            param.put("MemberNickName", paramMap.get("MemberName"));
	            setInsertInfoMapKey(param);
	            int memberInfoID = binbat121_Service.addMemberInfo(param);
	            param.put("BIN_MemberInfoID", memberInfoID);
	            param.put("GrantDate", sdf_YMD.format(tradeDateTime));
	            param.put("GrantTime", sdf_HMS.format(tradeDateTime));
	            setInsertInfoMapKey(param);
	            binbat121_Service.addMemCardInfo(param);
	            // 添加地址信息
	            String address = ConvertUtil.getString(paramMap.get("MemberAddress"));
	            Map<String,Object> addressParam = new HashMap<String,Object>();
	            addressParam.put("address", address);
	            addressParam.put("cityId", null);
	            addressParam.put("provinceId", null);
	            addressParam.put("postcode", null);
	            setInsertInfoMapKey(addressParam);
	            int addressInfoId = binbat121_Service.addAddressInfo(addressParam);
	            addressParam.put("memberInfoId", memberInfoID);
	            addressParam.put("addressInfoId", addressInfoId);
	            // 添加会员地址
	            binbat121_Service.addMemberAddress(addressParam);
	            
	            // 最终要获取到会员ID
	            resultMap.put("BIN_MemberInfoID", memberInfoID);
	            resultMap.put("MemberCode", memberCode);
	            resultMap.put("MemberName", paramMap.get("MemberName"));
        	} else if("1".equals(configValue)) {
        		// 无会员卡号时新增的会员卡号取手机号
        		String memberCode = ConvertUtil.getString(paramMap.get("MemberMobile"));
        		// 调用新增会员的webService
    			Map<String, Object> memberMap = new HashMap<String, Object>();
    			memberMap.put("BrandCode", brandCode);
    			memberMap.put("brandCode", brandCode);
    			memberMap.put("TradeType", "MemberInfoMaintenance");
    			memberMap.put("SubType", "0");
    			memberMap.put("MemberCode", memberCode);
    			memberMap.put("Name", paramMap.get("MemberName"));
//    			memberMap.put("Telephone", erpOrder.get("telephone"));
    			memberMap.put("MobilePhone", paramMap.get("MemberMobile"));
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
	            if(!"".equals(counterCode)) {
	            	// 开卡柜台号
	            	memberMap.put("CounterCode", counterCode);
	            }
	            // 天猫订单
	            memberMap.put("DataSource1", "Tmall");
        		try {
        			Map<String, Object> resultWebServiceMap = WebserviceClient.accessCherryWebService(memberMap);
        			String errorCode = ConvertUtil.getString(resultWebServiceMap.get("ERRORCODE"));
        			if(!"0".equalsIgnoreCase(errorCode)) {
        				// 只是记录一下，对于订单接收没有影响
        				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
                        batchLoggerDTO.setCode("EOT00095");
                        batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
                        batchLoggerDTO.addParam(memberCode);
                        batchLoggerDTO.addParam(ConvertUtil.getString(paramMap.get("MemberName")));
                        CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(BINBAT121_02BL.class);
                        cherryBatchLogger.BatchLogger(batchLoggerDTO);
                        
        				resultMap.put("BIN_MemberInfoID", null);
                        resultMap.put("MemberCode", null);
                        resultMap.put("MemberName", paramMap.get("MemberName"));
                        return resultMap;
        			}
        		} catch(Exception e) {
        			// 只是记录一下，对于订单接收没有影响
    				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
                    batchLoggerDTO.setCode("EOT00095");
                    batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
                    batchLoggerDTO.addParam(memberCode);
                    batchLoggerDTO.addParam(ConvertUtil.getString(paramMap.get("MemberName")));
                    CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(BINBAT121_02BL.class);
                    cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
                    
    				resultMap.put("BIN_MemberInfoID", null);
                    resultMap.put("MemberCode", null);
                    resultMap.put("MemberName", paramMap.get("MemberName"));
                    return resultMap;
        		}
        		// 重新用手机号去查询会员信息（此时为当前新增的会员）
        		List<Map<String,Object>> memberResultList = binbat121_Service.getMemberInfo(param);
        		if(null != memberResultList && memberResultList.size()>0){
                	// 调用webService成功后再次查询得到会员ID
                    resultMap.put("BIN_MemberInfoID", memberResultList.get(0).get("BIN_MemberInfoID"));
                    resultMap.put("MemberCode", memberResultList.get(0).get("MemberCode"));
                    resultMap.put("MemberName", memberResultList.get(0).get("MemberName"));
                }
        	}
        }
		return resultMap;
	}
	
	/**
	 * 根据订单中的产品信息从智能促销dll程序中取得相应的促销品
	 * 孙彩琳写的dll,云POS有调用，可以参考。
	 * @param paramMap
	 * @return
	 */
	public static Map<String,Object> getAct(Map<String,Object> paramMap){
		return null;
	}
	/**
	 * 共通
	 * @param map
	 */
    private static void setInsertInfoMapKey(Map<String,Object> map){
        map.put("CreatedBy","BINOTHONG01_BL");
        map.put("CreatePGM","BINOTHONG01_BL");
        map.put("UpdatedBy","BINOTHONG01_BL");
        map.put("UpdatePGM","BINOTHONG01_BL");
        map.put("createdBy","BINOTHONG01_BL");
        map.put("createPGM","BINOTHONG01_BL");
        map.put("updatedBy", "BINOTHONG01_BL");
        map.put("updatePGM", "BINOTHONG01_BL");
    }
/**
 * 把中文的来源转成定义的英文名
 * 对于天猫订单，根据店铺名取不同来源
 * @param name
 * @param shopName
 * @return
 */
    private static String getDataSourceByName(String name,String shopName){
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

}
