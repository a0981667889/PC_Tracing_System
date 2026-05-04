package template.dao;

import template.config.DBUtil;
import template.model.PerformanceRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordRepository {

    public void save(PerformanceRecord record) {
        // 修正：將資料表名稱改為你剛建立的 performance_records
        // 並將 game_name/avg_fps 改為更通用的 monitor_name/fps
        String sql = "INSERT INTO performance_records (monitor_name, fps, cpu_temp, gpu_temp, cpu_usage, gpu_usage) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 對應你在 Main 傳入的 "RealTime_Monitoring" 或目標 FPS
            pstmt.setString(1, record.getGameName()); // 雖然方法名是 getGameName，但存入 monitor_name 欄位
            pstmt.setDouble(2, record.getAvgFps());
            pstmt.setDouble(3, record.getCpuTemp());
            pstmt.setDouble(4, record.getGpuTemp());
            pstmt.setDouble(5, record.getCpuUsage());
            pstmt.setDouble(6, record.getGpuUsage());

            pstmt.executeUpdate();
            // 縮減訊息，避免無窮迴圈時洗屏
            System.out.println("[DB] 數據已存檔");

        } catch (SQLException e) {
            System.err.println("資料庫寫入失敗！");
            e.printStackTrace();
        }
    }

    public List<PerformanceRecord> findAll() {
        List<PerformanceRecord> list = new ArrayList<>();
        // 修正：查詢新的資料表
        String sql = "SELECT * FROM performance_records ORDER BY created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // 注意：這裡的欄位名稱要跟 SQL Schema 一致
                PerformanceRecord record = new PerformanceRecord(
                        rs.getString("monitor_name"),
                        rs.getDouble("fps"),
                        rs.getDouble("cpu_temp"),
                        rs.getDouble("gpu_temp"),
                        rs.getDouble("cpu_usage"),
                        rs.getDouble("gpu_usage")
                );
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}