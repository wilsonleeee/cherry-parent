:: stepID
set empStepId=SEmployeeMain
:: stepName
set empStepName=Ա��batch����
:: PGMID
set empPGMID=EmployeeMain
:: PGMName
set empPGMName=Ա��batch����
java com.cherry.EmployeeMain
set empConRtn=%ERRORLEVEL%
if %empConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %empStepId% %empStepName% %empPGMID% %empPGMName% NULL
exit
)
