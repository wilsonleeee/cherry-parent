package com.cherry.cm.cmbussiness.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM09_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM59_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM61_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM09_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 促销活动共通Action
 * @author huzude
 *
 */
public class BINOLCM09_Action extends BaseAction implements ModelDriven<BINOLCM09_Form>{
	private static final long serialVersionUID = -2391645573092581280L;
	
	@Resource
	private BINOLCM09_BL binOLCM09_BL;
	@Resource(name="binOLCM59_BL")
	private BINOLCM59_BL binOLCM59_BL;
	@Resource(name="binOLCM61_BL")
	private BINOLCM61_BL binOLCM61_BL;
	/** 系统配置项 共通 **/
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 参数FORM */
	private BINOLCM09_Form form = new BINOLCM09_Form();
	
	private static Logger logger = LoggerFactory.getLogger(BINOLCM09_Action.class.getName());
	
	/** 促销活动下发batch运行状态0：已停止 ，1：运行中 */
	private static int cm09Status = 0;
	
	@Override
	public BINOLCM09_Form getModel() {
		return form;
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
	/**
	 * 活动下发
	 * @throws Exception
	 */
	public String publicActive () throws CherryBatchException{

		if(cm09Status == 0){
			logger.info("♠♠♠♠♠♠♠♠♠♠♠♠♠♠促销活动BATCH处理开始♠♠♠♠♠♠♠♠♠♠♠♠♠♠");
			Date d1 = new Date();
			cm09Status = 1;
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
				logger.info("********促销活动下发处理开始********");
				flg = binOLCM09_BL.tran_publicProActive(map);
				if(flg == CherryBatchConstants.BATCH_SUCCESS){
					logger.info("********促销规则下发处理开始********");
					if ("2".equals(binOLCM14_BL.getConfigValue("1347", String.valueOf(userInfo.getBIN_OrganizationInfoID()), String.valueOf(brandInfoId)))) {
						flg = binOLCM61_BL.tran_PromotionRule(map);
					} else {
						flg = binOLCM59_BL.tran_publicProActiveRule(map);
					}
				}
			} catch (CherryBatchException cbx) {
				flg = CherryBatchConstants.BATCH_WARNING;
			} catch (Exception e) {
				flg = CherryBatchConstants.BATCH_ERROR;
			} finally {
				Date d2 = new Date();
				long time = (d2.getTime() - d1.getTime()) / 1000;
				if(flg == CherryBatchConstants.BATCH_SUCCESS) {
					this.addActionMessage("促销活动下发处理正常终了");
					logger.info("♠♠♠♠♠♠♠♠♠♠♠♠♠♠促销活动BATCH结束【正常: "+time+" 秒】♠♠♠♠♠♠♠♠♠♠♠♠♠♠");
				} else if(flg == CherryBatchConstants.BATCH_WARNING) {
					this.addActionError("促销活动下发处理警告终了");
					logger.info("♠♠♠♠♠♠♠♠♠♠♠♠♠♠促销活动BATCH结束【警告: "+time+" 秒】♠♠♠♠♠♠♠♠♠♠♠♠♠♠");
				} else if(flg == CherryBatchConstants.BATCH_ERROR) {
					this.addActionError("促销活动下发处理异常终了");
					logger.info("♠♠♠♠♠♠♠♠♠♠♠♠♠♠促销活动BATCH结束【异常: "+time+" 秒】♠♠♠♠♠♠♠♠♠♠♠♠♠♠");
				}
				cm09Status = 0;
			}
			
		}else{
			this.addActionError("促销活动下发程序正在运行中，请稍后再执行该下发程序！！！");
		}
		return "DOBATCHRESULT";
	}
}
