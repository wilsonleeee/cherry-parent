package com.cherry.middledbout.stand.counter.action;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.middledbout.stand.counter.bl.BINBAT118_BL;

/**
*
* 标准接口：柜台信息导入Action
*
* @author lzs
*
* @version  2015-10-14
*/
public class BINBAT118_Action extends BaseAction {

	private static final long serialVersionUID = 6532817660816821910L;
	
	private static Logger logger = LoggerFactory.getLogger(BINBAT118_Action.class.getName());

	/** JOB执行锁*/
	private static int execFlag = 0;
	
	/** 柜台信息导入BL */
	@Resource(name = "binBAT118_BL")
	private BINBAT118_BL binBAT118_BL;
	
	/**
	 * 
	 * 柜台信息导出batch处理
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binBAT118Exec() throws Exception {

		logger.info("******************************柜台信息导入（标准接口）BATCH处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			// 已有其他线程正在执行该JOBs
			if (0 == execFlag) {
				// 锁定
				execFlag = 1;
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 品牌Id
				map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
				// 品牌code
				map.put(CherryBatchConstants.BRAND_CODE, brandCode);
				// Job运行履历表的运行方式
				map.put("RunType", "MT");
				
				flg = binBAT118_BL.tran_binBAT118(map);
				
				// 释放锁
				execFlag = 0;	
			}
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
			logger.info("=============WARN MSG================");
			logger.info(cbx.getMessage(),cbx);
			logger.info("=====================================");
			// 释放锁
			execFlag = 0;	
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
			logger.error("=============ERROR MSG===============");
			logger.error(e.getMessage(),e);
			logger.error("=====================================");
			// 释放锁
			execFlag = 0;	
		} finally {
			if (execFlag == 1) {
				this.addActionMessage("柜台信息导入(标准接口)处理中，请稍后。。。");
				logger.info("******************************柜台产品导入(标准接口)处理处理中，请稍后。。。***************************");
			} else {
				if (flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("柜台信息导入（标准接口）BATCH处理正常终了");
					logger.info("******************************柜台信息导入（标准接口）BATCH处理正常终了***************************");
				} else if (flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("柜台信息导入（标准接口）BATCH处理警告终了");
					logger.info("******************************柜台信息导入（标准接口）BATCH处理警告终了***************************");
				} else if (flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("柜台信息导入（标准接口）BATCH处理异常终了");
					logger.info("******************************柜台信息导入（标准接口）BATCH处理异常终了***************************");
				}
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
