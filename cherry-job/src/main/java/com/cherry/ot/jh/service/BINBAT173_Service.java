package com.cherry.ot.jh.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BINBAT173_Service extends BaseService {

    /**
     * 获取所有ruleCode
     * @param map
     * @return
     */
    public List<String> getRuleCodeList(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT173.getRuleCodeList");
        return baseServiceImpl.getList(map);
    }

    public Map<String,Object> getRuleConditionMap(String ruleCode){
        return (Map<String, Object>) baseServiceImpl.get(ruleCode,"BINBAT173.getRuleConditionMap");
    }

    public List<Map<String,Object>> getCounterCodeByChannelID(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT173.getCounterCodeByChannelID");
        return baseServiceImpl.getList(map);
    }

    public List<Map<String,Object>> getCounterCodeByOrganizationID(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT173.getCounterCodeByOrganizationID");
        return baseServiceImpl.getList(map);
    }

    public void addCounterList(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list,"BINBAT173.addCounterList");
    }

    public void updateCounterDetail(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT173.updateCounterDetail");
        baseServiceImpl.update(map);
    }

    public void updateMemberDetail(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT173.updateMemberDetail");
        baseServiceImpl.update(map);
    }

    public void addProductDetail(List<Map<String,Object>> list){
        baseServiceImpl.saveAll(list,"BINBAT173.addProductDetail");
    }

    public List<Map<String,Object>> getProductListByCateID(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT173.getProductListByCateID");
        return baseServiceImpl.getList(map);
    }

    public List<Map<String,Object>> getCouponRule(Map<String,Object> map){
        return baseServiceImpl.getList(map, "BINBAT173.getCouponRule");
    }

    public void updatePromotionRule(String sendCondStr,String useCondStr,String ruleCode){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("sendCond",sendCondStr);
        map.put("useCond",useCondStr);
        map.put("ruleCode",ruleCode);
        map.put(CherryConstants.IBATIS_SQL_ID,"BINBAT173.updatePromotionRule");
        baseServiceImpl.update(map);
    }

    public int getUploadMemberCount(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID,"BINBAT173.getUploadMemberCount");
        return baseServiceImpl.getSum(map);
    }

    public int getUploadCounterCount(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID,"BINBAT173.getUploadCounterCount");
        return baseServiceImpl.getSum(map);
    }
}
