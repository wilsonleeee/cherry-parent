package com.cherry.cm.core;

import com.cherry.cm.util.CherryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统的一些全局配置放置在这里，以减少SQL查询，提高效率
 * （1）每个发布的系统内的品牌的基本信息（组织代号，品牌代号，数据源等等）
 * （2）全局的系统配置
 * （3）
 * Created by dingyongchang on 2016/12/15.
 */
public class SystemConfigManager implements InitializingBean {
    protected static final Logger logger = LoggerFactory.getLogger(SystemConfigManager.class);

    private static List<BrandInfoDTO> brandList = new ArrayList<BrandInfoDTO>();

    private static ConcurrentHashMap<String,WebserviceConfigDTO> webserviceConfigAll = new ConcurrentHashMap<String,WebserviceConfigDTO>();
    @Resource
    private BaseConfServiceImpl baseConfServiceImpl;

    @Resource
    protected BaseServiceImpl baseServiceImpl;

    public  void afterPropertiesSet() throws Exception {
        try {
            //获取各品牌基本信息
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("ibatis_sql_id", "SystemInitialize.getBrandDataSourceConfigList");
            List<BrandInfoDTO> list = baseConfServiceImpl.getList(paramMap);
            if(null!=list) {
                for (BrandInfoDTO dto : list) {
                    try {
                        //到各个品牌数据库中去查询品牌的信息,补全两个ID
                        CustomerContextHolder.setCustomerDataSourceType(dto.getDataSourceName());
                        BrandInfoDTO tmp = (BrandInfoDTO) baseServiceImpl.get(dto, "SystemInitialize.getBrandInfo");
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

            //获取webservice配置信息
            paramMap.put("ibatis_sql_id", "SystemInitialize.getWebserviceConfigList");
            List<WebserviceConfigDTO> wslist = baseConfServiceImpl.getList(paramMap);
            if(null!=wslist) {
                for (WebserviceConfigDTO dto : wslist) {
                    if(!CherryUtil.isEmpty(dto.getExtensionConfig())){
                        try {
                            dto.setExtensionConfigMap(CherryUtil.json2Map(dto.getExtensionConfig()));
                        }catch (Exception ex){
                            logger.error("系统初始化，将webservice配置信息从JSON转换成map时发生异常："+dto.getExtensionConfig(), ex);
                        }
                    }
                    webserviceConfigAll.put(dto.getBrandCode()+"_"+dto.getWebserviceIdentifier(),dto);
                }
            }

        } catch (Exception e) {
            logger.error("系统初始化，读取系统配置信息出现异常：", e);
            throw e;
        }
    }

    public static boolean setBrandDataSource(String brandCode){
        BrandInfoDTO dto = getBrandInfo(brandCode);
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

    public static BrandInfoDTO getBrandInfo(String brandCode){
        for (BrandInfoDTO dto : brandList) {
            if(dto.getBrandCode().equals(brandCode)) {
                return dto;
            }
        }
        logger.error("未查找到对应的品牌的配置信息:"+brandCode);
        return null;
    }

    public static int getOrganizationInfoID(String brandCode) throws Exception{
        BrandInfoDTO dto = getBrandInfo(brandCode);
        if(null==dto){
            throw new Exception("未查找到对应的品牌的配置信息:"+brandCode);
        }
        return dto.getOrganizationInfoID();
    }

    public static String getOrgCode(String brandCode) throws Exception{
        BrandInfoDTO dto = getBrandInfo(brandCode);
        if(null==dto){
            throw new Exception("未查找到对应的品牌的配置信息:"+brandCode);
        }
        return dto.getOrgCode();
    }

    public static String getAesKey(String brandCode) {
        BrandInfoDTO dto = getBrandInfo(brandCode);
        return dto.getAesKey();
    }

    public static int getBrandInfoID(String brandCode) throws Exception{
        BrandInfoDTO dto = getBrandInfo(brandCode);
        if(null==dto){
            throw new Exception("未查找到对应的品牌的配置信息:"+brandCode);
        }
        return dto.getBrandInfoID();
    }

    public static BrandInfoDTO getBrandInfoByDuibaAppkey(String duibaAppkey){
        for (BrandInfoDTO dto : brandList) {
            if(duibaAppkey.equals(dto.getDuibaAppKey())) {
                return dto;
            }
        }
        logger.error("未查找到对应的品牌的配置信息duibaAppkey:"+duibaAppkey);
        return null;
    }

    public static WebserviceConfigDTO getWebserviceConfigDTO(String webserviceIdentifier){
        return webserviceConfigAll.get("-9999_"+webserviceIdentifier);
    }

    public static WebserviceConfigDTO getWebserviceConfigDTO(String brandCode,String webserviceIdentifier){
        if(webserviceConfigAll.containsKey(brandCode+"_"+webserviceIdentifier)){
            return webserviceConfigAll.get(brandCode+"_"+webserviceIdentifier);
        }
        return webserviceConfigAll.get("-9999_"+webserviceIdentifier);
    }
}
