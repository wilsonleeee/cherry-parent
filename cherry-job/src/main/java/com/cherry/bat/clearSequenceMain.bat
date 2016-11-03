call SetBatchEnv.bat
java com.cherry.ClearSequenceMain
set rtn= %ERRORLEVEL%
echo %rtn%
IF %rtn%==0 (echo "Success")
IF %rtn%==1 (echo "Warning")
IF %rtn%==-1 (echo "Error")
pause