:: stepID
set depStepId=SDepartMain
:: stepName
set depStepName=部门数据过滤权限batch处理
:: PGMID
set depPGMID=Binbepldpl01Main
:: PGMName
set depPGMName=部门数据过滤权限batch处理
java com.cherry.Binbepldpl01Main
set depConRtn=%ERRORLEVEL%
if %depConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %depStepId% %depStepName% %depPGMID% %depPGMName% NULL
exit
)
