/*
 * @(#)MonitorConstants.java     1.0 2011/3/23
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

package com.cherry.mo.common;

/**
 * 
 * Monitor共通常量
 * 
 * @author niushunjie
 * @version 1.0 2011.3.23
 */
public class MonitorConstants {
	
    // ******** Monitor管理画面ID定义开始 ******** //
    /** 机器一览画面ID */
    public static final String BINOLMOMAN01 = "BINOLMOMAN01" ;
    
    /** 添加机器画面ID */
    public static final String BINOLMOMAN02 = "BINOLMOMAN02" ;
    
    /** 绑定机器画面ID */
    public static final String BINOLMOMAN03 = "BINOLMOMAN03" ;
    
    /** 柜台消息管理画面ID */
    public static final String BINOLMOCIO01 = "BINOLMOCIO01" ;
    
    /** 柜台消息发布画面ID */
    public static final String BINOLMOCIO14 = "BINOLMOCIO14" ;
    
    // ******** Monitor管理画面ID定义结束 ******** //

    /** 机器版本    WITPOSI（机器信息表 ）*/
    public static final String MachineType_WITPOSI = "W1";
    
    /** 机器版本    WITPOSII（机器信息表 ）*/
    public static final String MachineType_WITPOSII = "W2";
    
    /** 机器版本    WITPOSIII（机器信息表 ）*/
    public static final String MachineType_WITPOSIII = "W3";
    
    /** 机器版本    WITPOSIV（机器信息表 ）*/
    public static final String MachineType_WITPOSIV = "W4";
    
    /** 机器版本    WITBOX（机器信息表 ）*/
    public static final String MachineType_WITPOSBOX = "WB";
    
    /** 机器版本    WITSERVER（机器信息表 ）*/
    public static final String MachineType_WITSERVER = "WS";
    
    /** 机器版本    WITPOSmini（机器信息表 ）*/
    public static final String MachineType_WITPOSmini = "WM";
    
    /** 机器版本  MobilePOS（机器信息表）*/
    public static final String MachineType_MobilePOS = "MP";
    
    /** 机器状态    未下发（机器信息表 ）*/
    public static final String MachineStatus_UNSynchro = "2";
    
    /** 机器状态    已启用（机器信息表 ）*/
    public static final String MachineStatus_Start = "1";
    
    /** 机器状态    已停用（机器信息表 ）*/
    public static final String MachineStatus_Stop = "0";
    
    /** 机器状态    已报废（机器信息对照表）*/
    public static final String MachineStatus_Scrap = "3";
    
    /** 绑定状态    未分配（机器信息表 ）*/
    public static final String BindStatus_0 = "0";
    
    /** 绑定状态    已分配（机器信息表 ）*/
    public static final String BindStatus_1 = "1";
    
    /** 绑定状态    任意柜台（机器信息表 ）*/
    public static final String BindStatus_2 = "2";
    
    /** 绑定状态    任意柜台（机器信息表 ）*/
    public static final String BindStatus_2_Text = "任意柜台";
    
    /** 有效区分   无效（机器信息表 ）*/
    public static final String ValidFlag_0 = "0";
    
    /** 有效区分    有效（机器信息表 ）*/
    public static final String ValidFlag_1 = "1";

    /** IPOS3默认老机器号末位码（新旧机器号对照表 ）*/
    public static final String LastCode = "9";

    /** 机器升级  未升级的机器  */
    public static final String UPDATESTATUS_NO_LEVELED = "1";
    
    /** 机器升级 升级到正式版本 */
    public static final String UPDATESTATUS_OFFICIAL_LEVELED = "2";
    
    /** 机器升级 升级到测试版本*/
    public static final String UPDATESTATUS_TEST_LEVELED = "3";
    
    /**机器升级 区域类型为大区*/
    public static final String REGIONTYPE_REGION_LEVELED = "0";
    
    /**机器升级 区域类型为省或直辖市，自治区*/
    public static final String REGIONTYPE_PRIVINCE_LEVELED = "1";
    
    /**机器升级 区域类型为省会城市*/
    public static final String REGIONTYPE_CAPITAL_LEVELED = "2";
    
    /**机器升级 区域类型为一般性城市*/
    public static final String REGIONTYPE_CITY_LEVELED = "3";

    /** 最大日期跨度 */
    public static final String MAX_Date = "30";
    
    /**问卷(非考核问卷)问题类型为单选题*/
    public static final String QUESTIONTYPE_SINCHOICE = "1";
    
    /**问卷(非考核问卷)类型为多选题*/
    public static final String QUESTIONTYPE_MULCHOICE = "2";
    
    /**问卷(非考核问卷)问题类型为问答题*/
    public static final String QUESTIONTYPE_ESSAY = "0";
    
    /**问卷(非考核问卷)问题类型为填空题*/
    public static final String QUESTIONTYPE_APFILL = "3";
    
    /**考核问卷问题类型 单选题*/
    public static final String CHECKPAPER_QUESTIONTYPE_SINCHOICE = "0";
    
    /**考核问卷问题类型 填空题*/
    public static final String CHECKPAPER_QUESTIONTYPE_APFILL = "1";
    
    /**问卷状态 制作中*/
    public static final String PAPER_STATUS_MAKING = "0";
    
    /**问卷状态 已停用*/
    public static final String PAPER_STATUS_DISABLE = "1";
    
    /**问卷状态 可使用*/
    public static final String PAPER_STATUS_ENABLE = "2";
    
    /**问卷类型 普通问卷*/
    public static final String PAPER_TYPE_NORMAL = "0";
    
    /**问卷类型 会员问卷*/
    public static final String PAPER_TYPE_MEMBER = "1";
    
    /**问卷类型 商场问卷*/
    public static final String PAPER_TYPE_MARKET = "2";
    
    /**柜台消息发布控制标志 下发到此柜台  */
    public static final String ControlFlag_Allow = "1";
    
    /**柜台消息发布控制标志 禁止下发到此柜台*/
    public static final String ControlFlag_Forbidden = "2";
    
    /**U盘信息导入的sheet名称*/
    public static final String UpLoadUdisk_Sheet_Name = "U盘信息";
    
    /**U盘信息添加权限级别-美容顾问主管*/
    public static final int UpLoadUdisk_OwnerRight_Bas = 1;
    
    /**U盘信息添加权限级别-禁用U盘*/
    public static final int UpLoadUdisk_OwnerRight_Disable = 0;
    
    /**U盘信息添加权限级别-大区经理*/
    public static final int UpLoadUdisk_OwnerRight_Am = 2;
    
    /**U盘信息添加权限级别-培训师*/
    public static final int UpLoadUdisk_OwnerRight_Trainers = 3;
    
    /**U盘添加员工岗位名称-美容顾问主管*/
    public static final String UpLoadUdisk_EmployeePosition_Bas = "柜台主管";
    
    /**U盘添加员工岗位名称-区域经理*/
    public static final String UpLoadUdisk_EmployeePosition_Am = "城市经理";
    
    /**U盘添加员工岗位名称-区域培训师*/
    public static final String UpLoadUdisk_EmployeePosition_Trainers = "区域培训师";
    
    /**U盘添加属性-U盘序号*/
    public static final String ADDUDISK_UDISKSN__CHINESE = "U盘编号";
    
    /**U盘添加属性-员工CODE*/
    public static final String ADDUDISK_EMPLOYEECODE_CHINESE = "员工代码";
    
    /**U盘添加属性-员工岗位*/
    public static final String ADDUDISK_POSITION_CHINESE = "员工岗位";
}
