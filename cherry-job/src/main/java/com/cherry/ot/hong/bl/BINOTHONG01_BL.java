/*
 * @(#)BINOTHONG01_BL.java     1.0 2014/09/04
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
package com.cherry.ot.hong.bl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.axis.encoding.Base64;

import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM18_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM60_BL;
import com.cherry.cm.core.BatchExceptionDTO;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.cm.core.CherryBatchSecret;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryMD5Coder;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.bl.BINBEMQMES97_BL;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.ot.hong.interfaces.BINOTHONG01_IF;
import com.cherry.ot.hong.service.BINOTHONG01_Service;
import com.cherry.webservice.client.WebserviceClient;
import com.cherry.webservice.sale.bl.SaleInfoLogic;
import com.cherry.webservice.sale.service.SaleInfoService;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 
 * 宏巍电商订单获取 BL
 * 
 * 
 * @author niushunjie
 * @version 1.0 2014.09.04
 */
public class BINOTHONG01_BL implements BINOTHONG01_IF{

    private static CherryBatchLogger logger = new CherryBatchLogger(BINOTHONG01_BL.class);
    
    @Resource(name="saleInfoLogic")
    private SaleInfoLogic saleInfoLogic;
    
    @Resource(name="saleInfoService")
    private SaleInfoService saleInfoService;
    
    @Resource(name="binOLCM03_BL")
    private BINOLCM03_BL binOLCM03_BL;
    
    @Resource(name="binOLCM15_BL")
    private BINOLCM15_BL binOLCM15_BL;
    
    @Resource(name="binOLCM18_BL")
    private BINOLCM18_BL binOLCM18_BL;
    
    @Resource(name="binBEMQMES97_BL")
    private BINBEMQMES97_BL binBEMQMES97_BL;
    
    @Resource(name="binOTHONG01_Service")
    private BINOTHONG01_Service binOTHONG01_Service;
    
    /** 系统配置项 共通BL */
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 电商接口相关共通  */
	@Resource(name="binOLCM60_BL")
	private BINOLCM60_BL binOLCM60_BL;
	
    /** BATCH处理标志 */
    private int flag = CherryBatchConstants.BATCH_SUCCESS;
    
    /** 促销品对应的逻辑仓库CODE */
    public static final String PRMOTION_DEFAULTINVENTORYTYPECODE = "CX01";
    
    /** 处理总条数 */
    private int totalCount = 0;
    
    /** 失败条数 */
    private int failCount = 0;
    
	/** JOB执行相关共通 IF */
	@Resource(name="binbecm01_IF")
	private BINBECM01_IF binbecm01_IF;

    /**
     * 查询该订单收货人的会员信息【此方法为得到会员信息的新增方法】
     * @param orderMap
     * @return Map:该订单的会员信息【收货人不是会员时生成会员信息，注：生成会员有两种方式，通过系统配置项1321来控制】
     * @throws Exception
     */
	private Map<String, Object> getMemberInfoByParam(Map<String, Object> orderMap) throws Exception {
		Map<String, Object> memberParamMap=new HashMap<String, Object>();
		
		String mobilePhone = ConvertUtil.getString(orderMap.get("mobilePhone"));
		String brandCode = ConvertUtil.getString(orderMap.get("BrandCode"));
		mobilePhone = CherryBatchSecret.encryptData(brandCode, mobilePhone);
		
		memberParamMap.put("BIN_OrganizationInfoID", orderMap.get("BIN_OrganizationInfoID"));
		memberParamMap.put("BIN_BrandInfoID", orderMap.get("BIN_BrandInfoID"));
		memberParamMap.put("MobilePhone", mobilePhone);
		memberParamMap.put("BIN_EmployeeID", orderMap.get("BIN_EmployeeID"));
		memberParamMap.put("EmployeeCode", orderMap.get("EmployeeCode"));
		memberParamMap.put("orderDate", orderMap.get("orderDate"));
		memberParamMap.put("telephone", orderMap.get("telephone"));
		memberParamMap.put("MemberAddress", orderMap.get("address"));
		memberParamMap.put("brandCode", brandCode);
		memberParamMap.put("orderWay", orderMap.get("orderWay"));
		memberParamMap.put("shopName", orderMap.get("shopName"));
		memberParamMap.put("BIN_OrganizationID", orderMap.get("BIN_OrganizationID"));
		memberParamMap.put("CounterCode", orderMap.get("CounterCode"));
		memberParamMap.put("MemberName", orderMap.get("consignee"));
		memberParamMap.put("nickName", orderMap.get("memberName"));
		//源手机号，未加密的
		memberParamMap.put("sourceMobilePhone", orderMap.get("mobilePhone"));
		//调用查询会员信息共通方法，得到会员信息
		Map<String, Object> resultMap = binOLCM60_BL.getMemInfo(memberParamMap);
		return resultMap;
	}
    /**
     * 查询该订单收货人的会员信息
     * @param paramMap
     * @return Map:该订单的会员信息【收货人不是会员时生成会员信息，注：生成会员有两种方式，通过系统配置项1321来控制】
     * @throws Exception 
     */
    private Map<String,Object> getESOrderMainNeedValue(Map<String,Object> erpOrder) throws Exception{
        int organizationInfoID = CherryUtil.obj2int(erpOrder.get("BIN_OrganizationInfoID"));
        int brandInfoID = CherryUtil.obj2int(erpOrder.get("BIN_BrandInfoID"));
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", organizationInfoID);
        param.put("BIN_BrandInfoID", brandInfoID);
        //查询组织结构ID
        param.put("DepartName", erpOrder.get("shopName"));
        List<Map<String,Object>> departList = binOTHONG01_Service.getDepartInfo(param);
        String organizationID = "";
        String counterCode = "";
        if(null != departList && departList.size()>0){
            organizationID = ConvertUtil.getString(departList.get(0).get("BIN_OrganizationID"));
            counterCode = ConvertUtil.getString(departList.get(0).get("DepartCode"));
            resultMap.put("BIN_OrganizationID", organizationID);
            resultMap.put("CounterCode", counterCode);
        }
        
        String mobilePhone = ConvertUtil.getString(erpOrder.get("mobilePhone"));
        String brandCode = ConvertUtil.getString(erpOrder.get("BrandCode"));
        mobilePhone =  CherryBatchSecret.encryptData(brandCode,mobilePhone);
        param.put("MobilePhone", mobilePhone);
        String orderWay = ConvertUtil.getString(erpOrder.get("orderWay"));
        String shopName = ConvertUtil.getString(erpOrder.get("shopName"));
        // 系统配置项1321:【电商订单线上线下会员合并规则】0：不合并（默认）1：按手机号合并 
        String configValue = binOLCM14_BL.getConfigValue("1321", String.valueOf(organizationInfoID), String.valueOf(brandInfoID));
        if("0".equals(configValue) || "".equals(configValue)) {
        	//匹配会员规则
            //收货人手机号mobilePhone+收货人姓名consignee+入会途径
        	param.put("MemberName", erpOrder.get("consignee"));
        	param.put("DataSource", getDataSourceByName(orderWay,shopName));
        } else if("1".equals(configValue)) {
        	// 按手机号合并会员信息【匹配会员规则：收货人手机号mobilePhone】
        }
        // 目前不支持已上线品牌对此规则的切换，故不会有历史遗留数据（手机号一样的两个会员，分别来自线上、线下）
        List<Map<String,Object>> memberList = binOTHONG01_Service.getMemberInfo(param);
        if(null != memberList && memberList.size()>0){
        	// 存在指定的会员信息（0：只按手机号mobilePhone 1：按手机号mobilePhone+收货人姓名consignee+入会途径）
            resultMap.put("BIN_MemberInfoID", memberList.get(0).get("BIN_MemberInfoID"));
            resultMap.put("MemberCode", memberList.get(0).get("MemberCode"));
            resultMap.put("MemberName", memberList.get(0).get("MemberName"));
            resultMap.put("MemberLevel", memberList.get(0).get("MemberLevel"));
        }else{
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
	            param.put("MemberName", erpOrder.get("consignee"));
	        	param.put("DataSource", getDataSourceByName(orderWay,shopName));
	            param.put("MemberCode", memberCode);
	            param.put("BIN_EmployeeID", erpOrder.get("BIN_EmployeeID"));
	            param.put("BAcode", erpOrder.get("EmployeeCode"));
	            param.put("BIN_OrganizationID", organizationID);
	            param.put("CounterCode", counterCode);
	            String orderDate = ConvertUtil.getString(erpOrder.get("orderDate"));
	            SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
	            SimpleDateFormat sdf_YMD = new SimpleDateFormat("yyyyMMdd");
	            SimpleDateFormat sdf_HMS = new SimpleDateFormat("HH:mm:ss");
	            Date tradeDateTime = sdf.parse(orderDate);
	            param.put("JoinDate", sdf_YMD.format(tradeDateTime));
	            param.put("JoinTime", sdf_HMS.format(tradeDateTime));
	            param.put("Telephone", erpOrder.get("telephone"));
	            param.put("MemberNickName", erpOrder.get("memberName"));
	            setInsertInfoMapKey(param);
	            int memberInfoID = binOTHONG01_Service.addMemberInfo(param);
	            param.put("BIN_MemberInfoID", memberInfoID);
	            param.put("GrantDate", sdf_YMD.format(tradeDateTime));
	            param.put("GrantTime", sdf_HMS.format(tradeDateTime));
	            setInsertInfoMapKey(param);
	            binOTHONG01_Service.addMemCardInfo(param);
	            // 添加地址信息
	            String address = ConvertUtil.getString(erpOrder.get("address"));
	            Map<String,Object> addressParam = new HashMap<String,Object>();
	            addressParam.put("address", address);
	            addressParam.put("cityId", null);
	            addressParam.put("provinceId", null);
	            addressParam.put("postcode", null);
	            setInsertInfoMapKey(addressParam);
	            int addressInfoId = binOTHONG01_Service.addAddressInfo(addressParam);
	            addressParam.put("memberInfoId", memberInfoID);
	            addressParam.put("addressInfoId", addressInfoId);
	            // 添加会员地址
	            binOTHONG01_Service.addMemberAddress(addressParam);
	            
	            // 最终要获取到会员ID
	            resultMap.put("BIN_MemberInfoID", memberInfoID);
	            resultMap.put("MemberCode", memberCode);
	            resultMap.put("MemberName", erpOrder.get("consignee"));
        	} else if("1".equals(configValue)) {
        		// 无会员卡号时新增的会员卡号取手机号
        		String memberCode = ConvertUtil.getString(erpOrder.get("mobilePhone"));
        		// 调用新增会员的webService
    			Map<String, Object> memberMap = new HashMap<String, Object>();
    			memberMap.put("BrandCode", brandCode);
    			memberMap.put("brandCode", brandCode);
    			memberMap.put("TradeType", "MemberInfoMaintenance");
    			memberMap.put("SubType", "0");
    			memberMap.put("MemberCode", memberCode);
    			memberMap.put("Name", erpOrder.get("consignee"));
//    			memberMap.put("Telephone", erpOrder.get("telephone"));
    			memberMap.put("MobilePhone", erpOrder.get("mobilePhone"));
    			String orderDate = ConvertUtil.getString(erpOrder.get("orderDate"));
	            SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
	            SimpleDateFormat sdf_YMD = new SimpleDateFormat("yyyy-MM-dd");
	            SimpleDateFormat sdf_HMS = new SimpleDateFormat("HH:mm:ss");
	            Date tradeDateTime = sdf.parse(orderDate);
	            // 入会时间
	            memberMap.put("JoinDate", sdf_YMD.format(tradeDateTime));
	            memberMap.put("JoinTime", sdf_HMS.format(tradeDateTime));
	            memberMap.put("Address", ConvertUtil.getString(erpOrder.get("address")));
	            memberMap.put("EmployeeCode", ConvertUtil.getString(erpOrder.get("EmployeeCode")));
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
                        batchLoggerDTO.setCode("EOT00079");
                        batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
                        batchLoggerDTO.addParam(memberCode);
                        batchLoggerDTO.addParam(ConvertUtil.getString(erpOrder.get("consignee")));
                        CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
                        cherryBatchLogger.BatchLogger(batchLoggerDTO);
                        
        				resultMap.put("BIN_MemberInfoID", null);
                        resultMap.put("MemberCode", null);
                        resultMap.put("MemberName", erpOrder.get("consignee"));
                        return resultMap;
        			}
        		} catch(Exception e) {
        			// 只是记录一下，对于订单接收没有影响
    				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
                    batchLoggerDTO.setCode("EOT00079");
                    batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
                    batchLoggerDTO.addParam(memberCode);
                    batchLoggerDTO.addParam(ConvertUtil.getString(erpOrder.get("consignee")));
                    CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
                    cherryBatchLogger.BatchLogger(batchLoggerDTO,e);
                    
    				resultMap.put("BIN_MemberInfoID", null);
                    resultMap.put("MemberCode", null);
                    resultMap.put("MemberName", erpOrder.get("consignee"));
                    return resultMap;
        		}
        		// 重新用手机号去查询会员信息（此时为当前新增的会员）
        		List<Map<String,Object>> memberResultList = binOTHONG01_Service.getMemberInfo(param);
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
     * 设置插入/更新Sale.BIN_ESOrderMain值
     * @param erpOrder
     * @param dataType
     * @return
     */
    private Map<String,Object> setESOrderMain(Map<String,Object> erpOrder,String dataType){
        Map<String,Object> esOrderMain = new HashMap<String,Object>();
        if(dataType.equals("update")){
            esOrderMain.put("BIN_ESOrderMainID",erpOrder.get("BIN_ESOrderMainID"));
        }else if(dataType.equals("insert")){
            //新后台中的单据号
            String orderNo = binOLCM03_BL.getTicketNumber(ConvertUtil.getString(erpOrder.get("BIN_OrganizationInfoID")), ConvertUtil.getString(erpOrder.get("BIN_BrandInfoID")), "BATCH", "DS");
            esOrderMain.put("OrderNo",orderNo);
        }
        //所属组织ID
        esOrderMain.put("BIN_OrganizationInfoID",erpOrder.get("BIN_OrganizationInfoID"));
        //所属品牌ID
        esOrderMain.put("BIN_BrandInfoID",erpOrder.get("BIN_BrandInfoID"));
        //组织结构ID
        esOrderMain.put("BIN_OrganizationID",erpOrder.get("BIN_OrganizationID"));
        //员工ID 对于电商平台，也会虚拟一个BA
        esOrderMain.put("BIN_EmployeeID",erpOrder.get("BIN_EmployeeID"));
        //员工代码code
        esOrderMain.put("EmployeeCode",erpOrder.get("EmployeeCode"));
        //下单组织结构ID
        esOrderMain.put("BIN_OrganizationIDDX", erpOrder.get("BIN_OrganizationID"));
        //下单员工ID
        esOrderMain.put("BIN_EmployeeIDDX", erpOrder.get("BIN_EmployeeID"));
        //下单员工代码code
        esOrderMain.put("EmployeeCodeDX", erpOrder.get("EmployeeCode"));
        //数据来源
        String orderWay = ConvertUtil.getString(erpOrder.get("orderWay"));
        String shopName = ConvertUtil.getString(erpOrder.get("shopName"));
        esOrderMain.put("DataSource",getDataSourceByName(orderWay,shopName));
        //客户数据来源
        esOrderMain.put("DataSourceCustomer",null);
        //店铺名称
        esOrderMain.put("ShopName",erpOrder.get("shopName"));
        //订单编号，来自于电商（有可能被ERP处理过，不再是电商平台上展示给消费者的原始单据号），全局唯一
        esOrderMain.put("BillCode",erpOrder.get("orderNumber"));
        //业务关联单号，保留
        esOrderMain.put("RelevanceBillCode",null);
        //电商平台原始的单据号
        esOrderMain.put("OriginalBillCode",erpOrder.get("oldOrderNumber"));
        //SALE :标准订单 IN_BUY：内买订单 SUPPLEMENT：补差订单 EXCHANGE：换货订单
        //DEALERS：代销订单 JUS：聚划算 LBP：LBP GROUP：团购订单 REISSUE：补发订单 WHOLESALE：批发订单 UNDEFIND：未定义订单
        //交易类型（销售：NS，退货：SR,积分兑换:PX）。PX在旧的版本中也是作为NS处理的，为了能够更好的进行区分，在新版本中采用独立的类型PX，处理逻辑与NS相同。
        esOrderMain.put("SaleType",CherryConstants.BUSINESS_TYPE_NS);
        //单据类型
        esOrderMain.put("TicketType",erpOrder.get("Ticket_type"));
        //单据状态
        esOrderMain.put("BillState",erpOrder.get("BillState"));
        // 单据类型
        esOrderMain.put("BillType", erpOrder.get("BillType"));
        //消费者类型 NP：普通个人  NG：普通团购  MP：会员个人  MG：会员团购。
        if(ConvertUtil.getString(erpOrder.get("BIN_MemberInfoID")).equals("")){
            esOrderMain.put("ConsumerType","NP");
        }else{
            esOrderMain.put("ConsumerType","MP");
        }
        //新后台查到的会员ID
        esOrderMain.put("BIN_MemberInfoID",erpOrder.get("BIN_MemberInfoID"));
        //新后台查到的会员卡号
        esOrderMain.put("MemberCode",erpOrder.get("MemberCode"));
        //新后台查到的会员姓名
        esOrderMain.put("MemberName",erpOrder.get("MemberName"));
        //会员昵称
        esOrderMain.put("MemberNickname",erpOrder.get("memberName"));
        //买家姓名
        esOrderMain.put("BuyerName",erpOrder.get("MemberName"));
        //买家手机号
        esOrderMain.put("BuyerMobilePhone",erpOrder.get("mobilePhone"));
        //买家的其它标识
        esOrderMain.put("BuyerIdentifier",null);
        //收货人姓名
        esOrderMain.put("ConsigneeName",erpOrder.get("consignee"));
        //收货人手机
        esOrderMain.put("ConsigneeMobilePhone",erpOrder.get("mobilePhone"));
        //收货人地址
        String address = ConvertUtil.getString(erpOrder.get("address"));
        esOrderMain.put("ConsigneeAddress",address);
        String orderNumber = ConvertUtil.getString(erpOrder.get("orderNumber"));
      //收件人省市名称
        Map<String,Object> provAndCityMap= getProvNameAndCityName(address,orderNumber);
        esOrderMain.putAll(provAndCityMap);
        //买家留言
        esOrderMain.put("BuyerMessage",erpOrder.get("buyerMessage"));
        //卖家备注
        esOrderMain.put("SellerMemo",erpOrder.get("sellerMemo"));
        //单据创建时间
        esOrderMain.put("BillCreateTime",erpOrder.get("orderDate"));
        //单据付款时间
        String payTime = ConvertUtil.getString(erpOrder.get("payTime"));
        esOrderMain.put("BillPayTime",payTime);
        //单据关闭时间
        esOrderMain.put("BillCloseTime",null);
        String billState = ConvertUtil.getString(erpOrder.get("BillState"));
        if(billState.equals("4") || billState.equals("0")){
            esOrderMain.put("BillCloseTime",CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
        }
        //折前金额
        esOrderMain.put("OriginalAmount",erpOrder.get("totalAmount"));
        //整单折扣率
        esOrderMain.put("Discount", getFormatDiscount(erpOrder.get("totalDepositRate")));
        //折后金额
        esOrderMain.put("PayAmount",erpOrder.get("actualAmount"));
        //整单去零
        esOrderMain.put("DecreaseAmount","0");
        //花费积分
        esOrderMain.put("CostPoint","0");
        //花费积分对应的抵扣金额
        esOrderMain.put("CostpointAmount","0");
        //实收金额
        esOrderMain.put("Amount",erpOrder.get("initialActualAmount"));
        //计算明细得到
        esOrderMain.put("Quantity",erpOrder.get("totalQuantity"));
        //销售记录的被修改次数
        esOrderMain.put("ModifiedTimes",erpOrder.get("ModifiedTimes"));
        //快递公司代号
        esOrderMain.put("ExpressCompanyCode",erpOrder.get("logisticName"));
        //快递单编号
        esOrderMain.put("ExpressBillCode",erpOrder.get("expressNumber"));
        //快递费用
        esOrderMain.put("ExpressCost", erpOrder.get("deliveryCost"));
        //工作流ID
        esOrderMain.put("WorkFlowID",null);
        //备注
        esOrderMain.put("Comments",erpOrder.get("erpMemo"));
        // 是否预售单(状态为1，支付时间不为空)---- 做过预售的单子不一定能够设置这个状态（因为可能订单拉进来时状态已经不是状态1了）
        if(billState.equals("1") && !CherryBatchUtil.isBlankString(payTime)){
        	esOrderMain.put("PreSale","1");
        }
        
        setInsertInfoMapKey(esOrderMain);
        return esOrderMain;
    }
    /**
     * 得到根据地址截取到的省市名称
     * @param address
     * @param orderNumber
     * @return
     */
    public Map<String,Object> getProvNameAndCityName(String address,String orderNumber){
    	Map<String,Object> resultMap = new HashMap<String, Object>();
        try{
            if(CherryBatchUtil.isBlankString(address)){
            	//省份
            	resultMap.put("ConsigneeProvince",null);
            	//城市
            	resultMap.put("ConsigneeCity",null);
            	return resultMap;
            } else{
        		String prov = null;
        		String city = null;
        		int municiAddressSH = address.indexOf("上海");
        		int municiAddressBJ = address.indexOf("北京");
        		int municiAddressCQ = address.indexOf("重庆");
        		int municiAddressTJ = address.indexOf("天津");
        		int municiAddressTW = address.indexOf("台湾");
        		if (municiAddressSH == 0 || municiAddressBJ == 0 || municiAddressCQ == 0 || municiAddressTJ == 0) {
        			prov = address.substring(0, 2);
        			city = address.substring(2, 5);
        		} else if (address.contains("省") || municiAddressTW == 0) {
        			String tempAdress="";
        			prov = address.substring(0, address.indexOf("省") + 1);
        			tempAdress = address.substring(address.indexOf("省") + 1);
        			//台湾特殊处理逻辑
        			if(municiAddressTW == 0 && CherryBatchUtil.isBlankString(prov)){
        				prov = address.substring(0, 2);
        				tempAdress = address.substring(2);
        			}
        			// 如果省份长度大于4个字时进行以下判断
        			if (prov.length() > 4) {
        				String existsAddress = prov.substring(prov.indexOf("省") - 3,prov.indexOf("省"));
        				if (prov.contains("自治区") && !existsAddress.contains("自治区")) {
        					prov = prov.substring(0, prov.indexOf("自治区") + 3);
        					tempAdress = address.substring(address.indexOf("自治区") + 3);
        				} else if (prov.contains("台湾")) {
        					prov = prov.substring(0, prov.indexOf("台湾") + 2);
        					tempAdress = address.substring(address.indexOf("台湾") + 2);
        				}
        			}
        			if (tempAdress.contains("市")) {
        				city = tempAdress.substring(0, tempAdress.indexOf("市") + 1);
        				// 如果市份长度大于3个字时进行以下判断
        				if (city.length() > 3) {
        					String existsAddress = city.substring(tempAdress.indexOf("市") - 2,tempAdress.indexOf("市"));
        					// 如果截取市汉字的前两位不是“州”或“地区”，“林区”，“县”(县市级单位)时，判断截取的市份下是否有州或地区，林区，县，如果有，则重新截取
        					if (city.contains("州") && !existsAddress.contains("州")) {
        						city = city.substring(0, city.indexOf("州") + 1);
        					} else if (city.contains("地区") && !existsAddress.contains("地区")) {
        						city = tempAdress.substring(0,tempAdress.indexOf("地区") + 2);
        					} else if(city.contains("林区") && !existsAddress.contains("林区")){
        						city = tempAdress.substring(0,tempAdress.indexOf("林区") + 2);
        					} else if(city.contains("县") && !existsAddress.contains("县")){
        						//此为特殊处理，城市包括可以有县市级
        						city = tempAdress.substring(0,tempAdress.indexOf("县") + 1);
        					}
        				}
        			} else if (tempAdress.contains("地区")) {
        				city = tempAdress.substring(0, tempAdress.indexOf("地区") + 2);
        				// 如果市份长度大于3个字时进行以下判断
        				if (city.length() > 3) {
        					String existsAddress = city.substring(tempAdress.indexOf("地区") - 1,tempAdress.indexOf("地区"));
        					// 如果截取地区汉字的前一位不是“州”或“林区”，“县”（县市级单位）时，判断截取的地区下是否有州，林区，县（县市级单位），如果有，则重新截取
        					if (city.contains("州") && !existsAddress.contains("州")) {
        						city = city.substring(0, city.indexOf("州") + 1);
        					}else if(city.contains("林区") && !existsAddress.contains("林区")){
        						city = tempAdress.substring(0,tempAdress.indexOf("林区") + 2);
        					}else if(city.contains("县") && !existsAddress.contains("县")){
        						//此为特殊处理，城市包括可以有县市级
        						city = tempAdress.substring(0,tempAdress.indexOf("县") + 1);
        					}
        				}
        			} else if (tempAdress.contains("州")) {
        				city = tempAdress.substring(0, tempAdress.indexOf("州") + 1);
        				if (city.length() > 3) {
        					String existsAddress = city.substring(tempAdress.indexOf("州") - 2,tempAdress.indexOf("州"));
        					// 如果截取地区汉字的前一位不是“林区”，“县”（县市级单位）时，判断截取的地区下是否有林区，县（县市级单位），如果有，则重新截取
	        					if(city.contains("林区") && !existsAddress.contains("林区")){
	        						city = tempAdress.substring(0,tempAdress.indexOf("林区") + 2);
	        					}else if(city.contains("县") && !existsAddress.contains("县")){
	        						//此为特殊处理，城市包括可以有县市级
	        						city = tempAdress.substring(0,tempAdress.indexOf("县") + 1);
	        					}
        					}
        			} else if(tempAdress.contains("林区")){
        				city = tempAdress.substring(0,tempAdress.indexOf("林区") + 2);
        				if (city.length() > 3) {
        					String existsAddress = city.substring(tempAdress.indexOf("林区") - 1,tempAdress.indexOf("林区"));
        					// 如果截取地区汉字的前一位不是“县”（县市级单位）时，判断截取的地区下是否有县（县市级单位），如果有，则重新截取
	        					if(city.contains("县") && !existsAddress.contains("县")){
	        						//此为特殊处理，城市包括可以有县市级
	        						city = tempAdress.substring(0,tempAdress.indexOf("县") + 1);
	        					}
        					}
        			} else if(tempAdress.contains("县")){
        				city = tempAdress.substring(0,tempAdress.indexOf("县") + 1);
        			} else {
        				city = "未匹配到市";
        			}
        		} else if (address.contains("自治区")) {
        			prov = address.substring(0, address.indexOf("自治区") + 3);
        			String snapAddress = address.substring(address.indexOf("自治区") + 3);

        			if (snapAddress.contains("市")) {
        				city = snapAddress.substring(0, snapAddress.indexOf("市") + 1);
        				// 如果市份长度大于4个字时进行以下判断
        				if (city.length() > 4) {
        					String existsAddress = city.substring(snapAddress.indexOf("市") - 3,snapAddress.indexOf("市"));
        					// 如果截取市汉字的前三位不是“自治州”或“地区”或“盟”，“县”（县市级单位）时，判断截取的市份下是否有自治州或地区，盟，县（县市级单位），如果有，则重新截取
        					if (city.contains("自治州") && !existsAddress.contains("自治州")) {
        						city = snapAddress.substring(0,snapAddress.indexOf("自治州") + 3);
        					} else if (city.contains("地区") && !existsAddress.contains("地区")) {
        						city = snapAddress.substring(0,snapAddress.indexOf("地区") + 2);
        					} else if (city.contains("盟") && !existsAddress.contains("盟")) {
        						city = snapAddress.substring(0,snapAddress.indexOf("盟") + 1);
        					}else if(city.contains("县") && !existsAddress.contains("县")){
        						//此为特殊处理，城市包括可以有县市级
        						city = snapAddress.substring(0,snapAddress.indexOf("县") + 1);
        					}
        				}
        			} else if (snapAddress.contains("自治州")) {
        				city = snapAddress.substring(0, snapAddress.indexOf("自治州") + 3);
        				// 如果市份长度大于4个字时进行以下判断
        				if (city.length() > 4) {
        					String existsAddress = city.substring(snapAddress.indexOf("自治州") - 2,snapAddress.indexOf("自治州"));
        					// 如果截取自治州汉字的前两位不是“盟”或“地区”，“县”（县市级单位）时，判断截取的自治州下是否有盟或地区，县（县市级单位），如果有，则重新截取
        					if (city.contains("地区") && !existsAddress.contains("地区")) {
        						city = snapAddress.substring(0,snapAddress.indexOf("地区") + 2);
        					} else if (city.contains("盟") && !existsAddress.contains("盟")) {
        						city = snapAddress.substring(0,snapAddress.indexOf("盟") + 1);
        					}else if(city.contains("县") && !existsAddress.contains("县")){
        						//此为特殊处理，城市包括可以有县市级
        						city = snapAddress.substring(0,snapAddress.indexOf("县") + 1);
        					}
        				}
        			} else if (snapAddress.contains("地区")) {
        				city = snapAddress.substring(0, snapAddress.indexOf("地区") + 2);
        				// 如果市份长度大于4个字时进行以下判断
        				if (city.length() > 4) {
        					String existsAddress = city.substring(snapAddress.indexOf("地区") - 3,snapAddress.indexOf("地区"));
        					// 如果截取地区汉字的前三位不是“盟”，县（县市级单位）时，判断截取的地区下是否有盟，县（县市级单位），如果有，则重新截取
        					if (city.contains("盟") && !existsAddress.contains("盟")) {
        						city = snapAddress.substring(0,snapAddress.indexOf("盟") + 1);
        					}else if(city.contains("县") && !existsAddress.contains("县")){
        						//此为特殊处理，城市包括可以有县市级
        						city = snapAddress.substring(0,snapAddress.indexOf("县") + 1);
        					}
        				}
        			} else if (snapAddress.contains("盟")) {
        				city = snapAddress.substring(0, snapAddress.indexOf("盟") + 1);
        				// 如果市份长度大于4个字时进行以下判断
        				if (city.length() > 4) {
        					String existsAddress = city.substring(snapAddress.indexOf("盟") - 1,snapAddress.indexOf("盟"));
        					// 如果截取地区汉字的前三位不是县（县市级单位）时，判断截取的地区下是否有县（县市级单位），如果有，则重新截取
        					if(city.contains("县") && !existsAddress.contains("县")){
        						//此为特殊处理，城市包括可以有县市级
        						city = snapAddress.substring(0,snapAddress.indexOf("县") + 1);
        					}
        				}
        			} else if(snapAddress.contains("县")){
        				//此为特殊处理，城市包括可以有县市级
						city = snapAddress.substring(0,snapAddress.indexOf("县") + 1);
        			}else {
        				city = "未匹配到市";
        			}
        		} else if(address.contains("香港")){
        			//此为对香港城市的特殊处理
        			int municiAddressHK= address.indexOf("香港");
        			if(municiAddressHK == 0){
        				prov = address.substring(0,2)+"特别行政区";
        				String snapAddress = address.substring(2);
        				snapAddress = snapAddress.replace("特别行政区", "");
        				if(snapAddress.contains("香港岛")){
        					city = snapAddress.substring(0,snapAddress.indexOf("香港岛")+3);
        					if(city.contains("九龙") || city.contains("九龙半岛")){
        						city = city.substring(0,city.indexOf("九龙")+2)+"半岛";
        					} else if(city.contains("新界")){
        						city = city.substring(0,city.indexOf("新界")+2);
        					}
        				} else if(snapAddress.contains("九龙")){
        					city = snapAddress.substring(snapAddress.indexOf("九龙"),snapAddress.indexOf("九龙")+2)+"半岛";
        					if(city.contains("新界")){
        						city = city.substring(0,city.indexOf("新界")+2);
        					}
        				} else if(snapAddress.contains("新界")){
        					city = snapAddress.substring(0,snapAddress.indexOf("新界")+2);
        				}else{
        					city = "未匹配到市";
        				}
        			}
        		} else if(address.contains("澳门")){
        			//此为对澳门城市的特殊处理
        			int municiAddressMAC= address.indexOf("澳门");
        			if(municiAddressMAC == 0){
        				prov = address.substring(0,2)+"特别行政区";
        				String snapAddress = address.substring(2);
        				snapAddress = snapAddress.replace("特别行政区", "");
        				if(snapAddress.contains("澳门半岛")){
        					city = snapAddress.substring(0,snapAddress.indexOf("澳门半岛")+4);
        					if(city.contains("离岛")){
        						city = city.substring(0,city.indexOf("离岛")+2);
        					}
        				} else if(snapAddress.contains("离岛")){
        					city = snapAddress.substring(0,snapAddress.indexOf("离岛")+2);
        				}else{
        					city = "未匹配到市";
        				}
        			}
        		}else {
        			prov = "未匹配到省";
        			city = "未匹配到市";
        		}
            	
            	//省份
            	resultMap.put("ConsigneeProvince",prov);
            	//城市
            	resultMap.put("ConsigneeCity",city);
            	return resultMap;
            }
        }catch (Exception e) {
        	logger.outExceptionLog(e);
        	logger.outLog("截取省市名称出现错误，地址：【"+address+"】电商订单单号：【"+orderNumber+"】",CherryBatchConstants.LOGGER_ERROR);
        	//收货人省份
        	resultMap.put("ConsigneeProvince","未匹配到省");
        	//收货人城市
        	resultMap.put("ConsigneeCity","未匹配到市");
        	return resultMap;
		}
    }
    /**
     * 对产品条码进行处理后查询相关产品信息
     * 根据明细数据中platOutSkuId作为unitCode（先用原始code查询，查询不到再做头部去零处理后查询）来查询
     * @param paramMap
     * @return 产品的ID、UnitCode、BarCode、SaleType信息，不会返回NULL（在新后台未找到产品，MAP中的BIN_ProductVendorID=NULL）
     * @throws CherryMQException
     */
    private Map<String, Object> getESOrderDetailByHandleCode(Map<String, Object> paramMap) throws CherryMQException {
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	// 快速费与整单折扣还是作为虚拟促销品来维护
    	if(ConvertUtil.getString(paramMap.get("barcode")).equals("KDCOST")){
            resultMap.put("SaleType", "P");
            resultMap.put("BIN_ProductVendorID", paramMap.get("BIN_ProductVendorID"));
            resultMap.put("UnitCode", "KDCOST");
            resultMap.put("BarCode", "KDCOST");
            return resultMap;
        }else if(ConvertUtil.getString(paramMap.get("barcode")).equals("ZDZK")){
            resultMap.put("SaleType", "P");
            resultMap.put("BIN_ProductVendorID", paramMap.get("BIN_ProductVendorID"));
            resultMap.put("UnitCode", "ZDZK");
            resultMap.put("BarCode", "ZDZK");
            return resultMap;
        }
    	
    	resultMap.put("SaleType", "N");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
        param.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
        // 对获取的订单明细中的厂商编码【平台规格编码】进行处理
        String unitCode = ConvertUtil.getString(paramMap.get("platOutSkuId"));
        //当platOutSkuId（平台SKU商家编码）为空时，再判断platOuterId（平台宝贝商家编码）是否有对应我们系统的UnitCode
        if (unitCode.equals("")){
        	unitCode = ConvertUtil.getString(paramMap.get("platOuterId"));
        }
        // 先用原始code查询产品，若无此产品再做头部去零后再次查询
        param.put("UnitCode", unitCode);
        List<Map<String,Object>> productList = null;
        // 防止获取的unitCode为空的情况
        if(!"".equals(unitCode)) {
//        	productList = binOTHONG01_Service.getProductInfo(param);
        	productList = binOLCM60_BL.getProPrmList(param);
        }
        if((null == productList || productList.size() == 0) && !"".equals(unitCode)) {
            // 先把开头的"ZP"(不区分)字符去除
        	unitCode = unitCode.replaceFirst("^(?i:ZP)*", "");
            // 去除barCode开头的所有"0"
            unitCode = unitCode.replaceFirst("^0*", "");
            param.put("UnitCode", unitCode);
//            productList = binOTHONG01_Service.getProductInfo(param);
            productList = binOLCM60_BL.getProPrmList(param);
        }
        String productVendorID = "";
        String barCode = "";
        String type = ""; // 产品类型 N:正常产品 P:促销品
        if(null != productList && productList.size()>0){
            productVendorID = ConvertUtil.getString(productList.get(0).get("BIN_ProductVendorID"));
            barCode = ConvertUtil.getString(productList.get(0).get("BarCode"));
            type = ConvertUtil.getString(productList.get(0).get("type"));
        }
        if(productVendorID.equals("")){
            resultMap.put("BIN_ProductVendorID", null);
            resultMap.put("BarCode", null);
            resultMap.put("UnitCode", unitCode);
        }else{
            resultMap.put("BIN_ProductVendorID", productVendorID);
            resultMap.put("BarCode", barCode);
            resultMap.put("UnitCode", unitCode);
        }
        return resultMap;
    }
    
    /**
     * 查询Sale.BIN_ESOrderDetail需要的值;
     * 根据barCode或unitcode(通过系统配置项确定 )来查询产品信息
     * @param paramMap
     * @return 产品的ID、UnitCode、SaleType信息，不会返回NULL（在新后台未找到产品，MAP中的BIN_ProductVendorID=NULL）
     * @throws CherryMQException 
     */
    public Map<String,Object> getESOrderDetailNeedValue(Map<String,Object> paramMap) throws Exception{
        Map<String,Object> resultMap = new HashMap<String,Object>();
        
        if(ConvertUtil.getString(paramMap.get("barcode")).equals("KDCOST")){
            resultMap.put("SaleType", "P");
            resultMap.put("BIN_ProductVendorID", paramMap.get("BIN_ProductVendorID"));
            resultMap.put("UnitCode", "KDCOST");
            resultMap.put("barcode", paramMap.get("barcode"));
            return resultMap;
        }else if(ConvertUtil.getString(paramMap.get("barcode")).equals("ZDZK")){
            resultMap.put("SaleType", "P");
            resultMap.put("BIN_ProductVendorID", paramMap.get("BIN_ProductVendorID"));
            resultMap.put("UnitCode", "ZDZK");
            resultMap.put("barcode", paramMap.get("barcode"));
            return resultMap;
        }else if(!ConvertUtil.getString(paramMap.get("isSmartPrmFlag")).equals("")){ 
        	// 如果是智能促销返回的新增商品，则直接返回
        	resultMap.put("SaleType", ConvertUtil.getString(paramMap.get("SaleType")));
        	resultMap.put("BIN_ProductVendorID", ConvertUtil.getString(paramMap.get("BIN_ProductVendorID")));
        	resultMap.put("UnitCode", ConvertUtil.getString(paramMap.get("unitCode")));
        	resultMap.put("barcode", paramMap.get("barcode"));
        	return resultMap;
        }
        
        String saleType = ConvertUtil.getString(paramMap.get("SaleType")); 
//        resultMap.put("SaleType", "N");
//        resultMap.put("SaleType", "".equals(saleType) ? "N" : saleType); // 从智能促销返回的新增商品是有saleType的
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
        param.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
        List<Map<String,Object>> productList = null;
        
        // 电商订单匹配线下商品规则(1:使用barcode匹配、2:使用unitcode匹配)
        String organizationInfoID = ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID"));
        String brandInfoID = ConvertUtil.getString(paramMap.get("BIN_BrandInfoID"));
        String esPrtRuleConf = binOLCM14_BL.getConfigValue("1332", organizationInfoID, brandInfoID);
        Map<String,Object> productMap=new HashMap<String, Object>();
        
        if("1".equals(esPrtRuleConf)){
        	// 防止barCode为空时，SQL查询不带此参数而查出所有产品数据
        	if(!"".equals(ConvertUtil.getString(paramMap.get("barcode")))) {
        		param.put("BarCode", paramMap.get("barcode"));
//        		productList = binOTHONG01_Service.getProductInfo(param);
        		productList = binOLCM60_BL.getProPrmList(param);
        	}
		} else if ("2".equals(esPrtRuleConf)) {
			if ("true".equals(PropertiesUtil.pps.getProperty("ES_PrtRel_Flag","false").trim())) {
				productMap = getProductRelInfo(paramMap);
			} else {
				// productNumber 商品编码
				if (!"".equals(ConvertUtil.getString(paramMap.get("productNumber")))) {
					param.put("UnitCode", paramMap.get("productNumber"));
//					productList = binOTHONG01_Service.getProductInfo(param);
					productList = binOLCM60_BL.getProPrmList(param);
				}
			}
		}
        
        String productVendorID = "";
        String unitCode = "";
        String barCode = "";
        String type = ""; // 产品类型 N:正常产品 P:促销品
        String isRelationChange = ""; //产品对应关系是否改变
        if(null !=productMap && !productMap.isEmpty()){
        	productVendorID = ConvertUtil.getString(productMap.get("productVendorID"));
        	unitCode = ConvertUtil.getString(productMap.get("unitCode"));
        	barCode = ConvertUtil.getString(productMap.get("barCode"));
        	type = ConvertUtil.getString(productMap.get("type"));
        	isRelationChange = ConvertUtil.getString(productMap.get("isRelationChange"));
        }
        if(null != productList && productList.size()>0){
            productVendorID = ConvertUtil.getString(productList.get(0).get("BIN_ProductVendorID"));
            unitCode = ConvertUtil.getString(productList.get(0).get("UnitCode"));
            barCode = ConvertUtil.getString(productList.get(0).get("BarCode"));
            type = ConvertUtil.getString(productList.get(0).get("type"));
        } else {
//            productList = binOTHONG01_Service.getPrmProductInfo(param);
//            if(null != productList && productList.size()>0){
//                resultMap.put("SaleType", "P");
//                productVendorID = ConvertUtil.getString(productList.get(0).get("BIN_PromotionProductVendorID"));
//                unitCode = ConvertUtil.getString(productList.get(0).get("UnitCode"));
//            }
        }
        
//      resultMap.put("SaleType", "N");
        if(!"".equals(saleType)){
        	resultMap.put("SaleType", "N" ); // 从智能促销返回的新增商品是有saleType的
        } else{
        	resultMap.put("SaleType", type);
        }
        
      
        if(productVendorID.equals("")){
            resultMap.put("BIN_ProductVendorID", null);
            resultMap.put("UnitCode", null);
            resultMap.put("barcode", paramMap.get("barcode"));
        }else{
            resultMap.put("BIN_ProductVendorID", productVendorID);
            resultMap.put("UnitCode", unitCode);
            if ("2".equals(esPrtRuleConf)){
            	// 使用productNumber作为 unitCode时，barcode来自于从新后台查出的数值
            	resultMap.put("barcode", barCode);
            	paramMap.put("barcode",barCode); 
            }
        }
        resultMap.put("isRelationChange", isRelationChange);
        return resultMap;
    }
    /**
     * 维护电商SKU码和新后台编码的对应关系
     * @param paramMap
     * @return
     */
	private Map<String, Object> getProductRelInfo(Map<String, Object> paramMap) {
		//获得电商对应关系配置查询条件，1:代表SkuCode编码，2：代表OutCode编码
		String prtRelConfigCondition = PropertiesUtil.pps.getProperty("ES_PrtRelConfigCondition","S").trim();
		/** 电商产品对应关系改变标识 0：异常，1：正常 **/
		String isRelationChange = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BIN_OrganizationInfoID",paramMap.get("BIN_OrganizationInfoID"));
		param.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
		List<Map<String, Object>> productList = null;
		String skuID = ConvertUtil.getString(paramMap.get("platOutSkuId"));
		String outID = ConvertUtil.getString(paramMap.get("platOuterId")); // 宝贝编码;
		String configCondition = "";
		if("1".equalsIgnoreCase(prtRelConfigCondition)){
			configCondition = skuID;
		} else if ("2".equalsIgnoreCase(prtRelConfigCondition)){
			configCondition = outID;
		} else {
			logger.outLog("未找到合适的查询编码关系条件,配置查询编码关系条件为：【"+prtRelConfigCondition+"】",CherryBatchConstants.LOGGER_ERROR);
		}
		// productNumber 商品编码
		if (!"".equals(ConvertUtil.getString(paramMap.get("productNumber")))) {
			param.put("UnitCode", paramMap.get("productNumber"));
//			productList = binOTHONG01_Service.getProductInfo(param);
			productList = binOLCM60_BL.getProPrmList(param);
		} else {
			//当SKU编码不为空时，根据SKU编码和交易时间查询
			//当宝贝编码不为空时，根据宝贝编码和交易时间查询
			if(!CherryBatchUtil.isBlankString(configCondition)){
				Map<String,Object> unitCodeMap = new HashMap<String, Object>();
				unitCodeMap.put("TradeDateTime", paramMap.get("TradeDateTime"));
				if("1".equalsIgnoreCase(prtRelConfigCondition)){
					unitCodeMap.put("skuCode", skuID);
				} else if("2".equalsIgnoreCase(prtRelConfigCondition)){
					unitCodeMap.put("outCode", outID);
				}
				//查询GetDate<=TradeDate的数据，以GetDate（写入时间）倒序排序
				List<Map<String,Object>> getUnitCodeList = binOTHONG01_Service.getUnitCodeByTradeTime(unitCodeMap);
				if (!CherryBatchUtil.isBlankList(getUnitCodeList)) {
					isRelationChange = ConvertUtil.getString(getUnitCodeList.get(0).get("IsRelationChange"));
					String unitCode = ConvertUtil.getString(getUnitCodeList.get(0).get("UnitCode"));
					param.put("UnitCode", unitCode);
//					productList = binOTHONG01_Service.getProductInfo(param);
					productList = binOLCM60_BL.getProPrmList(param);
				}else{
					unitCodeMap.remove("TradeDateTime");
					unitCodeMap.put("exeistsTradeDateTime", paramMap.get("TradeDateTime"));
					//当GetDate<=TradeDate没有查到数据时，根据GetDate>=TradeDate以GetDate（写入时间）升序再次查询
					List<Map<String,Object>> getUnitCodeListByGetDate = binOTHONG01_Service.getUnitCodeByTradeTime(unitCodeMap);
					if(!CherryBatchUtil.isBlankList(getUnitCodeListByGetDate)){
						isRelationChange = ConvertUtil.getString(getUnitCodeListByGetDate.get(0).get("IsRelationChange"));
						String unitCode = ConvertUtil.getString(getUnitCodeListByGetDate.get(0).get("UnitCode"));
						param.put("UnitCode", unitCode);
//						productList = binOTHONG01_Service.getProductInfo(param);
						productList = binOLCM60_BL.getProPrmList(param);
					} else {
						//当根据SKU编码和交易时间未查询到数据时，根据SKU编码，交易时间以倒序查询取得第一条数据 
						if("1".equalsIgnoreCase(prtRelConfigCondition)){
							param.put("compareSkuCode", skuID);
						} else if("2".equalsIgnoreCase(prtRelConfigCondition)){
							param.put("compareOutCode", outID);
						}
						List<Map<String,Object>> comparecUnitCodeList = binOTHONG01_Service.getUnitCodeByTradeTime(param);
						if(!CherryBatchUtil.isBlankList(comparecUnitCodeList)){
							isRelationChange = ConvertUtil.getString(comparecUnitCodeList.get(0).get("IsRelationChange"));
							String compareUnitCode = ConvertUtil.getString(comparecUnitCodeList.get(0).get("UnitCode"));
							param.put("UnitCode", compareUnitCode);
//							productList = binOTHONG01_Service.getProductInfo(param);
							productList = binOLCM60_BL.getProPrmList(param);
						}
					}
				}
			}
		}
		if (!CherryBatchUtil.isBlankList(productList)) {
			String productVendorID = ConvertUtil.getString(productList.get(0).get("BIN_ProductVendorID"));
			String unitCode = ConvertUtil.getString(productList.get(0).get("UnitCode"));
			String barCode = ConvertUtil.getString(productList.get(0).get("BarCode"));
			String type = ConvertUtil.getString(productList.get(0).get("type")); // 产品类型 N:正常产品 P:促销品
			
			if(!CherryBatchUtil.isBlankString(configCondition)){
				Map<String, Object> unitCodeParamMap = new HashMap<String, Object>();
				if("1".equalsIgnoreCase(prtRelConfigCondition)){
					unitCodeParamMap.put("skuCode", skuID);
				} else if ("2".equalsIgnoreCase(prtRelConfigCondition)){
					unitCodeParamMap.put("outCode", outID);
				}
				// 写入产品对应关系表
				Map<String, Object> addEsProductMap = new HashMap<String, Object>();
				setInsertInfoMapKey(addEsProductMap);
				addEsProductMap.put("skuCode", skuID);
				addEsProductMap.put("outCode", outID);
				addEsProductMap.put("unitCode", unitCode);
				addEsProductMap.put("barCode", barCode);
				addEsProductMap.put("getDate", paramMap.get("TradeDateTime"));
				addEsProductMap.put("BIN_OrganizationInfoID",paramMap.get("BIN_OrganizationInfoID"));
				addEsProductMap.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
				addEsProductMap.put("esProductTitleName", paramMap.get("platTitle"));
				// 检查OutCode是否在【对应关系表】存在
				List<Map<String,Object>> existsUnitCodeList = binOTHONG01_Service.getUnitCodeByTradeTime(unitCodeParamMap);
				if(!CherryBatchUtil.isBlankList(existsUnitCodeList)){
					//比对数据是否存在 false：不存在 true：存在
					boolean existsFlag = false;
					Map<String,Object> existsMap = new HashMap<String, Object>();
					for (Map<String, Object> existsUnitCodeMap : existsUnitCodeList) {
						//当查询出的UnitCode和存在的UnitCode对比相等时，更新交易时间 ，否则添加一个新的电商产品对应关系，对应变化为【IsRelationChange=0】
						String existsUnitCode = ConvertUtil.getString(existsUnitCodeMap.get("UnitCode"));
						if(unitCode.equals(existsUnitCode)){
							existsFlag = true;
							existsMap.putAll(existsUnitCodeMap);
						}
					}
					if(existsFlag){
						//获取对应关系改变标识 0：异常，1：正常
						isRelationChange = ConvertUtil.getString(existsMap.get("IsRelationChange"));
						String existsTradeTime = ConvertUtil.getString(existsMap.get("GetDate"));
						String tradeDate = ConvertUtil.getString(paramMap.get("TradeDateTime"));
						//返回0表示两个日期相等，返回比0小的值表示value1在value2之前，返回比0大的值表示value1在value2之后
						//当交易时间小于已存在数据的交易时间，则更新数据
						int compareResult=DateUtil.compareDate(tradeDate,existsTradeTime);
						if(compareResult < 0){
							try{
								//更新电商对应关系表数据的交易时间
								String productRelationId = ConvertUtil.getString(existsMap.get("productRelationId"));
								Map<String,Object> updateMap=new HashMap<String, Object>();
								setInsertInfoMapKey(updateMap);
								updateMap.put("productRelationId", productRelationId);
								updateMap.put("tradeDate", tradeDate);
								binOTHONG01_Service.updateProductRelation(updateMap);
							}catch (Exception e) {
								logger.outExceptionLog(e);
								logger.outLog("更新电商产品对应关系数据时，出现异常。宝贝编码：【"+outID+"】UnitCode编码：【"+unitCode+"】BarCode编码：【"+barCode+"】ID为：【"+existsMap.get("productRelationId")+"】",CherryBatchConstants.LOGGER_ERROR);
							}
						}
					} else {
						try {
							//对应关系改变标识，0：异常，1：正常
							isRelationChange = "0";
							addEsProductMap.put("isRelationChange", isRelationChange);
							binOTHONG01_Service.addEsProductRelation(addEsProductMap);
						} catch (Exception e) {
							logger.outExceptionLog(e);
							logger.outLog("新增电商产品对应关系数据时，出现异常。宝贝编码：【"+outID+"】UnitCode编码：【"+unitCode+"】BarCode编码：【"+barCode+"】",CherryBatchConstants.LOGGER_ERROR);
						}
					}
				} else {
					try {
						//对应关系改变标识，0：异常，1：正常
						isRelationChange = "1";
						addEsProductMap.put("isRelationChange", isRelationChange);
						binOTHONG01_Service.addEsProductRelation(addEsProductMap);
					} catch (Exception e) {
						logger.outExceptionLog(e);
						logger.outLog("新增电商产品对应关系数据时，出现异常。宝贝编码：【"+outID+"】UnitCode编码：【"+unitCode+"】BarCode编码：【"+barCode+"】",CherryBatchConstants.LOGGER_ERROR);
					}
				}
			}
			resultMap.put("productVendorID", productVendorID);
			resultMap.put("unitCode", unitCode);
			resultMap.put("barCode", barCode);
			resultMap.put("type", type);
		}
		resultMap.put("isRelationChange", isRelationChange);
		return resultMap;
	}
    
    /**
     * 访问其他系统的WebService得到订单信息
     * @param paramMap
     * @return
     * @throws Exception 
     */
    private Map<String, Object> searchOrder(Map<String, Object> paramMap) throws Exception {
        //取电商接口信息
        Map<String,Object> esInterfaceInfo = getESInterfaceInfo(paramMap);
        paramMap.put("ESInterfaceInfo", esInterfaceInfo);
        
        List<Map<String,Object>> orderMainIFInfos = (List<Map<String,Object>>)esInterfaceInfo.get("GetOrderMain");
        Map<String,Object> orderDetailIFInfo = (Map<String,Object>)esInterfaceInfo.get("GetOrderDetail");
        Map<String,Object> resultMap = new HashMap<String,Object>();
        for(int i=0;i<orderMainIFInfos.size();i++){
            Map<String,Object> orderMainIFInfo = (Map<String, Object>) orderMainIFInfos.get(i);
            String startTime = ConvertUtil.getString(orderMainIFInfo.get("GetDataEndTime"));
            int timeStep = CherryUtil.obj2int(orderMainIFInfo.get("TimeStep"));
            String endTime = DateUtil.addDateByMinutes(DateUtil.DATETIME_PATTERN,startTime, timeStep);
            String sysDateTime = CherryUtil.getSysDateTime(DateUtil.DATETIME_PATTERN);
            if(DateUtil.compareDate(endTime,sysDateTime )>0){
                //控制截止时间不能超过当前服务器时间-1
                endTime = DateUtil.addDateByMinutes(DateUtil.DATETIME_PATTERN, sysDateTime, -1);
            }
            String organizationInfoID = ConvertUtil.getString(paramMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
            String brandInfoID = ConvertUtil.getString(paramMap.get(CherryBatchConstants.BRANDINFOID));
            Map<String,Object> param = new HashMap<String,Object>();
            
            // 目前只有雅芳品牌有产品套装的情况 ----只有支持套装的情况才去校验XNTZ9999必须存在
            boolean isSupportTZ = binOLCM14_BL.isConfigOpen("1322", organizationInfoID, brandInfoID);
            String xntzID = null;
            //查询条码为XNTZ9999的虚拟促销品快递费，不存在报错
            if(isSupportTZ) {
	            param.put("BIN_OrganizationInfoID", organizationInfoID);
	            param.put("BIN_BrandInfoID", brandInfoID);
	            param.put("BarCode", "XNTZ9999");
	            param.put("UnitCode", "XNTZ9999");
	            param.put("TradeDateTime", startTime);
	            int productVendorID = binBEMQMES97_BL.getProductVendorID(null, param, false);
	            if(productVendorID == 0){
	                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
	                batchExceptionDTO.setBatchName(this.getClass());
	                // 宏巍电商订单获取，需要增加一条编码条码为XNTZ9999的虚拟产品作为套装产品。
	                batchExceptionDTO.setErrorCode("EOT00078");
	                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
	                throw new CherryBatchException(batchExceptionDTO);
	            }
	            xntzID = ConvertUtil.getString(productVendorID);
            }
            
            /* 运费不加入到明细里面 2016.02.05
            //查询条码为KDCOST的虚拟套装产品，不存在报错
            param = new HashMap<String,Object>();
            param.put("BIN_OrganizationInfoID", organizationInfoID);
            param.put("BIN_BrandInfoID", brandInfoID);
            param.put("BarCode", "KDCOST");
            param.put("UnitCode", "KDCOST");
            param.put("TradeDateTime", startTime);
            Map<String,Object> prmInfo = binBEMQMES97_BL.getPrmInfo(null, param, false);
            if(null == prmInfo || ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID")).equals("")){
                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                batchExceptionDTO.setBatchName(this.getClass());
                batchExceptionDTO.setErrorCode("EOT00047");
                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                throw new CherryBatchException(batchExceptionDTO);
            }
            String kdCOSTID = ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID"));
            */
            String kdCOSTID = "0";
            
            //查询条码为ZDZK的虚拟促销品整单折扣，不存在报错
            param = new HashMap<String,Object>();
            param.put("BIN_OrganizationInfoID", organizationInfoID);
            param.put("BIN_BrandInfoID", brandInfoID);
            param.put("BarCode", "ZDZK");
            param.put("UnitCode", "ZDZK");
            param.put("TradeDateTime", startTime);
            Map<String,Object> prmInfo = binBEMQMES97_BL.getPrmInfo(null, param, false);
            if(null == prmInfo || ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID")).equals("")){
                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                batchExceptionDTO.setBatchName(this.getClass());
                batchExceptionDTO.setErrorCode("EOT00048");
                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                throw new CherryBatchException(batchExceptionDTO);
            }
            String zdzkID = ConvertUtil.getString(prmInfo.get("BIN_PromotionProductVendorID"));
            
            String searchType = "upd";//crt按照订单创建时间，upd按照订单修改时间
            int pageNo = 1;
            int pageSize = 10;
            String format = "json";
            
            Map<String,Object> wsParam = new HashMap<String,Object>();
            wsParam.put("BIN_OrganizationInfoID", organizationInfoID);
            wsParam.put("BIN_BrandInfoID", brandInfoID);
            wsParam.put("BrandCode", paramMap.get(CherryBatchConstants.BRAND_CODE));
            // 虚拟套装产品的ID
            wsParam.put("XNTZ9999_BIN_ProductVendorID", xntzID);
            wsParam.put("KDCOST_BIN_ProductVendorID", kdCOSTID);
            wsParam.put("ZDZK_BIN_ProductVendorID", zdzkID);
            wsParam.put("startTime", startTime);
            wsParam.put("endTime", endTime);
            wsParam.put("searchType", searchType);
            wsParam.put("pageNo", pageNo);
            wsParam.put("pageSize", pageSize);
            wsParam.put("format", format);
            wsParam.put("GetOrderMain", orderMainIFInfo);
            wsParam.put("GetOrderDetail", orderDetailIFInfo);
            String esInterfaceInfoID = ConvertUtil.getString(orderMainIFInfo.get("BIN_ESInterfaceInfoID"));
            paramMap.put(esInterfaceInfoID+"_GetDataEndTime",endTime);
            
            searchOrderByPage(wsParam,resultMap);
        }
        
        return resultMap;
    }
    
    /**
     * 分页调WebService取订单主数据
     * @param paramMap
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	private Map<String,Object> searchOrderByPage(Map<String,Object> wsParam,Map<String,Object> resultMap) throws Exception{
        DecimalFormat df = new DecimalFormat("#0.00");
        Map<String,Object> getOrderMainIF= (Map<String, Object>) wsParam.get("GetOrderMain");
        String esInterfaceInfoID = ConvertUtil.getString(getOrderMainIF.get("BIN_ESInterfaceInfoID"));
        String url = ConvertUtil.getString(getOrderMainIF.get("URL"));
        String nick = ConvertUtil.getString(getOrderMainIF.get("AccountName"));
        String name = ConvertUtil.getString(getOrderMainIF.get("AccountPWD"));
        String method = ConvertUtil.getString(getOrderMainIF.get("MethodName"));
        String format = ConvertUtil.getString(wsParam.get("format"));
        String startTime = ConvertUtil.getString(wsParam.get("startTime"));
        String endTime = ConvertUtil.getString(wsParam.get("endTime"));
        int pageNo = CherryUtil.obj2int(wsParam.get("pageNo"));
        int pageSize = CherryUtil.obj2int(wsParam.get("pageSize"));
        //取订单主数据
        WebResource webResource = WebserviceClient.getWebResource(url);
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("startTime", startTime);
        queryParams.add("endTime", endTime);
        queryParams.add("searchType", ConvertUtil.getString(wsParam.get("searchType")));
        queryParams.add("nick", nick);
        queryParams.add("name", name);
        queryParams.add("method", method);
        queryParams.add("pageNo", ConvertUtil.getString(pageNo));
        queryParams.add("pageSize", ConvertUtil.getString(pageSize));
        queryParams.add("format", format);
        String timestamp = getTimeStamp();
        queryParams.add("timestamp", getTimeStamp());
        String sign= getSign(nick,method,format,timestamp);
        queryParams.add("sign", sign);
        String result = webResource.queryParams(queryParams).get(String.class);
        
        Map<String,Object> resultJSONMap = CherryUtil.json2Map(result);
        String isSuccess = ConvertUtil.getString(resultJSONMap.get("isSuccess"));
        int orderTotalCount  = 0;
        if(isSuccess.equals("true")){
            //按店铺记录总数量
            String id_TotalCount = ConvertUtil.getString(resultMap.get(esInterfaceInfoID+"_TotalCount"));
            orderTotalCount = CherryUtil.obj2int(resultJSONMap.get("totalCount"));
            if(id_TotalCount.equals("")){
                resultMap.put(esInterfaceInfoID+"_TotalCount", orderTotalCount);
                totalCount += orderTotalCount;
            }
            
            if(pageNo == 1){
                //查询条件写入日志
                BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
                batchLoggerDTO1.setCode("IOT00001");
                batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
                batchLoggerDTO1.addParam(nick);
                batchLoggerDTO1.addParam(startTime);
                batchLoggerDTO1.addParam(endTime);
                batchLoggerDTO1.addParam(ConvertUtil.getString(orderTotalCount));
                logger.BatchLogger(batchLoggerDTO1);
            }
            
            // 预处理可能失败的件数
            int prepFailCount = 0;
            if(orderTotalCount > 0){
                //处理订单主数据
                Map<String,Object> erpOrders = (Map<String,Object>) resultJSONMap.get("erpOrders");
                List<Map<String,Object>> erpOrderList = (List<Map<String, Object>>) erpOrders.get("erpOrder");
                prepFailCount = erpOrderList.size();
                
                //查询员工ID
                String bacode = "G00001";
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("BIN_OrganizationInfoID", wsParam.get("BIN_OrganizationInfoID"));
                param.put("BIN_BrandInfoID", wsParam.get("BIN_BrandInfoID"));
                param.put("EmployeeCode", bacode);
                List<Map<String,Object>> employeeList = binOTHONG01_Service.getEmployeeInfo(param);
                String employeeID = "";
                if(null != employeeList && employeeList.size()>0){
                    employeeID = ConvertUtil.getString(employeeList.get(0).get("BIN_EmployeeID"));
                }
                
                Map<String,Object> getOrderDetailIF = (Map<String, Object>) wsParam.get("GetOrderDetail");
                //取订单明细
                Map<String,Object> orderDetailParam = new HashMap<String,Object>();
                orderDetailParam.putAll(getOrderDetailIF);
                orderDetailParam.put("BIN_OrganizationInfoID", wsParam.get("BIN_OrganizationInfoID"));
                orderDetailParam.put("BIN_BrandInfoID", wsParam.get("BIN_BrandInfoID"));
                orderDetailParam.put("XNTZ9999_BIN_ProductVendorID", wsParam.get("XNTZ9999_BIN_ProductVendorID"));
                orderDetailParam.put("KDCOST_BIN_ProductVendorID", wsParam.get("KDCOST_BIN_ProductVendorID"));
                orderDetailParam.put("ZDZK_BIN_ProductVendorID", wsParam.get("ZDZK_BIN_ProductVendorID"));
                StringBuffer billCodes = new StringBuffer();
                try{
                	// 循环取到的订单LIST（订单是分页抓取的）
                    for(int i=0;i<erpOrderList.size();i++){
                    	
                    	Map<String,Object> erpOrder = (Map<String, Object>) erpOrderList.get(i);
                    	
                    	 Map<String,Object> resultJSONDetailEx = new HashMap<String, Object>(); // 定义从接口取得的订单详细，发生异常时，将此对应写入失败履历表的Comments字段 。
                    	try{
                            erpOrder.put("BIN_OrganizationInfoID", wsParam.get("BIN_OrganizationInfoID"));
                            erpOrder.put("BIN_BrandInfoID", wsParam.get("BIN_BrandInfoID"));
                            erpOrder.put("BrandCode", wsParam.get("BrandCode"));
                            //下单时间
                            String orderDate = ConvertUtil.getString(erpOrder.get("orderDate"));
                            SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
                            Date orderDateTime = sdf.parse(orderDate);
                            if(DateUtil.compareDate(sdf.format(orderDateTime), "2014-10-01 00:00:00") < 0){
                                //不处理2014年10月01日前的数据
                                continue;
                            }
                            orderDetailParam.put("TradeDateTime", orderDate);
                            String orderNumber = ConvertUtil.getString(erpOrder.get("orderNumber"));
                            // 拼接获取订单明细的URL
                            billCodes.append("\"").append(orderNumber).append("\" ");
                            // 订单编号
                            orderDetailParam.put("orderNumber", erpOrder.get("orderNumber"));
                            // 快速费用
                            orderDetailParam.put("DeliverCost", erpOrder.get("deliveryCost"));
                            // 商品总让利
                            orderDetailParam.put("DiscountFee", erpOrder.get("discountFee"));
                            // 让利金额
                            orderDetailParam.put("SalesOrderAgioMoney", erpOrder.get("salesOrderAgioMoney"));
                            // 根据主单的相关参数抓取订单明细数据
                            Map<String,Object> resultJSONDetail = erpOrderDetail(orderDetailParam);
                            resultJSONDetailEx = resultJSONDetail;
                            
                            if(ConvertUtil.getString(resultJSONDetail.get("isSuccess")).equals("true")){
                                Map<String,Object> detail_erpOrders = (Map<String, Object>) resultJSONDetail.get("erpOrders");
                                List<Map<String,Object>> detail_erpOrder = (List<Map<String, Object>>) detail_erpOrders.get("erpOrder");
                                Map<String,Object> detail_erpOrderItems = new HashMap<String,Object>();
                                if(null != detail_erpOrder.get(0).get("erpOrderItems")){
                                    detail_erpOrderItems = (Map<String, Object>) detail_erpOrder.get(0).get("erpOrderItems");
                                }
                                List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
                                if(null != detail_erpOrderItems.get("erpOrderItem")){
                                    detailList = (List<Map<String, Object>>) detail_erpOrderItems.get("erpOrderItem");
                                }
                                String billState = getBillStateByOrderStatus(ConvertUtil.getString(erpOrder.get("orderStatus")));
                                boolean hasRefundDetail = false;//明细是否有退款标志
                                boolean allRefund = true;//明细全部是退款标志【明细全部为退款则不管主单的状态为何都认为是订单取消】
                                if(null != detailList && detailList.size()>0){
                                    for(int j=0;j<detailList.size();j++){
                                        Map<String,Object> detailDTO = detailList.get(j);
                                        String isRefund = ConvertUtil.getString(detailDTO.get("isRefund"));
                                        if(isRefund.equals("1") || billState.equals("0")){
                                            String totalAmount = ConvertUtil.getString(erpOrder.get("totalAmount"));
                                            String initialActualAmount = ConvertUtil.getString(erpOrder.get("initialActualAmount"));
                                            String actualAmount = ConvertUtil.getString(erpOrder.get("actualAmount"));
                                            String pricePay = ConvertUtil.getString(detailDTO.get("agioPrice"));
                                            String price = ConvertUtil.getString(detailDTO.get("price"));
                                            int orderCount = CherryUtil.obj2int(detailDTO.get("orderCount"));
                                            String payableAmount = df.format(new BigDecimal(pricePay).multiply(new BigDecimal(orderCount)));
                                            initialActualAmount = df.format(new BigDecimal(initialActualAmount).subtract(new BigDecimal(payableAmount)));
                                            if(new BigDecimal(initialActualAmount).doubleValue() < 0){
                                                erpOrder.put("initialActualAmount", "0.00");
                                            }else{
                                                erpOrder.put("initialActualAmount", initialActualAmount);
                                            }
                                            actualAmount = df.format(new BigDecimal(actualAmount).subtract(new BigDecimal(payableAmount)));
                                            if(new BigDecimal(initialActualAmount).doubleValue() < 0){
                                                erpOrder.put("actualAmount", "0.00");
                                            }else{
                                                erpOrder.put("actualAmount", actualAmount);
                                            }
                                            // 乘法
                                            String amount = df.format(new BigDecimal(price).multiply(new BigDecimal(orderCount)));
                                            // 减法
                                            totalAmount = df.format(new BigDecimal(totalAmount).subtract(new BigDecimal(amount)));
                                            if(new BigDecimal(totalAmount).doubleValue() < 0){
                                                erpOrder.put("totalAmount", "0.00");
                                            }else{
                                                erpOrder.put("totalAmount", totalAmount);
                                            }
                                            detailDTO.put("orderCount", "0");
                                            detailDTO.put("giftCount", "0");
                                            detailDTO.put("discountFee", "0.00");
                                            hasRefundDetail = true;
                                        }else{
                                            allRefund = false;
                                        }
                                    }
                                }
                                int totalQuantity = getTotalQuantity(detailList);
                                erpOrder.put("totalQuantity", totalQuantity);
                                erpOrder.put("BIN_EmployeeID", employeeID);
                                erpOrder.put("EmployeeCode", bacode);
                                erpOrder.put("Ticket_type", "NE");
                                erpOrder.put("ModifiedTimes", "0");
                                erpOrder.put("BillType", "1");
                                //处理电商订单主表
                                //根据接口返回的数据在新后台查出需要的数据
                                /**
                                 * NEWWITPOS-2347：对于两个手机号写在一个字段里的数据，只截止前11位作为手机号
                                 */
                                String mobilePhone = ConvertUtil.getString(erpOrder.get("mobilePhone")).trim();
                                // 电商订单表中的手机号字段长度最大为18位，超过此限制的截取前11位作为手机号。

                                erpOrder.put("mobilePhone", (mobilePhone.length() > 18 ? mobilePhone.substring(0, 11) : mobilePhone));
                               
                                /* 该段代码代码停用，开始使用BINOLCM60的共通方法----获取会员信息相关
                                Map<String,Object> baseInfoMap = getESOrderMainNeedValue(erpOrder);

                                erpOrder.put("BIN_OrganizationID", baseInfoMap.get("BIN_OrganizationID"));
                                erpOrder.put("BIN_MemberInfoID", baseInfoMap.get("BIN_MemberInfoID"));
                                erpOrder.put("MemberCode", baseInfoMap.get("MemberCode"));
                                erpOrder.put("MemberName", baseInfoMap.get("MemberName"));
                                erpOrder.put("CounterCode", baseInfoMap.get("CounterCode"));
                                erpOrder.put("MemberLevel", baseInfoMap.get("MemberLevel"));
                                */
                                
                                // ****************** NEWWITPOS-2346 会员信息处理开始使用共通方法 2015.10.12 start ******************
                                //以下代码为调用共通所使用的getMemInfo方法
                                Map<String, Object> departParamMap = new HashMap<String, Object>();
                                departParamMap.put("BIN_OrganizationInfoID", erpOrder.get("BIN_OrganizationInfoID"));
                                departParamMap.put("BIN_BrandInfoID", erpOrder.get("BIN_BrandInfoID"));
                                // 查询组织结构ID
                        		departParamMap.put("DepartName", erpOrder.get("shopName"));
                        		List<Map<String, Object>> departList = binOTHONG01_Service.getDepartInfo(departParamMap);
                        		
                        		String organizationID = "";
                        		String counterCode = "";
                        		if (!CherryBatchUtil.isBlankList(departList)) {
                        			organizationID = ConvertUtil.getString(departList.get(0).get("BIN_OrganizationID"));
                        			counterCode = ConvertUtil.getString(departList.get(0).get("DepartCode"));
                        		}
                        		erpOrder.put("BIN_OrganizationID", organizationID);
                        		erpOrder.put("CounterCode", counterCode);
                        		
                        		Map<String,Object> baseInfoMap = getMemberInfoByParam(erpOrder);
                                erpOrder.put("BIN_MemberInfoID", baseInfoMap.get("BIN_MemberInfoID"));
                                erpOrder.put("MemberCode", baseInfoMap.get("MemberCode"));
                                erpOrder.put("MemberName", baseInfoMap.get("MemberName"));
                                erpOrder.put("MemberLevel", baseInfoMap.get("MemberLevel"));
                                
                                // ****************** NEWWITPOS-2346 会员信息处理开始使用共通方法 2015.10.12 end ******************
                                
                               //判断是否已经存在电商订单
                                String oldBillState = "";
                                if(allRefund || billState.equals("0")){
                                    billState = "0";
                                    erpOrder.put("totalAmount", "0");
                                    erpOrder.put("initialActualAmount", "0");
                                    erpOrder.put("actualAmount", "0");
                                }
                                orderDetailParam.put("BillState",billState);
                                erpOrder.put("BillState", billState);
                                // 查询billCode是否存在等于orderNumber的主单数据
                                List<Map<String,Object>> esOrderMainList = binOTHONG01_Service.getESOrderMain(erpOrder);
                                List<Map<String,Object>> payDetailList = new ArrayList<Map<String,Object>>();
                                int billID = 0;
                                int modifiedTimes = 0;
                                orderDetailParam.put("BIN_OrganizationID", organizationID);
                                orderDetailParam.put("BrandCode", erpOrder.get("BrandCode"));
                                orderDetailParam.put("CounterCode", erpOrder.get("CounterCode"));
                                
                                /**
                                 * 薇诺娜对于预付定金的订单的处理：
                                 * 一般逻辑：因为主单状态为下单未付款，故不发送销售MQ
                                 * 薇诺娜特殊逻辑：对于下单未付款且付款日期不为空的订单也将发送MQ
                                 */
                                // 付款时间
                                String payTime = ConvertUtil.getString(erpOrder.get("payTime"));
                                //薇诺娜特殊配置【支持预付定金的订单发送销售MQ】
                                boolean depositOrderToSale = ConvertUtil.getString(PropertiesUtil.getMessage("DepositOrderToSale", null)).equals("true");
                                // 已经接收过的订单的付款时间
                                String oldPayTime = "";
                                /**
                                 * 对于电商主从表的处理：
                                 * 		1）电商订单数据已经存在：更新电商订单主表信息，删除电商订单明细表及支付方式明细表后重新插入。
                                 * 		2）电商订单数据不存在：新增电商订单主从表及电商支付方式表
                                 */
                                if(null != esOrderMainList && esOrderMainList.size()>0){
                                    //存在更新主表，删除明细后重新插入
                                    billID = CherryUtil.obj2int(esOrderMainList.get(0).get("BIN_ESOrderMainID"));
                                    erpOrder.put("BIN_ESOrderMainID", billID);
                                    modifiedTimes = CherryUtil.obj2int(esOrderMainList.get(0).get("ModifiedTimes"))+1;
                                    erpOrder.put("ModifiedTimes", modifiedTimes);
                                    oldBillState = ConvertUtil.getString(esOrderMainList.get(0).get("BillState"));
                                    oldPayTime = ConvertUtil.getString(esOrderMainList.get(0).get("BillPayTime"));
                                    try{
                                    	updateESOrderMain(erpOrder);
                                    }catch(Exception e){
                                		logger.outExceptionLog(e);
                                		logger.outLog("电商订单主数据更新失败:", CherryBatchConstants.LOGGER_ERROR);
                                		logger.outLog(erpOrder.toString(), CherryBatchConstants.LOGGER_ERROR);
                                		throw e;
                                    }
                                    // 已经接收的电商订单明细中产品ID为null的数量----不为零，则之前是没有发送过MQ的需要补发MQ
//                                    int oldNonProIDDetailCount = saleInfoService.getESOrderNonProIDDetailCount(erpOrder);
                                    saleInfoService.deleteESOrderDetail(erpOrder);
                                    saleInfoService.deleteESOrderPayList(erpOrder);
                                    orderDetailParam.put("BIN_ESOrderMainID", billID);
                                    //处理订单明细----产品数据的加工处理
                                    /**
                                     * 用于判断是否要发送销售相关MQ
                                     * true：存在新后台不存在的产品信息；----不需要做发送MQ的相关操作
                                     * false：不存在新后台不存在的产品信息----需要做发送MQ的相关操作
                                     */
                                    boolean ishasNonGoods = this.handleAndInsertESOrderDetail(orderDetailParam, detailList);
                                    /**
                                	 * 2015-12-21：通过判断电商明细表的产品ID是否存在为空的情况来判断是否发送过MQ不准确。
                                	 * 				1）与明细表的数据耦合性太高，一但明细被更改将造成该本程序的误判。
                                	 * 				2）在主表中增加【是否发送过MQ】字段来判断，历史数据不好处理，所以不使用此方案。
                                	 * 				3）直接去查询销售表，虽然会有MQ延迟的影响，但是不影响数据的接收，目前采用此方案。
                                	 * 				
                                	 * */
                                	Map<String, Object> saleRecord =  binOTHONG01_Service.getSaleRecordbByOrderCode(erpOrder);
                                	/**是否发送过MQ标记，true：已发送；false：未发送*/
                                	boolean isAlreadySendMQFlag = (null != saleRecord && !saleRecord.isEmpty());
                                	// 已经接进来的订单是否是需要发送MQ的订单（判断已经写入的订单数据）--用于补发销售MQ
									boolean isNeedSendMQForOldBill = (depositOrderToSale
											&& !"".equals(oldPayTime) && oldBillState.equals("1"))
											|| oldBillState.equals("2")
											|| oldBillState.equals("3")
											|| oldBillState.equals("4");
//                                    splitBomPrt(orderDetailParam, detailList);
                                    //处理付款方式明细
                                    Map<String,Object> payInfo = new HashMap<String,Object>();
                                    payInfo.put("PayTypeCode", erpOrder.get("payType"));
                                    payInfo.put("PayAmount", erpOrder.get("actualAmount"));
                                    payDetailList.add(payInfo);
                                    insertESOrderPayList(orderDetailParam, payDetailList);
                                    /**
                                     *	订单取消：0
    								 *	已下单未付款：1
    								 *	已付款：2
    								 *	已发货：3
    								 *	已收货（领取）：4
                                     */
                                    if(depositOrderToSale) {
                                    	// 大前提：当前产品都是存在的
                                    	if(!ishasNonGoods) {
	                                    	if(oldBillState.equals("1") && (billState.equals("2") || billState.equals("3") || billState.equals("4"))) {
	                                    		/**
	                                             *  BillState由1变成2/3/4时有两种情况：
	                                             *   1、上次接入时应发送MQ：
	                                             *   		1）未发送MQ：补发
	                                             *   		2）已发送：修改MQ----此情况只有预售单（付尾款时）
	                                             *   2、上次接入时不应发送MQ:
	                                             *   		1）当前应该发送MQ
	                                             */
	                                    		if(isNeedSendMQForOldBill) {
	                                    			// 当前订单在上次接入时已经应该发送MQ了
	                                    			if(!isAlreadySendMQFlag) {
		                                				// 之前应该发送MQ而没有发的，现应补一条销售MQ----MQ如果是延迟的话当前MQ会被忽略
		                                    			saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
		                                			} else {
		                                				// 对于已经发送过MQ(通过新逻辑获取的订单)--此为支付尾款的MQ，应发修改销售MQ
		                                				erpOrder.put("Ticket_type", "MO");
			                                            saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
		                                			}
	                                    		} else {
	                                    			// 之前没有必要发送MQ，现在要发送MQ
	                                    			saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
	                                    		}
	                                			
	                                    	} else {
	                                    		/**
	                                    		 * 订单状态不是:1->(2,3,4)
	                                    		 * 1、有退款的明细：
	                                    		 * 				1）未发送过销售MQ：上次接收是应发送MQ的则补发销售MQ
	                                    		 * 				2）已发送过销售MQ: 发送MO的MQ
	                                    		 * 2、无退款的明细：
	                                    		 * 				1）未发送过销售MQ：上次接收是应发送MQ的则补发销售MQ
	                                    		 * 				2）已发送过销售MQ: 目前订单状态发生变化，发送SC的MQ
	                                    		 */
	                            				if(hasRefundDetail){
	                            					if(!isAlreadySendMQFlag) {
	                            						// 之前应发送且没有发送MQ的应补发MQ
	                            						if(isNeedSendMQForOldBill) {
	                            							saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
	                            						}
	                            					} else {
	                                					// 1、已经发送过MQ的，对于退款明细应发送修改销售MQ
	                                					erpOrder.put("Ticket_type", "MO");
	    	                                            saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
	                            					}
	                            				} else {
	                            					// 没有包含退款的明细
	                            					if(!isAlreadySendMQFlag) {
	                            						// 之前应发送且没有发送MQ的应补发MQ
	                            						if(isNeedSendMQForOldBill) {
	                            							saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
	                            						}
	                            					} else if(!oldBillState.equals(billState)){
	        	                                        //电商订单状态发生改变还需要发送更改销售单据状态MQ--只对状态有发生过更改的
	    	                                        	saleInfoLogic.sendMQ_SC(getMQData_SC(erpOrder,detailList,payDetailList));
	        	                                    }
	                            				}
	                                    	}
                                    	}
                                    } else {
                                    	// 大前提：---当前产品全存在
                                    	if(!ishasNonGoods) {
	                                    	// 目前其他品牌发送销售MQ的逻辑不变----不支持预售订单的接入
	    	                                if(oldBillState.equals("1") && (billState.equals("2") || billState.equals("3") || billState.equals("4"))){
	    	                                    //电商订单更新，BillState：【1-->2/3/4】时，需要发送销售MQ----oldBillState='1'时未发送过销售MQ
	    	                                	if(!isAlreadySendMQFlag) {
	    	                                		// 未发送过MQ
	    	                                		saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
	    	                                	}
	    	                                }else{
	    	                                	/**
	                                    		 * 订单状态不是:1->(2,3,4)
	                                    		 * 1、有退款的明细：
	                                    		 * 				1）未发送过销售MQ：上次接收是应发送MQ的则补发销售MQ
	                                    		 * 				2）已发送过销售MQ: 发送MO的MQ
	                                    		 * 2、无退款的明细：
	                                    		 * 				1）未发送过销售MQ：上次接收是应发送MQ的则补发销售MQ
	                                    		 * 				2）已发送过销售MQ: 目前订单状态发生变化，发送SC的MQ
	                                    		 */
	                            				if(hasRefundDetail){
	                            					if(!isAlreadySendMQFlag) {
	                            						// 之前应发送且没有发送MQ的应补发MQ
	                            						if(isNeedSendMQForOldBill) {
	                            							saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
	                            						}
	                            					} else {
	                                					// 1、已经发送过MQ的，对于退款明细应发送修改销售MQ
	                                					erpOrder.put("Ticket_type", "MO");
	    	                                            saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
	                            					}
	                            				} else {
	                            					// 没有包含退款的明细
	                            					if(!isAlreadySendMQFlag) {
	                            						// 之前应发送且没有发送MQ的应补发MQ
	                            						if(isNeedSendMQForOldBill) {
	                            							saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
	                            						}
	                            					} else if(!oldBillState.equals(billState)){
	        	                                        //电商订单状态发生改变还需要发送更改销售单据状态MQ--只对状态有发生过更改的
	    	                                        	saleInfoLogic.sendMQ_SC(getMQData_SC(erpOrder,detailList,payDetailList));
	        	                                    }
	                            				}
	    	                                }
                                    	}
                                    }
                                } else {
                                	try{
                                		billID = insertESOrderMain(erpOrder);
                                	} catch(Exception e){
                                		logger.outExceptionLog(e);
                                		logger.outLog("电商订单主数据新增失败:", CherryBatchConstants.LOGGER_ERROR);
                                		logger.outLog(erpOrder.toString(), CherryBatchConstants.LOGGER_ERROR);
                                		throw e;
                                	}
                                    orderDetailParam.put("BIN_ESOrderMainID", billID);
                                    //处理订单明细----处理产品明细及插入明细信息
                                    boolean ishasNonGoods = this.handleAndInsertESOrderDetail(orderDetailParam, detailList);
                                    //处理付款方式明细
                                    Map<String,Object> payInfo = new HashMap<String,Object>();
                                    payInfo.put("PayTypeCode", erpOrder.get("payType"));
                                    payInfo.put("PayAmount", erpOrder.get("actualAmount"));
                                    payDetailList.add(payInfo);
                                    insertESOrderPayList(orderDetailParam, payDetailList);
                                    // 【薇诺娜】对于下单未付款但付款时间不为空的订单也发送销售MQ
                                    if((depositOrderToSale && !"".equals(payTime) && billState.equals("1")) || billState.equals("2") || billState.equals("3") || billState.equals("4")){
                                        //电商订单新增，BillState是2/3/4，需要发送销售MQ
                                    	if(!ishasNonGoods) {
                                    		// 对于订单明细中存在（新后台不存在的商品）时不发送销售MQ
                                    		saleInfoLogic.sendMQ_NS(getMQData_NS(erpOrder,detailList,payDetailList));
                                    	}
                                    }
                                }
                            }else{
                                //调用接口erpOrderDetail返回不成功
                                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                                batchExceptionDTO.setBatchName(this.getClass());
                                batchExceptionDTO.setErrorCode("EOT00045");
                                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                                batchExceptionDTO.addErrorParam(ConvertUtil.getString(getOrderDetailIF.get("MethodName")));
                                batchExceptionDTO.addErrorParam(ConvertUtil.getString(resultJSONDetail.get("queryParams")));
                                batchExceptionDTO.addErrorParam(ConvertUtil.getString(resultJSONDetail.get("GetOrderDetail_result")));
                                throw new CherryBatchException(batchExceptionDTO);
                            }
//                            throw new Exception("测试异常写入日志，第二次测试");
                    	}catch(Exception e){
                    		
                    		failCount ++;
                    		logger.outExceptionLog(e);
                    		logger.outLog("单据处理失败:", CherryBatchConstants.LOGGER_ERROR);
                    		logger.outLog(erpOrder.toString(), CherryBatchConstants.LOGGER_ERROR);
                    		//throw e;
                    		
                    		// 写入单号 
							// 设置失败履历表的参数
							Map<String,Object> falidMap = new HashMap<String, Object>();
							setInsertInfoMapKey(falidMap);
							falidMap.put("organizationInfoId", wsParam.get("BIN_OrganizationInfoID"));
                            falidMap.put("brandInfoId", wsParam.get("BIN_BrandInfoID"));
                            falidMap.put("JobCode", "BAT094"); // 
                            falidMap.put("UnionIndex", nick);
							falidMap.put("UnionIndex1", ConvertUtil.getString(erpOrder.get("orderNumber")));
							
							falidMap.put("ErrorMsg", ",{\"" + e.getMessage() + "\"}");
							
							String orderDataStr = erpOrder.toString(); // 将失败的订单信息写入失败履历
							if(null != resultJSONDetailEx && !resultJSONDetailEx.isEmpty()){
								orderDataStr += "," + resultJSONDetailEx.toString();
							}
							falidMap.put("Comments", orderDataStr); // 写入拉取订单原始信息，用于后续异常分析使用
							binbecm01_IF.mergeJobRunFaildHistory(falidMap);
                    	}
                    }
                    binOTHONG01_Service.manualCommit();
                }catch(Exception e){
                    failCount += prepFailCount;
                    //回滚
                    binOTHONG01_Service.manualRollback();
                    
                    BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
                    batchLoggerDTO.setCode("EOT00046");
                    batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
                    batchLoggerDTO.addParam(billCodes.toString());
                    batchLoggerDTO.addParam(nick);
                    batchLoggerDTO.addParam(startTime);
                    batchLoggerDTO.addParam(endTime);
                    batchLoggerDTO.addParam(ConvertUtil.getString(pageNo));
                    batchLoggerDTO.addParam(ConvertUtil.getString(pageSize));
                    CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
                    cherryBatchLogger.BatchLogger(batchLoggerDTO, e);
                    flag = CherryBatchConstants.BATCH_WARNING;
                }

                if(pageNo*pageSize < orderTotalCount){
                    wsParam.put("pageNo", pageNo+1);
                    //递归取下一页数据
                    searchOrderByPage(wsParam,resultMap);
                }
            }
        }else{
            //调用接口searchOrder返回不成功
            BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
            batchExceptionDTO.setBatchName(this.getClass());
            batchExceptionDTO.setErrorCode("EOT00045");
            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
            batchExceptionDTO.addErrorParam(method);
            // 打印调用参数
            batchExceptionDTO.addErrorParam("startTime=["+startTime+"],endTime=["+endTime
            		+"],searchType=["+ConvertUtil.getString(wsParam.get("searchType"))
            		+"],nick=["+nick+"],name=["+name+"],method=["+method+"],pageNo=["+pageNo
            		+"],pageSize=["+pageSize+"],format=["+format+"]");
            batchExceptionDTO.addErrorParam(result);
            throw new CherryBatchException(batchExceptionDTO);
        }
        resultMap.put("BatchResult", flag);
        resultMap.put(esInterfaceInfoID+"_GetDataEndTime", ConvertUtil.getString(wsParam.get("endTime")));
        
        return resultMap;
    }
    
    /**
     * 将销售明细中的产品类型为BOM的商品进行拆分，将拆分后的商品加入到销售MQ明细集合中（注：电商信息表只记录原始信息不做拆分）
     * 		拆分后的商品将根据"正常产品"或者"促销品"分别置为"正品仓"或者"促销品仓"
     * @param detailList
     * @return
     */
    public List<Map<String,Object>> splitBomPrt(Map<String,Object> erpOrder, List<Map<String,Object>> detailList){
    	// 定义返回的新的明细集合
    	List<Map<String,Object>> newDetailList = new ArrayList<Map<String,Object>>();
    	
    	Map<String,Object> sMap = new HashMap<String, Object>();
    	sMap.put("BIN_OrganizationInfoID",erpOrder.get("BIN_OrganizationInfoID"));
    	sMap.put("BIN_BrandInfoID",erpOrder.get("BIN_BrandInfoID"));
    	
    	for(int i=0; i< detailList.size(); i++ ){
    		
    		Map<String,Object> detailItem = detailList.get(i);
    		
    		String detailType = ConvertUtil.getString(detailItem.get("SaleType")); // 产品类型 N 正常产品 P 促销品
    		
    		// 产品必须在新后台存在 且 产品不是智能促销送的，才能进行套装产品拆分。
    		String BIN_ProductVendorID = ConvertUtil.getString(detailItem.get("BIN_ProductVendorID"));
    		String isSmartPrmFlag = ConvertUtil.getString(detailItem.get("isSmartPrmFlag")); // 有值：是智能促销返回的 、无值：不是智能促销返回的
    		if(detailType.equals(MessageConstants.SALE_TYPE_NORMAL_SALE) && !CherryBatchUtil.isBlankString(BIN_ProductVendorID) && CherryBatchUtil.isBlankString(isSmartPrmFlag)){
    			
    			String barCode = ConvertUtil.getString(detailItem.get("barcode"));
    			String unitCode = ConvertUtil.getString(detailItem.get("UnitCode"));
    			sMap.put("BarCode", barCode);
    			sMap.put("UnitCode", unitCode);
    			List<Map<String, Object>> prtprmBomList = binOLCM60_BL.getBomPrtList(sMap);
    			if(CherryBatchUtil.isBlankList(prtprmBomList)){
    				// 非BOM 直接添加到新的明细
    				newDetailList.add(detailItem);
    			} 
    			else{
    				// 产品BOM集合不为空，此时需要进行拆分 
    				
    		    	// 正常产品BOM集合
    		    	List<Map<String,Object>> prtBomList = new ArrayList<Map<String,Object>>();
    		    	// 促销产品BOM集合
    		    	List<Map<String,Object>> prmBomList = new ArrayList<Map<String,Object>>();
    		    	
    		    	
    		    	// 正常产品与促销品拆分处理逻辑不同，需要分别处理
    		    	for(Map<String, Object> prtBom : prtprmBomList){
    					String subProdouctType = ConvertUtil.getString(prtBom.get("SUB_ProdouctType"));
    					if("N".equals(subProdouctType)){
    						// 产品BOM
    						prtBomList.add(prtBom);
    					}else{
    						// 促销品BOM
    						prmBomList.add(prtBom);
    					}
    		    	}
    		    	
    		    	// BOM拆分"正常产品"处理
    		    	if(!CherryBatchUtil.isBlankList(prtBomList)){
    		    		addNewPrtPrmBom(newDetailList, prtBomList, detailItem, erpOrder);
    		    	}
    		    	// BOM拆分"促销品"处理
    		    	if(!CherryBatchUtil.isBlankList(prmBomList)){
    		    		addNewPrtPrmBom(newDetailList, prmBomList, detailItem, erpOrder);
    		    	}
 
    			}
    			
    			sMap.remove("BarCode");
    			sMap.remove("UnitCode");
    		} else{
    			newDetailList.add(detailItem);
    		}
    	}
    	
    	// 计算拆分后的总数量(不包括促销品的数量)
        int totalQuantity = getIgnorePrmTotalQuantity(newDetailList);
        erpOrder.put("totalQuantity", totalQuantity);
    	
    	return newDetailList;
    }
    
    /**
     * 拆分"正常产品"或"促销品"，并将拆分后处理的数据加入到新的明细中(newDetailList)
     * 拆分后的数据将根据产品属性（"正常产品"或"促销品"）设置（"正品仓"或者"促销品仓"）
     * @param newDetailList
     * @param prtprmBomList 
     * @param detailItem
     * @param erpOrder
     */
    private void addNewPrtPrmBom(List<Map<String,Object>> newDetailList,List<Map<String,Object>> prtprmBomList,Map<String,Object> detailItem,Map<String,Object> erpOrder){
    	
			// 求和BOM的产品数量（用于计算拆分时，将接口的套装产品金额按比例放到拆分后的商品明细中）--只计算类型为"正常产品"(2015.11.24)
		int prtBomTatolQuantity = 0; //ConvertUtil.getInt(comMap.get("Quantity"));
		for(Map<String, Object> prtBom : prtprmBomList){
			String subProdouctType = ConvertUtil.getString(prtBom.get("SUB_ProdouctType"));
			if("N".equals(subProdouctType)){
				int bomQuantity = ConvertUtil.getInt(prtBom.get("Quantity"));
				prtBomTatolQuantity += bomQuantity;
			}
		}
		
		// BOM商品的数量
		int orderCount = CherryUtil.obj2int(detailItem.get("orderCount")); 
		
		// 除最后一个明细商品之外的所有bom明细商品之和(不包括促销品)
		BigDecimal preBomTotalAgioPrice = new BigDecimal(0);
		BigDecimal preBomAmount = new BigDecimal(0);
		BigDecimal preBomDiscountFee = new BigDecimal(0);
		
		for(int j = 0; j < prtprmBomList.size(); j++){
			
			Map<String, Object> prtBom = prtprmBomList.get(j);
			
			// 定义拆分后新后明细行
			Map<String, Object> newDetailItem = new HashMap<String, Object>();
			newDetailItem.putAll(detailItem);
			newDetailItem.put("BIN_OrganizationInfoID",erpOrder.get("BIN_OrganizationInfoID"));
			newDetailItem.put("BIN_BrandInfoID",erpOrder.get("BIN_BrandInfoID"));
			
			// 产品核心属性 
			String subProdouctType = ConvertUtil.getString(prtBom.get("SUB_ProdouctType"));
			newDetailItem.put("BIN_ProductVendorID", prtBom.get("BIN_ProductVendorID"));
			newDetailItem.put("UnitCode", prtBom.get("UnitCode"));
			newDetailItem.put("barcode", prtBom.get("BarCode"));
			
			// 拆分后的商品数量
			int bomQuantity = ConvertUtil.getInt(prtBom.get("Quantity"));
			DecimalFormat df = new DecimalFormat("#0.00");
//			bomQuantity = df.format(new BigDecimal(orderCount).multiply(new BigDecimal(bomQuantity)));
			String billState = ConvertUtil.getString(erpOrder.get("BillState"));
            String isRefund = ConvertUtil.getString(detailItem.get("isRefund"));
            if(isRefund.equals("1") || billState.equals("0")){
            	newDetailItem.put("orderCount", "0");
            }else{
//            	newDetailItem.put("orderCount",  df.format(new BigDecimal(orderCount).multiply(new BigDecimal(bomQuantity))));
            	newDetailItem.put("orderCount",  orderCount * bomQuantity);
            }
            
            // bom明细商品原价 (取BOM的产品价格)
            newDetailItem.put("price", prtBom.get("Price"));
            
            // 实付金额
            String amount = ConvertUtil.getString(detailItem.get("amount"));
            
            // 商品折后价
            String agioPrice = ConvertUtil.getString(detailItem.get("agioPrice"));

            // 明细让利金额
            String discountFee = ConvertUtil.getString(detailItem.get("discountFee"));
            
            if( j != 0 && (j == (prtprmBomList.size() - 1))){
            	// 最后一个BOM明细商品
            	
            	// bom明细商品--折后价计算
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(agioPrice).compareTo(new BigDecimal(0)) != 0 && prtBomTatolQuantity != 0 ){
            			BigDecimal lastTotalAgioPrice = new BigDecimal(agioPrice).subtract(preBomTotalAgioPrice);
            			String bomAgioPrice = df.format(lastTotalAgioPrice.divide(new BigDecimal(bomQuantity),2));
            			newDetailItem.put("agioPrice", bomAgioPrice);
            			newDetailItem.put("PricePay", bomAgioPrice);
        			}else{
            			newDetailItem.put("agioPrice", new BigDecimal(0)); 
            			newDetailItem.put("PricePay", new BigDecimal(0));
        			}
        		}else{
        			// 促销品 不计入分摊
        			newDetailItem.put("agioPrice", new BigDecimal(0)); 
        			newDetailItem.put("PricePay", new BigDecimal(0));
        		}
            		
            	
            	// bom明细商品--实付金额
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(amount).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && prtBomTatolQuantity != 0 ){
            			String bomAmount = df.format(new BigDecimal(amount).subtract(preBomAmount));
            			newDetailItem.put("amount", bomAmount);
        			}else{
        				newDetailItem.put("amount", new BigDecimal(0));
        			}
        		}else{
        			// 促销品 不计入分摊
        			newDetailItem.put("amount", new BigDecimal(0));
        		}
            		
            	
            	// bom明细商品--让利金额
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(discountFee).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && prtBomTatolQuantity != 0 ){
            			String bomDiscountFee = df.format(new BigDecimal(discountFee).subtract(preBomDiscountFee));
            			newDetailItem.put("discountFee", bomDiscountFee);
        			}else{
        				newDetailItem.put("discountFee",  new BigDecimal(0));
        			}
        		} else{
        			// 促销品 不计入分摊
        			newDetailItem.put("discountFee",  new BigDecimal(0));
        		}
            	
            }else{
            	 
            	// bom明细商品--折后价计算
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(agioPrice).compareTo(new BigDecimal(0)) != 0 && prtBomTatolQuantity != 0 ){
            			String bomAgioPrice = df.format(new BigDecimal(agioPrice).divide(new BigDecimal(prtBomTatolQuantity),2));
            			newDetailItem.put("agioPrice", bomAgioPrice); 
            			newDetailItem.put("PricePay", bomAgioPrice);
            			BigDecimal bomTotalAgioPrice = new BigDecimal(bomAgioPrice).multiply(new BigDecimal(bomQuantity));
            			preBomTotalAgioPrice = preBomTotalAgioPrice.add(bomTotalAgioPrice);
        			}else{
        				newDetailItem.put("agioPrice", new BigDecimal(0)); 
        				newDetailItem.put("PricePay", new BigDecimal(0));
        			}
        		} else{
        			// 促销品 不计入分摊
        			newDetailItem.put("agioPrice", new BigDecimal(0)); 
        			newDetailItem.put("PricePay", new BigDecimal(0));
        		}
            		
            	
            	// bom明细商品--实付金额
            		
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(amount).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && prtBomTatolQuantity != 0 ){
            			BigDecimal b1 = new BigDecimal(amount).multiply(new BigDecimal(bomQuantity));
            			String bomAmount = df.format(b1.divide(new BigDecimal(prtBomTatolQuantity),2));
            			newDetailItem.put("amount", bomAmount);
            			preBomAmount = preBomAmount.add(new BigDecimal(bomAmount));
        			}else{
        				newDetailItem.put("amount", new BigDecimal(0));
        			}
        		}else{
        			// 促销品 不计入分摊
        			newDetailItem.put("amount", new BigDecimal(0));
        		}
            		
            	
            	// bom明细商品--让利金额
        		if("N".equals(subProdouctType)){
        			if(new BigDecimal(discountFee).compareTo(new BigDecimal(0)) != 0 && bomQuantity != 0 && prtBomTatolQuantity != 0 ){
            			BigDecimal b1 = new BigDecimal(discountFee).multiply(new BigDecimal(bomQuantity));
            			String bomDiscountFee = df.format(b1.divide(new BigDecimal(prtBomTatolQuantity),2));
            			newDetailItem.put("discountFee", bomDiscountFee);
            			preBomDiscountFee = preBomDiscountFee.add(new BigDecimal(bomDiscountFee));
        			}else{
        				newDetailItem.put("discountFee", new BigDecimal(0));
        			}
        		}else{
        			// 促销品 不计入分摊
        			newDetailItem.put("discountFee", new BigDecimal(0));
        		}
            		
            }
            
            // bom明细商品折扣率
            newDetailItem.put("discountRate", "1.0");
            
            // bom明细商品--备注
    		String BIN_ProductVendorID = ConvertUtil.getString(detailItem.get("BIN_ProductVendorID"));
			String barCode = ConvertUtil.getString(detailItem.get("barcode"));
			String unitCode = ConvertUtil.getString(detailItem.get("UnitCode"));
            newDetailItem.put("memo", "此销售明细来自产品套装拆分,原套装产品信息为：" + "厂商ID["+BIN_ProductVendorID + "]、厂商编码[" + unitCode + "]、产品条码[" + barCode +"]。"   );
            
            newDetailItem.put("SaleType", subProdouctType);
            // 对于拆分出的BOM商品，根据商品属性，设置不同的逻辑仓库
			String inventoryTypeCode = MessageConstants.SALE_TYPE_PROMOTION_SALE
					.equals(subProdouctType) ? PRMOTION_DEFAULTINVENTORYTYPECODE
					: ConvertUtil.getString(detailItem
							.get("productDefaultInventoryTypeCode"));
            newDetailItem.put("InventoryTypeCode", inventoryTypeCode);
            
            newDetailList.add(newDetailItem);
		}
    }
    
    /**
     * 转换销售MQ需要的数据
     * @param dataMap
     * @return
     * @throws ParseException 
     */
    private Map<String,Object> getMQData_NS(Map<String,Object> erpOrder,List<Map<String,Object>> detailList,List<Map<String,Object>> payDetailList) throws ParseException{
    	
        // 通过原始订单商品明细拆分BOM商品数据--通过系统配置项确定是否调用该方法(电商订单支持套装BOM产品拆分)
        int organizationInfoID = CherryUtil.obj2int(erpOrder.get("BIN_OrganizationInfoID"));
        int brandInfoID = CherryUtil.obj2int(erpOrder.get("BIN_BrandInfoID"));
        boolean isSplitPrtBom = binOLCM14_BL.isConfigOpen("1338", organizationInfoID, brandInfoID);
        if(isSplitPrtBom){
        	// 将销售明细中的产品类型为BOM的商品进行拆分，将拆分后的商品加入到销售MQ明细集合中
        	detailList = splitBomPrt(erpOrder, detailList);
        }
        
		// 系统配置项1321:【电商订单线上线下会员合并规则】0：不合并（默认）1：按手机号合并 2:不合并且不存在也不新增(销售MQ不加入会员信息)
		String configValue = binOLCM14_BL.getConfigValue("1321", String.valueOf(organizationInfoID),String.valueOf(brandInfoID));
    	
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put("BrandCode", erpOrder.get("BrandCode"));
        mainData.put("TradeNoIF", erpOrder.get("orderNumber"));
        mainData.put("ModifyCounts", erpOrder.get("ModifiedTimes"));
        mainData.put("CounterCode", erpOrder.get("CounterCode"));
        mainData.put("RelevantCounterCode", "");
        mainData.put("TotalQuantity", erpOrder.get("totalQuantity"));
        
        // 总金额 2016.02.02 应付总金额需要减去运费，但是当运费为0时，则不需要
        String actualAmount = getFormatDiscount(ConvertUtil.getString(erpOrder.get("actualAmount"))); // 应付金额
        String deliveryCost = getFormatDiscount(ConvertUtil.getString(erpOrder.get("deliveryCost"))); // 运费
        DecimalFormat df = new DecimalFormat("#0.00");
        if(new BigDecimal(0).compareTo(new BigDecimal(actualAmount)) != 0){
        	actualAmount = df.format(new BigDecimal(actualAmount).subtract(new BigDecimal(deliveryCost))); // 销售的总金额需要去除运费
        }
        mainData.put("TotalAmount", actualAmount);
//        mainData.put("TotalAmount", erpOrder.get("actualAmount"));
        
        mainData.put("TradeType", MessageConstants.BUSINESS_TYPE_NS);
        mainData.put("SubType", "");
        mainData.put("RelevantNo", "");
        mainData.put("Reason", "");
        String orderDate = ConvertUtil.getString(erpOrder.get("orderDate"));
        SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
        Date tradeDateTime = sdf.parse(orderDate);
        SimpleDateFormat sdf_YMD = new SimpleDateFormat(CherryConstants.DATE_PATTERN);
        SimpleDateFormat sdf_HMS = new SimpleDateFormat("HH:mm:ss");
        mainData.put("TradeDate", sdf_YMD.format(tradeDateTime));
        mainData.put("TradeTime", sdf_HMS.format(tradeDateTime));
        mainData.put("TotalAmountBefore", erpOrder.get("BillState"));
        mainData.put("TotalAmountAfter", "");
        
        if("2".equals(configValue)){
        	mainData.put("MemberCode", "");
        	mainData.put("Member_level", "");
        } else{
        	mainData.put("MemberCode", erpOrder.get("MemberCode"));
        	mainData.put("Member_level", erpOrder.get("MemberLevel"));
        }
        mainData.put("Counter_ticket_code", "");
        mainData.put("Counter_ticket_code_pre", "");
        mainData.put("Ticket_type", erpOrder.get("Ticket_type"));
        mainData.put("Sale_status", "OK");
        //消费者类型 NP：普通个人  NG：普通团购  MP：会员个人  MG：会员团购。
        if(ConvertUtil.getString(erpOrder.get("BIN_MemberInfoID")).equals("")){
            mainData.put("Consumer_type","NP");
        }else{
        	if(!"2".equals(configValue)){
        		mainData.put("Consumer_type","MP");
        	} else{
        		mainData.put("Consumer_type","NP");
        	}
        }
        mainData.put("Original_amount", erpOrder.get("totalAmount"));
        mainData.put("Discount", getFormatDiscount(erpOrder.get("totalDepositRate")));
        mainData.put("Pay_amount", actualAmount);
        mainData.put("Decrease_amount", "0");
        mainData.put("Costpoint", "0");
        mainData.put("Costpoint_amount", "0");
        mainData.put("Sale_ticket_time", sdf.format(tradeDateTime));
        String orderWay = ConvertUtil.getString(erpOrder.get("orderWay"));
        String shopName = ConvertUtil.getString(erpOrder.get("shopName"));
        mainData.put("Data_source", getDataSourceByName(orderWay,shopName));
        mainData.put("MachineCode", "");
        mainData.put("SaleSRtype", "3");//销售
        mainData.put("BAcode", erpOrder.get("EmployeeCode"));
        mainData.put("DepartCodeDX", erpOrder.get("CounterCode"));
        mainData.put("EmployeeCodeDX", erpOrder.get("EmployeeCode"));
        resultMap.put("MainData", mainData);
        
        List<Map<String,Object>> saleDetail = new ArrayList<Map<String,Object>>();
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailDTO = detailList.get(i);
            Map<String,Object> saleDTO = new HashMap<String,Object>();
            saleDTO.put("TradeNoIF", erpOrder.get("orderNumber"));
            saleDTO.put("ModifyCounts", erpOrder.get("ModifiedTimes"));
            saleDTO.put("DetailType", detailDTO.get("SaleType"));
            saleDTO.put("BAcode", erpOrder.get("EmployeeCode"));
            saleDTO.put("StockType", CherryConstants.STOCK_TYPE_OUT);
            // MQ消息体定义时为Barcode，此程序其他地方都作为变量时使用barCode，作为key时使用BarCode;而电商接口中使用barcode
            saleDTO.put("Barcode", detailDTO.get("barcode"));
            saleDTO.put("Unitcode", detailDTO.get("UnitCode"));
            saleDTO.put("InventoryTypeCode", detailDTO.get("InventoryTypeCode"));
            int orderCount = CherryUtil.obj2int(detailDTO.get("orderCount"));
            int giftCount = CherryUtil.obj2int(detailDTO.get("giftCount"));
            saleDTO.put("Quantity", orderCount+giftCount);
            saleDTO.put("QuantityBefore", "");
            saleDTO.put("Price",detailDTO.get("PricePay"));
            saleDTO.put("Reason", detailDTO.get("memo"));
            saleDTO.put("Discount", detailDTO.get("discountRate"));
            
            if("2".equals(configValue)){
            	saleDTO.put("MemberCodeDetail", "");
            }else{
            	saleDTO.put("MemberCodeDetail", erpOrder.get("MemberCode"));
            }
            
            //促销活动主码 如果参与了活动要和POS系统对接，需要填写POS系统中的活动代码
//          esOrderDetailMap.put("ActivityMainCode","");
            String activityMainCode = ConvertUtil.getString(detailDTO.get("ActivityMainCode"));
            saleDTO.put("ActivityMainCode","".equals(activityMainCode) ? "" : activityMainCode);
          //活动代码
//          esOrderDetailMap.put("ActivityCode","");
            String activityCode = ConvertUtil.getString(detailDTO.get("ActivityCode"));
            saleDTO.put("ActivityCode","".equals(activityCode) ? "" : activityCode);
            
//          saleDTO.put("ActivityMainCode", "");  
//          saleDTO.put("ActivityCode", "");
            saleDTO.put("OrderID", "");
            saleDTO.put("CouponCode", "");
            saleDTO.put("IsStock", "");
            saleDTO.put("InformType", "");
            saleDTO.put("UniqueCode", "");
            saleDetail.add(saleDTO);
        }
        resultMap.put("SaleDetail", saleDetail);
        
        List<Map<String,Object>> payDetail = new ArrayList<Map<String,Object>>();
        for(int i=0;i<payDetailList.size();i++){
            Map<String,Object> detailDTO = payDetailList.get(i);
            Map<String,Object> payDTO = new HashMap<String,Object>();
            payDTO.put("TradeNoIF", erpOrder.get("orderNumber"));
            payDTO.put("ModifyCounts", erpOrder.get("ModifiedTimes"));
            payDTO.put("DetailType", "Y");
            payDTO.put("PayTypeCode", detailDTO.get("PayTypeCode"));
            payDTO.put("PayTypeID", "");
            payDTO.put("PayTypeName", "");
            
            // 总金额 2016.02.02 应付总金额需要减去运费，但是当运费为0时，则不需要
            String payAmount = getFormatDiscount(ConvertUtil.getString(detailDTO.get("PayAmount"))); // 应付金额
            if(new BigDecimal(0).compareTo(new BigDecimal(payAmount)) != 0){
            	payAmount = df.format(new BigDecimal(payAmount).subtract(new BigDecimal(deliveryCost))); // 销售的总金额需要去除运费
            }
            payDTO.put("Price", payAmount);
//            payDTO.put("Price", detailDTO.get("PayAmount"));
            
            payDTO.put("Reason", "");
            payDetail.add(payDTO);
        }
        resultMap.put("PayDetail", payDetail);
        
        return resultMap;
    }
    
    /**
     * 转换销售MQ需要的数据
     * @param dataMap
     * @return
     * @throws ParseException 
     */
    private Map<String,Object> getMQData_SC(Map<String,Object> erpOrder,List<Map<String,Object>> detailList,List<Map<String,Object>> payDetailList){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("BrandCode", erpOrder.get("BrandCode"));
        resultMap.put("TradeType", MessageConstants.MSG_CHANGESALESTATE);
        resultMap.put("TradeNoIF", erpOrder.get("orderNumber"));
        resultMap.put("BillState", erpOrder.get("BillState"));
        resultMap.put("ChangeTime", CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS));
        resultMap.put("Comment", "");
        String orderWay = ConvertUtil.getString(erpOrder.get("orderWay"));
        String shopName = ConvertUtil.getString(erpOrder.get("shopName"));
        resultMap.put("DataSource", getDataSourceByName(orderWay,shopName));
        resultMap.put("ModifyCounts", erpOrder.get("ModifiedTimes"));
        resultMap.put("CounterCode", erpOrder.get("CounterCode"));
        return resultMap;
    }
    
    private int insertESOrderMain(Map<String,Object> erpOrder){
        Map<String,Object> esOrderMain = setESOrderMain(erpOrder, "insert");
        int billID = saleInfoService.insertESOrderMain(esOrderMain);
        return billID;
    }
    
    private int updateESOrderMain(Map<String,Object> erpOrder){
        Map<String,Object> esOrderMain = setESOrderMain(erpOrder, "update");
        int cnt = saleInfoService.updateESOrderMain(esOrderMain);
        return cnt;
    }
    
    /**
     * 传入订单中商品相关信息取得智能促销返回的新增商品
     * @return
     * @throws ParseException,Exception  
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSmartPromotionNewProList(Map<String,Object> paramMap,List<Map<String,Object>> detailList) throws ParseException,Exception {
    	
        String organizationInfoID = ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID"));
        String brandInfoID = ConvertUtil.getString(paramMap.get("BIN_BrandInfoID"));
        
        // 电商订单匹配线下商品规则  1 使用barcode匹配产品：通过barcode匹配新后台的产品部分信息；2 使用unitcode匹配产品：通过unitcode匹配新后台的产品部分信息; 3使用ItemCode匹配产品：通过ItemCode匹配新后台的产品部分信息;
        String esPrtRuleConf = binOLCM14_BL.getConfigValue("1332", organizationInfoID, brandInfoID);
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("BIN_OrganizationInfoID", paramMap.get("BIN_OrganizationInfoID"));
        param.put("BIN_BrandInfoID", paramMap.get("BIN_BrandInfoID"));
        
    	// 定义查询智能促销接口的参数Map
    	Map<String,Object> paramActMap = new HashMap<String, Object>();
    	
    	paramActMap.put("brandCode", paramMap.get("BrandCode")); // 品牌CODE
    	paramActMap.put("organizationId", paramMap.get("BIN_OrganizationID")); // 柜台组织结构ID
    	
    	Map<String,Object> mainInfo = new HashMap<String, Object>();
        String orderDate = ConvertUtil.getString(paramMap.get("TradeDateTime")); // 交易时间
        SimpleDateFormat sdf = new SimpleDateFormat(CherryConstants.DATE_PATTERN_24_HOURS);
        SimpleDateFormat sdf_YMD = new SimpleDateFormat("yyMMdd");
        SimpleDateFormat sdf_HMS = new SimpleDateFormat("HHmmss");
        Date tradeDateTime = sdf.parse(orderDate);
        mainInfo.put("saleDate", sdf_YMD.format(tradeDateTime));
        mainInfo.put("saleTime", sdf_HMS.format(tradeDateTime));
//    	mainInfo.put("counterCode", ConvertUtil.getString(paramMap.get("CounterCode")));
        mainInfo.put("counterCode", paramMap.get("BIN_OrganizationID"));
    	paramActMap.put("mainInfo", mainInfo); // 主单数据
    	
    	List<Map<String,Object>> actDetailList = new ArrayList<Map<String,Object>>();
    	for( Map<String,Object> detailItemMap : detailList){
    		Map<String,Object> detailMap = new HashMap<String, Object>();

    		List<Map<String, Object>> productList = null;
    		
    		if ("2".equals(esPrtRuleConf)){
    			param.remove("UnitCode");
//    			String productNumber = ConvertUtil.getString(detailItemMap.get("productNumber"));
//
//    			if(productNumber.equals("B4700162")){
//    				System.out.println("");
//    			}
        		param.put("UnitCode", detailItemMap.get("productNumber"));
        		
        		if(!"".equals(ConvertUtil.getString(detailItemMap.get("productNumber")))) {
        			
            		List<Map<String, Object>> proPrmList = binOLCM60_BL.getProPrmList(param);
            		if(!CherryBatchUtil.isBlankList(proPrmList)){
            			String type =  ConvertUtil.getString(proPrmList.get(0).get("type"));
            			if("P".equals(type)){
            				// 订单中如果是促销品，则不传入智能促销
            				continue;
            			}
            		}
        			
//        			productList = binOTHONG01_Service.getProductInfo(param);
        			productList = proPrmList;
        			
        			if(!CherryBatchUtil.isBlankList(productList)){
        				detailMap.put("barCode", ConvertUtil.getString(productList.get(0).get("BarCode")));
        			} else{
        				detailMap.put("barCode", ConvertUtil.getString(detailItemMap.get("barcode")));
        			}
        		} else{
        			detailMap.put("barCode", ConvertUtil.getString(detailItemMap.get("barcode")));
        		}
    		}else{
    			detailMap.put("barCode", ConvertUtil.getString(detailItemMap.get("barcode")));
    		}
    		detailMap.put("unitCode", ConvertUtil.getString(detailItemMap.get("productNumber")));
    		detailMap.put("price", ConvertUtil.getString(detailItemMap.get("agioPrice")));
    		detailMap.put("oriPrice", ConvertUtil.getString(detailItemMap.get("price")));
//    		detailMap.put("proType", "N"); // 作为产品使用
    		detailMap.put("proType", ConvertUtil.getString(productList.get(0).get("type"))); 
    		detailMap.put("quantity", ConvertUtil.getString(detailItemMap.get("orderCount")));
    		actDetailList.add(detailMap);
    	}
    	
    	paramActMap.put("detailList", actDetailList); // 明细数据
    	
    	
    	
    	// 匹配活动规则，取得最优解活动的产品信息集合
    	Map<String,Object> resultActProMap = binOLCM60_BL.getActProMap(paramActMap);
    	List<Map<String,Object>> newSmartPromotionNewProList = (List<Map<String,Object>>)resultActProMap.get("newFlagOutDetailList"); // 智能促销添加的商品
    	List<Map<String,Object>> outResultList = (List<Map<String,Object>>)resultActProMap.get("outResultList");
    	
    	// 智能促销返回已选中的活动
//    	Map<String,Object> actResult = new HashMap<String, Object>();
//    	for(Map<String,Object> outResult : outResultList){
//    		String checkflag = ConvertUtil.getString(outResult.get("checkflag"));
//    		if("1".equals(checkflag)){
//    			actResult.putAll(outResult);
//    		}
//    	}
    	
    	// 智能促销返回的新增的商品
    	List<Map<String,Object>> resultNewProList = new ArrayList<Map<String,Object>>();
    	
    	if(!CherryBatchUtil.isBlankList(newSmartPromotionNewProList)){
    		
    		for(Map<String,Object> newSmartPromotionNewPro : newSmartPromotionNewProList){
    			Map<String,Object> detailMap2 = new HashMap<String, Object>();
    			
    			detailMap2.put("BIN_ProductVendorID", ConvertUtil.getString(newSmartPromotionNewPro.get("productid")));
    			detailMap2.put("barcode", ConvertUtil.getString(newSmartPromotionNewPro.get("barcode")));
    			detailMap2.put("unitCode", ConvertUtil.getString(newSmartPromotionNewPro.get("unitcode"))); // 商品编码，后面作为 unitcode查询使用
    			
    			// 退货则置为0
				String billState = ConvertUtil.getString(paramMap.get("BillState"));
//                String isRefund = ConvertUtil.getString(detailDTO.get("isRefund"));
//				if(isRefund.equals("1") || billState.equals("0")){
                if(billState.equals("0")){
	            	detailMap2.put("orderCount", "0");
	            }else{
	            	detailMap2.put("orderCount", ConvertUtil.getString(newSmartPromotionNewPro.get("quantity")));
	            }
    			
    			detailMap2.put("price", ConvertUtil.getString(newSmartPromotionNewPro.get("ori_price"))); // 新后台返回的商品原价
    			detailMap2.put("agioPrice", ConvertUtil.getString(newSmartPromotionNewPro.get("price"))); // 新后台返回的销售价格
    			detailMap2.put("discountRate", "1.0");
    			detailMap2.put("memo", "["+ConvertUtil.getString(newSmartPromotionNewPro.get("mainname")) + "]活动，智能促销返回的商品(此商品仅为智能促销推算出来的，可能与实际不符，请以实际赠送商品为准) "); // 
    			
    			detailMap2.put("SaleType", ConvertUtil.getString(newSmartPromotionNewPro.get("type"))); // N P
    			
    			detailMap2.put("ActivityMainCode", ConvertUtil.getString(newSmartPromotionNewPro.get("activitycode"))); //促销活动主码 如果参与了活动要和POS系统对接，需要填写POS系统中的活动代码
    			detailMap2.put("ActivityCode", ConvertUtil.getString(newSmartPromotionNewPro.get("maincode"))); //活动代码
    			
    			detailMap2.put("isSmartPrmFlag", "1"); // 表示当前明细智能促销返回的新增商品
    			
    			resultNewProList.add(detailMap2);
    		}
    	}
    	
    	return resultNewProList;
    }
    
    /**
     * 处理产品信息及插入电商订单明细
     * 修改履历：
     * 		1、2016-05-18 WITPOSQA-21305（雅漾）
     * 			促销品需要将逻辑仓库设置为CX01（因为逻辑仓库与商品类型无关，暂时写死）
     * 
     * @param paramMap 
     * @param detailList
     * @return 是否有在新后台【不存在】的电商订单明细商品  flase：没有  true：有
     * @throws Exception
     */
    private boolean handleAndInsertESOrderDetail(Map<String,Object> paramMap,List<Map<String,Object>> detailList) throws Exception{
        if(null == detailList || detailList.size() == 0){
            return false;
        }
        boolean result = false;
        String organizationInfoID = ConvertUtil.getString(paramMap.get("BIN_OrganizationInfoID"));
        String brandInfoID = ConvertUtil.getString(paramMap.get("BIN_BrandInfoID"));
        // 目前只有雅芳品牌有产品套装的情况
        boolean isSupportTZ = binOLCM14_BL.isConfigOpen("1322", organizationInfoID, brandInfoID);
        
        // 通过原始订单明细查询智能促销中是否有新增的商品数据--通过系统配置项确定是否调用该方法(电商订单匹配智能促销活动)
        boolean isSmartPromAddPro = binOLCM14_BL.isConfigOpen("1333", organizationInfoID, brandInfoID);
        if(isSmartPromAddPro){
        	List<Map<String,Object>> smartPromotionNewProList = getSmartPromotionNewProList(paramMap, detailList);
        	if(!CherryBatchUtil.isBlankList(smartPromotionNewProList)){
        		detailList.addAll(smartPromotionNewProList);
        	}
        }
        
        //查询逻辑仓库
        Map<String,Object> logicparamMap = new HashMap<String,Object>();
        logicparamMap.put("BIN_BrandInfoID", brandInfoID);
        logicparamMap.put("Type", "1");//终端逻辑仓库
        List<Map<String,Object>> logicDepotList = binOLCM18_BL.getLogicDepotList(logicparamMap);
        String defaultInventoryTypeCode = ConvertUtil.getString(logicDepotList.get(0).get("LogicInventoryCode"));
        DecimalFormat df = new DecimalFormat("#0.00");
        
        String billState = ConvertUtil.getString(paramMap.get("BillState"));
        
        //让利金额
        String zdzk = ConvertUtil.getString(paramMap.get("SalesOrderAgioMoney"));
        if(zdzk.equals("")){
            zdzk = "0.00";
        }
        BigDecimal zdzkBD = new BigDecimal(zdzk);
        zdzkBD = zdzkBD.setScale(2, BigDecimal.ROUND_HALF_UP); 
        if(!zdzkBD.toString().equals("0.00")){
            // 整单折扣作为一个不扣库存的虚拟促销品加入明细
            Map<String, Object> expressDetail = new HashMap<String, Object>();
            expressDetail.put("BIN_ProductVendorID", paramMap.get("ZDZK_BIN_ProductVendorID"));
            expressDetail.put("barcode", "ZDZK");
            expressDetail.put("orderCount", "1");
            if(billState.equals("0")){
                expressDetail.put("orderCount", "0");
            }
            expressDetail.put("price", zdzkBD.multiply(new BigDecimal(-1)));
            expressDetail.put("agioPrice", zdzkBD.multiply(new BigDecimal(-1)));
            expressDetail.put("amount", "0");
            expressDetail.put("discountRate", "1.0");
            expressDetail.put("memo", "");
            detailList.add(expressDetail);
        }
        
        /* 2016.02.02 废除运费加入明细的操作
        String deliverCost = ConvertUtil.getString(paramMap.get("DeliverCost"));
        //快递费用
        if(!CherryBatchUtil.isBlankString(deliverCost)){
        	if(new BigDecimal(0).compareTo(new BigDecimal(deliverCost)) != 0){
        		// 快递费作为一个不扣库存的虚拟促销品加入明细
        		Map<String, Object> expressDetail = new HashMap<String, Object>();
        		expressDetail.put("BIN_ProductVendorID", paramMap.get("KDCOST_BIN_ProductVendorID"));
        		expressDetail.put("barcode", "KDCOST");
        		expressDetail.put("orderCount", "1");
        		if(billState.equals("0")){
        			expressDetail.put("orderCount", "0");
        		}
        		expressDetail.put("price", deliverCost);
        		expressDetail.put("agioPrice", deliverCost);
        		expressDetail.put("amount", "0");
        		expressDetail.put("discountRate", "1.0");
        		expressDetail.put("memo", "");
        		detailList.add(expressDetail);
        	}
        }
        */
        
        
        // 用于插入电商订单明细表的LIST
        List<Map<String,Object>> esOrderDetail = new ArrayList<Map<String,Object>>();
        // 这两个条码用于过滤不需要判断（商品是否在新后台存在）的情况----整单折扣、快递费、虚拟套装（此三者是需要在新后台预先加入的商品）
        String unitCode = "";
        String barCode = "";
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> detailDTO = detailList.get(i);
            detailDTO.put("BIN_OrganizationInfoID", organizationInfoID);
            detailDTO.put("BIN_BrandInfoID", brandInfoID);
            detailDTO.put("TradeDateTime", paramMap.get("TradeDateTime"));
            detailDTO.put("BIN_OrganizationID", paramMap.get("BIN_OrganizationID"));
            Map<String,Object> basicDetailMap = null;
            if(!isSupportTZ) {
            	basicDetailMap = getESOrderDetailNeedValue(detailDTO);
            } else {
                	/**
                	 * 雅芳特殊情况：只提供platOutSkuId作为unitCode来处理
                	 * 当platOutSkuId（平台SKU商家编码）为空时，再判断platOuterId（平台宝贝商家编码）是否有对应我们系统的UnitCode
                	 * 1)对厂商编码进行处理后作为unitCode来查询，且将其他写入到明细表的OriginalCode字段中（处理后的字段）
                	 * 2)找不到对应的产品时，认为此明细为套组，
                	 * 		在新后台记录为虚拟产品XNTZ9999,对应的platOutSkuId
                	 * 		(套组code写入明细表的OriginalCode字段中，注：无论是否为套组code都将写入到OriginalCode中方便出报表)
                	 */
            		basicDetailMap = getESOrderDetailByHandleCode(detailDTO);
            		if(null == basicDetailMap || null == basicDetailMap.get("BIN_ProductVendorID")) {
            			basicDetailMap = new HashMap<String, Object>();
            			// 支持产品套装时，套装码【平台规格编码】写入OriginalCode字段
//            			basicDetailMap.put("Comment", detailDTO.get("platOutSkuId"));
            			basicDetailMap.put("SaleType", "N");
            			basicDetailMap.put("BIN_ProductVendorID", paramMap.get("XNTZ9999_BIN_ProductVendorID"));
            			basicDetailMap.put("UnitCode", "XNTZ9999");
            			basicDetailMap.put("BarCode", "XNTZ9999");
            		}
            } 
            
            String saleType = ConvertUtil.getString(basicDetailMap.get("SaleType"));
			String detailInventoryTypeCode = MessageConstants.SALE_TYPE_PROMOTION_SALE
					.equals(saleType) ? PRMOTION_DEFAULTINVENTORYTYPECODE
					: defaultInventoryTypeCode;
            detailDTO.put("SaleType", saleType);
            detailDTO.put("InventoryTypeCode", detailInventoryTypeCode);
            // 保存产品使用的默认逻辑仓库CODE
            detailDTO.put("productDefaultInventoryTypeCode", defaultInventoryTypeCode);
            // 用于插入电商订单明细表的MAP
            Map<String,Object> esOrderDetailMap = new HashMap<String,Object>();
            //电商订单主表ID
            esOrderDetailMap.put("BIN_ESOrderMainID",paramMap.get("BIN_ESOrderMainID"));
            //当为产品时，此字段为产品厂商ID，如果是促销品则为促销品产品厂商ID（根据相应code查询不到商品信息时为NULL）
            esOrderDetailMap.put("BIN_ProductVendorID",basicDetailMap.get("BIN_ProductVendorID"));
            detailDTO.put("BIN_ProductVendorID", basicDetailMap.get("BIN_ProductVendorID"));
            unitCode = ConvertUtil.getString(basicDetailMap.get("UnitCode"));
			barCode = ConvertUtil.getString(basicDetailMap.get("BarCode"));
			// 是否是需要过滤掉的商品码
			boolean isFilterCode = "XNTZ9999".equals(unitCode)
					|| "KDCOST".equals(unitCode)
					|| "ZDZK".equals(unitCode);
            if("".equals(ConvertUtil.getString(basicDetailMap.get("BIN_ProductVendorID"))) && !isFilterCode) {
            	// 当前订单的明细存在新后台没有的商品信息----此时不发送MQ
            	// 对于整单折扣与快递费都已经加入到detailList中，都作为商品明细来处理（对于订单商品在新后台不存在而做的不发送MQ的判断须过滤掉此两种特殊商品）
            	result = true;
            }
            //当前记录的序号
            esOrderDetailMap.put("DetailNo",i+1);
            if(isSupportTZ) {
            	//厂商编码,此unitCode已做头部去零处理
                esOrderDetailMap.put("UnitCode",basicDetailMap.get("UnitCode"));
                // 发送MQ时有用
                detailDTO.put("UnitCode", basicDetailMap.get("UnitCode"));
                // 此barCode为用unitCode匹配到的barCode【此处全小写是将其作为订单原始数据来处理】
                detailDTO.put("barcode", basicDetailMap.get("BarCode"));
                //产品条码
            	esOrderDetailMap.put("BarCode",basicDetailMap.get("BarCode"));
            	// 原始码【新加字段，用于保存电商接口中的原始产品码】
            	String originalCode = ConvertUtil.getString(detailDTO.get("platOutSkuId"));
            	// 先把开头的"ZP"(不区分)字符去除
            	originalCode = originalCode.replaceFirst("^(?i:ZP)*", "");
            	originalCode = originalCode.replaceFirst("^0*", "");
            	esOrderDetailMap.put("OriginalCode", originalCode);
            } else {
            	//厂商编码(默认逻辑为用barCode去取得unitCode及产品id)
                esOrderDetailMap.put("UnitCode",basicDetailMap.get("UnitCode"));
                detailDTO.put("UnitCode", basicDetailMap.get("UnitCode"));
                //产品条码
//            	esOrderDetailMap.put("BarCode",detailDTO.get("barcode"));
            	esOrderDetailMap.put("BarCode",basicDetailMap.get("barcode"));
            	
            	// 原始码【新加字段，用于保存电商接口中的原始产品码】
            	String originalCode = ConvertUtil.getString(detailDTO.get("platOutSkuId"));
            	esOrderDetailMap.put("OriginalCode", originalCode);
            	// 宝贝编码 (产品对应关系使用的编码)
            	String outCode = ConvertUtil.getString(detailDTO.get("platOuterId"));
            	esOrderDetailMap.put("OutCode", outCode);
            }
            //数量 
            int orderCount = CherryUtil.obj2int(detailDTO.get("orderCount"));
            int giftCount = CherryUtil.obj2int(detailDTO.get("giftCount"));
            esOrderDetailMap.put("Quantity",orderCount + giftCount);
            //原始价格
            String price = ConvertUtil.getString(detailDTO.get("price"));
            esOrderDetailMap.put("Price",price);
            //折后单价
            String pricePay = ConvertUtil.getString(detailDTO.get("agioPrice"));
            //商品总金额
//          String amount = ConvertUtil.getString(detailDTO.get("amount"));
            //商品让利金额
            String discountAmount = getFormatDiscount(ConvertUtil.getString(detailDTO.get("discountFee")));
            if(discountAmount.equals("")){
                discountAmount = "0.00";
            }
            //销售单价
            esOrderDetailMap.put("PricePay",pricePay);
            detailDTO.put("PricePay", esOrderDetailMap.get("PricePay"));
            //应付金额 定价*数量
            String payableAmount = df.format(new BigDecimal(price).multiply(new BigDecimal(orderCount)));
            esOrderDetailMap.put("PayableAmount",payableAmount);
            //折扣率
            esOrderDetailMap.put("Discount",getFormatDiscount(detailDTO.get("discountRate")));
            //让利金额
            esOrderDetailMap.put("AgioAmount",discountAmount);
            //实付金额 应付金额-让利金额
            String actualAmount = df.format(new BigDecimal(payableAmount).subtract(new BigDecimal(discountAmount)));
            esOrderDetailMap.put("ActualAmount",actualAmount);
            //分摊后金额
            esOrderDetailMap.put("AmountPortion","");
            //逻辑仓库代码----根据产品的类型，产品使用正品仓，促销品使用促销品仓
            esOrderDetailMap.put("InventoryTypeCode",detailInventoryTypeCode);
            //批号或其它
            esOrderDetailMap.put("BatchCode","");
            //唯一码
            esOrderDetailMap.put("UniqueCode","");
            //销售类型 正常销售为N，促销为P。
            esOrderDetailMap.put("SaleType",basicDetailMap.get("SaleType"));
            //促销活动主码 如果参与了活动要和POS系统对接，需要填写POS系统中的活动代码
//            esOrderDetailMap.put("ActivityMainCode","");
            String activityMainCode = ConvertUtil.getString(detailDTO.get("ActivityMainCode"));
            esOrderDetailMap.put("ActivityMainCode","".equals(activityMainCode) ? "" : activityMainCode);
            //活动代码
//            esOrderDetailMap.put("ActivityCode","");
            String activityCode = ConvertUtil.getString(detailDTO.get("ActivityCode"));
            esOrderDetailMap.put("ActivityCode","".equals(activityCode) ? "" : activityCode);
            //备注
            esOrderDetailMap.put("Comment",detailDTO.get("memo"));
            
            // 产品是否存在于新后台通过相关条件查询订单明细中的产品是否在新后台存在 ( 不存在：0 、存在：1)
            if("".equals(ConvertUtil.getString(basicDetailMap.get("BIN_ProductVendorID")))){
            	esOrderDetailMap.put("IsExistsPos","0");
            }else{
            	esOrderDetailMap.put("IsExistsPos","1");
            }
            
            // 电商商品名称
            esOrderDetailMap.put("EsProductName",detailDTO.get("productName"));
            // 电商商品标题
            esOrderDetailMap.put("EsProductTitleName",detailDTO.get("platTitle"));
            //电商产品对应关系改变标识 0：异常，1：正常
            esOrderDetailMap.put("isRelationChange", basicDetailMap.get("isRelationChange"));
            
            setInsertInfoMapKey(esOrderDetailMap);
            
            if(!ConvertUtil.getString(detailDTO.get("isSmartPrmFlag")).equals("")){
            	// 智能促销返回的商品明细不写入电商明细表
            }else{
            	// 非智能促销返回的商品明细写入电商明细表
            	esOrderDetail.add(esOrderDetailMap);
            }
        }
        
        saleInfoService.insertESOrderDetail(esOrderDetail);
        return result;
    }
    
    /**
     * 插入支付方式表
     * @param paramMap
     * @param payDetailList
     * @return
     */
    private Map<String,Object> insertESOrderPayList(Map<String,Object> paramMap,List<Map<String,Object>> payDetailList){
        List<Map<String,Object>> esOrderPayList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<payDetailList.size();i++){
            Map<String,Object> payDetailDTO = payDetailList.get(i);
            payDetailDTO.put("BIN_ESOrderMainID",paramMap.get("BIN_ESOrderMainID"));
            payDetailDTO.put("DetailNo",i+1);
            payDetailDTO.put("SerialNumber","");
            payDetailDTO.put("Comment","");
            setInsertInfoMapKey(payDetailDTO);
            esOrderPayList.add(payDetailDTO);
        }
        saleInfoService.insertESOrderPayList(esOrderPayList);
        return null;
    }
    
    /**
     * 校验接口信息是否存在
     * @param dataMap
     * @param tradeCode
     * @param fieldName
     * @throws CherryBatchException
     */
    private void checkESInterfaceInfo(Map<String,Object> dataMap,String tradeCode,String fieldName) throws CherryBatchException{
        if(tradeCode.equals("GetOrderMain")){
            //GetOrderMain以List形式存在，遍历判断
            List<Map<String,Object>> getOrderMain = (List<Map<String, Object>>) dataMap.get("GetOrderMain");
            if(null == getOrderMain || getOrderMain.size() == 0){
                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                batchExceptionDTO.setBatchName(this.getClass());
                batchExceptionDTO.setErrorCode("EOT00043");
                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                batchExceptionDTO.addErrorParam("GetOrderMain");
                throw new CherryBatchException(batchExceptionDTO);
            }
            for(int i=0;i<getOrderMain.size();i++){
                String value = ConvertUtil.getString(getOrderMain.get(i).get(fieldName));
                if(value.equals("")){
                    BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                    batchExceptionDTO.setBatchName(this.getClass());
                    batchExceptionDTO.setErrorCode("EOT00044");
                    batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                    batchExceptionDTO.addErrorParam(tradeCode);
                    batchExceptionDTO.addErrorParam(fieldName);
                    throw new CherryBatchException(batchExceptionDTO);
                }
            }
        }else{
            Map<String,Object> tradeMap = (Map<String, Object>) dataMap.get(tradeCode);
            if(null == tradeMap || tradeMap.isEmpty()){
                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                batchExceptionDTO.setBatchName(this.getClass());
                batchExceptionDTO.setErrorCode("EOT00043");
                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                batchExceptionDTO.addErrorParam("GetOrderMain");
                throw new CherryBatchException(batchExceptionDTO);
            }
            String value = ConvertUtil.getString(tradeMap.get(fieldName));
            if(value.equals("")){
                BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
                batchExceptionDTO.setBatchName(this.getClass());
                batchExceptionDTO.setErrorCode("EOT00044");
                batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
                batchExceptionDTO.addErrorParam(tradeCode);
                batchExceptionDTO.addErrorParam(fieldName);
                throw new CherryBatchException(batchExceptionDTO);
            }
        }
    }
    
    /**
     * 取接口信息
     * @param paramMap
     * @return
     * @throws CherryBatchException
     */
    private Map<String, Object> getESInterfaceInfo(Map<String, Object> paramMap) throws CherryBatchException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 查询电商接口信息表
        Map<String, Object> paramESInterfaceInfo = new HashMap<String, Object>();
        paramESInterfaceInfo.put("BIN_OrganizationInfoID", paramMap.get(CherryBatchConstants.ORGANIZATIONINFOID));
        paramESInterfaceInfo.put("BIN_BrandInfoID", paramMap.get(CherryBatchConstants.BRANDINFOID));
        paramESInterfaceInfo.put("ESCode", "hongwei");
        List<Map<String, Object>> esInterfaceInfoList = binOTHONG01_Service.getESInterfaceInfo(paramESInterfaceInfo);
        // 判断TradeCode是否存在GetOrderMain:订单主单查询 GetOrderDetail:订单明细查询，不存在抛错
        if (null == esInterfaceInfoList || esInterfaceInfoList.size() == 0) {
            BatchExceptionDTO batchExceptionDTO = new BatchExceptionDTO();
            batchExceptionDTO.setBatchName(this.getClass());
            batchExceptionDTO.setErrorCode("EOT00042");
            batchExceptionDTO.setErrorLevel(CherryBatchConstants.LOGGER_ERROR);
            throw new CherryBatchException(batchExceptionDTO);
        }
        for (int i = 0; i < esInterfaceInfoList.size(); i++) {
            Map<String, Object> temp = esInterfaceInfoList.get(i);
            String tradeCode = ConvertUtil.getString(temp.get("TradeCode"));
            if (tradeCode.equals("GetOrderMain")) {
                //GetOrderMain支持访问多个店铺
                List<Map<String,Object>> getOrderMainList = (List<Map<String, Object>>) resultMap.get("GetOrderMain");
                if(null == getOrderMainList){
                    getOrderMainList = new ArrayList<Map<String,Object>>();
                }
                getOrderMainList.add(temp);
                resultMap.put("GetOrderMain", getOrderMainList);
            } else if (tradeCode.equals("GetOrderDetail")) {
                resultMap.put("GetOrderDetail", temp);
            }
        }

        checkESInterfaceInfo(resultMap, "GetOrderMain", "AccountName");
        checkESInterfaceInfo(resultMap, "GetOrderMain", "AccountPWD");
        checkESInterfaceInfo(resultMap, "GetOrderMain", "URL");
        checkESInterfaceInfo(resultMap, "GetOrderMain", "MethodName");
        checkESInterfaceInfo(resultMap, "GetOrderMain", "LastAccessTime");
        checkESInterfaceInfo(resultMap, "GetOrderMain", "GetDataEndTime");
        checkESInterfaceInfo(resultMap, "GetOrderMain", "TimeStep");

        checkESInterfaceInfo(resultMap, "GetOrderDetail", "AccountName");
        checkESInterfaceInfo(resultMap, "GetOrderDetail", "AccountPWD");
        checkESInterfaceInfo(resultMap, "GetOrderDetail", "URL");
        checkESInterfaceInfo(resultMap, "GetOrderDetail", "MethodName");

        return resultMap;
    }
    
    /**
     * 访问其他系统的WebService得到订单明细
     * @param paramMap
     * @return
     * @throws UnsupportedEncodingException 
     * @throws Exception 
     */
    private Map<String, Object> erpOrderDetail(Map<String, Object> paramMap) throws Exception {
        String webServiceUrl = ConvertUtil.getString(paramMap.get("URL"));
        String method = ConvertUtil.getString(paramMap.get("MethodName"));
        String nick= ConvertUtil.getString(paramMap.get("AccountName"));
        String name = ConvertUtil.getString(paramMap.get("AccountPWD"));
        String orderNumber = ConvertUtil.getString(paramMap.get("orderNumber"));
        String isLog = "0";//是否获取订单操作日志 1:获取,0:不获取
        String isInvoice = "0";//是否获取发票信息 1:获取,0:不获取
        String format = "json";
        
        WebResource webResource = WebserviceClient.getWebResource(webServiceUrl);
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        queryParams.add("orderNumber", orderNumber);
        queryParams.add("name", name);
        queryParams.add("isLog", isLog);
        queryParams.add("isInvoice", isInvoice);
        queryParams.add("nick", nick);
        queryParams.add("name", name);
        queryParams.add("method", method);
        queryParams.add("format", format);
        String timestamp = getTimeStamp();
        queryParams.add("timestamp", getTimeStamp());
        String sign= getSign(nick,method,format,timestamp);
        queryParams.add("sign", sign);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String result = webResource.queryParams(queryParams).get(String.class);
        
        resultMap = CherryUtil.json2Map(result);
        resultMap.put("GetOrderDetail_result", result);
        // 调用参数，用于在报错时，给到调用参数
        resultMap.put("queryParams", "orderNumber=["+orderNumber+"],name=["+name+"],isLog=["+isLog+"],isInvoice=["+isInvoice+
        		"],nick=["+nick+"],name=["+name+"],method=["+method+"],format=["+format+"]");
        return resultMap;
    }
    
    /**
     * 从明细取出总数量
     * @param detailList
     * @return
     */
    private int getTotalQuantity(List<Map<String,Object>> detailList){
        int totalQuantity = 0;
        if(null == detailList || detailList.size() == 0){
            return totalQuantity;
        }
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> tempMap = detailList.get(i);
            int quantity = CherryUtil.obj2int(tempMap.get("orderCount"));
            int giftCount = CherryUtil.obj2int(tempMap.get("giftCount"));
            totalQuantity += quantity+giftCount;
        }
        return totalQuantity;
    }
    
    /**
     * 从明细取出总数量(剔除促销品)
     * @param newDetailList
     * @return
     */
    private int getIgnorePrmTotalQuantity(List<Map<String,Object>> newDetailList){
        int totalQuantity = 0;
        if(null == newDetailList || newDetailList.size() == 0){
            return totalQuantity;
        }
        for(int i=0;i<newDetailList.size();i++){
            Map<String,Object> tempMap = newDetailList.get(i);
            
            String saleType = ConvertUtil.getString(tempMap.get("SaleType"));
            if(saleType.equals("N")){
            	int quantity = CherryUtil.obj2int(tempMap.get("orderCount"));
            	int giftCount = CherryUtil.obj2int(tempMap.get("giftCount"));
            	totalQuantity += quantity+giftCount;
            }
        }
        return totalQuantity;
    }
    
    /**
     * 取得时间戳
     * @return
     */
    private String getTimeStamp(){
        return  ConvertUtil.getString(new Date().getTime()/1000);
    }
    
    /**
     * 设置sign（取nick、method、format、timestamp各自的BASE64加起来的MD5值）
     * @param queryParams
     * @throws Exception
     */
    private String getSign(String nick,String method,String format,String timestamp) throws Exception{
        String base64nick = Base64.encode(nick.getBytes("UTF-8"));
        String base64method = Base64.encode(method.getBytes("UTF-8"));
        String base64format = Base64.encode(format.getBytes("UTF-8"));
        String base64timeStamp = Base64.encode(timestamp.getBytes("UTF-8"));
        String sign = CherryMD5Coder.encryptMD5(base64nick+base64method+base64format+base64timeStamp);
        return sign;
    }
    
    /**
     * 订单状态： 　　　　
     * NO_PAY：未付款(1)；CUSTOMER_AUDIT：客审(2);FINANCIAL_AUDIT：财审 (2) 　　　　
     * PRINT：打印(2) ;DISTRIBUTION：配货 (2) ;WAREHOUSING：出库 (3);
     * ON_THE_WAY：途中 (3)；SETTLEMENT：结算(3) ；SUCCESS：成功 (4)；
     * CANCEL：取消 (0)；OUT_OF_STOCK：缺货(2) ；UNDEFIND：未定义状态("")；
     * 
     * 1:生成；2：付款；3：发货；4：完成；0：取消
     * 
     * 取出最终写入数据库的单据状态
     * @param orderStatus
     * @return
     */
    private String getBillStateByOrderStatus(String orderStatus) {
        // 电商定义的订单状态
        // NO_PAY：未付款
        // CUSTOMER_AUDIT：客审
        // FINANCIAL_AUDIT：财审
        // PRINT：打印
        // DISTRIBUTION：配货
        // WAREHOUSING：出库
        // ON_THE_WAY：途中
        // SETTLEMENT：结算
        // SUCCESS：成功
        // CANCEL：取消
        // OUT_OF_STOCK：缺货
        // UNDEFIND：未定义状态
        String billState = "";
        if (orderStatus.equals("NO_PAY")) {
            billState = "1";// 生成：1
        } else if (orderStatus.equals("CUSTOMER_AUDIT") || orderStatus.equals("FINANCIAL_AUDIT")
                || orderStatus.equals("PRINT") || orderStatus.equals("DISTRIBUTION")
                || orderStatus.equals("OUT_OF_STOCK")) {
            billState = "2";// 付款：2
        } else if (orderStatus.equals("WAREHOUSING") || orderStatus.equals("ON_THE_WAY")
                || orderStatus.equals("SETTLEMENT")) {
            billState = "3";// 发货：3
        } else if (orderStatus.equals("SUCCESS")) {
            billState = "4";// 完成：4
        } else if (orderStatus.equals("CANCEL")) {
            billState = "0";// 订单取消：0
        }
        return billState;
    }
    
    /**
     * 把中文的来源转成定义的英文名
     * 对于天猫订单，根据店铺名取不同来源
     * @param name
     * @return
     */
    private String getDataSourceByName(String name,String shopName){
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
     * 把可能以科学计数法表示的折扣率转成小数点
     * @param obj
     * @return
     */
    private String getFormatDiscount(Object obj){
        String discount = ConvertUtil.getString(obj);
        if(!discount.equals("")){
            discount = new DecimalFormat("0.0000").format(Double.parseDouble(discount));
        }
        return discount;
    }
    
    private void setInsertInfoMapKey(Map<String,Object> map){
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
     * 输出处理结果信息
     * 
     * @throws CherryBatchException
     */
    private void outMessage() throws CherryBatchException {
        // 总件数
        BatchLoggerDTO batchLoggerDTO1 = new BatchLoggerDTO();
        batchLoggerDTO1.setCode("IIF00001");
        batchLoggerDTO1.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO1.addParam(String.valueOf(totalCount));
        // 成功总件数
        BatchLoggerDTO batchLoggerDTO2 = new BatchLoggerDTO();
        batchLoggerDTO2.setCode("IIF00002");
        batchLoggerDTO2.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO2.addParam(String.valueOf(totalCount - failCount));
        // 失败件数
        BatchLoggerDTO batchLoggerDTO5 = new BatchLoggerDTO();
        batchLoggerDTO5.setCode("IIF00005");
        batchLoggerDTO5.setLevel(CherryBatchConstants.LOGGER_INFO);
        batchLoggerDTO5.addParam(String.valueOf(failCount));
        // 处理总件数
        logger.BatchLogger(batchLoggerDTO1);
        // 成功总件数
        logger.BatchLogger(batchLoggerDTO2);
        // 失败件数
        logger.BatchLogger(batchLoggerDTO5);
    }
    
    @Override
    public int tran_batchOTHONG(Map<String, Object> map) throws Exception{
    	
        Map<String,Object> paramMap2 = new HashMap<String,Object>();
        paramMap2.putAll(map);
        
        String lastAccessTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
        //调用第三方WebService取得订单数据
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        Map<String,Object> resultMap = searchOrder(paramMap);
        if(null != resultMap && resultMap.get("BatchResult").equals(CherryBatchConstants.BATCH_SUCCESS)){
            flag = CherryBatchConstants.BATCH_SUCCESS;
            
            Map<String,Object> esInterfaceInfo = (Map<String, Object>) paramMap.get("ESInterfaceInfo");
            List<Map<String,Object>> orderMainIFInfos = (List<Map<String,Object>>)esInterfaceInfo.get("GetOrderMain");
            for(int i=0;i<orderMainIFInfos.size();i++){
                Map<String,Object> orderMainIFInfo = orderMainIFInfos.get(i);
                String esInterfaceInfoID = ConvertUtil.getString(orderMainIFInfo.get("BIN_ESInterfaceInfoID"));
                //处理完毕，更新电商接口信息表的最后一次访问时间，最后一次获取数据的截止时间
                Map<String,Object> updateESInterfaceInfo = new HashMap<String,Object>();
                updateESInterfaceInfo.put("BIN_OrganizationInfoID", map.get(CherryBatchConstants.ORGANIZATIONINFOID));
                updateESInterfaceInfo.put("BIN_BrandInfoID", map.get(CherryBatchConstants.BRANDINFOID));
                updateESInterfaceInfo.put("BIN_ESInterfaceInfoID", esInterfaceInfoID);
                //最后一次调用该接口的时间，是从JAVA中获取后写入该字段，不是在SQL中直接用GETDATE()
                updateESInterfaceInfo.put("LastAccessTime", lastAccessTime);
                //在以起止时间为参数获取数据时，将截止时间记入该字段，下次从该时间起再获取新的数据
                updateESInterfaceInfo.put("GetDataEndTime", resultMap.get(esInterfaceInfoID+"_GetDataEndTime"));
                setInsertInfoMapKey(updateESInterfaceInfo);
                binOTHONG01_Service.updateESInterfaceInfoLastTime(updateESInterfaceInfo);
            }
        }else{
            flag = CherryBatchConstants.BATCH_ERROR;
        }
        
        // 打印日志
        outMessage();
        
        return flag;
    }
}
