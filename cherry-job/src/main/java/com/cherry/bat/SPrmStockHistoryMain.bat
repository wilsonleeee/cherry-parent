:: stepID
set prmStockStepId=SPrmStockHistoryMain
:: stepName
set prmStockStepName=促销产品月度统计batch处理
:: PGMID
set prmStockPGMID=PrmStockHistoryMain
:: PGMName
set prmStockPGMName=促销产品月度统计batch处理
java com.cherry.PrmStockHistoryMain
set prmStockConRtn=%ERRORLEVEL%
if %prmStockConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %prmStockStepId% %prmStockStepName% %prmStockPGMID% %prmStockPGMName% NULL
exit
)