package com.cherry.mq.mes.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.cp.common.interfaces.BINOLCPCOM05_IF;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.cherry.mq.mes.common.MessageUtil;
import com.cherry.mq.mes.service.BINBEMQMES02_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
/**
 *  活动预约接收程序
 * @author lipc
 *
 */
public class BINBEMQMES12_BL{
	
	@Resource(name = "binolcpcom05IF")
	private BINOLCPCOM05_IF com05IF;
	
	@Resource(name="binBEMQMES97_BL")
	private BINBEMQMES97_BL mq97BL;
	
	@Resource(name = "binOLCM03_BL")
	private BINOLCM03_BL cm03bl;
	
	@Resource(name="binBEMQMES02_Service")
	private BINBEMQMES02_Service mqMes02Ser;

	public void handleMessage(Map<String, Object> map) throws Exception {
		int result = CherryConstants.SUCCESS;
		String tradeType = ConvertUtil.getString(map.get("tradeType"));
		if("PB".equalsIgnoreCase(tradeType)){
			Map<String, Object> order = new HashMap<String, Object>();
			order.put("tradeType", "PB");
			order.put("sendFlag", "1");
			Map<String, Object> comMap = getComMap(map);
			Object brandId = map.get("brandInfoID");
			Object orgId = map.get("organizationInfoID");
			Object memCode = map.get("memberCode");
			Object brandCode = map.get("brandCode");
			// 操作BA_ID
			map.put("EmployeeCode", map.get("BAcode"));
			map.put("BIN_OrganizationInfoID", orgId);
			map.put("BIN_BrandInfoID", brandId);
			map.put("MemCode", memCode);
			// 会员俱乐部代号
			String clubCode = (String) map.get("clubCode");
			if (!CherryChecker.isNullOrEmpty(clubCode)) {
				order.put("clubCode", map.get("clubCode"));
				// 查询会员俱乐部ID
				order.put("memberClubId", mqMes02Ser.selMemClubId(map));
			}
			// coupon码
			map.put(CampConstants.COUPON_CODE, map.get("couponCode"));
			// 单据类型[ 0：预约,1： 预约取消]
			String ticketType = ConvertUtil.getString(map.get("ticketType"));
			String bookDate = ConvertUtil.getString(map.get("bookDate"));
			String bookTime = ConvertUtil.getString(map.get("bookTime"));
			String optTime = bookDate + CherryConstants.SPACE + bookTime;
			order.put(CampConstants.OPT_TIME, optTime);
			comMap.put(CampConstants.OPT_TIME, optTime);
			order.put(CampConstants.DATA_CHANNEL, map.get("data_source"));
			// 获取会员信息
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("memCode", memCode);
			p.put("testTypeIgnore", 1);
			p.put("brandInfoId", brandId);
			p.put("organizationInfoId", orgId);
			p.put("brandCode", brandCode);
			// 获取会员信息
			Map<String, Object>  memInfo = com05IF.searchMemInfo(p);
			if(null == memInfo){
                MessageUtil.addMessageWarning(map,"会员号为\""+map.get("MemCode")+"\""+MessageConstants.MSG_ERROR_34);
            }
			order.put("memId", memInfo.get("memId"));
			// 所属柜台or发卡柜台
			order.put("cntCodeBelong", memInfo.get("counterCode"));
			// 首次购买柜台
			order.put("firstSaleCounterCode", memInfo.get("firstSaleCounterCode"));
			// 预约柜台
			order.put("orderCntCode",map.get("counterCode"));
			// 机器号
			order.put("machineCode",map.get("machineCode"));
			order.put("deliveryMothod",map.get("deliveryType"));
			order.put("address",map.get("deliveryAddress"));
			order.put("receiverName",map.get("receiverName"));
			order.put("receiverMobile",map.get("receiverMobile"));
			order.put("deliveryProvince",map.get("deliveryProvince"));
			order.put("deliveryCity",map.get("deliveryCity"));
			order.put("deliveryCounty",map.get("deliveryCounty"));
			if("0".equals(ticketType)){
				// 活动结束仍然可以预约
				//order.put(CampConstants.INCLUDE_STATE, CampConstants.STATE_2);
				// 预约
				order.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_RV);
				// 单据号
				order.put(CampConstants.BILL_NO, map.get("tradeNoIF"));
				order.put("memCode", map.get("memberCode"));
				if(CherryChecker.isNullOrEmpty(map.get("mobile"))){
					order.put("mobilePhone", memInfo.get("mobilePhone"));
				}else{
					order.put("mobilePhone", map.get("mobile"));
				}
				if(CherryChecker.isNullOrEmpty(map.get("weixin"))){
					order.put("messageId", memInfo.get("messageId"));
				}else{
					order.put("messageId", map.get("weixin"));
				}
				// 领用柜台
				order.put("counterCode", map.get("counterGotCode"));
				order.put("opeartor", mq97BL.getEmployeeInfo(map, true).get("BIN_EmployeeID"));
				// 设置预约礼品明细LIST
				setOrderPrtList(map,order);
			}else{
				// 取消预约
				order.put(CampConstants.BILL_STATE, CampConstants.BILL_STATE_CA);
				setOrder(map,order);
				if(!CherryChecker.isNullOrEmpty(map.get("relevantNo"))){
					// 单据号
					order.put(CampConstants.BILL_NO, map.get("relevantNo"));
				}else{
					// 没有关联到预约单据号
					MessageUtil.addMessageWarning(map,"没有关联到预约单据号");
				}
			}
			// MQ插入预约相关表
			try {
				result = com05IF.tran_campOrderMQ(comMap, order);
				if(result != CherryConstants.SUCCESS){
					MessageUtil.addMessageWarning(map,com05IF.getErrMsg(result + ""));
					result = CherryConstants.ERROR;
				}
			} catch (CherryException e) {
				MessageUtil.addMessageWarning(map,e.getErrMessage());
				result = CherryConstants.ERROR;
			}
			if(result == CherryConstants.SUCCESS){
				// 活动预约积分维护
				try {
					comMap.put(CampConstants.OPT_TIME, order.get(CampConstants.OPT_TIME));
					com05IF.sendPointMQ(comMap, order);
				} catch (CherryException e) {
					MessageUtil.addMessageWarning(map,e.getErrMessage());
					result = CherryConstants.ERROR;
				}
			}
			if(result == CherryConstants.SUCCESS){
				try {
					String batchNo = ConvertUtil.getString(comMap.get(CampConstants.BATCHNO));
					String opt = CampConstants.BILL_STATE_RV;
					if(!"0".equals(ticketType)){
						opt = CampConstants.BILL_STATE_CA;
					}
					// 发送沟通MQ
					int r2 = com05IF.sendGTMQ(comMap, batchNo, opt);
					if( r2 > result){
						result = r2;
					}
				} catch (Exception e) {
					MessageUtil.addMessageWarning(map,e.getMessage());
					result = CherryConstants.ERROR;
				}
			}
		}else{
			// 没有此业务类型
			MessageUtil.addMessageWarning(map,MessageConstants.MSG_ERROR_27);
		}
	}
	
	/**
	 * 设置mongoDB
	 * @param map
	 * @throws CherryMQException
	 */
	public void addMongoMsgInfo(Map<String,Object> map) throws CherryMQException {
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
        //员工代码
        dbObject.put("UserCode", map.get("BAcode"));
        // 发生时间
        dbObject.put("OccurTime", (String)map.get("bookDate")+" "+(String)map.get("bookTime"));
        map.put("dbObject", dbObject);
	}
	/**
	 * 取得共同信息
	 * @param map
	 * @return
	 */
	private Map<String, Object> getComMap(Map<String, Object> map){
		Map<String, Object> comMap = new HashMap<String, Object>();
		comMap.put(CherryConstants.ORG_CODE, map.get("orgCode"));
		comMap.put(CherryConstants.BRAND_CODE, map.get("brandCode"));
		comMap.put(CherryConstants.BRANDINFOID, map.get("brandInfoID"));
		comMap.put(CherryConstants.ORGANIZATIONINFOID, map.get("organizationInfoID"));
		comMap.put("employeeCode", map.get("BAcode"));
		comMap.put(CherryConstants.CREATEDBY, "BINBEMQMES12");
		comMap.put(CherryConstants.CREATEPGM, "BINBEMQMES12");
		comMap.put(CherryConstants.UPDATEDBY, "BINBEMQMES12");
		comMap.put(CherryConstants.UPDATEPGM, "BINBEMQMES12");
		return comMap;
	}
	
	/**
	 * 取得礼品ID及积分信息
	 * @param map
	 * @param detail
	 * @return
	 * @throws CherryMQException 
	 */
	private Map<String, Object> getPrtMap(Map<String, Object> map,Map<String, Object> detail) throws CherryMQException{
		Map<String, Object> prtInfo = new HashMap<String, Object>();
		String prtType = ConvertUtil.getString(detail.get("detailType")).toUpperCase();
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put("UnitCode",detail.get("unitcode"));
		param.put("BarCode",detail.get("barcode"));
		param.put("TradeDateTime",map.get("bookDate"));
		if(CampConstants.PRT_TYPE_P.equals(prtType)){
			Map<String,Object> prmInfo = mq97BL.getPrmInfo(map, param, true);
			prtInfo.put("proId", prmInfo.get("BIN_PromotionProductVendorID"));
			prtInfo.put("exPoint", prmInfo.get("ExPoint"));
			prtInfo.put("isStock", prmInfo.get("IsStock"));
		}else{
			int proId = mq97BL.getProductVendorID(map, param, true);
			prtInfo.put("proId", proId);
		}
		return prtInfo;
	}
	
	/**
	 * 设置预约礼品明细LIST
	 * @param map
	 * @return
	 * @throws CherryMQException 
	 */
	@SuppressWarnings("unchecked")
	private void setOrderPrtList(Map<String, Object> map,
			Map<String, Object> order) throws CherryMQException{
		List<Map<String,Object>> detailList = (List<Map<String,Object>>) map.get("detailDataDTOList");
		if(null != detailList && detailList.size() > 0){
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String,String> codeMap = new HashMap<String, String>();
			for(Map<String,Object> detail : detailList){
				Map<String,Object> item = getPrtMap(map,detail);
				String barCode = ConvertUtil.getString(detail.get("barcode"));
				item.put(CampConstants.BILL_NO,detail.get("tradeNoIF"));
				item.put(CampConstants.PRT_TYPE,detail.get("detailType"));
				item.put(CherryConstants.UNITCODE,detail.get("unitcode"));
				item.put(CherryConstants.BARCODE,barCode);
				int q = ConvertUtil.getInt(detail.get("quantity"));
				float a = ConvertUtil.getFloat(detail.get("amount"));
				item.put(CampConstants.QUANTITY,q);
				// DHCP || TZZK
				if(barCode.startsWith("DH") || barCode.startsWith("TZ")){
					item.put(CampConstants.PRICE,a/q);
				}else{
					item.put(CampConstants.PRICE,a);
				}
				
				// 预约档次Code
				if(!CherryChecker.isNullOrEmpty(detail.get("activityMainCode"))){
					order.put(CampConstants.SUBCAMP_CODE,detail.get("activityMainCode"));
					item.put(CampConstants.SUBCAMP_CODE,detail.get("activityMainCode"));
				}else{
					String subCampCode = CampConstants.SUBCAMP_CODE_DEF;
					String activityCode = ConvertUtil.getString(detail.get("activityCode"));
					if(!"".equals(activityCode)){
						if(null == codeMap.get("activityCode")){
							Map<String,Object> param = new HashMap<String, Object>();
							param.put("organizationInfoID", map.get("organizationInfoID"));
							param.put("brandInfoID", map.get("brandInfoID"));
							param.put("activityCode", activityCode);
							List<Map<String,Object>> codeList = mqMes02Ser.getActivityMainCodeList(param);
							if(null != codeList && codeList.size() > 0){
								subCampCode = ConvertUtil.getString(codeList.get(0).get("MainCode"));
								if(!"".equals(subCampCode)){
									codeMap.put(activityCode, subCampCode);
									order.put(CampConstants.SUBCAMP_CODE,subCampCode);
								}
							}
						}else{
							subCampCode = codeMap.get(activityCode);
						}
					}
					item.put(CampConstants.SUBCAMP_CODE,subCampCode);
				}
				list.add(item);
			}
			// 取得预约礼品明细LIST
			order.put(CampConstants.KEY_LIST, list);
		}else{
			// 没有预约礼品明细
			MessageUtil.addMessageWarning(map,"没有预约礼品明细");
		}
	}
	
	/**
	 * 取得主题活动信息
	 * @param map
	 * @return
	 * @throws CherryMQException 
	 */
	@SuppressWarnings("unchecked")
	private void setOrder(Map<String, Object> map,Map<String, Object> order) throws CherryMQException{
		List<Map<String,Object>> detailList = (List<Map<String,Object>>) map.get("detailDataDTOList");
		Map<String,Object> campInfo = null;
		if(null != detailList && detailList.size() > 0){
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> detail : detailList){
				Map<String,Object> item = new HashMap<String, Object>();
				String subCampCode = ConvertUtil.getString(detail.get("activityMainCode"));
				// 预约档次Code
				if(!"".equals(subCampCode)){
					if(null == campInfo){
						campInfo = com05IF.getCampInfo(subCampCode);
						order.putAll(campInfo);
					}
					item.put(CampConstants.SUBCAMP_CODE,subCampCode);
				}else{
					item.put(CampConstants.SUBCAMP_CODE,CampConstants.SUBCAMP_CODE_DEF);
				}
				list.add(item);
			}
			// 取得预约礼品明细LIST
			order.put(CampConstants.KEY_LIST, list);
		}else{
			// 没有预约礼品明细
			MessageUtil.addMessageWarning(map,"没有预约礼品明细");
		}
	}
}
