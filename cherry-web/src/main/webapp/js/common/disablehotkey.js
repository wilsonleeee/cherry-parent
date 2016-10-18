
//禁止Enter键
document.onkeydown = function(e){
    if ($.browser.msie) {
        if( event.keyCode==13 ){
            event.keyCode = 0;
            //阻止IE事件冒泡
            event.cancelBubble=true;
            event.returnvalue=false;
            return false;
         }
     }else{
         if(e.which==13 ){
            return false;
         }
     }
}

