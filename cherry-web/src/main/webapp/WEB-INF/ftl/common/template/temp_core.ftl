<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<#-- 活动条件确认  -->
	<#if camTemp.tempCode == "BUS000003">
	<div class="box4">
          <div class="box4-header"><strong><@s.text name="bInfo" /></strong></div>
          <div class="box4-content">
            <table border="0" class="clean">
              <tr>
                <th scope="row"><@s.text name="actName" /></th>
                <td>${(campInfo.campaignName)!?html}</td>
              </tr>
              <tr>
                <th scope="row"><@s.text name="brand" /></th>
                <td>${(campInfo.brandName)!?html}</td>
              </tr>
              <tr>
                <th scope="row"><@s.text name="creater" /></th>
                <td>${(campInfo.campaignSetByName)!?html}</td>
              </tr>
              <tr>
                <th scope="row"><@s.text name="actDel" /></th>
                <td>${(campInfo.descriptionDtl)!?html} </td>
              </tr>
            </table>
          </div>
        </div>
        <div class="box4">
          <div class="box4-header"><strong><@s.text name="delInfo" /></strong></div>
          <div class="box4-content">
            <table border="0" class="clean">
              <#list camTemp.combTemps as combTemp>
		          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
		        </#list>
            </table>
          </div>
        </div>
	</#if>
</@s.i18n>