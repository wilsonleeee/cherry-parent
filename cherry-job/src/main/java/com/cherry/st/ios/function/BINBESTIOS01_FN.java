package com.cherry.st.ios.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.st.ios.bl.BINBESTIOS01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/*  
 * @(#)BINBESTIOS01_BL.java    1.0 2012-2-24     
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
public class BINBESTIOS01_FN implements FunctionProvider {

	@Resource
	private BINBESTIOS01_BL binBESTIOS01_BL;
	
	private static Logger logger = LoggerFactory.getLogger(BINBESTIOS01_FN.class.getName());
	
	@Override
	public void execute(Map arg0, Map arg1, PropertySet arg2)
			throws WorkflowException {
		 try {
	            logger.info("******************************金蝶K3接口发货单/退库单数据导入BATCH处理开始***************************");
	            int flg = binBESTIOS01_BL.tran_importInvoiceData(arg0);
	            arg2.setInt("result", flg);
	        } catch (Exception e) {
	            // 打印错误信息
	            logger.error(e.getMessage(),e);
	            throw new WorkflowException();
	        } finally {
	            logger.info("******************************金蝶K3接口发货单/退库单数据导入BATCH处理结束***************************");
	        }

	}

}
