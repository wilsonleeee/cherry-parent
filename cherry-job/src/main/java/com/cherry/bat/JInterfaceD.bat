@echo off
setlocal enabledelayedexpansion
:: JOB执行开始时间
set jobStartTime=%date:~0,10% %time%
:: JobId
set JobId= JInterfaceD
:: JOB名称
set jobName=产品，柜台、省、市，员工，部门和岗位数据过滤batch控制
call SetBatchEnv.bat
:: ------------------产品batch处理开始------------------
:: 控制最多循环次数
set i=0
:proConPre
:: 产品batch控制
java com.cherry.ProControlMain
set proCon=%ERRORLEVEL%
set /A i=%i%+1
:: 循环5次后跳出
if %i%==5 goto proConNext
:: 返回标志为3时跳出
if %proCon%==3 goto proConNext
:: 等待一分钟
ping /n 2 127.0.0.1 >nul
if %proCon%==4 goto proConPre
:: 等待五分钟后或返回标志为3时所做操作
:proConNext
if %proCon%==3 (
call SProductMain.bat
set proRtn=%ERRORLEVEL%
) else echo %date% %time% 由于接口表数据未准备好，产品batch未执行>>jobControl.txt
:: ------------------产品batch处理结束------------------
:: ------------------柜台 省 市batch处理开始------------------
:: 控制最多循环次数
set j=0
:couConPre
:: 柜台batch控制
java com.cherry.CouControlMain
set couCon=%ERRORLEVEL%
set /A j=%j%+1
:: 循环5次后跳出
if %j%==5 goto couConNext
:: 返回标志为3时跳出
if %couCon%==3 goto couConNext
:: 等待一分钟
ping /n 2 127.0.0.1 >nul
if %couCon%==4 goto couConPre
:: 等待五分钟后或返回标志为3时所做操作
:couConNext
if %couCon%==3 (
call SCounterMain.bat
) else echo %date% %time% 由于接口表数据未准备好，柜台batch未执行>>jobControl.txt
:: ------------------柜台 省 市batch处理结束------------------
:: ------------------员工batch处理开始------------------
if %couCon%==3 (
call SEmployeeMain.bat
) else echo %date% %time% 由于柜台表数据未准备好，员工batch未执行>>jobControl.txt
:: ------------------员工batch处理结束------------------
:: ------------------部门数据过滤权限batch处理开始------------------
if %couCon%==3 (
call SDepartMain.bat
) else echo %date% %time% 由于员工表数据未准备好，部门数据过滤权限batch未执行>>jobControl.txt
:: ------------------部门数据过滤权限batch处理结束------------------
:: ------------------岗位数据过滤权限batch处理开始------------------
if %couCon%==3 (
call SPositionMain.bat
) else echo %date% %time% 由于员工表数据未准备好，岗位数据过滤权限batch未执行>>jobControl.txt
:: ------------------岗位数据过滤权限batch处理结束------------------
:: JOB执行结束时间
set jobEndTime=%date:~0,10% %time%
java com.cherry.BatchLogMain %jobId% %jobName% "%jobStartTime%" "%jobEndTime%" 0 NULL NULL NULL NULL NULL

pause