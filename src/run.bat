@echo off
title PC Performance Monitor
chcp 65001

echo [1/3] 正在編譯專案 (Maven Build)...
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo [ERROR] 編譯失敗，請檢查代碼或 Maven 設定！
    pause
    exit /b
)

echo.
echo [2/3] 檢查管理員權限...
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo [WARNING] 偵測到非管理員權限！
    echo 抓取 CPU 溫度需要管理員權限。
    echo 請對此檔案按「右鍵 -> 以系統管理員身分執行」。
    pause
    exit /b
)

echo.
echo [3/3] 啟動監控程式 (GPU & CPU)...
java -cp "target/classes;target/lib/*" template.Main

pause