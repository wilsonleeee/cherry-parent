/*  
 * @(#)BINBEMQMES02_BL.java     1.0 2011/05/31      
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
package com.cherry.mq.mes.bl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM01_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM18_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM22_IF;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.interfaces.AnalyzeMessage_IF;
import com.cherry.mq.mes.service.BINBEMQMES02_Service;
import com.cherry.mq.mes.service.BINBEMQMES99_Service;
import com.cherry.st.common.bl.BINOLSTCM07_BL;
import com.cherry.st.common.interfaces.BINOLSTCM00_IF;
import com.cherry.st.common.interfaces.BINOLSTCM01_IF;
import com.cherry.st.common.interfaces.BINOLSTCM02_IF;
import com.cherry.st.common.interfaces.BINOLSTCM03_IF;
import com.cherry.st.common.interfaces.BINOLSTCM04_IF;
import com.cherry.st.common.interfaces.BINOLSTCM06_IF;
import com.cherry.st.common.interfaces.BINOLSTCM08_IF;
import com.cherry.st.common.interfaces.BINOLSTCM09_IF;
import com.cherry.st.common.interfaces.BINOLSTCM13_IF;
import com.cherry.st.common.interfaces.BINOLSTCM14_IF;
import com.cherry.st.common.interfaces.BINOLSTCM16_IF;
import com.cherry.st.common.interfaces.BINOLSTCM21_IF;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
/**
 * 产品消息数据接收处理BL
 * 
 * @author huzude
 * 
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES02_BL implements AnalyzeMessage_IF{   
    @Resource(name="workflow")
    private Workflow workflow;
    
    @Resource(name="binOLCM01_BL")
    private BINOLCM01_BL binOLCM01_BL;
    
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name="binOLCM18_BL")
	private BINOLCM18_IF binOLCM18_BL;
	
	@Resource(name="binOLCM19_BL")
	private BINOLCM19_IF binOLCM19_BL;
	
	@Resource(name="binOLCM22_BL")
	private BINOLCM22_IF binOLCM22_BL;
	
	@Resource(name="binOLSTCM02_BL")
	private BINOLSTCM02_IF binOLSTCM02_BL;
	
	@Resource(name="binOLSTCM00_BL")
	private BINOLSTCM00_IF binOLSTCM00_BL;
	
	@Resource(name="binOLSTCM01_BL")
	private BINOLSTCM01_IF binOLSTCM01_BL;
	
	@Resource(name="binOLSTCM03_BL")
	private BINOLSTCM03_IF binOLSTCM03_BL;
	
	@Resource(name="binOLSTCM04_BL")
	private BINOLSTCM04_IF binOLSTCM04_BL;
	
	@Resource(name="binOLSTCM08_BL")
	private BINOLSTCM08_IF binOLSTCM08_BL;
	
	@Resource(name="binOLSTCM09_BL")
	private BINOLSTCM09_IF binOLSTCM09_BL;
	
	@Resource(name="binBEMQMES02_Service")
	private BINBEMQMES02_Service binBEMQMES02_Service;

	@Resource(name="binBEMQMES99_Service")
	private BINBEMQMES99_Service binBEMQMES99_Service;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLSTCM13_BL")
	private BINOLSTCM13_IF binOLSTCM13_BL;
	
	@Resource(name="binOLSTCM06_BL")
	private BINOLSTCM06_IF binOLSTCM06_BL;
	
	@Resource(name="binOLSTCM14_BL")
	private BINOLSTCM14_IF binOLSTCM14_BL;
	
    @Resource(name="binOLSTCM16_BL")
    private BINOLSTCM16_IF binOLSTCM16_BL;
	
    @Resource(name="binOLSTCM21_BL")
    private BINOLSTCM21_IF binOLSTCM21_BL;
    
	@Resource(name="CodeTable")
	private CodeTable code;
	
	@Resource(name="binBEMQMES97_BL")
	private BINBEMQMES97_BL binBEMQMES97_BL;
	
    @Resource(name="binOLSTCM07_BL")
    private BINOLSTCM07_BL binOLSTCM07_BL;
    
	private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES02_BL.class);
	
	/**
	 * 对销售/退货数据进行处理
	 * 
	 * @param map
	 * @throws Exception 
	 * @throws Exception 
	 */
	public void analyzeSaleData(Map<String, Object> map) throws Exception   {
		// 判断单据类型是否为退货单据
		if (map.get("saleSRtype")!=null){
			if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
				// 该单据为销售单据
				map.put("cherry_tradeType", MessageConstants.BUSINESS_TYPE_NS);
				// 交易类型
				map.put("saleType", "NS");
			}else{
				// 该单据为退货单据
				map.put("cherry_tradeType", MessageConstants.BUSINESS_TYPE_SR);
				// 交易类型
				map.put("saleType", "SR");
			}
		}
		
		// 设定交易时间
		map.put("saleTime", map.get("tradeDate")+" "+map.get("tradeTime"));
		// 判断单据类型是否为修改销售单据
		if (null!=map.get("ticket_type") && map.get("ticket_type").equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
			// 修改销售单据的类型
			
			// 查询销售主表的数据
			Map historySaleMap =  binBEMQMES02_Service.selSaleRecord(map);
			//
			if(historySaleMap==null){
				//销售/退货数据接收未成功时，却又进行修改销售操作
				MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_33);
			}
			// 设定新后台单据号
			map.put("cherry_no", historySaleMap.get("cherry_no"));
			// 查询销售明细表的数据
			List historySaleDetailList = binBEMQMES02_Service.selSaleRecordDetail(historySaleMap);
			// 插入历史销售主表
			int saleRecordHistoryID = binBEMQMES02_Service.addSaleRecordHistory(historySaleMap);
			for (int i=0;i<historySaleDetailList.size();i++){
				Map historySaleDetailMap = (Map)historySaleDetailList.get(i);
				// 设置历史销售主表主ID
				historySaleDetailMap.put("saleRecordHistoryID", saleRecordHistoryID);
			}
			// 插入历史销售明细表
			binBEMQMES02_Service.addSaleDetailHistory(historySaleDetailList);
			
			// 删除原有的销售记录
			binBEMQMES02_Service.delSaleRecord(historySaleMap);
			binBEMQMES02_Service.delSaleRecordDetail(historySaleMap);
		}else{
			// 单据号采番
			//this.getMQTicketNumber(map,"");
			map.put("cherry_no", map.get("tradeNoIF"));
		}
		
		// 插入销售数据主表
		int saleRecordID = binBEMQMES02_Service.addSaleRecord(map);
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");
		for (int i=0;i<detailDataList.size();i++){
			Map detailDataMap = (HashMap)detailDataList.get(i);
			// 设定销售主表ID
			detailDataMap.put("saleRecordID", saleRecordID);
			
		}
		HashMap cloneMap = (HashMap)ConvertUtil.byteClone(map); 
		this.delOppositeData(cloneMap, (List)cloneMap.get("detailDataDTOList"));
		// 插入销售数据明细表
		binBEMQMES02_Service.addSaleRecordDetail((List)cloneMap.get("detailDataDTOList"));
	}

    /**
     * 对销售/退货数据进行处理(新消息体Type=0007)
     * 
     * @param map
     * @throws Exception 
     */
    public void analyzeSaleReturnData(Map<String, Object> map) throws Exception   {
        // 判断单据类型是否为退货单据
        if (map.get("saleSRtype")!=null){
            if (MessageConstants.SR_TYPE_SALE.equals((String)map.get("saleSRtype"))){
                // 该单据为销售单据
                map.put("cherry_tradeType", MessageConstants.BUSINESS_TYPE_NS);
                // 交易类型
                map.put("saleType", "NS");
            }else{
                // 该单据为退货单据
                map.put("cherry_tradeType", MessageConstants.BUSINESS_TYPE_SR);
                // 交易类型
                map.put("saleType", "SR");
                
                //先收到退货单，关联单号不为空，查不到原单时报错
                String relevantNo = ConvertUtil.getString(map.get("relevantNo"));
                if(!"".equals(relevantNo)){
                    Map<String,Object> saleParamMap = new HashMap<String,Object>();
                    saleParamMap.put("tradeNoIF", relevantNo);
                    Map<String,Object> saleRecord = binBEMQMES02_Service.selSaleRecord(saleParamMap);
                    if(null == saleRecord || saleRecord.isEmpty()){
                        MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_64);
                    }
                }
                //当退货申请单号不为空时，查询此单号是否存在
                String saleReturnRequestCode = ConvertUtil.getString(map.get("saleReturnRequestCode"));
                if (!"".equals(saleReturnRequestCode)){
                	 Map<String,Object> saleRetrunParamMap = new HashMap<String,Object>();
                	 saleRetrunParamMap.put("tradeNoIF", saleReturnRequestCode);
                	  Map<String,Object> SaleReturnRequestInfo = binBEMQMES02_Service.SaleReturnRequestInfo(saleRetrunParamMap);
                	  if(null == SaleReturnRequestInfo || SaleReturnRequestInfo.isEmpty()){
                          MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_91+saleReturnRequestCode);
                      }else{
                    	  //存在此单号时，结束退货申请的工作流
                          Map<String,Object> param = new HashMap<String,Object>();
                          param.put("WorkFlowID", ConvertUtil.getString(SaleReturnRequestInfo.get("WorkFlowID")));
                          param.put("BIN_SaleReturnRequestID", ConvertUtil.getString(SaleReturnRequestInfo.get("BIN_SaleReturnRequestID")));
                          param.put("TradeDateTime", map.get("tradeDateTime"));
                          binOLSTCM21_BL.posConfirmReturnFinishFlow(param);
                      }
                }
            }
        }
        
        String relevantNo = ConvertUtil.getString(map.get("relevantNo"));
        String billModel = ConvertUtil.getString(map.get("billModel"));
        
        //设定销售单状态
        //TotalAmountBefore   入出库前柜台库存总金额     20140813:该字段一直未被使用，现在用来存放销售单状态。
        map.put("billState", map.get("totalAmountBefore"));
        
        if(!relevantNo.equals("") && (billModel.equals("2")  || billModel.equals("3"))){
            //如果来自电商订单的销售MQ，已线下取货 ，但没有billState，状态变成4
            map.put("billState", "4");
        }
        
        // 设定交易时间
        map.put("saleTime", map.get("tradeDate")+" "+map.get("tradeTime"));
        
        //判断明细全部都是对冲数据
        boolean allOppositeFlag = true;
        String mainModifyCounts = ConvertUtil.getString(map.get("modifyCounts"));
        List detailDataList = (List) map.get("detailDataDTOList");
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = (HashMap<String,Object>)detailDataList.get(i);
            //明细修改次数
            String deatilModifyCounts = ConvertUtil.getString(detailDataMap.get("modifyCounts"));
            if(mainModifyCounts.equals(deatilModifyCounts)){
                allOppositeFlag = false;
                break;
            }
        }
        
        //设置销售下单者的相关信息
        setSaleCreatorInfo(map);
        int clubId = 0;
        // 有俱乐部代号
        if (!CherryChecker.isNullOrEmpty(map.get("clubCode"))) {
        	clubId = binBEMQMES02_Service.selMemClubId(map);
        	// 查询会员俱乐部ID
        	map.put("memberClubId", clubId);
        }
        // 判断单据类型是否为修改销售单据
        if (null!=map.get("ticket_type") && map.get("ticket_type").equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
            // 修改销售单据的类型
            
            // 查询销售主表的数据
            Map historySaleMap =  binBEMQMES02_Service.selSaleRecord(map);
            //
            if(historySaleMap==null){
                //销售/退货数据接收未成功时，却又进行修改销售操作
                MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_33);
            }
            int oldClubId = 0;
            if (historySaleMap.get("memberClubId") != null) {
            	oldClubId = Integer.parseInt(String.valueOf(historySaleMap.get("memberClubId")));
            }
            if (0 != oldClubId && clubId != oldClubId) {
            	map.put("oldClubId", oldClubId);
            }
            // 设定新后台单据号
            map.put("cherry_no", historySaleMap.get("cherry_no"));
            // 查询销售明细表的数据
            List historySaleDetailList = binBEMQMES02_Service.selSaleRecordDetail(historySaleMap);
            // 插入历史销售主表
            int saleRecordHistoryID = binBEMQMES02_Service.addSaleRecordHistory(historySaleMap);
            for (int i=0;i<historySaleDetailList.size();i++){
                Map historySaleDetailMap = (Map)historySaleDetailList.get(i);
                // 设置历史销售主表主ID
                historySaleDetailMap.put("saleRecordHistoryID", saleRecordHistoryID);
            }
            // 插入历史销售明细表
            binBEMQMES02_Service.addSaleDetailHistory(historySaleDetailList);
            
            // 删除原有的销售记录
            binBEMQMES02_Service.delSaleRecord(historySaleMap);
            binBEMQMES02_Service.delSaleRecordDetail(historySaleMap);
            
            //删除会员参与活动履历
            Map<String,Object> campaignHistory = new HashMap<String,Object>();
            campaignHistory.put("TradeNoIF", map.get("tradeNoIF"));
            campaignHistory.put("OrgCode", map.get("orgCode"));
            campaignHistory.put("BrandCode", map.get("brandCode"));
            binBEMQMES02_Service.delCampaignHistory(campaignHistory);
            
            if(allOppositeFlag){
                map.put("totalQuantity", "0");
                map.put("totalAmount", "0.00"); 
            }
        }else{
            // 单据号采番
            //this.getMQTicketNumber(map,"");
        	map.put("cherry_no", map.get("tradeNoIF"));
        }
        
        // 当该值为"1"时，表示不处理库存 
        String stockFlag = ConvertUtil.getString(map.get("stockFlag"));
        if (stockFlag.equals("1")){
        	//不处理库存时将提货状态置9，表示全部未提货
        	map.put("pickupStatus", "9");
        }
       
        // 插入销售数据主表
        int saleRecordID = binBEMQMES02_Service.addSaleRecord(map);
        // 明细数据List
        for (int i=0;i<detailDataList.size();i++){
            Map detailDataMap = (HashMap)detailDataList.get(i);
            // 设定销售主表ID
            detailDataMap.put("saleRecordID", saleRecordID);
            
        }
        HashMap cloneMap = (HashMap)ConvertUtil.byteClone(map); 
//        this.delOppositeData(cloneMap, (List)cloneMap.get("detailDataDTOList"));
        
        String saleType = ConvertUtil.getString(map.get("saleType"));
        
        //分离销售数据明细、销售支付明细
        //排除对冲明细
        List<Map<String,Object>> detailList = (List<Map<String,Object>>)cloneMap.get("detailDataDTOList");
        List<Map<String,Object>> saleRecordDetailList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> salePayList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> campaignHistoryList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> giftDrawDetailList = new ArrayList<Map<String,Object>>();
        int detailNo = 1;
        Map<String,Object> memberAndMainCodeMap = new HashMap<String,Object>();
        //会员号与会员ID对应关系
        Map<String,Object> memberMap = new HashMap<String,Object>();
        //ActivityCode与campaignCode对应关系
        Map<String,Object> campaignCodeMap = new HashMap<String,Object>();
        List<String> orderIDList = new ArrayList<String>();
        
        Map<String,Object> campaignDataMap = new HashMap<String,Object>();
        campaignDataMap.put("map", map);
        campaignDataMap.put("memberAndMainCodeMap", memberAndMainCodeMap);
        campaignDataMap.put("campaignCodeMap", campaignCodeMap);
        campaignDataMap.put("campaignHistoryList", campaignHistoryList);
        campaignDataMap.put("memberMap", memberMap);
        campaignDataMap.put("allOppositeFlag", allOppositeFlag);
        
        //代理商优惠券数组
        List<String> baCouponList = new ArrayList<String>();
        
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = (Map<String,Object>)detailList.get(i);
            String detailType = ConvertUtil.getString(temp.get("detailType"));
            String deatilModifyCounts = ConvertUtil.getString(temp.get("modifyCounts"));
            String activityMainCode = ConvertUtil.getString(temp.get("activityMainCode"));
            String activityCode = ConvertUtil.getString(temp.get("activityCode"));
            //产品里也可能有mainCode、activityCode，所以从促销的逻辑里移出
            //mainCode、activityCode两个里面存在一个就写会员活动履历表
            if(!"".equals(activityMainCode) || !"".equals(activityCode)){
                campaignDataMap.put("activityCode", activityCode);
                campaignDataMap.put("activityMainCode", activityMainCode);
                campaignDataMap.put("detailDataMap", temp);
                setCampaignValue(campaignDataMap);
            }
            //OrderID不为空时加入礼品领用List
            String orderID = ConvertUtil.getString(temp.get("orderID"));
            if(mainModifyCounts.equals(deatilModifyCounts) && !"".equals(orderID)){
                giftDrawDetailList.add(temp);
                if(!orderIDList.contains(orderID)){
                    orderIDList.add(orderID);
                }
            }
            
            if(detailType.equals(MessageConstants.DETAILTYPE_BC)){
                String uniqueCode = ConvertUtil.getString(temp.get("uniqueCode"));
                if(!uniqueCode.equals("")){
                    baCouponList.add(uniqueCode);
                }
                temp.put("saleType", MessageConstants.DETAILTYPE_PROMOTION);
            }
            
            if(MessageConstants.DETAILTYPE_PAY.equals(detailType)){
                String payTypeID = ConvertUtil.getString(temp.get("payTypeID"));
                String payTypeCode = ConvertUtil.getString(temp.get("payTypeCode"));
                String data_source = ConvertUtil.getString(map.get("data_source"));
                if("POS2".equals(data_source) && "".equals(payTypeCode)){
                    //机器类型为POS2，且支付方式代号为空时，调用共通取出全部Codes值，
                    //根据orgCode、brandCode、codeType的组合取出Code值List，找不到组织、品牌的取-9999再次取，
                    //最后根据级别（payTypeID）取得对应的payTypeCode。
                    //这里取Key的逻辑和CodeTable.propKeyByGrade(String codeType,Object grade)一致，只是不使用session。
                    Map<String,Object> codesMap = code.getCodesMap();
                    String orgCode = ConvertUtil.getString(map.get("orgCode"));
                    String brandCode = ConvertUtil.getString(map.get("brandCode"));
                    String codeType = "1175";
                    String combKey = orgCode + "_" + brandCode + "_" + codeType;
                    if (!codesMap.containsKey(combKey)) {
                        // 组合key(品牌Code为全体共通)
                        combKey = orgCode + "_" + CherryConstants.Brand_CODE_ALL + "_" + codeType;
                        if (!codesMap.containsKey(combKey)) {
                            // 组合key(组织Code和品牌Code为全体共通)
                            combKey = CherryConstants.ORG_CODE_ALL + "_" + CherryConstants.Brand_CODE_ALL + "_" + codeType;
                        }
                    }
                    List<Map<String,Object>> codeList = (List<Map<String, Object>>) codesMap.get(combKey);
                    for(int j=0;j<codeList.size();j++){
                        Map<String,Object> codeMap = (Map<String,Object>) codeList.get(j);
                        if(payTypeID.equals(ConvertUtil.getString(codeMap.get("grade")))) {
                            // Key
                            Object codeKeyObj = codeMap.get("codeKey");
                            if (null != codeKeyObj) {
                                payTypeCode = String.valueOf(codeKeyObj);
                                break;
                            }
                        }
                    }
                    temp.put("payTypeCode", payTypeCode);
                }
                //设置支付方式明细的序号
                temp.put("detailNo", salePayList.size()+1);
                //当明细为支付方式时，字段CouponCode为支付方式的流水号
                temp.put("serialNumber", temp.get("couponCode"));
                this.setInsertInfoMapKey(temp);
                salePayList.add(temp);
            }else if(mainModifyCounts.equals(deatilModifyCounts) || allOppositeFlag){
                temp.put("detailNo", detailNo);
                
                //当明细全部是对冲数据时，插入数量为0的明细。
                if(allOppositeFlag){
                    temp.put("modifyCounts", mainModifyCounts);
                    temp.put("quantity", "0");
                }
                
                saleRecordDetailList.add(temp);
                detailNo ++;
            }
        }
        
        // 插入销售数据明细表
        binBEMQMES02_Service.addSaleRecordDetail(saleRecordDetailList);
        
        //插入销售支付构成表
        if(null != salePayList && !salePayList.isEmpty()){
            binBEMQMES02_Service.addSalePayList(salePayList);
        }
        
        if (MessageConstants.BUSINESS_TYPE_NS.equals(saleType)){
            //插入会员参与活动履历表（销售/修改销售）
            if(null != campaignHistoryList && !campaignHistoryList.isEmpty()){
                binBEMQMES02_Service.addCampaignHistory(campaignHistoryList);
            }
        }else if(MessageConstants.BUSINESS_TYPE_SR.equals(saleType)){
            //删除会员参与活动履历表（退货）
            if(null != campaignHistoryList){
                for(int i=0;i<campaignHistoryList.size();i++){
                    Map<String,Object> campaignHistory = new HashMap<String,Object>();
                    campaignHistory.put("TradeNoIF", map.get("relevantNo"));
                    campaignHistory.put("OrgCode", map.get("orgCode"));
                    campaignHistory.put("BrandCode", map.get("brandCode"));
                    campaignHistory.put("BIN_MemberInfoID", campaignHistoryList.get(i).get("BIN_MemberInfoID"));
                    campaignHistory.put("CampaignCode", campaignHistoryList.get(i).get("CampaignCode"));
                    campaignHistory.put("MainCode", campaignHistoryList.get(i).get("MainCode"));
                    binBEMQMES02_Service.delCampaignHistory(campaignHistory);
                }
            }
        }
        
        //退货时有关联单的情况下写入退货单号到销售单备注
        if(ConvertUtil.getString(map.get("cherry_tradeType")).equals(MessageConstants.BUSINESS_TYPE_SR) && !"".equals(ConvertUtil.getString(map.get("relevantNo")))){
            binBEMQMES02_Service.updSaleRecord(map);
        }
        
        //排除明细为支付方式
        detailList = (List<Map<String,Object>>)map.get("detailDataDTOList");
        for(int i=0;i<detailList.size();i++){
            Map<String,Object> temp = (Map<String,Object>)detailList.get(i);
            String detailType = ConvertUtil.getString(temp.get("detailType"));
            if(MessageConstants.DETAILTYPE_PAY.equals(detailType)){
                detailList.remove(i);
                i--;
                continue;
            }
        }

        //遍历orderIDList，取出相同orderID组成新list来处理礼品领用(一个消息体可能存在多个预约单号)
        if(null != giftDrawDetailList && giftDrawDetailList.size()>0 && null != orderIDList && orderIDList.size()>0){
            for(int i=0;i<orderIDList.size();i++){
                String orderID = ConvertUtil.getString(orderIDList.get(i));
                List<Map<String,Object>> subGiftDrawDetailList = new ArrayList<Map<String,Object>>();
                for(int j=0;j<giftDrawDetailList.size();j++){
                    String curOrderID = ConvertUtil.getString(giftDrawDetailList.get(j).get("orderID"));
                    if(orderID.equals(curOrderID)){
                        subGiftDrawDetailList.add(giftDrawDetailList.get(j));
                    }
                }
                operateNSGiftGraw(detailList,subGiftDrawDetailList,map);
            }
        }
        
        //更新代理商优惠券为已使用
        if(baCouponList.size()>0){
            Map<String,Object> baCouponMap = new HashMap<String,Object>();
            baCouponMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
            baCouponMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
            String[] baCouponArr = new String[baCouponList.size()];
            for(int i=0;i<baCouponList.size();i++){
                baCouponArr[i] = baCouponList.get(i);
            }
            baCouponMap.put("CouponCodeArr", baCouponArr);
            setInsertInfoMapKey(baCouponMap);
            binBEMQMES02_Service.updateBaCoupon(baCouponMap);
        }
        
        //更新电商订单的BillState为4已领用
        if(!relevantNo.equals("") && (billModel.equals("2")  || billModel.equals("3"))){
            Map<String,Object> updateESOrderMap = new HashMap<String,Object>();
            updateESOrderMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
            updateESOrderMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
            updateESOrderMap.put("BillCode", relevantNo);
            setInsertInfoMapKey(updateESOrderMap);
            binBEMQMES02_Service.updateESOrderMainBillState(updateESOrderMap);
        }
    }
	
	/**
	 * 对调入申请单进行处理
	 * 
	 * @param map
	 * @throws Exception 
	 */
	@Override
	public void analyzeAllocationInData(Map<String, Object> map) throws Exception {
        //String workFlowFlag = ConvertUtil.getString(map.get("workFlow"));
	    String workFlowFlag = "false";
	    //从工作流判断是否需要走工作流(SupportCounterWF=YES时才有效)
	    String organizationInfoCode = ConvertUtil.getString(map.get("orgCode"));
	    String brandCode = ConvertUtil.getString(map.get("brandCode"));
        String workFlowName = ConvertUtil.getWfName(organizationInfoCode, brandCode, "proFlowAC");
        WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workFlowName);
        Map<String, Object> metaAttributes = wd.getInitialAction(1).getMetaAttributes();
        if (null != metaAttributes && !metaAttributes.isEmpty()) {
            String supportCounterWF = ConvertUtil.getString(metaAttributes.get("SupportCounterWF"));
            if(supportCounterWF.equals("YES")){
                workFlowFlag = "true";
            }
        }
        
        if (workFlowFlag.equals("true")) {
            // 消息体带有workFlow=true，启动工作流
            allocationInOSWF(map);
            return;
        }
		map.put("cherry_tradeType", MessageConstants.BUSINESS_TYPE_BG);
		// 单据号采番
		//this.getMQTicketNumber(map,"");
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");
		Object tradeNoIF = null;
		if(map.get("tradeNoIF")==null){
			// 单据号采番
			this.getMQTicketNumber(map,"");
			tradeNoIF = map.get("cherry_no");
			map.put("tradeNoIF", String.valueOf(tradeNoIF));
		}else{
			map.put("cherry_no", map.get("tradeNoIF"));
		}

		// 插入产品调拨申请单据表
        map.put("organizationIDDX", map.get("organizationID"));
        map.put("employeeIDDX", map.get("employeeID"));
		int promotionAllocationID = binBEMQMES02_Service.addProductAllocation(map);
		// 循环明细数据List
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			// 设定产品调拨ID
			detailDataDTOMap.put("productAllocationID", promotionAllocationID);
		}
		// 批量插入产品调拨申请单据明细表
		binBEMQMES02_Service.addProductAllocationDetail(detailDataList);
	}

	/**
	 * 对调出确认单进行处理
	 * 
	 * @param map
	 */
	public void analyzeAllocationOutData(Map<String, Object> map) throws Exception {
        //判断关联的调入申请单号是否在新后台存在，否则抛错
        String relevantNo = ConvertUtil.getString(map.get("relevantNo"));
        String workflowid = "";
        List<Map<String,Object>> selProductAllocationList = binBEMQMES02_Service.selProductAllocationList(map);
        if("".equals(relevantNo) || null == selProductAllocationList || selProductAllocationList.size() == 0){
            MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_78+"\""+relevantNo+"\"");
        }else{
            workflowid = ConvertUtil.getString(selProductAllocationList.get(0).get("WorkFlowID"));
            map.put("WorkFlowID", workflowid);
        }
        if (!workflowid.equals("")) {
            //工作流ID不为空，启动工作流
            allocationOutOSWF(map);
            return;
        }
        
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");	
		
	    map.put("tradeStatus", "13");//已调入
	    //调出/拒绝调出标志 true 为调出，false为拒绝调出
	    boolean allocationOutFlag = false;
	    //明细都为0，状态改为已拒绝
	    for(int i=0;i<detailDataList.size();i++){
	        Map<String,Object> detailDTO = (Map<String, Object>) detailDataList.get(i);
	        if(!ConvertUtil.getString(detailDTO.get("quantity")).equals("0")){
	            allocationOutFlag = true;
	            break;
	        }
	    }
	    if(!allocationOutFlag){
	        map.put("tradeStatus", "14");//已拒绝
	    }
	    
        //更新调入申请单状态
        Map<String,Object> updateMap = new HashMap<String,Object>();
        updateMap.put("AllocationNoIF", map.get("relevantNo"));
        updateMap.put("TradeStatus", map.get("tradeStatus"));
        this.setInsertInfoMapKey(updateMap);
        binBEMQMES02_Service.updateProductAllocation(updateMap);
		
		// 新后台业务类型
		map.put("cherry_tradeType",MessageConstants.BUSINESS_TYPE_LG);
		// 单据号采番
		//this.getMQTicketNumber(map,"");
		map.put("cherry_no", map.get("tradeNoIF"));
		// 插入产品调出确认单据表
        map.put("organizationIDDX", map.get("organizationID"));
        map.put("employeeIDDX", map.get("employeeID"));
		int productAllocationOutID = binBEMQMES02_Service.addProductAllocationOut(map);
		// 循环明细数据List
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			// 设定产品调拨ID
			detailDataDTOMap.put("productAllocationOutID", productAllocationOutID);
		}
		// 批量插入产品调出单据明细表
		binBEMQMES02_Service.addProductAllocationOutDetail(detailDataList);
		// 单据号采番
		//this.getMQTicketNumber(map,"");
		// 插入产品调入单据表
        map.put("organizationIDDX", map.get("organizationID"));
        map.put("employeeIDDX", map.get("employeeID"));
		int productAllocationInID = binBEMQMES02_Service.addProductAllocationIn(map);
		// 循环明细数据List
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			// 设定产品调拨ID
			detailDataDTOMap.put("productAllocationInID", productAllocationInID);
		}
		// 批量插入产品调入单据明细表
		binBEMQMES02_Service.addProductAllocationInDetail(detailDataList);
		
		// 操作调出方库存
		
		// 设定入出库关联单据号 (调出单单据号)
		map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
		map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
		// 插入入出库表(调出方)
		this.addProductStockInfo(detailDataList, map);
		
		// 设定调入方信息
		HashMap allocationInMap = (HashMap)ConvertUtil.byteClone(map);
		// 调入方部门ID
		allocationInMap.put("organizationID", allocationInMap.get("relevantOrganizationID"));
		
		List andetailDataList = (List)allocationInMap.get("detailDataDTOList");
		
		for (int i=0;i<andetailDataList.size();i++){
			HashMap andetailDataMap = (HashMap)andetailDataList.get(i);
			// 设定关联部门实体仓库ID
			if (andetailDataMap.get("relevantInventoryInfoID")!=null && !"".equals(andetailDataMap.get("relevantInventoryInfoID"))){
				andetailDataMap.put("inventoryInfoID", andetailDataMap.get("relevantInventoryInfoID"));
			}

			// 调入方库存类型为0(入库)
			andetailDataMap.put("stockType","0");
		}
		// 插入入出库表(调入方)
		allocationInMap.put("cherry_tradeType", MessageConstants.BUSINESS_TYPE_BG);
		allocationInMap.put("stockInOut_relevantNo", map.get("cherry_no"));//调入主表中的接口单据号
		allocationInMap.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
		this.addProductStockInfo(andetailDataList, allocationInMap);
		
		// 操作库存数据(调出方)
		this.operateProductStockData(detailDataList, map);
		
		// 操作库存数据(调入方)
		this.operateProductStockData(andetailDataList, allocationInMap);
		
	}
	
    /**
     * 对调入确认进行处理，仅在工作流里配置了调入步骤时才能接收
     * 
     * @param map
     * @throws Exception 
     */
    @Override
    public void analyzeAllocationInConfirmData(Map<String, Object> map) throws Exception {
        //判断调拨流程
        String tradeNoIF = ConvertUtil.getString(map.get("tradeNoIF"));
        List<Map<String,Object>> productAllocationOut = binOLSTCM16_BL.selProductAllocationOut(tradeNoIF);
        if(null != productAllocationOut && productAllocationOut.size()>0){
            String workFlowID = ConvertUtil.getString(productAllocationOut.get(0).get("WorkFlowID"));
            if(workFlowID.equals("")){
                MessageUtil.addMessageWarning(map,"执行调入确认时，工作流ID不能为空");
            }

            //调用工作流
            long osID = Long.parseLong(workFlowID);
            ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(osID);
            int actionID = 0;
            if(null != adArr && adArr.length>0){
                Map metaMapTMp = null;
                for (int j = 0; j < adArr.length; j++) {
                    metaMapTMp = adArr[j].getMetaAttributes();
                    //元数据找到带有OS_OperateCode为70（调入）的action
                    if(null != metaMapTMp && metaMapTMp.containsKey("OS_OperateCode")){
                        String operateCode = ConvertUtil.getString(metaMapTMp.get("OS_OperateCode"));
                        if("70".equals(operateCode)){
                            ActionDescriptor ad = adArr[j];
                            actionID = ad.getId();
                            break;
                        }
                    }
                }
                if(actionID == 0){
                    MessageUtil.addMessageWarning(map,"执行调入确认时，无法找到当前能执行Action");
                }
            }else{
                MessageUtil.addMessageWarning(map,"执行调入确认时，调用BINOLCM19_BL共通代码getCurrActionByOSID未查到当前能操作的步骤。"+
                        "涉及主要参数：工作流ID\""+osID+"\"");
            }
            
            UserInfo userInfo = new UserInfo();
            userInfo.setBIN_EmployeeID(CherryUtil.obj2int(map.get("employeeID")));

            //查询用户表获得用户ID
            Map userMap = binBEMQMES99_Service.selUserByEempID(map);
            String userID = null;
            if(null == userMap || null == userMap.get("userID")){
                userID = "-9998";
            }else{
                userID = ConvertUtil.getString(userMap.get("userID"));
            }
            
            String organizationIDIn = ConvertUtil.getString(map.get("organizationID"));
            String organizationIDOut = ConvertUtil.getString(map.get("relevantOrganizationID"));
            
            Map<String,Object> mainData_BC = new HashMap<String,Object>();
            mainData_BC.put("TradeType", CherryConstants.BUSINESS_TYPE_BG);
            mainData_BC.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
            mainData_BC.put("BIN_BrandInfoID", map.get("brandInfoID"));
            mainData_BC.put("AllocationInNoIF", map.get("tradeNoIF"));
            mainData_BC.put("RelevanceNo", map.get("tradeNoIF"));
            mainData_BC.put("BIN_OrganizationIDIn", organizationIDIn);
            mainData_BC.put("BIN_OrganizationIDOut", organizationIDOut);
            mainData_BC.put("BIN_OrganizationIDDX", organizationIDIn);
            mainData_BC.put("BIN_OrganizationIDOut", organizationIDOut);
            mainData_BC.put("BIN_EmployeeID", map.get("employeeID"));
            mainData_BC.put("BIN_EmployeeIDDX", map.get("employeeID"));
            mainData_BC.put("TotalQuantity", map.get("totalQuantity"));
            mainData_BC.put("TotalAmount", map.get("totalAmount"));
            mainData_BC.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
            mainData_BC.put("TradeStatus", "13");
            mainData_BC.put("Comments", map.get("reason"));
            mainData_BC.put("Date", map.get("tradeDate"));
            mainData_BC.put("WorkFlowID", workFlowID);
            mainData_BC.put("CreatedBy", userID);
            mainData_BC.put("CreatePGM", "BINBEMQMES02");
            mainData_BC.put("UpdatedBy", userID);
            mainData_BC.put("UpdatePGM", "BINBEMQMES02");
            
            List detailDataList = (List) map.get("detailDataDTOList");
            List<Map<String,Object>> detailList_BC = new ArrayList<Map<String,Object>>();
            for(int i=0;i<detailDataList.size();i++){
                Map<String,Object> tempDTO = (Map<String, Object>) detailDataList.get(i);
                Map<String,Object> detailDTO = new HashMap<String,Object>();
                detailDTO.put("BIN_ProductVendorID", tempDTO.get("productVendorID"));
                detailDTO.put("DetailNo", i+1);
                detailDTO.put("Quantity", tempDTO.get("quantity"));
                detailDTO.put("Price", tempDTO.get("price"));
                detailDTO.put("BIN_InventoryInfoID", map.get("inventoryInfoID"));
                detailDTO.put("BIN_LogicInventoryInfoID", tempDTO.get("logicInventoryInfoID"));
                detailDTO.put("Comments", tempDTO.get("reason"));
                detailDTO.put("CreatedBy", userID);
                detailDTO.put("CreatePGM", "BINBEMQMES02");
                detailDTO.put("UpdatedBy", userID);
                detailDTO.put("UpdatePGM", "BINBEMQMES02");
                
                detailList_BC.add(detailDTO);
            }
            
            Map<String,Object> mainDataMap = new HashMap<String,Object>();
            mainDataMap.put("entryID", osID);
            mainDataMap.put("actionID", actionID);
            mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));
            mainDataMap.put("BIN_EmployeeID", map.get("employeeID"));
            mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userID);
            mainDataMap.put("BrandCode", map.get("brandCode").toString());
            mainDataMap.put("CurrentUnit", "BINBEMQMES02");
            mainDataMap.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
            mainDataMap.put("UserInfo", userInfo);
            mainDataMap.put("OrganizationCode",map.get("counterCode"));//调入部门编号
            mainDataMap.put("MQ_TRADETYPE", MessageConstants.MSG_ALLOCATION_IN_CONFRIM);
            mainDataMap.put("MainData_BC", mainData_BC);
            mainDataMap.put("DetailData_BC", detailList_BC);
            binOLSTCM00_BL.DoAction(mainDataMap);
        }else{
            // 没有查询到相关仓库信息
            MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_81+"\""+tradeNoIF+"\"");
        }
    }

	/**
	 * 对销售/退货数据进行库存处理
	 * 
	 * @param map
	 * @throws Exception 
	 */
	@Override
	public void analyzeSaleStockData(Map<String, Object> map) throws Exception {
		List detailDataList = (List) map.get("detailDataDTOList");
		
		// 设定入出库关联单据号
		map.put("stockInOut_relevantNo", map.get("tradeNoIF"));

		// 设定入出库区分
		if (map.get("saleSRtype")!=null){
			if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
				map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
			}else{
				map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
				
			}
		}
		
		// 产品入出库处理
		this.addProductStockInfo(detailDataList, map);
		
		// 产品库存处理
		this.operateProductStockData(detailDataList, map);
		
	}

    /**
     * 对销售/退货数据进行库存处理(新消息体Type=0007)
     * 
     * @param map
     * @throws Exception 
     */
    @Override
    public void analyzeSaleReturnStockData(Map<String, Object> map) throws Exception {
        List<Map<String,Object>> detailDataList = (List<Map<String,Object>>)map.get("detailDataDTOList");
        
        // 设定入出库关联单据号
        map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
        // 设定入出库区分
        if (map.get("saleSRtype")!=null){
            if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
                map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
            }else{
                map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
                map.put("stockInOut_SrRelevantNo", map.get("relevantNo")); // 退货时，设定原销售单号
            }
        }
        
        // 产品入出库处理
        this.addProductStockInfoForSale(detailDataList, map);
        
        // 产品库存处理
        this.operateProductStockDataForSale(detailDataList, map);
        
    }
	
	/**
	 * 对入库/退库数据进行处理
	 * 一、关联单号存在，使用关联单号去查询不同的业务单据：
	 * 		1、发货单----收货业务（TradeType=2）
	 * 		2、退库申请单----确认退库（TradeType=3）
	 * 		3、入库单----确认入库（TradeType=7）
	 * 二、无关联单号或者关联单号不在指定单据范围内--通过子类型为判断是入库还是退库
	 * 		1、subType='GR':入库（TradeType=7）
	 * 		2、subType='RR':退库（TradeType=3）
	 * 
	 * @param map
	 * @throws Exception 
	 */
	@Override
	public void analyzeStockData(Map<String, Object> map) throws Exception {
		HashMap detailDataDTO = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
		String stockType = String.valueOf(detailDataDTO.get("stockType"));
		// 用于判断当前的MQ数据将走的哪一个业务
		String cherry_tradeType = "";
		//用于判断关联单号是否为空
		String relevantNoFlag = "";
		String workFlowID = "";
		int relevantBillID = 0;
        if(map.get("relevantNo")!=null && !"".equals(map.get("relevantNo"))){
            // step1：【判断关联单号是否为发货单--收货业务】
            Map<String,Object> tempMap  = binBEMQMES02_Service.selPrtDeliverInfo(map);
            if(tempMap!=null){
                // 收货（订发货/发货流程）
                cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_RECEIVE;
                // 取得发货部门ID
                map.put("organizationIDSend", tempMap.get("organizationIDSend"));
                map.put("WorkFlowID", tempMap.get("WorkFlowID"));
                relevantNoFlag = ConvertUtil.getString(map.get("relevantNo"));
            }else{
                // 用关联单号查不到发货单
                // step2：【判断关联单号是否为退库申请单--确认退库】
                List<Map<String,Object>> proReturnRequestList = binOLSTCM13_BL.selProReturnRequest(ConvertUtil.getString(map.get("relevantNo")));
                if(null != proReturnRequestList && proReturnRequestList.size()>0){
                    // 退库申请流程
                    cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_CANCELLING_STOCKS;
                    workFlowID = ConvertUtil.getString(proReturnRequestList.get(0).get("WorkFlowID"));
                    relevantNoFlag = ConvertUtil.getString(map.get("relevantNo"));
                    map.put("RA_Model", ConvertUtil.getString(proReturnRequestList.get(0).get("Model")));
                    map.put("BIN_EmployeeIDAudit", ConvertUtil.getString(proReturnRequestList.get(0).get("BIN_EmployeeIDAudit")));
                }else{
                    // 用关联单号查不到退库申请单
                    // step3：【判断关联单号是否为入库单--确认入库】
                    List<Map<String,Object>> inDepotList = binOLSTCM08_BL.selProductInDepot(ConvertUtil.getString(map.get("relevantNo")));
                    if(null != inDepotList && inDepotList.size()>0){
                        //入库流程
                        cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_FREEDOM_STOCK_IN;
                        workFlowID = ConvertUtil.getString(inDepotList.get(0).get("WorkFlowID"));
                        relevantNoFlag = ConvertUtil.getString(map.get("relevantNo"));
                        relevantBillID = CherryUtil.obj2int(inDepotList.get(0).get("BIN_ProductInDepotID"));
                        map.put("WorkFlowID", workFlowID);
                        map.put("BIN_ProductInDepotID", relevantBillID);
                    }
                }
            }
        }
		
        /**
         *  当关联单号确实存在，无需再根据subType判断；
         *  反之，在判断完此MQ的关联单号不在【发货单、退库申请单、入库单】中，则需要通过判断子类型来确定是【入库】还是【退库】业务。
         */
        // step4：【无关联单号或者关联单号不在指定单据范围内--通过子类型为判断是入库还是退库】
        if("".equals(relevantNoFlag)){
            String subType = ConvertUtil.getString(map.get("subType"));
            if(subType.equals(MessageConstants.BUSINESS_TYPE_GR)){
                // 入库
                cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_FREEDOM_STOCK_IN;
            }else if(subType.equals(MessageConstants.BUSINESS_TYPE_RR)){
                // 退库
                cherry_tradeType = MessageConstants.CHERRY_TRADETYPE_CANCELLING_STOCKS;
            }
        }
        
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");
		// 退库
		if (cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_CANCELLING_STOCKS)){
            //对大仓进行处理
            Map deportMap = new HashMap();
            deportMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
            deportMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
            deportMap.put("DepotID", map.get("inventoryInfoID"));
            deportMap.put("InOutFlag", "OUT");
            deportMap.put("BusinessType", CherryConstants.OPERATE_RR);
            deportMap.put("language", "");
            //大仓实体Map
            Map deportMap1 = null;
            List list =  binOLCM18_BL.getOppositeDepotsByBussinessType(deportMap);
            if(list!=null&&list.size()>0) {
                deportMap1 = (Map) list.get(0);
            }
            
            if(deportMap1!=null&&deportMap1.get("BIN_OrganizationID")!=null){
                map.put("organizationIDReceive", deportMap1.get("BIN_OrganizationID"));
            }else{
                // 没有查询到相关仓库信息
                MessageUtil.addMessageWarning(map,"调用共通代码getOppositeDepotsByBussinessType,仓库ID为\""+map.get("inventoryInfoID")+"\""+"，业务类型代码为\""+CherryConstants.OPERATE_RR+"\""+MessageConstants.MSG_ERROR_36);
            }
            map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);//设置入出库状态为出库
            
            if(!"".equals(relevantNoFlag)){
                // 退库申请流程--确认退库【结束退库申请流程的工作流】
            	if(!ConvertUtil.isBlank(workFlowID)) {
	                map.put("WorkFlowID", workFlowID);
	                int productReturnID = this.productReturnByRA(detailDataList, map);
	
	            	long osID = Long.parseLong(workFlowID);
	            	PropertySet ps = workflow.getPropertySet(osID);
	            	ps.setInt("BIN_ProductReturnID", productReturnID);
	            	
	            	Map<String,Object> param = new HashMap<String,Object>();
	            	param.put("WorkFlowID", workFlowID);
	            	param.put("BIN_ProductReturnID", productReturnID);
	            	param.put("TradeDateTime", map.get("tradeDateTime"));
	            	binOLSTCM13_BL.posConfirmReturnFinishFlow(param);
            	} else {
            		// k3退库申请--确认退库的关联单号没有workFlowID时需要启动一个退库工作流（否则将不会写入出库记录）
            		this.productReturn(detailDataList, map);
            	}
            } else {
                // 退库流程--直接退库【需要开始退库工作流】
                this.productReturn(detailDataList, map);
            }
		}else if(cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_RECEIVE)){
		    // 收货
			map.put("cherry_tradeType", MessageConstants.BUSINESS_TYPE_RD); 
			// 单据号采番
			//this.getMQTicketNumber(map,"");
			map.put("cherry_no", map.get("tradeNoIF"));
//			Map<String,Object> tempMap  = binBEMQMES02_Service.selPrtDeliverInfo(map);
//			if(tempMap!=null){
//				//取得发货部门ID
//				map.put("organizationIDSend", tempMap.get("organizationIDSend"));
//			}
			// 产品收货主表
			map.put("organizationIDDX", map.get("organizationID"));
	        map.put("employeeIDDX", map.get("employeeID"));
	        // 发货流程或者订货流程，由终端确认收货时写收货单（此处的SynchFlag置为1）
	        map.put("SynchFlag", CherryConstants.BILL_SYNCHFLAG_1);
			int productReceiveID = binBEMQMES02_Service.addProductReceive(map);
            List deliverDetailList = (List) ConvertUtil.byteClone(detailDataList);
            //总数量
            int totalQuantity = 0;
            //总金额
            BigDecimal totalAmount = new BigDecimal(0);
			for (int i=0;i<deliverDetailList.size();i++){
				HashMap detailDataDTOMap = (HashMap) deliverDetailList.get(i);
				// 设定产品收货id
				detailDataDTOMap.put("productReceiveID", productReceiveID);
                // 根据该条明细的入出库区分，把数量转成“入库，数量”。
                // 如果该明细的入出库区分是“入库”，那么直接将明细中的数量写到收货单据中；
                // 如果该明细的入出库区分是“出库”，那么需要将明细中的数量×-1后写到收货单据中。
                if(MessageConstants.STOCK_TYPE_OUT.equals(ConvertUtil.getString(detailDataDTOMap.get("stockType")))){
                    detailDataDTOMap.put("quantity", CherryUtil.obj2int(detailDataDTOMap.get("quantity"))*-1);
                }
                //同时存在入库出库，重新计算总数量、总金额。
                int quantity = CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
                totalQuantity += quantity;
                if(null != detailDataDTOMap.get("price") && !"".equals(detailDataDTOMap.get("price"))){
                    BigDecimal amount = new BigDecimal(Double.parseDouble((String)detailDataDTOMap.get("price")));
                    totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
                }
                //参考价格
                detailDataDTOMap.put("referencePrice", detailDataDTOMap.get("referencePrice"));
			}
			binBEMQMES02_Service.addProductReceiveDetail(deliverDetailList);
			
			map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);//设置入出库状态为入库
			
            //判断map的总数量、总金额和计算出的总数量、总金额是否一致，不一致更新主表、map。
            DecimalFormat df=new DecimalFormat("#0.00");
            String strTotalAmount = df.format(totalAmount);
            if(CherryUtil.obj2int(map.get("totalQuantity")) != totalQuantity || !ConvertUtil.getString(map.get("totalAmount")).equals(strTotalAmount)){
                Map<String,Object> update = new HashMap<String,Object>();
                update.put("UpdatedBy", map.get("updatedBy"));
                update.put("UpdatePGM", map.get("updatePGM"));
                update.put("TotalQuantity", totalQuantity);
                update.put("TotalAmount", strTotalAmount);
                update.put("BIN_ProductReceiveID", productReceiveID);
                binBEMQMES02_Service.updProductReceive(update);
                map.put("totalQuantity", totalQuantity);
                map.put("totalAmount", strTotalAmount);
            }

			this.productReceive(detailDataList, map);
			Map<String,Object> mainData = new HashMap<String,Object>();
			mainData.put("DeliverNO", map.get("relevantNo"));
			mainData.put("BIN_EmployeeID", map.get("employeeID"));
			mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
			mainData.put("BIN_ProductReceiveID", productReceiveID);
			mainData.put("ReceiveNoIF", map.get("tradeNoIF"));
			mainData.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
			binOLSTCM00_BL.posReceiveFinishFlow(mainData);
		}else if(cherry_tradeType.equals(MessageConstants.CHERRY_TRADETYPE_FREEDOM_STOCK_IN)){
		    //入库
		    if(!"".equals(workFlowID)){
		        //有工作流入库
		        List<Map<String,Object>> cloneDetailList = (List<Map<String, Object>>) ConvertUtil.byteClone(detailDataList);
		        this.productInDepotOSWF(cloneDetailList,map);
		    }else{
		        //无工作流入库及操作入出库表、库存
	            List<Map<String,Object>> cloneDetailList = (List<Map<String, Object>>) ConvertUtil.byteClone(detailDataList);
	            this.productInDepotNoOSWF(cloneDetailList,map);
		    }
		}
	}

	/**
	 * 终端做退库
	 * @throws Exception 
	 */
	public void productReturn(List<Map<String,Object>> detailDataList, Map<String,Object> map) throws Exception{
		Map<String,Object> mainData = new HashMap<String,Object>();
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		
		mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
		mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
		mainData.put("ReturnNoIF",map.get("tradeNoIF"));
		mainData.put("BIN_OrganizationID", map.get("organizationID"));
		mainData.put("BIN_OrganizationIDReceive", map.get("organizationIDReceive"));
		mainData.put("BIN_EmployeeID", map.get("employeeID"));
		mainData.put("BIN_OrganizationIDDX", map.get("organizationID"));
	    mainData.put("BIN_EmployeeIDDX", map.get("employeeID"));
		mainData.put("TotalQuantity", map.get("totalQuantity"));
		mainData.put("TotalAmount", map.get("totalAmount"));
		mainData.put("TradeType", MessageConstants.BUSINESS_TYPE_RR);
		mainData.put("RelevanceNo", map.get("relevantNo"));
		mainData.put("Reason", map.get("reason"));
		mainData.put("ReturnDate", map.get("tradeDate"));
		mainData.put("CreatedBy", map.get("createdBy"));
		mainData.put("CreatePGM", map.get("createPGM"));
		mainData.put("UpdatedBy", map.get("updatedBy"));
		mainData.put("UpdatePGM", map.get("updatePGM"));
		
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
		for(int i=0;i<detailDataList.size();i++){
			Map<String,Object> detailDataMap = detailDataList.get(i);
			Map<String,Object> detailMap = new HashMap<String,Object>();
			detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
//			detailMap.put("BIN_ProductBatchID", "");
			detailMap.put("DetailNo", detailDataMap.get("detailNo"));
			// 根据该条明细的入出库区分，把数量转成“出库，数量”。
			// 如果该明细的入出库区分是“入库”，那么需要将明细中的数量×-1后写到退库单据中；
			// 如果该明细的入出库区分是“出库”，那么直接将明细中的数量写到退库单据中。
			if(MessageConstants.STOCK_TYPE_IN.equals(ConvertUtil.getString(detailDataMap.get("stockType")))){
			    detailMap.put("Quantity", CherryUtil.obj2int(detailDataMap.get("quantity"))*-1);
			}else{
			    detailMap.put("Quantity", detailDataMap.get("quantity"));
			}
			detailMap.put("Price", detailDataMap.get("price"));
//			detailMap.put("BIN_ProductVendorPackageID", detailDataMap.get(""));
			detailMap.put("StockType", detailDataMap.get("stockType"));
			detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
			detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
//			detailMap.put("BIN_StorageLocationInfoID", detailDataMap.get(""));
			detailMap.put("Reason", detailDataMap.get("comments"));
//			detailMap.put("ChangeCount", detailDataMap.get(""));
			detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
			detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
			detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
			detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
			detailList.add(detailMap);
			
            //同时存在入库出库，重新计算总数量、总金额。
            int quantity = CherryUtil.obj2int(detailMap.get("Quantity"));
            totalQuantity += quantity;
            if(null != detailMap.get("Price") && !"".equals(detailMap.get("Price"))){
                BigDecimal amount = new BigDecimal(Double.parseDouble((String)detailMap.get("Price")));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
		}
		DecimalFormat df=new DecimalFormat("#0.00");
		mainData.put("TotalQuantity", totalQuantity);
		mainData.put("TotalAmount", df.format(totalAmount));
		int prtReturnID = binOLSTCM09_BL.insertProductReturnAll(mainData, detailList);
		
		//查询用户表获得用户ID
		Map userMap = binBEMQMES99_Service.selUserByEempID(map);
		
		Map<String, Object> newMap1 = new HashMap<String, Object>();
		newMap1.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_RR);//业务类型
		newMap1.put(CherryConstants.OS_MAINKEY_BILLID, prtReturnID);//退库单ID
		newMap1.put("BIN_EmployeeID", map.get("employeeID"));//	员工ID
		newMap1.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));//	员工ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
				userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
		newMap1.put("CurrentUnit", "MQ");//当前机能ID
		newMap1.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));//组织ID
		newMap1.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
		UserInfo userInfo = new UserInfo();
		userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
		userInfo.setBrandCode(map.get("brandCode").toString());
		userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
		userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
		userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
		newMap1.put("UserInfo", userInfo);
		
		newMap1.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
		
		//工作流开始
		binOLSTCM00_BL.StartOSWorkFlow(newMap1);
	}

	   
	/**
     * 终端做退库(退库申请流程)
     * 注：此方法只插入退库单数据及操作日志流水表
     * @throws Exception 
     */
    public int productReturnByRA(List<Map<String,Object>> detailDataList, Map<String,Object> map) throws Exception{
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        mainData.put("ReturnNoIF",map.get("tradeNoIF"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_OrganizationIDReceive", map.get("organizationIDReceive"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("BIN_OrganizationIDDX", map.get("organizationID"));
        mainData.put("BIN_EmployeeIDDX", map.get("employeeID"));
        mainData.put("BIN_EmployeeIDAudit", map.get("BIN_EmployeeIDAudit"));
        mainData.put("TotalQuantity", map.get("totalQuantity"));
        mainData.put("TotalAmount", map.get("totalAmount"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainData.put("TradeType", MessageConstants.BUSINESS_TYPE_RR);
        mainData.put("RelevanceNo", map.get("relevantNo"));
        mainData.put("Reason", map.get("reason"));
        mainData.put("ReturnDate", map.get("tradeDate"));
        mainData.put("WorkFlowID", map.get("WorkFlowID"));
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = detailDataList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
//          detailMap.put("BIN_ProductBatchID", "");
            detailMap.put("DetailNo", detailDataMap.get("detailNo"));
            // 根据该条明细的入出库区分，把数量转成“出库，数量”。
            // 如果该明细的入出库区分是“入库”，那么需要将明细中的数量×-1后写到退库单据中；
            // 如果该明细的入出库区分是“出库”，那么直接将明细中的数量写到退库单据中。
            if(MessageConstants.STOCK_TYPE_IN.equals(ConvertUtil.getString(detailDataMap.get("stockType")))){
                detailMap.put("Quantity", CherryUtil.obj2int(detailDataMap.get("quantity"))*-1);
            }else{
                detailMap.put("Quantity", detailDataMap.get("quantity"));
            }
            detailMap.put("Price", detailDataMap.get("price"));
//          detailMap.put("BIN_ProductVendorPackageID", detailDataMap.get(""));
            detailMap.put("StockType", detailDataMap.get("stockType"));
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
//          detailMap.put("BIN_StorageLocationInfoID", detailDataMap.get(""));
            detailMap.put("Reason", detailDataMap.get("comments"));
//          detailMap.put("ChangeCount", detailDataMap.get(""));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailList.add(detailMap);
            
            //同时存在入库出库，重新计算总数量、总金额。
            int quantity = CherryUtil.obj2int(detailMap.get("Quantity"));
            totalQuantity += quantity;
            if(null != detailMap.get("Price") && !"".equals(detailMap.get("Price"))){
                BigDecimal amount = new BigDecimal(Double.parseDouble((String)detailMap.get("Price")));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
        }
        DecimalFormat df=new DecimalFormat("#0.00");
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", df.format(totalAmount));
        int prtReturnID = binOLSTCM09_BL.insertProductReturnAll(mainData, detailList);
        Map<String,Object> proReturnMainData = binOLSTCM09_BL.getProductReturnMainData(prtReturnID, null);
        Map<String,Object> logMap = new HashMap<String,Object>();
        //  工作流实例ID
        logMap.put("WorkFlowID",map.get("WorkFlowID"));
        //操作部门
        logMap.put("BIN_OrganizationID",map.get("organizationID"));
        //操作员工
        logMap.put("BIN_EmployeeID",map.get("employeeID")); 
        //操作业务类型
        logMap.put("TradeType","RA");
         //表名
        logMap.put("TableName", "Inventory.BIN_ProductReturn");
        //单据ID
        logMap.put("BillID",prtReturnID);      
        //单据编号
        logMap.put("BillNo", proReturnMainData.get("ReturnNoIF"));
        //操作代码
        logMap.put("OpCode","134");
        //操作结果
        logMap.put("OpResult","100");
        //操作时间
        logMap.put("OpDate",map.get("tradeDateTime"));
        //作成者   
        logMap.put("CreatedBy",mainData.get("BIN_EmployeeID")); 
        //作成程序名
        logMap.put("CreatePGM","OSWorkFlow");
        //更新者
        logMap.put("UpdatedBy",mainData.get("BIN_EmployeeID")); 
        //更新程序名
        logMap.put("UpdatePGM","OSWorkFlow");   
        binOLCM22_BL.insertInventoryOpLog(logMap);

        return prtReturnID;
    }
	
	/**
	 * 终端做收货时，写出入库表，并更改库存
	 */
	public void productReceive(List<Map<String,Object>> detailDataList, Map<String,Object> map){
		Map<String,Object> mainData = new HashMap<String,Object>();
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		//单据号采番
		this.getMQTicketNumber(map, MessageConstants.BUSINESS_TYPE_GR);
		
		mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
		mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
		mainData.put("TradeNo",map.get("stockInOut_tradeNo"));
		mainData.put("TradeNoIF",map.get("stockInOut_tradeNoIF"));
		mainData.put("RelevanceNo",map.get("tradeNoIF"));
		mainData.put("BIN_OrganizationID", map.get("organizationID"));
		mainData.put("BIN_EmployeeID", map.get("employeeID"));
		mainData.put("TotalQuantity", map.get("totalQuantity"));
		mainData.put("TotalAmount", map.get("totalAmount"));
		mainData.put("StockType", CherryConstants.STOCK_TYPE_IN);
		mainData.put("TradeType", "RD");
//		mainData.put("BIN_LogisticInfoID", map.get(""));
		mainData.put("Comments", map.get("comments"));
		mainData.put("StockInOutDate", map.get("tradeDate"));
		mainData.put("StockInOutTime", map.get("tradeDateTime"));
		mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		mainData.put("TotalAmountBefore", map.get("totalAmountBefore"));
		mainData.put("TotalAmountAfter", map.get("totalAmountAfter"));
//		mainData.put("CloseFlag", "");
//		mainData.put("ChangeCount", "");
//		mainData.put("WorkFlowID", map.get(""));
		mainData.put("CreatedBy", map.get("createdBy"));
		mainData.put("CreatePGM", map.get("createPGM"));
		mainData.put("UpdatedBy", map.get("updatedBy"));
		mainData.put("UpdatePGM", map.get("updatePGM"));
		
		for(int i=0;i<detailDataList.size();i++){
			Map<String,Object> detailDataMap = detailDataList.get(i);
			Map<String,Object> detailMap = new HashMap<String,Object>();
			detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
//			detailMap.put("BIN_ProductBatchID", "");
			detailMap.put("DetailNo", detailDataMap.get("detailNo"));
			detailMap.put("Quantity", detailDataMap.get("quantity"));
			detailMap.put("Price", detailDataMap.get("price"));
//			detailMap.put("BIN_ProductVendorPackageID", detailDataMap.get(""));
			detailMap.put("StockType", detailDataMap.get("stockType"));
			detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
			detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
//			detailMap.put("BIN_StorageLocationInfoID", detailDataMap.get(""));
			detailMap.put("Comments", detailDataMap.get("comments"));
//			detailMap.put("ChangeCount", detailDataMap.get(""));
			detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
			detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
			detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
			detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
			detailList.add(detailMap);
		}
		//将产品入出库信息写入入出库主从表。
		int productInOutID = binOLSTCM01_BL.insertProductInOutAll(mainData, detailList);
		
		Map<String,Object> praMap = new HashMap<String,Object>();
		praMap.put("BIN_ProductInOutID", productInOutID);
		praMap.put("CreatedBy", map.get("updatedBy"));
		praMap.put("CreatePGM", map.get("updatePGM"));
		//根据入出库单据修改库存。
		//该方法根据入出库记录的明细来更改【产品库存表】，如果该明细中的产品批次ID不为空，则还会更新【产品批次库存表】
		binOLSTCM01_BL.changeStock(praMap);
	}

    /**
     * 终端做入库时，无工作流
     * @throws Exception 
     */
    public void productInDepotNoOSWF(List<Map<String,Object>> detailDataList, Map<String,Object> map) throws Exception{
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        //单据号采番
        //this.getMQTicketNumber(map, MessageConstants.BUSINESS_TYPE_GR);
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        mainData.put("BillNo",map.get("tradeNoIF"));
        mainData.put("BillNoIF",map.get("tradeNoIF"));
        mainData.put("RelevanceNo",map.get("relevantNo"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("BIN_OrganizationIDDX", map.get("organizationID"));
        mainData.put("BIN_EmployeeIDDX", map.get("employeeID"));
        mainData.put("BIN_LogisticInfoID", 0);
        mainData.put("Comments", map.get("reason"));
        mainData.put("InDepotDate", map.get("tradeDate"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        // 入库数据同步区分，1：可同步
        mainData.put("SynchFlag", CherryConstants.BILL_SYNCHFLAG_1);
        mainData.put("TradeStatus", CherryConstants.BILLTYPE_GR_FINISH);
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        List<Map<String,Object>> cloneDetailList = (List<Map<String,Object>>) ConvertUtil.byteClone(detailDataList);
        for(int i=0;i<cloneDetailList.size();i++){
            Map<String,Object> detailDataMap = (Map<String, Object>) cloneDetailList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            
            // 根据该条明细的入出库区分，把数量转成“入库，数量”。
            // 如果该明细的入出库区分是“入库”，那么直接将明细中的数量写到入库单据中；
            // 如果该明细的入出库区分是“出库”，那么需要将明细中的数量×-1后写到入库单据中。
            int quantity = CherryUtil.obj2int(detailDataMap.get("quantity"));
            if(MessageConstants.STOCK_TYPE_OUT.equals(ConvertUtil.getString(detailDataMap.get("stockType")))){
                quantity = quantity*-1;
            }
            //同时存在入库出库，重新计算总数量、总金额。
            totalQuantity += quantity;
            String price = ConvertUtil.getString(detailDataMap.get("price"));
            if(!"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
            
            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
            detailMap.put("DetailNo", detailDataMap.get("detailNo"));
            detailMap.put("Quantity", quantity);
            detailMap.put("PreQuantity", quantity);
            detailMap.put("ReferencePrice", detailDataMap.get("referencePrice"));
            detailMap.put("Price", price);
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("Comments", detailDataMap.get("reason"));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailList.add(detailMap);
        }
        DecimalFormat df=new DecimalFormat("#0.00");
        String strTotalAmount = df.format(totalAmount);
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", strTotalAmount);
        mainData.put("PreTotalQuantity", totalQuantity);
        mainData.put("PreTotalAmount", strTotalAmount);
        map.put("totalQuantity", totalQuantity);
        map.put("totalAmount", strTotalAmount);
        
        //将产品入库信息写入入库主从表。
        int productInDepotID = binOLSTCM08_BL.insertProductInDepotAll(mainData, detailList);
        this.productInDepotStock(detailDataList, map);
    }
	
	/**
     * 终端做入库（无工作流）时，写出入出库表，并更改库存
     */
    public void productInDepotStock(List<Map<String,Object>> detailDataList, Map<String,Object> map){
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        mainData.put("RelevanceNo",map.get("tradeNoIF"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("TotalQuantity", map.get("totalQuantity"));
        mainData.put("TotalAmount", map.get("totalAmount"));
        mainData.put("StockType", CherryConstants.STOCK_TYPE_IN);
        mainData.put("TradeType", "GR");
//      mainData.put("BIN_LogisticInfoID", map.get(""));
        mainData.put("Comments", map.get("comments"));
        mainData.put("StockInOutDate", map.get("tradeDate"));
        mainData.put("StockInOutTime", map.get("tradeDateTime"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainData.put("TotalAmountBefore", map.get("totalAmountBefore"));
        mainData.put("TotalAmountAfter", map.get("totalAmountAfter"));
//      mainData.put("CloseFlag", "");
//      mainData.put("ChangeCount", "");
//      mainData.put("WorkFlowID", map.get(""));
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = detailDataList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
//          detailMap.put("BIN_ProductBatchID", "");
            detailMap.put("DetailNo", detailDataMap.get("detailNo"));
            detailMap.put("Quantity", detailDataMap.get("quantity"));
            detailMap.put("Price", detailDataMap.get("price"));
//          detailMap.put("BIN_ProductVendorPackageID", detailDataMap.get(""));
            detailMap.put("StockType", detailDataMap.get("stockType"));
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
//          detailMap.put("BIN_StorageLocationInfoID", detailDataMap.get(""));
            detailMap.put("Comments", detailDataMap.get("comments"));
//          detailMap.put("ChangeCount", detailDataMap.get(""));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailList.add(detailMap);
        }
        //将产品入出库信息写入入出库主从表。
        int productInOutID = binOLSTCM01_BL.insertProductInOutAll(mainData, detailList);
        
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductInOutID", productInOutID);
        praMap.put("CreatedBy", map.get("updatedBy"));
        praMap.put("CreatePGM", map.get("updatePGM"));
        //根据入出库单据修改库存。
        //该方法根据入出库记录的明细来更改【产品库存表】，如果该明细中的产品批次ID不为空，则还会更新【产品批次库存表】
        binOLSTCM01_BL.changeStock(praMap);
    }
	
    /**
     * 终端做入库时(入库流程)
     * @throws Exception 
     */
    public void productInDepotOSWF(List<Map<String,Object>> detailDataList, Map<String,Object> map) throws Exception{
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        
        int productInDepotMainID = CherryUtil.obj2int(map.get("BIN_ProductInDepotID"));
        String workFlowID = ConvertUtil.getString(map.get("WorkFlowID"));
        
        //原入库申请明细
        List<Map<String,Object>> oldList = binOLSTCM08_BL.getProductInDepotDetailData(productInDepotMainID, null);
        int inventoryInfoID = 0;
//        int logicInventoryInfoID = 0;
        //实际入库明细与申请明细对比
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDTO = detailDataList.get(i);
            if(i == 0){
                inventoryInfoID = CherryUtil.obj2int(detailDTO.get("inventoryInfoID"));
//                logicInventoryInfoID = CherryUtil.obj2int(detailDTO.get("logicInventoryInfoID"));
            }
            int productVendorID = CherryUtil.obj2int(detailDTO.get("productVendorID"));
            for(int j=0;j<oldList.size();j++){
                Map<String,Object> curdetailDTO = oldList.get(j);
                int curProductVendorID = CherryUtil.obj2int(curdetailDTO.get("BIN_ProductVendorID"));
                if(productVendorID == curProductVendorID){
                    detailDTO.put("BIN_ProductBatchID", curdetailDTO.get("BIN_ProductBatchID"));
                    detailDTO.put("PreQuantity", curdetailDTO.get("PreQuantity"));
                    // 原入库申请明细中的参考价
                    detailDTO.put("ReferencePrice", curdetailDTO.get("ReferencePrice"));
                    oldList.remove(j);
                    break;
                }
            }
            detailDTO.put("PreQuantity", CherryUtil.obj2int(detailDTO.get("PreQuantity")));
        }
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        int detailNo = 0;
        for(int i=0;i<detailDataList.size();i++){
            detailNo = detailNo+1;
            Map<String,Object> detailDataMap = detailDataList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductInDepotID", productInDepotMainID);
            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
            detailMap.put("BIN_ProductBatchID", detailDataMap.get("BIN_ProductBatchID"));
            detailMap.put("DetailNo", detailNo);
            // 根据该条明细的入出库区分，把数量转成“入库，数量”。
            // 如果该明细的入出库区分是“入库”，那么直接将明细中的数量写到入库单据中；
            // 如果该明细的入出库区分是“出库”，那么需要将明细中的数量×-1后写到入库单据中。
            int quantity = CherryUtil.obj2int(detailDataMap.get("quantity"));
            if(MessageConstants.STOCK_TYPE_OUT.equals(ConvertUtil.getString(detailDataMap.get("stockType")))){
                quantity = quantity*-1;
            }
            //同时存在入库出库，重新计算总数量、总金额。
            totalQuantity += quantity;
            String price = ConvertUtil.getString(detailDataMap.get("price"));
            if(!"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
            detailMap.put("Quantity", quantity);
            detailMap.put("PreQuantity", detailDataMap.get("PreQuantity"));
            // 此数据为原入库申请明细单里的参考价
            /**
             * 现在以终端上传的准
             */
            String referencePrice = ConvertUtil.getString(detailDataMap.get("referencePrice"));
//            if(referencePrice.equals("")){
//                referencePrice = price;
//            }
            detailMap.put("ReferencePrice",referencePrice);
            detailMap.put("Price", price);
            detailMap.put("BIN_ProductVendorPackageID", 0);
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("Comments", detailDataMap.get("reason"));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailList.add(detailMap);
        }
        //申请明细存在，入库单不存在，入库数量为0。
        for(int i=0;i<oldList.size();i++){
            detailNo = detailNo+1;
            Map<String,Object> oldDetailMap = oldList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductInDepotID", productInDepotMainID);
            detailMap.put("BIN_ProductVendorID", oldDetailMap.get("BIN_ProductVendorID"));
            detailMap.put("BIN_ProductBatchID", oldDetailMap.get("BIN_ProductBatchID"));
            detailMap.put("DetailNo", detailNo);
            detailMap.put("Quantity", 0);
            detailMap.put("PreQuantity", oldDetailMap.get("PreQuantity"));
            detailMap.put("ReferencePrice", oldDetailMap.get("ReferencePrice"));
            detailMap.put("Price", oldDetailMap.get("Price"));
            detailMap.put("BIN_ProductVendorPackageID", 0);
            detailMap.put("BIN_InventoryInfoID", inventoryInfoID);
            detailMap.put("BIN_LogicInventoryInfoID", oldDetailMap.get("BIN_LogicInventoryInfoID"));
            detailMap.put("BIN_StorageLocationInfoID", 0);
            detailMap.put("Comments", oldDetailMap.get("Comments"));
            detailMap.put("CreatedBy", map.get("updatedBy"));
            detailMap.put("CreatePGM", map.get("updatePGM"));
            detailMap.put("UpdatedBy", map.get("updatedBy"));
            detailMap.put("UpdatePGM", map.get("updatePGM"));
            detailList.add(detailMap);
        }
        
        //删除原有明细
        Map<String,Object> praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductInDepotID", productInDepotMainID);
        binOLSTCM08_BL.delProductInDepotDetailData(praMap);
        
        //最后的明细是申请明细+入库明细的并集
        binOLSTCM08_BL.insertProductInDepotDetail(detailList);
        
        DecimalFormat df=new DecimalFormat("#0.00");
        String strTotalAmount = df.format(totalAmount);
        
        //更新主表信息
        praMap = new HashMap<String,Object>();
        praMap.put("BIN_ProductInDepotID", productInDepotMainID);
        praMap.put("TotalQuantity", totalQuantity);
        praMap.put("TotalAmount", strTotalAmount);
        praMap.put("InDepotDate", map.get("tradeDate"));
        praMap.put("RelevanceNo", map.get("tradeNoIF"));//原申请单的关联单号填写终端入库MQ的单据号。
        praMap.put("UpdatedBy", map.get("updatedBy"));
        praMap.put("UpdatePGM", map.get("updatePGM"));
        binOLSTCM08_BL.updateProductInDepotMain(praMap);
        
        //调用工作流
        long osID = Long.parseLong(workFlowID);
        ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(osID);
        int actionID = 0;
        if(null != adArr && adArr.length>0){
            Map metaMapTMp = null;
            for (int j = 0; j < adArr.length; j++) {
                metaMapTMp = adArr[j].getMetaAttributes();
                //找到带有OS_DefaultAction元素的action
                if(null != metaMapTMp && metaMapTMp.containsKey("OS_DefaultAction")){
                    String defaultAction = ConvertUtil.getString(metaMapTMp.get("OS_DefaultAction"));
                    if("autoAgree".equals(defaultAction)){
                        ActionDescriptor ad = adArr[j];
                        actionID = ad.getId();
                        break;
                    }
                }
            }
            if(actionID == 0){
                MessageUtil.addMessageWarning(map,"执行确认入库时，无法找到当前能执行Action");
            }
        }else{
            MessageUtil.addMessageWarning(map,"执行确认入库时，调用BINOLCM19_BL共通代码getCurrActionByOSID未查到当前能操作的步骤。"+
                    "涉及主要参数：工作流ID\""+osID+"\"");
        }
        
        UserInfo userInfo = new UserInfo();
        userInfo.setBIN_EmployeeID(CherryUtil.obj2int(map.get("employeeID")));

        //查询用户表获得用户ID
        Map userMap = binBEMQMES99_Service.selUserByEempID(map);
        String userID = null;
        if(null == userMap || null == userMap.get("userID")){
            userID = "-9998";
        }else{
            userID = ConvertUtil.getString(userMap.get("userID"));
        }
        
        Map<String,Object> mainDataMap = new HashMap<String,Object>();
        mainDataMap.put("entryID", osID);
        mainDataMap.put("actionID", actionID);
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));
        mainDataMap.put("BIN_EmployeeID", map.get("employeeID"));
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userID);
        mainDataMap.put("BrandCode", map.get("brandCode").toString());
        mainDataMap.put("CurrentUnit", "BINBEMQMES02");
        mainDataMap.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
        mainDataMap.put("UserInfo", userInfo);
        mainDataMap.put("OrganizationCode",map.get("counterCode"));//入库部门编号
        binOLSTCM00_BL.DoAction(mainDataMap);
    }
	
	/**
	 * 对盘点数据进行处理
	 * 
	 * @param map
	 */
	public void analyzeStockTakingData(Map<String, Object> map) throws Exception {
		// 新后台业务类型
		String cherry_tradeType = MessageConstants.BUSINESS_TYPE_CA;
		map.put("cherry_tradeType",cherry_tradeType);
		// 单据号采番
		//this.getMQTicketNumber(map,"");
		map.put("cherry_no", map.get("tradeNoIF"));
		// 明细数据List
		List detailDataList = (List) map.get("detailDataDTOList");
		
		// 插入促销品盘点业务单据表
		map.put("organizationIDDX", map.get("organizationID"));
        map.put("employeeIDDX", map.get("employeeID"));
		int productTakingID = binBEMQMES02_Service.addProductStockTaking(map);
		// 明细是否为空
		boolean isNullDetail = (null == detailDataList || detailDataList.isEmpty());
		// 循环明细数据List
		if(!isNullDetail) {
			for (int i = 0; i < detailDataList.size(); i++) {
				HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
				// 产品库存查询
				Map<String,Integer> resultMap = binBEMQMES02_Service.selPrtStockNumInfo(detailDataDTOMap);
				// 如果库存结果不为空,则更新数据
				if (resultMap != null && resultMap.get("stockQuantity") != null) {
					int stockQuantity = resultMap.get("stockQuantity");
				
					int quantityBefore = CherryUtil.obj2int(detailDataDTOMap.get("quantityBefore").toString());
					if(quantityBefore!=stockQuantity){
						logger.info("盘点时，账面数量("+quantityBefore+")与库存数量("+stockQuantity+")不相等; " +
								    "其中盘差数为\""+detailDataDTOMap.get("quantity")+"\",单据号为\""+map.get("tradeNoIF")+
								    "\",厂商编码为\""+detailDataDTOMap.get("unitcode")+"\",产品条码为\""+detailDataDTOMap.get("barcode")+"\"");
					}
					
				}
				// 设定促销产品盘点ID
				detailDataDTOMap.put("productTakingID", productTakingID);
				int quantity = CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
				// 如果是出库
				if (detailDataDTOMap.get("stockType").equals(MessageConstants.STOCK_TYPE_OUT)){
					if (detailDataDTOMap.get("quantity")!=null && !"".equals("quantity")){
						// 则盘查为负数
						int gainQuantity = quantity * (-1);
						detailDataDTOMap.put("gainQuantity", gainQuantity);
					}
				}else{
					detailDataDTOMap.put("gainQuantity", quantity);
				}
			}
			
			// 批量插入盘点详细表【明细为空时不写明细数据】
			binBEMQMES02_Service.addProductTakingDetail(detailDataList);
		
			//盘点业务需要将入出库业务单据主表的总数量设定为正整数
			if(map.get("totalQuantity")!=null&&!map.get("totalQuantity").equals("")){
				int totalQuantity = CherryUtil.obj2int(map.get("totalQuantity").toString());
				if(totalQuantity<0){
					map.put("totalQuantity", String.valueOf(CherryUtil.obj2int(map.get("totalQuantity").toString())*(-1)));
					map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
			        if(map.get("totalAmount")!=null&&!map.get("totalAmount").equals("")){
			        	BigDecimal bigdecimal = new BigDecimal((String)map.get("totalAmount"));
			        	map.put("totalAmount", bigdecimal.multiply(new BigDecimal(-1)));
			        }
				}else{
					map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
				}
			}
			map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
			// 插入产品入出库表,入出库明细表
			this.addProductStockInfo(detailDataList, map);
			
			// 操作库存数据
			this.operateProductStockData(detailDataList, map);
		}
	}

	/**
	 * 盘点审核流程，终端确认盘点，生成盘点单
	 * @param detailDataList
	 * @param map
	 * @return
	 */
	public int proStocktakeRequest(List<Map<String,Object>> detailDataList, Map<String,Object> map){
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        mainData.put("StockTakingNoIF",map.get("tradeNoIF"));
        mainData.put("RelevanceNo", map.get("tradeNoIF"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("BIN_OrganizationIDDX", map.get("organizationID"));
        mainData.put("BIN_EmployeeIDDX", map.get("employeeID"));
        mainData.put("BIN_EmployeeIDAudit", map.get("BIN_EmployeeIDAudit"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
        mainData.put("Type", map.get("subType"));
        mainData.put("TradeType", CherryConstants.OS_BILLTYPE_CA);
        mainData.put("Comments", map.get("reason"));
        mainData.put("StockReason", map.get("stockReason"));
        mainData.put("Date", map.get("tradeDate"));
        mainData.put("TradeTime", map.get("tradeTime"));
        mainData.put("WorkFlowID", map.get("WorkFlowID"));
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        
        int proStocktakeRequestID = CherryUtil.obj2int(map.get("BIN_ProStocktakeRequestID"));
        List<Map<String,Object>> proStocktakeRequestDetailData = binOLSTCM14_BL.getProStocktakeRequestDetailData(proStocktakeRequestID, null);
        String handleType = ConvertUtil.getString(proStocktakeRequestDetailData.get(0).get("HandleType"));
        
        //盘差总数量
        int totalQuantity = 0;
        //盘差总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = detailDataList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
//          detailMap.put("BIN_ProductBatchID", "");
            detailMap.put("DetailNo", i+1);
            int bookQuantity = CherryUtil.obj2int(detailDataMap.get("bookQuantity"));
            int gainQuantity = CherryUtil.obj2int(detailDataMap.get("gainQuantity"));
            detailMap.put("Quantity", bookQuantity);
            detailMap.put("GainQuantity", gainQuantity);
            detailMap.put("Price", detailDataMap.get("price"));
//          detailMap.put("BIN_ProductVendorPackageID", detailDataMap.get(""));
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
//          detailMap.put("BIN_StorageLocationInfoID", detailDataMap.get(""));
            detailMap.put("Comments", detailDataMap.get("comments"));
            detailMap.put("HandleType", handleType);
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailList.add(detailMap);
            
            totalQuantity += CherryUtil.obj2int(gainQuantity);
            BigDecimal price = new BigDecimal(0);
            if (detailMap.get("Price")!=null && !"".equals(detailMap.get("Price"))){
                price = new BigDecimal(Double.parseDouble((String)detailMap.get("Price")));
            }
            totalAmount = totalAmount.add(price.multiply(new BigDecimal(CherryUtil.obj2int(gainQuantity))));
        }
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        
        int billID = binOLSTCM06_BL.insertStockTakingAll(mainData, detailList);
        Map<String,Object> stockTakingMainData = binOLSTCM06_BL.getStockTakingMainData(billID, null);
        Map<String,Object> logMap = new HashMap<String,Object>();
        //  工作流实例ID
        logMap.put("WorkFlowID",map.get("WorkFlowID"));
        //操作部门
        logMap.put("BIN_OrganizationID",map.get("organizationID"));
        //操作员工
        logMap.put("BIN_EmployeeID",map.get("employeeID")); 
        //操作业务类型
        logMap.put("TradeType","CR");
         //表名
        logMap.put("TableName", "Inventory.BIN_ProductStockTaking");
        //单据ID
        logMap.put("BillID",billID);      
        //单据编号
        logMap.put("BillNo", stockTakingMainData.get("StockTakingNoIF"));
        //操作代码
        logMap.put("OpCode","144");
        //操作结果
        logMap.put("OpResult","100");
        //操作时间
        logMap.put("OpDate",map.get("tradeDateTime"));
        //作成者   
        logMap.put("CreatedBy",mainData.get("BIN_EmployeeID")); 
        //作成程序名
        logMap.put("CreatePGM","OSWorkFlow");
        //更新者
        logMap.put("UpdatedBy",mainData.get("BIN_EmployeeID")); 
        //更新程序名
        logMap.put("UpdatePGM","OSWorkFlow");   
        binOLCM22_BL.insertInventoryOpLog(logMap);

        return billID;
	}
	
	/**
	 * 对产品订货单数据进行处理
	 * 
	 * @param map
	 */
	public void analyzeProductOrderData(Map<String, Object> map)throws Exception {
		List detailDataList = (List) map.get("detailDataDTOList");	
		int suggestedTotalQuantity = 0;
		// 循环明细数据List,给明细suggestedQuantity赋值，并计算其总和
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			// 给明细suggestedQuantity赋值   //为了保持与其他格式一致，所以把suggestedQuantity的值保存在discount中
			if(detailDataDTOMap.get("discount")!=null&&!detailDataDTOMap.get("discount").equals("")){
				detailDataDTOMap.put("suggestedQuantity", detailDataDTOMap.get("discount"));
				suggestedTotalQuantity  += Integer.parseInt(detailDataDTOMap.get("suggestedQuantity").toString());
		    }
		}
		map.put("suggestedTotalQuantity", suggestedTotalQuantity);
		map.put("cherry_tradeType", MessageConstants.BUSINESS_TYPE_OD);
		// 单据号采番
		//this.getMQTicketNumber(map,"");
		//对大仓进行处理
		Map deportMap = new HashMap();
		deportMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
		deportMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
		deportMap.put("DepotID", map.get("inventoryInfoID"));
		deportMap.put("InOutFlag", "IN");
		deportMap.put("BusinessType", CherryConstants.OPERATE_OD);
		deportMap.put("language", "");
		//大仓实体Map
		Map deportMap1 = null;
		List list1 =  binOLCM18_BL.getOppositeDepotsByBussinessType(deportMap);
		if(list1!=null&&list1.size()>0) {
			deportMap1 = (Map) list1.get(0);
		}
		if(deportMap1==null||deportMap1.isEmpty()||deportMap1.get("BIN_OrganizationID")==null||deportMap1.get("BIN_DepotInfoID")==null){
			MessageUtil.addMessageWarning(map,"订货业务，调用BINOLCM18_BL共通代码getOppositeDepotsByBussinessType未查到大仓实体仓库。"+
					    "涉及主要参数："+"仓库ID为\""+deportMap.get("DepotID")+"\",入/出库方区分\""+"IN\",业务类型代码为\""+CherryConstants.OPERATE_OD+"\"");
		}
		
		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.put("BIN_OrganizationInfoID",map.get("organizationInfoID"));
		newMap.put("BIN_BrandInfoID",map.get("brandInfoID"));
		newMap.put("OrderNo",map.get("tradeNoIF"));
		newMap.put("OrderNoIF",map.get("tradeNoIF"));
//		newMap.put("RelevanceNo",map.get(""));
		newMap.put("OrderType", map.get("subType"));
		newMap.put("BIN_OrganizationID",map.get("organizationID"));
		newMap.put("BIN_InventoryInfoID",map.get("inventoryInfoID"));//订货仓库
		newMap.put("BIN_LogicInventoryInfoID",((Map)detailDataList.get(0)).get("logicInventoryInfoID"));//	订货逻辑仓库
	    newMap.put("BIN_OrganizationIDAccept",deportMap1.get("BIN_OrganizationID"));
	    newMap.put("BIN_InventoryInfoIDAccept",deportMap1.get("BIN_DepotInfoID"));//接受订货的仓库ID
		newMap.put("BIN_EmployeeID",map.get("employeeID"));
		newMap.put("BIN_OrganizationIDDX", map.get("organizationID"));
        newMap.put("BIN_EmployeeIDDX", map.get("employeeID"));
//		newMap.put("BIN_EmployeeIDAudit",map.get(""));
		newMap.put("SuggestedQuantity",map.get("suggestedTotalQuantity"));
		newMap.put("ApplyQuantity",map.get("totalQuantity"));
		newMap.put("TotalQuantity",map.get("totalQuantity"));
		
		newMap.put("VerifiedFlag",CherryConstants.AUDIT_FLAG_UNSUBMIT);
		newMap.put("TradeStatus",10);
//		newMap.put("BIN_LogisticInfoID",map.get(""));
		newMap.put("Comments",map.get("reason"));
		newMap.put("Date",map.get("tradeDate"));
		newMap.put("OrderTime", map.get("tradeTime"));
		newMap.put("ExpectDeliverDate", map.get("expectDeliverDate"));
//		newMap.put("WorkFlowID",map.get(""));
		newMap.put("CreatedBy",map.get("createdBy"));
		newMap.put("CreatePGM",map.get("createPGM"));
		newMap.put("UpdatedBy",map.get("updatedBy"));
		newMap.put("UpdatePGM ",map.get("updatePGM"));
		
		Map<String, Object> newMapLog = new HashMap<String, Object>();
		newMapLog.put("BIN_BrandInfoID", map.get("brandInfoID"));
//		newMapLog.put("BusinessType", CherryConstants.OPERATE_OD);
		newMapLog.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_OD);
		newMapLog.put("Type", "0");
		newMapLog.put("ProductType", "1");
		newMapLog.put("language", "");
		//设定接收的逻辑仓库
//		List<Map<String,Object>> list2 = binOLCM18_BL.getLogicDepotByBusinessType(newMapLog);
		List<Map<String,Object>> list2 = binOLCM18_BL.getLogicDepotByBusiness(newMapLog);
		Map deportMap2 = null;
		int BIN_LogicInventoryInfoID=0;
		if(list2!=null&&list2.size()>0) {
			deportMap2 = (Map) list2.get(0);
		}
		
		if(deportMap2==null||deportMap2.get("BIN_LogicInventoryInfoID")==null){
//			MessageUtil.addMessageWarning(map,"订货业务，调用BINOLCM18_BL共通代码getLogicDepotByBusinessType未查到接收的逻辑仓库。"+
//				    "涉及主要参数："+"品牌ID为\""+newMapLog.get("BrandInfoID")+"\",业务类型为\""+CherryConstants.OPERATE_OD+ "\",终端、后台区分为\""+newMapLog.get("Type")+"\"");
		
		}else{	
			BIN_LogicInventoryInfoID = Integer.parseInt(String.valueOf(deportMap2.get("BIN_LogicInventoryInfoID")));
		}
		//接受订货的逻辑仓库ID
	    newMap.put("BIN_LogicInventoryInfoIDAccept", BIN_LogicInventoryInfoID);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>> ();
		//newMap.put("detailList", list);
//		BigDecimal totalAmount = new BigDecimal(0);
		for(int i=0;i<detailDataList.size();i++){
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			Map<String,Object> detailMap = new HashMap<String,Object>();
			//设定产品厂商ID
			detailMap.put("BIN_ProductVendorID", detailDataDTOMap.get("productVendorID"));
//			//获得结算价格   通过产品厂商ID
//			Map<String,Object> resultMap = binBEMQMES02_Service.selPrtGetPriceByPrtVenID(detailMap);
//			//通过结算价格   数量  得到总金额
//			if(resultMap.get("standardCost")!=null&&!resultMap.get("standardCost").equals("")
//					&&detailDataDTOMap.get("quantity")!=null&&!detailDataDTOMap.get("quantity").equals("")){
//				String price = String.valueOf(resultMap.get("standardCost"));
//				String quantity = String.valueOf(detailDataDTOMap.get("quantity"));
//				BigDecimal bigdePrice = new BigDecimal(price);
//				BigDecimal bigdeQuantity = new BigDecimal(quantity);
//				totalAmount=totalAmount.add(bigdePrice.multiply(bigdeQuantity));
//			}
			//直接取MQ上传的价格
			detailMap.put("Price", detailDataDTOMap.get("price"));
			detailMap.put("DetailNo", detailDataDTOMap.get("detailNo"));
			detailMap.put("SuggestedQuantity", detailDataDTOMap.get("suggestedQuantity"));
			detailMap.put("ApplyQuantity", detailDataDTOMap.get("quantity"));
			detailMap.put("Quantity", detailDataDTOMap.get("quantity"));
			
			
//			detailMap.put("BIN_ProductVendorPackageID", detailDataDTOMap.get(""));
			detailMap.put("BIN_InventoryInfoID", detailDataDTOMap.get("inventoryInfoID"));
			detailMap.put("BIN_LogicInventoryInfoID", detailDataDTOMap.get("logicInventoryInfoID"));
			if(deportMap1.get("BIN_DepotInfoID")!=null){
				//接受订货的仓库ID
				detailMap.put("BIN_InventoryInfoIDAccept",deportMap1.get("BIN_DepotInfoID"));
			}
			//接受订货的逻辑仓库ID
		    detailMap.put("BIN_LogicInventoryInfoIDAccept", BIN_LogicInventoryInfoID);
//			detailMap.put("BIN_StorageLocationInfoID", detailDataDTOMap.get(""));
			detailMap.put("Comments", detailDataDTOMap.get("reason"));
			detailMap.put("CreatedBy", detailDataDTOMap.get("createdBy"));
			detailMap.put("CreatePGM", detailDataDTOMap.get("createPGM"));
			detailMap.put("UpdatedBy", detailDataDTOMap.get("updatedBy"));
			detailMap.put("UpdatePGM", detailDataDTOMap.get("updatePGM"));
			list.add(detailMap);
		}
		newMap.put("TotalAmount", map.get("totalAmount"));
		//插入订货表并返回订货ID
		int prtOrID = binOLSTCM02_BL.insertProductOrderAll(newMap, list);
		//查询用户表获得用户ID
		Map userMap = binBEMQMES99_Service.selUserByEempID(map);
		
		Map<String, Object> newMap1 = new HashMap<String, Object>();
		newMap1.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_OD);//业务类型
		newMap1.put(CherryConstants.OS_MAINKEY_BILLID, prtOrID);//订货单ID
		newMap1.put("BIN_EmployeeID", map.get("employeeID"));//	员工ID
		newMap1.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));//	员工ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
				userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
		newMap1.put("BIN_OrganizationID", deportMap1.get("BIN_OrganizationID"));//接受订货的部门的部门ID
		newMap1.put("CurrentUnit", "MQ");//当前机能ID
		newMap1.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
        newMap1.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));//组织ID
        newMap1.put("BrandCode", map.get("brandCode"));//品牌编号
        Map<String,Object> departmentInfo = binOLCM01_BL.getDepartmentInfoByID(ConvertUtil.getString(deportMap1.get("BIN_OrganizationID")), null);
        if(null != departmentInfo && !departmentInfo.isEmpty()){
            newMap1.put("OrganizationCode", departmentInfo.get("DepartCode"));//发货部门编号
        }
		
		UserInfo userInfo = new UserInfo();
		userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
		userInfo.setBrandCode(map.get("brandCode").toString());
		userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
		userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
		userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
		newMap1.put("UserInfo", userInfo);
		
		newMap1.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
		//部分品牌的工作流中会根据订货方柜台的协同区分来做判断条件
		newMap1.put("ODInOrganizationID", map.get("organizationID"));
		//工作流开始
		binOLSTCM00_BL.StartOSWorkFlow(newMap1);
		
		
//		map.put("cherry_tradeType", MessageConstants.CHERRY_TRADETYPE_ProductOrder);
//		// 单据号采番
//		this.getMQTicketNumber(map,"");
//		// 明细数据List
//		List detailDataList = (List) map.get("detailDataDTOList");	
//		int suggestedTotalQuantity = 0;
//		// 循环明细数据List,给明细suggestedQuantity赋值，并计算其总和
//		for (int i = 0; i < detailDataList.size(); i++) {
//			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
//			// 给明细suggestedQuantity赋值   //为了保持与其他格式一致，所以把suggestedQuantity的值保存在discount中
//			if(detailDataDTOMap.get("discount")!=null&&!detailDataDTOMap.get("discount").equals("")){
//				detailDataDTOMap.put("suggestedQuantity", detailDataDTOMap.get("discount"));
//				suggestedTotalQuantity  += Integer.parseInt(detailDataDTOMap.get("suggestedQuantity").toString());
//		    }
//		}
//		map.put("suggestedTotalQuantity", suggestedTotalQuantity);
//		
//		// 插入产品订货单据表
//		int productOrderID = binBEMQMES02_Service.addProductOrder(map);
//		// 循环明细数据List
//		for (int i = 0; i < detailDataList.size(); i++) {
//			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
//			// 设定产品订货ID
//			detailDataDTOMap.put("productOrderID", productOrderID);
//		}
//		// 批量插入产品订货单据明细表
//		binBEMQMES02_Service.addProductOrderDetail(detailDataList);
	
	}
	/**
	 * 对生日礼领用单数据进行处理
	 * @param map
	 * @throws Exception 
	 */
	@Override
	public void analyzeStockBirPresentData(Map<String, Object> map)
			throws Exception {
		// 取得组织ID
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		// 取得品牌ID
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
//		// 新后台业务类型
		String cherry_tradeType = MessageConstants.BUSINESS_TYPE_SP;
		map.put("cherry_tradeType",cherry_tradeType);
		
        int giftDrawID = 0;
        int detailNo = 0;
        String stockBirPresent_no = "";
        
        //判断预约单是否重复领用，对于重复领用的预约单抛错
        String campaignOrderID = "";
        String campaignOrderState = "";
        String activityType = MessageConstants.ACTIVITYTYPE_PROM;//0：促销活动
        List<Map<String,Object>> campaignOrderList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> campaignOrderDetailList = new ArrayList<Map<String,Object>>();
        Map<String,Object> mainCodeMap = new HashMap<String,Object>();
        String relevantNo = ConvertUtil.getString(map.get("relevantNo"));
        if(relevantNo.length()>0){
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("TradeNoIF", relevantNo);
            campaignOrderList = binBEMQMES02_Service.getCampaignOrderList(paramMap);
            if(null != campaignOrderList && campaignOrderList.size()>0){
                campaignOrderState = ConvertUtil.getString(campaignOrderList.get(0).get("State"));
                if(campaignOrderState.equals(MessageConstants.CAMPAIGNORDER_STATE_OK)){
                    //预约单状态已领用，判断领用单据号是否存在，存在说明已经同时存在产品和促销品。
                    paramMap = new HashMap<String,Object>();
                    paramMap.put("BIN_OrganizationInfoID", organizationInfoID);
                    paramMap.put("BIN_BrandInfoID", brandInfoID);
                    paramMap.put("TradeNoIF", map.get("tradeNoIF"));
                    List<Map<String,Object>> giftDrawList = binBEMQMES02_Service.selGiftDrawNoIF(paramMap);
                    if(null == giftDrawList || giftDrawList.size() == 0){
                        MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_72);
                    }
                }
                String campaignCode = ConvertUtil.getString(campaignOrderList.get(0).get("CampaignCode"));
                map.put("campaignCode", campaignCode);
                
                campaignOrderID = ConvertUtil.getString(campaignOrderList.get(0).get("BIN_CampaignOrderID"));
                paramMap = new HashMap<String,Object>();
                paramMap.put("BIN_CampaignOrderID", campaignOrderID);
                //根据预约ID查出会员活动预约明细表的所有明细
                campaignOrderDetailList = binBEMQMES02_Service.getCampaignOrderDetailList(paramMap);
                
                //在会员活动表用campaignCode查询，如果找到就是会员活动。
                paramMap = new HashMap<String,Object>();
                paramMap.put("CampaignCode", campaignCode);
                List<Map<String,Object>> campaignList = binBEMQMES02_Service.getCampaignList(paramMap);
                if(null != campaignList && campaignList.size()>0){
                    activityType = MessageConstants.ACTIVITYTYPE_MEM;//1：会员活动
                }
                
                //取已插入会员活动履历表的MainCode
                paramMap = new HashMap<String,Object>();
                paramMap.put("CampaignCode", campaignCode);
                paramMap.put("TradeNoIF", map.get("tradeNoIF"));
                paramMap.put("TradeType",map.get("tradeType"));
                paramMap.put("State", MessageConstants.CAMPAIGNORDER_STATE_OK);
                List<Map<String,Object>> campaignHistoryList = binBEMQMES02_Service.getCampaignHistoryList(paramMap);
                for(int i=0;i<campaignHistoryList.size();i++){
                    mainCodeMap.put(ConvertUtil.getString(campaignHistoryList.get(i).get("MainCode")), null);
                }
            }
        }
        
        //判断礼品领用表是否已经存在，存在用相同giftDrawID
        Map<String,Object> giftDrawParam = new HashMap<String,Object>();
        giftDrawParam.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        giftDrawParam.put("BIN_BrandInfoID", map.get("brandInfoID"));
        giftDrawParam.put("TradeNoIF", map.get("tradeNoIF"));
        List<Map<String,Object>> maxDetailList = binBEMQMES02_Service.getMaxSPDetailList(giftDrawParam);
        if(null != maxDetailList && maxDetailList.size()>0){
            giftDrawID = CherryUtil.obj2int(maxDetailList.get(0).get("BIN_GiftDrawID"));
            detailNo = CherryUtil.obj2int(maxDetailList.get(0).get("MaxDetailNo"));
        }else{
            // 生日礼领用单据采番
            stockBirPresent_no = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES02", cherry_tradeType);
            map.put("stockBirPresent_no", stockBirPresent_no);
        }

		// 设定入出库关联单据号(生日礼领用单)
		map.put("stockInOut_relevantNo", map.get("tradeNoIF"));
		
		map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
		List detailDataList = (List) map.get("detailDataDTOList");
		
        String couponCode = "";
        String memberInfoID = ConvertUtil.getString(map.get("memberInfoID"));
        //根据CouponCode取出CampaignCode（表BIN_CampaignOrder），MainCode（表BIN_CampaignOrderDetail）
        for(int i=0;i<detailDataList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>)detailDataList.get(i);
            //couponCode在主表不支持一个消息体有多个couponCode
            //写入主表的couponCode只取第一次出现的couponCode
            if("".equals(couponCode)){
                couponCode = ConvertUtil.getString(detailDataDTOMap.get("couponCode"));
                map.put("couponCode", couponCode);
            }
            
            if(!"".equals(relevantNo)){
                //与查出的会员活动预约明细比较（UnitCode+BarCode），找到在消息体中存在的明细，设置ActivityType、MainCode
                if(null != campaignOrderDetailList && campaignOrderDetailList.size()>0){
                    String unitCode = ConvertUtil.getString(detailDataDTOMap.get("unitcode"));
                    String barCode = ConvertUtil.getString(detailDataDTOMap.get("barcode"));
                    detailDataDTOMap.put("ActivityType", activityType);//（0：促销活动，1：会员活动）
                    for(int j=0;j<campaignOrderDetailList.size();j++){
                        HashMap<String,Object> campaignOrderDetailDTOMap = (HashMap<String,Object>)campaignOrderDetailList.get(j);
                        String curUnitCode = ConvertUtil.getString(campaignOrderDetailDTOMap.get("UnitCode"));
                        String curBarCode = ConvertUtil.getString(campaignOrderDetailDTOMap.get("BarCode"));
                        String giftType = ConvertUtil.getString(campaignOrderDetailDTOMap.get("GiftType"));
                        if(MessageConstants.SALE_TYPE_NORMAL_SALE.equals(giftType)){
                            if(unitCode.equals(curUnitCode) && barCode.equals(curBarCode)){
                                detailDataDTOMap.put("mainCode", campaignOrderDetailDTOMap.get("MainCode"));
                            }
                        }
                    }
                }
            }
        }
        
        if(!"".equals(stockBirPresent_no)){
            //插入礼品领用主表
            giftDrawID = binBEMQMES02_Service.addGiftDraw(map);
        }
		
        //会员参与活动履历表List
        List<Map<String,Object>> campaignHistory = new ArrayList<Map<String,Object>>();
		
        //系统配置项配置的是否扣库存，默认为扣库存。
        String isStockSysConfig = binOLCM14_BL.getConfigValue("1036", organizationInfoID, brandInfoID);
        
        //插入礼品领用明细表
		for(int i=0;i<detailDataList.size();i++){
		    HashMap detailDataDTOMap = (HashMap)detailDataList.get(i);
		    detailNo = detailNo+1;
		    detailDataDTOMap.put("detailNo", detailNo);
		    detailDataDTOMap.put("giftDrawID", giftDrawID);
		    detailDataDTOMap.put("giftType", MessageConstants.SALE_TYPE_NORMAL_SALE);
		    
            //产品礼品领用是否扣库存以消息体上传上来的IsStock为准，IsStock没有的话看系统配置项的值。
            //如果系统配置项不扣库存，设置不扣库存。
            //如果系统配置项扣库存，设置扣库存。
            String isStockMQ = ConvertUtil.getString(detailDataDTOMap.get("isStockMQ"));
            if(!"".equals(isStockMQ)){
                detailDataDTOMap.put("isStock", isStockMQ);
            }else{
                detailDataDTOMap.put("isStock", isStockSysConfig);
            }
            
            //会员领用且MainCode不为空时需要写会员参与活动履历表
            String mainCode = ConvertUtil.getString(detailDataDTOMap.get("mainCode"));
            if(!"".equals(memberInfoID) && !"".equals(mainCode)){
                //剔除MainCode重复
                if(!mainCodeMap.containsKey(mainCode)){
                    Map<String,Object> campaignHistoryDTO = new HashMap<String,Object>();
                    campaignHistoryDTO.put("OrgCode", map.get("orgCode"));
                    campaignHistoryDTO.put("BrandCode", map.get("brandCode"));
                    campaignHistoryDTO.put("BIN_MemberInfoID", map.get("memberInfoID"));
                    campaignHistoryDTO.put("CampaignType", detailDataDTOMap.get("ActivityType"));
                    campaignHistoryDTO.put("CampaignCode", map.get("campaignCode"));
                    campaignHistoryDTO.put("MainCode", detailDataDTOMap.get("mainCode"));
                    campaignHistoryDTO.put("TradeNoIF", map.get("tradeNoIF"));
                    campaignHistoryDTO.put("TradeType", map.get("tradeType"));
                    campaignHistoryDTO.put("State", MessageConstants.CAMPAIGNORDER_STATE_OK);//已领用
                    campaignHistoryDTO.put("BIN_OrganizationID", map.get("organizationID"));
                    campaignHistoryDTO.put("ParticipateTime", map.get("tradeDateTime"));
                    campaignHistoryDTO.put("InformType", map.get("informType"));
                    this.setInsertInfoMapKey(campaignHistoryDTO);
                    campaignHistory.add(campaignHistoryDTO);
                    mainCodeMap.put(mainCode, null);
                }
            }
		}
		binBEMQMES02_Service.addGiftDrawDetail(detailDataList);

	    this.addProductStockInfo(detailDataList, map);
	    this.operateProductStockData(detailDataList, map);
		
        //插入会员参与活动履历表
        if(null != campaignHistory && campaignHistory.size()>0){
            binBEMQMES02_Service.addCampaignHistory(campaignHistory);
        }
        
        if(!campaignOrderState.equals(MessageConstants.CAMPAIGNORDER_STATE_OK)){
	        //更新会员活动预约主表的预约单状态
	        Map<String,Object> updateMap = new HashMap<String,Object>();
	        updateMap.put("State", MessageConstants.CAMPAIGNORDER_STATE_OK);//已经领用
	        updateMap.put("FinishTime", map.get("tradeDateTime"));//领用时间
	        updateMap.put("CounterGot", map.get("counterCode"));//领取柜台
	        updateMap.put("BIN_CampaignOrderID", campaignOrderID);
	        this.setInsertInfoMapKey(updateMap);
	        binBEMQMES02_Service.updCampaignOrderState(updateMap);
	    }
	}
	
	/**
     * 对K3发货单/退库单数据进行处理
     * @param map
	 * @throws Exception 
     */
    @Override
    public void analyzeDeliverData(Map<String, Object> map) throws Exception{
        String subType = ConvertUtil.getString(map.get("subType"));
        if(CherryConstants.OS_BILLTYPE_RJ.equals(subType)){
            //退库申请流程
            productReturn_K3(map);
            return;
        }else if("2".equals(subType) || "3".equals(subType)){
            //废弃订单、退库申请单  
            invalid_K3_Bill(map);
            return;
        }
        
        String workflowid = null;
        //发货部门
        int organizationID = 0;
        //发货实体仓库
        int depotInfoID = 0;
        //发货逻辑仓库
        int logicInventoryInfoID = 0;
        //接收部门
        int organizationIDReceive = CherryUtil.obj2int(map.get("organizationID"));
        
        //校验发货单接口号是否已存在
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("DeliverNoIF", map.get("tradeNoIF"));
        List<Map<String,Object>> deliverList = binBEMQMES02_Service.selPrtDeliverList(param);
        if(null != deliverList && deliverList.size()>0){
            MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，发货单已存在");
        }
        if(null == map.get("totalQuantity") || "".equals(map.get("totalQuantity"))){
            MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，主单总数量不能为空");
        }
//        if(null == map.get("totalAmount") || "".equals(map.get("totalAmount"))){
//            MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，主单总金额不能为空");
//        }
        if(null == map.get("tradeDate") || "".equals(map.get("tradeDate"))){
            MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，发货日期不能为空");
        }
        
        if(null == map.get("relevantNo") || "".equals(map.get("relevantNo"))){
        	/**
        	 * 	关联单号；
        	 *  	此处的关联单号为与发货单对应的订货单； 
			 *		1）该字段为空：
			 *			1.1）发货部门、发货仓库取系统中配置的-----老逻辑 
			 *			1.2）新增的字段OutDepartCode不为空，取此字段为出库部门，需要判断此字段必须是存在的部门
			 *		2）该字段不为空：取订货单中设定好的接受订货部门、仓库
        	 */
        	String outDepartCode = ConvertUtil.getString(map.get("outDepartCode"));
        	if(!"".equals(outDepartCode)) {
        		// 1、由MQ消息中的OutDepartCode来确认出库部门及仓库
        		Map<String, Object> searchParam = new HashMap<String, Object>();
        		searchParam.put("counterCode", outDepartCode);
        		searchParam.put("brandInfoID", map.get("brandInfoID"));
        		searchParam.put("organizationInfoID", map.get("organizationInfoID"));
        		// 此方法已支持查询部门
        		Map<String, Object> outDepartMap = binBEMQMES99_Service.selCounterDepartmentInfo(searchParam);
        		// 用后即焚
        		searchParam.clear();
        		// 出库部门ID
        		organizationID = CherryUtil.obj2int(outDepartMap.get("organizationID"));
        		
        		// 2、查询仓库信息
        		List<Map<String, Object>> list = binOLCM18_BL.getDepotsByDepartID(ConvertUtil.getString(organizationID), "");
        		if(null != list && list.size()>0){
        			// 出库实体仓库ID
        			depotInfoID = CherryUtil.obj2int(list.get(0).get("BIN_DepotInfoID"));
        		}
        		if(0 == depotInfoID){
        			// 没有查询到相关仓库信息
        			MessageUtil.addMessageWarning(map,"出库部门为\""+map.get("outDepartCode")+"\""+MessageConstants.MSG_ERROR_36);
        		}
        		
        		// 3、查询逻辑仓库
        		searchParam.put("BIN_BrandInfoID", map.get("brandInfoID"));
        		searchParam.put("LogicInventoryCode", "DF00");
        		searchParam.put("Type", "0");//后台逻辑仓库 
        		searchParam.put("language", null);
                Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(searchParam);
                // 用后即焚
        		searchParam.clear();
        		searchParam = null;
        		// 逻辑仓库ID
        		if(null != logicInventoryInfo && !logicInventoryInfo.isEmpty()) {
        			if(null == logicInventoryInfo.get("BIN_LogicInventoryInfoID")) {
        				MessageUtil.addMessageWarning(map, "后台逻辑仓库DF00不存在");
        			}
        			logicInventoryInfoID = CherryUtil.obj2int(logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
        		}
        	} else {
        		//对大仓进行处理
                Map<String,Object> deportMap = new HashMap<String,Object>();
                deportMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
                deportMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
                deportMap.put("DepotID", map.get("inventoryInfoID"));
                deportMap.put("InOutFlag", "IN");
                deportMap.put("BusinessType", CherryConstants.OPERATE_SD);
                deportMap.put("language", "");
                //大仓实体Map
                Map<String,Object> deportMap1 = null;
                List<Map<String,Object>> list1 =  binOLCM18_BL.getOppositeDepotsByBussinessType(deportMap);
                if(list1!=null&&list1.size()>0) {
                    deportMap1 = (Map<String,Object>) list1.get(0);
                }
                if(deportMap1==null||deportMap1.isEmpty()||deportMap1.get("BIN_OrganizationID")==null||deportMap1.get("BIN_DepotInfoID")==null){
                    MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，调用BINOLCM18_BL共通代码getOppositeDepotsByBussinessType未查到大仓实体仓库。"+
                                "涉及主要参数："+"仓库ID为\""+deportMap.get("DepotID")+"\",入/出库方区分\""+"IN\",业务类型代码为\""+CherryConstants.OPERATE_SD+"\"");
                }
                organizationID = CherryUtil.obj2int(deportMap1.get("BIN_OrganizationID"));
                depotInfoID = CherryUtil.obj2int(deportMap1.get("BIN_DepotInfoID"));
                
                Map<String, Object> newMapLogic = new HashMap<String, Object>();
                newMapLogic.put("BIN_BrandInfoID", map.get("brandInfoID"));
                newMapLogic.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_SD);
                newMapLogic.put("Type", "0");
                newMapLogic.put("ProductType", "1");
                newMapLogic.put("language", "");
                //设定发货的逻辑仓库
                List<Map<String,Object>> list2 = binOLCM18_BL.getLogicDepotByBusiness(newMapLogic);
                Map<String,Object> deportMap2 = null;
                if(list2!=null&&list2.size()>0) {
                    deportMap2 = (Map<String,Object>) list2.get(0);
                }
                if(deportMap2==null||deportMap2.isEmpty()||deportMap2.get("BIN_LogicInventoryInfoID")==null){
                    MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，调用BINOLCM18_BL共通代码getLogicDepotByBusiness未查到后台逻辑仓库。"+
                    		"涉及主要参数：业务类型代码为\""+CherryConstants.LOGICDEPOT_BACKEND_SD+"\"");
                }
                logicInventoryInfoID = CherryUtil.obj2int(deportMap2.get("BIN_LogicInventoryInfoID"));
        	}
            
        }else{
            //从产品订货单取得workflowid
            Map<String,Object> orderParam = new HashMap<String,Object>();
            orderParam.put("OrderNoIF", map.get("relevantNo"));
            List<Map<String,Object>> orderList = binBEMQMES02_Service.selPrtOrderList(orderParam);
            if(null == orderList || orderList.size() ==0){
                MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，未查到有效订货单。涉及主要参数：关联单号为"+map.get("relevantNo"));
            }else{
                Map<String,Object> orderMap = orderList.get(0);
                workflowid = ConvertUtil.getString(orderMap.get("WorkFlowID"));
                organizationID = CherryUtil.obj2int(orderMap.get("BIN_OrganizationIDAccept"));
                depotInfoID = CherryUtil.obj2int(orderMap.get("BIN_InventoryInfoIDAccept"));
                logicInventoryInfoID = CherryUtil.obj2int(orderMap.get("BIN_LogicInventoryInfoIDAccept"));
            }
        }
        
        if(!"".equals(workflowid)){
            //判断工作流ID在产品发货单据主表是否存在，存在走新建的发货流程、不存在走订货流程。
            //多个发货单对应一个订货单号，第一个发货单走订货流程、再有其他的发货单走新建的发货流程。
            //颖通发货流程直接使用标准的发货流程就可以实现，注意审核者、发货者不要配置。
            Map<String,Object> paramDeliver = new HashMap<String,Object>();
            paramDeliver.put("WorkFlowID", workflowid);
            List<Map<String,Object>> delivertList = binBEMQMES02_Service.selPrtDeliverListByWorkFlowID(paramDeliver);
            if(null != delivertList && delivertList.size()>0){
                //如果找到发货单，则把工作流ID置为空，接下来强制走发货流程。
                workflowid = "";
            }
        }
        
     // **************************** NEWWITPOS-2336(2015.08.17) *************************************************
        // 如果发现订单已经被废弃，那么置workflowid为空字符
        Map<String,Object> orderParam = new HashMap<String,Object>();
        orderParam.put("OrderNoIF", map.get("relevantNo"));
        List<Map<String,Object>> orderList = binBEMQMES02_Service.selPrtOrderList(orderParam);
        if(null != orderList && orderList.size() != 0){
         	// 订单审核状态
        	String odVerifiedFlag = ConvertUtil.getString(orderList.get(0).get("VerifiedFlag"));
        	if(CherryConstants.AUDIT_FLAG_DISCARD.equals(odVerifiedFlag)){
        		 workflowid = "";
        	}
        }
        
     // **************************** NEWWITPOS-2336(2015.08.17) *************************************************

        map.put("cherry_tradeType", MessageConstants.MSG_KS_DELIVER);
        // 单据号采番
        //this.getMQTicketNumber(map,"");
        Object tradeNoIF = null;
        if(map.get("tradeNoIF")==null){
        	this.getMQTicketNumber(map,"");
        	tradeNoIF = map.get("cherry_no");
        	map.put("tradeNoIF", String.valueOf(tradeNoIF));
        }
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        mainData.put("DeliverNo", map.get("tradeNoIF"));
        mainData.put("DeliverNoIF", map.get("tradeNoIF"));
        mainData.put("RelevanceNo", map.get("relevantNo"));
        //mainData.put("DeliverType", map.get("subType"));
        mainData.put("DeliverType", null);
        mainData.put("BIN_OrganizationID", organizationID);
        mainData.put("BIN_DepotInfoID", depotInfoID);
        mainData.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        mainData.put("BIN_StorageLocationInfoID", 0);
        mainData.put("BIN_OrganizationIDReceive", organizationIDReceive);
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("TotalQuantity", map.get("totalQuantity"));
        mainData.put("TotalAmount", map.get("totalAmount"));
        if(null == workflowid || "".equals(workflowid)){
            mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
        }else{
            mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
            mainData.put("BIN_EmployeeIDAudit", map.get("employeeID"));
        }
        mainData.put("TradeStatus", CherryConstants.PRO_DELIVER_TRADESTATUS_UNSEND);
        mainData.put("BIN_LogisticInfoID", 0);
        mainData.put("Comments", map.get("reason"));
        mainData.put("Date", map.get("tradeDate"));
        mainData.put("WorkFlowID", workflowid);
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();

//        int totalQuantity = CherryUtil.obj2int(map.get("totalQuantity"));
        
        // 明细数据List
        List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailDataDTOList");
//        int sumQuantity = 0;
        for(int i=0;i<detailDataList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) detailDataList.get(i);
            if(null == detailDataDTOMap.get("quantity") || "".equals(detailDataDTOMap.get("quantity"))){
                MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，数量不能为空");
            }
//            if(null == detailDataDTOMap.get("price") || "".equals(detailDataDTOMap.get("price"))){
//                MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，价格不能为空");
//            }
            if(!ConvertUtil.getString(map.get("tradeNoIF")).equals(ConvertUtil.getString(detailDataDTOMap.get("tradeNoIF")))){
                MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，主单单据号与明细单单据号不一致");
            }
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataDTOMap.get("productVendorID"));
            detailMap.put("DetailNo", detailDataDTOMap.get("detailNo"));
            detailMap.put("Quantity", detailDataDTOMap.get("quantity"));
            detailMap.put("BIN_InventoryInfoID", depotInfoID);
            detailMap.put("ReferencePrice", detailDataDTOMap.get("price"));
            detailMap.put("Price", detailDataDTOMap.get("price"));
            //detailMap.put("BIN_ProductVendorPackageID", "");
            detailMap.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
            //detailMap.put("BIN_StorageLocationInfoID", "");
            detailMap.put("Comments", detailDataDTOMap.get("comments"));
            detailMap.put("CreatedBy", detailDataDTOMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataDTOMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataDTOMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataDTOMap.get("updatePGM"));
            detailList.add(detailMap);
            
//            sumQuantity += CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
        }
        
//        //验证数据
//        if(totalQuantity != sumQuantity){
//            MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，主单总数量与明细单数量之和不一致");
//        }
        int productDeliverID = binOLSTCM03_BL.insertProductDeliverAll(mainData, detailList);
        
        //查询用户表获得用户ID
        Map userMap = binBEMQMES99_Service.selUserByEempID(map);
        String userID = null;
        if(null == userMap || null == userMap.get("userID")){
            userID = "-9998";
        }else{
            userID = ConvertUtil.getString(userMap.get("userID"));
        }
        
        if(null == workflowid || "".equals(workflowid)){
            //工作流开始
            Map<String,Object> startOSworkFlow = new HashMap<String,Object>();
            startOSworkFlow.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_SD);//业务类型
            startOSworkFlow.put(CherryConstants.OS_MAINKEY_BILLID, productDeliverID);//发货单ID
            startOSworkFlow.put("BIN_EmployeeID", map.get("employeeID"));// 员工ID
            startOSworkFlow.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));//  员工ID
            startOSworkFlow.put(CherryConstants.OS_ACTOR_TYPE_USER,userID);//用户ID
            startOSworkFlow.put("BIN_UserID", userID);
            startOSworkFlow.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
            startOSworkFlow.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
            startOSworkFlow.put("CurrentUnit", "BINBEMQMES02");//当前机能ID
            startOSworkFlow.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
            startOSworkFlow.put("BrandCode", map.get("brandCode").toString());
            UserInfo userInfo = new UserInfo();
            userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
            userInfo.setBrandCode(map.get("brandCode").toString());
            userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
            userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
            userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
            startOSworkFlow.put("UserInfo", userInfo);
            startOSworkFlow.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
            binOLSTCM00_BL.StartOSWorkFlow(startOSworkFlow);
        }else{
            long osID = Long.parseLong(workflowid);
            PropertySet ps = workflow.getPropertySet(osID);
            ps.setInt("BIN_ProductDeliverID", productDeliverID);
            ps.setString(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_SD);
            ps.setString(CherryConstants.OS_MAINKEY_BILLID, ConvertUtil.getString(productDeliverID));
            
            ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(osID);
            int actionID = 0;
            if(null != adArr && adArr.length>0){
                Map metaMapTMp = null;
                for (int j = 0; j < adArr.length; j++) {
                    metaMapTMp = adArr[j].getMetaAttributes();
                    //找到带有OS_DefaultAction元素的action
                    if(null != metaMapTMp && metaMapTMp.containsKey("OS_DefaultAction")){
                        String defaultAction = ConvertUtil.getString(metaMapTMp.get("OS_DefaultAction"));
                        if("autoAgree".equals(defaultAction)){
                            ActionDescriptor ad = adArr[j];
                            actionID = ad.getId();
                            break;
                        }
                    }
                }
                if(actionID == 0){
                    MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，无法找到当前能执行Action");
                }
            }else{
                MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，调用BINOLCM19_BL共通代码getCurrActionByOSID未查到当前能操作的步骤。"+
                        "涉及主要参数：工作流ID\""+osID+"\"");
            }
            
            UserInfo userInfo = new UserInfo();
            userInfo.setBIN_EmployeeID(CherryUtil.obj2int(map.get("employeeID")));

            Map<String,Object> mainDataMap = new HashMap<String,Object>();
            mainDataMap.put("entryID", osID);
            mainDataMap.put("actionID", actionID);
            mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));
            mainDataMap.put("BIN_EmployeeID", map.get("employeeID"));
            mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userID);
            mainDataMap.put("BrandCode", map.get("brandCode").toString());
            mainDataMap.put("CurrentUnit", "BINBEMQMES02");
            mainDataMap.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
            mainDataMap.put("UserInfo", userInfo);
            mainDataMap.put("OrganizationCode",map.get("relevantCounterCode"));//发货部门编号
            binOLSTCM00_BL.DoAction(mainDataMap);
            
            String workflowName = workflow.getWorkflowName(Long.parseLong(workflowid));
            boolean isProFlowOD_YT_FN = binOLSTCM02_BL.isProFlowOD_YT_FN(workflowName);
            //判断第三方审核执行后当前步骤是否是发货，如果不是说明已经自动发货，写自动发货日志
            String currentOperate = ps.getString("OS_Current_Operate");
            if(isProFlowOD_YT_FN && !currentOperate.equals(CherryConstants.OPERATE_SD)){
                //写入操作日志-发货
                Map<String, Object> logMap = new HashMap<String, Object>();
                //  工作流实例ID
                logMap.put("WorkFlowID",osID);
                //操作部门
                logMap.put("BIN_OrganizationID",map.get("organizationID"));
                //操作员工
                logMap.put("BIN_EmployeeID",map.get("employeeID"));
                //操作业务类型
                logMap.put("TradeType",CherryConstants.OS_BILLTYPE_SD);
                //表名
                logMap.put("TableName", "Inventory.BIN_ProductDeliver");
                //单据ID
                logMap.put("BillID",productDeliverID);
                //单据Code
                logMap.put("BillNo", mainData.get("DeliverNoIF"));
                //操作代码
                logMap.put("OpCode",CherryConstants.OPERATE_SD);
                //操作结果
                logMap.put("OpResult",105);//已发货
                //作成者   
                logMap.put("CreatedBy",userID); 
                //作成程序名
                logMap.put("CreatePGM","BINBEMQMES02");
                //更新者
                logMap.put("UpdatedBy",userID);
                //更新程序名
                logMap.put("UpdatePGM","BINBEMQMES02");
                binOLCM22_BL.insertInventoryOpLog(logMap);
            }
        }
    }
	
    /**
     * 处理K3退库申请单
     * @param map
     * @throws Exception 
     */
    private void productReturn_K3(Map<String,Object> map) throws Exception{
        //校验退库申请单号是否已存在
        String relevantNo = ConvertUtil.getString(map.get("relevantNo"));
        if(!ConvertUtil.isBlank(relevantNo)){
        	
        	List<Map<String,Object>> proReturnRequestList = binOLSTCM13_BL.selProReturnRequest(relevantNo);
        	if(null != proReturnRequestList && proReturnRequestList.size()>0){
        		if(null == map.get("totalQuantity") || "".equals(map.get("totalQuantity"))){
        			MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，主单总数量不能为空");
        		}
        		if(null == map.get("totalAmount") || "".equals(map.get("totalAmount"))){
        			MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，主单总金额不能为空");
        		}
        		long osID = Long.parseLong(ConvertUtil.getString(proReturnRequestList.get(0).get("WorkFlowID")));
        		
        		// 查询是否已经生成RJ
        		Map<String,Object> prParam = new HashMap<String,Object>();
        		prParam.put("relevantNo", relevantNo);
        		List<Map<String,Object>> proReturnRequestRJList = binOLSTCM13_BL.selPrtReturnReqListByRelevanceNo(prParam);
        		
        		// 退库申请审核状态 
        		String prVerifiedFlag = ConvertUtil.getString(proReturnRequestList.get(0).get("VerifiedFlag"));
        		
        		if(CherryConstants.AUDIT_FLAG_DISCARD.equals(prVerifiedFlag) || !CherryUtil.isBlankList(proReturnRequestRJList)){
        			// 如果退库申请单已经被废弃或者已经生成RJ,那么此处只生成RJ及发送MQ(不做工作流)
        			// 。。。。。。。。。。。。。。。。。。。。
        			// 拼接产品退库申请数据
        			Map<String, Object> mainData = assemblingPrtReturnReqData(map,proReturnRequestList, null, null);
        			createRJAndSendMQ(mainData);
        		}else{
        			ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(osID);
        			int actionID = 0;
        			if(null != adArr && adArr.length>0){
        				Map<String,Object> metaMapTMp = null;
        				for (int j = 0; j < adArr.length; j++) {
        					metaMapTMp = adArr[j].getMetaAttributes();
        					//找到带有OS_DefaultAction元素的action
        					if(null != metaMapTMp && metaMapTMp.containsKey("OS_DefaultAction")){
        						String defaultAction = ConvertUtil.getString(metaMapTMp.get("OS_DefaultAction"));
        						if("autoAgree".equals(defaultAction)){
        							ActionDescriptor ad = adArr[j];
        							actionID = ad.getId();
        							break;
        						}
        					}
        				}
        				if(actionID == 0){
        					MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，无法找到当前能执行Action");
        				}
        			}else{
        				MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，调用BINOLCM19_BL共通代码getCurrActionByOSID未查到当前能操作的步骤。"+
        						"涉及主要参数：工作流ID\""+osID+"\"");
        			}
        			
        			// 拼接产品退库申请数据
        			Map<String, Object> mainData = assemblingPrtReturnReqData(map,proReturnRequestList, osID, actionID);
        			
        			binOLSTCM00_BL.DoAction(mainData);
        		}
        		
        	}else{
        		MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，退库申请单不存在！");
        	}
        }else{
        	// 关联单号不存在的情况下，直接生成RJ及发送MQ
        	
			// 拼接产品退库申请数据
			Map<String, Object> mainData = assemblingPrtReturnReqData(map,null, null, null);
        	createRJAndSendMQ(mainData);
        }
    }
    
	/**
	 * 退库申请（RJ）及发送退库审核MQ
	 * 不参与工作流
	 * @throws Exception
	 */
	private void createRJAndSendMQ(Map<String, Object> mainData)throws Exception{
		int newBillID = 0;
		// 生成RJ
		Map<String, Object> proReturnReqMainData = (Map<String, Object>) mainData.get("returnReqMainData");
        List<Map<String,Object>> proReturnReqDetailList = (List<Map<String, Object>>) mainData.get("returnReqDetailList");
        newBillID = binOLSTCM13_BL.insertProReturnRequestAll(proReturnReqMainData, proReturnReqDetailList);
        
        // 发送退库审核MQ
        int proReturnRequestID =newBillID;
        Map<String, Object> proReturnRequest = binOLSTCM13_BL.getProReturnRequestMainData(proReturnRequestID,null);
        String organizationID = String.valueOf(proReturnRequest.get("BIN_OrganizationID"));
        String organizationInfoID = ConvertUtil.getString(proReturnRequest.get("BIN_OrganizationInfoID"));
        String brandInfoID = ConvertUtil.getString(proReturnRequest.get("BIN_BrandInfoID"));
        
        //如果是发送退库审核到柜台，则发送MQ
        if(binOLSTCM07_BL.checkOrganizationType(organizationID)){
            Map<String,String> mqMap = new HashMap<String,String>();
            mqMap.put("BIN_OrganizationInfoID",organizationInfoID);
            mqMap.put("BIN_BrandInfoID",brandInfoID);
            mqMap.put("CurrentUnit",String.valueOf(mainData.get("CurrentUnit")));
            mqMap.put("BIN_UserID",String.valueOf(mainData.get(CherryConstants.OS_ACTOR_TYPE_USER)));
            mqMap.put("BrandCode",String.valueOf(mainData.get("BrandCode")));
            mqMap.put("OrganizationCode",String.valueOf(mainData.get("OrganizationCode")));
            mqMap.put("OrganizationInfoCode",String.valueOf(mainData.get("OrganizationInfoCode")));
            binOLSTCM13_BL.sendMQ(new int[]{proReturnRequestID}, mqMap);
        } else {
        	// 柜台没有终端机时，不发送MQ直接更改写入出库记录及修改库存
        	Map<String, Object> map = (Map<String, Object>)mainData.get("mapOrginal");
        	String returnNo = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES99", MessageConstants.BUSINESS_TYPE_RR);
        	proReturnReqMainData.put("ReturnNo", returnNo);
            // 退库申请流程
        	this.productReturn_KS(proReturnReqMainData,proReturnReqDetailList,map);
        }
        
	}
	
	/**
	 * KS退库审核时工作已结束或者没有关联单号，会发送退库MQ给终端，然后通过接收确认退库的MQ来开启退库流程
	 * 此方法对应情景：当退库部门的是否有POS机为否时，不发送MQ，此时使用此方法进行确认退库MQ的操作（即开启退库流程）
	 * @param dataMap : 经过拼接的退库单主数据
	 * @param detailDataList ： 经过拼接的退库单明细数据
	 * @param map ：KS格式MQ的原始数据
	 * @return
	 * @throws CherryMQException
	 * @throws Exception
	 */
	private void productReturn_KS(Map<String, Object> dataMap, List<Map<String, Object>> detailDataList,Map<String, Object> map) 
			throws CherryMQException, Exception {
		Map<String,Object> mainData = new HashMap<String,Object>();
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		
		mainData.put("BIN_OrganizationInfoID", dataMap.get("BIN_OrganizationInfoID"));
		mainData.put("BIN_BrandInfoID", dataMap.get("BIN_BrandInfoID"));
		mainData.put("ReturnNo",dataMap.get("ReturnNo"));
		// 退库单采用KS审核单据号
		mainData.put("ReturnNoIF",map.get("tradeNoIF"));
		mainData.put("BIN_OrganizationID", dataMap.get("BIN_OrganizationID"));
		mainData.put("BIN_OrganizationIDReceive", dataMap.get("BIN_OrganizationIDReceive"));
		mainData.put("BIN_EmployeeID", dataMap.get("BIN_EmployeeID"));
		mainData.put("BIN_OrganizationIDDX", dataMap.get("BIN_OrganizationID"));
	    mainData.put("BIN_EmployeeIDDX", dataMap.get("BIN_EmployeeID"));
		mainData.put("TotalQuantity", dataMap.get("TotalQuantity"));
		mainData.put("TotalAmount", dataMap.get("TotalAmount"));
		mainData.put("TradeType", MessageConstants.BUSINESS_TYPE_RR);
		// KS审核单据号
		mainData.put("RelevanceNo", dataMap.get("BillNoIF"));
		mainData.put("Reason", map.get("reason"));
		mainData.put("ReturnDate", dataMap.get("TradeDate"));
		mainData.put("CreatedBy", dataMap.get("CreatedBy"));
		mainData.put("CreatePGM", dataMap.get("CreatePGM"));
		mainData.put("UpdatedBy", dataMap.get("UpdatedBy"));
		mainData.put("UpdatePGM", dataMap.get("UpdatePGM"));
		
		for(int i=0;i<detailDataList.size();i++){
			Map<String,Object> detailDataMap = detailDataList.get(i);
			Map<String,Object> detailMap = new HashMap<String,Object>();
			detailMap.put("BIN_ProductVendorID", detailDataMap.get("BIN_ProductVendorID"));
			detailMap.put("DetailNo", i+1);
			detailMap.put("Quantity", detailDataMap.get("Quantity"));
			detailMap.put("Price", detailDataMap.get("Price"));
			// 退库单，入出库区分必为出库，数量上在拼接数据时已做过处理（assemblingPrtReturnReqData）
			detailMap.put("StockType", MessageConstants.STOCK_TYPE_OUT);
			detailMap.put("BIN_InventoryInfoID", detailDataMap.get("BIN_InventoryInfoID"));
			detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("BIN_LogicInventoryInfoID"));
			detailMap.put("Reason", detailDataMap.get("Reason"));
			detailMap.put("CreatedBy", detailDataMap.get("CreatedBy"));
			detailMap.put("CreatePGM", detailDataMap.get("CreatePGM"));
			detailMap.put("UpdatedBy", detailDataMap.get("UpdatedBy"));
			detailMap.put("UpdatePGM", detailDataMap.get("UpdatePGM"));
			detailList.add(detailMap);
			
		}
		
		int prtReturnID = binOLSTCM09_BL.insertProductReturnAll(mainData, detailList);
		
		//查询用户表获得用户ID
		Map userMap = binBEMQMES99_Service.selUserByEempID(map);
		
		Map<String, Object> newMap1 = new HashMap<String, Object>();
		newMap1.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_RR);//业务类型
		newMap1.put(CherryConstants.OS_MAINKEY_BILLID, prtReturnID);//退库单ID
		newMap1.put("BIN_EmployeeID", map.get("employeeID"));//	员工ID
		newMap1.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));//	员工ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
				userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
		newMap1.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
		newMap1.put("CurrentUnit", "MQ");//当前机能ID
		newMap1.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));//组织ID
		newMap1.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
		
		UserInfo userInfo = new UserInfo();
		userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
		userInfo.setBrandCode(map.get("brandCode").toString());
		userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
		userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
		userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
		newMap1.put("UserInfo", userInfo);
		
		newMap1.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
		
		//工作流开始
		binOLSTCM00_BL.StartOSWorkFlow(newMap1);
		
	}

    /**
     * 拼接产品退库申请数据
     * @param map
     * @param proReturnRequestList
     * @param osID
     * @param actionID
     * @return
     * @throws CherryMQException
     * @throws Exception
     */
	private Map<String, Object> assemblingPrtReturnReqData( Map<String, Object> map, List<Map<String, Object>> proReturnRequestList, 
			Object _osID,Object _actionID) throws CherryMQException, Exception {
		
		long osID = ConvertUtil.getInt(_osID);
		int actionID = ConvertUtil.getInt(_actionID);
		
		String model;
		String createEmployeeID;
		String reason;
		if(!CherryUtil.isBlankList(proReturnRequestList)){
			model = ConvertUtil.getString(proReturnRequestList.get(0).get("Model"));
			createEmployeeID = ConvertUtil.getString(proReturnRequestList.get(0).get("BIN_EmployeeID"));
			reason = ConvertUtil.getString(proReturnRequestList.get(0).get("Reason"));
		}else{
			model = "M2";
			reason = "";
			createEmployeeID = "";
		}
		
		List<Map<String,Object>> detailDataDTOList = (List<Map<String,Object>>)map.get("detailDataDTOList");
		List<Map<String,Object>> returnReqDetailList = new ArrayList<Map<String,Object>>();
		
		//查询用户表获得用户ID
		Map<String,Object> userMap = binBEMQMES99_Service.selUserByEempID(map);
		
		//对大仓进行处理
		Map<String,Object> deportMap = new HashMap<String,Object>();
		deportMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
		deportMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
		deportMap.put("DepotID", map.get("inventoryInfoID"));
		deportMap.put("InOutFlag", "OUT");
		deportMap.put("BusinessType", CherryConstants.OPERATE_RR);
		deportMap.put("language", "");
		//大仓实体Map
		Map<String,Object> deportMap1 = null;
		int depotInfoIDReceive = 0;
		List<Map<String,Object>> list =  binOLCM18_BL.getOppositeDepotsByBussinessType(deportMap);
		if(list!=null&&list.size()>0) {
			deportMap1 = (Map<String,Object>) list.get(0);
		}
		
		if(deportMap1!=null&&deportMap1.get("BIN_OrganizationID")!=null){
			map.put("organizationIDReceive", deportMap1.get("BIN_OrganizationID"));
			//取得接收退库的实体仓库ID
			depotInfoIDReceive = CherryUtil.obj2int(deportMap1.get("BIN_DepotInfoID"));
		}else{
			// 没有查询到相关仓库信息
			MessageUtil.addMessageWarning(map,"调用共通代码getOppositeDepotsByBussinessType，仓库ID为\""+map.get("inventoryInfoID")+"\""+"，业务类型代码为\""+CherryConstants.OPERATE_RR+"\""+MessageConstants.MSG_ERROR_36);
		}
		
		//取得接收退库的逻辑仓库ID
		int logicInventoryInfoIDReceive = 0;
		Map<String,Object> logicMap = new HashMap<String,Object>();
		logicMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
//            logicMap.put("BusinessType", CherryConstants.OPERATE_RR);
		logicMap.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_AR);
		logicMap.put("Type", "0");
		logicMap.put("ProductType", "1");
//            List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusinessType(logicMap);
		List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(logicMap);
		if(null != logicList && logicList.size()>0){
			logicInventoryInfoIDReceive = CherryUtil.obj2int(logicList.get(0).get("BIN_LogicInventoryInfoID"));
		}else{
			// 没有查询到相关逻辑仓库信息
//                MessageUtil.addMessageWarning(map,"调用共通代码getLogicDepotByBusinessType，业务类型代码为\""+CherryConstants.OPERATE_RR+"\""+MessageConstants.MSG_ERROR_37);
			MessageUtil.addMessageWarning(map,"调用共通代码getLogicDepotByBusiness，业务类型代码为\""+CherryConstants.LOGICDEPOT_BACKEND_AR+"\""+MessageConstants.MSG_ERROR_37);
		}
		
		int inventoryInfoID = 0;
		int logicInventoryInfoID = 0; 
		for(int i=0;i<detailDataDTOList.size();i++){
			Map<String,Object> detailDataDTO = detailDataDTOList.get(i);
			if(i == 0){
				inventoryInfoID = CherryUtil.obj2int(detailDataDTO.get("inventoryInfoID"));
				logicInventoryInfoID = CherryUtil.obj2int(detailDataDTO.get("logicInventoryInfoID"));
			}
			Map<String,Object> detailMap = new HashMap<String,Object>();
			detailMap.put("BIN_ProductVendorID", detailDataDTO.get("productVendorID"));
			// 根据该条明细的入出库区分，把数量转成“出库，数量”。
			// 如果该明细的入出库区分是“入库”，那么需要将明细中的数量×-1后写到退库申请单据中；
			// 如果该明细的入出库区分是“出库”，那么直接将明细中的数量写到退库申请单据中。
			if(MessageConstants.STOCK_TYPE_IN.equals(ConvertUtil.getString(detailDataDTO.get("stockType")))){
				detailMap.put("Quantity", CherryUtil.obj2int(detailDataDTO.get("quantity"))*-1);
			}else{
				detailMap.put("Quantity", detailDataDTO.get("quantity"));
			}
			detailMap.put("Price", detailDataDTO.get("price"));
			detailMap.put("BIN_ProductVendorPackageID", "0");
			detailMap.put("BIN_InventoryInfoID", detailDataDTO.get("inventoryInfoID"));
			detailMap.put("BIN_LogicInventoryInfoID", detailDataDTO.get("logicInventoryInfoID"));
			detailMap.put("BIN_InventoryInfoIDReceive", depotInfoIDReceive);
			detailMap.put("BIN_LogicInventoryInfoIDReceive", logicInventoryInfoIDReceive);
			detailMap.put("BIN_StorageLocationInfoID", "0");
			detailMap.put("Reason", detailDataDTO.get("reason"));
			detailMap.put("CreatedBy", map.get("createdBy"));
			detailMap.put("CreatePGM", map.get("createPGM"));
			detailMap.put("UpdatedBy", map.get("updatedBy"));
			detailMap.put("UpdatePGM", map.get("updatePGM"));
			returnReqDetailList.add(detailMap);
		}
		
		Map<String,Object> returnReqMainData = new HashMap<String,Object>();
		returnReqMainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
		returnReqMainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
		returnReqMainData.put("BillNo", map.get("tradeNoIF"));
		returnReqMainData.put("BillNoIF", map.get("tradeNoIF"));
		returnReqMainData.put("BIN_OrganizationID", map.get("organizationID"));
		returnReqMainData.put("BIN_InventoryInfoID", inventoryInfoID);
		returnReqMainData.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
		returnReqMainData.put("BIN_OrganizationIDReceive", map.get("organizationIDReceive"));
		returnReqMainData.put("BIN_InventoryInfoIDReceive", depotInfoIDReceive);
		returnReqMainData.put("BIN_LogicInventoryInfoIDReceive", logicInventoryInfoIDReceive);
		returnReqMainData.put("BIN_EmployeeID", createEmployeeID);
		returnReqMainData.put("BIN_EmployeeIDAudit", map.get("employeeID"));
		returnReqMainData.put("TotalQuantity", map.get("totalQuantity"));
		returnReqMainData.put("TotalAmount", map.get("totalAmount"));
		returnReqMainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_AGREE);
		returnReqMainData.put("TradeType", CherryConstants.OS_BILLTYPE_RJ);
		returnReqMainData.put("Model", model);
		returnReqMainData.put("RelevanceNo", map.get("relevantNo"));
		returnReqMainData.put("BIN_LogisticInfoID", "0");
		returnReqMainData.put("Reason", reason);
		returnReqMainData.put("WorkFlowID", osID == 0 ? null : osID);
		returnReqMainData.put("TradeDate", map.get("tradeDate"));
		returnReqMainData.put("CreatedBy", map.get("createdBy"));
		returnReqMainData.put("CreatePGM", map.get("createPGM"));
		returnReqMainData.put("UpdatedBy", map.get("updatedBy"));
		returnReqMainData.put("UpdatePGM", map.get("updatePGM"));
		
		UserInfo userInfo = new UserInfo();
		userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
		userInfo.setBrandCode(map.get("brandCode").toString());
		userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
		userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
		userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
		
		Map<String,Object> mainData = new HashMap<String,Object>();
		mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_RA);//业务类型
		mainData.put("BIN_EmployeeID", map.get("employeeID"));// 员工ID
		mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));// 员工ID
		mainData.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
				userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
		mainData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
		mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
		mainData.put("CurrentUnit", "MQ");//当前机能ID
		mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
		mainData.put("UserInfo", userInfo);
		mainData.put("OrganizationInfoCode", map.get("orgCode"));
		mainData.put("BrandCode", map.get("brandCode"));
		mainData.put("OrganizationCode", map.get("counterCode"));
		mainData.put("returnReqMainData", returnReqMainData);
		mainData.put("returnReqDetailList", returnReqDetailList);
		mainData.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
		mainData.put("entryID", osID == 0 ? null : osID);
		mainData.put("actionID", actionID == 0 ? null : actionID);
		mainData.put("KSFlag", "KS");
		mainData.put("mapOrginal", map);
		return mainData;
	}
    
    /**
     * 处理K3废弃订单、退库申请单
     * @param map
     * @throws Exception 
     */
    private void invalid_K3_Bill(Map<String,Object> map) throws Exception{
        String subType = ConvertUtil.getString(map.get("subType"));
        String relevantNo = ConvertUtil.getString(map.get("relevantNo"));
        String workFlowID = "";
        if("2".equals(subType)){
            //废弃订单
            //从产品订货单取得workflowid
            Map<String,Object> orderParam = new HashMap<String,Object>();
            orderParam.put("OrderNoIF", relevantNo);
            List<Map<String,Object>> orderList = binBEMQMES02_Service.selPrtOrderList(orderParam);
            if(null != orderList && orderList.size()>0){
            	// 工作流ID
            	workFlowID = ConvertUtil.getString(orderList.get(0).get("WorkFlowID"));
            	
            	// **************************** NEWWITPOS-2336(2015.08.17) *************************************************
             	// 订单审核状态
            	String odVerifiedFlag = ConvertUtil.getString(orderList.get(0).get("VerifiedFlag"));
            	if(CherryConstants.AUDIT_FLAG_DISCARD.equals(odVerifiedFlag)){
            		// 如果发现订单已经被废弃，那么直接return
            		return;
            	}
                
                //校验单据是否已经在发货单中存在
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("WorkFlowID", workFlowID);
                List<Map<String,Object>> prtDeliverList = binBEMQMES02_Service.selPrtDeliverListByWorkFlowID(param);
                if(null != prtDeliverList && prtDeliverList.size() >0 ){
            		// 如果发现单据已经发过货，那么直接return
            		return;
                }
                
                // **************************** NEWWITPOS-2336(2015.08.17) *************************************************
            	
            }else{
                //订单未找到
                MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_60+"\""+relevantNo+"\"");
            }
        }else if("3".equals(subType)){
            //废弃退库申请单
            //从产品退库申请单取得workflowid
            List<Map<String,Object>> proReturnRequestList = binOLSTCM13_BL.selProReturnRequest(relevantNo);
            if(null != proReturnRequestList && proReturnRequestList.size()>0){
        		
            	// 查询是否已经生成RJ
            	Map<String,Object> prParam = new HashMap<String,Object>();
        		prParam.put("relevantNo", relevantNo);
        		List<Map<String,Object>> proReturnRequestRJList = binOLSTCM13_BL.selPrtReturnReqListByRelevanceNo(prParam);
        		
        		// 退库申请审核状态 
        		String prVerifiedFlag = ConvertUtil.getString(proReturnRequestList.get(0).get("VerifiedFlag"));
        		
        		// 如果退库申请单已经被废弃或者已经生成RJ,那么此处直接Return
        		if(CherryConstants.AUDIT_FLAG_DISCARD.equals(prVerifiedFlag) || !CherryUtil.isBlankList(proReturnRequestRJList)){
        			return;
        		}
            	
                workFlowID = ConvertUtil.getString(proReturnRequestList.get(0).get("WorkFlowID"));
            }else{
                //退库申请单未找到
                MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_61+"\""+relevantNo+"\"");
            }
        }

        long osID = Long.parseLong(workFlowID);
        ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(osID);
        int actionID = 0;
        if(null != adArr && adArr.length>0){
            Map<String,Object> metaMapTMp = null;
            for (int j = 0; j < adArr.length; j++) {
                metaMapTMp = adArr[j].getMetaAttributes();
                //找到带有OS_DefaultAction元素的action，且值为自动废弃
                if(null != metaMapTMp && metaMapTMp.containsKey("OS_DefaultAction")){
                    String defaultAction = ConvertUtil.getString(metaMapTMp.get("OS_DefaultAction"));
                    if("autoInvalid".equals(defaultAction)){
                        ActionDescriptor ad = adArr[j];
                        actionID = ad.getId();
                        break;
                    }
                }
            }
            if(actionID == 0){
                MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，无法找到当前能执行Action");
            }
        }else{
            MessageUtil.addMessageWarning(map,"执行第三方接口数据导入时，调用BINOLCM19_BL共通代码getCurrActionByOSID未查到当前能操作的步骤。"+
                    "涉及主要参数：工作流ID\""+osID+"\"");
        }
        
        //查询用户表获得用户ID
        Map<String,Object> userMap = binBEMQMES99_Service.selUserByEempID(map);
        String userID = null;
        if(null == userMap || null == userMap.get("userID")){
            userID = "-9998";
        }else{
            userID = ConvertUtil.getString(userMap.get("userID"));
        }
        
        Map<String,Object> mainDataMap = new HashMap<String,Object>();
        mainDataMap.put("entryID", osID);
        mainDataMap.put("actionID", actionID);
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));
        mainDataMap.put("BIN_EmployeeID", map.get("employeeID"));
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userID);
        mainDataMap.put("BrandCode", map.get("brandCode").toString());
        mainDataMap.put("CurrentUnit", "BINBEMQMES02");
        mainDataMap.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
        mainDataMap.put("OrganizationCode", ConvertUtil.getString(map.get("counterCode")));
        mainDataMap.put("OrganizationInfoCode", ConvertUtil.getString(map.get("orgCode")));
        binOLSTCM00_BL.DoAction(mainDataMap); 
    }
    
    /**
     * 对移库单数据进行处理
     * @param map
     * @throws Exception 
     */
    @Override
    public void analyzeShiftData(Map<String, Object> map) throws Exception{
        List<Map<String,Object>> detailDataDTOList = (List<Map<String,Object>>)map.get("detailDataDTOList");
        List<Map<String,Object>> productShiftDetailList = new ArrayList<Map<String,Object>>();
        int detailNo = 1;
        //总数量
        int totalQuantity = 0;
        //总金额
        BigDecimal totalAmount = new BigDecimal(0);
        
        for(int i=0;i<detailDataDTOList.size();i++){
            Map<String,Object> detailDataDTO = detailDataDTOList.get(i);
            int productVendorID = CherryUtil.obj2int(detailDataDTO.get("productVendorID"));
            String stockType = ConvertUtil.getString(detailDataDTO.get("stockType"));
            String depotInfoID = ConvertUtil.getString(detailDataDTO.get("inventoryInfoID"));
            String logicInventoryInfoID = ConvertUtil.getString(detailDataDTO.get("logicInventoryInfoID"));
            int quantity = CherryUtil.obj2int(detailDataDTO.get("quantity"));
            BigDecimal price = new BigDecimal(0);
            if (detailDataDTO.get("price")!=null && !"".equals(detailDataDTO.get("price"))){
                price = new BigDecimal(Double.parseDouble((String)detailDataDTO.get("price")));
            }
            String unitCode = ConvertUtil.getString(detailDataDTO.get("unitcode"));
            String barCode = ConvertUtil.getString(detailDataDTO.get("barcode"));
            String comments = ConvertUtil.getString(detailDataDTO.get("reason"));
            String createdBy = ConvertUtil.getString(detailDataDTO.get("createdBy"));
            String createPGM = ConvertUtil.getString(detailDataDTO.get("createPGM"));
            String updatedBy = ConvertUtil.getString(detailDataDTO.get("updatedBy"));
            String updatePGM = ConvertUtil.getString(detailDataDTO.get("updatePGM"));
            boolean flag = false;
            for(int j=0;j<productShiftDetailList.size();j++){
                Map<String,Object> temp = productShiftDetailList.get(j);
                int curProductVendorID = CherryUtil.obj2int(temp.get("BIN_ProductVendorID"));
                int curQuantity = CherryUtil.obj2int(temp.get("Quantity"));
                if(curProductVendorID == productVendorID){
                    if(quantity != curQuantity){
                        //移库单移出数量与移入数量不一致
                        MessageUtil.addMessageWarning(map, "厂商编码为\""+unitCode+"\"产品条码为\""+barCode+"\""+MessageConstants.MSG_ERROR_50);
                    }
                    flag = true;
                    if(stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
                        temp.put("FromLogicInventoryInfoID", logicInventoryInfoID);
                    }else if(stockType.equals(MessageConstants.STOCK_TYPE_IN)){
                        temp.put("ToLogicInventoryInfoID", logicInventoryInfoID);
                    }
                }
            }
            
            if(!flag){
                Map<String,Object> temp = new HashMap<String,Object>();
                temp.put("BIN_ProductVendorID", productVendorID);
                temp.put("DetailNo", detailNo);
                temp.put("Quantity", quantity);
                temp.put("Price", price);
                temp.put("BIN_ProductVendorPackageID", 0);
                temp.put("FromDepotInfoID", depotInfoID);
                temp.put("ToDepotInfoID", depotInfoID);
                temp.put("FromStorageLocationInfoID", 0);
                temp.put("ToStorageLocationInfoID", 0);
                if(stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
                    temp.put("FromLogicInventoryInfoID", logicInventoryInfoID);
                }else if(stockType.equals(MessageConstants.STOCK_TYPE_IN)){
                    temp.put("ToLogicInventoryInfoID", logicInventoryInfoID);
                }
                temp.put("unitcode", unitCode);
                temp.put("barcode", barCode);
                temp.put("Comments", comments);
                temp.put("CreatedBy", createdBy);
                temp.put("CreatePGM", createPGM);
                temp.put("UpdatedBy", updatedBy);
                temp.put("UpdatePGM", updatePGM);
                productShiftDetailList.add(temp);
                detailNo++;
                
                totalQuantity += quantity;
                totalAmount = totalAmount.add(price.multiply(new BigDecimal(quantity)));
            }
        }
        
        //验证移出、移入逻辑仓库必须同时存在
        for(int i=0;i<productShiftDetailList.size();i++){
            Map temp = productShiftDetailList.get(i);
            String fromLogicInventoryInfoID = ConvertUtil.getString(temp.get("FromLogicInventoryInfoID"));
            String toLogicInventoryInfoID = ConvertUtil.getString(temp.get("ToLogicInventoryInfoID"));
            if("".equals(fromLogicInventoryInfoID)){
                //移出方的逻辑仓库不存在
                MessageUtil.addMessageWarning(map, "厂商编码为\""+temp.get("unitcode")+"\"产品条码为\""+temp.get("barcode")+"\""+MessageConstants.MSG_ERROR_51);
            }else if("".equals(toLogicInventoryInfoID)){
                //移入方的逻辑仓库不存在
                MessageUtil.addMessageWarning(map, "厂商编码为\""+temp.get("unitcode")+"\"产品条码为\""+temp.get("barcode")+"\""+MessageConstants.MSG_ERROR_52);
            }
        }
        
        //查询用户表获得用户ID
        Map<String,Object> userMap = binBEMQMES99_Service.selUserByEempID(map);
        
        //单据号采番
        //this.getMQTicketNumber(map, MessageConstants.BUSINESS_TYPE_MV);
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        mainData.put("BillNo", map.get("tradeNoIF"));
        mainData.put("BillNoIF", map.get("tradeNoIF"));
        mainData.put("BusinessType", CherryConstants.OS_BILLTYPE_MV);
        mainData.put("RelevanceNo", map.get("relevanceNo"));
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("TotalQuantity", totalQuantity);
        mainData.put("TotalAmount", totalAmount);
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
        mainData.put("Comments", map.get("reason"));
        mainData.put("OperateDate", map.get("tradeDate"));
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        int productShiftID = binOLSTCM04_BL.insertProductShiftAll(mainData, productShiftDetailList);
        Map<String, Object> newMap1 = new HashMap<String, Object>();
        newMap1.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_MV);//业务类型
        newMap1.put(CherryConstants.OS_MAINKEY_BILLID, productShiftID);//移库单ID
        newMap1.put("BIN_EmployeeID", map.get("employeeID"));// 员工ID
        newMap1.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));//  员工ID
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
                userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
        newMap1.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
        newMap1.put("CurrentUnit", "MQ");//当前机能ID
        newMap1.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
        UserInfo userInfo = new UserInfo();
        userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
        userInfo.setBrandCode(map.get("brandCode").toString());
        userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
        userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
        userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
        newMap1.put("UserInfo", userInfo);
        newMap1.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
        //工作流开始
        binOLSTCM00_BL.StartOSWorkFlow(newMap1);
    }
    
    /**
     * 对退库申请单数据进行处理
     * @param map
     * @throws Exception 
     */
    @Override
    public void analyzeReturnRequestData(Map<String, Object> map) throws Exception{
        List<Map<String,Object>> detailDataDTOList = (List<Map<String,Object>>)map.get("detailDataDTOList");
        List<Map<String,Object>> returnReqDetailList = new ArrayList<Map<String,Object>>();
        
        //查询用户表获得用户ID
        Map<String,Object> userMap = binBEMQMES99_Service.selUserByEempID(map);
        
        //对大仓进行处理
        Map<String,Object> deportMap = new HashMap<String,Object>();
        deportMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        deportMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
        deportMap.put("DepotID", map.get("inventoryInfoID"));
        deportMap.put("InOutFlag", "OUT");
        deportMap.put("BusinessType", CherryConstants.OPERATE_RR);
        deportMap.put("language", "");
        //大仓实体Map
        Map<String,Object> deportMap1 = null;
        int depotInfoIDReceive = 0;
        List<Map<String,Object>> list =  binOLCM18_BL.getOppositeDepotsByBussinessType(deportMap);
        if(list!=null&&list.size()>0) {
            deportMap1 = (Map<String,Object>) list.get(0);
        }
        
        if(deportMap1!=null&&deportMap1.get("BIN_OrganizationID")!=null){
            map.put("organizationIDReceive", deportMap1.get("BIN_OrganizationID"));
            //取得接收退库的实体仓库ID
            depotInfoIDReceive = CherryUtil.obj2int(deportMap1.get("BIN_DepotInfoID"));
        }else{
            // 没有查询到相关仓库信息
            MessageUtil.addMessageWarning(map,"调用共通代码getOppositeDepotsByBussinessType，仓库ID为\""+map.get("inventoryInfoID")+"\""+"，业务类型代码为\""+CherryConstants.OPERATE_RR+"\""+MessageConstants.MSG_ERROR_36);
        }
        
        //取得接收退库的逻辑仓库ID
        int logicInventoryInfoIDReceive = 0;
        Map<String,Object> logicMap = new HashMap<String,Object>();
        logicMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
        logicMap.put("BusinessType", CherryConstants.LOGICDEPOT_BACKEND_AR);
        logicMap.put("Type", "0");
        logicMap.put("ProductType", "1");
        
//        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusinessType(logicMap);
        List<Map<String,Object>> logicList = binOLCM18_BL.getLogicDepotByBusiness(logicMap);
        if(null != logicList && logicList.size()>0){
            logicInventoryInfoIDReceive = CherryUtil.obj2int(logicList.get(0).get("BIN_LogicInventoryInfoID"));
        }else{
            // 没有查询到相关逻辑仓库信息
//            MessageUtil.addMessageWarning(map,"调用共通代码getLogicDepotByBusinessType，业务类型代码为\""+CherryConstants.OPERATE_RR+"\""+MessageConstants.MSG_ERROR_37);
            MessageUtil.addMessageWarning(map,"调用共通代码getLogicDepotByBusiness，业务类型代码为\""+CherryConstants.LOGICDEPOT_BACKEND_AR+"\""+MessageConstants.MSG_ERROR_37);
        }
        
        int inventoryInfoID = 0;
        int logicInventoryInfoID =0;
        for(int i=0;i<detailDataDTOList.size();i++){
            Map<String,Object> detailDataDTO = detailDataDTOList.get(i);
            if(i == 0){
                inventoryInfoID = CherryUtil.obj2int(detailDataDTO.get("inventoryInfoID"));
                logicInventoryInfoID = CherryUtil.obj2int(detailDataDTO.get("logicInventoryInfoID"));
            }
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataDTO.get("productVendorID"));
            // 根据该条明细的入出库区分，把数量转成“出库，数量”。
            // 如果该明细的入出库区分是“入库”，那么需要将明细中的数量×-1后写到退库申请单据中；
            // 如果该明细的入出库区分是“出库”，那么直接将明细中的数量写到退库申请单据中。
            if(MessageConstants.STOCK_TYPE_IN.equals(ConvertUtil.getString(detailDataDTO.get("stockType")))){
                detailMap.put("Quantity", CherryUtil.obj2int(detailDataDTO.get("quantity"))*-1);
            }else{
                detailMap.put("Quantity", detailDataDTO.get("quantity"));
            }
            detailMap.put("Price", detailDataDTO.get("price"));
            detailMap.put("BIN_ProductVendorPackageID", "0");
            detailMap.put("BIN_InventoryInfoID", detailDataDTO.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataDTO.get("logicInventoryInfoID"));
            detailMap.put("BIN_InventoryInfoIDReceive", depotInfoIDReceive);
            detailMap.put("BIN_LogicInventoryInfoIDReceive", logicInventoryInfoIDReceive);
            detailMap.put("BIN_StorageLocationInfoID", "0");
            detailMap.put("Reason", detailDataDTO.get("reason"));
            detailMap.put("CreatedBy", map.get("createdBy"));
            detailMap.put("CreatePGM", map.get("createPGM"));
            detailMap.put("UpdatedBy", map.get("updatedBy"));
            detailMap.put("UpdatePGM", map.get("updatePGM"));
            returnReqDetailList.add(detailMap);
        }
        
        Map<String,Object> returnReqMainData = new HashMap<String,Object>();
        returnReqMainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        returnReqMainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        returnReqMainData.put("BillNo", map.get("tradeNoIF"));
        returnReqMainData.put("BillNoIF", map.get("tradeNoIF"));
        returnReqMainData.put("BIN_OrganizationID", map.get("organizationID"));
        returnReqMainData.put("BIN_InventoryInfoID", inventoryInfoID);
        returnReqMainData.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        returnReqMainData.put("BIN_OrganizationIDReceive", map.get("organizationIDReceive"));
        returnReqMainData.put("BIN_InventoryInfoIDReceive", depotInfoIDReceive);
        returnReqMainData.put("BIN_LogicInventoryInfoIDReceive", logicInventoryInfoIDReceive);
        returnReqMainData.put("BIN_EmployeeID", map.get("employeeID"));
        returnReqMainData.put("TotalQuantity", map.get("totalQuantity"));
        returnReqMainData.put("TotalAmount", map.get("totalAmount"));
        returnReqMainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
        returnReqMainData.put("TradeType", CherryConstants.OS_BILLTYPE_RA);
        returnReqMainData.put("Model", map.get("model"));
        //MQ格式字段名RelevantNo对应数据库字段名RelevanceNo
        returnReqMainData.put("RelevanceNo", !ConvertUtil.getString(map.get("relevantNo")).equals("")?map.get("relevantNo"):map.get("relevanceNo"));
        returnReqMainData.put("BIN_LogisticInfoID", "0");
        returnReqMainData.put("Reason", map.get("reason"));
        returnReqMainData.put("Comment", map.get("comment"));
        returnReqMainData.put("TradeDate", map.get("tradeDate"));
        returnReqMainData.put("CreatedBy", map.get("createdBy"));
        returnReqMainData.put("CreatePGM", map.get("createPGM"));
        returnReqMainData.put("UpdatedBy", map.get("updatedBy"));
        returnReqMainData.put("UpdatePGM", map.get("updatePGM"));
        
        UserInfo userInfo = new UserInfo();
        userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
        userInfo.setBrandCode(map.get("brandCode").toString());
        userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
        userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
        userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_RA);//业务类型
        mainData.put("BIN_EmployeeID", map.get("employeeID"));// 员工ID
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));// 员工ID
        mainData.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
                userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
        mainData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
        mainData.put("CurrentUnit", "MQ");//当前机能ID
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
        mainData.put("UserInfo", userInfo);
        mainData.put("OrganizationInfoCode", map.get("orgCode"));
        mainData.put("BrandCode", map.get("brandCode"));
        mainData.put("OrganizationCode", map.get("counterCode"));
        mainData.put("returnReqMainData", returnReqMainData);
        mainData.put("returnReqDetailList", returnReqDetailList);
        mainData.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
        mainData.put("BIN_OrganizationID", map.get("organizationID"));//部门ID
        //工作流开始
        binOLSTCM00_BL.StartOSWorkFlow(mainData);
    }
    
    /**
     * 对盘点申请单数据进行处理
     * @param map
     * @throws Exception 
     */
    @Override
    public void analyzeStocktakeRequestData(Map<String, Object> map) throws Exception {
        List<Map<String,Object>> detailDataDTOList = (List<Map<String,Object>>)map.get("detailDataDTOList");
        List<Map<String,Object>> crDetailList = new ArrayList<Map<String,Object>>();
        
        //查询用户表获得用户ID
        Map<String,Object> userMap = binBEMQMES99_Service.selUserByEempID(map);
       
        int inventoryInfoID = 0;
        int logicInventoryInfoID =0;
        //盘差总数量
        int totalQuantity = 0;
        //盘差总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<detailDataDTOList.size();i++){
            Map<String,Object> detailDataDTO = detailDataDTOList.get(i);
            if(i == 0){
                inventoryInfoID = CherryUtil.obj2int(detailDataDTO.get("inventoryInfoID"));
                logicInventoryInfoID = CherryUtil.obj2int(detailDataDTO.get("logicInventoryInfoID"));
            }
            String bookQuantity = ConvertUtil.getString(detailDataDTO.get("bookQuantity"));
            String gainQuantity = ConvertUtil.getString(detailDataDTO.get("gainQuantity"));
            //账面数量、盘差必填
            if(!CherryChecker.isPositiveAndNegative(bookQuantity)){
                MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_57);
            }
            if(!CherryChecker.isPositiveAndNegative(gainQuantity)){
                MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_58);
            }
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataDTO.get("productVendorID"));
            detailMap.put("BIN_ProductBatchID", null);
            detailMap.put("BookQuantity", bookQuantity);
            detailMap.put("CheckQuantity",CherryUtil.obj2int(bookQuantity)+CherryUtil.obj2int(gainQuantity));
            detailMap.put("GainQuantity", gainQuantity);
            detailMap.put("Price", detailDataDTO.get("price"));
            detailMap.put("BIN_ProductVendorPackageID", "0");
            detailMap.put("BIN_InventoryInfoID", detailDataDTO.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataDTO.get("logicInventoryInfoID"));
            detailMap.put("BIN_StorageLocationInfoID", "0");
            detailMap.put("Comments", detailDataDTO.get("reason"));
            detailMap.put("HandleType", detailDataDTO.get("handleType"));
            detailMap.put("CreatedBy", map.get("createdBy"));
            detailMap.put("CreatePGM", map.get("createPGM"));
            detailMap.put("UpdatedBy", map.get("updatedBy"));
            detailMap.put("UpdatePGM", map.get("updatePGM"));
            crDetailList.add(detailMap);
            
            totalQuantity += CherryUtil.obj2int(gainQuantity);
            BigDecimal price = new BigDecimal(0);
            if (detailDataDTO.get("price")!=null && !"".equals(detailDataDTO.get("price"))){
                price = new BigDecimal(Double.parseDouble((String)detailDataDTO.get("price")));
            }
            totalAmount = totalAmount.add(price.multiply(new BigDecimal(CherryUtil.obj2int(gainQuantity))));
        }
        
        Map<String,Object> crMainData = new HashMap<String,Object>();
        crMainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        crMainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        crMainData.put("StockTakingNoIF", map.get("tradeNoIF"));
        crMainData.put("RelevanceNo", map.get("relevanceNo"));
        crMainData.put("BIN_OrganizationID", map.get("organizationID"));
        crMainData.put("BIN_InventoryInfoID", inventoryInfoID);
        crMainData.put("BIN_LogicInventoryInfoID", logicInventoryInfoID);
        crMainData.put("BIN_EmployeeID", map.get("employeeID"));
        crMainData.put("TotalQuantity", totalQuantity);
        crMainData.put("TotalAmount", totalAmount);
        crMainData.put("VerifiedFlag", CherryConstants.AUDIT_FLAG_UNSUBMIT);
        crMainData.put("StocktakeType", map.get("subType"));
        crMainData.put("TradeType", CherryConstants.OS_BILLTYPE_CR);
        crMainData.put("IsBatch", "0");
        crMainData.put("Comments", map.get("reason"));
        crMainData.put("StockReason", map.get("stockReason"));
        crMainData.put("Date", map.get("tradeDate"));
        crMainData.put("TradeTime", map.get("tradeTime"));
        crMainData.put("CreatedBy", map.get("createdBy"));
        crMainData.put("CreatePGM", map.get("createPGM"));
        crMainData.put("UpdatedBy", map.get("updatedBy"));
        crMainData.put("UpdatePGM", map.get("updatePGM"));
        
        UserInfo userInfo = new UserInfo();
        userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
        userInfo.setBrandCode(map.get("brandCode").toString());
        userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
        userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
        userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
        
        Map<String,Object> mainData = new HashMap<String,Object>();
        mainData.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_CR);//业务类型
        mainData.put("BIN_EmployeeID", map.get("employeeID"));// 员工ID
        mainData.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));// 员工ID
        mainData.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
                userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
        mainData.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
        mainData.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));//部门ID
        mainData.put("CurrentUnit", "MQ");//当前机能ID
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
        mainData.put("UserInfo", userInfo);
        mainData.put("OrganizationInfoCode", map.get("orgCode"));
        mainData.put("BrandCode", map.get("brandCode"));
        mainData.put("OrganizationCode", map.get("counterCode"));
        mainData.put("stocktakeReqMainData", crMainData);
        mainData.put("stocktakeReqDetailList", crDetailList);
        mainData.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
        //工作流开始
        binOLSTCM00_BL.StartOSWorkFlow(mainData);
    }
    
    /**
     * 对盘点确认数据进行处理
     * @param map
     * @throws Exception 
     */
    @Override
    public void analyzeStocktakeConfirmData(Map<String, Object> map) throws Exception {
        //判断盘点申请流程
        String tradeNoIF = ConvertUtil.getString(map.get("tradeNoIF"));
        List<Map<String,Object>> proStocktakeRequest = binOLSTCM14_BL.selProStocktakeRequest(tradeNoIF);
        if(null != proStocktakeRequest && proStocktakeRequest.size()>0){
            //盘点申请流程
            String workFlowID = ConvertUtil.getString(proStocktakeRequest.get(0).get("WorkFlowID"));
            map.put("WorkFlowID", workFlowID);
            map.put("BIN_EmployeeIDAudit", ConvertUtil.getString(proStocktakeRequest.get(0).get("BIN_EmployeeIDAudit")));
            map.put("BIN_ProStocktakeRequestID", proStocktakeRequest.get(0).get("BIN_ProStocktakeRequestID"));
            // 盘点申请中的备注及盘点原因code同时写入到盘点单中
            if("".equals(ConvertUtil.getString(map.get("reason")))){
            	// MQ消息中备注为空则取盘点申请表中的原始数据
            	map.put("reason", proStocktakeRequest.get(0).get("Comments"));
            }
            // 盘点确认MQ中没有StockReason字段，故取盘点申请表中的原始数据
            map.put("stockReason", proStocktakeRequest.get(0).get("StockReason"));
            
            // 明细数据List
            List detailDataList = (List) map.get("detailDataDTOList");

            int productStockTakingID = this.proStocktakeRequest(detailDataList, map);
            long osID = Long.parseLong(workFlowID);
            PropertySet ps = workflow.getPropertySet(osID);
            ps.setInt("BIN_ProductStockTakingID", productStockTakingID);
            
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("WorkFlowID", workFlowID);
            param.put("BIN_ProductStockTakingID", productStockTakingID);
            param.put("TradeDateTime", map.get("tradeDateTime"));
            binOLSTCM14_BL.posConfirmCAFinishFlow(param);
            return;
        }
    }
    
    /**
     * 对积分兑换（无需预约）进行处理（Type=0014）
     * @param map
     * @throws Exception
     */
    @Override
    public void analyzePXData(Map<String, Object> map) throws Exception {
        // 设定交易时间
        map.put("saleTime", map.get("tradeDate")+" "+map.get("tradeTime"));
        // 交易类型
        map.put("saleType", MessageConstants.MSG_TRADETYPE_PX);
        // 新后台业务类型
        map.put("cherry_tradeType", MessageConstants.MSG_TRADETYPE_PX);
        //单据类型 0：积分兑换 1：表示整单全退，此时修改回数需要+1 
        String ticketType = ConvertUtil.getString(map.get("ticketType"));
        map.put("ticket_type", ticketType);
        //修改次数
        int mainModifyCounts = CherryUtil.obj2int(map.get("modifyCounts"));
        
        // 明细数据List
        List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailDataDTOList");
        
        //判断明细全部都是对冲数据
        boolean allOppositeFlag = true;
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = (HashMap<String,Object>)detailDataList.get(i);
            if(MessageConstants.DETAILTYPE_PAY.equals(ConvertUtil.getString(detailDataMap.get("detailType")))) {
            	// 支付方式不参与判断
            	continue;
            }
            //明细修改次数
            int deatilModifyCounts = CherryUtil.obj2int(detailDataMap.get("modifyCounts"));
            if(mainModifyCounts == deatilModifyCounts){
                allOppositeFlag = false;
                break;
            }
        }
        
        //设置销售下单者的相关信息
        setSaleCreatorInfo(map);
        
        // 判断单据类型是否为修改积分兑换
        if (ticketType.equals(MessageConstants.PX_TYPE_1)){
            // 修改销售单据的类型
            
            // 查询销售主表的数据
            Map historySaleMap = binBEMQMES02_Service.selSaleRecord(map);
            //
            if(historySaleMap==null){
                //积分兑换活动接收未成功时，却又进行修改积分兑换操作
                MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_66);
            }
            // 设定新后台单据号
            map.put("cherry_no", historySaleMap.get("cherry_no"));
            // 查询销售明细表的数据
            List historySaleDetailList = binBEMQMES02_Service.selSaleRecordDetail(historySaleMap);
            // 插入历史销售主表
            int saleRecordHistoryID = binBEMQMES02_Service.addSaleRecordHistory(historySaleMap);
            for (int i=0;i<historySaleDetailList.size();i++){
                Map historySaleDetailMap = (Map)historySaleDetailList.get(i);
                // 设置历史销售主表主ID
                historySaleDetailMap.put("saleRecordHistoryID", saleRecordHistoryID);
            }
            // 插入历史销售明细表
            binBEMQMES02_Service.addSaleDetailHistory(historySaleDetailList);
            
            // 删除原有的销售记录
            binBEMQMES02_Service.delSaleRecord(historySaleMap);
            binBEMQMES02_Service.delSaleRecordDetail(historySaleMap);
            
            //删除会员参与活动履历
            Map<String,Object> campaignHistory = new HashMap<String,Object>();
            campaignHistory.put("TradeNoIF", map.get("tradeNoIF"));
            campaignHistory.put("OrgCode", map.get("orgCode"));
            campaignHistory.put("BrandCode", map.get("brandCode"));
            binBEMQMES02_Service.delCampaignHistory(campaignHistory);
            
            if(allOppositeFlag){
                map.put("totalQuantity", "0");
                map.put("totalAmount", "0.00"); 
            }
        }else{
            // 单据号采番
            //this.getMQTicketNumber(map,"");
        	map.put("cherry_no", map.get("tradeNoIF"));
        }
        
        //Data_source 0:POS2，1:POS3，2：WITPOSmini
        String data_source = ConvertUtil.getString(map.get("data_source"));
        if("0".equals(data_source)){
            map.put("data_source", "POS2");
        }else if("1".equals(data_source)){
            map.put("data_source", "POS3");
        }else if("2".equals(data_source)){
            map.put("data_source", "WITPOSmini");
        }
        // 有俱乐部代号
        if (!CherryChecker.isNullOrEmpty(map.get("clubCode"))) {
        	// 查询会员俱乐部ID
        	map.put("memberClubId", binBEMQMES02_Service.selMemClubId(map));
        }
        // 插入销售数据主表
        int saleRecordID = binBEMQMES02_Service.addSaleRecord(map);
        
        List<Map<String,Object>> saleRecordDetailList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> campaignHistoryList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> salePayList = new ArrayList<Map<String,Object>>();
        int detailNo = 1;
        Map<String,Object> memberAndMainCodeMap = new HashMap<String,Object>();
        //会员号与会员ID对应关系
        Map<String,Object> memberMap = new HashMap<String,Object>();
        //ActivityCode与campaignCode对应关系
        Map<String,Object> campaignCodeMap = new HashMap<String,Object>();
        
        Map<String,Object> campaignDataMap = new HashMap<String,Object>();
        campaignDataMap.put("map", map);
        campaignDataMap.put("memberAndMainCodeMap", memberAndMainCodeMap);
        campaignDataMap.put("campaignCodeMap", campaignCodeMap);
        campaignDataMap.put("campaignHistoryList", campaignHistoryList);
        campaignDataMap.put("memberMap", memberMap);
        campaignDataMap.put("allOppositeFlag", allOppositeFlag);
        
        //给明细设置相关的值
        for (int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = (HashMap<String,Object>)detailDataList.get(i);
            // 设定销售主表ID
            detailDataMap.put("saleRecordID", saleRecordID);
            // MQ明细消息中的amount如果是正常商品，表示单价；如果是促销品，表示总价,即单据*数量；如果是支付方式，表示支付金额
            detailDataMap.put("price", detailDataMap.get("amount"));
            String detailType = ConvertUtil.getString(detailDataMap.get("detailType"));
            // 提取支付方式后
            if(MessageConstants.DETAILTYPE_PAY.equals(detailType)){
            	String payTypeID = ConvertUtil.getString(detailDataMap.get("payTypeID"));
                String payTypeCode = ConvertUtil.getString(detailDataMap.get("payTypeCode"));
                if("0".equals(data_source) && "".equals(payTypeCode)){
                    //机器类型为POS2，且支付方式代号为空时，调用共通取出全部Codes值，
                    //根据orgCode、brandCode、codeType的组合取出Code值List，找不到组织、品牌的取-9999再次取，
                    //最后根据级别（payTypeID）取得对应的payTypeCode。
                    //这里取Key的逻辑和CodeTable.propKeyByGrade(String codeType,Object grade)一致，只是不使用session。
                    Map<String,Object> codesMap = code.getCodesMap();
                    String orgCode = ConvertUtil.getString(map.get("orgCode"));
                    String brandCode = ConvertUtil.getString(map.get("brandCode"));
                    String codeType = "1175";
                    String combKey = orgCode + "_" + brandCode + "_" + codeType;
                    if (!codesMap.containsKey(combKey)) {
                        // 组合key(品牌Code为全体共通)
                        combKey = orgCode + "_" + CherryConstants.Brand_CODE_ALL + "_" + codeType;
                        if (!codesMap.containsKey(combKey)) {
                            // 组合key(组织Code和品牌Code为全体共通)
                            combKey = CherryConstants.ORG_CODE_ALL + "_" + CherryConstants.Brand_CODE_ALL + "_" + codeType;
                        }
                    }
                    List<Map<String,Object>> codeList = (List<Map<String, Object>>) codesMap.get(combKey);
                    for(int j=0;j<codeList.size();j++){
                        Map<String,Object> codeMap = (Map<String,Object>) codeList.get(j);
                        if(payTypeID.equals(ConvertUtil.getString(codeMap.get("grade")))) {
                            // Key
                            Object codeKeyObj = codeMap.get("codeKey");
                            if (null != codeKeyObj) {
                                payTypeCode = String.valueOf(codeKeyObj);
                                break;
                            }
                        }
                    }
                    detailDataMap.put("payTypeCode", payTypeCode);
                }
                //设置支付方式明细的序号
                detailDataMap.put("detailNo", salePayList.size()+1);
                this.setInsertInfoMapKey(detailDataMap);
                Map<String, Object> salePayMap = new HashMap<String, Object>(detailDataMap);
                salePayList.add(salePayMap);
                
                // 排除掉支付方式
            	detailDataList.remove(i);
                i--;
                continue;
            } 
            //给除支付方式外每条明细都设置会员卡号，便于查询Campaign相关信息时调用共通处理。
            detailDataMap.put("memberCodeDetail", map.get("memberCode"));
            
            String activityMainCode = ConvertUtil.getString(detailDataMap.get("activityMainCode"));
            String activityCode = ConvertUtil.getString(detailDataMap.get("activityCode"));
            //产品里也可能有mainCode、activityCode，所以从促销的逻辑里移出
            //mainCode、activityCode两个里面存在一个就写会员活动履历表
            if(!"".equals(activityMainCode) || !"".equals(activityCode)){
                campaignDataMap.put("activityCode", activityCode);
                campaignDataMap.put("activityMainCode", activityMainCode);
                campaignDataMap.put("detailDataMap", detailDataMap);
                setCampaignValue(campaignDataMap);
            }
            if(MessageConstants.PX_TYPE_0.equals(ticketType)){
                detailDataMap.put("stockType", MessageConstants.STOCK_TYPE_OUT);
            }else{
                detailDataMap.put("stockType", MessageConstants.STOCK_TYPE_IN);
            }
            
            if(detailType.equals(MessageConstants.DETAILTYPE_PROMOTION)){
                detailDataMap.put("promotion_price", detailDataMap.get("amount"));
                if(allOppositeFlag){
                    detailDataMap.put("price", "0.00");
                    detailDataMap.put("promotion_price", "0.00");
                }
            }
        }

        //筛选符合的明细插入销售明细(detailDataList中已经排除了支付方式的明细)
        List<Map<String,Object>> cloneDetailList = (List<Map<String,Object>>) ConvertUtil.byteClone(detailDataList);
        
		for (int i = 0; i < cloneDetailList.size(); i++) {
			Map<String, Object> detailDataMap = (HashMap<String, Object>) cloneDetailList.get(i);
            //明细修改次数
            int deatilModifyCounts = CherryUtil.obj2int(detailDataMap.get("modifyCounts"));
            
            if(mainModifyCounts == deatilModifyCounts || allOppositeFlag){
                //当明细全部是对冲数据时，插入数量为0的明细。
                if(allOppositeFlag){
                    detailDataMap.put("modifyCounts", mainModifyCounts);
                    detailDataMap.put("quantity", "0");
                }
                detailDataMap.put("detailNo", detailNo);
                saleRecordDetailList.add(detailDataMap);
                detailNo ++; 
            }
        }
        
        // 插入销售数据明细表
        binBEMQMES02_Service.addSaleRecordDetail(saleRecordDetailList);

        //插入销售支付构成表
        if(null != salePayList && !salePayList.isEmpty()){
            binBEMQMES02_Service.addSalePayList(salePayList);
        }
        
        if (MessageConstants.PX_TYPE_0.equals(ticketType)){
            //积分兑换
            if(null != campaignHistoryList && !campaignHistoryList.isEmpty()){
                //插入会员参与活动履历表
                binBEMQMES02_Service.addCampaignHistory(campaignHistoryList);
            }
        }
    }
    
    /**
     * 对积分兑换（无需预约）数据进行库存处理
     * @param map
     * @throws Exception
     */
    @Override
    public void analyzePXStockData(Map<String, Object> map) throws Exception {
        List<Map<String,Object>> detailDataList = (List<Map<String,Object>>)map.get("detailDataDTOList");
        List<Map<String,Object>> cloneDetailList = (List<Map<String,Object>>) ConvertUtil.byteClone(detailDataList);
        
        // 设定入出库关联单据号
        map.put("stockInOut_relevantNo", map.get("tradeNoIF"));

        // 设定入出库区分
        if (map.get("ticketType")!=null){
            if (MessageConstants.PX_TYPE_0.equals(((String)map.get("ticketType")))){
                map.put("stockInOutType", MessageConstants.STOCK_TYPE_OUT);
            }else{
                map.put("stockInOutType", MessageConstants.STOCK_TYPE_IN);
            }
        }
        
        // 产品入出库处理
        this.addProductStockInfoForSale(cloneDetailList, map);
        
        // 产品库存处理
        this.operateProductStockDataForSale(cloneDetailList, map);
    }
    
    /**
     * 检查必填
     * @param map
     * @param dataMap
     * @param fieldName
     * @throws CherryMQException
     */
    private void checkRequired(Map<String,Object> map,Map<String,Object> dataMap,String fieldName) throws CherryMQException{
        String keyName = fieldName;
        if (fieldName.length() == 1) {
            keyName = fieldName.toLowerCase();
        } else if (!Character.isUpperCase(fieldName.charAt(1))) {
            keyName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        }
        
        String fieldValue = ConvertUtil.getString(dataMap.get(keyName));
        if(fieldValue.equals("")){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_74, fieldName));
        }
    }
    
    /**
     * 更改单据状态
     * @param map
     * @throws Exception
     */
    @Override
    public void analyzeChangeSaleState(Map<String, Object> map) throws Exception {
        checkRequired(map,map,"ChangeTime");
        checkRequired(map,map,"DataSource");
        
        //查询条件
        DBObject condition = new BasicDBObject();
        condition.put("TradeNoIF", map.get("tradeNoIF"));
        condition.put("BrandCode", map.get("brandCode"));
        //condition.put("TradeType", map.get("tradeType"));
        //查询结果字段
        DBObject keys = new BasicDBObject();
        keys.put("ModifyCounts", 1);
        keys.put("OccurTime", 1);
        keys.put("TradeType", 1);
        //排序值 =1 升序，=-1 降序
        DBObject orderBy = new BasicDBObject();
        //orderBy.put("ModifyCounts", -1);
        orderBy.put("OccurTime", -1);
        List<DBObject> businessLogList = MongoDB.find(MessageConstants.MQ_BUS_LOG_COLL_NAME, condition,keys,orderBy,0,0);
        String lastTime = "";
        if(null != businessLogList && businessLogList.size()>0){
            lastTime = ConvertUtil.getString(businessLogList.get(0).get("OccurTime"));
        }
        //时序控制
        String changeTime = ConvertUtil.getString(map.get("changeTime"));
        if(!CherryChecker.checkDate(changeTime, CherryConstants.DATE_PATTERN_24_HOURS)){
            MessageUtil.addMessageWarning(map, String.format(MessageConstants.MSG_ERROR_75, "ChangeTime", "YYYY-MM-DD HH:mm:SS"));
        }
        map.put("tradeDate", changeTime.split(" ")[0]);
        map.put("tradeTime", changeTime.split(" ")[1]);
        if(lastTime.equals("") || DateUtil.coverString2Date(changeTime).getTime()-DateUtil.coverString2Date(lastTime).getTime()>0){
           setInsertInfoMapKey(map);
           String comment = ConvertUtil.getString(map.get("comment"));
           String cherryclear = CherryConstants.CHERRY_CLEAR.toLowerCase();
           if(comment.toLowerCase().equals(cherryclear)){
               map.put("comment", cherryclear);
           }
           int cnt = binBEMQMES02_Service.updSaleRecordBillState(map);
           if(cnt == 0){
               MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_77);
           }
       }
    }
    
	/**
	 * 插入产品入出库表,入出库明细表
	 * @param detailDataList
	 * @param map
	 * @param organizationInfoID
	 * @param brandInfoID
	 */
	private void addProductStockInfo (List detailDataList,Map map){
		
		// 判断是否是修改销售记录
		String ticketType = (String)map.get("ticket_type");
		// 入出库时间
		map.put("stockInOutTime", (String)map.get("tradeDate")+" "+(String)map.get("tradeTime"));
		if (ticketType!=null && ticketType.equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
			// 单据类型为修改销售记录
			
			// 查询产品入出库主表的数据
			Map historyStockInOutMap = binBEMQMES02_Service.selProductInOut(map);
			// 设定新后台入出库单据号
//			map.put("stockInOut_tradeNo", historyStockInOutMap.get("stockInOut_tradeNo"));
//			map.put("stockInOut_tradeNoIF", historyStockInOutMap.get("stockInOut_tradeNoIF"));
			if(historyStockInOutMap!=null){
				// 查询产品入出库明细表的数据
				List historyStockInOutDetailList = binBEMQMES02_Service.selProductInOutDetail(historyStockInOutMap);
				if(historyStockInOutDetailList!=null){
					// 插入历史产品入出库主数据表
					int productInOutHistoryID = binBEMQMES02_Service.addProductInOutHistory(historyStockInOutMap);
					for (int i=0;i<historyStockInOutDetailList.size();i++){
						Map historyStockInOutDetailMap = (Map)historyStockInOutDetailList.get(i);
						// 设置历史产品入出库主表主ID
						historyStockInOutDetailMap.put("productInOutHistoryID", productInOutHistoryID);
					}
					// 插入历史产品入出库明细表
					binBEMQMES02_Service.addProductInOutDetailHistory(historyStockInOutDetailList);
			   }
			}
//			// 删除原有的产品入出库记录
//			binBEMQMES02_Service.delProductInOut(historyStockInOutMap);
//			binBEMQMES02_Service.delProductInOutDetail(historyStockInOutMap);
		}
		
        //当明细数量全是0，入出库主表从表都不插入，其他情况只插入数量不是0的明细
        for(int i=0;i<detailDataList.size();i++){
            HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
            boolean isNoStockFlag = detailDataDTOMap.get("isStock")!=null&&detailDataDTOMap.get("isStock").equals(MessageConstants.Stock_IS_NO)?true:false;
            if(CherryUtil.obj2int(detailDataDTOMap.get("quantity")) == 0){
                detailDataList.remove(i);
                i--;
                continue;
            }else if(isNoStockFlag){
                //根据产品明细MQ的IsStock，过滤属性为”否“的促销品。
                detailDataList.remove(i);
                i--;
                continue;
            }
        }
        if(detailDataList.size()==0){
            return;
        }
		
		// 新后台单据号采番
		this.getMQTicketNumber(map, MessageConstants.BUSINESS_TYPE_GR);
		
		//礼品领用主表已有，不需要再取消息体的接口单据号
//		if (MessageConstants.BUSINESS_TYPE_SP.equals(map.get("cherry_tradeType"))){
//			map.put("stockInOut_tradeNoIF", map.get("tradeNoIF"));
//		}
		
		// 设定关联单据号
		//map.put("stockInOut_relevantNo", map.get("cherry_no"));
		
		// 插入产品入出库表
		int productInOutID = binBEMQMES02_Service.addProductInOut(map);
		// 循环明细数据List
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			// 设定产品入出库记录ID
			detailDataDTOMap.put("productInOutID", productInOutID);
			
			detailDataDTOMap.put("detailNo", i+1);
			
			//对修改销售记录，对冲数据中的reason设为“原单对冲”
			if(map.get("modifyCounts")!=null&&detailDataDTOMap.get("modifyCounts")!=null&&
					!map.get("modifyCounts").equals(detailDataDTOMap.get("modifyCounts"))){
				detailDataDTOMap.put("reason", "原单对冲");
			}
		}
		// 批量插入产品入出库明细表
		binBEMQMES02_Service.addProductInOutDetail(detailDataList);
		
        // 是否记录产品入出库成本
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        boolean isConfigOpen = binOLCM14_BL.isConfigOpen("1365", organizationInfoID, brandInfoID);
        if(isConfigOpen){
        	// 将产品入出库批次信息写入入出库批次主从表，并处理产品批次库存表（入库、出库）。
        	this.handleProductInOutBatchForSale(detailDataList,map);
        }
	}
	
    /**
     * 插入产品入出库表,入出库明细表(新消息体Type=0007)
     * @param detailDataList
     * @param map
     * @param organizationInfoID
     * @param brandInfoID
     * @throws Exception 
     */
    private void addProductStockInfoForSale(List detailDataList,Map map) throws Exception{
        // 判断是否是修改销售记录
        String ticketType = ConvertUtil.getString(map.get("ticket_type"));
        // 入出库时间
        map.put("stockInOutTime", (String)map.get("tradeDate")+" "+(String)map.get("tradeTime"));
        Map<Integer,Object> quantityMap = new HashMap<Integer,Object>();
        String closeFlag = ""; 
        String old_stockInOut_tradeNo = "";
        String old_stockInOut_tradeNoIF = "";
        if(ticketType.equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE) || ticketType.equals(MessageConstants.PX_TYPE_1)){
            // 单据类型为修改销售记录
            
            // 查询产品入出库主表的数据
            Map<String,Object> historyStockInOutMap = binBEMQMES02_Service.selProductInOut(map);
            // 设定新后台入出库单据号
//          map.put("stockInOut_tradeNo", historyStockInOutMap.get("stockInOut_tradeNo"));
//          map.put("stockInOut_tradeNoIF", historyStockInOutMap.get("stockInOut_tradeNoIF"));
            if(historyStockInOutMap!=null){
                // 查询产品入出库明细表的数据
                List<Map<String,Object>> historyStockInOutDetailList = binBEMQMES02_Service.selProductInOutDetail(historyStockInOutMap);
                
                closeFlag = ConvertUtil.getString(historyStockInOutMap.get("closeFlag"));
                if(historyStockInOutDetailList!=null){
                    if(CherryConstants.CLOSEFLAG_NO.equals(closeFlag)){
                        //原入出库单据还未进行过月度库存计算
                        // 插入历史产品入出库主数据表
                        this.setInsertInfoMapKey(historyStockInOutMap);
                        int productInOutHistoryID = binBEMQMES02_Service.addProductInOutHistory(historyStockInOutMap);
                        for (int i=0;i<historyStockInOutDetailList.size();i++){
                            Map<String,Object> historyStockInOutDetailMap = (Map<String,Object>)historyStockInOutDetailList.get(i);
                            int productVendorID = CherryUtil.obj2int(historyStockInOutDetailMap.get("productVendorID"));
                            // 设置历史产品入出库主表主ID
                            historyStockInOutDetailMap.put("productInOutHistoryID", productInOutHistoryID);
                            this.setInsertInfoMapKey(historyStockInOutDetailMap);
                            
                            String stockType = ConvertUtil.getString(historyStockInOutDetailMap.get("stockType"));
                            int quantity = CherryUtil.obj2int(historyStockInOutDetailMap.get("quantity"));
                            if(stockType.equals(CherryConstants.STOCK_TYPE_OUT)){
                                quantity = quantity*-1;
                            }
                            quantityMap.put(productVendorID, quantity);
                        }
                        // 插入历史产品入出库明细表
                        binBEMQMES02_Service.addProductInOutDetailHistory(historyStockInOutDetailList);
                        
                        // 删除原有的产品入出库记录
                        binBEMQMES02_Service.delProductInOut(historyStockInOutMap);
                        binBEMQMES02_Service.delProductInOutDetail(historyStockInOutMap);
                        
                        old_stockInOut_tradeNo = ConvertUtil.getString(historyStockInOutMap.get("stockInOut_tradeNo"));
                        old_stockInOut_tradeNoIF = ConvertUtil.getString(historyStockInOutMap.get("stockInOut_tradeNoIF"));
                    }else{
                        //原入出库单据已经进行过月度库存计算
                        //原单对冲
                        String oldBillNoIF = ConvertUtil.getString(historyStockInOutMap.get("stockInOut_tradeNoIF"));
                        String reason = "原单("+oldBillNoIF+")对冲";
                        int totalQuantity = 0;
                        BigDecimal totalAmount = new BigDecimal(0);
                        for(int i=0;i<historyStockInOutDetailList.size();i++){
                            Map<String,Object> temp = (Map<String,Object>)historyStockInOutDetailList.get(i);
                            int quantity = CherryUtil.obj2int(temp.get("quantity"))*-1;
                            BigDecimal amount = new BigDecimal(Double.parseDouble(ConvertUtil.getString(temp.get("price"))));
                            temp.put("quantity", quantity);
                            temp.put("reason", reason);
                            totalQuantity += quantity;
                            totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
                        }
                        // 入出库对冲单据采番
                        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
                        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
                        String stockInOut_tradeNo = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES02", "7");
                        historyStockInOutMap.put("stockInOut_tradeNo", stockInOut_tradeNo);
                        historyStockInOutMap.put("stockInOut_tradeNoIF", stockInOut_tradeNo);
                        historyStockInOutMap.put("totalQuantity", totalQuantity);
                        historyStockInOutMap.put("totalAmount", totalAmount);
                        historyStockInOutMap.put("reason", reason);
                        this.setInsertInfoMapKey(historyStockInOutMap);
                        int billID = binBEMQMES02_Service.addProductInOut(historyStockInOutMap);
                        for(int i=0;i<historyStockInOutDetailList.size();i++){
                            Map<String,Object> temp = (Map<String,Object>)historyStockInOutDetailList.get(i);
                            temp.put("productInOutID", billID);
                            this.setInsertInfoMapKey(temp);
                            int quantity = CherryUtil.obj2int(temp.get("quantity"));
                            String stockType = ConvertUtil.getString(temp.get("stockType"));
                            if(stockType.equals(CherryConstants.STOCK_TYPE_IN)){
                                quantity = quantity*-1;
                            }
                            int productVendorID = CherryUtil.obj2int(temp.get("productVendorID"));
                            quantityMap.put(productVendorID, quantity);
                        }
                        binBEMQMES02_Service.addProductInOutDetail(historyStockInOutDetailList);
                    }
                }
            }
        }
        
        String mainModifyCounts = ConvertUtil.getString(map.get("modifyCounts"));
        //把新单存在的产品ID都放到Map，便于判断是否排除在对冲单存在，在新单不存在的明细。
        Map<String,Object> existNewDetailMap = new HashMap<String,Object>();
        for(int i=0;i<detailDataList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) detailDataList.get(i);
            String detailModifyCounts = ConvertUtil.getString(detailDataDTOMap.get("modifyCounts"));
            if(mainModifyCounts.equals(detailModifyCounts)){
                String productVendorID = ConvertUtil.getString(detailDataDTOMap.get("productVendorID"));
                existNewDetailMap.put(productVendorID, null);
            }
        }
        
        //当明细数量全是0，入出库主表从表都不插入，其他情况只插入数量不是0的明细
        //排除对冲明细
        int totalQuantity = 0;
        BigDecimal totalAmount = new BigDecimal(0);
        for(int i=0;i<detailDataList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) detailDataList.get(i);
            int quantity = CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
            String price = ConvertUtil.getString(detailDataDTOMap.get("price"));
            String detailModifyCounts = ConvertUtil.getString(detailDataDTOMap.get("modifyCounts"));
            String productVendorID = ConvertUtil.getString(detailDataDTOMap.get("productVendorID"));
            boolean isNoStockFlag = detailDataDTOMap.get("isStock")!=null&&detailDataDTOMap.get("isStock").equals(MessageConstants.Stock_IS_NO)?true:false;
            if(!mainModifyCounts.equals(detailModifyCounts)){
                if(existNewDetailMap.containsKey(productVendorID)){
                    detailDataList.remove(i);
                    i--;
                    continue;
                }else{
                    detailDataDTOMap.put("quantity", 0);
                }
            }
            if(isNoStockFlag){
                //根据产品明细的IsStock，过滤值为0不管理的促销品。
                detailDataList.remove(i);
                i--;
                continue;
            }
            
            totalQuantity += quantity;
            if(null != price && !"".equals(price)){
                BigDecimal amount = new BigDecimal(Double.parseDouble(price));
                totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
            }
            
            //修改前入出库数量
            detailDataDTOMap.put("preQuantity", CherryUtil.obj2int(quantityMap.get(CherryUtil.obj2int(detailDataDTOMap.get("productVendorID")))));
            //修改后入出库数量
            detailDataDTOMap.put("postQuantity", CherryUtil.obj2int(detailDataDTOMap.get("quantity")));
        }
        
        //移除数量为0的明细
        List<Map<String,Object>> cloneDetailList = (List<Map<String,Object>>) ConvertUtil.byteClone(detailDataList);
        for(int i=0;i<cloneDetailList.size();i++){
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) cloneDetailList.get(i);
            int quantity = CherryUtil.obj2int(detailDataDTOMap.get("quantity"));
            if(quantity == 0){
                cloneDetailList.remove(i);
                i--;
                continue;
            }
        }
        if(cloneDetailList.size()==0){
            return;
        }
        
        DecimalFormat df=new DecimalFormat("#0.00");
        String strTotalAmount = df.format(totalAmount);
        map.put("totalQuantity", totalQuantity);
        map.put("totalAmount", strTotalAmount);
        
        if(CherryConstants.CLOSEFLAG_NO.equals(closeFlag) && !"".equals(old_stockInOut_tradeNo)){
            //如果原入出库单据还没有进行过月度库存计算，单据号和原单一样。
            map.put("stockInOut_tradeNo", old_stockInOut_tradeNo);
            map.put("stockInOut_tradeNoIF", old_stockInOut_tradeNoIF);
        }else{
            // 新后台单据号采番
            this.getMQTicketNumber(map, MessageConstants.BUSINESS_TYPE_GR);     
        }

        // 插入产品入出库表
        int productInOutID = binBEMQMES02_Service.addProductInOut(map);
        // 循环明细数据List
        int detailNo = 1;
        for (int i = 0; i < cloneDetailList.size(); i++) {
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) cloneDetailList.get(i);
            // 设定产品入出库记录ID
            detailDataDTOMap.put("productInOutID", productInOutID);
            detailDataDTOMap.put("detailNo", detailNo);
            detailNo++;
        }
        // 批量插入产品入出库明细表
        binBEMQMES02_Service.addProductInOutDetail(cloneDetailList);
        
        // 是否记录产品入出库成本
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoID"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoID"));
        boolean isConfigOpen = binOLCM14_BL.isConfigOpen("1365", organizationInfoID, brandInfoID);
        if(isConfigOpen){
        	// 将产品入出库批次信息写入入出库批次主从表，并处理产品批次库存表（入库、出库）。
        	this.handleProductInOutBatchForSale(cloneDetailList,map);
        }
        
    }
    
    /**
     * 将产品入出库批次信息写入入出库批次主从表，并处理产品批次库存表（入库、出库）。
     * @param detailDataList
     * @param mainData
     */
    private void handleProductInOutBatchForSale(List<Map<String,Object>> detailDataList, Map map){
    	
        Map<String,Object> mainData = new HashMap<String,Object>();
        List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
        mainData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        
        mainData.put("RelevanceNo",map.get("stockInOut_relevantNo"));
        mainData.put("TradeNo", map.get("stockInOut_tradeNo"));
        mainData.put("TradeNoIF", map.get("stockInOut_tradeNoIF"));
        
        mainData.put("BIN_OrganizationID", map.get("organizationID"));
        mainData.put("BIN_EmployeeID", map.get("employeeID"));
        mainData.put("TotalQuantity", map.get("totalQuantity"));
        mainData.put("TotalAmount", map.get("totalAmount"));
        mainData.put("Amount", map.get("Amount"));
        mainData.put("StockType", map.get("stockInOutType"));
        mainData.put("TradeType",  map.get("cherry_tradeType"));
//      mainData.put("BIN_LogisticInfoID", map.get(""));
        mainData.put("Comments", map.get("reason"));
        mainData.put("StockInOutDate", map.get("tradeDate"));
        mainData.put("StockInOutTime", map.get("stockInOutTime"));
        mainData.put("VerifiedFlag", map.get("verifiedFlag"));
//        mainData.put("TotalAmountBefore", map.get("totalAmountBefore"));
//        mainData.put("TotalAmountAfter", map.get("totalAmountAfter"));
//      mainData.put("CloseFlag", "");
//      mainData.put("ChangeCount", "");
//      mainData.put("WorkFlowID", map.get(""));
        mainData.put("stockInOut_SrRelevantNo", map.get("stockInOut_SrRelevantNo"));
        
        mainData.put("CreatedBy", map.get("createdBy"));
        mainData.put("CreatePGM", map.get("createPGM"));
        mainData.put("UpdatedBy", map.get("updatedBy"));
        mainData.put("UpdatePGM", map.get("updatePGM"));
        
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = detailDataList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID" +
            		""));
//          detailMap.put("BIN_ProductBatchID", "");
            detailMap.put("DetailNo", detailDataMap.get("detailNo"));
            detailMap.put("Quantity", detailDataMap.get("quantity"));
            detailMap.put("Price", detailDataMap.get("price"));
//          detailMap.put("BIN_ProductVendorPackageID", detailDataMap.get(""));
            detailMap.put("StockType", detailDataMap.get("stockType"));
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
//          detailMap.put("BIN_StorageLocationInfoID", detailDataMap.get(""));
            detailMap.put("Comments", detailDataMap.get("reason"));
//          detailMap.put("ChangeCount", detailDataMap.get(""));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailList.add(detailMap);
        }
    	
    	binOLSTCM01_BL.handleProductInOutBatch(mainData, detailList);
    }
	
	/**
	 * 操作产品库存数据
	 * 
	 * @param detailDataList
	 * @param map
	 */
	private void operateProductStockData(List detailDataList, Map map) {
		// 处理库存明细表
		for (int i = 0; i < detailDataList.size(); i++) {
		    Map detailDataDTOMap = (HashMap) detailDataList.get(i);
		    int quantity = 0,updQuantity=0;
			
			if (detailDataDTOMap.get("quantity")!=null && !"".equals(detailDataDTOMap.get("quantity"))){
				quantity = Integer.parseInt(String.valueOf(detailDataDTOMap.get("quantity")));
			}
			if (MessageConstants.STOCK_TYPE_OUT.equals(detailDataDTOMap.get("stockType"))) {
				updQuantity -= quantity;
			} else if (MessageConstants.STOCK_TYPE_IN.equals(detailDataDTOMap.get("stockType"))) {
				updQuantity += quantity;
			}
			detailDataDTOMap.put("updQuantity", updQuantity);
			int k = binBEMQMES02_Service.updProductStock(detailDataDTOMap);
			if (k==0){
				detailDataDTOMap.put("stockQuantity", updQuantity);
				binBEMQMES02_Service.addProductStock(detailDataDTOMap);
			}
			
		}
	}
	
    /**
     * 操作产品库存数据(新消息体Type=0007)
     * 
     * @param detailDataList
     * @param map
     */
    private void operateProductStockDataForSale(List detailDataList, Map map) {
        // 处理库存明细表
        for (int i = 0; i < detailDataList.size(); i++) {
            HashMap<String,Object> detailDataDTOMap = (HashMap<String,Object>) detailDataList.get(i);
            int updQuantity=0;
            //修改后入出库数量-修改前入出库数量
            int postQuantity = CherryUtil.obj2int(detailDataDTOMap.get("postQuantity"));
            int preQuantity = CherryUtil.obj2int(detailDataDTOMap.get("preQuantity"));
            if (MessageConstants.STOCK_TYPE_OUT.equals(detailDataDTOMap.get("stockType"))) {
                updQuantity = postQuantity*-1 - preQuantity;
            } else if (MessageConstants.STOCK_TYPE_IN.equals(detailDataDTOMap.get("stockType"))) {
                updQuantity = postQuantity - preQuantity;
            }
            detailDataDTOMap.put("updQuantity", updQuantity);
            int k = binBEMQMES02_Service.updProductStock(detailDataDTOMap);
            if (k==0){
                detailDataDTOMap.put("stockQuantity", updQuantity);
                binBEMQMES02_Service.addProductStock(detailDataDTOMap);
            }
        }
    }
	
	/**
	 * 取得新后台自定义单据号
	 * @param cherry_tradeType
	 */
	public void getMQTicketNumber(Map<String,Object> map,String cherry_tradeType){
		// 取得组织ID
		String organizationInfoID = String.valueOf(map.get("organizationInfoID"));
		// 取得品牌ID
		String brandInfoID = String.valueOf(map.get("brandInfoID"));
		if (cherry_tradeType == null || "".equals(cherry_tradeType)){
			cherry_tradeType = (String)map.get("cherry_tradeType");
			// 新后台单据号采番
			String cherry_no = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES99", cherry_tradeType);
			map.put("cherry_no", cherry_no);
		}else if (MessageConstants.BUSINESS_TYPE_GR.equals(cherry_tradeType)){
			String cherry_no = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES99", cherry_tradeType);
			map.put("stockInOut_tradeNo", cherry_no);
//			if (!MessageConstants.MSG_STOCK_IN_OUT.equals(map.get("tradeType"))){
			map.put("stockInOut_tradeNoIF", cherry_no);
//			}else{
//				map.put("stockInOut_tradeNoIF", map.get("tradeNoIF"));
//			}
		}
	}

	/**
	 * 根据MQ消息里的主数据及明细数据查询在新后台对应的数据
	 * 注：对于明细的处理加入了ProductId字段的处理
	 */
	@Override
	public void selMessageInfo(Map map) throws Exception {
		this.setInsertInfoMapKey(map);
		// 取得部门信息
		HashMap resultMap = binBEMQMES99_Service.selCounterDepartmentInfo(map);

		if (resultMap!=null && resultMap.get("organizationID") != null) {
			// 设定部门ID
			map.put("organizationID", resultMap.get("organizationID"));
			// 设定柜台名称
			map.put("counterName", resultMap.get("counterName"));
			// 云POS 
			map.put("posFlag", resultMap.get("posFlag"));
			// 所属渠道ID
			map.put("pushChannelId", resultMap.get("channelId"));
			// 所属渠道名称
			map.put("pushChannelName", resultMap.get("channelName"));
		} else {
			// 没有查询到相关部门信息
			MessageUtil.addMessageWarning(map,"柜台号为\""+map.get("counterCode")+"\""+MessageConstants.MSG_ERROR_06);
		}


        //是否启用柜台积分计划，默认为否
        String isOpenSysConfig = binOLCM14_BL.getConfigValue("1396", ConvertUtil.getString(map.get("organizationInfoID")),  ConvertUtil.getString(map.get("brandInfoID")));
        if(isOpenSysConfig.equals("1")){//启用
            HashMap pointPlanMap =  binBEMQMES99_Service.getCounterPointPlan(map);//判断是否有正在进行的柜台积分计划
            if(pointPlanMap!=null){//需要算积分
                map.put("isPoint","");
            }else{
                map.put("isPoint","0");//不需要算积分
            }

            //关联退货的时候以关联的销售单为准，销售单算积分退货也算积分，销售单不算积分退货也不算积分
            if(!CherryChecker.isNullOrEmpty(map.get("saleSRtype"))){
                if((ConvertUtil.getString(map.get("saleSRtype"))).equals("2")){//表示关联退货
                    if(!CherryChecker.isNullOrEmpty(map.get("relevantNo"))){
                        HashMap saleRecordMap =  binBEMQMES99_Service.getSaleRecordByBillCode(map);//得到退货关联的销售单信息
                        if(null!=saleRecordMap){
                            map.put("isPoint",ConvertUtil.getString(saleRecordMap.get("IsPoint")));
                        }
                    }

                }
            }
        }

		// 取得关联部门信息
		if (map.get("relevantCounterCode")!=null && !"".equals(map.get("relevantCounterCode"))){
			HashMap parameterMap = new HashMap();
			// 组织ID
			parameterMap.put("organizationInfoID", map.get("organizationInfoID"));
			// 品牌ID
			parameterMap.put("brandInfoID", map.get("brandInfoID"));
			// 关联柜台号
			parameterMap.put("counterCode", map.get("relevantCounterCode"));
			// 查询关联柜台部门信息
			HashMap relevantResultMap = binBEMQMES99_Service.selCounterDepartmentInfo(parameterMap);
			if (relevantResultMap!=null && !"".equals(relevantResultMap.get("organizationID"))){
				map.put("relevantOrganizationID", relevantResultMap.get("organizationID"));				
				map.put("relevantCounterName", relevantResultMap.get("relevantCounterName"));
			}else{
				// 查询办事处部门信息
				relevantResultMap = binBEMQMES99_Service.selOfficeDepartmentInfo(parameterMap);
				if (relevantResultMap!=null && !"".equals(relevantResultMap.get("organizationID"))){
					map.put("relevantOrganizationID", relevantResultMap.get("organizationID"));
				}
			}	
		}
		//对于线下提货的特殊处理
        //库存标记
        String stockFlag = ConvertUtil.getString(map.get("stockFlag"));
        //原始数据来源
        String originalDataSource = ConvertUtil.getString(map.get("originalDataSource"));
		//实际处理库存的柜台
        String stockCounter = ConvertUtil.getString(map.get("stockCounter")); 
        //单据模式
        String billModel= ConvertUtil.getString(map.get("billModel")); 
		// 取得指定扣减库存部门信息
    	// 实际处理库存的柜台
		if (!ConvertUtil.isBlank(stockCounter)){
			Map<String,Object> parameterMap = new HashMap<String,Object>();
			// 组织ID
			parameterMap.put("organizationInfoID", map.get("organizationInfoID"));
			// 品牌ID
			parameterMap.put("brandInfoID", map.get("brandInfoID"));
			// 关联柜台号
			parameterMap.put("counterCode", stockCounter);
			// 查询关联柜台部门信息
			Map<String,Object> stockResultMap = binBEMQMES99_Service.selCounterDepartmentInfo(parameterMap);
			
			if (stockResultMap!=null && !"".equals(stockResultMap.get("organizationID"))){
				map.put("stockOrganizationID", stockResultMap.get("organizationID"));				
			} else{
				// 没有查询到相关部门信息
				MessageUtil.addMessageWarning(map,"实际处理库存的柜台号为\""+stockCounter+"\""+MessageConstants.MSG_ERROR_06);
			}
		}
		
		// 明细数据List
		List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailDataDTOList");
		
		Map<String, Object> detailDataMap = new HashMap<String, Object>();
		if(null != detailDataList && detailDataList.size()>0) {
			detailDataMap.putAll(detailDataList.get(0));
		}
        //同时兼容在主数据行或明细数据行的BAcode
		//判断map里是否已有BAcode且不为空，如果没有取【第一条】明细数据行的BAcode，放到map里。
        //例如PX积分兑换的BAcode在主数据行，明细数据行里没有BAcode，而其他消息体的BAcode一般都在明细数据行。
        String baCode = ConvertUtil.getString(map.get("BAcode"));
		if("".equals(baCode)){
		    baCode = ConvertUtil.getString(detailDataMap.get("BAcode"));
            map.put("BAcode", baCode);
        }else if(null != detailDataList && detailDataList.size()>0){
            //如果明细没有BAcode，给该条明细设置为主数据的BAcode。
            for(int i=0;i<detailDataList.size();i++){
                String curBACode = ConvertUtil.getString(detailDataList.get(i).get("BAcode"));
                if("".equals(curBACode)){
                    detailDataList.get(i).put("BAcode", baCode);
                }
            }
        }
		// 查询员工信息
		resultMap = binBEMQMES99_Service.selEmployeeInfo(map);
		if (resultMap != null && resultMap.get("employeeID") != null) {
			// 设定员工ID
			map.put("employeeID", resultMap.get("employeeID"));
			// 设定BA姓名
			map.put("BAname", resultMap.get("BAname"));
			//设定岗位ID
			map.put("positionCategoryID", resultMap.get("positionCategoryID"));
			// 设定BA岗位
			map.put("categoryName", resultMap.get("categoryName"));
		} else {
			// 没有查询到相关员工信息
			MessageUtil.addMessageWarning(map,"员工号为\""+map.get("BAcode")+"\""+MessageConstants.MSG_ERROR_07);
		}

		// 查询仓库信息
		List<Map<String, Object>> list = binOLCM18_BL.getDepotsByDepartID(map.get("organizationID").toString(), "");
		if(list!=null && list.size()>0){
			resultMap = (HashMap) list.get(0);
			// 设定实体仓库ID
			map.put("inventoryInfoID", resultMap.get("BIN_DepotInfoID"));
		}
		if("".equals(ConvertUtil.getString(map.get("inventoryInfoID")))){
			// 没有查询到相关仓库信息
			MessageUtil.addMessageWarning(map,"柜台号为\""+map.get("counterCode")+"\""+MessageConstants.MSG_ERROR_36);
		}
		map.put("inventoryTypeCode", detailDataMap.get("inventoryTypeCode"));
//		//获得逻辑仓库
//	    list = binBEMQMES99_Service.selLogicInventoryInfoList(map);
//		if(list!=null&&list.size()>0){
//			resultMap = (HashMap) list.get(0);
//			// 设定逻辑仓库ID
//			map.put("logicInventoryInfoID", resultMap.get("logicInventoryInfoID"));
//		}
//		if(map.get("logicInventoryInfoID")==null||map.get("logicInventoryInfoID").equals("")){
//			// 没有查询到相关逻辑仓库信息
//			MessageUtil.addMessageWarning(map,"逻辑仓库为\""+map.get("inventoryTypeCode")+"\""+MessageConstants.MSG_ERROR_37);
//		}
		
		// 查询关联部门仓库信息
		if (!"".equals(ConvertUtil.getString(map.get("relevantOrganizationID")))){
			HashMap parameterMap = new HashMap();
			// 组织ID
			parameterMap.put("organizationInfoID", map.get("organizationInfoID"));
			// 品牌ID
			parameterMap.put("brandInfoID", map.get("brandInfoID"));
			// 关联部门ID
			parameterMap.put("organizationID", map.get("relevantOrganizationID"));
//			HashMap relevantResultMap = binBEMQMES99_Service.selPrmStockInfo(parameterMap);
//			// 设定关联部门实体仓库ID
//			if(relevantResultMap!=null)
//			   map.put("relevantInventoryInfoID", relevantResultMap.get("inventoryInfoID"));
			// 查询仓库信息
			list = binOLCM18_BL.getDepotsByDepartID(parameterMap.get("organizationID").toString(), "");
			if(list.size()>0){
				resultMap = (HashMap) list.get(0);
				// 设定关联部门实体仓库ID
				map.put("relevantInventoryInfoID", resultMap.get("BIN_DepotInfoID"));
			}
		}
		

        //实际处理库存的柜台处理库存
		if (!ConvertUtil.isBlank(stockCounter)){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				// 组织ID
				paramMap.put("organizationInfoID", map.get("organizationInfoID"));
				// 品牌ID
				paramMap.put("brandInfoID", map.get("brandInfoID"));
				// 关联部门ID
				paramMap.put("organizationID", map.get("stockOrganizationID"));
				// 查询仓库信息
				list = binOLCM18_BL.getDepotsByDepartID(paramMap.get("organizationID").toString(), "");
				if(list.size()>0){
					resultMap = (HashMap) list.get(0);
					// 设定关联部门实体仓库ID
					map.put("stockInventoryInfoID", resultMap.get("BIN_DepotInfoID"));
				}
			}
		
        
		// 设定明细数据
		if(null != detailDataList && !detailDataList.isEmpty()) {
			// 设定明细数据【主要是产品明细数据,支付方式不处理，优惠券信息改为促销品来处理】
			/**
		 	* 此处增加了对productId字段的处理，
		 	* 需要detailType IN('N','P')且系统配置项【是否支持产品、促销品下发】打开时才支持此字段
		 	* 注：产品与促销品的配置项是分开的，可部分支持这此字段。
		 	*/
			this.setDetailDataInfo(detailDataList, map);
		} else if(MessageConstants.MSG_STOCK_TAKING.equals(ConvertUtil.getString(map.get("tradeType")))) {
			// 盘点单在无明细的情况下只接收主信息，并且将备注设置为【没有盘差】
			map.put("reason", MessageConstants.NULLSTOCKTAKING_COMMENTS);
		}
		
		String consumerType = ConvertUtil.getString(map.get("consumer_type"));
		boolean isMember = true;//是否会员标志
		if (map.get("memberCode")!=null && !"".equals(map.get("memberCode")) && !MessageConstants.ON_MEMBER_CARD.equals(map.get("memberCode"))){
			// 取得会员数据
			resultMap = binBEMQMES99_Service.selMemberInfo(map);
			if (resultMap != null && resultMap.get("memberInfoID") != null) {
				// 设定会员ID
				map.put("memberInfoID", resultMap.get("memberInfoID"));
				map.put("memName", resultMap.get("memName"));
			}else{
				// 如果该消息的类型为销售数据、积分兑换（无需预约）
				if (map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_SALE)){
					// 会员记录假登录
					int memberInfoID = binBEMQMES99_Service.addMemberInfo(map);
					map.put("grantDate", ((String)map.get("tradeDate")).replace("-", ""));
					map.put("memberInfoID", memberInfoID);
					// 添加会员持卡信息
					binBEMQMES99_Service.addMemCardInfo(map);
				}else if(map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_PX)){
				    // 积分兑换（无需预约）
				    // 没有查询到相关会员信息
	                MessageUtil.addMessageWarning(map, "会员号为\""+map.get("memberCode")+"\""+MessageConstants.MSG_ERROR_34);
				}
			}
		}else{
		    isMember = false;
		}
		
        if(consumerType.equals(MessageConstants.ConsumerType_PF)){
            //消费者类型为PF，写入新后台销售表的值还是PF。
        }else{
            //消费者类型不是PF，写入新后台销售表的值按是否会员有处理，会员MP，非会员NP。
            if(isMember){
                //会员卡号为其它的值，则ConsumerType='MP'
                map.put("consumer_type", MessageConstants.ConsumerType_MP);
            }else{
                //会员卡号为空或者为9个0，ConsumerType='NP'
                map.put("consumer_type", MessageConstants.ConsumerType_NP);
            }
        }
		
		//在此处设定插入MongoDB所需要的值，以免在其他地方被清除
		map.put("mongoTotalQuantity", map.get("totalQuantity"));//总金额
		map.put("mongoTotalAmount", map.get("totalAmount"));//总数量
		// 审核区分
		map.put("verifiedFlag", MessageConstants.AUDIT_FLAG_AGREE);
		if (map.get("machineCode")!=null && !"".equals(map.get("machineCode"))){
			// 机器号查询
			resultMap = binBEMQMES99_Service.selMachinCode(map);
			// 机器ID设定
			if(null != resultMap && resultMap.get("machineInfoID") != null){
			    map.put("machineInfoID", resultMap.get("machineInfoID"));
			}
		}
//		judgeIfIsRepeatSale(map);
	}

	@Override
	public void setDetailDataInfo(List detailDataList, Map map) throws Exception {
		String tradeDate = ConvertUtil.getString(map.get("tradeDate"));
		String tradeTime = ConvertUtil.getString(map.get("tradeTime"));
		String tradeType = ConvertUtil.getString(map.get("tradeType"));

		if (!"".equals(tradeDate) && !"".equals(tradeTime)) {
			// 设定交易时间
			map.put("tradeDateTime",map.get("tradeDate")+" "+map.get("tradeTime"));
		}
		// 循环明细数据List
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			// 明细类型，N：产品；P:促销品；Y：支付方式；BC：BA优惠劵【目前仅薇诺娜有用】
			String detailType = ConvertUtil.getString(detailDataDTOMap.get("detailType")).toUpperCase();
			/**
			 *  支持productId字段的原始明细类型(detailType)：N：产品；P:促销品；
			 *  其他类型不支持【"BC"即使后面作为"P"处理也不支持productId】
			 */
			boolean isSupportProductId = (MessageConstants.DETAILTYPE_PRODUCT
					.equals(detailType) || MessageConstants.DETAILTYPE_PROMOTION
					.equals(detailType));
			
			// 新增的厂商ID，此字段有值且系统配置项【是否支持产品下发、是否支持促销品下发】配置为【是】时以此id为准，需要联合unitcode、barcode进行产品的校验。
			String productId = ConvertUtil.getString(detailDataDTOMap.get("productId"));
			
			if(MessageConstants.DETAILTYPE_PAY.equals(detailType)){
	            // 明细为支付方法不处理
	            continue;
			}else if(MessageConstants.DETAILTYPE_BC.equals(detailType)){
			    // 明细为代理商优惠券则作为促销品来处理【注：此明细不支持ProductId】
			    detailType = MessageConstants.DETAILTYPE_PROMOTION;
			}
			// 设定部门ID
			detailDataDTOMap.put("organizationID", map.get("organizationID"));
			// 设定时间【查询产品及促销品时有用】
			detailDataDTOMap.put("tradeDateTime", map.get("tradeDateTime"));

			// 设定来自MQ消息体的IsStock，【新销售退货】或【礼品领用】的消息体才会有这个字段
			detailDataDTOMap.put("isStockMQ", detailDataDTOMap.get("isStock"));
			
			// 【新销售退货】，当OrderID不为空时isStock才有效，其它情况下忽略该字段。
			if(MessageConstants.MSG_TRADETYPE_SALE.equals(tradeType)){
			    String orderID = ConvertUtil.getString(detailDataDTOMap.get("orderID"));
	            if("".equals(orderID)){
	                detailDataDTOMap.put("isStock",null);
	            }
			}
			
			/**
			 * 1、老逻辑：detailType=''，先将code作为促销品去检索，没有找到后再作为产品去检索，最终没有找到则报错。
             *            detailType='P',将code作为促销品去检索，没有找到直接报错而不再作为产品去检索（detailType已明确此条明细为促销品）
             *            detalType='N',跳过检索促销品的逻辑直接作为产品去检索，没有找到则报错（detailType已明确此条明细为产品）
			 *
             * 2、新逻辑：系统配置项（分产品与促销品的配置项，需要打开），detailType in('N','P')，productId有值。
             * 				以【unitCode,barCode,productId】为准查询条码对照表，检索不到数据直接报错，退出程序。
			 * 
			 * 新增了productId字段，此字段有值时以此字段为准。系统配置项【是否支持产品、促销品下发】
			 * 1、在新后台管理产品：
			 * 		1）有此字段值，新逻辑
			 * 		2）无此字段值，老逻辑
			 * 2、不在新后台管理产品【通过第三方导入产品，新后台自己生成厂商ID且不下发到终端】：
			 * 		1）老逻辑，因为productId不在新后台维护，可能有差异，故不看此字段。
			 * 
			 */
			// 是否在新后台管理产品【即终端产品是从新后台下发的】
			boolean isOpenProductId = isSupportProductId && binOLCM14_BL.isConfigOpen("1295", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
			// 是否在新后台管理促销品【即终端促销品是新后台下发的】
			boolean isOpenPromotionId = isSupportProductId && binOLCM14_BL.isConfigOpen("1296", String.valueOf(map.get("organizationInfoID")), String.valueOf(map.get("brandInfoID")));
			// 标记是否走了新逻辑
			boolean isNewFlow = false;
			// 查询商品信息（产品或者促销品）
			Map<String, Object> resultMap = null;
			
			// productId有值且【产品】在新后台管理
			if(isOpenProductId && !"".equals(productId) && MessageConstants.DETAILTYPE_PRODUCT.equals(detailType)) {
				// 产品的情况，对unitCode,barCode,productId进行校验，若在产品条码关系表（即历史表）中则取到其厂商ID后返回
				resultMap = binBEMQMES99_Service.getProductInfoByIdAndCode(detailDataDTOMap);
				isNewFlow = true;
				// 在历史表中若不存在
				if(resultMap == null || resultMap.isEmpty()) {
					 // 没有查询到相关商品信息，程序将终止并抛出异常
                    MessageUtil.addMessageWarning(map, "产品厂商ID为\""+productId+"\"厂商编码为\""+detailDataDTOMap.get("unitcode")+"\"产品条码为\""+detailDataDTOMap.get("barcode")+"\""+MessageConstants.MSG_ERROR_09);
				}
			} 
			// productId有值且【促销品】在新后台管理
			if(isOpenPromotionId && !"".equals(productId) && MessageConstants.DETAILTYPE_PROMOTION.equals(detailType)) {
				// 促销品的情况，对unitCode,barCode,productId进行校验，若在促销品条码关系表（即历史表）中则取到其厂商ID后返回
				// 对于促销品需要额外地获取当前促销品的IsStock字段值。
				resultMap = binBEMQMES99_Service.getPrmPrtInfoByIdAndCode(detailDataDTOMap);
				isNewFlow = true;
				if(resultMap == null || resultMap.isEmpty()) {
					// 没有查询到相关商品信息，程序将终止并抛出异常
                    MessageUtil.addMessageWarning(map, "促销品厂商ID为\""+productId+"\"厂商编码为\""+detailDataDTOMap.get("unitcode")+"\"促销品条码为\""+detailDataDTOMap.get("barcode")+"\""+MessageConstants.MSG_ERROR_09);
				}
			} 
			
			// detailType值为空时(不知道productId代表的是产品还是促销品)，还是延用好的处理逻辑（使用code去检索）：
			if(!isNewFlow && ("".equals(detailType) || MessageConstants.DETAILTYPE_PROMOTION.equals(detailType))){
	            resultMap = binBEMQMES99_Service.selPrmProductInfo(detailDataDTOMap);
				if (resultMap == null || resultMap.get("promotionProductVendorID") == null) {
	                 resultMap = binBEMQMES99_Service.selPrmProductPrtBarCodeInfo(detailDataDTOMap);
	                 Map<String,Object> temp = new HashMap<String,Object>();
	                
	                 if(resultMap!=null){//促销品信息unitcode或barcode存在变更
	                     temp.put("promotionProductVendorID", resultMap.get("promotionProductVendorID"));
	                     temp.put("organizationID", detailDataDTOMap.get("organizationID"));
	                     resultMap = binBEMQMES99_Service.selPrmProductInfoByPrmVenID(temp);
	                     if(resultMap==null){
	                         // 查询促销产品信息 根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
	                         List list = binBEMQMES99_Service.selPrmAgainByPrmVenID(temp);
	                         if(list!=null&&!list.isEmpty()){
	                             resultMap = (HashMap)list.get(0);
	                         }else{
	                             //查询促销产品信息  根据促销产品厂商ID，不区分有效状态
	                             list = binBEMQMES99_Service.selPrmByPrmVenID(temp);
	                             if(list!=null&&!list.isEmpty()){
	                                 resultMap = (HashMap)list.get(0);
	                             }
	                         }
	                     }
	                 }else{
	                     //在促销产品条码对应关系表里找不到，放开时间条件再找一次，还是找不到的接下来查产品
	                     List<Map<String,Object>> prmPrtBarCodeList = binBEMQMES99_Service.selPrmPrtBarCodeList(detailDataDTOMap);
	                     if(null != prmPrtBarCodeList && prmPrtBarCodeList.size()>0){
	                         //取tradeDateTime与StartTime最接近的第一条
	                         temp.put("promotionProductVendorID", prmPrtBarCodeList.get(0).get("promotionProductVendorID"));
	                         //查询促销产品信息  根据促销产品厂商ID，不区分有效状态
	                         List list = binBEMQMES99_Service.selPrmByPrmVenID(temp);
	                         if(list!=null&&!list.isEmpty()){
	                             resultMap = (HashMap)list.get(0);
	                         }
	                     }
	                 }
	                 //明细里detailType为促销品时，resultMap为null抛错
                     if((null == resultMap || resultMap.get("promotionProductVendorID") == null) && MessageConstants.DETAILTYPE_PROMOTION.equals(detailType)){
                         // 没有查询到相关商品信息
                         MessageUtil.addMessageWarning(map, "厂商编码为\""+detailDataDTOMap.get("unitcode")+"\"促销品条码为\""+detailDataDTOMap.get("barcode")+"\""+MessageConstants.MSG_ERROR_09);
                     }
	            }
			}

			detailDataDTOMap.put("organizationInfoID", map.get("organizationInfoID"));
			detailDataDTOMap.put("brandInfoID", map.get("brandInfoID"));
			
            //当明细中的数量、金额为空时设为0
            if("".equals(ConvertUtil.getString(detailDataDTOMap.get("quantity")))){
                detailDataDTOMap.put("quantity","0");
            }
            if("".equals(ConvertUtil.getString(detailDataDTOMap.get("price")))){
                detailDataDTOMap.put("price","0");
            }
			
			if (resultMap == null || resultMap.get("promotionProductVendorID") == null){
				/**
				 * 1、resultMap == null：
				 * 			1）有productId字段且支持对其处理（系统配置项控制）,resultMap为空时会被挡回，不会到此逻辑。
				 * 			2）有productId字段但是不支持对其处理，走老的逻辑，此时通过code没有找到促销品信息就将其认为是产品去查询相应的产品信息（取得产品厂商ID）
				 * 			3) 无productId字段，即使支持对productId的处理，也是走的老逻辑（使用code先查询促销品，找不到时再当作产品查询）
				 * 2、 resultMap.get("promotionProductVendorID") == null：（注：【2）、3）】的情况已经在【1、】有体现，这里只是说明一下）
				 * 			1）有productId字段且支持对其处理（系统配置项控制），但是查询到的是产品的信息，此时resultMap不为空（不会再重复去检索），则只是标记此条信息为产品。
				 * 			2）有productId字段但是不支持对其处理，走老的逻辑，此时通过code没有找到促销品信息就将其认为是产品去查询相应的产品信息（取得产品厂商ID）
				 * 			3）无productId字段，即使支持对productId的处理，也是走的老逻辑（使用code先查询促销品，找不到时再当作产品查询）
				 */
				// 该商品为正常产品
				detailDataDTOMap.put("isPromotion", "0");

				// 在产品条码对应表中未找到产品信息，用code查询产品信息
				if(resultMap == null || "".equals(ConvertUtil.getString(resultMap.get("productVendorID")))) {
					// 不再过滤无效的产品
					resultMap = binBEMQMES99_Service.selProductInfo(detailDataDTOMap);
				}
				
				// 若没有找到则再去查询产品条码对应关系表中的产品数据
				if (resultMap == null || "".equals(ConvertUtil.getString(resultMap.get("productVendorID")))){
					// 查找对应的产品条码对应关系表【业务时间在起止时间内】
					resultMap = binBEMQMES99_Service.selPrtBarCode(detailDataDTOMap);
					Map<String,Object> temp = new HashMap<String,Object>();
					
					if(resultMap != null){
						temp.put("productVendorID", resultMap.get("productVendorID"));
						// 根据产品条码，继续查找产品表【确定此产品必须是有效的】
						resultMap = binBEMQMES99_Service.selProductInfoByPrtVenID(temp);
						// 若此产品为非有效，需要再次查询
						if(resultMap == null){
							/**
							 * 之所以要再次去查询是因为：
							 * 	在一品多码【一个产品ID对就多条厂商ID,即一个unitcode对应多个barcode】时，
							 * 		需要根据厂商ID先确定产品ID,再通过产品ID去查找另外的有效的厂商ID
							 */
							 // 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID
							 List list = binBEMQMES99_Service.selProAgainByPrtVenID(temp);
							 if(list!=null&&!list.isEmpty()){
								 resultMap = (HashMap)list.get(0);
							 }else{
							     resultMap = new HashMap();
							     resultMap.put("productVendorID", temp.get("productVendorID"));
							 }
						}
					}
                    if(resultMap==null){
                        // 在产品条码对应关系表里找不到，放开时间条件再找一次，找不到抛错
                        List<Map<String,Object>> prtBarCodeList = binBEMQMES99_Service.selPrtBarCodeList(detailDataDTOMap);
                        if(null != prtBarCodeList && prtBarCodeList.size()>0){
                            //取tradeDateTime与StartTime最接近的第一条
                            resultMap = new HashMap();
                            resultMap.put("productVendorID", prtBarCodeList.get(0).get("productVendorID"));
                        }else{
                            // 没有查询到相关商品信息
                            MessageUtil.addMessageWarning(map, "厂商编码为\""+detailDataDTOMap.get("unitcode")+"\"产品条码为\""+detailDataDTOMap.get("barcode")+"\""+MessageConstants.MSG_ERROR_09);
                        }
                    }
				}
				// 设定产品厂商ID
				detailDataDTOMap.put("productVendorID",resultMap.get("productVendorID"));
				// 设定销售类型
				if (tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)
				        || tradeType.equals(MessageConstants.MSG_TRADETYPE_PX)){
					detailDataDTOMap.put("saleType", MessageConstants.SALE_TYPE_NORMAL_SALE);
				}
			} else if (resultMap != null && resultMap.get("promotionProductVendorID") != null){
				// 该商品为促销品
				detailDataDTOMap.put("isPromotion", "1");
				// 设定销售类型
				if (tradeType.equals(MessageConstants.MSG_TRADETYPE_SALE)
				        || tradeType.equals(MessageConstants.MSG_TRADETYPE_PX)){
					detailDataDTOMap.put("saleType", MessageConstants.SALE_TYPE_PROMOTION_SALE);
				}
				// 促销产品厂商ID
				detailDataDTOMap.put("promotionProductVendorID", resultMap.get("promotionProductVendorID"));
				
				// 设定促销产品是否管理库存【新老逻辑都已取得了促销品的是否管理库存字段】
				if("".equals(ConvertUtil.getString(detailDataDTOMap.get("isStockMQ")))){
					detailDataDTOMap.put("isStock", resultMap.get("isStock"));
				}
				
				// 促销产品价格
				detailDataDTOMap.put("promotion_price", detailDataDTOMap.get("price"));
				// 促销品调拨价格
				detailDataDTOMap.put("allocation_price", detailDataDTOMap.get("price"));
				
                // 促销品分类【新老逻辑都已取得了促销品的是否管理库存字段】
                detailDataDTOMap.put("PromotionCateCD", resultMap.get("PromotionCateCD"));
			} else {
				// 没有查询到相关商品信息
				MessageUtil.addMessageWarning(map, "厂商编码为\""+detailDataDTOMap.get("unitcode")+"\"产品条码为\""+detailDataDTOMap.get("barcode")+"\""+MessageConstants.MSG_ERROR_09);
			}
			
			
			// 明细连番
			detailDataDTOMap.put("detailNo", i + 1);
			// 员工ID
			detailDataDTOMap.put("employeeID", map.get("employeeID"));
			// 实体仓库ID
			detailDataDTOMap.put("inventoryInfoID", map.get("inventoryInfoID"));

            String subType = ConvertUtil.getString(map.get("subType"));
            
            if("KS".equals(tradeType) && ("".equals(subType) || "SD".equals(subType) || "2".equals(subType) || "3".equals(subType))){
                //KS类型(发货、订货拒绝、退库申请拒绝)，可以没有逻辑仓库。
            }else{
                //逻辑仓库
                Map<String,Object> logicInventoryInfoMap = new HashMap<String,Object>();
                logicInventoryInfoMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
                logicInventoryInfoMap.put("LogicInventoryCode", detailDataDTOMap.get("inventoryTypeCode"));
                logicInventoryInfoMap.put("Type", "1");//终端逻辑仓库 
                logicInventoryInfoMap.put("language", null);
                Map<String, Object> logicInventoryInfo = binOLCM18_BL.getLogicDepotByCode(logicInventoryInfoMap);
                if(logicInventoryInfo != null && !logicInventoryInfo.isEmpty()){
                    int logicInventoryInfoID = CherryUtil.obj2int(logicInventoryInfo.get("BIN_LogicInventoryInfoID"));
                    // 设定逻辑仓库ID
                    detailDataDTOMap.put("logicInventoryInfoID", logicInventoryInfoID);
                }else{
                    // 没有查询到相关逻辑仓库信息
                    MessageUtil.addMessageWarning(map,"逻辑仓库为\""+detailDataDTOMap.get("inventoryTypeCode")+"\""+MessageConstants.MSG_ERROR_37);
                }
            }
			
			// 设定关联部门实体仓库ID
			if (!"".equals(ConvertUtil.getString(map.get("relevantInventoryInfoID")))){
				detailDataDTOMap.put("relevantInventoryInfoID", map.get("relevantInventoryInfoID"));
			}
			
			this.setInsertInfoMapKey(detailDataDTOMap);
			
			//设定包装类型ID  仓库库位ID
			detailDataDTOMap.put("productVendorPackageID", 0);
			detailDataDTOMap.put("storageLocationInfoID", 0);
		}
	}

	@Override
	public void setInsertInfoMapKey(Map map) {
		map.put("createdBy", "BINBEMQMES02");
		map.put("createPGM", "BINBEMQMES02");
		map.put("updatedBy", "BINBEMQMES02");
		map.put("updatePGM", "BINBEMQMES02");
	}

	@Override
	public void addMongoMsgInfo(Map map) throws CherryMQException {
			DBObject dbObject = new BasicDBObject();
			// 组织代码
			dbObject.put("OrgCode", map.get("orgCode"));
			// 品牌代码，即品牌简称
			dbObject.put("BrandCode", map.get("brandCode"));
			// 业务类型
			dbObject.put("TradeType", map.get("tradeType"));
			// 单据号
			dbObject.put("TradeNoIF", map.get("tradeNoIF"));
			// 修改次数
			dbObject.put("ModifyCounts", map.get("modifyCounts")==null
					||map.get("modifyCounts").equals("")?"0":map.get("modifyCounts"));
			if(MessageConstants.MSG_TRADETYPE_SALE.equals(map.get("tradeType"))
					||MessageConstants.MSG_BIR_PRESENT.equals(map.get("tradeType"))){
				// 业务主体
			    dbObject.put("TradeEntity", "0");
			}else{
				// 业务主体
			    dbObject.put("TradeEntity", "1");
			}
			// 业务主体代号
			dbObject.put("TradeEntityCode", map.get("memberCode")==null
					||map.get("memberCode").equals("")?"":map.get("memberCode"));
			// 业务主体名称
			dbObject.put("TradeEntityName", map.get("memName")==null
					||map.get("memName").equals("")?"":map.get("memName"));
			//员工代码
			dbObject.put("UserCode", map.get("BAcode"));
			//员工名称
			dbObject.put("UserName", map.get("BAname"));
			//岗位名称名称
			dbObject.put("UserPost", map.get("categoryName"));
			// 柜台名称
			// 柜台号
			dbObject.put("DeptCode", map.get("counterCode"));
			// 柜台名称
			dbObject.put("DeptName", map.get("counterName"));
			// 发生时间
			dbObject.put("OccurTime", (String)map.get("tradeDate")+" "+(String)map.get("tradeTime"));
			// 总数量
			dbObject.put("TotalQuantity", map.get("mongoTotalQuantity")==null?"":map.get("mongoTotalQuantity").toString());
			// 总金额
			dbObject.put("TotalAmount", map.get("mongoTotalAmount")==null?"":map.get("mongoTotalAmount").toString());
			if(map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_SALE)){
				   if(null==map.get("ticket_type") || !map.get("ticket_type").equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
					  if (map.get("saleSRtype")!=null){
							if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
								// 日志正文
								dbObject.put("Content", "销售");
						    }else{
						    	// 日志正文
								dbObject.put("Content", "退货");
						    }
					  }
				   }else if(null!=map.get("ticket_type") && map.get("ticket_type").equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
					    // 日志正文
						dbObject.put("Content", "修改销售");
				   }
			}else if(map.get("tradeType").equals(MessageConstants.MSG_STOCK_IN_OUT)){
				  String stockType = String.valueOf(map.get("stockInOutType"));
				  if (stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
					   // 日志正文
					   dbObject.put("Content", "出库");
				  }else{
					   // 日志正文
					   dbObject.put("Content", "入库");
				  }
			}else if(map.get("tradeType").equals(MessageConstants.MSG_ALLOCATION_IN)){
				// 日志正文
				dbObject.put("Content", "调入申请");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_ALLOCATION_OUT)){
				// 日志正文
				dbObject.put("Content", "调出确认");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_STOCK_TAKING)){
				// 日志正文
				dbObject.put("Content", "盘点");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_BIR_PRESENT)){
				// 日志正文
				dbObject.put("Content", "礼品领用(生日)");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_PRO_ORDER)){
				// 日志正文
				dbObject.put("Content", "产品订货");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_KS_DELIVER)){
	             // 日志正文
                dbObject.put("Content", "第三方导入发货单");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_ProductShift)){
			    // 日志正文
			    dbObject.put("Content", "移库");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_ReturnRequest)){
			    // 日志正文
			    dbObject.put("Content", "退库申请");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_StocktakeRequest)){
                // 日志正文
			    dbObject.put("Content", "盘点申请");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_StocktakeConfirm)){
			    // 日志正文
			    dbObject.put("Content", "盘点确认");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_STOCK_HB)){
			    // 日志正文
			    dbObject.put("Content", "合并库存");
			}else if(map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_PX)){
			    if(null!=map.get("ticketType") && map.get("ticketType").equals(MessageConstants.PX_TYPE_0)){
			        // 日志正文
	                dbObject.put("Content", "积分兑换（无需预约）");
			    }else if(null!=map.get("ticketType") && map.get("ticketType").equals(MessageConstants.PX_TYPE_1)){
	                 // 日志正文
                    dbObject.put("Content", "积分兑换取消（无需预约）");
			    }
            }else if(map.get("tradeType").equals(MessageConstants.MSG_CHANGESALESTATE)){
                // 日志正文
               dbObject.put("Content", "更改单据状态");
            }else if(map.get("tradeType").equals(MessageConstants.MSG_ALLOCATION_IN_CONFRIM)){
                // 日志正文
               dbObject.put("Content", "调入确认");
            }
			map.put("dbObject", dbObject);
//			binBEMQMES99_Service.addMongoDBBusLog(dbObject);
	}
//	/**
//	 * 判断是否是重复的数据
//	 * 
//	 * @param map
//	 * @throws CherryMQException 
//	 */
//	public void judgeIfIsRepeatSale(Map<String,Object> map) throws CherryMQException{
////		if(null!=map.get("tradeDate")&&null!=map.get("tradeTime")){
////		    String tradeDateTime=(String)map.get("tradeDate")+" "+(String)map.get("tradeTime");
////			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////			try {
////				Date d1 = df.parse(tradeDateTime);
////				map.put("tradeDateTime",d1);
////			} catch (ParseException e) {
////				e.printStackTrace();
////		    }
////		}
//		if(map.get("tradeType").equals(MessageConstants.MSG_TRADETYPE_SALE)){//判断是否是重复的销售/退货数据
//			   if(null==map.get("ticket_type") || !map.get("ticket_type").equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
//				  if (map.get("saleSRtype")!=null){
//					// 查询销售主表的数据
//					int count =  binBEMQMES02_Service.selSaleRecordByTradeTime(map);
//					if(count>0){
//						if (MessageConstants.SR_TYPE_SALE.equals(((String)map.get("saleSRtype")))){
//							// 是重复的正常销售 
//						    MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_17);
//					    }else{
//					    	// 是重复的正常退货
//					    	MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_18);
//					    }
//					}
//				  }
//			   }else if(null!=map.get("ticket_type") && map.get("ticket_type").equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
//					// 查询销售主表的数据
//					int count =  binBEMQMES02_Service.selSaleRecordByTradeTime(map);
//					if(count>0){
//						//是重复的修改销售记录
//						MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_19);
//					}
//			   }
//		}else if(map.get("tradeType").equals(MessageConstants.MSG_STOCK_IN_OUT)){//判断是否是重复的入库/退库数据
//			  HashMap detailDataDTO = (HashMap) ((List) map.get("detailDataDTOList")).get(0);
//			  String stockType = String.valueOf(detailDataDTO.get("stockType"));
//			  if (stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
//				 // 退库
//				 int count =  binBEMQMES02_Service.selProductReturnByNoIF(map);
//				 if(count>0){
//					//是重复的退库数据
//					MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_20);
//				 }
//			  }else{
//				 // 入库
//				 int count =  binBEMQMES02_Service.selProductReceiveByNoIF(map);
//				 if(count>0){
//					//是重复的入库数据
//					MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_21);
//				 }
//			  }
//		}else if(map.get("tradeType").equals(MessageConstants.MSG_ALLOCATION_IN)){//判断是否是重复的调入申请数据
//			 int count =  binBEMQMES02_Service.selAllocationByAllocationNoIF(map);
//			 if(count>0){
//				//是重复的调入申请数据
//				MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_22);
//			 }
//		}else if(map.get("tradeType").equals(MessageConstants.MSG_ALLOCATION_OUT)){//判断是否是重复的调出确认数据
//			 int count =  binBEMQMES02_Service.selAllocationOutByNoIF(map);
//			 if(count>0){
//				//是重复的调出确认数据
//				MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_23);
//			 }
//		}else if(map.get("tradeType").equals(MessageConstants.MSG_STOCK_TAKING)){//判断是否是重复的盘点数据
//			 int count =  binBEMQMES02_Service.selStockTakingByNoIF(map);
//			 if(count>0){
//				//是重复的盘点数据
//				MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_24);
//			 }
//		}else if(map.get("tradeType").equals(MessageConstants.MSG_BIR_PRESENT)){//判断是否是重复的生日礼品领用数据
//			 int count =  binBEMQMES02_Service.selStockByBirPresentNoIF(map);
//			 if(count>0){
//				//是重复的生日礼品领用数据
//				MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_26);
//			 }
//		}else if(map.get("tradeType").equals(MessageConstants.MSG_PRO_ORDER)){//判断是否是重复的产品订货数据
//			 int count =  binBEMQMES02_Service.selProductOrderByDateNoIF(map);
//			 if(count>0){
//				//是重复的产品订货数据
//				MessageUtil.addMessageWarning(map, MessageConstants.MSG_ERROR_32);
//			 }
//		}
//	 }

	private void delOppositeData(Map map,List detailDataList){
		// 单据类型
		String ticket_type = (String)map.get("ticket_type");
		// 退货区分
		String saleSRtype = (String)map.get("saleSRtype");

		// 循环明细数据List
		for (int i = 0; i < detailDataList.size(); i++) {
			HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
			String stockType = (String.valueOf(detailDataDTOMap.get("stockType")));
		   // 修改销售记录的情况下需要删除对冲数据
			if (ticket_type!=null && ticket_type.equals(MessageConstants.TICKET_TYPE_MODIFIE_SALE)){
				// 如果是修改正常销售记录
				if (saleSRtype!=null && MessageConstants.SR_TYPE_SALE.equals(saleSRtype)){
					// 删除入库的数据
					if (stockType.equals(MessageConstants.STOCK_TYPE_IN)){
						detailDataList.remove(i);
						i--;
						continue;
					}
				}else if (saleSRtype!=null && !MessageConstants.SR_TYPE_SALE.equals(saleSRtype)){
					// 修改的是退货记录
					// 删除出库数据
					if (stockType.equals(MessageConstants.STOCK_TYPE_OUT)){
						detailDataList.remove(i);
						i--;
						continue;
					}
				}
			}
	    }
	}
	
	/**
	 * 设置需要插入会员参与活动履历表的Map
	 * @param map
	 * @return
	 * @throws CherryMQException 
	 */
	private Map<String,Object> setCampaignHistory(Map<String,Object> map,Map<String,Object> tempDetailDTO) throws CherryMQException{
        String memberCode = ConvertUtil.getString(tempDetailDTO.get("memberCodeDetail"));
        String activityMainCode = ConvertUtil.getString(tempDetailDTO.get("activityMainCode"));
        String activityType = ConvertUtil.getString(tempDetailDTO.get("ActivityType"));
        if(!"".equals(activityMainCode)){
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("MemCode", memberCode);
            param.put("BIN_BrandInfoID", map.get("brandInfoID"));
            param.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
            Map<String,Object> memberInfo = binBEMQMES97_BL.getMemberInfo(param, false);
            String memberInfoID = null;
            if(null != memberInfo && null != memberInfo.get("BIN_MemberInfoID")){
                memberInfoID = ConvertUtil.getString(memberInfo.get("BIN_MemberInfoID"));
            }
            //活动代号
            param.put("ActivityCode", activityMainCode);

            Map<String,Object> campaignHistory = new HashMap<String,Object>();
            campaignHistory.put("OrgCode", map.get("orgCode"));
            campaignHistory.put("BrandCode", map.get("brandCode"));
            campaignHistory.put("BIN_MemberInfoID", memberInfoID);
            campaignHistory.put("CampaignType", activityType);
            if(MessageConstants.ACTIVITYTYPE_PROM.equals(activityType)){
                //促销活动
                List<Map<String,Object>> prmActGroupCodeList = binBEMQMES02_Service.getPrmActGroupCodeList(param);
                if(null != prmActGroupCodeList && prmActGroupCodeList.size()>0){
                    String campaignCode = ConvertUtil.getString(prmActGroupCodeList.get(0).get("GroupCode"));
                    campaignHistory.put("CampaignCode", campaignCode);
                }
            }else if(MessageConstants.ACTIVITYTYPE_MEM.equals(activityType)){
                //会员活动
                List<Map<String,Object>> campaignCodeList = binBEMQMES02_Service.getCampaignCodeList(param);
                if(null != campaignCodeList && campaignCodeList.size()>0){
                    String campaignCode = ConvertUtil.getString(campaignCodeList.get(0).get("CampaignCode"));
                    campaignHistory.put("CampaignCode", campaignCode);
                }
            }
            campaignHistory.put("MainCode", activityMainCode);
            campaignHistory.put("TradeNoIF", map.get("tradeNoIF"));
            campaignHistory.put("TradeType", map.get("saleType"));
            campaignHistory.put("State", MessageConstants.CAMPAIGNORDER_STATE_OK);//已经领用
            campaignHistory.put("BIN_OrganizationID", map.get("organizationID"));
            campaignHistory.put("ParticipateTime", map.get("saleTime"));
            setInsertInfoMapKey(campaignHistory);
            return campaignHistory;
        }else{
            return null;
        }
	}
	
	/**
	 * 销售礼品领用处理
	 * @param detailList
	 * @param giftDrawDetailList
	 * @param map
	 * @throws ParseException
	 */
	private void operateNSGiftGraw(List<Map<String,Object>> detailList,List<Map<String,Object>> giftDrawDetailList,Map<String,Object> map) throws ParseException{
        //领用处理标志
        boolean giftDrawFlag = true;
        if(null != giftDrawDetailList && giftDrawDetailList.size()>0){
            String orderID = ConvertUtil.getString(giftDrawDetailList.get(0).get("orderID"));//会员活动预约主表的预约单据号
            String campaignOrderID = "";
            String spSubType = "";//礼品领用子类型
            String campaignOrderState = "";//领用状态
            String campaignCode ="";
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("TradeNoIF", orderID);
            List<Map<String,Object>> campaignOrderList = binBEMQMES02_Service.getCampaignOrderList(param);
            if(null != campaignOrderList && !campaignOrderList.isEmpty()){
                campaignOrderID = ConvertUtil.getString(campaignOrderList.get(0).get("BIN_CampaignOrderID"));
                spSubType = ConvertUtil.getString(campaignOrderList.get(0).get("SubType"));
                campaignOrderState = ConvertUtil.getString(campaignOrderList.get(0).get("State"));
                campaignCode = ConvertUtil.getString(campaignOrderList.get(0).get("CampaignCode"));
            }
            //已经领用（出现场景：退货或同一coupon延迟上传）
            if(campaignOrderState.equals(MessageConstants.CAMPAIGNORDER_STATE_OK)){
                param.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
                param.put("BIN_BrandInfoID", map.get("brandInfoID"));
                param.put("tradeDateTime", map.get("tradeDateTime"));
                param.put("CampaignCode", campaignCode);
                //原单（NS001）绑定礼品A，终端已经领用但新后台还未收走；
                //接着终端又做了退货处理（SR001），同时又在另外一笔销售（NS002）中使用了该Coupon
                //领用的时间比原单的时间要晚（按时间顺序）
                List<Map<String,Object>> giftDrawList = binBEMQMES02_Service.getGiftDrawList(param);
                if(null != giftDrawList && giftDrawList.size() > 0){
                    //销售再退货，这时可以找到领用单，领用时间必然早于MQ单据时间。
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dt1 = df.parse(ConvertUtil.getString(map.get("tradeDateTime")));//退货时间
                    Date dt2 = df.parse(ConvertUtil.getString(giftDrawList.get(0).get("TradeDateTime")));//销售时间
                    if(dt1.getTime() < dt2.getTime()){
                        giftDrawFlag = false;
                    }
                }
            }
            //接收程序就不再更新领用记录及领用的库存
            if(!giftDrawFlag){
                for(int i=0;i<detailList.size();i++){
                    Map<String,Object> temp = (Map<String,Object>)detailList.get(i);
                    String curOrderID = ConvertUtil.getString(temp.get("OrderID"));
                    if(!"".equals(curOrderID)){
                        detailList.remove(i);
                        i--;
                        continue;
                    }
                }
            }else{
                //目前逻辑不考虑同一消息体内存在多个CouponCode
                if(MessageConstants.BUSINESS_TYPE_NS.equals(map.get("saleType"))){
                    //销售礼品领用处理
                    //总数量
                    int totalQuantity = 0;
                    //总金额
                    BigDecimal totalAmount = new BigDecimal(0);
                    for(int i=0;i<giftDrawDetailList.size();i++){
                        Map<String,Object> giftDrawDetailDTO = giftDrawDetailList.get(i);
                        String detailType = ConvertUtil.getString(giftDrawDetailDTO.get("detailType"));
                        giftDrawDetailDTO.put("mainCode", giftDrawDetailDTO.get("activityMainCode"));
                        giftDrawDetailDTO.put("detailNo", i+1);
                        if(MessageConstants.SALE_TYPE_NORMAL_SALE.equals(detailType)){
                            giftDrawDetailDTO.put("productVendorID", giftDrawDetailDTO.get("productVendorID"));
                        }else if(MessageConstants.SALE_TYPE_PROMOTION_SALE.equals(detailType)){
                            giftDrawDetailDTO.put("productVendorID", giftDrawDetailDTO.get("promotionProductVendorID"));
                        }
                        giftDrawDetailDTO.put("giftType", detailType);
                        
                        int quantity = CherryUtil.obj2int(giftDrawDetailDTO.get("quantity"));
                        totalQuantity += quantity;
                        if(null != giftDrawDetailDTO.get("price") && !"".equals(giftDrawDetailDTO.get("price"))){
                            BigDecimal amount = new BigDecimal(Double.parseDouble((String)giftDrawDetailDTO.get("price")));
                            totalAmount = totalAmount.add(amount.multiply(new BigDecimal(quantity)));
                        }
                    }
                    
                    Map<String,Object> giftDrawMain = new HashMap<String,Object>();
                    giftDrawMain.putAll(map);
                    int organizationInfoID = CherryUtil.obj2int(giftDrawMain.get("organizationInfoID"));
                    int brandInfoID = CherryUtil.obj2int(giftDrawMain.get("brandInfoID"));
                    String stockBirPresent_no = binOLCM03_BL.getTicketNumber(organizationInfoID, brandInfoID, "BINBEMQMES02", MessageConstants.CHERRY_TRADETYPE_BIRPRSSENT);
                    String couponCode = ConvertUtil.getString(giftDrawDetailList.get(0).get("couponCode"));
                    giftDrawMain.put("tradeType", MessageConstants.MSG_BIR_PRESENT);
                    giftDrawMain.put("subType", spSubType);
                    giftDrawMain.put("stockBirPresent_no", stockBirPresent_no);
                    giftDrawMain.put("relevantNo", orderID);
                    giftDrawMain.put("couponCode", couponCode);
                    giftDrawMain.put("campaignCode", giftDrawDetailList.get(0).get("CampaignCode"));
                    giftDrawMain.put("amount", totalAmount);
                    giftDrawMain.put("quantity", totalQuantity);
                    giftDrawMain.put("tradeDate", map.get("tradeDate"));
                    giftDrawMain.put("tradeDateTime", map.get("tradeDateTime"));
                    giftDrawMain.put("informType", giftDrawDetailList.get(0).get("informType"));
                    giftDrawMain.put("billState", null);

                    //写入礼品领用主表
                    this.setInsertInfoMapKey(giftDrawMain);
                    int giftDrawID = binBEMQMES02_Service.addGiftDraw(giftDrawMain);
                    
                    //写入礼品领用明细表
                    for(int i=0;i<giftDrawDetailList.size();i++){
                        Map<String,Object> giftDrawDetailDTO = giftDrawDetailList.get(i);
                        giftDrawDetailDTO.put("giftDrawID", giftDrawID);
                        this.setInsertInfoMapKey(giftDrawDetailDTO);
                    }
                    binBEMQMES02_Service.addGiftDrawDetail(giftDrawDetailList);
                    
                    //更新会员活动预约主表的预约单状态
                    Map<String,Object> updateMap = new HashMap<String,Object>();
                    updateMap.put("State", MessageConstants.CAMPAIGNORDER_STATE_OK);//已领用
                    updateMap.put("FinishTime", map.get("tradeDateTime"));//领用时间
                    updateMap.put("CounterGot", map.get("counterCode"));//领取柜台
                    updateMap.put("BIN_CampaignOrderID", campaignOrderID);
                    this.setInsertInfoMapKey(updateMap);
                    binBEMQMES02_Service.updCampaignOrderState(updateMap);
                }else if(MessageConstants.BUSINESS_TYPE_SR.equals(map.get("saleType"))){
                    //退货时礼品领用处理，礼品领用表改为无效，会员活动预约主表改为未领用
                    Map<String,Object> delMap = new HashMap<String,Object>();
                    delMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
                    delMap.put("BIN_BrandInfoID", map.get("brandInfoID"));
                    delMap.put("relevanceNo", map.get("relevantNo"));
                    delMap.put("CampaignCode", campaignCode);
                    this.setInsertInfoMapKey(delMap);
                    binBEMQMES02_Service.delGiftDrawDetail(delMap);
                    binBEMQMES02_Service.delGiftDraw(delMap);
                    
                    //更新会员活动预约主表的预约单状态
                    Map<String,Object> updateMap = new HashMap<String,Object>();
                    updateMap.put("State", MessageConstants.CAMPAIGNORDER_STATE_AR);//AR：已经到货
                    updateMap.put("BIN_CampaignOrderID", campaignOrderID);
                    this.setInsertInfoMapKey(updateMap);
                    binBEMQMES02_Service.updCampaignOrderState(updateMap);
                }
            }
        }
	}
	
    /**
     * 设置促销/会员活动相关的值
     * @param dataMap
     * @throws CherryMQException
     */
    private void setCampaignValue(Map<String,Object> campaignDataMap) throws CherryMQException{
        String activityCode = ConvertUtil.getString(campaignDataMap.get("activityCode"));
        String activityMainCode = ConvertUtil.getString(campaignDataMap.get("activityMainCode"));
        boolean allOppositeFlag = Boolean.valueOf(ConvertUtil.getString(campaignDataMap.get("allOppositeFlag")));
        Map<String,Object> map = (Map<String, Object>) campaignDataMap.get("map");
        Map<String,Object> detailDataMap = (Map<String, Object>) campaignDataMap.get("detailDataMap");
        Map<String,Object> memberAndMainCodeMap = (Map<String, Object>) campaignDataMap.get("memberAndMainCodeMap");
        Map<String,Object> memberMap = (Map<String, Object>) campaignDataMap.get("memberMap");
        Map<String,Object> campaignCodeMap = (Map<String, Object>) campaignDataMap.get("campaignCodeMap");
        List<Map<String,Object>> campaignHistoryList = (List<Map<String, Object>>) campaignDataMap.get("campaignHistoryList");
        
        //当前明细为对冲时，不必设置促销/会员活动相关的值，但是当明细全部都是对冲，需要设置。
        String mainModifyCounts = ConvertUtil.getString(map.get("modifyCounts"));
        String detailModifyCounts = ConvertUtil.getString(detailDataMap.get("modifyCounts"));
        if(!allOppositeFlag && !mainModifyCounts.equals(detailModifyCounts)){
            return;
        }
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("activityCode", activityCode);
        param.put("organizationInfoID", map.get("organizationInfoID"));
        param.put("brandInfoID", map.get("brandInfoID"));
        List<Map<String,Object>> activityMainCodeList = binBEMQMES02_Service.getActivityMainCodeList(param);
        if(null != activityMainCodeList && activityMainCodeList.size()>0){
            //活动类型  0：促销活动，1：会员活动
            detailDataMap.put("ActivityType", activityMainCodeList.get(0).get("ActivityType"));
        }else{
            if(!"".equals(activityMainCode)){
                //activityCode在Promotion.BIN_ActivityTransHis表找不到时（NOCODE），查询是否在会员子活动表存在，存在是会员活动不存在促销活动
                Map<String,Object> paramCampaign = new HashMap<String,Object>();
                paramCampaign.put("ActivityCode", activityMainCode);
                paramCampaign.put("BIN_BrandInfoID", map.get("brandInfoID"));
                paramCampaign.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
                List<Map<String,Object>> campaignCodeList = binBEMQMES02_Service.getCampaignCodeList(paramCampaign);
                if(null != campaignCodeList && campaignCodeList.size()>0){
                    detailDataMap.put("ActivityType", MessageConstants.ACTIVITYTYPE_MEM);
                }else{
                    detailDataMap.put("ActivityType", MessageConstants.ACTIVITYTYPE_PROM);
                }
            }
        }
        if("".equals(activityMainCode)){
            if(null != activityMainCodeList && activityMainCodeList.size()>0){
                activityMainCode = ConvertUtil.getString(activityMainCodeList.get(0).get("MainCode"));
                detailDataMap.put("activityMainCode", activityMainCode);
            }else{
                logger.error("活动下发历史表中促销代码 "+activityCode+"不存在");
            }
        }
        //排除重复记录（memberCode+activityMainCode）
        String memberCode = ConvertUtil.getString(detailDataMap.get("memberCodeDetail"));
        String memberAndMainCode = memberCode+"_"+activityMainCode;
        if(!memberAndMainCodeMap.containsKey(memberAndMainCode)){
            Map<String,Object> campaignHistoryMap = setCampaignHistory(map,detailDataMap);
            if(null != campaignHistoryMap && !campaignHistoryMap.isEmpty()){
                String memberInfoID = ConvertUtil.getString(campaignHistoryMap.get("BIN_MemberInfoID"));
                if(!allOppositeFlag && !"".equals(memberInfoID)){
                    campaignHistoryList.add(campaignHistoryMap);
                }
                memberAndMainCodeMap.put(memberAndMainCode, null);
                memberMap.put(memberCode, campaignHistoryMap.get("BIN_MemberInfoID"));
                campaignCodeMap.put(activityCode, campaignHistoryMap.get("CampaignCode"));
            }
        }
        detailDataMap.put("BIN_MemberInfoID", memberMap.get(memberCode));
        detailDataMap.put("CampaignCode", campaignCodeMap.get(activityCode));
    }
    
    /**
     * 设置销售下单者的相关信息
     * @param map
     */
    private void setSaleCreatorInfo(Map<String,Object> map){
        //下单组织结构ID
        String departCodeDX = ConvertUtil.getString(map.get("departCodeDX"));
        if(departCodeDX.equals("")){
            map.put("BIN_OrganizationIDDX", map.get("organizationID"));
        }else{
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("organizationInfoID", map.get("organizationInfoID"));
            paramMap.put("brandInfoID", map.get("brandInfoID"));
            paramMap.put("counterCode", departCodeDX);
            Map<String,Object> departInfo = binBEMQMES99_Service.selCounterDepartmentInfo(paramMap);
            if(null != departInfo){
                map.put("BIN_OrganizationIDDX", departInfo.get("organizationID"));
            }
        }
        //下单员工ID
        String employeeCodeDX = ConvertUtil.getString(map.get("employeeCodeDX"));
        if(employeeCodeDX.equals("")){
            map.put("BIN_EmployeeIDDX",map.get("employeeID"));
        }else{
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("organizationInfoID", map.get("organizationInfoID"));
            paramMap.put("brandInfoID", map.get("brandInfoID"));
            paramMap.put("BAcode", employeeCodeDX);
            Map<String,Object> employeeInfo = binBEMQMES99_Service.selEmployeeInfo(paramMap);
            if(null != employeeInfo){
                map.put("BIN_EmployeeIDDX", employeeInfo.get("employeeID"));
            }
        }
    }
    
    /**
     * 工作流-产品调入申请
     * @param map
     * @throws Exception
     */
    private void allocationInOSWF(Map<String, Object> map) throws Exception {
        map.put("cherry_tradeType", MessageConstants.BUSINESS_TYPE_BG);
        
        // 单据号采番
        if(map.get("tradeNoIF")==null || "".equals(map.get("tradeNoIF"))){
        	this.getMQTicketNumber(map,"");
        	Object tradeNoIF = map.get("cherry_no");
        	map.put("tradeNoIF", String.valueOf(tradeNoIF));
        }else{
        	map.put("cherry_no", map.get("tradeNoIF"));
        }
        // 明细数据List
        List detailDataList = (List) map.get("detailDataDTOList");
        map.put("organizationIDDX", map.get("organizationID"));
        map.put("employeeIDDX", map.get("employeeID"));
        // 审核区分 - 未提交审核
        map.put("verifiedFlag", MessageConstants.AUDIT_FLAG_UNSUBMIT);
        // 插入产品调拨申请单据表
        int productAllocationID = binBEMQMES02_Service.addProductAllocation(map);
        // 循环明细数据List
        for (int i = 0; i < detailDataList.size(); i++) {
            HashMap detailDataDTOMap = (HashMap) detailDataList.get(i);
            // 设定产品调拨ID
            detailDataDTOMap.put("productAllocationID", productAllocationID);
        }
        // 批量插入产品调拨申请单据明细表
        binBEMQMES02_Service.addProductAllocationDetail(detailDataList);
        
        //查询用户表获得用户ID
        Map<String,Object> userMap = binBEMQMES99_Service.selUserByEempID(map);
        
        String organizationIDIn = ConvertUtil.getString(map.get("organizationID"));
        String organizationIDOut = ConvertUtil.getString(map.get("relevantOrganizationID"));
        
        Map<String, Object> startWorkFlowMap = new HashMap<String, Object>();
        startWorkFlowMap.put(CherryConstants.OS_MAINKEY_BILLTYPE, CherryConstants.OS_BILLTYPE_BG);//业务类型
        startWorkFlowMap.put(CherryConstants.OS_MAINKEY_BILLID, productAllocationID);//调拨申请单ID
        startWorkFlowMap.put("BIN_EmployeeID", map.get("employeeID"));// 员工ID
        startWorkFlowMap.put(CherryConstants.OS_MAINKEY_BILLCREATOR_EMPLOYEE, map.get("employeeID"));//  员工ID
        startWorkFlowMap.put(CherryConstants.OS_ACTOR_TYPE_USER,userMap==null||
                userMap.get("userID")==null? "-9998":userMap.get("userID"));//用户ID
        startWorkFlowMap.put("BIN_UserID", startWorkFlowMap.get(CherryConstants.OS_ACTOR_TYPE_USER));
        startWorkFlowMap.put(CherryConstants.OS_ACTOR_TYPE_POSITION, map.get("positionCategoryID"));//岗位ID
        startWorkFlowMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, organizationIDIn);//调入部门ID
        startWorkFlowMap.put("BIN_OrganizationIDIn", organizationIDIn);//调入部门ID
        startWorkFlowMap.put("BIN_OrganizationIDOut", organizationIDOut);//调出部门ID
        startWorkFlowMap.put("CurrentUnit", "MQ");//当前机能ID
        startWorkFlowMap.put("BIN_BrandInfoID", map.get("brandInfoID"));//品牌ID
        startWorkFlowMap.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));//组织ID
        startWorkFlowMap.put("BrandCode", map.get("brandCode"));//品牌编号
        startWorkFlowMap.put("OrganizationCodeIn", map.get("counterCode"));//调入部门编号
        startWorkFlowMap.put("OrganizationCodeOut", map.get("relevantCounterCode"));//调出部门编号
        
        UserInfo userInfo = new UserInfo();
        userInfo.setOrganizationInfoCode(map.get("orgCode").toString());
        userInfo.setBrandCode(map.get("brandCode").toString());
        userInfo.setBIN_PositionCategoryID(Integer.parseInt(map.get("positionCategoryID").toString()));//岗位ID
        userInfo.setBIN_EmployeeID(Integer.parseInt(map.get("employeeID").toString()));//员工ID
        userInfo.setBIN_OrganizationID(Integer.parseInt(map.get("organizationID").toString()));
        startWorkFlowMap.put("UserInfo", userInfo);
        
        startWorkFlowMap.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
        
        //工作流开始
        binOLSTCM00_BL.StartOSWorkFlow(startWorkFlowMap);
    }
    
    /**
     * 工作流-产品调出调入
     * @param map
     * @throws Exception
     */
    private void allocationOutOSWF(Map<String, Object> map) throws Exception {
        // 明细数据List
        List<Map<String,Object>> detailDataList = (List<Map<String,Object>>) map.get("detailDataDTOList");
        
        String workflowid = ConvertUtil.getString(map.get("WorkFlowID"));
        long osID = Long.parseLong(workflowid);
        ActionDescriptor[] adArr = binOLCM19_BL.getCurrActionByOSID(osID);
        int actionID = 0;
        String subType = ConvertUtil.getString(map.get("subType"));
        //调出/拒绝调出标志 true 为调出，false为拒绝调出
        boolean allocationOutFlag = false;
        //明细都为0，状态改为已拒绝
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDTO = (Map<String, Object>) detailDataList.get(i);
            if(!ConvertUtil.getString(detailDTO.get("quantity")).equals("0")){
                allocationOutFlag = true;
                break;
            }
        }
        if(!allocationOutFlag){
            subType = "NG";
        }
        if(null != adArr && adArr.length>0){
            Map<String,Object> metaMapTMp = null;
            for (int j = 0; j < adArr.length; j++) {
                metaMapTMp = adArr[j].getMetaAttributes();
                //找到带有OS_DefaultAction元素的action
                if(null != metaMapTMp && metaMapTMp.containsKey("OS_DefaultAction")){
                    String defaultAction = ConvertUtil.getString(metaMapTMp.get("OS_DefaultAction"));
                    String operateCode = ConvertUtil.getString(metaMapTMp.get("OS_OperateCode"));
                    if(operateCode.equals("80")){
                        if(subType.equals("NG") && defaultAction.equals("NG")){
                            //拒绝
                            ActionDescriptor ad = adArr[j];
                            actionID = ad.getId();
                            break;
                        }else if(!subType.equals("NG") && defaultAction.equals("OK")){
                            //同意
                            ActionDescriptor ad = adArr[j];
                            actionID = ad.getId();
                            break;
                        }
                    }
                }
            }
            if(actionID == 0){
                MessageUtil.addMessageWarning(map,"执行柜台产品调出时，无法找到当前能执行Action");
            }
        }else{
            MessageUtil.addMessageWarning(map,"执行柜台产品调出时，调用BINOLCM19_BL共通代码getCurrActionByOSID未查到当前能操作的步骤。"+
                    "涉及主要参数：工作流ID\""+osID+"\"");
        }
        
        UserInfo userInfo = new UserInfo();
        userInfo.setBIN_EmployeeID(CherryUtil.obj2int(map.get("employeeID")));

        //查询用户表获得用户ID
        Map<String,Object> userMap = binBEMQMES99_Service.selUserByEempID(map);
        String userID = null;
        if(null == userMap || null == userMap.get("userID")){
            userID = "-9998";
        }else{
            userID = ConvertUtil.getString(userMap.get("userID"));
        }
        
        List<Map<String,Object>> detailTableData = new ArrayList<Map<String,Object>>();
        for(int i=0;i<detailDataList.size();i++){
            Map<String,Object> detailDataMap = detailDataList.get(i);
            Map<String,Object> detailMap = new HashMap<String,Object>();
            detailMap.put("BIN_ProductVendorID", detailDataMap.get("productVendorID"));
            detailMap.put("DetailNo", i+1);
            detailMap.put("Quantity", detailDataMap.get("quantity"));
            detailMap.put("Price", detailDataMap.get("price"));
            detailMap.put("BIN_InventoryInfoID", detailDataMap.get("inventoryInfoID"));
            detailMap.put("BIN_LogicInventoryInfoID", detailDataMap.get("logicInventoryInfoID"));
            detailMap.put("Comments", detailDataMap.get("reason"));
            detailMap.put("CreatedBy", detailDataMap.get("createdBy"));
            detailMap.put("CreatePGM", detailDataMap.get("createPGM"));
            detailMap.put("UpdatedBy", detailDataMap.get("updatedBy"));
            detailMap.put("UpdatePGM", detailDataMap.get("updatePGM"));
            detailTableData.add(detailMap);
        }
        
        Map<String,Object> mainTableData = new HashMap<String,Object>();
        mainTableData.put("BIN_OrganizationInfoID", map.get("organizationInfoID"));
        mainTableData.put("BIN_BrandInfoID", map.get("brandInfoID"));
        mainTableData.put("AllocationOutNoIF",map.get("tradeNoIF"));
        mainTableData.put("RelevanceNo", map.get("relevantNo"));
        mainTableData.put("BIN_OrganizationIDIn", map.get("relevantOrganizationID"));
        mainTableData.put("BIN_OrganizationIDOut", map.get("organizationID"));
        mainTableData.put("BIN_EmployeeID", map.get("employeeID"));
        mainTableData.put("BIN_OrganizationIDDX", map.get("organizationID"));
        mainTableData.put("BIN_EmployeeIDDX", map.get("employeeID"));
        mainTableData.put("BIN_EmployeeIDAudit", null);
        mainTableData.put("TotalQuantity", map.get("totalQuantity"));
        mainTableData.put("TotalAmount", map.get("totalAmount"));  
        mainTableData.put("VerifiedFlag",CherryConstants.AUDIT_FLAG_AGREE);
        mainTableData.put("Comments", map.get("reason"));
        mainTableData.put("Date", map.get("tradeDate"));
        mainTableData.put("WorkFlowID",osID);
        mainTableData.put("SynchFlag","1");//同步数据标志 1：可导出
        mainTableData.put("CreatedBy", map.get("createdBy"));
        mainTableData.put("CreatePGM", map.get("createPGM"));
        mainTableData.put("UpdatedBy", map.get("updatedBy"));
        mainTableData.put("UpdatePGM", map.get("updatePGM"));

        Map<String,Object> mainDataMap = new HashMap<String,Object>();
        mainDataMap.put("entryID", osID);
        mainDataMap.put("actionID", actionID);
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_DEPART, map.get("organizationID"));
        mainDataMap.put("BIN_EmployeeID", map.get("employeeID"));
        mainDataMap.put(CherryConstants.OS_ACTOR_TYPE_USER, userID);
        mainDataMap.put("BrandCode", map.get("brandCode").toString());
        mainDataMap.put("CurrentUnit", "BINBEMQMES02");
        mainDataMap.put("tradeDateTime", map.get("tradeDateTime"));//MQ单据的时间
        mainDataMap.put("UserInfo", userInfo);
        mainDataMap.put("OrganizationCodeIn",map.get("relevantCounterCode"));//调入部门编号
        mainDataMap.put("OrganizationCodeOut",map.get("counterCode"));//调出部门编号
        mainDataMap.put("LGMainTableData", mainTableData);
        mainDataMap.put("LGDetailTableData", detailTableData);
        binOLSTCM00_BL.DoAction(mainDataMap);

//        try{
//            PropertySet ps = workflow.getPropertySet(osID);
//            String currentOperate = ps.getString("OS_Current_Operate");
//            //流程结束写调入日志
//            if(currentOperate.equals("999")){
//                int billID = ps.getInt("BIN_ProductAllocationInID");
//                Map<String,Object> prtLGMainData = binOLSTCM16_BL.getProductAllocationInMainData(billID, null);
//                
//                //写入操作日志-调入
//                Map<String, Object> logMap = new HashMap<String, Object>();
//                //工作流实例ID
//                logMap.put("WorkFlowID",osID);
//                //操作部门
//                logMap.put("BIN_OrganizationID",map.get("organizationID"));
//                //操作员工
//                logMap.put("BIN_EmployeeID",map.get("employeeID"));
//                //操作业务类型
//                logMap.put("TradeType",CherryConstants.OS_BILLTYPE_BG);
//                //表名
//                logMap.put("TableName", "Inventory.BIN_ProductAllocationIn");
//                //单据ID
//                logMap.put("BillID",billID);
//                //单据Code
//                logMap.put("BillNo", prtLGMainData.get("AllocationInNoIF"));
//                //操作代码
//                logMap.put("OpCode",CherryConstants.OPERATE_BG);
//                //操作结果
//                logMap.put("OpResult","999");//结束
//                //调入时间
//                logMap.put("OpDate", map.get("tradeDateTime"));
//                //作成者   
//                logMap.put("CreatedBy",userID); 
//                //作成程序名
//                logMap.put("CreatePGM","BINBEMQMES02");
//                //更新者
//                logMap.put("UpdatedBy",userID);
//                //更新程序名
//                logMap.put("UpdatePGM","BINBEMQMES02");
//                binOLCM22_BL.insertInventoryOpLog(logMap);
//            }
//        }catch(Exception e){
//            
//        }
    }
}
