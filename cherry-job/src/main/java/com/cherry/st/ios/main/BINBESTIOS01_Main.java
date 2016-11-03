package com.cherry.st.ios.main;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CustomerWitContextHolder;
import com.cherry.cm.core.WebConfigListener;
import com.cherry.st.ios.bl.BINBESTIOS01_BL;

/*  
 * @(#)BINBESTIOS01_Main.java    1.0 2012-2-22     
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
/**
 *金蝶接口发货单/退库单导入main方法 
 *
 *@author zhanggl
 * 
 * */
public class BINBESTIOS01_Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		//组织ID
		String orgId = "1";
		//组织code
		String orgCode = "MGP";
		//品牌ID
		String brandInfoId = "2";
		//品牌code
		String brandCode = "MGP";
		// 新后台数据源
		String customerDataSourceType = "OldBrandDB";
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			ApplicationContext ac = new ClassPathXmlApplicationContext(
					new String[] {
							"classpath:/spring-conf/applicationContext.xml",
							"classpath:/spring-conf/beans-define/beans-cm.xml",
							"classpath:/spring-conf/beans-define/beans-ss.xml",
							"classpath:/spring-conf/beans-define/beans-st.xml",
							"classpath:/spring-conf/beans-define/beans-mq.xml",
							"classpath:/spring-conf/beans-define/beans-mh.xml"});
			WebConfigListener webConfigListener = new WebConfigListener();
			// 加载资源文件
			webConfigListener.contextInitialized(null);
			BINBESTIOS01_BL binBESTIOS01_BL = (BINBESTIOS01_BL) ac
					.getBean("binBESTIOS01_BL");
			Map<String, Object> map = new HashMap<String, Object>();
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 组织Id
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, orgId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			CustomerWitContextHolder.setCustomerWitDataSourceType(customerDataSourceType);
			flg = binBESTIOS01_BL.tran_importInvoiceData(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			e.printStackTrace();
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				System.exit(0);
			}
			if (flg == CherryBatchConstants.BATCH_WARNING) {
				System.exit(1);
			}
			if (flg == CherryBatchConstants.BATCH_ERROR) {
				System.exit(-1);
			}
		}
		
		
	}

}
