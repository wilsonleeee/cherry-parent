:: stepID
set prmActStepId=SPrmPrtMain
:: stepName
set prmActStepName=������Ʒ�·�batch����
:: PGMID
set prmActPGMID=PrmPrtMain
:: PGMName
set prmActPGMName=������Ʒ�·�batch����
java com.cherry.PrmPrtMain
set prmActConRtn=%ERRORLEVEL%
if %prmActConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %prmActStepId% %prmActStepName% %prmActPGMID% %prmActPGMName% NULL
exit
)