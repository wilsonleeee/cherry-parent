function BINOLWPCOM01(){};

BINOLWPCOM01.prototype = {
	// 生日选择联动框初始化
	"selectBirthDay": function(monthId, dayId, noHeader) {
		var $monthId = $("#"+monthId);
		var $dayId = $("#"+dayId);
		var headerVal = $dayId.html();
		if(!headerVal) {
			headerVal = '';
		}
		for(var i = 1; i <= 12; i++) {
			$monthId.append('<option value="'+i+'">'+i+'</option>');
		}
		$monthId.change(function(){
			$dayId.html(headerVal);
			var month = $(this).val();
			if(month == "") {
				return;
			}
			if(noHeader) {
				$dayId.empty();
			}
			var i = 1;
			var max = 0;
			if(month == '2') {
				max = 29;
			} else if(month == '4' || month == '6' || month == '9' || month == '11') {
				max = 30;
			} else {
				max = 31;
			}
			for(i = 1; i <= max; i++) {
				$dayId.append('<option value="'+i+'">'+i+'</option>');
			}
		});	
	}
};

var binolwpcom01 = new BINOLWPCOM01();