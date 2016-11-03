call SetBatchEnv.bat
java com.cherry.BatchLogMain
set rtn= %ERRORLEVEL%
echo %rtn%
IF %rtn%==0 (echo "Success")
IF %rtn%==1 (echo "Warning")
IF %rtn%==-1 (echo "Error")
pause