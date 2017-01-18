package com.cherry.webservice.member.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.Map;

/**
 * 获取会员发货单查询
 * Created by jasonliu on 2017/1/12.
 */
public class MemberDispatchBillService extends BaseService {

    /**
     * 获取所有会员等级信息
     * @param paramMap
     * @return
     */
    public Map<String,Object> getMemberDispatchBill(Map<String,Object> paramMap){
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberDispatchBill.getMemberDispatchBill");
        return (Map)baseServiceImpl.get(paramMap);
    }
}
