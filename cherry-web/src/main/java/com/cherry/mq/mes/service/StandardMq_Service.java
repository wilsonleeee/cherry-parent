package com.cherry.mq.mes.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: 标准MQ服务类
 *
 *@author: Wangminze
 *@Date: 2016/11/30
 **/
public class StandardMq_Service extends BaseService{

    /**
     * TODO : 从配置数据库查询品牌数据库对应表获取所有品牌的数据源
     * @param :
     * @return : List<Map<String, Object>> KEY:['BrandCode','DataSourceName']
     * @antuor : Wangminze
     * @version : 2016/11/30
     **/
    public List<Map<String, Object>> getBrandDataSourceConfigList (){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "StandardMq_DAO.getBrandDataSourceConfigList");
        return baseConfServiceImpl.getList(paramMap);
    }

    /**
     * TODO : 查询品牌信息
     *
     * @description :
     * @param : map['BrandCode']
     * @return : HashMap<String,Object> KEY:['BIN_BrandInfoID','BIN_OrganizationInfoID','OrgCode']
     * @antuor : Wangminze
     * @version : 2016/11/30
     **/
    public HashMap<String,Object> selBrandInfo (Map map){
        map.put(CherryConstants.IBATIS_SQL_ID, "StandardMq_DAO.selBrandInfo");
        return (HashMap<String,Object>)baseServiceImpl.get(map);
    }

    /**
     * TODO : 插入消息日志表
     *
     * @description :
     * @param : map<String,Object>
     * @return : int 插入的id
     * @antuor : Wangminze
     * @version : 2016/11/30
     **/
    public int addMessageLog (Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "StandardMq_DAO.addMQLog");
        return baseServiceImpl.saveBackId(map);
    }


}
