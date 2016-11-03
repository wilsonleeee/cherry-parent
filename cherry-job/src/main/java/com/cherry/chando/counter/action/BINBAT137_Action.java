package com.cherry.chando.counter.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.chando.counter.bl.BINBAT137_BL;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;

/**
*
* 自然堂：调整BAS生效时间
*
* @author lzs
*
* @version  2016-02-16
*/
public class BINBAT137_Action extends BaseAction {
	
	private static final long serialVersionUID = 4430391697340383706L;

	private static Logger logger = LoggerFactory.getLogger(BINBAT137_Action.class.getName());
	
	/** 自然堂：调整BAS生效时间 */
	@Resource(name = "binBAT137_BL")
	private BINBAT137_BL binBAT137_BL;
	
	/**
	 * 
	 * 调整BAS生效时间batch处理
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binBAT137Exec() throws Exception {

		logger.info("******************************调整BAS生效时间（自然堂）BATCH处理开始***************************");
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
			// Job运行履历表的运行方式
			map.put("RunType", "MT");
			map.put(CherryBatchConstants.CREATEDBY, userInfo.getBIN_UserID());
			map.put(CherryBatchConstants.UPDATEDBY, userInfo.getBIN_UserID());
			
			flg = binBAT137_BL.tran_binBAT137(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.error("=============WARN MSG================");
			logger.error(cbx.getMessage(),cbx);
			logger.error("=====================================");
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error("=============ERROR MSG===============");
			logger.error(e.getMessage(),e);
			logger.error("=====================================");
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("调整BAS生效时间（自然堂）BATCH处理正常终了");
				logger.info("******************************调整BAS生效时间（自然堂）BATCH处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("调整BAS生效时间（自然堂）BATCH处理警告终了");
				logger.info("******************************调整BAS生效时间（自然堂）BATCH处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("调整BAS生效时间（自然堂）BATCH处理异常终了");
				logger.info("******************************调整BAS生效时间（自然堂）BATCH处理异常终了***************************");
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
