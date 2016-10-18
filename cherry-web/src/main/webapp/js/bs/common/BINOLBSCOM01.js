
// 画面布局全局变量
var bscom01_layout = null;

$(function() {
	
	// 画面布局初期化处理
	bscom01_layout = $('#treeLayoutDiv').layout({
		spacing_open: 2, // 边框的间隙 
		spacing_closed: 5, // 关闭时边框的间隙 
		resizerTip: "", // 鼠标移到边框时，提示语
		togglerTip_open: "", // pane打开时，当鼠标移动到边框上按钮上，显示的提示语
		togglerTip_closed: "", // pane关闭时，当鼠标移动到边框上按钮上，显示的提示语
		fxName: "none", // 打开关闭的动画效果
		togglerLength_open: 0, // pane打开时，边框按钮的长度  
		togglerLength_closed: 0, // pane关闭时，边框按钮的长度  
		west__minSize: 120, // 可改变的最小长度
		west__maxSize: 360 // 可改变的最大长度
	});

});