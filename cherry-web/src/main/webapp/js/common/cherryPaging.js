function getPageContent(totalNumber,pageNumber,currentPage,clickName, textObj) {
	var maxPageCount = 5;
	var offsetPageCount = parseInt(maxPageCount/2,10);
	var totalPageCount = totalNumber%pageNumber==0 ? parseInt(totalNumber/pageNumber,10) : parseInt(totalNumber/pageNumber,10)+1;
	
	var startPage = currentPage - offsetPageCount;
	var endPage = currentPage + offsetPageCount;
	if(startPage < 1) {
		startPage = 1;
		endPage = startPage + maxPageCount - 1;
		if(endPage > totalPageCount) {
			endPage = totalPageCount;
		}
	} else {
		if(endPage > totalPageCount) {
			endPage = totalPageCount;
			startPage = endPage - maxPageCount + 1;
			if(startPage < 1) {
				startPage = 1;
			}
		}
	}
	
	var result = '<div class="page">';
	result += '<span class="pageLink">';
	if(totalPageCount > 1) {
		if(currentPage > 1) {
			result += '<a href="#" onclick="'+clickName+'({pageNo:\''+(currentPage-1)+'\',pageNumber:\''+pageNumber+'\'});return false;">'+textObj.pagePre+'</a>';
		}
		for(var i=startPage;i<=endPage;i++) {
			if(i == currentPage) {
				result += '<strong>'+i+'</strong>';
			} else {
				result += '<a href="#" onclick="'+clickName+'({pageNo:\''+i+'\',pageNumber:\''+pageNumber+'\'});return false;">'+i+'</a>';
			}
		}
		if(currentPage < totalPageCount) {
			result += '<a href="#" onclick="'+clickName+'({pageNo:\''+(currentPage+1)+'\',pageNumber:\''+pageNumber+'\'});return false;">'+textObj.pageNext+'</a>';
		}
	}
	result += '</span>';
	result += '<span class="pageInfo">'+textObj.pageInfoPre+'<strong>'+totalNumber+'</strong>'+textObj.pageInfoEnd+'</span>'
	result += '</div>';
	return result;
}