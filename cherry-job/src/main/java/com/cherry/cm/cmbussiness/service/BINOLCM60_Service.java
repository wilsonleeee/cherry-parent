package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * @author lzs	
 * @version 1.0 2015.09.28
 */
public class BINOLCM60_Service extends BaseService{
    /**
     * 查找会员信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getMemberInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM60.getMemberInfo");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 插入会员信息表，返回主表ID
     * @param map
     * @return
     */
    public int addMemberInfo(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM60.addMemberInfo");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入会员持卡信息表
     * @param map
     * @return
     */
    public void addMemCardInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM60.addMemCardInfo");
        baseServiceImpl.save(paramMap);
    }
    
    /**
     * 添加地址信息
     * 
     * @param map 添加内容
     * @return 地址ID
     */
    public int addAddressInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM60.addAddressInfo");
        return baseServiceImpl.saveBackId(paramMap);
    }
    
    /**
     * 添加会员地址
     * 
     * @param map 添加内容
     */
    public void addMemberAddress(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM60.addMemberAddress");
        baseServiceImpl.save(paramMap);
    }
    
    /**
     * 取得产品BOM的信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getBomPrtList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM60.getBomPrtList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 取得产品+促销品信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getProPrmList(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.putAll(map);
    	paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM60.getProPrmList");
    	return baseServiceImpl.getList(paramMap);
    }
}
