package template;

import template.Repository.RecordDao;
import template.model.PerformanceRecord;
import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.components.Gpu;
import template.hardware.HardwareMonitor;
import template.model.HardwareStats;
import java.util.Scanner; // 引入 Scanner 以便輸入 ID

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 系統啟動中 ===");

        // --- 修正點 1: 處理帳號系統 ---
        Scanner scanner = new Scanner(System.in);
        System.out.print("請輸入您的使用者 ID (例如 1 或 2): ");
        int userId = scanner.nextInt();

        // 1. 執行硬體診斷
        runHardwareDiagnostic();

        // 2. 初始化資源
        HardwareMonitor monitor = new HardwareMonitor();
        RecordDao repo = new RecordDao();

        System.out.println("\n=== 自動監控系統已啟動 ===");
        System.out.println("當前帳號 ID: " + userId);
        System.out.println("監控週期：每 5 秒一次");

        // 3. 開啟自動化監控迴圈
        while (true) {
            try {
                // A. 透過 JSensors 抓取真實數據
                HardwareStats stats = monitor.getStats();

                // B. 封裝成資料庫物件
                // 請確保 PerformanceRecord.java 裡有這個 6 個參數的構造函數
                PerformanceRecord record = new PerformanceRecord(
                        "RealTime_Monitoring",
                        240.0,
                        stats.CpuTemp,
                        stats.GpuTemp,
                        stats.CpuUsage,
                        stats.GpuUsage,
                        stats.monitorName
                );

                // C. 寫入資料庫 (傳入剛才取得的 userId)
                repo.save(record, userId);

                // D. 控制台即時回報
                System.out.printf("[%tT] 紀錄成功 -> CPU: %.1f°C (%.1f%%) | GPU: %.1f°C (%.1f%%)%n",
                        System.currentTimeMillis(),
                        stats.CpuTemp, stats.CpuUsage,
                        stats.GpuTemp, stats.GpuUsage);

                Thread.sleep(5000);

            } catch (InterruptedException e) {
                System.out.println("\n[系統通知] 正在安全關閉...");
                break;
            } catch (Exception e) {
                System.err.println("[錯誤] 失敗: " + e.getMessage());
                try { Thread.sleep(5000); } catch (InterruptedException ex) { break; }
            }
        }
    }

    public static void runHardwareDiagnostic() {
        // ... 原本的診斷邏輯不變 ...
    }
}