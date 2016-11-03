package com.cherry.mb.vis.main;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.CustomerWitContextHolder;
import com.cherry.cm.core.WebConfigListener;
import com.cherry.mb.vis.bl.BINBEMBVIS01_BL;

public class BINBEMBVIS01Main {

	
	public static void main(String[] args) throws Exception {
		
		// 组织ID
		String orgId = "1";
		// 品牌ID
		String brandId = "1";
		// 品牌code
		String brandCode = "mgp";
		// 新后台数据源
		String customerDataSourceType = "CherryBrandDB";
		// 老后台数据源
		String customerWitDataSourceType = "OldBrandDB";
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			ApplicationContext ac = new ClassPathXmlApplicationContext(
					new String[] {
							"classpath:/spring-conf/applicationContext.xml",
							"classpath:/spring-conf/beans-define/beans-cm.xml",
							"classpath:/spring-conf/beans-define/beans-st.xml",
							"classpath:/spring-conf/beans-define/beans-ss.xml",
							"classpath:/spring-conf/beans-define/beans-mb.xml" });
			WebConfigListener webConfigListener = new WebConfigListener();
			// 加载资源文件
			webConfigListener.contextInitialized(null);
			BINBEMBVIS01_BL binBEMBVIS01_BL = (BINBEMBVIS01_BL) ac
					.getBean("binBEMBVIS01_BL");
			Map<String, Object> map = new HashMap<String, Object>();
			// 品牌Id
			map.put("brandInfoID", brandId);
			// 组织Id
			map.put("organizationInfoID", orgId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			CustomerContextHolder.setCustomerDataSourceType(customerDataSourceType);
			CustomerWitContextHolder.setCustomerWitDataSourceType(customerWitDataSourceType);
			flg = binBEMBVIS01_BL.tran_BatchMemVist(map);
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
