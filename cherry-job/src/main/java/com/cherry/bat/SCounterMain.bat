:: stepID
set couStepId=SCounterMain
:: stepName
set couStepName=��̨batch����
:: PGMID
set couPGMID=CounterMain
:: PGMName
set couPGMName=��̨batch����
java com.cherry.CounterMain
set couConRtn=%ERRORLEVEL%
if %couConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %couStepId% %couStepName% %couPGMID% %couPGMName% NULL
exit
) else (
:: �޸�jobDate
java com.cherry.setSucFlagControlMain Counters
)
