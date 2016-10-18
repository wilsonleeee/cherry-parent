var TEMP000_dialogBody ="";
var TEMP000_GLOBAL = function () {

};
TEMP000_GLOBAL.prototype = {
		"selPrtIndex" : "",
		"showTable" : function (obj, flag){
				TEMP000_dialogBody = $('#productDialog').html();
				// 取索引值
				TEMP000.selPrtIndex = $("input[name='selPrtIndex']", $(obj).parent().parent().parent()).val();
				// token验证
				var csrftoken = $("#parentCsrftoken").serialize();
				if(flag == 0){
					// 弹出框确定事件
					$("#selectProducts").attr("onClick","TEMP000.selectProduct();");
				}
				// 弹出信息框
				CAMPAIGN_TEMPLATE.openProPopup(obj, 0, TEMP000.selPrtIndex, csrftoken);
				},
		"selectProduct" : function (){
					var selectedProArr = $('#prt_dataTable input:checked');
					var index = $("#bugProTest").find(":input").length;
					var msgHtml = "";
					for (var i=0;i<selectedProArr.length;i++){
						var inNum = parseInt(index) + parseInt(i);
						var selectedProInfoArr = $(selectedProArr[i]);
						var $selectedPro = window.JSON2.parse(selectedProInfoArr.val());
						if(inNum < 1){
							msgHtml = msgHtml + '<span><input type="hidden" name="unitCode" value="'+ $selectedPro.unitCode + '" />' +
			           				'<input type="hidden" name="barCode" value="' + $selectedPro.barCode + '" />'  + 
			           				$selectedPro.nameTotal + '<input type="text" class="input" style="margin-left:10px;" />元' +
			           				'<span class="close" onclick="TEMP000.deletePro(this);return false;">' +
				          			'<span class="ui-icon ui-icon-close"></span></span></span>';
						}else{
							msgHtml = msgHtml + '<p style="margin-left:87px;"><input type="hidden" name="unitCode" value="'+ $selectedPro.unitCode + '" />' +
			           				'<input type="hidden" name="barCode" value="' + $selectedPro.barCode + '" />' + 
			           				$selectedPro.nameTotal + '<input type="text" class="input" style="margin-left:10px;" />元' +
			           				'<span class="close" onclick="TEMP000.deletePro(this);return false;">' +
				          			'<span class="ui-icon ui-icon-close"></span></span></p>';
						}
					}
					$("#bugProTest").append(msgHtml);

					// 关闭弹出框
					closeCherryDialog('productDialog', TEMP000_dialogBody);
				},
		"deletePro" : function (obj){
					$(obj).parent().remove();
				},
		"addBuyRecord" : function (obj){
					var html = $(obj).parents("tbody").first().find("tr").last().html();
					html = '<tr>' +  html + '</tr>';
					$(obj).parents("tbody").first().append(html);
					var size = $(obj).parents("tbody").first().find("tr").last().find(":input[name='selPrtIndex']").val();
					var trIndex = size.split('_');
					var index = trIndex[1];
					if (index) {
						var sizeInt = parseInt(index);
						sizeInt++;
						$(obj).parents("tbody").first().find("tr").last().find(":input[name='selPrtIndex']").val(trIndex[0] + '_' + sizeInt);
					}
					$(obj).parents("tbody").first().find("tr").last().find("[id^='prtSel_']").attr("id", "prtSel_" + trIndex[0] + '_' + sizeInt);
					$(obj).parents("tbody").first().find("tr").last().find("[class^='showPro_']").attr("class", "showPro_" + trIndex[0] + '_' + sizeInt);
					$(obj).parents("tbody").first().find("tr").last().find("#billLabel").text(sizeInt + 1);
					$(obj).parents("tbody").first().find("tr").last().find(":input[name='ticketDate']").removeAttr("id");
					$(obj).parents("tbody").first().find("tr").last().find(":input[name='ticketDate']").cherryDate();
				},
		"showCounterTable" : function (obj){
					TEMP000_dialogBody = $('#productDialog').html();
					// 弹出信息框
					TEMP000.openProPopup(obj);
				},
		"openProPopup" : function (obj){
					var counterCallback = function (){
						var $checked = $('#counter_dataTable').find(":input[checked]");
						if($checked.length > 0) {
							$("#counterCode").val($checked.parent().next().text());
							$("#counterName").val($checked.parent().next().next().text());
						} else {
							$("#counterCode").val($("#counterCode").val());
							$("#counterName").val($("#counterName").val());
						}
					};
					// token验证
					var csrftoken = $("#parentCsrftoken").val();
					var param = 'csrftoken=' + csrftoken;
					popDataTableOfCounter(obj, param, counterCallback);
				}
};
var TEMP000 = new TEMP000_GLOBAL();