echo off
rem **********************************************
rem ���[�U�ݒ�
rem USER_ID:���O�C�����[�UID
rem USER_PASSWORD:���O�C���p�X���[�h
rem CSV_FILES_DIR:CSV�t�@�C���u����
rem TARGET_CSV_FILES:�Ώۃt�@�C����(CSV�`���ŕ����w�� ���w��:�S��)
rem **********************************************
set USER_ID=tsutomu.kobayashi@mobcast.jp
set USER_PASSWORD=mobcast!
set CSV_FILES_DIR=".\exp_csv"
set TARGET_CSV_FILES=

rem **********************************************
rem �����ؑ�
rem import:�C���|�[�g�̂�
rem import-clear:�C���|�[�g+�L���b�V���S�N���A
rem export:�G�N�X�|�[�g
rem **********************************************
set EXEC_TYPE=export

rem **********************************************
rem ���ؑ�
rem dev:�J���p
rem sand:sandbox
rem stag:�X�e�[�W���O
rem expo:�R��
rem conf.properties��"mpz.admin.url.***"��***�̕������w��
rem **********************************************
set ADMIN_TOOL=dev
set MSG=dev
set JAR_NAME=mpz-admin-client-csv-1.0.1.jar

SET /P ANSWER=%MSG%����CSV���G�N�X�|�[�g���܂��B��낵���ł��� (Y/N)?
if /i {%ANSWER%}=={y} (goto :YES)
if /i {%ANSWER%}=={yes} (goto :YES)
if /i {%ANSWER%}=={n} (goto :NO)
if /i {%ANSWER%}=={no} (goto :NO)

:YES
"jre1.6\bin\java.exe" -jar %JAR_NAME% %ADMIN_TOOL% %EXEC_TYPE% %USER_ID% %USER_PASSWORD% %CSV_FILES_DIR% %TARGET_CSV_FILES%
pause
:NO
