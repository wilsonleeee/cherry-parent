:: stepID
set proStepId=SProductMain
:: stepName
set proStepName=��Ʒbatch����
:: PGMID
set proPGMID=ProductMain
:: PGMName
set proPGMName=��Ʒbatch����
java com.cherry.ProductMain
set proConRtn=%ERRORLEVEL%
if %proConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %proStepId% %proStepName% %proPGMID% %proPGMName% NULL
exit
) else (
:: �޸�jobDate
java com.cherry.setSucFlagControlMain Product
)
