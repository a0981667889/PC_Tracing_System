import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class HardwareMonitor {
    private SystemInfo si = new SystemInfo();

    public HardwareStats getStats() {
        CentralProcessor processor = si.getHardware().getProcessor();
        // 抓取 CPU 負載 (需要等待 1 秒來計算平均值)
        double cpuLoad = processor.getSystemCpuLoad(1000) * 100;

        // 注意：OSHI 預設沒有強大的 GPU 監控，這裡我們先用假數值或留待後續優化
        double gpuLoad = 0.0;

        return new HardwareStats(cpuLoad, gpuLoad);
    }
}