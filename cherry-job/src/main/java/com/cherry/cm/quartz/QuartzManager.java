/*
 * @(#)QuartzManager.java     1.0 2013/02/27
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

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 * 任务管理器类
 * 
 * @author WangCT
 * @version 1.0 2013/02/27
 */
public class QuartzManager {
	
	private static Logger logger = LoggerFactory.getLogger(QuartzManager.class.getName());
	
	private static Scheduler scheduler;
	  
    static {
    	try {
    		scheduler = StdSchedulerFactory.getDefaultScheduler();
    		scheduler.start();
    	} catch (Exception e) {
    		logger.error("createScheduler error",e);
		}
    }
  
    /** 
     * 启动一个自定义的job 
     *  
     * @param customJob 
     *            自定义的job
     * @return 成功则返回true，否则返回false 
     */  
    public static boolean enableCronSchedule(CustomJob customJob) {
    	if (customJob == null) {
            return false;
        }
        try {
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(customJob.getTriggerName(), customJob.getJobGroup());
            // 如果不存在该trigger则创建一个  
            if (null == trigger) {
                MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
                methodInvokingJobDetailFactoryBean.setName(customJob.getJobId());
                methodInvokingJobDetailFactoryBean.setGroup(customJob.getJobGroup());
                methodInvokingJobDetailFactoryBean.setTargetObject(customJob.getTargetObject());
                methodInvokingJobDetailFactoryBean.setTargetMethod(customJob.getTargetMethod());
                methodInvokingJobDetailFactoryBean.setArguments(customJob.getArguments());
                methodInvokingJobDetailFactoryBean.setConcurrent(customJob.isConcurrent());
            	methodInvokingJobDetailFactoryBean.afterPropertiesSet();
            	JobDetail jobDetail = methodInvokingJobDetailFactoryBean.getObject();
                trigger = new CronTrigger(customJob.getTriggerName(), customJob.getJobGroup(), customJob.getCronExpression());
                scheduler.scheduleJob(jobDetail, trigger); 
            } else { // Trigger已存在，那么更新相应的定时设置
                trigger.setCronExpression(customJob.getCronExpression());
                scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
            }
        } catch (Exception e) {
        	logger.error("加载沟通调度计划失败，调度任务"+ customJob.getJobId() +"已过期，永不会被再次触发");
            return false;
        }
        return true;
    }
  
    /** 
     * 禁用一个job 
     *  
     * @param jobId 
     *            需要被禁用的job的ID 
     * @param jobGroupId 
     *            需要被禁用的job的组ID 
     * @return 成功则返回true，否则返回false 
     */  
    public static boolean disableSchedule(String jobId, String jobGroupId) {
    	if (jobId == null || "".equals(jobId) || 
        		jobGroupId == null || "".equals(jobGroupId)) {
            return false;
        }
        try {
            Trigger trigger = getJobTrigger(jobId, jobGroupId);
            if (trigger != null) {
                scheduler.deleteJob(jobId, jobGroupId);
            }
        } catch (Exception e) {
        	logger.error("disableSchedule error",e);
            return false;
        }
        return true;
    }
    
    /** 
     * 根据jobGroupId查询所有JobNames
     *  
     * @param jobGroupId 
     *            job的组ID 
     * @return 指定jobGroupId的所有JobNames
     */  
    public static String[] getJobNames(String jobGroupId) {
        if (jobGroupId == null || "".equals(jobGroupId)) {
            return null;
        }
        try {
            return scheduler.getJobNames(jobGroupId);
        } catch (Exception e) {
        	logger.error("getJobNames error",e);
            return null;
        }
    }
  
    /** 
     * 得到job的详细信息 
     *  
     * @param jobId 
     *            job的ID 
     * @param jobGroupId 
     *            job的组ID 
     * @return job的详细信息,如果job不存在则返回null 
     */  
    public static JobDetail getJobDetail(String jobId, String jobGroupId) {
        if (jobId == null || "".equals(jobId) || 
        		jobGroupId == null || "".equals(jobGroupId)) {
            return null;
        }
        try {
            return scheduler.getJobDetail(jobId, jobGroupId);
        } catch (Exception e) {
        	logger.error("getJobDetail error",e);
            return null;
        }
    }
  
    /** 
     * 得到job对应的Trigger 
     *  
     * @param jobId 
     *            job的ID 
     * @param jobGroupId 
     *            job的组ID 
     * @return job的Trigger,如果Trigger不存在则返回null 
     */  
    public static Trigger getJobTrigger(String jobId, String jobGroupId) {
    	if (jobId == null || "".equals(jobId) || 
        		jobGroupId == null || "".equals(jobGroupId)) {
            return null;
        }
        try {
            return scheduler.getTrigger(jobId + "Trigger", jobGroupId);
        } catch (Exception e) {
        	logger.error("getJobTrigger error",e);
            return null;
        }
    }
    
    /** 
     * 得到job对应的CronExpression 
     *  
     * @param jobId 
     *            job的ID 
     * @param jobGroupId 
     *            job的组ID 
     * @return CronExpression 
     */  
    public static String getCronExpression(String jobId, String jobGroupId) {
    	if (jobId == null || "".equals(jobId) || 
        		jobGroupId == null || "".equals(jobGroupId)) {
            return "";
        }
        try {
        	CronTrigger trigger = (CronTrigger)getJobTrigger(jobId, jobGroupId);
            if (trigger != null) {
            	return trigger.getCronExpression();
            }
        } catch (Exception e) {
        	logger.error("disableSchedule error",e);
            return "";
        }
        return "";
    }

}
