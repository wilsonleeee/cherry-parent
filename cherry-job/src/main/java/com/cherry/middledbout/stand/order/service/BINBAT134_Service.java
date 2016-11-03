package com.cherry.middledbout.stand.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 标准接口：发货单导入Service
 * 
 * @author chenkuan
 * 
 * @version 2015/12/15
 * 
 */
public class BINBAT134_Service extends BaseService {

	
	/**
	 * 取得标准发货单接口表的单据号
	 * @param map
	 * @return
	 */
	public List<String> getBillCodeList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.getBillCodeList");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 更新标准发货单接口表的synchFlag字段
	 * @param map
	 * @return
	 */
	public int updateSynchFlag(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.updateSynchFlag");
		return tpifServiceImpl.update(map);
	}
	/**
	 * 更新标准发货单接口明细表的SynchMsg字段
	 * @param map
	 * @return
	 */
	public int updateDetailSynchMsg(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.updateDetailSynchMsg");
		return tpifServiceImpl.update(map);
	}
	/**
	 * 取得标准发货单接口表数据(主数据)
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getExportTransList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.getExportTransList");
		return tpifServiceImpl.getList(map);
	}
	
	
	/**
	 * 取得标准发货单接口表数据（单据明细）
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getExportTransListdeatils(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.getExportTransListdeatils");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 取得品牌Code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBAT134.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得 1.业务日期,2.日结标志
	 * 
	 * @param map 查询条件
	 * @return 业务日期
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBussinessDateMap(Map<String, Object> map){	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBECMINC99.getBussinessDateMap");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	/**************************预先验证产品OR促销品是否存在****************************/
	/**
	 * 查询促销产品信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> selPrmProductInfo(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("organizationID", map.get("organizationID"));
		paramMap.put("unitcode", map.get("UnitCode"));
		paramMap.put("barcode", map.get("BarCode"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.selPrmProductInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	/**
	 * 查询barcode变更后的促销产品信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> selPrmProductPrtBarCodeInfo(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("barcode", map.get("BarCode"));
		paramMap.put("unitcode", map.get("UnitCode"));
		paramMap.put("tradeDateTime", map.get("tradeDateTime"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.selPrmProductPrtBarCodeInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
    /**
     * 查询barcode变更后的促销产品信息（按tradeDateTime与StartTime最接近的升序）
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrmPrtBarCodeList(Map<String,Object> map){
    	Map<String,Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("barcode", map.get("BarCode"));
    	paramMap.put("unitcode", map.get("UnitCode"));
    	paramMap.put("tradeDateTime", map.get("tradeDateTime"));
    	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.selPrmPrtBarCodeList");
        return (List<Map<String,Object>>)baseServiceImpl.getList(paramMap);
    }
	/**
     * 查询促销产品信息  根据促销产品厂商ID，不区分有效状态
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrmByPrmVenID(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.selPrmByPrmVenID");
        return (List<Map<String,Object>>)baseServiceImpl.getList(map);
    }
	/**
	 * 查询产品信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> selProductInfo(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("barcode", map.get("BarCode"));
		paramMap.put("unitcode", map.get("UnitCode"));
		paramMap.put("brandInfoID", map.get("brandInfoId"));
		paramMap.put("organizationInfoID", map.get("organizationInfoId"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.selProductInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	/**
	 * 查询barcode变更后的产品信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> selPrtBarCode(Map<String,Object> map){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("barcode", map.get("BarCode"));
		paramMap.put("unitcode", map.get("UnitCode"));
		paramMap.put("tradeDateTime", map.get("tradeDateTime"));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.selPrtBarCode");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	/**
     * 查询barcode变更后的产品信息（按tradeDateTime与StartTime最接近的升序）
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrtBarCodeList(Map<String,Object> map){
    	Map<String,Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("barcode", map.get("BarCode"));
    	paramMap.put("unitcode", map.get("UnitCode"));
    	paramMap.put("tradeDateTime", map.get("tradeDateTime"));
    	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.selPrtBarCodeList");
        return (List<Map<String,Object>>)baseServiceImpl.getList(paramMap);
    }
    /**
     * 查询柜台部门信息
     * @param map
     * @return
     */
    public Map<String,Object> selCounterDepartmentInfo (Map<String,Object> map){
    	Map<String,Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("counterCode", map.get("counterCode"));
    	paramMap.put("brandInfoID", map.get("brandInfoId"));
    	paramMap.put("organizationInfoID", map.get("organizationInfoId"));
    	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT134.selCounterDepartmentInfo");
        return (Map<String,Object>)baseServiceImpl.get(paramMap);
    }
}
