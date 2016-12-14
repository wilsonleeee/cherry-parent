package com.cherry.mq.mes.bl;

import com.cherry.mq.mes.service.StandardMq_Service;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO： 标准共通Mq相关数据查询接口
 * @ClassName: StandardMq_BL
 * @Description: 查询参数Key将统一使用数据库表中定义的字段名称(即首字母大写的形式)
 * @author Wangminze
 * @version 2016/11/30.
 *
 */
public class StandardMq_BL {

    @Resource( name = "standardMq_Service")
    private StandardMq_Service standardMq_Service;

    /**
     * TODO : 从配置数据库查询品牌数据库对应表获取所有品牌的数据源
     * @param :
     * @return : List
     * @antuor : Wangminze
     * @version : 2016/11/30
     **/
    public List<Map<String, Object>> getBrandDataSourceConfigList (){
        return standardMq_Service.getBrandDataSourceConfigList();
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
    public HashMap<String,Object> selBrandInfo(Map map){
        return standardMq_Service.selBrandInfo(map);
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
        return standardMq_Service.addMessageLog(map);
    }

}
