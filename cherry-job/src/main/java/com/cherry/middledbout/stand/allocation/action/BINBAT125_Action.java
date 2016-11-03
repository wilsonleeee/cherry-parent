package com.cherry.middledbout.stand.allocation.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.middledbout.stand.allocation.bl.BINBAT125_BL;

/**
 * 标准接口：调拨确认单据(调出)数据导出到标准接口表(调拨单)Action
 * 
 * @author lzs
 * 
 */
public class BINBAT125_Action extends BaseAction {
	private static final long serialVersionUID = -9129259731437186816L;
	private static Logger logger = LoggerFactory.getLogger(BINBAT125_Action.class.getName());
	@Resource(name = "binbat125_BL")
	private BINBAT125_BL binbat125_BL;

	/** 组织Id */
	private String organizationInfoId;

	/** 品牌ID */
	private int brandInfoId;
	
	/** 品牌代码 **/
	private String brandCode;

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String binbat125Exec() throws Exception {
		logger.info("*********************************调拨确认单据(调出)数据导出处理开始*******************************************");
		// 设置Batch处理标志
		int flag = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put(CherryBatchConstants.ORGANIZATIONINFOID,userInfo.getBIN_OrganizationInfoID());
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// Job运行履历表的运行方式
			map.put("RunType", "MT");

			flag = binbat125_BL.tran_binbat125(map);
		} catch (CherryBatchException cbe) {
			flag = CherryBatchConstants.BATCH_WARNING;
			logger.info("========================WARN MSG========================");
			logger.info(cbe.getMessage());
			logger.info("=======================================================");
		} catch (Exception e) {
			flag = CherryBatchConstants.BATCH_ERROR;
			logger.error("=======================ERROR MSG=======================");
			logger.error(e.getMessage(),e);
			logger.error("=======================================================");
		} finally {
			if (flag == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("调拨确认单据(调出)数据导出处理正常结束");
				logger.info("*******************************调拨确认单据(调出)数据导出处理正常结束************************************");
			} else if (flag == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("调拨确认单据(调出)数据导出处理警告结束");
				logger.info("*******************************调拨确认单据(调出)数据导出处理警告结束************************************");
			} else if (flag == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("调拨确认单据(调出)数据导出处理异常结束");
				logger.info("*******************************调拨确认单据(调出)数据导出处理异常结束*************************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
