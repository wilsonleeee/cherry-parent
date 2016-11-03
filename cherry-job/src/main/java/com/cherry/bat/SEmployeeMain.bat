:: stepID
set empStepId=SEmployeeMain
:: stepName
set empStepName=员工batch处理
:: PGMID
set empPGMID=EmployeeMain
:: PGMName
set empPGMName=员工batch处理
java com.cherry.EmployeeMain
set empConRtn=%ERRORLEVEL%
if %empConRtn%==-1 (
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %empStepId% %empStepName% %empPGMID% %empPGMName% NULL
exit
)
