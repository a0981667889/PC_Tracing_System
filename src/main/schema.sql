-- =============================================================================
-- Project: Hardware Performance Monitor
-- Description: Schema for storing CPU/GPU real-time telemetry data.
-- Author:foreverice
-- Date: 2026-05-04
-- =============================================================================

-- 1. 建立資料表 (Performance Records)
-- 此表用於儲存從 Java 程式 (OSHI/JSensors) 抓取的硬體數據
CREATE TABLE IF NOT EXISTS performance_records (
                                                   id              SERIAL PRIMARY KEY,                -- 自動遞增主鍵
                                                   monitor_name    VARCHAR(100) NOT NULL,             -- 監控名稱 (例如: PUBG_Session)

-- 效能數據 (使用 DOUBLE PRECISION 確保精確度)
    fps             DOUBLE PRECISION DEFAULT 0.0,      -- 幀率 (目標 240 FPS)
    cpu_temp        DOUBLE PRECISION DEFAULT 0.0,      -- CPU 溫度 (°C)
    gpu_temp        DOUBLE PRECISION DEFAULT 0.0,      -- GPU 溫度 (°C)
    cpu_usage       DOUBLE PRECISION DEFAULT 0.0,      -- CPU 使用率 (%)
    gpu_usage       DOUBLE PRECISION DEFAULT 0.0,      -- GPU 使用率 (%)

-- 時間戳記 (自動產生)
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                                  );

-- 2. 建立索引 (Indexes)
-- 針對頻繁查詢的欄位建立索引，提升在大數據量下的查詢效能
CREATE INDEX idx_performance_monitor_name ON performance_records (monitor_name);
CREATE INDEX idx_performance_created_at ON performance_records (created_at DESC);

-- 3. 註解 (Comments)
-- 為資料表與欄位添加描述，方便其他開發者理解
COMMENT ON TABLE performance_records IS '儲存 CPU、GPU 與 FPS 的即時監控數據';
COMMENT ON COLUMN performance_records.monitor_name IS '自定義的監控標籤或遊戲名稱';
COMMENT ON COLUMN performance_records.cpu_temp IS '由 OSHI 抓取的處理器封裝溫度';