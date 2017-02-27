package com.webconsole.service;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2017/2/22.
 */
public class CompareDataService extends BaseService {

    public List<Map<String, Object>> getDiffData(Map<String, Object> map) {
        map.put(CherryBatchConstants.IBATIS_SQL_ID, "compareData.getDiffDataList");
        return baseServiceImpl.getList(map);
    }

}
