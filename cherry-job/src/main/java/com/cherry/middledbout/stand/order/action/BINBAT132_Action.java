package com.cherry.middledbout.stand.order.action;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.middledbout.stand.order.bl.BINBAT132_BL;


/**
*
* 标准接口：订单数据导出Action
*
* @author chenkuan
*
* @version  2015-12-15
*/
public class BINBAT132_Action extends BaseAction {

	private static final long serialVersionUID = -615159224395927704L;

	@Resource(name = "binBAT132_BL")
	private BINBAT132_BL binBAT132_BL;

	private static Logger logger = LoggerFactory
			.getLogger(BINBAT132_Action.class.getName());

	public String binbat132Exec() throws Exception {

		logger.info("******************************标准产品订单导出处理开始***************************");
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
			flg = binBAT132_BL.tran_batchExportPrtOrder(map);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("标准产品订单导出处理正常终了");
				logger.info("******************************标准产品订单导出处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("标准产品订单导出处理警告终了");
				logger.info("******************************标准产品订单导出处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("标准产品订单导出处理异常终了");
				logger.info("******************************标准产品订单导出处理异常终了***************************");
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
