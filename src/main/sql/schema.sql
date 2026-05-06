-- ==========================================
-- 1. 建立使用者資料表 (Users)
-- ==========================================
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,                    -- 使用者唯一 ID
                                     username VARCHAR(50) UNIQUE NOT NULL,      -- 帳號名稱
    password_hash VARCHAR(100) NOT NULL,       -- 密碼雜湊
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

-- ==========================================
-- 2. 建立效能紀錄表 (Performance Records)
-- ==========================================
CREATE TABLE IF NOT EXISTS performance_records (
                                                   id SERIAL PRIMARY KEY,
                                                   monitor_name VARCHAR(100),                 -- 對應 PerformanceRecord.monitorName[cite: 7, 8]
    fps DOUBLE PRECISION,                      -- 對應 PerformanceRecord.avgFps[cite: 7, 8]
    cpu_temp DOUBLE PRECISION,                 -- CPU 溫度[cite: 7, 8]
    gpu_temp DOUBLE PRECISION,                 -- GPU 溫度[cite: 7, 8]
    cpu_usage DOUBLE PRECISION,                -- CPU 使用率[cite: 7, 8]
    gpu_usage DOUBLE PRECISION,                -- GPU 使用率[cite: 7, 8]
    user_id INTEGER NOT NULL,                  -- 外鍵：關聯到 users.id
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                             -- 設定外鍵約束，確保數據完整性
                             CONSTRAINT fk_user
                             FOREIGN KEY(user_id)
    REFERENCES users(id)
                         ON DELETE CASCADE
    );

-- ==========================================
-- 3. 建立索引 (性能優化)
-- ==========================================
-- 優化 findLatestByUserId 的查詢速度
CREATE INDEX IF NOT EXISTS idx_user_latest_record
    ON performance_records (user_id, created_at DESC);

-- ==========================================
-- 4. 初始化測試資料
-- ==========================================
-- 插入兩個測試帳號，供 Main.java 輸入使用[cite: 5]
INSERT INTO users (username, password_hash)
VALUES ('admin', 'admin123'), ('guest', 'guest123')
    ON CONFLICT (username) DO NOTHING;

-- (選填) 插入一筆初始數據供對比測試使用
-- INSERT INTO performance_records (monitor_name, fps, cpu_temp, gpu_temp, cpu_usage, gpu_usage, user_id)
-- VALUES ('Initial_Test', 240.0, 45.5, 60.2, 15.0, 30.0, 1);