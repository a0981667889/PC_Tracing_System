package template;

import template.Repository.RecordDao;
import template.model.PerformanceRecord;
import template.hardware.HardwareMonitor;
import template.model.HardwareStats;
import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.components.Gpu;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RecordDao repo = new RecordDao();
        int userId = -1;

        System.out.println("=== PC 效能監控系統登入 ===");

        // 1. 登入驗證迴圈[cite: 5]
        while (userId == -1) {
            System.out.print("帳號: ");
            String user = scanner.next();
            System.out.print("密碼: ");
            String pass = scanner.next();

            userId = repo.login(user, pass);

            if (userId == -1) {
                System.out.println("[錯誤] 帳號或密碼不正確，請重新輸入。");
            }
        }

        System.out.println("\n[系統] 登入成功！使用者 ID: " + userId);
        runHardwareDiagnostic();

        // 2. 初始化監控資源
        HardwareMonitor monitor = new HardwareMonitor();
        System.out.println("=== 自動監控已啟動 (每 5 秒存檔一次) ===");

        // 3. 監控主迴圈[cite: 5]
        while (true) {
            try {
                HardwareStats stats = monitor.getStats();

                PerformanceRecord record = new PerformanceRecord(
                        "RealTime_Monitoring",
                        240.0,
                        stats.CpuTemp,
                        stats.GpuTemp,
                        stats.CpuUsage,
                        stats.GpuUsage,
                        stats.monitorName
                );

                repo.save(record, userId);

                System.out.printf("[%tT] CPU: %.1f°C | GPU: %.1f°C (存檔成功)%n",
                        System.currentTimeMillis(), stats.CpuTemp, stats.GpuTemp);

                Thread.sleep(5000);
            } catch (Exception e) {
                System.err.println("監控異常: " + e.getMessage());
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }
    }

    public static void runHardwareDiagnostic() {
        System.out.println("--- 正在檢測硬體感測器 ---");
        Components components = JSensors.get.components();
        if (components.cpus != null && !components.cpus.isEmpty()) {
            System.out.println("偵測到 CPU: " + components.cpus.get(0).name);
        }
        if (components.gpus != null && !components.gpus.isEmpty()) {
            System.out.println("偵測到 GPU: " + components.gpus.get(0).name);
        }
        System.out.println("--- 診斷結束 ---\n");
    }
}