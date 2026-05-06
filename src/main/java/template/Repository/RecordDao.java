package template.Repository;

import template.config.DBUtil;
import template.model.PerformanceRecord;
import java.sql.*;

public class RecordDao {

    /**
     * 1. 登入驗證
     * 比對帳號與密碼，成功則回傳該使用者的 ID，失敗回傳 -1
     */
    public int login(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password_hash = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB 錯誤] 登入驗證時發生異常: " + e.getMessage());
        }
        return -1;
    }

    /**
     * 2. 儲存監控數據
     * 將即時抓取的 CPU/GPU 數據存入資料庫並關聯至特定使用者[cite: 8]
     */
    public void save(PerformanceRecord record, int userId) {
        String sql = "INSERT INTO performance_records (monitor_name, fps, cpu_temp, gpu_temp, cpu_usage, gpu_usage, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, record.getMonitorName());
            pstmt.setDouble(2, record.getAvgFps());
            pstmt.setDouble(3, record.getCpuTemp());
            pstmt.setDouble(4, record.getGpuTemp());
            pstmt.setDouble(5, record.getCpuUsage());
            pstmt.setDouble(6, record.getGpuUsage());
            pstmt.setInt(7, userId);

            pstmt.executeUpdate();
            System.out.println("[DB] 數據已存檔 (User ID: " + userId + ")");

        } catch (SQLException e) {
            System.err.println("[DB 錯誤] 資料寫入失敗: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 3. 查詢最新紀錄
     * 用於 ComparisonService 進行帳號間的效能對比
     */
    public PerformanceRecord findLatestByUserId(int userId) {
        String sql = "SELECT * FROM performance_records WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    PerformanceRecord record = new PerformanceRecord();
                    // 從 ResultSet 提取資料並封裝進物件
                    record.setMonitorName(rs.getString("monitor_name"));
                    record.setAvgFps(rs.getDouble("fps"));
                    record.setCpuTemp(rs.getDouble("cpu_temp"));
                    record.setGpuTemp(rs.getDouble("gpu_temp"));
                    record.setCpuUsage(rs.getDouble("cpu_usage"));
                    record.setGpuUsage(rs.getDouble("gpu_usage"));
                    return record;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB 錯誤] 讀取最新數據失敗: " + e.getMessage());
        }
        return null;
    }
}