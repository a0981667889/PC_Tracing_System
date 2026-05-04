
import template.model.PerformanceRecord;
import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.components.Gpu;
import template.dao.RecordRepository;
import template.hardware.HardwareMonitor;
import template.model.HardwareStats;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 系統啟動中 ===");

        // 1. 先執行硬體診斷（原本 HardwareTest 的邏輯）
        // 這樣你就能在控制台看到感測器清單，確認驅動與權限是否正常
        runHardwareDiagnostic();

        // 2. 初始化資源
        HardwareMonitor monitor = new HardwareMonitor();
        RecordRepository repo = new RecordRepository();

        System.out.println("\n=== 自動監控系統已啟動 ===");
        System.out.println("監控週期：每 5 秒一次");
        System.out.println("寫入目標：PostgreSQL (benchmark_records)");
        //System.out.println("按下 Ctrl+C 可停止程式\n");

        // 3. 開啟自動化監控迴圈
        while (true) {
            try {
                // A. 透過 JSensors 抓取真實數據
                HardwareStats stats = monitor.getStats();


                // B. 封裝成資料庫物件 (PerformanceRecord)
                // 這裡的 240.0 代表你追求的 240 FPS 目標值
                PerformanceRecord record = new PerformanceRecord(
                        "RealTime_Monitoring",
                        240.0,
                        stats.CpuTemp,
                        stats.GpuTemp,
                        stats.CpuUsage,
                        stats.GpuUsage
                );

                // C. 寫入資料庫
                repo.save(record);

                // D. 控制台即時回報
                System.out.printf("[%tT] 紀錄成功 -> CPU: %.1f°C (%.1f%%) | GPU: %.1f°C (%.1f%%)%n",
                        System.currentTimeMillis(),
                        stats.CpuTemp, stats.CpuUsage,
                        stats.GpuTemp, stats.GpuUsage);

                // E. 休息 5 秒
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                System.out.println("\n[系統通知] 偵測到中斷指令，正在安全關閉...");
                break;
            } catch (Exception e) {
                System.err.println("[錯誤] 讀取或存入資料庫失敗: " + e.getMessage());
                System.out.println("5 秒後重試...");
                try { Thread.sleep(5000); } catch (InterruptedException ex) { break; }
            }
        }
    }

    /**
     * 硬體診斷方法：負責在啟動時印出所有偵測到的感測器資訊
     */
    public static void runHardwareDiagnostic() {
        System.out.println("--- 正在檢測硬體感測器 ---");
        Components components = JSensors.get.components();

        if (components.cpus != null && !components.cpus.isEmpty()) {
            for (Cpu cpu : components.cpus) {
                System.out.println("偵測到 CPU: " + cpu.name);
                cpu.sensors.temperatures.forEach(t -> System.out.println("  [溫度] " + t.name + ": " + t.value + "°C"));
                cpu.sensors.loads.forEach(l -> System.out.println("  [負載] " + l.name + ": " + l.value + "%"));
            }
        } else {
            System.err.println("警告：未偵測到 CPU 感測器！請檢查是否以「管理員權限」執行。");
        }

        if (components.gpus != null && !components.gpus.isEmpty()) {
            for (Gpu gpu : components.gpus) {
                System.out.println("偵測到 GPU: " + gpu.name);
                gpu.sensors.temperatures.forEach(t -> System.out.println("  [溫度] " + t.name + ": " + t.value + "°C"));
            }
        } else {
            System.err.println("警告：未偵測到 GPU 感測器。");
        }
        System.out.println("--- 診斷結束 ---\n");
    }
}