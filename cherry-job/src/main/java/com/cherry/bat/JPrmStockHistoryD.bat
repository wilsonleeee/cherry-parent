@echo off
setlocal enabledelayedexpansion
:: JOBִ�п�ʼʱ��
set jobStartTime=%date:~0,10% %time%
:: JobId
set JobId=JPrmStockHistoryD
:: JOB����
set jobName=������Ʒ�¶ȿ��ͳ�Ƽ�����batch����
call SetBatchEnv.bat
:: ������Ʒ�¶�ͳ��batch
call SPrmStockHistoryMain.bat
:: ������Ʒ�¶ȿ������batch
call SPrmStockReComMain.bat
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "%jobEndTime%" 0 NULL NULL NULL NULL NULL
pause