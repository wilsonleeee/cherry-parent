@echo off
setlocal enabledelayedexpansion
:: JOB执行开始时间
set jobStartTime=%date:~0,10% %time%
:: JobId
set JobId=JPrmPrtD
:: JOB名称
set jobName=促销产品及活动下发batch
call SetBatchEnv.bat
:: 促销产品下发batch
call SPrmPrtMain.bat
:: 促销产品活动下发batch
call SPrmActMain.bat
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "%jobEndTime%" 0 NULL NULL NULL NULL NULL
pause