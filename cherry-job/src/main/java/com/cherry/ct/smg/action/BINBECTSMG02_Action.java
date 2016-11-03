package com.cherry.ct.smg.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.ct.smg.bl.BINBECTSMG02_BL;

public class BINBECTSMG02_Action extends BaseAction {

	private static final long serialVersionUID = -9088556675340637970L;

	private static Logger logger = LoggerFactory.getLogger(BINBECTSMG02_Action.class.getName());
	
	/** 沟通任务动态调度管理BL */
	@Resource
	private BINBECTSMG02_BL binBECTSMG02_BL;
	
	/**
	 * <p>
	 * 沟通任务动态调度处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String
	 * 
	 */
	public String binbectsmg02Exec() throws Exception {

		logger.info("******************************沟通任务动态调度处理开始***************************");
		// 设置batch处理标志
		int flg = CherryBatchConstants.BATCH_SUCCESS;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 组织代码
			map.put("orgCode", userInfo.getOrgCode());
			// 品牌Id
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			// 品牌code
			map.put(CherryBatchConstants.BRAND_CODE, brandCode);
			// 沟通任务动态调度处理
			flg = binBECTSMG02_BL.tran_ScheduleTask(map);
		} catch (CherryBatchException cbx) {
			flg = CherryBatchConstants.BATCH_WARNING;
		} catch (Exception e) {
			flg = CherryBatchConstants.BATCH_ERROR;
		} finally {
			if (flg == CherryBatchConstants.BATCH_SUCCESS) {
				this.addActionMessage("沟通任务动态调度处理正常终了");
				logger.info("******************************沟通任务动态调度处理正常终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_WARNING) {
				this.addActionError("沟通任务动态调度处理警告终了");
				logger.info("******************************沟通任务动态调度处理警告终了***************************");
			} else if (flg == CherryBatchConstants.BATCH_ERROR) {
				this.addActionError("沟通任务动态调度处理异常终了");
				logger.info("******************************沟通任务动态调度处理异常终了***************************");
			}
		}
		return "DOBATCHRESULT";
	}
	
	/**
	 * 调度任务一览画面
	 * 
	 * @param 无
	 * @return String
	 */
	public String searchJobList() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 品牌code
		map.put(CherryBatchConstants.BRAND_CODE, brandCode);
		// 查询调度信息List
		jobList = binBECTSMG02_BL.getJobList(map);
		
		return SUCCESS;
	}
	
	/**
	 * 删除调度任务处理
	 * 
	 * @param 无
	 * @return String
	 */
	public String deleteJob() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 品牌code
		map.put(CherryBatchConstants.BRAND_CODE, brandCode);
		// 调度任务ID
		map.put("schedulesId", jobId);
		try {
			// 删除指定调度任务
			binBECTSMG02_BL.tran_deleteJob(map);
			this.addActionMessage("删除调度任务处理成功");
		} catch (Exception e) {
			this.addActionError("删除调度任务处理失败");
			logger.error("删除调度任务处理失败",e);
		}
		return "DOBATCHRESULT";
	}
	
	/** 品牌Id */
	private String brandInfoId;
	
	/** 品牌code */
	private String brandCode;
	
	/** 调度信息List */
	private List<Map<String, Object>> jobList;
	
	/** 调度任务ID */
	private String jobId;

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

	public List<Map<String, Object>> getJobList() {
		return jobList;
	}

	public void setJobList(List<Map<String, Object>> jobList) {
		this.jobList = jobList;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
