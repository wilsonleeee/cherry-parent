package com.webconsole.action;

import com.cherry.cm.core.*;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.webconsole.bl.CompareDataBL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用于比对新老后台销售数据的Action
 * Created by abc on 2017/2/22.
 */
public class CompareDataAction extends BaseAction{

    protected static final Logger logger = LoggerFactory.getLogger(CompareDataAction.class);

    /** 比对新老后台销售数据 */
    @Resource
    private CompareDataBL compareDataBL;

    /**
     * 比对新老后台销售数据
     */
    public void compareData() {
        //封装查询数据Map
        Map<String, Object> map = new HashMap<String, Object>();
        //返回Map封装数据
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //从request域获取品牌brandCode
        String brandCode = request.getParameter(CherryBatchConstants.BRAND_CODE);
        try {
            //判断是否传入brandCode参数
            if(null == brandCode) {
                resultMap.put("brandCode", "Please input brandCode!");
                logger.error("No brandCode found!");
                //封装成JSON格式返回
                ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultMap));
                return;
            }

            // 根据brandCode设置数据源
            SystemConfigDTO systemConfigDTO = SystemConfigManager.getSystemConfig(brandCode);
            //判断是否正确取得brandCode的数据源
            if (null == systemConfigDTO) {
                resultMap.put("brandCode", "Please input the correct brandCode!");
                logger.error("Wrong brandCode!");
                //封装成JSON格式返回
                ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultMap));
                return;
            }

            // 将获取的数据源名设定到线程本地变量contextHolder中（新后台品牌数据库）
            CustomerContextHolder.setCustomerDataSourceType(systemConfigDTO.getDataSourceName());
            //将brandCode传入封装查询Map
            map.put(CherryBatchConstants.BRAND_CODE, brandCode);
            //查询取得封装了异常数据的List
            List<Map<String, Object>> diffDetailList = compareDataBL.getDiffDataList(map);
            //存放品牌brandCode
            resultMap.put("brandCode", brandCode);
            if (diffDetailList != null && !diffDetailList.isEmpty()) {
                //存放数据不一致的销售月份
                List<String> errorSaleYearMonths = new ArrayList<String>();
                for (Map diffDetailMap :  diffDetailList) {
                    String saleYearMonth = (String) diffDetailMap.get("saleYearMonth");
                    errorSaleYearMonths.add(saleYearMonth);
                }
                //存放不一致月份
                resultMap.put("errorSaleYearMonths", errorSaleYearMonths);
                //存放对比结果(不一致)
                resultMap.put("result", "Data Exception");
            } else {
                //存放对比结果(一致)
                resultMap.put("result", "Data OK");
            }
            //封装成JSON格式返回
            ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(resultMap));
        } catch (Exception ex) {
            logger.error("新老后台数据对比时出错：",ex);
        }
    }

}
