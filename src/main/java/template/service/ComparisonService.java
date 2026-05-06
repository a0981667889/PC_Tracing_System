package template.service;

import template.Repository.RecordDao;
import template.model.PerformanceRecord;
import java.util.List;

public class ComparisonService {
    private RecordDao recordRepo = new RecordDao();

    public void compareHardwarePerformance(int userA_id, int userB_id) {
        // 1. 從 DAO 抓取兩個帳號的最新數據（這裡需要你在 DAO 寫個 findLatestByUserId）
        PerformanceRecord dataA = recordRepo.findLatestByUserId(userA_id);
        PerformanceRecord dataB = recordRepo.findLatestByUserId(userB_id);

        if (dataA != null && dataB != null) {
            System.out.println("--- 效能對比報告 ---");
            System.out.printf("帳號 A FPS: %.2f | 帳號 B FPS: %.2f\n", dataA.getAvgFps(), dataB.getAvgFps());

            // 2. Java 邏輯對比
            double fpsDiff = dataA.getAvgFps() - dataB.getAvgFps();
            if (fpsDiff > 0) {
                System.out.println("結果：帳號 A 的設備效能高出 " + fpsDiff + " FPS");
            } else {
                System.out.println("結果：帳號 B 的設備較為優異");
            }

            // 3. 溫度對比（適合測試 RTX 5070 的散熱）
            System.out.printf("GPU 溫差: %.1f°C\n", Math.abs(dataA.getGpuTemp() - dataB.getGpuTemp()));
        }
    }
}
