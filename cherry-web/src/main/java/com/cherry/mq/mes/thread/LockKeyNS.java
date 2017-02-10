package com.cherry.mq.mes.thread;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.interfaces.LockKey_IF;
import java.util.Map;

public class LockKeyNS implements LockKey_IF {

	@Override
	public String generateLockKey(Map<String, Object> map) {
		// 品牌Code
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 会员卡号
		String memberCode = ConvertUtil.getString(map.get("memberCode"));
		// 如果会员卡号为"000000000"的表示为非会员直接返回空 或者为空字符串返回空
		// 如果卡号为""P5910302663""的浓妆淡抹的临时卡号会员 也返回空
		if (CherryConstants.UNMEMBERCODE.equals(memberCode)||
				CherryConstants.TEMP_MEMBERCODE.equals(memberCode)||
				"".equals(memberCode)){
			return null;
		}else {
			return brandCode + "_" + memberCode;
		}
	}
}
