:: stepID
set posStepId=SPositionMain
:: stepName
set posStepName=岗位数据过滤权限batch处理
:: PGMID
set posPGMID=Binbepldpl02Main
:: PGMName
set posPGMName=岗位数据过滤权限batch处理
java com.cherry.Binbepldpl02Main
set posConRtn=%ERRORLEVEL%
if %posConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %posStepId% %posStepName% %posPGMID% %posPGMName% NULL
exit
)
