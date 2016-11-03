package com.cherry.webservice.product.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ia.pro.bl.BINBEIFPRO04_BL;
import com.cherry.webservice.common.IWebservice;

/**
 * 产品实时下发
 * 
 * @author lzs 下午2:40:15
 */
public class ProductLogic implements IWebservice {
	private static final Logger logger = LoggerFactory.getLogger(ProductLogic.class);

	@Resource(name = "binbeifpro04_BL")
	private BINBEIFPRO04_BL binbeifpro04_BL;

	@Override
	public Map tran_execute(Map map) throws Exception {
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 发送MQ
		map.put("IsSendMQ", true);

		// 运行方式【手动】
		map.put("RunType", "MT");
		try {
			logger.info("*******************产品实时Batch下发处理开始***********************");
			resMap = binbeifpro04_BL.tran_batchProducts(map);
			
			//执行程序后的Flag
			int flag = ConvertUtil.getInt(resMap.get("flag"));
			if (flag == CherryBatchConstants.BATCH_WARNING || flag == CherryBatchConstants.BATCH_ERROR) {
				resMap.put("ERRORCODE", -1);
				return resMap;
			}
		} catch (CherryBatchException cbe) {
			logger.error("=============ERROR MSG===============");
			logger.error(cbe.getMessage(),cbe);
			logger.error("=====================================");
			
			resMap.put("ERRORCODE", -1);
			resMap.put("ERRORMSG", cbe.getMessage());
			return resMap;
		} catch (CherryException cx) {
			logger.error("=============ERROR MSG===============");
			logger.error(cx.getMessage(),cx);
			logger.error("=====================================");
			
			resMap.put("ERRORCODE", -1);
			resMap.put("ERRORMSG", cx.getMessage());
			return resMap;
		} catch (Exception e) {
			logger.error("=============ERROR MSG===============");
			logger.error(e.getMessage(),e);
			logger.error("=====================================");
			
			resMap.put("ERRORCODE", -1);
			resMap.put("ERRORMSG", e.getMessage());
			return resMap;
		} catch(Throwable t){
			logger.error("=============ERROR MSG===============");
			logger.error(t.getMessage(),t);
			logger.error("=====================================");
			
			resMap.put("ERRORCODE", -1);
			resMap.put("ERRORMSG", t.getMessage());
			return resMap;
		}
		logger.info("*******************产品实时Batch下发处理结束***********************");
		return resMap;
	}
}
