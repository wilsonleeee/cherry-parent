package com.cherry.webserviceout.kingdee.counterInfo.action;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.webserviceout.kingdee.counterInfo.bl.BINBEKDCOU01_BL;

/**
*
* Kingdee接口：柜台下发Action
*
* @author ZhaoCF
*
* @version  2015-4-29
*/
public class BINBEKDCOU01_Action extends BaseAction {

	private static final long serialVersionUID = 6532817660816821910L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBEKDCOU01_Action.class.getName());
	
	/** 柜台信息列表导入BL */
	@Resource
	private BINBEKDCOU01_BL binbekdcou01_BL;
	
	/**
	 * 
	 * 柜台列表导入batch处理
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbekdcou01Exec() throws Exception {

		logger.info("******************************柜台下发（韩束金蝶）BATCH处理开始***************************");
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
			
			flg = binbekdcou01_BL.tran_batchCou01(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.info("=============WARN MSG================");
			logger.info(cbx.getMessage(),cbx);
			logger.info("=====================================");
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error("=============ERROR MSG===============");
			logger.error(e.getMessage(),e);
			logger.error("=====================================");
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("柜台下发（韩束金蝶）BATCH处理正常终了");
				logger.info("******************************柜台下发（韩束金蝶）BATCH处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("柜台下发（韩束金蝶）BATCH处理警告终了");
				logger.info("******************************柜台下发（韩束金蝶）BATCH处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("柜台下发（韩束金蝶）BATCH处理异常终了");
				logger.info("******************************柜台下发（韩束金蝶）BATCH处理异常终了***************************");
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
