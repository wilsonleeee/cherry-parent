/*  
 * @(#)BINBEMQMES99_Service_Cache.java     1.0 2013/12/09      
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

/**
 * 消息数据接收共通处理Service（缓存）
 * @author niushunjie
 * @version 1.0 2013.12.09
 */
@SuppressWarnings("unchecked")
public class BINBEMQMES99_Service_Cache extends BaseService{
	
    /**
     * 查询柜台部门信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryDepartCache")
    public HashMap selCounterDepartmentInfo_c (Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selCounterDepartmentInfo");
        return (HashMap)baseServiceImpl.get(map);
    }
	
    /**
     * 查询员工信息（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryEmpCache")
    public HashMap selEmployeeInfo_c (Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selEmployeeInfo");
        return (HashMap)baseServiceImpl.get(map);
    }
	
	/**
	 * 查询促销产品信息（缓存）
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryPromotionCache")
	public HashMap selPrmProductInfo_c(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrmProductInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销产品信息  根据促销产品厂商ID（缓存）
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryPromotionCache")
	public HashMap selPrmProductInfoByPrmVenID_c(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrmProductInfoByPrmVenID");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询促销产品信息  根据促销产品厂商ID，去查产品ID，再去查有效的厂商ID（缓存）
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryPromotionCache")
	public List selPrmAgainByPrmVenID_c(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrmAgainByPrmVenID");
		return (List)baseServiceImpl.getList(map);
	}
	
	/**
     * 查询促销产品信息  根据促销产品厂商ID，不区分有效状态（缓存）
     * @param map
     * @return
     */
	@Cacheable(value="CherryPromotionCache")
    public List selPrmByPrmVenID_c(Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrmByPrmVenID");
        return (List)baseServiceImpl.getList(map);
    }
	
	/**
	 * 查询barcode变更后的促销产品信息（缓存）
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryPromotionCache")
	public HashMap selPrmProductPrtBarCodeInfo_c(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrmProductPrtBarCodeInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
    /**
     * 查询barcode变更后的促销产品信息（按tradeDateTime与StartTime最接近的升序）（缓存）
     * @param map
     * @return
     */
	@Cacheable(value="CherryPromotionCache")
    public List<Map<String,Object>> selPrmPrtBarCodeList_c(Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrmPrtBarCodeList");
        return (List<Map<String,Object>>)baseServiceImpl.getList(map);
    }
	
	/**
	 * 根据促销品厂商ID及unitCode、barCode查询促销品信息(缓存)
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryPromotionCache")
	public Map<String, Object> getPrmPrtInfoByIdAndCode_c(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getPrmPrtInfoByIdAndCode");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 根据产品厂商ID及unitCode、barCode查询促销品信息(缓存)
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryProductCache")
	public Map<String, Object> getProductInfoByIdAndCode_c(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.getProductInfoByIdAndCode");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询产品信息（缓存）
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryProductCache")
	public HashMap selProductInfo_c(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selProductInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询barcode变更后的产品信息（缓存）
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryProductCache")
	public HashMap selPrtBarCode_c(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrtBarCode");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
     * 查询barcode变更后的产品信息（按tradeDateTime与StartTime最接近的升序）（缓存）
     * @param map
     * @return
     */
	@Cacheable(value="CherryProductCache")
    public List<Map<String,Object>> selPrtBarCodeList_c(Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selPrtBarCodeList");
        return (List<Map<String,Object>>)baseServiceImpl.getList(map);
    }
	
	/**
	 * 查询产品信息  根据产品厂商ID（缓存）
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryProductCache")
	public HashMap selProductInfoByPrtVenID_c(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selProductInfoByPrtVenID");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询产品信息  根据产品厂商ID，去查产品ID，再去查有效的厂商ID（缓存）
	 * @param map
	 * @return
	 */
	@Cacheable(value="CherryProductCache")
	public List selProAgainByPrtVenID_c(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selProAgainByPrtVenID");
		return (List)baseServiceImpl.getList(map);
	}
	
    /**
     * 查询机器号（缓存）
     * @param map
     * @return
     */
    @Cacheable(value="CherryMachineCache")
    public HashMap selMachinCode_c (Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES99.selMachinCode");
        return (HashMap)baseServiceImpl.get(map);
    }
}