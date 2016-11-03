package com.cherry.cp.act.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.cp.act.bl.BINCPMEACT01_BL;

/**
 * 会员活动下发Action
 * @author lipc
 *
 */
public class BINCPMEACT01_Action extends BaseAction{
	
	private static final long serialVersionUID = -9090883436887141028L;

	private static Logger logger = LoggerFactory.getLogger(BINCPMEACT01_Action.class.getName());
	
	@Resource
	private BINCPMEACT01_BL bincpmeact01_BL;
	
	/** 活动下发batch运行状态0：已停止 ，1：运行中 */
	private static int status = 0;
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 品牌code */
	private String brandCode;

	public String getBrandInfoId() {
		return brandInfoId;
	}


	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}


	public String getBrandCode() {
		return brandCode;
	}


	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	/**
	 * 活动下发
	 * @throws Exception
	 */
	public String publicActive () throws CherryBatchException{
		
		if(status == 0){
			logger.info("**********************会员活动下发BATCH处理开始**********************");
			status = 1;
			// 设置batch处理标志
			int flg = CherryBatchConstants.BATCH_SUCCESS;
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryBatchConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 品牌信息ID
				map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
				// 品牌Code
				map.put(CherryBatchConstants.BRAND_CODE, brandCode);
				flg = bincpmeact01_BL.tran_publicActive(map);
			} catch (CherryBatchException cbx) {
				flg = CherryBatchConstants.BATCH_WARNING;
				logger.info("=============WARN MSG================");
				logger.info(cbx.getMessage());
				logger.info("=====================================");
			} catch (Exception e) {
				flg = CherryBatchConstants.BATCH_ERROR;
				logger.error("=============ERROR MSG===============");
				logger.error(e.getMessage(),e);
				logger.error("=====================================");
			} finally {
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("会员活动下发处理正常终了");
					logger.info("**********************会员活动下发BATCH处理正常终了**********************");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("会员活动下发处理警告终了");
					logger.info("**********************会员活动下发BATCH处理警告终了**********************");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("会员活动下发处理异常终了");
					logger.info("**********************会员活动下发BATCH处理异常终了**********************");
				}
				status = 0;
			}
		}else{
			this.addActionError("会员活动下发程序正在运行中，请稍后再执行该下发程序！！！");
		}
		return "DOBATCHRESULT";
	}
}
