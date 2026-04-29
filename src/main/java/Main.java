import com.pc.monitor.model.PerformanceRecord;

public class Main {
    public static void main(String[] args) {
        // 1. 初始化資源
        HardwareMonitor monitor = new HardwareMonitor();
        RecordRepository repo = new RecordRepository();

        System.out.println("=== 自動監控系統已啟動 ===");
        System.out.println("程式將每 5 秒自動記錄一次 CPU 使用率至資料庫");
        System.out.println("若要停止監控，請按下 Ctrl + C");

        // 2. 開啟無窮迴圈
        while (true) {
            try {
                // A. 抓取數據
                HardwareStats stats = monitor.getStats();

                // B. 封裝物件 (這裡暫時手動填入 FPS，未來可考慮進階方式)
                PerformanceRecord record = new PerformanceRecord(
                        "RealTime_Monitoring",
                        60.0,  // 假設 FPS
                        45.0,  // 假設溫度
                        50.0,  // 假設溫度
                        stats.CpuUsage,
                        stats.GpuUsage
                );

                // C. 寫入資料庫
                repo.save(record);

                System.out.printf("[%tT] 監控中... CPU 使用率: %.2f%%%n", System.currentTimeMillis(), stats.CpuUsage);

                // D. 暫停 5 秒 (5000 毫秒)，避免佔用過多系統資源
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                System.err.println("監控程式被中斷了！");
                break; // 發生中斷就跳出迴圈
            } catch (Exception e) {
                System.err.println("讀取或寫入發生錯誤，系統將在 5 秒後重試...");
                e.printStackTrace();
                // 發生錯誤不停止程式，繼續下一輪
            }
        }
    }
}