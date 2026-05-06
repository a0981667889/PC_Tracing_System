package template.model;

public class HardwareStats {
    public double CpuUsage;
    public double GpuUsage;
    public double CpuTemp;  // 新增
    public double GpuTemp;
    public String monitorName;// 新增

    // 關鍵在於這裡：建構子必須接收 5 個參數
    public HardwareStats(double cpuUsage, double gpuUsage, double cpuTemp, double gpuTemp,String monitorName) {
        this.CpuUsage = cpuUsage;
        this.GpuUsage = gpuUsage;
        this.CpuTemp = cpuTemp;
        this.GpuTemp = gpuTemp;
        this.monitorName = monitorName;
    }
}