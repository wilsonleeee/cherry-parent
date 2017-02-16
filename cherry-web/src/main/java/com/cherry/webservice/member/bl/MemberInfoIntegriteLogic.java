package com.cherry.webservice.member.bl;

import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.member.interfaces.MemberDispatchBill_IF;
import com.cherry.webservice.member.service.MemberInfoIntegriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员信息完整度查询
 * Created by jasonliu on 2017/2/8.
 */
public class MemberInfoIntegriteLogic implements MemberDispatchBill_IF {

    @Resource
    private MemberInfoIntegriteService memberInfoIntegriteService;

    private static final Logger logger = LoggerFactory.getLogger(MemberInfoIntegriteLogic.class);

    @Override
    public Map tran_execute(Map map) throws Exception {
        // 返回值
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            resultMap = validateParam(map);
            // 校验不通过时直接返回
            if (!CollectionUtils.isEmpty(resultMap)) {
                return resultMap;
            }

            // 通过memCode或者messageId获取会员BIN_MemberInfoID
            String memCode = ConvertUtil.getString(map.get("MemCode"));
            String messageId = ConvertUtil.getString(map.get("MessageId"));
            Map<String, Object> returnVal = null ;
            if(StringUtils.isEmpty(memCode) && !StringUtils.isEmpty(messageId)) {
                Map<String,Object> param = new HashMap<String, Object>();
                param.put("messageId",messageId);
                returnVal = memberInfoIntegriteService.getMemberInfoByCodeOrMessageId(param);
            }

            if(!StringUtils.isEmpty(memCode)) {
                Map<String,Object> param = new HashMap<String, Object>();
                param.put("memCode",memCode);
                returnVal = memberInfoIntegriteService.getMemberInfoByCodeOrMessageId(param);
            }

            if(CollectionUtils.isEmpty(returnVal)) {
                resultMap.put("ERRORCODE", "EMB22002");
                resultMap.put("ERRORMSG", "没有找到此会员相关信息。");
                return resultMap;
            }

            // 根据会员id查询会员完善度
            String memberInfoID = ConvertUtil.getString(returnVal.get("memberInfoID"));
            Map<String,Object> param = new HashMap<String, Object>();
            param.put("memberInfoID",memberInfoID);
            Map<String, Object> resultVal = memberInfoIntegriteService.getMemberInfoIntegrite(param);
            if(resultVal == null) {
                resultMap.put("ERRORCODE", "EMB22003");
                resultMap.put("ERRORMSG", "没有配置对应的会员信息完整度规则。");
                return resultMap;
            }

            // Percent为关键字
            Map<String, Object> resultContent = new HashMap<String, Object>();
            resultContent.put("Percent",resultVal.get("totalPercent"));
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

        // 判断必填字段是否为空
        if (ConvertUtil.isBlank(ConvertUtil.getString(map.get("MemCode"))) && ConvertUtil.isBlank(ConvertUtil.getString(map.get("MessageId")))) {
            resultMap.put("ERRORCODE", "EMB22001");
            resultMap.put("ERRORMSG", "MemCode参数或者MessageId参数缺失或格式错误 ");
            return resultMap;
        }

        return resultMap;
    }
}
