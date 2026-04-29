package com.pc.monitor.model;

public class PerformanceRecord {
    // 使用 private 隱私化欄位，這是良好的開發習慣
    private int id;
    private String gameName;
    private double avgFps;
    private double cpuTemp;
    private double gpuTemp;
    private double cpuUsage;
    private double gpuUsage;

    // 建構子 (Constructor)
    public PerformanceRecord(String gameName, double avgFps, double cpuTemp, double gpuTemp, double cpuUsage, double gpuUsage) {
        this.gameName = gameName;
        this.avgFps = avgFps;
        this.cpuTemp = cpuTemp;
        this.gpuTemp = gpuTemp;
        this.cpuUsage = cpuUsage;
        this.gpuUsage = gpuUsage;
    }

    // --- Getter 和 Setter 開始 ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public double getAvgFps() {
        return avgFps;
    }

    public void setAvgFps(double avgFps) {
        // 你可以在這裡加入邏輯，例如：FPS 不可能小於 0
        if (avgFps >= 0) {
            this.avgFps = avgFps;
        }
    }

    public double getCpuTemp() {
        return cpuTemp;
    }

    public void setCpuTemp(double cpuTemp) {
        this.cpuTemp = cpuTemp;
    }

    public double getGpuTemp() {
        return gpuTemp;
    }

    public void setGpuTemp(double gpuTemp) {
        this.gpuTemp = gpuTemp;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        // 防呆邏輯：占用率最高就是 100%
        this.cpuUsage = Math.min(cpuUsage, 100.0);
    }

    public double getGpuUsage() {
        return gpuUsage;
    }

    public void setGpuUsage(double gpuUsage) {
        this.gpuUsage = Math.min(gpuUsage, 100.0);
    }
}