package com.cherry.webservice.product.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.util.CherryBatchUtil;
import com.cherry.ia.pro.bl.BINBEIFPRO03_BL;
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

	@Resource
	private BINBEIFPRO03_BL binbeifpro03BL;

	@Resource(name = "binbeifpro04_BL")
	private BINBEIFPRO04_BL binbeifpro04_BL;

	@Override
	public Map tran_execute(Map map) throws Exception {
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;

		// 发送MQ
		map.put("IsSendMQ", true);

		// 运行方式【手动】
		map.put("RunType", "MT");
		try {
			logger.info("*******************产品实时Batch下发处理开始***********************");
			resMap = binbeifpro04_BL.tran_batchProducts(map);
			// 发送MQ
			String isSendMQ = ConvertUtil.getString(map.get("IsSendMQ"));
			flg = ConvertUtil.getInt(resMap.get("flag"));

			if (flg == CherryBatchConstants.BATCH_SUCCESS && !CherryBatchUtil.isBlankString(isSendMQ)) {
				// 备份产品下发数据/MQ下发
				Map<String, Object> flagMapMQ = binbeifpro04_BL.tran_batchProductsMQSend(map);
				resMap.putAll(flagMapMQ);

				logger.info("*******************柜台产品Batch下发处理开始***********************");
				flg = binbeifpro03BL.tran_batchCouProducts(map);
				logger.info("-------柜台产品Batch下发处理:flg="+flg);
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					// 备份产品下发数据/MQ下发
					flagMapMQ = binbeifpro03BL.tran_batchCntProductsMQSend(map);
					resMap.putAll(flagMapMQ);
					flg=ConvertUtil.getInt(resMap.get("flag"));
					// 柜台产品下发失败的场合，返回错误代码
					if (flg == CherryBatchConstants.BATCH_WARNING || flg == CherryBatchConstants.BATCH_ERROR) {
						resMap.put("ERRORCODE", -1);
					}
				} else {
					resMap.put("ERRORCODE", -1);
				}
			} else {
				resMap.put("ERRORCODE", -1);
			}

			binbeifpro03BL.outMessage();
			binbeifpro03BL.tran_programEnd(map);

			binbeifpro04_BL.outMessage();
			binbeifpro04_BL.tran_programEnd(map);
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
