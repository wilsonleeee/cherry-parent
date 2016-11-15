package com.cherry.cp.act.action;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cp.act.bl.BINCPMEACT05_BL;
import com.cherry.ia.pro.action.BINBEIFPRO02_Action;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员等级变换生日礼替换Action
 * @author GeHequn
 *
 */
public class BINCPMEACT05_Action extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource
	private BINCPMEACT05_BL bincpmeact05_BL;
	
	/** 活动下发batch运行状态0：已停止 ，1：运行中 */
	private static int status = 0;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	/** 品牌Id */
	private String brandInfoId;
	
	private static Logger logger = LoggerFactory
			.getLogger(BINCPMEACT05_Action.class.getName());
	
	/**
	 * 活动单据生成BATCH处理
	 * @throws Exception
	 */
	public String updateOrder() {
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		if(status == 0){
			status = 1;
			try {
				Map<String,Object> map = new HashMap<>();
				// 品牌信息ID
				map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
				flag = bincpmeact05_BL.tran_handleOrder(map);
			} catch (CherryBatchException e) {
				flag = 1;
				logger.info("=============WARN MSG================");
				logger.info(e.getMessage());
				logger.info("=====================================");
			}finally{
				status = 0;
				if (flag == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("会员等级变换生日礼替换正常终了");
				} else if (flag == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("会员等级变换生日礼替换警告终了");
				}
			}
		}else{
			this.addActionError("会员等级变换生日礼替换正在运行中，请稍后再执行该下发程序！！！");
		}
		
		return "DOBATCHRESULT";
	}
}
