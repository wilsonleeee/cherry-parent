package com.cherry.cm.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统的一些全局配置放置在这里，以减少SQL查询，提高效率
 * 比如每个发布的系统内的品牌的基本信息（组织代号，品牌代号，数据源等等）
 * Created by dingyongchang on 2016/12/15.
 */
public class SystemConfigManager implements InitializingBean {
    protected static final Logger logger = LoggerFactory.getLogger(SystemConfigManager.class);

    private static List<SystemConfigDTO> brandList = new ArrayList<SystemConfigDTO>();
    @Resource
    private BaseConfServiceImpl baseConfServiceImpl;

    @Resource
    protected BaseServiceImpl baseServiceImpl;

    public  void afterPropertiesSet() throws Exception {
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("ibatis_sql_id", "SystemInitialize.getBrandDataSourceConfigList");
            List<SystemConfigDTO> list = baseConfServiceImpl.getList(paramMap);
            if(null!=list) {
                for (SystemConfigDTO dto : list) {
                    try {
                        //到各个品牌数据库中去查询品牌的信息,补全两个ID
                        CustomerContextHolder.setCustomerDataSourceType(dto.getDataSourceName());
                        SystemConfigDTO tmp = (SystemConfigDTO) baseServiceImpl.get(dto, "SystemInitialize.getBrandInfo");
                        if(null!=tmp) {
                            dto.setOrganizationInfoID(tmp.getOrganizationInfoID());
                            dto.setBrandInfoID(tmp.getBrandInfoID());
                        }
                    }finally {
                        CustomerContextHolder.clearCustomerDataSourceType();
                    }
                }
                brandList.addAll(list);
            }
        } catch (Exception e) {
            logger.error("系统初始化，读取系统配置信息出现异常：", e);
            throw e;
        }
    }

    public static boolean setBrandDataSource(String brandCode){
        SystemConfigDTO dto = getSystemConfig(brandCode);
        if(null==dto){
            logger.error("setBrandDataSource未查找到对应的品牌的配置信息:"+brandCode);
            return false;
        }
        CustomerContextHolder.setCustomerDataSourceType(dto.getDataSourceName());
        return true;
    }

    public static void clearBrandDataSource(){
        CustomerContextHolder.clearCustomerDataSourceType();
    }

    public static SystemConfigDTO getSystemConfig(String brandCode){
        for (SystemConfigDTO dto : brandList) {
            if(dto.getBrandCode().equals(brandCode)) {
                return dto;
            }
        }
        logger.error("未查找到对应的品牌的配置信息:"+brandCode);
        return null;
    }

    public static int getOrganizationInfoID(String brandCode) throws Exception{
        SystemConfigDTO dto = getSystemConfig(brandCode);
        if(null==dto){
            throw new Exception("未查找到对应的品牌的配置信息:"+brandCode);
        }
        return dto.getOrganizationInfoID();
    }

    public static String getOrgCode(String brandCode) throws Exception{
        SystemConfigDTO dto = getSystemConfig(brandCode);
        if(null==dto){
            throw new Exception("未查找到对应的品牌的配置信息:"+brandCode);
        }
        return dto.getOrgCode();
    }

    public static String getAesKey(String brandCode) {
        SystemConfigDTO dto = getSystemConfig(brandCode);
        return dto.getAesKey();
    }

    public static int getBrandInfoID(String brandCode) throws Exception{
        SystemConfigDTO dto = getSystemConfig(brandCode);
        if(null==dto){
            throw new Exception("未查找到对应的品牌的配置信息:"+brandCode);
        }
        return dto.getBrandInfoID();
    }

    public static SystemConfigDTO getSystemConfigByDuibaAppkey(String duibaAppkey){
        for (SystemConfigDTO dto : brandList) {
            if(duibaAppkey.equals(dto.getDuibaAppKey())) {
                return dto;
            }
        }
        logger.error("未查找到对应的品牌的配置信息duibaAppkey:"+duibaAppkey);
        return null;
    }
}
