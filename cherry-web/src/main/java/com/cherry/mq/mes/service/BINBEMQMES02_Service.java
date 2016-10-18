/*  
 * @(#)BINBEMQMES02_Service.java     1.0 2011/05/31      
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

import org.springframework.cache.annotation.Cacheable;

import com.cherry.cm.annota.TimeLog;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 产品消息数据接收处理Service
 * @author huzude
 *
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES02_Service extends BaseService{
	
	/**
	 * 查询销售业务数据主表
	 * @param map
	 * @return
	 */
    @TimeLog
	public Map selSaleRecord (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selSaleRecord");
		return (Map)baseServiceImpl.get(map);
	}
	/**
	 * 通过交易时间查询销售业务数据主表  判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selSaleRecordByTradeTime (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selSaleRecordByTradeTime");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 通过交易日期及单据号查询产品退库主表 判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selProductReturnByNoIF (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selProductReturnByNoIF");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 通过交易日期及单据号查询调拨业务主表  判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selAllocationByAllocationNoIF (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selAllocationByAllocationNoIF");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 通过交易日期及单据号查询产品入库主表  判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selProductReceiveByNoIF (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selProductReceiveByNoIF");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 通过交易日期及单据号查询产品调出确认及促销产品调拨主表主表  判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selAllocationOutByNoIF (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selAllocationOutByNoIF");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 通过交易日期及单据号查询盘点主表  判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selStockTakingByNoIF (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selStockTakingByNoIF");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 生日礼领用单据号查询入出库主表  判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selStockByBirPresentNoIF (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selStockByBirPresentNoIF");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 通过交易日期及单据号查询产品订货单  判断是否是重复的数据
	 * @param map
	 * @return
	 */
	public int selProductOrderByDateNoIF (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selProductOrderByDateNoIF");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询销售业务数据明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public List selSaleRecordDetail (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selSaleRecordDetail");
		return (List)baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询产品入出库主表
	 * @param map
	 * @return
	 */
	@TimeLog
	public Map selProductInOut (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selProductInOut");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询产品入出库明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public List selProductInOutDetail (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selProductInOutDetail");
		return (List)baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询产品库存数据
	 * @param map
	 * @return 
	 * @return
	 */
	public Map<String,Integer>  selPrtStockNumInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selPrtStockNumInfo");
		return (Map)baseServiceImpl.get(map);
	}
	/**
	 * 查询促销产品收发货 发货部门
	 * @param map
	 * @return 
	 * @return
	 */
	public Map<String,Integer>  selPrmDev (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selPrmDev");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入销售业务数据主表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int addSaleRecord(Map map){
	    //花费积分如果是小数，转成整型
	    String strCostpoint = ConvertUtil.getString(map.get("costpoint"));
	    if(!"".equals(strCostpoint)){
	        int costpoint = 0;
	        try{
	            costpoint = (int)Double.parseDouble(strCostpoint);
	        }catch(Exception e){
	            
	        }
	        map.put("costpoint", costpoint);
	    }
	    
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addSaleRecord");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入销售业务数据明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public void addSaleRecordDetail (List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addSaleRecordDetail");	
	}
	
	/**
	 * 插入历史销售业务数据主表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int addSaleRecordHistory(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addSaleRecordHistory");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入历史销售业务数据明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public void addSaleDetailHistory(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addSaleDetailHistory");	
	}
	
	/**
	 * 删除销售业务数据主表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int delSaleRecord(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.delSaleRecord");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除销售业务数据明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int delSaleRecordDetail(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.delSaleRecordDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除产品入出库数据主表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int delProductInOut(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.delProductInOut");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除产品入出库数据明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int delProductInOutDetail(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.delProductInOutDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 插入产品入出库主表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int addProductInOut(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductInOut");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品入出库明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public void addProductInOutDetail (List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addProductInOutDetail");	
	}
	
	/**
	 * 插入历史产品入出库主表
	 * @param map
	 * @return
	 */
	@TimeLog
	public int addProductInOutHistory(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductInOutHistory");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入历史产品入出库明细表
	 * @param map
	 * @return
	 */
	@TimeLog
	public void addProductInOutDetailHistory (List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addProductInOutDetailHistory");	
	}
	
	/**
	 * 更新产品库存
	 * @param map
	 * @return
	 */
	@TimeLog
	public int updProductStock(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.updProductStock");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入产品库存
	 * @param map
	 * @return
	 */
	@TimeLog
	public void addProductStock(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductStock");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 插入产品收货主表
	 * @param map
	 * @return
	 */
	public int addProductReceive(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductReceive");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品收货明细表
	 * @param map
	 * @return
	 */
	public void addProductReceiveDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addProductReceiveDetail");	
	}
	
	/**
	 * 插入产品退库主表
	 * @param map
	 * @return
	 */
	public int addProductReturn(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductReturn");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品退库明细表
	 * @param map
	 * @return
	 */
	public void addProductReturnDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addProductReturnDetail");	
	}
	
	/**
	 * 插入产品调拨申请单据表
	 * @param map
	 * @return
	 */
	public int addProductAllocation(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductAllocation");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品调拨申请单据表明细表
	 * @param map
	 * @return
	 */
	public void addProductAllocationDetail (List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addProductAllocationDetail");	
	}
	
//	/**
//	 * 插入产品订货单据表
//	 * @param map
//	 * @return
//	 */
//	public int addProductOrder(Map map){
//		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductOrder");
//		return baseServiceImpl.saveBackId(map);
//	}
	
//	/**
//	 * 插入产品订货单据表明细表
//	 * @param map
//	 * @return
//	 */
//	public void addProductOrderDetail (List detailDataList){
//		// 批量插入
//		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addProductOrderDetail");	
//	}
	
	/**
	 * 插入产品调出单据表
	 * @param map
	 * @return
	 */
	public int addProductAllocationOut(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductAllocationOut");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品调出单据明细表
	 * @param map
	 * @return
	 */
	public void addProductAllocationOutDetail (List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addProductAllocationOutDetail");	
	}
	
	/**
	 * 插入产品调入单据表
	 * @param map
	 * @return
	 */
	public int addProductAllocationIn(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductAllocationIn");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品调入单据明细表
	 * @param map
	 * @return
	 */
	public void addProductAllocationInDetail (List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addProductAllocationInDetail");	
	}
	
    /**
     * 更新产品调拨申请单
     * @param map
     * @return
     */
    public int updateProductAllocation(Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.updateProductAllocation");
        return baseServiceImpl.update(map);
    }
	
	/**
	 * 插入产品盘点单据表
	 * @param map
	 * @return
	 */
	public int addProductStockTaking(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addProductStockTaking");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品盘点单据明细表
	 * @param map
	 * @return
	 */
	public void addProductTakingDetail(List detailDataList){
		// 批量插入
		baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addProductTakingDetail");	
	}
	
	/**
	 * 查询产品信息 获得结算价格   根据产品厂商ID
	 * @param map
	 * @return
	 */
	public HashMap selPrtGetPriceByPrtVenID(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selPrtGetPriceByPrtVenID");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询发货单据号 
	 * @param map
	 * @return
	 */
	public HashMap selPrtDeliverInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selPrtDeliverInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
     * 查询产品发货单据号 
     * @param map
     * @return
     */
	public List<Map<String,Object>> selPrtDeliverList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selPrtDeliverList");
        return baseServiceImpl.getList(map);
    }
	
    /**
     * 查询产品发货单据号根据WorkFlowID
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrtDeliverListByWorkFlowID(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selPrtDeliverListByWorkFlowID");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 查询订货单信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrtOrderList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selPrtOrderList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 更新产品收货主表（总数量、总金额）
     * @param map
     * @return
     */
    public int updProductReceive(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.updProductReceive");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 插入礼品领用主表
     * @param map
     * @return
     */
    @TimeLog
    public int addGiftDraw(Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.addGiftDraw");
        return baseServiceImpl.saveBackId(map);
    }
    
    /**
     * 插入礼品领用明细表
     * @param map
     * @return
     */
    @TimeLog
    public void addGiftDrawDetail (List detailDataList){
        // 批量插入
        baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addGiftDrawDetail");
    }
    
    /**
     * 更新销售业务数据主表（备注）
     * @param map
     * @return
     */
    @TimeLog
    public int updSaleRecord(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.updSaleRecord");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 更改销售单据状态
     * @param map
     * @return
     */
    @TimeLog
    public int updSaleRecordBillState(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.updSaleRecordBillState");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 插入销售支付构成表
     * @param detailDataList
     * @return
     */
    @TimeLog
    public void addSalePayList(List detailDataList){
        // 批量插入
        baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addSalePayList");   
    }
    
    /**
     * 查询促销活动主码
     * @param map
     * @return
     */
    @Cacheable(value="CherryActivityCache")
    @TimeLog
    public List<Map<String,Object>> getActivityMainCodeList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.getActivityMainCodeList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取促销活动组的活动组代号
     * @param map
     * @return
     */
    @TimeLog
    public List<Map<String,Object>> getPrmActGroupCodeList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.getPrmActGroupCodeList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取会员活动表的活动代号
     * @param map
     * @return
     */
    @Cacheable(value="CherryActivityCache")
    @TimeLog
    public List<Map<String,Object>> getCampaignCodeList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.getCampaignCodeList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 查询CampaignCode是否在会员活动表存在
     * @param map
     * @return
     */
    public List<Map<String,Object>> getCampaignList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.getCampaignList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 插入会员参与活动履历表
     * @param detailDataList
     * @return
     */
    @TimeLog
    public void addCampaignHistory(List detailDataList){
        // 批量插入
        baseServiceImpl.saveAll(detailDataList, "BINBEMQMES02.addCampaignHistory");   
    }
    
    /**
     * 会员活动履历List
     * @param map
     * @return
     */
    public List<Map<String,Object>> getCampaignHistoryList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.getCampaignHistoryList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 删除会员参与活动履历表
     * @param map
     * @return
     */
    @TimeLog
    public int delCampaignHistory(Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.delCampaignHistory");
        return baseServiceImpl.remove(map);
    }
    
    /**
     * 查询会员活动预约主表信息
     * @param map
     * @return
     */
    @TimeLog
    public List<Map<String,Object>> getCampaignOrderList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.getCampaignOrderList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 查询会员活动预约明细表
     * @param map
     * @return
     */
    public List<Map<String,Object>> getCampaignOrderDetailList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.getCampaignOrderDetailList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 更新会员活动预约主表的预约单状态
     * @param map
     * @return
     */
    @TimeLog
    public int updCampaignOrderState(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.updCampaignOrderState");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 通过生日礼领用单据号查询礼品领用主表
     * @param map
     * @return
     */
    public List<Map<String,Object>> selGiftDrawNoIF(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selGiftDrawNoIF");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 查询礼品领用表比较时间
     * @param map
     * @return
     */
    @TimeLog
    public List<Map<String,Object>> getGiftDrawList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.getGiftDrawList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 查询礼品领用明细表的最大DetailNo
     * @param map
     * @return
     */
    public List<Map<String,Object>> getMaxSPDetailList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.getMaxSPDetailList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 无效礼品领用主表
     * @param map
     * @return
     */
    @TimeLog
    public int delGiftDraw(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.delGiftDraw");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 无效礼品领用明细表
     * @param map
     * @return
     */
    @TimeLog
    public int delGiftDrawDetail(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.delGiftDrawDetail");
        return baseServiceImpl.update(map);
    }
    
    /**
	 * 查询会员俱乐部ID
	 * @param map
	 * @return 
	 */
	public Integer selMemClubId(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selMemClubId");
		Map<String, Object> clubMap = (Map<String, Object>) baseServiceImpl.get(map);
		if (null != clubMap && null != clubMap.get("memberClubId")) {
			return Integer.parseInt(clubMap.get("memberClubId").toString());
		}
		return null;
	}
    
    /**
     * 查询产品调拨单信息
     * @param map : relevantNo
     * @return
     */
    public List<Map<String,Object>> selProductAllocationList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.selProductAllocationList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 更新BaCoupon的UseFlag为1
     * @param map
     * @return
     */
    public int updateBaCoupon(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.updateBaCoupon");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 更新电商订单的BillState为4已领用
     * @param map
     * @return
     */
    public int updateESOrderMainBillState(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.updateESOrderMainBillState");
        return baseServiceImpl.update(map);
    }
    /**
     * 查询销售退货申请单号
     * @param map
     * @return
     */
	public Map SaleReturnRequestInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES02.SaleReturnRequestInfo");
		return (Map)baseServiceImpl.get(map);
	}
}
