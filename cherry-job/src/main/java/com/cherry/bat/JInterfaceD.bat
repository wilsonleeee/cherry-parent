@echo off
setlocal enabledelayedexpansion
:: JOBִ�п�ʼʱ��
set jobStartTime=%date:~0,10% %time%
:: JobId
set JobId= JInterfaceD
:: JOB����
set jobName=��Ʒ����̨��ʡ���У�Ա�������ź͸�λ���ݹ���batch����
call SetBatchEnv.bat
:: ------------------��Ʒbatch����ʼ------------------
:: �������ѭ������
set i=0
:proConPre
:: ��Ʒbatch����
java com.cherry.ProControlMain
set proCon=%ERRORLEVEL%
set /A i=%i%+1
:: ѭ��5�κ�����
if %i%==5 goto proConNext
:: ���ر�־Ϊ3ʱ����
if %proCon%==3 goto proConNext
:: �ȴ�һ����
ping /n 2 127.0.0.1 >nul
if %proCon%==4 goto proConPre
:: �ȴ�����Ӻ�򷵻ر�־Ϊ3ʱ��������
:proConNext
if %proCon%==3 (
call SProductMain.bat
set proRtn=%ERRORLEVEL%
) else echo %date% %time% ���ڽӿڱ�����δ׼���ã���Ʒbatchδִ��>>jobControl.txt
:: ------------------��Ʒbatch�������------------------
:: ------------------��̨ ʡ ��batch����ʼ------------------
:: �������ѭ������
set j=0
:couConPre
:: ��̨batch����
java com.cherry.CouControlMain
set couCon=%ERRORLEVEL%
set /A j=%j%+1
:: ѭ��5�κ�����
if %j%==5 goto couConNext
:: ���ر�־Ϊ3ʱ����
if %couCon%==3 goto couConNext
:: �ȴ�һ����
ping /n 2 127.0.0.1 >nul
if %couCon%==4 goto couConPre
:: �ȴ�����Ӻ�򷵻ر�־Ϊ3ʱ��������
:couConNext
if %couCon%==3 (
call SCounterMain.bat
) else echo %date% %time% ���ڽӿڱ�����δ׼���ã���̨batchδִ��>>jobControl.txt
:: ------------------��̨ ʡ ��batch�������------------------
:: ------------------Ա��batch����ʼ------------------
if %couCon%==3 (
call SEmployeeMain.bat
) else echo %date% %time% ���ڹ�̨������δ׼���ã�Ա��batchδִ��>>jobControl.txt
:: ------------------Ա��batch�������------------------
:: ------------------�������ݹ���Ȩ��batch����ʼ------------------
if %couCon%==3 (
call SDepartMain.bat
) else echo %date% %time% ����Ա��������δ׼���ã��������ݹ���Ȩ��batchδִ��>>jobControl.txt
:: ------------------�������ݹ���Ȩ��batch�������------------------
:: ------------------��λ���ݹ���Ȩ��batch����ʼ------------------
if %couCon%==3 (
call SPositionMain.bat
) else echo %date% %time% ����Ա��������δ׼���ã���λ���ݹ���Ȩ��batchδִ��>>jobControl.txt
:: ------------------��λ���ݹ���Ȩ��batch�������------------------
:: JOBִ�н���ʱ��
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "%jobEndTime%" 0 NULL NULL NULL NULL NULL

pause