package com.cherry.lg.top.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author Wangminze
 * @version 2016/12/22
 * @description
 */
public class Top_Service extends BaseService {

    /**
     * 取得柜台消息
     *
     * @param map
     * @return
     */
    public List<Map<String,Object>> getMsgList2(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "Top.getMsgList2");
        return baseServiceImpl.getList(map);

    }

    /**
     * 取得柜台消息数量
     * @param map
     * @return
     */
    public int getMsgList2Count(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "Top.getMsgList2Count");
        return baseServiceImpl.getSum(map);
    }

}
