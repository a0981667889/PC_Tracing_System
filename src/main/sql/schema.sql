-- ==========================================
-- 1. 建立使用者資料表 (Users)
-- ==========================================
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,                    -- 使用者唯一 ID
                                     username VARCHAR(50) UNIQUE NOT NULL,      -- 帳號名稱 (不可重複)
    password_hash VARCHAR(100) NOT NULL,       -- 密碼雜湊值
    role VARCHAR(20) DEFAULT 'USER',           -- 角色分級
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

-- ==========================================
-- 2. 建立效能紀錄表 (Performance Records)
-- ==========================================
CREATE TABLE IF NOT EXISTS performance_records (
                                                   id SERIAL PRIMARY KEY,
                                                   monitor_name VARCHAR(100),                 -- 硬體名稱 (例如: NVIDIA GeForce RTX 5070)
    fps DOUBLE PRECISION,                      -- 遊戲/系統平均幀數
    cpu_temp DOUBLE PRECISION,                 -- CPU 溫度 (℃)
    gpu_temp DOUBLE PRECISION,                 -- GPU 溫度 (℃)
    cpu_usage DOUBLE PRECISION,                -- CPU 使用率 (%)
    gpu_usage DOUBLE PRECISION,                -- GPU 使用率 (%)
    user_id INTEGER NOT NULL,                  -- 外鍵：關聯至 users.id
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                             -- 設定外鍵約束，確保數據完整性並支援連動刪除
                             CONSTRAINT fk_user
                             FOREIGN KEY(user_id)
    REFERENCES users(id)
                         ON DELETE CASCADE
    );

-- ==========================================
-- 3. 建立索引 (性能優化)
-- ==========================================
-- 當你呼叫 findLatestByUserId 時，此索引可大幅縮短掃描時間
CREATE INDEX IF NOT EXISTS idx_user_records_latest
    ON performance_records (user_id, created_at DESC);

-- ==========================================
-- 4. 初始化預設資料 (供測試使用)
-- ==========================================
-- 插入兩個測試帳號，對應 Main 登入流程
INSERT INTO users (username, password_hash)
VALUES
    ('admin', 'admin123'),
    ('guest', 'guest123')
    ON CONFLICT (username) DO NOTHING;

-- 預插一筆歷史數據，方便執行 ComparisonService 對比測試
INSERT INTO performance_records (monitor_name, fps, cpu_temp, gpu_temp, cpu_usage, gpu_usage, user_id)
VALUES ('GPU-Reference', 240.0, 45.0, 62.0, 15.5, 30.2, 1);