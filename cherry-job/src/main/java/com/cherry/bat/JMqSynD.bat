@echo off
setlocal enabledelayedexpansion
:: JOBִ�п�ʼʱ��
set jobStartTime=%date:~0,10% %time%
:: JobId
set JobId=JMqSynD
:: JOB����
set jobName=MQ��Ϣ����batch
call SetBatchEnv.bat
:: MQ����ͬ������batch
call SBinbemqsyn01.bat
:: MQ����ͬ������batch
call SBinbemqsyn02.bat
:: ������ʧ�ܵ�MQ��Ϣbatch
call SBinbemqsyn03.bat
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "%jobEndTime%" 0 NULL NULL NULL NULL NULL
pause