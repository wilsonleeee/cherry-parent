<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM88_3.js"></script>
<script>
$(document).ready(function() {
	PRM88.getActGrpInfo('#prmActGrp');
});
</script>
<@ruleBaseInfo page=pageA temp=pageTemp/>
<@timePlaceInfo page=pageB temp=pageTemp/>
<@memberInfo page=pageC temp=pageTemp/>
<@ruleDetailInfo page=pageD temp=pageTemp/>
<#include '/WEB-INF/ftl/common/dataTable_i18n.ftl'/>