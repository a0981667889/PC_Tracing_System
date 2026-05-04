package template.view;

import template.model.PerformanceRecord;
import java.util.Scanner;

public class MonitorView {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * 顯示歡迎標題與硬體資訊
     */
    public void showWelcome(String cpuName, String gpuName) {
        System.out.println("==========================================");
        System.out.println("    🚀 PC Performance Monitor System      ");
        System.out.println("==========================================");
        System.out.println("檢測到硬體: ");
        System.out.println("CPU: " + cpuName);
        System.out.println("GPU: " + gpuName);
        System.out.println("==========================================");
    }

    /**
     * 以格式化方式印出即時數據
     */
    public void renderRealTimeStats(PerformanceRecord record) {
        System.out.printf("\r[監控中] FPS: %-5.1f | CPU: %5.1f°C (%5.1f%%) | GPU: %5.1f°C (%5.1f%%)",
                record.getAvgFps(),
                record.getCpuTemp(),
                record.getCpuUsage(),
                record.getGpuTemp(),
                record.getGpuUsage());
        // 使用 \r 可以讓游標回到行首，達成動態更新數值的效果
    }

    /**
     * 顯示功能選單
     */
    public int showMenu() {
        System.out.println("\n\n--- 功能選單 ---");
        System.out.println("1. 開始即時監控並存檔");
        System.out.println("2. 查看歷史監控紀錄");
        System.out.println("3. 執行瓶頸分析 (Analysis)");
        System.out.println("0. 退出系統");
        System.out.print("請選擇操作: ");

        while (!scanner.hasNextInt()) {
            System.out.print("請輸入數字！: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public void showMessage(String msg) {
        System.out.println("[系統訊息] " + msg);
    }
}