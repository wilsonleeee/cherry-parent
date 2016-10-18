package com.cherry.mq.mes.dto;

/**
 * 机器信息DTO
 * 
 * @author zhhuyi
 */
public class MachMainDataDTO extends MainDataDTO{

	/** 机器版本号 */
	private String softWareVersion;
	/** 剩余磁盘空间 */
	private String  capacity;
	/** 数据来源*/
    private String sourse;
    
    /**单据号*/
    private String tradeNoIF;
    
	public String getSoftWareVersion() {
		return softWareVersion;
	}

	public void setSoftWareVersion(String softWareVersion) {
		this.softWareVersion = softWareVersion;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getSourse() {
		return sourse;
	}

	public void setSourse(String sourse) {
		this.sourse = sourse;
	}

    public String getTradeNoIF() {
        return tradeNoIF;
    }

    public void setTradeNoIF(String tradeNoIF) {
        this.tradeNoIF = tradeNoIF;
    }
}
