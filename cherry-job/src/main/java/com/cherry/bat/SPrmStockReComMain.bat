:: stepID
set prmStockStepId=SPrmStockReComMain
:: stepName
set prmStockStepName=������Ʒ�¶�ͳ������batch����
:: PGMID
set prmStockPGMID=PrmStockReComMain
:: PGMName
set prmStockPGMName=������Ʒ�¶�ͳ������batch����
java com.cherry.PrmStockReComMain
set prmStockConRtn=%ERRORLEVEL%
if %prmStockConRtn%==-1 (
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "!jobEndTime!" 1 %prmStockStepId% %prmStockStepName% %prmStockPGMID% %prmStockPGMName% NULL
exit
)