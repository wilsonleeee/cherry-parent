<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="js/common/commonAjax.js"></script>
<script type="text/javascript" src="js/common/cherry.js"></script>


<script type="text/javascript">

	function doBatch(url, object) {
        //促销产品库存同步 产品库存同步 平产品库存
        if(url.indexOf("BINBESSPRM05_init.action")!=-1 || url.indexOf("BINBESSPRO03_init.action")!=-1 || url.indexOf("BINBESSPRO04_init.action")!=-1||url.indexOf("BINBESSPRM06_init.action")!=-1){
            url = url+ "?brandInfoId="+$("#brandInfoId").val();
        	window.open(url,"","scrollbars=yes,childModel=1");
        } else if(url.indexOf("BINBESSPRM01_init.action")!=-1 || url.indexOf("BINBESSPRO01_init.action")!=-1 || url.indexOf("BINBESSRPS01_init.action")!=-1 || url.indexOf("BINBEIFEMP04_init.action")!=-1
        		|| url.indexOf("BINBEDRHAN04_init.action")!=-1 || url.indexOf("BINBEMBARC02_init.action")!=-1 || url.indexOf("BINBEMBARC03_init.action")!=-1
        		|| url.indexOf("BINBEMBARC07_init.action")!=-1 || url.indexOf("BINBEMBLEL02_init.action")!=-1 || url.indexOf("BINBEMBLEL03_init.action")!=-1
        		|| url.indexOf("BINBEMBTIF01_init.action")!=-1 || url.indexOf("BINBEMBTIF02_init.action")!=-1 || url.indexOf("BINBEMBTIF03_init.action")!=-1
        		|| url.indexOf("BINBEMBTIF04_init.action")!=-1 || url.indexOf("BINBEMBTIF05_init.action")!=-1 || url.indexOf("BINBEMBTIF06_init.action")!=-1
        		|| url.indexOf("BINBAT141_init.action")!=-1 || url.indexOf("BINCPMEACT04_init.action")!=-1 || url.indexOf("BINCPMEACT02_init.action")!=-1 
        		|| url.indexOf("BINBAT122_init.action")!=-1
        		|| url.indexOf("BINBAT124_init.action")!=-1
				|| url.indexOf("BINBAT125_init.action")!=-1
        		|| url.indexOf("BINBECTSMG09_init.action")!=-1 || url.indexOf("BINBECTSMG10_init.action")!=-1
        		|| url.indexOf("BINBESSPRM08_init.action")!=-1) {
        	url = url + "?" + $('#batchListForm').serialize();
        	window.open(url,"","height=800,width=1000,scrollbars=yes,childModel=1");
        } else if(url.indexOf("BINBEMBVIS02_init.action")!= -1) {
        	url = url + "?" + $('#batchListForm').serialize();
        	window.open(url,"","height=800,width=1000,scrollbars=yes,childModel=1");
        } else if(url.indexOf("BINBECTSMG02_searchJobList.action") != -1){
        	url = url + "?" + $('#batchListForm').serialize();
        	window.open(url,"","height=800,width=1000,scrollbars=yes,childModel=1");
        } else if(url.indexOf("BINBEPTRPS01_init.action") != -1){
        	url = url + "?" + $('#batchListForm').serialize();
        	window.open(url,"","height=800,width=1000,scrollbars=yes,childModel=1");
        }else if(url.indexOf("BINBETLBAT08_init.action") != -1){
        	// url = url + "?" + $('#batchListForm').serialize();
        	window.open(url,"","height=800,width=1000,scrollbars=yes,childModel=1");
        }else{
            var batchName = $(object).next().text();
            if(confirm("您确定要执行【"+batchName+"】BATCH吗？")) {
                $("#actionResultDisplay").empty();
                var param = $('#batchListForm').serialize();
                $.ajax({
                    url :url, 
                    type:'post',
                    dataType:'html',
                    data:param,
                    success:function(msg){
                        $('#actionResultDisplay').html(msg);
                        if(url.indexOf("BINBETLBAT04_init.action")!=-1){
                            location.replace(location);
                        }
                    }
                 });
            }
        }
	}
</script>
<form id="batchListForm">
<s:hidden name="brandInfoId"></s:hidden>
<s:hidden name="brandCode"></s:hidden>
</form>
<div class="section">
	<div class="section-content">
		<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
	  		<thead>
	    		<tr>
	      			<th width="20%">Batch代号</th>
	      			<th width="80%">Batch名称</th>
	    		</tr>
	  		</thead>
	  		<tbody>
	  			<s:iterator value="batchList" id="batchMap" status="status">
	  				<tr class='<s:if test="%{#status.index%2 == 0}">odd</s:if><s:else>even</s:else>'>
	  					<td><s:property value="batchCd"/></td>
	  					<s:url action='%{#batchMap.urlInfo}' id="doBatchUrl"></s:url>
	  					<td>
		  					<a class="add" onclick="doBatch('${doBatchUrl }',this);return false;">
		  						<span class="ui-icon icon-enable"></span><span class="button-text">执行</span>
		  					</a>
		  					<span><s:property value="batchName"/></span>
	  					</td>
	  				</tr>
	  			</s:iterator>
	  		</tbody>
		</table>
 	</div>
</div>
