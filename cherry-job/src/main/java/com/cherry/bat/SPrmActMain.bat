:: stepID
set prmActStepId=SPrmActMain
:: stepName
set prmActStepName=������·�batch����
:: PGMID
set prmActPGMID=PromotionActiveMain
:: PGMName
set prmActPGMName=������·�batch����
java com.cherry.PromotionActiveMain
set prmActConRtn=%ERRORLEVEL%
if %prmActConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %prmActStepId% %prmActStepName% %prmActPGMID% %prmActPGMName% NULL
exit
)