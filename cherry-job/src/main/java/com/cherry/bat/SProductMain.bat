:: stepID
set proStepId=SProductMain
:: stepName
set proStepName=产品batch处理
:: PGMID
set proPGMID=ProductMain
:: PGMName
set proPGMName=产品batch处理
java com.cherry.ProductMain
set proConRtn=%ERRORLEVEL%
if %proConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %proStepId% %proStepName% %proPGMID% %proPGMName% NULL
exit
) else (
:: 修改jobDate
java com.cherry.setSucFlagControlMain Product
)
