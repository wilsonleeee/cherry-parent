
package com.cherry.ot.yin.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ot.yin.bl.BINBAT116_BL;

/**
 * 颖通接口：礼品领用单据导出(颖通)Action
 * 
 * @author lzs
 * 
 * @version 2015-07-03
 * 
 */
public class BINBAT116_Action extends BaseAction {

	private static final long serialVersionUID = -3826988732635618560L;

	@Resource(name = "binBAT116_BL")
	private BINBAT116_BL binBAT116_BL;

	private static Logger logger = LoggerFactory.getLogger(BINBAT116_Action.class.getName());

	public String binbat116Exec() throws Exception {

		logger.info("******************************礼品领用单据导出(颖通)处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID,
					userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			
			// Job运行履历表的运行方式
			map.put("RunType", "MT");
			flg = binBAT116_BL.tran_batchExportGiftDraw(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.info("========================WARN MSG========================");
			logger.info(cbx.getMessage());
			logger.info("=======================================================");
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error("=======================ERROR MSG=======================");
			logger.error(e.getMessage(),e);
			logger.error("=======================================================");
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("礼品领用单据导出(颖通)处理正常结束");
				logger.info("******************************礼品领用单据导出(颖通)处理正常结束***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("礼品领用单据导出(颖通)处理警告结束");
				logger.info("******************************礼品领用单据导出(颖通)处理警告结束***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("礼品领用单据导出(颖通)处理异常结束");
				logger.info("******************************礼品领用单据导出(颖通)处理异常结束***************************");
			}
		}
		return "DOBATCHRESULT";
	}

	/** 品牌Id */
	private String brandInfoId;
	
	/** 组织Id */
	private String organizationInfoId;

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

}
