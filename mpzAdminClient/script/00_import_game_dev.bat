echo off
rem **********************************************
rem ユーザ設定
rem USER_ID:ログインユーザID
rem USER_PASSWORD:ログインパスワード
rem CSV_FILES_DIR:CSVファイル置き場
rem TARGET_CSV_FILES:対象ファイル名(CSV形式で複数指定 未指定:全て)
rem **********************************************
set USER_ID=tsutomu.kobayashi@mobcast.jp
set USER_PASSWORD=hogehoge
set CSV_FILES_DIR=".\imp_csv"
set TARGET_CSV_FILES=^
G_AREA.csv,^
G_DIVER.csv,^
G_DIVER_EXP_REINFORCE_TABLE.csv,^
G_DIVER_EXP_TABLE.csv,^
G_DIVER_SKILL.csv,^
G_DIVER_SKILL_LEADER.csv,^
G_DIVER_SKILL_RATE_REINFORCE_TABLE.csv,^
G_GODDESS.csv,^
G_RANK_EXP_TABLE.csv,^
G_STAGE.csv,^
G_STAGE_GODDESS_DROP_RATE.csv,^
G_STAGE_REWARD.csv

rem **********************************************
rem 処理切替
rem import:インポートのみ
rem import-clear:インポート+キャッシュ全クリア
rem import-export-clear:インポート+キャッシュ全クリア+更新前後エクスポート
rem export:エクスポート
rem **********************************************
set EXEC_TYPE=import-clear

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

SET /P ANSWER=%MSG%でCSVインポートを実施します。よろしいですか (Y/N)?
if /i {%ANSWER%}=={y} (goto :YES)
if /i {%ANSWER%}=={yes} (goto :YES)
if /i {%ANSWER%}=={n} (goto :NO)
if /i {%ANSWER%}=={no} (goto :NO)

:YES
"jre1.6\bin\java.exe" -jar %JAR_NAME% %ADMIN_TOOL% %EXEC_TYPE% %USER_ID% %USER_PASSWORD% %CSV_FILES_DIR% %TARGET_CSV_FILES%
pause
:NO
