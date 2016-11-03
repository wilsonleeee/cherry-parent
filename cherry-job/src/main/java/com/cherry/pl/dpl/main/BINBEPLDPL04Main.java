package com.cherry.pl.dpl.main;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.WebConfigListener;
import com.cherry.pl.dpl.bl.BINBEPLDPL03_BL;

public class BINBEPLDPL04Main {
	
	public static void main(String[] args) throws Exception {
		
//		// 组织ID
//		String orgId = "1";
//		// 品牌ID
//		String brandId = "3";
//		// 品牌code
//		String brandCode = "mgp";
//		// 新后台数据源
//		String customerDataSourceType = "CherryBrandDB";	
//		// 设置batch处理标志
//		int flg = CherryBatchConstants.BATCH_SUCCESS;
//		try {
//			ApplicationContext ac = new ClassPathXmlApplicationContext(
//					new String[] {
//							"classpath:/spring-conf/applicationContext.xml",
//							"classpath:/spring-conf/beans-define/beans-*.xml" });
//			WebConfigListener webConfigListener = new WebConfigListener();
//			// 加载资源文件
//			webConfigListener.contextInitialized(null);
//			BINBEPLDPL03_BL binBEPLDPL03_BL = (BINBEPLDPL03_BL) ac
//					.getBean("binBEPLDPL03_BL");
//			Map<String, Object> map = new HashMap<String, Object>();
//			// 品牌Id
//			map.put(CherryBatchConstants.BRANDINFOID, brandId);
//			// 组织Id
//			map.put(CherryBatchConstants.ORGANIZATIONINFOID, orgId);
//			// 品牌code
//			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
//			CustomerContextHolder.setCustomerDataSourceType(customerDataSourceType);
//			flg = binBEPLDPL03_BL.tran_createDepartPrivilegeIndex(map);
//		} catch (CherryBatchException cbx) {
//			flg = CherryBatchConstants.BATCH_WARNING;
//		} catch (Exception e) {
//			e.printStackTrace();
//			flg = CherryBatchConstants.BATCH_ERROR;
//		} finally {
//			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
//				System.exit(0);
//			}
//			if (flg == CherryBatchConstants.BATCH_WARNING) {
//				System.exit(1);
//			}
//			if (flg == CherryBatchConstants.BATCH_ERROR) {
//				System.exit(-1);
//			}
//		}
	}

}
