:: stepID
set depStepId=SDepartMain
:: stepName
set depStepName=�������ݹ���Ȩ��batch����
:: PGMID
set depPGMID=Binbepldpl01Main
:: PGMName
set depPGMName=�������ݹ���Ȩ��batch����
java com.cherry.Binbepldpl01Main
set depConRtn=%ERRORLEVEL%
if %depConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %depStepId% %depStepName% %depPGMID% %depPGMName% NULL
exit
)
