package com.cherry.webservice.member.bl;

import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.webservice.member.interfaces.MemberDispatchBill_IF;
import com.cherry.webservice.member.service.MemberDispatchBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取会员发货单查询
 * Created by jasonliu on 2017/1/12.
 */
public class MemberDispatchBillLogic implements MemberDispatchBill_IF {
    private static final Logger logger = LoggerFactory.getLogger(MemberDispatchBillLogic.class);

    @Resource(name = "memberDispatchBillService")
    private MemberDispatchBillService memberDispatchBillService ;

    @Resource
    private CodeTable codeTable;

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
            String ordNo = ConvertUtil.getString(map.get("OrdNo"));
            paramMap.put("tradeNoIF", ordNo);

            String brandId = ConvertUtil.getString(map.get("BIN_BrandInfoID"));
            paramMap.put("brandInfoId", brandId);

            Map<String, Object> resultContent = memberDispatchBillService.getMemberDispatchBill(paramMap);
            //判断是否存在会员等级信息
            if (null == resultContent) {
                resultMap.put("ERRORCODE", "EWC10002");
                resultMap.put("ERRORMSG", "该单据没有发货或状态异常。");
                return resultMap;
            }

            String expressName = "";
            if(!StringUtils.isEmpty(resultContent.get("ExpressID"))) {
                String expressCode = String.valueOf(resultContent.get("ExpressID"));
                expressName = codeTable.getValueByKey("1421",expressCode,ConvertUtil.getString(map.get("OrgCode")),ConvertUtil.getString(map.get("BrandCode")));
            }
            resultContent.put("ExpressName", expressName);
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
        if (ConvertUtil.isBlank(ConvertUtil.getString(map.get("OrdNo")))){
            resultMap.put("ERRORCODE", "EWC10001");
            resultMap.put("ERRORMSG", "预约单号不能为空");
            return resultMap;
        }
        return resultMap;
    }
}
