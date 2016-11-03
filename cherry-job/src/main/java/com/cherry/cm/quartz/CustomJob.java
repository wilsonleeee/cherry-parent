/*
 * @(#)CustomJob.java     1.0 2013/02/27
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
package com.cherry.cm.quartz;

import org.quartz.Scheduler;

/**
 * 自定义任务类
 * 
 * @author WangCT
 * @version 1.0 2013/02/27
 */
public class CustomJob {
	
	/** 默认组ID **/
	public static final String DEFAULT_GROUP = Scheduler.DEFAULT_GROUP;
    
	/** 任务Id，一般为所定义Bean的Id **/
    private String jobId;
    /** 任务组Id **/
    private String jobGroup;
    /** 任务运行时间表达式 **/
    private String cronExpression;
    /** 任务执行对象 **/
    private Object targetObject;
    /** 任务执行方法名 **/
    private String TargetMethod;
    /** 任务执行参数 **/
    private Object[] arguments;
    /** 是否并发，ture：允许并发，false：不允许并发 **/
    private boolean concurrent;
    
    /** 
     * 构造函数（任务组Id为默认组，任务执行默认设置成允许并发）
     */
    public CustomJob(String jobId, String cronExpression, Object targetObject, 
    		String targetMethod, Object[] arguments) {
    	this(jobId, DEFAULT_GROUP, cronExpression, targetObject, targetMethod, arguments, true);
    }
    
    /** 
     * 构造函数
     */
    public CustomJob(String jobId, String jobGroup, String cronExpression, 
    		Object targetObject, String targetMethod, Object[] arguments, boolean concurrent) {
    	this.jobId = jobId;
    	this.jobGroup = jobGroup;
    	this.cronExpression = cronExpression;
    	this.targetObject = targetObject;
    	this.TargetMethod = targetMethod;
    	this.arguments = arguments;
    	this.concurrent = concurrent;
    }
	
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public String getTargetMethod() {
		return TargetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		TargetMethod = targetMethod;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public boolean isConcurrent() {
		return concurrent;
	}

	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	/** 
     * 得到该job的Trigger名字 
     */  
    public String getTriggerName() {
        return this.getJobId() + "Trigger";  
    }
}
