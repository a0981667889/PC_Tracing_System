package template.model;

public class PerformanceRecord {

    private String gameName;
    private double avgFps;
    private double cpuTemp;
    private double gpuTemp;
    private double cpuUsage;
    private double gpuUsage;
    private String monitorName;

    public PerformanceRecord() {}

    public PerformanceRecord(String gameName, double avgFps, double cpuTemp, double gpuTemp, double cpuUsage, double gpuUsage, String monitorName) {
        this.gameName = gameName;
        this.avgFps = avgFps;
        this.cpuTemp = cpuTemp;
        this.gpuTemp = gpuTemp;
        this.cpuUsage = cpuUsage;
        this.gpuUsage = gpuUsage;
        this.monitorName = monitorName;
    }

    // Getter & Setter 務必包含 monitorName[cite: 7]
    public String getMonitorName() { return monitorName; }
    public void setMonitorName(String monitorName) { this.monitorName = monitorName; }
    public double getAvgFps() { return avgFps; }
    public double getCpuTemp() { return cpuTemp; }
    public double getGpuTemp() { return gpuTemp; }
    public double getCpuUsage() { return cpuUsage; }
    public double getGpuUsage() { return gpuUsage; }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setAvgFps(double avgFps) {
        this.avgFps = avgFps;
    }

    public void setCpuTemp(double cpuTemp) {
        this.cpuTemp = cpuTemp;
    }

    public void setGpuTemp(double gpuTemp) {
        this.gpuTemp = gpuTemp;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setGpuUsage(double gpuUsage) {
        this.gpuUsage = gpuUsage;
    }

}