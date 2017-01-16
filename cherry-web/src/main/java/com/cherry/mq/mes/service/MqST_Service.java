package com.cherry.mq.mes.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by menghao on 2017/1/16.
 */
public class MqST_Service extends BaseService {

    /**
     * 查询销售日目标设定表
     * @param map
     * @return
     */
    public List<Map<String,Object>> getSaleDayTarget(Map<String,Object> map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MqST.getSaleDayTarget");
        return baseServiceImpl.getList(parameterMap);
    }

    /**
     * 插入销售日目标设定表
     * @param map
     */
    public void insertSaleDayTarget(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MqST.insertSaleDayTarget");
        baseServiceImpl.save(parameterMap);
    }

    /**
     * 更新销售目标设定表
     * @param map
     * @return 更新件数
     */
    public int updateSaleDayTarget(Map map){
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "MqST.updateSaleDayTarget");
        return baseServiceImpl.update(parameterMap);
    }
}
