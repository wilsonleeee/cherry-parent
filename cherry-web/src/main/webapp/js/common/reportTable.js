;(function($) {
    
	/* 
	 * 根据bi返回的数据，生成报表共通js
	 * 
	 * Inputs:  Object:option 		生成报表时的属性参数
	 * 			option.data		           需要生成报表的数据，由bi返回，是一个二维的数组，例如：[[name,age],[wang,26]]
	 * 			option.colInfo 		以列形式展示的属性信息
	 * 			option.rowInfo 		以行形式展示的属性信息
	 * 			option.countInfo	统计的属性信息
	 * 
	 */
    $.fn.reportTable = function(option) {
        return this.each(function() {
			if(option.data instanceof Array && option.rowInfo instanceof Array 
				&& option.colInfo instanceof Array && option.countInfo instanceof Array) {
				// 统计项在一列上显示的场合
				if(option.statisticVisType == '0') {
					// 报表初期化处理，包括颜色的生成处理
					tableInit($(this),option);
				} else {
					// 报表初期化处理，包括颜色的生成处理
					tableRowInit($(this),option);
				}
				// 行单元格合并处理
				table_rowspan($(this), option);
				// 列单元格合并处理
				table_colspan($(this), option);
			}
        });
    };
	
	// 标题颜色
	var titleColor = '#c6d0d5';

	// 标题列颜色
	var rowColor = ['#acccdc','#bcdcdc','#ccecdc','#dcfcdc','#ecfddc'];

	// 小计标题的颜色
	var subTotalTitleColor = '#daf8ba';

	// 小计的颜色
	var subTotalColor = '#eeeeee';

	// 小计行列交叉处颜色
	var subTotalsColor = '#dddddd';

	// 总计标题的颜色
	var totalTitleColor = '#fadcb8';

	// 总计的颜色
	var totalColor = '#fcedda';

	// 总计行列交叉处颜色
	var totalsColor = '#fad9b1';

	// 统计项标题颜色
	var countColor = '#ece9d8';
	
	// 报表初期化处理，包括颜色的生成处理
	function tableInit($table,option) {
		
		// 列字段个数
		var col = option.colInfo.length;
		// 行字段个数
		var row = option.rowInfo.length;
		// 统计字段个数
		var count = option.countInfo.length;
		// 显示的数据
		var data = option.data;
		var html = '';
		// 记录是小计的列的列号
		var x = new Array();
		// 记录是总计的列的列号
		var y = new Array();
		// 保存钻透条件
		var drillQuery = new Array();
		// 钻透URL
		var drillUrl = option.drillUrl;
		// BI报表查询条件
		var query = option.query;
		// BI报表ID
		var biRptCode = option.biRptCode;
		var url = drillUrl + '?' + 'biQuery=' + encodeURIComponent(query) + '&biQueryDisPlay=' + encodeURIComponent(option.queryDisPlay) + '&biRptCode=' + biRptCode;
		// 生成列标题
		for(var i = 0; i < col; i++) {
			html = html + '<tr>';
			for(var j = 0; j < row; j++) {
				if(i+1 == col) {
					html = html + '<th>' + data[col+count-1][j] + '</th>';
				} else {
					html = html + '<th>' + '&nbsp;' + '</th>';
				}
			}
			for(var j = row-1; j < data[i].length; j++) {
				if(j == row-1) {
					html = html + '<th>' + data[i][j] + '</th>';
				} else {
					// 是小计的场合
					if((data[i][j] != null && data[i][j].indexOf('/') > -1) || x[j] != null) {
						if(x[j] == null) {
							// 记录小计的列号
							x[j] = i;
						}
						if(data[i][j] != null && $.trim(data[i][j]) != '') {
							html = html + '<th class="green">' + data[i][j] + '</td>';
						} else {
							html = html + '<th class="green">' + '&nbsp;' + '</td>';
						}
					} 
					// 是总计的场合
					else if((data[i][j] != null && data[i][j].indexOf('总计') > -1) || y[j] != null) {
						if(y[j] == null) {
							// 记录总计的列号
							y[j] = i;
						}
						if(data[i][j] && $.trim(data[i][j]) != '') {
							html = html + '<th class="red">' + data[i][j] + '</th>';
						} else {
							html = html + '<th class="red">' + '&nbsp;' + '</th>';
						}
					} else {
						if(data[i][j] != null && $.trim(data[i][j]) != '') {
							html = html + '<th class="blue">' + data[i][j] + '</th>';
						} else {
							html = html + '<th class="blue">' + '&nbsp;' + '</th>';
						}
					}				
				}
			}
			html = html + '</tr>';
		}
		// 记录当前是第几个统计项
		var m = 0;
		// 判断该行是否为小计行
		var n = -1;
		// 数据间隔样式
		var classVal = "odd";
		for(var i = col+count; i < data.length; i++) {
			// 统计项为下一轮的第一个时，重新设置成0，0表示第一个
			if(m == count) {
				m = 0;
				// 判断该行是否为小计行
				n = isSubTotal(data[i]);
				if(classVal == "odd") {
					classVal = "even";
				} else {
					classVal = "odd";
				}
			}
			html = html + '<tr class="'+classVal+'">';
			// 是小计行的场合
			if(n > -1) {
				for(var j = 0; j < data[i].length; j++) {
					if(j == row) {
						html = html + '<td class="yellow">' + data[col+m][row] + '</td>';
					}
					if(j < n) {
						if(data[i][j] != null && $.trim(data[i][j]) != '') {
							html = html + '<td class="blue">' + data[i][j] + '</td>';
						} else {
							html = html + '<td class="blue">' + '&nbsp;' + '</td>';
						}
					} else if(j < row) {
						if(data[i][j] != null && $.trim(data[i][j]) != '') {
							html = html + '<td class="green">' + data[i][j] + '</td>';
						} else {
							html = html + '<td class="green">' + '&nbsp;' + '</td>';
						}
					} else {
						// 可钻透的场合
						if(option.countInfo[m].isDrillThrough == "1") {
							drillQuery = [];
							for(var b = 0; b <= n; b++) {
								if(b == n) {
									drillQuery.push('{"key":"'+option.rowInfo[b].defValueTbl+'","value":"'+data[i][b].substring(0,data[i][b].indexOf('/'))+'","name":"'+option.rowInfo[b].defValue+'"}');
								} else {
									drillQuery.push('{"key":"'+option.rowInfo[b].defValueTbl+'","value":"'+data[i-count][b]+'","name":"'+option.rowInfo[b].defValue+'"}');
								}
							}
							if(y[j] != null) {
								if(data[i][j] != null && $.trim(data[i][j]) != '') {
									html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[m].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</a></td>';
								} else {
									html = html + '<td class="red">' + '&nbsp;' + '</td>';
								}
							} else {
								if(x[j] != null) {
									for(var b = 0; b <= x[j]; b++) {
										if(b == x[j]) {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j].substring(0,data[b][j].indexOf('/'))+'","name":"'+option.colInfo[b].defValue+'"}');
										} else {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j-1]+'","name":"'+option.colInfo[b].defValue+'"}');
										}
									}
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[m].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								} else {
									for(var b = 0; b < col; b++) {
										drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j]+'","name":"'+option.colInfo[b].defValue+'"}');
									}
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[m].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								}
							}
						} else {
							if(y[j] != null) {
								if(data[i][j] != null && $.trim(data[i][j]) != '') {
									html = html + '<td class="red">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</td>';
								} else {
									html = html + '<td class="red">' + '&nbsp;' + '</td>';
								}
							} else {
								if(x[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								} else {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								}
							}
						}
					}
				}
			} else {
				// 是总计的场合
				if(data[i][0] != null && data[i][0].indexOf('总计') > -1) {
					for(var j = 0; j < data[i].length; j++) {
						if(j == row) {
							html = html + '<td class="yellow">' + data[col+m][row] + '</td>';
						}
						if(j < row) {
							if(data[i][j] != null && $.trim(data[i][j]) != '') {
								html = html + '<td class="red">' + data[i][j] + '</td>';
							} else {
								html = html + '<td class="red">' + '&nbsp;' + '</td>';
							}
						} else {
							// 可钻透的场合
							if(option.countInfo[m].isDrillThrough == "1") {
								drillQuery = [];
								if(y[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[m].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								} else {
									if(x[j] != null) {
										for(var b = 0; b <= x[j]; b++) {
											if(b == x[j]) {
												drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j].substring(0,data[b][j].indexOf('/'))+'","name":"'+option.colInfo[b].defValue+'"}');
											} else {
												drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j-1]+'","name":"'+option.colInfo[b].defValue+'"}');
											}
										}
										if(data[i][j] != null && $.trim(data[i][j]) != '') {
											html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[m].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</a></td>';
										} else {
											html = html + '<td class="red">' + '&nbsp;' + '</td>';
										}
									} else {
										for(var b = 0; b < col; b++) {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j]+'","name":"'+option.colInfo[b].defValue+'"}');
										}
										if(data[i][j] != null && $.trim(data[i][j]) != '') {
											html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[m].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</a></td>';
										} else {
											html = html + '<td class="red">' + '&nbsp;' + '</td>';
										}
									}
								}
							} else {
								if(y[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								} else {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								}
							}
						}
					}
				} else {
					for(var j = 0; j < data[i].length; j++) {
						if(j == row) {
							html = html + '<td class="yellow">' + data[col+m][row] + '</td>';
						}
						if(j < row) {
							if(data[i][j] != null && $.trim(data[i][j]) != '') {
								html = html + '<td class="blue">' + data[i][j] + '</td>';
							} else {
								html = html + '<td class="blue">' + '&nbsp;' + '</td>';
							}
						} else {
							// 可钻透的场合
							if(option.countInfo[m].isDrillThrough == "1") {
								drillQuery = [];
								for(var b = 0; b < row; b++) {
									drillQuery.push('{"key":"'+option.rowInfo[b].defValueTbl+'","value":"'+data[i][b]+'","name":"'+option.rowInfo[b].defValue+'"}');
								}
								if(y[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[m].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								} else if(x[j] != null) {
									for(var b = 0; b <= x[j]; b++) {
										if(b == x[j]) {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j].substring(0,data[b][j].indexOf('/'))+'","name":"'+option.colInfo[b].defValue+'"}');
										} else {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j-1]+'","name":"'+option.colInfo[b].defValue+'"}');
										}
									}
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[m].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								} else {
									for(var b = 0; b < col; b++) {
										drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j]+'","name":"'+option.colInfo[b].defValue+'"}');
									}
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[m].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</a></td>';
									} else {
										html = html + '<td>' + '&nbsp;' + '</td>';
									}
								}
							} else {
								if(y[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								} else if(x[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green">' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								} else {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td>' + dataWrite(data[i][j], option.countInfo[m].defFormat) + '</td>';
									} else {
										html = html + '<td>' + '&nbsp;' + '</td>';
									}
								}
							}
						}
					}
				}
			}
			html = html + '</tr>';
			// 每次循环统计项加一位
			m++;
		}
		$table.html(html);
	}
	
	// 判断某行是否为小计行
	function isSubTotal(data) {
		for(var i = 0; i < data.length; i++) {
			if(data[i].indexOf('/') > -1) {
				return i;
			}
		}
		return -1;
	}
	
	// 数据不为空时按原数据返回，否则返回&nbsp;字符串
	function dataWrite(data, pattern) {
		if(data != null && $.trim(data) != '') {
			if(pattern != null && pattern != '') {
				return dataFormat(data, pattern);
			} else {
				return data;
			}
		} else {
			return '&nbsp;';
		}
	}
	
	// 数字格式化处理
	function dataFormat(number, pattern) {
		
		var patterns = pattern.split('.');
		if (patterns[1] && patterns[1].length) {
			number = Math.round(number*Math.pow(10,patterns[1].length))/Math.pow(10,patterns[1].length);
		} else {
			number = Math.round(number);
		}
		
		var numbers = ('' + number).split('.')
        , leftnumber = numbers[0].split('')
        , exec = function (lastMatch) {
            if (lastMatch == '0' || lastMatch == '#') {
                if (leftnumber.length) {
                    return leftnumber.pop();
                } else if (lastMatch == '0') {
                    return lastMatch;
                } else {
                    return '';
                }
            } else {
                return lastMatch;
            }
		}, string;
		
	    string = patterns[0].split('').reverse().join('').replace(/./g, exec).split('').reverse().join('');
	    string = leftnumber.join('') + string;
	    
	    if (patterns[1] && patterns[1].length) {
	        leftnumber = (numbers[1] && numbers[1].length) ? numbers[1].split('').reverse() : [];
	        string += '.' + patterns[1].replace(/./g, exec);
	    }
	    return string.replace(/\.$/, '');
	}
	
	
	/* 
	 * 行单元格合并处理
	 * 
	 * Inputs:  $table:需要处理的table的jquery对象
	 * 			cols:指定哪几列需要行合并处理，是一个数组，存放具体的列号
	 * 			row:需要合并的行的最小值
	 * 			count:小计或总计时需要合并的行数
	 * 
	 */
	function table_rowspan($table,option) {
		
		var cols = new Array();
		for(var i = 0; i < option.rowInfo.length; i++) {
			cols[i] = i+1;
		}
		var count = option.countInfo.length;
		// 统计项在一行上显示时
		if(option.statisticVisType == '1') {
			count = 1;
		}
	    
		// 需要合并的行的最小值
		var table_minrow = 0;
		// 需要合并的行的最大值
	    var table_maxrow = $table.find('tr td:nth-child(1)').length+1;    
		// 保存第一个td对象
	    var table_firsttd = "";  
		// 保存需要合并的行数
	    var table_SpanNum = 0;
		// 保存小计或总计时的第一个td对象
		var subTotal_firsttd = "";
		// 保存小计或总计时需要合并的行数
		var subTotal_SpanNum = 0;
		// 保存当前的td对象
	    var table_currenttd = "";  
	    for(var j=0; j<cols.length; j++) {
			subTotal_SpanNum = 0;
			table_SpanNum = 0;
			$table.find('tr td:nth-child(' + cols[j] + ')').slice(table_minrow,table_maxrow).each(function(i) {
				table_currenttd = $(this);
				// 不是小计或总计的场合
				if(table_currenttd.text().indexOf('/') == -1 
					&& table_currenttd.prevAll('td').text().indexOf('/') == -1) {
					subTotal_SpanNum = 0;
					if(table_SpanNum == 0) {
						table_firsttd = $(this);  
						table_SpanNum = 1;
					} else {
						if($.trim(table_currenttd.text()) == "" || table_currenttd.text() == table_firsttd.text()) {
							table_SpanNum++;  
							table_currenttd.hide();
							table_firsttd.attr("rowSpan",table_SpanNum);  
						} else {
							table_firsttd = $(this);  
							table_SpanNum = 1;
						}
					}
			    } else {
					table_SpanNum = 0;
					if(subTotal_SpanNum%count == 0) {
						subTotal_firsttd = $(this);  
						subTotal_SpanNum = 1;
					} else {
						subTotal_SpanNum++;  
						table_currenttd.hide(); 
						subTotal_firsttd.attr("rowSpan",subTotal_SpanNum);  
					}
			    }					 
			});
		}
	}
	
	/* 
	 * 列单元格合并处理
	 * 
	 * Inputs:  $table:需要处理的table的jquery对象
	 * 			rows:指定哪几行需要列合并处理，是一个数组，存放具体的行号
	 * 			col:需要合并的列的最小值
	 * 
	 */
	function table_colspan($table,option) {  
		
		var rows = new Array();
		// 统计项在一行上显示时
		if(option.statisticVisType == '1') {
			for(var i = 0; i < option.colInfo.length; i++) {
				rows[i] = i+1;
			}
		} else {
			if(option.colInfo.length > 1) {
				for(var i = 0; i < option.colInfo.length-1; i++) {
					rows[i] = i+1;
				}
			}
		}
		
		var count = 1;
		// 统计项在一行上显示时
		if(option.statisticVisType == '1') {
			count = option.countInfo.length;
		}
		
		// 需要合并的列的最小值
		var table_mincolnum = option.rowInfo.length+1;
		// 统计项在一行上显示时
		if(option.statisticVisType == '1') {
			table_mincolnum = option.rowInfo.length;
		}
		// 需要合并的列的最大值
	    var table_maxcolnum = $table.find('tr:nth-child(1)').children().length;  
		// 保存第一个td对象
        var table_firsttd = "";  
		// 保存需要合并的列数
        var table_SpanNum = 0;
        // 保存小计或总计时的第一个td对象
		var subTotal_firsttd = "";
		// 保存小计或总计时需要合并的行数
		var subTotal_SpanNum = 0;
		// 保存当前的td对象
        var table_currenttd = "";  
        var y = table_mincolnum;
		for(var j=0; j<rows.length; j++) {
			table_SpanNum = 0;
			$table.find('tr:nth-child(' + rows[j] + ')').children().slice(table_mincolnum,table_maxcolnum).each(function(i) {
				table_currenttd = $(this);  
				y = i+table_mincolnum;
				// 不是小计或总计的场合
				if(table_currenttd.text().indexOf('/') == -1 
					&& table_currenttd.parent('tr').prevAll().find('th:eq('+y+')').text().indexOf('/') == -1) {
					subTotal_SpanNum = 0;
					if(table_SpanNum == 0) {
						table_firsttd = $(this);  
						table_SpanNum = 1;
					} else {
						if($.trim(table_currenttd.text()) == "" || table_currenttd.text() == table_firsttd.text()) {
							table_SpanNum++; 
							//if(table_currenttd.is(':visible')) {
								//table_firsttd.width(parseInt(table_firsttd.width())+ parseInt(table_currenttd.width()));
							//}
							table_currenttd.hide();
							table_firsttd.attr("colSpan",table_SpanNum);
						} else {
							table_firsttd = $(this);  
							table_SpanNum = 1;
						}
					}
				} else {
					table_SpanNum = 0;
					if(subTotal_SpanNum%count == 0) {
						subTotal_firsttd = $(this);  
						subTotal_SpanNum = 1;
					} else {
						subTotal_SpanNum++;  
						table_currenttd.hide(); 
						subTotal_firsttd.attr("colSpan",subTotal_SpanNum);  
					}
				}
			     
			});
		}
   	}
	
	
	// 报表初期化处理，包括颜色的生成处理(统计项在一行上显示时用)
	function tableRowInit($table,option) {
		
		// 列字段个数
		var col = option.colInfo.length;
		// 行字段个数
		var row = option.rowInfo.length;
		// 统计字段个数
		var count = option.countInfo.length;
		// 显示的数据
		var data = option.data;
		var html = '';
		// 记录是小计的列的列号
		var x = new Array();
		// 记录是总计的列的列号
		var y = new Array();
		// 保存钻透条件
		var drillQuery = new Array();
		// 钻透URL
		var drillUrl = option.drillUrl;
		// BI报表查询条件
		var query = option.query;
		// BI报表ID
		var biRptCode = option.biRptCode;
		var url = drillUrl + '?' + 'biQuery=' + encodeURIComponent(query) + '&biQueryDisPlay=' + encodeURIComponent(option.queryDisPlay) + '&biRptCode=' + biRptCode;
		// 生成列标题
		for(var i = 0; i <= col; i++) {
			html = html + '<tr>';
			for(var j = 0; j < row; j++) {
				if(data[i][j] != null && $.trim(data[i][j]) != '') {
					html = html + '<th>' + data[i][j] + '</th>';
				} else {
					html = html + '<th>' + '&nbsp;' + '</th>';
				}
			}
			if(i == col) {
				for(var j = row; j < data[i].length; j++) {
					if(data[i][j] != null && $.trim(data[i][j]) != '') {
						html = html + '<th class="yellow">' + data[i][j] + '</th>';
					} else {
						html = html + '<th>' + '&nbsp;' + '</th>';
					}
				}
			} else {
				for(var j = row; j < data[i].length; j++) {
					// 是小计的场合
					if((data[i][j] != null && data[i][j].indexOf('/') > -1) || x[j] != null) {
						if(x[j] == null) {
							// 记录小计的列号
							x[j] = i;
						}
						if(data[i][j] != null && $.trim(data[i][j]) != '') {
							html = html + '<th class="green">' + data[i][j] + '</td>';
						} else {
							html = html + '<th class="green">' + '&nbsp;' + '</td>';
						}
					} 
					// 是总计的场合
					else if((data[i][j] != null && data[i][j].indexOf('总计') > -1) || y[j] != null) {
						if(y[j] == null) {
							// 记录总计的列号
							y[j] = i;
						}
						if(data[i][j] && $.trim(data[i][j]) != '') {
							html = html + '<th class="red">' + data[i][j] + '</th>';
						} else {
							html = html + '<th class="red">' + '&nbsp;' + '</th>';
						}
					} else {
						if(data[i][j] != null && $.trim(data[i][j]) != '') {
							html = html + '<th class="blue">' + data[i][j] + '</th>';
						} else {
							html = html + '<th class="blue">' + '&nbsp;' + '</th>';
						}
					}
				}
			}
			html = html + '</tr>';
		}
		
		// 判断该行是否为小计行
		var n = -1;
		// 数据间隔样式
		var classVal = "odd";
		for(var i = col+1; i < data.length; i++) {
			// 判断该行是否为小计行
			n = isSubTotal(data[i]);
			if(classVal == "odd") {
				classVal = "even";
			} else {
				classVal = "odd";
			}
			html = html + '<tr class="'+classVal+'">';
			// 是小计行的场合
			if(n > -1) {
				for(var j = 0; j < data[i].length; j++) {
					if(j < n) {
						if(data[i][j] != null && $.trim(data[i][j]) != '') {
							html = html + '<td class="blue">' + data[i][j] + '</td>';
						} else {
							html = html + '<td class="blue">' + '&nbsp;' + '</td>';
						}
					} else if(j < row) {
						if(data[i][j] != null && $.trim(data[i][j]) != '') {
							html = html + '<td class="green">' + data[i][j] + '</td>';
						} else {
							html = html + '<td class="green">' + '&nbsp;' + '</td>';
						}
					} else {
						// 可钻透的场合
						if(option.countInfo[(j-row)%count].isDrillThrough == "1") {
							drillQuery = [];
							for(var b = 0; b <= n; b++) {
								if(b == n) {
									drillQuery.push('{"key":"'+option.rowInfo[b].defValueTbl+'","value":"'+data[i][b].substring(0,data[i][b].indexOf('/'))+'","name":"'+option.rowInfo[b].defValue+'"}');
								} else {
									drillQuery.push('{"key":"'+option.rowInfo[b].defValueTbl+'","value":"'+data[i-1][b]+'","name":"'+option.rowInfo[b].defValue+'"}');
								}
							}
							if(y[j] != null) {
								if(data[i][j] != null && $.trim(data[i][j]) != '') {
									html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[(j-row)%count].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</a></td>';
								} else {
									html = html + '<td class="red">' + '&nbsp;' + '</td>';
								}
							} else {
								if(x[j] != null) {
									for(var b = 0; b <= x[j]; b++) {
										if(b == x[j]) {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j].substring(0,data[b][j].indexOf('/'))+'","name":"'+option.colInfo[b].defValue+'"}');
										} else {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j-count]+'","name":"'+option.colInfo[b].defValue+'"}');
										}
									}
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[(j-row)%count].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								} else {
									for(var b = 0; b < col; b++) {
										drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j]+'","name":"'+option.colInfo[b].defValue+'"}');
									}
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[(j-row)%count].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								}
							}
						} else {
							if(y[j] != null) {
								if(data[i][j] != null && $.trim(data[i][j]) != '') {
									html = html + '<td class="red">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</td>';
								} else {
									html = html + '<td class="red">' + '&nbsp;' + '</td>';
								}
							} else {
								if(x[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								} else {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								}
							}
						}
					}
				}
			} else {
				// 是总计的场合
				if(data[i][0] != null && data[i][0].indexOf('总计') > -1) {
					for(var j = 0; j < data[i].length; j++) {
						if(j < row) {
							if(data[i][j] != null && $.trim(data[i][j]) != '') {
								html = html + '<td class="red">' + data[i][j] + '</td>';
							} else {
								html = html + '<td class="red">' + '&nbsp;' + '</td>';
							}
						} else {
							// 可钻透的场合
							if(option.countInfo[(j-row)%count].isDrillThrough == "1") {
								drillQuery = [];
								if(y[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[(j-row)%count].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								} else {
									if(x[j] != null) {
										for(var b = 0; b <= x[j]; b++) {
											if(b == x[j]) {
												drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j].substring(0,data[b][j].indexOf('/'))+'","name":"'+option.colInfo[b].defValue+'"}');
											} else {
												drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j-count]+'","name":"'+option.colInfo[b].defValue+'"}');
											}
										}
										if(data[i][j] != null && $.trim(data[i][j]) != '') {
											html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[(j-row)%count].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</a></td>';
										} else {
											html = html + '<td class="red">' + '&nbsp;' + '</td>';
										}
									} else {
										for(var b = 0; b < col; b++) {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j]+'","name":"'+option.colInfo[b].defValue+'"}');
										}
										if(data[i][j] != null && $.trim(data[i][j]) != '') {
											html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[(j-row)%count].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</a></td>';
										} else {
											html = html + '<td class="red">' + '&nbsp;' + '</td>';
										}
									}
								}
							} else {
								if(y[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								} else {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								}
							}
						}
					}
				} else {
					for(var j = 0; j < data[i].length; j++) {
						if(j < row) {
							if(data[i][j] != null && $.trim(data[i][j]) != '') {
								html = html + '<td class="blue">' + data[i][j] + '</td>';
							} else {
								html = html + '<td class="blue">' + '&nbsp;' + '</td>';
							}
						} else {
							// 可钻透的场合
							if(option.countInfo[(j-row)%count].isDrillThrough == "1") {
								drillQuery = [];
								for(var b = 0; b < row; b++) {
									drillQuery.push('{"key":"'+option.rowInfo[b].defValueTbl+'","value":"'+data[i][b]+'","name":"'+option.rowInfo[b].defValue+'"}');
								}
								if(y[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[(j-row)%count].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								} else if(x[j] != null) {
									for(var b = 0; b <= x[j]; b++) {
										if(b == x[j]) {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j].substring(0,data[b][j].indexOf('/'))+'","name":"'+option.colInfo[b].defValue+'"}');
										} else {
											drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j-count]+'","name":"'+option.colInfo[b].defValue+'"}');
										}
									}
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green"><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[(j-row)%count].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</a></td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								} else {
									for(var b = 0; b < col; b++) {
										drillQuery.push('{"key":"'+option.colInfo[b].defValueTbl+'","value":"'+data[b][j]+'","name":"'+option.colInfo[b].defValue+'"}');
									}
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td><a href=\''+url+'&drillQuery=['+drillQuery.toString()+']&title='+option.countInfo[(j-row)%count].defValue+'\' onclick="openWin(this);return false;">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</a></td>';
									} else {
										html = html + '<td>' + '&nbsp;' + '</td>';
									}
								}
							} else {
								if(y[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="red">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</td>';
									} else {
										html = html + '<td class="red">' + '&nbsp;' + '</td>';
									}
								} else if(x[j] != null) {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td class="green">' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</td>';
									} else {
										html = html + '<td class="green">' + '&nbsp;' + '</td>';
									}
								} else {
									if(data[i][j] != null && $.trim(data[i][j]) != '') {
										html = html + '<td>' + dataWrite(data[i][j], option.countInfo[(j-row)%count].defFormat) + '</td>';
									} else {
										html = html + '<td>' + '&nbsp;' + '</td>';
									}
								}
							}
						}
					}
				}
			}
			html = html + '</tr>';
		}
		$table.html(html);
	}
   
})(jQuery);
