/*		
 * @(#)BINBEMQMES01_Service.java     1.0 2010/12/01		
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

import com.cherry.cm.annota.TimeLog;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销品消息数据接收处理Service
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES01_Service extends BaseService{
	
	
	/**
	 * 查询品牌信息
	 * @param map
	 * @return
	 */
	public HashMap selBrandInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selBrandInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询柜台部门信息
	 * @param map
	 * @return
	 */
	public HashMap selCounterDepartmentInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selCounterDepartmentInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询办事处部门信息
	 * @param map
	 * @return
	 */
	public HashMap selOfficeDepartmentInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selCounterDepartmentInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询仓库信息
	 * @param map
	 * @return
	 */
	public HashMap selPrmStockInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPrmStockInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询调拨单据号
	 * @param map
	 * @return
	 */
	public HashMap selPrmAllocationNo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPrmAllocationNo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销品销售单据表ID
	 * @param map
	 * @return
	 */
	public HashMap selPromotionSaleID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPromotionSaleID");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询销售单据明细表最大明细连番
	 * @param map
	 * @return
	 */
	public HashMap selPromotionSaleDetailMaxNo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPromotionSaleDetailMaxNo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销品入出库单据表ID
	 * @param map
	 * @return
	 */
	public HashMap selPromotionStockInOutID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPromotionStockInOutID");
		return (HashMap)baseServiceImpl.get(map);
	}

	/**
	 * 查询发货信息
	 * @param map
	 * @return
	 */
	public HashMap selPromotionDeliverInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPromotionDeliverInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询员工信息
	 * @param map
	 * @return
	 */
	public HashMap selEmployeeInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selEmployeeInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询会员信息
	 * @param map
	 * @return
	 */
	public HashMap selMemberInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selMemberInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销产品信息
	 * @param map
	 * @return
	 */
	public HashMap selPrmProductInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPrmProductInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询逻辑仓库信息
	 * @param map
	 * @return
	 */
	public List selLogicInventoryInfoList(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selLogicInventoryInfoList");
		return baseServiceImpl.getList(map);
	}
	
	
	/**
	 * 查询促销品库存数量
	 * @param map
	 * @return
	 */
	public HashMap selPrmStockNumInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPrmStockNumInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询库存促销品信息
	 * @param map
	 * @return
	 */
	public List selPrmPrtStockInfoList (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPrmPrtStockInfoList");
		return baseServiceImpl.getList(map);
	}
	
	
	/**
	 * 查询是否为促销品
	 * @param map
	 * @return
	 */
	public HashMap selIsPromotionProduct (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selIsPromotionProduct");
		return (HashMap)baseServiceImpl.get(map);
	}

	
	/**
	 * 插入消息日志表
	 * @param mainDataDTO
	 */
	public void addMessageLog (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addMQLog");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入促销品库存操作流水表
	 * @param map
	 * @return
	 */
	public int addPromotionInventoryLog(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addPromotionInventoryLog");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入促销产品销售业务单据表
	 * @param map
	 * @return
	 */
	public int addPromotionSale (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addPromotionSale");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入促销产品销售业务单据明细表
	 * @param detailDataList
	 */
	public void addPromotionSaleDetail (List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES01.addPromotionSaleDetail");	
	}
	
	/**
	 * 插入促销产品入出库表
	 * @param map
	 */
	@TimeLog
	public int addPromotionStockInOut (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addPromotionStockInOut");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入促销产品入出库明细表
	 * @param detailDataList
	 */
	@TimeLog
	public void addPromotionStockDetail (List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES01.addPromotionStockDetail");	
	}
	
	/**
	 * 插入促销品库存数据
	 * @param map
	 */
	@TimeLog
	public void addPromotionStock (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addPromotionStock");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入促销产品收发货业务单据表
	 * @param map
	 */
	public int addPromotionDeliver(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addPromotionDeliver");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入促销产品收发货业务单据明细表
	 * @param detailDataList
	 */
	public void addPromotionDeliverDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES01.addPromotionDeliverDetail");	
	}
	
	/**
	 * 插入促销产品退库业务单据表
	 * @param map
	 */
	public int addPromotionReturn(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addPromotionReturn");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入促销产品退库业务单据明细表
	 * @param detailDataList
	 */
	public void addPromotionReturnDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES01.addPromotionReturnDetail");	
	}
	
	
	/**
	 * 插入促销产品调拨业务单据表 
	 * @param map
	 * @return
	 */
	public int addPromotionAllocation(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addPromotionAllocation");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入促销产品调拨业务单据明细表
	 * @param detailDataList
	 */
	public void addPromotionAllocationDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES01.addPromotionAllocationDetail");	
	}
	
	/**
	 * 插入促销产品盘点业务单据表
	 * @param map
	 * @return
	 */
	public int addPromotionStockTaking(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addPromotionStockTaking");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入促销产品盘点业务单据明细表
	 * @param detailDataList
	 */
	public void addPromotionTakingDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES01.addPromotionTakingDetail");	
	}
	
	/**
	 * 更新促销品库存数据
	 * @param map
	 */
	@TimeLog
	public int updPromotionStock (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.updPromotionStock");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新促销产品收发货业务单据表
	 * @param map
	 */
	public void updPromotionDeliver(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.updPromotionDeliver");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 更新促销产品销售单据表
	 * @param map
	 */
	public void updPromotionSale(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.updPromotionSale");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 更新促销产品入出库单据表
	 * @param map
	 */
	public void updPromotionStockInOut(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.updPromotionStockInOut");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 查询促销产品入出库单据表
	 * @param map
	 */
	@TimeLog
	public Map selPrmProductInOut(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPrmProductInOut");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销产品入出库明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public List selPrmProductInOutDetail (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.selPrmProductInOutDetail");
		return (List)baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入历史促销产品入出库主表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int addPrmProductInOutHistory(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.addPrmProductInOutHistory");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入历史促销产品入出库明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public void addPrmProductInOutDetailHistory (List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES01.addPrmProductInOutDetailHistory");	
	}
	
	/**
	 * 删除促销产品入出库数据主表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int delPrmProductInOut(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.delPrmProductInOut");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销产品入出库数据明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int delPrmProductInOutDetail(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.delPrmProductInOutDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 更新促销品退库主表（总数量、总金额）
	 * @param map
	 * @return
	 */
	public int updPromotionReturn(Map<String,Object> map){
	    map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES01.updPromotionReturn");
	    return baseServiceImpl.update(map);
	}
}
