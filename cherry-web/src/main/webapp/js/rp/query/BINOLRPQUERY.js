
function BINOLRPQUERY() {};

BINOLRPQUERY.prototype = {
		
	"searchReportTable" : function(url) {
		var callback = function(msg) {
			if(msg) {
				var data = eval('('+msg.replace(/\\u0019/g,'NULL')+')');
				if(data.error) {
					$('#reportTable').empty();
					$('#reportTableError').html(data.error);
				} else {
					$('#reportTableError').empty();
					$('#reportTable').reportTable(data);
				}
			} else {
				$('#reportTable').empty();
				$('#reportTableError').empty();
			}
			$('#reportTable').parents('div.section').show();
		};
		cherryAjaxRequest({
			url: url,
			param: $('#reportTableForm').serialize(),
			callback: callback
		});
	},
	"checkClick" : function(object) {
		if($(object).hasClass('checked')) {
			$(object).removeClass('checked');
			$(object).parent().find('#colRowVisible').val('0');
		} else {
			$(object).addClass('checked');
			$(object).parent().find('#colRowVisible').val('1');
		}
	},
	"changeDefMode" : function(object) {
		$('#defModeTemp').find('#defMode_'+$(object).val()).find('#defMode').val($(object).val());
		$(object).parents('table').html($('#defModeTemp').find('#defMode_'+$(object).val()).html());
	},
	"changeDepart" : function(object, select_default) {
		$(object).parent().nextAll().find('#departId').empty();
		$(object).parent().nextAll().find('#departId').append('<option value="">'+select_default+'</option>');
		$(object).parent().nextAll().find('#queryValue').val('');
		if($(object).val() != "") {
			$(object).parent().find('#queryValue').val($(object).find('option:selected').text());
		} else {
			$(object).parent().find('#queryValue').val('');
			return false;
		}
		var $depart = $(object).parent().next().find('#departId');
		var url = $('#queryDepartUrl').attr('href');
		var param = $(object).serialize() + '&' + $(object).parent().next().find('#propValObj').serialize();
		var type = $(object).parent().next().find('#propValObj').val();
		var callback = function(msg) {
			if(msg) {
				var data = eval('('+msg+')');
				for(var i in data) {
					if(type == "-1") {
						$depart.append('<option value="'+data[i].employeeId+'">'+data[i].employeeName+'</option>');
					} else {
						$depart.append('<option value="'+data[i].departId+'">'+data[i].departName+'</option>');
					}
				}
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	}
		
};

var binolrpquery =  new BINOLRPQUERY();