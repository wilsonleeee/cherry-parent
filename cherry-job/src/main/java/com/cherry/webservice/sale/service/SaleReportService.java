package com.cherry.webservice.sale.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 销售报表Service
 * 
 * @author WangCT
 * @version 1.0 2014.11.24
 */
public class SaleReportService extends BaseService {

	/**
     * 统计柜台的销售金额和数量
     * 
     * @param map 查询条件
     * @return 柜台的销售金额和数量
     */
    public Map<String,Object> getSaleCountByCou(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleCountByCou");
        return (Map)baseServiceImpl.get(paramMap);
    }
    
    /**
     * 统计柜台的销售金额和数量（指定产品或者产品分类）
     * 
     * @param map 查询条件
     * @return 柜台的销售金额和数量
     */
    public Map<String,Object> getSaleCountByCouPrt(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleCountByCouPrt");
        return (Map)baseServiceImpl.get(paramMap);
    }
    
    /**
     * 统计柜台的发展会员数
     * 
     * @param map 查询条件
     * @return 柜台的发展会员数
     */
    public Map<String,Object> getMemCountByCou(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getMemCountByCou");
        return (Map)baseServiceImpl.get(paramMap);
    }
    
    /**
     * 根据柜台号取得部门ID
     * 
     * @param map 查询条件
     * @return 部门ID
     */
    public String getOrganizationId(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getOrganizationId");
        return (String)baseServiceImpl.get(paramMap);
    }
        
    /**
     * 
     * @author ZhaoCF
     * 根据柜台号取得品牌ID
     * 
     * @param map 查询条件
     * @return 品牌ID 
     *   
     */
    public String getBrandInfoId(Map<String,Object> map){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getBrandInfoId");
        return (String)baseServiceImpl.get(paramMap);
    }
        
    /**
     * 查询柜台销售记录
     * 
     * @param map
     * @return 销售记录
     */
    public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map); 
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "SaleReport.getSaleRecordList");
		return baseServiceImpl.getList(paramMap);
	}
    
    /**
     * 查询柜台销售记录+明细Detail
     * 
     * @param map
     * @return 销售记录明细
     */
    public List<Map<String, Object>> getSaleRecordDetailList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "SaleReport.getSaleRecordDetailList");
        return baseServiceImpl.getList(paramMap);
    }    
}
