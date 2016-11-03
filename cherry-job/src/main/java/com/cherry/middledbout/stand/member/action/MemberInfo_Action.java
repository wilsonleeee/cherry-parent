package com.cherry.middledbout.stand.member.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchException;
import com.cherry.middledbout.stand.member.bl.MemberInfo_BL;
/**
 * 标准接口:会员数据导出至标准接口表(IF_MemberInfo)Action
 * @author lzs
 * 下午1:54:03
 */
public class MemberInfo_Action extends BaseAction {
	private static final long serialVersionUID = 808247097244056006L;
	private static Logger logger=LoggerFactory.getLogger(MemberInfo_Action.class.getName());
	@Resource(name="memberInfo_BL")
	private MemberInfo_BL memberInfo_BL;
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
	public String memberInfoExec() throws Exception{
		logger.info("*********************************标准接口会员数据导出Batch处理开始*******************************************");
		//设置Batch处理标志
		int flag=CherryBatchConstants.BATCH_SUCCESS;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) session.get(CherryBatchConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryBatchConstants.BRANDINFOID, brandInfoId);
			map.put(CherryBatchConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// Job运行履历表的运行方式
			map.put("RunType", "MT");
		flag = memberInfo_BL.tran_batchMemberInfo(map);
		}catch(CherryBatchException cbe){
			flag=CherryBatchConstants.BATCH_WARNING;
			logger.error("========================WARN MSG========================");
			logger.error(cbe.getMessage(),cbe);
			logger.error("=======================================================");
		}catch(Exception e){
			flag=CherryBatchConstants.BATCH_ERROR;
			logger.error("=======================ERROR MSG=======================");
			logger.error(e.getMessage(),e);
			logger.error("=======================================================");
		}
		finally{
			if(flag==CherryBatchConstants.BATCH_SUCCESS){
				this.addActionMessage("标准接口会员数据导出处理正常结束");
				logger.info("*******************************标准接口会员数据导出Batch处理正常结束************************************");
			} else if(flag==CherryBatchConstants.BATCH_WARNING){
				this.addActionError("标准接口会员数据导出处理警告结束");
				logger.info("*******************************标准接口会员数据导出Batch处理警告结束************************************");
			}else if(flag==CherryBatchConstants.BATCH_ERROR){
				this.addActionError("标准接口会员数据导出处理异常结束");
				logger.info("*******************************标准接口会员数据导出Batch处理异常结束*************************************");
			}
		}
		return "DOBATCHRESULT";
	}
}
