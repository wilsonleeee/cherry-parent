:: stepID
set mqStepId=SBinbemqsyn02
:: stepName
set mqStepName=MQ����ͬ������
:: PGMID
set mqPGMID=Binbemqsyn02Main
:: PGMName
set mqPGMName=MQ����ͬ������
java com.cherry.Binbemqsyn02Main
set mqConRtn=%ERRORLEVEL%
if %mqConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %mqStepId% %mqStepName% %mqPGMID% %mqPGMName% NULL
exit
) 
