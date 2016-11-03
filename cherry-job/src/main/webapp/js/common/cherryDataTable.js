var oTableArr = new Array(null,null);
var fixedColArr = new Array(null,null);
$.fn.dataTableExt.oPagination.input = {
		/*
		 * Function: oPagination.input.fnInit
		 * Purpose:  Initalise dom elements required for pagination with input textbox
		 * Returns:  -
		 * Inputs:   object:oSettings - dataTables settings object
		 *           node:nPaging - the DIV which contains this pagination control
		 *           function:fnCallbackDraw - draw function which must be called on update
		 */
		"fnInit": function ( oSettings, nPaging, fnCallbackDraw )
		{
			var nFirst = document.createElement( 'span' );
			var nPrevious = document.createElement( 'a' );
			var nNext = document.createElement( 'a' );
			var nLast = document.createElement( 'span' );
			var nInput = document.createElement( 'input' );
			var nGo = document.createElement( 'a' );
			var nPage = document.createElement( 'span' );
			
			nFirst.innerHTML = oSettings.oLanguage.oPaginate.sFirst;
			nPrevious.innerHTML = oSettings.oLanguage.oPaginate.sPrevious;
			nNext.innerHTML = oSettings.oLanguage.oPaginate.sNext;
			nLast.innerHTML = oSettings.oLanguage.oPaginate.sLast;
			
			nFirst.className = "paginate_button first";
			nPrevious.className = "paginate_button previous movel";
			nNext.className="paginate_button next mover";
			nLast.className = "paginate_button last";
			nGo.className = "paginate_button gobutton";
			nPage.className = "paginate_page";
			nGo.innerHTML = "<span class='button-text'>GO</span>";
			
			if ( oSettings.sTableId !== '' )
			{
				nPaging.setAttribute( 'id', oSettings.sTableId+'_paginate' );
				nPrevious.setAttribute( 'id', oSettings.sTableId+'_previous' );
				nNext.setAttribute( 'id', oSettings.sTableId+'_next' );
				nLast.setAttribute( 'id', oSettings.sTableId+'_last' );
			}
			
			nInput.type = "text";
			nInput.style.width = "40px";
			nInput.style.display = "inline";
			// dataTable国际化 "转到:"信息
			nPage.innerHTML = $("#table_sGoto").text() + " ";
			
			nPaging.appendChild( nPage );
			nPaging.appendChild( nInput );
			nPaging.appendChild( nGo );
			nPaging.appendChild( nFirst );
			nPaging.appendChild( nPrevious );
			nPaging.appendChild( nNext );
			nPaging.appendChild( nLast );
			
			$(nFirst).click( function () {
				var flag = oSettings.oApi._fnPageChange( oSettings, "first" );
				if(flag){
					fnCallbackDraw( oSettings );
				}
			} );
			
			$(nPrevious).click( function() {
				var flag = oSettings.oApi._fnPageChange( oSettings, "previous" );
				if(flag){
					fnCallbackDraw( oSettings );
				}
			} );
			
			$(nNext).click( function() {
				var flag = oSettings.oApi._fnPageChange( oSettings, "next" );
				if(flag){
					fnCallbackDraw( oSettings );
				}
			} );
			
			$(nLast).click( function() {
				var flag = oSettings.oApi._fnPageChange( oSettings, "last" );
				if(flag){
					fnCallbackDraw( oSettings );
				}
			} );
			$(nGo).click( function () {
				var iOldStart = oSettings._iDisplayStart;
				var pageNum = parseInt($(nInput).val());
				if(isNaN(pageNum)){
					pageNum = 1;
					$(nInput).val(1);
				}
				var iNewStart = oSettings._iDisplayLength * (pageNum - 1);
				if ( iNewStart >= oSettings.fnRecordsDisplay() )
				{
					/* Display overrun */
					iNewStart = (Math.ceil(oSettings.fnRecordsDisplay() / 
						oSettings._iDisplayLength)-1) * oSettings._iDisplayLength;
					oSettings._iDisplayStart = iNewStart;
				}
				if(iNewStart < 0){
					iNewStart = 0;
					oSettings._iDisplayStart = 0;
				}else {
					oSettings._iDisplayStart = iNewStart;
				}
				
				if(iOldStart != iNewStart){
					fnCallbackDraw( oSettings );
				}
			} );
			/* Take the brutal approach to cancelling text selection */
			$('span', nPaging).bind( 'mousedown', function () { return false; } );
			$('span', nPaging).bind( 'selectstart', function () { return false; } );
		},
		
		/*
		 * Function: oPagination.input.fnUpdate
		 * Purpose:  Update the input element
		 * Returns:  -
		 * Inputs:   object:oSettings - dataTables settings object
		 *           function:fnCallbackDraw - draw function which must be called on update
		 */
		"fnUpdate": function ( oSettings, fnCallbackDraw )
		{
			if ( !oSettings.aanFeatures.p )
			{
				return;
			}
			var iCurrentPage = Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength) + 1;
			
			/* Loop over each instance of the pager */
			var an = oSettings.aanFeatures.p;
			for ( var i=0, iLen=an.length ; i<iLen ; i++ )
			{
				var spans = an[i].getElementsByTagName('span');
				var inputs = an[i].getElementsByTagName('input');
				inputs[0].value = iCurrentPage;
			}
			
			// IE7 表格和滚动条之间空隙问题修正
//			$(".dataTables_scrollBody").attr("style","zoom:1; overflow-x:auto; overflow-y:hidden;width:100%;");
		}
	};

// 获取dateTable表格
// tableSetting表格属性对象
function getTable(tableSetting){
	// === dataTable汉化信息取得  START === //
	var sProcessing = $("#table_sProcessing").text();
	var sLengthMenu = $("#table_sLengthMenu").text();
	var sZeroRecords = $("#table_sZeroRecords").text();
	var sInfo_1 = $("#table_sInfo_1").text();
	var sInfo_2 = $("#table_sInfo_2").text();
	var sInfo_3 = $("#table_sInfo_3").text();
	var sInfoEmpty = $("#table_sInfoEmpty").text();
	var sFirst = $("#table_sFirst").text();
	var sLast = $("#table_sLast").text();
	// === dataTable汉化信息取得   END === //
	
	//  ============== 默认值设置开始  ============== //
	var iDisplayLength = 25;
	var fixedColumns = 0;
	var sScrollXInner ="";
	var bAutoWidth = false;
	var sScrollX = "";
	var aiExclude = [];
	var aaSorting = [[1, "asc"]];
	var aLengthMenu = [[5, 10, 25, 50], [5, 10, 25, 50]];
	// datatable 对象索引,默认为0
	var index = 0;
	
	if(tableSetting.iDisplayLength != null){
		iDisplayLength = tableSetting.iDisplayLength;
	}
	if(tableSetting.fixedColumns != null){
		fixedColumns = tableSetting.fixedColumns;
	}
	if(tableSetting.sScrollXInner != null){
		sScrollXInner = tableSetting.sScrollXInner;
	}
	if (tableSetting.bAutoWidth !=null){
		bAutoWidth = tableSetting.bAutoWidth;
	}
	if(tableSetting.sScrollX != null){
		sScrollX = tableSetting.sScrollX;
	}
	if(tableSetting.aiExclude != null){
		aiExclude = tableSetting.aiExclude;
	}
	if(tableSetting.aaSorting != null){
		aaSorting = tableSetting.aaSorting;
	}
	if(tableSetting.aLengthMenu != null){
		aLengthMenu = tableSetting.aLengthMenu;
	}
	if (tableSetting.index !=null){
		index = tableSetting.index;
	}
	//  ============== 默认值设置结束  ============== //
	if(oTableArr[index] == null){
		 // 表格初始化
		oTableArr[index] = $(tableSetting.tableId).dataTable( {
		   // 加载数据时显示正在加载信息
		    "bProcessing": true,
		    // 服务器端获取数据
			"bServerSide": true,
			// 服务器端获取数据URL
		    "sAjaxSource": tableSetting.url,
			// 表格默认排序
			"aaSorting": aaSorting,
			// 表格及控件布局
			"sDom": 't<"toolbar_bottom clearfix"<"left"i><"right"lp>>C<"clear">r',
			// 使用JQUERY UI样式
			"bJQueryUI": true,
			// 不自动计算列宽度 
			"bAutoWidth": bAutoWidth,
			"sPaginationType": "input",
			// 每页显示记录数选择
			"aLengthMenu": aLengthMenu,
			// 汉化
			"oLanguage": {
		   		"sProcessing": sProcessing,
				"sLengthMenu": sLengthMenu + " _MENU_ ",
				"sZeroRecords": sZeroRecords,
				"sInfo": sInfo_1 + " <strong class='highlight'> _TOTAL_ </strong> "+sInfo_2+" _PAGES_ "+sInfo_3,
				"sInfoEmpty": sInfoEmpty,
				"oPaginate": {
					"sFirst": sFirst,
					"sPrevious": "<span class='ui-icon-triangle-1-w ui-icon'></span>",
					"sNext": "<span class='ui-icon-triangle-1-e ui-icon'></span>",
					"sLast": sLast
				}
			},
			// 每页显示记录数
			"iDisplayLength": iDisplayLength,
			// 设置表格列名，方便后台取得进行排序
			"aoColumns": tableSetting.aoColumns,
			// 不可设置显示或隐藏的列
			"oColVis": {"aiExclude": aiExclude }, 
			// 横向滚动条出现的临界宽度
			"sScrollX": sScrollX,
			// 滚动体的宽度
			"sScrollXInner": sScrollXInner,
			// datatable返回函数
			"callbackFun" : tableSetting.callbackFun,
			// 是否开启列选择功能 (默认为开启)
			"colVisFlag" : tableSetting.colVisFlag,
			"fnDrawCallback" : tableSetting.fnDrawCallback
		} );
	 }else{
		 var oSettings = oTableArr[index].fnSettings();
		 oSettings.sAjaxSource = tableSetting.url;
		 // 刷新表格数据
		 oTableArr[index].fnDraw();
	 }
	 if (fixedColArr[index] == null){
		// 设置固定列
		fixedColArr[index] = new FixedColumns( oTableArr[index], {
	 		"columns": fixedColumns
	 	} );
	 }
}

function datatableFilter (thisObj,tableIndex){
	var index;
	if (tableIndex!=null && tableIndex!=""){
		index = tableIndex;
	}else{
		index = popDataTable_global.index;
	}
	if (index==null){
		index = 0;
	}
	// 数据过滤
	oTableArr[index].fnFilter($(thisObj).val());
 }