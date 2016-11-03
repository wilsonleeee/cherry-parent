:: stepID
set mqStepId=SBinbemqsyn02
:: stepName
set mqStepName=MQ发送同步处理
:: PGMID
set mqPGMID=Binbemqsyn02Main
:: PGMName
set mqPGMName=MQ发送同步处理
java com.cherry.Binbemqsyn02Main
set mqConRtn=%ERRORLEVEL%
if %mqConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %mqStepId% %mqStepName% %mqPGMID% %mqPGMName% NULL
exit
) 
