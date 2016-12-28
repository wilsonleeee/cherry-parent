/*  
 * @(#)BINBEMQMES99_Service.java     1.0 2011/05/31      
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
package com.cherry.mq.mes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.annota.TimeLog;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.util.DateUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 消息数据接收共通处理Service
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES99_Service extends BaseService{
	
    @Resource(name="binBEMQMES99_Service_Cache")
    private BINBEMQMES99_Service_Cache binBEMQMES99_Service_Cache;
    
    private static final Logger logger = LoggerFactory.getLogger(BINBEMQMES99_Service.class);
    
	/**
	 * 从配置数据库查询品牌数据库对应表获取所有品牌的数据源
	 * @param map
	 * @return
	 */
	public List selBrandDataSourceConfigList (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selBrandDataSourceConfigList");
		return baseConfServiceImpl.getList(map);
	}
	
	
	/**
	 * 查询品牌信息
	 * @param map
	 * @return
	 */
	public HashMap selBrandInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selBrandInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询组织信息
	 * @param map
	 * @return
	 */
	public HashMap selOrgInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selOrgInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询柜台部门信息
	 * @param map
	 * @return
	 */
	@TimeLog
	public HashMap selCounterDepartmentInfo (Map map){
	    Map<String,Object> cacheMap = new HashMap<String,Object>();
	    cacheMap.put("counterCode", map.get("counterCode"));
	    cacheMap.put("brandInfoID", map.get("brandInfoID"));
	    cacheMap.put("organizationInfoID", map.get("organizationInfoID"));
		return binBEMQMES99_Service_Cache.selCounterDepartmentInfo_c(cacheMap);
	}
	
	/**
	 * 查询办事处部门信息
	 * @param map
	 * @return
	 */
	@TimeLog
	public HashMap selOfficeDepartmentInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selCounterDepartmentInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询仓库信息
	 * @param map
	 * @return
	 */
	public HashMap selPrmStockInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrmStockInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询员工信息
	 * @param map
	 * @return
	 */
	@TimeLog
	public HashMap selEmployeeInfo (Map map){
	    Map<String,Object> cacheMap = new HashMap<String,Object>();
	    cacheMap.put("BAcode", map.get("BAcode"));
	    cacheMap.put("brandInfoID", map.get("brandInfoID"));
	    cacheMap.put("organizationInfoID", map.get("organizationInfoID"));
	    return binBEMQMES99_Service_Cache.selEmployeeInfo_c(cacheMap);
	}

	/**
	 * 查询柜台积分计划信息
	 * @param map
	 * @return
	 */
	public HashMap getCounterPointPlan (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getCounterPointPlan");
		return (HashMap)baseServiceImpl.get(map);
	}

	/**
	 * 根据单据号查询销售业务数据主表信息
	 * @param map
	 * @return
	 */
	public HashMap getSaleRecordByBillCode (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getSaleRecordByBillCode");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询会员信息
	 * @param map
	 * @return
	 */
	@TimeLog
	public HashMap selMemberInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selMemberInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销产品信息
	 * 注：产品在初次查询时不过滤无效的产品，促销品不做这个处理（即过滤掉无效的促销品）
	 * 		产品不过滤无效产品原因：----目前促销品没有这种情况
	 * 				雅漾产品是通过第三方导入来维护产品（导入时会将新后台的产品进行无效后再导入）
	 * 				对于雅漾天猫线上的产品（只有新后台有而终端没有，每天的导入将会把这些线上的产品无效）
	 * 									
	 * @param map：organizationID、unitcode、barcode三个参数是必须的
	 * @return
	 */
	@TimeLog
	public HashMap selPrmProductInfo(Map map){
		Map<String,Object> cacheMap = new HashMap<String,Object>();
		cacheMap.put("organizationID", map.get("organizationID"));
		cacheMap.put("unitcode", map.get("unitcode"));
		cacheMap.put("barcode", map.get("barcode"));
		return binBEMQMES99_Service_Cache.selPrmProductInfo_c(cacheMap);
	}
	
	/**
	 * 查询促销产品信息  根据促销产品厂商ID(promotionProductVendorID)及部门ID(organizationID)
	 * @param map
	 * @return
	 */
	@TimeLog
	public HashMap selPrmProductInfoByPrmVenID(Map map){
	    Map<String,Object> cacheMap = new HashMap<String,Object>();
	    cacheMap.put("organizationID", map.get("organizationID"));
	    cacheMap.put("promotionProductVendorID", map.get("promotionProductVendorID"));
	    return binBEMQMES99_Service_Cache.selPrmProductInfoByPrmVenID_c(cacheMap);
	}
	
	/**
	 * 查询促销产品信息  根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID
	 * @param map
	 * @return
	 */
	@TimeLog
	public List selPrmAgainByPrmVenID(Map map){
	    Map<String,Object> cacheMap = new HashMap<String,Object>();
	    cacheMap.put("promotionProductVendorID", map.get("promotionProductVendorID"));
	    return binBEMQMES99_Service_Cache.selPrmAgainByPrmVenID_c(cacheMap);
	}
	
	/**
     * 查询促销产品信息  根据促销产品厂商ID，不区分有效状态
     * @param map
     * @return
     */
	@TimeLog
    public List selPrmByPrmVenID(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("promotionProductVendorID", map.get("promotionProductVendorID"));
        return binBEMQMES99_Service_Cache.selPrmByPrmVenID_c(cacheMap);
    }
	
	/**
	 * 查询barcode变更后的促销产品信息
	 * @param map
	 * @return
	 */
	@TimeLog
	public HashMap selPrmProductPrtBarCodeInfo(Map map){
	    Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("barcode", map.get("barcode"));
        cacheMap.put("unitcode", map.get("unitcode"));
        cacheMap.put("tradeDateTime", map.get("tradeDateTime"));
        return binBEMQMES99_Service_Cache.selPrmProductPrtBarCodeInfo_c(cacheMap);
	}
	
    /**
     * 查询barcode变更后的促销产品信息（按tradeDateTime与StartTime最接近的升序）
     * @param map
     * @return
     */
	@TimeLog
    public List<Map<String,Object>> selPrmPrtBarCodeList(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("barcode", map.get("barcode"));
        cacheMap.put("unitcode", map.get("unitcode"));
        cacheMap.put("tradeDateTime", map.get("tradeDateTime"));
        return binBEMQMES99_Service_Cache.selPrmPrtBarCodeList_c(cacheMap);
    }
	
//	/**
//	 * 查询逻辑仓库信息
//	 * @param map
//	 * @return
//	 */
//	public List selLogicInventoryInfoList(Map map){
//		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selLogicInventoryInfoList");
//		return baseServiceImpl.getList(map);
//	}
	
	/**
	 * 查询是否为促销品
	 * @param map
	 * @return
	 */
	public HashMap selIsPromotionProduct (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selIsPromotionProduct");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询机器号
	 * @param map
	 * @return
	 */
	@TimeLog
	public HashMap selMachinCode (Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("machineCode", map.get("machineCode"));
        cacheMap.put("organizationInfoID", map.get("organizationInfoID"));
        cacheMap.put("brandInfoID", map.get("brandInfoID"));
        return binBEMQMES99_Service_Cache.selMachinCode_c(cacheMap);
	}
	
	/**
	 * 添加会员信息
	 * @param map
	 * @return
	 */
	@TimeLog
	public int addMemberInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.addMemberInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 根据促销品厂商ID及unitCode、barCode查询促销品信息
	 * 查询编码条码对应表【历史表】符合条件的数据可能有多条，只取一条【厂商ID是唯一的】
	 * @param map: barcode、unitcode、productId参数是必须的
	 * @return 厂商ID及IsStock(放在MAP中)
	 */
	public Map<String, Object> getPrmPrtInfoByIdAndCode(Map<String, Object> map) {
		Map<String, Object> cacheMap = new HashMap<String, Object>();
		cacheMap.put("barcode", map.get("barcode"));
        cacheMap.put("unitcode", map.get("unitcode"));
        cacheMap.put("promotionProductVendorID", map.get("productId"));
        cacheMap.put("tradeDateTime", map.get("tradeDateTime"));
		return binBEMQMES99_Service_Cache.getPrmPrtInfoByIdAndCode_c(cacheMap);
	}
	
	/**
	 * 根据产品厂商ID及unitCode、barCode查询产品信息【按tradeDateTime与StartTime最接近的升序】
	 * 查询编码条码对应表【历史表】符合条件的数据可能有多条，只取一条【厂商ID是唯一的】
	 * @param map: barcode、unitcode、productId、tradeDateTime参数是必须的
	 * @return 厂商ID(放在MAP中)
	 */
	public Map<String, Object> getProductInfoByIdAndCode(Map<String, Object> map) {
		Map<String, Object> cacheMap = new HashMap<String, Object>();
		cacheMap.put("barcode", map.get("barcode"));
        cacheMap.put("unitcode", map.get("unitcode"));
        cacheMap.put("productVendorID", map.get("productId"));
        cacheMap.put("tradeDateTime", map.get("tradeDateTime"));
        return binBEMQMES99_Service_Cache.getProductInfoByIdAndCode_c(cacheMap);
	}
	
	/**
	 * 查询产品信息
	 * 注：产品在初次查询时不过滤无效的产品，促销品不做这个处理（即过滤掉无效的促销品）
	 * 		产品不过滤无效产品原因：----目前促销品没有这种情况
	 * 				雅漾产品是通过第三方导入来维护产品（导入时会将新后台的产品进行无效后再导入）
	 * 				对于雅漾天猫线上的产品（只有新后台有而终端没有，每天的导入将会把这些线上的产品无效）
	 * 
	 * 根据barcode、unitcode、brandInfoID、organizationInfoID查询产品表及产品厂商表中的产品信息【不管是否有效】
	 * @param map
	 * @return productVendorID（放在map中）
	 */
	@TimeLog
	public HashMap selProductInfo (Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("barcode", map.get("barcode"));
        cacheMap.put("unitcode", map.get("unitcode"));
        cacheMap.put("brandInfoID", map.get("brandInfoID"));
        cacheMap.put("organizationInfoID", map.get("organizationInfoID"));
		return binBEMQMES99_Service_Cache.selProductInfo_c(cacheMap);
	}
	
	/**
	 * 查询barcode变更后的产品信息
	 * 根据barcode、unitcode、tradeDateTime(此时间应在起止日期之内)查询产品条码对应关系表的产品信息
	 * 其中barcode、unitcode都是查询对应表中的相应的oldcode
	 * @param map
	 * @return barcode、unitcode、productVendorID（放在map中）
	 */
	@TimeLog
	public HashMap selPrtBarCode(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("barcode", map.get("barcode"));
        cacheMap.put("unitcode", map.get("unitcode"));
        cacheMap.put("tradeDateTime", map.get("tradeDateTime"));
        return binBEMQMES99_Service_Cache.selPrtBarCode_c(cacheMap);
	}
	
	/**
     * 查询barcode变更后的产品信息（按tradeDateTime与StartTime最接近的升序）
     * 根据barcode、unitcode、tradeDateTime（此时间必须大于StartTime）
     * 【其中barcode、unitcode的条件都为对应表中的oldcode】
     * @param map
     * @return
     */
	@TimeLog
    public List<Map<String,Object>> selPrtBarCodeList(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("barcode", map.get("barcode"));
        cacheMap.put("unitcode", map.get("unitcode"));
        cacheMap.put("tradeDateTime", map.get("tradeDateTime"));
        return binBEMQMES99_Service_Cache.selPrtBarCodeList_c(cacheMap);
    }
	
	/**
	 * 查询产品信息  根据产品厂商ID
	 * 根据productVendorID查询产品表及产品厂商表【两者必须是有效的】
	 * @param map
	 * @return productVendorID（放在MAP中）
	 */
	@TimeLog
	public HashMap selProductInfoByPrtVenID(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("productVendorID", map.get("productVendorID"));
        return binBEMQMES99_Service_Cache.selProductInfoByPrtVenID_c(cacheMap);
	}
	
	/**
	 * 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID
	 * @param map
	 * @return
	 */
	@TimeLog
	public List selProAgainByPrtVenID(Map map){
        Map<String,Object> cacheMap = new HashMap<String,Object>();
        cacheMap.put("productVendorID", map.get("productVendorID"));
        return binBEMQMES99_Service_Cache.selProAgainByPrtVenID_c(cacheMap);
	}
	
	/**
	 * 添加会员持卡信息
	 * @param map
	 * @return
	 */
	@TimeLog
	public void addMemCardInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.addMemCardInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 添加实体仓库信息
	 * @param map
	 * @return
	 */
	@CacheEvict(value="CherryIvtCache",allEntries=true,beforeInvocation=false)
	public int addInventoryInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.addInventoryInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入消息日志表
	 * @param mainDataDTO
	 */
	public int addMessageLog (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.addMQLog");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新机器信息表
	 * @param mainDataDTO
	 */
	public int updMachineInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.updMachineInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入消息信息(MongoDB)
	 * 
	 * @param map
	 * @throws CherryMQException 
	 */
	public void addMongoDBBusLog (DBObject dbObject) throws CherryMQException{
	   try{
		   String modifyCounts = (String)dbObject.get("ModifyCounts");
		   if(modifyCounts != null && !"".equals(modifyCounts)) {
			   dbObject.put("ModifyCounts", String.valueOf(Integer.parseInt(modifyCounts)));
		   } else {
			   dbObject.put("ModifyCounts", "0");
		   }
		   // 如果第一次插入失败将尝试重新插入
		   for (int i = 0; i <= CherryConstants.MGO_MAX_RETRY; i++) {
				try {
					MongoDB.insert(MessageConstants.MQ_BUS_LOG_COLL_NAME, dbObject);
					break;
				} catch (IllegalStateException ise) {
					if (i == CherryConstants.MGO_MAX_RETRY) {
						throw ise;
					}
					logger.error("**************************** Write mongodb fails! method : addMongoDBBusLog  time : " + (i + 1));
					long sleepTime = CherryConstants.MGO_SLEEP_TIME * (i + 1);
					// 延迟等待
					Thread.sleep(sleepTime);
				} catch (Exception e) {
					StringBuffer bf = new StringBuffer();
					bf.append("************ method addMongoDBBusLog throw exception! Exception Class : ")
					.append(e.getClass().getName()).append(" ************ Message : ").append(e.getMessage());
					logger.error(bf.toString(),e);
					throw e;
				} catch (Throwable t) {
					throw new Exception("method addMongoDBBusLog throw Throwable!!");
				}
			}
	   } catch (Exception e) {
		    throw new CherryMQException(MessageConstants.MSG_ERROR_12);
	   }
	}
	/**
	 * 查询U盘对应的员工信息
	 * @param map
	 * @return
	 */
	public Map selUdiskInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selUdiskInfo");
		return  (Map)baseServiceImpl.get(map);
	}
	/**
	 * 查询用户表对应的用户ID
	 * @param map
	 * @return
	 */
	public Map selUserByEempID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selUserByEempID");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询U盘信息表   by 员工ID
	 * @param map
	 * @return
	 */
	public Map selUdiskSNByEempID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selUdiskSNByEempID");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 删除接收成功的MQ日志记录
	 * @param map 删除条件
	 * @throws Exception 
	 */
	public void delMqLog(Map<String, Object> map) throws Exception{
		DBObject query = new BasicDBObject();
		// 单据号
		query.put("BillCode", map.get("tradeNoIF"));
		// 单据类型
		query.put("BillType", map.get("tradeType"));
		String modifyCounts = (String)map.get("modifyCounts");
		if(modifyCounts == null || "".equals(modifyCounts)) {
			// 修改次数
			query.put("ModifyCount", "0");
		} else {
			// 修改次数
			query.put("ModifyCount", modifyCounts);
		}
		// 品牌代码
		query.put("BrandCode", map.get("brandCode"));
		// 如果第一次删除失败将尝试重新删除
		for (int i = 0; i <= CherryConstants.MGO_MAX_RETRY; i++) {
			try {
				MongoDB.removeAll(CherryConstants.MGO_MQLOG, query);
				break;
			} catch (IllegalStateException ise) {
				if (i == CherryConstants.MGO_MAX_RETRY) {
					throw ise;
				}
				logger.error("**************************** Write mongodb fails! method : delMqLog  time : " + (i + 1));
				long sleepTime = CherryConstants.MGO_SLEEP_TIME * (i + 1);
				// 延迟等待
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				StringBuffer bf = new StringBuffer();
				bf.append("************ method delMqLog throw exception! Exception Class : ")
				.append(e.getClass().getName()).append(" ************ Message : ").append(e.getMessage());
				logger.error(bf.toString(),e);
				throw e;
			} catch (Throwable t) {
				throw new Exception("method delMqLog throw Throwable!!");
			}
		}
	}
	
	/**
	 * 通过会员卡号取得会员IDList
	 * @param map 查询条件
	 * @return 会员IDList
	 */
	public List<Map<String, Object>> getMemberIdList(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getMemberIdList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询产品分类IDList
	 * @param map 查询条件
	 * @return 产品分类IDList
	 */
	public List<String> selPrtCatPropValueId(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrtCatPropValueId");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询会员入会时间等属性 
	 * @param map
	 * @return
	 */
	public Map<String, Object> selMemJoinInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selMemJoinInfo");
		return  (Map)baseServiceImpl.get(map);
	}
	
	public Map<String, Object> upAndSendMem(Map<String, Object> map) {
		Map<String, Object> memJoinInfo = this.selMemJoinInfo(map);
		if (null != memJoinInfo && !memJoinInfo.isEmpty()) {
			boolean upFlg = false;
			String firstSaleDate = (String) memJoinInfo.get("firstSaleDate");
			if (!CherryChecker.isNullOrEmpty(firstSaleDate)) {
				String saletimecomp = (String) map.get("SALETIMECOMP");
				if (!CherryChecker.isNullOrEmpty(saletimecomp)) {
					if (DateUtil.compDateTime(saletimecomp, String.valueOf(memJoinInfo.get("firstSaleDateTime"))) > 0) {
						return null;
					}
				}
				String joinDate = (String) memJoinInfo.get("joinDate");
				String baseJdate = (String) map.get("BASEJDATE");
				boolean isEmpty = CherryChecker.isNullOrEmpty(joinDate);
				if (!isEmpty && !CherryChecker.isNullOrEmpty(baseJdate) 
						&& CherryChecker.checkDate(baseJdate)) {
					if (CherryChecker.compareDate(joinDate, baseJdate) < 0) {
						return null;
					}
				}
				if (isEmpty 
						|| CherryChecker.compareDate(firstSaleDate, joinDate) != 0) {
					upFlg = true;
				} else {
					String counterCodeBelong = (String) memJoinInfo.get("counterCodeBelong");
					String firstSaleCounterCode = (String) memJoinInfo.get("firstSaleCounterCode");
					boolean isNotNullFsc = !CherryChecker.isNullOrEmpty(firstSaleCounterCode);
					boolean isNullCcb = CherryChecker.isNullOrEmpty(counterCodeBelong);
					if (isNotNullFsc && isNullCcb || isNotNullFsc && !isNullCcb && 
							!firstSaleCounterCode.trim().equalsIgnoreCase(counterCodeBelong.trim())) {
						upFlg = true;
					} else {
						String baCodeBelong = (String) memJoinInfo.get("baCodeBelong");
						String firstBaCode = (String) memJoinInfo.get("firstBaCode");
						boolean isNotNullFbc = !CherryChecker.isNullOrEmpty(firstBaCode);
						boolean isNullBbl = CherryChecker.isNullOrEmpty(baCodeBelong);
						if (isNotNullFbc && isNullBbl || isNotNullFbc && !isNullBbl && 
								!firstBaCode.trim().equalsIgnoreCase(baCodeBelong.trim())) {
							upFlg = true;
						}
					}
				}
			}
			if (upFlg) {
				Map<String, Object> upMap = new HashMap<String, Object>();
				upMap.put("memberInfoId", map.get("memberInfoId"));
				upMap.put("firstSaleDate", memJoinInfo.get("firstSaleDate"));
				upMap.put("firstSaleCounter", memJoinInfo.get("firstSaleCounter"));
				upMap.put("firstSaleCounterCode", memJoinInfo.get("firstSaleCounterCode"));
				upMap.put("firstEmployeeId", memJoinInfo.get("firstEmployeeId"));
				upMap.put("firstBaCode", memJoinInfo.get("firstBaCode"));
				upMap.put("updatedBy", "BINBEMQMES99");
				upMap.put("updatePGM", "BINBEMQMES99");
				// 更新会员入会时间等属性
				this.updMemJoinInfo(upMap);
				upMap.put("ONLYREGMEM", "1");
				Map<String, Object> memInfo = this.getMemberInfoByID(upMap);
				if (null != memInfo && !memInfo.isEmpty()) {
					memInfo.put("orgCode", map.get("orgCode"));
					memInfo.put("brandCode", map.get("brandCode"));
					memInfo.put("organizationInfoId", map.get("organizationInfoId"));
					memInfo.put("brandInfoId", map.get("brandInfoId"));
					memInfo.put("subType", "1");
					memInfo.put("baCodeBelong", memInfo.get("employeeCode"));
					memInfo.put("counterCodeBelong", memInfo.get("organizationCode"));
					String birthYear = (String)memInfo.get("birthYear");
					String birthDay = (String)memInfo.get("birthDay");
					if(birthYear != null && !"".equals(birthYear) && birthDay != null && !"".equals(birthDay)) {
						memInfo.put("birth", birthYear+birthDay);
					}
					return memInfo;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取指定会员的建议书版本号属性
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSuggestVersionFromMemInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getSuggestVersionFromMemInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 更新会员建议书版本号属性
	 * @param map
	 * @return
	 */
	public int updMemSuggestVersionInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.updMemSuggestVersionInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 *更新会员入会时间等属性
	 * 
	 * @param map 查询条件
	 * @return 更新件数
	 */
	public int updMemJoinInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.updMemJoinInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新会员扩展属性
	 * 
	 * @param map 查询条件
	 * @return 更新件数
	 */
	public int updMemberExtInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.updMemberExtInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 添加会员扩展属性
	 * 
	 * @param map 添加内容
	 */
	public void addMemberExtInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.addMemberExtInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
     * 更新WebPos销售业务主表
     * @param mainDataDTO
     */
    public int updWebPosSaleRecord (Map map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.updWebPosSaleRecord");
        return baseServiceImpl.update(paramMap);
    }
    
    /**
	 * 查询问题为皮肤类型的问题号
	 * @param map
	 * @return
	 */
	public Map<String, Object> selSkinQuestion(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selSkinQuestion");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询会员信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMemberInfoByID(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getMemberInfoByID");
		return  (Map)baseServiceImpl.get(map);
	}
	
	/**
     * 更新会员发卡柜台
     * @param map
     */
    public int updMemberCounter (Map map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.updMemberCounter");
        return baseServiceImpl.update(paramMap);
    }


    /**
     * 查询柜台信息
     * @return
     */
	public Map<String, Object> getCounterInfoByCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getCounterInfoByCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}


	public Map<String, Object> getEmployeeInfoByCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getEmployeeInfoByCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 将MQ反向催单信息写入数据库
	 * @param map
	 */
	public void insertIntoReminder(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.insertIntoReminder");
		baseServiceImpl.save(map);
	}

	/**
	 * 判断重复数据
	 * @param map
	 * @return
	 */
	public int getReminderMsgCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getReminderMsgCount");
		return baseServiceImpl.getSum(map);
	}


	/**
	 * 查询产品或促销品是否存在
	 * @param map
	 * @return
	 */
	public Map<String, Object> getProductInfoMap(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getProductInfoMap");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}


	public Map<String, Object> getPrmProductInfoMap(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getPrmProductInfoMap");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
     * 更新积分变化维护履历主表
     * @param map
     */
    public int updateTmallUsedInfo (Map map){
    	map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.updateTmallUsedInfo");
        return baseServiceImpl.update(map);
    }
    
    /**
	 * 查询会员信息
	 * @param map
	 * @return
	 */
    public Map<String, Object> getMemberInfoByCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getMemberInfoByCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 根据单据号查询出交易记录
	 * @param map
	 * @return
     */
	public Map<String,Object> getCardTransactionByBillCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getCardTransactionByBillCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 更新储值卡的金额
	 * @param map
     * @return
     */
	public int updateCardCash(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.updateCardCash");
		return baseServiceImpl.update(map);
	}


	/**
	 * 将该交易记录解除冻结状态
	 * @param map
	 * @return
	 */
	public int relieveFrozen(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.relieveFrozen");
		return baseServiceImpl.update(map);
	}
}
