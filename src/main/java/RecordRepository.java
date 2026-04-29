
import com.pc.monitor.model.PerformanceRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class RecordRepository {

    public void save(PerformanceRecord record) {
        // SQL 的填空語法，用 ? 代表未來要填入的值
        String sql = "INSERT INTO benchmark_records (game_name, avg_fps, cpu_temp, gpu_temp, cpu_usage, gpu_usage) VALUES (?, ?, ?, ?, ?, ?)";

        // try-with-resources：這會自動幫你關閉連線，防止資源洩漏 (這點很重要！)
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 將物件裡的資料填入對應的 ? 位置 (順序從 1 開始)
            pstmt.setString(1, record.getGameName());
            pstmt.setDouble(2, record.getAvgFps());
            pstmt.setDouble(3, record.getCpuTemp());
            pstmt.setDouble(4, record.getGpuTemp());
            pstmt.setDouble(5, record.getCpuUsage());
            pstmt.setDouble(6, record.getGpuUsage());

            // 執行 SQL
            pstmt.executeUpdate();
            System.out.println("成功！資料已寫入資料庫。");

        } catch (SQLException e) {
            // 如果連線失敗，這裡會顯示原因 (例如密碼打錯、資料庫沒開)
            System.err.println("資料庫寫入失敗，錯誤訊息如下：");
            e.printStackTrace();
        }
    }
    public List<PerformanceRecord> findAll() {
        List<PerformanceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM benchmark_records";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // rs.next() 會一行一行往下讀
            while (rs.next()) {
                // 從資料庫的一行資料中，取出每個欄位的值
                String name = rs.getString("game_name");
                double fps = rs.getDouble("avg_fps");
                double cpuT = rs.getDouble("cpu_temp");
                double gpuT = rs.getDouble("gpu_temp");
                double cpuU = rs.getDouble("cpu_usage");
                double gpuU = rs.getDouble("gpu_usage");

                // 把這行資料封裝成物件
                PerformanceRecord record = new PerformanceRecord(name, fps, cpuT, gpuT, cpuU, gpuU);
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
