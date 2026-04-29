public class HardwareStats {
    public double CpuUsage;
    public double GpuUsage; // OSHI 較難直接取得 GPU 負載，通常這需要額外的 WMI 查詢或第三方軟體 API

    public HardwareStats(double cpuUsage, double gpuUsage) {
        this.CpuUsage = cpuUsage;
        this.GpuUsage = gpuUsage;
    }
}