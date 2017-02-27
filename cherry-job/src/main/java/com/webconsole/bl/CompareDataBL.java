package com.webconsole.bl;

import com.webconsole.service.CompareDataService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2017/2/22.
 */
public class CompareDataBL {

    /**新老后台销售数据比对Service */
    @Resource
    private CompareDataService compareDataService;

    /**
     * 取得新老后台销售差异数据
     *
     * @param map 查询条件
     * @return 差异数据
     */
    public List<Map<String, Object>> getDiffDataList(Map<String, Object> map) {

        //取得新老后台销售数据差异数据
        return compareDataService.getDiffData(map);
    }

}
