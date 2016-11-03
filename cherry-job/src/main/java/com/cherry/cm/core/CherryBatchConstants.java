package com.cherry.cm.core;

public class CherryBatchConstants {
	
	// SqlID
	public static final String IBATIS_SQL_ID = "ibatis_sql_id";
	
	//用户信息key
	public static final String SESSION_USERINFO = "userinfo";
	
	
	//读取topmenu菜单后生成的List存放于request中对应的key
	public static final String SESSION_TOPMENU_LIST = "topmenu";
	
	//读取leftmenu菜单后生成的xml document文档存放于request中对应的key
	public static final String SESSION_LEFTMENU_XMLDOCMAP = "xmldocumentmap";
	
	//读取leftmenu菜单后生成的xml格式字符串文档存放于request中对应的key
	public static final String SESSION_LEFTMENU_XMLSTRING = "leftmenu";


	/** 逗号 */
	public static final String COMMA = ",";

	/** 空字符串 */
	public static final String BLANK = "";
	
	/** 空格 */
	public static final String SPACE = " ";
	
	/** 下划线 */
	public static final String UNLINE = "_" ;
	
	/** 作成者 */
	public static final String CREATEDBY = "createdBy";
	
	/** batch默认作成,更新者 */
	public static final String UPDATE_NAME = "BATCH";
	
	/** Cherry默认作成,更新者 */
	public static final String CHERRY_UPDATE_NAME = "Cherry";
	
	/** 作成程序名 */
	public static final String CREATEPGM = "createPGM";
	
	/** 更新者 */
	public static final String UPDATEDBY = "updatedBy";
	
	/** 更新程序名 */
	public static final String UPDATEPGM = "updatePGM";
	
	/** 更新日时 */
	public static final String UPDATE_TIME = "updateTime";
	
	/** 创建时间 */
	public static final String CREATE_TIME = "createTime";

	/** 修改时间 */
	public static final String MODIFY_TIME = "modifyTime";
	
	/** 修改次数 */
	public static final String MODIFY_COUNT = "modifyCount";
	
	/** 有效区分 */
	public static final String VALID_FLAG = "validFlag";
	
	/** 有效性区分   有效*/
	public static final String VALIDFLAG_ENABLE = "1";
	
	/** 有效性区分   无效*/
	public static final String VALIDFLAG_DISABLE = "0";
	
	/** 程序运行开始时间 */
	public static final String RUN_STARTTIME = "RunStartTime";
	
	
	/** 每天活动单据处理上限 */
	public static final int SIZE_LIMIT_DEF = 100000;
	
	/** 活动开始日期与业务日期差值 */
	public static final int DIFFERDATE_DEF = 1;
	
	/** 每次批处理条数 */
	public static final int DATE_SIZE= 5000;
	
	/** 每次批处理条数 */
	public static final int DATE_SIZE_1000 = 1000;
	
	/** 每次批处理条数 */
	public static final int DATE_SIZE_2000 = 2000;
	
	/** 每次批处理条数 */
	public static final int DATE_SIZE_10W= 100000;
	
	/** 主单据批处理条数 */
	public static final int BATCH_SIZE = 2000;
	
	/** 世代管理上限 */
	public static final int COUNT = 5;

	/** 品牌ID*/
	public static final String BRAND_ID = "brandId" ;
	
	/** 品牌代码*/
	public static final String BRAND_CODE = "brandCode" ;
	
	/** 分页 开始条数*/
	public static final String START = "START" ;

	/** 分页 结束条数*/
	public static final String END = "END" ;
	
	/** 分页 排序字段(字符)*/
	public static final String SORT_ID = "SORT_ID" ;
	
	/** 没有设置LOG等级 */
	public static final int LOGGER_NONE = 0;
	/** 调试 */
	public static final int LOGGER_DEBUG = 10;
	
	/** 信息 */
	public static final int LOGGER_INFO = 20;	
	
	/** 警告 */
	public static final int LOGGER_WARNING = 30;
	
	/** 错误 */
	public static final int LOGGER_ERROR = 40;
	
	// 设置BATCH处理标志
	/** 成功 */
	public static final int BATCH_SUCCESS = 0;

	/** 警告 */
	public static final int BATCH_WARNING = 1;

	/** 失败 */
	public static final int BATCH_ERROR = -1;

	// CherryConstants 导入 start	
	/** 语言key */
	public static final String SESSION_LANGUAGE = "language";
	
	/** 语言key 中文*/
	public static final String SESSION_LANGUAGE_CN = "zh_CN";
	
	/** 语言key 英文*/
	public static final String SESSION_LANGUAGE_EN = "en_US";
	
	/** 全局错误*/
	public static final String GLOBAL_ERROR = "globalError";
	/** action执行结果（错误，成功，字段校验错误）*/
	public static final String GLOBAL_ACCTION_RESULT = "globalAcctionResult";
	/** action执行结果（错误，成功）替换整张页面 */
	public static final String GLOBAL_ACCTION_RESULT_BODY = "globalAcctionResultBody";
	/** 登录画面*/
	public static final String GLOBAL_LOGIN = "globalLogin";
	
	/** 非会员卡号 **/
	public static final String NOT_MEMBER_CARD = "000000000";
	
	/** 全体共通*/
	public static final int CODE_ALL = -8888;
		
	/** can use privilege key */
	public static final String SESSION_PRIVILEGE_ENABLE = "enablePrivilege";
	
	/** datatable 过滤字段名 */
	public static final String FILTER_LIST_NAME = "filterList" ;

	/** can not use privilege key */
	public static final String SESSION_PRIVILEGE_DISABLE = "disablePrivilege";
	
	/** 弹出datable -- 促销产品信息 */
	public static final String POP_PRMPRODUCT_LIST = "popPrmProductInfoList" ;
	/** 子系统ID长度 */
	public static final int SUBSYS_ID_LENGTH = 2;
	/** 模块ID长度 */
	public static final int MODULE_ID_LENGTH = 5;
	/** 功能ID长度 */
	public static final int FUNCTION_ID_LENGTH = 7;
	/** 画面ID连番长度 */
	public static final int PAGE_ID_LENGTH = 2;
	/** 控件ID长度 */
	public static final int CONTROL_ID_LENGTH = 14;
	/** 画面ID前缀 */
	public static final String PAGE_NAME_PRE = "BINOL";
	
	/** 用户ID */
	public static final String USERID = "userId";
	
	/** 组织ID */
	public static final String ORGANIZATIONINFOID = "organizationInfoId";
	
	/** 组织ID */
	public static final String POSITIONCATEGORYID = "positionCategoryId";
	
	/** 岗位ID */
	public static final String POSITIONID = "positionId";
	
	/** 岗位类别（ 美容顾问）*/
	public static final String CATRGORY_CODE_BA="01";
	
	/** 品牌ID */
	public static final String BRANDINFOID = "brandInfoId";
	
	/** 树节点位置 */
	public static final String PATH = "path";

	/** 树节点级别 */
	public static final String LEVEL = "level";
	
	/** 业务日期 */
	public static final String BUSINESS_DATE = "businessDate";
	
	/** 业务类型 */
	public static final String BUSINESS_TYPE = "businessType";
	
	/** 业务类型--基础数据  */
	public static final String BUSINESS_TYPE0 = "0";
	
	/** 业务类型--库存数据  */
	public static final String BUSINESS_TYPE1 = "1";
	
	/** 业务类型--会员数据  */
	public static final String BUSINESS_TYPE2 = "2";
	
	/** 操作类型 */
	public static final String OPERATION_TYPE = "operationType";
	
	/** 操作类型--更新（包括新增，删除）*/
	public static final String OPERATION_TYPE0 = "0";
	
	/** 操作类型--查询  */
	public static final String OPERATION_TYPE1 = "1";

	/** 品牌为总部时的值  */
	public static final int BRAND_INFO_ID_VALUE = -9999;
	
	/** 促销品ID */
	
	public static final String PROMOTIONPROID = "promotionProId";
	
	/** 促销品名称 */
	public static final String NAMETOTAL = "nameTotal";
	
	/** 厂商编码 */
	public static final String UNITCODE = "unitCode";
	
	/** 促销品条码 */
	public static final String BARCODE = "barCode";
	
	/** 促销品MODE */
	public static final String MODE = "mode";
	
	/**  促销品开始销售日期*/
	public static final String SELLSTARTDATE = "sellStartDate";
	
	/**  促销品结束销售日期*/
	public static final String SELLENDDATE = "sellEndDate";
	
    /** 促销品厂商ID */
	public static final String FACTORYID = "factoryId";
	
    /** 类别代码 */
	public static final String ITEMCLASSCODE = "itemClassCode";
	
	/** 促销活动基础条件 --时间 */
	public static final String BASE_PROP_TIME = "baseProp_time";
	
	/** 促销活动基础条件 --区域市 */
	public static final String BASE_PROP_CITY = "baseProp_city";
	
	/** 促销活动基础条件 --渠道*/
	public static final String BASE_PROP_CHANNEL = "baseProp_channal";
	
	/** 促销活动基础条件 --柜台*/
	public static final String BASE_PROP_COUNTER = "baseProp_counter";
	
	/** 促销活动基础条件--消费金额 */
	public static final String BASE_PROP_AMOUNT = "baseProp_amount";
	
	/** 促销活动基础条件--购买产品 */
	public static final String BASE_PROP_PRODUCT = "baseProp_product";
	
	/** 促销活动套装折扣产品条码*/
	public static final String PROMOTION_TZZK_UNIT_CODE = "TZZK999999";
	
	/** 促销活动套装折扣类别码*/
	public static final String PROMOTION_TZZK_TYPE_CODE = "TZZK";
	
	/** 促销活动套装折扣名称*/
	public static final String PROMOTION_TZZK_NAME = "套装折扣";
	
	/** 促销活动促销礼品类别码*/
	public static final String PROMOTION_CXLP_TYPE_CODE = "CXLP";
	
	/** 促销活动促销礼品名称*/
	public static final String PROMOTION_CXLP_NAME = "促销礼品";
	
	/** 促销活动促销礼品类别码*/
	public static final String PROMOTION_DHCP_TYPE_CODE = "DHCP";
	
	/** 促销活动促销礼品名称*/
	public static final String PROMOTION_DHCP_NAME = "积分兑礼";
	
	/** 促销活动积分兑现类别码*/
	public static final String PROMOTION_DHMY_TYPE_CODE = "DHMY";
	
	/** 促销活动积分兑现名称*/
	public static final String PROMOTION_DHMY_NAME = "积分兑现";
	
	/** 促销活动促销礼品类别码*/
	public static final String PROMOTION_ZDTL_TYPE_CODE = "ZDTL";
	
	/** 促销活动促销礼品名称*/
	public static final String PROMOTION_ZDTL_NAME = "整单去零";
	
	/** 促销活动 运费类别码*/
	public static final String PROMOTION_YF_TYPE_CODE = "YF";
	
	/** 促销活动 运费名称*/
	public static final String PROMOTION_YF_NAME = "运费";
	
	/** 促销品代码和名称*/
	public static enum PROMOTION_CODE_NAME {
		cxlpProm(PROMOTION_CXLP_TYPE_CODE, PROMOTION_CXLP_TYPE_CODE, PROMOTION_CXLP_NAME),
		tzzkProm(PROMOTION_TZZK_TYPE_CODE, PROMOTION_TZZK_TYPE_CODE, PROMOTION_TZZK_NAME),
		dhcpProm(PROMOTION_DHCP_TYPE_CODE, PROMOTION_DHCP_TYPE_CODE, PROMOTION_DHCP_NAME),
		dhmyProm(PROMOTION_DHMY_TYPE_CODE, PROMOTION_DHMY_TYPE_CODE, PROMOTION_DHMY_NAME),
		zdtlProm(PROMOTION_ZDTL_TYPE_CODE, PROMOTION_ZDTL_TYPE_CODE, PROMOTION_ZDTL_NAME),
		yfProm(PROMOTION_YF_TYPE_CODE,PROMOTION_YF_TYPE_CODE, PROMOTION_YF_NAME);
		
		public static String getName(Object key) {
			return getValue(key, 1);
		}
		
		public static String getCode(Object key) {
			return getValue(key, 0);
		}
		
		public static String getValue(Object key, int kbn) {
			if(null == key || "".equals(key)) {
				return null;
			}
			PROMOTION_CODE_NAME[] proms = PROMOTION_CODE_NAME.values();
			for(PROMOTION_CODE_NAME prom : proms) {
				if(prom.getKey().equals(key)) {
					if (0 == kbn) {
						return prom.getPromCode();
					} else {
						return prom.getPromName();
					}
				}
			}
			return null;
		}
		
		PROMOTION_CODE_NAME(String key, String promCode, String promName) {
			this.key = key;
			this.promCode = promCode;
			this.promName = promName;
		}
		
		public String getKey() {
			return key;
		}

		public String getPromCode() {
			return promCode;
		}

		public String getPromName() {
			return promName;
		}
		
		private String key;
		private String promCode;
		private String promName;
	}
	
	/** mangoDB 促销规则DRL表名*/
	public static final String PRM_RULE_COLL_NAME = "MGO_PrmRuleDetail";
	
	/** mangoDB 促销规则详细定义表名*/
	public static final String PRM_RULE_HEAD_NAME = "MGO_PrmRuleHeader";
	
	/** mangoDB 促销规则函数表名*/
	public static final String PRM_RULE_FUNCTION_NAME = "MGO_PrmRuleFunction";
	
	// CherryConstants 导入 end
	

	/** 所属组织代号 */
	public static final String ORGANIZATIONINFOID_VAL = "10";
	/** 所属品牌代号 */
	public static final String BRANDINFOID_VAL = "1";
	/** 消息发送方向：发送 */
	public static final String SEND = "S";
	/** 消息发送方向：接收 */
	public static final String RECE = "R";
	/** 消息发送接收标志位0：未比对 */
	public static final String RECEIVEFLAG0 = "0";
	/** 消息发送接收标志位1：发送成功 */
	public static final String RECEIVEFLAG1 = "1";
	/** 消息发送接收标志位2：发送失败 */
	public static final String RECEIVEFLAG2 = "2";
	
	// 默认仓库名称
	public static final String IVT_NAME_CN_DEFAULT = "默认仓库";
	// 缺省仓库区分
	public static final String IVT_DEFAULTFLAG = "1";
	// 仓库编码最小长度
	public static final String IVT_CODE_LEN = "4";
	// 仓库编码类型（仓库为3）
	public static final String IVT_CODE_TYPE = "3";
	// 仓库编码前缀
	public static final String IVT_CODE_PREFIX = "IVT";
	
	// 默认截止日
	public static final int STOCKCLOSEDAY_DEFAULT = 25;
	
	// 未知节点的部门代码
	public static final String UNKNOWN_DEPARTCODE = "ZZZ";
	
	// 未知节点的部门名称
	public static final String UNKNOWN_DEPARTNAME = "未知";
	
	// 未知节点的部门类型
	public static final String UNKNOWN_DEPARTTYPE = "Z";
	
	/** 根目录 */
	public static final String CHERRY_HOME = "CHERRY_HOME";
	
	/** 日期格式 */
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	
	/** 程序名:清零处理 */
	public static final String PGM_BINBEDRHAN02 = "BINBEDRHAN02";
	
	/** 程序名:降级处理 */
	public static final String PGM_BINBEDRHAN03 = "BINBEDRHAN03";
	
	/** 程序名:积分清零处理 */
	public static final String PGM_BINBEDRHAN04 = "BINBEDRHAN04";
	
	/** 活动下发区分: 未下发 */
	public static final String ACT_SENDFLAG_0 = "0";
	
	/** 活动下发区分: 已下发 */
	public static final String ACT_SENDFLAG_1 = "1";
	
	/** 活动类型 */
	public static final String ACTIVITYTYPE_0 = "0";
	
/**============================================金蝶K3接口共通字段定义start==========================================*/
	/**发货单据  默认发货员工Code*/
	public static final String K3_DEALER_CODE = "K3-DEALER";
	/**发货单据  默认审核人Code*/
	public static final String K3_AUDIT_CODE = "K3-AUDIT";
	
	
/**============================================金蝶K3接口共通字段定义end==========================================*/

	/**JOB代号*/
	public static final String BATCH_JOB_ID = "BATCH_JOB_ID";
	
	/**JOB名称*/
	public static final String BATCH_JOB_NAME = "BATCH_JOB_NAME";
	
	/**程序名: 记录Job运行日志*/
	public static final String PGM_BATCHJOBLOG = "BATCHJOBLOG";
	
	/**程序名: Job通信判断*/
	public static final String PGM_BATCHJOBCOND = "BATCHJOBCOND";
	
	/**Job运行状态: 正常结束*/
	public static final String JOBLOG_STATUS0 = "0";
	
	/**Job运行状态: 异常结束*/
	public static final String JOBLOG_STATUS1 = "1";
	
	/**Job运行状态: 进行中*/
	public static final String JOBLOG_STATUS2 = "2";
	
	/**Job运行状态: 等待的JOB已经执行*/
	public static final String JOBLOG_STATUS9 = "9";
	
	/**JOB01 代号*/
	public static final String BATCHJOBFLOW01_CODE = "BatchJobFlow01";
	
	/**JOB01 名称*/
	public static final String BATCHJOBFLOW01_NAME = "官网会员资料数据导入";
	
	/**默认延时时间*/
	public static final int DEFAULT_SLEEP_MINUTE = 5;
	
	/**默认时间上限*/
	public static final String TIME_LIMIT = "02:30:00";
	
	/**重连DB次数*/
	public static final int DB_RECONN_TIMES = 3;
	
	/**重连DB间隔时间*/
	public static final int RECONN_SLEEP_MINUTE = 1;
	
	/**日志类型*/
	public static final String LOG_TYPE0 = "规则计算";
	
	
/**============================================沟通共通字段定义start==========================================*/
	
	/** 调度任务类型*/
	public static final String TASK_TYPE = "taskType" ;
	
	/** 调度任务类型 （沟通调度）*/
	public static final String TASK_TYPE_VALUE = "CS" ;
	
	/** 调度任务类型 （事件触发沟通延时调度）*/
	public static final String TASK_TYPE_DELAY = "DE" ;
	
	/** 调度任务类型 （进销存操作统计调度）*/
	public static final String TASK_TYPE_IS = "IS" ;
	
	/** 调度任务类型 （销售分摊）*/
	public static final String TASK_TYPE_PR = "PR" ;
	
	/** 调度任务类型 （计算员工工资）*/
	public static final String TASK_TYPE_SC = "SC" ;
	
	/** 沟通处理模块名称*/
	public static final String MODULE_NAME_GT = "沟通处理程序";
	
	// 设置替换表达式
	/** 信息模板会员卡号替换表达式*/
	public static final String MEMBER_CODE = "<#MEMBER_CODE#>" ;
	
	/** 信息模板会员姓名替换表达式*/
	public static final String MEMBER_NAME = "<#MEMBER_NAME#>" ;
	
	/** 信息模板会员密码替换表达式*/
	public static final String MEMBER_PASSWORD = "<#MEMBER_PASSWORD#>" ;
	
	/** 信息模板会员性别替换表达式*/
	public static final String MEMBER_SEX = "<#MEMBER_SEX#>" ;
	
	/** 参考会员单据领取起始时间替换表达式 */
	public static final String MEMBER_GETFROMTIME = "<#GETFROMTIME#>" ;
	
	/** 参考会员单据领取截止时间替换表达式 */
	public static final String MEMBER_GETTOTIME = "<#GETTOTIME#>" ;
	
	/** 信息模板会员生日替换表达式*/
	public static final String BIRTHDAY = "<#BIRTHDAY#>" ;
	
	/** 信息模板积分替换表达式*/
	public static final String TOTAL_POINT = "<#TOTAL_POINT#>" ;
	
	/** 信息模板可兑换积分替换表达式*/
	public static final String CHANGABLE_POINT = "<#CHANGABLE_POINT#>" ;
	
	/** 信息模板即将失效积分替换表达式*/
	public static final String CURDISABLE_POINT = "<#CURDISABLE_POINT#>" ;
	
	/** 信息模板累计购买金额替换表达式*/
	public static final String AMOUNT = "<#AMOUNT#>" ;
	
	/** 信息模板会员转柜前柜台替换表达式*/
	public static final String OLDCOUNTER_NAME = "<#OLDCOUNTER_NAME#>" ;
	
	/** 信息模板会员所属柜台替换表达式*/
	public static final String COUNTER_NAME = "<#COUNTER_NAME#>" ;
	
	/** 信息模板会员活动替换表达式*/
	public static final String ACTIVITY_NAME = "<#ACTIVITY_NAME#>" ;
	
	/** 信息模板活动开始时间替换表达式*/
	public static final String ACTIVITY_DATA_BEGIN = "<#ACTIVITY_BEGIN#>" ;
	
	/** 信息模板活动截止时间替换表达式*/
	public static final String ACTIVITY_DATA_END = "<#ACTIVITY_END#>" ;
	
	/** 信息模板活动预约开始时间替换表达式*/
	public static final String RESERVE_DATA_BEGIN = "<#RESERVE_BEGIN#>" ;
	
	/** 信息模板活动预约截止时间替换表达式*/
	public static final String RESERVE_DATA_END = "<#RESERVE_END#>" ;
	
	/** 信息模板会员活动预约柜台换表达式*/
	public static final String RECOUNTER_NAME = "<#RECOUNTER_NAME#>" ;
	
	/** 信息模板会员礼品领取柜台换表达式*/
	public static final String GETCOUNTER_NAME = "<#GETCOUNTER_NAME#>" ;
	
	/** 信息模板活动验证号替换表达式*/
	public static final String COUPON_CODE = "<#COUPON_CODE#>" ;
	
	/** 信息模板活动参与单据号替换表达式*/
	public static final String ORDER_ID = "<#ORDER_ID#>" ;
	
	/** 信息模板活动参与时间替换表达式*/
	public static final String ORDER_TIME = "<#ORDER_TIME#>" ;
	
	/** 信息模板活动使用积分替换表达式*/
	public static final String POINTS_USED = "<#POINTS_USED#>" ;
	
	/** 信息模板积分差额替换表达式*/
	public static final String POINTS_DIFF = "<#POINTS_DIFF#>" ;
	
	/** 信息模板可兑换积分截止时间替换表达式*/
	public static final String POINTS_ENDDATE = "<#POINTS_ENDDATE#>" ;
	
	/** 信息模板客户姓氏替换表达式*/
	public static final String FIRSTNAME = "<#FIRSTNAME#>" ;
	
	/** 信息模板会员等级表达式*/
	public static final String MEMBERLEVEL = "<#MEMBER_LEVEL#>" ;
	
	/** 信息模板会员下一等级替换表达式*/
	public static final String NEXTMEMBERLEVEL = "<#NEXT_MEMBERLEVEL#>" ;
	
	/** 信息模板购买时间替换表达式*/
	public static final String SALEDATE = "<#SALE_DATE#>" ;
	
	/** 信息模板积分变化时间替换表达式*/
	public static final String POINT_CHANGEDATE = "<#POINT_CHANGEDATE#>" ;
	
	/** 信息模板单笔交易金额替换表达式*/
	public static final String TRADEAMOUNT = "<#TRADE_AMOUNT#>" ;
	
	/** 信息模板单笔交易积分替换表达式*/
	public static final String SALEPOINT = "<#SALE_POINT#>" ;
	
	/** 信息模板单次积分变化值替换表达式*/
	public static final String TRADEPOINT = "<#TRADE_POINT#>" ;
	
	/** 信息模板奖励积分替换表达式*/
	public static final String AWARDPOINT = "<#AWARD_POINT#>" ;
	
	/** 信息模板升级差额购买金额替换表达式*/
	public static final String UPLEVEL_AMOUNT = "<#UPLEVEL_AMOUNT#>" ;
	
	/** 信息模板可兑换金额替换表达式*/
	public static final String AVAILABLE_AMOUNT = "<#AVAILABLE_AMOUNT#>";
	
	/** 信息模板当月替换表达式*/
	public static final String THISMONTH = "<#THISMONTH#>" ;
	
	/** 信息模板上一月替换表达式*/
	public static final String LASTMONTH = "<#LASTMONTH#>" ;
	
	/** 信息模板下一月替换表达式*/
	public static final String NEXTMONTH = "<#NEXTMONTH#>" ;
	
	/** 信息模板昨天替换表达式*/
	public static final String YESTERDAY = "<#YESTERDAY#>" ;
	
	/** 信息模板今天替换表达式*/
	public static final String TODAY = "<#TODAY#>" ;
	
	/** 信息模板明天替换表达式*/
	public static final String TOMORROW = "<#TOMORROW#>" ;
	
	/** 信息模板后x天替换表达式*/
	public static final String NEXT_X_DAY = "<#NEXT_X_DAY#>" ;
	
	/** 信息模板当月月底替换表达式*/
	public static final String THISMONTH_ENDDATE = "<#THISMONTH_LAST#>" ;
	
	/** 信息模板下一月月底替换表达式*/
	public static final String NEXTMONTH_ENDDATE = "<#NEXTMONTH_LAST#>" ;
	
	/** 信息模板后x个月月底替换表达式*/
	public static final String NEXT_X_MONTH_LASTDATE = "<#X_MONTH_LAST#>" ;
	
	/** 信息模板下一月月初替换表达式*/
	public static final String NEXTMONTH_BEGINDATE = "<#NEXTMONTH_BEGIN#>" ;
	
	/** 信息模板后x个月月初替换表达式*/
	public static final String NEXT_X_MONTH_BEGINDATE = "<#X_MONTH_BEGIN#>" ;
	
	/** 信息有效截止日期替换表达式（作用与信息失效日期相同）*/
	public static final String MSGVALID_ENDDATE = "<#MSG_VALIDENDDATE#>";
	
	/** 信息失效日期替换表达式（作用与信息有效截止日期相同）*/
	public static final String MESSAGE_INVALIDDATE = "<#MSG_INVALIDDATE#>";
	
	/** 员工号替换表达式 */
	public static final String EMPLOYEE_CODE = "<#EMPLOYEE_CODE#>" ;
	
	/** 员工姓名替换表达式 */
	public static final String EMPLOYEE_NAME = "<#EMPLOYEE_NAME#>" ;
	
	/** 登陆用户名替换表达式 */
	public static final String LONGIN_NAME = "<#LONGIN_NAME#>" ;
	
	/** 登陆密码替换表达式 */
	public static final String LONGIN_PASSWORD = "<#LONGIN_PASSWORD#>" ;
	
	/** 会员密码查询失败时回复的默认信息内容 */
	public static final String NMP_DEFAULT_MESSAGE = "您好，未能获取到您的密码信息，可能您没有设置过密码" ;
	
	/** 用户登陆密码查询失败时回复的默认信息内容 */
	public static final String NUP_DEFAULT_MESSAGE = "您好，未能获取到您的密码信息，可能您输入了错误的手机号码或者您没有将您的手机号码录入员工信息" ;
	
	////////
	/** 男性会员称呼*/
	public static final String GENDER_MAN = "先生" ;
	
	/** 女性会员称呼*/
	public static final String GENDER_WONMAN = "女士" ;
	
	/** 月*/
	public static final String STR_MONTH = "月" ;
	
	/** 日*/
	public static final String STR_DAY = "日" ;
	
	/** 沟通批次号前缀*/
	public static final String BATCHID_PREFIX = "B";
	
	/** CouponCode默认值*/
	public static final String DEFAULT_COUPON_VALUE = "0000000000" ;
	
	/** CouponCode默认长度*/
	public static final int DEFAULT_COUPON_COUNT = 10;
	
	/** 日期格式*/
	public static final String DF_DATE_PATTERN = "yyyy-MM-dd";
	
	/** 日期格式*/
	public static final String DF_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	// 设置调度执行标志
	/** 成功 */
	public static final String SCHEDULES_RUN_SUCCESS = "5";

	/** 警告 */
	public static final String SCHEDULES_RUN_WARNING = "4";

	/** 失败 */
	public static final String SCHEDULES_RUN_ERROR = "3";
	
	// 设置信息编号前缀
	/** 短信发送编号前缀 */
	public static final String BILLTYPE_SM = "SM";
	
	/** 邮件发送编号前缀 */
	public static final String BILLTYPE_EM = "EM";
	
	/////////
	/** 沟通类型（短信） */
	public static final String COMMTYPE_SMS = "1";
	
	/** 沟通类型（邮件） */
	public static final String COMMTYPE_EMAIL = "2";
	
	////////
	/** 每批次获取会员数量 */
	public static final int GETMEMBERNUMONCE = 2000;
	
	/** 生成COUPON号时在客户总数的基础上增加的数量，用于防止按相对条件搜索发送信息时新增加符合条件的客户导致数组溢出  */
	public static final int COUPONCONSTANT = 1000;
	////////
	/** 每年第一天日期值 */
	public static final String FIRSTDAYOFYEAR = "01-01";
	
	/** 每年最后一天日期值 */
	public static final String LASTDAYOFYEAR = "12-31";
	
	/** 每月第一天日期值 */
	public static final String FIRSTDAYOFMONTH = "01";
	
	/** 每天的起始时间点 */
	public static final String STARTTIMEOFDAY = "00:00:00";
	
	/** 每天的截止时间点 */
	public static final String ENDTIMEOFDAY = "23:59:59";
	
	/** 沟通计划每天允许运行的时间截止点 */
	public static final String PLANRUN_ENDTIMEOFDAY = "22:00:00";
	
	// 设置信息发送情况判断标识
	/** 信息发送情况判断标识（发送成功的对象） */
	public static final String SENDSTATUS_SUCCESS = "1";
	
	/** 信息发送情况判断标识（发送失败的对象） */
	public static final String SENDSTATUS_ERROR = "2";
	
	/** 信息发送情况判断标识（已经发送过短信，防止重复发送的对象） */
	public static final String SENDSTATUS_REPEAT = "3";
	
	/** 信息发送情况判断标识（不接收短信的对象） */
	public static final String SENDSTATUS_NOTRECEIVE = "4";	
	
	/** 信息发送情况判断标识（信息接收号码不合法的对象） */
	public static final String SENDSTATUS_CODEILLEGAL = "5";
	
	/** 信息发送情况判断标识（存在不支持的模板参数，生成信息失败） */
	public static final String SENDSTATUS_NOTCREATEMSG = "6";
	
	// 会员过滤条件标识
	/** 参考会员预约时间的情况 */
	public static final String CONDITIONTYPE_YY = "1";
	
	/** 参考会员领取礼品时间的情况 */
	public static final String CONDITIONTYPE_LQ = "2";
	
	/** 会员积分达到指定分值的情况 */
	public static final String CONDITIONTYPE_JF = "3";
	
	/** 客户单笔购买金额满指定值的情况 */
	public static final String CONDITIONTYPE_GM = "4";
	
	/** 指定时间范围内购买次数达到指定次数的情况 */
	public static final String CONDITIONTYPE_CS = "5";
	
	/** 活动参与人数达到指定数量的情况 */
	public static final String CONDITIONTYPE_RS = "6";
	
	/** 参考会员单据领取起始时间的情况 */
	public static final String CONDITIONTYPE_FT = "13";
	
	/** 参考会员单据领取截止时间的情况 */
	public static final String CONDITIONTYPE_TT = "14";
	
	// 设置调度执行状态标识
	/** 执行成功 */
	public static final String RUNFLAG_SUCCESS = "0";
	
	/** 沟通执行时间达到 */
	public static final String RUNFLAG_ISRUNTIME = "1";
	
	/** 指定的沟通日期没有达到 */
	public static final String RUNFLAG_NOTSENDDATE = "2";
	
	/** 未到发送时间点或时间格式不正确 */
	public static final String RUNFLAG_NOTSENDTIME = "3";
	
	/** 没有取到沟通表信息 */
	public static final String RUNFLAG_NOCONFIG = "4";
	
	/** 不支持的时间类型 */
	public static final String RUNFLAG_ERRORTYPE = "5";
	
	/** 不支持的执行频率 */
	public static final String RUNFLAG_FREQUENCYERROR = "6";
	
	/** 不在指定的沟通日期范围 */
	public static final String RUNFLAG_NOTSENDLIMIT = "7";
	
	/** 沟通时间参考了活动时间，但活动时间不存在 */
	public static final String RUNFLAG_NOACTIVITYDATE = "8";
	
	/** 获取沟通时间时的系统错误 */
	public static final String RUNFLAG_TIMESYSERROR = "9";
	
	/** 系统错误 */
	public static final String RUNFLAG_SYSERROR = "10";
	
	/** 调度任务不存在 */
	public static final String RUNFLAG_NOTASK = "11";
	
	/** 活动已停用或删除 */
	public static final String RUNFLAG_ACTNOTSTART = "12";
	
	/** 沟通设置为空 */
	public static final String RUNFLAG_COMMSETNULL = "13";
	
	/** 沟通内容为空 */
	public static final String RUNFLAG_NOMESSAGE = "14";
	
	/** 按条件触发的沟通，无效的触发条件 */
	public static final String RUNFLAG_NOCONDITION = "15";
	
	/** 参考会员活动相关时间的沟通，沟通计划未关联沟通活动 */
	public static final String RUNFLAG_NOACTIVITY = "16";
	
	/** 没有取到搜索记录 */
	public static final String RUNFLAG_NOSEARCHINFO = "17";
	
	/** 没有取到沟通对象 */
	public static final String RUNFLAG_NOCOMMOBJ = "18";
	
	/** 暂时不支持的条件 */
	public static final String RUNFLAG_NOTSUPPORT = "19";
	
	/** 发送沟通信息时的系统错误 */
	public static final String RUNFLAG_SENDSYSERROR = "20";
	
	/** 活动参与人数未达到指定人数 */
	public static final String RUNFLAG_NOTNUMOFORDER = "21";
	
	/** 与活动相关的沟通，没有取到活动信息 */
	public static final String RUNFLAG_NOACTIVITYINFO = "22";
	
	/** 参考时间情况下获取客户信息的时间条件超出沟通计划运行时间范围 */
	public static final String RUNFLAG_OUTRUNDATERANGE = "23";
	
	/** 事件类型不存在 */
	public static final String RUNFLAG_NOEVENTTYPE = "30";
	
	/** 沟通计划部分运行失败 */
	public static final String RUNFLAG_RUNWARNING = "99";
	
	/** 运行失败 */
	public static final String RUNFLAG_RUNERROR = "100";
	
	/** 百家姓  复姓 */
	public static final String DOUBLEFIRSTNAME = "万俟,司马,上官,欧阳,夏侯,诸葛,闻人,东方,赫连,皇甫,尉迟,公羊,澹台,公冶,宗政,濮阳,淳于,单于,太叔,申屠,公孙,仲孙,轩辕," +
			"令狐,锺离,宇文,长孙,慕容,鲜于,闾丘,司徒,司空,丌官,司寇,子车,颛孙,端木,巫马,公西,漆雕,乐正,壤驷,公良,拓拔,夹谷,宰父,谷梁,段干,百里,东郭,南门,呼延,羊舌,微生,梁丘,左丘,东门,西门,南宫";
	
	// 设置事件触发沟通执行状态标识
	/** 事件执行成功 */
	public static final String EXECFLAG_SUCCESS = "0";
	
	/** 事件执行警告*/
	public static final String EXECFLAG_WARNING = "1";
	
	/** 事件执行失败*/
	public static final String EXECFLAG_ERROR = "2";
	
	/** 所有短信发送失败*/
	public static final String EXECFLAG_ALLMSGSENDFAILED = "3";
	
	/** 发送对象为空*/
	public static final String EXECFLAG_CUSTOMERNULL = "9";
	
	/** 触发事件设置信息为空*/
	public static final String EXECFLAG_EVENTSETNULL = "10";
	
	/** 积分变化事件默认ID*/
	public static final String POINTCHANGEEVENTID = "7";
	
	/** 销售导致的积分变化事件类型 */
	public static final String POINTEVENTTYPE_NS = "71" ;
	
	/** 退货导致的积分变化事件类型 */
	public static final String POINTEVENTTYPE_SR = "72" ;
	
	/** 积分兑换导致的积分变化事件类型 */
	public static final String POINTEVENTTYPE_PX = "73" ;
	
	/** 活动预约导致的积分变化事件类型 */
	public static final String POINTEVENTTYPE_PB = "74" ;
	
	/** 积分清零导致的积分变化事件类型 */
	public static final String POINTEVENTTYPE_PC = "75" ;
	
	/** 积分维护导致的积分变化事件类型 */
	public static final String POINTEVENTTYPE_PT = "76" ;
	
	/** 积分增加事件类型 */
	public static final String POINTEVENTTYPE_ZJ = "77" ;
	
	/** 积分减少事件类型 */
	public static final String POINTEVENTTYPE_JS = "78" ;
	
	// 短信接口参数代号
	/** 短信接口类型 */
	public static final String SMSIF_TYPE = "SMSIF_TYPE";
	
	/** 接口开放功能 */
	public static final String SMSIF_ENABLE = "SMSIF_ENABLE";
	
	/** 接口服务地址 */
	public static final String SMSIF_URL = "SMSIF_URL";
	
	/** 网关端口 */
	public static final String SMSIF_PORT = "SMSIF_PORT";
	
	/** 短信发送接口 */
	public static final String SMSIF_SEND = "SMSIF_SEND";
	
	/** 短信接收接口 */
	public static final String SMSIF_RECEIVE = "SMSIF_RECEIVE";
	
	/** 查询短信发送状态报告*/
	public static final String SMSIF_QUERYSENDRPT = "SMSIF_QUERYSENDRPT";
	
	/** 用户信息 */
	public static final String SMSIF_USERINFO = "SMSIF_USERINFO";
	
	/** 接收短信状态报告 */
	public static final String SMSIF_RECEIVERPT = "SMSIF_RECEIVERPT";
	
	/** 查询余额 */
	public static final String SMSIF_QUERYBALANCE = "SMSIF_QUERYBALANCE";
	
	/** 更新密码 */
	public static final String SMSIF_UPDATEPWD = "SMSIF_UPDATEPWD";
	
	/** 通道类型 */
	public static final String SMSIF_ACCOUNTTYPE = "SMSIF_ACCOUNTTYPE";
	
	/** 通道信息 */
	public static final String SMSIF_ACCOUNTINFO = "SMSIF_ACCOUNTINFO";
	
	/** 用户账号 */
	public static final String SMSPARAM_USERID = "SMSPARAM_USERID";
	
	/** 用户密码 */
	public static final String SMSPARAM_PASSWORD = "SMSPARAM_PASSWORD";
	
	/** 批次号 */
	public static final String SMSPARAM_BATCHID = "SMSPARAM_BATCHID";
	
	/** 发送时间 */
	public static final String SMSPARAM_SENDTIME = "SMSPARAM_SENDTIME";
	
	/** 接收号码*/
	public static final String SMSPARAM_RECEIVECODE = "SMSPARAM_RECEIVECODE";
	
	/** 信息内容 */
	public static final String SMSPARAM_MESSAGE = "SMSPARAM_MESSAGE";
	
	/** 发送号码数量 */
	public static final String SMSPARAM_NUMCOUNT = "SMSPARAM_NUMCOUNT";
	
	/** 其它参数 */
	public static final String SMSPARAM_OTHER = "SMSPARAM_OTHER";
	
	/** 任务类型 */
	public static final String SMSPARAM_TASKTYPE = "SMSPARAM_TASKTYPE";
	
	/** 公司ID */
	public static final String SMSPARAM_COMPANYID = "SMSPARAM_COMPANYID";
	
	/** 接口类型（HTTP接口） */
	public static final String SMSIF_TYPE_HTTP = "HTTP";
	
	/** 接口类型（WebService接口） */
	public static final String SMSIF_TYPE_WEBSERVICE = "WEBSERVICE";
	
	/** 接口开放功能（全部禁止） */
	public static final String SMSIF_ENABLE_ALLCLOSE = "0";
	
	/** 接口开放功能（可发送可接收） */
	public static final String SMSIF_ENABLE_ALLOPEN = "1";
	
	/** 接口开放功能（仅发送） */
	public static final String SMSIF_ENABLE_ONLYSEND = "2";
	
	/** 接口开放功能（仅接收） */
	public static final String SMSIF_ENABLE_ONLYRECEIVE = "3";
	
	// 接口调用状态标识
	
	/** 重新发送次数 */
	public static final int RETRY_TIMES = 3;
	
	/** 重新发送间隔时间(毫秒) */
	public static final int RETRY_SLEEP_TIME = 300;
	
	/** 接口调用状态（短信发送成功） */
	public static final String IFFLAG_SUCCESS = "0";
	
	/** 接口调用状态（接口调用系统错误） */
	public static final String IFFLAG_SYSERROR = "8000";
	
	/** 接口调用状态（接口配置为禁止发送禁止接收） */
	public static final String IFFLAG_CONFIGDISABLED = "8001";
	
	/** 接口调用状态（接口可用配置不存在或错误） */
	public static final String IFFLAG_CONFIGERROR = "8002";
	
	/** 接口调用状态（未获取到服务地址配置） */
	public static final String IFFLAG_NOIFSERVICEURL = "8003";
	
	/** 接口调用状态（接口地址不完整或者不支持的接口调用） */
	public static final String IFFLAG_SERVICEURLINCOMPLETE = "8004";
	
	/** 接口调用状态（接口类型不支持） */
	public static final String IFFLAG_IFTYPEERROR = "8005";
	
	/** 接口调用状态（接口供应商不支持） */
	public static final String IFFLAG_UNSUPPORTEDSUPPLIER = "8006";
	
	/** 接口调用状态（没有获取到供应商返回的结果） */
	public static final String IFFLAG_NOTGETRETURNMSG = "8007";
	
	/** 接口调用状态（供应商发送短信失败） */
	public static final String IFFLAG_IFSENDMSGERROR = "8008";
	
	/** 接口调用状态（设置接口参数出现异常） */
	public static final String IFFLAG_SETPARAMERROR = "8009";
	
	/** 接口调用状态（未能获取到信息接收号码） */
	public static final String IFFLAG_NOTGETRECEIVERCODE = "8010";
	
	/** 接口调用状态（信息发送参数为空） */
	public static final String IFFLAG_NOTGETSENDPARAM = "8011";
	
	/** 接口调用状态（配置参数为空） */
	public static final String IFFLAG_NOTGETCONFIGPARAM = "8012";
	
	/** 接口调用状态（供应商呼出失败） */
	public static final String IFFLAG_IFCALLERROR = "9001";
	
	/** 接口调用状态（发生运行时异常） */
	public static final String IFFLAG_IFCALLEXCEPTION = "9002";
	
	
	/** 铭传第三方接口系统代号 */
	public static final String MCIF_SYSTEMCODE = "MC";
	
	/** 梦网科技第三方接口系统代号 */
	public static final String MWIF_SYSTEMCODE = "MW";
	
	/** 梦网科技（松仁电讯）新接口系统代号 */
	public static final String MNIF_SYSTEMCODE = "MN";
	
	/** 首易接口系统代号 */
	public static final String SYIF_SYSTEMCODE = "SY";
	
	/** WE短信第三方接口系统代号 */
	public static final String WEIF_SYSTEMCODE = "WE";
	
	/** MXT短信第三方接口系统代号 */
	public static final String MXTIF_SYSTEMCODE = "MXT";
	
	/** 阿里大鱼短信第三方接口系统代号 */
	public static final String ALIDAYU_SYSTEMCODE = "ALDY";
	
	/** 亿美软通第三方接口系统代号*/
	public static final String EMAY_SYSTEMCODE = "EM";
	
	/** 是否发送的为长短信 */
	public static final String SMSPARAM_LongSms = "SMSPARAM_LongSms";
	
	/** 扩展号码 */
	public static final String SMSPARAM_AddNum = "SMSPARAM_AddNum";
	
	/** 天润电话接口系统代号 */
	public static final String TRIF_SYSTEMCODE = "TR";
	
	/** 电话接口类型 */
	public static final String PHONEIF_TYPE = "PHONEIF_TYPE";
	
	/** 接口开放功能 */
	public static final String PHONEIF_ENABLE = "PHONEIF_ENABLE";
	
	/** 接口服务地址 */
	public static final String PHONEIF_CALLURL = "PHONEIF_CALLURL";
	
	/** 网关端口 */
	public static final String PHONEIF_PORT = "PHONEIF_PORT";
	
	/** 电话呼出接口 */
	public static final String PHONEIF_CALL = "PHONEIF_CALL";
	
	/** 用户账号 */
	public static final String PHONEPARAM_USERID = "PHONEPARAM_USERID";
	
	/** 用户密码 */
	public static final String PHONEPARAM_PASSWORD = "PHONEPARAM_PASSWORD";
	
	/** 批次号 */
	public static final String PHONEPARAM_BATCHID = "PHONEPARAM_BATCHID";
	
	/** 发送时间 */
	public static final String PHONEPARAM_SENDTIME = "PHONEPARAM_SENDTIME";
	
	/** 接收号码*/
	public static final String PHONEPARAM_PHONE = "PHONEPARAM_PHONE";
	
	/** 信息内容 */
	public static final String PHONEPARAM_MESSAGE = "PHONEPARAM_MESSAGE";
	
	/** 发送号码数量 */
	public static final String PHONEPARAM_NUMCOUNT = "PHONEPARAM_NUMCOUNT";
	
	/** 企业ID */
	public static final String PHONEPARAM_ENPID = "PHONEPARAM_ENPID";
	
	/** 接口类型参数 */
	public static final String PHONEPARAM_IFTYPE = "PHONEPARAM_IFTYPE";
	
	/** 参数名 */
	public static final String PHONEPARAM_PARAMNAME = "PHONEPARAM_PARAMNAME";
	
	/** 参数类型 */
	public static final String PHONEPARAM_PARAMTYPE = "PHONEPARAM_PARAMTYPE";
	
	
	public static final String PHONEPARAM_IVRID = "PHONEPARAM_IVRID";
	
	
	public static final String PHONEPARAM_SYNC = "PHONEPARAM_SYNC";
	
	/** 实时触发事件Coupon号生成默认活动编号 */
	public static final String EVENT_GETCOUPON_CAMP = "GETVERIFICATIONCOUPON";
	
	/** 事件触发生成Coupon号时防止数组溢出的保护值 */
	public static final int EVENT_GETCOUPON_CONSTANT = 10;
	
/**============================================沟通共通字段定义End==========================================*/

	/**============================================利润分摊Start==========================================*/
	public static final int BATCH_ERROR_PR1 = 1;
	public static final int BATCH_ERROR_PR2 = 2;
	public static final int BATCH_ERROR_PR3 = 3;
	public static final int BATCH_ERROR_PR4 = 4;
}
