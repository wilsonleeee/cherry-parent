/**
 * 角色人员关系查询
 * 
 * @author zhanggl
 * 
 */
function BINOLPLRLA05() {
}

BINOLPLRLA05.prototype = {
	"html" : null,
	/**
	 * 根据员工查询角色画面 取得员工LIST
	 * 
	 */
	"getEmployees" : function(obj) {
		// oTableArr[0] = null;
		$("#tabs-1").find("#section").show();
		var $this = $(obj);
		var $form = $("#mainForm_FindRoles");
		// 表单验证
		if (!$form.valid()) {
			return;
		}
		var params = $form.serialize();
		var url = $("#getEmployees").val() + "?" + params + "&csrftoken="
				+ $("#csrftoken").val();
		// alert(url);
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable_FindRoles',
			// 数据URL
			url : url,
			// 表格列属性设置
			// 表格默认排序
			aaSorting : [ [ 1, "asc" ] ],
			aoColumns : [ {
				"sName" : "no.",
				"sWidth" : "5%",
				"bSortable" : false
			}, // 0
					{
						"sName" : "EmployeeCode",
						"sWidth" : "20%"
					}, // 1
					{
						"sName" : "LonginName",
						"sWidth" : "15%"
					}, // 2
					{
						"sName" : "DepartCode",
						"sWidth" : "23%"
					}, // 4
					{
						"sName" : "CategoryName",
						"sWidth" : "12%"
					}, // 5
					{
						"sName" : "BrandCode",
						"sWidth" : "15%",
						"bVisible" : false
					}, // 6
					{
						"sName" : "BIN_EmployeeID",
						"sWidth" : "10%",
						"bSortable" : false
					} ], // 7
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
//			fixedColumns : 2,
			index : 0
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},

	/**
	 * 根据角色查询员工画面 取得角色LIST
	 * 
	 */
	"getRoles" : function(obj) {
		$("#tabs-2").find("#section").show();
		var $this = $(obj);
		var $this = $(obj);
		var $form = $("#mainForm_FindEmplyees");
		// 表单验证
		if (!$form.valid()) {
			return;
		}
		var params = $form.serialize();
		var url = $("#getRoles").val() + "?" + params + "&csrftoken="
				+ $("#csrftoken").val();
		// alert(url);
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable_FindEmployee',
			// 数据URL
			url : url,
			// 表格列属性设置
			// 表格默认排序
			aaSorting : [ [ 2, "asc" ] ],
			aoColumns : [ {
				"sName" : "no.",
				"sWidth" : "5%",
				"bSortable" : false
			}, // 0
					{
						"sName" : "RoleName",
						"sWidth" : "20%"
					}, // 1
					{
						"sName" : "RoleKind",
						"sWidth" : "15%"
					}, // 2
					{
						"sName" : "BrandCode",
						"sWidth" : "15%",
						"bVisible" : false
					}, // 6
					{
						"sName" : "BIN_RoleID",
						"sWidth" : "10%",
						"bSortable" : false
					} ], // 7
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
//			fixedColumns : 2,
			index : 1
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},

	/**
	 * 根据员工ID查询其所对应的角色（包括部门、岗位以及用户角色）
	 * 
	 * 
	 */
	"getRolesByEmployee" : function(obj) {
		var $this = $(obj);
		var employeeId = $this.prop("id");
		var url = $("#getRolesByEmployee").val();
		var param = "employeeId=" + employeeId;
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		var dialogSetting = {
			dialogInit : "#popRoleTable",
			width : 700,
			height : 350,
			title : $("#RolesTitle").text(),
			confirm : $("#dialogClose").text(),
			confirmEvent : function() {
				removeDialog("#popRoleTable");
			}
		};
		openDialog(dialogSetting);

		var callback = function(msg) {
			$("#popRoleTable").html(msg);
		};

		cherryAjaxRequest( {
			url : url,
			param : param,
			callback : callback
		});
	},

	/**
	 * 根据角色取得拥有该角色的员工
	 * 
	 */
	"getEmployeesByRole" : function(obj) {
		var that = this;
		var $this = $(obj);
		var roleId = $this.prop("id");
		var roleKind = $this.prop("name");
		var text = $("#popEmployeeTable").html();
		var dialogSetting = {
			dialogInit : "#popEmployeeTable",
			width : 700,
			height : 350,
			text : text,
			title : $("#EmployeesTitle").text(),
			confirm : $("#dialogClose").text(),
			confirmEvent : function() {
				removeDialog("#popEmployeeTable");
				$("#popEmployeeTable").html(that.html);
				that.html = null;
			},
			closeEvent : function(event, ui) {
				removeDialog("#popEmployeeTable");
				$("#popEmployeeTable").html(that.html);
				that.html = null;
			}
		};
		that.html = text;
		openDialog(dialogSetting);
		$("#popEmployeeTable").find("#roleId").val(roleId);
		$("#popEmployeeTable").find("#roleKind").val(roleKind);

		var params = "roleId=" + $("#popEmployeeTable").find("#roleId").val()
				+ "&roleKind=" + $("#popEmployeeTable").find("#roleKind").val()
				+ "&csrftoken=" + $("#csrftoken").val() + "&brandInfoId="
				+ $("#mainForm_FindEmplyees").find("#brandInfoId1").val();
		var url = $("#getEmployeesByRole").val() + "?" + params;
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#popEmployeeDataTable',
			// 数据URL
			url : url,
			// 表格列属性设置
			// 表格默认排序
			aaSorting : [ [ 1, "asc" ] ],
			aoColumns : [ {
				"sName" : "no.",
				"sWidth" : "5%",
				"bSortable" : false
			}, // 0
					{
						"sName" : "EmployeeCode",
						"sWidth" : "20%"
					}, // 1
					{
						"sName" : "DepartCode",
						"sWidth" : "23%"
					}, // 4
					{
						"sName" : "CategoryName",
						"sWidth" : "12%"
					} ], // 5

			// 不可设置显示或隐藏的列
			aiExclude : [ 0 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			index : 12,
			iDisplayLength : 5
		};
		oTableArr[12] = null;
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	/**
	 * 根据员工ID查询其所拥有的菜单资源
	 * 
	 * 
	 */
	"getMenusByEmployee" : function(obj) {
		var $this = $(obj);
		var employeeId = $this.prop("id");
		var url = $("#getMenusByEmployee").val();
		var param = "employeeId=" + employeeId;
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		var dialogSetting = {
			dialogInit : "#popRoleTable",
			width : 800,
			height : 600,
			title : $("#ResourceTitle").text(),
			confirm : $("#dialogClose").text(),
			confirmEvent : function() {
				removeDialog("#popRoleTable");
			}
		};
		openDialog(dialogSetting);

		var callback = function(msg) {
			$("#popRoleTable").html(msg);
		};

		cherryAjaxRequest( {
			url : url,
			param : param,
			callback : callback
		});
	}

};

var binOLPLRLA05 = new BINOLPLRLA05();

$(document).ready(function() {
	$('.tabs').tabs();

	employeeBinding( {
		elementId : "employeeCode",
		selected : "code",
		privilegeFlag : "1"
	});

	employeeBinding( {
		elementId : "employeeName",
		selected : "name",
		privilegeFlag : "1"
	});
});