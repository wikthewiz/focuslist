rmdir dist\lib\ /q /s
xcopy lib\* dist\lib\ /S /Y

rmdir dist\resources\ /q /s
xcopy bin\resources dist\resources\ /S /Y

jar cmf Manifest.txt server_code.jar -C bin\ wikthewiz
xcopy server_code.jar dist\ /Y
rm server_code.jar
