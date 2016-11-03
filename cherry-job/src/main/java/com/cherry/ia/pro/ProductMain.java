package com.cherry.ia.pro;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.WebConfigListener;
import com.cherry.ia.pro.bl.BINBEIFPRO01_BL;

public class ProductMain {
	public static void main(String[] args) throws Exception {
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			ApplicationContext ac = new ClassPathXmlApplicationContext(
					new String[] {
							"classpath:/spring-conf/applicationContext.xml",
							"classpath:/spring-conf/beans-define/beans-cm.xml",
							"classpath:/spring-conf/beans-define/beans-if.xml" });
			WebConfigListener webConfigListener = new WebConfigListener();
			// 加载资源文件
			webConfigListener.contextInitialized(null);
			BINBEIFPRO01_BL binbeifpro01BL = (BINBEIFPRO01_BL) ac
					.getBean("binbeifpro01_BL");
			
			CustomerContextHolder.setCustomerDataSourceType("YAYANG");
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("organizationInfoId", 1);
			map.put("brandInfoId", 1);
			map.put("brandCode", "avene");
			flg = binbeifpro01BL.tran_batchProducts(map);
		} catch (Throwable e) {
			e.printStackTrace();
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			// 清除新后台品牌数据源
			CustomerContextHolder.clearCustomerDataSourceType();
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
