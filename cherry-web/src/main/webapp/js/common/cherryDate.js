/*
 * @(#)CherryDate.js     1.0 2010/11/03
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

(function($) {
	
	/* 
	 * 日历初始化函数
	 * 
	 * Inputs:  Object:options 			日历初始化时的属性参数
	 * 			options.holidayObj		假日对象数组（储存假日日期和假日名称的JSON对象）例如：[{date:2010-05-01,holidayName:'劳动节'},{date:2010-10-01,holidayName:'国庆节'}]
	 * 			options.language 		设置日历表示语言
	 * 			options.dateFormat 		设置日期格式  
	 * 			options.firstDay		设置周的第一天
	 *			options.minDate			设置最小日期
	 *			options.maxDate			设置最大日期
	 *			options.yearRange		设置年份范围	
	 *			options.onSelectEvent   选择日历回调函数	
	 *			options.beforeShow      日期生成开始前被调用的函数
	 * 
	 */
	$.fn.cherryDate = function(options) {
		var setting = {
			holidayObj: [],   // 假日对象数组
			//language: 'zh-CN',  // 语言
			dateFormat: 'yy-mm-dd', // 设置日期格式  
			firstDay: 0, // 设置周的第一天
			minDate: null, // 最小日期
			maxDate: null, // 最大日期
			yearRange: 'c-10:c+10' // 年份范围
		};
		$.extend(setting, options || {});
		
		var dateSetting = {
			showMonthAfterYear: true, // 月在年之后显示   
			changeMonth: true,   // 允许选择月份   
			changeYear: true,   // 允许选择年份   
			dateFormat: setting.dateFormat , // 设置日期格式   
			showOn: 'both',   // button:在输入框旁边显示按钮触发，默认为：focus。还可以设置为both   
//			buttonImage: 'noImage',   // 按钮图标   
			buttonImageOnly: false,        // 不把图标显示在按钮上，即去掉按钮   
			buttonText: '',   // 按钮的名称
			minDate: setting.minDate, // 最小日期
			maxDate: setting.maxDate, // 最大日期
			yearRange: setting.yearRange, // 年份范围
			firstDay: setting.firstDay // 设置周的第一天
		};
		var holidays = []; // 假日日期数组
		var holidayName = []; // 假日名称数组
		var holidayObj = setting.holidayObj;
		if(typeof holidayObj != "undefinde" && holidayObj != "") {
			holidayObj = eval('('+holidayObj+')');
		}
		if($.isArray(holidayObj) && holidayObj.length > 0) {
			for(var i = 0; i < holidayObj.length; i++) {
				holidays.push(holidayObj[i].date);
				holidayName.push(holidayObj[i].holidayName);
			}
			// 假日日期初始化函数
			dateSetting.beforeShowDay = function(date) {
				var d = new Date(date);
				var strDate = ChangeDateToString(d);
				var i = jQuery.inArray(strDate, holidays);
				if(i != -1 && holidayName.length > i) {
					return [true, 'ui-datepicker-week-end', holidayName[i]]; 
				} else {
					return [true];
				}
			};
		}
		if(typeof(setting.onSelectEvent) == "function") {
			dateSetting.onSelect = function(dateText, inst){
				setting.onSelectEvent(dateText, inst);
				//inst.input.focus();
				inst.input.parent().removeClass('error');
				inst.input.parent().find('#errorText').remove();
			};
		} else {
			dateSetting.onSelect = function(dateText, inst){
				//inst.input.focus();
				inst.input.parent().removeClass('error');
				inst.input.parent().find('#errorText').remove();
			};
		}
		
		dateSetting.beforeShow = function(input, inst){
			setTimeout(function(){$(".ui-datepicker").css("z-index", 20000);}, 10);
			if(typeof(setting.beforeShow) == "function") {
				var rel = setting.beforeShow(input);
				if($.isArray(rel) && rel.length == 2) {
					if(rel[1] == 'maxDate' || rel[1] == 'minDate') {
						var date = getInputDate(rel[0]);
						if(date != "") {
							$(input).datepicker( "option", rel[1] , date );
						} else {
							$(input).datepicker( "option", rel[1] , null );
						}
					} else {
						var dateS = getInputDate(rel[0]);
						var dateE = getInputDate(rel[1]);
						if(dateS != "") {
							$(input).datepicker( "option", 'minDate' , dateS );
						} else {
							$(input).datepicker( "option", 'minDate' , null );
						}
						if(dateE != "") {
							$(input).datepicker( "option", 'maxDate' , dateE );
						} else {
							$(input).datepicker( "option", 'maxDate' , null );
						}
					}
				}
			}
		};
		// 语言设置
		$.datepicker.setDefaults( $.datepicker.regional[ setting.language ] );
		return this.each(function() {
			// 日历初始化
			$(this).datepicker(dateSetting);
	    });
	};

	/* 
	 * 日期转换成[yyyy-MM-dd]这种格式的字符串
	 * 
	 * Inputs:  Date:DateIn 要转换的日期
	 * 
	 */
	function ChangeDateToString(DateIn) {
		
	    var Year=0;
	    var Month=0;
	    var Day=0;
	
	    var CurrentDate="";
	
	    Year = DateIn.getFullYear();
	    Month = DateIn.getMonth()+1;
	    Day = DateIn.getDate();
	
	    CurrentDate = Year + "-";
	    if (Month >= 10 ) {
	        CurrentDate = CurrentDate + Month + "-";
	    } else {
	        CurrentDate = CurrentDate + "0" + Month + "-";
	    }
	    if (Day >= 10 ) {
	        CurrentDate = CurrentDate + Day ;
	    } else {
	        CurrentDate = CurrentDate + "0" + Day ;
	    }
	    return CurrentDate;
	}
	
	/* 
	 * 符合日期格的字符串转换成[yyyy-MM-dd]这种格式的字符串
	 * 如果是不符合日期格式的字符串则返回空
	 * 
	 * Inputs:  string:value 要转换的字符串
	 * 
	 */
	function getInputDate(value) {
		var date = "";
		if(value == null) {
			return "";
		}
		var r = value.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
		if(r != null) {
			var d = new Date(r[1],r[3]-1,r[4]);
			if(d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]) {
				date = value.replace(new RegExp("/","gm"),"-");
			}
		} else {
			var pattern = /^\d{8}$/;
			if(pattern.exec(value)) {
				var d = new Date(value.substring(0,4) + '/' + value.substring(4,6) + '/' + value.substring(6,8));
				if(d.getFullYear()==value.substring(0,4)&&(d.getMonth()+1)==value.substring(4,6)&&d.getDate()==value.substring(6,8)) {
					date = value.substring(0,4) + '-' + value.substring(4,6) + '-' + value.substring(6,8);
				}
			}
		}
		return date;
	}

})(jQuery);