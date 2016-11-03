package com.cherry.mb.arc.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.mb.arc.bl.BINBEMBARC05_BL;

/**
 * 汇美舍官网奖励积分导入处理Action
 * @author menghao
 *
 */
public class BINBEMBARC05_Action extends BaseAction {

	private static final long serialVersionUID = -9034336822541677148L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC05_Action.class.getName());
	
	@Resource(name="binBEMBARC05_BL")
	private BINBEMBARC05_BL binBEMBARC05_BL;
	
	public String binbembarc05Exec() throws Exception {
		
		logger.info("******************************汇美舍官网奖励积分导入处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 组织code
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 汇美舍官网奖励积分导入处理
			flg = binBEMBARC05_BL.pointChangeImport(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error(cbx.getMessage(), cbx);
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error(e.getMessage(), e);
		} finally {
			if(flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("汇美舍官网奖励积分导入处理正常终了");
				logger.info("******************************汇美舍官网奖励积分导入处理正常终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("汇美舍官网奖励积分导入处理警告终了");
				logger.info("******************************汇美舍官网奖励积分导入处理警告终了***************************");
			} else if(flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("汇美舍官网奖励积分导入处理异常终了");
				logger.info("******************************汇美舍官网奖励积分导入处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
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

}
