package com.cherry.webservice.member.bl;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.common.IWebservice;
import com.cherry.webservice.member.service.MemberLevelsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员获取所有等级信息
 * Created by jasonliu on 2017/1/11.
 */
public class MemberLevelsLogic implements IWebservice {

    private static final Logger logger = LoggerFactory.getLogger(MemberLevelsLogic.class);

    @Resource(name = "memberLevelsService")
    private MemberLevelsService memberLevelsService ;

    @Override
    public Map tran_execute(Map map) throws Exception {
        // 返回值
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            resultMap = validateParam(map);
            // 校验不通过时直接返回
            if (null != resultMap && !resultMap.isEmpty()) {
                return resultMap;
            }

            Map<String,Object> paramMap = new HashMap<String, Object>();
            String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
            paramMap.put("brandInfoId", brandId);

            List<Map<String, Object>> memberLevels= memberLevelsService.getMemberLevelList(paramMap);
            //判断是否存在会员等级信息
            if (null == memberLevels || memberLevels.isEmpty()) {
                resultMap.put("ERRORCODE", "WSE9992");
                resultMap.put("ERRORMSG", "未能查到匹配的会员等级信息。");
                return resultMap;
            }

            Map<String,Object> resultContent = new HashMap<String, Object>();
            resultContent.put("LevelDetailList", memberLevels);
            resultMap.put("ResultContent", resultContent);

        } catch (Exception e) {
            logger.error("WebService发生未知异常。");
            logger.error(e.getMessage(),e);
            resultMap.put("ERRORCODE", "WSE9999");
            resultMap.put("ERRORMSG", "WebService处理过程中发生未知异常");
        }

        return resultMap;
    }

    /**
     * 参数校验
     * @param map
     * @return
     */
    private Map<String,Object> validateParam(Map map) {
        // 返回值
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 判断必填字段是否为空
        if (ConvertUtil.isBlank(ConvertUtil.getString(map.get("TradeType")))) {
            resultMap.put("ERRORCODE", "WSE9994");
            resultMap.put("ERRORMSG", "TradeType不能为空");
            return resultMap;
        }
        if (ConvertUtil.isBlank(ConvertUtil.getString(map.get("BrandCode")))){
            resultMap.put("ERRORCODE", "WSE9998");
            resultMap.put("ERRORMSG", "品牌代码不能为空");
            return resultMap;
        }
        return resultMap;
    }
}
