package com.cherry.webserviceout.alicloud.jstTrade.function;

import com.cherry.webserviceout.alicloud.jstTrade.bl.BINBAT123_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by hubing on 2016/11/15.
 */
public class BINBAT123_FN implements FunctionProvider {
    private static Logger logger = LoggerFactory.getLogger(BINBAT123_FN.class.getName());

    @Resource(name="binbat123_BL")
    private BINBAT123_BL binbat123_BL;

    @Override
    public void execute(Map transientVars, Map args, PropertySet ps)
            throws WorkflowException {
        try {
            logger.info("******************************天猫退款订单导入Batch处理开始***************************");
            int flag = binbat123_BL.tran_getRefund(transientVars);
            ps.setInt("result", flag);
        } catch (Exception e) {
            // 打印错误信息
            logger.error(e.getMessage(),e);
            throw new WorkflowException();
        } finally {
            logger.info("******************************天猫退款订单导入Batch处理结束***************************");
        }
    }
}
