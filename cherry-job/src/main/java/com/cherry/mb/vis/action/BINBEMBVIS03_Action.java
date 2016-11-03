package com.cherry.mb.vis.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.mb.vis.bl.BINBEMBVIS03_BL;

public class BINBEMBVIS03_Action extends BaseAction {
	
	private static final long serialVersionUID = -6115466719806186722L;

	private static Logger logger = LoggerFactory.getLogger(BINBEMBVIS03_Action.class.getName());
	
	/** 会员回访任务下发batch处理BL */
	@Resource
	private BINBEMBVIS03_BL binBEMBVIS03_BL;
	
	/**
	 * 会员回访
	 * 
	 * @param 无
	 * @return String
	 * */
	public String binbembvis03Exec() throws Exception {
		
		logger.info("******************************会员回访任务同步BATCH处理开始***************************");
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
			// 会员回访任务同步主处理
			flg = binBEMBVIS03_BL.tran_MemVisitTaskSyn(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("会员回访任务同步BATCH处理正常终了");
				logger.info("******************************会员回访任务同步BATCH处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("会员回访任务同步BATCH处理警告终了");
				logger.info("******************************会员回访任务同步BATCH处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("会员回访任务同步BATCH处理异常终了");
				logger.info("******************************会员回访任务同步BATCH处理异常终了***************************");
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
