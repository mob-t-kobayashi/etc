echo off
rem **********************************************
rem ユーザ設定
rem USER_ID:ログインユーザID
rem USER_PASSWORD:ログインパスワード
rem CSV_FILES_DIR:CSVファイル置き場
rem TARGET_CSV_FILES:対象ファイル名(CSV形式で複数指定 未指定:全て)
rem **********************************************
set USER_ID=tsutomu.kobayashi@mobcast.jp
set USER_PASSWORD=mobcast!
set CSV_FILES_DIR=".\exp_csv"
set TARGET_CSV_FILES=

rem **********************************************
rem 処理切替
rem import:インポートのみ
rem import-clear:インポート+キャッシュ全クリア
rem export:エクスポート
rem **********************************************
set EXEC_TYPE=export

rem **********************************************
rem 環境切替
rem dev:開発用
rem sand:sandbox
rem stag:ステージング
rem expo:審査
rem conf.propertiesの"mpz.admin.url.***"の***の部分を指定
rem **********************************************
set ADMIN_TOOL=dev
set MSG=dev
set JAR_NAME=mpz-admin-client-csv-1.0.1.jar

SET /P ANSWER=%MSG%からCSVをエクスポートします。よろしいですか (Y/N)?
if /i {%ANSWER%}=={y} (goto :YES)
if /i {%ANSWER%}=={yes} (goto :YES)
if /i {%ANSWER%}=={n} (goto :NO)
if /i {%ANSWER%}=={no} (goto :NO)

:YES
"jre1.6\bin\java.exe" -jar %JAR_NAME% %ADMIN_TOOL% %EXEC_TYPE% %USER_ID% %USER_PASSWORD% %CSV_FILES_DIR% %TARGET_CSV_FILES%
pause
:NO
