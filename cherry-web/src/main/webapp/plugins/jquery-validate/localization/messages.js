jQuery(function($){
	var language = $('#CHERRY_LANGUAGE').text();
	if(language == "zh_TW") {
		jQuery.extend(jQuery.validator.messages, {
	        required: "請輸入該值",
			remote: "請修正該字段",
			email: "請輸入正確格式的電子郵件",
			url: "請輸入合法的網址",
			date: "請輸入合法的日期",
			dateISO: "請輸入合法的日期(ISO).",
			number: "請輸入合法的數字",
			digits: "只能輸入整數",
			creditcard: "請輸入合法的信用卡號",
			equalTo: "請再次輸入相同的值",
			accept: "請輸入擁有合法後綴名的字符串",
			zipValid: "請輸入正確的郵編",
			dateValid: "請輸入正確的日期",
			alphanumeric: "請輸入英數字",
			timeValid: "請輸入正確的時間",
			maxlength: jQuery.validator.format("請輸入一個長度最多是 {0} 的字符串"),
			minlength: jQuery.validator.format("請輸入一個長度最少是 {0} 的字符串"),
			rangelength: jQuery.validator.format("請輸入一個長度介於 {0} 和 {1} 之間的字符串"),
			range: jQuery.validator.format("請輸入一個介於 {0} 和 {1} 之間的值"),
			max: jQuery.validator.format("請輸入一個最大為 {0} 的值"),
			min: jQuery.validator.format("請輸入一個最小為 {0} 的值"),
			floatValid: jQuery.validator.format("請輸入浮點數,整數不能超過{0}位，小數不能超過{1}位"),
			dateYYYYmm: jQuery.validator.format("請輸入正確的日期"),
			byteLengthValid: jQuery.validator.format("請輸入字節數不超過{0}個的字符串(1個漢字占兩個字節)"),
			prmCodeValid: "請輸入英數字、點號、橫杠或下劃綫",
			validIntNum:"請輸入整數",
			numberValid:"請輸入合法的數字",
			integerValid:"請輸入非負整數",
			rangeValid:"請輸入一個介於 {0}和 {1}之間的值",
			pointValid:"請輸入一個正數,整數不能超過{0}位，小數不能超過{1}位"
		});
	} else {
		jQuery.extend(jQuery.validator.messages, {
	        required: "请输入该值",
			remote: "请修正该字段",
			email: "请输入正确格式的电子邮件",
			url: "请输入合法的网址",
			date: "请输入合法的日期",
			dateISO: "请输入合法的日期 (ISO).",
			number: "请输入合法的数字",
			digits: "只能输入整数",
			creditcard: "请输入合法的信用卡号",
			equalTo: "请再次输入相同的值",
			accept: "请输入拥有合法后缀名的字符串",
			zipValid: "请输入正确的邮编",
			dateValid: "请输入正确的日期",
			alphanumeric: "请输入英数字",
			timeValid: "请输入正确的时间",
			maxlength: jQuery.validator.format("请输入一个长度最多是 {0} 的字符串"),
			minlength: jQuery.validator.format("请输入一个长度最少是 {0} 的字符串"),
			rangelength: jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
			range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
			max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
			min: jQuery.validator.format("请输入一个最小为 {0} 的值"),
			floatValid: jQuery.validator.format("请输入浮点数,整数不能超过{0}位，小数不能超过{1}位"),
			dateYYYYmm: jQuery.validator.format("请输入正确的日期"),
			byteLengthValid: jQuery.validator.format("请输入字节数不超过{0}个的字符串(1个汉字占两个字节)"),
			prmCodeValid: "请输入英数字、点号、横杠或下划线",
			validIntNum:"请输入整数",
			numberValid:"请输入合法的数字",
			integerValid:"请输入非负整数",
			rangeValid:"请输入一个介于 {0}和 {1}之间的值",
			pointValid:"请输入一个正数,整数不能超过{0}位，小数不能超过{1}位"
		});
	}
});