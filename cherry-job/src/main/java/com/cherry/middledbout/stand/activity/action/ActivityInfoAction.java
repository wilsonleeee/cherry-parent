package com.cherry.middledbout.stand.activity.action;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ia.cou.action.BINBEIFCOU01_Action;
import com.cherry.middledbout.stand.activity.bl.ActivityInfo_BL;
/**
 * 
 * 促销活动列表导出Action
 * 
 * @author ZhaoCF
 * 
 */
public class ActivityInfoAction extends BaseAction {

	private static final long serialVersionUID = 6532817660816821910L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFCOU01_Action.class.getName());
	
	/** 促销活动列表导出BL */
	@Resource
	private ActivityInfo_BL activityInfo_BL;
	
	/**
	 * <p>
	 * 促销活动列表导出batch处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String activityInfoExec() throws Exception {

		logger.info("******************************促销活动列表导出BATCH处理开始***************************");
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
			// 柜台列表导入batch主处理
			flg = activityInfo_BL.tran_activityInfo(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("促销活动导出BATCH处理正常终了");
				logger.info("******************************促销活动列表导出处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("促销活动导出BATCH处理警告终了");
				logger.info("******************************促销活动列表导出处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("促销活动导出BATCH处理异常终了");
				logger.info("******************************促销活动列表导出处理异常终了***************************");
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
