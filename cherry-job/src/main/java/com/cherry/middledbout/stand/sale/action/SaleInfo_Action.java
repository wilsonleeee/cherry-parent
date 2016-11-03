package com.cherry.middledbout.stand.sale.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.middledbout.stand.sale.bl.SaleInfo_BL;
/**
 * 标准接口：销售数据导出到标准接口表(销售)Action
 * @author lzs
 *
 */
public class SaleInfo_Action extends BaseAction {
	private static final long serialVersionUID = -4860852838128046943L;
	private static Logger logger=LoggerFactory.getLogger(SaleInfo_Action.class.getName());
	@Resource(name="saleInfo_BL")
	private SaleInfo_BL saleInfo_BL;
	
	/** 组织Id */
	private String organizationInfoId;
	
	/** 品牌ID */
	private int brandInfoId;

	public String getOrganizationInfoId() {
		return organizationInfoId;
	}

	public void setOrganizationInfoId(String organizationInfoId) {
		this.organizationInfoId = organizationInfoId;
	}

	public int getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(int brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	public String saleInfoExec() throws Exception{
		logger.info("*********************************标准接口销售数据导出（销售）处理开始*******************************************");
		//设置Batch处理标志
		int flag=CherryBatchConstants.BATCH_SUCCESS;
		try{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryBatchConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
		map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		
		// Job运行履历表的运行方式
		map.put("RunType", "MT");
		
		flag = saleInfo_BL.tran_batchSaleInfo(map);
		}catch(CherryBatchException cbe){
			flag=CherryBatchConstants.BATCH_WARNING;
			logger.info("========================WARN MSG========================");
			logger.info(cbe.getMessage());
			logger.info("=======================================================");
		}catch(Exception e){
			flag=CherryBatchConstants.BATCH_ERROR;
			logger.error("=======================ERROR MSG=======================");
			logger.error(e.getMessage(),e);
			logger.error("=======================================================");
		}
		finally{
			if(flag==CherryBatchConstants.BATCH_SUCCESS){
				this.addActionMessage("标准接口销售数据导出（销售）处理正常结束");
				logger.info("*******************************标准接口销售数据导出（销售）处理正常结束************************************");
			} else if(flag==CherryBatchConstants.BATCH_WARNING){
				this.addActionError("标准接口销售数据导出（销售）处理警告结束");
				logger.info("*******************************标准接口销售数据导出（销售）处理警告结束************************************");
			}else if(flag==CherryBatchConstants.BATCH_ERROR){
				this.addActionError("标准接口销售数据导出（销售）处理异常结束");
				logger.info("*******************************标准接口销售数据导出（销售）处理异常结束*************************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
