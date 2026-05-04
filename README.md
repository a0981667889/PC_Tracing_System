HardwareMonitorProject/
├── sql/
│   └── schema.sql         
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── pc/
│   │   │           └── monitor/
│   │   │               ├── Main.java             <-- 啟動進入點
│   │   │               ├── HardwareMonitor.java  <-- OSHI/JSensors 邏輯
│   │   │               ├── RecordRepository.java <-- JDBC/SQL 邏輯
│   │   │               ├── model/
│   │   │               │   ├── PerformanceRecord.java
│   │   │               │   └── HardwareStats.java
│   │   │               └── util/
│   │   │                   └── DBUtil.java       <-- 資料庫連線工具
│   │   └── resources/
│   │       └── application.properties           <-- 放置 DB 帳密 (選配)
├── pom.xml                                      <-- Maven 配置檔
└── README.md                                    <-- 專案說明文件
