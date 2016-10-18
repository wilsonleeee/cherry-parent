/**
 * @author user3
 */
window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};
