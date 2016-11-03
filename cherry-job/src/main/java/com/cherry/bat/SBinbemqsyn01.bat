:: stepID
set mqStepId=SBinbemqsyn01
:: stepName
set mqStepName=MQ接收同步处理
:: PGMID
set mqPGMID=Binbemqsyn01Main
:: PGMName
set mqPGMName=MQ接收同步处理
java com.cherry.Binbemqsyn01Main
set mqConRtn=%ERRORLEVEL%
if %mqConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %mqStepId% %mqStepName% %mqPGMID% %mqPGMName% NULL
exit
) 
