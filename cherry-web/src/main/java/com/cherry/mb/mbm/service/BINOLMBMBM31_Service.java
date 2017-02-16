package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

public class BINOLMBMBM31_Service {
    @Resource
    private BaseServiceImpl baseServiceImpl;

    /**
     * 取得竞争对手总数
     *
     * @param map
     * @return
     */
    public int getRuleCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM31.getRuleCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得规则List
     *
     * @param map
     * @return
     */
    public List getRuleList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM31.getRuleList");
        return baseServiceImpl.getList(map);
    }

    public List getRuleListWithoutPage(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM31.getRuleListWithoutPage");
        return baseServiceImpl.getList(map);
    }

    /**
     * 取得会员基本属性List
     *
     * @param map
     * @return
     */
    public List searchMemberAttrList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM31.searchMemberAttrList");
        return baseServiceImpl.getList(map);
    }

    public void saveMemCompleteRule(Map<String,Object> param){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(param);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM31.saveMemCompleteRule");
        baseServiceImpl.save(paramMap);
    }

    public int updateMemCompleteRule(Map<String,Object> param){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(param);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM31.updateMemCompleteRule");
        return baseServiceImpl.update(paramMap);
    }

    /**
     * 删除规则
     * @param map
     */
    public void deleteRule(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM31.deleteRule");
        baseServiceImpl.remove(paramMap);
    }

    /**
     * 更新规则截止时间
     * @param map
     */
    public void updateRuleEndTime(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM31.updateRuleEndTime");
        baseServiceImpl.update(paramMap);
    }

}
