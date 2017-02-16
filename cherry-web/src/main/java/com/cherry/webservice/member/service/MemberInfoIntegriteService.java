package com.cherry.webservice.member.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.Map;

/**
 * 会员信息完整度查询
 * Created by jasonliu on 2017/2/8.
 */
public class MemberInfoIntegriteService extends BaseService {

    /**
     * 根据memCode获取会员信息
     * @param paramMap
     * @return
     */
    public Map<String,Object> getMemberInfoByCodeOrMessageId(Map<String,Object> paramMap){
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberInfoIntegrite.getMemberInfoByCodeOrMessageId");
        return (Map)baseServiceImpl.get(paramMap);
    }

    /**
     * 会员信息完整度查询
     * @param paramMap
     * @return
     */
    public Map<String,Object> getMemberInfoIntegrite(Map<String,Object> paramMap){
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberInfoIntegrite.getMemberInfoIntegrite");
        return (Map)baseServiceImpl.get(paramMap);
    }
}
