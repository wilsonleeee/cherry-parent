package com.cherry.webservice.member.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 获取所有会员等级信息
 * Created by jasonliu on 2017/1/11.
 */
public class MemberLevelsService extends BaseService {

    /**
     * 获取所有会员等级信息
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getMemberLevelList(Map<String,Object> paramMap){
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberLevels.getMemberLevelList");
        return baseServiceImpl.getList(paramMap);
    }
}
