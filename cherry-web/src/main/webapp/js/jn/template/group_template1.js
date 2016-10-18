var GRPTEMP1_GLOBAL = function () {

};
GRPTEMP1_GLOBAL.prototype = {
		
		"extendOption" : {
			"BASE000032_INFO" : function (id){
			var m = [];
			var n = [];
			var $id = $("#" + id);
			$id.find(":input").not($(":input","[id^='rulePriority']")).each(function (){
				m.push('"'+ this.name+'":"'+
						$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
			});
			$id.find("#rulePriorityTable").find('tr').not(".thClass").each(function(){
				var p = [];
				$(this).find(":input").each(function (){
					p.push('"'+ this.name+'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					if(this.name == "campaignId"){
						var keys = [];
						keys.push(this.value);
					}
					p.push('"keys":["' + keys + '"]');
				});
				n.push('{' + p.toString() + '}');
			});
			m.push('"priorityRuleList":[' + n.toString() + "]");
			$("#extraInfo").val('[' + n.toString() + ']');
			return m.toString();
		}
	}
};
var grptemp1 = new GRPTEMP1_GLOBAL();
CAMPAIGN_TEMPLATE.addOption(grptemp1.extendOption);
CAMPAIGN_TEMPLATE.addTempRules(grptemp1.extendRules);
$(document).ready(function() {
	$( "#rulePriorityTable" ).sortable({
		update:function (){CAMPAIGN_TEMPLATE.arrowsFlag("rulePriorityTable");},
		connectWith: "tbody",
		items: "tr:not(.thClass)",
		cursor: "pointer",
		grid: [500, 20]
	});
	$( "#rulePriorityDotConTable" ).sortable({
		update:function (){CAMPAIGN_TEMPLATE.arrowsFlag("rulePriorityDotConTable");},
		connectWith: "tbody",
		items: "tr:not(.thClass)",
		cursor: "pointer"
	});
	
	$("#rulePriorityTable", "#rulePriorityDotConTable").disableSelection();
});