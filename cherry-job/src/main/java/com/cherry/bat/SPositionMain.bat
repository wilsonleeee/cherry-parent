:: stepID
set posStepId=SPositionMain
:: stepName
set posStepName=��λ���ݹ���Ȩ��batch����
:: PGMID
set posPGMID=Binbepldpl02Main
:: PGMName
set posPGMName=��λ���ݹ���Ȩ��batch����
java com.cherry.Binbepldpl02Main
set posConRtn=%ERRORLEVEL%
if %posConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %posStepId% %posStepName% %posPGMID% %posPGMName% NULL
exit
)
