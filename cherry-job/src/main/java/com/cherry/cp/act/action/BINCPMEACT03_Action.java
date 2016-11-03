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
import com.cherry.cp.act.bl.BINCPMEACT03_BL;

public class BINCPMEACT03_Action extends BaseAction {
	
	private static final long serialVersionUID = 8534717054514522453L;

	private static Logger logger = LoggerFactory.getLogger(BINCPMEACT03_Action.class.getName());
	
	/** 导入会员活动和会员活动预约信息处理BL */
	@Resource
	private BINCPMEACT03_BL binCPMEACT03_BL;
	
	/**
	 * <p>
	 * 导入会员活动和会员活动预约信息处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String importCampaignInfo() throws Exception {

		logger.info("******************************导入会员活动和会员活动预约信息处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 导入会员活动和会员活动预约信息处理
			flg = binCPMEACT03_BL.tran_importCampaignInfo(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.warn(cbx.getMessage());
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(),e);
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("导入会员活动和会员活动预约信息处理正常终了");
				logger.info("******************************导入会员活动和会员活动预约信息处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("导入会员活动和会员活动预约信息处理警告终了");
				logger.info("******************************导入会员活动和会员活动预约信息处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("导入会员活动和会员活动预约信息处理异常终了");
				logger.info("******************************导入会员活动和会员活动预约信息处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌code */
	private String brandCode;

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

}
