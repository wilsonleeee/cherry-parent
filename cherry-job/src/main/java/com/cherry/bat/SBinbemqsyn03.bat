:: stepID
set mqStepId=SBinbemqsyn03
:: stepName
set mqStepName=������ʧ�ܵ�MQ��Ϣ
:: PGMID
set mqPGMID=Binbemqsyn03Main
:: PGMName
set mqPGMName=������ʧ�ܵ�MQ��Ϣ
java com.cherry.Binbemqsyn03Main
set mqConRtn=%ERRORLEVEL%
if %mqConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %mqStepId% %mqStepName% %mqPGMID% %mqPGMName% NULL
exit
) 
