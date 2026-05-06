package template.Repository;

import template.config.DBUtil;
import template.model.PerformanceRecord;
import java.sql.*;

public class RecordDao {

    public void save(PerformanceRecord record, int userId) {
        // 修正點 1: SQL 欄位補上 user_id，確保與 7 個 ? 對應
        String sql = "INSERT INTO performance_records (monitor_name, fps, cpu_temp, gpu_temp, cpu_usage, gpu_usage, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 修正點 2: 建議確認 get方法名稱。若已改名為 setMonitorName，這裡應改為 getMonitorName()
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
            System.err.println("資料庫寫入失敗！原因：" + e.getMessage());
            e.printStackTrace();
        }
    }

    public PerformanceRecord findLatestByUserId(int userId) {
        String sql = "SELECT * FROM performance_records WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    PerformanceRecord record = new PerformanceRecord();
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
            e.printStackTrace();
        }
        return null;
    }
}