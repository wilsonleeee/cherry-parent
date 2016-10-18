// 报表导出Dialog
var global_exportDialogHtml = null;
function BINOLCM99(){
	
}
BINOLCM99.prototype = {	
	// 取得报表导出对话框
	"getExportDialog":	function(pageId,reportMode) {
		var that = this;
		var $dialog = $("#exportDialog");
		var dialogSetting = {
			bgiframe: true,
			width:450, 
			height:280,
			minWidth:350,
			minHeight:280,
			zIndex: 9999,
			modal: true, 
			title:$("#BillExportTitle").text(),
			resizable : false,
			close: function(event, ui) { removeDialog('#exportDialog');}
		};
		if (null == global_exportDialogHtml) {
			$("#export_hide").empty();
			global_exportDialogHtml = $dialog.html();
		} else {
			$dialog.html(global_exportDialogHtml);
		}
		$dialog.dialog(dialogSetting);
		$("#e_pageId").val(pageId);
		$("#e_csrftoken").val(that.getTokenVal());
		if(reportMode != null || reportMode != undefined){
			$("#e_reportMode").val(reportMode);
		}
	},
	// 取得Token值
	"getTokenVal":	function() {
		var $token = $('#csrftoken');
		if($token.length == 0 && window.opener){
			$token = $('#csrftoken', window.opener.document);
		}
		return $token.val();
	},
	// 对象JSON化
	"toJSON" : function (obj){
		var JSON = [];
		$(obj).find(':input').each(function(){
			$this = $(this);
			if (($this.attr("type") == "radio" && this.checked)|| $this.attr("type") != "radio") {
				var value = $.trim($this.val());
				var name = $.trim($this.prop("name"));
				if(value != '' && name!="") {
					if(name=='counter'){
						// 共通条情况下
						name="departId";
					}
					JSON.push('"'+name+'":"'
						+encodeURIComponent(value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				}
			}
		});
		return "{"+JSON.toString()+"}";
	},
	// 设置数据源查询条件参数
	"setExportParams":function(expr){
		var json = this.toJSON(expr);
		$("#e_params").val(json);
	}
};
var BINOLCM99 = new BINOLCM99();