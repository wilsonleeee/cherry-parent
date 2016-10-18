package com.cherry.mb.rpt.form;

/**
 * 
 * @ClassName: BINOLMBRPT05_Form 
 * @Description: TODO(会员销售统计Form) 
 * @author menghao
 * @version v1.0.0 2015-1-6 
 *
 */
public class BINOLMBRPT05_Form{

	/** 销售时间上限 */
	private String startDate;
	
	/** 销售时间下限 */
	private String endDate;
	
	/** 渠道ID */
	private String channelId;
	
	/** 柜台部门ID */
	private String organizationId;
	
	/** 渠道名称 */
	private String channelName;
	
	/** 柜台部门名称 */
	private String organizationName;
	
	/**所属系统*/
	private String belongFaction;
	
	/**统计类型（1：消费次数  2：消费金额  3：年龄段）*/
	private String statisticsType;
	
	/**是否包含测试柜台*/
	private String testFlag;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getBelongFaction() {
		return belongFaction;
	}

	public void setBelongFaction(String belongFaction) {
		this.belongFaction = belongFaction;
	}

	public String getStatisticsType() {
		return statisticsType;
	}

	public void setStatisticsType(String statisticsType) {
		this.statisticsType = statisticsType;
	}

	public String getTestFlag() {
		return testFlag;
	}

	public void setTestFlag(String testFlag) {
		this.testFlag = testFlag;
	}

}
