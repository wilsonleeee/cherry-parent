$().ready(function(){
	var jsonArr = new Array();
	
	var json_0 ={};
	json_0.when = '#buydate#==2010/03/08 && #buyCount# = 1';
	var jsonThen_0 ={};
	jsonThen_0.thenType = '0';
	jsonThen_0.thenDrl = 'point=point*2';
	json_0.then = jsonThen_0;
	jsonArr.push(json_0);
	
	var json_1 ={};
	json_1.when = '#buydate#==2010/03/08 && #buyCount# = 1';
	var jsonThen_1  = {}; 
	var then_1_Arr = new Array();
	var then_1_json_0 = {};
	then_1_json_0.startPoint = '0';
	then_1_json_0.endPoint = '100';
	then_1_json_0.multiple = '2.1';
	then_1_json_0.rewardPoint = '101';
	then_1_Arr.push(then_1_json_0);
	
	var then_1_json_1 = {};
	then_1_json_1.startPoint = '100';
	then_1_json_1.endPoint = '200';
	then_1_json_1.multiple = '2.2';
	then_1_json_1.rewardPoint = '201';
	then_1_Arr.push(then_1_json_1);
	
	jsonThen_1.thenType = '1';
	jsonThen_1.subsectionJsonArr = then_1_Arr;
	json_1.then = jsonThen_1 ; 
	jsonArr.push(json_1);
	
	var json_2 ={};
	json_2.when = '#buydate#==2010/03/08 && #buyCount# = 1';
	var jsonThen_2  = {}; 
	var then_2_Arr = new Array();
	var then_2_json_0 = {};
	then_2_json_0.startPoint = '0';
	then_2_json_0.endPoint = '100';
	then_2_json_0.multiple = '2.1';
	then_2_json_0.rewardPoint = '101';
	then_2_Arr.push(then_2_json_0);
	
	var then_2_json_1 = {};
	then_2_json_1.startPoint = '100';
	then_2_json_1.endPoint = '200';
	then_2_json_1.multiple = '2.2';
	then_2_json_1.rewardPoint = '201';
	then_2_Arr.push(then_2_json_1);
	
	jsonThen_2.thenType = '2';
	jsonThen_2.subsectionJsonArr = then_2_Arr;
	json_2.then = jsonThen_2 ; 
	jsonArr.push(json_2);
	
	$('#hide1').val(JSON.stringify(jsonArr[0]));
	$('#hide2').val(JSON.stringify(jsonArr[1]));
	$('#hide3').val(JSON.stringify(jsonArr[2]));
	
	
	
})