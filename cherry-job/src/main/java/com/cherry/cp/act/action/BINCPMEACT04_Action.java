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
import com.cherry.cp.act.bl.BINCPMEACT04_BL;

/**
 * 会员活动下发Action
 * @author lipc
 *
 */
public class BINCPMEACT04_Action extends BaseAction{
	
	private static final long serialVersionUID = -9090883436887141028L;

	private static Logger logger = LoggerFactory.getLogger(BINCPMEACT04_Action.class.getName());
	
	@Resource
	private BINCPMEACT04_BL bincpmeact04_BL;
	
	/** 活动下发batch运行状态0：已停止 ，1：运行中 */
	private static int status = 0;
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 品牌code */
	private String brandCode;
	
	/**活动code*/
	private String activityCode;
	
	private String sysBusDate;
	
	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}


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
	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	/**
	 * 活动单据生成BATCH处理
	 * @throws Exception
	 */
	public String makeOrder() throws CherryBatchException{
		
		if(status == 0){
			logger.info("**********************活动单据生成BATCH处理开始**********************");
			status = 1;
			// 设置batch处理标志
			int flg = CherryBatchConstants.BATCH_SUCCESS;
			try {
				BINCPMEACT04_BL.setStatus(brandCode,BINCPMEACT04_BL.getStatus(brandCode) + 1);
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryBatchConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 组织Code
				map.put("orgCode", userInfo.getOrgCode());
				// 品牌信息ID
				map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
				// 品牌Code
				map.put(CherryBatchConstants.BRAND_CODE, brandCode);
				// 指定业务日期
				map.put("sysBusDate", sysBusDate);
				// 操作类型
				map.put("option", "BT");
				
				if(activityCode!=null){
					String[] activityCodeArr=activityCode.split(",");
					map.put("campCodeArr", activityCodeArr);
				}
//				map.put("EXEC_NOT_STARTED",1);
//				map.put("DIFFER_DATE",1);
//				map.put("DATE_SIZE_LIMIT",10000);
				flg = bincpmeact04_BL.makeOrder(map)[0];
			} catch (CherryBatchException cbx) {
				flg = CherryBatchConstants.BATCH_WARNING;
				logger.info("=============WARN MSG================");
				logger.info(cbx.getMessage());
				logger.info("=====================================");
			} catch (Exception e) {
				flg = CherryBatchConstants.BATCH_ERROR;
				logger.error("=============ERROR MSG===============");
				logger.error(e.getMessage(),e);
				logger.error("=====================================");
			} finally {
				status = 0;
				BINCPMEACT04_BL.setStatus(brandCode,BINCPMEACT04_BL.getStatus(brandCode) - 1);
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("活动单据生成处理正常终了");
					logger.info("**********************活动单据生成BATCH处理正常终了**********************");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("活动单据生成BATCH处理警告终了");
					logger.info("**********************活动单据生成BATCH处理警告终了**********************");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("活动单据生成BATCH处理异常终了");
					logger.info("**********************活动单据生成BATCH处理异常终了**********************");
				}
			}
		}else{
			this.addActionError("活动单据生成程序正在运行中，请稍后再执行该下发程序！！！");
		}
		return "DOBATCHRESULT";
	}

	public String getSysBusDate() {
		return sysBusDate;
	}

	public void setSysBusDate(String sysBusDate) {
		this.sysBusDate = sysBusDate;
	}
}
