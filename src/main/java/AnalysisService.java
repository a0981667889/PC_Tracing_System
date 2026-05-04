import template.model.PerformanceRecord;
public class AnalysisService {
    public void checkBottleneck(template.model.PerformanceRecord record) {
        System.out.println("\n--- 自動效能分析報告 ---");

        if (record.getGpuUsage() < 80 && record.getCpuUsage() > 90) {
            System.out.println("警報：偵測到 CPU 瓶頸！你的 CPU 佔用過高，導致顯卡無法全力運作。");
            System.out.println("建議：嘗試優化 CPU 時脈或檢查背景程式。");
        } else if (record.getGpuTemp() > 85) {
            System.out.println("警告：顯卡溫度過高 (Thermal Throttling)！");
            System.out.println("建議：檢查機殼風道或調整風扇曲線。");
        } else {
            System.out.println("系統狀態：良好。硬體配置目前平衡。");
        }
    }
}