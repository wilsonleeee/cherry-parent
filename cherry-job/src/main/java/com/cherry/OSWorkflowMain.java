package com.cherry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cherry.cm.cmbussiness.bl.BINOLCM06_BL;
import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.CustomerSmsContextHolder;
import com.cherry.cm.core.CustomerTpifContextHolder;
import com.cherry.cm.core.CustomerWitContextHolder;
import com.cherry.cm.core.WebConfigListener;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.config.SpringConfiguration;

public class OSWorkflowMain {
	
	private static Logger logger = LoggerFactory
	.getLogger(OSWorkflowMain.class.getName());
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			ApplicationContext ac = new ClassPathXmlApplicationContext(
					new String[] {
							"classpath:/spring-conf/applicationContext.xml",
							"classpath:/spring-conf/beans-define/beans-*.xml" });
			WebConfigListener webConfigListener = new WebConfigListener();
			// 加载资源文件
			webConfigListener.contextInitialized(null);
			SpringConfiguration conf = (SpringConfiguration) ac
					.getBean("osworkflowConfiguration");
			Workflow wf = (Workflow) ac.getBean("workflow");
			wf.setConfiguration(conf);
			try {
				BINOLCM06_BL binOLCM06_BL = (BINOLCM06_BL) ac.getBean("binOLCM06_BL");
				Map<String, Object> map = new HashMap<String, Object>();
				List<Map<String, Object>> confBrandList = binOLCM06_BL.getConfBrandInfoList(map);
//				// test start
//				List<Map<String, Object>> brandList = new ArrayList<Map<String, Object>>();
//				Map<String, Object> map2 = new HashMap<String, Object>();
//				map2.put("organizationInfoId", 32);
//				map2.put("brandInfoId", 8);
//				map2.put("brandCode", "AVENE");
//				map2.put("dataSourceName", "YAYANG");
//				map2.put("witDataSourceName", "YAYANG");
//				brandList.add(map2);
				// test end
				long wf_id = -1;
				// 品牌信息
				if (null != confBrandList) {
					for (Map<String, Object> brandInfo : confBrandList) {
						try {
							// 新后台品牌数据源
							String dataSource = (String) brandInfo.get("dataSourceName");
							// 老后台品牌数据源
							String witDataSource = (String) brandInfo.get("oldDataSourceName");
							// 第三方接口数据源
							String ifDataSourceName = (String) brandInfo.get("ifDataSourceName");
							CustomerContextHolder.setCustomerDataSourceType(dataSource);
							CustomerWitContextHolder.setCustomerWitDataSourceType(witDataSource);
							CustomerTpifContextHolder.setCustomerTpifDataSourceType(ifDataSourceName);
							CustomerSmsContextHolder.setCustomerSmsDataSourceType( (String) brandInfo.get("SMSDataSourceName"));
							Map<String, Object> brandMap = new HashMap<String, Object>();
							// 组织Code
							brandMap.put("orgCode", brandInfo.get("orgCode"));
							// 品牌code
							brandMap.put("brandCode", brandInfo.get("brandCode"));
							Map<String, Object> osbrandInfo = binOLCM06_BL.getOSBrandInfo(brandMap);
							if (null == osbrandInfo) {
								BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
								batchLoggerDTO.setCode("EJR00004");
								batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
//								CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(
//										OSWorkflowMain.getClass());
//								cherryBatchLogger.BatchLogger(batchLoggerDTO);
								continue;
							}
							brandInfo.putAll(osbrandInfo);
							wf_id = wf.initialize("CherryBatchFlow", 1000, null);
							// 当前可执行的动作
							int[] actionIdArr = wf.getAvailableActions(wf_id, null);
							while (null != actionIdArr && actionIdArr.length > 0) {
								for (int actionId : actionIdArr) {
									// 执行动作
									wf.doAction(wf_id, actionId, brandInfo);
								}
								actionIdArr = wf.getAvailableActions(wf_id, null);
							}
						} catch (Exception e) {
							logger.error(e.getMessage(),e);
						} finally {
							// 清除新后台品牌数据源
							CustomerContextHolder.clearCustomerDataSourceType();
							// 清除老后台品牌数据源
							CustomerWitContextHolder.clearCustomerWitDataSourceType();
							// 清除第三方接口数据源
							CustomerTpifContextHolder.clearCustomerTpifDataSourceType();
							// 清除短信接口数据源
							CustomerSmsContextHolder.clearCustomerSmsDataSourceType();
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				throw e;
			}
		}catch (Exception e) {
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
