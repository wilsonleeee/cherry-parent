@echo off
setlocal enabledelayedexpansion
:: JOB执行开始时间
set jobStartTime=%date:~0,10% %time%
:: JobId
set JobId=JPrmStockHistoryD
:: JOB名称
set jobName=促销产品月度库存统计及重算batch控制
call SetBatchEnv.bat
:: 促销产品月度统计batch
call SPrmStockHistoryMain.bat
:: 促销产品月度库存重算batch
call SPrmStockReComMain.bat
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "%jobEndTime%" 0 NULL NULL NULL NULL NULL
pause