@echo off
setlocal enabledelayedexpansion
:: JOB执行开始时间
set jobStartTime=%date:~0,10% %time%
:: JobId
set JobId=JMqSynD
:: JOB名称
set jobName=MQ消息处理batch
call SetBatchEnv.bat
:: MQ接收同步处理batch
call SBinbemqsyn01.bat
:: MQ发送同步处理batch
call SBinbemqsyn02.bat
:: 处理发送失败的MQ消息batch
call SBinbemqsyn03.bat
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "%jobEndTime%" 0 NULL NULL NULL NULL NULL
pause