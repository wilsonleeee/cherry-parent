$(document).ready(function() {
	//IE圆角修复 & IE表单交互
	if((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0))
	{
		//IE圆角修复
		$('.panel').prepend('<b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b>');
		$('.panel').append('<b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b>');
		$('.menu').append('<b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b><b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b>');
		$('.sidemenu a.on').append('<b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b><b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b>');
		
		
		$(":text").focus(function(){
			$(this).addClass('input-focus');
		});
		$(":text").blur(function(){
			$(this).removeClass('input-focus');
		});
	}
})