<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
	var configHelpHead = '<div class="box-yew"><div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span><s:text name="scf_configComment"></s:text></strong></div><div class="box-yew-content bg_yew"><div class="text">';
	var configHelpFoot = '</div></div></div>';
	var BINOLPLSCF01_js_i18n_configHelp = [];
	/* for(int i=0; i<92; i++){
		if(i<10){
			BINOLPLSCF01_js_i18n_configHelp[i] = '<s:text name="scf_configHelp_100'+i+'"/>';
		}else if(i<100){
			BINOLPLSCF01_js_i18n_configHelp[i] = '<s:text name="scf_configHelp_10'+i+'"/>';
		}
		
	} */
	BINOLPLSCF01_js_i18n_configHelp[7]='<s:text name="scf_configHelp_1007"/>';
	BINOLPLSCF01_js_i18n_configHelp[8]='<s:text name="scf_configHelp_1008"/>';
	BINOLPLSCF01_js_i18n_configHelp[9]='<s:text name="scf_configHelp_1009"/>';
	BINOLPLSCF01_js_i18n_configHelp[10]='<s:text name="scf_configHelp_1010"/>';
	BINOLPLSCF01_js_i18n_configHelp[11]='<s:text name="scf_configHelp_1011"/>';
	BINOLPLSCF01_js_i18n_configHelp[12]='<s:text name="scf_configHelp_1012"/>';
	BINOLPLSCF01_js_i18n_configHelp[13]='<s:text name="scf_configHelp_1013"/>';
	BINOLPLSCF01_js_i18n_configHelp[14]='<s:text name="scf_configHelp_1014"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[20]='<s:text name="scf_configHelp_1020"/>';
	BINOLPLSCF01_js_i18n_configHelp[21]='<s:text name="scf_configHelp_1021"/>';
	BINOLPLSCF01_js_i18n_configHelp[22]='<s:text name="scf_configHelp_1022"/>';
	BINOLPLSCF01_js_i18n_configHelp[23]='<s:text name="scf_configHelp_1023"/>';
	BINOLPLSCF01_js_i18n_configHelp[24]='<s:text name="scf_configHelp_1024"/>';
	BINOLPLSCF01_js_i18n_configHelp[25]='<s:text name="scf_configHelp_1025"/>';
	BINOLPLSCF01_js_i18n_configHelp[26]='<s:text name="scf_configHelp_1026"/>';
	BINOLPLSCF01_js_i18n_configHelp[27]='<s:text name="scf_configHelp_1027"/>';
	BINOLPLSCF01_js_i18n_configHelp[28]='<s:text name="scf_configHelp_1028"/>';
	BINOLPLSCF01_js_i18n_configHelp[29]='<s:text name="scf_configHelp_1029"/>';
	BINOLPLSCF01_js_i18n_configHelp[30]='<s:text name="scf_configHelp_1030"/>';
	BINOLPLSCF01_js_i18n_configHelp[31]='<s:text name="scf_configHelp_1031"/>';
	BINOLPLSCF01_js_i18n_configHelp[32]='<s:text name="scf_configHelp_1032"/>';
	BINOLPLSCF01_js_i18n_configHelp[33]='<s:text name="scf_configHelp_1033"/>';
	BINOLPLSCF01_js_i18n_configHelp[34]='<s:text name="scf_configHelp_1034"/>';
	BINOLPLSCF01_js_i18n_configHelp[35]='<s:text name="scf_configHelp_1035"/>';
	BINOLPLSCF01_js_i18n_configHelp[36]='<s:text name="scf_configHelp_1036"/>';
	BINOLPLSCF01_js_i18n_configHelp[37]='<s:text name="scf_configHelp_1037"/>';
	BINOLPLSCF01_js_i18n_configHelp[38]='<s:text name="scf_configHelp_1038"/>';
	BINOLPLSCF01_js_i18n_configHelp[39]='<s:text name="scf_configHelp_1039"/>';
	BINOLPLSCF01_js_i18n_configHelp[40]='<s:text name="scf_configHelp_1040"/>';
	BINOLPLSCF01_js_i18n_configHelp[41]='<s:text name="scf_configHelp_1041"/>';
	BINOLPLSCF01_js_i18n_configHelp[42]='<s:text name="scf_configHelp_1042"/>';
	BINOLPLSCF01_js_i18n_configHelp[43]='<s:text name="scf_configHelp_1043"/>';
	BINOLPLSCF01_js_i18n_configHelp[44]='<s:text name="scf_configHelp_1044"/>';
	BINOLPLSCF01_js_i18n_configHelp[45]='<s:text name="scf_configHelp_1045"/>';
	BINOLPLSCF01_js_i18n_configHelp[46]='<s:text name="scf_configHelp_1046"/>';
	BINOLPLSCF01_js_i18n_configHelp[47]='<s:text name="scf_configHelp_1047"/>';
	BINOLPLSCF01_js_i18n_configHelp[48]='<s:text name="scf_configHelp_1048"/>';
	BINOLPLSCF01_js_i18n_configHelp[49]='<s:text name="scf_configHelp_1049"/>';
	BINOLPLSCF01_js_i18n_configHelp[50]='<s:text name="scf_configHelp_1050"/>';
	BINOLPLSCF01_js_i18n_configHelp[51]='<s:text name="scf_configHelp_1051"/>';
	BINOLPLSCF01_js_i18n_configHelp[52]='<s:text name="scf_configHelp_1052"/>';
	BINOLPLSCF01_js_i18n_configHelp[53]='<s:text name="scf_configHelp_1053"/>';
	BINOLPLSCF01_js_i18n_configHelp[54]='<s:text name="scf_configHelp_1054"/>';
	BINOLPLSCF01_js_i18n_configHelp[55]='<s:text name="scf_configHelp_1055"/>';
	BINOLPLSCF01_js_i18n_configHelp[56]='<s:text name="scf_configHelp_1056"/>';
	BINOLPLSCF01_js_i18n_configHelp[57]='<s:text name="scf_configHelp_1057"/>';
	BINOLPLSCF01_js_i18n_configHelp[58]='<s:text name="scf_configHelp_1058"/>';
	BINOLPLSCF01_js_i18n_configHelp[59]='<s:text name="scf_configHelp_1059"/>';
	BINOLPLSCF01_js_i18n_configHelp[60]='<s:text name="scf_configHelp_1060"/>';
	BINOLPLSCF01_js_i18n_configHelp[61]='<s:text name="scf_configHelp_1061"/>';
	BINOLPLSCF01_js_i18n_configHelp[62]='<s:text name="scf_configHelp_1062"/>';
	BINOLPLSCF01_js_i18n_configHelp[63]='<s:text name="scf_configHelp_1063"/>';
	BINOLPLSCF01_js_i18n_configHelp[64]='<s:text name="scf_configHelp_1064"/>';
	BINOLPLSCF01_js_i18n_configHelp[65]='<s:text name="scf_configHelp_1065"/>';
	BINOLPLSCF01_js_i18n_configHelp[66]='<s:text name="scf_configHelp_1066"/>';
	BINOLPLSCF01_js_i18n_configHelp[67]='<s:text name="scf_configHelp_1067"/>';
	BINOLPLSCF01_js_i18n_configHelp[68]='<s:text name="scf_configHelp_1068"/>';
	BINOLPLSCF01_js_i18n_configHelp[69]='<s:text name="scf_configHelp_1069"/>';
	BINOLPLSCF01_js_i18n_configHelp[70]='<s:text name="scf_configHelp_1070"/>';
	BINOLPLSCF01_js_i18n_configHelp[71]='<s:text name="scf_configHelp_1071"/>';
	BINOLPLSCF01_js_i18n_configHelp[72]='<s:text name="scf_configHelp_1072"/>'; 
	BINOLPLSCF01_js_i18n_configHelp[73]='<s:text name="scf_configHelp_1073"/>';
	BINOLPLSCF01_js_i18n_configHelp[74]='<s:text name="scf_configHelp_1074"/>';
	BINOLPLSCF01_js_i18n_configHelp[75]='<s:text name="scf_configHelp_1075"/>';
	BINOLPLSCF01_js_i18n_configHelp[76]='<s:text name="scf_configHelp_1076"/>';
	BINOLPLSCF01_js_i18n_configHelp[77]='<s:text name="scf_configHelp_1077"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[79]='<s:text name="scf_configHelp_1079"/>';
	BINOLPLSCF01_js_i18n_configHelp[80]='<s:text name="scf_configHelp_1080"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[92]='<s:text name="scf_configHelp_1092"/>';
	BINOLPLSCF01_js_i18n_configHelp[93]='<s:text name="scf_configHelp_1093"/>';
	BINOLPLSCF01_js_i18n_configHelp[94]='<s:text name="scf_configHelp_1094"/>';
	BINOLPLSCF01_js_i18n_configHelp[96]='<s:text name="scf_configHelp_1096"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[132]='<s:text name="scf_configHelp_1132"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[288]='<s:text name="scf_configHelp_1288"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[291]='<s:text name="scf_configHelp_1291"/>';
	BINOLPLSCF01_js_i18n_configHelp[292]='<s:text name="scf_configHelp_1292"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[294]='<s:text name="scf_configHelp_1294"/>';
	BINOLPLSCF01_js_i18n_configHelp[295]='<s:text name="scf_configHelp_1295"/>';
	BINOLPLSCF01_js_i18n_configHelp[296]='<s:text name="scf_configHelp_1296"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[300]='<s:text name="scf_configHelp_1300"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[304]='<s:text name="scf_configHelp_1304"/>';
	
	BINOLPLSCF01_js_i18n_configHelp[321]='<s:text name="scf_configHelp_1321"/>';

	BINOLPLSCF01_js_i18n_configHelp[332]='<s:text name="scf_configHelp_1332"/>';
	BINOLPLSCF01_js_i18n_configHelp[333]='<s:text name="scf_configHelp_1333"/>';
	BINOLPLSCF01_js_i18n_configHelp[336]='<s:text name="scf_configHelp_1336"/>';
	
	
</script>
