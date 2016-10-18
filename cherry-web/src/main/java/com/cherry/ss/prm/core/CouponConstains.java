package com.cherry.ss.prm.core;

public class CouponConstains {
	
	/** 批处理一页最大数 */
	public final static int BATCH_PAGE_MAX_NUM = 10000;
	
	/** 优惠券的类型 */
	public final static String COUPON_TYPE = "CP";
	
	/** 优惠券的BATCH批次 */
	public final static String COUPON_TYPE_BATCH = "CB";
	
	/** 代金券 */
	public final static String COUPON_DJQ = "1";
	
	/** 代金券虚拟商品 */
	public final static String DJQ_NAME = "代金券虚拟商品";
	
	/** 折扣券虚拟商品 */
	public final static String ZKQ_NAME = "折扣券虚拟商品";
	
	/** 代物券虚拟商品 */
	public final static String DWQ_NAME = "代物券虚拟商品";
	
	/** 资格券虚拟商品 */
	public final static String ZGQ_NAME = "资格券虚拟商品";
	
	/** 生成方式：生成固定数的券 */
	public final static String BATCHMODE_1 = "1";
	
	/** 生成方式：生成会员对应的券 */
	public final static String BATCHMODE_2 = "2";
	
	/** 是否需要生成校验码：需要生成校验码 */
	public final static String VALIDMODE_1 = "1";
	
	/** 可否赠送：可以 */
	public final static String ISGIVE_1 = "1";
	
	/** 错误码：生成券号失败 */
	public final static int RESULT_COUPON_NO_ERR = -1;
	
	/** 错误码：生成券码失败 */
	public final static int RESULT_COUPON_PSW_ERR = -2;
	
	/** 错误码：保存券号失败 */
	public final static int RESULT_COUPON_ADD_ERR = -3;
	
	/** 错误信息：生成券号失败 */
	public final static String RESULT_COUPON_NO_ERRMSG = "生成券号失败";
	
	/** 错误信息：生成券码失败 */
	public final static String RESULT_COUPON_PSW_ERRMSG = "生成券码失败";
	
	/** 错误信息：保存券号失败 */
	public final static String RESULT_COUPON_ADD_ERRMSG = "保存券号失败";
	
	/** 错误信息：券号和券码数量不匹配 */
	public final static String RESULT_NUM_NOMATCH = "券号和券码数量不匹配";
	
	/** 接口错误提示：参数不正确 */
	public final static String IF_ERROR_PARAM = "参数不正确";
	
	public final static String IF_ERROR_PARAM_CODE = "WSESP0101";
	
	/** 接口错误提示：获取不到品牌信息 */
	public final static String IF_ERROR_NO_BRAND = "获取不到品牌信息";
	
	public final static String IF_ERROR_NO_BRAND_CODE = "WSESP0102";
	
	/** 接口错误提示：卡号和手机号码都为空 */
	public final static String IF_ERROR_NO_CARDANDPHONE = "卡号和手机号码都为空";
	
	public final static String IF_ERROR_NO_CARDANDPHONE_CODE = "WSESP0103";
	
	/** 接口错误提示：没有上传优惠券信息 */
	public final static String IF_ERROR_NO_COUPON = "没有上传优惠券信息";
	
	public final static String IF_ERROR_NO_COUPON_CODE = "WSESP0104";
	
	/** 接口错误提示：该优惠券不存在 */
	public final static String IF_ERROR_NOTEXIST_COUPON = "该优惠券不存在，请重新输入";
	
	public final static String IF_ERROR_NOTEXIST_COUPON_CODE = "WSESP0105";
	
	/** 接口错误提示：优惠券密码不正确 */
	public final static String IF_ERROR_PASSWORD = "优惠券密码不正确，请重新输入";
	
	public final static String IF_ERROR_PASSWORD_CODE = "WSESP0106";
	
	/** 接口错误提示：非本人使用该券 */
	public final static String IF_ERROR_MEMMATCH = "该电子优惠券仅会员本人可使用，请先了解活动详情";
	
	public final static String IF_ERROR_MEMMATCH_CODE = "WSESP0107";
	
	/** 接口错误提示：优惠券活动不存在 */
	public final static String IF_ERROR_RULE = "优惠券活动不存在";
	
	public final static String IF_ERROR_RULE_CODE = "WSESP0108";
	
	/** 接口错误提示：优惠券活动内容无法获取 */
	public final static String IF_ERROR_CONTENT = "优惠券活动内容无法获取";
	
	public final static String IF_ERROR_CONTENT_CODE = "WSESP0109";
	
	/** 接口错误提示：接口发生异常 */
	public final static String IF_ERROR_EXCEPTION = "接口发生异常";
	
	public final static String IF_ERROR_EXCEPTION_CODE = "WSESP0110";
	
	/** 接口错误提示：交易日期为空 */
	public final static String IF_ERROR_SALEDATE = "交易日期为空";
	
	public final static String IF_ERROR_SALEDATE_CODE = "WSESP0111";
	
	/** 接口错误提示： 该券还没到开始日期 */
	public final static String IF_ERROR_STARTTIME = "该券还没到开始日期，请先了解活动详情";
	
	public final static String IF_ERROR_STARTTIME_CODE = "WSESP0112";
	
	/** 接口错误提示： 该券已过期 */
	public final static String IF_ERROR_ENDTTIME = "该券已过期";
	
	public final static String IF_ERROR_ENDTTIME_CODE = "WSESP0113";
	
	/** 接口错误提示： 该券不能使用 */
	public final static String IF_ERROR_STATUS = "该券不能使用";
	
	public final static String IF_ERROR_STATUS_CODE = "WSESP0114";
	
	/** 接口错误提示： 订单中没有折扣券对应的产品 */
	public final static String IF_ERROR_DPZK = "订单中没有折扣券对应的产品，请先了解活动详情";
	
	public final static String IF_ERROR_DPZK_CODE = "WSESP0115";
	
	/** 接口错误提示： 不符合门店条件 */
	public final static String IF_ERROR_COUNTER = "门店不在本次活动的参与范围，请先了解活动详情";
	
	public final static String IF_ERROR_COUNTER_CODE = "WSESP0116";
	
	/** 接口错误提示： 不符合产品条件 */
	public final static String IF_ERROR_PRT = "您购物车中的产品不在本次优惠券活动所使用的产品范围内，请重新添加产品";
	
	public final static String IF_ERROR_PRT_CODE = "WSESP0117";
	
	/** 接口错误提示： 不符合金额条件 */
	public final static String IF_ERROR_AMOUNT = "您的消费金额不符合该电子优惠券的金额要求，请先了解活动详情";
	
	public final static String IF_ERROR_AMOUNT_CODE = "WSESP0118";
	
	/** 接口错误提示： 不符合会员条件 */
	public final static String IF_ERROR_MEMBER = "您不符合当前电子优惠券设定的会员要求，请先了解活动详情";
	
	public final static String IF_ERROR_MEMBER_CODE = "WSESP0119";
	
	/** 接口错误提示： 不符合须参加的条件 */
	public final static String IF_ERROR_ACT = "您不符合须参加的条件，请先了解活动详情";
	
	public final static String IF_ERROR_ACT_CODE = "WSESP0120";
	
	/** 接口错误提示： 该券不能和其它券一起使用 */
	public final static String IF_ERROR_OTHER = "该券不能和其它类型券一起使用，请先了解活动详情";
	
	public final static String IF_ERROR_OTHER_CODE = "WSESP0121";
	
	/** 接口错误提示： 未选择资格券对应的活动 */
	public final static String IF_ERROR_ZGQ = "未选择资格券对应的活动，请先了解活动详情";
	
	public final static String IF_ERROR_ZGQ_CODE = "WSESP0122";
	
	/** 接口错误提示： 获取代物券对应的产品失败 */
	public final static String IF_ERROR_TYPE_DWQ = "获取代物券对应的产品失败";
	
	public final static String IF_ERROR_TYPE_DWQ_CODE = "WSESP0123";
	
	/** 接口错误提示：没有上传优惠券信息 */
	public final static String IF_ERROR_NO_RULE = "没有选择券活动";
	
	public final static String IF_ERROR_NO_RULE_CODE = "WSESP0124";
	
	/** 接口错误提示：券活动验证未通过 */
	public final static String IF_ERROR_SEND_COUPON = "券活动验证未通过";
	
	public final static String IF_ERROR_SEND_COUPON_CODE = "WSESP0125";
	
	/** 接口错误提示：发券失败 */
	public final static String IF_ERROR_SEND_ERROR = "发券失败";
	
	public final static String IF_ERROR_SEND_ERROR_CODE = "WSESP0126";
	
	/** 接口错误提示：券的使用场景不正确 */
	public final static String IF_ERROR_COUPON_ERROR = "券的使用场景不正确，请于代物券页面使用";
	
	public final static String IF_ERROR_COUPON_ERROR_CODE = "WSESP0127";
	
	/** 接口错误提示： 该券已使用，您不能重复使用 */
	public final static String IF_ERROR_STATUS_OK = "该券已使用，您不能重复使用";
	
	public final static String IF_ERROR_STATUS_OK_CODE = "WSESP0128";
	
	/** 接口错误提示： 该券已废弃，您不能使用 */
	public final static String IF_ERROR_STATUS_CA = "该券已废弃，您不能使用";
	
	public final static String IF_ERROR_STATUS_CA_CODE = "WSESP0129";
	
	/** 接口错误提示： 该券状态异常，您不能使用 */
	public final static String IF_ERROR_STATUS_OTHER = "该券状态异常，您不能使用";
	
	public final static String IF_ERROR_STATUS_OTHER_CODE = "WSESP0130";
	
	/** 接口错误提示： 同类型的券只能使用一张 */
	public final static String IF_ERROR_OTHER_SAME = "同类型的券只能使用一张";
	
	public final static String IF_ERROR_OTHER_SAME_CODE = "WSESP0131";
	
	/** 接口错误提示： 短信发送发生异常 */
	public final static String IF_ERROR_SMS_SAME = "短信发送发生异常";
	
	public final static String IF_ERROR_SMS_CODE = "WSESP0132";
	
	/** 接口错误提示： 不在券活动的执行期内 */
	public final static String IF_ERROR_CREATE_DATE = "不在券活动的执行期内，活动：";
	
	public final static String IF_ERROR_CREATE_DATE_CODE = "WSESP0133";
	
	/** 接口错误提示： 不在券活动的执行期内 */
	public final static String IF_ERROR_CREATE_AMOUNT = "所选券活动的金额条件之和超过了本单金额，请重新选择";
	
	public final static String IF_ERROR_CREATE_AMOUNT_CODE = "WSESP0134";
	
	/** 接口错误提示： 不符合产品条件 */
	public final static String IF_ERROR_CREATE_PRT = "不满足产品条件，活动：";
	
	public final static String IF_ERROR_CREATE_PRT_CODE = "WSESP0135";
	
	/** 接口错误提示： 超过发券数量上限 */
	public final static String IF_ERROR_MAX_NUM = "超过发券数量上限，活动：";
	
	public final static String IF_ERROR_MAX_NUM_CODE = "WSESP0136";
	
	/** 接口错误提示： 超过单个会员发券数量上限 */
	public final static String IF_ERROR_MEM_MAX_NUM = "超过单个会员发券数量上限，活动：";
	
	public final static String IF_ERROR_MEM_MAX_NUM_CODE = "WSESP0137";
	
	/** 券使用期限：指定日期 */
	public final static String USETIMETYPE_0 = "0";
	
	/** 有效期单位：天 */
	public final static String VALIDITYUNIT_0 = "0";
	
	/** 券状态：未领用 */
	public final static String STATUS_AR = "AR";
	
	/** 券状态：已使用 */
	public final static String STATUS_OK = "OK";
	
	/** 券状态：取消（废弃） */
	public final static String STATUS_CA = "CA";
	
	/** 是否需要密码：不需要 */
	public final static String PASSWORDFLAG_0 = "0";
	
	/** 是否需要密码：需要 */
	public final static String PASSWORDFLAG_1 = "1";
	
	/** 是否选中：不选中 */
	public final static String CHECKFLAG_0 = "0";
	
	/** 是否选中：选中 */
	public final static String CHECKFLAG_1 = "1";
	
	/** 接口返回结果：成功 */
	public final static String RESULT_SUCCESS = "0";
	
	/** 接口返回结果：失败 */
	public final static String RESULT_FAIL = "-1";
	
	/** 接口返回结果：成功 */
	public final static int RESULTCODE_SUCCESS = 0;
	
	/** 接口返回结果：失败 */
	public final static int RESULTCODE_FAIL = -1;
	
	/** 门店导入sheet名 */
	public final static String COUNTER_SHEET_NAME = "门店数据";
	
	/** 会员导入sheet名 */
	public final static String MEMBER_SHEET_NAME = "会员数据";
	
	/** 单次导入上限 */
	public final static int UPLOAD_MAX_COUNT = 20000;
	
	/** 错误提示最多行数 */
	public final static int ERRORMSG_MAX_NUM = 10;
	
	/** 购买门店：导入 */
	public final static String COUNTERKBN_1 = "1";
	
	/** 发券对象：导入 */
	public final static String MEMBERKBN_1 = "1";
	
	/** 导入模式：新增 */
	public final static String UPMODE_1 = "1";
	
	/** 导入模式：覆盖 */
	public final static String UPMODE_2 = "2";
	
	/** 条件类型：发送门槛 */
	public final static String CONDITIONTYPE_1 = "1";
	
	/** 条件类型：使用门槛 */
	public final static String CONDITIONTYPE_2 = "2";
	
	/** 券类型：代金券 */
	public final static String COUPONTYPE_1 = "1";
	/** 券类型：代物券 */
	public final static String COUPONTYPE_2 = "2";
	/** 券类型：资格券 */
	public final static String COUPONTYPE_3 = "3";
	/** 券类型：折扣券 */
	public final static String COUPONTYPE_5 = "5";
	
	/** 接口类型 ：查询*/
	public final static int IF_TYPE_1 = 1;
	
	/**  */
	public static enum ERRMSG {
		errmsg1(RESULT_COUPON_NO_ERR, RESULT_COUPON_NO_ERRMSG),
		errmsg2(RESULT_COUPON_PSW_ERR, RESULT_COUPON_PSW_ERRMSG);
		
		public static String getMessage(int key) {
			return getValue(key);
		}
		private static String getValue(int key) {
			ERRMSG[] opers = ERRMSG.values();
			for(ERRMSG oper : opers) {
				if(oper.getKey() == key) {
					return oper.getContent();
				}
			}
			return null;
		}
		ERRMSG(int key, String content) {
			this.key = key;
			this.content = content;
		}
		private int getKey() {
			return key;
		}
		private String getContent() {
			return content;
		}
		private int key;
		private String content;
	}
	

}
