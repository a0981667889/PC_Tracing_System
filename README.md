🖥️ PC 效能即時監控系統 (Hardware Performance Monitor)
PC 硬體設計的監控系統，支援多帳號登入與效能數據對比分析。

## 🏗️ 專案架構

```
pc-monitor/
├── src/main/java/template/
│   ├── Main.java                ← 程式入口（處理登入驗證與監控主迴圈）
│   ├── config/
│   │   └── DBUtil.java          ← PostgreSQL JDBC 連線設定
│   ├── hardware/
│   │   └── HardwareMonitor.java ← 核心：OSHI 與 JSensors 混合驅動（已修正遞迴錯誤）
│   ├── model/
│   │   ├── HardwareStats.java   ← 原始硬體感測器數據物件
│   │   └── PerformanceRecord.java ← 整合後的效能紀錄物件（支援 monitorName）
│   ├── Repository/
│   │   └── RecordDao.java       ← 效能紀錄 CRUD 與登入驗證 (支援 user_id)
│   ├── service/
│   │   └── ComparisonService.java ← 帳號數據對比邏輯（分析 FPS 與溫差報告）
│   └── view/
│       └── MonitorView.java     ← 控制台介面輸出與動態更新
├── sql/
│   └── schema.sql               ← 資料庫結構、索引與初始化資料
├── run.bat                      ← Windows 一鍵管理員權限執行
└── pom.xml                      ← Maven 依賴配置
```

## 🚀 如何使用

### 1. 建立資料庫

```bash
-- 1. 建立使用者與紀錄表
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(100) NOT NULL
);

CREATE TABLE performance_records (
    id SERIAL PRIMARY KEY,
    monitor_name VARCHAR(100),
    fps DOUBLE PRECISION,
    cpu_temp DOUBLE PRECISION,
    gpu_temp DOUBLE PRECISION,
    cpu_usage DOUBLE PRECISION,
    gpu_usage DOUBLE PRECISION,
    user_id INTEGER REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

```
-- 2. 插入測試帳號
```
INSERT INTO users (username, password_hash) VALUES ('admin', 'admin123'), ('guest', 'guest123');
);
```



### 2. 編譯 & 執行

(1) 設定連線：請確保 DBUtil.java 中的資料庫密碼正確。
(2) 啟動程式：
```Bash
mvn clean package
java -jar target/pc-monitor-1.0.jar
```
(3) 操作流程：

啟動後，請先進行帳號登入（輸入 username 與 password）。

驗證成功後，系統將執行硬體診斷並開啟每 5 秒一次的自動存檔監控。


### 3. 環境要求
Java: JDK 17+

權限: 必須以 系統管理員身分 執行，否則感測器讀數可能為 0。

資料庫: PostgreSQL 13+。

## 📐 架構說明

```
Hardware(PC) → HardwareMonitor(OSHI/JSensors) → RecordRepository(JDBC) → PostgreSQL
                      ↑ 讀取感測器                ↑ 數據封裝              ↑ SQL 寫入
```

### 各層職責

| 層 | 職責 | 可以做 | 不能做 |
|----|------|--------|--------|
| **Hardware** | 獲取硬體底層數據 |OSHI (CPU 使用率/溫度), JSensors (GPU 數據) |
| **DAO** | 資料持久化與關聯查詢 |帳號登入驗證、根據 user_id 儲存紀錄與查詢最新數據 |
| **Model** | 資料載體 | POJO (儲存溫度、負載、FPS) |
| **Service** | 業務邏輯處理 | 分析不同帳號間的 FPS 差異與硬體溫差報告|
| **View** | 監控介面 | 提供功能選單與控制台動態格式化輸出 |

## 📊 類別圖（Mermaid）

```mermaid
classDiagram
    class RecordDao {
        +login(String, String) int
        +save(PerformanceRecord, int) void
        +findLatestByUserId(int) PerformanceRecord
    }
    class HardwareMonitor {
        +getStats() HardwareStats
    }
    class ComparisonService {
        +compareHardwarePerformance(int, int) void
    }
    class Main {
        +main(String[] args)
    }

    Main --> RecordDao : 登入與存檔
    Main --> HardwareMonitor : 獲取數據
    ComparisonService --> RecordDao : 讀取數據對比
```

## 📊 ERD（Mermaid）

```mermaid
erDiagram
    USERS ||--o{ PERFORMANCE_RECORDS : "擁有"
    USERS {
        int id PK
        string username
        string password_hash
    }
    PERFORMANCE_RECORDS {
        int id PK
        string monitor_name
        double fps
        double cpu_temp
        double gpu_temp
        int user_id FK
        timestamp created_at
    }
```

---

