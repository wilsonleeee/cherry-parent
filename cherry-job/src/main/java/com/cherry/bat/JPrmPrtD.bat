@echo off
setlocal enabledelayedexpansion
:: JOBִ�п�ʼʱ��
set jobStartTime=%date:~0,10% %time%
:: JobId
set JobId=JPrmPrtD
:: JOB����
set jobName=������Ʒ����·�batch
call SetBatchEnv.bat
:: ������Ʒ�·�batch
call SPrmPrtMain.bat
:: ������Ʒ��·�batch
call SPrmActMain.bat
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "%jobEndTime%" 0 NULL NULL NULL NULL NULL
pause