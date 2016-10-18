/*  
 * @(#)BINOLBSCHA04_Form.java     1.0 2011/05/31      
 *      
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD       
 * All rights reserved      
 *      
 * This software is the confidential and proprietary information of         
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not        
 * disclose such Confidential Information and shall use it only in      
 * accordance with the terms of the license agreement you entered into      
 * with SHANGHAI BINGKUN.       
 */
package com.cherry.bs.cha.form;

	public class BINOLBSCHA04_Form{
		
		/** 渠道ID */
		private String channelId;
		
		/** 渠道名称 */	
		private String channelName;
		
		/** 渠道名称 */	
		private String channelNameForeign;
		
		/** 加入日期 */
		private String joinDate;
		
		/**  渠道类型 */
		private String status;
		
		/** 默认日期 */
		private String endDate;
		
		private String count;
		
		private String brandInfoId;
		
		public String getChannelName() {
			return channelName;
		}

		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}

		public String getChannelNameForeign() {
			return channelNameForeign;
		}

		public void setChannelNameForeign(String channelNameForeign) {
			this.channelNameForeign = channelNameForeign;
		}

		public String getJoinDate() {
			return joinDate;
		}

		public void setJoinDate(String joinDate) {
			this.joinDate = joinDate;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setCount(String count) {
			this.count = count;
		}

		public String getCount() {
			return count;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}

		public String getChannelId() {
			return channelId;
		}

		public void setBrandInfoId(String brandInfoId) {
			this.brandInfoId = brandInfoId;
		}

		public String getBrandInfoId() {
			return brandInfoId;
		}
}
