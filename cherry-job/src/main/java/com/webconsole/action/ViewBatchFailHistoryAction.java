package com.webconsole.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM06_BL;
import com.cherry.cm.core.*;
import com.webconsole.bl.ViewBatchHistoryLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.cherry.cm.batcmbussiness.interfaces.BINBECM01_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;

/**
 *
 */
public class ViewBatchFailHistoryAction extends BaseAction{

	protected static final Logger logger = LoggerFactory.getLogger(ViewBatchFailHistoryAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** JOB执行相关共通 IF */
	@Resource(name="viewBatchHistoryLogic")
	private ViewBatchHistoryLogic viewBatchHistoryLogic;
	@Resource
	private BINOLCM06_BL binOLCM06_BL;

	public void getJobFailureRunHistory() throws Exception{
		Map retMap  = new HashMap<String, Object>();
		List<Map<String,Object>> retMapList = null;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> dataSourceMap = binOLCM06_BL.getConfBrandInfoList(map);
			for(Map brandInfo : dataSourceMap) {
				// 对登录账户做一系列的检查,通过则返回账户ID，不通过则会抛出多种错误信息
				CustomerContextHolder.setCustomerDataSourceType(ConvertUtil.getString(brandInfo.get("dataSourceName")));
				Map<String, Object> osbrandInfo = null;
				// 如果发生DB连接异常，将尝试重新连接
				for (int i = 0; i < CherryBatchConstants.DB_RECONN_TIMES + 1; i++) {
					try {
						osbrandInfo = binOLCM06_BL.getOSBrandInfo(brandInfo);
						break;
					} catch (Exception ec) {
						if (i == CherryBatchConstants.DB_RECONN_TIMES) {
							//throw ec;
						}
						// 重连数据库
						BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
						batchLoggerDTO.setCode("ECM00003");
						batchLoggerDTO.addParam(String.valueOf(i + 1));
						batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
						CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
						cherryBatchLogger.BatchLogger(batchLoggerDTO);
						// 等待一秒
						long millis = 1000L;
						// 等待间隔时间
						Thread.sleep(millis);
					}
				}
				if (null == osbrandInfo) {
					BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
					batchLoggerDTO.setCode("EOS00001");
					batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
					CherryBatchLogger cherryBatchLogger = new CherryBatchLogger(this.getClass());
					cherryBatchLogger.BatchLogger(batchLoggerDTO);
					// 设置错误信息到返回结果
					retMap.put(brandInfo.get("BrandName"),"取得数据源失败！");
					// 清除新后台品牌数据源
					CustomerContextHolder.clearCustomerDataSourceType();
					continue;
				}
				// 数据源存在的场合，取得Batch执行失败履历
				retMapList=viewBatchHistoryLogic.getJobFailureRunHistory(osbrandInfo);
				if (retMapList != null && !retMapList.isEmpty()) {
					// 设置取得失败信息到返回结果
					retMap.put(brandInfo.get("BrandName"),retMapList);
				}
				// 清除新后台品牌数据源
				CustomerContextHolder.clearCustomerDataSourceType();
			}

			// 没有失败信息的场合，返回成功信息
			if (retMap == null || retMap.isEmpty()) {
				ConvertUtil.setResponseByAjax(response, "所有Batch正常执行成功！");
			} else {
				// 存在失败信息的场合，返回失败信息
				ConvertUtil.setResponseByAjax(response, JSONUtil.serialize(retMap));
			}
		}catch(Exception ex){
			logger.error("读取Batch失败履历操作出错：",ex);
		}finally{
		}		
	}


}
