:: stepID
set mqStepId=SBinbemqsyn03
:: stepName
set mqStepName=处理发送失败的MQ消息
:: PGMID
set mqPGMID=Binbemqsyn03Main
:: PGMName
set mqPGMName=处理发送失败的MQ消息
java com.cherry.Binbemqsyn03Main
set mqConRtn=%ERRORLEVEL%
if %mqConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %mqStepId% %mqStepName% %mqPGMID% %mqPGMName% NULL
exit
) 
