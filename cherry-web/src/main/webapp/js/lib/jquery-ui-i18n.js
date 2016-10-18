﻿jQuery(function($){
	$.datepicker.regional['zh-CN'] = {
			closeText: '关闭',
			prevText: '&#x3c;上月',
			nextText: '下月&#x3e;',
			currentText: '今天',
			monthNames: ['一月','二月','三月','四月','五月','六月',
			'七月','八月','九月','十月','十一月','十二月'],
			monthNamesShort: ['1','2','3','4','5','6',
			'7','8','9','10','11','12'],
			dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
			dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
			dayNamesMin: ['日','一','二','三','四','五','六'],
			isRTL: false};
	$.datepicker.regional['zh-TW'] = {
			closeText: '關閉',
			prevText: '&#x3c;上月',
			nextText: '下月&#x3e;',
			currentText: '今天',
			monthNames: ['一月','二月','三月','四月','五月','六月',
			'七月','八月','九月','十月','十一月','十二月'],
			monthNamesShort: ['1','2','3','4','5','6',
			'7','8','9','10','11','12'],
			dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
			dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
			dayNamesMin: ['日','一','二','三','四','五','六'],
			isRTL: false};
	var language = $('#CHERRY_LANGUAGE').text();
	if(language == "zh_TW") {
		$.datepicker.setDefaults($.datepicker.regional['zh-TW']);
	} else {
		$.datepicker.setDefaults($.datepicker.regional['zh-CN']);
	}
});
