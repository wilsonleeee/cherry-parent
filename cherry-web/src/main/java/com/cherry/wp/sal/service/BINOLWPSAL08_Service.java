package com.cherry.wp.sal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

public class BINOLWPSAL08_Service extends BaseService{
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPromotionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getPromotionList");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCampaignPromotionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getCampaignPromotionList");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCampaignList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getCampaignList");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCouponOrderList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getCouponOrderList");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPromotionInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getPromotionInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPromotionProduct(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getPromotionProduct");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCampaignInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getCampaignInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCampaignProduct(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getCampaignProduct");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getActivityType(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getActivityType");
        return baseServiceImpl.getList(paramMap);
    }
	
	@SuppressWarnings("unchecked")
    public Map<String, Object> getPromotionProductinfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getPromotionProductinfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
	
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getLYHDProductInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getLYHDProductInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getLYHDProductDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getLYHDProductDetail");
        return baseServiceImpl.getList(paramMap);
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRegular(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getRegular");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> checkMobilephoneMemcode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(CherryUtil.removeEmptyVal(map));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.checkMobilephoneMemcode");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getProductBrand(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(CherryUtil.removeEmptyVal(map));
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWPSAL08.getProductBrand");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
    
}
