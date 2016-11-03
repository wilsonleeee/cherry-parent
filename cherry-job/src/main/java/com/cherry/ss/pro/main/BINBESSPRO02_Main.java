package com.cherry.ss.pro.main;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.WebConfigListener;
import com.cherry.ss.pro.bl.BINBESSPRO02_BL;

public class BINBESSPRO02_Main {
	
	public static void main(String[] args) throws Exception {
		
		// 组织ID
		String orgId = "1";
		// 品牌ID
		String brandId = "1";
		// 品牌code
		String brandCode = "AVENE";
		// 新后台数据源
		String customerDataSourceType = "CherryBrand_BK";
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			ApplicationContext ac = new ClassPathXmlApplicationContext(
					new String[] {
							"classpath:/spring-conf/applicationContext.xml",
							"classpath:/spring-conf/beans-define/beans-cm.xml",
							"classpath:/spring-conf/beans-define/beans-ss.xml" });
			WebConfigListener webConfigListener = new WebConfigListener();
			// 加载资源文件
			webConfigListener.contextInitialized(null);
			BINBESSPRO02_BL binbesspro02_BL = (BINBESSPRO02_BL) ac
					.getBean("binbesspro02_BL");
			Map<String, Object> map = new HashMap<String, Object>();
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandId);
			// 组织Id
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, orgId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			CustomerContextHolder.setCustomerDataSourceType(customerDataSourceType);
			flg = binbesspro02_BL.tran_batchHistory(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
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
