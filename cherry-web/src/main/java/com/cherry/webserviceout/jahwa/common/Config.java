package com.cherry.webserviceout.jahwa.common;

public class Config {
	
	public static String WSIP = "jhpitest.jahwa.com.cn";
	/** WS访问PORT **/
	public static String WSPORT = "443";
	/** WS访问用户名 **/
	public static String USERNAME = "ZPMSTEST";
	/** WS访问密码 **/
	public static String PASSWORD = "123456";
	
	public static String WSIP_SMS = "jhpi.jahwa.com.cn";
	/** WS访问PORT **/
	public static String WSPORT_SMS = "443";
	/** WS访问用户名 **/
	public static String USERNAME_SMS = "ZPMS";
	/** WS访问密码 **/
	public static String PASSWORD_SMS = "654321";
	/** 短信渠道 **/
	public static String CHANNEL_SMS = "BCJCRM001";
	/** WS访问参数-组织代码-佰草集 **/
	public static String VKORG = "C004";
	/** WS访问参数-组织代码-佰草集 **/
	public static String VKORGC024 = "C024";
	/** WS访问参数-数据来源-佰草集线下 **/
	public static String DATASOURCE = "0002";
	/** WS访问超时等异常时，重试次数 **/
	public static int RECONNECTION_TIMES = 3;
	/** WS访问活动传输模式 1：增量传输，2：指定活动重新传输，3：全部活动重新传输 **/
	public static String ZTRAN_FLAG = "1";
	
}
