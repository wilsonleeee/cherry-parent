package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ss.prm.bl.BINBESSPRM04_BL;

/**
 * 促销活动规则计算
 * 
 * @author huzude
 * 
 */
public class BINBESSPRM04_Action extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4484998482217964908L;

	@Resource
	private BINBESSPRM04_BL binbessprm04BL;

	private static Logger logger = LoggerFactory.getLogger(BINBESSPRM01_Action.class.getName());
	
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

	public String binbessprm04Exec() throws Exception {
		logger.info("******************************BINBESSPRM04处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put("bin_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
			// 品牌信息ID
			map.put("brandInfoID", brandInfoId);
			// 品牌Code
			map.put("brandCode", brandCode);
			// 促销活动规则计算
			flg = binbessprm04BL.tran_prmRuleBatch(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			e.printStackTrace();
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("促销活动规则计算处理正常终了");
				logger.info("******************************BINBESSPRM04处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("促销活动规则计算处理警告终了");
				logger.info("******************************BINBESSPRM04处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("促销活动规则计算处理异常终了");
				logger.info("******************************BINBESSPRM04处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
