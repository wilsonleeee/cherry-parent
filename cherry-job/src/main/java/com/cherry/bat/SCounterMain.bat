:: stepID
set couStepId=SCounterMain
:: stepName
set couStepName=柜台batch处理
:: PGMID
set couPGMID=CounterMain
:: PGMName
set couPGMName=柜台batch处理
java com.cherry.CounterMain
set couConRtn=%ERRORLEVEL%
if %couConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %couStepId% %couStepName% %couPGMID% %couPGMName% NULL
exit
) else (
:: 修改jobDate
java com.cherry.setSucFlagControlMain Counters
)
