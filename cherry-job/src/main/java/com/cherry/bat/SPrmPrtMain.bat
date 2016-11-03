:: stepID
set prmActStepId=SPrmPrtMain
:: stepName
set prmActStepName=促销产品下发batch处理
:: PGMID
set prmActPGMID=PrmPrtMain
:: PGMName
set prmActPGMName=促销产品下发batch处理
java com.cherry.PrmPrtMain
set prmActConRtn=%ERRORLEVEL%
if %prmActConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %prmActStepId% %prmActStepName% %prmActPGMID% %prmActPGMName% NULL
exit
)