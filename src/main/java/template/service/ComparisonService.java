package template.service;

import template.Repository.RecordDao;
import template.model.PerformanceRecord;

public class ComparisonService {
    private RecordDao recordRepo = new RecordDao();

    /**
     * 對比兩個不同使用者的硬體效能
     * @param userA_id 第一個使用者的 ID
     * @param userB_id 第二個使用者的 ID
     */
    public void compareHardwarePerformance(int userA_id, int userB_id) {
        // 1. 從 DAO 抓取兩個帳號最新的監控數據
        PerformanceRecord dataA = recordRepo.findLatestByUserId(userA_id);
        PerformanceRecord dataB = recordRepo.findLatestByUserId(userB_id);

        System.out.println("\n========== 跨帳號效能對比報告 ==========");

        if (dataA == null || dataB == null) {
            System.out.println("[錯誤] 無法取得其中一方的數據，請確保兩個帳號都有監控紀錄。");
            return;
        }

        // 2. 顯示基本資訊與硬體名稱
        System.out.printf("帳號 A (ID: %d) 設備: %s\n", userA_id, dataA.getMonitorName());
        System.out.printf("帳號 B (ID: %d) 設備: %s\n", userB_id, dataB.getMonitorName());
        System.out.println("---------------------------------------");

        // 3. FPS 效能對比[cite: 1]
        double fpsA = dataA.getAvgFps();
        double fpsB = dataB.getAvgFps();
        System.out.printf("帳號 A FPS: %.2f | 帳號 B FPS: %.2f\n", fpsA, fpsB);

        double fpsDiff = fpsA - fpsB;
        if (Math.abs(fpsDiff) < 1.0) {
            System.out.println("結果：兩者效能旗鼓相當。");
        } else if (fpsDiff > 0) {
            System.out.printf("結果：帳號 A 領先 %.2f FPS\n", fpsDiff);
        } else {
            System.out.printf("結果：帳號 B 領先 %.2f FPS\n", Math.abs(fpsDiff));
        }

        // 4. 散熱與負載對比 (適合測試 14700KF 與 5070 的溫控)[cite: 1]
        System.out.println("---------------------------------------");
        System.out.printf("CPU 溫差: %.1f°C (A: %.1f | B: %.1f)\n",
                Math.abs(dataA.getCpuTemp() - dataB.getCpuTemp()), dataA.getCpuTemp(), dataB.getCpuTemp());
        System.out.printf("GPU 溫差: %.1f°C (A: %.1f | B: %.1f)\n",
                Math.abs(dataA.getGpuTemp() - dataB.getGpuTemp()), dataA.getGpuTemp(), dataB.getGpuTemp());
        System.out.println("=======================================\n");
    }
}